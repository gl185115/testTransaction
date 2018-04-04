/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * IStoreDAO
 *
 * Is a DAO interface for Store Maintenance.
 *
 */

package ncr.res.mobilepos.store.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.store.model.CMPresetInfo;
import ncr.res.mobilepos.store.model.PresetSroreInfo;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.StoreInfo;
import ncr.res.mobilepos.store.model.ViewStore;

/**
 * Interface class for Store maintenance DAO implementation.
 */
public interface IStoreDAO {
    
    /**
     * View Store Details.
     *
     * @param retailStoreID Store's id
     * @throws DaoException if error occurred.
     * @return Store instance of Store class.
     */
    ViewStore getStoreDetaiInfo(String retailStoreID,String companyId) throws DaoException;

    /**
     * List all stores details.
     *
     * @param companyId The company id of the store to be search and listed.
     * @param key   The store id of the Store(s) to be search and listed.
     * @param name  The name of the Store(s) to be search and listed.
     * @param limit the number of rows to retrieve.
     * @return  The List of store
     * @throws DaoException The exception when error occurred.
     */
    List<Store> listStores(String companyId, String key, String name, 
    		int limit) throws DaoException;

    /**
     * List all CM preset info.
     *
     * @param companyId         The company id of the CM preset info to be searched and listed.
     * @param storeId           The store id of the CM preset info to be searched and listed.
     * @param terminalId        The name of the CM preset info to be searched and listed.
     * @param businessDayDate   The business day date
     * @return  The List of CM Preset Info
     * @throws DaoException The exception when error occurred.
     */
    List<CMPresetInfo> listCMPresetInfo(String companyId, String storeId, String terminalId, 
            String businessDayDate) throws DaoException;
    
    /**
     * get the PresetSroreInfo
     * @param companyId The Id of company
     * @param storeId The Id of Store
     * @param workStactionId  The Id of workStaction
     * @return PresetSroreInfo
     * @throws DaoException The Exception of Sql
     */
    PresetSroreInfo getPresetSroreInfo(String companyId,String storeId,String workStactionId) throws DaoException;
    
    /**
     * get the SummaryReceiptNo
     * @param companyId The Id of company
     * @param storeId The Id of Store
     * @param workStactionId  The Id of workStaction
     * @param traning  The flag of traningMode
     * @return Subnum1
     * @throws DaoException The Exception of Sql
     */
    String getSummaryReceiptNo(String companyId,String storeId,String workStactionId, String traning) throws DaoException;
    
    /**
     * update the SummaryReceiptNo
     * @param SubNum1 The SummaryReceiptNo
     * @param companyId The Id of company
     * @param storeId The Id of Store
     * @param workStactionId  The Id of workStaction
     * @param traning  The flag of traningMode
     * @return The updated instance of SummaryReceiptNo.
     * @throws DaoException The Exception of Sql
     */
    int updateSummaryReceiptNo(int SubNum1,String companyId,String storeId,String workStactionId, String traning) throws DaoException;
    
	
    /**
     * get the sunNum2
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param businessdaydate
     * @return The updated instance of StoreInfo.
     * @throws DaoException The Exception of Sql
     */
	StoreInfo getStoreTotal(String companyId, String storeId, String terminalId, String businessDayDate) throws DaoException;
	
    /**
     * add Store Total
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param businessdaydate
     * @return StoreInfo
     * @throws DaoException The Exception of Sql
     */
    StoreInfo addStoreTotal(String companyId, String storeId, String terminalId, String businessdaydate) throws DaoException;
    
}
