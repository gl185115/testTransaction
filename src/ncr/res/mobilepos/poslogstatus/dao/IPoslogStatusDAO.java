package ncr.res.mobilepos.poslogstatus.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;

public interface IPoslogStatusDAO {

	/**
    * Check TXL_SALES_JOURNAL's value that Status and SendStatus1(or SendStatus2).
    * @param consolidation - consolidation flag
    * @param transfer - transfer flag
    * @param columnName - columnName
    * @return PoslogStatus Model
    * @throws DaoException - exception
    */
    PoslogStatusInfo checkPoslogStatus(boolean consolidation, boolean transfer, String columnName) throws DaoException;
    
    /**
     * check poslog status and return count of poslog not deal with.
     * @param checkColumnName Column name to Check
     * @return PoslogStatusInfo.
     * @throws DaoException - if database fails.
     */
    int countUnsendPoslog(String checkColumnName) throws DaoException;
}
