/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ItemResource
 *
 * Resource which provides Web Service for Item details retrieval
 *
 * Meneses, Chris Niven
 * Campos, Carlos
 */

package ncr.res.mobilepos.pricing.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.helper.ItemHelper;
import ncr.res.mobilepos.pricing.model.BrandProducts;
import ncr.res.mobilepos.pricing.model.GroupLines;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
import ncr.res.mobilepos.pricing.model.PickList;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

/**
 * ItemResource Web Resource Class
 *
 * <P>
 * Supports MobilePOS Item Pricing/Search processes.
 *
 */
@Path("/pricing")
@Api(value="/pricing", description="���i�ݒ�API")
public class ItemResource {

    /**
     * ServletContext.
     */
    @Context
    private ServletContext context;

    /**
     * SecurityContext.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }

    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * The resource DAO factory.
     */
    private DAOFactory sqlServerDAO;
    /**
     * The program name.
     */
    private String progname = "ItemRes";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private String pathName = "pricing";

    /**
     * Default Constructor. Instantiate ioWriter and sqlServerDAO member
     * variables.
     */
    public ItemResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Sets the DaoFactory of the ItemResource to use the DAO methods.
     *
     * @param daoFactory
     *            The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daoFactory) {
        this.sqlServerDAO = daoFactory;
    }

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * Method called by the Web Service for retrieving the information of an
     * Item by specifying its Store ID and the Item's price Look Up.<br>
     * <br>
     *
     * Interface Name: Price Lookup<br>
     * Request Type: POST<br>
     * URL: {Base URI}/pricing/{storeid}/{plucode}<br>
     * Produces: {@link SearchedProduct} JSON Object<br>
     *
     * @param storeID
     *            The Store ID of the store which the item is located
     * @param pluCode
     *            The Item's Price Look Up
     * @param companyId
     * @param bussinessDate
     * @return The details of an Item
     */
    @Path("/{storeid}/{plucode}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���X�܂̓����i�̒l�i���オ��", response=SearchedProduct.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="�v���W�F�N�g�͑��݂��Ȃ�")
        })
    public final SearchedProduct getItemByPLUcode(
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @PathParam("storeid") final String storeID,
    		@ApiParam(name="plucode", value="���i�̒l�i���オ��") @PathParam("plucode") final String pluCode,
    		@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId,
    		@ApiParam(name="bussinessDate", value="�c�Ɠ�") @FormParam("bussinessDate") final String bussinessDate) {

        String functionName = "ItemResource.getItemByPLUcode";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("storeid", storeID)
                .println("plucode", pluCode)
                .println("companyId", companyId);
        SearchedProduct searchedProduct = new SearchedProduct();

        // Check if pricingtype is normal(0).
        // If 1, only PLU code is returned without searching database (MST_PLU).
        // If 0, continue searching.
        if (!isNormalPricing()) {
            Item item = new Item();
            item.setItemId(pluCode);
            item.setAgeRestrictedFlag(0);
            searchedProduct.setItem(item);
            tp.println("Pricing is not normal.");
            tp.methodExit(searchedProduct);
            return searchedProduct;
        }

        Item returnItem = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            returnItem = itemDAO.getItemByPLU(storeID, pluCode,companyId,Integer.parseInt(priceIncludeTax),bussinessDate);

        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get the item details.\n" + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchedProduct.setMessage(daoEx.getMessage());
        } catch (Exception e) {
            tp.println(context.toString());
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            searchedProduct.setMessage(e.getMessage());
        } finally {
            if (null == returnItem) {
                returnItem = new Item();
                returnItem.setItemId(pluCode);
                returnItem.setAgeRestrictedFlag(0);
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
            }
            searchedProduct.setItem(returnItem);
            tp.methodExit(searchedProduct);
        }

        return searchedProduct;
    }

    /**
     * Method called by the Web Service for retrieving a list of Items in a
     * particular Store. This is used by the Supervisor maintenance. *
     *
     * @param storeId
     *            The ID of the store which the Items are located
     * @param key
     *            The key used to search the Items by PLU
     * @param name
     *            The name is used to search the Item by Name (En,Ja)
     * @param deviceId
     *            The device id
     * @param limit
     *            Search Limit 0 = for SystemConfig.searchMaxResult
     *                        -1 = search all
     *
     * @return The list of Items having Price Look Up code of same key
     */
    @Path("/list")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final SearchedProducts list(
            @FormParam("storeid") final String storeId,
            @FormParam("key") final String key,
            @FormParam("deviceid") final String deviceId,
            @FormParam("limit") final int limit,
            @FormParam("name") final String name) {

        String functionName = "ItemResource.list";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("storeid", storeId).println("key", key)
                .println("name", name)
                .println("deviceid", deviceId).println("limit", limit);

        SearchedProducts searchproduct = new SearchedProducts();

        if (!isNormalPricing()) {
            searchproduct
                    .setNCRWSSResultCode(ResultBase.RES_ITEM_MANUAL_SEARCH_NOTALLOWED);
            tp.println("Pricing is not normal.");
            tp.methodExit(searchproduct);
            return searchproduct;
        }


        List<Item> itemData = null;
        try {
            int searchLimit = (limit == 0) ? GlobalConstant
                    .getMaxSearchResults() : limit;
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            itemData = itemDAO.listItems(storeId, key,name,
                    Integer.valueOf(priceIncludeTax), searchLimit);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, "ItemResource.List",
                    Logger.RES_EXCEP_DAO, "Failed to get the list of items.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                searchproduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                searchproduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchproduct.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            searchproduct.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            searchproduct.setItems(itemData);
            tp.methodExit(searchproduct);
        }

        return searchproduct;
    }

    /**
     * Method called by the Web Service for searching Items from a particular Store.
     *
     * @param storeId
     *            The ID of the store which the Items are located
     * @param key
     *            The key used to search the Items by PLU
     * @param name
     *            The name is used to search the Item by Name (En,Ja)
     * @param limit
     *            Search Limit 0 = for SystemConfig.searchMaxResult
     *                        -1 = search all
     *
     * @return The list of Items having same key or name
     */
    @Path("/search")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="����̃�������{������", response=SearchedProducts.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ITEM_MANUAL_SEARCH_NOTALLOWED, message="�艿�͐���łȂ�")
    })
    public final SearchedProducts search(
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @FormParam("storeid") final String storeId,
    		@ApiParam(name="key", value="���i�R�[�h") @FormParam("key") final String key,
    		@ApiParam(name="name", value="���i��") @FormParam("name") final String name,
    		@ApiParam(name="limit", value="�������") @FormParam("limit") final int limit) {

        String functionName = "ItemResource.search";

        tp.methodEnter(DebugLogger.getCurrentMethodName())
        	.println("storeid", storeId)
        	.println("key", key)
        	.println("name", name)
        	.println("limit", limit);

        SearchedProducts searchedProducts = new SearchedProducts();

        if (!isNormalPricing()) {
        	searchedProducts.setNCRWSSResultCode(
        			ResultBase.RES_ITEM_MANUAL_SEARCH_NOTALLOWED);
            tp.println("Pricing is not normal.");
            tp.methodExit(searchedProducts);

            return searchedProducts;
        }

        List<Item> itemData = null;
        try {
            int searchLimit = (limit == 0) ?
            		GlobalConstant.getMaxSearchResults() : limit;

            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            itemData = itemDAO.searchItems(storeId, key, name, searchLimit,
            		Integer.valueOf(priceIncludeTax));
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, functionName,
                    Logger.RES_EXCEP_DAO, "Failed to get the list of items.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
            	searchedProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	searchedProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchedProducts.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            searchedProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
        	searchedProducts.setItems(itemData);
            tp.methodExit(searchedProducts);
        }

        return searchedProducts;
    }

    /**
     * The Web Method called to update Item's.
     *
     * @param storeId
     *            The Retail Store ID.
     * @param itemId
     *            The Item ID.
     * @param jsonItem
     *            The JSON representation of Item.
     * @return The Json representation of the updated item.
     */
    @Path("/maintenance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="�v���W�F�N�g�|�C���g�̑��x", response=ItemMaintenance.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="���ڂ����݂��Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ITEM_ALREADY_EXIST, message="���̓X�͌�����Ȃ���"),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ITEM_STORE_NOT_EXIST, message="���̓X�͌�����Ȃ���"),
            @ApiResponse(code=ResultBase.RES_ERROR_DPTNOTFOUND, message="���傪�݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ITEM_DPT_NOT_EXIST, message="���Y�v���W�F�N�g�̕����T�����ĂĂ��Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
            @ApiResponse(code=ResultBase.RES_ITEM_NO_UPDATE, message="���̓X�͌�����Ȃ���")
        })
    public final ItemMaintenance updateItem(
    		@ApiParam(name="retailstoreid", value="�����X�R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="itemid", value="���i�R�[�h") @FormParam("itemid") final String itemId,
    		@ApiParam(name="item", value="���i") @FormParam("item") final String jsonItem) {

        String functionname = "ItemResource.updateItem";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("RetailStoreID", storeId).println("ItemID", itemId)
                .println("Item", jsonItem);

        ItemMaintenance itemMaintenance = new ItemMaintenance();
        itemMaintenance.setStoreid(storeId);

        if (StringUtility.isNullOrEmpty(itemId)) {
            itemMaintenance.setNCRWSSResultCode(ResultBase.RES_ITEM_NO_UPDATE);
            tp.println("ItemID is required.");
            tp.methodExit(itemMaintenance);
            return itemMaintenance;
        }

        try {
            JsonMarshaller<Item> itemJsonMarshaller = new JsonMarshaller<Item>();
            Item item = itemJsonMarshaller.unMarshall(jsonItem, Item.class);
            String appId = pathName.concat(".maintenance");
            item.setUpdAppId(appId);
            item.setUpdOpeCode(getOpeCode());
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            String newStoreId = item.getRetailStoreId();

            boolean  exists = itemDAO.isItemExists(storeId, itemId);
            if (!exists) {
                Item returnItem = new Item();
                returnItem.setAgeRestrictedFlag(0);
                itemMaintenance.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
                return itemMaintenance;
            }

            if(newStoreId != null && (!storeId.equals(newStoreId) || !itemId.equals(item.getItemId()))){
                boolean hasNoNewItem = StringUtility.isNullOrEmpty(item.getItemId());
                String newStoreItemId = (hasNoNewItem) ? itemId: item.getItemId();
                exists = itemDAO.isItemExists(newStoreId, newStoreItemId);

                if (exists) {
                    Item  returnNewItem = new Item();
                    returnNewItem.setAgeRestrictedFlag(0);
                    itemMaintenance.setNCRWSSResultCode(ResultBase.RES_ITEM_ALREADY_EXIST);
                    return itemMaintenance;
                }
            }


            //validate item's store if exists
            if (!isEnterpriseStore(storeId) && !isEnterpriseStore(newStoreId)) {
                StoreResource storeRes = new StoreResource();
                ViewStore store = new ViewStore();

                store = storeRes.viewStore(newStoreId == null ? storeId : newStoreId);
                if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
                    itemMaintenance
                            .setNCRWSSResultCode(ResultBase.RES_ITEM_STORE_NOT_EXIST);
                    tp.println("New RetailStoreID does not exist.");
                    tp.methodExit(itemMaintenance);
                    return itemMaintenance;
                }
            }



            // validate item's store department if exists.
            if (null != item.getDepartment()) {
                DepartmentResource deptRes = new DepartmentResource();
                ViewDepartment viewDept = deptRes.selectDepartmentDetail(item.getCompanyId(),
                        newStoreId == null ? storeId: newStoreId, item.getDepartment());
                if (viewDept.getNCRWSSResultCode() == ResultBase.RES_ERROR_DPTNOTFOUND) {
                    itemMaintenance
                            .setNCRWSSResultCode(ResultBase.RES_ITEM_DPT_NOT_EXIST);
                    tp.println("Item's DepartmentID for Store with Store ID"
                            +  newStoreId + " does not exist.");
                    return itemMaintenance;
                }
            }

            Item updatedItem = itemDAO.updateItem(storeId, itemId, item);
            if (null == updatedItem) {
                itemMaintenance
                        .setNCRWSSResultCode(ResultBase.RES_ITEM_NO_UPDATE);
                updatedItem = new Item();
                updatedItem.setItemId(itemId);
                tp.println("Item not updated.");
            }
            itemMaintenance.setItem(updatedItem);
        } catch (DaoException e) {
            Throwable cause = e.getCause();
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_DAO,
                    cause.getMessage());
            if (cause instanceof SQLException) {
                int errorcode = ((SQLException) cause).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_ITEM_ALREADY_EXIST;
                    tp.println("Item update duplicated.");
                } else {
                    errorcode = ResultBase.RES_ERROR_DB;
                }
                itemMaintenance.setMessage(cause.getMessage());
                itemMaintenance.setNCRWSSResultCode(errorcode);
            } else {
                itemMaintenance.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                itemMaintenance.setMessage(cause.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            itemMaintenance.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            itemMaintenance.setMessage(e.getMessage());
        } finally {
            tp.methodExit(itemMaintenance);
        }

        return itemMaintenance;
    }

    /**
     * Item Maintenance for changing the price of an item.
     *
     * @param storeID
     *            Store's store number
     * @param pluCode
     *            Item's plu code
     * @param salesUnitPrice
     *            Item's sales unit price
     * @return Edited item Item with price changed
     */
    @Path("maintenance/{storeid}/{plucode}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ItemMaintenance changePrice(
            @PathParam("storeid") final String storeID,
            @PathParam("plucode") final String pluCode,
            @FormParam("regularsalesunitprice") final String salesUnitPrice) {

        String functionName = "ItemResource.changePrice";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("storeid", storeID).println("plucode", pluCode)
                .println("regularsalesunitprice", salesUnitPrice);

        Item itemData = null;
        ItemMaintenance editedItem = new ItemMaintenance();
        double newSalesPrice = 0.0;

        // if salesprice is negative or empty, do not update
        try {
            newSalesPrice = Double.parseDouble(salesUnitPrice);
            if (newSalesPrice < 0) {
                editedItem
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_PRICECHANGE);
                tp.println("RegularSalesUnitPrice is less than 0.");
                tp.methodExit(editedItem);
                return editedItem;
            }
        } catch (NumberFormatException nfe) {
            editedItem.setNCRWSSResultCode(ResultBase.RES_ERROR_PRICECHANGE);
            tp.println("Cannot parse SalesUnitPrice.");
            tp.methodExit(editedItem);
            return editedItem;
        }

        try {
            String appId = pathName.concat(".maintenance.price");
            Item item = new Item();
            item.setRetailStoreId(storeID);
            item.setItemId(pluCode);
            item.setRegularSalesUnitPrice(newSalesPrice);
            item.setUpdAppId(appId);
            item.setUpdOpeCode(getOpeCode());

            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            itemData = itemDAO.setItemUnitPrice(item);

            if (null == itemData) {
                itemData = new Item();
                itemData.setItemId(pluCode);
                itemData.setAgeRestrictedFlag(0);
                editedItem
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_PRICECHANGE);
                tp.println("Item's price not updated.");
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                editedItem.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                editedItem.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            editedItem.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            editedItem.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            editedItem.setItem(itemData);
            tp.methodExit(editedItem);
        }

        return editedItem;
    }

    /**
     * Deletes an item.
     *
     * @param storeId
     *            The storeid of an item.
     * @param itemId
     *            The plu code of an item.
     * @return resulBase The ResultBase object that contains resultcode.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���ڂ��폜����", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="�v���W�F�N�g�͑��݂��Ȃ�")
        })
    public final ResultBase deleteItem(
    		@ApiParam(name="retailstoreid", value="�����X�̃R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="itemid", value="���ڃR�[�h") @FormParam("itemid") final String itemId) {

        String functionName = "ItemResource.deleteItem";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("RetailStoreID", storeId)
                .println("ItemID", itemId);

        ResultBase resultBase = null;

        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            boolean  itemExists = itemDAO.isItemExists(storeId, itemId);

            if (!itemExists) {
                Item returnItem = new Item();
                returnItem.setAgeRestrictedFlag(0);
                resultBase = new ResultBase();
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
                return resultBase;
            }

            resultBase = itemDAO.deleteItem(storeId, itemId);

        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                        e.getMessage());
            } else {
                resultBase = new ResultBase(ResultBase.RES_ERROR_DAO,
                        e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    /**
     * The Web Method resource that gets the list of discount reason codes.
     *
     * @return The ReasonDataList object that contains the resultcode.
     */
    @Path("/getDiscountReason")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ReasonDataList getDiscountReason() {
        String functionName = "ItemResource.getDiscountReason";
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        ReasonDataList discountreason = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            discountreason = itemDAO.getDiscountReason();
        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                ReasonDataList reasondiscount = new ReasonDataList();
                reasondiscount
                        .setNCRWSSExtendedResultCode(ReasonDataList.RES_ERROR_DB);
                reasondiscount.setMessage(e.getMessage());
                discountreason = reasondiscount;
            } else {
                ReasonDataList reasondiscount = new ReasonDataList();
                reasondiscount
                        .setNCRWSSExtendedResultCode(ReasonDataList.RES_ERROR_DAO);
                reasondiscount.setMessage(e.getMessage());
                discountreason = reasondiscount;
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            ReasonDataList reasondiscount = new ReasonDataList();
            reasondiscount
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            reasondiscount.setMessage(e.getMessage());
            discountreason = reasondiscount;
        } finally {
            tp.methodExit(discountreason);
        }

        return discountreason;
    }

    /**
     * The Web Method resource that gets the list of discount buttons.
     *
     * @return The ReasonDataList object that contains the resultcode.
     */
    @Path("/getDiscountButton")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ReasonDataList getDiscountButton() {
        String functionName = "ItemResource.getDiscountButton";
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        ReasonDataList discountButtons = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            discountButtons = itemDAO.getDiscountButtons();
        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                ReasonDataList discountButtonData = new ReasonDataList();
                discountButtonData
                        .setNCRWSSExtendedResultCode(ReasonDataList.RES_ERROR_DB);
                discountButtonData.setMessage(e.getMessage());
                discountButtons = discountButtonData;
            } else {
                ReasonDataList discountButtonData = new ReasonDataList();
                discountButtonData
                        .setNCRWSSExtendedResultCode(ReasonDataList.RES_ERROR_DAO);
                discountButtonData.setMessage(e.getMessage());
                discountButtons = discountButtonData;
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            ReasonDataList reasondiscount = new ReasonDataList();
            reasondiscount
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            reasondiscount.setMessage(e.getMessage());
            discountButtons = reasondiscount;
        } finally {
            tp.methodExit(discountButtons);
        }

        return discountButtons;
    }

    /**
     * The Web Method resource for creating an item.
     *
     * @param storeId
     *            The Storeid intended for the item.
     * @param itemId
     *            The plu code of the item.
     * @param jsonitem
     *            The JSON representation of the item.
     * @return The ResultBase object that contains the resultcode.
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���i���X�g�쐬", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_ALREADY_EXIST, message="���̃v���W�F�N�g�̃X�g���[�W��������܂���ł���"),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ITEM_STORE_NOT_EXIST, message="���̃v���W�F�N�g�̃X�g���[�W��������"),
            @ApiResponse(code=ResultBase.RES_ITEM_DPT_NOT_EXIST, message="���Y�v���W�F�N�g�̕����T�����ĂĂ��Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ITEM_INVALIDPARAMETER, message="�艿�T�[�r�X���̖����p�����[�^�["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^")
        })
    public final ResultBase createItem(
    		@ApiParam(name="retailstoreid", value="�����X�R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="itemid", value="�v���W�F�N�g�R�[�h") @FormParam("itemid") final String itemId,
    		@ApiParam(name="item", value="�v���W�F�N�g") @FormParam("item") final String jsonitem) {

        String functionname = "ItemResource.createItem";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("retailstoreid", storeId).println("itemid", itemId)
                .println("item", jsonitem);

        ResultBase resultbase = new ResultBase();

        if (StringUtility.isNullOrEmpty(storeId, itemId, jsonitem)) {
            resultbase
                    .setNCRWSSResultCode(ResultBase.RES_ITEM_INVALIDPARAMETER);
            tp.println("Parameter[s] is null or empty.");
            tp.methodExit(resultbase);
            return resultbase;
        }

        if (!isEnterpriseStore(storeId)) {
            StoreResource storeRes = new StoreResource();
            ViewStore store = storeRes.viewStore(storeId);
            if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
                resultbase
                        .setNCRWSSResultCode(ResultBase.RES_ITEM_STORE_NOT_EXIST);
                tp.println("RetailStoreID did not exist.");
                tp.methodExit(resultbase);
                return resultbase;
            }
        }

        try {
            String appId = pathName.concat(".create");
            JsonMarshaller<Item> itemJsonMarshaller = new JsonMarshaller<Item>();
            Item item = itemJsonMarshaller.unMarshall(jsonitem, Item.class);
            item.setUpdAppId(appId);
            item.setUpdOpeCode(getOpeCode());

            // return if dpt is null or empty
            if (StringUtility.isNullOrEmpty(item.getDepartment())) {
                resultbase
                        .setNCRWSSResultCode(ResultBase.RES_ITEM_INVALIDPARAMETER);
                tp.println("Item's DepartmentID is required.");
                return resultbase;
            } else {
                DepartmentResource deptRes = new DepartmentResource();
                ViewDepartment viewDept = deptRes.selectDepartmentDetail(item.getCompanyId(),
                        storeId, item.getDepartment());
                if (viewDept.getNCRWSSResultCode() != ResultBase.RES_DPTMT_OK) {
                    resultbase
                            .setNCRWSSResultCode(ResultBase.RES_ITEM_DPT_NOT_EXIST);
                    tp.println("Item's DepartmentID did not exist.");
                    return resultbase;
                }
            }
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            String taxRate = GlobalConstant.getTaxRate();
            if (null == priceIncludeTax || priceIncludeTax.isEmpty()) {
                priceIncludeTax = String.valueOf(Item.ROUND_DOWN);
            }
            item.setTaxRate(Integer.valueOf(taxRate));
            item.setRegularSalesUnitPrice(ItemHelper.calculateSalesUnitPrice(
                    item, (long) item.getRegularSalesUnitPrice(),
                    Integer.valueOf(priceIncludeTax)));
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            resultbase = itemDAO.createItem(storeId, itemId, item);

        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                int errorcode = ((SQLException) e.getCause()).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_ITEM_ALREADY_EXIST;
                    tp.println("Item is duplicated.");
                } else {
                    errorcode = ResultBase.RES_ERROR_DB;
                }
                resultbase.setNCRWSSResultCode(errorcode);
                resultbase.setMessage(e.getMessage());
            } else {
                resultbase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultbase.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            resultbase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultbase.setMessage(e.getMessage());
        } finally {
            tp.methodExit(resultbase);
        }
        return resultbase;
    }

    /**
     * ���i�����p���i���擾.
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
     * @return Item
     * 			���i�����p���i���.
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    @Path("/getItemForSeachingItem")
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="���i�����p���i���擾", response=SearchedProducts.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o")
        })
    public final SearchedProducts getItemForSeachingItem(
    		@ApiParam(name="Plu", value="���i�R�[�h") @QueryParam("Plu") final String plu,
    		@ApiParam(name="MdName", value="���i��") @QueryParam("MdName") final String mdName,
    		@ApiParam(name="ProductNum", value="�i��") @QueryParam("ProductNum") final String productNum,
    		@ApiParam(name="Color", value="�J���[") @QueryParam("Color") final String color,
    		@ApiParam(name="ItemSize", value="�T�C�Y") @QueryParam("ItemSize") final String itemSize,
    		@ApiParam(name="Dpt", value="Div�R�[�h") @QueryParam("Dpt") final String dpt,
    		@ApiParam(name="VenderCode", value="�u�����h") @QueryParam("VenderCode") final String venderCode,
    		@ApiParam(name="Annual", value="�W�J�N�x") @QueryParam("Annual") final String annual) {

    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
				.println("Plu", plu)
				.println("MdName", mdName)
				.println("ProductNum", productNum)
				.println("Color", color)
				.println("ItemSize", itemSize)
				.println("Dpt", dpt)
				.println("VenderCode", venderCode)
				.println("Annual", annual);

		SearchedProducts items = new SearchedProducts();

		try{
			DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IItemDAO iItemDAO = daoFactory.getItemDAO();

			//�l�̌ܓ��̔��f�t���O�@�P�A�傫�Ȓl�iceil�j�@�Q�A�������l�ifloor�j �@�R�A�l�̌ܓ��iround�j
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();

			items = iItemDAO.getItemInfo(plu, mdName, productNum, color, itemSize,
					dpt, venderCode, annual, priceIncludeTax);

		} catch (DaoException daoEx) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO,
					functionName
							+ ": Failed to get Item info for SearchingItem.",
					daoEx);
			items.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			items.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			items.setMessage(daoEx.getMessage());
		} catch (Exception e) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
					functionName
							+ ": Failed to get Item info for SearchingItem.", e);
			items.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			items.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			items.setMessage(e.getMessage());
		}finally{
			tp.methodExit(items);
		}
		return items;
    }

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
	 * @return BrandProductInfo
	 * 			     �u�����h���i���
	 */
    @Path("/getBrandProductInfo")
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="�u�����h���i���擾", response=BrandProducts.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o")
        })
    public final BrandProducts getBrandProductInfo(
    		@ApiParam(name="Dpt", value="����") @QueryParam("Dpt") final String dpt,
    		@ApiParam(name="VenderCode", value="���[�J�[�R�[�h") @QueryParam("VenderCode") final String venderCode,
    		@ApiParam(name="Annual", value="�N�x") @QueryParam("Annual") final String annual,
    		@ApiParam(name="Reservation", value="�\��") @QueryParam("Reservation") final String reservation,
    		@ApiParam(name="GroupId", value="�O���[�v�R�[�h") @QueryParam("GroupId") final String groupId,
    		@ApiParam(name="Line", value="�t�@�C���s") @QueryParam("Line") final String line) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("Dpt", dpt)
				.println("VenderCode", venderCode).println("Annual", annual)
				.println("Reservation", reservation).println("GroupId", groupId)
				.println("Line", line);

		BrandProducts brandProducts = new BrandProducts();

		try {
			DAOFactory daoFactory = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IItemDAO iItemDAO = daoFactory.getItemDAO();

			// �l�̌ܓ��̔��f�t���O�@�P�A�傫�Ȓl�iceil�j�@�Q�A�������l�ifloor�j �@�R�A�l�̌ܓ��iround�j
			String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();

			brandProducts = iItemDAO.getBrandProductInfo(dpt, venderCode,
					annual, reservation, groupId, line, priceIncludeTax);

		} catch (DaoException daoEx) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get Brand�@Product�@Info.", daoEx);
			brandProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			brandProducts
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			brandProducts.setMessage(daoEx.getMessage());
		} catch (Exception e) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get Brand�@Product�@Info.", e);
			brandProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			brandProducts
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			brandProducts.setMessage(e.getMessage());
		} finally {
			tp.methodExit(brandProducts);
		}
		return brandProducts;
	}

    /**
     * Get The Item Price List.
     * @param transaction transaction
     * @param storeId  The ID of The Store
     * @param companyId The ID of The Company
     * @return itemList The List of The Item Price
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/getItemPrice")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="�v���W�F�N�g�̉��i�\���l����", response=Item.class)
    public final List<Item> getItemsPrice(
    		@ApiParam(name="transaction", value="�Ɩ�") @FormParam("transaction") String transaction,
    		@ApiParam(name="StoreId", value="�X�܃R�[�h") @FormParam("StoreId") String storeId,
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String companyId ,
    		@ApiParam(name="businessDate", value="�c�Ɠ�") @FormParam("businessDate") String businessDate) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("transaction", transaction).println("storeId", storeId)
                .println("companyId", companyId);

        List<Item> itemList = new ArrayList<Item>();
        if (StringUtility.isNullOrEmpty(transaction, companyId, storeId,businessDate)) {
            return itemList;
        }
        try {
            JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
            Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
            List<Sale> sales = transactionIn.getSales();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IItemDAO iItemDAO = daoFactory.getItemDAO();
            for (Sale saleIn : sales) {
                Item item = new Item();
                item = iItemDAO.getItemBypluCode(storeId, saleIn.getItemId(), companyId,businessDate);
                if (null != item) {
                    itemList.add(item);
                }
            }

        } catch (IOException e1) {
            LOGGER.logAlert(progname, Logger.RES_EXCEP_IO, functionName + ": Failed to get item price.", e1);
            return null;
        } catch (DaoException daoEx) {
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO, functionName + ": Failed to get Group Line�@Info.",
                    daoEx);
            return null;
        } catch (Exception e) {
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get Group Line�@Info.", e);
            return null;
        } finally {
            tp.methodExit(itemList);
        }
        return itemList;
    }

    /**
	 * �O���[�v�ƃ��C�����擾.
	 *
	 * @return GroupLines
	 * 			     �O���[�v�ƃ��C�����
	 */
    @Path("/getGroupLineInfo")
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="�O���[�v�ƃ��C�����擾", response=GroupLines.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o")
        })
    public final GroupLines getGroupLines(){
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);

		GroupLines groupLines = new GroupLines();
		try {
			DAOFactory daoFactory = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IItemDAO iItemDAO = daoFactory.getItemDAO();

			groupLines = iItemDAO.getGroupLines();

		} catch (DaoException daoEx) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get Group Line�@Info.", daoEx);
			groupLines.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			groupLines
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			groupLines.setMessage(daoEx.getMessage());
		} catch (Exception e) {
			LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get Group Line�@Info.", e);
			groupLines.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			groupLines
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			groupLines.setMessage(e.getMessage());
		} finally {
			tp.methodExit(groupLines);
		}
    	return groupLines;
    }
    /**
     *
     * @param companyId
     * @param storeId
     * @param itemType
     * @return
     */
    @Path("/getpicklistitems")
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="�I�����X�g", response=PickList.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final PickList getPickList(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="itemType", value="�A�C�e���^�C�v") @QueryParam("itemType") final String itemType) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("companyId", companyId)
          .println("storeId", storeId)
          .println("itemType", itemType);
        PickList pickList = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            pickList = new PickList();
            pickList.setItems(itemDAO.getPickListItems(companyId, storeId, itemType));
        } catch (Exception ex) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            pickList.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(pickList);
        }
        return pickList;
    }

    /**
     * Item entry for requesting item and promotion information.
     *
     * @param itemId The ItemId
     * @param companyId
     *            The companyId
     * @return {@link Transaction}
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/getitementry")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="�l���v���W�F�N�g�Ƒ��i���̓��͂����߂܂�", response=PromotionResponse.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="�v���W�F�N�g�͑��݂��Ȃ�")
        })
    public final PromotionResponse getitementry(
    		@ApiParam(name="itemId", value="�v���W�F�N�g�R�[�h") @FormParam("itemId") final String itemId,
    		@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("RetailStoreId", itemId).println("companyId", companyId);
        Transaction transactionOut = new Transaction();
        PromotionResponse response = new PromotionResponse();
        try {
            if (StringUtility.isNullOrEmpty(itemId, companyId)) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tp.println("Parameter[s] is empty or null.");
                return response;
            }

            IItemDAO itemDao = new SQLServerItemDAO();
            Item item = itemDao.getItemByApiData(itemId, companyId);

            if (item == null) {
                response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
                return response;
            }

            Sale sale = new Sale();
            Sale saleItem = SaleItemsHandler.createSale(item, sale);

            transactionOut.setSale(saleItem);
            response.setTransaction(transactionOut);
        } catch (Exception e) {
            LOGGER.logAlert("ItemResoure", Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send item entry.", e);
            response = new PromotionResponse(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(response);
        }
        return response;
    }
    /**
     * Checks if storeid is an enterprise store(0).
     *
     * @param storeID
     *            to check.
     * @return true if an enterprise storeid, false if not.
     */
    private boolean isEnterpriseStore(final String storeID) {
        return null != storeID && !storeID.isEmpty() &&
        		"0".equals(storeID.trim());
    }

    /** The constant for normal pricing. */
    private static final String NORMAL_PRICING = "0";

    /**
     * Checks if is normal pricing.
     *
     * @return true, if is normal pricing
     */
    private boolean isNormalPricing() {
        String pricingType = GlobalConstant.getPricingType();
        return pricingType == null || pricingType.equals(NORMAL_PRICING);
    }

    private String getOpeCode() {
        return ((securityContext != null) && (securityContext
                .getUserPrincipal()) != null) ? securityContext
                .getUserPrincipal().getName() : null;
    }

}
