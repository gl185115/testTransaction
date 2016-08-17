package ncr.res.mobilepos.promotion.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.promotion.model.GroupMixMatchData;
import ncr.res.mobilepos.promotion.model.NormalMixMatchData;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

/**
 * A Data access object implementation for Mix Match.
 * @author RD185102
 *
 */
public class SQLServerMixMatchDAO extends AbstractDao implements IMixMatchDAO {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "MixMatchDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Identify if date is before or after the current date.
     */
    private enum DATE_PERIOD { 
    	Before, 
    	After 
    }
    /**
     * Default Constructor for SQLServerMixMatchDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerMixMatchDAO() throws DaoException {
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
     * Retrieves mix match information.
     * @param companyid The Company ID
     * @param storeid   The Store ID.
     * @param code      MixMatch code.
     * @return MixMatchData
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final NormalMixMatchData getNormalMixMatchData(final String companyid, final String storeid,
			final String code) throws DaoException {
        tp.methodEnter("getNormalMixMatchData");
		tp.println("StoreId", storeid).println("Code", code);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultSet = null;
        NormalMixMatchData normalMixMatchData = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-MixMatch"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, code);
            resultSet = select.executeQuery();

            if (resultSet.next()) {
            	Date mixMatchStartDate = resultSet.getDate(resultSet.findColumn("StartDate"));
            	Date mixmatchEndDate = resultSet.getDate(resultSet.findColumn("EndDate"));
				if (isInActive(mixMatchStartDate, DATE_PERIOD.After, companyid, storeid)
						|| isInActive(mixmatchEndDate, DATE_PERIOD.Before, companyid, storeid)) {
					LOGGER.logAlert(progname,
							"SQLServerMixMatchDAO.getNormalMixMatchData()",
							Logger.RES_EXCEP_GENERAL,
							"Mix Match Data is not yet active.\n");
                    return null;
            	}
            	
            	normalMixMatchData = new NormalMixMatchData(); 
                normalMixMatchData.setEndDate(mixmatchEndDate);
                normalMixMatchData.setStartDate(mixMatchStartDate);
                normalMixMatchData.setCode(code);
                
                long[] discountprice = new long[2];
                discountprice[0] = resultSet.getLong(
                        resultSet.findColumn("DiscountPrice1"));
                discountprice[1] = resultSet.getLong(
                        resultSet.findColumn("DiscountPrice2"));
                normalMixMatchData.setDiscountprice(discountprice);

                long[] emprice1 = new long[2];
                emprice1[0] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice11"));
                emprice1[1] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice21"));
                normalMixMatchData.setEmpprice1(emprice1);

                long[] emprice2 = new long[2];
                emprice2[0] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice12"));
                emprice2[1] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice22"));
                normalMixMatchData.setEmpprice2(emprice2);

                long[] emprice3 = new long[2];
                emprice3[0] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice13"));
                emprice3[1] = resultSet.getLong(
                        resultSet.findColumn("EmpPrice23"));
                normalMixMatchData.setEmpprice3(emprice3);

                normalMixMatchData.setMustBuyFlag(resultSet.getInt(
                        resultSet.findColumn("MustBuyFlag")));
                normalMixMatchData.setName(resultSet.getString(
                        resultSet.findColumn("Name")));

                int[] quantity = new int[2];
                quantity[0] = resultSet.getInt(
                        resultSet.findColumn("Quantity1"));
                quantity[1] = resultSet.getInt(
                        resultSet.findColumn("Quantity2"));
                normalMixMatchData.setQuantity(quantity);
                normalMixMatchData.setType(resultSet.getInt(resultSet.findColumn("Type")));                
            } else {
                tp.println("Mix Match Data not found.");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getMixMatchData()",
                    Logger.RES_EXCEP_SQL,
                    "Failed to get the Mix Match Data.\n"
                            + e.getMessage());
            throw new DaoException("SQLException: @getMixMatch ", e);
        } catch (SQLStatementException e) {
            LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getMixMatchData()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the Mix Match Data.\n"
                            + e.getMessage());
            throw new DaoException("SQLStatementException: @getMixMatchData ",
                    e);
        } catch (ParseException e) {
        	LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getMixMatchData()",
                    Logger.RES_EXCEP_PARSE,
                    "Failed to get the Mix Match Data.\n"
                            + e.getMessage());
            throw new DaoException("ParseException: @getMixMatchData ",
            		e);
		} finally {
            closeConnectionObjects(connection, select, resultSet);
            
            tp.methodExit(normalMixMatchData);
        }
        return normalMixMatchData;
    }
    
    /**
     * Retrieves group mix match information.
     * @param companyid     The Company ID
     * @param storeid   	The Store ID.
     * @param code1OrCode2  MixMatch code for either code1 or code2.
     * @return GroupMixMatchData
     * @throws DaoException Exception when error occurs.
     */
	@Override
	public GroupMixMatchData getGroupMixMatchData(String companyid, String storeid,
			String code1OrCode2) throws DaoException {

    	tp.methodEnter("getNormalMixMatchData");
        tp.println("StoreId", storeid)
          .println("Code1OrCode2", code1OrCode2);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultSet = null;
        GroupMixMatchData groupMixMatchData = null;
        
        try{
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-GroupMixMatch"));
			select.setString(SQLStatement.PARAM1, storeid);
			select.setString(SQLStatement.PARAM2, code1OrCode2);
			select.setString(SQLStatement.PARAM3, code1OrCode2);
            resultSet = select.executeQuery();

            while(resultSet.next()){
				Date promoEndDate = resultSet.getDate(resultSet
						.findColumn("EndDate"));
				Date promoStartDate = resultSet.getDate(resultSet
						.findColumn("StartDate"));
            	
            	// check if group mixmatch is expired
            	if (isInActive(promoEndDate, DATE_PERIOD.Before, companyid, storeid)) {
            		continue;
            	}            	
            	// check if group mixmatch has commenced
            	if (isInActive(promoStartDate, DATE_PERIOD.After, companyid, storeid)) {
            		continue;
            	}
            	
            	groupMixMatchData = new GroupMixMatchData();            	
            	groupMixMatchData.setCode(resultSet.getString(
            			resultSet.findColumn("GroupCode")));
            	groupMixMatchData.setStartDate(promoStartDate);
            	groupMixMatchData.setEndDate(promoEndDate);
            	groupMixMatchData.setName(resultSet.getString(
            			resultSet.findColumn("Name")));
            	groupMixMatchData.getSubCodes().add(
            			resultSet.getString(
                    			resultSet.findColumn("Code1")));
            	groupMixMatchData.getSubCodes().add(
            			resultSet.getString(
                    			resultSet.findColumn("Code2")));
            	groupMixMatchData.setType(0);
            	
            	// Stop on first valid result.
            	break;
            }
        	
        } catch (SQLException e) {
            LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getGroupMixMatchData()",
                    Logger.RES_EXCEP_SQL,
                    "Failed to get the Group Mix Match Data.\n"
                            + e.getMessage());
            throw new DaoException("SQLException: @getGroupMixMatch ", e);
        } catch (SQLStatementException e) {
            LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getGroupMixMatchData()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the Group Mix Match Dat.\n"
                            + e.getMessage());
            throw new DaoException("SQLStatementException: @getGroupMixMatchData ",
                    e);
        } catch (ParseException e) {
        	LOGGER.logAlert(
                    progname,
                    "SQLServerMixMatchDAO.getGroupMixMatchData()",
                    Logger.RES_EXCEP_PARSE,
                    "Failed to get the Group Mix Match Dat.\n"
                            + e.getMessage());
            throw new DaoException("ParseException: @getGroupMixMatchData ",
            		e);
        } finally {
            closeConnectionObjects(connection, select, resultSet);
            
            tp.methodExit(groupMixMatchData);
        }
        
        return groupMixMatchData;
    }
    
    /**
     * Validates mixmatch startdate or enddate.
     * @param mixMatchDate		StartDate or EndDate.
     * @param datePeriod		Date period.
     * @param companyId         The Company ID
     * @param storeId           The Store ID
     * @return	true or false.
     * @throws DaoException		If there is no businessdate setting.
     * @throws ParseException	If businessdate's Today cannot be parse to "yyyy-MM-dd".	
     */
	private boolean isInActive(Date mixMatchDate, DATE_PERIOD datePeriod, String companyId, String storeId)
			throws DaoException, ParseException {
		SystemSettingResource sysSetting = new SystemSettingResource();
		DateSetting dateSetting = sysSetting.getDateSetting(companyId, storeId).getDateSetting();
		if (dateSetting == null) {
			tp.println("Business date is not set!");
			throw new DaoException("Business date is not set!");
		}

		String dayDate = dateSetting.getToday();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = (Date) formatter.parse(dayDate);
		if(datePeriod.equals(DATE_PERIOD.Before)){
			if (mixMatchDate.before(currentDate)) {
				tp.println("Current Date is: " + currentDate + ". "
						+ "MixMatch End Date is: " + mixMatchDate + ". "
						+ "Mix Match End Date has expired!");
				return true;
			}
		}else if ((datePeriod.equals(DATE_PERIOD.After)) 
		        && (mixMatchDate.after(currentDate))){
				tp.println("Current Date is: " + currentDate + ". "
						+ "MixMatch Start Date is: " + mixMatchDate + ". "
						+ "Mix Match Start Date is before the current date!");
				return true;

		}		
		return false;
	}
	
}
