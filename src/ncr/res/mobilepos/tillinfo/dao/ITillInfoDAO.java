package ncr.res.mobilepos.tillinfo.dao;

import java.sql.Connection;
import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

/**
 * Interface class for Store maintenance DAO implementation.
 */
public interface ITillInfoDAO {
	
    /**
     * View Till Details.
     *
     * @param storeid Store's id
     * @param tillid Till/Drawer identifier
     * @throws DaoException if error occurred.
     * @return Till instance of Till class.
     */
	ViewTill viewTill(String storeid, String tillId) 
    		throws DaoException;
    /**
     * Creates Till.
     *
     * @param storeID - Store number
     * @param tillID - till number
     * @param till - Till
     * @throws DaoException if error occurred
     * @return ResultBase
     */
    ResultBase createTill(String storeId, String tillId, Till till)
             throws DaoException;    
    /**
     * Updates Till.
     *
     * @param storeID - Store number
     * @param tillID - till number
     * @param till - Till
     * @throws DaoException if error occurred
     * @return ResultBase
     */
    ViewTill updateTill(String storeId, String tillId, Till till)
             throws DaoException;   
    
    /**
     * Updates specific fields of Till after successful SOD/EOD.
     * @param storeId - Store number which the Till belongs to.
     * @param tillId - Till/Drawer identifier
     * @param oldSodFlag - The old value of sod flag to be changed.
     * @param newSodFlag - The new value of sod flag to bet set.
     * @param oldEodFlag - The old value of eod flag to be changed.
     * @param newEodFlag - The new value of eod flag to bet set.
     * @param bizDate - The business day date from SOD POSlog
     * @param operatorId - The operator who performs the SOD/EOD. Retrieved from SOD Poslog.
     * @return ResultBase
     * @throws DaoException
     */
    void updateTillOnJourn(Connection conn, Till till,String oldSodFlag, String oldEodFlag, boolean isEnterprise)
    	throws DaoException, TillException;
    
    ResultBase searchLogonUsers(String companyId, String storeId, String tillId, 
    		String terminalId) throws DaoException;
    
    /**
     * get Till information list.
     * @param storeID - Store number
     * @return Till information list
     */
    List<Till> getTillInformation(String storeId)
             throws DaoException;

    /**
     * Fetches single till by primary key search.
     * @param companyId
     * @param storeId
     * @param tillId
     * @return Found till, null for not found.
     */
    Till fetchOne(final String companyId, final String storeId, final String tillId) throws DaoException;

    /**
     * Updates specific fields of Till when daily operations are triggered, SOD or EOD.
     * @param currentTill Till to have current data
     * @param updatingTill Till to have updating data
     * @return ResultBase
     * @throws DaoException
     */
    ResultBase updateTillDailyOperation(Till currentTill, Till updatingTill) throws DaoException;

}
