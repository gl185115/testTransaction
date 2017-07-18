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
import ncr.res.mobilepos.settlement.model.PaymentAmtInfo;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.model.VoucherDetails;
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
    public SettlementInfo getVoucherList(String companyId, String storeId, 
    		String businessDayDate, String terminalId, int trainingFlag) throws Exception {
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
            	voucherInfo.setStoreId(storeId);
            	voucherInfo.setVoucherCompanyId(result.getString("TenderId"));
            	voucherInfo.setVoucherType(result.getString(("TenderType")));
            	voucherInfo.setVoucherName(result.getString("TenderName"));
            	voucherInfo.setVoucherKanaName(result.getString("TenderKanaName"));
             	voucherInfo.setVoucherDetails(this.getVoucherDetails(companyId, storeId, terminalId, 
            	        businessDayDate, trainingFlag, voucherInfo.getVoucherCompanyId()));
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
    @Override
    public SettlementInfo getVoucherListByTillId(String companyId, String storeId, 
    		 String businessDayDate, String tillId, int trainingFlag) throws Exception {
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
            	voucherInfo.setStoreId(storeId);
            	voucherInfo.setVoucherCompanyId(result.getString("TenderId"));
            	voucherInfo.setVoucherType(result.getString(("TenderType")));
            	voucherInfo.setVoucherName(result.getString("TenderName"));
            	voucherInfo.setVoucherKanaName(result.getString("TenderKanaName"));
             	voucherInfo.setVoucherDetails(this.getVoucherDetailsByTillId(companyId, storeId, tillId, 
            	        businessDayDate, trainingFlag, voucherInfo.getVoucherCompanyId()));
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
    
    /* (non-Javadoc)
     * @see ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO#getTxCountByBusinessDate(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public SettlementInfo getTxCountByBusinessDate(String companyId, String storeId, String workStationId,
    		String txtype, String businessDate, int trainingFlag)throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("companyId", companyId)
        .println("storeId", storeId)
        .println("workStationId", workStationId)
        .println("txtype", txtype)
        .println("businessDate", businessDate)
        .println("trainingFlag", trainingFlag);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        SettlementInfo settlement = new SettlementInfo();
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-tx-count-by-businessdate"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, workStationId);
            statement.setString(SQLStatement.PARAM4, txtype);
            statement.setString(SQLStatement.PARAM5, businessDate);
            statement.setInt(SQLStatement.PARAM6, trainingFlag);
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
    
    @Override
    public SettlementInfo getCredit(String companyId, String storeId, String terminalId, String businessDate,
            int trainingFlag, String dataType, String itemLevel1, String itemLevel2) throws Exception {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("terminalId", terminalId)
          .println("businessDate", businessDate)
          .println("trainingFlag", trainingFlag)
          .println("dataType", dataType)
          .println("itemLevel1", itemLevel1)
          .println("itemLevel2", itemLevel2);
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        SettlementInfo settlement = new SettlementInfo();
    
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-report-item"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, terminalId);
            statement.setString(SQLStatement.PARAM4, businessDate);
            statement.setInt(SQLStatement.PARAM5, trainingFlag);
            statement.setString(SQLStatement.PARAM6, dataType);
            statement.setString(SQLStatement.PARAM7, itemLevel1);
            statement.setString(SQLStatement.PARAM8, itemLevel2);
            
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
    public SettlementInfo getCreditByTillId(String companyId, String storeId, String tilleId,
    		String businessDate, int trainingFlag, String dataType, String itemLevel1, String itemLevel2) throws Exception {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("tilleId", tilleId)
          .println("businessDate", businessDate)
          .println("trainingFlag", trainingFlag)
          .println("dataType", dataType)
          .println("itemLevel1", itemLevel1)
          .println("itemLevel2", itemLevel2);
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        SettlementInfo settlement = new SettlementInfo();
    
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-report-item-tillId"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, tilleId);
            statement.setString(SQLStatement.PARAM4, businessDate);
            statement.setInt(SQLStatement.PARAM5, trainingFlag);
            statement.setString(SQLStatement.PARAM6, dataType);
            statement.setString(SQLStatement.PARAM7, itemLevel1);
            statement.setString(SQLStatement.PARAM8, itemLevel2);
            
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
    /* (non-Javadoc)
     * @see ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO#getVoucherDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, int, int, int)
     */
    @Override
    public List<VoucherDetails> getVoucherDetails(String companyId, String storeId, String terminalId,
            String businessDate, int trainingFlag, String itemLevel3)
                    throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("companyId", companyId)
        .println("storeId", storeId)
        .println("terminalId", terminalId)
        .println("businessDayDate", businessDate)
        .println("trainingFlag", trainingFlag)
        .println("itemLevel3", itemLevel3);

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet result = null;
    List<VoucherDetails> voucherDetails = new ArrayList<VoucherDetails>();
     try {
        connection = dbManager.getConnection();
        SQLStatement sqlStatement = SQLStatement.getInstance();
        statement = connection.prepareStatement(
                sqlStatement.getProperty("get-voucher-details"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, terminalId);
        statement.setString(SQLStatement.PARAM4, businessDate);
        statement.setInt(SQLStatement.PARAM5, trainingFlag);
        statement.setString(SQLStatement.PARAM6, itemLevel3);
        result = statement.executeQuery();
        while(result.next()) {
                VoucherDetails voucherDetail = new VoucherDetails();
                voucherDetail.setCompanyId(result.getString("CompanyId"));
                voucherDetail.setDataType(result.getString("ItemType"));
                voucherDetail.setItemAmt(result.getString("ItemAmt"));
                voucherDetail.setItemLevel3(result.getString("ItemLevel3"));
                voucherDetail.setItemLevel1(result.getString("ItemLevel1"));
                voucherDetail.setItemLevel2(result.getString("ItemLevel2"));
                voucherDetail.setItemLevel4(result.getString("ItemLevel4"));
                voucherDetail.setItemCount(result.getInt("ItemCnt"));
                voucherDetail.setBusinessDayDate(result.getString("BusinessDayDate"));
                voucherDetail.setStoreId(result.getString("RetailStoreId"));
                voucherDetails.add(voucherDetail);
        }

    } catch (Exception e) {
        LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
                + ": Failed to get voucher detail list.", e);
        throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
                + functionName, e);
    }  finally {
        closeConnectionObjects(connection, statement, result);
        tp.methodExit(voucherDetails);
    }
    return voucherDetails;
    }
    /* (non-Javadoc)
     * @see ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO#getVoucherDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, int, int, int)
     */
    @Override
    public List<VoucherDetails> getVoucherDetailsByTillId(String companyId, String storeId, String tillId,
            String businessDate, int trainingFlag, String itemLevel3)
                    throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("companyId", companyId)
        .println("storeId", storeId)
        .println("tillId", tillId)
        .println("businessDayDate", businessDate)
        .println("trainingFlag", trainingFlag)
        .println("itemLevel3", itemLevel3);

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet result = null;
    List<VoucherDetails> voucherDetails = new ArrayList<VoucherDetails>();
     try {
        connection = dbManager.getConnection();
        SQLStatement sqlStatement = SQLStatement.getInstance();
        statement = connection.prepareStatement(
                sqlStatement.getProperty("get-voucher-details-tillId"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, tillId);
        statement.setString(SQLStatement.PARAM4, businessDate);
        statement.setInt(SQLStatement.PARAM5, trainingFlag);
        statement.setString(SQLStatement.PARAM6, itemLevel3);
        result = statement.executeQuery();
        while(result.next()) {
                VoucherDetails voucherDetail = new VoucherDetails();
                voucherDetail.setCompanyId(result.getString("CompanyId"));
                voucherDetail.setDataType(result.getString("ItemType"));
                voucherDetail.setItemAmt(result.getString("ItemAmt"));
                voucherDetail.setItemLevel3(result.getString("ItemLevel3"));
                voucherDetail.setItemLevel1(result.getString("ItemLevel1"));
                voucherDetail.setItemLevel2(result.getString("ItemLevel2"));
                voucherDetail.setItemLevel4(result.getString("ItemLevel4"));
                voucherDetail.setItemCount(result.getInt("ItemCnt"));
                voucherDetail.setBusinessDayDate(result.getString("BusinessDayDate"));
                voucherDetail.setStoreId(result.getString("RetailStoreId"));
                voucherDetails.add(voucherDetail);
        }

    } catch (Exception e) {
        LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
                + ": Failed to get voucher detail list.", e);
        throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
                + functionName, e);
    }  finally {
        closeConnectionObjects(connection, statement, result);
        tp.methodExit(voucherDetails);
    }
    return voucherDetails;
    }
    
    @Override
    public SettlementInfo getCountPaymentAmt(String companyId, String storeId, String businessDate, int trainingFlag) throws Exception {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("businessDate", businessDate)
		  .println("trainingFlag", trainingFlag);
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        SettlementInfo settlement = null;
        PaymentAmtInfo paymentAmtInfo = null;
        List<PaymentAmtInfo> paymentAmtList = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-count-payment-amt"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, businessDate);
            statement.setInt(SQLStatement.PARAM4, trainingFlag);
            
            result = statement.executeQuery();
            settlement = new SettlementInfo();
            paymentAmtList = new ArrayList<PaymentAmtInfo>();
            while (result.next()){
                paymentAmtInfo = new PaymentAmtInfo();
                paymentAmtInfo.setTenderId(result.getString("TenderId"));
                paymentAmtInfo.setTenderName(result.getString("TenderName"));
                paymentAmtInfo.setTenderType(result.getString("TenderType"));
                paymentAmtInfo.setTenderIdentification(result.getString("TenderIdentification"));
                paymentAmtInfo.setSumAmt(result.getInt("SumAmt"));
                paymentAmtList.add(paymentAmtInfo);
        	}
            settlement.setPaymentAmtList(paymentAmtList);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
                    + ": Failed to get payment amt.", e);
            throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
                    + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(settlement);
        }
        return settlement;
    }
    
    @Override
    public SettlementInfo getPaymentAmtByTerminalId(String companyId, String storeId, String businessDate, int trainingFlag, String terminalId) throws Exception {
        
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("businessDate", businessDate)
          .println("trainingFlag", trainingFlag)
          .println("terminalId", terminalId);
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<PaymentAmtInfo> paymentAmtList = null;
        SettlementInfo settlement = null;
        PaymentAmtInfo paymentAmtInfo = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("get-payment-amt-by-terminalid"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, businessDate);
            statement.setInt(SQLStatement.PARAM4, trainingFlag);
            statement.setString(SQLStatement.PARAM5, terminalId);
            
            result = statement.executeQuery();
            settlement = new SettlementInfo();
            paymentAmtList = new ArrayList<PaymentAmtInfo>();
            while (result.next()){
                paymentAmtInfo = new PaymentAmtInfo();
                paymentAmtInfo.setTenderId(result.getString("TenderId"));
                paymentAmtInfo.setTenderName(result.getString("TenderName"));
                paymentAmtInfo.setTenderType(result.getString("TenderType"));
                paymentAmtInfo.setTenderIdentification(result.getString("TenderIdentification"));
                paymentAmtInfo.setSumAmt(result.getInt("SumAmt"));
                paymentAmtList.add(paymentAmtInfo);
            }
            settlement.setPaymentAmtList(paymentAmtList);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName 
                    + ": Failed to get payment amt.", e);
            throw new Exception(e.getCause() + ": @SQLServerSettlementInfoDAO."
                    + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(settlement);
        }
        return settlement;
    }
}
