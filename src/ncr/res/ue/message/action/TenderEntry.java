package ncr.res.ue.message.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates tender entry messages.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderEntry")
public class TenderEntry extends OutgoingMessage {

    /**
     * Unique identifier for the TENDER_ENTRY within the transaction.
     */
    @XmlElement(name = "EntryID")
    private int entryID = -1;
    /**
     * the entry flag.
     */
    @XmlElement(name = "EntryFlag")
    private int entryFlag = -1;
    /**
     * TenderID for the type of Tender being used.
     */
    @XmlElement(name = "TenderID")
    private int tenderID = -1;
    /**
     * TenderSubID for the type of Tender being used.
     */
    @XmlElement(name = "TenderSubID")
    private int tenderSubID = -1;
    /**
     * Bin number of the tender.
     */
    @XmlElement(name = "BIN")
    private String bin = "";
    /**
     * Precision field for TenderAmount.
     */
    @XmlElement(name = "TenderPrecision")
    private int tenderPrecision = -1;
    /**
     * The monetary value tendered.
     */
    @XmlElement(name = "TenderAmount")
    private int tenderAmount = -1;

    /*
     * (non-Javadoc)
     *
     * @see ncr.res.ue.message.OutgoingMessage#createMessage(java.lang.String,
     * java.lang.String)
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%06d", entryID);
        messageBody += String.format("%1d", entryFlag);
        messageBody += String.format("%02d", tenderID);
        messageBody += String.format("%02d", tenderSubID);
        messageBody += String.format("%-8s", bin);
        messageBody += String.format("%1d", tenderPrecision);
        messageBody += String.format("%010d", tenderAmount);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;

        return fullMessage;
    }

    /**
     * constructor.
     * @param entryIDToSet - entryID
     * @param entryFlagToSet - entryFlag
     * @param tenderIDToSet - tenderID
     * @param tenderSubIDToSet - tenderSubID
     * @param binToSet - bin
     * @param tenderPrecisionToSet - tenderPrecision
     * @param tenderAmountToSet - tenderAmount
     */
    public TenderEntry(final int entryIDToSet,
            final int entryFlagToSet,
            final int tenderIDToSet,
            final int tenderSubIDToSet,
            final String binToSet,
            final int tenderPrecisionToSet,
            final int tenderAmountToSet) {
        super(MessageTypes.TENDER_ENTRY);

        this.entryID = entryIDToSet;
        this.entryFlag = entryFlagToSet;
        this.tenderID = tenderIDToSet;
        this.tenderSubID = tenderSubIDToSet;
        this.bin = binToSet;
        this.tenderPrecision = tenderPrecisionToSet;
        this.tenderAmount = tenderAmountToSet;

    }

    /**
     * Instantiates a new tender entry.
     */
    public TenderEntry() {
        super(MessageTypes.TENDER_ENTRY);
    }

    /**
     * Gets the entry id.
     *
     * @return the entry id
     */
    public final int getEntryID() {
        return entryID;
    }

    /**
     * Sets the entry id.
     *
     * @param newEntryID the new entry id
     */
    public final void setEntryID(final int newEntryID) {
        this.entryID = newEntryID;
    }

    /**
     * Gets the entry flag.
     *
     * @return the entry flag
     */
    public final int getEntryFlag() {
        return entryFlag;
    }

    /**
     * Sets the entry flag.
     *
     * @param newEntryFlag the new entry flag
     */
    public final void setEntryFlag(final int newEntryFlag) {
        this.entryFlag = newEntryFlag;
    }

    /**
     * Gets the tender id.
     *
     * @return the tender id
     */
    public final int getTenderID() {
        return tenderID;
    }

    /**
     * Sets the tender id.
     *
     * @param newTenderID the new tender id
     */
    public final void setTenderID(final int newTenderID) {
        this.tenderID = newTenderID;
    }

    /**
     * Gets the tender sub id.
     *
     * @return the tender sub id
     */
    public final int getTenderSubID() {
        return tenderSubID;
    }

    /**
     * Sets the tender sub id.
     *
     * @param newTenderSubID the new tender sub id
     */
    public final void setTenderSubID(final int newTenderSubID) {
        this.tenderSubID = newTenderSubID;
    }

    /**
     * Gets the bin.
     *
     * @return the bin
     */
    public final String getBin() {
        return bin;
    }

    /**
     * Sets the bin.
     *
     * @param newBin the new bin
     */
    public final void setBin(final String newBin) {
        this.bin = newBin;
    }

    /**
     * Gets the tender precision.
     *
     * @return the tender precision
     */
    public final int getTenderPrecision() {
        return tenderPrecision;
    }

    /**
     * Sets the tender precision.
     *
     * @param newTenderPrecision the new tender precision
     */
    public final void setTenderPrecision(final int newTenderPrecision) {
        this.tenderPrecision = newTenderPrecision;
    }

    /**
     * Gets the tender amount.
     *
     * @return the tender amount
     */
    public final int getTenderAmount() {
        return tenderAmount;
    }

    /**
     * Sets the tender amount.
     *
     * @param newTenderAmount the new tender amount
     */
    public final void setTenderAmount(final int newTenderAmount) {
        this.tenderAmount = newTenderAmount;
    }


}
