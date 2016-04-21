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
    public final SettlementInfo getCreditSummary(
    		@QueryParam("companyId") final String companyId,
    		@QueryParam("storeId") final String storeId,
    		@QueryParam("businessDayDate") final String businessDayDate,
    		@QueryParam("trainingFlag") final int trainingFlag) {
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
    public final SettlementInfo getVoucherList(
    		@QueryParam("companyId") final String companyId,
    		@QueryParam("storeId") final String storeId,
    		@QueryParam("terminalId") final String terminalId,
    		@QueryParam("businessDayDate") final String businessDayDate,
    		@QueryParam("trainingFlag") final int trainingFlag) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        	.println("companyId", companyId)
        	.println("storeId", storeId)
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
    	
    	try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            settlement = settlementDao.getVoucherList(companyId, storeId, 
            		businessDayDate, terminalId, trainingFlag);
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
    public final SettlementInfo getTransactionCount(
        @QueryParam("companyId") final String companyId,
        @QueryParam("storeId") final String storeId,
        @QueryParam("txtype") final String txtype,
        @QueryParam("trainingFlag") final int trainingFlag) {
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
    public final SettlementInfo getCredit(
            @FormParam("companyId") final String companyId,
            @FormParam("storeId") final String storeId,
            @FormParam("terminalId") final String terminalId,
            @FormParam("businessDate") final String businessDate,
            @FormParam("trainingFlag") final int trainingFlag,
            @FormParam("dataType") final String dataType,
            @FormParam("itemLevel1")final String itemLevel1,
            @FormParam("itemLevel2") final String itemLevel2) {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("trainingFlag", trainingFlag)
          .println("datatype", dataType)
          .println("itemLevel1", itemLevel1)
          .println("itemLevel2", itemLevel2);
        
        SettlementInfo settlement = new SettlementInfo();
        
        if (StringUtility.isNullOrEmpty(companyId, storeId, businessDate, terminalId)) {
            tp.println("A required parameter is null or empty.");
            settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(settlement.toString());
            return settlement;
        }
        
        try {
            ISettlementInfoDAO settlementDao = daoFactory.getSettlementInfoDAO();
            settlement = settlementDao.getCredit(companyId, storeId, terminalId, 
                    businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
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
