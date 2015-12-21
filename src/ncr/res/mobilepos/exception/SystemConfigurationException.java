package ncr.res.mobilepos.exception;

public class SystemConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemConfigurationException() {
		super("System Configuration Exception.");
	}

	public SystemConfigurationException(String msg) {
		super(msg);
		
	}

	public SystemConfigurationException(Throwable cause) {
		super(cause);
		
	}

	public SystemConfigurationException(String msg, Throwable cause) {
		super(msg, cause);
		
	}

	

}
