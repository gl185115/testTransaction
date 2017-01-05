package ncr.res.mobilepos.deviceinfo.dao;

import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.TerminalStatus;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.model.Till;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * IDeviceInfoDAO Interface.
 */
public interface IDeviceInfoDAO {
    /**
     * Set the Pos Terminal Link association for a device.
     * @param corpid - corp identifier
     * @param storeid - store identifier
     * @param terminalid - terminal identifier
     * @param linkposterminalid - pos terminal identifier to associate to device
     * @return ResultBase
     * @throws SQLException - sql
     * @throws DaoException - dao
     */
    ResultBase setLinkPosTerminalId(String storeid, String terminalid,
            String linkposterminalid) throws Exception;
    /**
     * Set the Printer Id association for a device.
     * @param storeid  store identifier
     * @param terminalid terminal identifier
     * @param printerid  printer identifier to associate to device
     * @return ResultBase
     * @throws SQLException The exception thrown when SQL related issue fail.
     * @throws DaoException The exception thrown for non-SQL related issue.
     */
    ResultBase setPrinterId(String storeid,
            String terminalid, String printerid, String updAppId, String updOpeCode) throws Exception;
    /**
     * Create peripheral device association for a terminal/device.
     * @param deviceInfo    The Device information to be added.
     * @return ResultBase
     * @throws SQLException The exception thrown when SQL related issue fail.
     * @throws DaoException The exception thrown for non-SQL related issue.
     * @throws IOException 
     */
    ResultBase createPeripheralDeviceInfo(DeviceInfo  deviceInfo)
    throws Exception;
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
    
    /**
     * Get Printers registered in the store configuration.
     * @param storeid - store identifier
     * @param key - search key identifier
     * @param name - the current search for printer description
     * @param limit - if 0, use the systemConfig defined limit
     *                if -1, no limit
     *                if any int value, search limit
     * @return ArrayList<PrinterInfo> array of PrinterInfo
     * @throws SQLException - sql
     * @throws DaoException - dao
     */
    List<PrinterInfo> getAllPrinterInfo(
            String storeid, String key, String name, int limit) throws Exception;   
    
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
     * List devices.
     *
     * @param key The key to search. Either a device ID or Name.
     * @param storeId The storeid.
     * @param limit 
     * @param name 
     * @return the list of device.
     * @throws DaoException thrown when error occurs.
     */
    List<DeviceInfo> listDevices(String storeId, String key, String name, int limit) throws DaoException;
    
    /**
     * Update device.
     * @param companyid  - company identifier
     * @param storeid    - store identifier
     * @param terminalid - terminal identifier
     * @param deviceinfo - the model that contains the new values
     * @param connection - connection for sql query
     * @return ViewDeviceInfo
     * @throws Exception - exception
     */
    ViewDeviceInfo updateDevice(String companyid,
            String storeid, String terminalid,
            DeviceInfo deviceinfo, int trainingmode, Connection connection) throws Exception;
    
    /**
     * Update printer info.
     * @param storeid - store identifier
     * @param printerID The Printer ID.
     * @param printerinfo - the model that contains the new values
     * @return ViewPrinterInfo
     * @throws Exception - exception
     */
    ViewPrinterInfo updatePrinterInfo(
            String storeID, String printerID, PrinterInfo printerinfoToSet) throws Exception;

    /**
     * Create Printer Information.
     * @param storeID   The Retail Store ID.
     * @param printerID The Printer ID.
     * @param printerInfo   The Printer Information.
     * @return  The Result Base.
     * @throws DaoException The exception thrown when error occur.
     */
    ResultBase createPrinterInfo(String storeID, String printerID,
            PrinterInfo printerInfo) throws DaoException;
    
    /**
     * Delete Printer Information.
     * @param storeId   The Retail Store ID.
     * @param printerId The Printer ID.
     * @param 
     * @return  The Result Base.
     * @throws DaoException The exception thrown when error occur.
     */
    ResultBase deletePrinter(String storeId, String printerId, String updAppId, String updOpeCode) throws DaoException;
    
    /**
     * Set the Signature Link for the device.
     * @param retailStoreID     The Retail Store ID.
     * @param terminalID        The Device ID.
     * @param signatureLink     The POS Link ID for Signature.
     * @param signatureLink     The POS Link ID for Signature.
     * @param appId - application identifier
     * @return ResultBase object with result code.
     * @throws DaoException     Thrown when exception occurs.
     */
    ResultBase setSignatureLink(String retailStoreID, String terminalID,
            String signatureLink, String appId, String opeCode) throws DaoException;
    /**
     * Set the Signature Link for the device.
     * @param retailStoreID     The Retail Store ID.
     * @param terminalID        The Device ID.
     * @param signatureLink     The POS Link ID for Signature.
     * @param appId - application identifier
     * @param opeCode - operator code
     * @return ResultBase object with result code.
     * @throws DaoException     Thrown when exception occurs.
     */
    ResultBase setAuthorizationLink(String retailStoreID, String terminalID,
            String signatureLink, String appId, String opeCode) throws DaoException;

    /**
     * Set the QueueBuster Link for a device.
     * @param storeid - store identifier
     * @param terminalid - terminal identifier
     * @param queuebusterlink - link identifier for the queue buster
     * @param appId - application identifier
     * @param opeCode - operator code
     * @return ResultBase - result of the operation
     * @throws SQLException - exception due to sql execution
     * @throws DaoException - exception in the dao
     */
    ResultBase setQueueBusterLink(String storeid,
            String terminalid, String queuebusterlink, String appId, String opeCode
            ) throws Exception;
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
     * Sets the Till Id association for a device/terminal.
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @param tillId		- The till/drawer identifier.
     * @param updAppId		- The application identifier.
     * @param updOpeCode	- The identifier of the operator who performs the operation.
     * @return ResultBase	- The result of the operation.
     * @throws DaoException	- Thrown when DAO error is encountered.
     */
    ResultBase setTillId(final String storeId, final String terminalId, 
    		final String tillId,final String updAppId, final String updOpeCode) 
    				throws DaoException;

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
}
