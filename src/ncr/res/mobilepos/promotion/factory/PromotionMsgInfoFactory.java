package ncr.res.mobilepos.promotion.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.promotion.dao.IPromotionMsgInfoDAO;
import ncr.res.mobilepos.promotion.model.PromotionMsgInfo;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads PromotionMsgInfo from MST_PROMOTIONMSG_INFO, MST_PROMOTIONMSG_DETAIL and MST_PROMOTIONMSG_STORE.
 */
public class PromotionMsgInfoFactory {

	/** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "PromotionMsgFactory";

    private static List<PromotionMsgInfo> instance;

    public PromotionMsgInfoFactory() {
    }

    public static List<PromotionMsgInfo> initialize(String companyId, String storeId) throws DaoException {
        instance = null;
        instance = promotionMsgInfoConstant(companyId, storeId);
        return instance;
    }

    public static List<PromotionMsgInfo> getInstance() {
        return instance;
    }

    /**
     * Get PromotionMsg  Information.
     * @param companyId
     * @param storeId
     * @return List<PromotionMsgInfo>
     * @throws DaoException
     */
    private static List<PromotionMsgInfo> promotionMsgInfoConstant(String companyId, String storeId) throws DaoException {
    	Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PromotionMsgInfoFactory.class);
        String functionName = "promotionMsgInfoConstant";

        List<PromotionMsgInfo> promMsgList = null;

        try {
            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();

            promMsgList = new ArrayList<PromotionMsgInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPromotionMsgInfoDAO promMsgInfDAO = daoFactory.getPromotionMsgInfoDAO();
            promMsgList = promMsgInfDAO.getPromotionMsgInfoList(companyId, storeId, dayDate);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get promotionMsgInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(promMsgList);
            }
        }

        return promMsgList;
    }
}
