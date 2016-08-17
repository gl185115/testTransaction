package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DepositsReceivedTotal")
public class DepositsReceivedTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "ReservationDownPayment")
	private ReservationDownPayment reservationDownPayment;
		
	public final ReservationDownPayment getReservationDownPayment() {
		return this.reservationDownPayment;
	}
	public final void setResevationDownPayment(ReservationDownPayment rdp) {
		this.reservationDownPayment = rdp;
	}
	
	public final Amount getAmount() {
		return this.amount;
	}
	public final void setAmount(Amount amt) {
		this.amount = amt;
	}
	
	public final String getCount() {
		return this.count;
	}
	public final void setCount(String cnt) {
		this.count = cnt;
	}
}