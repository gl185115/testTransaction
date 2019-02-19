package ncr.res.mobilepos.ej.Factory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.ej.dao.INameSystemInfoDAO;
import ncr.res.mobilepos.ej.model.EjInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.promotion.factory.TaxRateInfoFactory;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

public class NameCategoryFactory {
	/** A private member variable used for the servlet context. */
	private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "NameCategoryFactory";

    private static List<EjInfo> instance;

    public NameCategoryFactory() {
    }

    public static List<EjInfo> initialize() throws DaoException {
        instance = null;
        instance = nameSystemInfoConstant();
        return instance;
    }

    public static List<EjInfo> getInstance() {
        return instance;
    }

    /**
     * Get Name System Information.
     * @param void
     * @return List<EjInfo>
     * @throws Exception
     */
    private static List<EjInfo> nameSystemInfoConstant() throws DaoException {
    	Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), TaxRateInfoFactory.class);
        String functionName = DebugLogger.getCurrentMethodName();

        List<EjInfo> listNameSystemInfo = new ArrayList<EjInfo>();

        try {
            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);

            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            INameSystemInfoDAO nameSystemInfoDAO = daoFactory.getNameSystemInfo();
            listNameSystemInfo = nameSystemInfoDAO.getNameSystemInfo();

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get NameCategory.", e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(listNameSystemInfo);
            }
        }
        return listNameSystemInfo;
    }
}
