package ncr.res.mobilepos.terminalInfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

/**
 * A model Class that represents the TerminalInfo configured in the Web Store
 * Server.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TerminalInfo")
@ApiModel(value = "TerminalInfo")
public class TerminalInfo extends ResultBase {
    /**
     * CompanyID
     */
    @XmlElement(name = "Txid")
    private String txid;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (txid != null) {
            sb.append(txid.toString());
        }
        return sb.toString();
    }
}
