package ncr.res.mobilepos.poslogstatus.resource;

import java.sql.SQLException;

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
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.poslogstatus.dao.IPoslogStatusDAO;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;

@Path("/poslogstatus")
@Api(value="/poslogstatus", description="未処理POSLog件数取得API")
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

    /**
     * Check the count of result that not deal with.
     * @param consolidation
     *            the consolidation check flag
     * @param transfer
     *            the transfer check flag
     * @return PoslogStatusInfo
     *            the count of result that not deal with.
     */
    @Path("/check")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="POSLogの集計や転送の未処理件数取得", response=PoslogStatusInfo.class)
    @ApiResponses(value={
    	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    	    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
    	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    	    })
    public final PoslogStatusInfo checkResultCount(
            @ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
            @ApiParam(name="retailStoreId", value="店番号") @QueryParam("retailStoreId") final String retailStoreId,
            @ApiParam(name="businessDayDate", value="業務日付") @QueryParam("businessDayDate") final String businessDayDate,
            @ApiParam(name="consolidation", value="Consolidationフラグ") @QueryParam("consolidation") final boolean consolidation,
            @ApiParam(name="transfer", value="POSLog転送フラグ") @QueryParam("transfer") final boolean transfer) {
    	String functionName = "checkResultCount";
		tp.methodEnter(functionName).println("Consolidation", consolidation).println("Transfer", transfer);
    	PoslogStatusInfo response = new PoslogStatusInfo();
    	DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);

    	if ((!consolidation && !transfer) || StringUtility.isNullOrEmpty(companyId)
    			|| StringUtility.isNullOrEmpty(retailStoreId) || StringUtility.isNullOrEmpty(businessDayDate)){
    		LOGGER.logAlert(
            		PROG_NAME,
                    functionName,
                    Logger.RES_PARA_ERR,
                    "リクエストデータフォーマットエラー。\nクエリーパラメータ設定エラー。(consolidation/transfer/companyId/retailStoreId/businessDayDate)\n");
    		response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			tp.println("Parameter[s] is all false.");
			return response;
    	}

    	try {
    	    IPoslogStatusDAO dao = daoFactory.getPoslogStatusDAO();
    	    String poslogTransferColumnName = EnvironmentEntries.getInstance().getPoslogTransferStatusColumn();

            if (transfer && StringUtility.isNullOrEmpty(poslogTransferColumnName)) {
            	LOGGER.logAlert(
                		PROG_NAME,
                		functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "パラメータ取得エラー。\nweb.xmlのPOSLogTransferStatusColumnパラメータが未設定です。\n");
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("web.xml POSLogTransferStatusColumn is empty.");
    			tp.println("web.xml POSLogTransferStatusColumn is empty.");
    			return response;
            }

    	    response = dao.checkPoslogStatus(consolidation, transfer,companyId, retailStoreId, businessDayDate, poslogTransferColumnName);

		} catch (DaoException daoEx) {
            LOGGER.logAlert(
            		PROG_NAME,
                    functionName,
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
            		functionName,
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
