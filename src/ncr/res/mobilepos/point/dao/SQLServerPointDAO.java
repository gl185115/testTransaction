package ncr.res.mobilepos.point.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.point.model.CashingUnit;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.TranPointRate;
import ncr.res.mobilepos.property.SQLStatement;

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
    private static final String FLAG_OFF = "0";
    private static final String FLAG_ON = "1";
    private static final String FLAG_MULTI = "2";

    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerPointDAO() throws Exception {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
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
    public List<ItemPointRate> getItemPointRate(String companyId, String storeId, String businessdate, String deptcode,
            String groupcode, String brandId, String sku) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId)
                .println("businessDate", businessdate).println("deptCode", deptcode).println("groupCode", groupcode)
                .println("brandId", brandId).println("sku", sku);

        List<ItemPointRate> itemPointRateList = new ArrayList<ItemPointRate>();

        Connection connection3 = null;
        PreparedStatement statement3 = null;
        ResultSet result3 = null;
        try {
            connection3 = dbManager.getConnection();
            SQLStatement sqlStatement3 = SQLStatement.getInstance();

            statement3 = connection3.prepareStatement(sqlStatement3.getProperty("get-point-item-campaign-rate"));
            statement3.setString(SQLStatement.PARAM1, companyId);
            statement3.setString(SQLStatement.PARAM2, storeId);
            statement3.setString(SQLStatement.PARAM3, businessdate);
            statement3.setString(SQLStatement.PARAM4, deptcode);
            statement3.setString(SQLStatement.PARAM5, groupcode);
            statement3.setString(SQLStatement.PARAM6, brandId);
            statement3.setString(SQLStatement.PARAM7, sku);
            result3 = statement3.executeQuery();
            while (result3.next()) {
                if (IsCampaignEnabled(businessdate, result3.getString("TargetDaySettingFlag"),
                        result3.getString("TargetDay1"), result3.getString("TargetDay2"),
                        result3.getString("TargetDay3"), result3.getString("TargetDay4"),
                        result3.getString("TargetDay5"), result3.getString("TargetDay6"),
                        result3.getString("TargetDay7"), result3.getString("TargetDay8"),
                        result3.getString("TargetDay9"), result3.getString("DayOfWeekSettingFlag"),
                        result3.getString("DayOfWeekMonFlag"), result3.getString("DayOfWeekTueFlag"),
                        result3.getString("DayOfWeekWedFlag"), result3.getString("DayOfWeekThuFlag"),
                        result3.getString("DayOfWeekFriFlag"), result3.getString("DayOfWeekSatFlag"),
                        result3.getString("DayOfWeekSunFlag"))) {
                    if (IsCampaignItemEnabled(result3.getString("ItemSettingFlag"),
                            result3.getString("GroupTargetType"), result3.getString("GroupIdStart"),
                            result3.getString("GroupIdEnd"), groupcode, result3.getString("DptTargetType"),
                            result3.getString("DptStart"), result3.getString("DptEnd"), deptcode,
                            result3.getString("BrandTargetType"), result3.getString("BrandIdStart"),
                            result3.getString("BrandIdEnd"), brandId, result3.getString("SkuTargetType"),
                            result3.getString("SkuStart"), result3.getString("SkuEnd"), sku,
                            result3.getString("TargetId"), result3.getString("TargetType"))) {
                        ItemPointRate itemPointRate = new ItemPointRate();
                        itemPointRate.setType("2");
                        itemPointRate.setCompanyId(companyId);
                        itemPointRate.setStoreId(storeId);
                        itemPointRate.setPointFlag("1");
                        itemPointRate.setBasePrice(result3.getString("BasePrice"));
                        itemPointRate.setRecordId(result3.getString("RecordId"));
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
                        if (FLAG_MULTI.equals(result3.getString("GroupTargetType"))
                                || FLAG_MULTI.equals(result3.getString("DptTargetType"))
                                || FLAG_MULTI.equals(result3.getString("BrandTargetType"))
                                || FLAG_MULTI.equals(result3.getString("SkuTargetType"))) {
                            itemPointRate.setBasePoint(result3.getString("DetailBasePoint"));
                        } else if (FLAG_ON.equals(result3.getString("GroupTargetType"))
                                || FLAG_ON.equals(result3.getString("DptTargetType"))
                                || FLAG_ON.equals(result3.getString("BrandTargetType"))
                                || FLAG_ON.equals(result3.getString("SkuTargetType"))) {
                            itemPointRate.setBasePoint(result3.getString("InfoBasePoint"));
                        } else {
                            itemPointRate.setBasePoint(result3.getString("BasePoint"));
                        }

                        itemPointRateList.add(itemPointRate);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get item point rate3.", e);
            throw new Exception("SQLException: @SQLServerPointDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection3, statement3, result3);
            tp.methodExit(itemPointRateList);
        }
        return itemPointRateList;
    }

    @Override
    public List<TranPointRate> getTranPointRate(String companyId, String storeId, String businessdate)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("businessDate",
                businessdate);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<TranPointRate> tranPointRateList = new ArrayList<TranPointRate>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-point-tran-rate"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, businessdate);

            result = statement.executeQuery();
            while (result.next()) {
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
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get tran point rate.", e);
            throw new Exception("SQLException: @SQLServerPointDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(tranPointRateList);
        }
        return tranPointRateList;
    }

    private boolean IsCampaignEnabled(String businessDate, String dateflag, String date1, String date2, String date3,
            String date4, String date5, String date6, String date7, String date8, String date9, String weekflag,
            String monday, String tuesday, String wednesday, String thursday, String friday, String saturday,
            String sunday) {

        if (FLAG_OFF.equals(dateflag) && FLAG_OFF.equals(weekflag)) {
            return true;
        }

        String[] date = businessDate.split("-");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));

        boolean datehit = false;
        boolean weekhit = false;
        if (FLAG_ON.equals(dateflag)) {
            int checkdate = cal.get(Calendar.DATE);
            if ((checkdate == parseInt(date1)) || (checkdate == parseInt(date2)) || (checkdate == parseInt(date3))
                    || (checkdate == parseInt(date4)) || (checkdate == parseInt(date5))
                    || (checkdate == parseInt(date6)) || (checkdate == parseInt(date7))
                    || (checkdate == parseInt(date8)) || (checkdate == parseInt(date9))) {
                datehit = true;
            }
        }

        if (FLAG_ON.equals(weekflag)) {
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                if (FLAG_ON.equals(sunday)) {
                    weekhit = true;
                }
                break;
            case Calendar.MONDAY:
                if (FLAG_ON.equals(monday)) {
                    weekhit = true;
                }
                break;
            case Calendar.TUESDAY:
                if (FLAG_ON.equals(tuesday)) {
                    weekhit = true;
                }
                break;
            case Calendar.WEDNESDAY:
                if (FLAG_ON.equals(wednesday)) {
                    weekhit = true;
                }
                break;
            case Calendar.THURSDAY:
                if (FLAG_ON.equals(thursday)) {
                    weekhit = true;
                }
                break;
            case Calendar.FRIDAY:
                if (FLAG_ON.equals(friday)) {
                    weekhit = true;
                }
                break;
            case Calendar.SATURDAY:
                if (FLAG_ON.equals(saturday)) {
                    weekhit = true;
                }
                break;
            }
        }

        if (FLAG_ON.equals(dateflag) && FLAG_ON.equals(weekflag)) {
            if (datehit && weekhit) {
                return true;
            }
        } else if (FLAG_ON.equals(dateflag) && FLAG_OFF.equals(weekflag)) {
            if (datehit) {
                return true;
            }
        } else if (FLAG_OFF.equals(dateflag) && FLAG_ON.equals(weekflag)) {
            if (weekhit) {
                return true;
            }
        }

        return false;
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean IsCampaignItemEnabled(String itemflag, String groupidflag, String groupidstart, String groupidend,
            String groupid, String dptflag, String dptstart, String dptend, String dpt, String brandidflag,
            String brandidstart, String brandidend, String brandid, String skuflag, String skustart, String skuend,
            String sku, String targetid, String targettype) {
        boolean grouphit = false;
        boolean dpthit = false;
        boolean brandhit = false;
        boolean skuhit = false;
        if (FLAG_OFF.equals(itemflag)) {
            return true;
        }

        if (FLAG_OFF.equals(groupidflag)) {
            grouphit = false;
        } else if (FLAG_ON.equals(groupidflag)) {
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
        } else if (FLAG_MULTI.equals(groupidflag) && "1".equals(targettype)) {
            if (groupid.startsWith(targetid)) {
                grouphit = true;
            }
        }

        if (FLAG_OFF.equals(dptflag)) {
            dpthit = false;
        } else if (FLAG_ON.equals(dptflag)) {
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
        } else if (FLAG_MULTI.equals(dptflag) && "2".equals(targettype)) {
            if (dpt.startsWith(targetid)) {
                dpthit = true;
            }
        }

        if (FLAG_OFF.equals(skuflag)) {
            skuhit = false;
        } else if (FLAG_ON.equals(skuflag)) {
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
        } else if (FLAG_MULTI.equals(skuflag) && "4".equals(targettype)) {
            if (sku.startsWith(targetid)) {
                skuhit = true;
            }
        }

        if (FLAG_OFF.equals(brandidflag)) {
            brandhit = false;
        } else if (FLAG_ON.equals(brandidflag)) {
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
        } else if (FLAG_MULTI.equals(brandidflag) && "3".equals(targettype)) {
            if (brandid.startsWith(targetid)) {
                brandhit = true;
            }
        }

        if (grouphit || dpthit || skuhit) {
            if (brandhit) {
                return true;
            } else if (FLAG_OFF.equals(brandidflag) && StringUtility.isNullOrEmpty(brandidstart)
                    && StringUtility.isNullOrEmpty(brandidend)) {
                return true;
            }
        } else if (brandhit) {
            return true;
        }
        return false;
    }
    
    @Override
    public CashingUnit getCashingUnitInfo() throws DaoException {
        String functionName = "";

        tp.methodEnter("");;

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
