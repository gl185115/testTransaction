package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Model for representing POS Link Information.
 * @author RD185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLinkInfo")
public class POSLinkInfo {
    /**
     * The POS Link ID.
     */
    @XmlElement(name = "LinkID")
    private String poslinkid;
    /**
     * The POS Link Name.
     */
    @XmlElement(name = "LinkName")
    private String linkname;
    /**
     * The retail store id where the POS Link belongs.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailstoreid;
    
    private String status;
    
   	private String updAppId;
    
    private String updOpeCode;
    
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    public String getUpdAppId() {
        return updAppId;
    }
    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }
    public String getUpdOpeCode() {
        return updOpeCode;
    }
    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }

    /**
     * Setter for Retail Store ID.
     * @param   retailstoreidToSet   The ID of the Store to set.
     */
    public final void setRetailStoreId(final String retailstoreidToSet) {
        this.retailstoreid = retailstoreidToSet;
    }
    /**
     * Getter for the Retail Store ID.
     * @return  The Retail Store ID.
     */
    public final String getRetailStoreId() {
        return retailstoreid;
    }
    /**
     * Setter for the POS Link Name.
     * @param linknameToSet  The Name of QueueBuster Link to set.
     */
    public final void setLinkName(final String linknameToSet) {
        this.linkname = linknameToSet;
    }
    /**
     * Getter for the POS Link Name.
     * @return  The POS Link Name.
     */
    public final String getLinkName() {
        return linkname;
    }
    /**
     * Setter for the POS Link ID.
     * @param poslinkidToSet The POS Link ID to set.
     */
    public final void setPosLinkId(final String poslinkidToSet) {
        this.poslinkid = poslinkidToSet;
    }
    /**
     * Getter for the POS Link ID.
     * @return  The POS Link ID.
     */
    public final String getPosLinkId() {
        return poslinkid;
    }
    @Override
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
