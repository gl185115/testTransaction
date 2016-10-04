package ncr.res.mobilepos.queuesignature.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuesignature.dao.IQueueSignatureDao;
import ncr.res.mobilepos.queuesignature.dao.QueueSignatureDao;
import ncr.res.mobilepos.queuesignature.model.CAInfo;
import ncr.res.mobilepos.queuesignature.model.SignatureRequestBill;
import ncr.res.mobilepos.queuesignature.model.SignatureRequestList;
import ncr.res.mobilepos.queuesignature.model.Transaction;


/**
 * QueueSignatureResource is a Web Resource that supports for queuing Signature.
 */
@Path("/QueueSignature")
@Api(value="/QueueSignature", description="外部CAリクエストAPI")
public class QueueSignatureResource {
    /**
     * The IOWriter for the log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer. */
    private Trace.Printer tp;
    private static final String PROG_NAME = "QueueSignatureResource";
    /**
     * Default Constructor.
     */
    public QueueSignatureResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * The Web Method Resource called for Adding Signature Request.
     * @param storeid           The Store ID for the Signature Request.
     * @param queue             The Queue for the Signature Request.
     * @param workstationid     The Workstation ID for the Signature Request.
     * @param sequenceno        The Sequence Number for the signature Request.
     * @param cainfo            The CaInfo for the Signature Request.
     * @return   The ResultBase containing the result code for the method.
     */
    @Path("/add/{RetailStoreID}/{Queue}/{WorkstationID}/{SequenceNumber}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes("application/x-www-form-urlencoded")
    @ApiOperation(value="外部CAリクエスト追加", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ResultBase addSignatureRequest(
            @ApiParam(name="retailstoreid", value="店舗コード") @PathParam("retailstoreid") final String storeid,
            @ApiParam(name="queue", value="キュー") @PathParam("queue") final String queue,
            @ApiParam(name="workstationid", value="端末コード") @PathParam("workstationid") final String workstationid,
            @ApiParam(name="sequencenumber", value="連番") @PathParam("sequencenumber") final String sequenceno,
            @ApiParam(name="cainfo", value="CaInfo") @FormParam("cainfo") final String cainfo) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("WorkstationID", workstationid)
          .println("SequenceNumber", sequenceno)
          .println("CAInfo", cainfo);
        //initialize results
        ResultBase rb = new ResultBase();
        int result = QueueSignatureDao.SIGNATURE_CAPTURE_SUCCESS;

        //check if any of the parameters are lacking
        if (StringUtility.isNullOrEmpty(cainfo, queue,
                workstationid, sequenceno, storeid)) {
            tp.println("Parameter(s) are invalid.")
              .println("Either CAInfo, Queue, WorkstationID,"
                    + " SequenceNumber or StoreID is null/empty.");
            rb = new ResultBase(
                    QueueSignatureDao.SIGNATURE_CAPTURE_ABNORMAL_END,
            "Lacking Parameters");
            tp.methodExit(rb.toString());
            return rb;
        }

        //access the dao to insert the new request
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueSignatureDao qsDao = sqlServer.getQueueSignatureDao();
            result = qsDao.addSignatureRequest(
                    storeid, queue, workstationid, sequenceno, cainfo);
        } catch (Exception e) {
        	LOGGER.logAlert(PROG_NAME, "addSignatureRequest", Logger.RES_EXCEP_GENERAL,
                    "Failed to add signature request. \n"
                               + e.getMessage());
            return new ResultBase(
                    QueueSignatureDao.SIGNATURE_CAPTURE_ABNORMAL_END,
                    "Error: Exception Occurred");
        } finally {
            rb.setNCRWSSResultCode(result);
            tp.methodExit(rb.toString());
        }
        return rb;
    }
    
