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

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.point.dao.IPointDAO;
import ncr.res.mobilepos.point.model.Point;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.TranPointRate;
import ncr.res.mobilepos.point.model.PointRateResponse;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

@Path("/point")
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
    public final PointRateResponse getItemPointRate(
            @QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId,
            @QueryParam("businessDate") final String businessDate,
            @QueryParam("deptCode") final String deptCode,
            @QueryParam("groupCode") final String groupCode,
            @QueryParam("brandId") final String brandId,
            @QueryParam("sku") final String sku
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
    public final PointRateResponse getTranPointRate(
            @QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId,
            @QueryParam("businessDate") final String businessDate,
            @QueryParam("cardClassId") final String cardClassId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDate", businessDate)
            .println("cardClassId", cardClassId);
        PointRateResponse response = new PointRateResponse();
        List<TranPointRate> tranPointRateList = new ArrayList<TranPointRate>();
        
        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }
        
        try {
            IPointDAO pointDao = daoFactory.getPointDAO();
            tranPointRateList = pointDao.getTranPointRate(companyId, storeId, businessDate, cardClassId);
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
                    + "storeId#" + storeId + " and cardClassId#" + cardClassId + ": "
                    + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }
}
