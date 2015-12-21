package ncr.res.mobilepos.creditauthorization.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * The Interface ISummaryDAO.
 */
public interface ISummaryDAO {

    /**
     * Select.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean select(CommonTx tx, CommonTx dbtx) throws Exception;

    /**
     * Insert.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean insert(CommonTx tx) throws Exception;

    /**
     * Update sales.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean updateSales(CommonTx tx) throws Exception;

    /**
     * Update init.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean updateInit(CommonTx tx) throws Exception;

    /**
     * Update finish.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    boolean updateFinish(CommonTx tx) throws Exception;
}
