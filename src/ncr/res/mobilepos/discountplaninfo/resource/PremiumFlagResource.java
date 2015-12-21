package ncr.res.mobilepos.discountplaninfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.discountplaninfo.dao.IPremiumFlagDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.xebioapi.model.JSONData;

@Path("/premiumInfo")
public class PremiumFlagResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "PremiumFlagResource";
    @Context
    private ServletContext servletContext;

    public PremiumFlagResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param dptIdList
     * @return premiumFlag(only return the dptList where the premiumFlag is true)
     * @throws DaoException
     */
    @Path("/getPremiumFlag")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final JSONData getPremiumFlag(@QueryParam("CompanyId") final String companyId,
            @QueryParam("StoreId") final String storeId, @QueryParam("TerminalId") final String terminalId,
            @QueryParam("DptIdList") final String dptIdList) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("TerminalId", terminalId).println("DptIdList", dptIdList);

        JSONData premiumFlag = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId, dptIdList)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                premiumFlag.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                premiumFlag.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                premiumFlag.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return premiumFlag;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPremiumFlagDAO iPremiumFlagDAO = sqlServer.getPremiumFlagDAO();

            premiumFlag = iPremiumFlagDAO.getPremiumFlag(companyId, storeId, terminalId, dptIdList);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get Premium Flag infomation.", e);
            premiumFlag.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            premiumFlag.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            premiumFlag.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get Premium Flag infomation.", ex);
            premiumFlag.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            premiumFlag.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            premiumFlag.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(premiumFlag);
        }
        return premiumFlag;
    }

}
