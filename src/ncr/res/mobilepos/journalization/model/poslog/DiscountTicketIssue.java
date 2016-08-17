package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DiscountTicketIssue")
public class DiscountTicketIssue {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "TEST")
	private String tEST;
	
	public final String getCount() {
		return this.count;
	}
	
	public final void setCount(String cnt) {
		this.count = cnt;
	}
	
	public final String getTEST() {
		return this.tEST;
	}
	
	public void setTEST(String val) {
		this.tEST = val;
	}
}