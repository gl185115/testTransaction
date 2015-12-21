package ncr.res.ue.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.ue.exception.CommunicationException;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.TransportHeader;
import ncr.res.ue.message.response.UEResponseBase;
import ncr.res.ue.message.response.UEResponseFactory;

/**
 * handles communication with UE.
 *
 * @author AP185142
 *
 */
public class Communicator {
	/**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * the socket connection.
     */
    private static Socket commSocket = null;
    /**
     * the object used to send to the socket.
     */
    private static PrintStream sendThroughSocket = null;
    /**
     * the object used to read from the socket.
     */
    private static BufferedReader readThroughSocket = null;
    /**
     * class instance of debug trace printer.
     */
    private static Trace.Printer tp;
    
    private static final String PROG_NAME = "Communicator";

    /**
     * Constructor.
     *
     * @param ipAddress
     *            - ip address of UE Server
     * @param socketNumber
     *            - socket to connect to
     * @throws Exception
     *             - thrown when any error occurs
     */
    public Communicator(final String ipAddress, final int socketNumber)
            throws Exception {

        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        String functionName = "constructor";
        try {
            commSocket = new Socket(ipAddress, socketNumber);
            sendThroughSocket = new PrintStream(commSocket.getOutputStream(),
                    true, "UTF-8");
            readThroughSocket = new BufferedReader(new InputStreamReader(
                    commSocket.getInputStream(), Charset.forName("UTF-8")));
        } catch (UnknownHostException e) {
            tp.write("UE.Communicator - constructor : unknown host name "
                    + ipAddress);
            
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Unknown host name.", e);
            throw new CommunicationException(
                    "UE.Communicator - constructor : unknown host name "
                            + ipAddress);
        } catch (IOException e) {
            tp.write("UE.Communicator - constructor : could not connect "
                    + "IP: " + ipAddress + " Socket: "
                    + Integer.toString(socketNumber));
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": could not connect "+ "IP: " + ipAddress + " Socket: "
                    + Integer.toString(socketNumber), e);
            throw new CommunicationException(
                    "UE.Communicator - constructor : could not connect "
                            + "IP: " + ipAddress + " Socket: "
                            + Integer.toString(socketNumber));
        }
    }

    /**
     * Default Constructor. should not be used
     *
     * @throws MessageException
     *             - thrown immediately
     */
    @SuppressWarnings("unused")
    private Communicator() throws MessageException {

    }

    /**
     * sets the socket read timeout.
     *
     * @param socketTimeout
     *            - number of miliseconds before timeout occurs
     * @throws SocketException
     *             - thrown when an error occurs
     */
    public final void setSocketTimeout(final int socketTimeout)
            throws SocketException {
        if (commSocket == null) {
            return;
        }

        if (socketTimeout <= 0) {
            return;
        }

        commSocket.setSoTimeout(socketTimeout);
    }

    /**
     * sends the given message and gets the reply.
     *
     * @param message
     *            - the message to send to the UE server
     * @return UEResponseBase - the base class of a generated response class
     *         containing the data from the reply. Can be cast to its original
     *         class from UEResponseBase to access the parsed data.
     * @throws MessageException
     *             - thrown when any error occurs
     */
    public final List<UEResponseBase> sendReceiveMessage(final String message)
            throws MessageException {
    	String functionName = "sendReceiveMessage";
        List<UEResponseBase> result = null;
        
        if (commSocket == null || !commSocket.isConnected()) {
            throw new MessageException("Communicator.sendReceiveMessage :"
                    + "error accessing socket - "
                    + "socket is null or not connected");
        }

        if (sendThroughSocket == null) {
            throw new MessageException("Communicator.sendReceiveMessage :"
                    + "error PrintStream is null");
        }

        // send message through socket
        sendThroughSocket.println(message);

        // receive reply
        try {
            result = getReply();
        } catch (SocketTimeoutException ste) {
            tp.write("UE.Communicator - sendReceiveMsg : receive timeout "
                    + message);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": socket timeout exception..", ste.getMessage());
            throw new MessageException("Communicator.sendReceiveMessage :"
                    + "socket timeout exception. "+ste.getMessage());
        } catch (IOException e) {
            tp.write("UE.Communicator - sendReceiveMsg : cannot receive "
                    + message);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + "BufferedReader error. ", e.getMessage());
            throw new MessageException("Communicator.sendReceiveMessage :"
                    + "BufferedReader error. "+e.getMessage());
        }

        return result;
    }

    /**
     * Stops the socket connection.
     */
    public final void stopConnection() {
    	String functionName = "stopConnection";
        if (sendThroughSocket != null) {
            sendThroughSocket.close();
            sendThroughSocket = null;
        }

        if (readThroughSocket != null) {
            try {
                readThroughSocket.close();
                readThroughSocket = null;
            } catch (IOException e) {
            	
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
    					functionName + "BufferedReader error. ", e.getMessage());
            }
        }

        if (commSocket != null) {
            try {
                commSocket.close();
                commSocket = null;
            } catch (IOException e) {
            	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
    					functionName + "BufferedReader error. ", e.getMessage());
            }
        }
    }

    /**
     * determines if the given string is a terminating reply.
     *
     * @param reply
     *            - the string to parse
     * @return boolean
     */
    private boolean isTerminatingReply(final String reply) {
        if (null == reply || reply.isEmpty()) {
            return false;
        }
        String messageType = reply.substring(
                TransportHeader.TRANSPORT_HEADER_LENGTH,
                TransportHeader.TRANSPORT_HEADER_LENGTH
                        + TransportHeader.DATA_MESSAGE_HEADER_LENGTH);

        if (messageType.length()
                != TransportHeader.DATA_MESSAGE_HEADER_LENGTH) {
            return false;
        }

        switch (Integer.parseInt(messageType)) {
        case MessageTypes.IM_ADJUSTMENT_RESPONSE:
        case MessageTypes.IM_BEGIN_TRANSACTION_RESPONSE:
        case MessageTypes.IM_CONNECTION_INITIALIZE_RESPONSE:
        case MessageTypes.IM_END_TRANSACTION_RESPONSE:
        case MessageTypes.IM_GS1_COUPON_RESPONSE:
        case MessageTypes.IM_ITEM_ENTRY_RESPONSE:
        case MessageTypes.IM_ITEM_POINTS_RESPONSE:
        case MessageTypes.IM_ITEM_QUANTITY_RESPONSE:
        case MessageTypes.IM_MEMBER_ID_RESPONSE:
        case MessageTypes.IM_RESUME_TRANSACTION_RESPONSE:
        case MessageTypes.IM_STATUS_RESPONSE:
        case MessageTypes.IM_SUSPEND_TRANSACTION_RESPONSE:
        case MessageTypes.IM_TENDER_ENTRY_RESPONSE:
        case MessageTypes.IM_TOTAL_RESPONSE:
        case MessageTypes.IM_TRIGGER_CODE_RESPONSE:
            return true;
        default:
            return false;
        }
    }

    /**
     * determines if the message is complete basing on the message length taken
     * from the message.
     *
     * @param reply
     *            - the message to check
     * @return true if complete, false if length does not match
     */
    private boolean isMessageComplete(final String reply) {
        if (null == reply || reply.isEmpty()) {
            return false;
        }
        int expectedMessageLength = Integer.parseInt(reply.substring(0,
                TransportHeader.TH_MESSAGELENGTH_LENGTH));

        int actualMessageLength = reply.length() - 2; // minus terminator

        if (expectedMessageLength == actualMessageLength) {
            return true;
        }
        return false;
    }

    /**
     * retrieves the reply.
     *
     * @return the generated reply class with parsed data
     * @throws IOException
     *             - thrown when error occurs in the input output
     * @throws MessageException
     *             - thrown when a custom error occurs
     */
    private List<UEResponseBase> getReply() throws IOException,
            MessageException {

        List<String> replyArray = new ArrayList<String>();
        String partialReply = "";

        /*
         * reads all the data sent from UE. breaks when it encounters a
         * terminating reply.
         *
         * if it has not encountered a valid terminating reply, it will continue
         * to wait. It will eventually throw an exception when the duration of
         * the socketTimeout is reached and no data has been read.
         */
        do {
            partialReply = readThroughSocket.readLine();
            replyArray.add(partialReply);
            // check if terminating reply.
            if (isTerminatingReply(partialReply)) {
                break;
            }
        } while (true);

        List<String> verifiedReply = new ArrayList<String>();
        for (String reply : replyArray) {
            if (!isMessageComplete(reply)) {
                tp.write("UE.Communicator - check Reply length : incomplete - "
                        + reply);
            } else {
                verifiedReply.add(reply);
            }
        }

        return UEResponseFactory.generateResponse(verifiedReply);

    }

}
