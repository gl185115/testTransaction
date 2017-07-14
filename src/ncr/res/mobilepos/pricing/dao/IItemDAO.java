/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IItemDAO
 *
 * Interface for the DAO specific to a table
 *
 * Meneses, Chris Niven
 */

package ncr.res.mobilepos.pricing.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickListItemType;
/**
 * IItemDAO is a DAO interface for Item.
 */
public interface IItemDAO {
    /**
     * Gets the information of a single item by
     * specifying the Store ID and
     * and its corresponding PLU code.
     * @param   companyid The Company ID
     * @param   storeid  The Store ID which the item is located
     * @param   pluCode  The Item's Price Look Up Code
     * @param   priceIncludeTax The Price Include Tax
     * @param   companyId The companyId Id
     * @param   bussinessDate The bussinessDate
     * @return  The details of the particular item
     * @throws  DaoException    Exception thrown when
     *                  getting the item information failed.
     */

    Item getItemByPLU(String storeid, String pluCode, String companyId,int priceIncludeTax,String bussinessDate) throws DaoException;

    /**
     * get the mixmatchInfo by sku
     * @param storeId 店舗コード
     * @param sku 自社品番
     * @param companyId 会社コード
     * @param businessDate currentDate
     * @return object
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getMixMatchInfo(String storeId,String sku,String companyId,String businessDate) throws DaoException;


	/**
     * get the item by param
     * @param storeId 店舗コード
     * @param sku 自社品番
     * @param companyId 会社コード
     * @return object itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getItemBypluCode(String storeId,String plucode,String companyId,String businessDate) throws DaoException;

    /**
     * get the items for pick list.
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param itemType
     * @return
     * @throws DaoException
     */
    List<PickListItemType> getPickListItems(String companyId, String storeId, String terminalId, String itemType) throws DaoException;


    /**
     * get the item by param
     * @param companyId 会社コード
     * @param storeId 店舗コード
     * @param plucode itemId
     * @return object itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getItemByApiData(String plucode,String companyId,String storeId) throws DaoException;
}
