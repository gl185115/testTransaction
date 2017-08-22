package ncr.res.mobilepos.systemsetting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * A model Class that represents the TerminalInfo
 * configured in the Web Store Server.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TerminalInfo")
@ApiModel(value="TerminalInfo")
public class TerminalInfo extends ResultBase {
    /**
     * CompanyID
     */
    @XmlElement(name = "CompanyID")
    private String companyId;
    /**
     * StoreID
     */
    @XmlElement(name = "StoreID")
    private String storeId;
    /**
     * TerminalID
     */
    @XmlElement(name = "TerminalID")
    private String terminalId;

    @ApiModelProperty( value="会社コード", notes="会社コード")
    public final String getCompanyId() {
        return companyId;
    }

    public final void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }
    
    @ApiModelProperty( value="店番号", notes="店番号")
    public final String getStoreID() {
        return storeId;
    }

    public final void setStoreId(final String storeId) {
        this.storeId = storeId;
    }
    
    @ApiModelProperty( value="ターミナル番号", notes="ターミナル番号")
    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(final String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (companyId != null) {
            sb.append(companyId.toString());
        }
        if (storeId != null) {
            sb.append(storeId.toString());
        }
        if (terminalId != null) {
            sb.append(terminalId.toString());
        }
        return sb.toString();
    }
}
