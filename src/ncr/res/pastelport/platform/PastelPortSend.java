// Copyright(c) 2012NCR Japan Ltd.
//
//
// $Id: SocketClient.java
//
package ncr.res.pastelport.platform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

/**
 * RealGate<-->CAPS for CN Gateway Option Client for communication. To send and
 * receive telegrams to create a Socket.
 *
 * @version $Revision: 1.9 $ $Date: 2008/06/02 02:31:50 $
 */

public class PastelPortSend {
	

    /**
     * The Socket.
     */
    private Socket skt = null;
    /**
     * The input Stream Socket.
     */
    private BufferedInputStream inSkt = null;
    /**
     * The output stream socket.
     */
    private BufferedOutputStream otSkt = null;
    /**
     * The Receive Time out.
     */
    private int recvTimeOut = 0;
    /**
     * The Connection Time Out.
     */
    private int conTimeOut = 0;
    /**
     * The Retry count.
     */
    private int retryCount = 1;
    /**
     * The Connection Host.
     */
    private String conHost = "localhost";
    /**
     * The Print log flag.
     */
    private boolean isPrintLog = false;
    /**
     * The Program name.
     */
    private static final String PROGNAME = "PastelPortSend";
    /**
     * The Second in millisecond.
     */
    private static final int ONE_SECOND = 1000;
    /**
     * The Memory count.
     */
    private static final int MAX_MEMORY_COUNT = 4096;

    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get logger
    /** The Debug Trace Printer. */
    private Trace.Printer tp;

