package ncr.res.mobilepos.intaPay.factory;

import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;
import ncr.res.mobilepos.intaPay.dao.IIntaPayDAO;
import ncr.res.mobilepos.intaPay.model.IntaPaySystemConfig;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

/**
 * This class loads IntaPay from XML File.
 */
public class IntaPayFactory {

    private static IntaPaySystemConfig intaPaySystemConfig;

    private IntaPayFactory() {
    }

    public static IntaPaySystemConfig initialize() throws DaoException {
        intaPaySystemConfig = new IntaPaySystemConfig();
		DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();

        String val = systemDao.getParameterValue(IntaPayConstants.KEYID_CERTUSE, IntaPayConstants.INTAPAY_CATEGORY);
		if (!StringUtility.isNullOrEmpty(val)) {
			intaPaySystemConfig.setCertuse("1".equals(val));
		}
		val = systemDao.getParameterValue(IntaPayConstants.KEYID_DEBUG, IntaPayConstants.INTAPAY_CATEGORY);
		if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setDebugFlag("1".equals(val));
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_GATEWAY_SERVER_URL, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setGateway_server_url(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_CERTPW, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setCertpw(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_CERTFILEPATH, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setCertfilepath(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_API_KEY, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setApiKey(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_MCH_CODE, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setMchCode(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_BODY, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setBodyMessage(val);
        }
        val = systemDao.getParameterValue(IntaPayConstants.KEYID_DEVICE_INFO, IntaPayConstants.INTAPAY_CATEGORY);
        if (!StringUtility.isNullOrEmpty(val)) {
            intaPaySystemConfig.setDevice_info(val);
        }
        return intaPaySystemConfig;
    }

    public static IntaPaySystemConfig getIntaPaySystemConfig() {
        return intaPaySystemConfig;
    }
}
