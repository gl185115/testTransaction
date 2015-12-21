package ncr.res.mobilepos.queuesignature.dao;

import java.util.List;

import atg.taglib.json.util.JSONException;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuesignature.model.CAInfo;
import ncr.res.mobilepos.queuesignature.model.Transaction;

/**
 *  DAO Interface for Queuing Signature implementation.
 * @author NCRP
 *
 */
public interface IQueueSignatureDao {
    /**
     * Add Signature Request to queue.
     * @param storeid           The StoreID the Signature belongs.
     * @param queue             The Queue the Signature belongs.
     * @param workstationid     The Workstation ID.
     * @param sequencenum       The Sequence Number.
     * @param cainfo            The CA Info.
     * @return The status of adding Signature from Queue.
     * @throws DaoException     The Exception thrown
     *                              when DAO related error occur.
     * @throws JSONException    The exception when JSON parsing error occur.
     */
    int addSignatureRequest(String storeid, String queue,
            String workstationid, String sequencenum, String cainfo)
    throws Exception;
    /**
     * Get the pending Signature requests.
     * @param storeid       The Store ID.
     * @param queue         The Queue.
     * @param businessdate  The Business date
     * @return  The Transaction with regards to the pending Signature.
     * @throws DaoException The Exception thrown when error occur.
     */
    List<Transaction> getPendingSignatureRequests(String storeid,
            String queue, String businessdate) throws DaoException;
    /**
     * Get the Signature Request.
     * @param storeid           The StoreID where the Signtuer belongs.
     * @param queue             The Queue where the Signature belongs.
     * @param posterminalid     The Target POS terminal
     *                               for the Signature request.
     * @param seqnum            The Sequence Number.
     * @param businessdate      The Business Date.
     * @return  The CA Information {@link CAInfo}
     * @throws DaoException The exception thrown when error occur.
     */
    CAInfo getSignatureRequest(String storeid, String queue,
            String posterminalid, String seqnum, String businessdate)
    throws DaoException;
    /**
     * Update the Signature Request.
     * @param status            The status of the Signature Request.
     * @param storeid           The Store ID of the Signature request belongs.
     * @param queue             The Queue for the Signature request.
     * @param posterminalid     The POS terminal ID of the Signature Request.
     * @param seqnum            The Sequence Number.
     * @param businessdate      The Business Date.
     * @return  The Result base containing result code for the function.
     * @throws DaoException The excepion thrown when error occur.
     */
    ResultBase updateSignatureRequest(String status, String storeid,
            String queue, String posterminalid, String seqnum,
            String businessdate) throws DaoException;
}

