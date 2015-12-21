package ncr.res.mobilepos.helper;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;

public class POSLogUtility {
	 /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "POSLogUtility";
    

    /** Default Constructor. */
    private POSLogUtility() { 
    	
    }
    
	public static PosLog toPosLog(String posLogXml){
		PosLog poslogData = null;
		try {
			XmlSerializer<PosLog> serializer = new XmlSerializer<PosLog>();
			poslogData = serializer.unMarshallXml(posLogXml, PosLog.class);
		} catch (JAXBException e) {
			LOGGER.logAlert(PROG_NAME, PROG_NAME+".toPosLog", Logger.RES_EXCEP_JAXB, "Failed to convert XML to PosLog object. "+e.getMessage());
		}
		return poslogData;
	}
}
