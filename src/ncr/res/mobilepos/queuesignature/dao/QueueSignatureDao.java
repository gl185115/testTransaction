package ncr.res.mobilepos.queuesignature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.queuesignature.model.CAInfo;
import ncr.res.mobilepos.queuesignature.model.Transaction;
/**
 * The Data Access Object class for Queuing Signature.
 *
 */
public class QueueSignatureDao
extends AbstractDao implements IQueueSignatureDao {
    /**
     * The Database manager.
     */
    private DBManager dbManager;
    /**
     * The IOwritter for the Log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Class name for the Log.
     */
    private String className = "QueueSigDao";
    /** The Trace Printer. */
    private Trace.Printer tp;
    /**
     * The Prog Name for the Log.
     */
    private String progName = "QuSigDao";
    /** Transaction Pending. */
    public static final int TRANSACTION_PENDING = 3;
    /** Transaction Processing. */
    public static final int TRANSACTION_PROCESSING = 6;
    /** Transaction processed. */
    public static final int TRANSACTION_PROCESSED = 4;
    /** Transaction processed Abnormal. */
    public static final int TRANSACTION_PROCESSED_AB = 5;
    /** Transaction not found. */
    public static final int TRANSACTION_NOT_FOUND = 0;
    /** Signature Capture Abnormal End. */
    public static final int SIGNATURE_CAPTURE_ABNORMAL_END = 5;
    /** Success Signature Capture. */
    public static final int SIGNATURE_CAPTURE_SUCCESS = 0;
    /**
     * Default constructor.
     * @exception DaoException The Exception thrown when error occur.
     */
    public QueueSignatureDao() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp =  DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * {@inheritDoc}
     */
    public final int addSignatureRequest(final String storeid,
                                    final String queue,
                                    final String workstationid,
                                    final String sequencenum,
                                    final String cainfo)
    throws Exception {
        String functionName = className + "addSignatureRequest";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("WorkstationID", workstationid)
          .println("SequenceNumber", sequencenum)
          .println("CAInfo", cainfo);

        Connection connection = null;
        PreparedStatement update = null;
        int result = 0;
        int status = SIGNATURE_CAPTURE_ABNORMAL_END;

		// convert cainfo json string into json object
		JSONObject jsonObj = new JSONObject(cainfo);
		// extract timeofuse and convert to date
		String txdate = formatTxDate(jsonObj.getString("TimeOfUse"));
		// extract other needed data
		String pan = jsonObj.getString("CardNo");
		double amount = jsonObj.getDouble("Sum");
		// NOTE: the JSONObject.get methods will throw an error if
		// the key does not exist

		if (amount <= 0 || pan == null || pan.isEmpty() || txdate == null
				|| txdate.isEmpty()) {
			tp.println("An abnormal end has occured."
					+ "Either Sum, CardNo or TimeOfUse is invalid.");
			tp.methodExit(status);
			return status;
		}

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            update = connection.prepareStatement(
            		sqlStatement.getProperty("add-signature-request"));
            
            //set the variables to their respective indexes
            update.setString(SQLStatement.PARAM1, storeid);
            update.setString(SQLStatement.PARAM2, workstationid);
            update.setString(SQLStatement.PARAM3, txdate);
            update.setString(SQLStatement.PARAM4, sequencenum);
            update.setInt(SQLStatement.PARAM5, 3);
            update.setString(SQLStatement.PARAM6, queue);
            update.setString(SQLStatement.PARAM7, pan);
            update.setDouble(SQLStatement.PARAM8, amount);
            update.setString(SQLStatement.PARAM9, cainfo);
            result = update.executeUpdate();
            
            //if affected row is only 1, then the insert is a success
            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
            	status = SIGNATURE_CAPTURE_SUCCESS;
            } else {
            	tp.println("The Signature Request was not added.");
            }

            connection.commit();
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to add Signature Request: error in SQLException :"
                    + ex.getMessage());
            throw new DaoException("SQLException: @QueueSignature.queueSignature", ex);
        } finally {
            closeConnectionObjects(connection, update);
            
            tp.methodExit(status);
        }
        return status;
    }
    /**
     * {@inheritDoc}
     */
    public final List<Transaction> getPendingSignatureRequests(
            final String storeid, final String queue, final String businessdate)
            throws DaoException {
        String functionName = className + "getPendingSignatureRequests";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("BusinessDate", businessdate);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        List<Transaction> transactions = new ArrayList<Transaction>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-signature-request-list"));
            //set the variables to their respective indexes
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, queue);
            select.setString(SQLStatement.PARAM3, businessdate);

           rs = select.executeQuery();
           while (rs.next()) {
               Transaction trans = new Transaction();
               trans.setSequenceNumber(rs.getString("SequenceNumber"));
               trans.setWorkstationID(rs.getString("WorkstationId"));
               trans.setTotal(rs.getDouble("Amount"));
               transactions.add(trans);
           }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get Signature Request List:"
                    + " error in SQLException :"
                    + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.getPendingSignatureRequests", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get Signature Request List: error in Excetion :"
                    + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.getPendingSignatureRequests", ex);
        } finally {
            closeConnectionObjects(connection, select, rs);
            
            tp.methodExit(transactions.size());
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    public final CAInfo getSignatureRequest(final String storeid,
            final String queue, final String posterminalid, final String seqnum,
            final String businessdate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("StoreID", storeid).println("Queue", queue)
          .println("POSTerminalID", posterminalid)
          .println("SequenceNumbers", seqnum)
          .println("BusinessDate", businessdate);
        String functionName = className + "getSignatureRequest";
        Connection connection = null;
        PreparedStatement select = null;
        CAInfo result = new CAInfo();
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-signature-request"));
            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, queue);
            select.setString(SQLStatement.PARAM3, posterminalid);
            select.setString(SQLStatement.PARAM4, seqnum);
            select.setString(SQLStatement.PARAM5, businessdate);
           rs = select.executeQuery();
           if (!rs.next()) {
               result.setStatus(TRANSACTION_NOT_FOUND);
               tp.println("Transaction not found.");
               return result;
           }
           int status = rs.getInt("Status");
           if (status == TRANSACTION_PENDING) {
               String cainforaw = rs.getString("CaInfoRaw");
               JSONObject json = new JSONObject(cainforaw);
               result = new CAInfo();
               result.setTimeOfUse(json.getString("TimeOfUse"));
               result.setCardNo(json.getString("CardNo"));
               result.setBusinessDivision(json.getString("BusinessDivision"));
               result.setSum(json.getDouble("Sum"));
               result.setSign(json.getString("Sign"));
               //if JSONObject.get encounters a
               //key that does not exist it will throw an error
				try {
					result.setApprovalNo(json.getString("ApprovalNo"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get ApprovalNo from cainforaw");
				}
				try {
					result.setShopName(json.getString("ShopName"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get ShopName from cainforaw");
				}
				try {
					result.setSlipNo(json.getInt("SlipNo"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get SlipNo from cainforaw");
				}
				try {
					result.setExpireDate(json.getString("ExpireDate"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get ExpireDate from cainforaw");
				}
				try {
					result.setMerchandiseCode(json.getString("MerchandiseCode"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get MerchandiseCode from cainforaw");
				}
				try {
					result.setCardCompanyCode(json.getString("CardCompanyCode"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get CardCompanyCode from cainforaw");
				}
				try {
					result.setCardCompanyName(json.getString("CardCompanyName"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get CardCompanyName from cainforaw");
				}
				try {
					result.setPaymentDivision(json.getInt("PaymentDivision"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get PaymentDivision from cainforaw");
				}
				try {
					result.setInstallmentAmount(json
							.getDouble("InstallmentAmount"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get InstallmentAmount from cainforaw");
				}
				try {
					result.setNumberOfBonuses(json.getInt("NumberOfBonuses"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get NumberOfBonuses from cainforaw");
				}
				try {
					result.setTaxAndPostage(json.getInt("TaxAndPostage"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get TaxAndPostage from cainforaw");
				}
				try {
					result.setRomanName(json.getString("RomanName"));
				} catch (JSONException e) {
					LOGGER.logAlert(progName, functionName,
							Logger.RES_EXCEP_PARSE,
							"Failed to get RomanName from cainforaw");
				}
               result.setStatus(TRANSACTION_PENDING);
               tp.println("Transaction is in Pending state.");
           } else if (status == TRANSACTION_PROCESSING) {
               result.setStatus(TRANSACTION_PROCESSING);
               tp.println("Transaction is in Processing state.");
           } else if (status == TRANSACTION_PROCESSED_AB
                   || status == TRANSACTION_PROCESSED) {
               result.setStatus(TRANSACTION_PROCESSED);
               tp.println("Transaction is in Processed state.");
           }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get Signature Request List:"
                    + " error in SQLException :" + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.getSignatureRequest", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get Signature Request List: error in Excetion :"
                    + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.getSignatureRequest", ex);
        } finally {
            closeConnectionObjects(connection, select, rs);
            
            tp.methodExit(result.toString());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public final ResultBase updateSignatureRequest(
            final String status, final String storeid,
            final String queue, final String posterminalid, final String seqnum,
            final String businessdate) throws DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("Status", status)
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("WorkstationID", posterminalid)
          .println("SequenceNumber", seqnum)
          .println("BusinessDate", businessdate);
        String functionName = className + "updateSignatureRequest";
        Connection connection = null;
        PreparedStatement update = null;
        ResultBase result = new ResultBase();
        result.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
        int rs = ResultBase.RESEXTCA_ERROR_NOTFOUND;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            update = connection.prepareStatement(
                    sqlStatement.getProperty("update-signature-request"));

            update.setString(SQLStatement.PARAM1, status);
            update.setString(SQLStatement.PARAM2, storeid);
            update.setString(SQLStatement.PARAM3, queue);
            update.setString(SQLStatement.PARAM4, posterminalid);
            update.setString(SQLStatement.PARAM5, seqnum);
            update.setString(SQLStatement.PARAM6, businessdate);

            rs = update.executeUpdate();
            if (rs == SQLResultsConstants.ONE_ROW_AFFECTED) {
                result.setNCRWSSResultCode(ResultBase.RESEXTCA_OK);
                connection.commit();
            } else {
                tp.println("Signature Request was not updated.");
            }

        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update Signature Request:"
                    + " error in SQLException :" + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.updateSignatureRequest", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Signature Request:"
                    + " error in Excetion :" + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + "QueueSignature.updateSignatureRequest", ex);
        } finally {
            closeConnectionObjects(connection, update);
            
            tp.methodExit(result.toString());
        }
        return result;
    }

    /**
     * Private Helper to format Transaction date.
     * @param date  The Date.
     * @return  The Date in "YYYY-MM-DD" format.
     */
    private String formatTxDate(final String date) {
        String expectedFormat = "YYYYMMDDHHmmss";
        int yearIndex = expectedFormat.lastIndexOf("Y") + 1;
        int monthIndex = expectedFormat.lastIndexOf("M") + 1;
        int dayIndex = expectedFormat.lastIndexOf("D") + 1;

        //TimeOfUse format is YYYYMMDDHHmmss
        //get first 4 digits, YYYY
        String year = date.substring(0, yearIndex);

        //get next 2 digits, MM
        String month = date.substring(yearIndex, monthIndex);

        //get next 2 digits, DD
        String day = date.substring(monthIndex, dayIndex);
        //return the date formatted string
        return String.format("%s-%s-%s", year, month, day);
     }
}
