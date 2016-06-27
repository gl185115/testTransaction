package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceAttribute")
@ApiModel(value="DeviceAttribute")
public class DeviceAttribute extends ResultBase {
    @XmlElement(name = "CompanyId")
    private String companyId;

	@XmlElement(name = "StoreId")
    private String storeId;

	@XmlElement(name = "TerminalId")
	private String terminalId;

	@XmlElement(name = "Training")
    private int training;

	@XmlElement(name = "DeviceName")
	private String deviceName;

	@XmlElement(name = "AttributeId")
	private String attributeId;

	@XmlElement(name = "PrinterId")
	private String printerId;

	@XmlElement(name = "TillId")
	private String tillId;

	@XmlElement(name= "LinkQueueBuster")
	private String linkQueueBuster;

	@XmlElement(name = "PrintDes")
	private String printDes;

	@XmlElement(name = "DrawerId")
	private String drawerId;

	@XmlElement(name = "DisplayName")
	private String displayName;

	@XmlElement(name = "AttributeDes")
	private String attributeDes;

	/**
     * @return the companyId
     */
	@ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public final String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public final void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	/**
	 * @return the storeId
	 */
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the terminalId
	 */
	@ApiModelProperty(value="�[���ԍ�", notes="�[���ԍ�")
	public final String getTerminalId() {
		return terminalId;
	}

	/**
	 * @param terminalId the terminalId to set
	 */
	public final void setTerminalId(final String terminalId) {
		this.terminalId = terminalId;
	}

	/**
     * @return the training mode
     */
	@ApiModelProperty(value="�g���[�j���O�t���O", notes="�g���[�j���O�t���O")
    public final int getTrainingMode() {
        return training;
    }

    /**
     * @param trainingMode to set
     */
    public final void setTrainingMode(final int trainingMode) {
        this.training = trainingMode;
    }

	/**
	 * @return the deviceName
	 */
    @ApiModelProperty(value="�[������", notes="�[������")
	public final String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public final void setDeviceName(final String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the attributeId
	 */
	@ApiModelProperty(value="�����R�[�h", notes="�����R�[�h")
	public final String getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId the attributeId to set
	 */
	public final void setAttributeId(final String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the printerId
	 */
	@ApiModelProperty(value="�v�����^�[ID", notes="�v�����^�[ID")
	public final String getPrinterId() {
		return printerId;
	}

	/**
	 * @param printerId the printerId to set
	 */
	public final void setPrinterId(final String printerId) {
		this.printerId = printerId;
	}

	/**
	 * @return the tillId
	 */
	@ApiModelProperty(value="�h�����[ID", notes="�h�����[ID")
	public final String getTillId() {
		return tillId;
	}

	/**
	 * @param tillId the tillId to set
	 */
	public final void setTillId(final String tillId) {
		this.tillId = tillId;
	}

	/**
	 * @return the linkQueueBuster
	 */
	@ApiModelProperty(value="Bluetooth�v�����^�[ID", notes="Bluetooth�v�����^�[ID")
	public final String getLinkQueueBuster() {
		return linkQueueBuster;
	}

	/**
	 * @param linkQueueBuster the linkQueueBuster to set
	 */
	public final void setLinkQueueBuster(final String linkQueueBuster) {
		this.linkQueueBuster = linkQueueBuster;
	}

	/**
	 * @return the printDes
	 */
	@ApiModelProperty(value="���l", notes="���l")
	public final String getPrintDes() {
		return printDes;
	}

	/**
	 * @param printDes the printDes to set
	 */
	public final void setPrintDes(final String printDes) {
		this.printDes = printDes;
	}

	/**
	 * @return the drawerId
	 */
	@ApiModelProperty(value="�h�����[�R�[�h", notes="�h�����[�R�[�h")
	public final String getDrawerId() {
		return drawerId;
	}

	/**
	 * @param drawerId the drawerId to set
	 */
	public final void setDrawerId(final String drawerId) {
		this.drawerId = drawerId;
	}

	/**
	 * @return the displayName
	 */
	@ApiModelProperty(value="iphone�̉�ʏ�ɕ\�����ꂽ���O", notes="iphone�̉�ʏ�ɕ\�����ꂽ���O")
	public final String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public final void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the attributeDes
	 */
	@ApiModelProperty(value="�L�q", notes="�L�q")
	public final String getAttributeDes() {
		return attributeDes;
	}

	/**
	 * @param attributeDes the attributeDes to set
	 */
	public final void setAttributeDes(final String attributeDes) {
		this.attributeDes = attributeDes;
	}

	@Override
	public String toString() {
		return "DeviceAttribute [storeId=" + storeId + ", terminalId="
				+ terminalId + ", deviceName=" + deviceName + ", attributeId="
				+ attributeId + ", printerId=" + printerId + ", tillId="
				+ tillId + ", linkQueueBuster=" + linkQueueBuster
				+ ", printDes=" + printDes + ", drawerId=" + drawerId
				+ ", displayName=" + displayName + ", attributeDes="
				+ attributeDes + "]";
	}

}
