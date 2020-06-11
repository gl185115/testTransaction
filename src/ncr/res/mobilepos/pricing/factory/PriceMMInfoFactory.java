package ncr.res.mobilepos.pricing.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.dao.IPriceMMInfoDAO;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads PriceMMInfo from MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
 */
public class PriceMMInfoFactory {
	/** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
	/**
	 * A private member variable used for logging the class implementations.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * The Program Name.
	 */
	private static final String PROG_NAME = "PriceMMFactory";

	private static List<PriceMMInfo> instance;

	public PriceMMInfoFactory() {
	}

	public static List<PriceMMInfo> initialize(String companyId, String storeId) throws Exception {
		instance = null;
		instance = priceMMInfoConstant(companyId, storeId);
		return instance;
	}

	public static List<PriceMMInfo> getInstance() {
		return instance;
	}

	/**
	 * Get PriceMMInfo Information.
	 * @param companyId
	 * @param storeId
	 * @return List<PriceMMInfo>
	 * @throws Exception
	 */
	private static List<PriceMMInfo> priceMMInfoConstant(String companyId, String storeId) throws Exception {
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PriceMMInfoFactory.class);
		String functionName = "priceMMInfoConstant";

		List<PriceMMInfo> PriceMMList = null;

		try {

			SystemSettingResource sysSetting = new SystemSettingResource();
			sysSetting.setContext(context);

			DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
			if (dateSetting == null) {
				throw new DaoException("Business date is not set!");
			}
			String bizDay = dateSetting.getToday();

			PriceMMList = new ArrayList<PriceMMInfo>();
			DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IPriceMMInfoDAO priceMMInfDAO = daoFactory.getPriceMMInfoDAO();
			PriceMMList = priceMMInfDAO.getPriceMMInfoList(companyId, storeId, bizDay);

		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get PriceMMInfo.", e);
			throw e;
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get PriceMMInfo.", e);
			throw e;
		} finally {
			if(tp != null) {
				tp.methodExit(PriceMMList);
			}
		}

		return PriceMMList;
	}
}