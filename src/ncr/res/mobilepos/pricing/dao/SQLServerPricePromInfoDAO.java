package ncr.res.mobilepos.pricing.dao;

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
    private static final String FLAG_ON = "1";
    private static final String FLAG_OFF = "0";

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
	 *        dayDate
     * @return PricePromList
     * @throws DaoException Exception when error occurs.
     */
	@Override
	public List<PricePromInfo> getPricePromInfoList(String companyId, String storeId, String dayDate)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
        tp.println("storeId", storeId);
        tp.println("dayDate", dayDate);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        List<PricePromInfo> PricePromList = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-price-prom-info-list"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, dayDate);
            result = select.executeQuery();

            PricePromInfo pricePromInfo = null;
            while(result.next()){
            	if (IsPricePromEnabled(dayDate,
                        result.getString("DayOfWeekSettingFlag"), result.getString("DayOfWeekMonFlag"), result.getString("DayOfWeekTueFlag"),
                        result.getString("DayOfWeekWedFlag"), result.getString("DayOfWeekThuFlag"), result.getString("DayOfWeekFriFlag"),
                        result.getString("DayOfWeekSatFlag"), result.getString("DayOfWeekSunFlag"))) {

            		if (PricePromList == null){
                        PricePromList = new ArrayList<PricePromInfo>();
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

                    PricePromList.add(pricePromInfo);

            	}
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the PriceProm information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPricePromInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the PriceProm information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getPricePromInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the PriceProm information.\n" + e.getMessage());
            throw new DaoException("Exception: @getPricePromInfo ", e);
        } finally {
        	closeConnectionObjects(connection, select, result);
            tp.methodExit(PricePromList);
        }

        return PricePromList;
	}

	private boolean IsPricePromEnabled(String businessDate,String weekflag,
			String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {

		if (weekflag.equals(FLAG_OFF)) {
			return true;
		}

		String[] date = businessDate.split("-");
		Calendar cal = Calendar.getInstance();
	    cal.clear();
	    cal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));

	    if (weekflag.equals(FLAG_ON)) {
		    switch (cal.get(Calendar.DAY_OF_WEEK)) {
		    case Calendar.SUNDAY:
		    	if (sunday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.MONDAY:
		            if (monday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.TUESDAY:
		            if (tuesday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.WEDNESDAY:
		            if (wednesday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.THURSDAY:
		            if (thursday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.FRIDAY:
		            if (friday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		        case Calendar.SATURDAY:
		            if (saturday.equals(FLAG_ON)) {
		                return true;
		            }
		            break;
		    }
	    }
	    return false;
	}

}
