package ncr.res.mobilepos.promotion.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.model.TaxRateInfo;
import ncr.res.mobilepos.promotion.dao.ITaxRateInfoDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads TaxRateInfo from MST_TAXRATE.
 */
public class TaxRateInfoFactory {
    /** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "TaxRateAFactory";

    private static List<TaxRateInfo> instance;

    public TaxRateInfoFactory() {
    }

    public static List<TaxRateInfo> initialize(String companyId, String storeId) throws DaoException {
        instance = null;
        instance = taxInfoConstant(companyId , storeId);
        return instance;
    }

    public static List<TaxRateInfo> getInstance() {
        return instance;
    }

    /**
     * Get TaxRate Information.
     * @param companyId
     * @param storeId
     * @return List<TaxRateInfo>
     * @throws Exception
     */
    private static List<TaxRateInfo> taxInfoConstant(String companyId, String storeId) throws DaoException {
    	Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), TaxRateInfoFactory.class);
        String functionName = "taxInfoConstant";

        List<TaxRateInfo> taxRateList = null;

        try {
            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();

            taxRateList = new ArrayList<TaxRateInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ITaxRateInfoDAO taxRateInfDAO = daoFactory.getTaxRateInfoDAO();
            taxRateList = taxRateInfDAO.getTaxRateInfoList(dayDate);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get taxRateInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(taxRateList);
            }
        }
        return taxRateList;
    }
}
