package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PremiumTicketIssue")
public class PremiumTicketIssue {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "BSSHeadKa")
	private String bSSHeadKa;
	
	public final String getBSSHeadKa() {
		return this.bSSHeadKa;
	}
	public final void setBSSHeadKa(String val) {
		this.bSSHeadKa = val;
	}
	
	public final String getCount() {
		return this.count;
	}
	public final void setCount(String cnt) {
		this.count = cnt;
	}
	
}