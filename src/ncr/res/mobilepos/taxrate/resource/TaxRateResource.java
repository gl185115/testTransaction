package ncr.res.mobilepos.taxrate.resource;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.taxrate.dao.ITaxRateDao;
import ncr.res.mobilepos.taxrate.dao.SQLServerTaxRateDao;

/**
 * PromotionResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/taxrate")
public class TaxRateResource {

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
                                                                        // the
                                                                        // Logger
    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "taxRate";

    /**
     * Default Constructor for PromotionResource.
     *
     * <P>
     * Initializes the logger object.
     */
    public TaxRateResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * get the taxRate
     * @param businessdate the date of request
     * @return taxRate
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/gettaxrate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final Map<String,String> itemMixMatchInfobySku(@FormParam("businessDate") final String businessdate) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("businessdate", businessdate);
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (StringUtility.isNullOrEmpty(businessdate)) {
                tp.println("Parameter[s] is empty or null.");
                return map;
            }
            ITaxRateDao dao = new SQLServerTaxRateDao();
            map = dao.getTaxRateByDate(businessdate);

        } catch (DaoException e) {
            if (e.getCause() instanceof SQLException) {
                LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                        functionName + ": Failed to get taxRate." + e.getMessage(), e);
                map.put("errorCode",String.valueOf(ResultBase.RES_ERROR_DB));
            } else {
                LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                        functionName + ": Failed to get taxRate." + e.getMessage(), e);
                map.put("errorCode", String.valueOf(ResultBase.RES_ERROR_DAO));
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get taxRate.", e);
            map.put("errorCode", String.valueOf(ResultBase.RES_ERROR_GENERAL));
        } finally {
            tp.methodExit(map);
        }
        return map;
    }

}
