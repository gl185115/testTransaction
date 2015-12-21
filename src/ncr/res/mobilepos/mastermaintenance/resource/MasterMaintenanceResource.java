/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * MasterMaintenanceResource
 *
 * Resource which provides Web Service for Master Maintenance
 *
 * del Rio, Rica Marie
 */

package ncr.res.mobilepos.mastermaintenance.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.mastermaintenance.dao.IMasterMaintenanceDAO;
import ncr.res.mobilepos.mastermaintenance.model.MaintenanceTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMMMastTbl;
import ncr.res.mobilepos.model.ResultBase;

/**
 * MasterMaintenanceResource Web Resource.
 *
 * Processes on-time synchronization of database between Spart and
 * Web Store Server.
 */
@Path("/mastermaintenance")
public class MasterMaintenanceResource {
    /** The Flag value that refer to MD_MM_MAST_TBL. */
    private static final int MD_MM_MAST_TBL = 1;
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE5 = "5";
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE6 = "6";
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE7 = "7";
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE8 = "8";
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE14 = "14";
    /** The Flag value that refer to MM_MIXMATCH TYPE. */
    private static final String MMTYPE17 = "17";
    /** The value that would determine the correct figure. */
    private static final int ONEHUNDRED = 100;
    /** A private member variable used for the servlet context. */
    @Context
    private ServletContext context;
    /**
     * The Snap Logger.
     */
    private SnapLogger snapLogger;
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /** The program name use in logging. */
    private static final String PROG_NAME = "MasterMaintenance";
    /**
     * Default Constructor for MasterMaintenanceResource.
     *
     * <P> Initializes the logger object.
     */

    private DAOFactory daoFactory;

    /**
     * The Default constructor.
     */
    public MasterMaintenanceResource() {
         this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
         this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
          getClass());
         this.snapLogger = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * @param pricenotax - price form SPART values
     * @param mmtype - flag to determine the mixmatch type
     * @return string
     */
    private String computetax(final String pricenotax, final String mmtype) {
        String rate =
                     (String) context.getAttribute(GlobalConstant.TAX_RATE_KEY);
        double taxrate = 1 + (Double.parseDouble(rate) / ONEHUNDRED);

        if (mmtype.trim().equalsIgnoreCase(MMTYPE5)
                || mmtype.trim().equalsIgnoreCase(MMTYPE6)
                || mmtype.trim().equalsIgnoreCase(MMTYPE7)
                || mmtype.trim().equalsIgnoreCase(MMTYPE8)
                || mmtype.trim().equalsIgnoreCase(MMTYPE14)
                || mmtype.trim().equalsIgnoreCase(MMTYPE17)
                || pricenotax == null) {
            return pricenotax == null ? "0" : pricenotax;
        }
		
        return Long.toString((long) Math.floor(Double.parseDouble(pricenotax) * taxrate));
    }

    /**
     * @param fieldValues - CSV form field values
     * @param tableFlag - flag to determine the table for modification
     * @return rsBase
     */
    @Path("/import/row")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase spartImport(
       @FormParam("row") final String fieldValues,
       @FormParam("targettable") final int tableFlag) {
    	String functionName = "MasterMaintenanceResource.spartImport";
    tp.methodEnter(functionName)
      .println("row", fieldValues)
      .println("targettable", Integer.toString(tableFlag));

    ResultBase result = new ResultBase();

    try {
       if (fieldValues == null || fieldValues.trim().isEmpty()) {
          tp.println("Invalid value for row");
          result.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);
          return result;
       }

       MaintenanceTbl targetRow = MaintenanceTbl.getMaintenanceModelFactory(
               tableFlag, fieldValues);
        IMasterMaintenanceDAO mastermaintenanceDAO =
             daoFactory.getMasterMaintenanceDAO();
        // check mixmatch table and add tax
        if (tableFlag == MD_MM_MAST_TBL) {
            MdMMMastTbl mdMMMastTbl = (MdMMMastTbl) targetRow;
            String mmType = mdMMMastTbl.getMmType();
            mdMMMastTbl.setDiscountPrice1(String.valueOf(computetax(
                 mdMMMastTbl.getDiscountPrice1(), mmType)));
            mdMMMastTbl.setDiscountPrice2(String.valueOf(computetax(
                 mdMMMastTbl.getDiscountPrice2(), mmType)));
            mdMMMastTbl.setEmpPrice11(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice11(), mmType)));
            mdMMMastTbl.setEmpPrice12(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice12(), mmType)));
            mdMMMastTbl.setEmpPrice13(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice13(), mmType)));
            mdMMMastTbl.setEmpPrice21(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice21(), mmType)));
            mdMMMastTbl.setEmpPrice22(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice22(), mmType)));
            mdMMMastTbl.setEmpPrice23(String.valueOf(computetax(
                 mdMMMastTbl.getEmpPrice23(), mmType)));
            result = mastermaintenanceDAO.importSpartData(mdMMMastTbl);
        } else {
            result = mastermaintenanceDAO.importSpartData(targetRow);
        }
    } catch (Exception ex)  {
        tp.println("General Exception error occured.");
        
		LOGGER.logAlert(PROG_NAME,
                functionName,
                Logger.RES_EXCEP_GENERAL,
                "General Exception error occured. " + ex.getMessage());
        Snap.SnapInfo info =
            this.snapLogger.write(
                    "General exception Occured",
                    fieldValues);
        LOGGER.logSnap(PROG_NAME, functionName,
            "Output error MD_MAST_TBL in JSON object representation"
                + " data to snap file.", info);
    } finally {
       tp.methodExit(result.toString());
    }
        return result;
    }
    
}

