package ncr.res.mobilepos.promotion.resource;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.ue.core.Connection;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.action.Adjustment;
import ncr.res.ue.message.action.GSOneCoupon;
import ncr.res.ue.message.action.ItemEntry;
import ncr.res.ue.core.Transaction;
import ncr.res.ue.message.response.AdjustmentResponse;
import ncr.res.ue.message.response.BeginTransactionResponse;
import ncr.res.ue.message.response.CancelOperationResponse;
import ncr.res.ue.message.response.ConnectionInitializationResponse;
import ncr.res.ue.message.response.GS1CouponResponse;
import ncr.res.ue.message.response.ItemPointsResponse;
import ncr.res.ue.message.response.ItemQuantityResponse;
import ncr.res.ue.message.response.ItemSVResponse;
import ncr.res.ue.message.response.MemberIdResponse;
import ncr.res.ue.message.response.ResumeResponse;
import ncr.res.ue.message.response.SuspendTransactionResponse;
import ncr.res.ue.message.response.TenderEntryResponse;
import ncr.res.ue.message.response.TotalResponse;
import ncr.res.ue.message.response.TriggerCodeResponse;
import ncr.res.ue.message.response.UEResponseBase;
import ncr.res.ue.message.response.EndTransactionResponse;

/**
 * UePromotionResource Class is a Web Resource which support
 * MobilePOS Promotion processes.
 *
 */
@Path("/promotion/ue")
public class UePromotionResource {
    /** A private member variable used for the servlet context. */
    @Context
    private ServletContext context; //to access the web.xml
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;
    /**
     * Default Constructor for UePromotionResource.
     *
     * <P>Initializes the logger object.
     */
    public UePromotionResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
    /**
     * A dummy web resource method.
     *
     * @return  returns "Promotion OK" always.
     */
    @Path("/dodummy")
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public final String doDummy() {
        return "UE Promotion OK ";
    }

