package ncr.res.ue.core;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.action.Adjustment;
import ncr.res.ue.message.action.BeginTransaction;
import ncr.res.ue.message.action.CancelOperation;
import ncr.res.ue.message.action.EndTransaction;
import ncr.res.ue.message.action.GSOneCoupon;
import ncr.res.ue.message.action.ItemEntry;
import ncr.res.ue.message.action.ItemPoints;
import ncr.res.ue.message.action.ItemQuantity;
import ncr.res.ue.message.action.ItemSV;
import ncr.res.ue.message.action.MemberId;
import ncr.res.ue.message.action.Resume;
import ncr.res.ue.message.action.SuspendTransaction;
import ncr.res.ue.message.action.TenderEntry;
import ncr.res.ue.message.action.Total;
import ncr.res.ue.message.action.TriggerCode;
import ncr.res.ue.message.response.AdjustmentResponse;
import ncr.res.ue.message.response.BeginTransactionResponse;
import ncr.res.ue.message.response.CancelOperationResponse;
import ncr.res.ue.message.response.EndTransactionResponse;
import ncr.res.ue.message.response.GS1CouponResponse;
import ncr.res.ue.message.response.ItemPointsResponse;
import ncr.res.ue.message.response.ItemSVResponse;
import ncr.res.ue.message.response.MemberIdResponse;
import ncr.res.ue.message.response.ResumeResponse;
import ncr.res.ue.message.response.SuspendTransactionResponse;
import ncr.res.ue.message.response.TotalResponse;
import ncr.res.ue.message.response.UEResponseBase;

/**
 * Transaction
 * UE Integration core class to handle Transaction
 * level operations.
 * @author jg185106
 *
 */
public class Transaction {
    /**
     * Instance of debug trace printer.
     */
    private static Trace.Printer tp;
    /**
     * Connection instance.
     */
    private Connection thisConnection;

    /**
     * Main constructor.
     * @param connection the connection instance to use.
     */
    public Transaction(final Connection connection) {
        if (tp == null) {
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        }
        this.thisConnection = connection;
    }
    /**
     * Send BeginTransaction action.
     * @param transactionMode the transaction mode
     * @param fuelFlag the Fule Flag.
     * @param operator the operator
     * @param termId the terminal ID
     * @param transactionId the transacton id
     * @return BeginTransaction
     * @throws MessageException thrown when Message has wrong data
     */
    public final BeginTransactionResponse beginTransaction(
            final int transactionMode,
            final int fuelFlag,
            final String operator,
            final String termId,
            final String transactionId) throws MessageException {
        tp.methodEnter("beginTransaction");
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String startDate = dateFormat.format(date);
        String startTime = timeFormat.format(date);
        BeginTransaction beginTx = new BeginTransaction(
                transactionMode, 0, startDate, startTime, operator);
        String message =
            beginTx.createMessage(termId, transactionId);        
        Communicator communicator = thisConnection.getCommunicator();
        tp.println("startDate", startDate)
        .println("startTime", startTime)
        .println("message", message);

        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);

