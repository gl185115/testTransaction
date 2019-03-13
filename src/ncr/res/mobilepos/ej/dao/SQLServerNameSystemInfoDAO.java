package ncr.res.mobilepos.ej.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.ej.model.EjInfo;
import ncr.res.mobilepos.ej.model.PosLogInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerNameSystemInfoDAO extends AbstractDao implements INameSystemInfoDAO{
	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "SQLServerNameSystemInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private List<EjInfo> listNameSystemInfo = null;

    /**
     * Default Constructor for SQLServerMixMatchDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerNameSystemInfoDAO() throws DaoException {
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
     * Get NameSystemInfo from MST_NAME_SYSTEM
     *
     * @param void
     *
     * @return List<EjInfo>
     *
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final List<EjInfo> getNameSystemInfo() throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        listNameSystemInfo = null;
        EjInfo nameSystemInfo = null;
        try {
        	listNameSystemInfo = new  ArrayList<EjInfo>();

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-name-system-info"));
            result = select.executeQuery();

            while(result.next()){
            	nameSystemInfo = new EjInfo();
            	nameSystemInfo.setCompanyId(result.getString(result.findColumn("CompanyId")));
            	nameSystemInfo.setStoreId(result.getString(result.findColumn("StoreId")));
            	nameSystemInfo.setNameCategory(result.getString(result.findColumn("NameCategory")));
                nameSystemInfo.setTxType(result.getString(result.findColumn("NameId")));
                nameSystemInfo.setTxTypeName(result.getString(result.findColumn("NameText")));
                listNameSystemInfo.add(nameSystemInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the system name information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getNameSystemInfo ", sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the system name information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getNameSystemInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the system name information.\n" + e.getMessage());
            throw new DaoException("Exception: @getNameSystemInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(listNameSystemInfo);
        }
        return listNameSystemInfo;
    }

	/**
	 * Get List EJ Info
	 *
	 * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param TxType
     * @param SequencenumberFrom
     * @param SequencenumberTo
     * @param BusinessDateTimeFrom
     * @param BusinessDateTimeTo
     * @param OperatorId
     * @param SalesPersonId
     * @param TrainingFlag
     *
	 * @return List<EjInfo>
	 * @throws DaoException
	 *
	 * The exception thrown when searching failed.
	 */
    @Override
    public List<EjInfo> getEjInfo(String CompanyId,String RetailStoreId,String WorkstationId,String TxType,String SequencenumberFrom,String SequencenumberTo,
    		String BusinessDateTimeFrom,String BusinessDateTimeTo,String OperatorId,String SalesPersonId, String TrainingFlag) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		EjInfo ejInfo = null;
		List<EjInfo> listEjInfo = new ArrayList<EjInfo>();
		StringBuffer sqlCondition = new StringBuffer("");

		// Add Sql Condition
		if(!StringUtility.isNullOrEmpty(OperatorId)){
			sqlCondition.append(" AND tr.OperatorId = " + OperatorId);
		}
		if(!StringUtility.isNullOrEmpty(SalesPersonId)){
			sqlCondition.append(" AND detail.SalesClerkCd = " + SalesPersonId);
		}
		if(!StringUtility.isNullOrEmpty(TxType)){
			sqlCondition.append(" AND tr.TxType = " + TxType);
		}
		if(StringUtility.isNullOrEmpty(TrainingFlag)){
			sqlCondition.append(" AND tr.TrainingFlag = " + 0);
		}else{
			sqlCondition.append(" AND tr.TrainingFlag = " + TrainingFlag);
		}

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			String query = String.format(sqlStatement.getProperty("get-ej-info"), sqlCondition);
			select = connection.prepareStatement(query);
			select.setString(SQLStatement.PARAM1, CompanyId);
			select.setString(SQLStatement.PARAM2, RetailStoreId);
			select.setString(SQLStatement.PARAM3, WorkstationId);
			select.setString(SQLStatement.PARAM4, TxType);
			select.setString(SQLStatement.PARAM5, SequencenumberFrom);
			select.setString(SQLStatement.PARAM6, SequencenumberTo);
			select.setString(SQLStatement.PARAM7, BusinessDateTimeFrom);
			select.setString(SQLStatement.PARAM8, BusinessDateTimeTo);
			select.setString(SQLStatement.PARAM9, OperatorId);
			select.setString(SQLStatement.PARAM10, SalesPersonId);
			select.setString(SQLStatement.PARAM11, TrainingFlag);

			result = select.executeQuery();
			while (result.next()) {
				ejInfo = new EjInfo();
				ejInfo.setTxType(result.getString(result.findColumn("TxType")));
				ejInfo.setBillingAmt(result.getString(result.findColumn("BillingAmt")));
				ejInfo.setBusinessDateTime(result.getString(result.findColumn("BusinessDateTimeStart")));
				ejInfo.setSequenceNumber(result.getString(result.findColumn("SequenceNumber")));
				ejInfo.setWorkstationId(result.getString(result.findColumn("WorkstationId")));
				listEjInfo.add(ejInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the Ej Info.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getEjInfo ", sqlEx);
		} catch (NumberFormatException nuEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
					"Failed to get the Ej Info.\n" + nuEx.getMessage());
			throw new DaoException("NumberFormatException: @getEjInfo ", nuEx);
		} catch (Exception e) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get the Ej Info.\n" + e.getMessage());
			throw new DaoException("Exception: @getEjInfo ", e);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(listEjInfo);
		}
		return listEjInfo;
	}

	/**
	 * Get PosLog Info
	 *
	 * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param SequencenumberTo
     * @param BusinessDateTimeFrom
     * @param TrainingFlag
     *
	 * @return PosLogInfo
	 *
	 * @throws DaoException
	 *
	 * The exception thrown when searching failed.
	 */
    public PosLogInfo getPosLogInfo(String CompanyId, String RetailStoreId, String WorkstationId, String Sequencenumber,
			String BusinessDateTime, String TrainingFlag) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		PosLogInfo posLogInfo = new PosLogInfo();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty(
					"get-poslog-xml-by-companyid-storeid-workstationid-bussinessdate-seqnum-trainningflag"));
			select.setString(SQLStatement.PARAM1, CompanyId);
			select.setString(SQLStatement.PARAM2, RetailStoreId);
			select.setString(SQLStatement.PARAM3, WorkstationId);
			select.setString(SQLStatement.PARAM4, Sequencenumber);
			select.setString(SQLStatement.PARAM5, BusinessDateTime);
			select.setString(SQLStatement.PARAM6, TrainingFlag);

			result = select.executeQuery();
			if (result.next()) {
				posLogInfo.setCompanyId(CompanyId);
				posLogInfo.setRetailStoreId(RetailStoreId);
				posLogInfo.setBusinessDate(BusinessDateTime);
				posLogInfo.setPOSLog(result.getString(result.findColumn("Tx")));
				posLogInfo.setTrainingFlag(Integer.parseInt(TrainingFlag));
				posLogInfo.setWorkstationId(WorkstationId);
				posLogInfo.setSequenceNumber(Sequencenumber);
			}else{
				posLogInfo.setNCRWSSExtendedResultCode(ResultBase.RES_POSLOG_NOTFOUND);
				posLogInfo.setNCRWSSResultCode(ResultBase.RES_POSLOG_NOTFOUND);
				posLogInfo.setMessage("PosLog info search failed");
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the PosLog Info.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPosLogInfo ", sqlEx);
		} catch (NumberFormatException nuEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
					"Failed to get the PosLog Info.\n" + nuEx.getMessage());
			throw new DaoException("NumberFormatException: @getPosLogInfo ", nuEx);
		} catch (Exception e) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get the PosLog Info.\n" + e.getMessage());
			throw new DaoException("Exception: @getPosLogInfo ", e);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(posLogInfo);
		}
		return posLogInfo;
	}
}
