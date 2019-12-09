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

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.customeraccount.model.LoyaltyAccountInfo;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * CustomerAccountResource Class is a Web Resource which support
 * MobilePOS Customer Account processes.
 */
@Path("/customeraccount")
@Api(value="/customeraccount", description="顧客アカウント関連API")
public class CustomerAccountResource {
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    @Context
    private ServletContext servletContext;
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
    @ApiOperation(value="顧客情報の取得", response=Customer.class)
    @ApiResponses(value={})
    public final Customer getCustomerByID(@ApiParam(name="customerid", value="顧客番号") @PathParam("customerid")
                          final String customerid,
                          @ApiParam(name="deviceno", value="デバイス番号") @FormParam("deviceno") final String deviceNo,
                          @ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo) {

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
    
	@Path("/getLoyaltyAccountList")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "売掛、売上外の顧客検索する。", response = LoyaltyAccountInfo.class)
	@ApiResponses(value={
	        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
	    })
	public final LoyaltyAccountInfo getLoyaltyAccountInfo(
			@ApiParam(name = "companyId", value = "会社コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "storeId", value = "店番号") @FormParam("storeId") final String storeId,
			@ApiParam(name = "connName", value = "顧客名") @FormParam("connName") final String connName,
			@ApiParam(name = "connKanaName", value = "顧客カナ名") @FormParam("connKanaName") final String connKanaName,
			@ApiParam(name = "connTel", value = "顧客電話番号") @FormParam("connTel") final String connTel) {

		tp.methodEnter("getLoyaltyAccountList");
		tp.println("companyId", companyId).println("storeId", storeId).println("connName", connName)
				.println("connKanaName", connKanaName).println("connTel", connTel);

		LoyaltyAccountInfo loyaltyAccountInfo = new LoyaltyAccountInfo();
		if (StringUtility.isNullOrEmpty(companyId, storeId)) {
			loyaltyAccountInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			tp.methodExit("Parameter[s] is empty or null.");
			return loyaltyAccountInfo;
		}
		
		try {
			ICustomerDAO customerDAO;
			customerDAO = sqlServerDAO.getCustomerDAO();

			if (!StringUtility.isNullOrEmpty(connName) || !StringUtility.isNullOrEmpty(connKanaName)
					|| !StringUtility.isNullOrEmpty(connTel)) {
				loyaltyAccountInfo = customerDAO.getLoyaltyAccountInfo(companyId, storeId, connName, connKanaName,
						connTel);
			}

		} catch (DaoException ex) {
			LOGGER.logAlert("CustAcnt", "CustomerAccountResource.getLoyaltyAccountInfo", Logger.RES_EXCEP_DAO,
					"Failed to get getLoyaltyAccountInfo: \n" + ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert("CustAcnt", "CustomerAccountResource.getLoyaltyAccountInfo", Logger.RES_EXCEP_GENERAL,
					"Failed to get getLoyaltyAccountInfo: \n" + ex.getMessage());
		} finally {
			tp.methodExit(loyaltyAccountInfo);
		}
		return loyaltyAccountInfo;
	}
}
