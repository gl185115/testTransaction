package ncr.res.mobilepos.pricing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPricePromInfoDAO extends AbstractDao implements IPricePromInfoDAO {
	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "PricePromInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * constant. the falg
     */
    private static final int FLAG_ON = 1;
    private static final int FLAG_OFF = 0;

    /**
     * Default Constructor for SQLServerPricePromInfoDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerPricePromInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for SQLServerItemDAO.
     *
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Get PricePromInfo from MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
     * @param companyId,
	 *        storeId,
	 *        dayDate,
	 *        itemId,
	 *        dpt,
	 *        line,
	 *        classId
     * @return PricePromList
     * @throws DaoException Exception when error occurs.
     */
	@Override
	public List<PricePromInfo> getPricePromInfoList(String companyId, String storeId, String dayDate, String itemId, String dpt, String line, String classId)
			throws DaoException {
		String functionName = "getPricePromInfoList";
    	tp.println("CompanyId", companyId);
        tp.println("storeId", storeId);
        tp.println("dayDate", dayDate);
        tp.println("itemId", itemId);
        tp.println("dpt", dpt);
        tp.println("line", line);
        tp.println("classId", classId);

        List<PricePromInfo> pricePromList = new ArrayList<PricePromInfo>();
        try (Connection connection = dbManager.getConnection();
              PreparedStatement statement = prepareGetPricePromInfoList(companyId, storeId, dayDate, connection, itemId, dpt, line, classId);
              ResultSet result = statement.executeQuery();)
        {
            PricePromInfo pricePromInfo = null;
            while(result.next()){
            	if (!isPricePromEnabled(dayDate,
                        result.getInt("DayOfWeekSettingFlag"), result.getInt("DayOfWeekMonFlag"), result.getInt("DayOfWeekTueFlag"),
                        result.getInt("DayOfWeekWedFlag"), result.getInt("DayOfWeekThuFlag"), result.getInt("DayOfWeekFriFlag"),
                        result.getInt("DayOfWeekSatFlag"), result.getInt("DayOfWeekSunFlag"))) {
            		continue;
            	}
            	pricePromInfo = new PricePromInfo();
            	pricePromInfo.setPromotionNo(result.getString(result.findColumn("PromotionNo")));
            	pricePromInfo.setPromotionName(result.getString(result.findColumn("PromotionName")));
            	pricePromInfo.setPromotionType(result.getString(result.findColumn("PromotionType")));
            	pricePromInfo.setBrandFlag(result.getInt(result.findColumn("BrandFlag")));
            	pricePromInfo.setDpt(result.getString(result.findColumn("Dpt")));
            	pricePromInfo.setLine(result.getString(result.findColumn("Line")));
            	pricePromInfo.setClas(result.getString(result.findColumn("Class")));
            	pricePromInfo.setSku(result.getString(result.findColumn("Sku")));
            	pricePromInfo.setDiscountClass(result.getString(result.findColumn("DiscountClass")));
            	pricePromInfo.setDiscountRate(result.getDouble(result.findColumn("DiacountRate")));
            	pricePromInfo.setDiscountAmt(result.getLong(result.findColumn("DiscountAmt")));
            	pricePromInfo.setBrandId(result.getString(result.findColumn("BrandId")));
            	pricePromInfo.setPromotionDisplayNo(result.getString(result.findColumn("PromotionDisplayNo")));
            	pricePromInfo.setSalesPrice(result.getLong(result.findColumn("SalesPrice")));
            	pricePromInfo.setStartDate(result.getDate(result.findColumn("StartDate")));
            	pricePromInfo.setEndDate(result.getDate(result.findColumn("EndDate")));
            	pricePromInfo.setStartTime(result.getString(result.findColumn("StartTime")));
            	pricePromInfo.setEndTime(result.getString(result.findColumn("EndTime")));
            	pricePromInfo.setSaleStartTime(result.getString(result.findColumn("SaleStartTime")));
            	pricePromInfo.setSaleEndTime(result.getString(result.findColumn("SaleEndTime")));
            	pricePromInfo.setDayOfWeekSettingFlag(result.getInt(result.findColumn("DayOfWeekSettingFlag")));
            	pricePromInfo.setDayOfWeekMonFlag(result.getInt(result.findColumn("DayOfWeekMonFlag")));
            	pricePromInfo.setDayOfWeekTueFlag(result.getInt(result.findColumn("DayOfWeekTueFlag")));
            	pricePromInfo.setDayOfWeekWedFlag(result.getInt(result.findColumn("DayOfWeekWedFlag")));
            	pricePromInfo.setDayOfWeekThuFlag(result.getInt(result.findColumn("DayOfWeekThuFlag")));
            	pricePromInfo.setDayOfWeekFriFlag(result.getInt(result.findColumn("DayOfWeekFriFlag")));
            	pricePromInfo.setDayOfWeekSatFlag(result.getInt(result.findColumn("DayOfWeekSatFlag")));
            	pricePromInfo.setDayOfWeekSunFlag(result.getInt(result.findColumn("DayOfWeekSunFlag")));
            	pricePromInfo.setSubNum1(result.getInt(result.findColumn("SubNum1")));
            	pricePromInfo.setSubNum2(result.getInt(result.findColumn("SubNum2")));
            	pricePromInfo.setSubCode1(result.getInt(result.findColumn("SubCode1")));
            	pricePromInfo.setSubCode2(result.getInt(result.findColumn("SubCode2")));
            	pricePromInfo.setSubCode3(result.getString(result.findColumn("SubCode3")));
            	pricePromInfo.setSubCode4(result.getInt(result.findColumn("SubCode4")));
            	pricePromInfo.setSubCode5(result.getInt(result.findColumn("SubCode5")));
            	pricePromList.add(pricePromInfo);

            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the PriceProm information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPricePromInfo ", sqlEx);
        } finally {
        	tp.methodExit(pricePromList);
        }
        return pricePromList;
	}

	/**
	 * Get PricePromInfo from MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and
	 * MST_PRICE_PROM_STORE.
	 * 
	 * @param companyId, storeId, dayDate,
	 * @return PricePromList
	 * @throws DaoException Exception when error occurs.
	 */
	@Override
	public List<PricePromInfo> getPricePromInfoDpt(String companyId, String storeId, String businessDate)
			throws DaoException {
		String functionName = "getPricePromInfoDpt";
		tp.println("CompanyId", companyId);
		tp.println("storeId", storeId);
		tp.println("businessDate", businessDate);

		List<PricePromInfo> pricePromList = new ArrayList<PricePromInfo>();
		try (Connection connection = dbManager.getConnection();
				PreparedStatement statement = prepareGetPricePromInfoList(companyId, storeId, businessDate, connection);
				ResultSet result = statement.executeQuery();) {
			PricePromInfo pricePromInfo = null;
			while (result.next()) {
				pricePromInfo = new PricePromInfo();
				pricePromInfo.setPromotionNo(result.getString(result.findColumn("PromotionNo")));
				pricePromInfo.setPromotionName(result.getString(result.findColumn("PromotionName")));
				pricePromInfo.setPromotionDisplayNo(result.getString(result.findColumn("PromotionDisplayNo")));
				pricePromInfo.setPromotionDetailNo(result.getString(result.findColumn("PromotionDetailNo")));
				pricePromInfo.setMemberFlag(result.getString(result.findColumn("MemberFlag")));
				pricePromInfo.setReasonCode(result.getString(result.findColumn("ReasonCode")));
				pricePromInfo.setDptNameLocal(result.getString(result.findColumn("DptNameLocal")));
				pricePromInfo.setDpt(result.getString(result.findColumn("Dpt")));
				pricePromInfo.setDiscountClass(result.getString(result.findColumn("DiscountClass")));
				pricePromInfo.setDiscountRate(result.getDouble(result.findColumn("DiacountRate")));
				pricePromInfo.setDiscountAmt(result.getLong(result.findColumn("DiscountAmt")));
				pricePromList.add(pricePromInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the PriceProm information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPricePromInfo ", sqlEx);
		} finally {
			tp.methodExit(pricePromList);
		}
		return pricePromList;
	}

	private boolean isPricePromEnabled(String businessDate,int weekflag,
			int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {

		if (weekflag == FLAG_OFF) {
			return true;
		}

	    Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = null;
            date = sdf.parse(businessDate);
            calendar.setTime(date);
        } catch (ParseException e){
            // Invalid business date, unlikely happen.
        	return false;
        }

        int dayOfTheWeekFlag = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY:
        	dayOfTheWeekFlag = sunday;
            break;
        case Calendar.MONDAY:
        	dayOfTheWeekFlag = monday;
            break;
        case Calendar.TUESDAY:
        	dayOfTheWeekFlag = tuesday;
            break;
        case Calendar.WEDNESDAY:
        	dayOfTheWeekFlag = wednesday;
            break;
        case Calendar.THURSDAY:
        	dayOfTheWeekFlag = thursday;
            break;
        case Calendar.FRIDAY:
        	dayOfTheWeekFlag = friday;
            break;
        case Calendar.SATURDAY:
        	dayOfTheWeekFlag = saturday;
            break;
	    }
        return dayOfTheWeekFlag == FLAG_ON;
	}

    protected PreparedStatement prepareGetPricePromInfoList(String companyId,String storeId,String businessDate,Connection connection,String itemId,String dpt,String line,String classId
                                                            ) throws SQLException {
        // Creates PreparedStatement .
    	SQLStatement sqlStatement = SQLStatement.getInstance();
        PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-price-prom-info-list"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, businessDate);
        statement.setString(SQLStatement.PARAM4, itemId);
        statement.setString(SQLStatement.PARAM5, dpt);
        statement.setString(SQLStatement.PARAM6, line);
        statement.setString(SQLStatement.PARAM7, classId);
        return statement;
    }

	protected PreparedStatement prepareGetPricePromInfoList(String companyId, String storeId, String businessDate,
			Connection connection) throws SQLException {
		SQLStatement sqlStatement = SQLStatement.getInstance();
		PreparedStatement statement = connection
				.prepareStatement(sqlStatement.getProperty("get-price-prom-info-list-dpt"));
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, storeId);
		statement.setString(SQLStatement.PARAM3, businessDate);
		return statement;
	}
}
