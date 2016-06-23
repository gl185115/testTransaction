package ncr.res.mobilepos.settlement.resource;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO;
import ncr.res.mobilepos.settlement.model.SettlementInfo;

@Path("/settlement")
@Api(value="/Settlement", description="決済API")
public class SettlementResource {
	private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private DAOFactory daoFactory;
    private static final String PROG_NAME = "SettlementResource";
    private static final String PATH_NAME = "settlement";
    @Context
    private ServletContext servletContext;

    public SettlementResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
        		getClass());
    }

    @Path("/getcreditsummary")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クレジットの概要を得る", response=SettlementInfo.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CREDIT_SUMMARY_NOT_FOUND, message="特定営業日の日付ためにクレジットサマリーが見つからない"),
        })
    public final SettlementInfo getCreditSummary(
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@QueryParam("storeId") final String storeId,
    		@ApiParam(name="businessDayDate", value="営業日")@QueryParam("businessDayDate") final String businessDayDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@QueryParam("trainingFlag") final int trainingFlag) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        	.println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("businessDayDate", businessDayDate)
        	.println("trainingFlag", trainingFlag);
        SettlementInfo settlement = new SettlementInfo();

    	if (StringUtility.isNullOrEmpty(companyId, storeId, businessDayDate)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
    	}

    	try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            settlement = settlementDao.getCreditSummary(companyId, storeId, businessDayDate, trainingFlag);
    	} catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
            	loggerErrorCode = Logger.RES_EXCEP_DAO;
            	resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
            	loggerErrorCode = Logger.RES_EXCEP_DAO;
            	resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
            	loggerErrorCode = Logger.RES_EXCEP_GENERAL;
            	resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            settlement.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
            	"Failed to get credit summary for companyId=" + companyId + ", "
            	+ "storeId=" + storeId + ", businessDayDate=" + businessDayDate + " and "
            	+ "trainingFlag=" + trainingFlag + " : " + e.getMessage());
    	} finally {
    		tp.methodExit(settlement.toString());
    	}
    	return settlement;
    }

    @Path("/getvoucherlist")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="商品券リストを取得する", response=SettlementInfo.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
        })
    public final SettlementInfo getVoucherList(
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@QueryParam("storeId") final String storeId,
    		@ApiParam(name="tillid", value="ドロワーコード")@QueryParam("tillId") final String tillId,
    		@ApiParam(name="terminalId", value="端末番号")@QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="businessDayDate", value="営業日")@QueryParam("businessDayDate") final String businessDayDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@QueryParam("trainingFlag") final int trainingFlag) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        	.println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("tillId", tillId)
        	.println("businessDayDate", businessDayDate)
        	.println("terminalId", terminalId)
        	.println("trainingFlag", trainingFlag);
        SettlementInfo settlement = new SettlementInfo();

    	if (StringUtility.isNullOrEmpty(companyId, storeId, businessDayDate)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
    	}
    	if(StringUtility.isNullOrEmpty(tillId) && StringUtility.isNullOrEmpty(terminalId)){
    		tp.println("tillId and terminalId both are is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
    	}
    	try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            if(StringUtility.isNullOrEmpty(tillId)){
            	settlement = settlementDao.getVoucherList(companyId, storeId,
                		businessDayDate, terminalId, trainingFlag);
            }else{
            	settlement = settlementDao.getVoucherListByTillId(companyId, storeId,
                		businessDayDate, tillId, trainingFlag);
            }

    	} catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
            	loggerErrorCode = Logger.RES_EXCEP_DAO;
            	resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
            	loggerErrorCode = Logger.RES_EXCEP_DAO;
            	resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
            	loggerErrorCode = Logger.RES_EXCEP_GENERAL;
            	resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            settlement.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
            	"Failed to get voucher list for companyId=" + companyId + ", "
            	+ "storeId=" + storeId + ", businessDayDate=" + businessDayDate + " and "
            	+ "trainingFlag=" + trainingFlag + " : " + e.getMessage());
    	} finally {
    		tp.methodExit(settlement.toString());
    	}
    	return settlement;
    }

    @Path("/gettransactioncount")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="トランザクションの数を得る", response=SettlementInfo.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
        })
    public final SettlementInfo getTransactionCount(
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@QueryParam("storeId") final String storeId,
    		@ApiParam(name="txtype", value="取引種別")@QueryParam("txtype") final String txtype,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@QueryParam("trainingFlag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("txtype", txtype)
            .println("trainingFlag", trainingFlag);
        SettlementInfo settlement = new SettlementInfo();

        if (StringUtility.isNullOrEmpty(companyId, storeId, txtype)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
        }

        try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            settlement = settlementDao.getTransactionCount(companyId, storeId, txtype, trainingFlag);
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            settlement.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                "Failed to get EOD count for companyId=" + companyId + ", "
                + "storeId=" + storeId + ", and "
                + "trainingFlag=" + trainingFlag + " : " + e.getMessage());
        } finally {
            tp.methodExit(settlement.toString());
        }
        return settlement;
    }
    @Path("/getcredit")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クレジット", response=SettlementInfo.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CREDIT_SUMMARY_NOT_FOUND, message="特定営業日の日付ためにクレジットサマリーが見つからない"),
        })
    public final SettlementInfo getCredit(
    		@ApiParam(name="companyId", value="会社コード")@FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@FormParam("storeId") final String storeId,
    		@ApiParam(name="tillId", value="ドロワーコード")@FormParam("tillId") final String tillId,
    		@ApiParam(name="terminalId", value="端末番号")@FormParam("terminalId") final String terminalId,
    		@ApiParam(name="businessDate", value="営業日")@FormParam("businessDate") final String businessDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@FormParam("trainingFlag") final int trainingFlag,
    		@ApiParam(name="dataType", value="データ種別")@FormParam("dataType") final String dataType,
    		@ApiParam(name="itemLevel1", value="項目レベル１")@FormParam("itemLevel1")final String itemLevel1,
    		@ApiParam(name="itemLevel2", value="項目レベル２")@FormParam("itemLevel2") final String itemLevel2){

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("trainingFlag", trainingFlag)
          .println("datatype", dataType)
          .println("itemLevel1", itemLevel1)
          .println("itemLevel2", itemLevel2);

        SettlementInfo settlement = new SettlementInfo();

        if (StringUtility.isNullOrEmpty(companyId, storeId, businessDate)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
        }
        if(StringUtility.isNullOrEmpty(tillId) && StringUtility.isNullOrEmpty(terminalId)){
        	tp.println("tillId and terminalId both are null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
        }
        try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            if(StringUtility.isNullOrEmpty(tillId)){
            	settlement = settlementDao.getCredit(companyId, storeId, terminalId,
                        businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
            }else{
            	settlement = settlementDao.getCreditByTillId(companyId, storeId, tillId,
                        businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
            }
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            settlement.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                "Failed to get credit  for companyId=" + companyId + ", "
                + "storeId=" + storeId + ", businessDayDate=" + businessDate + " and "
                + "trainingFlag=" + trainingFlag + " : " + e.getMessage());
        } finally {
            tp.methodExit(settlement.toString());
        }
        return settlement;
    }

}
