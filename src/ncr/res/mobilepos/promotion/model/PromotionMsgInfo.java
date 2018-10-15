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

	@ApiModelProperty(value="管理番号", notes="管理番号")
    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

	@ApiModelProperty(value="タイトル", notes="タイトル")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

	@ApiModelProperty(value="下限金額", notes="下限金額")
    public Long getMinimunPrice() {
        return minimunPrice;
    }

    public void setMinimunPrice(Long minimunPrice) {
        this.minimunPrice = minimunPrice;
    }

	@ApiModelProperty(value="本文", notes="本文")
    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

	@ApiModelProperty(value="商品コード", notes="商品コード")
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

	@ApiModelProperty(value="商品名", notes="商品名")
    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }
}
