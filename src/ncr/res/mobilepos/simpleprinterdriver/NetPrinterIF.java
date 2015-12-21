package ncr.res.mobilepos.simpleprinterdriver;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.20      FENGSHA       80mmの紙変更を対応
 * 1.02            2015.02.06      FENGSHA       Printer時間の設定変更を対応
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * NetPrinterIF class specifies net printer information.
 */
public class NetPrinterIF implements IPrinter {

    /**
     * For Batch Command(connect to TCP PORT).
     */
    private Socket socket = null;
    /**
     * For Real Time Command(connect to UDP PORT).
     */
    private DatagramSocket dgSocket = null;
    /**
     * Stream for receive MSG from TCP PORT.
     */
    private BufferedInputStream inSkt = null;
    /**
     * Stream for send MSG to TCP PORT.
     */
    private BufferedOutputStream outSkt = null;
    /**
     * Packet for receive MSG from UDP PORT.
     */
    private DatagramPacket recvDgSkt = null;
    /**
     * Packet for send MSG to UDP PORT.
     */
    private DatagramPacket sendDgSkt = null;

    /**
     * Net Socket Address for UDP.
     */
    private InetSocketAddress addressUDP = null;
    /**
     * Net Socket Address for TCP.
     */
    private InetSocketAddress addressTCP = null;

    /**
     * Print Data.
     */
    private List<byte[]> printData = new ArrayList<byte[]>();

    // * @param _url IP address of Printer
    // * @param tcpport TCP Protocol PORT
    // * @param udpport UDP Protocol PORT
    //
    /**
     * Constructor Print Service for 7197 printer without driver.
     *
     * @param info Net printer information
     */
    public NetPrinterIF(final NetPrinterInfo info) {

        addressUDP = new InetSocketAddress(info.getUrl(), info.getPortUDP());
        addressTCP = new InetSocketAddress(info.getUrl(), info.getPortTCP());
    }

