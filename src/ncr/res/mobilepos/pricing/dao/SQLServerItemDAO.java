/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerItemDAO
 *
 * DAO which handles the operations in the table specific for Items
 *
 * Meneses, Chris Niven
 * del Rio, Rica Marie
 */
package ncr.res.mobilepos.pricing.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.dao.MstBrandInfoDAO;
import ncr.res.mobilepos.dao.MstClassInfoDAO;
import ncr.res.mobilepos.dao.MstColorInfoDAO;
import ncr.res.mobilepos.dao.MstDptInfoDAO;
import ncr.res.mobilepos.dao.MstGroupInfoDAO;
import ncr.res.mobilepos.dao.MstNameSystemDAO;
import ncr.res.mobilepos.dao.MstPluDAO;
import ncr.res.mobilepos.dao.MstPluStoreDAO;
import ncr.res.mobilepos.dao.MstPriceUrgentForStoreDAO;
import ncr.res.mobilepos.dao.MstSizeInfoDAO;
import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.dao.model.MstClassInfo;
import ncr.res.mobilepos.dao.model.MstColorInfo;
import ncr.res.mobilepos.dao.model.MstDptInfo;
import ncr.res.mobilepos.dao.model.MstGroupInfo;
import ncr.res.mobilepos.dao.model.MstNameSystem;
import ncr.res.mobilepos.dao.model.MstPlu;
import ncr.res.mobilepos.dao.model.MstPluStore;
import ncr.res.mobilepos.dao.model.MstPriceUrgentForStore;
import ncr.res.mobilepos.dao.model.MstSizeInfo;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.helper.ItemHelper;
import ncr.res.mobilepos.pricing.model.BrandProductInfo;
import ncr.res.mobilepos.pricing.model.BrandProducts;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.pricing.model.GroupLineInfo;
import ncr.res.mobilepos.pricing.model.GroupLines;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.ItemReasonDataList;
import ncr.res.mobilepos.pricing.model.OverrideReasonDataList;
import ncr.res.mobilepos.pricing.model.PickListItem;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.model.PremiumInfo;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.pricing.model.ReasonData;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.pricing.model.TransactionReasonDataList;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Item Pricing.
 *
 * @see IItemDAO
 */

public class SQLServerItemDAO extends AbstractDao implements IItemDAO {
    /**
     * The Program name.
     */
    private static final String CLASS_NAME = SQLServerItemDAO.class.getSimpleName();

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerItemDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerItemDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     *
     * @param storeId
     *            The Store ID which the item is located
     * @param pluCode
     *            The Item's Price Look Up Code
     * @return
     * @throws DaoException
     */
    public boolean isItemExists(final String storeId, final String pluCode) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("StoreId", storeId).println("PLU", pluCode);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        boolean exists = false;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-item-without-enterprise"));
            select.setString(SQLStatement.PARAM1, pluCode);
            select.setString(SQLStatement.PARAM2, storeId);
            result = select.executeQuery();

            if (result.next()) {
                exists = true;
            } else {
                tp.println("Item not found.");
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.isItemExists()", Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the item information.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @isItemExists ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.isItemExists()", Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @isItemExists ", sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(exists);
        }

