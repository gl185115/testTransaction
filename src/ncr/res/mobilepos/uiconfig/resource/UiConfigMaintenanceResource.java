/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * JournalizationResource
 *
 * Resource which provides Web Service for journalizing transaction
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.uiconfig.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.uiconfig.constants.UiConfigProperties;
import ncr.res.mobilepos.uiconfig.dao.IUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.dao.SQLServerUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfoList;


/**
 * uiconfigMaintenance Web Resource Class.
 *
 * <P>uiconfigMaintenance.
 *
 */
@Path("/uiconfigMaintenance")
public class UiConfigMaintenanceResource {

    /**
     * UiConfigProperties
     */
    private static final UiConfigProperties configProperties = UiConfigProperties.getInstance();
	
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * Trace Printer.
     */
    private Trace.Printer tp = null;

    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public UiConfigMaintenanceResource() {
        // Initialize trace printer, Constructor is called by each request.
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    @Path("/getcompanyinfo")
    @POST
    @Produces({"application/json;charset=UTF-8"})
    
	public final CompanyInfoList getCompanyInfo() {
		// Logs given parameters.
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/getcompanyinfo/");
		
		CompanyInfoList companyInfo = null;
		try {
			companyInfo = new CompanyInfoList();
			IUiConfigCommonDAO icmyInfoDao = new SQLServerUiConfigCommonDAO();
			if(StringUtility.isNullOrEmpty(icmyInfoDao.getCompanyInfo())) {
				tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
				companyInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				companyInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				companyInfo.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return companyInfo;
			} else {
				companyInfo.setCompanyInfo(icmyInfoDao.getCompanyInfo());
			}
		} catch (DaoException ex) {
			tp.println("Failed to get company information.");
			LOGGER.logAlert(functionName,
					Logger.RES_EXCEP_DAO,
					functionName + ":Failed to get company information.",
					ex);
		} catch(Exception ex) {
			tp.println("Failed to get company information.");
			LOGGER.logAlert(functionName,
					Logger.RES_EXCEP_GENERAL,
					functionName + ":Failed to get company information.",
					ex);
        } finally {
        	tp.methodExit(companyInfo.toString());
        }
		return companyInfo;
    }
}

