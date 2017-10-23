package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "IndicatorInfo")
@ApiModel(value="IndicatorInfo")
public class IndicatorInfo {

    @XmlElement(name = "DisplayName")
    private String displayName;

    @XmlElement(name = "CheckInterval")
    private int checkInterval;

    @XmlElement(name = "RequestType")
    private String requestType;

    @XmlElement(name = "URL")
    private String url;

    @XmlElement(name = "Request")
    private String request;

    @XmlElement(name = "ReturnKey")
    private String returnKey;

    @XmlElement(name = "NormalValue")
    private String normalValue;
    
    @XmlElement(name = "DisplayOrder")
    private int displayOrder;

    @ApiModelProperty(value="�C���W�P�[�^�[�̕\����", notes="�C���W�P�[�^�[�̕\����")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @ApiModelProperty(value="�`�F�b�N�Ԋu(�b)", notes="�`�F�b�N�Ԋu(�b)")
    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    @ApiModelProperty(value="'GET' or 'POST' or 'WS' or 'EVENT'", notes="'GET' or 'POST' or 'WS' or 'EVENT'")
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @ApiModelProperty(value="���N�G�X�g��URL", notes="���N�G�X�g��URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(value="���N�G�X�g�f�[�^ ", notes="���N�G�X�g�f�[�^ ")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @ApiModelProperty(value="����/�ُ�𔻒f����ׂ̃��X�|���X�f�[�^�̃L�[", notes="����/�ُ�𔻒f����ׂ̃��X�|���X�f�[�^�̃L�[")
    public String getReturnKey() {
        return returnKey;
    }

    public void setReturnKey(String returnKey) {
        this.returnKey = returnKey;
    }

    @ApiModelProperty(value="'����'�Ɣ��f����l�i�h����h�̃��X�|���X�f�[�^�̃L�[�ɃZ�b�g�����l�j", notes="'����'�Ɣ��f����l�i�h����h�̃��X�|���X�f�[�^�̃L�[�ɃZ�b�g�����l�j")
    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }
    
    @ApiModelProperty(value="��ʂւ̕\����", notes="��ʂւ̕\����")
    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DisplayName: ").append(displayName);
        sb.append("CheckInterval: ").append(checkInterval);
        sb.append("RequestType: ").append(requestType);
        sb.append("URL: ").append(url);
        sb.append("Request: ").append(request);
        sb.append("ReturnKey: ").append(returnKey);
        sb.append("NormalValue: ").append(normalValue);
        sb.append("DisplayOrder: ").append(displayOrder);
        return sb.toString();
    }
}
