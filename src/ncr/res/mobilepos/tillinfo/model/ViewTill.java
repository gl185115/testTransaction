package ncr.res.mobilepos.tillinfo.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Store JSON data for view store.
 */
@XmlRootElement(name = "ViewTill")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewTill extends ResultBase {
    /**
     * Store instance. Holds store data.
     */
    @XmlElement(name = "Till")
    private Till till;
    
    @XmlElement(name = "TillList")
    private List<Till> tillList;
    
    public ViewTill() {
    	tillList = new ArrayList<Till>();
    }
    
	public final Till getTill() {
		return till;
	}

	public final void setTill(Till till) {
		this.till = till;
	}
	
	public final void setTillList(final List<Till> tillList) {
		this.tillList = tillList; 
	}
	
	public final List<Till> getTillList() {
		return tillList;
	}
	
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Till: ").append(till).append("; ");
        return sb.toString();
    }
}
