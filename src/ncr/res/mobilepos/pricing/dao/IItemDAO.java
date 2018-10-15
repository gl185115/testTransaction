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
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
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
     * @return  The details of the particular item
     * @throws  DaoException    Exception thrown when
     *                  getting the item information failed.
     */

    Item getItemByPLU(String storeid, String pluCode, String companyId,int priceIncludeTax,String bussinessDate) throws DaoException;

    /**
     * get the mixmatchInfo by sku
     * @param storeId �X�܃R�[�h
     * @param sku ���Еi��
     * @param companyId ��ЃR�[�h
     * @param businessDate currentDate
     * @return object
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getMixMatchInfo(String storeId,String sku,String companyId,String businessDate) throws DaoException;


	/**
     * get the item by param
     * @param storeId �X�܃R�[�h
     * @param sku ���Еi��
     * @param companyId ��ЃR�[�h
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
     * @param companyId ��ЃR�[�h
     * @param plucode itemId
     * @return object itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Item getItemByApiData(String plucode,String companyId) throws DaoException;
    
    /**
     * get the item by param
     * @param companyId ��ЃR�[�h
     * @param storeid storeid
     * @param itemCode itemCode
     * @return itemInfo itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    Sale getItemNameFromPluName(final String companyId, final String storeid, final String itemCode) throws DaoException;

    /**
     * ���������s�Ǘ��}�X�^ ���擾
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	void getCouponInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;

    /**
     * �v���~�A�����i�Ǘ��}�X�^ ���擾
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	void getPremiumitemInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;

    /**
     * �����Ǘ��}�X�^ ���擾
     * @param the pricePromInfo
     * @param searchedItem the Item
     * @param the salePrice
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPromDetailInfoList(PricePromInfo pricePromInfo, Item item, double salePrice) throws DaoException;

    /**
     * �o���h���~�b�N�X ���擾
     * @param priceMMInfo 
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasPriceMMInfoList(PriceMMInfo priceMMInfo, Item item) throws DaoException;

    /**
     * �N���u���ւ��T�|�[�g�Ǘ��}�X�^ ���擾
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	boolean isHasReplaceSupportDetailInfo(String retailStoreId, Item item, String companyId, String businessDate) throws DaoException;
}