    /**
     * Initialize the UE Connection.
     * @return {@link ConnectionInitializationResponse}
     */
    @Path("/connection/init")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ConnectionInitializationResponse ueInitiliaze() {
        tp.methodEnter("UeInitiliaze");
        ConnectionInitializationResponse result = null;
        try {
            Connection connection = GlobalConstant.getUeConnection();
            if (connection == null) {
                connection = new Connection(context);
            }
            result =
                connection.initializeUeConnection("0");
            if (result.getResponseFlag() == 0) {
                GlobalConstant.setUeConnection(connection);
            } else {
                tp.println("UE Connection Initialization failed"
                        + "with response : ", result.getResponseFlag());
                GlobalConstant.setUeConnection(null);
            }
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn",
                    "UePromotionResource.initiliazeUe",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to initialize UE connection.\n" + e.getMessage());
            result = new ConnectionInitializationResponse(
                    ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return result;
    }
    /**
     * Stop Ue Connection.
     * @return {@link ResultBase}
     */
    @Path("/connection/close")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase closeUeConnection() {
        ResultBase result = new ResultBase();
        Connection connection = GlobalConstant.getUeConnection();
        if (connection != null) {
            connection.closeConnection();
            GlobalConstant.setUeConnection(null);
        }
        return result;
    }

    /**
     * Send item entry.
     *
     * @param terminalID
     *            the terminal id
     * @param transactionID
     *            the transaction id
     * @param jsonItemEntry
     *            the json format of an item entry
     * @return the list of UEResponseBase (ItemEntryResponse and/or
     *         DiscountResponse)
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/itementry")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final List<UEResponseBase> sendItemEntry(
            @FormParam("termid") final String terminalID,
            @FormParam("txid") final String transactionID,
            @FormParam("itementry") final String jsonItemEntry) {
        String functionName = "sendItemEntry";
        tp.methodEnter(functionName).println("termid", terminalID)
                .println("txid", transactionID)
                .println("itementry", jsonItemEntry);

        List<UEResponseBase> responses = new ArrayList<UEResponseBase>();

        try {

            JsonMarshaller<ItemEntry> jsonMarshall =
                new JsonMarshaller<ItemEntry>();
            ItemEntry itemEntry = jsonMarshall.unMarshall(jsonItemEntry,
                    ItemEntry.class);

            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            responses = transactionObj.sendItemEntry(terminalID, transactionID,
                    itemEntry.getItemEntryID(), itemEntry.getEntryFlag(),
                    itemEntry.getReturnFlag(), itemEntry.getItemCode(),
                    itemEntry.getFamilyCode(), itemEntry.getDepartment(),
                    itemEntry.getClearanceLevel(),
                    itemEntry.getPricePrecision(), itemEntry.getUnitPrice(),
                    itemEntry.getQuantityType(),
                    itemEntry.getQuantitySubType(),
                    itemEntry.getQuantityPrecision(), itemEntry.getQuantity(),
                    itemEntry.getDiscountFlag(), itemEntry.getTlqFlag(),
                    itemEntry.getAlternatePrice());

        } catch (MessageException e) {
        	LOGGER.logAlert("Prmtn", "UePromotionResource." + functionName,
                    Logger.RES_EXCEP_ENCODING, "Failed to send item entry.\n"
                            + e.getMessage());
        } catch (IOException e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource." + functionName,
                    Logger.RES_EXCEP_IO,
                    "Failed to send item entry.\n" + e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource." + functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to send item entry.\n" + e.getMessage());
        } finally {
            tp.methodExit(responses.size());
        }

        return responses;
    }
    /**
     * Begin the Transaction.
     * @param transactionmode   The transaction mode
     * @param operator  The operator
     * @param termid    The terminal ID
     * @param transactionid The transaction ID
     * @return {@link BeginTransactionResponse}
     */
    @Path("/begintransaction")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final BeginTransactionResponse beginTransaction(
            @QueryParam("transactionmode") final int transactionmode,
            @QueryParam("operator") final String operator,
            @QueryParam("termid") final String termid,
            @QueryParam("transactionid") final String transactionid) {

        tp.methodEnter("BeginTransaction")
            .println("Transaction Mode", transactionmode)
            .println("Operator", operator)
            .println("Term ID", termid)
            .println("Transaction ID", transactionid);

        BeginTransactionResponse beginTrans = null;

        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            beginTrans = transactionObj.beginTransaction(transactionmode,
                    0, operator, termid, transactionid);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.beginTransaction",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to begin the transaction. \n"
                    + e.getMessage());
            beginTrans = new BeginTransactionResponse(
                    ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return beginTrans;
    }

    /**
     * Gets the persistent connection.
     *
     * @param fromContext the from context
     * @return the persistent connection
     * @throws Exception the exception
     */
    private Connection getPersistentConnection(final ServletContext fromContext)
            throws Exception {
        Connection connection = GlobalConstant.getUeConnection();

        if (connection == null) {
            connection = new Connection(fromContext);
            ConnectionInitializationResponse result;

            result = connection.initializeUeConnection("0");

            if (result.getResponseFlag() == 0) {
                GlobalConstant.setUeConnection(connection);
            } else {
                GlobalConstant.setUeConnection(null);
            }
        }
        return connection;
    }

    /**
     * Total amount.
     * @param totalprecision    Precision of Total
     * @param total     Total Amount
     * @param usertotal User Total Amount
     * @param termid    Terminal ID
     * @param transactionid  Transaction ID
     * @return {@link TotalResponse}
     */
    @Path("/total")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final TotalResponse total(
            @QueryParam("totalprecision") final int totalprecision,
            @QueryParam("total") final int total,
            @QueryParam("usertotal") final int usertotal,
            @QueryParam("termid") final String termid,
            @QueryParam("transactionid") final String transactionid) {

        tp.methodEnter("Total")
        .println("Total Precision", totalprecision)
        .println("Total", total)
        .println("User Total", usertotal)
        .println("Term ID", termid)
        .println("Transaction ID", transactionid);

        TotalResponse totalRes = null;
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transObj = new Transaction(connection);
            totalRes = transObj.totalTx(totalprecision,
                    total, usertotal, termid, transactionid);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.total",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get total. \n" + e.getMessage());
            totalRes = new TotalResponse(
                    ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return totalRes;
    }

    /**
     * End the Transaction.
     * @param status Transaction status
     * @param termid Terminal ID
     * @param transactionid Transaction ID
     * @return {@link EndTransactionResponse}}
     */
    @Path("/endtransaction")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final EndTransactionResponse endTransaction(
            @QueryParam("status") final int status,
            @QueryParam("termid") final String termid,
            @QueryParam("transactionid") final String transactionid) {

        tp.methodEnter("EndTransaction")
        .println("Status", status).println("Term ID", termid)
        .println("Transaction ID", transactionid);

        EndTransactionResponse endTrans = null;

        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            endTrans = transactionObj.endTransaction(status,
                    termid, transactionid);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.endTransaction",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            endTrans = new EndTransactionResponse(
                    ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return endTrans;
    }

    /**
     * The Web Resource for Trigger Code.
     * @param termID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param entryFlag The Entry Flag.
     * @param triggerEntryID The Trigger Entry ID.
     * @param triggerCode The Trigger Code.
     * @return The {@link UEResponseBase}
     */
    @Path("/triggercode")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final List<UEResponseBase> triggerCode(
            @QueryParam("termid") final String termID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("entryflag") final int entryFlag,
            @QueryParam("triggerentryid") final int triggerEntryID,
            @QueryParam("triggercode") final int triggerCode) {
        tp.methodEnter("triggerCode")
            .println("Terminal ID", termID)
            .println("Transaction ID", transactionID)
            .println("Entry Flag", entryFlag)
            .println("TriggerEntryID", triggerEntryID)
            .println("TriggerCode", triggerCode);
        List<UEResponseBase> ueRespList = null;
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            ueRespList =
                transactionObj.triggerCode(
                        termID, transactionID, triggerEntryID,
                        entryFlag, triggerCode);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.triggerCode",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            ueRespList = new ArrayList<UEResponseBase>();
                ueRespList.add(
                        new TriggerCodeResponse(ResultBase.RES_ERROR_GENERAL));
        } finally {
            tp.methodExit();
        }
        return ueRespList;
    }

    /**
     * The Web Resource for Item Quantity Code.
     * @param termID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param entryFlag The Entry Flag.
     * @param itemEntryID The Trigger Entry ID.
     * @param rewardCheckSum The Reward Check Sum.
     * @param quantity The Quantity.
     * @return The List of UE Response Base.
     */
    @Path("/itemquantity")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final List<UEResponseBase> itemQuantity(
            @QueryParam("termid") final String termID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("entryflag") final int entryFlag,
            @QueryParam("itementryid") final int itemEntryID,
            @QueryParam("rewardchecksum") final int rewardCheckSum,
            @QueryParam("quantity") final int quantity) {
        tp.methodEnter("itemQuantity")
            .println("Terminal ID", termID)
            .println("Transaction ID", transactionID)
            .println("Entry Flag", entryFlag)
            .println("Item Entry ID", itemEntryID)
            .println("Reward Check Sum", rewardCheckSum)
            .println("Quantity", quantity);
        List<UEResponseBase> itemQtyListResponse = null;
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            itemQtyListResponse =
                transactionObj.itemQuantity(termID, transactionID, entryFlag,
                        itemEntryID, rewardCheckSum, quantity);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.itemQuantity",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            itemQtyListResponse = new  ArrayList<UEResponseBase>();
            itemQtyListResponse
                .add(new ItemQuantityResponse(ResultBase.RES_ERROR_GENERAL));
        } finally {
            tp.methodExit();
        }
        return itemQtyListResponse;
    }

