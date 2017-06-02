package ncr.res.mobilepos.point.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.point.model.CashingUnit;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.TranPointRate;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.helper.DateFormatUtility;


public class SQLServerPointDAO extends AbstractDao implements IPointDAO {
    /**
     * DBManager instance, provides database connection object.
     */
    private DBManager dbManager;
    /**
     * Logger instance, logs error and information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * program name of the class.
     */
    private static final String PROG_NAME = "PointDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * constant. the falg
     */
    private static final String FLAG_ON = "1";
    private static final String FLAG_OFF = "0";
    
    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerPointDAO() throws Exception {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves DBManager.
     * 
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }
    
    @Override
    public List<ItemPointRate> getItemPointRate(String companyId, String storeId,
            String businessdate, String deptcode, String groupcode, String brandId, String sku) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDate", businessdate)
            .println("deptCode", deptcode)
            .println("groupCode", groupcode)
            .println("brandId", brandId)
            .println("sku", sku);
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<ItemPointRate> itemPointRateList = new ArrayList<ItemPointRate>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            
            // for normal item point rate
            statement = connection.prepareStatement(
                        sqlStatement.getProperty("get-point-item-rate"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, businessdate);
            statement.setString(SQLStatement.PARAM4, deptcode);
            result = statement.executeQuery();
            while(result.next())  {
                ItemPointRate itemPointRate = new ItemPointRate();
                itemPointRate.setType("0");
                itemPointRate.setCompanyId(companyId);
                itemPointRate.setStoreId(storeId);
                itemPointRate.setPointFlag(result.getString("PointFlag"));
                itemPointRate.setBasePrice(result.getString("BasePrice"));
                itemPointRate.setRecordId(result.getString("RecordId"));
                itemPointRate.setBasePoint(result.getString("BasePoint"));
                itemPointRate.setDptSettingFlag("0");
                itemPointRate.setItemSettingFlag("0");
                itemPointRate.setCardSettingFlag(result.getString("CardSettingFlag"));
                itemPointRate.setStoreSettingFlag(result.getString("StoreSettingFlag"));
                itemPointRate.setCardClassId(result.getString("CardClassId"));
                itemPointRateList.add(itemPointRate);
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get item point rate.", e);
            throw new Exception("SQLException: @SQLServerPointDAO."
            		+ functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(itemPointRateList);
        }

        Connection connection2 = null;
        PreparedStatement statement2 = null;
        ResultSet result2 = null;
        try {
            connection2 = dbManager.getConnection();
            SQLStatement sqlStatement2 = SQLStatement.getInstance();
            
            // for normal item point rate
            statement2 = connection2.prepareStatement(
                        sqlStatement2.getProperty("get-point-dpt-campaign-rate"));
            statement2.setString(SQLStatement.PARAM1, companyId);
            statement2.setString(SQLStatement.PARAM2, storeId);
            statement2.setString(SQLStatement.PARAM3, businessdate);
            statement2.setString(SQLStatement.PARAM4, deptcode);
            result2 = statement2.executeQuery();
            while(result2.next())  {
                if (IsCampaignEnabled(businessdate,
                result2.getString("TargetDaySettingFlag"), result2.getString("TargetDay1"), result2.getString("TargetDay2"),
                result2.getString("TargetDay3"), result2.getString("TargetDay4"), result2.getString("TargetDay5"),
                result2.getString("TargetDay6"), result2.getString("TargetDay7"), result2.getString("TargetDay8"), result2.getString("TargetDay9"),
                result2.getString("DayOfWeekSettingFlag"), result2.getString("DayOfWeekMonFlag"), result2.getString("DayOfWeekTueFlag"),
                result2.getString("DayOfWeekWedFlag"), result2.getString("DayOfWeekThuFlag"), result2.getString("DayOfWeekFriFlag"),
                result2.getString("DayOfWeekSatFlag"), result2.getString("DayOfWeekSunFlag"))) {
                    ItemPointRate itemPointRate = new ItemPointRate();
                    itemPointRate.setType("1");
                    itemPointRate.setCompanyId(companyId);
                    itemPointRate.setStoreId(storeId);
                    itemPointRate.setPointFlag("1");
                    itemPointRate.setBasePrice(result2.getString("BasePrice"));
                    itemPointRate.setRecordId(result2.getString("RecordId"));
                    itemPointRate.setBasePoint(result2.getString("BasePoint"));
                    itemPointRate.setPointTender(result2.getString("PointTender"));
                    itemPointRate.setTenderSettingFlag(result2.getString("TenderSettingFlag"));
                    itemPointRate.setDptSettingFlag(result2.getString("DptSettingFlag"));
                    itemPointRate.setItemSettingFlag(result2.getString("ItemSettingFlag"));
                    itemPointRate.setCardSettingFlag(result2.getString("CardSettingFlag"));
                    itemPointRate.setStoreSettingFlag(result2.getString("TargetStoreType"));
                    itemPointRate.setCardClassId(result2.getString("CardClassId"));
                    itemPointRateList.add(itemPointRate);
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get item point rate2.", e);
            throw new Exception("SQLException: @SQLServerPointDAO."
            		+ functionName, e);
        } finally {
            closeConnectionObjects(connection2, statement2, result2);
            tp.methodExit(itemPointRateList);
        }

        Connection connection3 = null;
        PreparedStatement statement3 = null;
        ResultSet result3 = null;
        try {
        	connection3 = dbManager.getConnection();
            SQLStatement sqlStatement3 = SQLStatement.getInstance();
            
            statement3 = connection3.prepareStatement(
                        sqlStatement3.getProperty("get-point-item-campaign-rate"));
            statement3.setString(SQLStatement.PARAM1, companyId);
            statement3.setString(SQLStatement.PARAM2, storeId);
            statement3.setString(SQLStatement.PARAM3, businessdate);
            statement3.setString(SQLStatement.PARAM4, deptcode);
            statement3.setString(SQLStatement.PARAM5, groupcode);
            statement3.setString(SQLStatement.PARAM6, brandId);
            statement3.setString(SQLStatement.PARAM7, sku);
            result3 = statement3.executeQuery();
            while(result3.next())  {
                if (IsCampaignEnabled(businessdate,
                result3.getString("TargetDaySettingFlag"), result3.getString("TargetDay1"), result3.getString("TargetDay2"),
                result3.getString("TargetDay3"), result3.getString("TargetDay4"), result3.getString("TargetDay5"),
                result3.getString("TargetDay6"), result3.getString("TargetDay7"), result3.getString("TargetDay8"), result3.getString("TargetDay9"),
                result3.getString("DayOfWeekSettingFlag"), result3.getString("DayOfWeekMonFlag"), result3.getString("DayOfWeekTueFlag"),
                result3.getString("DayOfWeekWedFlag"), result3.getString("DayOfWeekThuFlag"), result3.getString("DayOfWeekFriFlag"),
                result3.getString("DayOfWeekSatFlag"), result3.getString("DayOfWeekSunFlag"))) {
                    if (IsCampaignItemEnabled(result3.getString("ItemSettingFlag"), result3.getString("GroupTargetType"), result3.getString("GroupIdStart"), result3.getString("GroupIdEnd"), groupcode,
                        result3.getString("DptTargetType"), result3.getString("DptStart"), result3.getString("DptEnd"), deptcode,
                        result3.getString("BrandTargetType"), result3.getString("BrandIdStart"), result3.getString("BrandIdEnd"), brandId,
                        result3.getString("SkuTargetType"), result3.getString("SkuStart"), result3.getString("SkuEnd"), sku, result3.getString("TargetId"), result3.getString("TargetType"))) {
                        ItemPointRate itemPointRate = new ItemPointRate();
                        itemPointRate.setType("2");
                        itemPointRate.setCompanyId(companyId);
                        itemPointRate.setStoreId(storeId);
                        itemPointRate.setPointFlag("1");
                        itemPointRate.setBasePrice(result3.getString("BasePrice"));
                        itemPointRate.setRecordId(result3.getString("RecordId"));
                        itemPointRate.setBasePoint(result3.getString("BasePoint"));
                        itemPointRate.setPointTender(result3.getString("PointTender"));
                        itemPointRate.setTenderSettingFlag(result3.getString("TenderSettingFlag"));
                        itemPointRate.setTenderType(result3.getString("TenderType"));
                        itemPointRate.setTenderId(result3.getString("TenderId"));
                        itemPointRate.setBasePointCash(result3.getString("BasePointCash"));
                        itemPointRate.setBasePointAffiliate(result3.getString("BasePointAffiliate"));
                        itemPointRate.setBasePointNonAffiliate(result3.getString("BasePointNonAffiliate"));
                        itemPointRate.setDptSettingFlag(result3.getString("DptSettingFlag"));
                        itemPointRate.setItemSettingFlag(result3.getString("ItemSettingFlag"));
                        itemPointRate.setCardSettingFlag(result3.getString("CardSettingFlag"));
                        itemPointRate.setStoreSettingFlag(result3.getString("TargetStoreType"));
                        itemPointRate.setCardClassId(result3.getString("CardClassId"));
                        itemPointRateList.add(itemPointRate);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get item point rate3.", e);
            throw new Exception("SQLException: @SQLServerPointDAO."
            		+ functionName, e);
        } finally {
            closeConnectionObjects(connection3, statement3, result3);
            tp.methodExit(itemPointRateList);
        }
        return itemPointRateList;
    }

    @Override
    public List<TranPointRate> getTranPointRate(String companyId, String storeId,  String businessdate) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDate", businessdate);
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<TranPointRate> tranPointRateList = new ArrayList<TranPointRate>();
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                        sqlStatement.getProperty("get-point-tran-rate"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, businessdate);

            result = statement.executeQuery();
            while(result.next())  {
                TranPointRate tranPointRate = new TranPointRate();
                tranPointRate.setType("0");
                tranPointRate.setCompanyId(companyId);
                tranPointRate.setStoreId(storeId);
                tranPointRate.setPointFlag(result.getString("PointFlag"));
                tranPointRate.setBasePrice(result.getString("BasePrice"));
                tranPointRate.setBasePoint(result.getString("BasePoint"));
                tranPointRate.setCashingUnit(result.getString("CashingUnit"));
                tranPointRate.setRecordId(result.getString("RecordId"));
                tranPointRate.setBasePointCash(result.getString("BasePointCash"));
                tranPointRate.setBasePointAffiliate(result.getString("BasePointAffiliate"));
                tranPointRate.setBasePointNonAffiliate(result.getString("BasePointNonAffiliate"));
                tranPointRate.setPointCalcType(result.getString("PointCalcType"));
                tranPointRate.setTaxCalcType(result.getString("TaxCalcType"));
                tranPointRate.setRoundType(result.getString("RoundType"));
                tranPointRate.setCardSettingFlag(result.getString("CardSettingFlag"));
                tranPointRate.setDptSettingFlag(result.getString("DptSettingFlag"));
                tranPointRateList.add(tranPointRate);
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get tran point rate.", e);
            throw new Exception("SQLException: @SQLServerPointDAO."
                    + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(tranPointRateList);
        }
    	
        // Connection connection2 = null;
        // PreparedStatement statement2 = null;
        // ResultSet result2 = null;

        // try {
        //     connection2 = dbManager.getConnection();
        //     SQLStatement sqlStatement2 = SQLStatement.getInstance();
        //     statement2 = connection2.prepareStatement(
        //                 sqlStatement2.getProperty("get-point-tran-rate-campaign"));
        //     statement2.setString(SQLStatement.PARAM1, companyId);
        //     statement2.setString(SQLStatement.PARAM2, storeId);
        //     statement2.setString(SQLStatement.PARAM3, businessdate);
        //     statement2.setString(SQLStatement.PARAM4, cardclassid);
        //     result2 = statement2.executeQuery();
        //     while(result2.next())  {
        //         if (IsCampaignEnabled(businessdate,
        //         result2.getString("TargetDaySettingFlag"), result2.getString("TargetDay1"), result2.getString("TargetDay2"),
        //         result2.getString("TargetDay3"), result2.getString("TargetDay4"), result2.getString("TargetDay5"),
        //         result2.getString("TargetDay6"), result2.getString("TargetDay7"), result2.getString("TargetDay8"), result2.getString("TargetDay9"),
        //         result2.getString("DayOfWeekSettingFlag"), result2.getString("DayOfWeekMonFlag"), result2.getString("DayOfWeekTueFlag"),
        //         result2.getString("DayOfWeekWedFlag"), result2.getString("DayOfWeekThuFlag"), result2.getString("DayOfWeekFriFlag"),
        //         result2.getString("DayOfWeekSatFlag"), result2.getString("DayOfWeekSunFlag"))) {
        //             TranPointRate tranPointRate = new TranPointRate();
        //             tranPointRate.setType("1");
        //             tranPointRate.setCompanyId(companyId);
        //             tranPointRate.setStoreId(storeId);
        //             tranPointRate.setBasePrice(result2.getString("BasePrice"));
        //             tranPointRate.setBasePoint(result2.getString("BasePoint"));
        //             tranPointRate.setRecordId(result2.getString("RecordId"));
        //             tranPointRate.setBasePointCash(result2.getString("BasePointCash"));
        //             tranPointRate.setBasePointAffiliate(result2.getString("BasePointAffiliate"));
        //             tranPointRate.setBasePointNonAffiliate(result2.getString("BasePointNonAffiliate"));
        //             tranPointRate.setCardSettingFlag(result2.getString("CardSettingFlag"));
        //             tranPointRate.setDptSettingFlag(result2.getString("DptSettingFlag"));
        //             tranPointRate.setItemSettingFlag(result2.getString("ItemSettingFlag"));
        //             tranPointRate.setTargetStoreType(result2.getString("TargetStoreType"));
        //             tranPointRateList.add(tranPointRate);
        //         }
        //     }
        // } catch (Exception e) {
        //     LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
        //             + ": Failed to get tran point rate2.", e);
        //     throw new Exception("SQLException: @SQLServerPointDAO."
        //             + functionName, e);
        // } finally {
        //     closeConnectionObjects(connection2, statement2, result2);
        //     tp.methodExit(tranPointRateList);
        // }
        return tranPointRateList;
    }

    private boolean IsCampaignEnabled(String businessDate, 
        String dateflag, String date1, String date2, String date3, String date4, String date5, String date6, String date7, String date8, String date9,
        String weekflag, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

        if (dateflag.equals(FLAG_OFF) && weekflag.equals(FLAG_OFF)) {
            return true;
        }
        
        String[] date = businessDate.split("-");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
        
        boolean datehit = false;
        boolean weekhit = false;
        if (dateflag.equals(FLAG_ON)) {
            int checkdate = cal.get(Calendar.DATE);
            if ((checkdate == parseInt(date1)) || 
                (checkdate == parseInt(date2)) || 
                (checkdate == parseInt(date3)) || 
                (checkdate == parseInt(date4)) || 
                (checkdate == parseInt(date5)) || 
                (checkdate == parseInt(date6)) || 
                (checkdate == parseInt(date7)) || 
                (checkdate == parseInt(date8)) || 
                (checkdate == parseInt(date9))) {
                datehit = true;
            }
        }
        
        if (weekflag.equals(FLAG_ON)) {
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                if (sunday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.MONDAY:
                if (monday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.TUESDAY:
                if (tuesday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.WEDNESDAY:
                if (wednesday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.THURSDAY:
                if (thursday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.FRIDAY:
                if (friday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            case Calendar.SATURDAY:
                if (saturday.equals(FLAG_ON)) {
                    weekhit = true;
                }
                break;
            }
        }

        if (dateflag.equals(FLAG_ON) && weekflag.equals(FLAG_ON)) {
            if (datehit && weekhit) {
                return true;
            }
        } else if (dateflag.equals(FLAG_ON) && weekflag.equals(FLAG_OFF)) {
            if (datehit) {
                return true;
            }
        } else if (dateflag.equals(FLAG_OFF) && weekflag.equals(FLAG_ON)) {
            if (weekhit) {
                return true;
            }
        }

        return false;
    }
    private int parseInt(String value) {
        try{
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private long parseLong(String value) {
        try{
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private boolean IsCampaignItemEnabled(String itemflag, String groupidflag, String groupidstart, String groupidend, String groupid,
        String dptflag, String dptstart, String dptend, String dpt,
        String brandidflag, String brandidstart, String brandidend, String brandid,
        String skuflag, String skustart, String skuend, String sku, String targetid, String targettype) {
        boolean grouphit = false;
        boolean dpthit = false;
        boolean brandhit = false;
        boolean skuhit = false;
        if (itemflag.equals(FLAG_OFF)) {
            return true;
        }
        
        if (groupidflag.equals(FLAG_OFF)) {
            if (StringUtility.isNullOrEmpty(groupidstart) && StringUtility.isNullOrEmpty(groupidend)) {
                grouphit = false;
            } else {
                if (parseLong(groupidend) == 0) {
                    groupidend = "999";
                }
                if (parseLong(groupidstart) <= parseLong(groupid) && parseLong(groupid) <= parseLong(groupidend)) {
                    grouphit = true;
                }
            }
        } else if (targettype.equals("1")) {
            if (groupid.equals(targetid)) {
                grouphit = true;
            }
        }

        if (dptflag.equals(FLAG_OFF)) {
            if (StringUtility.isNullOrEmpty(dptstart) && StringUtility.isNullOrEmpty(dptend)) {
                dpthit = false;
            } else {
                if (parseLong(dptend) == 0) {
                    dptend = "999";
                }
                if (parseLong(dptstart) <= parseLong(dpt) && parseLong(dpt) <= parseLong(dptend)) {
                    dpthit = true;
                }
            }
        } else if (targettype.equals("2")) {
            if (dpt.equals(targetid)) {
                dpthit = true;
            }
        }

        if (skuflag.equals(FLAG_OFF)) {
            if (StringUtility.isNullOrEmpty(skustart) && StringUtility.isNullOrEmpty(skuend)) {
                skuhit = false;
            } else {
                if (parseLong(skuend) == 0) {
                    skuend = "99999999";
                }
                if (parseLong(skustart) <= parseLong(sku) && parseLong(sku) <= parseLong(skuend)) {
                    skuhit = true;
                }
            }
        } else if (targettype.equals("4")) {
            if (sku.equals(targetid)) {
                skuhit = true;
            }
        }

        if (brandidflag.equals(FLAG_OFF)) {
            if (StringUtility.isNullOrEmpty(brandidstart) && StringUtility.isNullOrEmpty(brandidend)) {
                brandhit = false;
            } else {
                if (parseLong(brandidend) == 0) {
                    brandidend = "999";
                }
                if (parseLong(brandidstart) <= parseLong(brandid) && parseLong(brandid) <= parseLong(brandidend)) {
                    brandhit = true;
                }
            }
        } else if (targettype.equals("3")) {
            if (brandid.equals(targetid)) {
                brandhit = true;
            }
        }

        if (grouphit || dpthit || skuhit) {
            if (brandhit) {
                return true;
            } else if (brandidflag.equals(FLAG_OFF) && StringUtility.isNullOrEmpty(brandidstart) && StringUtility.isNullOrEmpty(brandidend)){
                return true;
            }
        } else if (brandhit) {
        	return true;
        }
        return false;
    }
    
    @Override
    public CashingUnit getCashingUnitInfo(String companyId,String recordId) throws DaoException {
        String functionName = "";

        tp.methodEnter("");
        tp.println("CompanyId", companyId).println("RecordId", recordId);

        CashingUnit cashingUnit = new CashingUnit();
        //Store store = new Store();
        //cashingUnit.setRetailStoreID(retailStoreID);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-point-cashing-unit"));

            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, recordId);

            result = select.executeQuery();

            if (result.next()) {
                cashingUnit.setCashingUnit(result.getString("CashingUnit"));
            } else {
                tp.println("CashingUnit not found.");
            }

        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get CashingUnit.", e);
            throw new DaoException("SQLException: @SQLServerPointDAO."
                    + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(cashingUnit);
        }
        return cashingUnit;
    }
}
