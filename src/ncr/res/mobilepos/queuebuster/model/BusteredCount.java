package ncr.res.mobilepos.queuebuster.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BusteredCount")
public class BusteredCount extends ResultBase {
	
	 @XmlElement(name = "Count")
	    private String count;

	/**
	 * @return the count
	 */
	public final String getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public final void setCount(String count) {
		this.count = count;
	}
	 
}
