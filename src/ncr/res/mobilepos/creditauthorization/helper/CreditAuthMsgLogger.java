/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthMsgLogger
 *
 * Class Handler for logging a given Credit Authorization
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.helper;

import javax.xml.bind.JAXBException;
import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.creditauthorization.constant.CreditAuthRespConstants;
import ncr.res.mobilepos.creditauthorization.dao.ICreditDAO;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.XmlSerializer;

/**
 * CreditAuthMsgLogger is a helper class that implements the Credit
 * Authorization of a transaction.
 */
public class CreditAuthMsgLogger {

    /**
     * The private member variable that is used as the DAO Factory for the
     * class.
     */
    private DAOFactory daoFactory;
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp;
    
    public CreditAuthMsgLogger () {
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Sets the DAO Factory of the class.
     *
     * @param daoFactry
     *            The new Dao Factory
     */
    public final void setDAOFactory(final DAOFactory daoFactry) {
        this.daoFactory = daoFactry;
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Lock/Release a Credit Authorization Request.
     *
     * @param credit
     *            The Credit to be Lock/Request
     * @param toLock
     *            The Flag that tell if the Credit should be lock or released
     * @return The number of rows affected
     * @throws DaoException
     *             the dao exception
     * @throws JAXBException
     *             the jAXB exception
     */
    public final int lock(final CreditAuthorization credit,
            final boolean toLock) throws DaoException, JAXBException {

        tp.methodEnter("lock");
        String threadname = "Thread" + this.hashCode();
        String toLockStr = "";
        if (toLock) {
            toLockStr = "Lock";
        } else {
            toLockStr = "Release";
        }
        tp.println("+CreditAuthMsgLogger.lock - "
                + toLockStr + " for CA start for " + threadname);

        // Is there any CreditAuthorization Object?
        // If no, stop the process and return null
        if (null == credit) {
			LOGGER.logAlert("CAMsgLgr", "lock", Logger.RES_EXCEP_JAXB,
					"Failed to lock CreditAuthorization.\n"
							+ " CreditAuthorization is null. "
							+ "\nCredit Authorization Parse Error.");
            throw new JAXBException("Credit " + toLockStr
                    + " has Parse Error.");
        }

        if (null == daoFactory) {
            setDAOFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        }

        ICreditDAO tenderDAO = daoFactory.getCreditDAO();

        // Ask the ICreditDAO to save the Credit Authorization
        int nResult = tenderDAO.lockOrReleaseCARequest(credit, toLock);

        tp.println("-CreditAuthMsgLogger.lock - "
                + toLockStr + "for CA end for " + threadname);
        tp.methodExit();
        return nResult;
    }

    /**
     * Implements the Credit Authorization of a transaction.
     *
     * @param creditAuthorizationXml
     *            The Credit Authorization Xml.
     * @param fromExtCA
     *            The flag that tells if the CA Request if from External POS
     * @return CreditAuthorizationResp The Credit Authorization Response.
     * @throws JAXBException
     *             The Exception thrown when the Credit Authorization xml can
     *             not be parsed.
     * @throws DaoException
     *             The Exception thrown when Credit Authorization fail.
     * @see CreditAuthorizationResp
     */
    public final CreditAuthorizationResp log(
            final String creditAuthorizationXml, final boolean fromExtCA)
            throws JAXBException, DaoException {
        // Begin log
        tp.methodEnter("log");
        String threadname = "Thread" + this.hashCode();
        tp.println("+CreditAuthMsgLogger.log - log for CA start for "
                + threadname);

        XmlSerializer<CreditAuthorization> xmlTmpl =
            new XmlSerializer<CreditAuthorization>();

        CreditAuthorization creditAuth = xmlTmpl.unMarshallXml(
                creditAuthorizationXml, CreditAuthorization.class);

        // Is there any CreditAuthorization Object?
        // If no, stop the process and return null
        if (null == creditAuth) {
			LOGGER.logAlert("CAMsgLgr", "log", Logger.RES_EXCEP_JAXB,
					"Failed to log CreditAuthorization.\n"
							+ " CreditAuthorization is null. "
							+ "\nCredit Authorization Parse Error.");
            throw new JAXBException("Credit Authorization Parse Error.");
        }

        // transact to DataBase
        CreditAuthorizationResp creditAuthResp = null;
        if (fromExtCA) {
            creditAuthResp = transactExtCreditAuthorization(creditAuth,
                    creditAuthorizationXml);
        } else {
            creditAuthResp = transactCreditAuthorization(creditAuth,
                        creditAuthorizationXml);
        }

        tp.println("-CreditAuthMsgLogger.log - log for CA end for " + threadname
                + "\nCreditAuthResp was " + creditAuth);
        tp.methodExit();
        return creditAuthResp;
    }

    /**
     * Implements the Credit Authorization of a transaction NOT form External CA
     * Request.
     *
     * @param creditAuthorizationXml
     *            The Credit Authorization Xml.
     * @return CreditAuthorizationResp The Credit Authorization Response.
     * @throws JAXBException
     *             The Exception thrown when the Credit Authorization xml can
     *             not be parsed.
     * @throws DaoException
     *             The Exception thrown when Credit Authorization fail.
     * @see CreditAuthorizationResp
     */
    public final CreditAuthorizationResp log(
            final String creditAuthorizationXml)
                throws JAXBException, DaoException {
        return log(creditAuthorizationXml, false);
    }

    /**
     * A private method that saves the Credit Information in the database.
     *
     * @param creditAuth
     *            The Credit Authorization Information
     * @param creditAuthXml
     *            The Credit Authorization XML
     * @return The Credit Authorization Response. Return
     * @throws DaoException
     *             the dao exception
     */
    private CreditAuthorizationResp transactCreditAuthorization(
            final CreditAuthorization creditAuth, final String creditAuthXml)
            throws DaoException {
        // Begin Log
        tp.methodEnter("transactCreditAuthorization");
        String threadname = "Thread" + this.hashCode();
        tp.println("+CreditAuthMsgLogger.transactCreditAuthorization"
                + " - transact CA start for " + threadname);

        CreditAuthorizationResp creditAuthResp = null;

        if (null == daoFactory) {
            setDAOFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        }

        ICreditDAO tenderDAO = daoFactory.getCreditDAO();

        // Ask the ICreditDAO to save the Credit Authorization
        int nResult = tenderDAO.saveCreditAuthorization(creditAuth,
                creditAuthXml);

        String status = "";
        if (SQLResultsConstants.ONE_ROW_AFFECTED == nResult) {
            status = CreditAuthRespConstants.STATUS_NORMAL_END;
        } else {
            status = CreditAuthRespConstants.STATUS_ERROR_END;
        }

        // The codes are therefore subject to change
        creditAuthResp = new CreditAuthorizationResp();
        creditAuthResp.setApprovalcode(String.valueOf(creditAuth.getTxid()));
        creditAuthResp.setStatus(status);
        creditAuthResp.setCreditstatus(CreditAuthRespConstants.CREDITSTATUS);
        creditAuthResp
                .setErrorcode(CreditAuthRespConstants.ERRORCODE_NORMAL_END);

        tp.println("-CreditAuthMsgLogger.transactCreditAuthorization"
                + " - transact CA end for " + threadname);
        tp.methodExit();

        return creditAuthResp;
    }

    /**
     * A private method that saves the Credit Information in the database.
     *
     * @param creditAuth
     *            The Credit Authorization Information
     * @param creditAuthXml
     *            The Credit Authorization XML
     * @return The Credit Authorization Response.
     * @throws DaoException
     *             The exception thrown when the Method failed.
     */
    private CreditAuthorizationResp transactExtCreditAuthorization(
            final CreditAuthorization creditAuth, final String creditAuthXml)
            throws DaoException {
        // Begin Log
        tp.methodEnter("transactExtCreditAuthorization");
        String threadname = "Thread" + this.hashCode();
        tp.println("+CreditAuthMsgLogger.transactCreditAuthorization(overload)"
                + " - transact ExtCA start for " + threadname);

        CreditAuthorizationResp creditAuthResp = null;

        if (null == daoFactory) {
            setDAOFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        }

        ICreditDAO tenderDAO = daoFactory.getCreditDAO();

        // Ask the ICreditDAO to save the Credit Authorization
        int nResult = tenderDAO.saveExtCreditAuthorization(creditAuth,
                creditAuthXml);

        String status = "";
        if (SQLResultsConstants.ONE_ROW_AFFECTED <= nResult) {
            status = CreditAuthRespConstants.STATUS_NORMAL_END;
        } else {
            status = CreditAuthRespConstants.STATUS_ERROR_END;
        }

        // The codes are therefore subject to change
        creditAuthResp = new CreditAuthorizationResp();
        creditAuthResp.setApprovalcode(String.valueOf(creditAuth.getTxid()));
        creditAuthResp.setStatus(status);
        creditAuthResp.setCreditstatus(CreditAuthRespConstants.CREDITSTATUS);

        if (status.equals(CreditAuthRespConstants.STATUS_ERROR_END)) {
            creditAuthResp
                    .setErrorcode(CreditAuthRespConstants.ERRORCODE_NORMAL_END);
        }

        tp.println("-CreditAuthMsgLogger.transactCreditAuthorization(overload)"
                + " - transact CA end for " + threadname);
        tp.methodExit();
        return creditAuthResp;
    }

    /**
     * Implements the Credit Authorization of a transaction from external
     * source.
     *
     * @param pastelPortResp
     *            the pastel port response
     * @param storeid
     *            the storeid
     * @param posterminalid
     *            the POS terminalid
     * @param txdate
     *            the txdate
     * @param txid
     *            the txid
     * @return CreditAuthorizationResp The CreditAuthorizationResp object
     * @throws DaoException
     *             The Exception thrown when Credit Authorization fail.
     * @see CreditAuthorizationResp
     */
    public CreditAuthorizationResp logExtCA(
            final PastelPortResp pastelPortResp, final String storeid,
            final String posterminalid, final String txdate, final String txid)
            throws DaoException {

        tp.methodEnter("logExtCA");
        String threadname = "Thread" + this.hashCode();
        tp.println("+CreditAuthMsgLogger.logExtCA - log for CA start for "
                + threadname);

        CreditAuthorizationResp creditAuthorizationResp =
            transactExtCreditAuthorization(pastelPortResp, storeid,
                    posterminalid, txdate, txid);

        tp.println("-CreditAuthMsgLogger.logExtCA - log for CA end for "
                + threadname + "\nCreditAuthResp was "
                + creditAuthorizationResp);
        tp.methodExit();
        return creditAuthorizationResp;
    }

    /**
     * A private method that saves the ExternalCreditAuthorization to database.
     *
     * @param pastelPortResp
     *            The PastelPortResponse object
     * @param storeId
     *            The store identifier
     * @param posterminalId
     *            The POS terminal identifier
     * @param txDate
     *            The transaction date
     * @param txId
     *            The transaction identifier
     * @return The CreditAuthorizationResp object
     * @throws DaoException
     *             The exception thrown when the Method failed
     */
    private CreditAuthorizationResp transactExtCreditAuthorization(
            final PastelPortResp pastelPortResp, final String storeId,
            final String posterminalId, final String txDate, final String txId)
            throws DaoException {

        String threadname = "Thread" + this.hashCode();
        tp.methodEnter("transactExtCreditAuthorization");
        tp.println("+CreditAuthMsgLogger.transactExtCreditAuthorization"
                + " - transact ExtCA start for " + threadname);

        if (null == daoFactory) {
            setDAOFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        }

        ICreditDAO tenderDAO = daoFactory.getCreditDAO();

        int nResult = tenderDAO.saveExtCreditAuthorization(pastelPortResp,
                storeId, posterminalId, txDate, txId);

        String status = "";
        if (SQLResultsConstants.ONE_ROW_AFFECTED == nResult) {
            status = CreditAuthRespConstants.STATUS_NORMAL_END;
        } else {
            status = CreditAuthRespConstants.STATUS_ERROR_END;
        }

        CreditAuthorizationResp creditAuthResp = new CreditAuthorizationResp();
        creditAuthResp.setStatus(status);

        tp.println("-CreditAuthMsgLogger.transactExtCreditAuthorization"
                + " - transact CA end for " + threadname);
        tp.methodExit();
        return creditAuthResp;
    }
}
