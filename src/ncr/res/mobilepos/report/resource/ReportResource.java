/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportResource
 *
 * Web Service that provides Reports for the Mobile POS
 *
 * Meneses, Chris Niven
 */

package ncr.res.mobilepos.report.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.dao.IReportDAO;
import ncr.res.mobilepos.report.model.DailyReportItems;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.model.TotalAmount;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * ReportResource class is a web resource which provides support for reports
 * creation.
 */
@Path("/report")
@Api(value="/report", description="レポートAPI")
public class ReportResource {

    /** The context. */
    @Context
    private ServletContext context;

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * Setter function for the servlet context.
     *
     * @param contextToSet
     *            the new context
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger

    private static final String PROG_NAME = "Report";
    /**
     * constructor.
     */
    public ReportResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Accountancy report info.
     * @param companyid
     *            the company id
     * @param storeid
     *            the store id
     * @param tillid
     *            the till id
     * @param operatorNo
     *            the operator No
     * @param language
     *            the language
     * @return the Accountancy Report
     */
    @Path("/getAccountancyReport")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="項目報告", response=ReportItems.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        })
    public final ReportItems getAccountancyReport(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyid,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeid,
    		@ApiParam(name="tillid", value="ドロワーコード")@QueryParam("tillid") final String tillid,
    		@ApiParam(name="language", value="言葉")@QueryParam("language") final String language ) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeid", storeid).println("tillid", tillid)
                .println("language", language);
        ReportItems result = new ReportItems();
        try {
            if (StringUtility.isNullOrEmpty(storeid)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(companyid, storeid)
                    .getDateSetting();
            String businessDayDate = dateSetting.getToday();
            String storeName = null;
            if (ReportConstants.ALL_STORE.equals(storeid)) {
                storeName = ReportConstants.ALL_STORENAME;
            } else if (ReportConstants.FLAGSHIP_STORE.equals(storeid)) {
                storeName = ReportConstants.FLAGSHIP_STORENAME;
            } else if (ReportConstants.OUTLET_STORE.equals(storeid)) {
                storeName = ReportConstants.OUTLET_STORENAME;
            } else {
                storeName = reportDAO.findStoreName(storeid);
                if (StringUtility.isNullOrEmpty(storeName)) {
                    storeName = "";
                }
            }
            result = reportDAO.generateAccountancyReport(storeid, tillid,
                    businessDayDate, language);
            result.setStoreId(storeid);
            result.setStoreName(storeName);
            result.setTillid(tillid);
            result.setBusinessDayDate(businessDayDate);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyid, storeid);
            result.setBusinessDayDate(businessDayDate);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }

    @Path("/gettotalamount")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="合計金額を得る", response=TotalAmount.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        })
    public final TotalAmount getTotalAmount(
    		@ApiParam(name="storeId", value="店舗番号")@QueryParam("storeId") final String storeId,
    		@ApiParam(name="tillId", value="ドロワーコード")@QueryParam("tillId") final String tillId,
    		@ApiParam(name="businessDate", value="営業日付")@QueryParam("businessDate") final String businessDate) {

    	String functionName = DebugLogger.getCurrentMethodName();

		tp.methodEnter(functionName);
		tp.println("storeid", storeId)
		  .println("tillId", tillId)
		  .println("businessDate", businessDate);

		TotalAmount result = new TotalAmount();

		try{
			if(StringUtility.isNullOrEmpty(storeId, tillId, businessDate)){
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);

				return result;
			}

			DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

			result = reportDAO.getTotalAmount(storeId, tillId, businessDate);

		}catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to get Total Amount.\n" + daoEx.getMessage(), daoEx);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(daoEx.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get Total Amount.\n" + ex.getMessage(), ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(result);
		}
		return result;
    }

    @Path("/getreportitems")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="レポート項目を得る", response=DailyReportItems.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        })
    public final DailyReportItems getReportItems(
    		@ApiParam(name="companyId", value="会社コード")@FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@FormParam("storeId") final String storeId,
    		@ApiParam(name="tillId", value="ドロワーコード")@FormParam("tillId") final String tillId,
    		@ApiParam(name="businessDate", value="営業日付")@FormParam("businessDate") final String businessDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@FormParam("trainingFlag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("tillId", tillId)
          .println("businessDate", businessDate)
          .println("trainingFlag", trainingFlag);

        DailyReportItems result = new DailyReportItems();

        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, businessDate)) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();
            result.setReportItems(reportDAO.getDailyReportItems(companyId, storeId,
            		tillId, businessDate, trainingFlag));
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to get daily report items.\n" + daoEx.getMessage(), daoEx);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to getdaily report items.\n" + ex.getMessage(), ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }

}