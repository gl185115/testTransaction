package ncr.res.mobilepos.cashaccount.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cashaccount.dao.ICashAccountDAO;
import ncr.res.mobilepos.cashaccount.model.CashBalance;
import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

@Path("cashaccount")
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
    public final GetCashBalance getCashBalance(
    		@QueryParam("tillid") final String tillId, 
    		@QueryParam("storeid") final String storeId,
    		@QueryParam("businessdaydate") final String businessDayDate) {
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

}
