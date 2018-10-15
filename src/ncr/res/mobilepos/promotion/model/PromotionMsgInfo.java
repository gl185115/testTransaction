package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PromotionMsgInfo")
@ApiModel(value="PromotionMsgInfo")
public class PromotionMsgInfo {

	@XmlElement(name = "recordId")
    private Integer recordId;

	@XmlElement(name = "subject")
    private String subject;

	@XmlElement(name = "minimunPrice")
    private Long minimunPrice;

	@XmlElement(name = "MessageBody")
    private String messageBody;

	@XmlElement(name = "itemCode")
    private String itemCode;

	@XmlElement(name = "MdName")
    private String mdName;

	@ApiModelProperty(value="�Ǘ��ԍ�", notes="�Ǘ��ԍ�")
    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

	@ApiModelProperty(value="�^�C�g��", notes="�^�C�g��")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

	@ApiModelProperty(value="�������z", notes="�������z")
    public Long getMinimunPrice() {
        return minimunPrice;
    }

    public void setMinimunPrice(Long minimunPrice) {
        this.minimunPrice = minimunPrice;
    }

	@ApiModelProperty(value="�{��", notes="�{��")
    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

	@ApiModelProperty(value="���i��", notes="���i��")
    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }
}
