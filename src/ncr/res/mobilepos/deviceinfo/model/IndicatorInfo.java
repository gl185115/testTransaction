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

    @ApiModelProperty(value="インジケーターの表示名", notes="インジケーターの表示名")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @ApiModelProperty(value="チェック間隔(秒)", notes="チェック間隔(秒)")
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

    @ApiModelProperty(value="リクエスト先URL", notes="リクエスト先URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(value="リクエストデータ ", notes="リクエストデータ ")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @ApiModelProperty(value="正常/異常を判断する為のレスポンスデータのキー", notes="正常/異常を判断する為のレスポンスデータのキー")
    public String getReturnKey() {
        return returnKey;
    }

    public void setReturnKey(String returnKey) {
        this.returnKey = returnKey;
    }

    @ApiModelProperty(value="'正常'と判断する値（”正常”のレスポンスデータのキーにセットされる値）", notes="'正常'と判断する値（”正常”のレスポンスデータのキーにセットされる値）")
    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }
    
    @ApiModelProperty(value="画面への表示順", notes="画面への表示順")
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
