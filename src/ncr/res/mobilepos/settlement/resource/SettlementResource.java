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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
    @Context
    private ServletContext servletContext;
    
    public SettlementResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), 
        		getClass());
    }
    
    @Path("/getvoucherlist")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="商品券リスト取得", response=SettlementInfo.class)
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
    @ApiOperation(value="トランザクション数取得", response=SettlementInfo.class)
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
    
    @Path("/gettxcountbybusinessdate")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="取引数取得", response=SettlementInfo.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
        })
    public final SettlementInfo getTxCountByBusinessDate(
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗番号")@QueryParam("storeId") final String storeId,
    		@ApiParam(name = "workstationid", value = "ターミナル番号")@QueryParam("workstationid") final String workStationId,
    		@ApiParam(name="txtype", value="取引種別")@QueryParam("txtype") final String txtype,
    		@ApiParam(name = "businessDate", value = "営業日") @QueryParam("businessDate") final String businessDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ")@QueryParam("trainingFlag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("workstationid", workStationId)
            .println("txtype", txtype)
            .println("businessDate", businessDate)
            .println("trainingFlag", trainingFlag);
        SettlementInfo settlement = new SettlementInfo();
        
        if (StringUtility.isNullOrEmpty(companyId, storeId, workStationId, txtype, businessDate)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
        }
        
        try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            settlement = settlementDao.getTxCountByBusinessDate(companyId, storeId, workStationId, txtype, businessDate, trainingFlag);
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
    @ApiOperation(value="クレジット情報取得", response=SettlementInfo.class)
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
    		@ApiParam(name="itemLevel1", value="項目レベル１")@FormParam("itemLevel1")final String itemLevel1){
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("trainingFlag", trainingFlag)
          .println("datatype", dataType)
          .println("itemLevel1", itemLevel1);
        
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
                        businessDate, trainingFlag, dataType, itemLevel1);
            }else{
            	settlement = settlementDao.getCreditByTillId(companyId, storeId, tillId, 
                        businessDate, trainingFlag, dataType, itemLevel1);
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
    
	@Path("/getcountpaymentamt")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value="金種集計情報取得", response=SettlementInfo.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
			@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
		})
	public final SettlementInfo getCountPaymentAmt(
			@ApiParam(name="companyId", value="会社コード")@FormParam("companyId") final String companyId,
			@ApiParam(name="storeId", value="店舗番号")@FormParam("storeId") final String storeId,
			@ApiParam(name="businessDate", value="営業日")@FormParam("businessDate") final String businessDate,
			@ApiParam(name="trainingFlag", value="トレーニングフラグ")@FormParam("trainingFlag") final int trainingFlag,
			@ApiParam(name="terminalId", value="端末番号")@FormParam("terminalId") final String terminalId,
			@ApiParam(name="txType", value="取引種別")@FormParam("txType") final String txType){
		
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyId", companyId)
			.println("storeId", storeId)
			.println("businessDate", businessDate)
			.println("trainingFlag", trainingFlag)
			.println("terminalId", terminalId)
			.println("txType", txType);
		
		SettlementInfo settlement = new SettlementInfo();
		
		if (StringUtility.isNullOrEmpty(companyId, storeId, businessDate)) {
			tp.println("A required parameter is null or empty.");
			settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			tp.methodExit(settlement.toString());
			return settlement;
		}
		try {
			ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
			if (StringUtility.isNullOrEmpty(txType)) {
				settlement = settlementDao.getPaymentAmtByTerminalId(companyId, storeId, businessDate, trainingFlag, terminalId);
			} else {
				settlement = settlementDao.getPaymentAmtByTxType(companyId, storeId, businessDate, trainingFlag, txType);
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
				"Failed to get payment amt for companyId=" + companyId + ", " 
				+ "storeId=" + storeId + ", businessDayDate=" + businessDate + " : " + e.getMessage());
		} finally {
			tp.methodExit(settlement.toString());
		}
		return settlement;
	}
}
