package ncr.res.mobilepos.exception;

import java.sql.SQLException;

import ncr.res.mobilepos.constant.SQLResultsConstants;

/**
 * DaoException is a custom exception dedicated for the DAO objects.
 *
 * @see SQLServerDAOFactory
 * @see SQLServerCreditDAO
 * @see SQLServerItemDAO
 * @see SQLServerPosLogDAO
 * @see SQLServerCustomerInfoDAO
 */
public class DaoException extends Exception {

    /**
     * The Serial version ID of the Exception.
     */
    private static final long serialVersionUID = 7428789400290930809L;
    
    /**
     * The DAO Error Code.
     */
    private int errorCode;
    
    /**
     * The Default Constructor.
     */
    public DaoException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with message to set.
     * @param message   The error message.
     */
    public DaoException(final String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with cause of the exception to set.
     * @param cause The Throwable exception.
     */
    public DaoException(final Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * The Custom Constructor with cause of the exception and message to set.
     * @param message       The Error Message.
     * @param cause         The Throwable exception.
     */
    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Tells if the major cause was ROW Duplicate Exception.
     * @return TRUE if row duplicate error occur. Else, FALSE.
     */
    public final boolean hasRowDuplicateExceptionOccur() {
        return this.getCause() instanceof SQLException
                && Math.abs(SQLResultsConstants.ROW_DUPLICATE)
                == ((SQLException) this.getCause()).getErrorCode();
    }
    
    /**
     * The Custom Constructor with cause of the exception, message  and errorCode to set.
     * @param message       The Error Message.
     * @param cause         The Throwable exception.
     * @param errorCode 	The error code.
     */
    public DaoException(final String message, final Throwable cause, final int errorCode) {
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
