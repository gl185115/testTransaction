/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthRespConstants
 *
 * Enumerates constant values response by CreditAuthorization
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.constant;

/**
 * CreditAuthRespConstants is a class that enumerates constant values for Credit
 * Authorization response.
 */
public final class CreditAuthRespConstants {
	/** Default Constructor. */
	private CreditAuthRespConstants() {
	}

	/** The normal end for Status of the Credit Authorization response. */
	public static final String STATUS_NORMAL_END = "0";

	/** The error end for Status of the Credit Authorization response. */
	public static final String STATUS_ERROR_END = "9";

	/** The Credit Status of the Credit Authorization response. */
	public static final String CREDITSTATUS = "00";

	/** The Error Code normal end of the credit authorization response. */
	public static final String ERRORCODE_NORMAL_END = "    ";

	/** The Error Code error end of the credit authorization response. */
	public static final String ERRORCODE_ERROR_END = "99";

}
