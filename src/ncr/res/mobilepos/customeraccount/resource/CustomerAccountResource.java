/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* CustomerAccountResource
*
* CustomerAccountResource Class is a Model representation and mainly
* holds the Customer's Information.
*
* Campos, Carlos
*/
package ncr.res.mobilepos.customeraccount.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

/**
 * CustomerAccountResource Class is a Web Resource which support
 * MobilePOS Customer Account processes.
 */
@Path("/customeraccount")
public class CustomerAccountResource {
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Default Constructor.
     */
    public CustomerAccountResource() {
        this.sqlServerDAO = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
    /**
     * The DAO Factory for Customer Account.
     */
    private DAOFactory sqlServerDAO;
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Set the DaoFactory of the ItemResource.
     * @param daoFactory Represents factory for dao object.
     */
    public final void setDaoFactory(final DAOFactory daoFactory) {
        this.sqlServerDAO = daoFactory;
    }

    /**
     * Retrieves a single customer.
     * @param customerid   The Customer ID
     * @param deviceNo     Device Number
     * @param operatorNo   Operator's operator number
     * @return        The Customer Data Information
     */
    @Path("/{customerid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public final Customer getCustomerByID(@PathParam("customerid")
                          final String customerid,
                          @FormParam("deviceno") final String deviceNo,
                          @FormParam("operatorno") final String operatorNo) {

        tp.methodEnter("getCustomerByID");
        tp.println("CustomerID", customerid).println("DeviceNo", deviceNo).
            println("OperatorNo", operatorNo);

        Customer customerData = new Customer();
        try {
            ICustomerDAO customerDAO;
            customerDAO = sqlServerDAO.getCustomerDAO();

            customerData = customerDAO.getCustomerByID(customerid);
        } catch (DaoException ex) {
            LOGGER.logAlert("CustAcnt",
                    "CustomerAccountResource.getCustomerByID",
                     Logger.RES_EXCEP_DAO,
                    "Failed to get Customer Information: \n" + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert("CustAcnt",
                    "CustomerAccountResource.getCustomerByID",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get Customer Information: \n" + ex.getMessage());
        } finally {
            tp.methodExit(customerData);
        }
        return customerData;
    }
}