        return exists;
    }

    @Override
    public final Item setItemUnitPrice(final Item item) throws DaoException {

        String functionName = "SQLServerItemDAO.setItemUnitPrice";
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("item", item);

        Connection connection = null;
        PreparedStatement selectUpdateStmt = null;
        ResultSet result = null;
        Item updatedItem = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectUpdateStmt = connection.prepareStatement(sqlStatement.getProperty("set-itemPrice"));

            selectUpdateStmt.setDouble(SQLStatement.PARAM1, item.getRegularSalesUnitPrice());
            selectUpdateStmt.setString(SQLStatement.PARAM2, item.getItemId());
            selectUpdateStmt.setString(SQLStatement.PARAM3, item.getRetailStoreId());
            selectUpdateStmt.setString(SQLStatement.PARAM4, item.getUpdAppId());
            selectUpdateStmt.setString(SQLStatement.PARAM5, item.getUpdOpeCode());
            result = selectUpdateStmt.executeQuery();

            if (result.next()) {
                updatedItem = new Item();
                updatedItem.setItemId(result.getString("MdInternal")); // PK

                // item description
                Description description = new Description();
                description.setEn(result.getString("MdName"));
                description.setJa(result.getString("MdNameLocal"));

                updatedItem.setDescription(description);

                updatedItem.setRegularSalesUnitPrice(Double.parseDouble(result.getString("SalesPrice1")));
                updatedItem.setDepartment(result.getString("Dpt"));
                updatedItem.setLine(result.getString("Line"));
                updatedItem.setItemClass(result.getString("Class"));
                updatedItem.setDiscountType(result.getInt("DiscountType"));
                int ageRestrictedFlag = result.getInt(result.findColumn("AgeRestrictedFlag"));

                if (result.getObject("AgeRestrictedFlag") == null || ageRestrictedFlag < 0) {
                    ageRestrictedFlag = 0;
                }
                updatedItem.setAgeRestrictedFlag(ageRestrictedFlag);

            } else {
                tp.println("Item not found.");
            }
            connection.commit();
        } catch (NumberFormatException nfe) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to parse new price. \n" + nfe.getMessage());
            throw new DaoException("NumberFormatException: @setItemUnitPrice ", nfe);
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to change item price.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @setItemUnitPrice ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to change item price.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @setItemUnitPrice ", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to change item price.\n" + ex.getMessage());
            throw new DaoException("Exception: @setItemUnitPrice ", ex);
        } finally {
            closeConnectionObjects(connection, selectUpdateStmt, result);

            tp.methodExit(updatedItem);
        }

        return updatedItem;
    }

    /**
     * Gets the list of items in a particular Store having Price Look Up(PLU)
     * contains a given key.
     *
     * @param storeId
     *            The ID of the Store where the items are located
     * @param key
     *            The given key used to search the item(s)
     * @param limit
     *            The limit of item(s) to be search.
     * @param priceIncludeTax
     *            The Price Include Tax.
     * @throws DaoException
     *             The Exception thrown when getting the List of Items failed.
     * @return Array of Items
     */
    public final List<Item> listItems(final String storeId, final String key, final String name,
            final int priceIncludeTax, final int limit) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("RetailStoreID", storeId).println("Key", key)
                .println("SearchLimit", limit);

        List<Item> searchList = new ArrayList<Item>();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-item-list"));
            select.setInt(SQLStatement.PARAM1, limit);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, StringUtility.isNullOrEmpty(key) ? null : key + '%');
            select.setString(SQLStatement.PARAM4, StringUtility.isNullOrEmpty(name) ? null : '%' + name + '%');
            result = select.executeQuery();

            while (result.next()) {
                Item searchedItem = new Item();
                String salesPrice = result.getString(result.findColumn("SalesPrice1"));
                if ("NULL".equalsIgnoreCase(salesPrice) || salesPrice == null) {
                    salesPrice = "0";
                }

                int ageRestrictedFlag = result.getInt(result.findColumn("AgeRestrictedFlag"));
                if (result.getObject("AgeRestrictedFlag") == null || ageRestrictedFlag < 0) {
                    ageRestrictedFlag = 0;
                }

                searchedItem.setAgeRestrictedFlag(ageRestrictedFlag);
                searchedItem.setItemId(result.getString(result.findColumn("MdInternal")));

                Description description = new Description();
                description.setEn(result.getString(result.findColumn("MdName")));
                description.setJa(result.getString(result.findColumn("MdNameLocal")));

                searchedItem.setDescription(description);
                searchedItem.setDepartment(result.getString(result.findColumn("Dpt")));
                searchedItem.setLine(result.getString(result.findColumn("Line")));
                searchedItem.setItemClass(result.getString(result.findColumn("Class")));
                searchedItem.setMixMatchCode(result.getString(result.findColumn("MMCode")));
                searchedItem.setDiscountType(result.getInt(result.findColumn("DiscountType")));
                searchedItem.setTaxType(result.getInt(result.findColumn("TaxType")));
                searchedItem.setTaxRate(result.getInt(result.findColumn("TaxRate")));
                searchedItem.setSubInt10(result.getInt(result.findColumn("SubInt10")));
                searchedItem.setRegularSalesUnitPrice(ItemHelper.calculateRegularSalesUnitPrice(searchedItem,
                        Integer.valueOf(salesPrice), priceIncludeTax));

                String discountFlag = result.getString(result.findColumn("DiscountFlag"));
                searchedItem.setDiscountFlag((discountFlag == null) ? -1 : Integer.parseInt(discountFlag));

                double dDiscountRate = result.getDouble(result.findColumn("DiscountRate"));
                double dDiscountAmount = result.getDouble(result.findColumn("DiscountAmt"));
                if (dDiscountAmount > searchedItem.getRegularSalesUnitPrice()) {
                    dDiscountAmount = searchedItem.getRegularSalesUnitPrice();
                }
                searchedItem.setDiscountAmount(dDiscountAmount);
                searchedItem.setDiscount(dDiscountRate);
                searchedItem.setActualSalesUnitPrice(Double.parseDouble(salesPrice));
                searchedItem.setCouponFlag(result.getString(result.findColumn("Md32")));
                searchedItem.setInheritFlag(result.getString("InheritFlag"));
                searchedItem.setNonSales(result.getInt("PosMdType"));
                searchedItem.setRetailStoreId(result.getString(result.findColumn("StoreId")));
                searchList.add(searchedItem);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.listItems()", Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the items.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @listItems ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.listItems()", Logger.RES_EXCEP_SQL,
                    "Failed to get the items.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @listItems ", sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit("Items count: " + searchList.size());
        }

        return searchList;
    }

    @Override
    public final List<Item> searchItems(final String storeId, final String key, final String name, final int limit,
            final int priceIncludeTax) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("RetailStoreID", storeId).println("Key", key)
                .println("Name", name).println("SearchLimit", limit).println("PriceIncludeTax", priceIncludeTax);

        List<Item> searchList = new ArrayList<Item>();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-items-by-key-name"));
            select.setInt(SQLStatement.PARAM1, limit);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, key);
            select.setString(SQLStatement.PARAM4, name);
            result = select.executeQuery();

            while (result.next()) {
                Item searchedItem = new Item();
                searchedItem.setItemId(result.getString("MdInternal"));
                searchedItem.setRetailStoreId(result.getString("StoreId"));

                Description description = new Description();
                description.setEn(result.getString("MdName"));
                description.setJa(result.getString("MdNameLocal"));
                searchedItem.setDescription(description);

                searchedItem.setDepartment(result.getString("Dpt"));
                searchedItem.setLine(result.getString("Line"));
                searchedItem.setItemClass(result.getString("Class"));
                searchedItem.setTaxType(result.getInt("TaxType"));
                searchedItem.setTaxRate(result.getInt("TaxRate"));
                searchedItem.setRegularSalesUnitPrice(ItemHelper.calculateRegularSalesUnitPrice(searchedItem,
                        new BigDecimal(result.getString("SalesPrice1")).longValue(), priceIncludeTax));
                searchedItem.setDiscountType(result.getInt("DiscountType"));
                searchedItem.setDiscount(result.getDouble("DiscountRate"));

                String discountFlag = result.getString("DiscountFlag");
                int iDiscountFlag = -1;
                if (discountFlag != null) {
                    iDiscountFlag = Integer.parseInt(discountFlag);
                }
                searchedItem.setDiscountFlag(iDiscountFlag);

                BigDecimal discountAmount = result.getBigDecimal("DiscountAmt");
                if (discountAmount != null) {
                    if (discountAmount.doubleValue() > searchedItem.getRegularSalesUnitPrice()) {
                        searchedItem.setDiscountAmount(searchedItem.getRegularSalesUnitPrice());
                    } else {
                        searchedItem.setDiscountAmount(discountAmount.doubleValue());
                    }
                }
                searchedItem.setActualSalesUnitPrice(ItemHelper.calculateActualSalesPrice(searchedItem));
                searchedItem.setMixMatchCode(result.getString("MMCode"));
                searchedItem.setSubInt10(result.getInt("SubInt10"));

                int ageRestrictedFlag = result.getInt("AgeRestrictedFlag");
                if ((result.getObject("AgeRestrictedFlag") == null) || (ageRestrictedFlag <= 0)) {
                    ageRestrictedFlag = 0;
                }
                searchedItem.setAgeRestrictedFlag(ageRestrictedFlag);
                searchedItem.setCouponFlag(result.getString("Md32"));
                searchedItem.setInheritFlag(result.getString("InheritFlag"));
                searchedItem.setNonSales(result.getInt("PosMdType"));
                searchList.add(searchedItem);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.searcItems()", Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the items.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @searcItems ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.searcItems()", Logger.RES_EXCEP_SQL,
                    "Failed to get the items.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @searcItems ", sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit("Items count: " + searchList.size());
        }

        return searchList;
    }

    /**
     * Retrieves Department Details.
     *
     * @param retailStoreID
     *            Store number
     * @throws DaoException
     *             Exception when error occurs
     * @return Department object
     */

    @Override
    public final ResultBase deleteItem(final String retailStoreID, final String itemID) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("RetailStoreID", retailStoreID).println("ItemID",
                itemID);

        Connection connection = null;
        PreparedStatement deleteStmt = null;
        int result = 0;
        ResultBase resultBase = new ResultBase();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteStmt = connection.prepareStatement(sqlStatement.getProperty("delete-item"));
            setValues(deleteStmt, retailStoreID, itemID);

            result = deleteStmt.executeUpdate();

            if (result == 0) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
                tp.println("No item was deleted.");
            }

            connection.commit();

        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@SQLServerItemDAO.deleteItem", e);
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.deleteItem", Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to delete item\n " + e.getMessage());
            throw new DaoException("SQLServerItemDAO: @deleteItem ", e);
        } catch (SQLException e) {
            rollBack(connection, "SQLException:@SQLServerItemDAO.deleteItem", e);
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.deleteItem", Logger.RES_EXCEP_SQL,
                    "Failed to delete item\n " + e.getMessage());
            throw new DaoException("SQLException: @SQLServerItemDAO.deleteItem ", e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@SQLServerItemDAO.deleteItem", e);
            LOGGER.logAlert(CLASS_NAME, "SQLServerItemDAO.deleteItem", Logger.RES_EXCEP_GENERAL,
                    "Failed to delete item\n " + e.getMessage());
            throw new DaoException("SQLException: @SQLServerItemDAO.deleteItem ", e);
        } finally {
            closeConnectionObjects(connection, deleteStmt);

            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    /**
     * {@inheritDoc} .
     */
    @Override
    public final ResultBase createItem(final String storeId, final String itemId, final Item item) throws DaoException {

        String functionName = "SQLServerItemDAO.createItem";
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("RetailStoreID", storeId).println("ItemID", itemId)
                .println("Item", item);

        ResultBase resultBase = new ResultBase();
        Connection connection = null;
        PreparedStatement insertStmt = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertStmt = connection.prepareStatement(sqlStatement.getProperty("insert-item"));

            insertStmt.setString(SQLStatement.PARAM1, storeId);
            insertStmt.setString(SQLStatement.PARAM2, itemId);
            insertStmt.setString(SQLStatement.PARAM3, itemId);
            insertStmt.setString(SQLStatement.PARAM4, item.getDepartment());
            insertStmt.setString(SQLStatement.PARAM5, "" + item.getDiscount());
            insertStmt.setDouble(SQLStatement.PARAM6, item.getActualSalesUnitPrice());
            insertStmt.setString(SQLStatement.PARAM7, itemId);

            // item description
            String en = "";
            String ja = "";
            if (item.getDescription() != null) {
                en = item.getDescription().getEn() != null ? item.getDescription().getEn() : "";
                ja = item.getDescription().getJa() != null ? item.getDescription().getJa() : "";
            }

            insertStmt.setString(SQLStatement.PARAM8, en);
            insertStmt.setString(SQLStatement.PARAM9, ja);
            insertStmt.setString(SQLStatement.PARAM10, item.getItemClass()); // CLASS

            int discountType = item.getDiscountType();
            if (discountType < 0) {
                discountType = 0;
            }
            insertStmt.setInt(SQLStatement.PARAM11, discountType);
            insertStmt.setString(SQLStatement.PARAM12, item.getLine()); // LINE
            String discountFlag = null;
            if (item.getDiscountFlag() != -1) {
                discountFlag = "" + item.getDiscountFlag();
            }
            insertStmt.setString(SQLStatement.PARAM13, discountFlag);
            insertStmt.setInt(SQLStatement.PARAM14, item.getTaxRate());
            insertStmt.setInt(SQLStatement.PARAM15, item.getTaxType());

            String ageRestricted = "" + item.getAgeRestrictedFlag();

            if (item.getAgeRestrictedFlag() < 0) {
                insertStmt.setString(SQLStatement.PARAM16, "" + 0);
            } else {
                insertStmt.setString(SQLStatement.PARAM16, ageRestricted);
            }

            insertStmt.setString(SQLStatement.PARAM17, item.getUpdAppId());
            insertStmt.setString(SQLStatement.PARAM18, item.getUpdOpeCode());

            String couponFlag = getCouponFlag(item.getCouponFlag());
            insertStmt.setString(SQLStatement.PARAM19, couponFlag);
            insertStmt.setDouble(SQLStatement.PARAM20, item.getDiscountAmount());
            insertStmt.setInt(SQLStatement.PARAM21, item.getNonSales());
            insertStmt.setString(SQLStatement.PARAM22, item.getInheritFlag());
            insertStmt.setInt(SQLStatement.PARAM23, item.getSubInt10());

            insertStmt.executeUpdate();
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to create item\n " + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL, "Failed to create item\n " + e.getMessage());
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) != e.getErrorCode()) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to create item\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, insertStmt);

            tp.methodExit(resultBase);
        }
        return resultBase;
    }

    private String getCouponFlag(final String itemCouponFlag) {
        String couponFlag;
        if (itemCouponFlag == null) {
            couponFlag = null;
        } else if (Integer.valueOf(itemCouponFlag) <= 0) {
            couponFlag = "0";
        } else {
            couponFlag = "1";
        }
        return couponFlag;
    }

    /**
     * The method called to update Item's Sales Price.
     *
     * @param storeId
     *            The Retail Store ID.
     * @param itemId
     *            The Item ID.
     * @param item
     *            The Item.
     * @return The Updated item.
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    public final Item updateItem(final String storeId, final String itemId, final Item item) throws DaoException {

        String functionName = "SQLServerItemDAO.updateItem";
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("RetailStoreID", storeId).println("ItemID", itemId)
                .println("Item", item);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        ResultSet result = null;
        Item updatedItem = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();

            updateStmt = connection.prepareStatement(sqlStatement.getProperty("update-item"));
            updateStmt.setString(SQLStatement.PARAM1, item.getDepartment());
            updateStmt.setString(SQLStatement.PARAM2, "" + item.getDiscount());
            updateStmt.setDouble(SQLStatement.PARAM3, item.getActualSalesUnitPrice());

            // item description
            String en = null;
            String ja = null;
            if (item.getDescription() != null) {
                en = item.getDescription().getEn() != null ? item.getDescription().getEn() : null;
                ja = item.getDescription().getJa() != null ? item.getDescription().getJa() : null;
            }
            updateStmt.setString(SQLStatement.PARAM4, en);
            updateStmt.setString(SQLStatement.PARAM5, ja);

            updateStmt.setString(SQLStatement.PARAM6, item.getItemId());
            updateStmt.setInt(SQLStatement.PARAM7, item.getDiscountType());
            updateStmt.setString(SQLStatement.PARAM8,
                    StringUtility.isNullOrEmpty(item.getRetailStoreId()) ? storeId : item.getRetailStoreId());
            updateStmt.setString(SQLStatement.PARAM9, itemId);
            updateStmt.setInt(SQLStatement.PARAM10, item.getAgeRestrictedFlag());
            updateStmt.setString(SQLStatement.PARAM11, item.getUpdAppId());
            updateStmt.setString(SQLStatement.PARAM12, item.getUpdOpeCode());
            String couponFlag = getCouponFlag(item.getCouponFlag());
            updateStmt.setString(SQLStatement.PARAM13, couponFlag);
            updateStmt.setString(SQLStatement.PARAM14, "" + item.getDiscountAmount());
            updateStmt.setString(SQLStatement.PARAM15,
                    (item.getDiscountFlag() == -1) ? null : "" + item.getDiscountFlag());
            updateStmt.setInt(SQLStatement.PARAM16, item.getTaxType());
            updateStmt.setString(SQLStatement.PARAM17, storeId);
            updateStmt.setString(SQLStatement.PARAM18, item.getLine());
            updateStmt.setString(SQLStatement.PARAM19, item.getItemClass());
            updateStmt.setInt(SQLStatement.PARAM20, item.getTaxRate());
            updateStmt.setInt(SQLStatement.PARAM21, item.getNonSales());
            updateStmt.setString(SQLStatement.PARAM22, item.getInheritFlag());
            updateStmt.setInt(SQLStatement.PARAM23, item.getSubInt10());
            result = updateStmt.executeQuery();

            if (result.next()) {
                updatedItem = new Item();

                // item description
                Description description = new Description();
                description.setEn(result.getString(result.findColumn("MdName")));
                description.setJa(result.getString(result.findColumn("MdNameLocal")));

                updatedItem.setDescription(description);

                updatedItem.setDepartment(result.getString(result.findColumn("Dpt")));
                updatedItem.setDiscount(result.getDouble(result.findColumn("DiscountRate")));
                updatedItem.setRegularSalesUnitPrice(
                        Double.parseDouble(result.getString(result.findColumn("SalesPrice1"))));
                updatedItem.setActualSalesUnitPrice(
                        Double.parseDouble(result.getString(result.findColumn("SalesPrice1"))));
                updatedItem.setItemId(result.getString(result.findColumn("MdInternal")));
                int ageRestrictedFlag = result.getInt(result.findColumn("AgeRestrictedFlag"));
                updatedItem.setAgeRestrictedFlag(ageRestrictedFlag < 0 ? 0 : ageRestrictedFlag);
                updatedItem.setDiscountType(result.getInt(result.findColumn("DiscountType")));
                updatedItem.setCouponFlag(result.getString(result.findColumn("Md32")));
                updatedItem.setDiscountAmount(result.getDouble(result.findColumn("DiscountAmt")));
                String strDiscountFlag = result.getString(result.findColumn("DiscountFlag"));
                if (strDiscountFlag != null && Integer.parseInt(strDiscountFlag) >= 0) {
                    updatedItem.setDiscountFlag(Integer.parseInt(strDiscountFlag));
                }
                updatedItem.setTaxType(result.getInt(result.findColumn("TaxType")));
            } else {
                tp.println("Item not found.");
            }
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update item\n " + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {
            // Dont call rollback when Row Duplicate occur.
            // It is not necessary.And also, Rollback will throw exception
            // causing false error report to the caller method.
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL, "Failed to update item\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update item\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, updateStmt, result);

            tp.methodExit(updatedItem);
        }
        return updatedItem;
    }

    /**
     * 商品情報取得.
     * 
     * @param plu
     *            商品コード.
     * @param mdName
     *            商品名.
     * @param productNum
     *            品番.
     * @param color
     *            カラー.
     * @param itemSize
     *            サイズ
     * @param dpt
     *            Divコード
     * @param venderCode
     *            ブランド
     * @param annual
     *            展開年度
     * @param priceIncludeTax
     *            四捨五入のオプション
     * @return Items 商品検索用商品情報
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    @Override
    public final SearchedProducts getItemInfo(String plu, String mdName, String productNum, String color,
            String itemSize, String dpt, String venderCode, String annual, String priceIncludeTax) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();

        tp.methodEnter(functionName).println("Plu", plu).println("MdName", mdName).println("Md01", productNum)
                .println("Md03", color).println("ItemSize", itemSize).println("Dpt", dpt)
                .println("VenderCode", venderCode).println("Md05", annual).println("PriceIncludeTax", priceIncludeTax);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet result = null;
        SearchedProducts searchedItems = null;
        Item itemInfo = null;
        List<Item> itemList = null;

        try {
            searchedItems = new SearchedProducts();

            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();

            selectStmt = connection.prepareStatement(sqlStatement.getProperty("get-item-for-searchproduct"));

            selectStmt.setString(SQLStatement.PARAM1, plu);
            selectStmt.setString(SQLStatement.PARAM2, mdName);
            selectStmt.setString(SQLStatement.PARAM3, productNum);
            selectStmt.setString(SQLStatement.PARAM4, color);
            selectStmt.setString(SQLStatement.PARAM5, itemSize);
            selectStmt.setString(SQLStatement.PARAM6, dpt);
            selectStmt.setString(SQLStatement.PARAM7, venderCode);
            selectStmt.setString(SQLStatement.PARAM8, annual);

            result = selectStmt.executeQuery();

            while (result.next()) {
                if (itemList == null) {
                    itemList = new ArrayList<Item>();
                }
                itemInfo = new Item();
                // 商品コード
                itemInfo.setItemId(result.getString("MdInternal"));
                // 商品名. en : 英語商品名, ja:日本語商品名
                Description description = new Description();
                description.setEn(result.getString("MdName"));
                description.setJa(result.getString("MdNameLocal"));
                itemInfo.setDescription(description);
                // 課税区分（0:外税、1:内税、2:非課税）
                itemInfo.setTaxType(result.getInt("TaxType"));
                // 消費税率 （例）5% → 5.000
                itemInfo.setTaxRate(result.getInt("TaxRate"));
                // 値引・割引区分（0:対象、1:対象外）
                itemInfo.setDiscountType(result.getInt("DiscountType"));
                // 通常価格
                if ("".equals(result.getString("SalesPrice"))) {
                    itemInfo.setRegularSalesUnitPrice(0);
                } else {
                    itemInfo.setRegularSalesUnitPrice(ItemHelper.calculateRegularSalesUnitPrice(itemInfo,
                            Long.valueOf(result.getString("SalesPrice")), Integer.valueOf(priceIncludeTax)));
                }

                // no pre-processing. set whatever is received
                String discountFlag = result.getString("DiscountFlag");
                int iDiscountFlag = -1;
                if ((discountFlag != null) && (Integer.parseInt(discountFlag) >= 0)) {
                    iDiscountFlag = Integer.parseInt(discountFlag);
                }
                itemInfo.setDiscountFlag(iDiscountFlag);

                BigDecimal discountAmount = result.getBigDecimal("DiscountAmt");
                if (discountAmount != null) {
                    itemInfo.setDiscountAmount(discountAmount.doubleValue());
                    if ((iDiscountFlag == 0) && (discountAmount.doubleValue() > itemInfo.getRegularSalesUnitPrice())) {
                        itemInfo.setDiscountAmount(itemInfo.getRegularSalesUnitPrice());
                    }
                }

                if (itemInfo.getDiscountType() == 0) {
                    itemInfo.setDiscount(result.getDouble("DiscountRate"));
                } else {
                    itemInfo.setDiscount(0);
                }

                double discountAmt = 0;
                if (itemInfo.getDiscountType() == 0 && itemInfo.getDiscountFlag() == 0) {
                    discountAmt = itemInfo.getDiscountAmount();

                    if (discountAmt > itemInfo.getRegularSalesUnitPrice() || discountAmt < 0) {
                        discountAmt = 0;
                    }
                } else if (itemInfo.getDiscountType() == 0 && itemInfo.getDiscountFlag() == 1) {
                    discountAmt = Math.floor(itemInfo.getDiscount() / 100 * itemInfo.getRegularSalesUnitPrice());
                }
                itemInfo.setDiscountAmount(discountAmt);

                // 現在価格
                itemInfo.setActualSalesUnitPrice(itemInfo.getRegularSalesUnitPrice());

                if (itemInfo.getDiscountAmount() <= itemInfo.getRegularSalesUnitPrice()
                        && itemInfo.getDiscountAmount() > 0) {
                    itemInfo.setActualSalesUnitPrice(
                            itemInfo.getRegularSalesUnitPrice() - itemInfo.getDiscountAmount());
                }
                itemList.add(itemInfo);
            }
            if (itemList == null) {
                searchedItems.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchedItems.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchedItems.setMessage(ResultBase.RES_NODATAFOUND_MSG);
            } else {
                searchedItems.setItems(itemList);
                searchedItems.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                searchedItems.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                searchedItems.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName + ": Failed to get ItemInfo.",
                    sqlStmtEx);
            throw new DaoException("SQLStatementException:@" + functionName, sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get ItemInfo.", sqlEx);
            throw new DaoException("SQLException:@" + functionName, sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to get ItemInfo.", nuEx);
            throw new DaoException("NumberFormatException:@" + functionName, nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get ItemInfo.", e);
            throw new DaoException("Exception:@" + functionName, e);
        } finally {
            closeConnectionObjects(connection, selectStmt, result);
            tp.methodExit(itemInfo);
        }
        return searchedItems;
    }

    /**
     * ブランド商品情報取得.
     * 
     * @param dpt
     *            Divコード
     * @param venderCode
     *            ブランド
     * @param annual
     *            展開年度
     * @param reservation
     *            予約可能
     * @param groupId
     *            グループId
     * @param line
     *            アイテムId
     * @param priceIncludeTax
     *            四捨五入のオプション
     * @return BrandProducts ブランド商品情報
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    @Override
    public final BrandProducts getBrandProductInfo(String dpt, String venderCode, String annual, String reservation,
            String groupId, String line, String priceIncludeTax) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();

        tp.methodEnter(functionName).println("Dpt", dpt).println("VenderCode", venderCode).println("Annual", annual)
                .println("Reservation", reservation).println("GroupId", groupId).println("Line", line)
                .println("PriceIncludeTax", priceIncludeTax);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet result = null;
        BrandProductInfo brandProduct = null;
        List<BrandProductInfo> brandProductList = null;
        BrandProducts brandProducts = new BrandProducts();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();

            selectStmt = connection.prepareStatement(sqlStatement.getProperty("get-brand-product-info"));

            selectStmt.setString(SQLStatement.PARAM1, dpt);
            selectStmt.setString(SQLStatement.PARAM2, venderCode);
            selectStmt.setString(SQLStatement.PARAM3, annual);
            selectStmt.setString(SQLStatement.PARAM4, reservation);
            selectStmt.setString(SQLStatement.PARAM5, groupId);
            selectStmt.setString(SQLStatement.PARAM6, line);

            result = selectStmt.executeQuery();

            while (result.next()) {
                if (brandProductList == null) {
                    brandProductList = new ArrayList<BrandProductInfo>();
                }
                brandProduct = new BrandProductInfo();
                // line
                brandProduct.setLine(result.getString("Line"));
                // ブランド名
                brandProduct.setBrandName(result.getString("BrandName"));
                // 品番
                brandProduct.setProductNum(result.getString("Md01"));
                // 商品名
                brandProduct.setMdShortName(result.getString("MdShortName"));
                // 課税区分（0:外税、1:内税、2:非課税）
                brandProduct.setTaxType(result.getInt("TaxType"));
                // 消費税率 （例）5% → 5.000
                brandProduct.setTaxRate(result.getInt("TaxRate"));
                // 値引・割引区分（0:対象、1:対象外）
                brandProduct.setDiscountType(result.getInt("DiscountType"));
                // 件数
                brandProduct.setRowCount(result.getInt("Cnt"));

                long salesPrice = 0;
                if ("".equals(result.getString("SalesPrice"))) {
                    brandProduct.setSalesPrice(salesPrice);
                } else {
                    salesPrice = Long.valueOf(result.getString("SalesPrice"));

                    switch (brandProduct.getTaxType()) {
                    // 0:外税
                    case Item.TAX_EXCLUDED:
                        double salespricewithtaxAmount = salesPrice
                                + salesPrice * (Double.valueOf(brandProduct.getTaxRate()) / GlobalConstant.PERCENT);
                        // 四捨五入の判断フラグ １、大きな値（ceil） ２、小さい値（floor） ３、四捨五入（round）
                        switch (Integer.valueOf(priceIncludeTax)) {
                        case Item.ROUND_OFF:
                            salesPrice = (long) Math.round(salespricewithtaxAmount);
                            break;
                        case Item.ROUND_UP:
                            salesPrice = (long) Math.ceil(salespricewithtaxAmount);
                            break;
                        case Item.ROUND_DOWN:
                        default:
                            salesPrice = (long) Math.floor(salespricewithtaxAmount);
                        }
                        break;
                    // 1:内税、2:非課税
                    case Item.TAX_INCLUDED:
                    case Item.TAX_FREE:
                    default:
                    }
                    // 通常価格
                    brandProduct.setSalesPrice(salesPrice);
                }

                String discountFlag = result.getString("DiscountFlag");
                int iDiscountFlag = -1;
                if ((discountFlag != null) && (Integer.parseInt(discountFlag) >= 0)) {
                    iDiscountFlag = Integer.parseInt(discountFlag);
                }
                brandProduct.setDiscountFlag(iDiscountFlag);

                BigDecimal discountAmount = result.getBigDecimal("DiscountAmt");
                if (discountAmount != null) {
                    brandProduct.setDiscountAmt(discountAmount.doubleValue());
                    if ((iDiscountFlag == 0) && (discountAmount.doubleValue() > brandProduct.getSalesPrice())) {
                        brandProduct.setDiscountAmt(brandProduct.getSalesPrice());
                    }
                }

                if (brandProduct.getDiscountType() == 0) {
                    brandProduct.setDiscountRate(result.getDouble("DiscountRate"));
                } else {
                    brandProduct.setDiscountRate(0);
                }

                double discountAmt = 0;
                if (brandProduct.getDiscountType() == 0 && brandProduct.getDiscountFlag() == 0) {
                    discountAmt = brandProduct.getDiscountAmt();

                    if (discountAmt > brandProduct.getSalesPrice() || discountAmt < 0) {
                        discountAmt = 0;
                    }
                } else if (brandProduct.getDiscountType() == 0 && brandProduct.getDiscountFlag() == 1) {
                    discountAmt = Math.floor(brandProduct.getDiscountRate() / 100 * brandProduct.getSalesPrice());
                }
                brandProduct.setDiscountAmt(discountAmt);

                // 現在価格
                brandProduct.setActualSalesPrice(brandProduct.getSalesPrice());

                if (brandProduct.getDiscountAmt() <= brandProduct.getSalesPrice()
                        && brandProduct.getDiscountAmt() > 0) {
                    brandProduct.setActualSalesPrice(brandProduct.getSalesPrice() - brandProduct.getDiscountAmt());
                }
                brandProductList.add(brandProduct);
            }

            if (brandProductList == null) {
                brandProducts.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                brandProducts.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                brandProducts.setMessage(ResultBase.RES_NODATAFOUND_MSG);
            } else {
                brandProducts.setBrandProductList(brandProductList);
                brandProducts.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                brandProducts.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                brandProducts.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get Brand　Product　Info.", sqlStmtEx);
            throw new DaoException("SQLStatementException:@" + functionName, sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Brand　Product　Info.",
                    sqlEx);
            throw new DaoException("SQLException:@" + functionName, sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to get Brand　Product　Info.",
                    nuEx);
            throw new DaoException("NumberFormatException:@" + functionName, nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Brand　Product　Info.",
                    e);
            throw new DaoException("Exception:@" + functionName, e);
        } finally {
            closeConnectionObjects(connection, selectStmt, result);
            tp.methodExit(brandProducts);
        }
        return brandProducts;
    }

    /**
     * グループとライン情報取得.
     * 
     * @return GroupLines グループとライン情報
     * @throws DaoException
     *             The exception thrown when error occurred.
     */
    public final GroupLines getGroupLines() throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet result = null;
        GroupLines groupLines = null;
        List<GroupLineInfo> groupLineList = null;
        GroupLineInfo groupLineInfo = null;

        try {
            groupLines = new GroupLines();
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            selectStmt = connection.prepareStatement(sqlStatement.getProperty("get-group-line"));
            result = selectStmt.executeQuery();
            while (result.next()) {
                if (groupLineList == null) {
                    groupLineList = new ArrayList<GroupLineInfo>();
                }
                groupLineInfo = new GroupLineInfo();

                groupLineInfo.setGroupId(result.getString("GroupId"));
                groupLineInfo.setGroupName(result.getString("GroupName"));
                groupLineInfo.setLine(result.getString("Line"));
                groupLineInfo.setLineName(result.getString("LineNameLocal"));

                groupLineList.add(groupLineInfo);
            }
            if (groupLineList == null) {
                groupLines.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                groupLines.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                groupLines.setMessage(ResultBase.RES_NODATAFOUND_MSG);
            } else {
                groupLines.setGroupLineList(groupLineList);
                groupLines.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                groupLines.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                groupLines.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName + ": Failed to get Group Line　Info.",
                    sqlStmtEx);
            throw new DaoException("SQLStatementException:@" + functionName, sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Group Line　Info.", sqlEx);
            throw new DaoException("SQLException:@" + functionName, sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Group Line　Info.", e);
            throw new DaoException("Exception:@" + functionName, e);
        } finally {
            closeConnectionObjects(connection, selectStmt, result);
            tp.methodExit(groupLines);
        }
        return groupLines;
    }

    @Override
    public final ReasonDataList getDiscountReason() throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        ReasonDataList reasonDataList = new ReasonDataList();
        TransactionReasonDataList txReasonData = new TransactionReasonDataList();
        ItemReasonDataList itemReasonData = new ItemReasonDataList();
        OverrideReasonDataList overrideReasonData = new OverrideReasonDataList();

        List<ReasonData> txReasons = new ArrayList<ReasonData>(), itemReasons = new ArrayList<ReasonData>(),
                overrideReasons = new ArrayList<ReasonData>();

        // TODO: change implementation since PRM_DISCOUNT_REASON_CODE table was
        // removed

        // temp response
        txReasonData.setReasonData(txReasons);
        itemReasonData.setReasonData(itemReasons);
        overrideReasonData.setReasonData(overrideReasons);

        reasonDataList.setTxReasonDataList(txReasonData);
        reasonDataList.setItemReasonDataList(itemReasonData);
        reasonDataList.setOverrideReasonDataList(overrideReasonData);

        tp.methodExit(reasonDataList);

        return reasonDataList;
    }

    @Override
    public final ReasonDataList getDiscountButtons() throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        ReasonDataList discountDataList = new ReasonDataList();
        discountDataList.setNCRWSSResultCode(ResultBase.RES_OK);

        TransactionReasonDataList txDiscBtns = new TransactionReasonDataList();
        ItemReasonDataList itemDiscBtns = new ItemReasonDataList();
        OverrideReasonDataList overrideDiscBtns = new OverrideReasonDataList();

        List<ReasonData> discDataItems = new ArrayList<ReasonData>(), discDataTx = new ArrayList<ReasonData>(),
                discDataOverride = new ArrayList<ReasonData>();
        List<ReasonData> discDataItems2 = new ArrayList<ReasonData>(), discDataTx2 = new ArrayList<ReasonData>(),
                discDataOverride2 = new ArrayList<ReasonData>();

        // TODO: change implementation since PRM_DISCOUNT_BUTTONS table was
        // removed

        // temp response
        txDiscBtns.setReasonData(discDataTx);
        txDiscBtns.setReasonData2(discDataTx2);
        itemDiscBtns.setReasonData(discDataItems);
        itemDiscBtns.setReasonData2(discDataItems2);
        overrideDiscBtns.setReasonData(discDataOverride);
        overrideDiscBtns.setReasonData2(discDataOverride2);

        discountDataList.setTxReasonDataList(txDiscBtns);
        discountDataList.setItemReasonDataList(itemDiscBtns);
        discountDataList.setOverrideReasonDataList(overrideDiscBtns);

        tp.methodExit(discountDataList);

        return discountDataList;
    }

    private static final String NAME_CATEGORY_MAGICNUMBER = "0060";

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getItemByPLU(String storeId, String pluCode, String companyId, String businessDate)
            throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("StoreID", storeId)
                .println("PLU", pluCode)
                .println("CompanyId", companyId)
                .println("BusinessDate", businessDate);
        final String functionName = "SQLServerItemDAO.getItemByPLU";
        Item returnItem = null;

        try(Connection connection = dbManager.getConnection();) {

            MstPluDAO mstPluDAO = new MstPluDAO(connection);
            MstPlu mstPlu = mstPluDAO.selectWithDefaultId(companyId, storeId, pluCode);
            if(mstPlu != null) {
                // Gets related dao-models.
                MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
                MstPluStore mstPluStore =
                        mstPluStoreDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMdInternal());

                MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
                MstDptInfo mstDptInfo = mstDptInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getDpt());

                MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(connection);
                MstGroupInfo mstGroupInfo = mstGroupInfoDAO.selectWithDefaultId(
                        companyId, storeId, mstDptInfo.getSubCode1());

                MstClassInfoDAO mstClassInfoDAO = new MstClassInfoDAO(connection);
                MstClassInfo mstClassInfo = mstClassInfoDAO.selectWithDefaultId(
                                companyId, storeId, mstPlu.getClassCode(), mstPlu.getDpt(), mstPlu.getLine());

                MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(connection);
                MstNameSystem mstNameSystem = mstNameSystemDAO.selectWithDefaultId(
                                companyId, storeId, String.valueOf(mstPlu.getTaxType()), NAME_CATEGORY_MAGICNUMBER);

                MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
                MstColorInfo mstColorInfo = mstColorInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMd01());

                MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
                MstSizeInfo mstSizeInfo = mstSizeInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMd05());

                MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(connection);
                MstBrandInfo mstBrandInfo = mstBrandInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMd07());

                MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO = new MstPriceUrgentForStoreDAO(connection);
                MstPriceUrgentForStore mstPriceUrgentForStore = mstPriceUrgentForStoreDAO.selectOne(
                        companyId, storeId, mstPlu.getSku(), mstPlu.getMd01(), businessDate);

                // Sets regular unit price from regional information,
                // such as Urgent-Price-Change, Store-Price, and Plu-itself.
                returnItem = new Item();
                returnItem = populateRegionalRegularSalesUnitPrice(returnItem, mstPlu, mstPluStore, mstPriceUrgentForStore);
                returnItem.setActualSalesUnitPrice(returnItem.getRegularSalesUnitPrice());

                // Populates item values by dao-models.
                returnItem = populateItemInfo(returnItem, mstPlu, mstDptInfo, mstGroupInfo, mstClassInfo,
                        mstNameSystem, mstColorInfo, mstSizeInfo, mstBrandInfo);

                // Sets promotional discount price into Item, such as Bargain, Bundling and trade-in replacement.
                returnItem = populatePromotionalDiscountInfo(storeId, companyId, businessDate, returnItem);

                // Sets additional information into Item, such as Coupon, Premium and QRCode.
                returnItem = populateAdditionalItemInfo(storeId, companyId, businessDate, returnItem);

            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemByPLU ", sqlEx);
        } finally {
            tp.methodExit(returnItem);
        }
        return returnItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getItemPriceByPLU(String storeId, String pluCode, String companyId, String businessDate)
            throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("StoreID", storeId)
                .println("PLU", pluCode)
                .println("CompanyId", companyId)
                .println("BusinessDate", businessDate);
        final String functionName = "SQLServerItemDAO.getItemByPLU";
        Item returnItem = null;

        try(Connection connection = dbManager.getConnection();) {

            MstPluDAO mstPluDAO = new MstPluDAO(connection);
            MstPlu mstPlu = mstPluDAO.selectWithDefaultId(companyId, storeId, pluCode);
            if(mstPlu != null) {
                // Gets related dao-models.
                MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
                MstPluStore mstPluStore =
                        mstPluStoreDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMdInternal());

                MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO = new MstPriceUrgentForStoreDAO(connection);
                MstPriceUrgentForStore mstPriceUrgentForStore = mstPriceUrgentForStoreDAO.selectOne(
                        companyId, storeId, mstPlu.getSku(), mstPlu.getMd01(), businessDate);

                returnItem = new Item();

                // Populates item values by dao-models.
                returnItem.setItemId(mstPlu.getMdInternal());
                returnItem.setCompanyId(mstPlu.getCompanyId());
                returnItem.setStoreId(mstPlu.getStoreId());

                // Sets regular unit price from regional information,
                // such as Urgent-Price-Change, Store-Price, and Plu-itself.
                returnItem = populateRegionalRegularSalesUnitPrice(returnItem, mstPlu, mstPluStore, mstPriceUrgentForStore);
            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemPriceByPLU ", sqlEx);
        } finally {
            tp.methodExit(returnItem);
        }
        return returnItem;
    }

    // Params for SalesPriceFrom.
    private static final String PRICE_FROM_URGENT_INFO_FORSTORE = "2";
    private static final String PRICE_FROM_PLU_STORE = "1";
    private static final String PRICE_FROM_PLU = "0";

    /**
     * Sets RegularSalesUnitPrice from URGENT, STORE and PLU.
     * @param returnItem
     * @param mstPlu
     * @param mstPluStore
     * @param mstPriceUrgentForStore
     * @return
     */
    private Item populateRegionalRegularSalesUnitPrice(Item returnItem, MstPlu mstPlu, MstPluStore mstPluStore,
                                                       MstPriceUrgentForStore mstPriceUrgentForStore) {
        // Top Priority: Price from MST_PRICE_URGENT_INFO_FORSTORE.NewPrice
        if(mstPriceUrgentForStore != null) {
            returnItem.setRegularSalesUnitPrice(mstPriceUrgentForStore.getNewPrice());
            returnItem.setOldPrice(mstPriceUrgentForStore.getOldPrice());
            returnItem.setSalesPriceFrom(PRICE_FROM_URGENT_INFO_FORSTORE);
            return returnItem;
        }
        // 2nd Priority: MST_PLU_STORE.SalesPrice
        if(mstPluStore != null) {
            returnItem.setRegularSalesUnitPrice(mstPluStore.getSalesPrice());
            returnItem.setSalesPriceFrom(PRICE_FROM_PLU_STORE);
            return returnItem;
        }
        // Lowest Priority: MST_PLU.SalesPrice1
        returnItem.setRegularSalesUnitPrice(mstPlu.getSalesPrice1());
        returnItem.setSalesPriceFrom(PRICE_FROM_PLU);
        return returnItem;
    }

    /**
     * Populates Item values by dao-models.
     * @param item Item object to populate values.
     * @param mstPlu
     * @param mstDptInfo
     * @param mstGroupInfo
     * @param mstClassInfo
     * @param mstNameSystem
     * @param mstColorInfo
     * @param mstSizeInfo
     * @param mstBrandInfo
     * @return Item
     * @throws DaoException
     */
    private Item populateItemInfo(Item item, MstPlu mstPlu, MstDptInfo mstDptInfo, MstGroupInfo mstGroupInfo,
                                  MstClassInfo mstClassInfo, MstNameSystem mstNameSystem, MstColorInfo mstColorInfo,
                                  MstSizeInfo mstSizeInfo, MstBrandInfo mstBrandInfo) throws DaoException {
        // PK MST_PLU.MdInternal -> ItemId.
        item.setItemId(mstPlu.getMdInternal());

        // MST_PLU.MdNameLocal -> Description.
        Description description = new Description();
        description.setJa(mstPlu.getMdNameLocal());
        item.setDescription(description);

        // MST_PLU.
        item.setLine(mstPlu.getLine());
        item.setItemClass(mstPlu.getClassCode());
        item.setTaxType(mstPlu.getTaxType());
        item.setTaxRate(mstPlu.getTaxRate());
        item.setSubNum1(mstPlu.getSubNum1());
        item.setSubNum2(mstPlu.getSubNum2());
        item.setPluPrice(mstPlu.getSalesPrice1());
        item.setMd01(mstPlu.getMd01());
        item.setOrgSalesPrice1(mstPlu.getOrgSalesPrice1());
        item.setDepartment(mstPlu.getDpt());
        item.setDiscountType(mstPlu.getDiscountType());
        item.setMd02(mstPlu.getMd02());
        item.setMd03(mstPlu.getMd03());
        item.setMd04(mstPlu.getMd04());
        item.setMd05(mstPlu.getMd05());
        item.setMd06(mstPlu.getMd06());
        item.setMd07(mstPlu.getMd07());
        item.setMd08(mstPlu.getMd08());
        item.setMd09(mstPlu.getMd09());
        item.setMd10(mstPlu.getMd10());
        item.setMd11(String.valueOf(mstPlu.getMd11()));
        item.setMd12(String.valueOf(mstPlu.getMd12()));
        item.setMd13(String.valueOf(mstPlu.getMd13()));
        item.setMd14(String.valueOf(mstPlu.getMd14()));
        item.setMd15(String.valueOf(mstPlu.getMd15()));
        item.setMd16(String.valueOf(mstPlu.getMd16()));
        item.setMdType(mstPlu.getMdType());
        item.setSku(mstPlu.getSku());
        item.setMdNameLocal(mstPlu.getMdNameLocal());
        item.setMdKanaName(mstPlu.getMdKanaName());
        item.setSalesPrice2(mstPlu.getSalesPrice2());
        item.setPaymentType(mstPlu.getPaymentType());
        item.setSubCode1(mstPlu.getSubCode1());
        item.setSubCode2(mstPlu.getSubCode2());
        item.setSubCode3(mstPlu.getSubCode3());
        item.setMdVender(mstPlu.getMdVender());
        item.setCostPrice1(mstPlu.getCostPrice1());
        item.setMakerPrice(mstPlu.getMakerPrice());
        item.setConn1(mstPlu.getConn1());
        item.setConn2(mstPlu.getConn2());

        // MST_DPTINFO
        if(mstDptInfo != null) {
            item.setDptDiscountType(mstDptInfo.getDiscountType());
            item.setDptNameLocal(mstDptInfo.getDptNameLocal());
            item.setDptSubCode1(mstDptInfo.getSubCode1());
        }

        // MST_CLASSINFO
        if(mstClassInfo != null) {
            item.setClassNameLocal(mstClassInfo.getClassNameLocal());
        }

        // MST_GROUPINFO
        if(mstGroupInfo != null) {
            item.setGroupName(mstGroupInfo.getGroupName());
            item.setGroupID(mstGroupInfo.getGroupId());
        }

        // MST_NAME_SYSTEM
        if(mstNameSystem != null) {
            item.setNameText(mstNameSystem.getNameText());
        }

        // MST_COLORINFO
        if(mstColorInfo != null) {
            item.setColorkananame(mstColorInfo.getColorKanaName());
        }

        // MST_SIZEINFO
        if(mstSizeInfo != null) {
            item.setSizeKanaName(mstSizeInfo.getSizeKanaName());
        }

        // MST_BRANDINFO
        if(mstBrandInfo != null) {
            item.setBrandName(mstBrandInfo.getBrandName());
        }

        return item;
    }

    /**
     * Populates PromotionalDiscount information on Item.
     * @param storeId
     * @param companyId
     * @param businessDate
     * @param item Item to set values.
     * @return
     * @throws DaoException
     */
    private Item populatePromotionalDiscountInfo(String storeId, String companyId, String businessDate, Item item)
            throws DaoException {
        // Top Priority: TOKUBAI, Bargain.
        if (hasPromDetailInfo(storeId, item, companyId, businessDate)) {
            return item;
        }
        // 2nd: Mix-Match bundle discount.
        if (hasPriceMMInfo(storeId, item, companyId, businessDate)) {
            return item;
        }
        // 3rd: SHITADORI, Trade-in and replacement support discount.
        hasReplaceSupportDetailInfo(storeId, item, companyId, businessDate);
        return item;
    }

    /**
     * Populates additional item information, such as Coupon, Premium and QrCode.
     * @param storeId
     * @param companyId
     * @param businessDate
     * @param item Item to set values.
     * @return
     * @throws DaoException
     */
    private Item populateAdditionalItemInfo(String storeId, String companyId, String businessDate, Item item)
            throws DaoException {
        // 割引券発行管理
        getCouponInfo(storeId, item, companyId, businessDate);
        // プレミアム商品
        getPremiumitemInfo(storeId, item, companyId, businessDate);
        //QRcode
        getQrCodeInfo(storeId, item, companyId, businessDate);
        return item;
    }

    /**
     * 特売管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    private boolean hasPromDetailInfo(final String storeid, final Item searchedItem, final String companyId,
                                      final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        boolean isHaveValue = false;
        String functionName = "SQLServerItemDAO.hasPromDetailInfo";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-price-prom-info"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, searchedItem.getSku());
            select.setString(SQLStatement.PARAM4, bussinessDate);
            select.setString(SQLStatement.PARAM5, searchedItem.getDepartment());
            select.setString(SQLStatement.PARAM6, searchedItem.getMd07());
            select.setString(SQLStatement.PARAM7, searchedItem.getItemClass());
            result = select.executeQuery();
            if(result.next()){
                isHaveValue = true;
              searchedItem.setPromotionNo(result.getString(result.findColumn("PromotionNo")));
              searchedItem.setDiscountClass(result.getInt(result.findColumn("DiscountClass")));
              searchedItem.setDiacountRate(result.getDouble(result.findColumn("DiacountRate")));
              searchedItem.setDiscountAmt(result.getInt(result.findColumn("DiscountAmt")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @hasPromDetailInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @hasPromDetailInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @hasPromDetailInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }
        return isHaveValue;
    }
    
    /**
     * バンドルミックス 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    private boolean hasPriceMMInfo(final String storeid, final Item searchedItem, final String companyId,
                                   final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        boolean isHaveValue = false;
        String functionName = "SQLServerItemDAO.hasPriceMMInfo";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-price-mm-info"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, searchedItem.getSku());
            select.setString(SQLStatement.PARAM4, bussinessDate);
            result = select.executeQuery();
            if (result.next()) {
                isHaveValue = true;
                searchedItem.setMixMatchCode(result.getString(result.findColumn("MMNo")));
                searchedItem.setRuleQuantity1(result.getInt(result.findColumn("ConditionCount1")));
                searchedItem.setRuleQuantity2(result.getInt(result.findColumn("ConditionCount2")));
                searchedItem.setRuleQuantity3(result.getInt(result.findColumn("ConditionCount3")));
                
                searchedItem.setConditionPrice3(result.getDouble(result.findColumn("ConditionPrice3")));
                searchedItem.setConditionPrice2(result.getDouble(result.findColumn("ConditionPrice2")));
                searchedItem.setConditionPrice1(result.getDouble(result.findColumn("ConditionPrice1")));
                
                if(result.getObject(result.findColumn("DecisionPrice1")) != null ){
                    searchedItem.setDecisionPrice1(result.getDouble(result.findColumn("DecisionPrice1")));
                }
                
                if(result.getObject(result.findColumn("DecisionPrice2")) != null ){
                    searchedItem.setDecisionPrice2(result.getDouble(result.findColumn("DecisionPrice2")));
                }
                
                if(result.getObject(result.findColumn("DecisionPrice3")) != null ){
                    searchedItem.setDecisionPrice3(result.getDouble(result.findColumn("DecisionPrice3")));
                }
               
                searchedItem.setAveragePrice1(result.getDouble(result.findColumn("AveragePrice1")));
                searchedItem.setAveragePrice2(result.getDouble(result.findColumn("AveragePrice2")));
                searchedItem.setAveragePrice3(result.getDouble(result.findColumn("AveragePrice3")));
                
                searchedItem.setNote(result.getString(result.findColumn("Note")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @hasPriceMMInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @hasPriceMMInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @hasPriceMMInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }
        return isHaveValue;
    }
    
    /**
     * クラブ買替えサポート管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    private boolean hasReplaceSupportDetailInfo(final String storeid, final Item searchedItem, final String companyId,
                                                final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        boolean isHaveValue = false;
        String functionName = "SQLServerItemDAO.hasReplaceSupportDetailInfo";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-price-ReplaceSupport-info"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, searchedItem.getSku());
            select.setString(SQLStatement.PARAM4, bussinessDate);
            result = select.executeQuery();
            if(result.next()){
                isHaveValue = true;
                searchedItem.setPromotionId(result.getString(result.findColumn("PromotionId")));
                searchedItem.setReplaceSupportdiscountAmt(result.getInt(result.findColumn("DiscountAmt")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @hasReplaceSupportDetailInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @hasReplaceSupportDetailInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @hasReplaceSupportDetailInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }
        return isHaveValue;
    }
    
    
    /**
     * 割引券発行管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    private void getCouponInfo(final String storeid, final Item searchedItem, final String companyId,
            final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String functionName = "SQLServerItemDAO.getCouponInfo";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-coupon-info"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, searchedItem.getSku());
            select.setString(SQLStatement.PARAM4, bussinessDate);
            select.setString(SQLStatement.PARAM5, searchedItem.getDepartment());
            select.setString(SQLStatement.PARAM6, searchedItem.getMd07());
            result = select.executeQuery();
            if(result.next()){
                searchedItem.setCouponNo(result.getString(result.findColumn("CouponNo")));
                searchedItem.setEvenetName(result.getString(result.findColumn("EvenetName")));
                searchedItem.setReceiptName(result.getString(result.findColumn("ReceiptName")));
                searchedItem.setIssueType(result.getString(result.findColumn("IssueType")));
                searchedItem.setIssueCount(result.getInt(result.findColumn("IssueCount")));
                searchedItem.setUnitPrice(result.getDouble(result.findColumn("UnitPrice")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getCouponInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getCouponInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @getCouponInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(searchedItem);
        }
    }
    
    
    /**
     * プレミアム商品管理マスタ 情報取得
     * @param storeid The ID of the Store where the items are located
     * @param searchedItem the Item
     * @param companyId The ID of the companyId
     * @param bussinessDate the bussinessDate
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    private void getPremiumitemInfo(final String storeid, final Item searchedItem, final String companyId,
            final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String functionName = "SQLServerItemDAO.getPremiumitemInfo";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-premiumitem-info"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, searchedItem.getSku());
            select.setString(SQLStatement.PARAM4, bussinessDate);
            select.setString(SQLStatement.PARAM5, searchedItem.getDepartment());
            select.setString(SQLStatement.PARAM6, searchedItem.getMd07());
            select.setString(SQLStatement.PARAM7, searchedItem.getConn1());
            result = select.executeQuery();
            List<PremiumInfo> list = new ArrayList<PremiumInfo>();
            while(result.next()){
                PremiumInfo premiumInfo = new PremiumInfo();
                premiumInfo.setPremiumItemNo(result.getString(result.findColumn("PremiumItemNo")));
                premiumInfo.setPremiumItemName(result.getString(result.findColumn("PremiumItemName")));
                premiumInfo.setTargetPrice(result.getString(result.findColumn("TargetPrice")));
                premiumInfo.setTargetCount(result.getInt(result.findColumn("TargetCount")));
                list.add(premiumInfo);
            }
            searchedItem.setPremiumList(list);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPremiumitemInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getPremiumitemInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @getPremiumitemInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(searchedItem);
        }
    }
    
    private void getQrCodeInfo(final String storeid, final Item searchedItem, final String companyId,
            final String bussinessDate) throws DaoException{
        
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String functionName = "SQLServerItemDAO.getQrCodeInfo";
        List<QrCodeInfo> list = new ArrayList<QrCodeInfo>(); 
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-qrcode-info"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, searchedItem.getDepartment());
            select.setString(SQLStatement.PARAM3, searchedItem.getMd07());
            select.setString(SQLStatement.PARAM4, searchedItem.getItemClass());
            select.setString(SQLStatement.PARAM5, searchedItem.getSku());
            select.setString(SQLStatement.PARAM6, storeid);
            select.setString(SQLStatement.PARAM7, bussinessDate);
            select.setString(SQLStatement.PARAM8, searchedItem.getConn1());
            result = select.executeQuery();
            while(result.next()){
            	if(null == result.getObject(result.findColumn("ExclsionInfo"))){
                QrCodeInfo qr = new QrCodeInfo();
                qr.setBmpFileCount(result.getString(result.findColumn("BmpFileCount")));
                qr.setBmpFileFlag(result.getString(result.findColumn("BmpFileFlag")));
                qr.setBmpFileName(result.getString(result.findColumn("BmpFileName")));
                if(null != result.getObject(result.findColumn("MinimumPrice"))){
                    qr.setMinimumPrice(result.getDouble(result.findColumn("MinimumPrice")));
                }else{
                    qr.setMinimumPrice(null);
                }
                qr.setOutputTargetValue(result.getString(result.findColumn("OutputTargetValue")));
                qr.setPromotionId(result.getString(result.findColumn("QRPromotionId")));
                qr.setPromotionName(result.getString(result.findColumn("QRPromotionName")));
                qr.setOutputType(result.getString(result.findColumn("OutputType")));
                list.add(qr);
              }
            }
            searchedItem.setQrCodeList(list);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the qrcode information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getQrCodeInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the qrcode information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getQrCodeInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the qrcode information.\n" + e.getMessage());
            throw new DaoException("Exception: @getQrCodeInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(searchedItem);
        }
    }

    @Override
    public Item getMixMatchInfo(String storeId, String sku, String companyId, String businessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeId)
        .println("sku", sku)
        .println("BusinessDate", businessDate)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String functionName = "SQLServerItemDAO.hasPriceMMInfo";
        Item searchedItem = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-price-mm-info"));
            select.setString(SQLStatement.PARAM1, storeId);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, sku);
            select.setString(SQLStatement.PARAM4, businessDate);
            result = select.executeQuery();
            if (result.next()) {
                searchedItem = new Item();
                
                searchedItem.setMixMatchCode(result.getString(result.findColumn("MMNo")));
                searchedItem.setRuleQuantity1(result.getInt(result.findColumn("ConditionCount1")));
                searchedItem.setRuleQuantity2(result.getInt(result.findColumn("ConditionCount2")));
                searchedItem.setRuleQuantity3(result.getInt(result.findColumn("ConditionCount3")));

                searchedItem.setConditionPrice1(result.getDouble(result.findColumn("ConditionPrice1")));
                searchedItem.setConditionPrice2(result.getDouble(result.findColumn("ConditionPrice2")));
                searchedItem.setConditionPrice3(result.getDouble(result.findColumn("ConditionPrice3")));

                if(result.getObject(result.findColumn("DecisionPrice1")) != null ){
                    searchedItem.setDecisionPrice1(result.getDouble(result.findColumn("DecisionPrice1")));
                }
                
                if(result.getObject(result.findColumn("DecisionPrice2")) != null ){
                    searchedItem.setDecisionPrice2(result.getDouble(result.findColumn("DecisionPrice2")));
                }
                
                if(result.getObject(result.findColumn("DecisionPrice3")) != null ){
                    searchedItem.setDecisionPrice3(result.getDouble(result.findColumn("DecisionPrice3")));
                }

                searchedItem.setAveragePrice1(result.getDouble(result.findColumn("AveragePrice1")));
                searchedItem.setAveragePrice2(result.getDouble(result.findColumn("AveragePrice2")));
                searchedItem.setAveragePrice3(result.getDouble(result.findColumn("AveragePrice3")));
                searchedItem.setNote(result.getString(result.findColumn("Note")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @hasPriceMMInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @hasPriceMMInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @hasPriceMMInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }
        return searchedItem;
    }

   /**
    * get items for pick list
    */
    @Override
    public List<PickListItemType> getPickListItems(String companyId, String storeId, String itemType) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("companyId", companyId)
        .println("storeId", storeId)
        .println("itemType", itemType);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        PickListItem item = null;
        PickListItemType itemTypes [] = new PickListItemType[4];
        List<PickListItemType> items = new ArrayList<>();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-picklist-items"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);
            if(itemType == null || itemType.isEmpty()) {
                select.setNull(SQLStatement.PARAM3, java.sql.Types.NULL);
                select.setNull(SQLStatement.PARAM4, java.sql.Types.NULL);
            } else {
                select.setString(SQLStatement.PARAM3, itemType);
                select.setString(SQLStatement.PARAM4, itemType);
            }
            result = select.executeQuery();
            int itemtype = 0;
            itemTypes[itemtype] = new PickListItemType();
            while(result.next()) {
                item = new PickListItem();
                item.setDisplayOrder(result.getInt("DisplayOrder"));
                item.setItemName(result.getString("ItemName"));
                item.setItemType(result.getInt("ItemType"));
                item.setMdInternal(result.getString("MdInternal"));
                if(item.getItemType() < 4) {
                    if(itemtype != item.getItemType()) {
                        itemtype = item.getItemType();
                    } 
                    if(itemTypes[itemtype] == null) {
                        itemTypes[itemtype] = new PickListItemType();
                    }
                    itemTypes[itemtype].addItem(item);
                }
            }
            items = new ArrayList<PickListItemType>(Arrays.asList(itemTypes));
        } catch (Exception e) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the item information.\n" + e.getMessage());
            throw new DaoException("Exception: @getPickListItems ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(itemTypes);
        }
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getItemAttributeByPLU(String storeId, String pluCode, String companyId)
            throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("StoreID", storeId)
                .println("PLU", pluCode)
                .println("CompanyId", companyId);
        final String functionName = "SQLServerItemDAO.getItemAttributeByPLU";
        Item returnItem = null;

        // Default value of storeId.
        if(storeId == null) {
            storeId = "0";
        }

        try(Connection connection = dbManager.getConnection();) {

            MstPluDAO mstPluDAO = new MstPluDAO(connection);
            MstPlu mstPlu = mstPluDAO.selectWithDefaultId(companyId, storeId, pluCode);
            if(mstPlu != null) {
                // Gets related dao-models.
                MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
                MstColorInfo mstColorInfo = mstColorInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMd01());

                MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
                MstSizeInfo mstSizeInfo = mstSizeInfoDAO.selectWithDefaultId(companyId, storeId, mstPlu.getMd05());

                // Sets regular unit price from regional information,
                // such as Urgent-Price-Change, Store-Price, and Plu-itself.
                returnItem = new Item();
                returnItem = populateRegionalRegularSalesUnitPrice(returnItem, mstPlu, null, null);
                returnItem.setActualSalesUnitPrice(returnItem.getRegularSalesUnitPrice());

                // Populates item values by dao-models.
                returnItem = populateItemInfo(returnItem, mstPlu, null, null, null, null,
                        mstColorInfo, mstSizeInfo, null);
            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(CLASS_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemAttributeByPLU ", sqlEx);
        } finally {
            tp.methodExit(returnItem);
        }
        return returnItem;
    }
    
}
