package ncr.res.mobilepos.deviceinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.deviceinfo.model.AttributeInfo;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.IndicatorInfo;
import ncr.res.mobilepos.deviceinfo.model.Indicators;
import ncr.res.mobilepos.deviceinfo.model.PosControlOpenCloseStatus;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.TerminalInfo;
import ncr.res.mobilepos.deviceinfo.model.TerminalStatus;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.tillinfo.model.Till;
/**
 * PeripheralControl data access object.
 * @see IDeviceInfoDAO
 */
public class SQLDeviceInfoDAO extends AbstractDao implements IDeviceInfoDAO {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "DevDao";
    /**
     * table not found error
     */
    private static final int SQL_ERROR_TABLE_NOT_FOUND = 208;
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;
    /**
     * class instance of trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     *  Snap Logger.
     */
    private SnapLogger snap;
	/**
	 * SQLStatement.
	 */
	private SQLStatement sqlStatement;

    /**
     * SQLDeviceInfoDAO default constructor.
     * @throws DaoException database exception
     */
    public SQLDeviceInfoDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
            Thread.currentThread().getId(), getClass());
        // Gets Singleton reference from the factory.
        this.sqlStatement = SQLStatement.getInstance();
	}

    /**
     * Get Printer Information for a Printer Id.
     * @param storeid store identifier
     * @param printerid printer to be identified
     * @return ResultBase
     * @throws DaoException The exception thrown for non-SQL related issue.
     */
	public final PrinterInfo getPrinterInfo(final String storeid,
			final String printerid) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("printerid",printerid)
            .println("storeid", storeid);

        PrinterInfo printerInfo = new PrinterInfo();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-printerinfo"));
			select.setString(SQLStatement.PARAM1, (storeid == null) ? "%"
					: storeid);
			select.setString(SQLStatement.PARAM2, printerid);

			result = select.executeQuery();
            if (result.next()) {
                printerInfo.setPrinterId(
                        result.getString(result.findColumn("PrinterId")));
                printerInfo.setRetailStoreId(
                        result.getString(result.findColumn("StoreId")));
                printerInfo.setPrinterName(
                        result.getString(result.findColumn("PrinterName")));
                printerInfo.setPrinterDescription(
                        result.getString(result.findColumn("Description")));
                printerInfo.setIpAddress(
                        result.getString(result.findColumn("IpAddress")));
                printerInfo.setPortNumTcp(
                        result.getString(result.findColumn("PortNumTcp")));
                printerInfo.setPortNumUdp(
                		result.getString(result.findColumn("PortNumUdp")));
            } else {
                printerInfo = null;
                tp.println("Failed to find printer information.");
            }
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get Printer Info.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get Printer Info.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, select, result);

			tp.methodExit(printerInfo);
		}
		return printerInfo;
	}

    /**** FOR NEW DEVICE INFO FUNCTIONS ****/
    @Override
	public final DeviceInfo getDeviceInfo(final String companyId, final String storeid,
			final String terminalid, final int trainingmode) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("companyid", companyId)
			.println("storeid", storeid)
			.println("terminalid", terminalid)
			.println("trainingmode", trainingmode);

    	DeviceInfo devInfo = new DeviceInfo();
        PrinterInfo printerInfo = new PrinterInfo();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-peripheraldeviceinfo"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeid);
            select.setString(SQLStatement.PARAM3, terminalid);
            select.setInt(SQLStatement.PARAM4, trainingmode);

            result = select.executeQuery();
            if (result.next()) {
            	devInfo.setCompanyId(result.getString(
                		result.findColumn("CompanyId")));
            	devInfo.setRetailStoreId(StringUtility.convNullToEmpty(
            			result.getString(result.findColumn("StoreId"))));
                devInfo.setDeviceId(StringUtility.convNullToEmpty(
                		result.getString(result.findColumn("TerminalId"))));
                devInfo.setTillId(result.getString(
                		result.findColumn("TId")));
                devInfo.setDeviceName(result.getString(
                		result.findColumn("DevName")));
                devInfo.setLogSize(StringUtility.convNullToEmpty(
                		result.getString(result.findColumn("SendLogFile"))));
                devInfo.setSaveLogFile(StringUtility.convNullToEmpty(
                		result.getString(result.findColumn("SaveLogFile"))));
                devInfo.setLogAutoUpload(StringUtility.convNullToEmpty(
                		result.getString(result.findColumn("AutoUpload"))));
                devInfo.setQueuebusterlink(result.getString(
                        result.findColumn("LinkQueueBuster")));
                devInfo.setAuthorizationlink(result.getString(
                        result.findColumn("LinkAuthorization")));
                // this "devprinterid" is get from MST_DEVICEINFO.
                devInfo.setPrinterId(result.getString(
                		result.findColumn("DevPrinterId")));
                devInfo.setSignaturelink(result.getString(
                        result.findColumn("LinkSignature")));
                devInfo.setLinkPOSTerminalId(StringUtility.convNullToEmpty(
                		result.getString(result.findColumn("LinkPosTerminalId"))));
                devInfo.setPricingType(result.getString("PricingType"));
                devInfo.setTxid(result.getString("LastTxId"));
                devInfo.setSuspendtxid(result.getString("LastSuspendTxId"));
                devInfo.setStatus(result.getString(
                		result.findColumn("Status")));
                devInfo.setTrainingMode(result.getInt(
                		result.findColumn("Training")));
                printerInfo.setPrinterId(result.getString(
                		result.findColumn("PrinterId")));
                printerInfo.setPrinterName(result.getString(
                		result.findColumn("PrinterName")));
                printerInfo.setPrinterDescription(result.getString(
                		result.findColumn("Description")));
                printerInfo.setStatus(result.getString(
                		result.findColumn("Status")));
                printerInfo.setPortNumUdp(result.getString(
                		result.findColumn("PortNumUdp")));
                printerInfo.setIpAddress(result.getString(
                		result.findColumn("IpAddress")));
                printerInfo.setPortNumTcp(result.getString(
                		result.findColumn("PortNumTcp")));
                devInfo.setAttributeId(result.getString(
                		result.findColumn("AttributeId")));
            } else {
                devInfo.setLinkPOSTerminalId("");
            }
            devInfo.setPrinterInfo(printerInfo);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to retrieve DeviceInfo.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to retrieve DeviceInfo.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(devInfo);
		}
		return devInfo;
	}

	@Override
	public final ResultBase deleteDevice(final String deviceID,
			final String retailStoreID, final String appId, final String opeCode)
			throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("deviceID", deviceID)
            .println("retailStoreID", retailStoreID)
            .println("appId", appId)
            .println("opeCode", opeCode);

        Connection connection = null;
        PreparedStatement deleteDeviceStmt = null;
        ResultBase resultBase = new ResultBase();
        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteDeviceStmt = connection.prepareStatement(sqlStatement
                    .getProperty("delete-deviceinfo"));
            deleteDeviceStmt.setString(SQLStatement.PARAM1, appId);
            deleteDeviceStmt.setString(SQLStatement.PARAM2, opeCode);
            deleteDeviceStmt.setString(SQLStatement.PARAM3, retailStoreID);
            deleteDeviceStmt.setString(SQLStatement.PARAM4, deviceID);

            deleteDeviceStmt.executeUpdate();
            connection.commit();

		} catch (SQLException ex) {
			rollBack(connection, functionName, ex);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to delete device.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			rollBack(connection, functionName, ex);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to delete device.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, deleteDeviceStmt);

			tp.methodExit(resultBase);
		}
		return resultBase;
	}

	@Override
	public final ViewDeviceInfo updateDevice(final String companyID, final String retailStoreID,
			final String deviceID, final DeviceInfo deviceInfoToSet,
			final int trainingMode, Connection connection) throws DaoException{
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyid", companyID)
			.println("retailstoreid", retailStoreID)
			.println("deviceid", deviceID)
			.println("deviceinfo", deviceInfoToSet.toString())
			.println("trainingflag", trainingMode)
			.println("connection", connection);

        boolean isNewConnection = false;
        PreparedStatement updateStatement = null;
        ResultSet result = null;
        ViewDeviceInfo viewInfo = new ViewDeviceInfo();
        DeviceInfo devInfoToReturn = new DeviceInfo();
        PrinterInfo printerInfo = new PrinterInfo();

        //start update after determining if printer exists
        try {
        	if (connection == null) {
        		connection = dbManager.getConnection();
        		isNewConnection = true;
        	}
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStatement = connection.prepareStatement(
                       sqlStatement.getProperty("update-deviceinfo"));
            updateStatement.setString(SQLStatement.PARAM1,
                    deviceInfoToSet.getCompanyId());
            updateStatement.setString(SQLStatement.PARAM2,
            		deviceInfoToSet.getRetailStoreId());
            updateStatement.setString(SQLStatement.PARAM3,
            		deviceInfoToSet.getDeviceId());
            updateStatement.setString(SQLStatement.PARAM4,
            		deviceInfoToSet.getPrinterId());
            updateStatement.setString(SQLStatement.PARAM5,
                    deviceInfoToSet.getTillId());
            updateStatement.setString(SQLStatement.PARAM6,
            		deviceInfoToSet.getLinkPOSTerminalId());
            updateStatement.setString(SQLStatement.PARAM7,
            		deviceInfoToSet.getLogSize());
            updateStatement.setString(SQLStatement.PARAM8,
            		deviceInfoToSet.getSaveLogFile());
            updateStatement.setString(SQLStatement.PARAM9,
            		deviceInfoToSet.getLogAutoUpload());
            updateStatement.setString(SQLStatement.PARAM10,
            		deviceInfoToSet.getQueuebusterlink());
            updateStatement.setString(SQLStatement.PARAM11,
            		deviceInfoToSet.getAuthorizationlink());
            updateStatement.setString(SQLStatement.PARAM12,
            		deviceInfoToSet.getSignaturelink());
            updateStatement.setString(SQLStatement.PARAM13,
            		deviceInfoToSet.getTxid());
            updateStatement.setString(SQLStatement.PARAM14,
            		deviceInfoToSet.getSuspendtxid());
            updateStatement.setInt(SQLStatement.PARAM15,
            		deviceInfoToSet.getEjSequence());
            updateStatement.setString(SQLStatement.PARAM16,
            		deviceInfoToSet.getDeviceName());
            updateStatement.setString(SQLStatement.PARAM17,
                    deviceInfoToSet.getUpdAppId());
            updateStatement.setString(SQLStatement.PARAM18,
                    deviceInfoToSet.getUpdOpeCode());
            updateStatement.setString(
                    SQLStatement.PARAM19, companyID);
            updateStatement.setString(
                    SQLStatement.PARAM20, retailStoreID);
            updateStatement.setString(
                    SQLStatement.PARAM21, deviceID);
            updateStatement.setInt(
                    SQLStatement.PARAM22, trainingMode);
            result = updateStatement.executeQuery();
            if (result.next()) {
            	devInfoToReturn.setCompanyId(
            			result.getString(result.findColumn("CompanyId")).trim());
            	devInfoToReturn.setDeviceId(
            			result.getString(result.findColumn("TerminalId")).trim());
            	devInfoToReturn.setRetailStoreId(
            			result.getString(result.findColumn("StoreId")).trim());
            	devInfoToReturn.setLogSize(
            			result.getString(result.findColumn("SendLogFile")).trim());
            	devInfoToReturn.setDeviceName(
            			result.getString(result.findColumn("DeviceName")));
            	devInfoToReturn.setLogSize(
            			result.getString(result.findColumn("SendLogFile")).trim());
            	devInfoToReturn.setSaveLogFile(
            			result.getString(result.findColumn("SaveLogFile")).trim());
            	devInfoToReturn.setLogAutoUpload(
            			result.getString(result.findColumn("AutoUpload")).trim());
            	devInfoToReturn.setQueuebusterlink(
            			result.getString(result.findColumn("LinkQueueBuster")));
            	devInfoToReturn.setAuthorizationlink(
            			result.getString(result.findColumn("LinkAuthorization")));
            	devInfoToReturn.setSignaturelink(
            			result.getString(result.findColumn("LinkSignature")));
            	devInfoToReturn.setLinkPOSTerminalId(
            			result.getString(result.findColumn("LinkPosTerminalId")));
            	devInfoToReturn.setTillId(
            			result.getString(result.findColumn("TillId")));
            	devInfoToReturn.setTxid(
            			result.getString(result.findColumn("LastTxid")));
            	devInfoToReturn.setSuspendtxid(
            			result.getString(result.findColumn("LastSuspendTxId")));
            	devInfoToReturn.setPrinterId(
            			result.getString(result.findColumn("PrinterId")));
            	devInfoToReturn.setTrainingMode(
            			result.getInt(result.findColumn("Training")));
            	//if the printerid has a value, get printerInfo of the printer
            	if (!StringUtility.isNullOrEmpty(devInfoToReturn.getPrinterId())) {
            		printerInfo = this.getPrinterInfo(
            				devInfoToReturn.getRetailStoreId(), devInfoToReturn.getPrinterId());
            	} else {
            		printerInfo = new PrinterInfo();
            	}
            	viewInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_OK);
            	viewInfo.setMessage("Success");
            } else {
            	viewInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOTFOUND);
            	devInfoToReturn.setLinkPOSTerminalId("");
            	viewInfo.setMessage("The info with given storeid,"
            			+ " and terminalid does not exist");
            	//reset the printer info if no device found
            	devInfoToReturn.setPrinterInfo(new PrinterInfo());
            }
            devInfoToReturn.setPrinterInfo(printerInfo);
            viewInfo.setDeviceInfo(devInfoToReturn);
            connection.commit();
        } catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update device.", ex);
            if (ex.getErrorCode() == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                viewInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_ALREADY_EXIST);
                DeviceInfo prevdeviceInfo = getDeviceInfo(companyID, retailStoreID,
                		deviceID, trainingMode);
                String companyIdToSet = deviceInfoToSet.getCompanyId() != null ?
                		deviceInfoToSet.getCompanyId() : companyID;
                String storeIdToSet = deviceInfoToSet.getRetailStoreId() != null ?
                		deviceInfoToSet.getRetailStoreId() : retailStoreID;
                String deviceIdToSet = deviceInfoToSet.getDeviceId() != null ?
                		deviceInfoToSet.getDeviceId() : deviceID;
                DeviceInfo deviceInfo = getDeviceInfo(companyID, storeIdToSet,
                		deviceIdToSet, trainingMode);

            	if ("Deleted".equalsIgnoreCase(deviceInfo.getStatus())) {
            		closeConnectionObjects(connection, updateStatement);
            		try {
            			connection = dbManager.getConnection();
					} catch (SQLException e) {
						LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
								+ ": Failed to update device.", ex);
					}
            		setDeviceInfo(deviceInfoToSet, prevdeviceInfo);
                    ResultBase activateResult = activateDevice(companyIdToSet, storeIdToSet,
                    		deviceIdToSet, connection);
                    if (activateResult.getNCRWSSResultCode() == ResultBase.RES_OK) {
                    	ViewDeviceInfo viewDeviceInfo = updateDevice(companyIdToSet, storeIdToSet,
                    			deviceIdToSet, deviceInfoToSet, trainingMode, connection);
                        ResultBase deleteResult = deleteDevice(deviceID, retailStoreID,
                        		deviceInfoToSet.getUpdAppId(), deviceInfoToSet.getUpdOpeCode());
                        if (deleteResult.getNCRWSSResultCode() == ResultBase.RES_OK) {
                        	viewInfo.setDeviceInfo(viewDeviceInfo.getDeviceInfo());
                            viewInfo.setNCRWSSResultCode(viewDeviceInfo.getNCRWSSResultCode());
                        } else {
                        	rollBack(connection, "updateDevice", null);
                        }
                    }
                    closeConnectionObjects(connection, updateStatement);
                }
            } else {
                throw new DaoException(ex);
            }
		} catch (Exception ex) {
			rollBack(connection, functionName, ex);
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update device.", ex);
			throw new DaoException(ex);
		} finally {
			if (isNewConnection) {
				closeConnectionObjects(connection, updateStatement, result);
			} else {
				closeConnectionObjects(null, updateStatement, result);
			}
			tp.methodExit(viewInfo);
		}
		return viewInfo;
	}

	private void setDeviceInfo(final DeviceInfo deviceInfoToSet,
			DeviceInfo prevdeviceInfo) {
		if (deviceInfoToSet.getDeviceName() == null) {
			deviceInfoToSet.setDeviceName(prevdeviceInfo.getDeviceName());
		}
		if (deviceInfoToSet.getLinkPOSTerminalId() == null) {
			deviceInfoToSet.setLinkPOSTerminalId(prevdeviceInfo
					.getLinkPOSTerminalId());
		}
		if (deviceInfoToSet.getLogAutoUpload() == null) {
			deviceInfoToSet.setLogAutoUpload(prevdeviceInfo.getLogAutoUpload());
		}
		if (deviceInfoToSet.getLogSize() == null) {
			deviceInfoToSet.setLogSize(prevdeviceInfo.getLogSize());
		}
		if (deviceInfoToSet.getPricingType() == null) {
			deviceInfoToSet.setPricingType(prevdeviceInfo.getPricingType());
		}
		if (deviceInfoToSet.getPrinterId() == null) {
			deviceInfoToSet.setPrinterId(prevdeviceInfo.getPrinterId());
		}
		if (deviceInfoToSet.getQueuebusterlink() == null) {
			deviceInfoToSet.setQueuebusterlink(prevdeviceInfo
					.getQueuebusterlink());
		}
		if (deviceInfoToSet.getSignaturelink() == null) {
			deviceInfoToSet.setSignaturelink(prevdeviceInfo.getSignaturelink());
		}
		if (deviceInfoToSet.getAuthorizationlink() == null) {
			deviceInfoToSet.setAuthorizationlink(prevdeviceInfo
					.getAuthorizationlink());
		}
		if (deviceInfoToSet.getAuthorizationlink() == null) {
			deviceInfoToSet.setAuthorizationlink(prevdeviceInfo
					.getAuthorizationlink());
		}
		if (deviceInfoToSet.getTillId() == null) {
			deviceInfoToSet.setTillId(prevdeviceInfo.getTillId());
		}
		if (deviceInfoToSet.getSaveLogFile() == null) {
			deviceInfoToSet.setSaveLogFile(prevdeviceInfo.getSaveLogFile());
		}
		if (deviceInfoToSet.getTxid() == null) {
			deviceInfoToSet.setTxid(prevdeviceInfo.getTxid());
		}
		if (deviceInfoToSet.getSuspendtxid() == null) {
			deviceInfoToSet.setSuspendtxid(prevdeviceInfo.getSuspendtxid());
		}
		if (deviceInfoToSet.getEjSequence() < 0) {
			deviceInfoToSet.setEjSequence(prevdeviceInfo.getEjSequence());
		}

	}

    private ResultBase activateDevice(final String companyID, final String retailStoreID,
            final String deviceID, Connection connection) throws DaoException {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyID)
		        .println("retailstoreid", retailStoreID)
		        .println("deviceid", deviceID)
		        .println("connection", connection);

		PreparedStatement activateStatement = null;
		ResultSet result = null;
		ResultBase resultBase = new ResultBase();
		try {
			if (connection == null) {
				connection = dbManager.getConnection();
			}
			SQLStatement sqlStatement = SQLStatement.getInstance();
			activateStatement = connection.prepareStatement(sqlStatement
					.getProperty("activate-deleted-deviceinfo"));
			activateStatement.setString(SQLStatement.PARAM1, companyID);
			activateStatement.setString(SQLStatement.PARAM2, retailStoreID);
			activateStatement.setString(SQLStatement.PARAM3, deviceID);

			result = activateStatement.executeQuery();
			if (result.next()) {
				resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_OK);
				resultBase.setMessage("Success");
			} else {
				resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOTFOUND);
				resultBase.setMessage("The info with given storeid,"
						+ " and terminalid does not exist");
			}
			connection.commit();

		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to activate device.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to activate device.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(null, activateStatement, result);

			tp.methodExit(resultBase);
		}
		return resultBase;
	}

    public final boolean updateLastTxidAtJournal(final Transaction transaction,
    		final Connection connection, int trainingMode) throws DaoException {
        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }
        if (tp == null) {
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        }
        tp.methodEnter(DebugLogger.getCurrentMethodName());

		boolean updated = this.updateLastTxidOfDeviceInfo(transaction.getOrganizationHierarchy().getId(),
				transaction.getRetailStoreID(), transaction.getWorkStationID().getValue(),
				transaction.getSequenceNo(), trainingMode, connection) ==
				SQLResultsConstants.ONE_ROW_AFFECTED;

		tp.methodExit(updated);
        return updated;
    }

	public final boolean updateLastTxidAtCreditAuth(final String jsonStr)
			throws DaoException {
		if (snap == null) {
			snap = (SnapLogger) SnapLogger.getInstance();
		}
		if (tp == null) {
			tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
					getClass());
		}

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("jsonStr", jsonStr);

		boolean updated = false;
		Connection connection = null;
		try {
			connection = dbManager.getConnection();
			JSONObject jsonObj = new JSONObject(jsonStr);
			updated = (this.updateLastTxidOfDeviceInfo(
			        jsonObj.getString("companyid"),
					jsonObj.getString("storeid"),
					jsonObj.getString("terminalid"),
					jsonObj.getString("txid"),
					jsonObj.getInt("trainingMode"),connection) ==
					SQLResultsConstants.ONE_ROW_AFFECTED);
			connection.commit();
		} catch (JSONException e) {
			tp.println("JSONException", StringUtility.printStackTrace(e));
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("JSON data in credit authorization", jsonStr),
					snap.write("JSONException", e) };
			LOGGER.logSnap(PROG_NAME, functionName,
					"Output error json data to snap file", infos);
			throw new DaoException(e);
		} catch (DaoException e) {
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("JSON data in credit authorization", jsonStr),
					snap.write("DaoException", e) };
			LOGGER.logSnap(PROG_NAME, functionName,
					"Output error json data to snap file", infos);
			throw new DaoException(e);
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update last transaction number.", e);
			throw new DaoException(e);
		} finally {
			closeConnectionObjects(connection, null);
			tp.methodExit(updated);
		}
		return updated;
	}

    public final boolean updateLastSuspendTxidAtQueueBuster(final PosLog
    		posLog, final Connection connection) throws DaoException {
        if (tp == null) {
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        }
        tp.methodEnter(DebugLogger.getCurrentMethodName());

		Transaction transaction = posLog.getTransaction();
		boolean updated = this.updateLastSuspendTxidOfDeviceInfo(transaction.getOrganizationHierarchy().getId(),
				transaction.getRetailStoreID(), transaction.getWorkStationID().getValue(),
				transaction.getSequenceNo(), transaction.getTrainingModeFlag(), connection) ==
				SQLResultsConstants.ONE_ROW_AFFECTED;

		tp.methodExit(updated);
        return updated;
    }

	private final int updateLastTxidOfDeviceInfo(final String companyid, final String storeid,
			final String deviceid, final String lasttxid, int trainingMode,
			final Connection connection) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeid).println("companyid", companyid)
				.println("deviceid", deviceid).println("lasttxid", lasttxid);

		if (StringUtility.isNullOrEmpty(storeid, deviceid, lasttxid)) {
			tp.methodExit("Invalid parameters.");
			throw new DaoException("Parameter is null/empty. CompanyId=" + companyid
			        + "RetailStoreId="
					+ storeid + " WorkstationId=" + deviceid
					+ " SequenceNumber=" + lasttxid);
		}

		PreparedStatement updateTxidStmt = null;
		int affectedRow = 0;
		try {
			SQLStatement sqlStatement = SQLStatement.getInstance();
			updateTxidStmt = connection.prepareStatement(sqlStatement
					.getProperty("update-last-txid"));
			updateTxidStmt.setInt(SQLStatement.PARAM1,
					Integer.valueOf(lasttxid));
			updateTxidStmt.setString(SQLStatement.PARAM2, storeid);
			updateTxidStmt.setString(SQLStatement.PARAM3, deviceid);
			updateTxidStmt.setString(SQLStatement.PARAM4, companyid);
			updateTxidStmt.setInt(SQLStatement.PARAM5, trainingMode);
			affectedRow = updateTxidStmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update last transaction number.", e);
			throw new DaoException(e);

		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update last transaction number.", e);
			throw new DaoException(e);
		} finally {
			closeConnectionObjects(null, updateTxidStmt);
			tp.methodExit(affectedRow);
		}
		return affectedRow;
	}

	private final int updateLastSuspendTxidOfDeviceInfo(final String companyid, final String storeid,
			final String deviceid, final String lasttxid, final String trainingFlag,
			final Connection connection) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeid).println("companyid", companyid)
				.println("deviceid", deviceid).println("lasttxid", lasttxid);

		if (StringUtility.isNullOrEmpty(companyid, storeid, deviceid, lasttxid, trainingFlag)) {
			tp.methodExit("Invalid parameters.");
			throw new DaoException("Parameter is null/empty. CompanyId=" + companyid
			        + "RetailStoreId="
					+ storeid + " WorkstationId=" + deviceid
					+ " SequenceNumber=" + lasttxid);
		}

		PreparedStatement updateTxidStmt = null;
		int affectedRow = 0;
		try {
			SQLStatement sqlStatement = SQLStatement.getInstance();
			updateTxidStmt = connection.prepareStatement(sqlStatement
					.getProperty("update-last-suspendtxid"));
			updateTxidStmt.setInt(SQLStatement.PARAM1,
					Integer.valueOf(lasttxid));
			updateTxidStmt.setString(SQLStatement.PARAM2, storeid);
			updateTxidStmt.setString(SQLStatement.PARAM3, deviceid);
			updateTxidStmt.setString(SQLStatement.PARAM4, companyid);
			updateTxidStmt.setInt(SQLStatement.PARAM5, trainingFlag.equals("false") ? 0 : 1);
			affectedRow = updateTxidStmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update last suspend transaction number.", e);
			throw new DaoException(e);

		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update last suspend transaction number.", e);
			throw new DaoException(e);
		} finally {
			closeConnectionObjects(null, updateTxidStmt);

			tp.methodExit(affectedRow);
		}
		return affectedRow;
	}

    /*
     * (non-Javadoc)
     * @see ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO#getAllTills(java.lang.String, java.lang.String, int)
     */
    public final List<Till> getAllTills(final String storeId, final String key,
            final int limit) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeId)
                .println("key", key).println("limit", limit);

        List<Till> tillList = new ArrayList<>();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-all-tills"));
            select.setString(SQLStatement.PARAM1,
                    StringUtility.isNullOrEmpty(storeId) ? null : storeId);
            select.setString(SQLStatement.PARAM2,
                    StringUtility.isNullOrEmpty(key) ? null : key.trim() + "%");

            tp.println("searchlimit", GlobalConstant.getMaxSearchResults());
            int searchLimit = (limit == 0) ? GlobalConstant
                    .getMaxSearchResults() : limit;
            select.setInt(SQLStatement.PARAM3, searchLimit);

            result = select.executeQuery();
            while (result.next()) {
                Till aTill = new Till();
                aTill.setStoreId(result.getString(result.findColumn("StoreId")));
                aTill.setTillId(result.getString(result.findColumn("TillId")));
                aTill.setTerminalId(result.getString(result.findColumn("TerminalId")));
                aTill.setBusinessDayDate(result.getString(result.findColumn("BusinessDayDate")));
                aTill.setSodFlag(result.getString(result.findColumn("SodFlag")));
                aTill.setEodFlag(result.getString(result.findColumn("EodFlag")));
                aTill.setUpdOpeCode(result.getString(result.findColumn("UpdOpeCode")));

                tillList.add(aTill);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to retrieve tills.", ex);
            throw new DaoException("SQLException: @SQLDeviceInfoDAO"
                    + "." + functionName + " - Failed to retrieve tills.", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to retrieve tills.", ex);
            throw new DaoException("Exception: @SQLDeviceInfoDAO"
                    + "." + functionName + " - Failed to retrieve tills.", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(tillList);
        }
        return tillList;
    }

	/**
	 * Gets the Device Attribute.
	 * @param storeId		- The store identifier.
	 * @param terminalId	- The terminal/device identifier.
	 * @param companyId
	 * @param training
	 * @return AttributeInfo - The Info of the Device Attribute.
	 * @throws DaoException	- Thrown when DAO error is encountered.
     */
    public final ResultBase getAttributeInfo(final String storeId, final String terminalId, String companyId, int training)
    		throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeId", storeId)
                .println("terminalId", terminalId)
                .println("companyId", companyId)
                .println("training", training);
        ResultBase returnData = null;
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(this.sqlStatement.getProperty("get-attribute-info"))) {
            ps.setString(SQLStatement.PARAM1, storeId);
            ps.setString(SQLStatement.PARAM2, terminalId);
            ps.setString(SQLStatement.PARAM3, companyId);
            ps.setInt(SQLStatement.PARAM4, training);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    AttributeInfo attributeInfo = new AttributeInfo();
                    attributeInfo.setAttributeId(resultSet.getString("AttributeId"));
                    attributeInfo.setPrinter(resultSet.getString("Printer"));
                    attributeInfo.setTill(resultSet.getString("Till"));
                    attributeInfo.setCreditTerminal(resultSet.getString("CreditTerminal"));
                    attributeInfo.setMSR(resultSet.getString("MSR"));
                    attributeInfo.setCashChanger(resultSet.getString("CashChanger"));
                    attributeInfo.setAttribute1(resultSet.getString("Attribute1"));
                    attributeInfo.setAttribute2(resultSet.getString("Attribute2"));
                    attributeInfo.setAttribute3(resultSet.getString("Attribute3"));
                    attributeInfo.setAttribute4(resultSet.getString("Attribute4"));
                    attributeInfo.setAttribute5(resultSet.getString("Attribute5"));
                    attributeInfo.setAttribute6(resultSet.getString("Attribute6"));
                    attributeInfo.setAttribute7(resultSet.getString("Attribute7"));
                    // Currently Attribute8, Attribute9 and Attribute10 are reserved for future use, it can be null.
                    attributeInfo.setAttribute8(resultSet.getString("Attribute8"));
                    attributeInfo.setAttribute9(resultSet.getString("Attribute9"));
                    attributeInfo.setAttribute10(resultSet.getString("Attribute10"));

                    attributeInfo.setTrainingMode(resultSet.getInt("Training"));
                    attributeInfo.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                    attributeInfo.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                    attributeInfo.setMessage(ResultBase.RES_SUCCESS_MSG);

                    returnData = attributeInfo;
                } else {
                    // No data found.
                    ResultBase errorReturn = new ResultBase();
                    errorReturn.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                    errorReturn.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                    errorReturn.setMessage(ResultBase.RES_NODATAFOUND_MSG);

                    returnData = errorReturn;
                }
            }
        } catch (SQLException sqlEx) {
            throw new DaoException("SQLException: @SQLDeviceInfoDAO"
                    + "." + functionName + " - Failed to Get the Attribute Info.", sqlEx);
        } finally {
            tp.methodExit(returnData);
        }
        return returnData;
    }

	/**
     * Gets the Attribute Info of Device.
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @return DeviceAttribute	- The Info of the Device Attribute.
     * @throws DaoException	- Thrown when DAO error is encountered.
     */
    public final DeviceAttribute getDeviceAttributeInfo(final String companyId, final String storeId, final String terminalId)
    		throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalId", terminalId);

        DeviceAttribute deviceAttributeInfo = new DeviceAttribute();
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-device-attribute-info"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, terminalId);

            resultSet = select.executeQuery();

            if (resultSet.next()) {
                deviceAttributeInfo.setCompanyId(resultSet.getString("CompanyId"));
            	deviceAttributeInfo.setStoreId(resultSet.getString("StoreId"));
            	deviceAttributeInfo.setTerminalId(resultSet.getString("TerminalId"));
            	deviceAttributeInfo.setDeviceName(resultSet.getString("DeviceName"));
            	deviceAttributeInfo.setAttributeId(resultSet.getString("AttributeId"));
            	deviceAttributeInfo.setPrinterId(resultSet.getString("PrinterId"));
            	deviceAttributeInfo.setTillId(resultSet.getString("TillId"));
            	deviceAttributeInfo.setLinkQueueBuster(resultSet.getString("LinkQueueBuster"));
            	deviceAttributeInfo.setPrintDes(resultSet.getString("PrintDes"));
            	deviceAttributeInfo.setDrawerId(resultSet.getString("DrawerId"));
            	deviceAttributeInfo.setDisplayName(resultSet.getString("DisplayName"));
            	deviceAttributeInfo.setAttributeDes(resultSet.getString("AttributeDes"));
            	deviceAttributeInfo.setTrainingMode(resultSet.getInt("Training"));

            	deviceAttributeInfo.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            	deviceAttributeInfo.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            	deviceAttributeInfo.setMessage(ResultBase.RES_SUCCESS_MSG);
            }else{

            	deviceAttributeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	deviceAttributeInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	deviceAttributeInfo.setMessage(ResultBase.RES_NODATAFOUND_MSG);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to Get the Device attribute Info.", sqlEx);
            throw new DaoException("SQLException: @SQLDeviceInfoDAO"
                    + "." + functionName + " - Failed to Get the Device attribute Info.", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to Get the Device attribute Info.", ex);
            throw new DaoException("Exception: @SQLDeviceInfoDAO"
                    + "." + functionName + " - Failed to Get the Device attribute Info.", ex);
        } finally {
            closeConnectionObjects(connection, select, resultSet);
            tp.methodExit(deviceAttributeInfo);
        }
        return deviceAttributeInfo;
    }

    public final ViewTerminalInfo getTerminalInfo(String companyId, String storeId,
    		String terminalId) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("terminalId", terminalId);

        ResultSet result = null;
        Connection connection = null;
        PreparedStatement statement = null;
    	ViewTerminalInfo viewTerminalInfo = new ViewTerminalInfo();

    	try {
    		connection = dbManager.getConnection();
    		SQLStatement sqlStatement = SQLStatement.getInstance();
    		statement = connection.prepareStatement(
    				sqlStatement.getProperty("get-terminal-info"));
    		statement.setString(SQLStatement.PARAM1, companyId);
    		statement.setString(SQLStatement.PARAM2, storeId);
    		statement.setString(SQLStatement.PARAM3, terminalId);
    		result = statement.executeQuery();

    		if (result.next()) {
    			TerminalInfo terminalInfo = new TerminalInfo();
    			terminalInfo.setCompanyId(result.getString("CompanyId"));
    			terminalInfo.setStoreId(result.getString("StoreId"));
    			terminalInfo.setTerminalId(result.getString("TerminalId"));
    			terminalInfo.setFloorId(result.getString("FloorId"));
    			terminalInfo.setTerminalName(result.getString("TerminalName"));
    			terminalInfo.setIpAddress(result.getString("IPAddress"));
    			terminalInfo.setStoreClass(result.getString("StoreClass"));
    			terminalInfo.setTerminalType(result.getString("TerminalType"));
    			terminalInfo.setTillType(result.getString("TillType"));
    			terminalInfo.setRelationType(result.getString("RelationType"));
    			terminalInfo.setConnectionFlag1(result.getInt("ConnectionFlag1"));
    			terminalInfo.setConnectionFlag2(result.getInt("ConnectionFlag2"));
    			terminalInfo.setConnectionFlag3(result.getInt("ConnectionFlag3"));
    			terminalInfo.setConnectionFlag4(result.getInt("ConnectionFlag4"));
    			terminalInfo.setConnectionFlag5(result.getInt("ConnectionFlag5"));
    			terminalInfo.setConnectionFlag6(result.getInt("ConnectionFlag6"));
    			terminalInfo.setConnectionFlag7(result.getInt("ConnectionFlag7"));
    			terminalInfo.setConnectionFlag8(result.getInt("ConnectionFlag8"));
    			terminalInfo.setLogoFileName(result.getString("LogoFileName"));
    			terminalInfo.setInshiFileName(result.getString("InshiFileName"));
    			terminalInfo.setSubCode1(result.getString("SubCode1"));
    			terminalInfo.setSubCode2(result.getString("SubCode2"));
    			terminalInfo.setNote(result.getString("Note"));
    			terminalInfo.setCompanyName(result.getString("CompanyName"));
    			checkFloorIdExist(terminalInfo);

    			viewTerminalInfo.setTerminalInfo(terminalInfo);
    		} else {
    			viewTerminalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
    			viewTerminalInfo.setMessage("Terminal info not found.");
    			tp.println("Terminal info not found.");
    		}
    	} catch (Exception e) {
    		LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
    				+ ": Failed to get terminal info.", e);
    		throw new Exception(e.getCause() + ": @SQLServerDeviceInfoDAO."
    				+ functionName, e);
    	} finally {
    		closeConnectionObjects(connection, statement, result);
    		tp.methodExit(viewTerminalInfo);
    	}
    	return viewTerminalInfo;
    }

    private TerminalInfo checkFloorIdExist(TerminalInfo terminalInfo) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("terminalInfo", terminalInfo);

        ResultSet result = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("check-floorid-exist"));
            statement.setString(SQLStatement.PARAM1, terminalInfo.getCompanyId());
            statement.setString(SQLStatement.PARAM2, terminalInfo.getStoreId());
            statement.setString(SQLStatement.PARAM3, terminalInfo.getFloorId());
            statement.setString(SQLStatement.PARAM4, terminalInfo.getTerminalId());
            result = statement.executeQuery();

            if (result.next()) {
                terminalInfo.setSingleFloorId(0);
            } else {
                terminalInfo.setSingleFloorId(1);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to check the FloorId.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @checkFloorIdExist ", sqlEx);
        } catch (NumberFormatException nuEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to check the FloorId.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @checkFloorIdExist ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to check the FloorId.\n" + e.getMessage());
            throw new DaoException("Exception: @checkFloorIdExist ", e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(terminalInfo);
        }
        return terminalInfo;
    }
	/**
	 * getPosCtrlOpenCloseStatus
	 * @param companyId
	 * @param storeId
	 * @param terminalId
	 * @param thisBusinessDay
	 * @return
     * @throws DaoException
     */
	@Override
	public ResultBase getPosCtrlOpenCloseStatus(String companyId, String storeId, String terminalId, String thisBusinessDay)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
				.println("companyId", companyId)
				.println("storeId", storeId)
				.println("terminalId", terminalId)
				.println("thisBusinessDay", thisBusinessDay);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultBase resultBase = new ResultBase();

		try {
			connection = dbManager.getConnection();
			statement = connection.prepareStatement(
					this.sqlStatement.getProperty("get-posctrl-openclose-status"));

			statement.setString(SQLStatement.PARAM1, companyId);
			statement.setString(SQLStatement.PARAM2, storeId);
			statement.setString(SQLStatement.PARAM3, terminalId);
			statement.setString(SQLStatement.PARAM4, thisBusinessDay);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				PosControlOpenCloseStatus posControlOpenCloseStatus = new PosControlOpenCloseStatus();
				posControlOpenCloseStatus.setNCRWSSResultCode(ResultBase.RES_OK);
				posControlOpenCloseStatus.setOpenCloseStat(resultSet.getShort("OpenCloseStat"));
				resultBase = posControlOpenCloseStatus;
			} else {
				// PosCtrl not found.
				resultBase.setNCRWSSResultCode(ResultBase.RES_TERMINAL_NOT_WORKING);
				resultBase.setMessage("PosCtrl not found.");
				tp.println("PosCtrl not found.");
			}

		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
					"DapException: @SQLDeviceInfoDAO." + functionName 	+ " Failed to get PosCtrl OpenCloseStat.", ex);
			throw new DaoException("SQLException: @" + functionName, ex);
		} finally {
			closeConnectionObjects(connection, statement, resultSet);
			tp.methodExit(resultBase);
		}
		return resultBase;
	}

	/**
	 * Get working device status from AUT_DEVICES, TXU_POS_CTRL, MST_DEVICEINFO.
	 * @return List<TerminalStatus> list of TerminalStatus.
	 * @throws DaoException - holds the exception that was thrown
	 */
	@Override
	public List<TerminalStatus> getWorkingDeviceStatus() throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("getWorkingDeviceStatus");
		tp.methodEnter(functionName);

		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		List<TerminalStatus> terminals = new ArrayList<>();

		try {
			connection = dbManager.getConnection();
			selectStmt = connection.prepareStatement(sqlStatement.getProperty("get-working-device-status"));

			resultSet = selectStmt.executeQuery();
			while(resultSet.next()) {
				TerminalStatus device = new TerminalStatus();
				device.setCompanyId(resultSet.getString("CompanyId"));
				device.setStoreId(resultSet.getString("StoreId"));
				device.setTerminalId(resultSet.getString("TerminalId"));
				device.setTillId(resultSet.getString("TillId"));
				device.setTerminalName(resultSet.getString("DeviceName"));
				device.setOpenCloseStat(resultSet.getShort("OpenCloseStat"));
				device.setSodTime(resultSet.getTimestamp("SodTime"));
				device.setEodTime(resultSet.getTimestamp("EodTime"));
				terminals.add(device);
			}

		} catch (SQLException sqle) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL, sqle.getMessage());
			throw new DaoException("SQLException is thrown", sqle);
		} finally {
			closeConnectionObjects(connection, selectStmt, resultSet);
			tp.methodExit(terminals);
		}
		return terminals;
	}
	
	/**
     * Get indicatorInfo from PRM_DEVICE_INDICATOR
     * @param attributeid
     * @return Indicators
     * @throws DaoException - holds the exception that was thrown
     */
    @Override
    public Indicators getDeviceIndicators(String attributeid) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getDeviceIndicators");
        tp.methodEnter(functionName).println("attributeid", attributeid);;

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        List<IndicatorInfo> indicatorInfoList = new ArrayList<>();
        Indicators indicators = new Indicators();
        try {
            connection = dbManager.getConnection();
            selectStmt = connection.prepareStatement(sqlStatement.getProperty("get-device-indicators"));
            selectStmt.setString(SQLStatement.PARAM1, attributeid);
            
            resultSet = selectStmt.executeQuery();
            while(resultSet.next()) {
                IndicatorInfo indicatorInfo = new IndicatorInfo();
                indicatorInfo.setDisplayName(resultSet.getString("DisplayName"));
                indicatorInfo.setCheckInterval(resultSet.getInt("CheckInterval"));
                indicatorInfo.setNormalValue(resultSet.getString("NormalValue"));
                indicatorInfo.setRequest(resultSet.getString("Request"));
                indicatorInfo.setRequestType(resultSet.getString("RequestType"));
                indicatorInfo.setReturnKey(resultSet.getString("ReturnKey"));
                indicatorInfo.setUrl(resultSet.getString("URL"));
                indicatorInfo.setDisplayOrder(resultSet.getInt("DisplayOrder"));
                
                indicatorInfoList.add(indicatorInfo);
            }
        } catch (SQLException sqle) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL, sqle.getMessage());
            if (sqle.getErrorCode() != SQL_ERROR_TABLE_NOT_FOUND){
                throw new DaoException("SQLException is thrown", sqle);
            }
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);
            tp.methodExit(indicators);
        }
        indicators.setIndicatorsList(indicatorInfoList);
        
        return indicators;
    }

}