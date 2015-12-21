/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * ConsolidationResource
 *
 * Web Resource which support MobilePOS Transaction Consolidation processes.
 *
 * Campos, Carlos  (cc185102)
 */
package ncr.res.mobilepos.consolidation.resource;

import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.consolidation.dao.CurDbInfo;
import ncr.res.mobilepos.consolidation.dao.IConsolidate;
import ncr.res.mobilepos.consolidation.dao.TransactionInfo;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;

/**
 * ConsolidationResource Class is a Web Resource which support MobilePOS
 * Transaction Consolidation processes.
 */
@Path("/consolidation")
public class ConsolidationResource {

    /**
     * ServletContext instance.
     */
    @Context
    private ServletContext context;
    /**
     * Static variable for poslog's xml not consolidated.
     */
    private static final int NO_POSLOGXML_CONSOLIDATED = 2;

    /**
     * Static variable for poslog's xml that is consolidated.
     */
    private static final int POSLOGXML_CONSOLIDATED = 0;
    /**
     * Logs exception or information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Program name use in logging.
     */
    private String progName = "Consldtn";
    /**
     * Class name use in logging.
     */
    private String className = "ConsolidationResource.";
    /**
     * DAOFactory instance use to access CredentialDAO.
     */
    private DAOFactory daoFactory = null;
    /** The Trace Printer. */
    private Trace.Printer tp = null;
    /**
     * Instance of SnapLogger for output error transaction data to snap file.
     */
    private SnapLogger snap;

    /**
     * Default constructor. Instantiate ioWriter and daoFactory.
     */
    public ConsolidationResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * The web resource method called by the Web Service to implement the
     * Consolidation of a transaction.
     *
     * @return A DcResp needed for the Consolidation Requester. DcRsp is set to
     *         "0" if OK, "2" if NG, "16" if busy.
     *
     * @See {@link DcRsp}
     */
    @Path("")
    @POST
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final DcRsp consolidate() {
        String functionName = "consolidate";
        String threadName = "Thread" + this.hashCode();
        tp.methodEnter(functionName);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        DcRsp dc = new DcRsp();
        dc.setStatus(NO_POSLOGXML_CONSOLIDATED);

        String poslogXml = "";

        try {
            IConsolidate consolidate = daoFactory.getConsolidationDAO();

            TransactionInfo txinfo = selectPOSLogXML();

            if (null != txinfo) {
                poslogXml = txinfo.getTx();

                if (null != poslogXml && !poslogXml.isEmpty()) {
                    // A transaction should be in consolidated status.
                    // It does not care it has been successful or not
                    dc.setStatus(POSLOGXML_CONSOLIDATED);

                    XmlSerializer<PosLog> posLogSerializer =
                        new XmlSerializer<PosLog>();
                    PosLog poslog = posLogSerializer.unMarshallXml(poslogXml,
                            PosLog.class);
                    consolidate.doPOSLogJournalization(poslog, txinfo);
                } else {
                    tp.println("Transaction has an empty/null POSLog xml.");
                }
            } else {
                tp.println("No transactions to consolidate.");
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    className + functionName,
                    Logger.RES_EXCEP_DAO,
                    threadName + "\nFailed to process consolidation.\n "
                            + ex.getMessage());
        } catch (JAXBException ex) {
            LOGGER.logAlert(
                    progName,
                    className + functionName,
                    Logger.RES_EXCEP_JAXB,
                    threadName + "\nFailed to process consolidation.\n "
                            + ex.getMessage());
            Snap.SnapInfo info = snap.write("Failed to unMarshall POSLog xml",
                    poslogXml);
            LOGGER.logSnap(progName, className + functionName,
                    "Output error transaction data to snap file", info);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    className + functionName,
                    Logger.RES_EXCEP_GENERAL,
                    threadName + "\nFailed to process consolidation.\n "
                            + ex.getMessage());
            Snap.SnapInfo info = snap.write("Failed to unMarshall POSLog xml",
                    poslogXml);
            LOGGER.logSnap(progName, className + functionName,
                    "Output error transaction data to snap file", info);
        } finally {
            tp.methodExit(dc);
        }
        return dc;
    }

    /**
     * The private method that decide when and which POSLog XML should be
     * selected.
     *
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    private void dbSelectScheduleCall() throws DaoException {
        DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IConsolidate consolidate = dao.getConsolidationDAO();
        CurDbInfo curDBInfo = (CurDbInfo) context
                .getAttribute(GlobalConstant.CURDBINFO);

        // Is it necessary to refresh the PosLog Xml Selection this time?
        // The CURDBINFO.seqnum will be assigned back to 0 for every 30 minutes
        if (curDBInfo.isRecoveryTime()) {
            curDBInfo.setSeqNum("0");
        }

        context.setAttribute(GlobalConstant.TRANSACTIONINFO,
                consolidate.selectPOSLogXML(curDBInfo.getSeqNum()));
    }

    /**
     * The private helper method that selects a specific POSLog XML for
     * every(MultiThreaded) running process.
     *
     * @return the PosLOG xml in string format.
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    @SuppressWarnings("unchecked")
    private TransactionInfo selectPOSLogXML() throws DaoException {
        TransactionInfo tranInfo;
        CurDbInfo curDBInfo = (CurDbInfo) context
                .getAttribute(GlobalConstant.CURDBINFO);

        // Get a PosLOG xml
        synchronized ((List<TransactionInfo>) context
                .getAttribute(GlobalConstant.TRANSACTIONINFO)) {
            if (((List<TransactionInfo>) context
                    .getAttribute(GlobalConstant.TRANSACTIONINFO)).isEmpty()) {

                dbSelectScheduleCall();

                if (((List<TransactionInfo>) context
                        .getAttribute(GlobalConstant.TRANSACTIONINFO))
                        .isEmpty()) {
                    curDBInfo.setSeqNum("0");
                    tranInfo = null;
                } else {
                    tranInfo = (((List<TransactionInfo>) context
                            .getAttribute(GlobalConstant.TRANSACTIONINFO))
                            .remove(0));
                }
            } else {
                tranInfo = ((List<TransactionInfo>) context
                        .getAttribute(GlobalConstant.TRANSACTIONINFO))
                        .remove(0);
            }

            if (tranInfo == null) {
                return null;
            }

            curDBInfo.setSeqNum(tranInfo.getSeqNum());
            context.setAttribute(GlobalConstant.CURDBINFO, curDBInfo);
        }

        return tranInfo;
    }
}
