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
@Api(value="/poslogstatus", description="������POSLog�����擾API")
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
    @ApiOperation(value="POSLog�̏W�v��]���̖����������擾", response=PoslogStatusInfo.class)
    @ApiResponses(value={
    	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    	    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
    	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
    	    })
    public final PoslogStatusInfo checkResultCount(
            @ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
            @ApiParam(name="retailStoreId", value="�X�ԍ�") @QueryParam("retailStoreId") final String retailStoreId,
            @ApiParam(name="businessDayDate", value="�Ɩ����t") @QueryParam("businessDayDate") final String businessDayDate,
            @ApiParam(name="consolidation", value="Consolidation�t���O") @QueryParam("consolidation") final boolean consolidation,
            @ApiParam(name="transfer", value="POSLog�]���t���O") @QueryParam("transfer") final boolean transfer) {
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
                    "���N�G�X�g�f�[�^�t�H�[�}�b�g�G���[�B\n�N�G���[�p�����[�^�ݒ�G���[�B(consolidation/transfer/companyId/retailStoreId/businessDayDate)\n");
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
                        "�p�����[�^�擾�G���[�B\nweb.xml��POSLogTransferStatusColumn�p�����[�^�����ݒ�ł��B\n");
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
