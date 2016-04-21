/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * LineItem
 *
 * Model Class for LineItem
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.constant.TransactionVariable;

/**
 * LineItem Model Object.
 *
 * <P>A LineItem Node in POSLog XML.
 *
 * <P>The LineItem node is under CustomerOrderTransaction Node.
 * And mainly holds the details of the Customer's transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(

		)
public class LineItem {
    /**
     * The private member variable that will hold the Sequence number.
     */
    @XmlElement(name = "SequenceNumber")
    private int sequenceNo;

    /**
     * The private member variable that will hold the PluChange.
     */
    @XmlElement(name = "PluChange")
    private PluChange pluChange;
    
    /**
     * The private member variable that will hold the Tender.
     */
    @XmlElement(name = "Tender")
    private Tender tender;
    /**
     * The private member variable that will hold the Sale.
     */
    @XmlElement(name = "Sale")
    private Sale sale;
    
    /**
     * The private member variable that will hold the LayawaySale.
     */
    @XmlElement(name = "LayawaySale")
    private LayawaySale layawaySale;

    /**
     * The private member variable that will hold the Discount.
     */
    @XmlElement(name = "Discount")
    private Discount discount;

    /**
     * The private member variable that will hold the Tax.
     */
    @XmlElement(name = "Tax")
    private List<Tax> tax;

    /**
     * The private member variable that will hold the Return.
     */
    @XmlElement(name = "Return")
    private Return retrn;

    /**
     * The private member variable that will hold the LayawayReturn.
     */
    @XmlElement(name = "LayawayReturn")
    private LayawayReturn layawayReturn;

    /**
     * The private member variable that will hold the member points info.
     */
    @XmlElement(name = "MemberInfo")
    private MemberInfo points;

    /**
     * The private member variable that will hold the payment on account info.
     * currently used for donations
     */
    @XmlElement(name = "PaymentOnAccount")
    private PaymentOnAccount paymentOnAccount;
    /**
     * The private member variable that will hold the layaway
     */
    @XmlElement(name = "Layaway")
    private Layaway layaway;

    /**
     * previousLayaway
     */
    @XmlElement(name = "PreviousLayaway")
    private PreviousLayaway previousLayaway;

    /**
     * RainCheck
     */
    @XmlElement(name = "RainCheck")
    private RainCheck rainCheck;

    /**
     * barPoints
     */
    @XmlElement(name = "barPoints")
    private BarPoints barPoints;
    
    /**
     * addpointslater
     */
    @XmlElement(name = "PostPoint")
    private PostPoint postPoint;
    /**
     * updateinfo for member card transaction
     */
    @XmlElement(name = "UpdateInfo")
    private UpdateInfo updateInfo;
    
    @XmlElement(name = "PointTicketIssue")
    private PointTicketIssue pointTicket;
    
	/**
     * cashIn for member card transaction
     */
    @XmlElement(name = "CashIn")
    private CashIn cashIn;

    /**
     * cashOut for member card transaction
     */
    @XmlElement(name = "CashOut")
    private CashOut cashOut;

    /**
     * Gets the cashIn under LineItem.
     *
     * @return        The cashIn under LineItem.
     */
    public final CashIn getCashIn() {
		return cashIn;
	}
    
    /**
     * Sets the cashIn under LineItem.
     *
     * @param cashInToSet       The new value for the cashIn under LineItem.
     */
	public final void setCashIn(final CashIn cashInToSet) {
		this.cashIn = cashInToSet;
	}
	
    /**
     * Gets the cashOut under LineItem.
     *
     * @return        The cashOut under LineItem.
     */
	public final CashOut getCashOut() {
		return cashOut;
	}
	
    /**
     * Sets the cashOut under LineItem.
     *
     * @param cashOutToSet       The new value for the cashOut under LineItem.
     */
	public final void setCashOut(final CashOut cashOutToSet) {
		this.cashOut = cashOutToSet;
	}
    /**
     * Gets the pluChange under LineItem.
     *
     * @return        The pluChange under LineItem.
     */
    public final PluChange getPluChange() {
        return pluChange;
    }
    /**
     * Sets the pluChange under LineItem.
     *
     * @param pluChange        The new value for the pluChange under LineItem.
     */
    public final void setPluChange(final PluChange pluChange) {
        this.pluChange = pluChange;
    }

    /**
     * Gets the Return under LineItem.
     *
     * @return        The Return under LineItem.
     */
    public final Return getRetrn() {
        return retrn;
    }
    /**
     * Sets the Return under LineItem.
     *
     * @param retrnToSet       The new value for the Return under LineItem.
     */
    public final void setRetrn(final Return retrnToSet) {
        this.retrn = retrnToSet;
    }
    /**
     * Gets the LayawayReturn under LineItem.
     *
     * @return        The LayawayReturn under LineItem.
     */
    public LayawayReturn getLayawayReturn() {
        return layawayReturn;
    }
    /**
     * Sets the LayawayReturn under LineItem.
     *
     * @param layawayReturn       The new value for the LayawayReturn under LineItem.
     */
    public void setLayawayReturn(final LayawayReturn layawayReturn) {
        this.layawayReturn = layawayReturn;
    }
    /**
     * Gets the Discount under LineItem.
     *
     * @return        The Discount under LineItem.
     */
    public final Discount getDiscount() {
        return discount;
    }
    /**
     * Sets the Discount under LineItem.
     *
     * @param discountToSet      The new value for the Discount under LineItem.
     */
    public final void setDiscount(final Discount discountToSet) {
        this.discount = discountToSet;
    }
    /**
     * Gets the Tender under LineItem.
     *
     * @return        The Tender under LineItem.
     */
    public final Tender getTender() {
        return tender;
    }
    /**
     * Sets the Tender under LineItem.
     *
     * @param tenderToSet        The new value for the Tender under LineItem.
     */
    public final void setTender(final Tender tenderToSet) {
        this.tender = tenderToSet;
    }
    /**
     * Gets the Sale under LineItem.
     *
     * @return        The Sale under LineItem.
     */
    public final Sale getSale() {
        return sale;
    }
    /**
     * Sets the LayawaySale under LineItem.
     *
     * @param layawaySale        The new value for the LayawaySale under LineItem.
     */
    public final void setLayawaySale(final LayawaySale layawaySale) {
        this.layawaySale = layawaySale;
    }
    /**
     * Gets the LayawaySale under LineItem.
     *
     * @return        The LayawaySale under LineItem.
     */
    public final LayawaySale getLayawaySale() {
        return layawaySale;
    }
    /**
     * Sets the Sale under LineItem.
     *
     * @param sales        The new value for the Sale under LineItem.
     */
    public final void setSale(final Sale sales) {
        this.sale = sales;
    }
    /**
     * Gets the Sequence number under LineItem.
     *
     * @return        The Sequence number under LineItem.
     */
    public final int getSequenceNo() {
        return sequenceNo;
    }
    /**
     * Sets the Sequence number under LineItem.
     *
     * @param sequenceNoToSet        The new value for the Sequence number
     *                              under LineItem.
     */
    public final void setSequenceNo(final int sequenceNoToSet) {
        this.sequenceNo = sequenceNoToSet;
    }
    /**
     * Gets the Points number under LineItem.
     *
     * @return        The Points number under LineItem.
     */
    public final MemberInfo getPoints() {
        return points;
    }
    /**
     * Sets the Points number under LineItem.
     *
     * @param  pointsToSet        The new value for the Sequence number
     *                              under LineItem.
     */
    public final void setPoints(final MemberInfo pointsToSet) {
        this.points = pointsToSet;
    }
    /**
     * Gets the Account code.
     *
     * @return        The account code containing Sale,Discount,and Tender
     */
    public final String getAccountCode() {

        if (sale != null) {
            return TransactionVariable.ITEM_CODE;
        }

        if (discount != null) {
            return TransactionVariable.DISCOUNT_CODE;
        }

        if (null == tender) {
            return null;
        }
        String tenderType = tender.getTenderType();
        if (tenderType.equals(TransactionVariable.CASH))  {
            return TransactionVariable.CASH_CODE;
        }

        if (tenderType.equals(TransactionVariable.CREDITDEBIT)) {
            return TransactionVariable.CREDITDEBIT_CODE;
        }

        if (tenderType.equals(TransactionVariable.VOUCHER)) {
            return TransactionVariable.VOUCHER_CODE;
        }
        return null;
    }
    /**
     * Sets the Tax under LineItem.
     *
     * @param taxToSet        The new value for the Tax under LineItem.
     */
    public final void setTax(final List<Tax> taxToSet) {
        this.tax = taxToSet;
    }
    /**
     * Gets the Tax under LineItem.
     *
     * @return        The Tax under LineItem.
     */
    public final List<Tax> getTax() {
        return tax;
    }

    /**
     * Gets the Payment on Account info
     *
     * @return        The Payment on Account info.
     */
    public final PaymentOnAccount getPaymentOnAccount() {
        return paymentOnAccount;
    }

    /**
     * Sets the Payment on Account info
     *
     * @param paymentOnAccount        The new value for the Payment on Account info
     */
    public final void setPaymentOnAccount(PaymentOnAccount paymentOnAccount) {
        this.paymentOnAccount = paymentOnAccount;
    }


    /**
     * @return layaway
     */
    public final Layaway getLayaway() {
        return layaway;
    }
    /**
     * @param layaway セットする layaway
     */
    public final void setLayaway(Layaway layaway) {
        this.layaway = layaway;
    }
    /**
     * @return previousLayaway
     */
    public final PreviousLayaway getPreviousLayaway() {
        return previousLayaway;
    }
    /**
     * @param previousLayaway セットする previousLayaway
     */
    public final void setPreviousLayaway(PreviousLayaway previousLayaway) {
        this.previousLayaway = previousLayaway;
    }
    /**
     * @return rainCheck
     */
    public final RainCheck getRainCheck() {
        return rainCheck;
    }
    /**
     * @param rainCheck セットする rainCheck
     */
    public final void setRainCheck(RainCheck rainCheck) {
        this.rainCheck = rainCheck;
    }
    /**
     * @return barPoints
     */
    public final BarPoints getBarPoints() {
        return barPoints;
    }
    /**
     * @param barPoints セットする barPoints
     */
    public final void setBarPoints(BarPoints barPoints) {
        this.barPoints = barPoints;
    }
    /**
     * 
     * @return
     */
    public PostPoint getPostPoint() {
        return postPoint;
    }
    /**
     * 
     * @param postPoint
     */
    public void setPostPoint(PostPoint postPoint) {
        this.postPoint = postPoint;
    }
    /**
     * 
     * @return
     */
    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }
    /**
     * 
     * @param updateInfo
     */
    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }
    
    /**
     * @return the pointTicket
     */
    public PointTicketIssue getPointTicket() {
        return pointTicket;
    }
    /**
     * @param pointTicket the pointTicket to set
     */
    public void setPointTicket(PointTicketIssue pointTicket) {
        this.pointTicket = pointTicket;
    }
    /**
     * Override toString() Method.
     * @return The String Representation of LineItem.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("SequenceNumber : ").append(this.sequenceNo);

        if (null != this.discount) {
            str.append(crlf).append("Discount : ")
               .append(this.discount.toString());
        }

        if (null != this.sale) {
            str.append(crlf).append("Sale : ").append(this.sale.toString());
        }

        if (null != this.layawaySale) {
            str.append(crlf).append("LayawaySale : ").append(this.layawaySale.toString());
        }

        if (null != this.retrn) {
            str.append(crlf).append("Return : ").append(this.retrn.toString());
        }

        if (null != this.layawayReturn) {
            str.append(crlf).append("LayawayReturn : ").append(this.layawayReturn.toString());
        }

        if (null != this.tender) {
            str.append(crlf).append("Tender : ").append(this.tender.toString());
        }

        if (null != this.tax) {
            str.append(crlf).append("Tax : ").append(this.tax.toString());
        }

        if (null != this.paymentOnAccount) {
            str.append(crlf).append("PaymentOnAccount : ").append(this.paymentOnAccount.toString());
        }

        return str.toString();
    }
}
