package ncr.res.mobilepos.buyadditionalinfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
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
import ncr.res.mobilepos.buyadditionalinfo.dao.IBuyadditionalInfoDAO;
import ncr.res.mobilepos.buyadditionalinfo.model.BuyadditionalInfoList;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

@Path("/buyadditionalinfo")
@Api(value="/buyadditionalinfo", description="wâîñæ¾API")
public class BuyadditionalInfoResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private DAOFactory daoFactory;
    private static final String PROG_NAME = "BuyadditionalInfoResource";
    @Context
    private ServletContext servletContext;

    public BuyadditionalInfoResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
        		getClass());
    }

	/**
	 * Get buy additional info list
	 *
	 * @param company
	 *            id
	 * @param store
	 *            id
	 * @return buy additional info list
	 */
    @Path("/getbuyadditionalinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="wâîñæ¾", response=BuyadditionalInfoList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="f[^x[XG["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="ÄpG["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="³øÈp[^")
    })
    public final BuyadditionalInfoList getBuyadditionalInfo(
    		@ApiParam(name="companyId", value="ïÐR[h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="XÔ") @QueryParam("storeId") final String storeId) {
    	String functionName = "getBuyadditionalInfo";
        tp.methodEnter(functionName)
        	.println("companyId", companyId)
        	.println("storeId", storeId);
        BuyadditionalInfoList buyadditionalInfo = new BuyadditionalInfoList();

        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
        	buyadditionalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
        	tp.methodExit(buyadditionalInfo.toString());
        	return buyadditionalInfo;
        }

        try {
        	IBuyadditionalInfoDAO buyadditionalInfoDao = daoFactory.getBuyadditionalInfoDAO();
        	buyadditionalInfo.setBuyadditionalInfoList(buyadditionalInfoDao.getBuyadditionalInfo(companyId, storeId));
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get buyadditional info for companyId.", ex);
			buyadditionalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			buyadditionalInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			buyadditionalInfo.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get buyadditional info for companyId.", ex);
			buyadditionalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			buyadditionalInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			buyadditionalInfo.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(buyadditionalInfo);
		}
        return buyadditionalInfo;
    }
}
