package ncr.res.mobilepos.pricing.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that encapsulates List of ReasonData.
 * @author rd185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OverrideReasonDataList")
public class OverrideReasonDataList {
	/**
	 * List of ReasonData.
	 */
	@XmlElement(name = "ReasonData")
	private List<ReasonData> reasonData;
    /**
     * List of ReasonData.
    */
    @XmlElement(name = "ReasonData2")
    private List<ReasonData> reasonData2;  
	/**
	 * Getter for list of ReasonData.
	 * @return	List of ReasonData
	 */
	public List<ReasonData> getReasonData() {
		return reasonData;
	}
	/**
	 * Setter for list of ReasonData.
	 * @param reasonDataToSet	List of ReasonData.
	 */
	public void setReasonData(List<ReasonData> reasonDataToSet) {
		this.reasonData = reasonDataToSet;
	}
    /**
     * Getter for list of ReasonData.
     * @return   ReasonData
     */
    public final List<ReasonData> getReasonData2() {
        return reasonData2;
    }
    /**
     * Setter for list of Reason.
     * @param reasonDataToSet   ReasonData to set.
     */
    public final void setReasonData2(
                   final List<ReasonData> reasonDataToSet) {
        this.reasonData2 = reasonDataToSet;
    }  
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; ");
        sb.append("ReasonData: " + reasonData).append("; ");
        sb.append("ReasonData2: " + reasonData2).append("; ");
		return sb.toString();
	}
}
