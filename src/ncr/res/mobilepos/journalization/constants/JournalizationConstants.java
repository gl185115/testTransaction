package ncr.res.mobilepos.journalization.constants;

public class JournalizationConstants {

	/**
     * The Default contsructor.
     * Do not allow the class to be instantiated.
     */
    private JournalizationConstants() {    	
    }
    /**
     * The journalList api.
     */
    public static final String JOURNALLIST_URL = "/TranAPI/Tran.json/";
    /**
     * The journalDetails api.
     */
    public static final String JOURNALDETAILS_URL = "/EjnlIndexAPI/EjnlIndex.json/";
    /**
     * The ProcessingTran api.
     */
    public static final String PROCESSINGTRAN_URL = "/ProcessingTranApi/ProcessingTran?";
}