    /**
     * The Item Store Value.
     * @param termID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param itemEntryID The Item Entry ID.
     * @param itemQuantity The Item Quantity.
     * @param programID The ProgramID.
     * @param quantity The Quantity.
     * @param expiration The Expiration.
     * @return The {@link ItemSVResponse}
     */
    @Path("/itemstoredvalue")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ItemSVResponse itemStoredValue(
            @QueryParam("termid") final String termID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("itementryid") final int itemEntryID,
            @QueryParam("itemquantity") final int itemQuantity,
            @QueryParam("programid") final int programID,
            @QueryParam("quantity") final int quantity,
            @QueryParam("expiration") final String expiration) {
        ItemSVResponse itemSVResp = null;
        tp.methodEnter("itemStoredValue")
            .println("termid", termID)
            .println("transactionid", transactionID)
            .println("itementryid", itemEntryID)
            .println("itemquantity", itemQuantity)
            .println("programid", programID)
            .println("quantity", quantity)
            .println("expiration", expiration);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            itemSVResp =
                transactionObj.itemStoreValue(
                        termID, transactionID, itemEntryID,
                        itemQuantity, programID, quantity, expiration);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.itemStoredValue",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            itemSVResp =
                new ItemSVResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return itemSVResp;
    }

    /**
     * The Cancel Operation.
     * @param termID The TerminalID.
     * @param transactionID The Transaction ID.
     * @param reasonFlag The Reason Flag.
     * @return The {@link CancelOperationResponse}
     */
    @Path("/canceloperation")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final CancelOperationResponse cancelOperation(
            @QueryParam("termid") final String termID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("reasonflag") final int reasonFlag) {
        CancelOperationResponse cancelOpResp = null;
        tp.methodEnter("cancelOperation")
            .println("termid", termID)
            .println("transactionid", transactionID)
            .println("reasonflag", reasonFlag);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            cancelOpResp =
                transactionObj.cancelOperation(termID,
                        transactionID, reasonFlag);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.cancelOperation",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            cancelOpResp =
                new CancelOperationResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return cancelOpResp;
    }

