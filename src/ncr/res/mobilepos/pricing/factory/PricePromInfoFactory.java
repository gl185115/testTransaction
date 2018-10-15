package ncr.res.mobilepos.pricing.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.dao.IPricePromInfoDAO;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads PricePromInfo from MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
 */
public class PricePromInfoFactory {
    /** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "PricePromFactory";

    private static List<PricePromInfo> instance;

    public PricePromInfoFactory() {
    }

    public static List<PricePromInfo> initialize(String companyId, String storeId) throws Exception {
        instance = null;
        instance = pricePromInfoConstant(companyId, storeId);
        return instance;
    }

    public static List<PricePromInfo> getInstance() {
        return instance;
    }

    /**
     * Get PricePromInfo Information.
     * @param systemPath
     * @return List<PricePromInfo>
     * @throws Exception
     */
    private static List<PricePromInfo> pricePromInfoConstant(String companyId, String storeId) throws Exception {
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PricePromInfoFactory.class);
        String functionName = DebugLogger.getCurrentMethodName();

        List<PricePromInfo> PricePromList = null;

        try {

        	SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                throw new DaoException("Business date is not set!");
            }
            String bizDay = dateSetting.getToday();

            PricePromList = new ArrayList<PricePromInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPricePromInfoDAO pricePromInfDAO = daoFactory.getPricePromInfoDAO();
            PricePromList = pricePromInfDAO.getPricePromInfoList(companyId, storeId, bizDay);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get PricePromInfo.", e);
            throw e;
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get PricePromInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(PricePromList);
            }
        }

        return PricePromList;
    }
}