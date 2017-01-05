package ncr.res.mobilepos.authentication.dao;

import ncr.res.mobilepos.authentication.model.SignDetails;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;
/**
 * interface for the authentication dao.
 *
 */
public interface IAuthDeviceDao {
    /**
     * registers a terminal.
     * @param companyid - the company id
     * @param storeid - the store id
     * @param terminalid - the new terminal id to register
     * @param devicename - the name of the device
     * @param udid - unique device identifier
     * @param uuid - universally unique identifier
     * @param signstatus - status if device is registered for signature capture
     * @param signtid - terminal identifier for signature copied from MST_ACTIVATION_LIST
     * @param signactivationkey - activation key for signature copied from MST_ACTIVATION_LIST
     * @return int - the resultcode of the request
     * @throws DaoException - holds the exception that was thrown
     */
	int registerTerminal(String companyid, String storeid, String terminalid, String devicename, 
	        String udid, String uuid, int signstatus, String signtid, String signactivationkey)
			throws DaoException;

    /**
     * authenticates a device.
     * 
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device
     * 
     * @return int - the resultcode of the request
     * 
     * @throws DaoException - holds the exception that was thrown
     */
    int authenticateUser(String storeid, String terminalid) 
    		throws DaoException;

    /**
     * gets the users status.
     * @param corpid - the company id
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device
     * @return int - status of the user
     * @throws DaoException - holds the exception that was thrown
     */
    int getUserStatus(String storeid, String terminalid)
    throws DaoException;

    /**
     * Checks device existence.
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device
     * @param matchall - true if all should be matched,
     * false to check if any of the parameters is already existing
     * @return true if exists, false if not
     * @throws DaoException - holds the exception that was thrown
     */
	boolean isDeviceExisting(String companyid, String storeid, String terminalid, boolean matchall)
			throws DaoException;

    /**
     * updates the existing device's token.
     * @param corpid - the company id
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device
     * @param uuid - the random generated string attributed
     * to the device
     * @param udid - the unique device id of the device
     * @param devicename - the new device name
     * @return true if success, false if fail
     * @throws DaoException - holds the exception that was thrown
     */
    boolean updateExistingDeviceToken(String storeid,
            String terminalid, String udid, String uuid,
            String devicename) throws DaoException;
    /**
     * deletes the device given only the udid.
     * @param udid - the unique device id of the device
     * @return true if success, false if fail
     * @throws DaoException - holds the exception that was thrown
     */
    boolean deleteDeviceWithUdid(String udid)
    throws DaoException;
   
    /**
     * gets the store name.
     * @param corpid - the company id
     * @param storeid - the store id
     * @return String - the store name
     * @throws DaoException - holds the exception that was thrown
     */
    String getStoreName(String corpid, String storeid)
    throws DaoException;
    /**
     * gets the url of the api of the given company id.
     * @param corpid - the company id
     * @param passcode - the passcode for the company id
     * @return ResultBase - the result of the request.
     * the url is in the message.
     * @throws DaoException - holds the exception that was thrown
     */
    ResultBase getWebApiUrl(String corpid, String passcode)
    throws DaoException;
    
    /**
     * gets the url of the api of the given company id.
     * @param corpid - the company id
     * @return String - url of the api
     * @throws DaoException - holds the exception that was thrown
     */
    String getWebApiUrl(String corpid)
    throws DaoException;
    /**
     * gets the name of the given company id.
     * @param corpid - the company id
     * @return String - the name of the company
     * @throws DaoException - holds the exception that was thrown
     */
    String getCorpName(String corpid)
    throws DaoException;

    /**
     * sets the signature activation status.
     * @param storeId - the store id
     * @param terminalId - the terminal id
     * @param udid - the unique id of the device
     * @param uuid - the random generated value
     * attributed to the device.
     * @param signStatus - the new status
     * @param signTid - signature id
     * @param signActivationKey - signature activation key
     * @return int - result of the request
     * @throws DaoException - holds the exception that was thrown
     */
    int setSignatureActivationStatus(String storeId,
            String terminalId, String udid, String uuid, int signStatus,
            String signTid, String signActivationKey) throws DaoException;
    /**
     * gets the signature details.
     * @param companyId - the company id
     * @param storeId - the store id
     * @param terminalId - the terminal id
     * @param udid - the unique id of the device
     * @param uuid - the random generated value
     * attributed to the device.
     * @return SignDetails - the signature details of the terminal
     * @throws DaoException - holds the exception that was thrown
     */
    SignDetails getSignDetails(String companyId, String storeId,
            String terminalId, String udid, String uuid) throws DaoException;

    /**
     * Validates the store passcode.
     * @param storeid - the store id
     * @param passcode - the passcode for the store
     * @return ResultBase - 0 if valid, non-zero if not
     * @throws DaoException - holds the exception that was thrown
     */
	ViewCorpStore validateCorpStore(String companyid, String storeid, String passcode)
			throws DaoException;
}
