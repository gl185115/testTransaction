package ncr.res.mobilepos.point.resource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import ncr.res.mobilepos.point.dao.IPointDAO;
import ncr.res.mobilepos.point.model.Point;
import ncr.res.mobilepos.point.model.CashingUnit;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.TranPointRate;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.point.model.PointRateResponse;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

@Path("/point")
@Api(value="/point", description="�|�C���g�̑��xAPI")
public class PointResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private DAOFactory daoFactory;
    private static final String PROG_NAME = "PointResource";
    private static final String PATH_NAME = "point";
    @Context
    private ServletContext servletContext;
    
    public PointResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), 
        		getClass());
    }
    
    @Path("/getitempointrate")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���i�|�C���g���擾", response=PointRateResponse.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^")
        })
    public final PointRateResponse getItemPointRate(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="businessDate", value="�c�Ɠ�") @QueryParam("businessDate") final String businessDate,
    		@ApiParam(name="deptCode", value="����R�[�h") @QueryParam("deptCode") final String deptCode,
    		@ApiParam(name="groupCode", value="�O���[�v�R�[�h") @QueryParam("groupCode") final String groupCode,
    		@ApiParam(name="brandId", value="�u�����h�R�[�h") @QueryParam("brandId") final String brandId,
    		@ApiParam(name="sku", value="���Еi��") @QueryParam("sku") final String sku
    ) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDate", businessDate)
            .println("deptCode", deptCode)
            .println("groupCode", groupCode)
            .println("brandId", brandId)
            .println("sku", sku);
        PointRateResponse response = new PointRateResponse();
        List<ItemPointRate> itemPointRateList = new ArrayList<ItemPointRate>();
        
        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }
        
        try {
            IPointDAO pointDao = daoFactory.getPointDAO();
            itemPointRateList = pointDao.getItemPointRate(companyId, storeId, businessDate, deptCode, groupCode, brandId, sku);
            response.setItemPointRateList(itemPointRateList);
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
            response.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get item point rate for companyId#" + companyId + ", "
                    + "storeId#" + storeId + " and deptCode#" + deptCode
                    + " and groupCode#" + groupCode + " and brandId#" + brandId + " and sku#" + sku + ": "
                    + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }
    
    
    @Path("/gettranpointrate")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="����|�C���g���擾", response=PointRateResponse.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^")
        })
    public final PointRateResponse getTranPointRate(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="businessDate", value="�c�Ɠ�") @QueryParam("businessDate") final String businessDate) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDate", businessDate);
        PointRateResponse response = new PointRateResponse();
        List<TranPointRate> tranPointRateList = new ArrayList<TranPointRate>();
        
        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }
        
        try {
            IPointDAO pointDao = daoFactory.getPointDAO();
            tranPointRateList = pointDao.getTranPointRate(companyId, storeId, businessDate);
            response.setTranPointRateList(tranPointRateList);
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
            response.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get tran point rate companyId#" + companyId + ", "
                    + "storeId#" + storeId + ": " + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }
    
    /**
     * Service to view CashingUnit of given parameter companyid and recordId.
     * 
     * @param companyid
     * @param recordId
     * @return JSON type of CashingUnit.
     */
    @Path("/getcashingunit")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="POINT�Ҍ������擾", response=ViewStore.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final CashingUnit getCashingUnit() {

        String functionName = "";

        tp.methodEnter("getCashingUnit");
        CashingUnit cashingUnit = new CashingUnit();

        try {

            IPointDAO pointDAO = daoFactory.getPointDAO();
            cashingUnit = pointDAO.getCashingUnitInfo();

        } catch (DaoException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to view CashingUnit# " + cashingUnit + ": "
                            + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view cashingUnit# " + cashingUnit + ": "
                            + ex.getMessage());
        } finally {
            tp.methodExit(cashingUnit.toString());
        }

        return cashingUnit;
    }
}
