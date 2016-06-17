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
@Api(value="/cashaccount", description="�����A�J�E���g���API")
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
    @ApiOperation(value="�����c�z���擾", response=GetCashBalance.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE, message="�c�z�����o")
        })
    public final GetCashBalance getCashBalance(
    		@ApiParam(name="tillid", value="�h���AID") @QueryParam("tillid") final String tillId, 
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @QueryParam("storeid") final String storeId,
    		@ApiParam(name="businessdaydate", value="�c�Ɠ�") @QueryParam("businessdaydate") final String businessDayDate) {
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
    @ApiOperation(value="�����c�z���擾", response=GetCashBalance.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
            @ApiResponse(code=ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE, message="�c�z�����o")
        })
    public final GetCashBalance getReportItems(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @FormParam("storeId") final String storeId,
    		@ApiParam(name="tillId", value="till�R�[�h") @FormParam("tillId") final String tillId,
    		@ApiParam(name="terminalId", value="POS�R�[�h") @FormParam("terminalId") final String terminalId,
    		@ApiParam(name="businessDate", value="�c�Ɠ�") @FormParam("businessDate") final String businessDate,
    		@ApiParam(name="trainingFlag", value="�g���[�j���O�t���O") @FormParam("trainingFlag") final int trainingFlag,
    		@ApiParam(name="dataType", value="�f�[�^����") @FormParam("dataType") final String dataType,
    		@ApiParam(name="itemLevel1", value="���ڃ��x���P") @FormParam("itemLevel1")final String itemLevel1,
    		@ApiParam(name="itemLevel2", value="���ڃ��x���Q") @FormParam("itemLevel2") final String itemLevel2) {
        
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
