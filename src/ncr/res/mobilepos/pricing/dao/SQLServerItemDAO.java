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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

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
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.resource.PromotionConstants;
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

	private static final String COLUMN_OF_DPT = "ColumnOfDpt";
	private static final String COLUMN_OF_LINE = "ColumnOfLine";
	private static final String COLUMN_OF_CLASS = "ColumnOfClass";
	private static final String COLUMN_OF_PLU = "ColumnOfPlu";

	//default 税率カラム値設定
	private String dptTaxId = "dpt.SubNum5 dptSubNum5";
	private String clsTaxId = "classInfo.SubNum1 clsSubNum1";
	private String lineTaxId = "lineInfo.SubNum1 lineSubNum1";
	private String pluTaxId = "plu.SubNum1 pluSubNum1";

	//default 型番カラム値設定
	private String defaultComstdName = "plu.MdNameLocal1 ComstdName";
	public static final String BarcodePrefix = "20";
	public static final String[] BarcodePrefixArr = {"02","21","24","25"};
	
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
     *            The bussinessDate
     * @param companyId
     *            The companyId
     * @param priceIncludeTax
     *            The priceIncludeTax
     * @param mapTaxId
     *            The mapTaxId
     * @return The details of the particular item
     * @throws DaoException
     *             Exception thrown when getting the item information failed.
     */
    @Override
    public Item getItemByPLU(String storeid, String pluCode, String companyId, int priceIncludeTax, String bussinessDate, Map<String, String> mapTaxId, String comstdName)
            throws DaoException {
        return this.getItemByPLUWithStoreFixation(storeid, pluCode, true, companyId, priceIncludeTax,bussinessDate,mapTaxId,comstdName);
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
    * @param companyId
    *            The companyId
    * @param priceIncludeTax
    *            The priceIncludeTax
    * @param mapTaxId
    *            The mapTaxId
    * @return The details of the particular item
    * @throws DaoException
    *             Exception thrown when getting the item information failed.
    */
    private Item getItemByPLUWithStoreFixation(final String storeid, final String pluCode, final boolean storeFixation,
            final String companyId, final int priceIncludeTax, final String bussinessDate, final Map<String, String> mapTaxId,
            String comstdName) throws DaoException {

        tp.methodEnter("getItemByPLUWithStoreFixation").println("StoreID", storeid).println("PLU", pluCode)
                .println("IsStoreFixation", storeFixation).println("PriceIncludeTax", priceIncludeTax).println("BussinessDate", bussinessDate)
                .println("MapTaxId", mapTaxId).println("ComstdName", comstdName);

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
		} else if (Arrays.asList(BarcodePrefixArr).contains(pluCode.substring(0, 2))){
			mdInternal = pluCode.substring(0,7) + "00000" + getCheckSum(pluCode.substring(0,7) + "00000");
		} else if (pluCode.substring(0, 2).equals(BarcodePrefix)){
			mdInternal = pluCode.substring(0,8) + "0000" + getCheckSum(pluCode.substring(0,8) + "0000");
		} else {
			mdInternal = pluCode;
		}
        
        tp.println("mdInternal", mdInternal);

        //型番設定
        if (!StringUtility.isNullOrEmpty(comstdName)) {
        	defaultComstdName = "plu." + comstdName + " ComstdName";
        }
        // 税率区分の情報を取得する
        getSaleTaxIdInfo(mapTaxId);

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            String query = String.format(sqlStatement.getProperty("get-item"), defaultComstdName, pluTaxId, clsTaxId ,dptTaxId ,lineTaxId);
            select = connection.prepareStatement(query);
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

                if (result.getObject(result.findColumn("TaxType")) != null) {
                	searchedItem.setTaxType(result.getString(result.findColumn("TaxType")));
                	searchedItem.setPluTaxType(result.getString(result.findColumn("TaxType")));
                }
                searchedItem.setDptTaxType(result.getString(result.findColumn("dptTaxType")));
                searchedItem.setClsTaxType(result.getString(result.findColumn("clsTaxType")));
                searchedItem.setLineTaxType(result.getString(result.findColumn("lineTaxType")));
                searchedItem.setTaxRate(result.getInt(result.findColumn("TaxRate")));
                searchedItem.setSubNum1(result.getInt(result.findColumn("SubNum1")));
                searchedItem.setSubNum2(result.getInt(result.findColumn("SubNum2")));
                searchedItem.setSubNum3(result.getInt(result.findColumn("SubNum3")));

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

                searchedItem.setPluSubNum1(result.getString(result.findColumn(pluTaxId.split(" ")[1])));
                searchedItem.setClassInfoSubNum1(result.getString(result.findColumn(clsTaxId.split(" ")[1])));
                searchedItem.setDptSubNum5(result.getString(result.findColumn(dptTaxId.split(" ")[1])));
                searchedItem.setLineInfoSubNum1(result.getString(result.findColumn(lineTaxId.split(" ")[1])));
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
                //DRUG add start by wl 20191113
                if (result.getObject(result.findColumn("PluTaxFreeFlag")) != null) {
                	searchedItem.setTaxExemptFlag(result.getString(result.findColumn("PluTaxFreeFlag")));
                } else {
                	searchedItem.setTaxExemptFlag(result.getString(result.findColumn("TaxFreeFlag")));
                }
                searchedItem.setAgeRestrictedFlag(result.getString(result.findColumn("AgeRestrictedFlag")));
                if (result.getObject(result.findColumn("DisplayEndDate")) != null) {
                    Date today = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date endDate = sdf.parse(result.getString(result.findColumn("DisplayEndDate")));
                    if (today.before(endDate)) {
                        searchedItem.setRecallFlag(result.getString(result.findColumn("RecallFlag")));
                    } else {
                        searchedItem.setRecallFlag("0");
                    }
                } else {
                    searchedItem.setRecallFlag(result.getString(result.findColumn("RecallFlag")));
                }
                searchedItem.setPharmaceuticalFlag(result.getString(result.findColumn("PharmaceuticalFlag")));
                searchedItem.setCountLimitFlag(result.getString(result.findColumn("CountLimitFlag")));
                searchedItem.setCountLimit(result.getString(result.findColumn("CountLimit")));
                searchedItem.setCertificatePrintFlag(result.getString(result.findColumn("CertificatePrintFlag")));
                searchedItem.setSelfFlag(result.getString(result.findColumn("SelfFlag")));
                searchedItem.setDrugType(result.getString(result.findColumn("DrugType")));
                searchedItem.setTransferWriteType(result.getString(result.findColumn("TransferWriteType")));
                searchedItem.setDrugActType(result.getString(result.findColumn("DrugActionArea")));
                searchedItem.setTransferActType(result.getString(result.findColumn("TransferActionArea")));
                searchedItem.setCertificateNo(result.getString(result.findColumn("CertificateNo")));
                searchedItem.setClsAgeRestrictedFlag(result.getString(result.findColumn("ClsAgeRestrictedFlag")));
                searchedItem.setLineAgeRestrictedFlag(result.getString(result.findColumn("LineAgeRestrictedFlag")));
                searchedItem.setDptAgeRestrictedFlag(result.getString(result.findColumn("DptAgeRestrictedFlag")));
                searchedItem.setCallInReason(result.getString(result.findColumn("CallInReason")));
                searchedItem.setSelfMedicationMark(result.getString(result.findColumn("SelfMedicationMark")));
                searchedItem.setComstdName(result.getString(result.findColumn("ComstdName")));
                //DRUG add end by wl 20191113
                //MUJI add by wgq start
                searchedItem.setFoodFlag(result.getString(result.findColumn("SubCode1")));
                searchedItem.setSaleRestrictedFlag(result.getString(result.findColumn("SubCode3")));
                searchedItem.setSelfSaleRestrictedFlag(result.getString(result.findColumn("SubCode4")));
                searchedItem.setOrderSaleFlag(result.getString(result.findColumn("SubCode2")));
                searchedItem.setBestBeforePeriod(result.getString(result.findColumn("BestBeforePeriod")));
                searchedItem.setSalePeriod(result.getString(result.findColumn("SalesPeriod")));
                //MUJI add by wgq end
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
        tp.methodEnter("getItemNameFromPluName")
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
    	tp.methodEnter("isHasPromDetailInfoList")
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
     * 特売管理マスタ 情報取得
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    public boolean isHasPromDetailList(final List<PricePromInfo> pricePromInfo, final Item searchedItem) throws DaoException {
    	tp.methodEnter("isHasPromDetailList")
        .println("PricePromInfo", pricePromInfo)
        .println("searchedItem", searchedItem);
    	
    	boolean isHaveValue = false;
    	List<PricePromInfo> cleanList = cleanPricePromList(pricePromInfo,searchedItem, isHaveValue);
    	
    	if (null != cleanList && cleanList.size() > 0) {
	        searchedItem.setPricePromList(cleanList);
    	}
    	return isHaveValue;
    }
    
		//　　　　                  値引区分　会員フラグ  　　レジ制御                      商品抽出時(item_entry)　　　　　　　　優先順位
		//1.通常の特売　　　　  0       0                                                  一件のみ(安め優先)　　　　　　　　　　       １(安め優先)　
		//2.会員対象の特売  　0       1　　　　　　　　　　　                        一件のみ(安め優先)　　　　　　　　　　        １(会員登録あり＆安め優先)　
		//3.単品クーポン　　　    1       1                                                 複数件あり(クーポン選択画面で判断)　　    ２
		//4.一括割引               1       0  1～4 基本ポップアップあり  　　 一件のみ(理由コードで判断)　　　　　　        ３
    private List<PricePromInfo> cleanPricePromList(List<PricePromInfo> priceMMInfoList, Item searchedItem, boolean isHaveValue){
    	List<PricePromInfo> list = new ArrayList<>();
    	Map<String, PricePromInfo> map = new HashMap<String, PricePromInfo>();
    	for(PricePromInfo info : priceMMInfoList) {
    		//del by wl start
//    		if(PromotionConstants.DISCOUNT_CLASS_3.equals(info.getDiscountClass())) {
//    			list.add(info);
//    			continue;
//    		}
    		//del by wl end
    		if(info.getSubCode2() == PromotionConstants.NORMAL_SALE_INT && info.getSubNum1() == PromotionConstants.NORMAL_SALE_INT) {
    			choicePrimeInfo(map, info, searchedItem, "TM");
    		} else if(info.getSubCode2() == PromotionConstants.NORMAL_SALE_INT && info.getSubNum1() == PromotionConstants.CUSTOMER_SALE_INT) {
    			choicePrimeInfo(map, info, searchedItem, "HY");
    		}else if(info.getSubCode2() == PromotionConstants.DISCOUNT_FLAG_1 && info.getSubNum1() == PromotionConstants.CUSTOMER_SALE_INT) {
    			list.add(info);
    		} else {
    			String line = searchedItem.getLine();
    			String sbc3 = info.getSubCode3();
				boolean isYk = false;
    			if(StringUtility.isNullOrEmpty(sbc3, line)) {
    				isYk = true;
    			}else {
    				String[] sbcArrary = sbc3.split(",");
    				for(String str : sbcArrary) {
    					if(str.equals(line)) {
    						isYk = true;
    						break;
    					}
    				}
    			}
    			if(isYk) {
    				choicePrimeInfo(map, info, searchedItem, "YK");
    			}
    		}
    	}
    	if(map.size() > 0) {
    		boolean hasTM = false;
    		if(map.containsKey("TM") && map.containsKey("HY")) {
    			hasTM = true;
    			if(discountPrice(map.get("TM"), searchedItem) >= discountPrice(map.get("HY"), searchedItem)) {
    				isHaveValue = true;
    				list.add(map.get("TM"));
    			}else {
    				isHaveValue = true;
    				list.add(map.get("TM"));
    				list.add(map.get("HY"));
    			}
    		} 
    		if(map.containsKey("TM") && map.containsKey("TM1")) {
    			isHaveValue = true;
    			list.add(map.get("TM"));
    			list.add(map.get("TM1"));
    		}
    		if(map.containsKey("TM") && !map.containsKey("TM1") && !map.containsKey("HY")) {
    			isHaveValue = true;
    			hasTM = true;
    			list.add(map.get("TM"));
    		}
    		if(!map.containsKey("TM") && map.containsKey("HY")) {
    			isHaveValue = true;
    			list.add(map.get("HY"));
    		}
    		
    		if(map.containsKey("YK") && !hasTM) {
    			list.add(map.get("YK"));
    		}
    	}
    	return list;
    }
    
    private void choicePrimeInfo(Map<String, PricePromInfo> map, PricePromInfo info, Item searchedItem, String key) {
		if(map.containsKey(key)) {
			PricePromInfo mapInfo = map.get(key);
			if("YK".equals(key)) {
				if(mapInfo.getSubCode5() != 0 && info.getSubCode5() < mapInfo.getSubCode5()) {
					map.put(key, info);
				}
			} else {
				String[] discount = {PromotionConstants.DISCOUNT_CLASS_1, PromotionConstants.DISCOUNT_CLASS_2};
				String mapInfoDiscountClass = mapInfo.getDiscountClass();
				String infoDiscountClass = info.getDiscountClass();
				if (!mapInfoDiscountClass.equals(infoDiscountClass)) {
					if (map.size() == 1) {
						if (Arrays.asList(discount).indexOf(mapInfoDiscountClass) < 0 && Arrays.asList(discount).indexOf(infoDiscountClass) >= 0) {
							map.put(key + "1", info);
						} else if(Arrays.asList(discount).indexOf(mapInfoDiscountClass) >= 0 && Arrays.asList(discount).indexOf(infoDiscountClass) < 0) { 
							map.put(key, info);
							map.put(key + "1", mapInfo);
						} else {
							if(discountPrice(mapInfo, searchedItem) < discountPrice(info, searchedItem)) {
								map.put(key, info);
							}
						}
					} else {
						if (Arrays.asList(discount).indexOf(infoDiscountClass) >= 0) {
							PricePromInfo mapInfoWithDiscount = map.get(key + "1");
							PricePromInfo mapInfoWithSepcialPrice = map.get(key);
							double actualPrice = searchedItem.getActualSalesUnitPrice();
							double specialPriceMapInfo = discountPrice(mapInfoWithSepcialPrice, searchedItem);
							double salePrice = actualPrice - specialPriceMapInfo; 
							double discountedMapInfoPrice = discountPriceByAmount(mapInfoWithDiscount, salePrice);
							double discountedInfoPrice = discountPriceByAmount(info,  salePrice);
							if (discountedMapInfoPrice > discountedInfoPrice) {
								map.put(key + "1", info);
							}
						} else {
							if (discountPrice(mapInfo, searchedItem) < discountPrice(info, searchedItem)) {
								map.put(key, info);
							}
						}
					}
				} else {
					if(discountPrice(mapInfo,searchedItem) < discountPrice(info,searchedItem)) {
						map.put(key, info);
					}	
				}
			}
		}else {
			map.put(key, info);
		}
    }
    
    private double discountPrice(PricePromInfo info, Item searchedItem) {
    	double price = 0;
    	if(PromotionConstants.DISCOUNT_CLASS_1.equals(info.getDiscountClass())) {
    		price = searchedItem.getActualSalesUnitPrice() * (info.getDiscountRate() / 100);
    	}
    	if(PromotionConstants.DISCOUNT_CLASS_2.equals(info.getDiscountClass())) {
    		price = info.getDiscountAmt();
    	}
    	//add by wl start
    	if(PromotionConstants.DISCOUNT_CLASS_3.equals(info.getDiscountClass())) {
    		price = searchedItem.getActualSalesUnitPrice() - info.getSalesPrice();
    	}
    	//add by wl end
    	
    	return price;
    }
    
    private double discountPriceByAmount(PricePromInfo info, double itemPrice) {
    	double price = 0;
    	if (PromotionConstants.DISCOUNT_CLASS_1.equals(info.getDiscountClass())) {
    		price = itemPrice - (itemPrice * (info.getDiscountRate() / 100));
    	}
    	if (PromotionConstants.DISCOUNT_CLASS_2.equals(info.getDiscountClass())) {
    		price = itemPrice - info.getDiscountAmt();
    	}
    	return price;
    }
    
    /**
     * 緊急売変 情報取得
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
	@Override
	public boolean isHasPriceUrgentInfo(final PriceUrgentInfo priceUrgentInfo, final Item searchedItem) throws DaoException {
    	tp.methodEnter("isHasPriceUrgentInfo")
        .println("PricePromInfo", priceUrgentInfo)
        .println("searchedItem", searchedItem);
    	
    	boolean isHaveValue = false;
    	
    	if (null != priceUrgentInfo) {
	        isHaveValue = true;
	        searchedItem.setPriceUrgentInfo(priceUrgentInfo);
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
    	tp.methodEnter("isHasPriceMMInfoList")
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
     * MM 情報取得
     * @param searchedItem the Item
     * @throws DaoException   Exception thrown when getting the item information failed.
     */
    public boolean isHasPriceMMDetailList(final List<PriceMMInfo> priceMMInfo, final Item searchedItem) throws DaoException {
    	tp.methodEnter("isHasPriceMMDetailList")
        .println("PricePromInfo", priceMMInfo)
        .println("searchedItem", searchedItem);
    	
    	boolean isHaveValue = false;
    	
    	if (null != priceMMInfo && priceMMInfo.size() > 0) {
	        isHaveValue = true;
	        searchedItem.setPriceMMInfoList(priceMMInfo);
    	}
    	return isHaveValue;
    }
    
    // lilx 20191206 add end 
    
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
        tp.methodEnter("isHasReplaceSupportDetailInfo")
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
        tp.methodEnter("getCouponInfo")
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
        tp.methodEnter("getPremiumitemInfo")
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
        tp.methodEnter("getMixMatchInfo")
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
        tp.methodEnter("getItemBypluCode").println("StoreID", storeId).println("PLU", plucode)
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
        String functionName = "getPickListItems";
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
        tp.methodEnter("getItemByApiData").println("PLU", plucode).println("CompanyId", companyId);

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

    /**
	 * 税率区分の情報を取得する
	 *
	 * @param mapReturn
	 */
	private void getSaleTaxIdInfo(Map<String, String> mapTaxId) {
		if (mapTaxId != null ) {
			for (String key : mapTaxId.keySet()) {
				if ((COLUMN_OF_DPT).equals(key)) {
					if (!StringUtility.isNullOrEmpty(mapTaxId.get(COLUMN_OF_DPT))) {
						dptTaxId = "dpt." + mapTaxId.get(COLUMN_OF_DPT) + " dpt" + mapTaxId.get(COLUMN_OF_DPT);
					}
				}
				if ((COLUMN_OF_CLASS).equals(key)) {
					if (!StringUtility.isNullOrEmpty(mapTaxId.get(COLUMN_OF_CLASS))) {
						clsTaxId = "classInfo." + mapTaxId.get(COLUMN_OF_CLASS) + " classInfo"
								+ mapTaxId.get(COLUMN_OF_CLASS);
					}
				}
				if ((COLUMN_OF_LINE).equals(key)) {
					if (!StringUtility.isNullOrEmpty(mapTaxId.get(COLUMN_OF_LINE))) {
						lineTaxId = "lineInfo." + mapTaxId.get(COLUMN_OF_LINE) + " lineInfo" + mapTaxId.get(COLUMN_OF_LINE);
					}
				}
				if ((COLUMN_OF_PLU).equals(key)) {
					if (!StringUtility.isNullOrEmpty(mapTaxId.get(COLUMN_OF_PLU))) {
						pluTaxId = "plu." + mapTaxId.get(COLUMN_OF_PLU) + " plu" + mapTaxId.get(COLUMN_OF_PLU);
					}
				}
			}
		}
		if (StringUtility.isNullOrEmpty(dptTaxId)) {
			dptTaxId = "dpt.SubNum5 dptSubNum5";
		}
		if (StringUtility.isNullOrEmpty(clsTaxId)) {
			clsTaxId = "classInfo.SubNum1 clsSubNum1";
		}
		if (StringUtility.isNullOrEmpty(lineTaxId)) {
			lineTaxId = "lineInfo.SubNum1 lineSubNum1";
		}
		if (StringUtility.isNullOrEmpty(pluTaxId)) {
			pluTaxId = "plu.SubNum1 pluSubNum1";
		}
	}
    /**
	 * チェックデジット計算する
	 *
	 * @param mapReturn
	 */
	private String getCheckSum(String data) {
        int evenSum = 0,
            baseSum = 0,
            sum = 0;
        for (int i = 0; i < data.length(); i++) {
            if (i % 2 == 0) {
                baseSum += Integer.parseInt(String.valueOf(data.charAt(i)));
            } else {
                evenSum += Integer.parseInt(String.valueOf(data.charAt(i)));
            }
        }
        if (data.length() == 11) {
            sum = baseSum * 3 + evenSum;
        } else {
            sum = evenSum * 3 + baseSum;
        }
        // return (sum.charAt(sum.length - 1) == '0' ? 0 : 10 - parseInt(sum.charAt(sum.length - 1))).toString();
        return Integer.toString((10 - sum % 10) % 10);
    }
}
