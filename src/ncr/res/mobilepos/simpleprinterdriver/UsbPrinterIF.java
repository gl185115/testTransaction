package ncr.res.mobilepos.simpleprinterdriver;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.20      FENGSHA       80mmの紙変更を対応
 * 1.02            2015.02.06      FENGSHA       Printer時間の設定変更を対応
 */
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class UsbPrinterIF implements IPrinter{

    private String printerCom;

    private BufferedInputStream inSkt = null;
    private BufferedOutputStream outSkt = null;

    private SerialPort serialPort = null;

    private List<byte[]> printData = new ArrayList<byte[]>();

    public UsbPrinterIF(String com) {
        this.printerCom = com;
    }

    public final boolean connectPrinter() {
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(this.printerCom);
            //1.02 2015.02.06 FENGSHA MOD START
            //serialPort = (SerialPort) portId.open(portId.getName(), 1000);
            serialPort = (SerialPort) portId.open(portId.getName(), CONNECT_TIME_OUT);
            //1.02 2015.02.06 FENGSHA MOD END
            serialPort.setDTR(true);

            inSkt = new BufferedInputStream(serialPort.getInputStream());
            outSkt = new BufferedOutputStream(serialPort.getOutputStream());

            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (NoSuchPortException e) {
            return false;
        } catch (PortInUseException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (UnsupportedCommOperationException e) {
            return false;
        }
        return true;
    }

    public final boolean initPrinter() {
        if (serialPort == null) {
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
           // outSkt.write(INIT_CMD);
          //1.01 2015.01.20 FENGSHA ADD END
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public final boolean clearBuffer() {
        if (serialPort == null) {
            return false;
        }
        try {
            outSkt.write(CLEAR_BUFFER_CMD);
        } catch (IOException e) {
            return false;
        }
        printData = new ArrayList<byte[]>();
        return true;
    }

    public final boolean writeBmp(final byte[] data, final int flag, final int paperFlg) {
        byte[] buffer = getBmp(data, flag, paperFlg);
        if (buffer == null) {
            return false;
        }

        printData.add(buffer);
        return true;
    }

    public final void writeText(final byte[] data) {
        if (data != null && data.length != 0) {
            printData.add(data);
        }
    }

    public final int print() {
        int result = 0;
        PrinterCheckResult checkResult = new PrinterCheckResult();

        // Check printer status
        try {
            checkResult = checkPrinterStatus();
        } catch (IOException e) {
            this.clearBuffer();
            result = ERROR_NUM_CONNECT;
            return result;
        }

        if (checkResult.isFlgTimeout()) {
            result = ERROR_NUM_CONNECT;
            return result;
        }

        for (byte[] bs : this.printData) {
            try {
                outSkt.write(bs);
                outSkt.flush();
            } catch (IOException e) {
                return ERROR_NUM_CONNECT;
            }
        }
        //modify by mlwang 2014/10/28
        // Check printer status
        try {
            checkResult = checkPrinterStatus();
        } catch (IOException e) {
            this.clearBuffer();
            result = ERROR_NUM_CONNECT;
            return result;
        }

        if (checkResult.isFlgTimeout()) {
            result = ERROR_NUM_CONNECT;
            return result;
        }
        //end modify 2014/10/28
        return result;
    }

    public final boolean cutPaper() {
        try {
            outSkt.write(CUT_CMD);
            outSkt.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public final boolean close() {
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

        if (serialPort != null) {
            serialPort.close();
        }
        return true;
    }

    private PrinterCheckResult checkPrinterStatus() throws IOException {
        PrinterCheckResult result = new PrinterCheckResult();

        // check the socket connecting to the printer is useful
        if (serialPort == null) {
            result.setFlgTimeout(PRINTER_ERROR);
            return result;
        }

        byte[] res = null;
        byte datum = 1;

        // RS232C busy status
        outSkt.write(GET_BUSY_STATUS_UDP_CMD);
        outSkt.flush();

        res = new byte[8];
        if (inSkt.read(res) > 0) {
            if ((res[0] >> 2 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_COVER);
                return result;
            } else if ((res[0] >> 5 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_STOP_PRINT);
                return result;
            } else if ((res[0] >> 6 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_ERR_COMDITION);
                return result;
            }
        }

        // Error status
        outSkt.write(GET_ERROR_STATUS_UDP_CMD);
        outSkt.flush();

        res = new byte[8];
        if (inSkt.read(res) > 0) {
            if ((res[0] >> 3 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_KNIFE);
                return result;
            } else if ((res[0] >> 5 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_UNRECOVERABLE);
                return result;
            } else if ((res[0] >> 6 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_POWER);
                return result;
            }
        }

        // Receipt paper status
        outSkt.write(GET_PAPER_STATUE_UDP_CMD);
        outSkt.flush();

        res = new byte[8];
        if (inSkt.read(res) > 0) {
            if ((res[0] >> 2 & datum) == datum) {
                //do nothing
            } else if ((res[0] >> 5 & datum) == datum) {
                result.setErrorNo(ERROR_NUM_PAPER);
                return result;
            }
        }

        return result;
    }

    private byte[] getBmp(
            final byte[] data, final int flag, final int paperFlg) {
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

    public void writeText(byte[] data, int index) {
        if (data != null && data.length != 0) {
            printData.add(index, data);
        }
    }

    public boolean openDrawer() {
        if (serialPort == null) {
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
}
