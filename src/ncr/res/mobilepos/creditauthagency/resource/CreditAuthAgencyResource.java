/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthAgencyResource
 *
 * CreditAuthAgencyResource Class is a Web Resource which support
 * POS for Credit Authorization request.
 *
 * jd185128
 */

package ncr.res.mobilepos.creditauthagency.resource;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.creditauthagency.dao.IExtCARequestDAO;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResps;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.constants.PosLogRespConstants;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;

/**
 * CreditAuthAgencyResource Class is a Web Resource which support
 * POS for Credit Authorization request.
 */
@Path("/creditauthagency")
public class CreditAuthAgencyResource {
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
	/**
     * Servlet context holder.
     */
    @Context private ServletContext context;    
	/** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * CreditAuthAgencyResource progname.
     */
    private String progName = "CAAgency";
    /**
     * CreditAuthAgencyResource classname.
     */
    private String className = "CreditAuthAgencyResource.";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Constructor.
     */
    public CreditAuthAgencyResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * @deprecated
     * Service resource for recieving uploads for credit
     * authorization request.
     * @param posLogXml poslog xml of transaction to authorize
     * @return PosLogResp
     * @produces MediaType.APPLICATION_XML
     * @consumes application/x-www-form-urlencoded
     * @action POST
     * @path /creditauthagency/creditauthrequestold
     */
    @Deprecated
    @Path("/creditauthrequestold")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final PosLogResp uploadCARequest(
            @FormParam("poslogxml")final String posLogXml) {

        String functionName = className + "uploadCARequest";
        //Begin Log
        tp.methodEnter("uploadCARequest");
        tp.println("PosLogXML", posLogXml);

        PosLogResp posLogResponse = new PosLogResp();
        

        try {
			DAOFactory daoFactory = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IExtCARequestDAO extCAReqDAO = daoFactory.getExtCAReqDAO();

            // Unmarshall xml string to PosLog object
            XmlSerializer<PosLog> xml = new XmlSerializer<PosLog>();
            PosLog posLog = (PosLog) xml.unMarshallXml(posLogXml, PosLog.class);

            String status = (extCAReqDAO.saveExtCARequest(posLog)
                    >= SQLResultsConstants.ONE_ROW_AFFECTED)
                                ? PosLogRespConstants.NORMAL_END
                                        : PosLogRespConstants.ERROR_END_1;

            posLogResponse.setStatus(status);

        } catch (DaoException ex) {
            //Is the uploaded CA Request was a duplicate?
            //If yes, return the CA Upload as sucessful
            final int sqlErrPrimaryKeyConstraint = 2627;
            if (ex.getCause() instanceof SQLException
                    && ((SQLException) ex.getCause()).getErrorCode()
                    == sqlErrPrimaryKeyConstraint) {
                posLogResponse.setStatus(PosLogRespConstants.NORMAL_END);
                posLogResponse.setNCRWSSResultCode(0);
                tp.println("Upload CA request successful.");
            } else {
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                        "Failed to process external CA request.\n"
                        + ex.getMessage());
                posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
                posLogResponse.setMessage(ex.getMessage());
                posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (JAXBException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_JAXB,
                    "Failed to process external CA request.\n"
                    + ex.getMessage());
            posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
            posLogResponse.setMessage("Invalid POSLOg XML");
            posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_JAXB);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to process external CA request.\n"
                    + ex.getMessage());
            posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
            posLogResponse.setMessage(ex.getMessage());
            posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(posLogResponse);
        }
        return posLogResponse;
    }
    /**
     * Search a Credit Authorization Request by givin the storeid, terminal id
     * and business date.
     *
     * @param storeid        The Store ID
     * @param termid        The Terminal ID
     * @param txdate        The BusinessDay Date
     * @return CreditAuthorizationResps
     * @produces MediaType.APPLICATION_XML
     * @consumes MediaType.APPLICATION_FORM_URLENCODED
     * @action GET
     * @path /creditauthagency/creditauthrequest/search
     */
    @GET
    @Produces({MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @Path("/creditauthrequest/search")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final CreditAuthorizationResps searchCreditRequest(
            @QueryParam("storeid")final String storeid,
            @QueryParam("termid")final String termid,
            @QueryParam("txdate")final String txdate) {

        tp.methodEnter("searchCreditRequest");
        tp.println("StoreID", storeid).println("TerminalID", termid).
            println("TransactionDate", txdate);

        CreditAuthorizationResps creditResps = new CreditAuthorizationResps();

        try {
            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IExtCARequestDAO extCAReqDAO = daoFactory.getExtCAReqDAO();

            List<CreditAuthorizationResp> caRespList =
                extCAReqDAO.getExtCARequest(storeid, termid, txdate);
            creditResps.setCreditAutrhorizationResp(caRespList);

            if (creditResps.getCreditAutrhorizationResp().isEmpty()) {
                creditResps = new CreditAuthorizationResps();
                creditResps.setNCRWSSResultCode(
                        ResultBase.RESEXTCA_ERROR_NOTFOUND);
                tp.println("External CA data not found.");
            }
        } catch (DaoException ex) {
            LOGGER.logAlert("CAAgency", "searchCreditRequest",
                    Logger.RES_EXCEP_DAO,
                    "Failed to search a Credit Authorization Request.\n"
                    + ex.getMessage());
            creditResps.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert("CAAgency", "searchCreditRequest",
                    Logger.RES_EXCEP_GENERAL,
            "Failed to search a Credit Authorization Request.\n"
                    + ex.getMessage());
            creditResps.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);

        } finally {
            tp.methodExit(creditResps);
        }

        return creditResps;
    }

    /**
     * Get a  CA Request Status
     * This method is called by the POS terminal to identify if
     * the CA request has been successfully authorized.
     *
     * @param storeid        The StoreId of the device
     * @param termid        The POS Terminal ID to check
     * @param txid            The Trasaction ID to Check
     * @param txdate        The date of the Transaction
     * @return CreditAuthorizationResp
     * @produces MediaType.APPLICATION_XML
     * @consumes MediaType.APPLICATION_FORM_URLENCODED
     * @action GET
     * @path /creditauthagency/creditauthrequest/status
     */
    @GET
    @Path("/creditauthrequest/status")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_XML + ";charset=UTF-8")
    public final CreditAuthorizationResp getCARequestStatus(
            @QueryParam("storeid")final String storeid,
            @QueryParam("termid")final String termid,
            @QueryParam("txid")final String txid,
            @QueryParam("txdate")final String txdate) {

        String functionName = className + "getCARequestStatus";
        tp.methodEnter("getCARequestStatus");
        tp.println("StoreID", storeid).println("TerminalID", termid).
            println("TransactionID", txid).println("TransactionDate", txdate);

        CreditAuthorizationResp response = new CreditAuthorizationResp();

        try {
            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IExtCARequestDAO extCAReqDAO = daoFactory.getExtCAReqDAO();
            response =
                extCAReqDAO.getExtCARequestStatus(
                        storeid, termid, txid, txdate);
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to search a Credit Authorization Request.\n"
                    + ex.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to search a Credit Authorization Request.\n"
                    + ex.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(response);
        }

        return response;
    }
    /**
     * Upload CA request with PastelPort data.
     *
     * @param pastelportresp The pastelport response string
     * @param storeid        The StoreId of the device
     * @param posterminalid  The POS Terminal ID to check
     * @param txid            The Trasaction ID to Check
     * @param txdate        The date of the Transaction
     * @param operatorcode  Operator code
     * @return PosLogResp
     * @produces MediaType.APPLICATION_XML
     * @consumes MediaType.APPLICATION_FORM_URLENCODED
     * @action POST
     * @path /creditauthagency/creditauthextrequest
     */
    @Path("/creditauthrequest")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public PosLogResp uploadExtCARequest(
            @FormParam("pastelportdata")final PastelPortResp pastelportresp,
            @FormParam("storeid")final String storeid,
            @FormParam("posterminalid")final String posterminalid,
            @FormParam("txdate")final String txdate,
            @FormParam("txid")final String txid,
            @FormParam("operatorno")final String operatorcode) {

        String functionName = className + "uploadExtCARequest";
        tp.methodEnter("uploadExtCARequest");
        tp.println("StoreID", storeid).println("POSTerminalID", posterminalid).
            println("TransactionDate", txdate).println("TransactionID", txid).
            println("OperatorCode", operatorcode);

        PosLogResp posLogResponse = new PosLogResp();       
        // type = 1, means credit authorization from POS.
        int type = 1;
        // The default status of credit authorization which is from POS is 0.
        String defaultStatus = "0";
        try {
        	DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IExtCARequestDAO extCAReqDAO = daoFactory.getExtCAReqDAO();

            String status = (extCAReqDAO.saveExtCARequest(pastelportresp,
                    storeid, posterminalid, txdate, txid, type, defaultStatus,
                        operatorcode) >= SQLResultsConstants.ONE_ROW_AFFECTED)
                        ? PosLogRespConstants.NORMAL_END
                                : PosLogRespConstants.ERROR_END_1;

            posLogResponse.setStatus(status);

        } catch (DaoException ex) {
            //Is the uploaded CA Request was a duplicate?
            //If yes, return the CA Upload as sucessful
            final int sqlErrPrimaryKeyConstraint = 2627;
            if (ex.getCause() instanceof SQLException
                    && ((SQLException) ex.getCause()).getErrorCode()
                    == sqlErrPrimaryKeyConstraint) {
                posLogResponse.setStatus(PosLogRespConstants.NORMAL_END);
                tp.println("Upload CA request successful.");
            } else {
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                        "Failed to process external CA request with pastelport "
                        + "response.\n" + ex.getMessage());
                posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
                posLogResponse.setMessage(ex.getMessage());
            }
            posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (JAXBException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_JAXB,
                    "Failed to process external CA request with pastelport"
                    + " response.\n" + ex.getMessage());
            posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
            posLogResponse.setMessage("Invalid POSLOg XML");
            posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_JAXB);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to process external CA request with pastelport"
                    + " response.\n" + ex.getMessage());
            posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
            posLogResponse.setMessage(ex.getMessage());
            posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(posLogResponse);
        }

        return posLogResponse;
    }
}
