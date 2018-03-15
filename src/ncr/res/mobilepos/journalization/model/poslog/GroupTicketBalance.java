package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GroupTicketBalance")
public class GroupTicketBalance {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "POSRegistrationCount")
	private String pOSRegistrationCount;
	
	@XmlElement(name = "ActualRemainingCount")
	private String actualRemainingCount;
	
	public final String getCount() {
		return this.count;
	}
	
	public final void setCount(String cnt) {
		this.count = cnt;
	}
	
	public final String getPOSRegistrationCount() {
		return this.pOSRegistrationCount;
	}
	
	public final void setPOSRegistrationCount(String val) {
		this.pOSRegistrationCount = val;
	}
	
	public final String getActualRemainingcount() {
		return this.actualRemainingCount;
	}
	
	public final void setActualRemainingCount(String val) {
		this.actualRemainingCount = val;
	}
	
}