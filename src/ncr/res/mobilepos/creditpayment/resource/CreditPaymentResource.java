/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* CreditPaymentResource
*
* CreditPaymentResource Class is a Model representation and mainly
* holds the Credit Payments Information.
*
* Rosales, Vener
*/
package ncr.res.mobilepos.creditpayment.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditpayment.dao.ICreditPaymentDAO;
import ncr.res.mobilepos.creditpayment.model.CreditPaymentDataList;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

@Path("/creditpayment")
public class CreditPaymentResource {
	 /**
     * context.
     */
    @Context
    private ServletContext context;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The DAO Factory for Customer Account.
     */
    private DAOFactory sqlServerDAO;
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * constructor.
     */
    public CreditPaymentResource() {
        this.sqlServerDAO = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
   
    /**
     * Retrieves a single credit payment.
     * @return        The Credit Payment Data Information
     */
    @Path("/getCreditPayment")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final CreditPaymentDataList getCreditPayment() {

        tp.methodEnter("getCreditPayment");

        CreditPaymentDataList creditpayment = new CreditPaymentDataList();
        try {

            ICreditPaymentDAO creditpaymentDAO =
                    sqlServerDAO.getCreditPaymentDAO();
            creditpayment = creditpaymentDAO.getCreditPayment();

        } catch (DaoException ex) {
            LOGGER.logAlert("CreditPayment",
                    "CreditPaymentResouce.getCreditPayment",
                     Logger.RES_EXCEP_DAO,
                    "Failed to get Credit Payment Information: \n"
                     + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert("CreditPayment",
                    "CreditPaymentResouce.getCreditPayment",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get Credit Payment Information: \n"
                    + ex.getMessage());
        } finally {
            tp.methodExit(creditpayment);
        }
        return creditpayment;
    }
}
