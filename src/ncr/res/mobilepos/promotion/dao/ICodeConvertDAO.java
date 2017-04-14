package ncr.res.mobilepos.promotion.dao;

import ncr.res.mobilepos.exception.DaoException;

public interface ICodeConvertDAO {

	/**
	 * Convert CCode to DptCode from MST_CCODEINFO
	 * 
	 * @param companyId ,
	 *        CCode
	 * @return Dpt code.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String convertCCodeToDpt(String companyId, String code) 
			throws DaoException;
	
	/**
	 * Convert MagCode to DptCode from MST_MAGCODEINFO
	 * 
	 * @param companyId ,
	 *        magCode
	 * @return Dpt code.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String convertMagCodeToDpt(String companyId, String code) 
			throws DaoException;
}

