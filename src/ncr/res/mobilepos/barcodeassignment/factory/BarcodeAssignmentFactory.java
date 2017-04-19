package ncr.res.mobilepos.barcodeassignment.factory;

import java.io.File;
import javax.xml.bind.JAXBException;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;

/**
 * This class loads BarcodeAssignment from XML File.
 */
public class BarcodeAssignmentFactory {
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "BarAFactory";
    
    private static BarcodeAssignment instance;

    private BarcodeAssignmentFactory() {
    }

    public static BarcodeAssignment initialize(String paraBasePath) throws JAXBException {
        instance = null;
        instance = itemCodeXMLConstant(paraBasePath);
        return instance;
    }

    public static BarcodeAssignment getInstance() {
        return instance;
    }

    /**
     * Get ItemCode.xml  Information.
     * @return BarcodeAssignment    The BarcodeAssignment Object
     * @throws JAXBException
     */
    private static BarcodeAssignment itemCodeXMLConstant(String paraBasePath) throws JAXBException {
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), BarcodeAssignmentFactory.class);
        
        BarcodeAssignment barCode = null;
        String path = paraBasePath + File.separator + GlobalConstant.ITEMCODE_FILENAME;
        File conFileXml = new File(path);
        if(!conFileXml.isFile() || !conFileXml.exists()) {
            String errorMessage = "No giftCardConfig File found." + "(" + path + ")";
            tp.println(errorMessage);
        }
        
        try {
            XmlSerializer<BarcodeAssignment> conSerializer = new XmlSerializer<BarcodeAssignment>();
            barCode = conSerializer.unMarshallXml(conFileXml, BarcodeAssignment.class);
        } catch (JAXBException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB,
                    "can't read:" + conFileXml.getAbsolutePath(), e);
            throw e;
        } finally {
            if(tp != null) {
                tp.methodExit(barCode);
            }
        }
        return barCode;
    }
}
