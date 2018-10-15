package ncr.res.mobilepos.tillinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

public class SQLServerTillInfoDAO  extends AbstractDao implements ITillInfoDAO{
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
    private static final String PROG_NAME = "TilDao";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * SQLStatement to access sql_statement.xml and to be used to get SQLs.
     */
    private SQLStatement sqlStatement;


    private static final String SOD_FLAG_PROCESSING= "9";

    private static final String SOD_FLAG_FINISHED = "1";

    private static final String SOD_STATE_ALLOW = "1";

    private static final String SOD_STATE_FINISHED = "2";

    private static final String SOD_STATE_OTHER_PROCESSING = "3";
    /**
     * Initializes DBManager.
     *
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerTillInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        // Gets Singleton reference from the factory.
        this.sqlStatement = SQLStatement.getInstance();
    }

    /**
     * Retrieves DBManager.
     *
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    /**
     * Fetch only one record by primary key.
     * @param companyId
     * @param storeId
     * @param tillId
     * @return Found till or null for not found.
     * @throws DaoException
     */
    @Override
    public final Till fetchOne(final String companyId, final String storeId, final String tillId)
        throws DaoException {
        final String thisMethodName = "SQLServerTillInfoDAO.fetchOne";

        tp.methodEnter("fetchOne");
        tp.println("companyId", companyId);
        tp.println("storeId", storeId);
        tp.println("tillId", tillId);

        Till till = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            statement = connection.prepareStatement(this.sqlStatement.getProperty("fetch-one-till"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, tillId);
            result = statement.executeQuery();
            if (result.next()) {
                // Found.
                till = new Till();
                till.setCompanyId(result.getString("CompanyId"));
                till.setStoreId(result.getString("StoreId"));
                till.setTillId(result.getString("TillId"));
                till.setTerminalId(result.getString("TerminalId"));
                till.setBusinessDayDate(result.getString("BusinessDayDate"));
                till.setSaveBusinessDayDate(result.getString("SaveBusinessDayDate"));
                till.setSodFlag(result.getString("SodFlag"));
                till.setEodFlag(result.getString("EodFlag"));
                // Ignores the rest of columns.
            } else {
                // Not found.
                tp.println("Till not found.");
            }

        } catch (SQLException sqle) {
            LOGGER.logAlert(PROG_NAME, thisMethodName, Logger.RES_EXCEP_SQL,
                    "Failed to fetch single till#"
                            + "CompanyId:" + companyId + ":StoreId:" + storeId + ":TillId:" + tillId + ":"
                            + sqle.getMessage());
            throw new DaoException("SQLException: @SQLServerTillInfoDAO.fetchOne", sqle);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(till);
        }
        return till;
    }

