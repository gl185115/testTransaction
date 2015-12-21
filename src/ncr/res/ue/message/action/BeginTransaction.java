package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;
/**
 * BeginTransaction action message generator.
 * @author jg185106
 *
 */
public class BeginTransaction extends OutgoingMessage {
    /**
     * The TransactionMode for the transaction.
     */
    private int thisTransactionMode = -1;
    /**
     * The StartDate of transaction.
     */
    private String thisStartDate = "";
    /**
     * The StartTime of transaction.
     */
    private String thisStartTime = "";
    /**
     * The fuel flag for the transaction.
     * Fixed to 0 (for RES 3.1)
     */
    private int thisFuelFlag = 0;
    /**
     * The Operator of the transaction.
     */
    private String thisOperator = "";
    /**
     * The Operator Language Override.
     * Fixed to 0.
     */
    private int thisOperatorLanguageOverride = 0;
   /**
    * The main constructor for BeginTransaction.
    * @param transactionMode    the Transaction Mode for the transaction
    *                           Possible values:
    *                           0 = Normal
    *                           1 = Training
    *                           2 = NonSales
    *                           3 = Alt ID Processing
    *                           4 = Deferred Sales Transaction
    *                           5 = Deferred Sales Completion
    * @param fuelFlag   The fuel flag.
    * @param startDate  the start date of the transaction (MMDDYY)
    * @param startTime  the start time of the transaction (HHMMSS)
    * @param operator   the operator id
    */
    public BeginTransaction(
            final int transactionMode,
            final int fuelFlag,
            final String startDate,
            final String startTime,
            final String operator) {
        super(MessageTypes.BEGIN_TRANSACTION);
        this.thisTransactionMode = transactionMode;
        this.thisFuelFlag = fuelFlag;
        this.thisStartDate = startDate;
        this.thisStartTime = startTime;
        this.thisOperator = operator;
    }
    /*
     * (non-Javadoc)
     * @see ncr.res.ue.message.OutgoingMessage#
     * createMessage(java.lang.String, java.lang.String)
     */
    @Override
    public final String createMessage(
            final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += this.thisTransactionMode;
        messageBody += this.thisStartDate;
        messageBody += this.thisStartTime;
        messageBody += this.thisFuelFlag;
        messageBody += String.format("%8s",
                this.thisOperator).replace(" ", "0");
        messageBody += String.format("%02d",
                this.thisOperatorLanguageOverride);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;
        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;
        return fullMessage;
    }
}
