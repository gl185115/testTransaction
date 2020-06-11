package ncr.res.mobilepos.giftcard.factory;

import java.io.File;

import javax.xml.bind.JAXBException;

import ncr.realgate.util.Trace;
import ncr.res.giftcard.toppan.model.Config;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.DataBinding;

/**
 * This class loads ToppanGiftCardConfigure from XML File.
 */
public class ToppanGiftCardConfigFactory {
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "TpnGCFactory";

    private static final int DEFAULT_WEBSERVICE_CONNTIMEOUT = 3000;
    private static final int DEFAULT_WEBSERVICE_CONNINTERVAL = 1000;
    private static final int DEFAULT_WEBSERVICE_CONNRETRY = 3;


    private static Config instance;

    private ToppanGiftCardConfigFactory() {
    }

    public static Config initialize(String configDirPath) throws JAXBException {
        instance = null;
        instance = getGiftCardConfig(configDirPath);
        return instance;
    }

    public static Config getInstance() {
        return instance;
    }

    /**
     * Get GiftCard Configuration Information.
     * @return Config    The Config Object
     * @throws JAXBException
     */
    private static Config getGiftCardConfig(String giftConfigPath) throws JAXBException {
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), ToppanGiftCardConfigFactory.class);

        Config giftcardConfig = null;
        String path = giftConfigPath + File.separator + Config.FILENAME;
        File configFile = new File(path);
        if(!configFile.isFile() || !configFile.exists()) {
        	String errorMessage = "No giftCardConfig File found." + "(" + path + ")";
            tp.println(errorMessage);
        }

        try {
            DataBinding<Config> serializer = new DataBinding<Config>(Config.class);
            giftcardConfig = serializer.unMarshallXml(configFile);
            if(giftcardConfig != null){
                if (giftcardConfig.getConnectionRetryOver() == 0) {
                    giftcardConfig.setConnectionRetryOver(DEFAULT_WEBSERVICE_CONNRETRY);
                }
                if (giftcardConfig.getConnectionTimeout() > DEFAULT_WEBSERVICE_CONNTIMEOUT) {
                    giftcardConfig.setConnectionTimeout(DEFAULT_WEBSERVICE_CONNTIMEOUT);
                }
                if (giftcardConfig.getConnectionRetryInterval() > DEFAULT_WEBSERVICE_CONNINTERVAL) {
                    giftcardConfig.setConnectionRetryInterval(DEFAULT_WEBSERVICE_CONNINTERVAL);
                }
                if(tp != null) {
                    tp.println("config.connectionTimeout", giftcardConfig.getConnectionTimeout());
                    tp.println("config.connectionRetryOver", giftcardConfig.getConnectionRetryOver());
                    tp.println("config.connectionRetryInterval", giftcardConfig.getConnectionRetryInterval());
                    tp.println("config.nonActivityTimeout", giftcardConfig.getNonActivityTimeout());
                    tp.println("config.responseTimeout", giftcardConfig.getResponseTimeout());
                    tp.println("config.jdbc", giftcardConfig.getJdbc());
                }
            }
        } catch (JAXBException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB,
                    "can't read:" + configFile.getAbsolutePath(), e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(giftcardConfig);
            }
        }

        return giftcardConfig;
    }

}
