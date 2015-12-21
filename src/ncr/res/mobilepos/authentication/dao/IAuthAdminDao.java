package ncr.res.mobilepos.authentication.dao;

import ncr.res.mobilepos.exception.DaoException;

/**
 * interface for the dao class for authentication
 * administrator side.
 *
 */
public interface IAuthAdminDao {
    /***
     * validate the passcode.
     * @param passcode - the passcode to validate
     * @return int - the result of the request
     * @throws DaoException - thrown when an exception occurs
     */
    int validatePasscode(String passcode) throws DaoException;
    /**
     * sets the passcode of the administrator.
     * @param passcode - the new passcode
     * @param adminKey - the key to validate user
     * @param expiry - number of miliseconds before the new passcode
     * will expire
     * @return int - the result of the request
     * @throws DaoException - thrown when an exception occurs
     */
    int setPasscode(String passcode, String adminKey, int expiry)
    throws DaoException;
    /**
     * sets the admin key.
     * @param currentkey - the current admin key for validation
     * @param newkey - the new admin key
     * @return int - result of the request
     * @throws DaoException - thrown when an exception occurs
     */
    int setAdminKey(String currentkey, String newkey) throws DaoException;
    /**
     * removes an existing device.
     * @param deviceid - the terminal id of the device to remove
     * @param adminKey - the key for validation
     * @return int - result of the request
     * @throws DaoException - thrown when an exception occurs
     */
    int removeDevice(String deviceid, String adminKey) throws DaoException;
    /**
     * gets the duration of validity.
     * @return int - number of miliseconds that the status of
     * the device is valid
     * @throws DaoException - thrown when an exception occurs
     */
    int getActiveValidityDuration() throws DaoException;
}
