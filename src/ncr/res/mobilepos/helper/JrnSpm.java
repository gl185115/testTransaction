package ncr.res.mobilepos.helper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;

/**
 * <P>SPM (System Performance Monitor) for Journalization<br/>
 * Monitors the processing time during journalize.<br/>
 * From the start time after receiving the PosLog <br/>
 * Until the time before returning response.
 * 
 * @author JD185128
 *
 */
public class JrnSpm {

	/**
	 * Header titles column of Spm file.
	 */
	public static final String HEADER = "STORE  TERM TX   TYPE           ITEM  ST START         END           ProcTime\n";
	/**
	 * For Unknown data.
	 */
	public static final String NA = "NA";
	/**
	 * Handles the Journalization SPM file.
	 */
	private static volatile SpmFileWriter fw;
	/**
	 * Start time after receiving the poslog data.
	 */
	private long startTime;
	/**
	 * End time before returning the response.
	 */
	private long endTime;
	/**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "POSLogUtility";

	/**
	 * Single instance constructor.
	 * 
	 * @param fileWriter
	 *            the journalization spm filewriter.
	 */
	private JrnSpm(SpmFileWriter fileWriter) {
		fw = fileWriter;
		startTime = System.currentTimeMillis();
	}
    
    /**
     * Starts the logging of spm data.
     * @param fw FileWriter・ｽ・ｽ・後う・ｽ・ｽ・ｽ・ｽ・ｽ・ｽX・ｽ・ｽ^・ｽ・ｽ・ｽ・ｽ・ｽ・ｽX
     */
    public static JrnSpm startSpm(SpmFileWriter fw) {
    	return new JrnSpm(fw);
    }
    
	/**
     * Sets the end time and adds the information to spm file.<br>
     * @param poslog POSLog that was received.
     * @param status Status of POSLog journalization.
     */
	public void endSpm(PosLog poslog, int status) {		
		endTime = System.currentTimeMillis();
		
		String s = makeSpmData(poslog, status);		
		try {
			fw.write(s);
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, PROG_NAME+".endSpm", Logger.RES_EXCEP_IO, "Failed to write info into spm file. "+e.getMessage());
		}
	}
    
    /**
     * Constructs the SPM Data to be written to file.
     * @param poslog	POSLog instance.	
     * @param status	Status of POSLog journalization.
     * @return	Constructed spm string data in one line.
     */
    private String makeSpmData(PosLog poslog, int status) {
        StringBuilder sb = new StringBuilder(64);
        int itemCnt = 0;
        
        Transaction tran = null; 
		if (poslog != null){
			tran = poslog.getTransaction();
		}else{
			return sb.toString();
		}
		
		if (tran == null){
			return sb.toString();
		}
        String storeId = tran.getRetailStoreID();
		String workStationId = tran.getWorkStationID().getValue();
		String txId = tran.getSequenceNo();
		
		long mt = endTime - startTime;	
	    Calendar ca = Calendar.getInstance();
		ca.clear();
	    ca.setTimeInMillis(startTime);
	
	    DecimalFormat nf = new DecimalFormat("00");
		DecimalFormat nf3 = new DecimalFormat("000");
	
		String txType = POSLogHandler.getTransactionType(poslog);
		
		// get item count
		RetailTransaction retailTransaction = tran.getRetailTransaction();
		if (retailTransaction == null) {
			itemCnt = 1;
		} else {
			itemCnt = retailTransaction.getItemCount();
		}
		
		sb.append(String.format("%6s", (storeId != null) ? storeId: NA).replace(" ", "0"))
		    .append(" ")
		    .append(String.format("%4s", (workStationId != null) ? workStationId: NA).replace(" ", "0"))
		    .append(" ")
		    .append(String.format("%4s", (txId != null) ? txId: NA).replace(" ", "0"))
		    .append(" ")
		    .append(String.format("%1$-" + 14 + "s", txType))
		    .append(" ")
		    .append(String.format("%3s", itemCnt).replace(" ", "0"))	    
		    .append("   ")
		    .append(status).append("  ")
		    .append(nf.format(ca.get(Calendar.HOUR_OF_DAY))).append(":")
		    .append(nf.format(ca.get(Calendar.MINUTE))).append(":")
		    .append(nf.format(ca.get(Calendar.SECOND))).append(".")
		    .append(nf3.format(ca.get(Calendar.MILLISECOND)))
		    .append("  ");
		ca.clear();
		ca.setTimeInMillis(endTime);
		sb.append(nf.format(ca.get(Calendar.HOUR_OF_DAY))).append(":")
		    .append(nf.format(ca.get(Calendar.MINUTE))).append(":")
		    .append(nf.format(ca.get(Calendar.SECOND))).append(".")
		    .append(nf3.format(ca.get(Calendar.MILLISECOND)))
		    .append("  ").append(nf3.format(mt / 1000)).append(".")
		    .append(nf3.format(mt % 1000)).append("\n");
		
	    return sb.toString();
    }
}