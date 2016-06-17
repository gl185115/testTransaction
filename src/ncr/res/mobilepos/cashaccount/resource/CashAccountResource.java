package ncr.res.mobilepos.cashaccount.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cashaccount.dao.ICashAccountDAO;
import ncr.res.mobilepos.cashaccount.model.CashBalance;
import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.report.model.DailyReport;

@Path("cashaccount")
@Api(value="/cashaccount", description="現金アカウント情報API")
public class CashAccountResource {
	
    /**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Program name, used for logging
     */
    private String progName = "CashAcct";
    
    public CashAccountResource() {
    	this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }
    
    /**
     * Retrieve the cash balance of a drawer given its id
     *
     * @param tillId			drawer id
     * @param storeId			store id
     * 
     * @return GetCashBalance   Cash Balance response
     */
    @Path("/getcashbalance")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="現金残額情報取得", response=GetCashBalance.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE, message="残額未検出")
        })
    public final GetCashBalance getCashBalance(
    		@ApiParam(name="tillid", value="ドロアID") @QueryParam("tillid") final String tillId, 
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeId,
    		@ApiParam(name="businessdaydate", value="営業日") @QueryParam("businessdaydate") final String businessDayDate) {
    	tp.methodEnter("getCashBalance");
    	tp.println("Till Id", tillId)
    		.println("Store Id", storeId)
    		.println("Business Day Date", businessDayDate);
    	
    	GetCashBalance response = new GetCashBalance();
    	
    	try {
    		DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    		ICashAccountDAO cashAccountDAO = sqlServer.getCashAccountDAO();
    		
    		response = cashAccountDAO.getCashBalance(tillId, storeId, 
    				businessDayDate);
    	} catch (DaoException e) {
			LOGGER.logAlert(progName, "getCashBalance", Logger.RES_EXCEP_DAO,
					"Failed to get Cash Balance.\n" + e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
    	} catch (Exception e) {
			LOGGER.logAlert(progName, "getCashBalance",
					Logger.RES_EXCEP_GENERAL, "Failed to get Cash Balance.\n"
							+ e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
    	} finally {
    		tp.methodExit(response.toString());
    	}
    	
    	return response;
    }
    
    /**
     * get cash balance in txu_daily_report
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param businessDate
     * @param trainingFlag
     * @param dataType
     * @param itemLevel1
     * @param itemLevel2
     * @return
     */
    @Path("/getcash")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="現金残額情報取得", response=GetCashBalance.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
            @ApiResponse(code=ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE, message="残額未検出")
        })
    public final GetCashBalance getReportItems(
    		@ApiParam(name="companyId", value="会社コード") @FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @FormParam("storeId") final String storeId,
    		@ApiParam(name="tillId", value="tillコード") @FormParam("tillId") final String tillId,
    		@ApiParam(name="terminalId", value="POSコード") @FormParam("terminalId") final String terminalId,
    		@ApiParam(name="businessDate", value="営業日") @FormParam("businessDate") final String businessDate,
    		@ApiParam(name="trainingFlag", value="トレーニングフラグ") @FormParam("trainingFlag") final int trainingFlag,
    		@ApiParam(name="dataType", value="データ分類") @FormParam("dataType") final String dataType,
    		@ApiParam(name="itemLevel1", value="項目レベル１") @FormParam("itemLevel1")final String itemLevel1,
    		@ApiParam(name="itemLevel2", value="項目レベル２") @FormParam("itemLevel2") final String itemLevel2) {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("trainingFlag", trainingFlag)
          .println("datatype", dataType)
          .println("itemLevel1", itemLevel1)
          .println("itemLevel2", itemLevel2);
        
        GetCashBalance response = new GetCashBalance();
        
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICashAccountDAO cashAccountDAO = sqlServer.getCashAccountDAO();
            
            if (StringUtility.isNullOrEmpty(companyId, storeId, businessDate)) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return response;
            }
            if (StringUtility.isNullOrEmpty(tillId) && StringUtility.isNullOrEmpty(terminalId)) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return response;
            }
            if(StringUtility.isNullOrEmpty(tillId)){
            	response = cashAccountDAO.getCashBalance(companyId, storeId, terminalId, 
                        businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
            }else{
            	response = cashAccountDAO.getCashBalanceByTillId(companyId, storeId, tillId, 
                        businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
            }
        } catch (DaoException e) {
            LOGGER.logAlert(progName, "getCashBalance", Logger.RES_EXCEP_DAO,
                    "Failed to get Cash Balance.\n" + e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(progName, "getCashBalance",
                    Logger.RES_EXCEP_GENERAL, "Failed to get Cash Balance.\n"
                            + e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(response.toString());
        }
        
        return response;
    }
    
      
}
