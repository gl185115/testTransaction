/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IExtCARequestDAO
 *
 * Interface in DAO for External CA(CreditAuthorization) Request
 *
 * jd185128
 */

package ncr.res.mobilepos.creditauthagency.dao;

import java.util.List;

import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;

/**
 * IExtCARequestDAO is DAO Interface for External Credit Authorization Request.
 *
 */
public interface IExtCARequestDAO {
    /**
     * Saves the CARequest from the external source.
     *
     * @param posLog        The POSLog to save in the TXL_EXTERNAL_CA_REQ.
     * @return int          Return the number of rows affected in the database.
     * @throws Exception    The exception thrown when the process fail.
     */
    int saveExtCARequest(PosLog posLog) throws Exception;
    /**
     * Gets the list of credit CARequest from the external source.
     *
     * @param storeid           The Store ID of the CA Request
     * @param termid            The Terminal ID of the CA Request.
     * @param txdate            The Transaction date of the CA Request.
     * @return List<CreditAuthorizationResp>
     *                          The unsettled Credit Request
     * @throws Exception        The Exception when the function fail.
     */
    List<CreditAuthorizationResp> getExtCARequest(String storeid,
            String termid, String txdate) throws Exception;

    /**
     * Gets the current status of a CARequest from the external source
     * as identified by the following parameters.
     *
     * @param storeid           The Store ID of the CA Request
     * @param termid            The Terminal ID of the CA Request.
     * @param txid              The Transaction ID of the CA Request.
     * @param txdate            The date of the transaction
     * @return CreditAuthorizationResp
     *                          The unsettled Credit Request
     * @throws Exception        The Exception when the function fail.
     */
    CreditAuthorizationResp getExtCARequestStatus(String storeid,
            String termid, String txid, String txdate) throws Exception;

    /**
     * Saves the External CreditAuthorization replied by PastelPort.
     *
     * @param pastelPortResp      The PastelPortResp object
     * @param storeid             The store identifier
     * @param posterminalid       The POS Terminal identifier
     * @param txdate              The transaction date
     * @param txid                The transaction identifier
     * @param type                The type of credit authorization
     * @param status            The status of CARequest.
     * @param operatorcode        The operator number
     * @return int                Return the number of rows affected in the
     *                               database.
     * @throws Exception          The exception thrown when the process fail.
     */
    int saveExtCARequest(PastelPortResp pastelPortResp, String storeid,
            String posterminalid, String txdate, String txid, int type,
            String status, String operatorcode) throws Exception;
}