        BeginTransactionResponse response =
            (BeginTransactionResponse) responses.get(responses.size() - 1);
        tp.methodExit();
        return response;
    }

    /**
     * Send item entry.
     *
     * @param terminalID the terminal id
     * @param transactionID the transaction id
     * @param itemEntryID the Item Entry ID.
     * @param entryFlag the entry flag
     * @param returnFlag the return flag
     * @param itemCode the item code
     * @param familyCode the family code
     * @param department the department
     * @param clearanceLevel the clearance level
     * @param pricePrecision the price precision
     * @param unitPrice the unit price
     * @param quantityType the quantity type
     * @param quantitySubType the quantity sub type
     * @param quantityPrecision the quantity precision
     * @param quantity the quantity
     * @param discountFlag the discount flag
     * @param tlqFlag the TLQ flag
     * @param alternatePrice the alternate price
     * @return the list
     * @throws MessageException the message exception
     */
    public final List<UEResponseBase> sendItemEntry(final String terminalID,
            final String transactionID, final int itemEntryID,
            final int entryFlag, final int returnFlag, final String itemCode,
            final int familyCode, final int department,
            final int clearanceLevel, final int pricePrecision,
            final int unitPrice, final int quantityType,
            final int quantitySubType, final int quantityPrecision,
            final int quantity, final int discountFlag, final int tlqFlag,
            final int alternatePrice) throws MessageException {
        tp.methodEnter("sendItemEntry");
        ItemEntry itemEntry = new ItemEntry(itemEntryID, entryFlag, returnFlag,
                itemCode, familyCode, alternatePrice, department,
                clearanceLevel, pricePrecision, unitPrice, quantityType,
                quantitySubType, quantityPrecision, quantity, discountFlag,
                tlqFlag);
        String message = itemEntry.createMessage(terminalID, transactionID);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses = communicator
                .sendReceiveMessage(message);
        tp.methodExit();
        return responses;
    }

    /**
     * Send EndTransaction action.
     * @param status Status of the transaction
     * @param termId The Terminal ID
     * @param transactionId The Transaction ID
     * @return EndTransaction
     * @throws MessageException thrown when message has wrong data
     */
    public final EndTransactionResponse endTransaction(final int status,
            final String termId, final String transactionId)
                                throws MessageException {
        tp.methodEnter("endTransaction");
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String endTxDate = dateFormat.format(date);
        String endTxTime = timeFormat.format(date);

        EndTransaction endTx = new EndTransaction(status, endTxDate, endTxTime);
        String message = endTx.createMessage(termId, transactionId);
        tp.println("message", message);        
        Communicator communicator = thisConnection.getCommunicator();
        tp.methodExit();
        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
        return (EndTransactionResponse) responses.get(responses.size() - 1);

    }

    /**
     *  Send Total action and receive TotalResponse.
     *  @param totalPrecision   Precision for the total
     *  @param total            The total
     *  @param userTotal        User Total
     *  @param termId           The terminal ID
     *  @param transactionId    The transaction ID
     *  @return TotalResponse
     *  @throws MessageException thrown when message has wrong data
     */
    public final TotalResponse totalTx(final int totalPrecision,
            final int total, final int userTotal, final String termId,
            final String transactionId)throws MessageException {
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date();

        String totalDate = dateFormat.format(date);
        String totalTime = timeFormat.format(date);

        Total totalTx = new Total(totalPrecision, total, userTotal, totalDate,
                totalTime);
        String message = totalTx.createMessage(termId, transactionId);        
        Communicator communicator = thisConnection.getCommunicator();

        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
         return (TotalResponse) responses.get(responses.size() - 1);
    }

    /**
     * Tender Entry Action and Receive response.
     * @param terminalID    The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param entryID The Entry ID.
     * @param entryFlag The Entry Flag.
     * @param tenderID  The TenderID.
     * @param tenderSubID   The Tender Sub ID.
     * @param bin   The Bin.
     * @param tenderPrecision   The Tender Precision.
     * @param tenderAmt The Tender Amount.
     * @return  The Send Tender Entry.
     * @throws MessageException The exception thrown.
     */
    public final List<UEResponseBase> sendTenderEntry(final String terminalID,
            final String transactionID, final int entryID,
            final int entryFlag, final int tenderID, final int tenderSubID,
            final String bin, final int tenderPrecision,
            final int tenderAmt) throws MessageException {
        tp.methodEnter("sendTenderEntry");
        TenderEntry itemEntry = new TenderEntry(entryID, entryFlag, tenderID,
                tenderSubID, bin, tenderPrecision, tenderAmt);
        String message = itemEntry.createMessage(terminalID, transactionID);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses = communicator
                .sendReceiveMessage(message);
        tp.methodExit();
        return responses;
    }

    /**
     * Send TriggerCode Action and receive response.
     * @param termID The Terminal ID.
     * @param transactionId The Transaction ID.
     * @param entryID The Entry ID.
     * @param entryFlag The Entry Flag.
     * @param triggerCode The Trigger Code.
     * @return The Trigger Code Response.
     * @throws MessageException The Exception thrown when error occur.
     */
    public final List<UEResponseBase> triggerCode(final String termID,
            final String transactionId, final int entryID,
            final int entryFlag, final int triggerCode)
    throws MessageException {
        tp.methodEnter("triggerCode");

        TriggerCode triggerCodeAction =
             new TriggerCode(entryID, entryFlag, triggerCode);

         String message =
             triggerCodeAction.createMessage(termID, transactionId);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return responses;
    }

    /**
     * Send ItemQuantity Action and receiver response.
     * @param termID The Terminal ID.
     * @param transactionID The transaction ID.
     * @param entryFlag The Entry Flag.
     * @param entryID   The Entry ID.
     * @param rewardCheckSum The Reward Check Sum.
     * @param quantity The Quantity.
     * @exception MessageException The Exception thrown when an error occur.
     * @return The list of UE Response Base.
     */
    public final List<UEResponseBase> itemQuantity(final String termID,
            final String transactionID, final int entryFlag,
            final int entryID, final int rewardCheckSum,
            final int quantity)
    throws MessageException {
        tp.methodEnter("itemQuantity");

        ItemQuantity itemQuantityAction =
            new ItemQuantity(entryFlag, entryID,
                    rewardCheckSum, quantity);
         String message =
             itemQuantityAction.createMessage(termID, transactionID);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return responses;
    }

    /**
     * Send Item Store Action and Receiver response.
     * @param termID    The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param itemEntryID The Item Entry ID.
     * @param itemQuantity  The Item Quantity.
     * @param programID The Program ID.
     * @param quantity  The Quantity.
     * @param expiration    The Expiration.
     * @return  The Item Store Value Response.
     * @throws MessageException The Exception thrown when error occur.
     */
    public final ItemSVResponse itemStoreValue(final String termID,
            final String transactionID,
            final int itemEntryID,
            final int itemQuantity, final int programID,
            final int quantity, final String expiration)
    throws MessageException {
        tp.methodEnter("itemStoreValue");
        ItemSV itemSV =
            new ItemSV(itemEntryID,
                    itemQuantity, programID,
                    quantity, expiration);
         String message =
             itemSV.createMessage(termID, transactionID);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return (ItemSVResponse) responses.get(responses.size() - 1);
    }

    /**
     * The Cancel Operation.
     * @param termID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param reasonFlag The Reason Flag.
     * @return The {@link CancelOperationResponse}
     * @throws MessageException The exception thrown.
     */
    public final CancelOperationResponse cancelOperation(
            final String termID,
            final String transactionID,
            final int reasonFlag)
    throws MessageException {
        tp.methodEnter("cancelOperation");
        CancelOperation cancelOp =
            new CancelOperation(reasonFlag);
         String message =
             cancelOp.createMessage(termID, transactionID);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return (CancelOperationResponse) responses.get(responses.size() - 1);
    }

    /**
     * MemberID Action and Receiver Response.
     * @param termID    The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param memberID  The Member ID.
     * @param entryFlag The Entry Flag.
     * @param type  The Type.
     * @return  The {@link MemberIdResponse}
     * @throws MessageException The exception thrown when the method fail.
     */
    public final MemberIdResponse memberID(
            final String termID,
            final String transactionID,
            final BigInteger memberID,
            final int entryFlag,
            final int type)
    throws MessageException {
        tp.methodEnter("memberID");
        MemberId memberidAction =
            new MemberId(memberID, entryFlag, type);
         String message =
             memberidAction.createMessage(termID, transactionID);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return (MemberIdResponse) responses.get(responses.size() - 1);
    }
    /**
     * Send Resume action and receive ResumeResponse.
     * @param resumeFlag    Transaction resume flag.
     * @param resumeId      Transaction resume ID.
     * @param termId        Terminal ID.
     * @param transactionId Transaction ID.
     * @return ResumeResponse
     * @throws MessageException MessageException thrown when message
     * has wrong data
     */
    public final ResumeResponse resumeTransaction(final int resumeFlag,
            final String resumeId, final String termId,
            final String transactionId) throws MessageException {
        tp.methodEnter("ResumeTransaction");
        Resume resumeTrans = new Resume(resumeFlag, resumeId);
        String message = resumeTrans.createMessage(termId, transactionId);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
        tp.methodExit();
        return (ResumeResponse) responses.get(responses.size() - 1);
    }
    /**
     * Send Suspend Transaction action and receive Suspend Transaction Response.
     * @param suspendFlag   The suspend flag.
     * @param resumeId      The resume identifier.
     * @param termId        Terminal ID.
     * @param transactionId Transaction ID.
     * @return SuspendTransactionResponse
     * @throws MessageException Exception thrown when message has wrong data.
     */
    public final SuspendTransactionResponse suspendTransaction(
            final int suspendFlag, final int resumeId, final String termId,
            final String transactionId) throws MessageException {

        tp.methodEnter("SuspendTransaction");
        SuspendTransaction suspendTxn =
            new SuspendTransaction(suspendFlag, resumeId);
        String message = suspendTxn.createMessage(termId, transactionId);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
        tp.methodExit();
        return (SuspendTransactionResponse) responses.get(responses.size() - 1);
    }

    /**
     * The Adjustment Action anf Receive response.
     * @param termID The Terminal ID.
     * @param transactionID The Transaction ID.
     * @param adjustment The Adjustment.
     * @return The Adjustment Response.
     * @throws MessageException The Exception thrown when error occur.
     */
    public final AdjustmentResponse adjustment(
            final String termID,
            final String transactionID,
            final Adjustment adjustment) throws MessageException {
        tp.methodEnter("adjustment");
         String message =
             adjustment.createMessage(termID, transactionID);
         tp.println("message", message);
         Communicator communicator = thisConnection.getCommunicator();
         List<UEResponseBase> responses = communicator
             .sendReceiveMessage(message);
         tp.methodExit();
         return (AdjustmentResponse) responses.get(responses.size() - 1);
    }
    /**
     * Send GSOneCoupon action and receive GS1_CouponResponse.
     * @param termId    Terminal ID.
     * @param transactionId Transaction ID.
     * @param gsOneCoupon   The GSOneCoupon.
     * @return  GS1_CouponResponse
     * @throws MessageException Exception thrown when message has wrong data.
     */
    public final GS1CouponResponse gsOneCoupon(final String termId,
            final String transactionId, final GSOneCoupon gsOneCoupon)
        throws MessageException {

        tp.methodEnter("GS1_Coupon");
        String message = gsOneCoupon.createMessage(termId, transactionId);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
        tp.methodExit();
        return (GS1CouponResponse) responses.get(responses.size() - 1);
    }
    /**
     * Send ItemPoints action and receive ItemPointsResponse.
     * @param termId    Terminal ID.
     * @param transactionId     Transaction ID.
     * @param itemEntryId       Item Entry ID.
     * @param itemQuantity      Item Quantity.
     * @param programId     Program ID.
     * @param quantity      Quantity.
     * @return  ItemPointsResponse
     * @throws MessageException Exception thrown when message has wrong data.
     */
    public final ItemPointsResponse itemPoints(final String termId,
            final String transactionId, final int itemEntryId,
            final int itemQuantity, final int programId, final int quantity)
        throws MessageException {

        tp.methodEnter("ItemPoints");
        ItemPoints itemPoints =
            new ItemPoints(itemEntryId, itemQuantity, programId, quantity);
        String message = itemPoints.createMessage(termId, transactionId);
        tp.println("message", message);
        Communicator communicator = thisConnection.getCommunicator();
        List<UEResponseBase> responses =
            communicator.sendReceiveMessage(message);
        tp.methodExit();
        return (ItemPointsResponse) responses.get(responses.size() - 1);
    }
}
