/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* SQLResultsConstants
*
* SQLResultsConstants is a class that
*  enumerates constant values for SQL results.
*
* Campos, Carlos (cc185102)
*/
package ncr.res.mobilepos.constant;

/**
 * SQLResultsConstants is a class that enumerates
 * constant values for SQL results.
 */
public final class SQLResultsConstants {
    /** Default Constructor. */
    private SQLResultsConstants() {    	
    }
    /**
     * Constant for SQL Result "No row affected".
     */
    public static final int NO_ROW_AFFECTED = 0;
    /**
     * Constant for SQL Result "One row affected".
     */
    public static final int ONE_ROW_AFFECTED = 1;
    /**
     * Constant for SQL Result "Two row affected".
     */
    public static final int TWO_ROW_AFFECTED = 2;
    /**
     * Constant for SQL Result for that tells no sql query called.
     */
    public static final int NO_QUERY_CALLED = -1;
    /**
     * Constant for SQL Result for that tells sqlquery has been duplicate.
     */
    public static final int ROW_DUPLICATE = -2627;
    /**
     * Constant for SQL Result that tells sqlQuery has been deadlock.
     */
    public static final int DEADLOCK = -1205;
}
