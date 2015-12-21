package ncr.res.mobilepos.settlement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.settlement.model.CreditInfo;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.model.VoucherInfo;

public class SQLServerSettlementInfoDAO extends AbstractDao implements ISettlementInfoDAO {
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
    private static final String PROG_NAME = "SettlementInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    
    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerSettlementInfoDAO() throws Exception {
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
    public SettlementInfo getCreditSummary(String companyId, String storeId, 
    		String businessDayDate, int trainingFlag) throws Exception {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName)
    		.println("companyId", companyId)
    		.println("storeId", storeId)
    		.println("businessDayDate", businessDayDate)
    		.println("trainingFlag", trainingFlag);
    
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet result = null;
    	SettlementInfo settlement = new SettlementInfo();
    
    	try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
            		sqlStatement.getProperty("get-credit-summary"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, businessDayDate);
            statement.setInt(SQLStatement.PARAM4, trainingFlag);
            result = statement.executeQuery();
            if (result.next()) {
            	CreditInfo creditInfo = new CreditInfo();
            	creditInfo.setCompanyId(result.getString("CompanyId"));
            	creditInfo.setStoreId(result.getString("RetailStoreId"));
            	creditInfo.setBusinessDayDate(result.getString("BusinessDayDate"));
            	creditInfo.setTrainingFlag(result.getInt("TrainingFlag"));
            	creditInfo.setSalesCntSum(result.getInt("CntSum"));
            	creditInfo.setSalesAmtSum(result.getDouble("AmtSum"));
            	settlement.setCreditInfo(creditInfo);
            } else {
            	tp.println("Credit summary not found.");
            	settlement.setNCRWSSResultCode(ResultBase.RES_CREDIT_SUMMARY_NOT_FOUND);
            	settlement.setMessage("Credit summary not found.");
            }
    	} catch (Exception e) {
    		LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
    				+ ": Failed to get credit summary.", e);
    		throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
    				+ functionName, e);
        }  finally {
        	closeConnectionObjects(connection, statement, result);
        	tp.methodExit(settlement);
        }
    	return settlement;
    }
    
    @Override
    public SettlementInfo getVoucherList(String companyId, String storeId, 
    		String businessDayDate, int trainingFlag) throws Exception {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName)
    		.println("companyId", companyId)
    		.println("storeId", storeId)
    		.println("businessDayDate", businessDayDate)
    		.println("trainingFlag", trainingFlag);
    
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet result = null;
    	SettlementInfo settlement = new SettlementInfo();
    	List<VoucherInfo> voucherList = new ArrayList<VoucherInfo>();
    	
    	try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
            		sqlStatement.getProperty("get-voucher-list"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, businessDayDate);
            statement.setInt(SQLStatement.PARAM4, trainingFlag);
            result = statement.executeQuery();
            while (result.next()) {
            	VoucherInfo voucherInfo = new VoucherInfo();
            	voucherInfo.setCompanyId(result.getString("CompanyId"));
            	voucherInfo.setStoreId(result.getString("StoreId"));
            	voucherInfo.setVoucherCompanyId(result.getString("TenderId"));
            	voucherInfo.setVoucherType(result.getString(("TenderType")));
            	voucherInfo.setVoucherName(result.getString("TenderName"));
            	voucherInfo.setVoucherKanaName(result.getString("TenderKanaName"));
            	voucherInfo.setBusinessDayDate(result.getString("BusinessDayDate"));
            	voucherInfo.setTrainingFlag(result.getInt("TrainingFlag"));
            	voucherInfo.setSalesItemCnt(result.getInt("SalesItemCnt"));
            	voucherInfo.setSalesItemAmt(result.getDouble("SalesItemAmt"));
            	voucherList.add(voucherInfo);
            }
            settlement.setVoucherList(voucherList);
            if (voucherList.isEmpty()) {
            	tp.println("Voucher list not found.");
            	settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	settlement.setMessage("Voucher list not found.");
            }
    	} catch (Exception e) {
    		LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
    				+ ": Failed to get voucher list.", e);
    		throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
    				+ functionName, e);
        }  finally {
        	closeConnectionObjects(connection, statement, result);
        	tp.methodExit(settlement);
        }
    	return settlement;
    }
    
    /* (non-Javadoc)
     * @see ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO#getTransactionCount(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public SettlementInfo getTransactionCount(String companyId, String storeId, String txtype, int trainingFlag)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("companyId", companyId)
        .println("storeId", storeId)
        .println("trainingFlag", trainingFlag);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        SettlementInfo settlement = new SettlementInfo();
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-transaction-count"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, txtype);
            statement.setInt(SQLStatement.PARAM4, trainingFlag);
            result = statement.executeQuery();
            
            if (result.next()) {
                settlement.setTxCount(result.getInt("TxCount"));
            } else {
                tp.println("EOD count not found.");
                settlement.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                settlement.setMessage("Tx count not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
                    + ": Failed to get Tx count.", e);
            throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
                    + functionName, e);
        }  finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(settlement);
        }
        return settlement;
    }
    
    
    
}
