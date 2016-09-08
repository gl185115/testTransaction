/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* SQLStatementException
*
* SQLStatementException which handles Exception
*
* Dela Cerna, Jessel
*/

package ncr.res.mobilepos.exception;

/**
 * A Custom Exception dedicated for SQL Prepared Statement
 * of any type.
 *
 */
public class SQLStatementException extends Exception {

    /**
     * The Servial Version Unique ID.
     */
    private static final long serialVersionUID = 1901919295717413830L;

    /**
     * The default constructor.
     */
    public SQLStatementException() {
        super();
    }

    /**
     * A Custom Constructor of the class.
     * Sets the Message and the Cause of the Exception.
     *
     * @param message        The Message of the Exception.
     * @param cause            The Cause of the Exception.
     */
    public SQLStatementException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * A Custom Constructor of the class.
     * Sets the Message of the Exception.
     *
     * @param message        The Message of the Exception.
     */
    public SQLStatementException(final String message) {
        super(message);
    }

    /**
     * A Custom Constructor of the class.
     * Sets the Message of the Exception.
     *
     * @param cause            The Cause of the Exception.
     */
    public SQLStatementException(final Throwable cause) {
        super(cause);
    }

}
