package ncr.res.mobilepos.poslogstatus.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;
import ncr.res.mobilepos.poslogstatus.model.UnsendTransaction;

public interface IPoslogStatusDAO {

	/**
    * Check TXL_SALES_JOURNAL's value that Status and SendStatus1(or SendStatus2).
    * @param consolidation - consolidation flag
    * @param transfer - transfer flag
    * @param columnName - columnName
	 * @param poslogTransferColumnName 
	 * @param businessDayDate 
	 * @param retailStoreId 
    * @return PoslogStatus Model
    * @throws DaoException - exception
    */
    PoslogStatusInfo checkPoslogStatus(boolean consolidation, boolean transfer, String companyId, String retailStoreId, String businessDayDate, String poslogTransferColumnName) throws DaoException;
    

    /**
     * @param companyId
     * @param retailStoreId
     * @param businessDayDate
     * @param checkColumnName
     * @return
     * @throws DaoException
     */
    int countUnsendPoslog(String companyId, String retailStoreId, String businessDayDate, String checkColumnName) throws DaoException;

	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param checkColumnName
	 * @return
	 * @throws DaoException
	 */
	int countFailedPoslog(String companyId, String retailStoreId, String businessDayDate, String checkColumnName)
			throws DaoException;


	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param checkColumnName
	 * @throws DaoException
	 */
	void retryFailedPoslog(String companyId, String retailStoreId, String businessDayDate, String checkColumnName)
			throws DaoException;

    List<UnsendTransaction> getUnsendTransactionList(String companyId, String retailStoreId, String businessDayDate,
            String checkColumnName) throws DaoException;
}
