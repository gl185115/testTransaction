package ncr.res.mobilepos.promotion.dao;

import ncr.res.mobilepos.exception.DaoException;

public interface ICodeConvertDAO {

	/**
	 * Convert CCode to DptCode from MST_CCODEINFO
	 * 
	 * @param companyId ,
	 *        CCode,
	 *        storeId
	 * @return Dpt code.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String convertCCodeToDpt(String companyId, String code, String storeId) 
			throws DaoException;
	
	/**
	 * Convert MagCode to DptCode from MST_MAGCODEINFO
	 * 
	 * @param companyId ,
	 *        magCode,
	 *        storeId
	 * @return Dpt code.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String convertMagCodeToDpt(String companyId, String code, String storeId) 
			throws DaoException;
}