    /**
     * Connect to TCP Port the printer using Socket for batch command.
     *
     * @return Boolean result if the printer is connected
     */
    public final boolean connectPrinter() {

        socket = new Socket();
        try {
            //1.02 2015.02.06 FENGSHA MOD START
            //socket.connect(addressTCP, 1000);
              socket.connect(addressTCP, CONNECT_TIME_OUT);
            //1.02 2015.02.06 FENGSHA MOD END
        } catch (IOException e) {
            return false;
        }

        // if not connected
        if (!socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
            socket = null;
            return false;
        }

        try {
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(RECV_TCP_TIMEOUT);

            inSkt = new BufferedInputStream(socket.getInputStream());
            outSkt = new BufferedOutputStream(socket.getOutputStream());
        } catch (SocketException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Initialize printer.
     *
     * @return Boolean result if printer is initialized.
     */
    public final boolean initPrinter() {

        if (socket == null) {
            return false;
        }
        try {
            //1.01 2015.01.20 FENGSHA ADD START
            String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                printPaperLength = (String) env.lookup("Printpaperlength");
            } catch (NamingException e) {
                printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            }
            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                outSkt.write(INIT_CMD);
            }else{
                outSkt.write(INIT_CMDFOR80);
            }
            //outSkt.write(INIT_CMD);
          //1.01 2015.01.20 FENGSHA ADD END
            outSkt.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Send Real Time Command to UDP. Recover and Clear Buffers.
     *
     * @return Results to true if buffer is cleared, otherwise false.
     */
    public final boolean clearBuffer() {

        byte[] seq = {0x00, 0x00, 0x00, 0x01};
        byte[] cmd = byteArrayAppend(seq, CLEAR_BUFFER_CMD);
        try {
            dgSocket = new DatagramSocket();

            sendDgSkt = new DatagramPacket(cmd, cmd.length, addressUDP);
            dgSocket.send(sendDgSkt);
            dgSocket.close();
            dgSocket = null;
            sendDgSkt = null;
            printData = new ArrayList<byte[]>();
        } catch (SocketException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Check the status of Printer result.
     *
     * @return Printer check result
     */
    private PrinterCheckResult checkPrinter() {

        PrinterCheckResult result = new PrinterCheckResult();

        // check the socket connecting to the printer is useful
        if (socket == null) {
            result.setFlgTimeout(PRINTER_ERROR);
            return result;
        }

        // RS232C Busy Status
        byte[] seq1 = {0x00, 0x00, 0x00, 0x02};
        byte[] cmd1 = byteArrayAppend(seq1, GET_BUSY_STATUS_UDP_CMD);
        // Error status
        byte[] seq2 = {0x00, 0x00, 0x00, 0x03};
        byte[] cmd2 = byteArrayAppend(seq2, GET_ERROR_STATUS_UDP_CMD);
        // Receipt paper status
        byte[] seq3 = {0x00, 0x00, 0x00, 0x04};
        byte[] cmd3 = byteArrayAppend(seq3, GET_PAPER_STATUE_UDP_CMD);

        boolean flgTimeoutTCP = false; // TCP connect if timeout
        boolean flgStatusTCP = true; // Transmit Status is OK or Error
        boolean flgTimeoutUDP = false; // TCP connect if timeout
        boolean flgStatusUDP1 = true; // Transmit Busy Status is OK or Error
        boolean flgStatusUDP2 = true; // Transmit Error Status is OK or Error

        // Transmit Receipt Paper Status is OK or Error
        boolean flgStatusUDP3 = true;

        for (int i = 0; i < CON_MAX_TIMES; i++) {
            byte[] statusTCP = new byte[1];
            byte[] statusUDP = new byte[5];
            // Transmit Status to TCP
            flgTimeoutTCP = transmitStatusTCP(GET_STATUS_TCP_CMD, statusTCP);
            // If get status from printer
            if (!flgTimeoutTCP) {
                if (statusTCP[0] == (byte) 0x00) {
                    flgStatusTCP = true;
                } else {
                    flgStatusTCP = false;
                }
                // If status has any errors
                if (!flgStatusTCP) {
                    setCheckResult(result, flgTimeoutTCP, flgStatusTCP,
                            statusTCP[0], getErrorNumTCP(statusTCP[0]));
                    this.clearBuffer();
                    return result;
                }
            }

            // Transmit RS232C Busy Status
            flgTimeoutUDP = transmitStatusUDP(cmd1, statusUDP);
            if (!flgTimeoutUDP) {
                if (statusUDP[4] == (byte) 0x12) {
                    flgStatusUDP1 = true;
                } else {
                    flgStatusUDP1 = false;
                }

                if (!flgStatusUDP1) {
                    setCheckResult(result, flgTimeoutUDP, flgStatusUDP1,
                            statusUDP[4], getBusyStsErrorNumUDP(statusUDP[4]));
                    this.clearBuffer();
                    return result;
                }
            } else {
                result.setFlgTimeout(flgTimeoutUDP);
                this.clearBuffer();
                return result;
            }
            statusUDP = new byte[5];

            // Transmit Error Status
            flgTimeoutUDP = transmitStatusUDP(cmd2, statusUDP);
            if (!flgTimeoutUDP) {
                if (statusUDP[4] == (byte) 0x12) {
                    flgStatusUDP2 = true;
                } else {
                    flgStatusUDP2 = false;
                }

                if (!flgStatusUDP2) {
                    setCheckResult(result, flgTimeoutUDP, flgStatusUDP2,
                            statusUDP[4], getErrorStsErrorNumUDP(statusUDP[4]));
                    this.clearBuffer();
                    return result;
                }
            } else {
                result.setFlgTimeout(flgTimeoutUDP);
                this.clearBuffer();
                return result;
            }
            statusUDP = new byte[5];

            // Transmit Receipt Paper Status
            flgTimeoutUDP = transmitStatusUDP(cmd3, statusUDP);
            if (!flgTimeoutUDP) {
                int errNum = getRcptPaperErrorNumUDP(statusUDP[4]);
                if (errNum != 0) {
                    flgStatusUDP3 = false;
                    setCheckResult(result, flgTimeoutUDP, flgStatusUDP3,
                            statusUDP[4], errNum);
                    this.clearBuffer();
                    return result;
                } else {
                    flgStatusUDP3 = true;
                }
            }

            if (!flgTimeoutTCP && flgStatusTCP && flgStatusUDP1
                    && flgStatusUDP2 && flgStatusUDP3) {
                setCheckResult(result, false, true, (byte) 0x00, 0);
                return result;
            }
        }
        result.setFlgTimeout(true);

        return result;
    }

    /**
     * Add bitmap.
     *
     * @param data binary data of bitmap file
     * @param flag the type of bitmap 1:monochrome 2:16color 3:256color 4:24bits
     *            color
     * @param paperFlg flag of paper 1:53mm 2:80mm
     * @return Boolean result if bitmap is added.
     */
    public final boolean writeBmp(final byte[] data, final int flag,
            final int paperFlg) {

        // Change bitmap for printer
        byte[] buffer = getBmp(data, flag, paperFlg);
        if (buffer == null) {
            return false;
        }

        printData.add(buffer);

        return true;
    }

    /**
     * Change windows bitmap to the format for printer.
     *
     * @param data Bitmap data for printer
     * @param flag Flag for windows bitmap
     * @param paperFlg Paper flag
     * @return Bitmap data for printer
     */
    private byte[] getBmp(final byte[] data, final int flag, final int paperFlg) {

        if (data == null || data.length < 0x0d) {
            return new byte[0];
        }
        //1.01 2015.01.20 FENGSHA MOD START
        //BmpInfo bmpInfo = new BmpInfo(data, FLG_PAPER53);
         BmpInfo bmpInfo = new BmpInfo(data, paperFlg);
        //1.01 2015.01.20 FENGSHA MOD END
        // check if bitmap
        if (!bmpInfo.checkBitmap()) {
            return new byte[0];
        }

        if (!bmpInfo.init()) {
            return new byte[0];
        }
        byte[] buffer = null;

        switch (flag) {
            case 1:
                // get print data
                buffer = bmpInfo.getMonoBmpdataForPrinter();
                break;
            case 2:
                buffer = bmpInfo.get16ClrBmpdataForPrinter();
                break;
            case 3:
                buffer = bmpInfo.get256ClrBmpdataForPrinter();
                break;
            case 4:
                buffer = bmpInfo.get24bitsClrBmpdataForPrinter();
                break;
            case 90:
                buffer = bmpInfo.get90DegreeBmpdataForPrinter();
                break;
            default:
                break;
        }

        if (buffer == null) {
            return new byte[0];
        }
        return buffer;
    }

    /**
     * Add text.
     *
     * @param data Data to be added in the text.
     */
    public final void writeText(final byte[] data) {

        if (data != null && data.length != 0) {
            printData.add(data);
        }
        return;
    }

    /**
     * Add text.
     *
     * @param data Data to be added in the text.
     * @param index
     */
    public final void writeText(final byte[] data, int index) {

        if (data != null && data.length != 0) {
            printData.add(index, data);
        }
    }

    /**
     * Send print data to printer.
     *
     * @return Result after printer check result
     */
    synchronized public final int print() {

        int result = 0;
        PrinterCheckResult checkResult = new PrinterCheckResult();

        // Check printer status
        checkResult = checkPrinter();
        if (checkResult.isFlgTimeout()) {
            result = ERROR_NUM_CONNECT;
            return result;
        } else {
            if (!checkResult.isFlgPrinterStatus()) {
                result = checkResult.getErrorNo();
                return result;
            }
        }

        try {
            for (byte[] data : printData) {
                outSkt.write(data);
                outSkt.flush();
            }
        } catch (IOException e) {
            result = ERROR_NUM_CONNECT;
            return result;
        }

        // Check printer status after send print data
        checkResult = checkPrinter();
        if (checkResult.isFlgTimeout()) {
            result = ERROR_NUM_CONNECT;
            return result;
        } else {
            if (!checkResult.isFlgPrinterStatus()) {
                result = checkResult.getErrorNo();
                return result;
            }
        }

        return result;
    }

    /**
     * Cut paper.
     *
     * @return Returns true or false if the paper is cut
     */
    public final boolean cutPaper() {

        if (socket == null) {
            return false;
        }
        try {
            // cut the paper with feeding 100 dot rows
            outSkt.write(CUT_CMD);
            outSkt.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Open Drawer.
     *
     * @return Returns true or false if the drawer is opened
     */
    public final boolean openDrawer() {

        if (socket == null) {
            return false;
        }
        try {
            outSkt.write(OPENDRAWER_CMD);
            outSkt.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Disconnect to the printer.
     *
     * @return Returns true or false if the printer is disconnected
     */
    public final boolean close() {

        // close input stream
        if (inSkt != null) {
            try {
                inSkt.close();
            } catch (IOException e) {
                return false;
            }
            inSkt = null;
        }
        // close output stream
        if (outSkt != null) {
            try {
                outSkt.close();
            } catch (IOException e) {
                return false;
            }
            outSkt = null;
        }
        // close socket
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
            socket = null;
        }

        return true;
    }

    /**
     * Transmits the status of TCP.
     *
     * @param senMSG Message sent
     * @param recvMSG Message received
     * @return Results to true or false if the status of TCP is transmitted.
     */
    private boolean transmitStatusTCP(final byte[] senMSG, final byte[] recvMSG) {

        if (socket == null || inSkt == null || outSkt == null) {
            return true;
        }

        try {
            outSkt.write(senMSG);
            outSkt.flush();

            int b = inSkt.read(recvMSG);
            if (b == 0) {
                return true;
            }
        } catch (IOException e) {
            return true;
        }

        return false;
    }

    /**
     * Gets TCP Error number.
     *
     * @param status Status
     * @return Error number
     */
    private int getErrorNumTCP(final byte status) {

        int errNum = 0;
        byte key = (byte) 0x01;

        if ((status & (key << 1)) == (key << 1)) {
            errNum = ERROR_NUM_COVER;
            return errNum;
        } else if ((status & key) == key || (status & (key << 2)) == (key << 2)) {
            errNum = ERROR_NUM_PAPER;
            return errNum;
        }

        return errNum;
    }

    /**
     * Transmits UDP status.
     *
     * @param sendMSG Send Message
     * @param recvMSG Receive Message
     * @return Boolean result if UDP status is transmitted
     */
    private boolean transmitStatusUDP(final byte[] sendMSG, final byte[] recvMSG) {

        try {
            dgSocket = new DatagramSocket();
            dgSocket.setSoTimeout(RECV_UDP_TIMEOUT);

            sendDgSkt = new DatagramPacket(sendMSG, sendMSG.length, addressUDP);
            recvDgSkt = new DatagramPacket(recvMSG, recvMSG.length);

            dgSocket.send(sendDgSkt);

            dgSocket.receive(recvDgSkt);
        } catch (SocketException e) {
            return true;
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    /**
     * Gets error type of busy status from received message.
     *
     * @param status Busy status
     * @return Error number
     */
    private int getBusyStsErrorNumUDP(final byte status) {

        int errNum = 0;
        byte key = (byte) 0x01;

        if ((status & key << 2) == key << 2) {
            errNum = ERROR_NUM_COVER;
            return errNum;
        } else if ((status & key << 5) == key << 5) {
            errNum = ERROR_NUM_STOP_PRINT;
            return errNum;
        } else if ((status & key << 6) == key << 6) {
            errNum = ERROR_NUM_ERR_COMDITION;
            return errNum;
        }

        return errNum;
    }

    /**
     * Gets error type of error status from received message.
     *
     * @param status Error status
     * @return Error number
     */
    private int getErrorStsErrorNumUDP(final byte status) {

        int errNum = 0;
        byte key = (byte) 0x01;

        if ((status & key << 3) == key << 3) {
            errNum = ERROR_NUM_KNIFE;
            return errNum;
        } else if ((status & key << 5) == key << 5) {
            errNum = ERROR_NUM_UNRECOVERABLE;
            return errNum;
        } else if ((status & key << 6) == key << 6) {
            errNum = ERROR_NUM_POWER;
            return errNum;
        }

        return errNum;
    }

    /**
     * Gets error type of Receipt Paper Status from received message.
     *
     * @param status Receipt paper status
     * @return Error number
     */
    private int getRcptPaperErrorNumUDP(final byte status) {

        int errNum = 0;
        byte key = (byte) 0x01;

        if ((status & key << 5) == key << 5 || (status & key << 6) == key << 6) {
            errNum = ERROR_NUM_PAPER;
            return errNum;
        }

        return errNum;
    }

    /**
     * Sets the Check result if status has any errors.
     *
     * @param result Printer check result
     * @param isTimeout Flag timeout
     * @param printerStatus Printer status
     * @param status TCP status
     * @param errorNum Error number
     */
    private void setCheckResult(final PrinterCheckResult result,
            final boolean isTimeout, final boolean printerStatus,
            final byte status, final int errorNum) {

        result.setFlgTimeout(isTimeout);
        result.setFlgPrinterStatus(printerStatus);
        result.setErrorStatus(status);
        result.setErrorNo(errorNum);
    }

    /**
     * Copies data from one array into another.
     *
     * @param array1 Object Source
     * @param array2 Object Source
     * @return Result copied from object source
     */
    private byte[] byteArrayAppend(final byte[] array1, final byte[] array2) {

        byte[] result = new byte[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }
}
