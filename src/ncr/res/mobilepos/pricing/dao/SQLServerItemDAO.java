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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickListItem;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.model.PremiumInfo;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Item Pricing.
 *
 * @see IItemDAO
 */

public class SQLServerItemDAO extends AbstractDao implements IItemDAO {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
                                                                        // the
                                                                        // Logger
    /**
     * The Program name.
     */
    private String progname = "ItemDAO";
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
     * Gets the Database Manager for SQLServerItemDAO.
     *
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
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
    
    private String chooseMdNameLocal(String mdName, String mdNameLocal, String dptNameLocal) {
        if(mdName != null) {
            return mdName; 
        }
        if(mdNameLocal != null) {
            return mdNameLocal;  
        }
        return dptNameLocal;
   }
    
    /**
     * Gets the information of a single item by specifying the Store ID and and
     * its corresponding PLU code.
     *
     * @param storeid
     *            The Store ID which the item is located
     * @param pluCode
     *            The Item's Price Look Up Code
     * @param bussinessDate
     *            The date 
     * @param EventId
     *            The EventId
     * @return The details of the particular item
     * @throws DaoException
     *             Exception thrown when getting the item information failed.
     */
    @Override
    public Item getItemByPLU(String storeid, String pluCode, String companyId, int priceIncludeTax, String bussinessDate)
            throws DaoException {
        return this.getItemByPLUWithStoreFixation(storeid, pluCode, true, companyId, priceIncludeTax,bussinessDate);
    }

    
    /** Gets the information of a single item by specifying the Store ID and and
    * its corresponding PLU code.
    *
    * @param storeid
    *            The Store ID which the item is located
    * @param pluCode
    *            The Item's Price Look Up Code
    * @param storeFixation
    *            Flag that tells that should be fixed.
    * @param bussinessDate
    *            The bussinessDate
    * @param EventId
    *            The EventId
    * @return The details of the particular item
    * @throws DaoException
    *             Exception thrown when getting the item information failed.
    */
    private Item getItemByPLUWithStoreFixation(final String storeid, final String pluCode, final boolean storeFixation,
            final String companyId, final int priceIncludeTax, final String bussinessDate) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("StoreID", storeid).println("PLU", pluCode)
                .println("IsStoreFixation", storeFixation).println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        Item searchedItem = null;
        String functionName = "SQLServerItemDAO.getItemByPLUWithStoreFixation";
        String actualStoreid = storeid;
        String salesNameLocal = "";
        String salesName = "";
        String pluMdName = "";
        String sizeId = "";
        String mdInternal = "";
        if (pluCode.contains(" ")) {
        	mdInternal = pluCode.split(" ")[0];
        	sizeId = pluCode.split(" ")[1].substring(4, 7);
		} else {
			mdInternal = pluCode;
		}
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-item"));
            select.setString(SQLStatement.PARAM1, actualStoreid);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, mdInternal);
            select.setString(SQLStatement.PARAM4, bussinessDate);
            select.setString(SQLStatement.PARAM5, sizeId);
            result = select.executeQuery();

            if (result.next()) {
                searchedItem = new Item();
                searchedItem.setItemId(result.getString(result.findColumn("MdInternal"))); // PK

                // get item description
                Description description = new Description();
                salesNameLocal = chooseMdNameLocal(null,result.getString(result.findColumn("MdNameLocal")),
                        result.getString(result.findColumn("DptNameLocal")));
                salesName = chooseMdNameLocal(result.getString(result.findColumn("MdName")),null,
                        result.getString(result.findColumn("DptName")));
                pluMdName = result.getString(result.findColumn("pluMdName"));
                if(StringUtility.isNullOrEmpty(pluMdName)) {
                    description.setJa(salesNameLocal);
                    description.setEn(salesName);
                } else {
                    description.setJa(pluMdName);
                    description.setEn(pluMdName);
                }
                searchedItem.setDescription(description);
                
                if (result.getString(result.findColumn("pluMdName")) != null) {
                	searchedItem.setSalesNameSource("1");
                } else if (result.getString(result.findColumn("pluMdName")) == null
                		&& result.getString(result.findColumn("MdNameLocal")) != null) {
                	searchedItem.setSalesNameSource("2");
                } else if (result.getString(result.findColumn("pluMdName")) == null
                		&& result.getString(result.findColumn("MdNameLocal")) == null
                		&& result.getString(result.findColumn("DptNameLocal")) != null){
                	searchedItem.setSalesNameSource("3");
                }

                if (result.getObject(result.findColumn("TaxType")) != null && 
                		result.getDouble(result.findColumn("SalesPrice1")) != 0) {
                	searchedItem.setTaxType(result.getString(result.findColumn("TaxType")));
                }
                searchedItem.setDptTaxType(result.getString(result.findColumn("dptTaxType")));
                searchedItem.setClsTaxType(result.getString(result.findColumn("clsTaxType")));
                searchedItem.setLineTaxType(result.getString(result.findColumn("lineTaxType")));
                searchedItem.setTaxRate(result.getInt(result.findColumn("TaxRate")));
                searchedItem.setSubNum1(result.getInt(result.findColumn("SubNum1")));
                searchedItem.setSubNum2(result.getInt(result.findColumn("SubNum2")));

                if (result.getObject(result.findColumn("NewPrice")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("NewPrice")));
                    searchedItem.setOldPrice(result.getDouble(result.findColumn("OldPrice")));
                    searchedItem.setSalesPriceFrom("2");
                } else if (result.getObject(result.findColumn("NewPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("SalesPrice")));
                    searchedItem.setSalesPriceFrom("1");
                } else if (result.getObject(result.findColumn("NewPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice1")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("SalesPrice1")));
                    searchedItem.setSalesPriceFrom("0");
                } else {
                    searchedItem.setRegularSalesUnitPrice(0);
                }
                if(result.getObject(result.findColumn("SalesPrice1")) != null){
                    searchedItem.setPluPrice(result.getDouble(result.findColumn("SalesPrice1")));
                }else{
                    searchedItem.setPluPrice(0);
                }
                searchedItem.setMd01(result.getString(result.findColumn("Md01")));
                if (result.getObject(result.findColumn("OrgSalesPrice1")) == null) {
                    searchedItem.setOrgSalesPrice1(0);
                } else {
                    searchedItem.setOrgSalesPrice1(result.getDouble(result.findColumn("OrgSalesPrice1")));
                }
                searchedItem.setDepartment(result.getString(result.findColumn("Dpt")));
                searchedItem.setDiscountType(result.getString(result.findColumn("DiscountType")));
                searchedItem.setDptDiscountType(result.getString(result.findColumn("dptDiscountType")));
                searchedItem.setClsDiscountType(result.getString(result.findColumn("clsDiscountType")));
                searchedItem.setLineDiscountType(result.getString(result.findColumn("lineDiscountType")));
                searchedItem.setMd02(result.getString(result.findColumn("Md02")));
                searchedItem.setMd03(result.getString(result.findColumn("Md03")));
                searchedItem.setMd04(result.getString(result.findColumn("Md04")));
                searchedItem.setMd05(result.getString(result.findColumn("Md05")));
                searchedItem.setMd06(result.getString(result.findColumn("Md06")));
                searchedItem.setMd07(result.getString(result.findColumn("Md07")));
                searchedItem.setMd08(result.getString(result.findColumn("Md08")));
                searchedItem.setMd09(result.getString(result.findColumn("Md09")));
                searchedItem.setMd10(result.getString(result.findColumn("Md10")));
                searchedItem.setMd11(result.getString(result.findColumn("Md11")));
                searchedItem.setMd12(result.getString(result.findColumn("Md12")));
                searchedItem.setMd13(result.getString(result.findColumn("Md13")));
                searchedItem.setMd14(result.getString(result.findColumn("Md14")));
                searchedItem.setMd15(result.getString(result.findColumn("Md15")));
                searchedItem.setMd16(result.getString(result.findColumn("Md16")));
                searchedItem.setMdType(result.getString(result.findColumn("MdType")));
                if (result.getObject(result.findColumn("HostFlag")) == null) {
                    searchedItem.setHostFlag(1);
                } else {
                    searchedItem.setHostFlag(result.getInt(result.findColumn("HostFlag")));
                }
                searchedItem.setSku(result.getString(result.findColumn("Sku")));
                searchedItem.setMdNameLocal(salesNameLocal);
                searchedItem.setMdName(salesName);
                searchedItem.setMdKanaName(result.getString(result.findColumn("MdKanaName")));
                searchedItem.setSalesPrice2(result.getLong(result.findColumn("SalesPrice2")));
                searchedItem.setPaymentType(result.getInt(result.findColumn("PaymentType")));
                searchedItem.setSubCode1(result.getString(result.findColumn("SubCode1")));
                searchedItem.setSubCode2(result.getString(result.findColumn("SubCode2")));
                searchedItem.setSubCode3(result.getString(result.findColumn("SubCode3")));
                searchedItem.setMdVender(result.getString(result.findColumn("MdVender")));
                searchedItem.setPaymentType(result.getInt(result.findColumn("PaymentType")));
                if (result.getObject(result.findColumn("PosMdType")) == null) {
                	searchedItem.setNonSales(result.getInt(result.findColumn("ExceptionFlag")));
                } else {
                	searchedItem.setNonSales(result.getInt(result.findColumn("PosMdType")));
                }
                
                searchedItem.setCostPrice1(result.getLong(result.findColumn("CostPrice1")));
                searchedItem.setMakerPrice(result.getLong(result.findColumn("MakerPrice")));
                searchedItem.setConn1(result.getString(result.findColumn("Conn1")));
                searchedItem.setConn2(result.getString(result.findColumn("Conn2")));
                searchedItem.setPublishingCode(result.getString(result.findColumn("VenderCode")));
                
                searchedItem.setDptNameLocal(result.getString(result.findColumn("DptNameLocal")));
                searchedItem.setDptName(result.getString(result.findColumn("DptName")));
                searchedItem.setClassNameLocal(result.getString(result.findColumn("ClassNameLocal")));
                searchedItem.setGroupName(result.getString(result.findColumn("GroupName")));
                searchedItem.setNameText(result.getString(result.findColumn("NameText")));
                searchedItem.setDptSubCode1(result.getString(result.findColumn("dptSubCode1")));
                searchedItem.setDptSubNum1(result.getString(result.findColumn("dptSubNum1")));
                searchedItem.setDptSubNum2(result.getString(result.findColumn("dptSubNum2")));
                searchedItem.setDptSubNum3(result.getString(result.findColumn("dptSubNum3")));
                searchedItem.setDptSubNum4(result.getString(result.findColumn("dptSubNum4")));
                searchedItem.setGroupID(result.getString(result.findColumn("GroupId")));
                searchedItem.setColorkananame(result.getString(result.findColumn("Colorkananame")));
                searchedItem.setSizeKanaName(result.getString(result.findColumn("SizeKanaName")));
                searchedItem.setBrandName(result.getString(result.findColumn("BrandName")));
                searchedItem.setBrandSaleName(result.getString(result.findColumn("BrandSaleName")));
                searchedItem.setSaleSizeCode(result.getString(result.findColumn("SizePatternName")));
                searchedItem.setSizePatternId(result.getString(result.findColumn("SizePatternId")));
                searchedItem.setPointAddFlag(result.getString(result.findColumn("PointAddFlag")));
                searchedItem.setPointUseFlag(result.getString(result.findColumn("PointUseFlag")));
                searchedItem.setTaxExemptFlag(result.getString(result.findColumn("TaxFreeFlag")));
                if (storeFixation) {
                    searchedItem.setLine(result.getString(result.findColumn("Line")));
                    searchedItem.setItemClass(result.getString(result.findColumn("Class")));
                    // Compute the Actual Sales Price
                    searchedItem.setActualSalesUnitPrice(searchedItem.getRegularSalesUnitPrice());
                }

            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemByPLUWithStoreFixation ", sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the item information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getItemByPLUWithStoreFixation ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the item information.\n" + e.getMessage());
            throw new DaoException("Exception: @getItemByPLUWithStoreFixation ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }

        return searchedItem;
    }
    

    /**
     * 商品名前取得する
     * @param companyId The ID of the companyId
     * @param storeid The ID of the Store where the items are located
     * @param itemCode
     * @return MdName
     * @throws DaoException   Exception thrown when getting the name information failed.
     */
    public Sale getItemNameFromPluName(final String companyId, final String storeid, final String itemCode) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("CompanyId", companyId)
        .println("StoreID", storeid)
        .println("itemCode", itemCode);

        Sale sale = new Sale();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String functionName = "SQLServerItemDAO.getItemNameFromPluName";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-plu-name"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeid);
            select.setString(SQLStatement.PARAM3, itemCode);
            result = select.executeQuery();
            if(result.next()){
            	sale.setMdNameLocal(result.getString(result.findColumn("MdName")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemNameFromPluName ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getItemNameFromPluName ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @getItemNameFromPluName ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(sale);
        }
        return sale;
    }
    
    /**
     * 特売管理マスタ 情報取得
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    public boolean isHasPromDetailInfoList(final PricePromInfo pricePromInfo, final Item searchedItem, final double salePrice) throws DaoException {
    	tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("PricePromInfo", pricePromInfo)
        .println("searchedItem", searchedItem)
        .println("salePrice", salePrice);
    	
    	boolean isHaveValue = false;
    	
    	if (null != pricePromInfo) {
	        isHaveValue = true;
            searchedItem.setPromotionNo(pricePromInfo.getPromotionNo());
            searchedItem.setDiscountClass(Integer.parseInt(pricePromInfo.getDiscountClass()));
            searchedItem.setDiacountRate(pricePromInfo.getDiscountRate());
            searchedItem.setDiscountAmt((int)pricePromInfo.getDiscountAmt());
            searchedItem.setPromotionType(pricePromInfo.getPromotionType());
    	}
    	return isHaveValue;
    }
    
    /**
     * バンドルミックス 情報取得
     * @param priceMMInfo 
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    public boolean isHasPriceMMInfoList(final PriceMMInfo priceMMInfo, final Item searchedItem) throws DaoException {
    	tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("PriceMMInfo", priceMMInfo)
        .println("searchedItem", searchedItem);
    	
    	boolean isHaveValue = false;
    	
    	if (null != priceMMInfo) {
    		isHaveValue = true;
    		searchedItem.setMixMatchCode(priceMMInfo.getMMNo());
            searchedItem.setRuleQuantity1(priceMMInfo.getConditionCount1());
            searchedItem.setRuleQuantity2(priceMMInfo.getConditionCount2());
            searchedItem.setRuleQuantity3(priceMMInfo.getConditionCount3());
            
            searchedItem.setConditionPrice3(priceMMInfo.getConditionPrice3());
            searchedItem.setConditionPrice2(priceMMInfo.getConditionPrice2());
            searchedItem.setConditionPrice1(priceMMInfo.getConditionPrice1());
            searchedItem.setDecisionPrice1(priceMMInfo.getDecisionPrice1());
            searchedItem.setDecisionPrice2(priceMMInfo.getDecisionPrice2());
            searchedItem.setDecisionPrice3(priceMMInfo.getDecisionPrice3());
           
            searchedItem.setAveragePrice1(priceMMInfo.getAveragePrice1());
            searchedItem.setAveragePrice2(priceMMInfo.getAveragePrice2());
            searchedItem.setAveragePrice3(priceMMInfo.getAveragePrice3());
            
            searchedItem.setNote(priceMMInfo.getNote());
            searchedItem.setSku(priceMMInfo.getSku());
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
    public boolean isHasReplaceSupportDetailInfo(final String storeid, final Item searchedItem, final String companyId,
            final String bussinessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("StoreID", storeid)
        .println("searchedItem", searchedItem)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        boolean isHaveValue = false;
        String functionName = "SQLServerItemDAO.isHasReplaceSupportDetailInfo";
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
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @isHasReplaceSupportDetailInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @isHasReplaceSupportDetailInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @isHasReplaceSupportDetailInfo ", e);
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
    public void getCouponInfo(final String storeid, final Item searchedItem, final String companyId,
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
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getCouponInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getCouponInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
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
    public void getPremiumitemInfo(final String storeid, final Item searchedItem, final String companyId,
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
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPremiumitemInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getPremiumitemInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @getPremiumitemInfo ", e);
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
        String functionName = "SQLServerItemDAO.isHasPriceMMInfo";
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

//                searchedItem.setDecisionPrice1(result.getDouble(result.findColumn("DecisionPrice1")));
//                searchedItem.setDecisionPrice2(result.getDouble(result.findColumn("DecisionPrice2")));
//                searchedItem.setDecisionPrice3(result.getDouble(result.findColumn("DecisionPrice3")));
                
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
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the prom information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @isHasPriceMMInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the prom information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @isHasPriceMMInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the prom information.\n" + e.getMessage());
            throw new DaoException("Exception: @isHasPriceMMInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }
        return searchedItem;
    }

    /**
     * get the item by param
     * @param storeId 店舗コード
     * @param companyId 会社コード
     * @return object itemInfo
     * @throws DaoException The exception thrown when error occurred.
     */
    public Item getItemBypluCode(String storeId, String plucode, String companyId,String businessDate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("StoreID", storeId).println("PLU", plucode)
        .println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        Item searchedItem = null;
        String functionName = "SQLServerItemDAO.getItemBypluCode";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-item-price"));
            select.setString(SQLStatement.PARAM1, storeId);
            select.setString(SQLStatement.PARAM2, companyId);
            select.setString(SQLStatement.PARAM3, plucode);
            select.setString(SQLStatement.PARAM4, businessDate);
            result = select.executeQuery();
            if (result.next()) {
                searchedItem = new Item();
                searchedItem.setItemId(result.getString(result.findColumn("MdInternal"))); // PK
                searchedItem.setCompanyId(result.getString(result.findColumn("CompanyId")));
                searchedItem.setStoreId(result.getString(result.findColumn("StoreId")));

                if (result.getObject(result.findColumn("NewPrice")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("NewPrice")));
                    searchedItem.setOldPrice(result.getDouble(result.findColumn("OldPrice")));
                    searchedItem.setSalesPriceFrom("2");
                } else if (result.getObject(result.findColumn("NewPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("SalesPrice")));
                    searchedItem.setSalesPriceFrom("1");
                } else if (result.getObject(result.findColumn("NewPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice")) == null
                        && result.getObject(result.findColumn("SalesPrice1")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("SalesPrice1")));
                    searchedItem.setSalesPriceFrom("0");
                } else {
                    searchedItem.setRegularSalesUnitPrice(0);
                }
            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemBypluCode ", sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the item information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getItemBypluCode ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the item information.\n" + e.getMessage());
            throw new DaoException("Exception: @getItemBypluCode ", e);
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
            select.setString(SQLStatement.PARAM3, itemType);
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
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the item information.\n" + e.getMessage());
            throw new DaoException("Exception: @getPickListItems ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(itemTypes);
        }
        return items;
    }

    @Override
    public Item getItemByApiData(String plucode, String companyId) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("PLU", plucode).println("CompanyId", companyId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        Item searchedItem = null;
        String functionName = "SQLServerItemDAO.getItemByApiData";
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-item-byapidata"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, plucode);

            result = select.executeQuery();

            if (result.next()) {
                searchedItem = new Item();
                searchedItem.setItemId(result.getString(result.findColumn("MdInternal"))); // PK

                // get item description
                Description description = new Description();
                description.setJa(result.getString(result.findColumn("MdNameLocal")));
                searchedItem.setDescription(description);

                searchedItem.setTaxType(result.getString(result.findColumn("TaxType")));
                searchedItem.setTaxRate(result.getInt(result.findColumn("TaxRate")));
                searchedItem.setSubNum1(result.getInt(result.findColumn("SubNum1")));
                searchedItem.setSubNum2(result.getInt(result.findColumn("SubNum2")));

                if (result.getObject(result.findColumn("SalesPrice1")) != null) {
                    searchedItem.setRegularSalesUnitPrice(result.getDouble(result.findColumn("SalesPrice1")));
                    searchedItem.setSalesPriceFrom("0");
                } else {
                    searchedItem.setRegularSalesUnitPrice(0);
                }
                
                if (result.getObject(result.findColumn("SalesPrice1")) != null) {
                    searchedItem.setPluPrice(result.getDouble(result.findColumn("SalesPrice1")));
                } else {
                    searchedItem.setPluPrice(0);
                }
                searchedItem.setMd01(result.getString(result.findColumn("Md01")));
                if (result.getObject(result.findColumn("OrgSalesPrice1")) == null) {
                    searchedItem.setOrgSalesPrice1(0);
                } else {
                    searchedItem.setOrgSalesPrice1(result.getDouble(result.findColumn("OrgSalesPrice1")));
                }
                searchedItem.setDepartment(result.getString(result.findColumn("Dpt")));
                searchedItem.setDiscountType(result.getString(result.findColumn("DiscountType")));
                searchedItem.setMd02(result.getString(result.findColumn("Md02")));
                searchedItem.setMd03(result.getString(result.findColumn("Md03")));
                searchedItem.setMd04(result.getString(result.findColumn("Md04")));
                searchedItem.setMd05(result.getString(result.findColumn("Md05")));
                searchedItem.setMd06(result.getString(result.findColumn("Md06")));
                searchedItem.setMd07(result.getString(result.findColumn("Md07")));
                searchedItem.setMd08(result.getString(result.findColumn("Md08")));
                searchedItem.setMd09(result.getString(result.findColumn("Md09")));
                searchedItem.setMd10(result.getString(result.findColumn("Md10")));
                searchedItem.setMd11(result.getString(result.findColumn("Md11")));
                searchedItem.setMd12(result.getString(result.findColumn("Md12")));
                searchedItem.setMd13(result.getString(result.findColumn("Md13")));
                searchedItem.setMd14(result.getString(result.findColumn("Md14")));
                searchedItem.setMd15(result.getString(result.findColumn("Md15")));
                searchedItem.setMd16(result.getString(result.findColumn("Md16")));
                searchedItem.setMdType(result.getString(result.findColumn("MdType")));
                searchedItem.setSku(result.getString(result.findColumn("Sku")));
                searchedItem.setMdNameLocal(result.getString(result.findColumn("MdNameLocal")));
                searchedItem.setMdKanaName(result.getString(result.findColumn("MdKanaName")));
                searchedItem.setSalesPrice2(result.getLong(result.findColumn("SalesPrice2")));
                searchedItem.setPaymentType(result.getInt(result.findColumn("PaymentType")));
                searchedItem.setSubCode1(result.getString(result.findColumn("SubCode1")));
                searchedItem.setSubCode2(result.getString(result.findColumn("SubCode2")));
                searchedItem.setSubCode3(result.getString(result.findColumn("SubCode3")));
                searchedItem.setMdVender(result.getString(result.findColumn("MdVender")));
                searchedItem.setPaymentType(result.getInt(result.findColumn("PaymentType")));

                searchedItem.setCostPrice1(result.getLong(result.findColumn("CostPrice1")));
                searchedItem.setMakerPrice(result.getLong(result.findColumn("MakerPrice")));
                searchedItem.setConn1(result.getString(result.findColumn("Conn1")));
                searchedItem.setConn2(result.getString(result.findColumn("Conn2")));

                searchedItem.setLine(result.getString(result.findColumn("Line")));
                searchedItem.setItemClass(result.getString(result.findColumn("Class")));
                // Compute the Actual Sales Price
                searchedItem.setActualSalesUnitPrice(searchedItem.getRegularSalesUnitPrice());
                searchedItem.setColorkananame(result.getString(result.findColumn("Colorkananame")));
                searchedItem.setSizeKanaName(result.getString(result.findColumn("SizeKanaName")));
                
                //searchedItem.setBrandName(result.getString(result.findColumn("BrandName")));

                // // 特売管理
                // if (!isHasPromDetailInfo(actualStoreid, searchedItem,
                // companyId, bussinessDate)) {
                // // バンドルミックス
                // if (!isHasPriceMMInfo(actualStoreid, searchedItem, companyId,
                // bussinessDate)) {
                // // 買替サポート
                // isHasReplaceSupportDetailInfo(actualStoreid, searchedItem,
                // companyId, bussinessDate);
                // }
                // }
                // // 割引券発行管理
                // getCouponInfo(actualStoreid, searchedItem, companyId,
                // bussinessDate);
                // // プレミアム商品
                // getPremiumitemInfo(actualStoreid, searchedItem, companyId,
                // bussinessDate);
                // //QRcode
                // getQrCodeInfo(actualStoreid, searchedItem, companyId,
                // bussinessDate);

            } else {
                tp.println("Item not found.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the item information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getItemByApiData ", sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the item information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getItemByApiData ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the item information.\n" + e.getMessage());
            throw new DaoException("Exception: @getItemByApiData ", e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(searchedItem);
        }

        return searchedItem;
    }
    
    
    
}