    /**
     * The MemberID operation.
     * @param termID    The terminal ID.
     * @param transactionID The Transaction ID.
     * @param memberID  The member ID.
     * @param entryFlag The Entry Flag.
     * @param type  The Type.
     * @return  The {@link MemberIdResponse}
     */
    @Path("/memberid")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final MemberIdResponse memberID(
            @QueryParam("termid") final String termID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("memberid") final BigInteger memberID,
            @QueryParam("entryflag") final int entryFlag,
            @QueryParam("type") final int type) {
        MemberIdResponse memberidOpResp = null;
        tp.methodEnter("memberID")
            .println("termid", termID)
            .println("transactionid", transactionID)
            .println("memberID", memberID)
            .println("entryFlag", entryFlag)
            .println("type", type);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            memberidOpResp =
                transactionObj.memberID(termID, transactionID,
                        memberID, entryFlag, type);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.memberID",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            memberidOpResp =
                new MemberIdResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return memberidOpResp;
    }

    /**
     * The Adjustment operation.
     * @param termID    The terminal ID.
     * @param transactionID The Transaction ID.
     * @param adjustmentJSON The Adjustment JSON.
     * @return  The {@link AdjustmentResponse}
     */
    @Path("/adjustment")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final AdjustmentResponse adjustment(
            @FormParam("termid") final String termID,
            @FormParam("transactionid") final String transactionID,
            @FormParam("adjustment") final String adjustmentJSON) {
        AdjustmentResponse adjustmentResp = null;
        tp.methodEnter("adjustment")
            .println("Term ID", termID)
            .println("Transaction ID", transactionID)
            .println("Adjustment", adjustmentJSON);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            JsonMarshaller<Adjustment> adjustMarshaller =
                new JsonMarshaller<Adjustment>();
            Adjustment adjustment =
                adjustMarshaller.unMarshall(adjustmentJSON, Adjustment.class);
            adjustmentResp =
                transactionObj.adjustment(termID, transactionID, adjustment);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.adjustment",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            adjustmentResp =
                new AdjustmentResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return adjustmentResp;
    }

