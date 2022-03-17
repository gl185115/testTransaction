package ncr.res.mobilepos.deviceinfo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.Indicators;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.TerminalStatus;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.model.Till;

/**
 * IDeviceInfoDAO Interface.
 */
public interface IDeviceInfoDAO {

    /**
     * Get Printer Information for a Printer Id.
     * @param storeid - store identifier
     * @param printerid - printer to be identified
     * @return ResultBase
     * @throws SQLException The exception thrown when SQL related issue fail.
     * @throws DaoException The exception thrown for non-SQL related issue.
     */
    PrinterInfo getPrinterInfo(String storeid,
            String printerid)
    throws SQLException, DaoException;

    //FOR NEW DEVICE INFO FUNCTIONS --------------------
    /**
     * Retrieve device info for a specific device.
     * @param storeid - store identifier
     * @param terminalid - terminal identifier
     * @return ViewDeviceInfo
     * @throws Exception - exception
     */
    DeviceInfo getDeviceInfo(
            String companyId, String storeid, String terminalid, int trainingmode) throws Exception;

    /**
     * Delete device.
     *
     * @param deviceID
     *            the device to delete.
     * @param retailStoreID
     * @param appId
     *            the application ID that updates the device.
     * @param opeCode
     *            the operator code that attempts to update the device.
     *            the storeid where the device belongs.
     * @return the ResultBase object that holds resultcode. 0 for success.
     *
     * @throws DaoException
     *             the exception thrown when exception occurs.
     */
    ResultBase deleteDevice(String deviceID, String retailStoreID, String appId, String opeCode)
            throws DaoException;

    /**
     * Update last txid at journal service.
     * @param transaction object of poslog
     * @param trainingMode
     * @param db connection object
     * @return
     * @throws DaoException
     */
    boolean updateLastTxidAtJournal(final Transaction transaction,
    		final Connection connection, int trainingMode) throws DaoException;
    /**
     * Update last txid at credit authorization service.
     * @param jsonStr
     * @return
     * @throws DaoException
     */
    boolean updateLastTxidAtCreditAuth(final String jsonStr) throws DaoException;
    /**
     * Update last suspend txid at queue buster service.
     * @param posLog object
     * @param db connection object
     * @return
     * @throws DaoException
     */
    boolean updateLastSuspendTxidAtQueueBuster(final PosLog posLog,
    		final Connection connection) throws DaoException;

    /**
     * Gets tills/drawers registered in the store configuration.
     * @param storeId - store identifier
     * @param key - key identifier
     * @param limit - indicates the limit/size of the result set to be returned
     * 			    - if 0, use the systemConfig defined limit
     *                if -1, no limit
     *                if any int value, search limit
     * @return List<Till> the list of Till
     * @throws DaoException
     */
    List<Till> getAllTills(final String storeId, final String key,
    		final int limit) throws DaoException;

    /**
     * Gets the Device Attribute.
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @return Attribute	- The Info of the Device Attribute.
     * @throws DaoException	- Thrown when DAO error is encountered.
     */
    ResultBase getAttributeInfo(final String storeId,final String terminalId, String companyId, int training)	throws DaoException;

	/**
     * Gets the Attribute Info of Device.
     * @param companyId     - The company identifier
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @return DeviceAttribute	- The Info of the Device Attribute.
     * @throws DaoException	- Thrown when DAO error is encountered.
     */
	DeviceAttribute getDeviceAttributeInfo(String companyId, String storeId, String terminalId)
					throws DaoException;

	/**
	 * Gets the terminal info of a terminal.
	 * @param companyyId 	- The company identifier.
	 * @param storeId		- The store identifier.
	 * @param terminalId	- The terminal identifier.
	 * @return TerminalInfo	- The info pertaining to the terminal.
	 * @throws Exception	- Thrown when an error is encountered.
	 */
	ViewTerminalInfo getTerminalInfo(String companyyId, String storeId,
			String terminalId) throws Exception;

    /**
     * Gets Terminal Open-Close status from TXU_POS_CTRL.OpenCloseStatus.
     * @param storeId - Store ID
     * @param terminalId - Terminal ID
     * @param companyId - Company ID
     * @return PosControlOpenCloseStatus for successful return, ResultBase for failure.
     */
    ResultBase getPosCtrlOpenCloseStatus(String companyId, String storeId, String terminalId, String thisBusinessDay)
            throws DaoException;

    /**
     * Get working device status from AUT_DEVICES, TXU_POS_CTRL, MST_DEVICEINFO.
     * @return WorkingDevice - a class which holds the status of the working device
     * @throws DaoException - holds the exception that was thrown
     */
    List<TerminalStatus> getWorkingDeviceStatus() throws DaoException;
    
    /**
     * Get indicatorInfo from PRM_DEVICE_INDICATOR
     * @param attributeid - attributeid
     * @return Indicators
     * @throws DaoException - holds the exception that was thrown
     */
    Indicators getDeviceIndicators(String attributeid) throws DaoException;

    /**
     * Gets the Additional Attribute Info of Device.
     * @param companyId     - The company identifier
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @return DeviceAttribute	- The Info of the Device Attribute.
     * @throws DaoException	- Thrown when DAO error is encountered.
     */
	ResultBase getAdditionalDeviceAttributeInfo(String storeId, String terminalId, String companyId,
			int parsedTraining) throws DaoException;
}