    /**
     * The default constructor.
     */
    public PastelPortSend() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }

    /**
     * the receive timeout value timeout IP address and connection port number
     * is set to CardNetTx, creating a new socket.
     *
     * @param pastelPortEnv the Pastel Port Environment.
     * @throws IOException  The exception thrown when error occur.
     */
    public PastelPortSend(final PastelPortEnv pastelPortEnv)
    throws IOException {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
        if (pastelPortEnv.getTimeOut() >= 0) {
            // Setting Connection Timeout
            conTimeOut = pastelPortEnv.getTimeOut() * ONE_SECOND;
            // Setting Connection Timeout
            recvTimeOut = pastelPortEnv.getTimeOut() * ONE_SECOND;
        }
        if (pastelPortEnv.getRetryCount() >= 0) {
            // Setting Connection retry count
            retryCount = pastelPortEnv.getRetryCount();
        }
        if (pastelPortEnv.getPastelPortMultiServerIP() != null) {
            // Setting Connection host
            conHost = pastelPortEnv.getPastelPortMultiServerIP();
        }
        // the flag to print the send message
        isPrintLog = pastelPortEnv.isPrintPPLog();

        skt = new Socket();
        InetSocketAddress inetSocketAddress = null;

        for (int i = 0; i <= retryCount; i++) {
            try {
                if (inetSocketAddress == null) {
                    inetSocketAddress = new InetSocketAddress(conHost,
                            pastelPortEnv.getConnectionPortNO());
                }
                skt.connect(inetSocketAddress, conTimeOut);
            } catch (Exception e1) {
                LOGGER.logAlert(PROGNAME,
                        PROGNAME+".Constructor",
                        Logger.RES_EXCEP_GENERAL,
                        "connect to pastelport is failed!");
            }
            if (skt.isConnected()) {
                break;
            }
        }

        if (!skt.isConnected()) {
            try {
                skt.close();
            } catch (IOException e) {
                LOGGER.logAlert(PROGNAME,
                        PROGNAME+".Constructor",
                        Logger.RES_EXCEP_GENERAL,
                        "close connection is failed!");
            }
            skt = null;
            throw new IOException("connect timeOut");
        }
        try {
            skt.setTcpNoDelay(true);
            skt.setSoTimeout(recvTimeOut);
        } catch (SocketException e) {
            LOGGER.logAlert(PROGNAME,
                    PROGNAME+".Constructor",
                    Logger.RES_EXCEP_GENERAL,
                    "setting of connection to pastelport is failed!");
            throw new IOException("connect timeOut");
        }
        try {
            inSkt = new BufferedInputStream(skt.getInputStream());
            otSkt = new BufferedOutputStream(skt.getOutputStream());
        } catch (IOException e) {
            LOGGER.logAlert(PROGNAME,
                    PROGNAME+".Constructor",
                    Logger.RES_EXCEP_GENERAL,
                    "new IOStream of connection to pastelport is failed!");
            throw new IOException("connect timeOut");
        }

    }

    /**
     * Telegram to send message E.
     * @param tx        The PastelPort Base to send.
     * @return          The length of the Send Message.
     * @throws IOException  The exception thrown when error occur.
     */
    public final int sendMsg(final PastelPortTxSendBase tx)
    throws IOException {
        tp.methodEnter("sendMsg");

        // Check the OutputStream of the sending socket
        if (otSkt == null) {
            tp.println("送信ソケットのOutputStreamを確認できない");
            tp.methodExit();
            throw new IOException("sending message is failed");
        }
        int length = 0;
        // Communication to the telegram set the ID = S
        String sndMsg = tx.toString();
        if (isPrintLog) {
            tx.check();
        }
        tp.println("送信データ");

        // Sent to the Gateway option
        try {
            otSkt.write(sndMsg.getBytes(Charset.forName("UTF-8")));
            otSkt.flush();
        } catch (UnsupportedEncodingException e) {
            tp.println("未サポートのエンコード " + e.getMessage());
            throw e;
        } catch (IOException e) {
            tp.println("送信中にIOException検出 " + e.getMessage());
            throw e;
        } finally {
            if  (null != sndMsg) {
                length = sndMsg.length();
            }
            tp.methodExit(length);
        }
        return length;
    }

    /**
     * Telegram to receive E.
     * @return The array of byte of the receive message.
     * @throws IOException The exception thrown when error occur.
     */
    public final byte[] recvMsg() throws IOException {
        tp.methodEnter("recvMsg");
        int rcvLen = 0;
        int memoryCount = MAX_MEMORY_COUNT;
        ByteArrayOutputStream recvMsg = new ByteArrayOutputStream();
        OutputStream os = new BufferedOutputStream(recvMsg);
        // Check the receive socket InputStream
        if (inSkt == null) {
            tp.println("受信ソケットのOutputStreamを確認できない");
            tp.methodExit();
            throw new IOException("receiving message is failed");
        }
        try {
            // Received from the Gateway option
            while (rcvLen != -1) {
                byte[] b = new byte[memoryCount]; // Choose to provide extra
                rcvLen = inSkt.read(b, 0, memoryCount);

                tp.println("受信データ");

                if (rcvLen > 0) {
                    os.write(b, 0, rcvLen);
                    os.flush();
                }
            }
          
        } catch (java.io.EOFException e) {
            tp.println("受信中にEOF検出");
            throw e;
        } catch (java.net.SocketTimeoutException e) {
            tp.println("受信中にタイムアウト発生");
            throw e;
        } catch (java.net.SocketException e) {
            tp.println("受信中にSocketException検出 " + e.getMessage());
            throw e;
        } catch (IOException e) {
            tp.println("受信中にIOException検出 " + e.getMessage());
            throw e;
        } catch (NullPointerException e) {
            tp.println("受信中にNullPointerException検出 "
                    + e.getMessage());
            throw e;
        }  finally {
            tp.methodExit(recvMsg);
            try {
                if (os != null) {
                    os.close();
                }
                if (recvMsg != null) {
                	recvMsg.close();
                }
            } catch (Exception e) {
                //do nothing.
            	LOGGER.logAlert(PROGNAME,
                        PROGNAME+".recvMsg()",
                        Logger.RES_EXCEP_GENERAL,
                        "Failed to close output stream!");
            }
        }

        return recvMsg.toByteArray();
    }

    /**
     * close socket.
     */
    public final void close() {
        tp.methodEnter("close");
        try {
        	if (inSkt != null){
        		inSkt.close();
        	}
        	
        	if (otSkt != null){
        		otSkt.close();
        	}
        	 if (skt != null){
        		 skt.close(); 
        	 }
        	
            
        } catch (Exception e) {
        	LOGGER.logAlert(PROGNAME,
                    PROGNAME+".close",
                    Logger.RES_EXCEP_GENERAL,
                    "close connection is failed!");
        }finally{
        	inSkt = null;
        	otSkt = null;
        	skt = null;
        }
        tp.methodExit();
    }

    /**
     * get the socket.
     * @return The Socket.
     */
    public final Socket getSocket() {
        return skt;
    }
}
