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

import java.awt.print.PrinterException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.FormatReportByXML;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.PrinterDrawer;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.networkreceipt.helper.PaperReceiptPrint;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.dao.IReportDAO;
import ncr.res.mobilepos.report.helper.FinancialReportFormatter;
import ncr.res.mobilepos.report.helper.FinancialReportPrint;
import ncr.res.mobilepos.report.helper.FinancialReportPrinter;
import ncr.res.mobilepos.report.model.DailyReportItems;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;
import ncr.res.mobilepos.report.model.ItemMode;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.model.ReportMode;
import ncr.res.mobilepos.report.model.ReportModes;
import ncr.res.mobilepos.report.model.TotalAmount;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;
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

    /**
    * Local Printer.
    */
    private static final String LOCAL_PRINTER = "0";
    /**
    * Local Printer.
    */
    private static final String PRINTER_TAG = "Printer";
    /**
    * Local Printer.
    */
    private static final String PORTS_TAG = "Ports";

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

    // FOR NEW IMPLEMENTATION
    /**
     * Gets the report new.
     * @param companyId
     *            the company id
     * @param reportType
     *            the report type
     * @param deviceNo
     *            the device no
     * @param operatorNo
     *            the operator no
     * @param storeNo
     *            the store no
     * @return the report new
     */
    @Path("/getsalesreport")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="新しい報告書を得る", response=ReportItems.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        })
    public final ReportItems getReportNew(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="reporttype", value="レポートタイプ")@QueryParam("reporttype") final String reportType,
    		@ApiParam(name="operatorno", value="従業員番号")@QueryParam("operatorno") final String operatorNo,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeNo) {

        String threadname = "Thread" + this.hashCode();
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("reportType", reportType)
            .println("operatorNo", operatorNo)
            .println("storeNo", storeNo);

        ReportItems result = new ReportItems();

        try {
            if (StringUtility.isNullOrEmpty(storeNo)
                    || StringUtility.isNullOrEmpty(reportType)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();
            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeNo)
                    .getDateSetting();
            String businessDayDate = dateSetting.getToday();
            String startTime = dateSetting.getEod();
            String storeName = null;
            if (ReportConstants.ALL_STORE.equals(storeNo)) {
                storeName = ReportConstants.ALL_STORENAME;
            } else if (ReportConstants.FLAGSHIP_STORE.equals(storeNo)) {
                storeName = ReportConstants.FLAGSHIP_STORENAME;
            } else if (ReportConstants.OUTLET_STORE.equals(storeNo)) {
                storeName = ReportConstants.OUTLET_STORENAME;
            } else {
                storeName = reportDAO.findStoreName(storeNo);
                if (StringUtility.isNullOrEmpty(storeName)) {
                    storeName = "";
                }
            }
            String operatorName = null;
            operatorName = reportDAO.findOperatorName(operatorNo);
            if (StringUtility.isNullOrEmpty(operatorName)) {
                operatorName = "";
            }
            if (ReportConstants.SALESTYPE_HOURLY.equals(reportType)) {
                result = reportDAO.generateHourlyReportNew(
                        businessDayDate, storeNo, startTime);
            }
            /* グループ別売上表 */
            else if (ReportConstants.SALESTYPE_GROUP.equals(reportType)) {
                result = reportDAO.generateGroupReportNew(businessDayDate,
                        storeNo);
            }
            /* 販売員・単品別売上表 */
            else if (ReportConstants.SALESTYPE_CLERKPRODUCT.equals(reportType)) {
                result = reportDAO.generateClerkProductReportNew(operatorNo,
                        businessDayDate, storeNo);
            }
            /* 店舗別売上表 */
            else if (ReportConstants.SALESTYPE_STORE.equals(reportType)) {
                result = reportDAO.generateStoreReportNew(businessDayDate,
                        storeNo);
            }
            /* 客層別売上表 */
            else if (ReportConstants.SALESTYPE_TGT_MARKET.equals(reportType)) {
                result = reportDAO.generateTargetMarketReportNew(
                        businessDayDate, storeNo);
            }
            /* 販売員別売上表 */
            else if (ReportConstants.SALESTYPE_SALESMAN.equals(reportType)) {
                result = reportDAO.generateSalesManReportNew(operatorNo,
                        businessDayDate, storeNo);
            }
            else {
                 tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                 result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                 result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                 result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                LOGGER.logError(
                        PROG_NAME,
                        functionName,
                        Logger.LOG_MSGID,
                        "-ReportResource.getReport - generating of report "
                                + threadname + "\nResponse is "
                                + result.getMessage());
                result.setBusinessDayDate(businessDayDate);
                return result;
            }
            result.setCompanyId(companyId);
            result.setStoreName(storeName);
            result.setStoreId(storeNo);
            result.setOperatorName(operatorName);
            result.setOperatorId(operatorNo);
            result.setBusinessDayDate(businessDayDate);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);
            result.setBusinessDayDate(businessDayDate);
        }catch (Exception e) {
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
    /**
     * Gets the report new.
     *
     * @param companyId
     *            the company id
     * @param reportType
     *            the report type
     * @param operatorNo
     *            the operator no
     * @param storeNo
     *            the store no
     * @param departmentNo
     *            the departmentno no
     * @return the report new
     */
    @Path("/getsalesreportbydivcode")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="divコードによって売上報告を得る", response=ReportItems.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        })
    public final ReportItems getReportByDivCode(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="reporttype", value="レポートタイプ")@QueryParam("reporttype") final String reportType,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeNo,
    		@ApiParam(name="departmentno", value="部門番号")@QueryParam("departmentno") final String departmentNo){

        String threadname = "Thread" + this.hashCode();
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("reportType", reportType)
            .println("storeNo", storeNo)
            .println("departmentno",departmentNo);

        ReportItems result = new ReportItems();

        try {
            if (StringUtility.isNullOrEmpty(storeNo)
                    || StringUtility.isNullOrEmpty(reportType)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeNo)
                    .getDateSetting();
            String businessDayDate = dateSetting.getToday();
            String storeName = null;
            if(ReportConstants.ALL_STORE.equals(storeNo)){
                storeName = ReportConstants.ALL_STORENAME;
             }
            else if(ReportConstants.FLAGSHIP_STORE.equals(storeNo)){
                storeName = ReportConstants.FLAGSHIP_STORENAME;
             }
            else if(ReportConstants.OUTLET_STORE.equals(storeNo)){
                storeName = ReportConstants.OUTLET_STORENAME;
             }
            else {
                storeName = reportDAO.findStoreName(storeNo);
                if (StringUtility.isNullOrEmpty(storeName)) {
                    storeName = "";
                }
            }

            String dptName = null;
            if (StringUtility.isNullOrEmpty(departmentNo)) {
                dptName = "";
            } else {
                dptName = reportDAO.findDepartmentName(storeNo, departmentNo);
                if (StringUtility.isNullOrEmpty(dptName)) {
                    dptName = "";
                }
            }
            /*部門別売上表*/
            if (ReportConstants.SALESTYPE_DPT.equals(reportType)) {
                 result = reportDAO.generateDepartmentReportNew(
                          businessDayDate, storeNo,departmentNo);
            }
            /*アイテム別売上表*/
            else if (ReportConstants.SALESTYPE_ITEM.equals(reportType)) {
                if (StringUtility.isNullOrEmpty(departmentNo)) {
                    tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                    result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return result;
                }

                result = reportDAO.generateItemReportNew(businessDayDate,
                        storeNo, departmentNo);
            }
            /*Div(部門)・時間帯別売上表*/
            else if (ReportConstants.SALESTYPE_DPT_HOURLY.equals(reportType)) {
                result = reportDAO.generateDivHourlyReportNew(businessDayDate, storeNo, departmentNo);
            }
            else {
                 tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                 result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                 result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                 result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                LOGGER.logError(
                        PROG_NAME,
                        functionName,
                        Logger.LOG_MSGID,
                        "-ReportResource.getReport - generating of report "
                                + threadname + "\nResponse is "
                                + result.getMessage());
                result.setBusinessDayDate(businessDayDate);
                return result;
            }
            result.setCompanyId(companyId);
            result.setStoreName(storeName);
            result.setStoreId(storeNo);
            result.setDepartmentName(dptName);
            result.setDepartmentId(departmentNo);
            result.setBusinessDayDate(businessDayDate);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);
            result.setBusinessDayDate(businessDayDate);
        }catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
        }finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Gets the financial report.
     *
     * @param companyId
     *            the company id
     * @param deviceNo
     *            the device no
     * @param storeNo
     *            the store no
     * @return the financial report
     */
    @Path("/getfinancialreport")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @ApiOperation(value="財務報告を得る", response=FinancialReport.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final FinancialReport getFinancialReport(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="deviceid", value="デバイスコード")@QueryParam("deviceid") final String deviceNo,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeNo) {

        tp.methodEnter("getFinancialReport");
        tp.println("CompanyID", companyId)
            .println("DeviceID", deviceNo)
            .println("StoreID", storeNo);

        FinancialReport result = new FinancialReport();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IReportDAO reportDAO;

        try {
            reportDAO = daoFactory.getReportDAO();
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);

            result = reportDAO.generateFinancialReportNew(deviceNo,
                    businessDayDate, storeNo);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setMessage("Success");
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
        } catch (DaoException e) {
            LOGGER.logAlert("Report", "ReportResource.getReport",
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (SQLException e) {
            LOGGER.logAlert("Report", "ReportResource.getReport",
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("Report", "ReportResource.getReport",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());

        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Gets the drawer financial report.
     *
     * @param companyId
     *            the company id
     * @param printerID
     *            the printer id
     * @param storeNo
     *            the store no
     * @return the drawer financial report
     */
    @Path("/getdrawerfinancialreport")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @ApiOperation(value="財務報告の引き出し", response=DrawerFinancialReport.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final DrawerFinancialReport getDrawerFinancialReport(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="printerid", value="プリンターID")@QueryParam("printerid") final String printerID,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeNo) {

        tp.methodEnter("getDrawerFinancialReport");
        tp.println("companyId", companyId)
            .println("printerID", printerID)
            .println("storeNo", storeNo);

        DrawerFinancialReport result = new DrawerFinancialReport();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IReportDAO reportDAO;

        try {
            reportDAO = daoFactory.getReportDAO();
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);

            result = reportDAO.generateDrawerFinancialReport(printerID,
                    businessDayDate, storeNo);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setMessage("Success");
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
        } catch (DaoException e) {
            LOGGER.logAlert("Report",
                    "ReportResource.getDrawerFinancialReport",
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            result.setMessage(e.getMessage());
        } catch (SQLException e) {
            LOGGER.logAlert("Report",
                    "ReportResource.getDrawerFinancialReport",
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("Report",
                    "ReportResource.getDrawerFinancialReport",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to generate report.\n" + e.getMessage(), e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Print financial report with windows printer driver.
     *
     * @param companyId
     *            the company id
     * @param deviceNo
     *            the device no
     * @param operatorNo
     *            the operator no
     * @param storeNo
     *            the store no
     * @param language
     *            the language
     * @return the result base
     */
    @Path("/printfinancialreport2")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase printFinancialReport2(
            @FormParam("companyid") final String companyId,
            @FormParam("deviceno") final String deviceNo,
            @FormParam("operatorno") final String operatorNo,
            @FormParam("storeno") final String storeNo,
            @FormParam("language") final String language) {

        tp.methodEnter("printFinancialReport2");
        tp.println("companyId", companyId)
            .println("deviceNo", deviceNo)
            .println("operatorNo", operatorNo)
            .println("storeNo", storeNo)
            .println("language", language);

        ResultBase resultBase = new ResultBase();

        // If parameter language is null, use default language
        String sysLanguage = "";
        if (language == null || "".equals(language)) {
            sysLanguage = GlobalConstant.getDefaultLanguage();
        } else {
            sysLanguage = language;
        }

        try {
            // Get the data of Financial Report
            // Get the interface of ReportDAO
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            // Get the business date
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);

            // Get Financial Report
            FinancialReport financialReport = reportDAO.getFinancialReportObj(
                    deviceNo, businessDayDate, storeNo);
            IReceiptDAO receiptDAO = daoFactory.getReceiptDAO();
            String printerName = "";
            printerName = receiptDAO.getPrinterName(storeNo, deviceNo);
            // Print Financial Report
            FinancialReportPrinter reportPrinter = new FinancialReportPrinter(
                    sysLanguage, printerName);
            if (reportPrinter.printFinancialReport(financialReport)) {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
            } else {
                resultBase
                        .setNCRWSSResultCode(ResultBase
                                .RESNETRECPT_ERROR_NOTFOUND);
            }
        } catch (DaoException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_DAO, e.getMessage(), e);
        } catch (SQLException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_IOEXCEPTION);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_SQL, e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_UNSUPPORTEDENCODING);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_ENCODING, e.getMessage(), e);
        } catch (PrinterException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_PRINTEREXCEPTION);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
        } catch (Exception e) {
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_GENERAL);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
        } finally {
            tp.methodExit(resultBase.toString());
        }

        return resultBase;
    }
    /* 1.05 2014.12.22  売上表のPrintを対応  ADD START */
    /**
     * Print Sales Report For Common
     *
     * @param companyid
     *            the company id
     * @param storename
     *            the store name
     * @param businessdate
     *            the businessdate
     * @param reporttype
     *            the report type
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param operatorno
     *            the operator number
     * @param deviceno
     *            the device number
     * @param storeidsearch
     *         the search's storeid
     * @param printerid
     *            the printer id
     * @param totalquantitystr
     *            the total quantity str
     * @param trainingflag
     *            the TrainingModeFlag
     * @param datastr
     *            the print data str
     * @return the result base
     */
    @Path("/printsalesreportforcommon")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="印刷する一般の営業報告書", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミングエラーが発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase printSalesReportForCommon(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="storename", value="店舗名")@FormParam("storename") final String storeName,
    		@ApiParam(name="businessdate", value="営業日")@FormParam("businessdate") final String bussinessDate,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reportType,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeId,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="deviceno", value="装置番号")@FormParam("deviceno") final String deviceNo,
    		@ApiParam(name="storeidsearch", value="店舗番号検索")@FormParam("storeidsearch") final String storeidSearch,
    		@ApiParam(name="printrid", value="プリンターID")@FormParam("printerid") final String printerid,
    		@ApiParam(name="totalquantitystr", value="総量文字列")@FormParam("totalquantitystr") final String totalQuantityStr,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag,
    		@ApiParam(name="datastr", value="データ文字列")@FormParam("datastr") final String dataStr){
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("storename", storeName)
            .println("businessdate", bussinessDate)
            .println("reporttype", reportType)
            .println("storeid", storeId)
            .println("language", language)
            .println("operatorno", operatorNo)
            .println("deviceno", deviceNo)
            .println("storeidsearch", storeidSearch)
            .println("printerid", printerid)
            .println("totalquantitystr", totalQuantityStr)
            .println("trainingflag", trainingFlag)
            .println("datastr", dataStr);
        ResultBase resultBase = new ResultBase();
        try{
            if(StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(bussinessDate)
                    || StringUtility.isNullOrEmpty(reportType)
                    || StringUtility.isNullOrEmpty(storeId)
                    || StringUtility.isNullOrEmpty(operatorNo)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(storeidSearch)
                    || StringUtility.isNullOrEmpty(totalQuantityStr)
                    || StringUtility.isNullOrEmpty(dataStr)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(begindatetime)){
                    tp.println("parameters error");
                    resultBase.setNCRWSSExtendedResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return resultBase;
                }
                //report type check,data correct check,bussinessDate correct check
            if(!this.checkReportTypeForCommon(reportType.trim())
                        || !this.checkDataStr(reportType.trim(), dataStr.trim())
                        || !this.checkBusinessDayDate(bussinessDate.trim())
                        || !this.checkTotalQuantityStr(totalQuantityStr.trim(), reportType.trim())){
                    tp.println("parameters error");
                    resultBase.setNCRWSSExtendedResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return resultBase;
                }
            ReportMode reportMode = new ReportMode();
          //"," is the split character
            String reportquantityItem[] = totalQuantityStr.split(ReportConstants
                    .SALESREPORTPRINT_SPLITCOMMASTR);
          //合計の値を設定
            reportMode.setAllGuestCount(StringUtility
                    .convNullToLongZero(reportquantityItem[0].trim()));
            reportMode.setAllItemCount(StringUtility
                    .convNullToLongZero(reportquantityItem[1].trim()));
            reportMode.setAllSalesAmount(StringUtility
                    .convNullToDoubleZero(reportquantityItem[2].trim()));
            List<ItemMode> itemList = new ArrayList<ItemMode>();
            reportMode.setCompanyID(companyId.trim());
            reportMode.setSequenceNo(sequenceno.trim());
            reportMode.setWorkStationID(deviceNo.trim());
            reportMode.setBegindatetime(begindatetime.trim());
            reportMode.setSubdateid1(begindatetime.trim());
            reportMode.setSubdateid2(begindatetime.trim());
            reportMode.setStoreID(storeId);
            reportMode.setStoreName(storeName.trim());
            reportMode.setStoreidSearch(storeidSearch.trim());
            reportMode.setBusinessDayDate(bussinessDate.trim());
            reportMode.setLanguage(language);
            reportMode.setReportType(reportType.trim());
            reportMode.setOperaterNo(operatorNo.trim());
            reportMode.setTrainingFlag(trainingFlag);
            ItemMode itemMode = null;
            switch (reportType.trim()){
           /*時間帯別*/
            case ReportConstants.SALESTYPE_HOURLY:

           /*グループ別*/
            case ReportConstants.SALESTYPE_GROUP:

           /*販売員別*/
            case ReportConstants.SALESTYPE_SALESMAN:

           /*客層別*/
            case ReportConstants.SALESTYPE_TGT_MARKET:

           /*店舗別*/
            case ReportConstants.SALESTYPE_STORE:
              //";" is the split character
                String[] Line = dataStr.split(ReportConstants
                                .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                String[] item;
                for (int i = 0; i < Line.length; i++) {
                    itemMode = new ItemMode();
               //"," is the split character
                    item = Line[i].split(ReportConstants
                                .SALESREPORTPRINT_SPLITCOMMASTR);
                    itemMode.setItemName(item[0].trim());
                    itemMode.setGuestCnt(StringUtility
                            .convNullToLongZero(item[1].trim()));
                    itemMode.setItemCnt(StringUtility
                            .convNullToLongZero(item[2].trim()));
                    itemMode.setSalesAmt(StringUtility
                            .convNullToDoubleZero(item[3].trim()));
                    itemList.add(itemMode);
                }
                reportMode.setRptlist(itemList);
                break;
            }
            NetPrinterInfo netPrinterInfo = null;
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            //check if printer default setting
            if(LOCAL_PRINTER.equalsIgnoreCase(printerid)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                .lookup("java:comp/env");
                String xmlPath = (String) env
                .lookup("localPrinter");

                //read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                //check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)){
                    String errorMessage = "No default printer port found";
                    resultBase = new ResultBase(
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND,
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
                //set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            //for network printing
            else{
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeId,
                        deviceNo);
                if (netPrinterInfo == null) {
                    tp.println("PrinterInfo is null.");
                    String errorMessage = "No PrinterInfo found for store="
                            + storeId + ";device=" + deviceNo;
                    resultBase = new ResultBase(
                            ResultBase.RESDEVCTL_NOPRINTERFOUND,
                            ResultBase.RESDEVCTL_NOPRINTERFOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
            }
          /*logo found*/
            String logopath = iReceiptDAO.getLogoFilePath(storeId);
            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
         /*xmlFormat found*/
            String ReptFormatPath = (String) env
                    .lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(ReptFormatPath)
                    && (new File(ReptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(reportMode,
                        ReptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logAlert(PROG_NAME,
                        functionName,
                        Logger.RES_EXCEP_FILENOTFOUND, ReptFormatPath
                                + "file is not found");
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase.setMessage("file is not found");
                return resultBase;
            }
          /*printer found */
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeId, deviceNo, null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setMessage("Failed to print sales report for common.");
                tp.println("Failed to print sales report for common.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                resultBase.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print Sales Report For Common.");
            }
        }catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO, "DaoException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setMessage(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_ENCODING,
                    "UnsupportedEncodingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase.setMessage(e.getMessage());
        } catch(NamingException e){
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_NAMINGEXC,
                    "NamingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase.setMessage(e.getMessage());
        }catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, "Exception : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setMessage(e.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }
    /**
     * Print Report New With Div Code.
     *
     * @param companyid
     *            the company id
     * @param storename
     *            the store name
     * @param businessdate
     *            the businessdate
     * @param reporttype
     *            the report type
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param operatorno
     *            the operator number
     * @param deviceno
     *            the device number
     * @param divcode
     *            the department code
     * @param divname
     *            the department name
     * @param printerid
     *            the printer id
     * @param storeidsearch
     *         the search's storeid
     * @param totalquantitystr
     *            the total quantity str
     * @param trainingflag
     *            the TrainingModeFlag
     * @param datastr
     *            the print data str
     * @return the result base
     */
    @Path("/printsalesreportwithdivcode")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="印刷販売divコード情報", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミングエラーが発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase printSalesReportWithDivCode(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="storename", value="店舗名")@FormParam("storename") final String storeName,
    		@ApiParam(name="businessdate", value="営業日")@FormParam("businessdate") final String bussinessDate,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reportType,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeId,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="deviceno", value="装置番号")@FormParam("deviceno") final String deviceNo,
    		@ApiParam(name="divcode", value="divコード")@FormParam("divcode") final String divCode,
    		@ApiParam(name="divname", value="div名前")@FormParam("divname") final String divName,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerid,
    		@ApiParam(name="storeidsearch", value="店舗番号検索")@FormParam("storeidsearch") final String storeidSearch,
    		@ApiParam(name="totalquantitystr", value="総量文字列")@FormParam("totalquantitystr") final String totalQuantityStr,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag,
    		@ApiParam(name="datastr", value="データ文字列")@FormParam("datastr") final String dataStr){
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("sequenceno", sequenceno)
            .println("storename", storeName)
            .println("businessdate", bussinessDate)
            .println("reporttype", reportType)
            .println("storeid", storeId)
            .println("language", language)
            .println("operatorno", operatorNo)
            .println("deviceno", deviceNo)
            .println("divcode", divCode)
            .println("divname", divName)
            .println("printerid", printerid)
            .println("storeidsearch", storeidSearch)
            .println("totalquantitystr", totalQuantityStr)
            .println("trainingflag", trainingFlag)
            .println("datastr", dataStr);
        ResultBase resultBase = new ResultBase();
        try{
            if (StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(bussinessDate)
                    || StringUtility.isNullOrEmpty(reportType)
                    || StringUtility.isNullOrEmpty(storeId)
                    || StringUtility.isNullOrEmpty(operatorNo)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(storeidSearch)
                    || StringUtility.isNullOrEmpty(dataStr)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(begindatetime)){
                tp.println("parameters error");
                resultBase.setNCRWSSExtendedResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }
            //report type check,data correct check,bussinessDate correct check
            if(!this.checkReportTypeWithDiv(reportType.trim())
                    || !this.checkDataStr(reportType.trim(), dataStr.trim())
                    || !this.checkBusinessDayDate(bussinessDate.trim())){
                tp.println("parameters error");
                resultBase.setNCRWSSExtendedResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }
            if(ReportConstants.SALESTYPE_ITEM.equals(reportType.trim())){
                if(StringUtility.isNullOrEmpty(divCode)){
                    tp.println("parameters error");
                    resultBase.setNCRWSSExtendedResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return resultBase;
                }
            }
          //客数合計,点数合計,金額合計check
            if(!ReportConstants.SALESTYPE_DPT_HOURLY.equals(reportType.trim())){
                if(StringUtility.isNullOrEmpty(totalQuantityStr)){
                    tp.println("parameters error");
                    resultBase.setNCRWSSExtendedResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return resultBase;
                }
                if (!this.checkTotalQuantityStr(totalQuantityStr.trim(),
                        reportType.trim())) {
                    tp.println("parameters error");
                    resultBase.setNCRWSSExtendedResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_ERROR_INVALIDPARAMETER);
                    resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return resultBase;
                }
            }
            ReportMode reportMode = new ReportMode();
            List<ItemMode> itemList = new ArrayList<ItemMode>();
            reportMode.setCompanyID(companyId);
            reportMode.setSequenceNo(sequenceno.trim());
            reportMode.setWorkStationID(deviceNo.trim());
            reportMode.setBegindatetime(begindatetime.trim());
            reportMode.setSubdateid1(begindatetime.trim());
            reportMode.setSubdateid2(begindatetime.trim());
            reportMode.setStoreName(storeName.trim());
            reportMode.setStoreidSearch(storeidSearch.trim());
            reportMode.setBusinessDayDate(bussinessDate.trim());
            reportMode.setLanguage(language);
            reportMode.setReportType(reportType.trim());
            reportMode.setDiv(divCode.trim());
            reportMode.setDivName(divName.trim());
            reportMode.setOperaterNo(operatorNo.trim());
            reportMode.setStoreID(storeId.trim());
            reportMode.setTrainingFlag(trainingFlag);
            ItemMode itemMode = null;
            switch (reportType.trim()){

            /*部門別*/
            case ReportConstants.SALESTYPE_DPT:

           /*アイテム別*/
            case ReportConstants.SALESTYPE_ITEM:
              //"," is the split character
                String reportquantityItem[] = totalQuantityStr
                        .split(ReportConstants.SALESREPORTPRINT_SPLITCOMMASTR);
                reportMode.setAllGuestCount(StringUtility
                        .convNullToLongZero(reportquantityItem[0].trim()));
                reportMode.setAllItemCount(StringUtility
                        .convNullToLongZero(reportquantityItem[1].trim()));
                reportMode.setAllSalesAmount(StringUtility
                        .convNullToDoubleZero(reportquantityItem[2].trim()));
              //";" is the split character
                String[] Line = dataStr.split(ReportConstants
                        .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                String[] item;
                for (int i = 0; i < Line.length; i++) {
                    itemMode = new ItemMode();
              //"," is the split character
                    item = Line[i].split(ReportConstants
                        .SALESREPORTPRINT_SPLITCOMMASTR);
                    itemMode.setItemName(item[0].trim());
                    itemMode.setGuestCnt(StringUtility
                            .convNullToLongZero(item[1].trim()));
                    itemMode.setItemCnt(StringUtility
                            .convNullToLongZero(item[2].trim()));
                    itemMode.setSalesAmt(StringUtility
                            .convNullToDoubleZero(item[3].trim()));
                    itemList.add(itemMode);
                }
                reportMode.setRptlist(itemList);
                break;

           /*部門別時間帯別*/
            case ReportConstants.SALESTYPE_DPT_HOURLY:
              //"#" is the split character
               String[] block = dataStr.split(ReportConstants
                       .SALESREPORTPRINT_SPLITFORBLOCKSTR);
               for(int i = 0 ; i < block.length ; i++){
              //";" is the split character
                   String[] lineDptHourly = block[i].split(ReportConstants
                           .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                   for(int j = 0 ; j <= lineDptHourly.length ; j++){
                       String[] itemDptHourly = null;
                       if(j < lineDptHourly.length){
                           itemDptHourly = lineDptHourly[j].split(
                           ReportConstants.SALESREPORTPRINT_SPLITCOMMASTR);
                       }
                       itemMode = new ItemMode();
                       if( j > 0 && j < lineDptHourly.length){
                           //客数、点数、売上金額の設定Flag
                           itemMode.setIsBodyFlag(ReportConstants
                                   .SALESREPORTPRINT_SETFLAGSTR);
                           itemMode.setItemName(itemDptHourly[0].trim());
                           itemMode.setGuestCnt(StringUtility
                                   .convNullToLongZero(itemDptHourly[1].trim()));
                           itemMode.setItemCnt(StringUtility
                                   .convNullToLongZero(itemDptHourly[2].trim()));
                           itemMode.setSalesAmt(StringUtility
                                   .convNullToDoubleZero(itemDptHourly[3].trim()));
                       }else if(j == 0){
                           //Divsionの名前の設定Flag="True"
                           itemMode.setIsHeaderFlag(ReportConstants
                                   .SALESREPORTPRINT_SETFLAGSTR);
                           itemMode.setDivisionName(itemDptHourly[0].trim());
                       }else if(j == lineDptHourly.length){
                           //客数合計、点数合計、売上金額合計の設定Flag="True"
                           itemMode.setIsFooterFlag(ReportConstants
                                   .SALESREPORTPRINT_SETFLAGSTR);
                           itemMode.setGuestCntSum(StringUtility
                             .convNullToLongZero(lineDptHourly[0].split(ReportConstants
                                     .SALESREPORTPRINT_SPLITCOMMASTR)[1].trim()));
                           itemMode.setItemCntSum(StringUtility
                             .convNullToLongZero(lineDptHourly[0].split(ReportConstants
                                     .SALESREPORTPRINT_SPLITCOMMASTR)[2].trim()));
                           itemMode.setSalesAmtSum(StringUtility
                             .convNullToDoubleZero(lineDptHourly[0].split(ReportConstants
                                     .SALESREPORTPRINT_SPLITCOMMASTR)[3].trim()));
                       }
                       itemList.add(itemMode);
                   }
               }
               reportMode.setRptlist(itemList);
               break;
            }
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = null;

            //check if printer default setting
            if(LOCAL_PRINTER.equalsIgnoreCase(printerid)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                .lookup("java:comp/env");
                String xmlPath = (String) env
                .lookup("localPrinter");

                //read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                //check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)){
                    String errorMessage = "No default printer port found";
                    resultBase = new ResultBase(
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND,
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
                //set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            //for network printing
            else{
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeId,
                        deviceNo);
                if (netPrinterInfo == null) {
                    tp.println("PrinterInfo is null.");
                    String errorMessage = "No PrinterInfo found for store="
                            + storeId + ";device=" + deviceNo;
                    resultBase = new ResultBase(
                            ResultBase.RESDEVCTL_NOPRINTERFOUND,
                            ResultBase.RESDEVCTL_NOPRINTERFOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
            }
          /*logo found*/
            String logopath = iReceiptDAO.getLogoFilePath(storeId);

            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
         /*xmlFormat found*/
            String ReptFormatPath = (String) env
                    .lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(ReptFormatPath)
                    && (new File(ReptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(reportMode,
                        ReptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logAlert(PROG_NAME,
                        functionName,
                        Logger.RES_EXCEP_FILENOTFOUND, ReptFormatPath
                                + "file is not found");
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase.setMessage("file is not found");
                return resultBase;
            }
          /*printer found */
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeId, deviceNo, null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setMessage("Failed to print sales report with div code.");
                tp.println("Failed to print sales report with div code.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                resultBase.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print Sales Report With Div Code.");
            }
        }catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO, "DaoException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setMessage(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_ENCODING,
                    "UnsupportedEncodingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase.setMessage(e.getMessage());
        } catch(NamingException e){
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_NAMINGEXC,
                    "NamingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase.setMessage(e.getMessage());
        }catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, "Exception : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setMessage(e.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }
    /**
     * print sales report with salesmanno
     *
     * @param companyid
     *            the company id
     * @param storename
     *            the store name
     * @param businessdate
     *            the businessdate
     * @param reporttype
     *            the report type
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param operatorno
     *            the operator number
     * @param deviceno
     *            the device number
     * @param storeidsearch
     *         the search's storeid
     * @param printerid
     *            the printer id
     * @param salesmanno
     *            the sales man number
     * @param salesmanname
     *            the sales man name
     * @param totalquantitystr
     *            the total quantity str
     * @param trainingflag
     *            the TrainingModeFlag
     * @param datastr
     *            the print data str
     * @return the result base
     */
    @Path("/printsalesreportwithsalesmanno")//for have saleman code
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="販売人員番号によって営業報告書を印刷する", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミングエラーが発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase printSalesReportWithSalesManNo(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="storename", value="店舗名")@FormParam("storename") final String storeName,
    		@ApiParam(name="businessdate", value="営業日")@FormParam("businessdate") final String bussinessDate,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reportType,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeId,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="deviceno", value="装置番号")@FormParam("deviceno") final String deviceNo,
    		@ApiParam(name="storeidsearch", value="店舗番号検索")@FormParam("storeidsearch") final String storeidSearch,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerId,
    		@ApiParam(name="salesmanno", value="販売人員番号")@FormParam("salesmanno") final String salesManNo,
    		@ApiParam(name="salesmanname", value="販売人員名前")@FormParam("salesmanname") final String salesManName,
    		@ApiParam(name="totalquantitystr", value="総量文字列")@FormParam("totalquantitystr") final String totalQuantityStr,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag,
    		@ApiParam(name="datastr", value="データ文字列")@FormParam("datastr") final String dataStr){
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("sequenceno",sequenceno)
            .println("begindatetime",begindatetime)
            .println("storename", storeName)
            .println("businessdate", bussinessDate)
            .println("reporttype", reportType)
            .println("storeid", storeId)
            .println("language", language)
            .println("operatorno", operatorNo)
            .println("deviceno", deviceNo)
            .println("storeidsearch", storeidSearch)
            .println("printerid", printerId)
            .println("salesmanno", salesManNo)
            .println("salesmanname", salesManName)
            .println("totalquantitystr", totalQuantityStr)
            .println("trainingflag", trainingFlag)
            .println("datastr", dataStr);
        ResultBase resultBase = new ResultBase();
        try{
            if(StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(bussinessDate)
                    || StringUtility.isNullOrEmpty(reportType)
                    || StringUtility.isNullOrEmpty(storeId)
                    || StringUtility.isNullOrEmpty(operatorNo)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(storeidSearch)
                    || StringUtility.isNullOrEmpty(salesManNo)
                    || StringUtility.isNullOrEmpty(totalQuantityStr)
                    || StringUtility.isNullOrEmpty(dataStr)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(begindatetime)){
                tp.println("parameters error");
                resultBase.setNCRWSSExtendedResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);

    		        resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }
            //report type check,data correct check,bussinessDate correct check
            if(!ReportConstants.SALESTYPE_CLERKPRODUCT.equals(reportType.trim())
                    || !this.checkDataStr(reportType.trim(), dataStr.trim())
                    || !this.checkBusinessDayDate(bussinessDate.trim())
                    || !this.checkTotalQuantityStr(totalQuantityStr.trim()
                            , reportType.trim())){
                tp.println("parameters error");
                resultBase.setNCRWSSExtendedResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
                }
            ReportMode reportMode = new ReportMode();
            String reportquantityItem[] = totalQuantityStr.split(ReportConstants
                    .SALESREPORTPRINT_SPLITCOMMASTR);
            reportMode.setAllItemCount(StringUtility
                   .convNullToLongZero(reportquantityItem[0].trim()));
            reportMode.setAllSalesAmount(StringUtility
                    .convNullToDoubleZero(reportquantityItem[1].trim()));
            List<ItemMode> itemList = new ArrayList<ItemMode>();
            reportMode.setCompanyID(companyId);
            reportMode.setStoreName(storeName.trim());
            reportMode.setStoreidSearch(storeidSearch.trim());
            reportMode.setBusinessDayDate(bussinessDate.trim());
            reportMode.setLanguage(language);
            reportMode.setReportType(reportType.trim());
            reportMode.setOperaterNo(operatorNo.trim());
            reportMode.setSalesManNo(salesManNo.trim());
            reportMode.setSalesManName(salesManName.trim());
            reportMode.setWorkStationID(deviceNo.trim());
            reportMode.setSubdateid1(begindatetime.trim());
            reportMode.setSubdateid2(begindatetime.trim());
            reportMode.setSequenceNo(sequenceno.trim());
            reportMode.setStoreID(storeId);
            reportMode.setTrainingFlag(trainingFlag);
            ItemMode itemMode = null;
            switch (reportType.trim()){
           /*販売員・単品別*/
            case ReportConstants.SALESTYPE_CLERKPRODUCT:
                String[] Line = dataStr.split(ReportConstants
                        .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                String[] item;
                for (int i = 0; i < Line.length; i++) {
                    itemMode = new ItemMode();
                    item = Line[i].split(ReportConstants
                            .SALESREPORTPRINT_SPLITCOMMASTR);
                    itemMode.setItemName(item[0].trim());
                    itemMode.setItemCode(item[1].trim());
                    itemMode.setItemCnt(StringUtility
                            .convNullToLongZero(item[2].trim()));
                    itemMode.setSalesAmt(StringUtility
                            .convNullToDoubleZero(item[3].trim()));
                    itemList.add(itemMode);
                }
                reportMode.setRptlist(itemList);
                break;
            }
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = null;

            //check if printer default setting
            if(LOCAL_PRINTER.equalsIgnoreCase(printerId)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                .lookup("java:comp/env");
                String xmlPath = (String) env
                .lookup("localPrinter");

                //read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                //check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)){
                    String errorMessage = "No default printer port found";
                    resultBase = new ResultBase(
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND,
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
                //set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            //for network printing
            else{
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeId,
                        deviceNo);
                if (netPrinterInfo == null) {
                    tp.println("PrinterInfo is null.");
                    String errorMessage = "No PrinterInfo found for store="
                            + storeId + ";device=" + deviceNo;
                    resultBase = new ResultBase(
                            ResultBase.RESDEVCTL_NOPRINTERFOUND,
                            ResultBase.RESDEVCTL_NOPRINTERFOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
            }
          /*logo found*/
            String logopath = iReceiptDAO.getLogoFilePath(storeId);
            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
         /*xmlFormat found*/
            String ReptFormatPath = (String) env
                    .lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(ReptFormatPath)
                    && (new File(ReptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(reportMode,
                        ReptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logError(PROG_NAME,
                        functionName,
                        Logger.RES_EXCEP_FILENOTFOUND, ReptFormatPath
                                + "file is not found");
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase.setMessage("file is not found");
                return resultBase;
            }
          /*printer found */
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeId, deviceNo, null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setMessage("Failed to print sales report with salesman number.");
                tp.println("Failed to print sales report with salesman number.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                resultBase.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print Sales Report With Salesman Number.");
            }
        }catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO, "DaoException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setMessage(e.getMessage());
        }catch (UnsupportedEncodingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_ENCODING,
                    "UnsupportedEncodingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase.setMessage(e.getMessage());
        }catch(NamingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_NAMINGEXC,
                    "NamingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase.setMessage(e.getMessage());
        }catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, "Exception : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setMessage(e.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws NamingException
     *
     */
    private String getLocalPrinterPort(String xmlPath) throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException,
            NamingException {

        String printerPort="";
        SAXReader sr = new SAXReader();
        try {
            Document doc = sr.read(new File(xmlPath));
            for (Iterator<?> it = doc.getRootElement().elementIterator(PORTS_TAG); it
                    .hasNext();) {
                Element elem = (Element) it.next();
                for (Iterator<?> itInner = elem.elementIterator(); itInner
                        .hasNext();) {
                    Element elemInner = (Element) itInner.next();
                    if(PRINTER_TAG.equalsIgnoreCase(elemInner.getName())){
                        printerPort= elemInner.getData().toString();
                        break;
                    }
                }
            }
        } catch (DocumentException e) {
            LOGGER.logError(PROG_NAME, PROG_NAME+".format()",
                    Logger.RES_EXCEP_GENERAL,
                    "DocumentException:" + e.getMessage());
        }

        return printerPort;
    }

    /**
     * check the DataStr.
     *
     * @param reportType
     *            the report type
     * @param datastr
     *            print's data
     * @return the check DataStr Flag(true or false)
     */
    private boolean checkDataStr(String reportType,String datastr){
        boolean checkDataStrFlag = true;
        String[] block = null;
        String[] line = null;
        String[] item = null;
        Pattern pattern = Pattern.compile("^-?\\d+$");
        switch(StringUtility.convNullToEmpty(reportType)){
        /* Div(部門)別 */
        case ReportConstants.SALESTYPE_DPT:
        /* アイテム別 */
        case ReportConstants.SALESTYPE_ITEM:
        /* 時間帯別 */
        case ReportConstants.SALESTYPE_HOURLY:
        /* グループ別 */
        case ReportConstants.SALESTYPE_GROUP:
        /* 販売員別 */
        case ReportConstants.SALESTYPE_SALESMAN:
        /* 客層別 */
        case ReportConstants.SALESTYPE_TGT_MARKET:
        /* 店舗別 */
        case ReportConstants.SALESTYPE_STORE:
            //";" is the split character
            line = datastr.split(ReportConstants
                            .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                for(int i = 0;i<line.length;i++){
            //","is the split character
                       item = line[i].split(ReportConstants
                            .SALESREPORTPRINT_SPLITCOMMASTR);
            //data's quantity is not 4 check
                    if(item.length != ReportConstants
                            .SALESREPORTPRINT_DATASTRISFOUR){
                        checkDataStrFlag =  false;
                        break;
                    }
            //is not numeric check
                    for(int j = 0;j<item.length;j++){
                        if(j > 0){
                            Matcher isNum = pattern.matcher(item[j].trim());
                            if( !isNum.matches() ){
                                checkDataStrFlag = false;
                                break;
                            }
                        }
                    }
                }
             break;
          //会計レポート
        case ReportConstants.SALESTYPE_ACCOUNTANCY:
            line = datastr
                    .split(ReportConstants.SALESREPORTPRINT_SPLITSEMICOLONSTR);
            for (int i=0; i<line.length;i++) {
               item=line[i].split(":");
               //data's quantity is not 4 check
               if(item.length != ReportConstants
                       .SALESREPORTPRINT_DATASTRISFOUR){
                   checkDataStrFlag =  false;
                   break;
               }
               //is not numeric check
               for(int j = 0;j<item.length;j++){
                   if(j > 1){
                       Matcher isNum = pattern.matcher(item[j].trim());
                       if( !isNum.matches() ){
                           checkDataStrFlag = false;
                           break;
                       }
                   }
               }
            }
            break;
            /* 販売員・単品別 */
        case ReportConstants.SALESTYPE_CLERKPRODUCT:
            //";" is the split character
            line = datastr.split(ReportConstants
                            .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                for(int i = 0;i<line.length;i++){
            //","is the split character
                       item = line[i].split(ReportConstants
                            .SALESREPORTPRINT_SPLITCOMMASTR);
            //data's quantity is not 4 check
                    if(item.length != ReportConstants
                            .SALESREPORTPRINT_DATASTRISFOUR){
                        checkDataStrFlag =  false;
                        break;
                    }
           //is not numeric check
                    for(int j = 0;j<item.length;j++){
                        if(j > 1){
                            Matcher isNum = pattern.matcher(item[j].trim());
                            if( !isNum.matches() ){
                                checkDataStrFlag = false;
                                break;
                            }
                        }
                    }
                }
             break;
        /* Div(部門)・時間帯別 */
        case ReportConstants.SALESTYPE_DPT_HOURLY:
            //"#" is the split character
            block = datastr.split(ReportConstants
                            .SALESREPORTPRINT_SPLITFORBLOCKSTR);
            for(int i = 0;i<block.length ;i++){
              //";" is the split character
                line = block[i].split(ReportConstants
                            .SALESREPORTPRINT_SPLITSEMICOLONSTR);
                    for(int j = 0;j < line.length;j++){
             //"," is the split character
                        item =  line[j].split(ReportConstants
                            .SALESREPORTPRINT_SPLITCOMMASTR);
             //data's quantity is not 4 check
                        if(item.length != ReportConstants
                                .SALESREPORTPRINT_DATASTRISFOUR ){
                            checkDataStrFlag =  false;
                            break;
                        }
              //is not numeric check
                        for(int k = 0;k<item.length;k++){
                            if(k > 0){
                                Matcher isNum = pattern.matcher(item[k].trim());
                                if( !isNum.matches() ){
                                    checkDataStrFlag = false;
                                    break;
                                }
                            }
                        }
                }
            }
            break;
            default:
                checkDataStrFlag = false;
        }
        return checkDataStrFlag;
    }

    /**
     * check the TotalQuantityStr.
     *
     * @param reportType
     *            the report type
     * @param totalQuantityStr
     *            print's the total data
     * @return the totalquantity Check Flag(true or false)
     */
    private boolean checkTotalQuantityStr(String totalQuantityStr,String reportType){
        boolean totalquantityCheckFlag = true;
        Pattern pattern = Pattern.compile("^-?\\d+$");
        String [] item = null;
        switch(StringUtility.convNullToEmpty(reportType)){
        /* Div(部門)別 */
        case ReportConstants.SALESTYPE_DPT:
        /* アイテム別 */
        case ReportConstants.SALESTYPE_ITEM:
        /* 時間帯別 */
        case ReportConstants.SALESTYPE_HOURLY:
        /* グループ別 */
        case ReportConstants.SALESTYPE_GROUP:
        /* 販売員別 */
        case ReportConstants.SALESTYPE_SALESMAN:
        /* 客層別 */
        case ReportConstants.SALESTYPE_TGT_MARKET:
        /* 店舗別 */
        case ReportConstants.SALESTYPE_STORE:
            item = totalQuantityStr.split(ReportConstants
                    .SALESREPORTPRINT_SPLITCOMMASTR);
          //data's quantity is not 3 check
            if(item.length != ReportConstants.SALESREPORTPRINT_DATASTRISTHREE){
                totalquantityCheckFlag = false;
                break;
            }
            //is not numeric check
            for(int i = 0;i<item.length;i++){
                    Matcher isNum = pattern.matcher(item[i].trim());
                    if( !isNum.matches() ){
                        totalquantityCheckFlag = false;
                        break;
                }
            }
            break;
        /* 販売員・単品別 */
        case ReportConstants.SALESTYPE_CLERKPRODUCT:
            item = totalQuantityStr.split(ReportConstants
                    .SALESREPORTPRINT_SPLITCOMMASTR);
          //data's quantity is not 2 check
            if(item.length != ReportConstants.SALESREPORTPRINT_DATASTRISTWO){
                totalquantityCheckFlag = false;
                break;
            }
            //is not numeric check
            for(int i = 0;i<item.length;i++){
                    Matcher isNum = pattern.matcher(item[i].trim());
                    if( !isNum.matches() ){
                        totalquantityCheckFlag = false;
                        break;
                }
            }
            break;
            default:
                totalquantityCheckFlag = false;

    }
        return totalquantityCheckFlag;
    }

    /**
     * check the Report Type With Div.
     *
     * @param reportType
     *            the report type
     * @return the report Check Flag(true or false)
     */
    private boolean checkReportTypeWithDiv(String reportType){

        boolean reportCheckFlag = false;

        switch(StringUtility.convNullToEmpty(reportType)){
        /*部門別*/
            case ReportConstants.SALESTYPE_DPT:
        /*アイテム別*/
            case ReportConstants.SALESTYPE_ITEM:
       /*部門時間帯別*/
            case ReportConstants.SALESTYPE_DPT_HOURLY:
                reportCheckFlag = true;
                 break;
            default:
                reportCheckFlag = false;
        }
        return reportCheckFlag;
    }

    /**
     * check Report Type For Common.
     *
     * @param reportType
     *            the report type
     * @return the report Check Flag(true or false)
     */
    private boolean checkReportTypeForCommon(String reportType){
        boolean reportCheckFlag = false;
        switch(StringUtility.convNullToEmpty(reportType)){
        /*時間帯別*/
        case ReportConstants.SALESTYPE_HOURLY:
       /*グループ別*/
        case ReportConstants.SALESTYPE_GROUP:
       /*販売員別*/
        case ReportConstants.SALESTYPE_SALESMAN:
       /*客層別*/
        case ReportConstants.SALESTYPE_TGT_MARKET:
       /*店舗別*/
        case ReportConstants.SALESTYPE_STORE:
            reportCheckFlag = true;
            break;
            default:
                reportCheckFlag = false;
        }
        return reportCheckFlag;
    }

    /**
     * check BusinessDayDate.
     * @param businessdaydate
     *            the businessdaydate
     * @return true or false
     */
    private boolean checkBusinessDayDate(String businessdaydate){
        Pattern p = Pattern.compile("\\d{4}+[-]\\d{1,2}+[-]\\d{1,2}+");
        Matcher m = p.matcher(businessdaydate);
        if (!m.matches()) {
            return false;
        }
        // 年、月、日の取得
        String[] array = businessdaydate.split("-");
        int year = Integer.valueOf(array[0]);
        int month = Integer.valueOf(array[1]);
        int day = Integer.valueOf(array[2]);
        if (month < 1 || month > 12) {
            return false;
        }
        int[] monthLengths = new int[] { 0, 31, -1, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            monthLengths[2] = 29;
        } else {
            monthLengths[2] = 28;
        }
        int monthLength = monthLengths[month];
        if (day < 1 || day > monthLength) {
            return false;
        }
        return true;
    }
    /* 1.05 2014.12.22  売上表のPrintを対応  ADD END */

    // 1.07 2015.01.19 点検.精算レポート出力を対応　 ADD START
    /**
     * Print Checking and Settlement Report without windows driver.
     *
     * @param companyid
     *            the company id
     * @param sequenceno
     *            the sequenceNo
     * @param reporttype
     *            the report@type
     * @param deviceid
     *            the device id
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param begindatetime
     *            the begin date time
     * @param dataType
     *            the all print data
     * @param operatorno
     *            the operator　No
     * @param printerid
     *            the net printer id
     * @param type
     *            the Checking or Settlement Division 　
     * @param trainingflag
     *            the TrainingMode Flag 　
     * @return the result base
     */
    @Path("/printCheckAndSettleReport")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="印刷するチェックと解決レポート", response=ReportModes.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    		@ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
    		@ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="発生するネットワークレシート印刷エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミングエラーが発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),

        })
    public final ReportModes printCheckAndSettleReport(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reporttype,
    		@ApiParam(name="deviceid", value="デバイス番号")@FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeNo,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="tillid", value="ドロワーコード")@FormParam("tillid") final String tillid,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerid,
    		@ApiParam(name="type", value="タイプ")@FormParam("type") final String type,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
                .println("sequenceno", sequenceno)
                .println("reporttype", reporttype)
                .println("deviceid", deviceNo)
                .println("storeid", storeNo)
                .println("language", language)
                .println("tillid", tillid)
                .println("operatorno", operatorNo)
                .println("begindatetime", begindatetime)
                .println("printerid", printerid)
                .println("type", type)
                .println("trainingflag", trainingFlag);

        ReportModes reportModes = new ReportModes();
        ReportMode reportMode = new ReportMode();

        try {
            if (StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(reporttype)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(storeNo)
                    || StringUtility.isNullOrEmpty(begindatetime)
                    || StringUtility.isNullOrEmpty(operatorNo)) {
                tp.println("parameters error");
                reportModes
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                reportModes
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                reportModes.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return reportModes;
            }
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();
            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeNo)
                    .getDateSetting();
            String businessDayDate = dateSetting.getToday();
            String storeName = reportDAO.findStoreName(storeNo);
            if (StringUtility.isNullOrEmpty(storeName)) {
                storeName = "";
            }
            String storeTel = reportDAO.getStoreTel(storeNo);
            String dataType = reportDAO.getActData(storeNo, tillid, businessDayDate,
                    language);
            reportMode=getReportModeObj(reportMode,dataType);
            reportMode.setCompanyID(companyId);
            reportMode.setSequenceNo(sequenceno);
            reportMode.setStoreName(storeName);
            reportMode.setStoreID(storeNo);
            reportMode.setTillId(tillid);
            reportMode.setTrainingFlag(trainingFlag);
            reportMode.setTelephone(storeTel);
            reportMode.setReportType(reporttype);
            reportMode.setType(type);
            reportMode.setWorkStationID(deviceNo);
            reportMode.setBusinessDayDate(businessDayDate);
            reportMode.setSubdateid1(begindatetime);
            reportMode.setSubdateid2(begindatetime);
            reportMode.setLanguage(language);
            reportMode.setOperaterNo(operatorNo);

            reportModes.setReportMode(reportMode);
            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = null;
            //check if printer default setting

            if(LOCAL_PRINTER.equalsIgnoreCase(printerid)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                .lookup("java:comp/env");
                String xmlPath = (String) env
                .lookup("localPrinter");

                //read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                // check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)) {
                    String errorMessage = "No default printer port found";
                    reportModes
                            .setNCRWSSResultCode(ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                    reportModes
                            .setNCRWSSExtendedResultCode(ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                    reportModes.setMessage(StringUtility
                            .printStackTrace(new Exception(errorMessage)));
                    return reportModes;
                }
                // set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            //for network printing
            else{
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeNo,
                        deviceNo);

                if (netPrinterInfo == null) {
                    tp.println("PrinterInfo is null.");
                    String errorMessage = "No PrinterInfo found for store="
                            + storeNo + ";device=" + deviceNo;
                    reportModes
                            .setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                    reportModes
                            .setNCRWSSExtendedResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                    reportModes.setMessage(StringUtility
                            .printStackTrace(new Exception(errorMessage)));
                    return reportModes;
            }
            }
            // get logo
            String logopath = iReceiptDAO.getLogoFilePath(storeNo);
            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
            // get formatxml file
            String nrRcptFormatPath = (String) env.lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(nrRcptFormatPath)
                    && (new File(nrRcptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(reportMode,
                        nrRcptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logAlert(PROG_NAME, functionName,
                        Logger.RES_EXCEP_FILENOTFOUND, nrRcptFormatPath
                                + "file is not found");
                reportModes
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                reportModes
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                return reportModes;
            }
            // get print ip
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeNo, deviceNo, null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                reportModes.setNCRWSSExtendedResultCode(printResult);
                reportModes.setNCRWSSResultCode(printResult);
                reportModes.setMessage("Failed to print Check And Settle report.");
                tp.println("Failed to print Check And Settle report.").println("result",
                        printResult);
            } else {
                reportModes.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                reportModes.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                reportModes.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print Check And Settle report.");
            }
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                                    "DaoException : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            reportModes.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            reportModes.setMessage(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                        "UnsupportedEncodingException : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            reportModes.setMessage(e.getMessage());
        } catch (NamingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_NAMINGEXC,
                    "NamingException : " + e.getMessage(), e);
            reportModes
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            reportModes.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "Exception : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(reportModes);
        }
        return reportModes;

    }

    /**
     * Print cash in draw Report without windows driver.
     *
     * @param companyid
     *            the company id
     * @param sequenceno
     *            the sequenceNo
     * @param businessdate
     *            the businessDate
     * @param deviceid
     *            the device id
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param begindatetime
     *            the begin date time
     * @param dataType
     *            the all print data
     * @param operatorno
     *            the operator　No
     * @param totalAmount
     *            the total Amount
     * @param printerid
     *            the net printer id
     * @param type
     *            the Checking or Settlement Division 　
     * @param trainingflag
     *            the TrainingMode Flag 　
     * @return the result base
     */
    @Path("/printCashInDrawReport")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="印刷するレポート現金", response=ReportModes.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="発生するネットワークレシート印刷エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミングエラーが発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ReportModes printCashInDrawReport(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reporttype,
    		@ApiParam(name="tillid", value="ドロワーコード")@FormParam("tillid") final String tillid,
    		@ApiParam(name="deviceid", value="デバイス番号")@FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeNo,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="dataType", value="データ種別")@FormParam("dataType") final String dataType,
    		@ApiParam(name="totaldata", value="全データ")@FormParam("totaldata") final String totaldata,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerid,
    		@ApiParam(name="type", value="タイプ")@FormParam("type") final String type,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
                .println("sequenceno", sequenceno)
                .println("reporttype", reporttype)
                .println("tillid", tillid)
                .println("deviceid", deviceNo)
                .println("storeid", storeNo)
                .println("language", language)
                .println("begindatetime", begindatetime)
                .println("dataType", dataType)
                .println("operatorno", operatorNo)
                .println("totaldata", totaldata)
                .println("printerid", printerid)
                .println("type", type)
                .println("trainingflag", trainingFlag);

        ReportModes reportModes = new ReportModes();
        try {
            if (StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(reporttype)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(totaldata)
                    || StringUtility.isNullOrEmpty(storeNo)
                    || StringUtility.isNullOrEmpty(dataType)
                    || StringUtility.isNullOrEmpty(begindatetime)
                    || StringUtility.isNullOrEmpty(operatorNo)) {
                tp.println("parameters error");
                reportModes
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                reportModes
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                reportModes.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return reportModes;
            }
            ReportMode reportmode = new ReportMode();
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();
            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeNo)
                    .getDateSetting();
            String businessDayDate = dateSetting.getToday();
            String storeName = reportDAO.findStoreName(storeNo);
            if (StringUtility.isNullOrEmpty(storeName)) {
                storeName = "";
            }
            String storeTel = reportDAO.getStoreTel(storeNo);
            reportmode.setCompanyID(companyId);
            reportmode.setStoreName(storeName);
            reportmode.setTelephone(storeTel);
            reportmode.setStoreID(storeNo);
            reportmode.setTillId(tillid);
            reportmode.setTrainingFlag(trainingFlag);
            reportmode.setWorkStationID(deviceNo);
            reportmode.setBusinessDayDate(businessDayDate);
            reportmode.setLanguage(language);
            reportmode.setReportType(reporttype);
            reportmode.setType(type);
            reportmode.setSequenceNo(sequenceno);
            reportmode.setOperaterNo(operatorNo);
            reportmode.setSubdateid1(begindatetime);
            reportmode.setSubdateid2(begindatetime);
            List<ItemMode> itemList = new ArrayList<ItemMode>();
            String[] line = dataType.split(";");
            String[] item;
            ItemMode itemMode = null;
            for (int i = 0; i < line.length; i++) {
                itemMode = new ItemMode();
                item = line[i].split(":");
                itemMode.setItemName(item[0]);
                itemMode.setItemCnt(StringUtility.convNullToLongZero(item[1]
                        .trim()));
                itemMode.setSalesAmt(StringUtility.convNullToDoubleZero(item[2]
                        .trim()));
                itemList.add(itemMode);
            }
            //1.08  2015.1.30     MAJINHUI 点検.精算レポート出力変更を対応 add start
            String[] linestr = totaldata.split(";");
            for (String le : linestr) {
                String[] itemstr = le.split(":");
                switch (itemstr[0]) {
                // Cash total
                case ReportConstants.CASH_TOTAL:
                	reportmode.setAllDrawAmount(StringUtility
                        .convNullToDoubleZero(itemstr[1]));
                	break;
                // クレジット
                case ReportConstants.ATYREPORT_CREDIT:
                    reportmode.setCreditAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setCreditPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // 銀聯
                case ReportConstants.ATYREPORT_UNIONPAY:
                	reportmode.setUnionPayAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                	reportmode.setUnionPayPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                	break;
                // ギフトカード
                case ReportConstants.ATYREPORT_GIFTCARD:
                    reportmode.setGiftCardAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setGiftCardPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                    // 計
                case ReportConstants.ATYREPORT_GOLDSPECIESSUBTOTAL:
                    reportmode.setGoldSpeciesSubtotal(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    break;
                // 買物券
                case ReportConstants.CASH_BARNEYSVOUCHER:
                    reportmode.setShoppingTicketAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setShoppingTicketPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // ギフト券
                case ReportConstants.CASH_BARNEYSGIFT:
                    reportmode.setGiftVoucherAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setGiftVoucherPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // 伊勢丹
                case ReportConstants.CASH_ISETANGIFT:
                    reportmode.setIsetanAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setIsetanPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // DC
                case ReportConstants.CASH_DC:
                    reportmode
                            .setDCAmt(StringUtility.convNullToDoubleZero(itemstr[1]));
                    reportmode.setDCLablePoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // AMEX
                case ReportConstants.CASH_AMEX:
                    reportmode.setAMEXAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setAMEXLablePoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // JCB
                case ReportConstants.CASH_JCB:
                    reportmode.setJCBAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setJCBLablePoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // diners
                case ReportConstants.CASH_DINERS:
                    reportmode.setDinersAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setDinersLablePoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // Iカード
                case ReportConstants.CASH_ICARD:
                    reportmode.setICardAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    break;
                // 三井
                case ReportConstants.CASH_MITSUI:
                    reportmode.setMitsuiAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setMitsuiLablePoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // 軽井沢
                case ReportConstants.CASH_PRINCE:
                    reportmode.setKaruizawaAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setKaruizawaPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // Chel 1000
                case ReportConstants.CASH_CHEL1000:
                    reportmode.setChel1000Amt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setChel1000Points(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // Chel 2000
                case ReportConstants.CASH_CHEL2000:
                    reportmode.setChel2000Amt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setChel2000Points(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // その他
                case ReportConstants.SONOTA:
                    reportmode.setSonotaAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    reportmode.setSonotaPoints(StringUtility
    						.convNullToLongZero(itemstr[2]));
                    break;
                // 計
                case ReportConstants.ATYREPORT_GIFTCERTIFICATESSUBTOTAL:
                    reportmode.setGiftCertificatesSubtotalAmt(StringUtility
                            .convNullToDoubleZero(itemstr[1]));
                    break;
                case ReportConstants.CASH_CALCULATETOTAL:
                	reportmode.setCalculateTotalAmt(StringUtility
                			.convNullToDoubleZero(itemstr[1]));
                	break;
                case ReportConstants.CASH_REALTOTAL:
                	reportmode.setRealTotalAmt(StringUtility
                			.convNullToDoubleZero(itemstr[1]));
                	break;
                case ReportConstants.CASH_GAP:
                	reportmode.setGapAmt(StringUtility
                			.convNullToDoubleZero(itemstr[1]));
                	break;

                default:
                    break;
                }
            }
            reportmode.setRptlist(itemList);
            reportModes.setReportMode(reportmode);

            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = null;
            // check if printer default setting

            if (LOCAL_PRINTER.equalsIgnoreCase(printerid)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                        .lookup("java:comp/env");
                String xmlPath = (String) env.lookup("localPrinter");

                // read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                // check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)) {
                    String errorMessage = "No default printer port found";
                    reportModes
                            .setNCRWSSResultCode(ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                    reportModes
                            .setNCRWSSExtendedResultCode(ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                    reportModes.setMessage(StringUtility
                            .printStackTrace(new Exception(errorMessage)));

                    return reportModes;
                }
                // set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            // for network printing
            else {
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeNo, deviceNo);

                if (netPrinterInfo == null) {
                    tp.println("PrinterInfo is null.");
                    String errorMessage = "No PrinterInfo found for store="
                            + storeNo + ";device=" + deviceNo;
                    reportModes
                            .setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                    reportModes
                            .setNCRWSSExtendedResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                    reportModes.setMessage(StringUtility
                            .printStackTrace(new Exception(errorMessage)));
                    return reportModes;
                }
            }
            // get logo
            String logopath = iReceiptDAO.getLogoFilePath(storeNo);
            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
            // get formatxml file
            String nrRcptFormatPath = (String) env
                    .lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(nrRcptFormatPath)
                    && (new File(nrRcptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(reportmode,
                        nrRcptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logError(PROG_NAME, "printCashInDrawReport",
                                Logger.RES_EXCEP_FILENOTFOUND,
                                nrRcptFormatPath + "file is not found");
                reportModes
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                reportModes
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                return reportModes;
            }
            // get print ip
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeNo, deviceNo,
                    null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                reportModes.setNCRWSSExtendedResultCode(printResult);
                reportModes.setNCRWSSResultCode(printResult);
                reportModes.setMessage("Failed to print cash in draw report.");
                tp.println("Failed to print cash in draw report.").println(
                        "result", printResult);
            } else {
                reportModes.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                reportModes.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                reportModes.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print cash in draw report.");
            }
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    "DaoException : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            reportModes.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
        } catch (UnsupportedEncodingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                    "UnsupportedEncodingException : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
        } catch (NamingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_NAMINGEXC,
                                    "Exception : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
        } catch (Exception e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                                    "Exception : " + e.getMessage(), e);
            reportModes.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            reportModes
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(reportModes.toString());
        }
        return reportModes;
    }

    /**
     * Print financial report without windows driver.
     *
     * @param companyId
     *            the company id
     * @param deviceNo
     *            the device no
     * @param storeNo
     *            the store no
     * @param language
     *            the language
     * @return the result base
     */
    @Path("/printfinancialreport")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="プリントする財務報告", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NOTFOUND, message="トランザクションプリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="発生するネットワークレシート印刷エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase printFinancialReport(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="deviceid", value="デバイスコード")@FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="storeid", value="店舗名")@FormParam("storeid") final String storeNo,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language) {

        tp.methodEnter("printFinancialReport");
        tp.println("companyId", companyId)
            .println("deviceNo", deviceNo)
            .println("storeNo", storeNo)
            .println("language", language);

        ResultBase resultBase = new ResultBase();

        // If parameter language is null, use default language
        String sysLanguage = "";
        if (language == null || "".equals(language)) {
            sysLanguage = GlobalConstant.getDefaultLanguage();
        } else {
            sysLanguage = language;
        }

        try {
            // Get the data of Financial Report
            // Get the interface of ReportDAO
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            // Get the business date
            JournalizationResource journ = new JournalizationResource(context);
            String businessDayDate = journ.getBussinessDate(companyId, storeNo);

            // Get Financial Report
            FinancialReport financialReport = reportDAO.getFinancialReportObj(
                    deviceNo, businessDayDate, storeNo);
            IReceiptDAO receiptDAO = daoFactory.getReceiptDAO();

            NetPrinterInfo printerInfo = receiptDAO.getPrinterInfo(storeNo, deviceNo);

            if (printerInfo == null) {
                resultBase
                        .setNCRWSSResultCode(ResultBase
                                .RESNETRECPT_ERROR_NOTFOUND);
                tp.println("printerInfo is invalid");
                return resultBase;
            }

            // Print Financial Report
            FinancialReportFormatter financialFormatter =
                new FinancialReportFormatter(sysLanguage);
            List<String> reportLines = financialFormatter
                    .getFinancialReportText(financialReport);

            FinancialReportPrint reportPrint = new FinancialReportPrint(
                    printerInfo);

            int printResult = reportPrint.printFinancialReport(reportLines);
            if (printResult == 0) {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
            } else {
                resultBase.setNCRWSSResultCode(printResult);
            }
        } catch (DaoException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logSnapException("Report", Logger.RES_EXCEP_DAO, e.getMessage(), e);
        } catch (SQLException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_IOEXCEPTION);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                                    Logger.RES_EXCEP_SQL, e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_UNSUPPORTEDENCODING);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_ENCODING, e.getMessage(), e);
        } catch (Exception e) {
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase
                            .RES_ERROR_GENERAL);
            LOGGER.logAlert("Report", "ReportResource.printFinancialReport",
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
        } finally {
            tp.methodExit(resultBase.toString());
        }

        return resultBase;
    }

    /**
     * Print Drawer Financial Report.
     *
     * @param companyId
     *            the company id
     * @param printerID
     *            the printer id
     * @param storeNo
     *            the store no
     * @param storeName
     *            the store name
     * @param language
     *            the language
     * @return the result base
     */
    @Path("/printdrawerfinancialreport")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="プリントする引き出し財務報告", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NOTFOUND, message="トランザクションプリンタが見ない"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="発生するネットワークレシート印刷エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        })
    public final ResultBase printDrawerFinancialReport(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerID,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeNo,
    		@ApiParam(name="storename", value="店舗名")@FormParam("storename") final String storeName,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language) {

        tp.methodEnter("printDrawerFinancialReport");
        tp.println("companyId", companyId)
            .println("printerID", printerID)
            .println("storeNo", storeNo)
            .println("storeName", storeName)
            .println("language", language);

        ResultBase result = new ResultBase();

        // If parameter language is null, use default language
        String sysLanguage = "";
        if (language == null || "".equals(language)) {
            sysLanguage = GlobalConstant.getDefaultLanguage();
        } else {
            sysLanguage = language;
        }

        try {
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            NetPrinterInfo printerInfo = reportDAO.getPrinterInfo(storeNo, printerID);
            if (printerInfo == null) {
                result.setNCRWSSResultCode(ResultBase
                        .RESNETRECPT_ERROR_NOTFOUND);
                tp.println("printerInfo is invalid");
                throw new Exception("Not Found Printer");
            }
            tp.println("IpAddress", printerInfo.getUrl());
            tp.println("TCP PORT", printerInfo.getPortTCP());
            tp.println("UDP PORT", printerInfo.getPortUDP());

            // Get Drawer Financial Report
            DrawerFinancialReport drawerFinancialReport =
                getDrawerFinancialReport(companyId, printerID, storeNo);
            if (drawerFinancialReport.getNCRWSSResultCode()
                    != ResultBase.RESRPT_OK) {
                result.setNCRWSSResultCode(drawerFinancialReport
                        .getNCRWSSResultCode());
                tp.println("drawerFinancialReport is invalid");
                throw new Exception("Can not get drawer financial report.");
            }

            drawerFinancialReport.setStoreid(storeNo);
            drawerFinancialReport.setStoreName(storeName);

            FinancialReportFormatter financialFormatter =
                new FinancialReportFormatter(
                    sysLanguage);
            List<String> reportLines = financialFormatter
                    .getDrawerFinancialReportText(drawerFinancialReport);
            FinancialReportPrint reportPrint = new FinancialReportPrint(
                    printerInfo);

            int printResult = reportPrint.printFinancialReport(reportLines);
            if (printResult == 0) {
                result.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
            } else {
                result.setNCRWSSResultCode(printResult);
            }
        } catch (DaoException e) {
            result.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_NG);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logAlert("Report",
                    "ReportResource.printDrawerFinancialReport",
                    Logger.RES_EXCEP_DAO, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.logAlert("Report",
                    "ReportResource.printDrawerFinancialReport",
                    Logger.RES_EXCEP_GENERAL, e.getMessage(), e);
        } finally {
            tp.methodExit(result.toString());
        }

        return result;
    }

    /**
     * Open drawer with windows printer driver.
     *
     * @param storeid
     *            the storeid
     * @param deviceNo
     *            the device no
     * @return the result base
     */
    @Path("/opendrawer2")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase openDrawer2(
            @FormParam("storeid") final String storeid,
            @FormParam("deviceno") final String deviceNo) {

        tp.methodEnter("openDrawer2");
        tp.println("storeid", storeid)
            .println("deviceNo", deviceNo);

        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReceiptDAO receiptDAO = daoFactory.getReceiptDAO();
            String printerName = receiptDAO.getPrinterName(storeid, deviceNo);

            PrinterDrawer drawer = new PrinterDrawer(printerName);
            if (drawer.openPrinterDrawer()) {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
            } else {
                resultBase
                        .setNCRWSSResultCode(ResultBase
                                .RESNETRECPT_ERROR_DRAWERNOTFOUND);
            }
        } catch (DaoException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_DRAWER);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logAlert("Report", "ReportResource.openDrawer2",
                    Logger.RES_EXCEP_DAO, e.getMessage(), e);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * Open drawer without windows printer driver.
     *
     * @param storeid
     *            the storeid
     * @param deviceNo
     *            the device no
     * @return the result base
     */
    @Path("/opendrawer")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="開いた引き出し", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NOTFOUND, message="トランザクションプリンタが見ない"),
            @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_DRAWER, message="オープンする引き出し失敗"),
        })
    public final ResultBase openDrawer(
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeid,
    		@ApiParam(name="deviceid", value="デバイスコード")@FormParam("deviceid") final String deviceNo) {

        tp.methodEnter("openDrawer");
        tp.println("storeid", storeid)
            .println("deviceNo", deviceNo);

        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReceiptDAO receiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = receiptDAO.getPrinterInfo(storeid, deviceNo);
            if (netPrinterInfo == null) {
                resultBase
                        .setNCRWSSResultCode(ResultBase
                                .RESNETRECPT_ERROR_NOTFOUND);
                tp.println("netPrinterInfo is invalid");
                return resultBase;
            }
            tp.println("IpAddress", netPrinterInfo.getUrl());
            tp.println("TCP PORT", netPrinterInfo.getPortTCP());
            tp.println("UDP PORT", netPrinterInfo.getPortUDP());

            PrinterDrawer drawer = new PrinterDrawer(netPrinterInfo);
            if (drawer.openDrawer()) {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
            } else {
                resultBase
                        .setNCRWSSResultCode(ResultBase
                                .RESNETRECPT_ERROR_DRAWERNOTFOUND);
            }
        } catch (DaoException e) {
            resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_ERROR_DRAWER);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logAlert("Report", "ReportResource.openDrawer",
                    Logger.RES_EXCEP_DAO, e.getMessage(), e);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * Print Accountancy　Report without windows driver.
     *
     * @param companyid
     *            the company id
     * @param sequenceno
     *            the sequenceNo
     * @param storename
     *            the store name
     * @param businessdate
     *            the businessdate
     * @param reporttype
     *            the report type
     * @param deviceid
     *            the device id
     * @param storeid
     *            the store id
     * @param language
     *            the language
     * @param begindatetime
     *            the begin date time
     * @param dataType
     *            the all print data
     * @param operatorno
     *            the operator　No
     * @param printerid
     *            the net printer id
     * @param storeidsearch
     *            the search store id 　
     * @param tillid
     *            the search till id 　
     * @param trainingflag
     *            the TrainingMode Flag 　
     * @return the result base
     */
    @Path("/printAccountancyReport")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
    @ApiOperation(value="プリントする会計報告", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンタポートが見ない"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOPRINTERFOUND, message="プリンタが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定したファイルが見ない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="エンコードするデータをサポートされない"),
            @ApiResponse(code=ResultBase.RES_ERROR_NAMINGEXCEPTION, message="ネーミング例外が発生する"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase printAccountancyReport(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="sequenceno", value="送信管理順序")@FormParam("sequenceno") final String sequenceno,
    		@ApiParam(name="storename", value="店舗名")@FormParam("storename") final String storename,
    		@ApiParam(name="businessdate", value="営業日")@FormParam("businessdate") final String businessDate,
    		@ApiParam(name="reporttype", value="レポートタイプ")@FormParam("reporttype") final String reporttype,
    		@ApiParam(name="deviceid", value="店舗番号")@FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeNo,
    		@ApiParam(name="language", value="言葉")@FormParam("language") final String language,
    		@ApiParam(name="begindatetime", value="POSLogのシステム日時")@FormParam("begindatetime") final String begindatetime,
    		@ApiParam(name="dataType", value="データ種別")@FormParam("dataType") final String dataType,
    		@ApiParam(name="operatorno", value="従業員番号")@FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="printerid", value="プリンターID")@FormParam("printerid") final String printerid,
    		@ApiParam(name="storeidsearch", value="店舗番号検索")@FormParam("storeidsearch") final String storeidSearch,
    		@ApiParam(name="tillid", value="ドロワーコード")@FormParam("tillid") final String tillid,
    		@ApiParam(name="type", value="タイプ")@FormParam("type") final String type,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("sequenceno", sequenceno)
            .println("storename", storename)
            .println("businessDate", businessDate)
            .println("reporttype", reporttype)
            .println("deviceNo", deviceNo)
            .println("storeNo", storeNo)
            .println("language", language)
            .println("begindatetime", begindatetime)
            .println("dataType", dataType)
            .println("operatorNo", operatorNo)
            .println("printerid", printerid)
            .println("storeidSearch", storeidSearch)
            .println("tillid", tillid)
            .println("type", type)
            .println("trainingflag", trainingFlag);

        ReportMode atyReportMode = new ReportMode();
        ResultBase resultBase = new ResultBase();

        try {
            if (StringUtility.isNullOrEmpty(companyId)
                    || StringUtility.isNullOrEmpty(sequenceno)
                    || StringUtility.isNullOrEmpty(businessDate)
                    || StringUtility.isNullOrEmpty(reporttype)
                    || StringUtility.isNullOrEmpty(deviceNo)
                    || StringUtility.isNullOrEmpty(storeNo)
                    || StringUtility.isNullOrEmpty(begindatetime)
                    || StringUtility.isNullOrEmpty(storeidSearch)
                    || StringUtility.isNullOrEmpty(dataType)
                    || StringUtility.isNullOrEmpty(operatorNo)) {
                tp.println("parameters error");
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }
            if (!ReportConstants.SALESTYPE_ACCOUNTANCY.equals(StringUtility
                    .convNullToEmpty(reporttype.trim()))
                    || !this.checkDataStr(reporttype.trim(), dataType.trim())
                    || !this.checkBusinessDayDate(businessDate.trim())) {
                tp.println("parameters error");
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IReportDAO reportDAO = daoFactory.getReportDAO();

            String storeTel = reportDAO.getStoreTel(storeidSearch);
            atyReportMode=getReportModeObj(atyReportMode,dataType);
            atyReportMode.setCompanyID(companyId);
            atyReportMode.setSequenceNo(sequenceno);
            atyReportMode.setStoreName(storename);
            atyReportMode.setStoreID(storeNo);
            atyReportMode.setTrainingFlag(trainingFlag);
            atyReportMode.setStoreidSearch(storeidSearch);
            atyReportMode.setTelephone(storeTel);
            atyReportMode.setReportType(reporttype);
            atyReportMode.setWorkStationID(deviceNo);
            atyReportMode.setBusinessDayDate(businessDate);
            atyReportMode.setSubdateid1(begindatetime);
            atyReportMode.setSubdateid2(begindatetime);
            atyReportMode.setLanguage(language);
            atyReportMode.setOperaterNo(operatorNo);
            atyReportMode.setTillId(tillid);
            if(StringUtility.isNullOrEmpty(type)){
            	atyReportMode.setType(reporttype);
            } else {
            	atyReportMode.setType(type);
            }

            IReceiptDAO iReceiptDAO = daoFactory.getReceiptDAO();
            NetPrinterInfo netPrinterInfo = null;
            //check if printer default setting

            if(LOCAL_PRINTER.equalsIgnoreCase(printerid)) {

                netPrinterInfo = new NetPrinterInfo();

                javax.naming.Context env = (javax.naming.Context) new InitialContext()
                .lookup("java:comp/env");
                String xmlPath = (String) env
                .lookup("localPrinter");

                //read from xml
                String printerPort = getLocalPrinterPort(xmlPath);

                //check if no port is retrieved from interface.xml
                if (StringUtility.isNullOrEmpty(printerPort)){
                    String errorMessage = "No default printer port found";
                    resultBase = new ResultBase(
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND,
                            ResultBase.RES_PRINTER_PORT_NOT_FOUND, new Exception(
                                    errorMessage));
                    return resultBase;
                }
                //set retrieved printer port to url
                netPrinterInfo.setUrl(printerPort);
            }
            //for network printing
            else{
                // get net printer information
                netPrinterInfo = iReceiptDAO.getPrinterInfo(storeNo,
                        deviceNo);

            if (netPrinterInfo == null) {
                tp.println("PrinterInfo is null.");
                String errorMessage = "No PrinterInfo found for store="
                        + storeNo + ";device=" + deviceNo;
                resultBase = new ResultBase(
                        ResultBase.RESDEVCTL_NOPRINTERFOUND,
                        ResultBase.RESDEVCTL_NOPRINTERFOUND, new Exception(
                                errorMessage));
                return resultBase;
            }
            }
            // get logo
            String logopath = iReceiptDAO.getLogoFilePath(storeNo);
            List<List<byte[]>> reportsList = new ArrayList<List<byte[]>>();
            javax.naming.Context env = (javax.naming.Context) new InitialContext()
                    .lookup("java:comp/env");
            // get formatxml file
            String nrRcptFormatPath = (String) env.lookup("ReportFormatNewPath");
            if (!StringUtility.isNullOrEmpty(nrRcptFormatPath)
                    && (new File(nrRcptFormatPath)).exists()) {
                FormatReportByXML frbx = new FormatReportByXML(atyReportMode,
                        nrRcptFormatPath);
                reportsList.add(frbx.getReport());
            } else {
                LOGGER.logAlert(PROG_NAME, functionName,
                        Logger.RES_EXCEP_FILENOTFOUND, nrRcptFormatPath
                                + "file is not found");
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                resultBase
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                return resultBase;
            }
            // get print ip
            int printResult;
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, null, false, storeNo, deviceNo, null);
            printResult = receiptPrint.printAllReceipt(reportsList);
            if (printResult != ResultBase.RESRPT_OK) {
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setMessage("Failed to print accountancy report.");
                tp.println("Failed to print accountancy report.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                resultBase.setMessage(ResultBase.RES_SUCCESS_MSG);
                tp.println("print accountancy report.");
            }
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "DaoException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setMessage(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_ENCODING,
                    "UnsupportedEncodingException : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNSUPPORTEDENCODING);
            resultBase.setMessage(e.getMessage());
        } catch (NamingException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_NAMINGEXC,
                    "NamingException : " + e.getMessage(), e);
            resultBase
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NAMINGEXCEPTION);
            resultBase.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Exception : " + e.getMessage(), e);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }

    /*
     * @param atyReportMode the ReportMode obj　 /* @param storeidsearch the data
     * str 　
     *
     * @return the result base
     */
    private ReportMode getReportModeObj(ReportMode atyReportMode, String dataStr) {
        if (dataStr == null) {
            return atyReportMode;
        }
        String[] Line = dataStr.split(";");
        String[] item;
        for (int i = 0; i < Line.length; i++) {
            item = Line[i].split(":");
            String itemStr = item[0];
            switch (itemStr) {
            // 売上
            case ReportConstants.ATYREPORT_SALES:
                atyReportMode.setSales(item[1]);
                atyReportMode.setSalesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 売上値引
            case ReportConstants.ATYREPORT_SALESDISCOUNT:
                atyReportMode.setSalesDiscount(item[1]);
                atyReportMode.setSalesDiscountPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSalesDiscountAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 取消
            case ReportConstants.ATYREPORT_CANCEL:
                atyReportMode.setCancel(item[1]);
                atyReportMode.setCancelPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setCancelAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 小計
            case ReportConstants.ATYREPORT_SALESSUBTOTAL:
                atyReportMode.setSalesSubTotal(item[1]);
                atyReportMode.setSalesTotalPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSalesTotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 返品売上
            case ReportConstants.ATYREPORT_RETURNSALES:
                atyReportMode.setReturnSales(item[1]);
                atyReportMode.setReturnSalesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setReturnSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 返品値引
            case ReportConstants.ATYREPORT_RETURNDISCOUNT:
                atyReportMode.setReturnDiscount(item[1]);
                atyReportMode.setReturnDiscountPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setReturnDiscountAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 返品取消
            case ReportConstants.ATYREPORT_RETURNCANCEL:
                atyReportMode.setReturnCancel(item[1]);
                atyReportMode.setReturnCancelPoint(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setReturnCancelAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 小計
            case ReportConstants.ATYREPORT_RETURNSUBTOTAL:
                atyReportMode.setReturnSubTotal(item[1]);
                atyReportMode.setReturnTotalPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setReturnTotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 純売上
            case ReportConstants.ATYREPORT_NETSALES:
                atyReportMode.setNetSales(item[1]);
                atyReportMode.setNetSalesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setNetSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 純売上(税抜)
            case ReportConstants.ATYREPORT_TAXNETSALES:
                atyReportMode.setTaxNetSales(item[1]);
                atyReportMode.setTaxNetSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 総売上
            case ReportConstants.ATYREPORT_TOTALSALES:
                atyReportMode.setTotalSales(item[1]);
                atyReportMode.setTotalSalesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setTotalSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 総売上(税抜)
            case ReportConstants.ATYREPORT_TAXTOTALSALES:
                atyReportMode.setTaxTotalSales(item[1]);
                atyReportMode.setTaxTotalSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 客数
            case ReportConstants.ATYREPORT_CUSTOMERS:
                atyReportMode.setCustomers(item[1]);
                atyReportMode.setCustomersNo(StringUtility
                        .convNullToLongZero(item[2]));
                break;
            // 課税(税抜)
            case ReportConstants.ATYREPORT_TAXATION:
                atyReportMode.setTaxation(item[1]);
                atyReportMode.setTaxationPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setTaxationAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 税
            case ReportConstants.ATYREPORT_TAX:
                atyReportMode.setTax(item[1]);
                atyReportMode.setTaxAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 非課税
            case ReportConstants.ATYREPORT_TAXEXEMPTION:
                atyReportMode.setTaxExemption(item[1]);
                atyReportMode.setTaxExemptionPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setTaxExemptionAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 計
            case ReportConstants.ATYREPORT_AMOUNTSUBTOAL:
                atyReportMode.setTaxSubtotal(item[1]);
                atyReportMode.setTaxSubtotalPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setTaxSubtotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // ＜値引内訳＞
            // 明細割引
            case ReportConstants.ATYREPORT_ITEMDISCOUNTS:
                atyReportMode.setItemDiscounts(item[1]);
                atyReportMode.setItemDiscountsPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setItemDiscountsAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 明細値引
            case ReportConstants.ATYREPORT_ITEMNEBIKI:
                atyReportMode.setItemNebiki(item[1]);
                atyReportMode.setItemNebikiPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setItemNebikiAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // イベント
            case ReportConstants.ATYREPORT_EVENTSNAME:
                atyReportMode.setEventsName(item[1]);
                atyReportMode.setEventsPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setEventsAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 社員販売
            case ReportConstants.ATYREPORT_EMPLOYEESALES:
                atyReportMode.setEmployeeSales(item[1]);
                atyReportMode.setEmployeeSalesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setEmployeeSalesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 小計割引
            case ReportConstants.ATYREPORT_SUBTOTALDISCOUNTS:
                atyReportMode.setSubtotalDiscounts(item[1]);
                atyReportMode.setSubtotalDiscountsPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSubtotalDiscountsAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 小計値引
            case ReportConstants.ATYREPORT_SUBTOTANEBIKI:
                atyReportMode.setSubtotalNebiki(item[1]);
                atyReportMode.setSubtotalNebikiPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSubtotalNebikiAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 計
            case ReportConstants.ATYREPORT_DICOUNTSSUBTOTAL:
                atyReportMode.setDiscountsSubtotal(item[1]);
                atyReportMode.setDiscountSubtotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 販売
            case ReportConstants.ATYREPORT_SELL:
                atyReportMode.setSell(item[1]);
                atyReportMode.setSellPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSellAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 販売取消
            case ReportConstants.ATYREPORT_SELLCANCEL:
                atyReportMode.setSellCancel(item[1]);
                atyReportMode.setSellCancelPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSellCancelAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 計
            case ReportConstants.ATYREPORT_SELLSUBTOTAL:
                atyReportMode.setSellSubtotal(item[1]);
                atyReportMode.setSellSubtotalPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setSellSubtotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // ＜前受金＞
            // 前受金
            case ReportConstants.ATYREPORT_ADVANCES:
                atyReportMode.setAdvances(item[1]);
                atyReportMode.setAdvancesPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setAdvancesAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 前受金取消
            case ReportConstants.ATYREPORT_ADVANCESCANCEL:
                atyReportMode.setAdvancesCancel(item[1]);
                atyReportMode.setAdvancesCancelPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setAdvancesCancelAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 計
            case ReportConstants.ATYREPORT_ADVANCESSUBTOTAL:
                atyReportMode.setAdvancesSubtotal(item[1]);
                atyReportMode.setAdvancesSubtotalPoints(StringUtility
                        .convNullToLongZero(item[2]));
                atyReportMode.setAdvancesSubtotalAmt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 現金
			case ReportConstants.ATYREPORT_CASH:
				atyReportMode.setCash(item[1]);
				atyReportMode.setCashPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setCashAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// クレジット
			case ReportConstants.ATYREPORT_CREDIT:
				atyReportMode.setCredit(item[1]);
				atyReportMode.setCreditPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setCreditAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 銀聯
			case ReportConstants.ATYREPORT_UNIONPAY:
				atyReportMode.setUnionPay(item[1]);
				atyReportMode.setUnionPayPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setUnionPayAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// ギフトカード
			case ReportConstants.ATYREPORT_GIFTCARD:
				atyReportMode.setGiftCard(item[1]);
				atyReportMode.setGiftCardPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setGiftCardAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 商品券
			case ReportConstants.ATYREPORT_GIFTCERTIFICATES:
				atyReportMode.setGiftCertificates(item[1]);
				atyReportMode.setGiftCertificatesPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setGiftCertificatesAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 振込入金
			case ReportConstants.ATYREPORT_TRANSFERPAYMENT:
				atyReportMode.setTransferPayment(item[1]);
				atyReportMode.setTransferPaymentPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setTransferPaymentAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;

			// 前受金売上
			case ReportConstants.ATYREPORT_ADVANCESSALES:
				atyReportMode.setAdvancesSales(item[1]);
				atyReportMode.setAdvancesSalesPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setAdvancesSalesAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 計
			case ReportConstants.ATYREPORT_GOLDSPECIESSUBTOTAL:
				atyReportMode.setGoldSpecies(item[1]);
				atyReportMode.setGoldSpeciesPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setGoldSpeciesSubtotal(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 買物券
			case ReportConstants.ATYREPORT_SHOPPINGTICKET:
				atyReportMode.setShoppingTicket(item[1]);
				atyReportMode.setShoppingTicketPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setShoppingTicketAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// ギフト券
			case ReportConstants.ATYREPORT_GIFTVOUCHER:
				atyReportMode.setGiftVoucher(item[1]);
				atyReportMode.setGiftVoucherPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setGiftVoucherAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 伊勢丹
			case ReportConstants.ATYREPORT_ISETAN:
				atyReportMode.setIsetan(item[1]);
				atyReportMode.setIsetanPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setIsetanAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// DC
			case ReportConstants.ATYREPORT_DC:
				atyReportMode.setDCLable(item[1]);
				atyReportMode.setDCLablePoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setDCAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// AMEX
			case ReportConstants.ATYREPORT_AMEX:
				atyReportMode.setAMEXLable(item[1]);
				atyReportMode.setAMEXLablePoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setAMEXAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// JCB
			case ReportConstants.ATYREPORT_JCB:
				atyReportMode.setJCBLable(item[1]);
				atyReportMode.setJCBLablePoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setJCBAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// Diners
			case ReportConstants.ATYREPORT_DINERS:
				atyReportMode.setDinersLable(item[1]);
				atyReportMode.setDinersLablePoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setDinersAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// Iカード
			case ReportConstants.ATYREPORT_ICARD:
				atyReportMode.setICard(item[1]);
				atyReportMode.setICardPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setICardAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 三井
			case ReportConstants.ATYREPORT_MITSUI:
				atyReportMode.setMitsuiLable(item[1]);
				atyReportMode.setMitsuiLablePoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setMitsuiAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 軽井沢
			case ReportConstants.ATYREPORT_KARUIZAWA:
				atyReportMode.setKaruizawa(item[1]);
				atyReportMode.setKaruizawaPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setKaruizawaAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// Chel 1000
			case ReportConstants.ATYREPORT_CHEL1000:
				atyReportMode.setChel1000(item[1]);
				atyReportMode.setChel1000Points(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setChel1000Amt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// Chel 2000
			case ReportConstants.ATYREPORT_CHEL2000:
				atyReportMode.setChel2000(item[1]);
				atyReportMode.setChel2000Points(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setChel2000Amt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// その他
			case ReportConstants.ATYREPORT_SONOTA:
				atyReportMode.setSonota(item[1]);
				atyReportMode.setSonotaPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setSonotaAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 計
			case ReportConstants.ATYREPORT_GIFTCERTIFICATESSUBTOTAL:
				atyReportMode.setGiftCertificatesSubtotal(item[1]);
				atyReportMode.setGiftCertificatesSubtotalPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setGiftCertificatesSubtotalAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 回収金
			case ReportConstants.ATYREPORT_COLLECTED:
				atyReportMode.setCollectedfunds(item[1]);
				atyReportMode.setCollectedfundsPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setCollectedfundAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
			// 払戻金
			case ReportConstants.ATYREPORT_MODOKINN:
				atyReportMode.setModorikinn(item[1]);
				atyReportMode.setModorikinnPoints(StringUtility
						.convNullToLongZero(item[2]));
				atyReportMode.setModorikinnAmt(StringUtility
						.convNullToDoubleZero(item[3]));
				break;
            // 中止回数
            case ReportConstants.ATYREPORT_DISCONTINUATION:
                atyReportMode.setDiscontinuation(item[1]);
                atyReportMode.setDiscontinuationPoints(StringUtility
                        .convNullToLongZero(item[2]));
                break;
            // 両替回数
            case ReportConstants.ATYREPORT_EXCHANGE:
                atyReportMode.setExchange(item[1]);
                atyReportMode.setExchangePoints(StringUtility
                        .convNullToLongZero(item[2]));
                break;
            // 印紙200
            case ReportConstants.ATYREPORT_STAMP200:
                atyReportMode.setStamp200(item[1]);
                atyReportMode.setStamp200Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 印紙400
            case ReportConstants.ATYREPORT_STAMP400:
                atyReportMode.setStamp400(item[1]);
                atyReportMode.setStamp400Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 印紙600
            case ReportConstants.ATYREPORT_STAMP600:
                atyReportMode.setStamp600(item[1]);
                atyReportMode.setStamp600Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 印紙1,000
            case ReportConstants.ATYREPORT_STAMP1000:
                atyReportMode.setStamp1000(item[1]);
                atyReportMode.setStamp1000Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 印紙2,000
            case ReportConstants.ATYREPORT_STAMP2000:
                atyReportMode.setStamp2000(item[1]);
                atyReportMode.setStamp2000Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            // 印紙4,000
            case ReportConstants.ATYREPORT_STAMP4000:
                atyReportMode.setStamp4000(item[1]);
                atyReportMode.setStamp4000Amt(StringUtility
                        .convNullToDoubleZero(item[3]));
                break;
            }
        }
        return atyReportMode;
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