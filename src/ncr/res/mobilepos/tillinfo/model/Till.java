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

    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

    @ApiModelProperty(value="�h�����[�R�[�h", notes="�h�����[�R�[�h")
	public final String getTillId() {
		return tillId;
	}
	public final void setTillId(String tillId) {
		this.tillId = tillId;
	}

    @ApiModelProperty(value="�c�Ɠ�", notes="�c�Ɠ�")
	public final String getBusinessDayDate() {
		return businessDayDate;
	}
	public final void setBusinessDayDate(String businessDayDate) {
		this.businessDayDate = businessDayDate;
	}

    @ApiModelProperty(value="SOD�t���O", notes="SOD�t���O")
	public final String getSodFlag() {
		return sodFlag;
	}
	public final void setSodFlag(String sodFlag) {
		this.sodFlag = sodFlag;
	}

    @ApiModelProperty(value="�Z��SOD�t���O", notes="�Z��SOD�t���O")
    public final short getSodFlagAsShort() {
        return Short.parseShort(this.sodFlag);
    }

    @ApiModelProperty(value="EOD�t���O", notes="EOD�t���O")
	public final String getEodFlag() {
		return eodFlag;
	}
	public final void setEodFlag(String eodFlag) {
		this.eodFlag = eodFlag;
	}

    @ApiModelProperty(value="�Z��EOD�t���O", notes="�Z��EOD�t���O")
    public final short getEodFlagAsShort() {
        return Short.parseShort(this.eodFlag);
    }

    @ApiModelProperty(value="�o�^����", notes="�o�^����")
	public final String getInsDate() {
		return insDate;
	}
	public final void setInsDate(String insDate) {
		this.insDate = insDate;
	}

    @ApiModelProperty(value="�ŏI�X�V����", notes="�ŏI�X�V����")
	public final String getUpdDate() {
		return updDate;
	}
	public final void setUpdDate(String updDate) {
		this.updDate = updDate;
	}

    @ApiModelProperty(value="�o�^�v���O�����h�c", notes="�o�^�v���O�����h�c")
	public final String getUpdAppId() {
		return updAppId;
	}
	public final void setUpdAppId(String updAppId) {
		this.updAppId = updAppId;
	}

    @ApiModelProperty(value="�o�^���[�U�h�c", notes="�o�^���[�U�h�c")
	public final String getUpdOpeCode() {
		return updOpeCode;
	}
	public final void setUpdOpeCode(String updOpeCode) {
		this.updOpeCode = updOpeCode;
	}

    @ApiModelProperty(value="���", notes="���")
	public final String getState() {
		return state;
	}
	public final void setState(String state) {
		this.state = state;
	}

    @ApiModelProperty(value="�[���R�[�h", notes="�[���R�[�h")
	public final String getTerminalId() {
		return this.terminalId;
	}

	public final void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

    @ApiModelProperty(value="�[������", notes="�[������")
	public final String getDeviceName() {
		return this.deviceName;
	}

	public final void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
    @ApiModelProperty(value="�W�v�σt���O", notes="�W�v�σt���O")
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
