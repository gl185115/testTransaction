package ncr.res.mobilepos.creditauthorization.dao;

import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * ILogDAO is a DAO interface for Credit Authorization.
 */
public interface ILogDAO {

    /**
     * Select.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean select(CommonTx tx, CommonTx dbtx) throws DaoException,
            JSONException;

    /**
     * Insert.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean insert(CommonTx tx) throws Exception;

    /**
     * Update rca.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean updateRca(CommonTx tx, PastelPortResp resp) throws Exception;

    /**
     * Update sdc.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean updateSdc(CommonTx tx) throws Exception;

    /**
     * Update rdc.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean updateRdc(CommonTx tx, PastelPortResp resp) throws Exception;

    /**
     * Update void.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean updateVoid(CommonTx tx) throws Exception;

    /**
     * Gets the max serverpaymentseq.
     *
     * @param tx
     *            the tx
     * @return the max serverpaymentseq
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    int getMaxServerPaymentseq(CommonTx tx) throws Exception;

    /**
     * Update server paymentseq.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @return the int
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    int updateServerPaymentseq(CommonTx tx, PastelPortResp resp)
            throws Exception;

    /**
     * Select selectVoid.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    boolean selectVoid(CommonTx tx, CommonTx dbtx) throws Exception;
}
