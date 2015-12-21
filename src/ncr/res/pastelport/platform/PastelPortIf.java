package ncr.res.pastelport.platform;

import java.io.IOException;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortConvert;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortRestore;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.helper.DebugLogger;
import atg.taglib.json.util.JSONException;

/**
 * The Pastel Port utility.
 *
 */
public final class PastelPortIf {

    /** The Default Constructor. */
    private PastelPortIf() {
    }

   /**
    * The Debug Trace Printer.
    */
    private static Trace.Printer tp;

    /**
     * Prepare the environment for Pastel Port.
     * @param servletContext The Servlet Context.
     * @return Returns true.
     */
    public static boolean init(final ServletContext servletContext) {
        return true;
    }

    /**
     * 20120420 lzb.
     *
     * @param pastelPortEnv
     *            : environment infomation
     * @param tx
     *            : the message that send to PatalPortServer
     * @param resp
     *            : the result that communication with pastelport
     * @throws IOException      Exception thrown for IO error.
     * @throws JSONException    Exception thrown for JSON Parsing.
     * @return The PastelPort Transaction Receive Base.
     */
    public static PastelPortTxRecvBase communicationWithPastalPort(
            final PastelPortEnv pastelPortEnv,
            final CommonTx tx, final PastelPortResp resp)
            throws Exception {

        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                PastelPortIf.class);
        tp.methodEnter("PastelPortTxRecvBase")
            .println("PastelPortEnv", pastelPortEnv.toString())
            .println("CommonTx", tx.toString())
            .println("PastelPortResp", resp.toString());

        PastelPortConvert pastalPortConvert = new PastelPortConvert(
                pastelPortEnv);

        PastelPortTxSendBase ppTxSend = pastalPortConvert.convertSendTx(tx);

        PastelPortTxRecvBase pastelPortTxRecvImpl = null;
        byte[] recvMsg = null;
        try {
            PastelPortSend pastelPortSend = new PastelPortSend(pastelPortEnv);
            pastelPortSend.sendMsg(ppTxSend);
            recvMsg = pastelPortSend.recvMsg();
            pastelPortSend.close();
            pastelPortTxRecvImpl = pastalPortConvert.stringToObject(recvMsg);
            PastelPortRestore ppRestore = new PastelPortRestore();
            ppRestore.restore(pastelPortTxRecvImpl, resp);
        } catch (IOException e) {
            PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                    "その他エラー");
            tp.println("Cummunication with pastelport failed.");
        } catch (JSONException e) {
            PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                    "その他エラー");
            tp.println("Cummunication with pastelport failed.");
        } finally {
            String pastelPortTxRecvImplStr = null;
            if (null != pastelPortTxRecvImpl) {
                pastelPortTxRecvImplStr = pastelPortTxRecvImpl.toString();
            }
            tp.methodExit(pastelPortTxRecvImplStr);
        }
        return pastelPortTxRecvImpl;
    }
}
