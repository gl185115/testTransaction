package ncr.res.mobilepos.point.factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.point.dao.IPointDAO;
import ncr.res.mobilepos.point.model.PointInfo;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * This class loads PointInfo from MST_QRCODE_INFO and MST_QRCODE_STORE.
 */
public class PointRateFactory {
    /** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "PointRateFactory";

    private static List<PointInfo> rateInstanceItem;
    private static List<PointInfo> rateInstanceTran;

    public PointRateFactory() {
    }

    public static void initialize(String companyId, String storeId) throws Exception {
        rateInstanceItem = new ArrayList<PointInfo>();
        rateInstanceTran = new ArrayList<PointInfo>();
        rateInstanceItem = rateInfoItem(companyId, storeId);
        rateInstanceTran = rateInfoTran(companyId, storeId);
        return;
    }

    public static List<PointInfo> getRateInstanceItem() {
        return rateInstanceItem;
    }

    public static List<PointInfo> getRateInstanceTran() {
        return rateInstanceTran;
    }
    /**
     * Get PointInfo  Information.
     * @param companyId
     * @param storeId
     * @return List<PointInfo>
     * @throws Exception
     */
    private static List<PointInfo> rateInfoItem(String companyId, String storeId) throws Exception {
    	Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PointRateFactory.class);
        String functionName = DebugLogger.getCurrentMethodName();

        List<PointInfo> pointInfoList = null;

        try {

            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();

            pointInfoList = new ArrayList<PointInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPointDAO pointDAO = daoFactory.getPointDAO();
            pointInfoList = pointDAO.getPointInfoList(companyId, storeId, dayDate);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get PointInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(pointInfoList);
            }
        }

        return pointInfoList;
    }
    /**
     * Get PointInfo  Information.
     * @param companyId
     * @param storeId
     * @return List<PointInfo>
     * @throws Exception
     */
    private static List<PointInfo> rateInfoTran(String companyId, String storeId) throws Exception {
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), PointRateFactory.class);
        String functionName = DebugLogger.getCurrentMethodName();

        List<PointInfo> pointInfoList = null;

        try {

            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();

            pointInfoList = new ArrayList<PointInfo>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPointDAO pointDAO = daoFactory.getPointDAO();
            pointInfoList = pointDAO.getTranPointInfoList(companyId, storeId, dayDate);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get TranPointInfo.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(pointInfoList);
            }
        }

        return pointInfoList;
    }
}
