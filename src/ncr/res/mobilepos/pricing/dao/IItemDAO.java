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
import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;
import ncr.res.mobilepos.promotion.model.Sale;
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
     * @param   mapTaxId The mapTaxId
     * @param   comstdName The comstdName
     * @return  The details of the particular item
     * @throws  DaoException    Exception thrown when
     *                  getting the item information failed.
     */

    Item getItemByPLU(String storeid, String pluCode, String companyId,int priceIncludeTax,String bussinessDate,Map<String, String> mapTaxId, String comstdName) throws DaoException;

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
     * @param itemType
     * @return
     * @throws DaoException
     */
    List<PickListItemType> getPickListItems(String companyId, String storeId, String itemType) throws DaoException;
    
    
    /**
     * get the item by param
     * @param companyId 会社コード
     * @param plucode itemId
     * @return object itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getItemByApiData(String plucode,String companyId) throws DaoException;
    
    /**
     * get the item by param
     * @param companyId 会社コード
     * @param storeid storeid
     * @param itemCode itemCode
     * @return itemInfo itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Sale getItemNameFromPluName(final String companyId, final String storeid, final String itemCode) throws DaoException;

    /**
     * 割引券発行管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	void getCouponInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;

    /**
     * プレミアム商品管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	void getPremiumitemInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;

    /**
     * 特売管理マスタ 情報取得
     * @param the pricePromInfo
     * @param searchedItem the Item
     * @param the salePrice
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPromDetailInfoList(PricePromInfo pricePromInfo, Item item, double salePrice) throws DaoException;
	
    /**
     * 特売管理マスタ 情報取得
     * @param the pricePromInfo
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPromDetailList(List<PricePromInfo> pricePromInfo, Item item) throws DaoException;

    /**
     * バンドルミックス 情報取得
     * @param priceMMInfo 
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPriceMMInfoList(PriceMMInfo priceMMInfo, Item item) throws DaoException;
	
    /**
     * バンドルミックス 情報取得
     * @param priceMMInfo 
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPriceMMDetailList(final List<PriceMMInfo> priceMMInfo, final Item searchedItem) throws DaoException;
	
    /**
     * 緊急売変 情報取得
     * @param priceUrgentInfo 
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPriceUrgentInfo(PriceUrgentInfo priceUrgentInfo, Item item) throws DaoException;

    /**
     * クラブ買替えサポート管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasReplaceSupportDetailInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;
}
