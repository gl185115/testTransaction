package ncr.res.mobilepos.pricing.factory;

import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.dao.IPriceUrgentInfoDAO;
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;

/**
 * This class loads PriceUrgentInfo from MST_PLU_URGENT..
 */
public class PriceUrgentInfoFactory {
	/**
	 * A private member variable used for logging the class implementations.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * The Program Name.
	 */
	private static final String PROG_NAME = "PriceUrgentInfoFactory";

	private static List<PriceUrgentInfo> instance;

	public PriceUrgentInfoFactory() {
	}

	public static List<PriceUrgentInfo> initialize(String companyId, String storeId, String mdInternal) throws Exception {
		instance = null;
		instance = pricePromInfoConstant(companyId, storeId, mdInternal);
		return instance;
	}

	public static List<PriceUrgentInfo> getInstance() {
		return instance;
	}

	/**
	 * Get PriceUrgentInfo Information.
	 * 
	 * @param companyId , storeId , mdInternal
	 * @return List<PriceUrgentInfo>
	 * @throws Exception
	 */
	private static List<PriceUrgentInfo> pricePromInfoConstant(String companyId, String storeId, String mdInternal) throws Exception {
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PriceUrgentInfoFactory.class);
		String functionName = "pricePromInfoConstant";

		List<PriceUrgentInfo> priceUrgentList = null;

		try {
			priceUrgentList = new ArrayList<PriceUrgentInfo>();
			DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IPriceUrgentInfoDAO priceUrgentInfDAO = daoFactory.getPriceUrgentInfoDAO();
			priceUrgentList = priceUrgentInfDAO.getPriceUrgentInfoList(companyId, storeId, mdInternal);

		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get PriceUrgentInfo.", e);
			throw e;
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get PriceUrgentInfo.", e);
			throw e;
		} finally {
			if (tp != null) {
				tp.methodExit(priceUrgentList);
			}
		}

		return priceUrgentList;
	}
}