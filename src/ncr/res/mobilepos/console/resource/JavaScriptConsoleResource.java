package ncr.res.mobilepos.console.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import ncr.realgate.util.Trace;
import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Snap;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * JavaScript's console object alternative implementation.
 * This class is delegated from JavaScript side module (NCRConsole) and write messages into IOWLog or Trace.
 */
@Path("/console")
public class JavaScriptConsoleResource {
    /**
     * A private member variable used for logging the class implementations.
     */
    private Logger ioWriter;
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp;

    static final String PROG_NAME = "Jconsole";
    static final String JS_CODE = "JS";
    static final String TRACE_TITLE = "RES MeX";

    /** The context. */
    @Context
    private ServletContext context;
    /**
     * Sets the context.
     *
     * @param contextToSet the new context
     */
    public final void setContext(final ServletContext contextToSet) {
        context = contextToSet;
    }

    public JavaScriptConsoleResource() {
        ioWriter = (Logger) Logger.getInstance();
        tp = DebugLogger.getDbgPrinter(
                   Thread.currentThread().getId(), getClass());
    }

    /**
     * Write specified message to IOWLog with Error level.
     * format: C:cccccccccc S:ssss T:tttt X:xxxx message
     * @param message log message
     * @param storeid storeid of the terminal
     * @param terminalid terminal id
     * @param txid transaction id
     */
    @Path("/log")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public ResultBase log(@FormParam("message") String message, 
                    @FormParam("storeid") String storeid,
                    @FormParam("terminalid") String terminalid, @FormParam("txid") String txid) {
        write(IoWriter.ERROR, message, storeid, terminalid, txid);
        return new ResultBase(ResultBase.RES_OK, "");
    }
    /**
     * Write specified message to IOWLog with Error level.
     * format: C:cccccccccc S:ssss T:tttt X:xxxx message
     * @param message log message
     * @param storeid storeid of the terminal
     * @param terminalid terminal id
     * @param txid transaction id
     */
    @Path("/error")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public ResultBase error(@FormParam("message") String message, 
                      @FormParam("storeid") String storeid,
                      @FormParam("terminalid") String terminalid, @FormParam("txid") String txid) {
        write(IoWriter.ERROR, message, storeid, terminalid, txid);
        return new ResultBase(ResultBase.RES_OK, "");
    }
    /**
     * Write specified message to IOWLog with Warning level.
     * format: C:cccccccccc S:ssss T:tttt X:xxxx message
     * @param message log message
     * @param storeid storeid of the terminal
     * @param terminalid terminal id
     * @param txid transaction id
     */
    @Path("/warn")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public ResultBase warn(@FormParam("message") String message, 
                     @FormParam("storeid") String storeid,
                     @FormParam("terminalid") String terminalid, @FormParam("txid") String txid) {
        write(IoWriter.WARNING, message, storeid, terminalid, txid);
        return new ResultBase(ResultBase.RES_OK, "");
    }
    /**
     * Write specified message to IOWLog with Informational level.
     * format: C:cccccccccc S:ssss T:tttt X:xxxx message
     * @param message log message
     * @param storeid storeid of the terminal
     * @param terminalid terminal id
     * @param txid transaction id
     */
    @Path("/info")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public ResultBase info(@FormParam("message") String message, 
                     @FormParam("storeid") String storeid,
                     @FormParam("terminalid") String terminalid, @FormParam("txid") String txid) {
        write(IoWriter.LOG, message, storeid, terminalid, txid);
        return new ResultBase(ResultBase.RES_OK, "");
    }
    /**
     * Write specified message to Trace.
     * format: from-iOS C:cccccccccc S:ssss T:tttt X:xxxx message
     * @param message log message
     * @param storeid storeid of the terminal
     * @param terminalid terminal id
     * @param txid transaction id
     */
    @Path("/debug")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public ResultBase debug(@FormParam("message") String message, 
                      @FormParam("storeid") String storeid,
                      @FormParam("terminalid") String terminalid, @FormParam("txid") String txid) {
        tp.println(TRACE_TITLE, 
                   createLoggingMessage(message, storeid, terminalid, txid));
        tp.flush();
        return new ResultBase(ResultBase.RES_OK, "");
    }

    void write(char flag, String message, String storeid, String terminalid, String txid) {
        if (message.length() >= IoWriter.WIDTH * IoWriter.MAX_LINES) {
            Snap.SnapInfo info = ((SnapLogger)SnapLogger.getInstance()).write("jsdata", message);
            ioWriter.write(flag, PROG_NAME, JS_CODE, 
                       createLoggingMessage(message, storeid, terminalid, txid),
                       info);
        } else {
            ioWriter.write(flag, PROG_NAME, JS_CODE, 
                       createLoggingMessage(message, storeid, terminalid, txid));
        }
    }

    CharSequence createLoggingMessage(String message,
                                      String storeid, String terminalid, String txid) {
        StringBuilder sb = new StringBuilder("S:");
        adjust(sb, storeid, 4).append(" T:");
        adjust(sb, terminalid, 4);
        if (txid != null && txid.length() > 0 && !txid.equals("null")) {
            sb.append(" X:");
            adjust(sb, txid, 4);
        }
        sb.append(' ').append(message);
        return sb;
    }

    StringBuilder adjust(StringBuilder sb, String val, int len) {
        if (val == null) {
            val = "";
        }
        if (len < val.length()) {
            for (int i = 0; i < val.length() - len; i++) {
                sb.append('0');
            }
        }
        return sb.append(val);
    }
}


