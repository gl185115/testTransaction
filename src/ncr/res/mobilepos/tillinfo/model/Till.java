package ncr.res.mobilepos.tillinfo.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="Till")
@ApiModel(value="Till")
public class Till{

	/**
	 * Stores companyId
	 */
	@XmlElement(name = "CompanyId")
	private String companyId;
    /**
     * Store number which the Till belongs to.
     */
    @XmlElement(name = "StoreId")
    private String storeId;
	/**
     * Till/Drawer identifier.
     */
    @XmlElement(name = "TillId")
    private String tillId;
    /**
     * Device Name
     */
    @XmlElement(name = "DeviceName")
    private String deviceName;
    /**
     * Transaction business day date.
     */
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;
    /**
     * Start of day flag. (0: not finished, 9: in SOD processing, 1: finished)
     */
    @XmlElement(name = "SodFlag")
    private String sodFlag;
    /**
     * End of day flag. (0: not finished, 9: in EOD processing, 1: finished)
     */
    @XmlElement(name = "EodFlag")
    private String eodFlag;
    /**
     * Insertion date.
     */
    private String insDate;
    /**
     * Update date
     */
    private String updDate;
    /**
     * Update application ID
     */
    private String updAppId;
    /**
     * Update operator code
     */
    @XmlElement(name = "OperatorId")
    private String updOpeCode;
    /**
     * Update SOD flag state.
     * (1) Allow "SOD" processing
     * (2) Already finish "SOD"
     * (3) Other tablet is in the "SOD" processing.
     */
    @XmlElement(name = "State")
    private String state;
    /**
     * Terminal/Device identifier
     */
    @XmlElement(name = "TerminalId")
    private String terminalId;
    
    /**
     * EodSummary
     */
    @XmlElement(name = "EodSummary")
    private String eodSummary;

    @ApiModelProperty(value="会社コード", notes="会社コード")
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    @ApiModelProperty(value="店舗コード", notes="店舗コード")
	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

    @ApiModelProperty(value="ドロワーコード", notes="ドロワーコード")
	public final String getTillId() {
		return tillId;
	}
	public final void setTillId(String tillId) {
		this.tillId = tillId;
	}

    @ApiModelProperty(value="営業日", notes="営業日")
	public final String getBusinessDayDate() {
		return businessDayDate;
	}
	public final void setBusinessDayDate(String businessDayDate) {
		this.businessDayDate = businessDayDate;
	}

    @ApiModelProperty(value="SODフラグ", notes="SODフラグ")
	public final String getSodFlag() {
		return sodFlag;
	}
	public final void setSodFlag(String sodFlag) {
		this.sodFlag = sodFlag;
	}

    @ApiModelProperty(value="短いSODフラグ", notes="短いSODフラグ")
    public final short getSodFlagAsShort() {
        return Short.parseShort(this.sodFlag);
    }

    @ApiModelProperty(value="EODフラグ", notes="EODフラグ")
	public final String getEodFlag() {
		return eodFlag;
	}
	public final void setEodFlag(String eodFlag) {
		this.eodFlag = eodFlag;
	}

    @ApiModelProperty(value="短いEODフラグ", notes="短いEODフラグ")
    public final short getEodFlagAsShort() {
        return Short.parseShort(this.eodFlag);
    }

    @ApiModelProperty(value="登録日時", notes="登録日時")
	public final String getInsDate() {
		return insDate;
	}
	public final void setInsDate(String insDate) {
		this.insDate = insDate;
	}

    @ApiModelProperty(value="最終更新日時", notes="最終更新日時")
	public final String getUpdDate() {
		return updDate;
	}
	public final void setUpdDate(String updDate) {
		this.updDate = updDate;
	}

    @ApiModelProperty(value="登録プログラムＩＤ", notes="登録プログラムＩＤ")
	public final String getUpdAppId() {
		return updAppId;
	}
	public final void setUpdAppId(String updAppId) {
		this.updAppId = updAppId;
	}

    @ApiModelProperty(value="登録ユーザＩＤ", notes="登録ユーザＩＤ")
	public final String getUpdOpeCode() {
		return updOpeCode;
	}
	public final void setUpdOpeCode(String updOpeCode) {
		this.updOpeCode = updOpeCode;
	}

    @ApiModelProperty(value="状態", notes="状態")
	public final String getState() {
		return state;
	}
	public final void setState(String state) {
		this.state = state;
	}

    @ApiModelProperty(value="端末コード", notes="端末コード")
	public final String getTerminalId() {
		return this.terminalId;
	}

	public final void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

    @ApiModelProperty(value="端末名称", notes="端末名称")
	public final String getDeviceName() {
		return this.deviceName;
	}

	public final void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
    @ApiModelProperty(value="集計済フラグ", notes="集計済フラグ")
	public final String getEodSummary() {
		return this.eodSummary;
	}

	public final void setEodSummary(String eodSummary) {
		this.eodSummary = eodSummary;
	}

    /**
     * Default constructor.
     */
    public Till() {
    }

    /**
     * Copy constructor.
     */
    public Till(Till sourceTill) {
        this.companyId = sourceTill.companyId;
        this.storeId = sourceTill.storeId;
        this.tillId = sourceTill.tillId;
        this.deviceName = sourceTill.deviceName;
        this.businessDayDate = sourceTill.businessDayDate;
        this.sodFlag = sourceTill.sodFlag;
        this.eodFlag = sourceTill.eodFlag;
        this.insDate = sourceTill.insDate;
        this.updDate = sourceTill.updDate;
        this.updAppId = sourceTill.updAppId;
        this.updOpeCode = sourceTill.updOpeCode;
        this.state = sourceTill.state;
        this.terminalId = sourceTill.terminalId;
    }
}
