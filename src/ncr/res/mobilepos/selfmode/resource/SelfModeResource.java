/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ForwardItemListResource
 *
 * Resource for Transfer transactions between smart phone and POS
 *
 */
package ncr.res.mobilepos.selfmode.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.selfmode.dao.ISelfModeDAO;
import ncr.res.mobilepos.selfmode.model.SelfMode;
import ncr.res.mobilepos.selfmode.model.SelfModeInfo;

/**
 * Transfer transactions between smart phone and POS.
 */
@Path("/selfmode")
@Api(value = "/selfmode", description = "セルフモード状態")
public class SelfModeResource {
	/**
	 * The IOWriter for the Log.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;
	/**
	 * The program name.
	 */
	private static final String PROG_NAME = "Selfmode";

	@Context
	private ServletContext context; // to access the web.xml

	/**
	 * Constructor.
	 */
	public SelfModeResource() {
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * Sets the DaoFactory of the ForwardItemListResource to use the DAO methods.
	 *
	 * @param daofactory - The new value for the DAO Factory
	 */
	public final void setDaoFactory(final DAOFactory daofactory) {
		// no implementation
	}


	@Path("/updateStatus")
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value = "セルフモード状態通知", response = ResultBase.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_SQL, message = "データ挿入失敗"), })
	public final ResultBase updateStatus(
			@ApiParam(name = "CompanyId", value = "企業コード") @FormParam("CompanyId") final String companyId,
			@ApiParam(name = "RetailStoreId", value = "店舗番号") @FormParam("RetailStoreId") final String retailStoreId,
			@ApiParam(name = "WorkstationId", value = "レジ番号") @FormParam("WorkstationId") final String workstationId,
			@ApiParam(name = "Training", value = "トレーニングモード状態（テーブル定義参照）") @FormParam("Training") final int training,
			@ApiParam(name = "Status", value = "セルフモード状態（テーブル定義参照）") @FormParam("Status") final int status,
			@ApiParam(name = "Detail", value = "セルフモード状態詳細（テーブル定義参照）") @FormParam("Detail") final int detail,
			@ApiParam(name = "Printer", value = "プリンター状態（テーブル定義参照）") @FormParam("Printer") final int printer,
			@ApiParam(name = "CashChanger", value = "釣銭機状態（テーブル定義参照）") @FormParam("CashChanger") final int cashChanger,
			@ApiParam(name = "CashChangerCount", value = "釣銭機在高 （テーブル定義参照）") @FormParam("CashChangerCount") final String cashChangerCount,
			@ApiParam(name = "CashChangerCountStatus", value = "釣銭機在高状態 （テーブル定義参照）") @FormParam("CashChangerCountStatus") final String cashChangerCountStatus,
			@ApiParam(name = "Message", value = "通知文言") @FormParam("Message") final String message,
			@ApiParam(name = "Alert", value = "警告フラグ") @FormParam("Alert") final int alert,
			@ApiParam(name = "UpdateDateTime", value = "システム日時") @FormParam("UpdateDateTime") final String updateDateTime) {

		tp.methodEnter("updateStatus");
		tp.println("CompanyId", companyId).println("RetailStoreId", retailStoreId)
				.println("WorkstationId", workstationId).println("Status", status).println("Training", training)
				.println("Detail", detail).println("Printer", printer).println("CashChanger", cashChanger)
				.println("CashChangerCount", cashChangerCount).println("CashChangerCountStatus", cashChangerCountStatus)
				.println("Message", message).println("Alert", alert).println("UpdateDateTime", updateDateTime);
		SelfMode selfMode = new SelfMode();

		selfMode.setCompanyId(companyId);
		selfMode.setRetailStoreId(retailStoreId);
		selfMode.setWorkstationId(workstationId);
		selfMode.setTraining(training);
		selfMode.setStatus(status);
		selfMode.setDetail(detail);
		selfMode.setPrinter(printer);
		selfMode.setCashChanger(cashChanger);
		selfMode.setCashChangerCount(cashChangerCount);
		selfMode.setCashChangerCountStatus(cashChangerCountStatus);
		selfMode.setMessage(message);
		selfMode.setAlert(alert);
		selfMode.setUpdateDateTime(updateDateTime);

		ResultBase result = null;
		try {
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			ISelfModeDAO selfModeDAO = sqlServer.getISelfModeDAO();
			result = selfModeDAO.updateStatus(selfMode);
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, "SelfModeResource.updateStatus", Logger.RES_EXCEP_DAO,
					"Failed to process updateStatus.\n" + e.getMessage());
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, "SelfModeResource.updateStatus", Logger.RES_EXCEP_GENERAL,
					"Failed to process updateStatus.\n" + e.getMessage());
		} finally {
			tp.methodExit(result);
		}
		return result;
	}


	@Path("/getStatus")
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value = "セルフモード状態取得", response = SelfModeInfo.class)
	public final SelfModeInfo getStatus(
			@ApiParam(name = "CompanyId", value = "企業コード") @FormParam("CompanyId") final String companyId,
			@ApiParam(name = "RetailStoreId", value = "店舗番号") @FormParam("RetailStoreId") final String retailStoreId,
			@ApiParam(name = "WorkstationId", value = "レジ番号") @FormParam("WorkstationId") final String workstationId) {

		tp.methodEnter("getStatus");
		tp.println("CompanyId", companyId).println("RetailStoreId", retailStoreId).println("WorkstationId",
				workstationId);

		SelfModeInfo result = null;
		try {
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			ISelfModeDAO selfModeDAO = sqlServer.getISelfModeDAO();
			result = selfModeDAO.getStatus(companyId, retailStoreId, workstationId);
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, "SelfModeResource.getStatus", Logger.RES_EXCEP_DAO,
					"Failed to process getStatus.\n" + e.getMessage());
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, "SelfModeResource.getStatus", Logger.RES_EXCEP_GENERAL,
					"Failed to process getStatus.\n" + e.getMessage());
		} finally {
			tp.methodExit(result);
		}
		return result;
	}
}
