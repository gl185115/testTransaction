/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ForwardItemListResource
 *
 * Resource for Transfer transactions between smart phone and POS
 *
 */
package ncr.res.mobilepos.forwarditemlist.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.IForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountWithCashier;
import ncr.res.mobilepos.forwarditemlist.model.ForwardItemCount;
import ncr.res.mobilepos.forwarditemlist.model.ForwardvoidList;
import ncr.res.mobilepos.forwarditemlist.model.ForwardvoidListInfo;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.dao.ICommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.model.ForwardCashierList;
import ncr.res.mobilepos.journalization.model.ForwardCashierListInfo;
import ncr.res.mobilepos.journalization.model.ForwardCheckerList;
import ncr.res.mobilepos.journalization.model.ForwardCheckerListInfo;
import ncr.res.mobilepos.journalization.model.ForwardList;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.ForwardUnprocessedList;
import ncr.res.mobilepos.journalization.model.ForwardUnprocessedListInfo;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.journalization.model.SearchForwardWithTerminalidPosLog;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Tax;
import ncr.res.mobilepos.journalization.model.poslog.TaxableAmount;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.model.poslog.WorkstationID;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


/**
 * Transfer transactions between smart phone and POS.
 */
@Path("/ItemForward")
@Api(value="/ItemForward", description="前捌きデータのアップロードと取得API")
public class ForwardItemListResource {
    /**
     * The IOWriter for the Log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Instance of SnapLogger for output error transaction data to snap file.
     */
    private SnapLogger snap;
    /**
     * The program name.
     */
    private static final String PROG_NAME = "ForwdItm";

    private static final String Category_TwinChecker = "TwinChecker";
    private static final String KeyId_TwinChecker = "MaxPendingCheckerTran";

    @Context
    private ServletContext context; //to access the web.xml

    /**
     * Constructor.
     */
    public ForwardItemListResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.snap =  (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Sets the DaoFactory of the ForwardItemListResource to
     * use the DAO methods.
     *
     * @param daofactory
     *            - The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daofactory) {
    	//no implementation
    }

