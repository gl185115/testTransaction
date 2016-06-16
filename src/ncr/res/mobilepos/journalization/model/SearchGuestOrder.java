package ncr.res.mobilepos.journalization.model;

/**
 * ���藚��
 * �o�[�W����      ������t        �S���Җ�      ������e
 * 1.01     2014.11.19  FENGSHA    �O������擾(�V�K) 
 * 1.02     2014.12.15  MAJINHUI   �O����ԍ��擾 
 */
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * @author 
 * 
 * Sales SearchGuestOrder Class is a Model representation of the guest order List.
 */
@XmlRootElement(name = "SearchGuestOrder")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="SearchGuestOrder")
@XmlSeeAlso({ SearchGuestOrderInfo.class })
public class SearchGuestOrder extends ResultBase {
    
	/*
	 * This list is used to save guest order information
	 * 
	 */
    @XmlElementWrapper(name = "SearchGuestOrder")
    @XmlElementRef()
    private List<SearchGuestOrderInfo> searchGuestOrderInfo = null;
    
    //1.02     2014.12.15  MAJINHUI   �O����ԍ��擾  ADD START
    /**
     * the Guest NO
     */
    @XmlElement(name = "GuestNO")
    private String guestNo;

    /**
     * @return GuestNO
     */
    @ApiModelProperty(value="���q�l�ԍ�", notes="���q�l�ԍ�")
    public String getGuestNo() {
        return guestNo;
    }

    /**
     * @param guestNo
     */
    public void setGuestNo(String guestNo) {
        this.guestNo = guestNo;
    }
    //1.02     2014.12.15  MAJINHUI   �O����ԍ��擾  ADD END

    /**
	 * Gets the guest order list.
	 *
	 * @return the guest order list
	 */
    @ApiModelProperty(value="�����̃Q�X�g�̂��߂̏����擾����", notes="�����̃Q�X�g�̂��߂̏����擾����")
    public List<SearchGuestOrderInfo> getSearchGuestOrderInfo() {
        return searchGuestOrderInfo;
    }
    
    /**
	 * Sets the guest order information of the list.
	 *
	 * @param guest order information ToSet
	 *            the new guest order
	 */
    public void setSearchGuestOrderInfo(
            List<SearchGuestOrderInfo> searchGuestOrderInfo) {
        this.searchGuestOrderInfo = searchGuestOrderInfo;
    }

    @Override
    public final String toString() {
    	
    	StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
    	
		if(null != this.searchGuestOrderInfo){
			sb.append(crlf).append("SearchGuestOrderInfo: ").append(this.searchGuestOrderInfo.toString());
		}
		
		if(null != this.guestNo){
			sb.append(crlf).append("GuestNo: ").append(this.guestNo.toString());
		}
    	return sb.toString();
    }
}
