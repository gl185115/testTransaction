package ncr.res.mobilepos.promotion.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.promotion.dao.IQrCodeInfoDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads QrCodeInfo from MST_QRCODE_INFO and MST_QRCODE_STORE.
 */
public class QrCodeInfoFactory {
    /** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "QrCodeAFactory";

    private static List<QrCodeInfo> instance;

    public QrCodeInfoFactory() {
    }

    public static List<QrCodeInfo> initialize(String companyId, String storeId) throws DaoException {
        instance = null;
        instance = codeInfoConstant(companyId, storeId);
        return instance;
    }

    public static List<QrCodeInfo> getInstance() {
        return instance;
    }

    /**
     * Get QrCodeInfo  Information.
     * @param companyId
     * @param storeId
     * @return List<QrCodeInfo>
     * @throws Exception
     */
    private static List<QrCodeInfo> codeInfoConstant(String companyId, String storeId) throws DaoException {
    	Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), QrCodeInfoFactory.class);
        String functionName = "codeInfoConstant";

        List<QrCodeInfo> qrCodeList = null;

        try {

            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();

            qrCodeList = new ArrayList<QrCodeInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQrCodeInfoDAO codeInfDAO = daoFactory.getQrCodeInfoDAO();
            qrCodeList = codeInfDAO.getQrCodeInfoList(companyId, storeId, dayDate);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get codeInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(qrCodeList);
            }
        }

        return qrCodeList;
    }
}
