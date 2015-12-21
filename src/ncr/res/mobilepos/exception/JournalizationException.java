package ncr.res.mobilepos.exception;

/**
 * JournalizationException is a custom exception dedicated
 * for journalization exceptions.
 */
public class JournalizationException extends Exception {

    /**
     * the serialVersionUID.
     */
    private static final long serialVersionUID = -1935511637333992476L;

    /**
     * the Journalization Error Code.
     */
    private int errorCode;

    /**
     * The Default Constructor.
     * @param errorCodeToSet the error code to set
     */
    public JournalizationException(final int errorCodeToSet) {
        // TODO Auto-generated constructor stub
        setErrorCode(errorCodeToSet);
    }

    /**
     * The Custom Constructor with message to set.
     * @param message   The error message.
     * @param errorCodeToSet the error code to set
     */
    public JournalizationException(final String message,
            final int errorCodeToSet) {
        super(message);
        // TODO Auto-generated constructor stub
        setErrorCode(errorCodeToSet);
    }

    /**
     * The Custom Constructor with cause of the exception to set.
     * @param cause The Throwable exception.
     * @param errorCodeToSet the error code to set
     */
    public JournalizationException(final Throwable cause,
            final int errorCodeToSet) {
        super(cause);
        // TODO Auto-generated constructor stub
        setErrorCode(errorCodeToSet);
    }

    /**
     * The Custom Constructor with cause of the exception and message to set.
     * @param message       The Error Message.
     * @param cause         The Throwable exception.
     * @param errorCodeToSet the error code to set
     */
    public JournalizationException(
            final String message, final Throwable cause,
            final int errorCodeToSet) {
        super(message, cause);
        // TODO Auto-generated constructor stub
        setErrorCode(errorCodeToSet);
    }

    /**
     * sets the error code.
     * @param errorCodeToSet error code to set
     */
    private void setErrorCode(final int errorCodeToSet) {
        this.errorCode = errorCodeToSet;
    }

    /**
     * returns the errorcode.
     * @return the errorcode.
     */
    public final int getErrorCode() {
        return errorCode;
    }

}
