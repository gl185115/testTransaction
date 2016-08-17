package ncr.res.mobilepos.constant;

public final class SuspendTransactionStatus {
	
	private SuspendTransactionStatus() {		
	}
	
	public static final String INITIAL = "0";
	public static final String FORWARD_COMPLETE = "1";
	public static final String TRANSACTION_CANCEL = "2";
	public static final String TRANSACTION_COMPLETE = "3";

}
