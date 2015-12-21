/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLogRespConstants
 *
 * Enumerates the response of PosLog transaction
 *
 * Carlos Campos
 */

package ncr.res.mobilepos.journalization.constants;

/**
 * PosLogRespConstants is class that enumerates all
 * the constant values necessary For POSLog Journalization Response.
 *
 */
public final class PosLogRespConstants {
    /**
     * The Default contsructor.
     * Do not allow the class to be instantiated.
     */
    private PosLogRespConstants() {    	
    }
    /**
     * The Normal End.
     */
    public static final String NORMAL_END     = "0";
    /**
     * The Error End.
     */
    public static final String ERROR_END_1     = "1";
    /**
     * The Status for Busy.
     */
    public static final String ERROR_END_2     = "6";
}