    /**
     * The Web Method Resource called for getting a List of Signature Request.
     * @param storeID   The Store ID where the signature Request belongs.
     * @param queue     The Queue ID for the Signature Request.
     * @param txDate    The Transaction Date.
     * @return The list of Signature Request.
     */
    @Path("/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="外部CAリクエストステータス検索", response=SignatureRequestBill.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESEXTCA_OK, message="取引売上処理保留"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_DATEINVALID, message="営業日無効"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_NORMEND, message="取引処理済"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_INPROG, message="取引処理中"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_NOTFOUND, message="取引未存在"),
    })
    public final SignatureRequestList getSignatureRequestList(
            @ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String storeID,
            @ApiParam(name="queue", value="キューID") @QueryParam("queue") final String queue,
            @ApiParam(name="txdate", value="取引営業日") @QueryParam("txdate")final String txDate) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("retailstoreid", storeID)
          .println("queue", queue)
          .println("txdate", txDate);
        //initialize results
        SignatureRequestList srl = new SignatureRequestList();
        List<Transaction> result = null;

        //check if any of the parameters are lacking
        if (StringUtility.isNullOrEmpty(queue, storeID, txDate)) {
            srl.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
            tp.println("Parameter(s) are invalid.").println(
                    "Either Queue, StoreId or TxDate is empty or null.");
            tp.methodExit(srl);
            return srl;
        }

        //set the queue id
        srl.setQueue(queue);

        //access the dao to retrieve requests
        
		try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueSignatureDao qsDao = sqlServer.getQueueSignatureDao();

            if (!DateFormatUtility.isLegalFormat(txDate, "yyyy-MM-dd")) {
                srl.setNCRWSSResultCode(
                        ResultBase.RESEXTCA_ERROR_DATEINVALID);
                tp.println("Invalid date format.");
                return srl;
            }

            result = qsDao.getPendingSignatureRequests(
                    storeID, queue, txDate);

            //if their are transactions that are found
            if (result != null && (!result.isEmpty())) {
                srl.setTransactionList(result);
                srl.setNCRWSSResultCode(ResultBase.RESEXTCA_OK);
            } else {
                //else there are no transactions with the specified parameters
                tp.println("No transactions found.");
                srl.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
            }
        } catch (DaoException e) {
            srl.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to retrieve signature request list.\n"
                    + e.getMessage());
        } catch (Exception e) {
            srl.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to retrieve signature request list.\n"
                    + e.getMessage());
        } finally {
            tp.methodExit(srl);
        }
        return srl;
    }
    /**
     * Get a Signature Request.
     * @param storeid           The StoreID for the Signature Request.
     * @param queue             The Queue ID for the Signature Request.
     * @param posterminalid     The POS terminal Id for the Signature Request.
     * @param seqnum            The Sequence Number for the Signature Request.
     * @param businessdate      The date when transaction was uploaded.
     * @return  The Signature Request Bill.
     */
    @Path("/get")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes("application/x-www-form-urlencoded")
    @ApiOperation(value="外部CAリクエストステータス取得", response=SignatureRequestBill.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESEXTCA_OK, message="取引処理保留"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_DATEINVALID, message="営業日無効"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_NORMEND, message="取引処理済"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_INPROG, message="取引処理中"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_NOTFOUND, message="取引未存在"),
    })
    public final SignatureRequestBill getSignatureRequest(
            @ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String storeid,
            @ApiParam(name="queue", value="キューID") @QueryParam("queue") final String queue,
            @ApiParam(name="workstationid", value="端末コード") @QueryParam("workstationid") final String posterminalid,
            @ApiParam(name="sequencenumber", value="連番") @QueryParam("sequencenumber") final String seqnum,
            @ApiParam(name="txdate", value="取引営業日") @QueryParam("txdate") final String businessdate) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("WorkstationID", posterminalid)
          .println("SequenceNumber", seqnum)
          .println("TxDate", businessdate);
        //initialize results
        SignatureRequestBill srb = new SignatureRequestBill();
        CAInfo result = null;

        //check if any of the parameters are lacking
        if (StringUtility.isNullOrEmpty(queue, storeid, businessdate)) {
            srb.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
            tp.println("Parameter(s) are invalid.")
              .println("Either Queue, StoreID, or BusinessDate is lacking.");
            tp.methodExit(srb.toString());
            return srb;
        }

        if (!DateFormatUtility.isLegalFormat(businessdate, "yyyy-MM-dd")) {
            srb.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_DATEINVALID);
            tp.println("Business Date is invalid.")
              .println("Must be in yyyy-MM-dd format.");
            tp.methodExit(srb.toString());
            return srb;
        }
        //set the parameters
        srb.setQueue(queue);
        srb.setRetailStoreID(storeid);
        srb.setSequenceNumber(seqnum);
        srb.setWorkstationID(posterminalid);

        //access the dao to retrieve requests
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueSignatureDao qsDao = sqlServer.getQueueSignatureDao();

            result = qsDao.getSignatureRequest(storeid, queue, posterminalid,
                    seqnum, businessdate);

            //if their are transactions that are found
            if (result.getStatus() == QueueSignatureDao.TRANSACTION_PENDING) {
                srb.setCAInfo(result);
                srb.setNCRWSSResultCode(ResultBase.RESEXTCA_OK);
                tp.println("Transaction is Pending state.");
            } else if (result.getStatus()
                    == QueueSignatureDao.TRANSACTION_PROCESSING) {
                //else there are no transactions with the specified parameters
                srb.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_INPROG);
                tp.println("Transaction is in Processing state.");
            } else if (result.getStatus()
                    == QueueSignatureDao.TRANSACTION_PROCESSED) {
                //else there are no transactions with the specified parameters
                srb.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NORMEND);
                tp.println("Transaction is already Processed.");
            } else if (result.getStatus()
                    == QueueSignatureDao.TRANSACTION_NOT_FOUND) {
                //else there are no transactions with the specified parameters
                srb.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
                tp.println("Transaction not found.");
            }
        } catch (Exception e) {
            srb.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            LOGGER.logAlert(PROG_NAME,
                    "getSignatureRequest",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to retrieve signature request list.\n"
                    + e.getMessage());
            return srb;
        } finally {
            tp.methodExit(srb.toString());
        }
        return srb;
    }
    /**
     * The Web Method called to update Signature Request Status.
     * @param storeid           The Store ID for the Signature Request.
     * @param queue             The Queue ID for the Signature Request.
     * @param posterminalid     The POS terminal ID for the Signature Request.
     * @param seqnum            The Sequence Number.
     * @param status            The Status.
     * @param businessdate      The date when transaction was uploaded.
     * @return  The ResultBase containing the result code fof the method.
     */
    @Path("/update")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes("application/x-www-form-urlencoded")
    @ApiOperation(value="外部CAリクエストステータス更新", response=SignatureRequestBill.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_DATEINVALID, message="営業日無効"),
            @ApiResponse(code=ResultBase.RESEXTCA_ERROR_NOTFOUND, message="売上未存在"),
    })
    public final ResultBase updateSignatureRequestStatus(
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String storeid,
            @ApiParam(name="queue", value="キューID") @FormParam("queue") final String queue,
            @ApiParam(name="workstationid", value="端末ID") @FormParam("workstationid") final String posterminalid,
            @ApiParam(name="sequencenumber", value="連番") @FormParam("sequencenumber") final String seqnum,
            @ApiParam(name="txdate", value="取引営業日") @FormParam("txdate") final String businessdate,
            @ApiParam(name="status", value="ステータス") @FormParam("status") final String status) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("RetailStoreID", storeid)
          .println("Queue", queue)
          .println("WorkstationID", posterminalid)
          .println("SequenceNumber", seqnum)
          .println("Status", status)
          .println("TxDate", businessdate);
        //initialize results
        ResultBase result = new ResultBase();

        //check if any of the parameters are lacking
        if (StringUtility.isNullOrEmpty(storeid, queue, businessdate)) {
            result.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_NOTFOUND);
            tp.println("Parameter(s) are invalid.")
              .println("Either Queue, StoreId or businessdate "
                      + "is empty or null.");
            tp.methodExit(result);
            return result;
        }

        if (!DateFormatUtility.isLegalFormat(businessdate, "yyyy-MM-dd")) {
            result.setNCRWSSResultCode(
                    ResultBase.RESEXTCA_ERROR_DATEINVALID);
            tp.println("Business Date is invalid.")
              .println("Must be in yyyy-MM-dd format.");
            tp.methodExit(result.toString());
            return result;
        }

        //access the dao to retrieve requests
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueSignatureDao qsDao = sqlServer.getQueueSignatureDao();

            result = qsDao.updateSignatureRequest(status, storeid, queue,
                    posterminalid, seqnum, businessdate);
        } catch (Exception e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            LOGGER.logAlert(PROG_NAME,
                    "updateSignatureRequestStatus",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to retrieve signature request list.\n"
                    + e.getMessage());
            return result;
        } finally {
            tp.methodExit(result.toString());
        }
        return result;
    }
}