    /*
     * view till details.
     *
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#viewTill(java.lang.String)
     */
    @Override
    public final ViewTill viewTill(final String storeID, String tillID)
            throws DaoException {

        String functionName = "SQLServerTillInfoDAO.viewTill";

        tp.methodEnter("viewTill");
        tp.println("storeID", storeID);
        tp.println("tillID", tillID);

        Till till = new Till();
        till.setStoreId(storeID);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        ViewTill viewTill = new ViewTill();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-till-info"));

            select.setString(SQLStatement.PARAM1, storeID);
            select.setString(SQLStatement.PARAM2, tillID);

            result = select.executeQuery();

            if (result.next()) {

                till.setStoreId(result.getString("StoreId"));
                till.setTillId(result.getString("TillId"));
                till.setBusinessDayDate(result.getString("BusinessDayDate"));
                till.setSodFlag(result.getString("SodFlag"));
                till.setEodFlag(result.getString("EodFlag"));

            } else {
                till.setStoreId(storeID);
                till.setTillId(tillID);

                viewTill.setNCRWSSResultCode(ResultBase.RES_TILL_NOT_EXIST);
                tp.println("Till not found.");
            }


        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to View Till#" + storeID + " : "
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @SQLServerTillInfoDAO"
                    + ".viewTill - Error view store", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to View Till#" + storeID + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerStoreDAO"
                    + ".viewTill - Error view till", ex);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(till);
        }

        viewTill.setTill(till);
        return viewTill;
    }
    /*
     * create till info.
     *
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#createTill(java.lang.String)
     */
	@Override
	public ResultBase createTill(String storeId, String tillId, Till till)
			throws DaoException {
        String functionName = "SQLServerTillInfoDAO.createTill";
        tp.methodEnter("createTill");
        tp.println("storeId", storeId);
        tp.println("tillId", tillId);
        tp.println("till", till);

        Connection connection = null;
        PreparedStatement insert = null;
        ResultBase res = new ResultBase();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            insert = connection.prepareStatement(sqlStatement
                    .getProperty("create-till-info"));

            insert.setString(SQLStatement.PARAM1, storeId);
            insert.setString(SQLStatement.PARAM2, tillId);
            insert.setString(SQLStatement.PARAM3, till.getBusinessDayDate());
            insert.setString(SQLStatement.PARAM4, till.getSodFlag());
            insert.setString(SQLStatement.PARAM5, till.getEodFlag());
            insert.setString(SQLStatement.PARAM6, till.getUpdAppId());
            insert.setString(SQLStatement.PARAM7, till.getUpdOpeCode());

            int result = insert.executeUpdate();
	          if (result > 0) {
	        	  res.setNCRWSSResultCode(ResultBase.RES_STORE_OK);
	          }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to add Till\n " + e.getMessage());
            rollBack(connection, "@SQLServerTillInfoDAO.createTill ", e);
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) == e
                    .getErrorCode()) {
            	res.setNCRWSSResultCode(ResultBase.RES_TILL_EXISTS);
                tp.println("Store entry is duplicated.");
            }else{
            	throw new DaoException("SQLException:"
                        + "@SQLServerTillInfoDAO.createTill ", e);
            }

        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to add Store\n " + e.getMessage());
            rollBack(connection, "@SQLServerTillInfoDAO.createTill ", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerTillInfoDAO.createTill ", e);
        } finally {
            closeConnectionObjects(connection, insert);

            tp.methodExit(res);
        }
        return res;
	}

    /**
     * Updates specific fields of Till when daily operations are triggered, SOD or EOD.
     * @param currentTill Till to have current data
     * @param updatingTill Till to have updating data
     * @return ResultBase
     * @throws DaoException
     */
    @Override
    public ResultBase updateTillDailyOperation(Till currentTill, Till updatingTill)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
                .println("current till", currentTill)
                .println("updating till", updatingTill);

        Connection connection = null;
        PreparedStatement updateTill = null;
        ResultSet result = null;
        ResultBase resultBase = new ResultBase();

        try {
            connection = dbManager.getConnection();
            updateTill = connection.prepareStatement(
                    this.sqlStatement.getProperty("update-till-operation-flag"));
            // Set
            updateTill.setString(SQLStatement.PARAM1, updatingTill.getTerminalId());
            updateTill.setInt(SQLStatement.PARAM2, updatingTill.getSodFlagAsShort());
            updateTill.setInt(SQLStatement.PARAM3, updatingTill.getEodFlagAsShort());
            updateTill.setString(SQLStatement.PARAM4, updatingTill.getBusinessDayDate());
            updateTill.setString(SQLStatement.PARAM5, updatingTill.getSaveBusinessDayDate());
            updateTill.setString(SQLStatement.PARAM6, updatingTill.getUpdAppId());
            updateTill.setString(SQLStatement.PARAM7, updatingTill.getUpdOpeCode());
            // Where
            updateTill.setString(SQLStatement.PARAM8, currentTill.getCompanyId());
            updateTill.setString(SQLStatement.PARAM9, currentTill.getStoreId());
            updateTill.setString(SQLStatement.PARAM10, currentTill.getTillId());
            updateTill.setInt(SQLStatement.PARAM11, currentTill.getSodFlagAsShort());
            updateTill.setInt(SQLStatement.PARAM12, currentTill.getEodFlagAsShort());

            int updateCount = updateTill.executeUpdate();
            if(updateCount == 0) {
                // Optimistic locking error due to race condition.
                // Other terminal is already processing EOD.
                resultBase.setNCRWSSResultCode(ResultBase.RES_OPTIMISTIC_LOCKING_ERROR);
                resultBase.setMessage("EodFlag has been changed, Optimistic locking error");
                tp.println("Failed to update EOD Till Info.");
            }
            connection.commit();
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to update EOD Till Info.", ex);
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, updateTill, result);
            tp.methodExit(resultBase);
        }
        return resultBase;
    }

    /**
     * Updates specific fields of Till after successful SOD/EOD.
     * @see ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO#updateTillOnJourn(Connection,Till, Till, boolean)
     */
    @Override
    public void updateTillOnJourn(Connection connection,Till currentTill, Till updatingTill, boolean isEnterprise)
    		throws DaoException, TillException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("updateTillOnJourn");
        tp.println("Updating till", updatingTill);
        tp.println("oldSodFlag", currentTill.getSodFlag());
        tp.println("oldEodFlag", currentTill.getEodFlag());

        PreparedStatement updateTill = null;
        ResultSet result = null;

        try {
            String stringSqlStatement = isEnterprise ? "update-till-on-journ-updlock-ent" : "update-till-on-journ-updlock";

            updateTill = connection.prepareStatement(
                    this.sqlStatement.getProperty(stringSqlStatement));
            updateTill.setString(SQLStatement.PARAM1, currentTill.getStoreId());
            updateTill.setString(SQLStatement.PARAM2, currentTill.getTillId());
            updateTill.setInt(SQLStatement.PARAM3, currentTill.getSodFlagAsShort());
            updateTill.setInt(SQLStatement.PARAM4, updatingTill.getSodFlagAsShort());
            updateTill.setInt(SQLStatement.PARAM5, currentTill.getEodFlagAsShort());
            updateTill.setInt(SQLStatement.PARAM6, updatingTill.getEodFlagAsShort());
            updateTill.setString(SQLStatement.PARAM7, updatingTill.getBusinessDayDate());
            updateTill.setString(SQLStatement.PARAM8, updatingTill.getUpdOpeCode());
            updateTill.setString(SQLStatement.PARAM9, currentTill.getCompanyId());

            result = updateTill.executeQuery();
            if(!result.next()) {
                //Concurrent access scenario. Failed to update till.
                throw new TillException("TillException: @SQLServerTillInfoDAO." + functionName +
                        " Failed to update till. Other terminal is processing the current till.",
                        ResultBase.RES_TILL_NO_UPDATE);
            }
            //connection.commit() is commented out, journalization resource execute the commit functionality.
            //so that rollback functionality will work.
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to update till.", ex);
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName, ex);
        } catch (TillException ex) {
        	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_TILL, functionName
                    + ": Failed to update till. Other terminal is processing the current till.", ex);
        	throw new TillException("TillException: @SQLServerTillInfoDAO." + functionName, ex,
        			ex.getErrorCode());
        } finally {
            //rollback functionlity will work on same connection. it's neccessary not to close it until
            //saving of poslog is done.
            closeConnectionObjects(null, updateTill, result);
            tp.methodExit();
        }
    }

    @Override
    public ResultBase searchLogonUsers(String companyId, String storeId, String tillId,
    		String terminalId) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("tillId", tillId)
        	.println("terminalId", terminalId);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        ResultBase resultBase = new ResultBase();

        try {
        	connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("search-logon-users"));

            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, tillId);
            statement.setString(SQLStatement.PARAM4, terminalId);

            result = statement.executeQuery();

            if (result.next()) {
                resultBase.setNCRWSSResultCode(
                		ResultBase.RES_TILL_OTHER_USERS_SIGNED_ON);
                resultBase.setMessage("Other users connected to till are "
                		+ "still signed on.");
                tp.println("Other users connected to till are still "
                		+ "signed on.");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search for logon users.", e);
            throw new DaoException("SQLException: @SQLServerTillInfoDAO."
            		+ functionName, e);
        } finally {
        	closeConnectionObjects(connection, statement, result);
        	tp.methodExit(resultBase);
        }

    	return resultBase;
    }

    /*
     * get till information.
     *
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#getTillInfoList(java.lang.String)
     */
    @Override
	public  List<Till> getTillInformation(String storeId)
			throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();

        tp.methodEnter(functionName);
        tp.println("StoreId", storeId);

        Connection connection = null;
        PreparedStatement selectStmnt = null;
        ResultSet result = null;
        List<Till> tillInfoList = null;

        try {

        	connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-till-information-list"));
            selectStmnt.setString(SQLStatement.PARAM1,storeId);
			result = selectStmnt.executeQuery();

			while(result.next()){
				if(tillInfoList == null){
					tillInfoList = new ArrayList<Till>();
				}
				Till till = new Till();
				till.setStoreId(result.getString("StoreId"));
				till.setTillId(result.getString("TillId"));
				till.setTerminalId(result.getString("TerminalId"));
				till.setBusinessDayDate(result.getString("BusinessDayDate"));
				till.setSodFlag(result.getString("SodFlag"));
				till.setEodFlag(result.getString("EodFlag"));
				tillInfoList.add(till);
			}

        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get till infomation.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerTillInfoDAO.getTillInformation", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get till infomation.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerTillInfoDAO.getTillInformation", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, result);
            tp.methodExit(tillInfoList);
        }
        return tillInfoList;
    }

    /**
     * Populates a list of Tills from ResultSet
     * @param resultSet
     * @return List of Tills
     */
	protected List<Till> populateResultTillList(ResultSet resultSet) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		List<Till> resultList = new ArrayList<Till>();
		try {
			while(resultSet.next()){
				Till till = new Till();
				till.setCompanyId(resultSet.getString("CompanyId"));
				till.setStoreId(resultSet.getString("StoreId"));
				till.setTillId(resultSet.getString("TillId"));
				till.setTerminalId(resultSet.getString("TerminalId"));
				till.setDeviceName(resultSet.getString("DeviceName"));
				till.setBusinessDayDate(resultSet.getString("BusinessDayDate"));
				till.setSodFlag(resultSet.getString("SodFlag"));
				till.setEodFlag(resultSet.getString("EodFlag"));
				
				if ("0".equals(till.getEodFlag())) {
					till.setEodSummary("0");
				} else {
					getEodSummary(till);
				}
				resultList.add(till);
			}
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get populateResultTillList infomation.", e);
			throw new DaoException("SQLException:"
					+ " @SQLServerTillInfoDAO.populateResultTillList", e);
		}
		return resultList;
	}
	
	/**
	 * Get EodSummary from TXL_SALES_JOURNAL
	 * @param till
	 * @return Till
	 */
	private void getEodSummary(Till till) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();

		tp.methodEnter(functionName);
		tp.println("Till", till);

		Connection connection = null;
		PreparedStatement selectStmnt = null;
		ResultSet result = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-eodsummary"));
			selectStmnt.setString(SQLStatement.PARAM1,till.getCompanyId());
			selectStmnt.setString(SQLStatement.PARAM2,till.getStoreId());
			selectStmnt.setString(SQLStatement.PARAM3,till.getTerminalId());
			selectStmnt.setString(SQLStatement.PARAM4,till.getBusinessDayDate());
			result = selectStmnt.executeQuery();

			while(result.next()){
				till.setEodSummary(result.getString("Status"));
			}

		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get eodsummary infomation.", sqlEx);
			throw new DaoException("SQLException:"
					+ " @SQLServerTillInfoDAO.getEodSummary", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get eodsummary infomation.", ex);
			throw new DaoException("Exception:"
					+ " @SQLServerTillInfoDAO.getEodSummary", ex);
		} finally {
			closeConnectionObjects(connection, selectStmnt, result);
			tp.methodExit(till);
		}
	}

    /**
     * Populates PreparedStatement with given SQL name.
     * @param connection DB-connection
     * @param sqlName SQL name in sql_statement.xml.
     * @param companyId
     * @param storeId
     * @param businessDate with yyyy-MM-dd format.
     * @param trainingFlag
     * @return
     * @throws SQLException
     */
    protected PreparedStatement prepareStatementTillsOnBusinessDay(
                                                            Connection connection,
                                                            String sqlName,
                                                            String companyId,
                                                            String storeId,
                                                            String businessDate,
                                                            int trainingFlag) throws SQLException {
        // Creates PreparedStatement from SQL name.
        PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty(sqlName));
        statement.setInt(SQLStatement.PARAM1, trainingFlag);
        statement.setString(SQLStatement.PARAM2, companyId);
        statement.setString(SQLStatement.PARAM3, storeId);
        statement.setString(SQLStatement.PARAM4, businessDate);
        return statement;
    }

    /**
     * Returns activated (done SOD) tills on the given business day regardless it is still open or closed.
     * @see ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO#getActivatedTillsOnBusinessDay(String, String, String, int)
     */
    @Override
    public  List<Till> getActivatedTillsOnBusinessDay(String companyId, String storeId, String businessDate, int trainingFlag)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyId);
        tp.println("StoreId", storeId);
        tp.println("BusinessDate", businessDate);

        List<Till> tillInfoList = null;
        try(Connection connection = dbManager.getConnection();
            PreparedStatement statement = prepareStatementTillsOnBusinessDay(connection,
                    "get-activated-tills-on-businessday", companyId, storeId, businessDate, trainingFlag);
            ResultSet result = statement.executeQuery()){
            tillInfoList = populateResultTillList(result);
        } catch (SQLException sqlEx) {
            // From SQLException to DaoException.
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get till infomation.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerTillInfoDAO.getTillInformation", sqlEx);
        } finally {
            tp.methodExit(tillInfoList);
        }
        return tillInfoList;
    }

    /*
     * Select unclosed (before EOD) tills on a given businessDate.
     * @see ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO#getUnclosedTillsOnBusinessDay(String, String, String, int)
     */
    @Override
    public List<Till> getUnclosedTillsOnBusinessDay(String companyId, String storeId, String businessDate, int trainingFlag)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyId);
        tp.println("StoreId", storeId);
        tp.println("BusinessDate", businessDate);

        List<Till> tillInfoList = null;
        try(Connection connection = dbManager.getConnection();
            PreparedStatement statement = prepareStatementTillsOnBusinessDay(connection,
                    "get-unclosed-tills-on-businessday", companyId, storeId, businessDate, trainingFlag);
            ResultSet result = statement.executeQuery()){
            tillInfoList = populateResultTillList(result);
        } catch (SQLException sqlEx) {
            // From SQLException to DaoException.
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get till infomation.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerTillInfoDAO.getTillInformation", sqlEx);
        } finally {
            tp.methodExit(tillInfoList);
        }
        return tillInfoList;
    }

}