    /**
     * The Tender Entry operation.
     * @param terminalID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param entryID The Entry ID.
     * @param entryFlag The Entry Flag.
     * @param tenderID The Tender ID.
     * @param tenderSubID The Tender Sub ID.
     * @param bin The BIN.
     * @param tenderPrecision The Tender Precision.
     * @param tenderAmt The Tender Amount.
     * @return  The List of {@link UEResponseBase}.
     */
    @Path("/tenderentry")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final List<UEResponseBase> tenderEntry(
            @QueryParam("termid") final String terminalID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("entryid") final int entryID,
            @QueryParam("entryflag") final int entryFlag,
            @QueryParam("tenderid") final int tenderID,
            @QueryParam("tendersubid") final int tenderSubID,
            @QueryParam("bin") final String bin,
            @QueryParam("tenderprecision") final int tenderPrecision,
            @QueryParam("tenderamount") final int tenderAmt) {
        List<UEResponseBase> listUEResponseBase = null;
        tp.methodEnter("tenderEntry")
            .println("Terminal ID", terminalID)
            .println("Transaction ID", transactionID)
            .println("Entry ID", entryID)
            .println("Entry Flag", entryFlag)
            .println("Tender ID", tenderID)
            .println("Tender Sub ID", tenderSubID)
            .println("bin", bin)
            .println("Tender Precision", tenderPrecision)
            .println("Tender Amount", tenderAmt);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            listUEResponseBase =
                transactionObj.sendTenderEntry(terminalID,
                        transactionID, entryID, entryFlag,
                        tenderID, tenderSubID, bin, tenderPrecision, tenderAmt);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.tenderEntry",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            listUEResponseBase = new ArrayList<UEResponseBase>();
            listUEResponseBase
                .add(new TenderEntryResponse(ResultBase.RES_ERROR_GENERAL));
        } finally {
            tp.methodExit();
        }
        return listUEResponseBase;
    }

    /**
     * The Suspend Operation.
     * @param terminalID    The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param suspendFlag   The Suspend Flag.
     * @param resumeId      The Resume ID.
     * @return The {@link SuspendTransactionResponse}
     */
    @Path("/suspend")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final SuspendTransactionResponse suspend(
            @QueryParam("termid") final String terminalID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("suspendflag") final int suspendFlag,
            @QueryParam("resumeid") final int resumeId) {
        SuspendTransactionResponse suspendResp = null;
        tp.methodEnter("suspend")
            .println("Terminal ID", terminalID)
            .println("Transaction ID", transactionID)
            .println("Suspend Flag", suspendFlag)
            .println("Resume ID", resumeId);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            suspendResp =
                transactionObj.suspendTransaction(suspendFlag,
                        resumeId, terminalID, transactionID);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.suspend",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            suspendResp =
                new SuspendTransactionResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return suspendResp;
    }

    /**
     * The Resume Operation.
     * @param terminalID    The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param resumeFlag    The Resume Flag.
     * @param resumeId      The Resume ID.
     * @return The {@link ResumeResponse}
     */
    @Path("/resume")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResumeResponse resume(
            @QueryParam("termid") final String terminalID,
            @QueryParam("transactionid") final String transactionID,
            @QueryParam("resumeflag") final int resumeFlag,
            @QueryParam("resumeid") final String resumeId) {
        ResumeResponse resumeResp = null;
        tp.methodEnter("suspend")
            .println("Terminal ID", terminalID)
            .println("Transaction ID", transactionID)
            .println("Resume Flag", resumeFlag)
            .println("Resume ID", resumeId);
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            resumeResp =
                transactionObj.resumeTransaction(resumeFlag,
                        resumeId, terminalID, transactionID);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.resume",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to end the transaction. \n" + e.getMessage());
            resumeResp =
                new ResumeResponse(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return resumeResp;
    }
    /**
     * Item Points Response.
     * @param itementryid   Item Entry ID.
     * @param itemquantity  Item Quantity.
     * @param programid     Program ID.
     * @param quantity      Quantity.
     * @param termid        Terminal ID.
     * @param transactionid Transaction ID.
     * @return The {@link ItemPointsResponse}
     */
    @Path("/itempoints")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ItemPointsResponse itemPoints(
            @QueryParam("itementryid") final int itementryid,
            @QueryParam("itemquantity")final int itemquantity,
            @QueryParam("programid")final int programid,
            @QueryParam("quantity")final int quantity,
            @QueryParam("termid") final String termid,
            @QueryParam("transactionid") final String transactionid) {

        tp.methodEnter("ItemPoints")
        .println("ItemEntryID", itementryid)
        .println("ItemQuantity", itemquantity)
        .println("ProgramID", programid)
        .println("Quantity", quantity)
        .println("TermID", termid)
        .println("TransactionID", transactionid);

        ItemPointsResponse itemPointsResp = null;
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            itemPointsResp = transactionObj.itemPoints(termid,
                    transactionid, itementryid, itemquantity,
                    programid, quantity);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.itemPoints",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get item points. \n"
                    + e.getMessage());
            itemPointsResp = new ItemPointsResponse(
                    ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit();
        }
        return itemPointsResp;
    }

    /**
     * GS1_Coupon Response.
     * @param termid    The Terminal ID.
     * @param transactionid The Transaction ID.
     * @param gsonecouponjson   The GS1_Coupon JSON.
     * @return  {@link GS1CouponResponse}
     */
    @Path("/gsonecoupon")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final GS1CouponResponse gsOneCoupon(
            @QueryParam("termid") final String termid,
            @QueryParam("transactionid") final String transactionid,
            @QueryParam("gsonecoupon")final String gsonecouponjson) {

        tp.methodEnter("GS1Coupon")
        .println("TermID", termid)
        .println("TransactionID", transactionid)
        .println("GS1Coupon", gsonecouponjson);

        GS1CouponResponse gsOneCouponResp = null;
        try {
            Connection connection = getPersistentConnection(context);
            Transaction transactionObj = new Transaction(connection);
            JsonMarshaller<GSOneCoupon> gsCouponMarshaller =
                new JsonMarshaller<GSOneCoupon>();
            GSOneCoupon gsCoupon =
                gsCouponMarshaller.unMarshall(
                        gsonecouponjson, GSOneCoupon.class);
            gsOneCouponResp =
                transactionObj.gsOneCoupon(termid, transactionid, gsCoupon);
        } catch (Exception e) {
            LOGGER.logAlert("Prmtn", "UePromotionResource.gsOneCoupon",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to evaluate the GS1 Coupon. \n" + e.getMessage());
            gsOneCouponResp =
                new GS1CouponResponse(ResultBase.RES_ERROR_GENERAL);
        }
        return gsOneCouponResp;
    }
}
