package ncr.res.mobilepos.tenderinfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
    public final JSONData getTenderInfo(@QueryParam("CompanyId") final String companyId,
            @QueryParam("StoreId") final String storeId, @QueryParam("TenderType") final String tenderType) {
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
    public final JSONData getTenderInfoByType(@QueryParam("CompanyId") final String companyId,
            @QueryParam("StoreId") final String storeId, 
            @QueryParam("TenderType") final String tenderType,
            @QueryParam("TenderId") final String tenderId) {
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