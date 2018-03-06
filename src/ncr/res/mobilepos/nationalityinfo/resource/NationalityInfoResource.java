package ncr.res.mobilepos.nationalityinfo.resource;

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
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.nationalityinfo.dao.INationalityInfoDAO;
import ncr.res.mobilepos.nationalityinfo.model.NationalityInfoList;

@Path("/nationalityinfo")
@Api(value="/nationalityinfo", description="国籍情報API")
public class NationalityInfoResource {
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	private Trace.Printer tp;
	private DAOFactory daoFactory;
	private static final String PROG_NAME = "NationalityInfoResource";
	@Context
	private ServletContext servletContext;

	public NationalityInfoResource() {
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * Get nationality info list
	 * 
	 * @param company
	 *            id
	 * @param store
	 *            id
	 * @return nationality info list
	 */
	@Path("/getnationalityinfo")
	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value="国籍情報取得", response=NationalityInfoList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
	public final NationalityInfoList getNationalityInfo(@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
			@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId);
		NationalityInfoList nationalityInfo = new NationalityInfoList();

		if (StringUtility.isNullOrEmpty(companyId, storeId)) {
			nationalityInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			tp.methodExit(nationalityInfo.toString());
			return nationalityInfo;
		}

		try {
			INationalityInfoDAO nationalityInfoDao = daoFactory.getNationalityInfoDAO();
			nationalityInfo.setNationalityInfoList(nationalityInfoDao.getNationalityInfo(companyId, storeId));
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get nationality info for companyId.", ex);
			nationalityInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			nationalityInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			nationalityInfo.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get nationality info for companyId.", ex);
			nationalityInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			nationalityInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			nationalityInfo.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(nationalityInfo);
		}
		return nationalityInfo;
	}
}
