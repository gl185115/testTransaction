package ncr.res.mobilepos.creditauthorization.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * The Interface ILoginCheckDAO.
 */
public interface ILoginCheckDAO {

    /**
     * Gets the logging information.
     *
     * @param tx
     *            the tx
     * @return the logging info
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean getLoggingInfo(CommonTx tx) throws Exception;

    /**
     * Save logging info.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean saveLoggingInfo(CommonTx tx) throws Exception;
}
