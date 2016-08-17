package ncr.res.mobilepos.exception;

public class TillException extends Exception {

	/**
	 * TillException is a custom exception dedicated for Till exceptions such concurrent access scenarios.
	 * 
	 * @see SQLServerTillInfoDAO
	 */
	private static final long serialVersionUID = -6398636764704823189L;

    /**
     * The Till Error Code.
     */
    private int errorCode;
    
	/**
     * The Default Constructor.
     */
    public TillException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with message to set.
     * @param message   The error message.
     */
    public TillException(final String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with cause of the exception to set.
     * @param cause The Throwable exception.
     */
    public TillException(final Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with cause of the exception and message to set.
     * @param message       The Error Message.
     * @param cause         The Throwable exception.
     */
    public TillException(final String message, final Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * The Custom Constructor with message to set.
     * @param message   The error message.
     * @param errorCode 	The error code.
     */
    public TillException(final String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        // TODO Auto-generated constructor stub
    }
    
    /**
     * The Custom Constructor with cause of the exception, message  and errorCode to set.
     * @param message       The Error Message.
     * @param cause         The Throwable exception.
     * @param errorCode 	The error code.
     */
    public TillException(final String message, final Throwable cause, final int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Sets the error code.
     * @param errorCodeToSet error code to set
     */
    private void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns the errorcode.
     * @return the errorcode.
     */
    public final int getErrorCode() {
        return errorCode;
    }

}