    /**
     * The method called by POS to get Forward data.
     * @param storeid      The Store ID
     * @param terminalid   The Terminal ID
     * @param txdate       The BusinessDate
     * @return Forward Item data
     * @deprecated
     */
    @Deprecated
//    @Path("/request")
//    @GET
//    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    public final String requestForwardData(
            @QueryParam("storeid") final String storeid,
            @QueryParam("terminalid") final String terminalid,
            @QueryParam("txdate") final String txdate) {

        tp.methodEnter("requestForwardData");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        String posLogXml = null;

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
        String actualstoreid = String.format("%6s", storeid).replace(" ", "0");

        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            posLogXml = forwardItemListDAO.getShoppingCartData(actualstoreid,
                    terminalid, txdate);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } finally {
            tp.methodExit(posLogXml);
        }
        return posLogXml;
    }

    /**
     * The method called by POS to get Forward data count.
     * @param storeid          The Store ID
     * @param terminalid       The Terminal ID
     * @param txdate           The BusinessDate
     * @return the count of Forward data
     * @deprecated
     */
    @Deprecated
    @Path("/getCount")
    @GET
    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    @ApiOperation(value="前捌き保留数カウント", response=String.class)
    @ApiResponses(value={
    })
    public final String getCount(
            @ApiParam(name="storeid", value="店番号") @QueryParam("storeid") final String storeid,
            @ApiParam(name="terminalid", value="ターミナル番号") @QueryParam("terminalid") final String terminalid,
            @ApiParam(name="txdate", value="業務日付") @QueryParam("txdate") final String txdate) {

        tp.methodEnter("getCount");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
        String actualstoreid = String.format("%6s", storeid).replace(" ", "0");

        ForwardCountData forwardCountData = new ForwardCountData();
        String countXml = null;
        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            forwardCountData = forwardItemListDAO.getForwardCountData(
                    actualstoreid, terminalid, txdate);

            countXml = GlobalConstant.forwardcountdataDataBinding.marshallObj(forwardCountData, "shift_jis");
            countXml = countXml.replace(" standalone=\"yes\"", "");
        } catch (JAXBException e) {
            LOGGER.logAlert(PROG_NAME, "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_JAXB,
                    "Failed to process getCount.\n " + e.getMessage());
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process getCount.\n " + e.getMessage());
        } finally {
            tp.methodExit(countXml);
        }
        return countXml;
    }

    /**
     * The method is called by Mobile to upload the
     * information of Shopping Cart as Forward Item Data.
     * @param poslogXml     The POSLog xml
     * @param terminalNo    The Terminal No
     * @param deviceNo      The Device Number
     * @return The POSLog Response {@see PosLogResp}
     * @deprecated
     */
    @Deprecated
    @Path("/uploadforward")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータのアップロード", response=PosLogResp.class)
    @ApiResponses(value={
    		 @ApiResponse(code=ResultBase.RES_FORWARD_ITEM_NO_INSERT, message="データ挿入失敗"),
    })
    public final PosLogResp uploadForwardData(
    		@ApiParam(name="poslogxml", value="POSLog (xmlデータ)") @FormParam("poslogxml") final String poslogXml,
    		@ApiParam(name="deviceid", value="デバイス番号") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="terminalid", value="ターミナル番号") @FormParam("terminalid") final String terminalNo) {

        tp.methodEnter("uploadForwardData");
        tp.println("POSLogXML", poslogXml).println("DeviceNo", deviceNo).
            println("TerminalNo", terminalNo);

        PosLogResp posLogResponse = null;
        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            posLogResponse = forwardItemListDAO.uploadItemForwardData(deviceNo,
                    terminalNo, poslogXml);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } finally {
            tp.methodExit(posLogResponse);
        }
        return posLogResponse;
    }

    /**
     * 前捌きデータ登録
     * @param poslogxml
     * @param queue
     * @param workstationid
     * @param trainingmode
     * @param total
     * @return ResultBase
     */
    @POST
    @Path("/suspend")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータのアップロード", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="無効なキュー番号"),
    })
    public final ResultBase saveForwardPosLog(
    		@ApiParam(name="poslogxml", value="POSLog (xmlデータ)") @FormParam("poslogxml") final String poslogxml,
    		@ApiParam(name="queue", value="キュー番号") @FormParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationid,
    		@ApiParam(name="trainingmode", value="トレーニングモードフラグ") @FormParam("trainingmode") final String trainingmode,
    		@ApiParam(name="total", value="トータル") @FormParam("total") final String total) {

        String functionName = "saveForwardPosLog";
        tp.methodEnter(functionName).println("poslogxml", poslogxml);
        tp.methodEnter(functionName).println("queue", queue);
        tp.methodEnter(functionName).println("workstationid", workstationid);
        tp.methodEnter(functionName).println("trainingmode", trainingmode);
        tp.methodEnter(functionName).println("total", total);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        ResultBase resultBase = new ResultBase();

        try {
            PosLog posLog = GlobalConstant.poslogDataBinding.unMarshallXml(poslogxml);

            // check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
                tp.println("Required POSLog elements are missing.");
                Snap.SnapInfo info = snap.write("Required POSLog elements are missing.", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, "Invalid POSLog Transaction to snap file", info);
                resultBase.setMessage("Required POSLog elements are missing.");
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                return resultBase;
            }

            // check if valid business date format
            String businessDate = posLog.getTransaction().getBusinessDayDate();
            if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
                tp.println("BusinessDayDate should be in yyyy-MM-dd format.");
                resultBase.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_DATEINVALID);
                resultBase.setMessage("BusinessDayDate should be in yyyy-MM-dd format.");
                return resultBase;
            }

            // save poslog
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO quebusterDAO = dao.getPOSLogDAO();
            int result = quebusterDAO.saveForwardPosLog(posLog, poslogxml, queue, total);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate suspended transaction. It writes out to a snap file.");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }

            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (JAXBException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB, ResultBase.RES_ERROR_JAXB, ex);
        } catch (Exception ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * 前捌きデータ取得
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param Queue
     * @param BusinessDayDate
     * @param TrainingFlag
     * @return
     */
    @Path("/resume")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータ取得", response=SearchForwardPosLog.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="前捌きデータ検索エラー(見付からない)"),
    })
    public final SearchForwardPosLog getForwardItems(@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="店番号") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="WorkstationId", value="ターミナル番号") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="SequenceNumber", value="取引番号") @FormParam("SequenceNumber") String SequenceNumber,
    		@ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
    		@ApiParam(name="BusinessDayDate", value="業務日付") @FormParam("BusinessDayDate") String BusinessDayDate,
    		@ApiParam(name="TrainingFlag", value="トレーニングモードフラグ") @FormParam("TrainingFlag") String TrainingFlag) {
        String functionName = "getForwardItems";
        tp.println("CompanyId", CompanyId);
        tp.println("RetailStoreId", RetailStoreId);
        tp.println("WorkstationId", WorkstationId);
        tp.println("SequenceNumber", SequenceNumber);
        tp.println("Queue", Queue);
        tp.println("BusinessDayDate", BusinessDayDate);
        tp.println("TrainingFlag", TrainingFlag);

        SearchForwardPosLog poslog = new SearchForwardPosLog();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
            poslog = posLogDAO.getForwardItemsPosLog(CompanyId, RetailStoreId, WorkstationId, SequenceNumber, Queue,
                    BusinessDayDate, TrainingFlag);

            if (StringUtility.isNullOrEmpty(poslog.getPosLogXml())) {
                poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                tp.println("Forward poslog not found.");
            } else {
                poslog.setPoslog(GlobalConstant.poslogDataBinding.unMarshallXml(poslog.getPosLogXml()));
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchForwardPosLog) tp.methodExit(poslog);
    }

    /**
     * 前捌きデータ一覧取得
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param TrainingFlag
     * @param LayawayFlag
     * @param Queue
     * @return ResultBase
     */
    @POST
    @Path("/list")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータ一覧取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="不正なリクエストパラメータ"),
    })
    public final ResultBase getForwardList(
    		@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="店番号") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="TrainingFlag", value="トレーニングモードフラグ") @FormParam("TrainingFlag") String TrainingFlag,
    		@ApiParam(name="LayawayFlag", value="予約フラグ") @FormParam("LayawayFlag") String LayawayFlag,
    		@ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
            @ApiParam(name="TxType", value="取引タイプ") @FormParam("TxType") String TxType) {
        String functionName = "getForwardList";
        tp.println("CompanyId", CompanyId);
        tp.println("StoreCode", RetailStoreId);
        tp.println("Training", TrainingFlag);
        tp.println("LayawayFlag", LayawayFlag);
        tp.println("Queue", Queue);
        tp.println("TxType", TxType);

        ForwardList result = new ForwardList();
        try {
            if (StringUtility.isNullOrEmpty(RetailStoreId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }
            
            SystemSettingResource sysSetting = new SystemSettingResource();       
            DateSetting dateSetting = sysSetting.getDateSetting(CompanyId, RetailStoreId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String BussinessDayData = dateSetting.getToday();

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            List<ForwardListInfo> forwardList = iCommonDAO.getForwardList(CompanyId, RetailStoreId,
                    TrainingFlag, LayawayFlag, Queue, TxType, BussinessDayData);
            result.setForwardListInfo(forwardList);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get forward list.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardList) tp.methodExit(result);
    }

    /**
     * 前捌きデータ数カウント
     * @param companyId
     * @param storeId
     * @param businessDayDate
     * @param workstationId
     * @param queue
     * @param trainingFlag
     * @return Forward Item Count
     */
    @GET
    @Path("/getforwarditemcount")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="前捌き保留数カウント取得", response=ForwardItemCount.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ForwardItemCount getForwardItemCount(
    	@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    	@ApiParam(name="storeId", value="店番号") @QueryParam("storeId") final String storeId,
    	@ApiParam(name="businessDayDate", value="業務日付") @QueryParam("businessDayDate") final String businessDayDate,
    	@ApiParam(name="workstationId", value="ターミナル番号") @QueryParam("workstationId") final String workstationId,
    	@ApiParam(name="queue", value="キュー番号") @QueryParam("queue") final String queue,
    	@ApiParam(name="trainingFlag", value="トレーニングモードフラグ") @QueryParam("trainingFlag") final String trainingFlag) {
        String functionName = "getForwardItemCount";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDayDate", businessDayDate)
            .println("workstationId", workstationId)
            .println("queue", queue)
            .println("trainingFlag", trainingFlag);

        String count = null;
        ForwardItemCount result = new ForwardItemCount();
        try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = dao.getForwardItemListDAO();
            count = forwardItemListDAO.selectForwardItemCount(companyId, storeId,
                    businessDayDate, workstationId, queue, trainingFlag);
            result.setCount(count);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to Select Forward Item Count of"
                    + " CompanyId=" + companyId
                    + ";StoreId=" + storeId
                    + ";Businessdaydate=" + businessDayDate
                    + ";WorkstationId=" + workstationId
                    + ";Queue=" + queue
                    + ";trainingFlag=" + trainingFlag
                    , daoEx);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(daoEx.getMessage());
        } catch(Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,functionName
                    + ": Failed to Select Forward Item Count of"
                    + " CompanyId=" + companyId
                    + ";StoreId=" + storeId
                    + ";Businessdaydate=" + businessDayDate
                    + ";WorkstationId=" + workstationId
                    + ";Queue=" + queue
                    + ";trainingFlag=" + trainingFlag
                    , ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        } finally {
            tp.methodExit();
        }
        return result;
    }

    /**
     * 前捌きデータステータス更新
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param Queue
     * @param BusinessDayDate
     * @param TrainingFlag
     * @param Status
     * @return ResultBase
     */
    @Path("/request")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータステータス更新", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ResultBase updateForwardStatus(
    		@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="店番号") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="WorkstationId", value="ターミナル番号") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="SequenceNumber", value="取引番号") @FormParam("SequenceNumber") String SequenceNumber,
    		@ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
    		@ApiParam(name="BusinessDayDate", value="業務日付") @FormParam("BusinessDayDate") String BusinessDayDate,
    		@ApiParam(name="TrainingFlag", value="トレーニングモードフラグ") @FormParam("TrainingFlag") String TrainingFlag,
    		@ApiParam(name="Status", value="ステータス") @FormParam("Status") int Status) {
        String functionName = "updateForwardStatus";
        tp.println("CompanyId", CompanyId);
        tp.println("RetailStoreId", RetailStoreId);
        tp.println("WorkstationId", WorkstationId);
        tp.println("SequenceNumber", SequenceNumber);
        tp.println("Queue", Queue);
        tp.println("BusinessDayDate", BusinessDayDate);
        tp.println("TrainingFlag", TrainingFlag);
        tp.println("Status", Status);

        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            int result = iCommonDAO.updateForwardStatus(CompanyId, RetailStoreId, WorkstationId, SequenceNumber,
                    Queue, BusinessDayDate, TrainingFlag, Status);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Failed to update Forward status.\n");
            }
            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to update Forward status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Forward status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }
    
    /**
     * 前捌き ユーザーの権限を取得
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param OperatorId
     */
    @POST
    @Path("/userPermission")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きユーザーの権限取得", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="不正なリクエストパラメータ"),
    })
    public final Operator UserPermission(
    		@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="店番号") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="WorkstationId", value="ターミナル番号") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="OperatorId", value="ユーザーID") @FormParam("OperatorId") String OperatorId) {
    	
    	CredentialResource CredenRou = new CredentialResource();
    	Operator operator = CredenRou.getStatusOfOperator(CompanyId,OperatorId);
    	operator.setOperatorNo(OperatorId);
    	
        return operator;
    }

    /**
     * タグ番号で前捌きデータ登録
     * @param companyid
     * @param retailstoreid
     * @param queue
     * @param workstationid
     * @param trainingmode
     * @param tag
     * @param total
     * @param poslogxml
     * @return ResultBase
     */
    @POST
    @Path("/SuspendWithTag")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="タグ番号による前捌きデータのアップロード", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="無効なキュー番号"),
    })
    public final ResultBase saveForwardPosLogIncludeTag(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="queue", value="キュー番号") @FormParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="trainingmode", value="トレーニングモードフラグ") @FormParam("trainingmode") final String trainingMode,
    		@ApiParam(name="tag", value="タグ番号") @FormParam("tag") final String tag,
    		@ApiParam(name="total", value="合計金額") @FormParam("total") final String total,
    		@ApiParam(name="poslogxml", value="POSLog (xmlデータ)") @FormParam("poslogxml") final String posLogXml) {

    	String functionName = "saveForwardPosLogIncludeTag";
        tp.methodEnter(functionName)
          .println("companyid", companyId)
          .println("retailstoreid", retailStoreId)
          .println("queue", queue)
          .println("workstationid", workstationId)
          .println("trainingmode", trainingMode)
          .println("tag", tag)
          .println("total", total)
          .println("poslogxml", posLogXml);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        ResultBase resultBase = new ResultBase();
        
        try {
            PosLog posLog = GlobalConstant.poslogDataBinding.unMarshallXml(posLogXml);

            // check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
                tp.println("Required POSLog elements are missing.");
                Snap.SnapInfo info = snap.write("Required POSLog elements are missing.", posLogXml);
                LOGGER.logSnap(PROG_NAME, functionName, "Invalid POSLog Transaction to snap file", info);
                resultBase.setMessage("Required POSLog elements are missing.");
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                return resultBase;
            }

            // check if valid business date format
            String businessDate = posLog.getTransaction().getBusinessDayDate();
            if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
                tp.println("BusinessDayDate should be in yyyy-MM-dd format.");
                resultBase.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_DATEINVALID);
                resultBase.setMessage("BusinessDayDate should be in yyyy-MM-dd format.");
                return resultBase;
            }
            
            // save poslog
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO quebusterDAO = dao.getPOSLogDAO();
            int result = quebusterDAO.saveForwardPosLogIncludeTag(posLog, posLogXml, queue, tag, total);
            	
            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate suspended transaction. It writes out to a snap file.");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", posLogXml);
                LOGGER.logSnap(PROG_NAME, functionName, "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }

            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (JAXBException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB, ResultBase.RES_ERROR_JAXB, ex);
        } catch (Exception ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }
    
    /**
     * 	タグ番号で前捌きデータ取得
     *
     * @param companyId     	The Company ID
     * @param retailStoreId		The Retail Store ID
     * @param queue     		The Queue from POS
     * @param businessDayDate	The BusinessDay Date
     * @param tag				The Ext1
     * @param trainingflag		The TrainingFlag
     * @return SearchForwardPosLog
     */
    @GET
    @Path("/ResumeWithTag")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="タグ番号で前捌きデータ取得", response=SearchForwardPosLog.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="前捌きデータ取得エラー (見付からない)")
        })
    public final SearchForwardPosLog getForwardItemsWithTag(
    		@ApiParam(name="CompanyId", value="会社コード") @QueryParam("CompanyId") final String companyId,
    		@ApiParam(name="RetailStoreId", value="店番号") @QueryParam("RetailStoreId") final String retailStoreId,
    		@ApiParam(name="Queue", value="キュー番号") @QueryParam("Queue") final String queue,
    		@ApiParam(name="Businessdaydate", value="業務日付") @QueryParam("Businessdaydate") final String businessDayDate,
    		@ApiParam(name="tag", value="タグ番号") @QueryParam("tag") final String tag,
    		@ApiParam(name="trainingflag", value="トレーニングモードフラグ") @QueryParam("trainingflag") final String trainingFlag) {
        String functionName = "getForwardItemsWithTag";
        tp.println("CompanyId", companyId);
        tp.println("RetailStoreId", retailStoreId);
        tp.println("Queue", queue);
        tp.println("Businessdaydate", businessDayDate);
        tp.println("tag", tag);
        tp.println("trainingflag", trainingFlag);

        SearchForwardPosLog poslog = new SearchForwardPosLog();
        try {
               DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
               IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
               poslog = posLogDAO.getForwardItemsPosLogWithTag(companyId, retailStoreId,
                		queue, businessDayDate, tag, trainingFlag);

               if (StringUtility.isNullOrEmpty(poslog.getPosLogXml())) {
                    poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                    tp.println("Forward poslog with tag not found.");
               } else {
                    poslog.setPoslog(GlobalConstant.poslogDataBinding.unMarshallXml(poslog.getPosLogXml()));
               }
		} catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get poslog xml with tag", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get poslog xml with tag", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchForwardPosLog) tp.methodExit(poslog);
    }

    /**
     * ターミナル番号で前捌きデータ登録
     * @param companyid
     * @param retailstoreid
     * @param poslogxml
     * @param queue
     * @param workstationid
     * @param trainingflag
     * @param total
     * @param cashierid
     * @return ResultBase
     */
    @POST
    @Path("/suspendWithTerminalid")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="ターミナル番号で前捌きデータ登録", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="リクエストパラメータが不正"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="無効なキュー番号"),
    })
    public final ResultBase saveForwardPosLogIncludeTerminalid(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="poslogxml", value="POSLog Xml") @FormParam("poslogxml") final String posLogXml,
    		@ApiParam(name="queue", value="キュー番号") @FormParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="trainingflag", value="トレーニングモード") @FormParam("trainingflag") final String trainingFlag,
    		@ApiParam(name="total", value="トータル") @FormParam("total") final String total,
    		@ApiParam(name="cashierid", value="精算機の端末番号") @FormParam("cashierid") final String cashierId ) {

    	String functionName = "saveForwardPosLogIncludeTerminalid";
        tp.methodEnter(functionName)
          .println("companyid", companyId)
          .println("retailstoreid", retailStoreId)
          .println("poslogxml", posLogXml)
          .println("queue", queue)
          .println("workstationid", workstationId)
          .println("trainingflag", trainingFlag)
          .println("total", total)
          .println("cashierid", cashierId);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        ResultBase resultBase = new ResultBase();
        int maxPending = 0;

        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, queue, workstationId, trainingFlag, total, cashierId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }

            // unmarshall poslog xml
            XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
            PosLog posLog = poslogSerializer.unMarshallXml(posLogXml, PosLog.class);

            // check if valid business date format
            String businessDate = posLog.getTransaction().getBusinessDayDate();
            if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
                tp.println("BusinessDayDate should be in yyyy-MM-dd format.");
                resultBase.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_DATEINVALID);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESSYS_ERROR_QB_DATEINVALID);
                resultBase.setMessage("BusinessDayDate should be in yyyy-MM-dd format.");
                return resultBase;
            }

            // check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
                tp.println("Required POSLog elements are missing.");
                Snap.SnapInfo info = snap.write("Required POSLog elements are missing.", posLogXml);
                LOGGER.logSnap(PROG_NAME, functionName, "Invalid POSLog Transaction to snap file", info);
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                resultBase.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return resultBase;
            }

            // get TwinCheckerCategory
            SQLServerSystemConfigDAO systemDao = dao.getSystemConfigDAO();
            String temMaxPending = systemDao.getParameterValue(KeyId_TwinChecker, Category_TwinChecker);
            if (StringUtility.isNullOrEmpty(temMaxPending)) {
            	maxPending = 3;
            }else if (!temMaxPending.matches("\\d+") || Integer.parseInt(temMaxPending) < 1) {
            	maxPending = 3;
            }else
            {
                maxPending = Integer.parseInt(temMaxPending);
            }

            // get Pending Count
            IPosLogDAO posLogDAO = dao.getPOSLogDAO();
            int maxCount = posLogDAO.getForwardItemsPendingCount(posLog, companyId, retailStoreId, queue, cashierId, trainingFlag);

            if(maxCount >= maxPending)
            {
                resultBase.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_QUEUEFULL);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESSYS_ERROR_QB_QUEUEFULL);
                resultBase.setMessage("Too many pending transactions");
                return resultBase;
            }

            // save poslog
            IPosLogDAO quebusterDAO = dao.getPOSLogDAO();
            int result = quebusterDAO.saveForwardPosLogIncludeTerminalid(posLog, posLogXml, queue, cashierId, total);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate suspended transaction. It writes out to a snap file.");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", posLogXml);
                LOGGER.logSnap(PROG_NAME, functionName, "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }

            resultBase.setNCRWSSExtendedResultCode(result);
            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            resultBase.setMessage(ex.getMessage());
        } catch (JAXBException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_JAXB);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_JAXB);
            resultBase.setMessage(ex.getMessage());
        } catch (Exception ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * 精算機情報一覧取得
     *
     * @param companyid
     * @param retailstoreid
     * @param ipadress
     * @param trainingflag
     * @return ResultBase
     */
    @POST
    @Path("/cashierList")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="精算機情報一覧取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ResultBase getCashierList(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final String trainingFlag,
    		@ApiParam(name="terminalId", value="チェッカーの端末番号") @FormParam("terminalId") final String terminalId ) throws DaoException {

        String functionName = "getCashierList";
        tp.println("companyid", companyId);
        tp.println("retailstoreid", retailStoreId);
        tp.println("trainingflag", trainingFlag);
        tp.println("terminalId", terminalId);

        ForwardCashierList result = new ForwardCashierList();
        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, trainingFlag, terminalId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            List<ForwardCashierListInfo> forwardCashierList = iCommonDAO.getForwardCashierList(companyId, retailStoreId, trainingFlag, terminalId);
            result.setForwardCashierListInfo(forwardCashierList);
            result.setCount(forwardCashierList.size());
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get forward list.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardCashierList) tp.methodExit(result);
    }

    /**
     * 	ターミナル番号で前捌きデータ取得
     *
     * @param companyid
     * @param retailstoreid
     * @param workstationid
     * @param queue
     * @param businessDayDate
     * @param cashierid
     * @param trainingflag
     * @return SearchForwardPosLog
     */
    @Path("/resumeWithTerminalid")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="ターミナル番号で前捌きデータ取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="取引データ未検出"),
    })
    public final ResultBase getForwardItemsWithTerminalid(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") String companyId,
    		@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") String retailStoreId,
    		@ApiParam(name="queue", value="キュー番号") @FormParam("queue") String queue,
    		@ApiParam(name="businessdaydate", value="POS業務日付") @FormParam("businessdaydate") String businessDayDate,
    		@ApiParam(name="cashierid", value="精算機の端末番号") @FormParam("cashierid") String cashierId,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") String trainingFlag) {
        String functionName = "getForwardItemsWithTerminalid";
        tp.println("companyid", companyId);
        tp.println("retailstoreid", retailStoreId);
        tp.println("queue", queue);
        tp.println("businessdaydate", businessDayDate);
        tp.println("cashierid", cashierId);
        tp.println("trainingflag", trainingFlag);

        SearchForwardWithTerminalidPosLog poslog = new SearchForwardWithTerminalidPosLog();
        try {
               DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
               IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
               poslog = posLogDAO.getForwardItemsPosLogWithTerminalid(companyId, retailStoreId,
                		queue, businessDayDate, cashierId, trainingFlag);

               if (StringUtility.isNullOrEmpty(poslog.getPosLogXml())) {
                    tp.println("No suspend transaction found");
                    poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                    poslog.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                    poslog.setMessage("No suspend transaction found");
                    return poslog;
               } else {
                    XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
                    poslog.setPoslog(poslogSerializer.unMarshallXml(poslog.getPosLogXml(), PosLog.class));
               }
               poslog.setNCRWSSResultCode(ResultBase.RESRPT_OK);
               poslog.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
               poslog.setMessage(ResultBase.RES_SUCCESS_MSG);
		} catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get poslog xml with terminalid", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            poslog.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            poslog.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get poslog xml with terminalid", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            poslog.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            poslog.setMessage(ex.getMessage());
        }
        return (SearchForwardWithTerminalidPosLog) tp.methodExit(poslog);
    }

    /**
     * 商品登録機情報一覧取得
     *
     * @param companyid
     * @param retailstoreid
     * @param terminalid
     * @param trainingflag
     * @return ResultBase
     */
    @POST
    @Path("/checkerList")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="精算機情報一覧取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ResultBase getCheckerList(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final String trainingFlag,
    		@ApiParam(name="terminalid", value="ターミナル番号") @FormParam("terminalid") final String workstationId ) throws DaoException {

        String functionName = "getCheckerList";
        tp.println("companyid", companyId);
        tp.println("retailstoreid", retailStoreId);
        tp.println("trainingflag", trainingFlag);
        tp.println("terminalid", workstationId);

        ForwardCheckerList result = new ForwardCheckerList();
        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, trainingFlag, workstationId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            List<ForwardCheckerListInfo> forwardCheckerList = iCommonDAO.getForwardCheckerList(companyId, retailStoreId, trainingFlag, workstationId);
            result.setForwardCheckerListInfo(forwardCheckerList);
            result.setCount(forwardCheckerList.size());
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get forward list.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage("Database error");
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardCheckerList) tp.methodExit(result);
    }

    /**
     * 前捌未処理データ一覧取得
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param TrainingFlag
     * @param LayawayFlag
     * @param Queue
     * @return ResultBase
     */
    @POST
    @Path("/unprocessedList")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌きデータ一覧取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ResultBase getForwardUnprocessedList(
    		@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="店舗コード") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="Terminalid", value="ターミナル番号") @FormParam("Terminalid") String Terminalid,
    		@ApiParam(name="TrainingFlag", value="トレーニングフラグ") @FormParam("TrainingFlag") String TrainingFlag,
    		@ApiParam(name="LayawayFlag", value="予約フラグ") @FormParam("LayawayFlag") String LayawayFlag,
    		@ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
            @ApiParam(name="TxType", value="取引タイプ") @FormParam("TxType") String TxType) {
        String functionName = "getForwardUnprocessedList";
        tp.println("CompanyId", CompanyId);
        tp.println("StoreCode", RetailStoreId);
        tp.println("Terminalid", Terminalid);
        tp.println("Training", TrainingFlag);
        tp.println("LayawayFlag", LayawayFlag);
        tp.println("Queue", Queue);
        tp.println("TxType", TxType);

        ForwardUnprocessedList result = new ForwardUnprocessedList();
        try {
            if (StringUtility.isNullOrEmpty(CompanyId, RetailStoreId, Terminalid, TrainingFlag, LayawayFlag, Queue, TxType)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            SystemSettingResource sysSetting = new SystemSettingResource();
            DateSetting dateSetting = sysSetting.getDateSetting(CompanyId, RetailStoreId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String BussinessDayData = dateSetting.getToday();

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            List<ForwardUnprocessedListInfo> forwardList = iCommonDAO.getForwardUnprocessedList(CompanyId, RetailStoreId, Terminalid,
                    TrainingFlag, LayawayFlag, Queue, TxType, BussinessDayData);
            result.setForwardUnprocessedListInfo(forwardList);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get forward unprocessedList.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward unprocessedList.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardUnprocessedList) tp.methodExit(result);
    }

    /**
     * 呼出取消データ取得
     *
     * @param companyid
     * @param retailstoreid
     * @param workstationid
     * @param trainingflag
     * @return ResultBase
     */
    @POST
    @Path("/resumevoidList")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="呼出取消データ取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ResultBase getResumeVoidList(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") String retailStoreId,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") String workStationId,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") String trainingFlag) {
        String functionName = "getResumeVoidList";
        tp.println("companyid", companyId);
        tp.println("retailstoreid", retailStoreId);
        tp.println("workstationid", workStationId);
        tp.println("trainingflag", trainingFlag);

        ForwardvoidList result = new ForwardvoidList();

        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, workStationId, trainingFlag)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = dao.getForwardItemListDAO();
            List<ForwardvoidListInfo> forwardVoidList = forwardItemListDAO.getForwardResumeVoidList(companyId, retailStoreId, workStationId, trainingFlag);
            result.setForwardvoidListInfo(forwardVoidList);
            result.setCount(forwardVoidList.size());
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get resumeVoidList.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get resumeVoidList.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardvoidList) tp.methodExit(result);
    }

    /**
     * 前捌保留件数取得（精算機ごと）
     *
     * @param companyid
     * @param retailstoreid
     * @param cashierid
     * @param trainingflag
     * @return ResultBase
     */
    @POST
    @Path("/getCountWithCashier")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌保留件数取得", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ResultBase getForwardCountWithCashier(
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") String companyId,
    		@ApiParam(name="retailstoreid", value="店番号") @FormParam("retailstoreid") String retailStoreId,
    		@ApiParam(name="cashierid", value="精算機の端末番号") @FormParam("cashierid") String cashierId,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") String trainingFlag) {
        String functionName = "getForwardCountWithCashier";
        tp.println("companyid", companyId);
        tp.println("retailstoreid", retailStoreId);
        tp.println("cashierid", cashierId);
        tp.println("trainingflag", trainingFlag);

        String count = null;
        ForwardCountWithCashier result = new ForwardCountWithCashier();

        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, cashierId, trainingFlag)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = dao.getForwardItemListDAO();
            count = forwardItemListDAO.getForwardCountWithCashier(companyId, retailStoreId, cashierId, trainingFlag);
            result.setCount(count);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get countwithcashier.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get countwithcashier.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardCountWithCashier) tp.methodExit(result);
    }
}
