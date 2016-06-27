package ncr.res.mobilepos.tenderinfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
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
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tenderinfo.dao.ITenderInfoDAO;
import ncr.res.mobilepos.xebioapi.model.JSONData;

@Path("/tenderinfo")
@Api(value="/tenderinfo", description="差し出す情報API")
public class TenderInfoResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "TenderInfoResource";
    @Context
    private ServletContext servletContext;

    public TenderInfoResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     */
    @Path("/getTenderInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="差し出す情報を得る", response=JSONData.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    		@ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final JSONData getTenderInfo(
    		@ApiParam(name="CompanyId", value="会社コード")@QueryParam("CompanyId") final String companyId,
    		@ApiParam(name="StoreId", value="店舗番号")@QueryParam("StoreId") final String storeId,
    		@ApiParam(name="TenderType", value="支払種別")@QueryParam("TenderType") final String tenderType) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId).println("TenderType", tenderType);

        JSONData tender = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, tenderType)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return tender;
            }
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ITenderInfoDAO iTenderInfoDAO = sqlServer.getTenderInfoDAO();

            tender = iTenderInfoDAO.getTenderInfo(companyId, storeId, tenderType);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get tender infomation.", e);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            tender.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get tender infomation.", ex);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(tender);
        }
        return tender;
    }

    /**
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     */
    @Path("/gettenderinfobytype")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="タイプによって差し出す情報を得る", response=JSONData.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    		@ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final JSONData getTenderInfoByType(@QueryParam("CompanyId") final String companyId,
    		@ApiParam(name="StoreId", value="店舗番号")@QueryParam("StoreId") final String storeId,
    		@ApiParam(name="TenderType", value="支払種別")@QueryParam("TenderType") final String tenderType,
    		@ApiParam(name="TenderId", value="種別コード")@QueryParam("TenderId") final String tenderId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                                    .println("TenderType", tenderType).println("TenderId", tenderId);

        JSONData tender = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, tenderType,tenderId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return tender;
            }
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ITenderInfoDAO iTenderInfoDAO = sqlServer.getTenderInfoDAO();
            tender = iTenderInfoDAO.getTenderInfoByType(companyId, storeId, tenderType,tenderId);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get tender infomation.", e);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            tender.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get tender infomation.", ex);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(tender);
        }
        return tender;
    }
}