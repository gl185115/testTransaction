package ncr.res.mobilepos.test.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.searchapi.model.JSONData;

/**
 * 
 * @author WangXu
 */
@Path("/test")
@Api(value="/Test", description="テストAPI")
public class TestResource {
    
    /** The Trace Printer. */
    private Trace.Printer tp = null;
    
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    /**
     * variable that holds the class codename.
     */
    private static final String PROG_NAME = "TestRsc";
    
    /**
     * constructor.
     */
    public TestResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    
    /**
     * Service to test Connection is ok.
     * @return JSON type of ResultBase.
     */
    @Path("/GetConnectionResult")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="接続の結果を取得する", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_OK, message="一般ok"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase GetConnectionResult() {	

        String functionName = DebugLogger.getCurrentMethodName();
        
        ResultBase resultBase = new ResultBase();
        try {
            resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_OK);
            resultBase.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to GetConnectionResult.", ex);
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            resultBase.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }
    
}
