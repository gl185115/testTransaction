package ncr.res.ue.core;

import java.util.List;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.ue.message.action.ConnectionInitialization;
import ncr.res.ue.message.response.ConnectionInitializationResponse;
import ncr.res.ue.message.response.UEResponseBase;

/**
 * Handles connection
 * to UE.
 * @author jg185106
 *
 */
public class Connection {
    /**
     * Success response flag for Connection Initialization.
     */
    public static final int CONN_INIT_SUCCESS = 0;
    /**
     * Servlet context.
     */
    private ServletContext context;
    /**
     * Socket communicator.
     */
    private Communicator ueCommunicator = null;
    /**
     * Socket communicator.
     * (read only)
     * @return Communicator socket handler created during
     * communicator initialization
     */
    public final Communicator getCommunicator() {
        return ueCommunicator;
    }
    /**
     * Default constructor.
     *
     * @param thisContext the servlet context
     * @throws Exception general exception only
     */
    public Connection(final ServletContext thisContext) throws Exception {
        this.context = thisContext;
        ueCommunicator = initializeCommunicator();
    }
    /**
     * Creates the communicator socket handler.
     * @return Communicator
     * @throws Exception general exception
     */
    private Communicator initializeCommunicator() throws Exception {
        String ueIpAddress = (String)
                    this.context.getInitParameter(
                            GlobalConstant.UE_IOSERVER_ADDRESS);
        String uePort = (String)
                    this.context.getInitParameter(
                            GlobalConstant.UE_IOSERVER_PORT);
        return new Communicator(ueIpAddress, Integer.parseInt(uePort));
    }
    /**
     * Initialize connection to UE.
     * @throws Exception general exception
     * @return ConnectionInitializationResponse
     */
    public final ConnectionInitializationResponse initializeUeConnection()
                throws Exception {
        return initializeUeConnection(GlobalConstant.getCorpid());
    }
    /**
     * Initialize connection to UE.
     * @param terminalId Register or Terminal Number for the
     *          transport header
     * @throws Exception general exception
     * @return ConnectionInitializationResponse
     */
    public final ConnectionInitializationResponse
                    initializeUeConnection(final String terminalId)
                throws Exception {
        String locationCode = (String) this.context.getInitParameter(
                GlobalConstant.UE_LOCATION_CODE);
        String protocolVersion = (String) this.context.getInitParameter(
                GlobalConstant.UE_PROTOCOL_VERSION);
        String protocolBuild = (String) this.context.getInitParameter(
                GlobalConstant.UE_PROTOCOL_BUILD);
        ConnectionInitialization initConn = new ConnectionInitialization(
                locationCode, protocolVersion, protocolBuild);
        List<UEResponseBase> responses =
            ueCommunicator.sendReceiveMessage(
                    initConn.createMessage(terminalId, "0"));
        return (ConnectionInitializationResponse)
                    responses.get(responses.size() - 1);
    }
    /**
     * Close the socket connection.
     */
    public final void closeConnection() {
        ueCommunicator.stopConnection();
    }
}
