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
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPriceMMInfoDAO extends AbstractDao implements IPriceMMInfoDAO {
	/**
	 * The Database Manager of the class.
	 */
	private DBManager dbManager;
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
	/**
	 * The Program name.
	 */
	private String progname = "PriceMMInfoDAO";
    /**
     * constant. the falg
     */
    private static final int FLAG_ON = 1;
    private static final int FLAG_OFF = 0;
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	/**
	 * Default Constructor for SQLServerPriceMMInfoDAO
	 *
	 * <P>
	 * Sets DBManager for database connection, and Logger for logging.
	 *
	 * @throws DaoException
	 *			 The exception thrown when the constructor fails.
	 */
	public SQLServerPriceMMInfoDAO() throws DaoException {
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
	 * Get PriceMMInfo from MST_PRICE_MM_INFO,MST_PRICE_MM_DETAIL and MST_PRICE_MM_STORE.
	 * @param companyId,
	 *		storeId,
	 *		dayDate,
	 *		mdInternal
	 * @return PriceMMList
	 * @throws DaoException Exception when error occurs.
	 */
	@Override
	public List<PriceMMInfo> getPriceMMInfoList(String companyId, String storeId, String dayDate, String mdInternal)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.println("CompanyId", companyId);
		tp.println("storeId", storeId);
		tp.println("dayDate", dayDate);
		tp.println("mdInternal", mdInternal);

		List<PriceMMInfo> pricePromList = new ArrayList<PriceMMInfo>();
		try (Connection connection = dbManager.getConnection();
				PreparedStatement statement = prepareGetPriceMMInfoList(companyId, storeId, dayDate, connection, mdInternal);
				ResultSet result = statement.executeQuery();)
		{
			PriceMMInfo priceMMInfo = null;
			while(result.next()){
				priceMMInfo = new PriceMMInfo();
				priceMMInfo.setMMNo(result.getString(result.findColumn("MMNo")));
				priceMMInfo.setConditionCount1(result.getInt(result.findColumn("ConditionCount1")));
				priceMMInfo.setConditionCount2(result.getInt(result.findColumn("ConditionCount2")));
				priceMMInfo.setConditionCount3(result.getInt(result.findColumn("ConditionCount3")));
				
				priceMMInfo.setConditionPrice3(result.getDouble(result.findColumn("ConditionPrice3")));
				priceMMInfo.setConditionPrice2(result.getDouble(result.findColumn("ConditionPrice2")));
				priceMMInfo.setConditionPrice1(result.getDouble(result.findColumn("ConditionPrice1")));

				priceMMInfo.setDiscountClass(result.getString((result.findColumn("DiscountClass"))));
				
				
				if(result.getObject(result.findColumn("ConditionRate1")) != null ){
					priceMMInfo.setConditionRate1(result.getDouble(result.findColumn("ConditionRate1")));
				}
				
				if(result.getObject(result.findColumn("ConditionRate2")) != null ){
					priceMMInfo.setConditionRate2(result.getDouble(result.findColumn("ConditionRate2")));
				}
				
				if(result.getObject(result.findColumn("ConditionRate3")) != null ){
					priceMMInfo.setConditionRate3(result.getDouble(result.findColumn("ConditionRate3")));
				}
				
				if(result.getObject(result.findColumn("DecisionRate1")) != null ){
					priceMMInfo.setDecisionRate1(result.getDouble(result.findColumn("DecisionRate1")));
				}
				
				if(result.getObject(result.findColumn("DecisionRate2")) != null ){
					priceMMInfo.setDecisionRate2(result.getDouble(result.findColumn("DecisionRate2")));
				}
				
				if(result.getObject(result.findColumn("DecisionRate3")) != null ){
					priceMMInfo.setDecisionRate3(result.getDouble(result.findColumn("DecisionRate3")));
				}
				
				if(result.getObject(result.findColumn("DecisionPrice1")) != null ){
					priceMMInfo.setDecisionPrice1(result.getDouble(result.findColumn("DecisionPrice1")));
				}

				if(result.getObject(result.findColumn("DecisionPrice2")) != null ){
					priceMMInfo.setDecisionPrice2(result.getDouble(result.findColumn("DecisionPrice2")));
				}

				if(result.getObject(result.findColumn("DecisionPrice3")) != null ){
					priceMMInfo.setDecisionPrice3(result.getDouble(result.findColumn("DecisionPrice3")));
				}

				priceMMInfo.setAveragePrice1(result.getDouble(result.findColumn("AveragePrice1")));
				priceMMInfo.setAveragePrice2(result.getDouble(result.findColumn("AveragePrice2")));
				priceMMInfo.setAveragePrice3(result.getDouble(result.findColumn("AveragePrice3")));

				priceMMInfo.setNote(result.getString(result.findColumn("Note")));
				priceMMInfo.setSku(result.getString(result.findColumn("Sku")));
				priceMMInfo.setTargetStoreType(result.getString(result.findColumn("TargetStoreType")));
				priceMMInfo.setMmDisplayNo(result.getString(result.findColumn("MMDisplayNo")));
				priceMMInfo.setStartDate(result.getDate(result.findColumn("StartDate")));
				priceMMInfo.setEndDate(result.getDate(result.findColumn("EndDate")));
				priceMMInfo.setStartTime(result.getString(result.findColumn("StartTime")));
				priceMMInfo.setEndTime(result.getString(result.findColumn("EndTime")));
				priceMMInfo.setSaleStartTime(result.getString(result.findColumn("SaleStartTime")));
				priceMMInfo.setSaleEndTime(result.getString(result.findColumn("SaleEndTime")));
				priceMMInfo.setDayOfWeekSettingFlag(result.getInt(result.findColumn("DayOfWeekSettingFlag")));
				priceMMInfo.setDayOfWeekMonFlag(result.getInt(result.findColumn("DayOfWeekMonFlag")));
				priceMMInfo.setDayOfWeekTueFlag(result.getInt(result.findColumn("DayOfWeekTueFlag")));
				priceMMInfo.setDayOfWeekWedFlag(result.getInt(result.findColumn("DayOfWeekWedFlag")));
				priceMMInfo.setDayOfWeekThuFlag(result.getInt(result.findColumn("DayOfWeekThuFlag")));
				priceMMInfo.setDayOfWeekFriFlag(result.getInt(result.findColumn("DayOfWeekFriFlag")));
				priceMMInfo.setDayOfWeekSatFlag(result.getInt(result.findColumn("DayOfWeekSatFlag")));
				priceMMInfo.setDayOfWeekSunFlag(result.getInt(result.findColumn("DayOfWeekSunFlag")));
				priceMMInfo.setSubNum1(result.getInt(result.findColumn("SubNum1")));
				pricePromList.add(priceMMInfo);

			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the PriceMM information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPriceMMInfo ", sqlEx);
		} finally {
			tp.methodExit(pricePromList);
		}
		return pricePromList;
	}

	private boolean isPriceMMEnabled(String businessDate,int weekflag,
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
	
	protected PreparedStatement prepareGetPriceMMInfoList(String companyId,String storeId,String businessDate,Connection connection,
															String mdInternal) throws SQLException {
		// Creates PreparedStatement .
		SQLStatement sqlStatement = SQLStatement.getInstance();
		PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-price-mm-info-list"));
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, storeId);
		statement.setString(SQLStatement.PARAM3, businessDate);
		statement.setString(SQLStatement.PARAM4, mdInternal);
		return statement;
	}
}
