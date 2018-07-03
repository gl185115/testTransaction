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
import ncr.res.mobilepos.point.factory.PointRateFactory;
import ncr.res.mobilepos.point.model.CashingUnit;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.PointInfo;
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
    public List<PointInfo> getPointInfoList(String companyId, String storeId, String businessdate) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("businessDate",
                businessdate);

        List<PointInfo> pointInfoList = new ArrayList<PointInfo>();

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
                    PointInfo pointInfo = new PointInfo();
                    pointInfo.setPointType(result3.getString("PointType"));
                    pointInfo.setBasePrice(result3.getString("BasePrice"));
                    pointInfo.setRecordId(result3.getString("RecordId"));
                    pointInfo.setBasePoint(result3.getString("BasePoint"));
                    pointInfo.setPointTender(result3.getString("PointTender"));
                    pointInfo.setTenderSettingFlag(result3.getString("TenderSettingFlag"));
                    pointInfo.setBasePointCash(result3.getString("BasePointCash"));
                    pointInfo.setBasePointAffiliate(result3.getString("BasePointAffiliate"));
                    pointInfo.setBasePointNonAffiliate(result3.getString("BasePointNonAffiliate"));
                    pointInfo.setCardSettingFlag(result3.getString("CardSettingFlag"));
                    pointInfo.setTargetStoreType(result3.getString("TargetStoreType"));
                    pointInfo.setItemSettingFlag(result3.getString("ItemSettingFlag"));
                    pointInfo.setDptSettingFlag(result3.getString("DptSettingFlag"));
                    pointInfo.setCardClassId(result3.getString("CardClassId"));
                    pointInfo.setTenderId(result3.getString("TenderId"));
                    pointInfo.setTenderType(result3.getString("TenderType"));
                    pointInfo.setGroupTargetType(result3.getString("GroupTargetType"));
                    pointInfo.setGroupIdStart(result3.getString("GroupIdStart"));
                    pointInfo.setGroupIdEnd(result3.getString("GroupIdEnd"));
                    pointInfo.setDptTargetType(result3.getString("DptTargetType"));
                    pointInfo.setDptStart(result3.getString("DptStart"));
                    pointInfo.setDptEnd(result3.getString("DptEnd"));
                    pointInfo.setBrandTargetType(result3.getString("BrandTargetType"));
                    pointInfo.setBrandIdStart(result3.getString("BrandIdStart"));
                    pointInfo.setBrandIdEnd(result3.getString("BrandIdEnd"));
                    pointInfo.setSkuTargetType(result3.getString("SkuTargetType"));
                    pointInfo.setSkuStart(result3.getString("SkuStart"));
                    pointInfo.setSkuEnd(result3.getString("SkuEnd"));
                    pointInfo.setInfoBasePoint(result3.getString("InfoBasePoint"));
                    pointInfo.setDetailBasePoint(result3.getString("DetailBasePoint"));
                    pointInfo.setTargetType(result3.getString("TargetType"));
                    pointInfo.setTargetId(result3.getString("TargetId"));
                    pointInfoList.add(pointInfo);
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get item point rate3.", e);
            throw new Exception("SQLException: @SQLServerPointDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection3, statement3, result3);
            tp.methodExit(pointInfoList);
        }
        return pointInfoList;
    }

    @Override
    public List<ItemPointRate> getItemPointRate(String companyId, String storeId, String businessdate, String deptcode,
            String groupcode, String brandId, String barCode) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId)
                .println("businessDate", businessdate).println("deptCode", deptcode).println("groupCode", groupcode)
                .println("brandId", brandId).println("barCode", barCode);

        List<ItemPointRate> itemPointRateList = new ArrayList<ItemPointRate>();
        List<PointInfo> pointInfoList = PointRateFactory.getRateInstanceItem();
        for (PointInfo pointInfo : pointInfoList) {
            if (IsCampaignItemEnabled(pointInfo.getItemSettingFlag(), pointInfo.getGroupTargetType(),
                    pointInfo.getGroupIdStart(), pointInfo.getGroupIdEnd(), groupcode, pointInfo.getDptTargetType(),
                    pointInfo.getDptStart(), pointInfo.getDptEnd(), deptcode, pointInfo.getBrandTargetType(),
                    pointInfo.getBrandIdStart(), pointInfo.getBrandIdEnd(), brandId, pointInfo.getSkuTargetType(),
                    pointInfo.getSkuStart(), pointInfo.getSkuEnd(), barCode, pointInfo.getTargetId(),
                    pointInfo.getTargetType())) {
                ItemPointRate itemPointRate = new ItemPointRate();
                itemPointRate.setType("2");
                itemPointRate.setCompanyId(companyId);
                itemPointRate.setStoreId(storeId);
                itemPointRate.setPointFlag("1");
                itemPointRate.setBasePrice(pointInfo.getBasePrice());
                itemPointRate.setRecordId(pointInfo.getRecordId());
                itemPointRate.setPointTender(pointInfo.getPointTender());
                itemPointRate.setTenderSettingFlag(pointInfo.getTenderSettingFlag());
                itemPointRate.setTenderType(pointInfo.getTenderType());
                itemPointRate.setTenderId(pointInfo.getTenderId());
                itemPointRate.setBasePointCash(pointInfo.getBasePointCash());
                itemPointRate.setBasePointAffiliate(pointInfo.getBasePointAffiliate());
                itemPointRate.setBasePointNonAffiliate(pointInfo.getBasePointNonAffiliate());
                itemPointRate.setDptSettingFlag(pointInfo.getDptSettingFlag());
                itemPointRate.setItemSettingFlag(pointInfo.getItemSettingFlag());
                itemPointRate.setCardSettingFlag(pointInfo.getCardSettingFlag());
                itemPointRate.setStoreSettingFlag(pointInfo.getTargetStoreType());
                itemPointRate.setCardClassId(pointInfo.getCardClassId());
                if (FLAG_MULTI.equals(pointInfo.getGroupTargetType()) || FLAG_MULTI.equals(pointInfo.getDptTargetType())
                        || FLAG_MULTI.equals(pointInfo.getBrandTargetType())
                        || FLAG_MULTI.equals(pointInfo.getSkuTargetType())) {
                    itemPointRate.setBasePoint(pointInfo.getDetailBasePoint());
                } else if (FLAG_ON.equals(pointInfo.getGroupTargetType())
                        || FLAG_ON.equals(pointInfo.getDptTargetType())
                        || FLAG_ON.equals(pointInfo.getBrandTargetType())
                        || FLAG_ON.equals(pointInfo.getSkuTargetType())) {
                    itemPointRate.setBasePoint(pointInfo.getInfoBasePoint());
                } else {
                    itemPointRate.setBasePoint(pointInfo.getBasePoint());
                }

                itemPointRateList.add(itemPointRate);
            }
        }
        tp.methodExit(itemPointRateList);
        return itemPointRateList;
    }

    @Override
    public List<TranPointRate> getTranPointRate(String companyId, String storeId, String businessdate)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("businessDate",
                businessdate);
        List<TranPointRate> tranPointRateList = new ArrayList<TranPointRate>();
        List<PointInfo> pointInfoList = PointRateFactory.getRateInstanceTran();

           for (PointInfo pointInfo : pointInfoList) {
                TranPointRate tranPointRate = new TranPointRate();
                tranPointRate.setType("0");
                tranPointRate.setCompanyId(companyId);
                tranPointRate.setStoreId(storeId);
                tranPointRate.setPointFlag(pointInfo.getPointFlag());
                tranPointRate.setBasePrice(pointInfo.getBasePrice());
                tranPointRate.setBasePoint(pointInfo.getBasePoint());
                tranPointRate.setCashingUnit(pointInfo.getCashingUnit());
                tranPointRate.setRecordId(pointInfo.getRecordId());
                tranPointRate.setBasePointCash(pointInfo.getBasePointCash());
                tranPointRate.setBasePointAffiliate(pointInfo.getBasePointAffiliate());
                tranPointRate.setBasePointNonAffiliate(pointInfo.getBasePointNonAffiliate());
                tranPointRate.setPointCalcType(pointInfo.getPointCalcType());
                tranPointRate.setTaxCalcType(pointInfo.getTaxCalcType());
                tranPointRate.setRoundType(pointInfo.getRoundType());
                tranPointRate.setCardSettingFlag(pointInfo.getCardSettingFlag());
                tranPointRate.setDptSettingFlag(pointInfo.getDptSettingFlag());
                tranPointRateList.add(tranPointRate);
            }
        tp.methodExit(tranPointRateList);
        return tranPointRateList;
    }
    @Override
    public List<PointInfo> getTranPointInfoList(String companyId, String storeId, String businessdate)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("businessDate",
                businessdate);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<PointInfo> pointInfoList = new ArrayList<PointInfo>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-point-tran-rate"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, businessdate);

            result = statement.executeQuery();
            while (result.next()) {
                PointInfo pointInfo = new PointInfo();
                pointInfo.setPointFlag(result.getString("PointFlag"));
                pointInfo.setBasePrice(result.getString("BasePrice"));
                pointInfo.setBasePoint(result.getString("BasePoint"));
                pointInfo.setCashingUnit(result.getString("CashingUnit"));
                pointInfo.setRecordId(result.getString("RecordId"));
                pointInfo.setBasePointCash(result.getString("BasePointCash"));
                pointInfo.setBasePointAffiliate(result.getString("BasePointAffiliate"));
                pointInfo.setBasePointNonAffiliate(result.getString("BasePointNonAffiliate"));
                pointInfo.setPointCalcType(result.getString("PointCalcType"));
                pointInfo.setTaxCalcType(result.getString("TaxCalcType"));
                pointInfo.setRoundType(result.getString("RoundType"));
                pointInfo.setCardSettingFlag(result.getString("CardSettingFlag"));
                pointInfo.setDptSettingFlag(result.getString("DptSettingFlag"));
                pointInfoList.add(pointInfo);
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get tran point rate.", e);
            throw new Exception("SQLException: @SQLServerPointDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(pointInfoList);
        }
        return pointInfoList;
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
            String barCode, String targetid, String targettype) {
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
                if (parseLong(skustart) <= parseLong(barCode) && parseLong(barCode) <= parseLong(skuend)) {
                    skuhit = true;
                }
            }
        } else if ((FLAG_MULTI.equals(skuflag) || StringUtility.isNullOrEmpty(skuflag)) && "4".equals(targettype)) {
            if (!StringUtility.isNullOrEmpty(targetid)) {
                if (targetid.contains("*")) {
                    if (barCode.startsWith(targetid.replace("*", ""))) {
                        skuhit = true;
                    }
                } else {
                    if (barCode.equals(targetid)) {
                        skuhit = true;
                    }
                }
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
            } else if ((FLAG_OFF.equals(brandidflag) || StringUtility.isNullOrEmpty(brandidflag)) && StringUtility.isNullOrEmpty(brandidstart)
                    && StringUtility.isNullOrEmpty(brandidend)) {
                return true;
            }
        } else if (brandhit) {
            return true;
        }
        return false;
    }
    
    @Override
    public CashingUnit getCashingUnitInfo(String companyId, String businessdate) throws DaoException {
        String functionName = "";

        tp.methodEnter(functionName).println("companyId", companyId).println("businessDate",
                businessdate);

        CashingUnit cashingUnit = new CashingUnit();
        List<PointInfo> pointInfoList = PointRateFactory.getRateInstanceTran();

        for (PointInfo pointInfo : pointInfoList) {
            cashingUnit.setCashingUnit(pointInfo.getCashingUnit());
        }
        tp.methodExit(cashingUnit);
        return cashingUnit;
    }
}
