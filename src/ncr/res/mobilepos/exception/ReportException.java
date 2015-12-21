package ncr.res.mobilepos.exception;

public class ReportException extends Exception {

	public ReportException() {
		super("Report Exception.");
	}

	public ReportException(String message) {
		super(message);
		
	}

	public ReportException(Throwable cause) {
		super(cause);
		
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
		
	}

	
}
