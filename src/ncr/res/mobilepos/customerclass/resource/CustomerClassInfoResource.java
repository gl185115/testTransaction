package ncr.res.mobilepos.customerclass.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customerclass.dao.ICustomerClassInfoDAO;
import ncr.res.mobilepos.customerclass.model.CustomerClassInfoList;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

@Path("/customerclass")
@Api(value="/customerclass", description="客層API")
public class CustomerClassInfoResource {
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	private Trace.Printer tp;
	private DAOFactory daoFactory;
	private static final String PROG_NAME = "CustomerClassInfoResource";
	@Context
	private ServletContext servletContext;

	public CustomerClassInfoResource() {
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * Get customer class info list
	 *
	 * @param company
	 *            id
	 * @param store
	 *            id
	 * @return customer class info list
	 */
	@Path("/getcustomerclassinfo")
	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value="客層情報を取得する", response=CustomerClassInfoList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    })
	public final CustomerClassInfoList getCustomerClassInfo(@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
			@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId);
		CustomerClassInfoList customerInfo = new CustomerClassInfoList();

		if (StringUtility.isNullOrEmpty(companyId, storeId)) {
			customerInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			tp.methodExit(customerInfo.toString());
			return customerInfo;
		}

		try {
			ICustomerClassInfoDAO customerInfoDao = daoFactory.getCustomerClassInfoDAO();
			customerInfo.setCustomerClassInfoList(customerInfoDao.getCustomerClassInfo(companyId, storeId));
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get customer class info for companyId.", ex);
			customerInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			customerInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			customerInfo.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get customer class info for companyId.", ex);
			customerInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			customerInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			customerInfo.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(customerInfo);
		}
		return customerInfo;
	}
}
