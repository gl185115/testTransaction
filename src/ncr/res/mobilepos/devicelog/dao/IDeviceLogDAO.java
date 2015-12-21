package ncr.res.mobilepos.devicelog.dao;

import java.io.OutputStream;
import java.util.Date;

import ncr.res.mobilepos.devicelog.model.DeviceLog;
import ncr.res.mobilepos.exception.DaoException;

import org.apache.commons.fileupload.FileItem;

/**
 * The Interface for File Management of Logs from the Device.
 *
 * @author Developer
 */
public interface IDeviceLogDAO {
    /**
     * Save the Log File from a device.
     * @param udid      The Unique Device ID from which the Log belongs.
     * @param logDate   The date of the Log.
     * @param logFile   The File of log.
     * @return  The information of the Log from the device
     * @throws DaoException The exception thrown when error occur.
     */
    DeviceLog saveLogFile(String udid, Date logDate, FileItem logFile)
    throws DaoException;
    /**
     * Update the device's Log File.
     * @param rowId     The RowID of the Log.
     * @param logFile   The Log file information.
     * @return  The information of the Log from the device.
     * @throws DaoException The exception thrown when error occur.
     */
    DeviceLog updateLogFile(String rowId, FileItem logFile)
    throws DaoException;
    /**
     * Get the device's Log information.
     * @param rowId     The RowID of the Log.
     * @return  The information of the Log from the device.
     * @throws DaoException The exception thrown when error occur.
     */
    DeviceLog getDeviceLogInfo(String rowId) throws DaoException;
    /**
     * Get the device's Log(s) informations.
     * @param udid          The Unique Device ID from which the Log belongs.
     * @param startIndex    The start index on where to get the logs.
     * @param endIndex      The end index on where to get the logs.
     * @return      An array of log information from the device.
     * @throws DaoException The exception thrown when error occur.
     */
    DeviceLog[] getLogsOfDevice(String udid, int startIndex,
            int endIndex)
    throws DaoException;
    /**
     * Read the binary data of the Log.
     *
     * @param rowId     The RowID of the Log.
     * @param out       The output stream for the Log file.
     * @return  True, if success, else false.
     * @throws DaoException The exception thrown when error occur.
     */
    boolean readBinaryData(String rowId, OutputStream out)
    throws DaoException;
}
