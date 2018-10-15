package ncr.res.mobilepos.promotion.dao;

import ncr.res.mobilepos.exception.DaoException;

public interface ICashingUnitConvertDAO {

	/**
     * Convert RecordId to CashingUnit from MST_MAGCODEINFO.
     * @param companyId ,
     *        recordId    
     * @return CashingUnit cashingUnit
     * @throws DaoException Exception when error occurs.
     */
    String convertRecordIdToCashingUnit(String companyId, String recordId) 
            throws DaoException;
}

