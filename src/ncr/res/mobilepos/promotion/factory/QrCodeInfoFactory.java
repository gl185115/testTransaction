package ncr.res.mobilepos.promotion.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

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
    @Context
    private static ServletContext context; // to access the web.xml
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * QrCodeInfo configuration filename.
     */
    private static final String COMPANYID_FILENAME = "COMPANYID";
    
    private static final String STOREID_FILENAME = "STOREID";

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "QrCodeAFactory";
    
    private static List<QrCodeInfo> instance;

    public QrCodeInfoFactory() {
    }

    public static List<QrCodeInfo> initialize(String systemPath) throws Exception {
        instance = null;
        instance = codeInfoConstant(systemPath);
        return instance;
    }

    public static List<QrCodeInfo> getInstance() {
        return instance;
    }

    /**
     * Get QrCodeInfo  Information.
     * @param systemPath
     * @return List<QrCodeInfo>
     * @throws Exception
     */
    private static List<QrCodeInfo> codeInfoConstant(String systemPath) throws Exception {
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), QrCodeInfoFactory.class);
        String functionName = DebugLogger.getCurrentMethodName();
        
        List<QrCodeInfo> QrCodeList = null;
        BufferedReader readerCompany = null;
        BufferedReader readerStore = null;
        try {
            File companyIdFile = new File(systemPath + File.separator + COMPANYID_FILENAME);
            File storeIdFile = new File(systemPath + File.separator + STOREID_FILENAME);
            
            if(!companyIdFile.isFile() || !companyIdFile.exists()) {
                String errorMessage = "No COMPANYID File found." + "(" + systemPath + ")";
                tp.println(errorMessage);
            } else if (!storeIdFile.isFile() || !storeIdFile.exists()) {
                String errorMessage = "No STOREID File found." + "(" + systemPath + ")";
                tp.println(errorMessage);
            }
            
            readerCompany = new BufferedReader(new FileReader(companyIdFile));
            String companyId = readerCompany.readLine().trim();
            
            readerStore = new BufferedReader(new FileReader(storeIdFile));
            String storeId = readerStore.readLine();
            if (storeId != null){
                storeId = storeId.trim();
            }
            
            SystemSettingResource sysSetting = new SystemSettingResource();
            sysSetting.setContext(context);
            
            DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
            if (dateSetting == null) {
                tp.println("Business date is not set!");
                throw new DaoException("Business date is not set!");
            }
            String dayDate = dateSetting.getToday();
            
            QrCodeList = new ArrayList<QrCodeInfo>(); 
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQrCodeInfoDAO codeInfDAO = daoFactory.getQrCodeInfoDAO();
            QrCodeList = codeInfDAO.getkinokuniyaQrCodeInfo(companyId, storeId, dayDate);
            
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get codeInfo.", e);
            throw e;
        } catch (IOException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                    functionName + ": Failed to read file.", e);
            throw e;
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get codeInfo.", e);
            throw e;
        } finally {
            if (readerCompany != null) {
                try {
                    readerCompany.close();
                } catch (IOException e1) {
                    throw e1;
                }
            } 
            if (readerStore != null) {
                try {
                    readerStore.close();
                } catch (IOException e1) {
                    throw e1;
                }
            }
            if(tp != null) {
                tp.methodExit(QrCodeList);
            }
        }
        
        return QrCodeList;
    }
}
