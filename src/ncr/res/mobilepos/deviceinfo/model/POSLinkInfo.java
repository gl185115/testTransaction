package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Model for representing POS Link Information.
 * @author RD185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLinkInfo")
@ApiModel(value="POSLinkInfo")
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
    
    @ApiModelProperty(value="�X�e�[�^�X", notes="�X�e�[�^�X")
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
	@ApiModelProperty(value="�ŏI�X�V�v���O����ID", notes="�ŏI�X�V�v���O����ID")
    public String getUpdAppId() {
        return updAppId;
    }
    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }
    
    @ApiModelProperty(value="�ŏI�X�V���[�U�[ID", notes="�ŏI�X�V���[�U�[ID")
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
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
    
    @ApiModelProperty(value="�����N��", notes="�����N��")
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
    
    @ApiModelProperty(value="�����N�R�[�h", notes="�����N�R�[�h")
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
