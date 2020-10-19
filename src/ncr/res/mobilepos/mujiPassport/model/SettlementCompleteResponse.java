package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class SettlementCompleteResponse extends ResultBase {
    @XmlElement(name = "resultCode")
    private int resultCode;

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "SettlementCompleteResponse [resultCode=" + resultCode + "]";
    }
}
