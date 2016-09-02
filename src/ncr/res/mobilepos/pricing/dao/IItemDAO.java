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
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.BrandProducts;
import ncr.res.mobilepos.pricing.model.GroupLines;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
/**
 * IItemDAO is a DAO interface for Item.
 */
public interface IItemDAO {
    /**
     * Gets the list of items in a particular
     * Store, Common Store or even All Stores
     * Having Price Look Up(PLU) contains a
     * given key (pluCode or name).
     * Can bypass the searchLimit defined in the SystemConfig max search results
     * using limit
     *
     * @param   storeId The ID of the Store where
     *                          the items are located
     * @param   key     The given key used
     *                          to search the item(s) by code
     * @param   name     The given name used
     *                          to search the item(s) by name
     * @param   limit   The limit of the item to be search.
     * @param   priceIncludeTax The Price Include Tax.
     * @return  The list of items.
     * @throws  DaoException
     *  The Exception thrown when getting the List of Items failed.
     */
    List<Item> listItems(String storeId, String key, String name,
            int priceIncludeTax, int limit) throws DaoException;
 
    /**
     * Search for items in a particular Store
     *
     * @param   storeId The ID of the Store where
     *                          the items are located
     * @param   key     The given key used
     *                          to search the item(s) by code
     * @param   name     The given name used
     *                          to search the item(s) by name
     * @param   limit   The limit of the item to be search.
     * @param   priceIncludeTax The Price Include Tax.
     * 
     * @return  The list of items.
     * 
     * @throws  DaoException
     *  The Exception thrown when getting the List of Items failed.
     */
    List<Item> searchItems(String storeId, String key, String name, int limit, 
    		int priceIncludeTax) throws DaoException;

    /**
     * Set the an Item's unit price.
     * @param item       	The Item details object
     * 
     * @return  The new Item.
     * 
     * @throws DaoException The exception thrown when error occured.
     */
    Item setItemUnitPrice(Item item) throws DaoException;
    
    /**
     * Deletes an item.
     * @param retailStoreID The storeid of an item.
     * @param itemID        The item plucode.
     * @return resultBase   The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown if error exists.
     */
    ResultBase deleteItem(String retailStoreID,
            String itemID) throws DaoException;
    /**
     * Create an Item.
     * @param retailStoreID The item's retail store id.
     * @param itemid        The item's plucode.
     * @param item          The new values of the item.
     * @return              The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown when if error exists.
     */
    ResultBase createItem(String retailStoreID,
            String itemid, Item item) throws DaoException;

    /**
     * Update an Item.
     * @param retailStoreID The StoreID
     * @param itemid        The Item's PLU code.
     * @param item          The item to be updated.
     * @return              The updated item's information.
     * @throws DaoException The exception thrown when error occur.
     */
    Item updateItem(String retailStoreID,
            String itemid, Item item) throws DaoException;
    
    /**
     * 
     * @param  retailStoreID The Store ID which the item is located
     * @param  itemId The Item's Price Look Up Code
     * @return
     * @throws DaoException 
     */
    boolean isItemExists(String retailStoreID,
            String itemId) throws DaoException;
    
    
    /**
     *  Gets the data of Discount Reason both for Transaction and Item.
     * @return DiscountReasonList
     * @throws DaoException The exception thrown when error occur.
     */    
   
    ReasonDataList getDiscountReason() throws DaoException;
    /**
     *  Gets the data of Discount Button both for Transaction and Item.
     * @return ReasonDataList
     * @throws DaoException The exception thrown when error occur.
     */
    ReasonDataList getDiscountButtons() throws DaoException;
    /**
     * ���i���擾.
     *
     * @param plu
     *            ���i�R�[�h.
     * @param mdName
     *            ���i��.
     * @param productNum
     * 			  �i��.
     * @param color
     *           �J���[. 
     * @param itemSize
     * 			�T�C�Y
     * @param dpt
     * 			Div�R�[�h
     * @param venderCode
     * 			�u�����h
     * @param annual
     * 			�W�J�N�x
	 * @param priceIncludeTax
	 * 			  �l�̌ܓ��̃I�v�V����
     * @return Items
     * 			���i�����p���i���
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    SearchedProducts getItemInfo(String Plu, String MdName, String ProductNum,
				String Color, String ItemSize, String Dpt, String VenderCode,
				String Annual, String PriceIncludeTax) throws DaoException;

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
	 * �u�����h���i���擾.
	 * 
	 * @param dpt
	 *            Div�R�[�h
	 * @param venderCode
	 *            �u�����h
	 * @param annual
	 *            �W�J�N�x
	 * @param reservation
	 *            �\��\
	 * @param groupId
	 *            �O���[�vId
	 * @param line
	 *            �A�C�e��Id
	 * @param priceIncludeTax
	 * 			  �l�̌ܓ��̃I�v�V����
	 * @return BrandProducts
	 * 			     �u�����h���i���
	 * @throws DaoException
	 *             The exception thrown when error occurred.
	 */
	BrandProducts getBrandProductInfo(String dpt, String venderCode,
			String annual, String reservation, String groupId, String line,
			String priceIncludeTax) throws DaoException;
	
	/**
	 * �O���[�v�ƃ��C�����擾.
	 * 
	 * @return GroupLines
	 * 			     �O���[�v�ƃ��C�����
	 * @throws DaoException
	 *             The exception thrown when error occurred.
	 */
	GroupLines getGroupLines()throws DaoException;


	/**
	 * Gets the information of a single item by specifying the Store ID and and
	 * its corresponding PLU code.
	 * @param storeId
	 * @param pluCode
	 * @param companyId
	 * @param businessDate
	 * @return
	 * @throws DaoException
	 */
    Item getItemPriceByPLU(String storeId, String pluCode, String companyId, String businessDate) throws DaoException;

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
	 * Gets Item information.
	 * @param storeId �X�܃R�[�h
	 * @param pluCode ���Еi��
	 * @param companyId ��ЃR�[�h
	 * @return object itemInfo
	 * @throws DaoException The exception thrown when error occurred.
	 */
	Item getItemByPLU(String storeId, String pluCode, String companyId, String businessDate)
			throws DaoException;

	/**
	 * get Item attributes, such as color and size by PLU.
	 * @param storeId �X�܃R�[�h
	 * @param companyId ��ЃR�[�h
	 * @param pluCode itemId
	 * @return object itemInfo
	 * @throws DaoException The exception thrown when error occurred.
	 */
	Item getItemAttributeByPLU(String storeId, String pluCode, String companyId)
            throws DaoException;
}
