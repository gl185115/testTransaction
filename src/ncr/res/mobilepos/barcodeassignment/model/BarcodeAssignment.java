package ncr.res.mobilepos.barcodeassignment.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barcode")
@ApiModel(value="barcode")
public class BarcodeAssignment extends ResultBase{
	
	@XmlElement(name = "sale")
	private Sale sale;
	
	@XmlElement(name = "payment")
	private Payment payment;
	
	@XmlElement(name = "signon")
	private Signon signon;
	
	@XmlElement(name = "member")
	private Member member;
	
	@XmlElement(name = "transaction")
	private Transaction transaction;
	
	@XmlElement(name = "tradeSequenceReceipt")
	private TradeSequenceReceipt tradeSequenceReceipt;

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Signon getSignon() {
		return signon;
	}

	public void setSignon(Signon signon) {
		this.signon = signon;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TradeSequenceReceipt getTradeSequenceReceipt() {
		return tradeSequenceReceipt;
	}

	public void setTradeSequenceReceipt(TradeSequenceReceipt tradeSequenceReceipt) {
		this.tradeSequenceReceipt = tradeSequenceReceipt;
	}
	
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
