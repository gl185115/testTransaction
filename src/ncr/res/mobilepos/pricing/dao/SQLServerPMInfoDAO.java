/**
 * 
 */
package ncr.res.mobilepos.pricing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.promotion.model.PmItemInfo;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * @author hxd
 *
 */
public class SQLServerPMInfoDAO extends AbstractDao implements IPMInfoDAO {

	/**
	 * The Database Manager of the class.
	 */
	private DBManager dbManager;
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
	
	public final static int DAY_OF_WEEKMON = 1;
	public final static int DAY_OF_WEEKTUE = 2;
	public final static int DAY_OF_WEEKWED = 3;
	public final static int DAY_OF_WEEKTHU = 4;
	public final static int DAY_OF_WEEKFRI = 5;
	public final static int DAY_OF_WEEKSAT = 6;
	public final static int DAY_OF_WEEKSUN = 7;
	
	public final static int DAY_OF_WEEK_SETTING = 1;
	// the
	// Logger
	/**
	 * The Program name.
	 */
	private String progname = "PMInfoDAO";

	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	public SQLServerPMInfoDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/* (non-Javadoc)
	 * @see ncr.res.mobilepos.pricing.dao.IPMInfoDAO#getPmItemInfo()
	 */
	@Override
	public List<PmItemInfo> getPmItemInfo(String companyId, String storeId, String mdInternal, String businessDate) throws DaoException {
		String functionName = "SQLServerPMInfoDAO.getPmItemInfo";
		tp.methodEnter("getPmItemInfo")
		.println("companyId", companyId)
		.println("storeId", storeId)
		.println("mdInternal", mdInternal)
		.println("businessDate", businessDate);
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		PmItemInfo pmItemInfo = null;
		String time = "";
		List<PmItemInfo> list = new ArrayList<>();
		try {
			DateTimeFormatter dft = DateTimeFormatter.ofPattern("HHmm");
			LocalTime localTime = LocalTime.now(ZoneId.systemDefault());
			time = dft.format(localTime);
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("get-price-pm-info"));
			select.setString(SQLStatement.PARAM1, storeId);
			select.setString(SQLStatement.PARAM2, companyId);
			select.setString(SQLStatement.PARAM3, mdInternal);
			select.setString(SQLStatement.PARAM4, businessDate.replaceAll("-", "") + time);
			result = select.executeQuery();
			while (result.next()) {
				String startTime = result.getString(result.findColumn("SaleStartTime"));
				String endTime = result.getString(result.findColumn("SaleEndTime"));
				boolean timeFlag = false;
				boolean isPmDay = true;
				pmItemInfo = new PmItemInfo();
				
				if(!StringUtility.isNullOrEmpty(startTime,endTime)) {
					if(time.compareToIgnoreCase(startTime) >= 0 && endTime.compareToIgnoreCase(time) >= 0) {
						timeFlag = true;
					}else {
						isPmDay = false;
						continue;
					}
				}else {
					timeFlag = true;
				}
				
				if(DAY_OF_WEEK_SETTING == result.getInt(result.findColumn("DayOfWeekSettingFlag")) && timeFlag) {
					isPmDay = isPmDay(result, businessDate);
				}
				if(isPmDay) {
					pmItemInfo.setDayOfWeekSettingFlag(result.getInt(result.findColumn("DayOfWeekSettingFlag")));
					pmItemInfo.setPmNo(result.getInt(result.findColumn("PMNo")));
					pmItemInfo.setpMCnt(result.getInt(result.findColumn("PMCnt")));
					pmItemInfo.setpMDisplayNo(result.getString(result.findColumn("PMDisplayNo")));
					pmItemInfo.setpMName(result.getString(result.findColumn("PMName")));
					pmItemInfo.setPmPrice(result.getDouble(result.findColumn("PMPrice")));
					pmItemInfo.setPMGroupNo(result.getInt(result.findColumn("PMGroupNo")));
					pmItemInfo.setStartDate(result.getDate(result.findColumn("StartDate")));
					pmItemInfo.setEndDate(result.getDate(result.findColumn("EndDate")));
					pmItemInfo.setSaleStartTime(result.getString(result.findColumn("SaleStartTime")));
					pmItemInfo.setSaleEndTime(result.getString(result.findColumn("SaleEndTime")));
					
					pmItemInfo.setStartTime(result.getString(result.findColumn("StartTime")));
					pmItemInfo.setEndTime(result.getString(result.findColumn("EndTime")));
					
					pmItemInfo.setDayOfWeekMonFlag(result.getInt(result.findColumn("DayOfWeekMonFlag")));
					pmItemInfo.setDayOfWeekTueFlag(result.getInt(result.findColumn("DayOfWeekTueFlag")));
					pmItemInfo.setDayOfWeekWedFlag(result.getInt(result.findColumn("DayOfWeekWedFlag")));
					pmItemInfo.setDayOfWeekThuFlag(result.getInt(result.findColumn("DayOfWeekThuFlag")));
					pmItemInfo.setDayOfWeekFriFlag(result.getInt(result.findColumn("DayOfWeekFriFlag")));
					
					pmItemInfo.setDayOfWeekSatFlag(result.getInt(result.findColumn("DayOfWeekSatFlag")));
					pmItemInfo.setDayOfWeekSunFlag(result.getInt(result.findColumn("DayOfWeekSunFlag")));
					
					pmItemInfo.setDiscountClass(result.getString(result.findColumn("DiscountClass")));
					pmItemInfo.setDecisionPMPrice(result.getDouble(result.findColumn("DecisionPMPrice")));
					pmItemInfo.setPmRate(result.getDouble(result.findColumn("PMRate")));
					pmItemInfo.setDecisionPMRate(result.getDouble(result.findColumn("DecisionPMRate")));
					
					pmItemInfo.setSubNum1(result.getInt(result.findColumn("SubNum1")));

					list.add(pmItemInfo);
				}
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the item information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPmItemInfo ", sqlEx);
		} catch (NumberFormatException nuEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
					"Failed to get the item information.\n" + nuEx.getMessage());
			throw new DaoException("NumberFormatException: @getPmItemInfo ", nuEx);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get the item information.\n" + e.getMessage());
			throw new DaoException("Exception: @getPmItemInfo ", e);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(list);
		}
		return list;
	}
	
	/**
	 * current Day is the PM Day
	 * @param result SQL result
	 * @return  day of PM day
	 * @throws SQLException
	 */
	private boolean isPmDay(ResultSet result, String date) throws SQLException {
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, dtFormatter);
		int dayOfWeek = localDate.getDayOfWeek().getValue();
		boolean dayOfWeekFlag = true;
		switch(dayOfWeek){
        case DAY_OF_WEEKMON:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekMonFlag")) == 1;
            break;
        case DAY_OF_WEEKTUE:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekTueFlag")) == 1;
            break;
        case DAY_OF_WEEKWED:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekWedFlag")) == 1;
        	break;
        case DAY_OF_WEEKTHU:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekThuFlag")) == 1;
        	break;
        case DAY_OF_WEEKFRI:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekFriFlag")) == 1;
        	break;
        case DAY_OF_WEEKSAT:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekSatFlag")) == 1;
        	break;
        case DAY_OF_WEEKSUN:
        	dayOfWeekFlag = result.getInt(result.findColumn("DayOfWeekSatFlag")) == 1;
        	break;
        default:
        	dayOfWeekFlag = false;
            break;
        }
		return dayOfWeekFlag;
	}

}
