package ncr.res.mobilepos.poslogstatus.resource;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.poslogstatus.dao.IPoslogStatusDAO;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;

@Path("/poslogstatus")
@Api(value="/poslogstatus", description="poslogステータスAPI")
public class PoslogStatusResource {

	/**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    
    /**
	 * The Program Name.
	 */
	private static final String PROG_NAME = "PoslogStatus";
	/**
     * context.
     */
    @Context
    private ServletContext context;

    /**
     * Constructor.
     */
    public PoslogStatusResource() {
        // Initialize trace printer, Constructor is called by each request.
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }
    
    @Path("/check")
    @GET
    @Produces({"application/javascript;charset=UTF-8"})
    @ApiOperation(value="未処理件数取得", response=PoslogStatusInfo.class)
    /**
     * Check the count of result that not deal with.
     */
    public final PoslogStatusInfo checkResultCount(
            @ApiParam(name="consolidation", value="Consolidationフラグ") @QueryParam("consolidation") final boolean consolidation,
            @ApiParam(name="transfer", value="POSLog転送フラグ") @QueryParam("transfer") final boolean transfer) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("Consolidation", consolidation).println("Transfer", transfer);
    	PoslogStatusInfo response = new PoslogStatusInfo();
    	DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    	
    	if (!consolidation && !transfer){
    		response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			tp.println("Parameter[s] is all false.");
			return response;
    	}
    	
    	try {
			IPoslogStatusDAO dao = daoFactory.getPoslogStatusDAO();
			response = dao.checkPoslogStatus(consolidation, transfer);
			
		} catch (DaoException daoEx) {
            LOGGER.logAlert(
            		PROG_NAME,
                    "poslogStatusResource.checkResultCount",
                    Logger.RES_EXCEP_DAO,
                    "Failed to get the info of poslog status.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
            	response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            	response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            	response.setMessage("DB ERROR");
            } else {
            	response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            	response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DAO);
            	response.setMessage("DAO ERROR");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
            		PROG_NAME,
            		"poslogStatusResource.checkResultCount",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get Poslog Status " + ": "
                            + ex.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage("GENERAL ERROR");
        } finally {
            tp.methodExit(response);
        }
        return response;
    }
}
