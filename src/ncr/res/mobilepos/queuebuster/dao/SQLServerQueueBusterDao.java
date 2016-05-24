package ncr.res.mobilepos.queuebuster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.constant.SuspendTransactionStatus;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Sale;
import ncr.res.mobilepos.journalization.model.poslog.TillSettle;
import ncr.res.mobilepos.journalization.model.poslog.Total;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.queuebuster.model.BusteredTransaction;
import ncr.res.mobilepos.queuebuster.model.CashDrawer;
import ncr.res.mobilepos.queuebuster.model.ResumedTransaction;
import ncr.res.mobilepos.queuebuster.model.SuspendData;

/**
 * The Data Access Object implementation for QueueBuster.
 *
 */
public class SQLServerQueueBusterDao extends AbstractDao implements
        IQueueBusterDAO {
    /**  The Database Manager of the class. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /** The Program Name for IOWriter. */
    private static final String PROG_NAME = "BusterDAO";
    /** The Trace Printer. */
    private Trace.Printer tp;
    /**
     * Snap Logger.
     */
    private SnapLogger snap;

    /**
     * Default Constructor for SQLServerQueueBusterDao.
     *
     * <P>Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException The exception thrown when the
     *                             constructor fails.
     */
    public SQLServerQueueBusterDao() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp =  DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Gets the Database Manager for SQLServerItemDAO.
     *
     * @return Returns a DBManager Instance
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public int saveTransactionToQueue(PosLog posLog,
                                      String posLogXml,
                                      String queue) throws DaoException {

		String functioName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functioName).println("storeid", posLogXml)
				.println("queue", queue);

		int result = 0;
		Connection connection = null;
        PreparedStatement saveTransToQuePrepStmnt = null;

        try {
        	connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            Transaction transaction = posLog.getTransaction();

			saveTransToQuePrepStmnt = connection.prepareStatement(
					sqlStatement.getProperty("save-transaction-to-queue"));
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM1,
                    transaction.getOrganizationHierarchy().getId());
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM2,
					transaction.getRetailStoreID());
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM3,
					transaction.getWorkStationID().getValue());
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM4,
					transaction.getSequenceNo());
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM5,
					transaction.getBusinessDayDate());
	         saveTransToQuePrepStmnt.setInt(SQLStatement.PARAM6,
	                    (transaction.getTrainingModeFlag().equals("false")) ? 0 : 1);
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM7, queue);
			saveTransToQuePrepStmnt.setString(SQLStatement.PARAM8,
					posLogXml);
			if (saveTransToQuePrepStmnt.executeUpdate() == 1) {
                updateLastSuspendTxId(posLog, connection);
            } else {
                result = ResultBase.RESSYS_ERROR_QB_QUEUEFULL;
            }
			connection.commit();
        } catch (SQLStatementException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functioName + ": Failed to save transaction to Queue.", e);
			rollBack(connection, functioName, e);
			throw new DaoException("SQLStatementException: @" + functioName, e);
		} catch (SQLException e) {
			if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
						+ ": Failed to save transaction to Queue.", e);
				rollBack(connection, functioName, e);
				throw new DaoException("SQLException: @" + functioName, e);
			}
			Snap.SnapInfo duplicateSuspend = snap.write("Poslogxml to suspend",
					posLogXml);
			LOGGER.logSnap(PROG_NAME, functioName,
					"Duplicate suspend transaction to snap file",
					duplicateSuspend);
			result = SQLResultsConstants.ROW_DUPLICATE;
			tp.println("Duplicate Entry of Transaction.");
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
					+ ": Failed to save transaction to Queue.", e);
			throw new DaoException("Exception: @" + functioName, e);
		} finally {
			closeConnectionObjects(connection, saveTransToQuePrepStmnt);

			tp.methodExit(result);
		}

		return result;
	}

    private void updateLastSuspendTxId(PosLog posLog, Connection connection) {
        String functionName = DebugLogger.getCurrentMethodName();
        try {
            IDeviceInfoDAO iDeviceInfoDao = DAOFactory.getDAOFactory(
            		DAOFactory.SQLSERVER).getDeviceInfoDAO();
        	boolean updated = iDeviceInfoDao.updateLastSuspendTxidAtQueueBuster(
        			posLog, connection);

        	if (!updated) {
        		tp.methodExit("Failed to update last suspend txid - " +
        				posLog.getTransaction().getSequenceNo() + ".");
        	}
        } catch (DaoException e) {
        	tp.methodExit("Failed to update last suspend txid - " +
    				posLog.getTransaction().getSequenceNo() + ".");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update last suspend txid - " +
					posLog.getTransaction().getSequenceNo() + ".", e);
        } catch (Exception e) {
        	tp.methodExit("Failed to update last suspend txid - " +
    				posLog.getTransaction().getSequenceNo() + ".");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName +
					": Failed to update last suspend txid - " +
					posLog.getTransaction().getSequenceNo() + ".", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResumedTransaction selectTransactionFromQueue(
            final String companyId,
            final String retailStoreId, final String queue,
            final String workStationId, final String sequenceNumber,
            final String businessDayDate, final int trainingFlag) throws DaoException {

        String functioName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functioName)
                .println("companyid", companyId)
                .println("storeid", retailStoreId)
                .println("queue", queue)
                .println("workstationid", workStationId)
                .println("txid", sequenceNumber)
                .println("txdate", businessDayDate)
                .println("trainingFlag", trainingFlag);

        Connection connection = null;
        PreparedStatement selectTransaction = null;
        PreparedStatement resumeTransaction = null;
        ResultSet selectResult = null;
        ResultSet resumeResult = null;

        ResumedTransaction resumedTransaction = new ResumedTransaction();
        resumedTransaction.setRetailStoreID(retailStoreId);
        resumedTransaction.setQueue(queue);
        resumedTransaction.setWorkstationID(workStationId);
        resumedTransaction.setSequenceNumber(sequenceNumber);

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            // check if transaction exist and if still resumable
            selectTransaction = connection.prepareStatement(
                    sqlStatement.getProperty("select-transaction-from-queue"));
            selectTransaction.setString(SQLStatement.PARAM1, companyId);
            selectTransaction.setString(SQLStatement.PARAM2, retailStoreId);
            selectTransaction.setString(SQLStatement.PARAM3, queue);
            selectTransaction.setString(SQLStatement.PARAM4, workStationId);
            selectTransaction.setString(SQLStatement.PARAM5, sequenceNumber);
            selectTransaction.setString(SQLStatement.PARAM6, businessDayDate);
            selectTransaction.setInt(SQLStatement.PARAM7, trainingFlag);

            selectResult = selectTransaction.executeQuery();

            if (selectResult.next()) {
                // transaction exist

                // check if valid poslog
                String poslogString = selectResult.getString(selectResult.findColumn("Tx"));
                PosLog poslogObject = POSLogHandler.toObject(poslogString);
                if (!POSLogHandler.isValid(poslogObject)) {
                    tp.println("POSLog to resume is invalid.");
                    resumedTransaction.setMessage("POSLog to resume is invalid.");
                    resumedTransaction.setNCRWSSResultCode(ResultBase.RES_ERROR_TXINVALID);
                    return resumedTransaction;
                }

                String status = selectResult.getString(selectResult.findColumn("Status"));
                if (status.equals(SuspendTransactionStatus.INITIAL)) {
                    // transaction not yet resumed
                    resumeTransaction = connection.prepareStatement(sqlStatement.
                            getProperty("select-transaction-from-queue-updlock"));
                    resumeTransaction.setString(SQLStatement.PARAM1, companyId);
                    resumeTransaction.setString(SQLStatement.PARAM2, retailStoreId);
                    resumeTransaction.setString(SQLStatement.PARAM3, queue);
                    resumeTransaction.setString(SQLStatement.PARAM4, workStationId);
                    resumeTransaction.setString(SQLStatement.PARAM5, sequenceNumber);
                    resumeTransaction.setString(SQLStatement.PARAM6, businessDayDate);
                    resumeTransaction.setInt(SQLStatement.PARAM7, trainingFlag);

                    resumeResult = resumeTransaction.executeQuery();
                    connection.commit();

                    if (resumeResult.next()) {
                        // transaction successfully resumed
                        String posLog = resumeResult.getString(selectResult.
                                findColumn("Tx"));
                        resumedTransaction.setPoslog(posLog);
                    } else {
                        // concurrent access scenario: transaction already resumed
                        resumedTransaction.setNCRWSSResultCode(
                                ResultBase.RESSYS_ERROR_QB_TXALREADYRESUMED);
                    }
                } else {
                    // transaction already resumed
                    resumedTransaction.setNCRWSSResultCode(
                            ResultBase.RESSYS_ERROR_QB_TXALREADYRESUMED);
                }
            } else {
            	// transaction does not exist
            	resumedTransaction.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_TXNOTFOUND);
            }
        } catch (SQLStatementException e) {
            LOGGER.logAlert(
                    PROG_NAME,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    functioName
                            + ": Failed to select suspended transaction from queue.",
                    e);
            throw new DaoException("SQLStatementException: @" + functioName, e);
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
                    + ": Failed to select suspended transaction from queue.", e);
            throw new DaoException("SQLException: @" + functioName, e);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
                    + ": Failed to select suspended transaction from queue.", e);
            throw new DaoException("Exception: @" + functioName, e);

        } finally {
            closeConnectionObjects(null, selectTransaction, selectResult);
            closeConnectionObjects(connection, resumeTransaction, resumeResult);

            tp.methodExit(resumedTransaction.toString());
        }

        return resumedTransaction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public final String selectOldestTransactionFromQueue(final String queue,
			final String retailStoreId) throws DaoException {

    	String result = null;
        String functioName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functioName).println("queue", queue)
				.println("storeid", retailStoreId);

        Connection connection = null;
        PreparedStatement slctTransFromQuePrepStmnt = null;
        ResultSet resultset = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            slctTransFromQuePrepStmnt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "select-oldest-transaction-from-queue"));

            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM1, retailStoreId);
            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM2, queue);

            resultset = slctTransFromQuePrepStmnt.executeQuery();

            if (resultset.next()) {
                result = resultset.getString(resultset.findColumn("Tx"));

                /*Update the status*/
                PosLog poslog = POSLogHandler.toObject(result);
                String status = "2";

                if (POSLogHandler.isValid(poslog)) {
                    tp.println("The POSLog xml retrieved is valid.");
                    status = "1";
                }

                String workstationid =
                    resultset.getString(resultset.findColumn("WorkstationId"));
                String businessdate =
                    resultset.getString(resultset.findColumn("BusinessDayDate"));
                String sequencenumber =
                    resultset.getString(resultset.findColumn("SequenceNumber"));

                //Try to set the status of the Resumed Transaction
                //in the queue since there exist
                updateBustedTransactionStatusFromQueue(connection,
                        retailStoreId, queue,
                        workstationid, businessdate, sequencenumber, status);
            }

		} catch (SQLStatementException e) {
			LOGGER.logAlert(
					PROG_NAME,
					Logger.RES_EXCEP_SQLSTATEMENT,
					functioName
							+ ": Failed to select oldest transaction from queue.",
					e);
			throw new DaoException("SQLStatementException: @" + functioName, e);
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
					+ ": Failed to select oldest transaction from queue.", e);
			throw new DaoException("SQLException: @" + functioName, e);
		} catch (JAXBException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_JAXB, functioName
					+ ": Failed to select oldest transaction from queue.", e);
			throw new DaoException("JAXBException: @" + functioName, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
					+ ": Failed to select oldest transaction from queue.", e);
			throw new DaoException("Exception: @" + functioName, e);
		} finally {
			closeConnectionObjects(connection, slctTransFromQuePrepStmnt,
					resultset);

			tp.methodExit(result);
		}

        return result;
    }

    /**
     * * {@inheritDoc}
     */
    @Override
    public final List<BusteredTransaction> listTransactionFromQueue(
            final String queue, final String companyId, final String retailStoreId,
            final String workstationId, final int trainingFlag) throws DaoException {

        String functioName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functioName).println("queue", queue)
                .println("storeid", companyId)
                .println("storeid", retailStoreId)
                .println("deviceid", workstationId)
                .println("training", trainingFlag);

        Connection connection = null;
        PreparedStatement slctTransFromQuePrepStmnt = null;
        PreparedStatement selectOperatorQueueStmt = null;
        ResultSet resultset = null;

        List<BusteredTransaction> bustList = new ArrayList<BusteredTransaction>();
		XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();

        try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			slctTransFromQuePrepStmnt = connection
					.prepareStatement(sqlStatement
							.getProperty("select-all-transaction-from-queue"));
			selectOperatorQueueStmt = connection.prepareStatement(sqlStatement
					.getProperty("select-operator-queue"));
			slctTransFromQuePrepStmnt.setString(SQLStatement.PARAM1, companyId);
			slctTransFromQuePrepStmnt.setString(SQLStatement.PARAM2, retailStoreId);
			slctTransFromQuePrepStmnt.setString(SQLStatement.PARAM3, workstationId);
			slctTransFromQuePrepStmnt.setString(SQLStatement.PARAM4, queue);
			slctTransFromQuePrepStmnt.setInt(SQLStatement.PARAM5, trainingFlag);
			resultset = slctTransFromQuePrepStmnt.executeQuery();

			while (resultset.next()) {
				String poslogxml = resultset.getString("Tx");
				String sequenceNumber = resultset.getString("SequenceNumber");
				String businessDayDate = resultset.getString("BusinessDayDate");

				PosLog poslog = poslogSerializer.unMarshallXml(poslogxml,
						PosLog.class);
				Transaction transaction = poslog.getTransaction();
				BusteredTransaction bustTransaction = new BusteredTransaction();
				bustTransaction.setSequencenumber(sequenceNumber);
				bustTransaction.setWorkstationid(workstationId);
				bustTransaction.setBusinessDayDate(businessDayDate);
				bustTransaction.setReceiptDateTime(transaction
						.getReceiptDateTime());

				// get subtotal
				double grandAmount = 0;
				List<Total> totals = null;
				RetailTransaction retailTransaction = transaction.getRetailTransaction();
				if (retailTransaction != null) {
					totals = retailTransaction.getTotal();
				}
				if (totals != null) {
					for (Total total : totals) {
						if ("TransactionGrandAmount".equalsIgnoreCase(total.getTotalType())) {
							grandAmount = total.getAmount();
							break;
						}
					}
				}

				bustTransaction.setTotal(grandAmount); // set subtotal

				List<LineItem> lineItem = retailTransaction.getLineItems();
				LineItem lineItemFirst = lineItem.get(0);
				Sale sale = lineItemFirst.getSale();
				if (sale.getAssociate() != null) {
				    String salesPersonName = sale.getAssociate().getAssociateName();
				    bustTransaction.setsalesPerson(salesPersonName);
				}else {
				    bustTransaction.setsalesPerson("");
				}

				String operatorNumber = transaction.getOperatorID().getValue();
				selectOperatorQueueStmt.setString(SQLStatement.PARAM1, operatorNumber);
				selectOperatorQueueStmt.setString(SQLStatement.PARAM2, companyId);
				ResultSet result = selectOperatorQueueStmt.executeQuery();
				if (result.next()) {
					Employee employee = new Employee();
					employee.setName(result.getString("OperatorName"));
					employee.setNumber(operatorNumber);
					bustTransaction.setEmployee(employee);
				}
				closeObject(result);
				bustList.add(bustTransaction);
			}

		} catch (SQLStatementException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functioName + ": Failed to list suspended transactions.", e);
			throw new DaoException("SQLStatementException: @" + functioName, e);
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
					+ ": Failed to list suspended transactions.", e);
			throw new DaoException("SQLException: @" + functioName, e);
		} catch (JAXBException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_JAXB, functioName
					+ ": Failed to list suspended transactions.", e);
			throw new DaoException("JAXBException: @" + functioName, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
					+ ": Failed to list suspended transactions.", e);
			throw new DaoException("Exception: @" + functioName, e);
		} finally {
			closeConnectionObjects(null, selectOperatorQueueStmt);
			closeConnectionObjects(connection, slctTransFromQuePrepStmnt,
					resultset);

			int bustListSize = bustList != null ? bustList.size() : 0;

			tp.methodExit(bustListSize);
		}

		return bustList;
	}

    /**
     * Private Method used for updating a given
     * Busted Transaction status in the Queue.
     *
     * @param connection        The SQL Connection
     * @param params            The Parameters necessary to save the transaction
     * @return                    The number of rows Affected
     * @throws DaoException        The Exception thrown when Error Occur
     */
	private int updateBustedTransactionStatusFromQueue(
			final Connection connection, final String retailStoreId,
			final String queue, final String workstationId,
			final String businessDate, final String sequenceNumber,
			final String status) throws DaoException {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", retailStoreId)
				.println("queue", queue).println("termid", workstationId)
				.println("txdate", businessDate)
				.println("txid", sequenceNumber).println("status", status);

        PreparedStatement updateQueTransStatsStmnt = null;
        int result = 0;

        try {
            updateQueTransStatsStmnt = connection.prepareStatement(
                    SQLStatement.getInstance()
                    .getProperty("update-status-from-queue"));
            updateQueTransStatsStmnt.setString(
            		SQLStatement.PARAM1, status);
            updateQueTransStatsStmnt.setString(
            		SQLStatement.PARAM2, retailStoreId);
            updateQueTransStatsStmnt.setString(
            		SQLStatement.PARAM3, workstationId);
            updateQueTransStatsStmnt.setString(
            		SQLStatement.PARAM4, sequenceNumber);
            updateQueTransStatsStmnt.setString(
                    SQLStatement.PARAM5, businessDate);
            updateQueTransStatsStmnt.setString(
                    SQLStatement.PARAM6, queue);

            result += updateQueTransStatsStmnt.executeUpdate();
            connection.commit();

		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update status of suspended transaction.", e);
			rollBack(connection, functionName, e);
			throw new DaoException("Exception:@" + functionName, e);
		} finally {
			closeObject(updateQueTransStatsStmnt);
			tp.methodExit(result);
		}
		return result;
	}

    //FOR DISNEY STORE QUEUE BUSTER ==================

    @Override
	public final int forwardTransactionToQueue(final String posLogXml)
			throws DaoException {

		int result = 0;
		String functioName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functioName).println("poslogxml", posLogXml);

        String retailstoreid = "";
		String workstationid = "";
		String txdate = "";
		String sequencenumber = "";

		Connection connection = null;
        PreparedStatement saveTransToQuePrepStmnt = null;

        try {

            XmlSerializer<PosLog> xmlTmpl = new XmlSerializer<PosLog>();
            PosLog posLog =
                (PosLog) xmlTmpl.unMarshallXml(posLogXml, PosLog.class);

            Transaction poslogTransaction = posLog.getTransaction();
            if (poslogTransaction != null) {
                retailstoreid = poslogTransaction.getRetailStoreID();
                workstationid = poslogTransaction.getWorkStationID().getValue();
                txdate = poslogTransaction.getBusinessDayDate();
                sequencenumber = poslogTransaction.getSequenceNo();
            }

            tp.println("RetailStoreID", retailstoreid)
            .println("Date", txdate)
            .println("WorkstationID", workstationid)
            .println("SequenceNumber", sequencenumber);

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveTransToQuePrepStmnt = connection.prepareStatement(
                    sqlStatement.getProperty("save-transaction-to-queue"));

            saveTransToQuePrepStmnt.setString(
                    SQLStatement.PARAM1, retailstoreid);
            saveTransToQuePrepStmnt.setString(
            		SQLStatement.PARAM2, workstationid);
            saveTransToQuePrepStmnt.setString(
                    SQLStatement.PARAM3, sequencenumber);
            saveTransToQuePrepStmnt.setString(
                    SQLStatement.PARAM4, txdate);
            //TODO: Cannot be null, missing Queue property in PosLog xml.
            saveTransToQuePrepStmnt.setString(
                    SQLStatement.PARAM5, "0");
            saveTransToQuePrepStmnt.setString(
                    SQLStatement.PARAM6, posLogXml);

            result = saveTransToQuePrepStmnt.executeUpdate();
            connection.commit();

        } catch (SQLStatementException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functioName + ": Failed to forward transaction to Queue.",
                    e);
            rollBack(connection, functioName, e);
            throw new DaoException("SQLStatementException: @" + functioName, e);
        } catch (SQLException e) {
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
                        functioName + ": Failed to forward transaction to Queue.",
                        e);
                rollBack(connection, functioName, e);
                throw new DaoException("SQLException: @" + functioName, e);
            }
            result = SQLResultsConstants.ROW_DUPLICATE;
            tp.println("Duplicate Entry of Transaction.");
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functioName + ": Failed to forward transaction to Queue.",
                    e);
            throw new DaoException("Exception: @" + functioName, e);
        } finally {
             closeConnectionObjects(connection, saveTransToQuePrepStmnt);

            tp.methodExit(result);
        }

        return result;
    }

    @Override
	public final int updateQueuedTransactionStatus(final String retailstoreid,
			final String txdate, final String workstationid, final String txid,
			final int methodCode) throws DaoException {

    	String functioName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functioName).println("storeid", retailstoreid)
				.println("txdate", txdate).println("termid", workstationid)
				.println("txid", txid).println("methodcode", methodCode);

        Connection connection = null;
        PreparedStatement updateQueuedTransStatPrepStmt = null;

        int result = 0;
        int validity = this.validateRequestToQueuedTransaction(
                retailstoreid, txdate, workstationid, txid, methodCode);
        if (validity != ResultBase.RES_OK) {
            return validity;
        }

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateQueuedTransStatPrepStmt = connection.prepareStatement(
              sqlStatement.getProperty("update-transaction-status-in-queue"));

            updateQueuedTransStatPrepStmt.setInt(
                                        SQLStatement.PARAM1, methodCode);
            updateQueuedTransStatPrepStmt.setString(
                                        SQLStatement.PARAM2, retailstoreid);
            updateQueuedTransStatPrepStmt.setString(
                                        SQLStatement.PARAM3, workstationid);
            updateQueuedTransStatPrepStmt.setString(
                                        SQLStatement.PARAM4, txid);
            updateQueuedTransStatPrepStmt.setString(
                                        SQLStatement.PARAM5, txdate);

            result = updateQueuedTransStatPrepStmt.executeUpdate();
            if (result == SQLResultsConstants.NO_ROW_AFFECTED) {
                result = ResultBase.RESSYS_ERROR_QB_TXNOTFOUND;
            } else if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                result = ResultBase.RES_OK;
            }

            connection.commit();

        } catch (SQLStatementException e) {
			LOGGER.logAlert(
					PROG_NAME,
					Logger.RES_EXCEP_SQLSTATEMENT,
					functioName
							+ ": Failed to update status of suspended transaction.",
					e);
			rollBack(connection, functioName, e);
			throw new DaoException("SQLStatementException: @" + functioName, e);
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
					+ ": Failed to update status of suspended transaction.", e);
			rollBack(connection, functioName, e);
			throw new DaoException("SQLException: @" + functioName, e);

        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
                    + ": Failed to update status of suspended transaction.", e);
            throw new DaoException("Exception: @" + functioName, e);
        } finally {
            closeConnectionObjects(connection, updateQueuedTransStatPrepStmt);

            tp.methodExit(result);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public final SuspendData selectSuspendTransactionFromQueue(
			final String retailStoreID, final String txDate,
			final String workstationID, final String txID) throws DaoException {

		String functioName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functioName).println("storeid", retailStoreID)
				.println("workstationid", workstationID)
				.println("txdate", txDate).println("txid", txID);

        Connection connection = null;
        PreparedStatement slctTransFromQuePrepStmnt = null;
        ResultSet resultset = null;
        SuspendData suspendData = new SuspendData();

        //By definition, if no transaction has been found, the response status
        //must be ABNORMAL_END
        suspendData.setStatus(SuspendData.ABNORMAL_END);

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            slctTransFromQuePrepStmnt =
             connection.prepareStatement(sqlStatement
             .getProperty("select-transaction-from-queue-disregard-postermid"));

            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM1, retailStoreID);
            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM2, workstationID);
            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM3, txID);
            slctTransFromQuePrepStmnt.setString(
                    SQLStatement.PARAM4, txDate);

            resultset = slctTransFromQuePrepStmnt.executeQuery();

            //Is the Suspended Transaction found?
            //If yes, generate the suspended transaction object.
            if (resultset.next()) {
                String poslogXml =
                    resultset.getString(resultset.findColumn("Tx"));
                int suspendStatus =
                    Integer.parseInt(
                        resultset.getString(resultset.findColumn("Status")));

                //Based on RES-2549 comments
                //any transaction that will be retrieved
                //will have status changed to 1
                updateQueuedTransactionStatus(retailStoreID , txDate,
                            workstationID, txID, SuspendData.FORWARD_COMPLETE);

                PosLog posLog = POSLogHandler.toObject(poslogXml);
                suspendData.setPoslog(posLog);
                suspendData.setSuspendStatus(
                        Integer.toString(suspendStatus));
                suspendData.setStatus(SuspendData.NORMAL_END);
            } else {
                suspendData
                 .setErrormessage("No Transaction found in the queue.");
                tp.println("No transaction found in the queue.");
            }
		} catch (SQLStatementException e) {
			LOGGER.logAlert(
					PROG_NAME,
					Logger.RES_EXCEP_SQLSTATEMENT,
					functioName
							+ ": Failed to select suspended transaction from queue.",
					e);
			throw new DaoException("SQLStatementException: @" + functioName
					+ "- An exception on the"
					+ " SQL prepared statement occured during retrieval of"
					+ " suspended data.", e);
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName
					+ ": Failed to select suspended transaction from queue.", e);
			throw new DaoException("SQLException: @" + functioName
					+ "-  An exception on executing the SQL"
					+ "command occured during retrieval of suspended data.", e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName
					+ ": Failed to select suspended transaction from queue.", e);
			throw new DaoException("Exception: @" + functioName
					+ "- A general exception occured. ", e);
		} finally {
			closeConnectionObjects(connection, slctTransFromQuePrepStmnt,
					resultset);

			tp.methodExit(suspendData);
		}

		return suspendData;
	}

    @Override
    public final int validateRequestToQueuedTransaction(
            final String retailstoreid, final String txdate,
            final String workstationid, final String txid, final int status)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", retailstoreid)
            .println("txdate", txdate)
            .println("termid", workstationid)
            .println("txid", txid)
            .println("status", status);

        int validity = ResultBase.RESSYS_ERROR_QB_REQINVALID;
        Connection connection = null;
        PreparedStatement getQueuedTransStatPrepStmt = null;
        ResultSet resultset = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            getQueuedTransStatPrepStmt =
             connection.prepareStatement(sqlStatement
             .getProperty("get-queued-transaction-status"));

            getQueuedTransStatPrepStmt.setString(
                    SQLStatement.PARAM1, retailstoreid);
            getQueuedTransStatPrepStmt.setString(
                    SQLStatement.PARAM2, workstationid);
            getQueuedTransStatPrepStmt.setString(
                    SQLStatement.PARAM3, txid);
            getQueuedTransStatPrepStmt.setString(
                    SQLStatement.PARAM4, txdate);

            resultset = getQueuedTransStatPrepStmt.executeQuery();

            if (resultset.next()) {
                int dbStatus = resultset.getInt(resultset.findColumn("Status"));

                switch (status) {
                case SuspendData.FORWARD_COMPLETE:
                    validity = (dbStatus == SuspendData.INITIAL_STATUS || dbStatus == SuspendData.CANCEL_FROM_POS) ? 0
                            : ResultBase.RESSYS_ERROR_QB_REQINVALID;
                    break;
                case SuspendData.CANCEL_FROM_POS:
                case SuspendData.PROCESS_COMPLETE:
                    if (dbStatus == SuspendData.FORWARD_COMPLETE) {
                        validity = 0;
                    }
                    break;
                default:
                    break;
                }
            } else {
                validity = ResultBase.RESSYS_ERROR_QB_TXNOTFOUND;
            }

        } catch (SQLStatementException e) {
            LOGGER.logAlert(
                    PROG_NAME,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to validate the request to queue.",
                    e);
            throw new DaoException("SQLStatementException: @" + functionName, e);
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to validate the request to queue.", e);
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to validate the request to queue.", e);
            throw new DaoException("Exception: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, getQueuedTransStatPrepStmt,
                    resultset);

            tp.methodExit(validity);
        }
        return validity;
    }

	@Override
	public final String selectForwardItemCount(final String companyId,
			final String storeId, final String businessDayDate,
			final String workstationId, final String queue) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("CompanyId", companyId)
			.println("StoreId", storeId)
			.println("BusinessDayDate",businessDayDate)
			.println("WorkstationId", workstationId)
			.println("Queue", queue);

		String resultCnt = null;
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet resultset = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-forward-item-count"));
			select.setString(SQLStatement.PARAM1, companyId);
			select.setString(SQLStatement.PARAM2, storeId);
			select.setString(SQLStatement.PARAM3, businessDayDate);
			select.setString(SQLStatement.PARAM4, StringUtility.convNullToEmpty(workstationId));
			select.setString(SQLStatement.PARAM5, StringUtility.convNullToEmpty(queue));
			resultset = select.executeQuery();

			if (resultset.next()) {
				resultCnt = resultset.getString("count");
			}
		} catch (SQLStatementException stmntEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName
					+ ": Failed to Select Forward Item Count.", stmntEx);
			throw new DaoException("SQLStatementException: @" + functionName, stmntEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to Select Forward Item Count.", sqlEx);
			throw new DaoException("SQLException: @" + functionName, sqlEx);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to Select Forward Item Count.", e);
			throw new DaoException("Exception: @" + functionName, e);
		} finally {
			closeConnectionObjects(connection, select, resultset);
			tp.methodExit();
		}
		return resultCnt;
	}

	@Override
	public final ResultBase deleteForwardItem(final String companyId,
			final String storeId, final String businessDayDate)
					throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyId", companyId)
			.println("storeid", storeId)
			.println("businessDayDate", businessDayDate);

		ResultBase result = new ResultBase();
		Connection connection = null;
		PreparedStatement deleteStmnt = null;
		int status = 0;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			deleteStmnt = connection.prepareStatement(sqlStatement
					.getProperty("delete-forward-item"));
			deleteStmnt.setString(SQLStatement.PARAM1, companyId);
			deleteStmnt.setString(SQLStatement.PARAM2, storeId);
			deleteStmnt.setString(SQLStatement.PARAM3, businessDayDate);
			status = deleteStmnt.executeUpdate();

			if (status < SQLResultsConstants.ONE_ROW_AFFECTED) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
				tp.println(ResultBase.RES_NODATAFOUND_MSG);
				return result;
			} else {
				connection.commit();
				result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				result.setMessage(ResultBase.RES_SUCCESS_MSG);
			}
		} catch (SQLStatementException stmntEx) {
			rollBack(connection, functionName, stmntEx);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to Delete Forward Item.", stmntEx);
			throw new DaoException("SQLStatementException: @" + functionName,
					stmntEx);
		} catch (SQLException sqlEx) {
			rollBack(connection, functionName, sqlEx);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to Delete Forward Item.", sqlEx);
			throw new DaoException("SQLException: @" + functionName, sqlEx);
		} catch (Exception e) {
			rollBack(connection, functionName, e);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to Delete Forward Item.", e);
			throw new DaoException("Exception: @" + functionName, e);
		} finally {
			closeConnectionObjects(connection, deleteStmnt, null);
			tp.methodExit();
		}
		return result;
	}

	@Override
	public CashDrawer getPreviousAmount(String companyId, String storeId) throws DaoException {
		tp.methodEnter("getPreviousAmount");
    	tp.println("Company Id", companyId)
    		.println("Store Id", storeId);

    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet result = null;
    	CashDrawer cashDrawer = new CashDrawer();
    	PosLog poslog = null;

    	try {
    		connection = dbManager.getConnection();
    		SQLStatement sqlStatement = SQLStatement.getInstance();
    		statement = connection.prepareStatement(sqlStatement
                    .getProperty("get-previous-amount"));
    		statement.setString(SQLStatement.PARAM1, companyId);
    		statement.setString(SQLStatement.PARAM2, storeId);
    		result = statement.executeQuery();
		    cashDrawer.setCompanyId(companyId);
            cashDrawer.setStoreId(storeId);
            cashDrawer.setCashOnHand("0");
    		if (result.next()) {
    		    XmlSerializer<PosLog> xmlTmpl = new XmlSerializer<PosLog>();
    		    poslog = (PosLog) xmlTmpl.unMarshallXml(result.getString("Tx"), PosLog.class);
    		    TillSettle tillSettle = poslog.getTransaction().getTenderControlTransaction().getTillSettle();
    			String cashOnHand =  tillSettle.getTender().getAmount();
    			cashDrawer.setCashOnHand(cashOnHand);
    		}
    	} catch (Exception e) {
    		LOGGER.logAlert(PROG_NAME, "SQLServerQueueBusterDao.getPreviousAmount()",
    				Logger.RES_EXCEP_SQL,
    				"Failed to get previous amount.\n" + e.getMessage());
    		throw new DaoException("DaoException: "
    				+ "@SQLServerQueueBusterDao.getPreviousAmount() - " + e.getMessage(), e);
    	} finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(cashDrawer.toString());
    	}
 		return cashDrawer;
	}

	@Override
	public void updatePreviousAmount(Connection connection, CashDrawer cashDrawer) throws DaoException {
		tp.methodEnter("updatePreviousAmount");
    	tp.println(cashDrawer.toString());

    	String functionName = DebugLogger.getCurrentMethodName();
        PreparedStatement updateSOD = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        java.sql.Date sqlDate = null;
        int result = 0;
        try {
        	if(!StringUtility.isNullOrEmpty(cashDrawer.getBusinessDayDate())) {
        		date = dateFormat.parse(cashDrawer.getBusinessDayDate());
        		sqlDate = new java.sql.Date(date.getTime());
        	}
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateSOD = connection.prepareStatement(
                sqlStatement.getProperty("update-previous-amount"));
            updateSOD.setString(SQLStatement.PARAM1, cashDrawer.getCompanyId());
            updateSOD.setString(SQLStatement.PARAM2, cashDrawer.getStoreId());
            updateSOD.setString(SQLStatement.PARAM3, cashDrawer.getTillId());
            updateSOD.setString(SQLStatement.PARAM4, cashDrawer.getTerminalId());
            updateSOD.setString(SQLStatement.PARAM5, cashDrawer.getCashOnHand());
            updateSOD.setDate(SQLStatement.PARAM6, sqlDate);
            updateSOD.setString(SQLStatement.PARAM7, cashDrawer.getOperatorId());
            result = updateSOD.executeUpdate();
        } catch (Exception  ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to update previous amount.", ex);
            throw new DaoException("DaoException: "
                    + "@SQLServerQueueBusterDao.updatePreviousAmount() - " + ex.getMessage(), ex);
        } finally {
            tp.methodExit();
        }

	}
}
