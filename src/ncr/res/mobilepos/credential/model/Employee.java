package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Employee is a Model Class that represents an Operator's info It only consists
 * of Operator's number and name.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Employee")
@ApiModel(value="Employee")
public class Employee {
    /**
     * The company ID.
     */
    @XmlElement(name = "CompanyId")
    private String companyId;
    /**
     * The operator number.
     */
    @XmlElement(name = "OperatorID")
    private String number;

    /**
     * The operator name.
     */
    @XmlElement(name = "OperatorName")
    private String name;
    /**
     * The type of operator.
     */
    @XmlElement(name = "OperatorType")
    private String operatorType;
    /**
     * The workstation number.
     */
    @XmlElement(name = "WorkstationID")
    private String workStationID;
    /**
     * The store number.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

    /**
     * the passcode for the operator.
     */
    @XmlElement(name = "Passcode")
    private String passcode;
    
    private String updOpeCode;
    
    private String updAppId;
    
    private String status;
    
    private String role;

   /**
     * gets the passcode.
     *
     * @return passcode
     */
    @ApiModelProperty(value="�p�X���[�h", notes="�p�X���[�h")
    public final String getPasscode() {
        return passcode;
    }
    
   
    /**
     * sets the passcode.
     *
     * @param passCode
     *            - the passcode
     */
    public final void setPasscode(final String passCode) {
        this.passcode = passCode;
    }
    
    @ApiModelProperty(value="�ŏI�X�V���[�U�[�R�[�h", notes="�ŏI�X�V���[�U�[�R�[�h")
    public String getUpdOpeCode() {
        return updOpeCode;
    }

    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }

    @ApiModelProperty(value="�ŏI�X�V�v���O�����[�R�[�h", notes="�ŏI�X�V�v���O�����[�R�[�h")
    public String getUpdAppId() {
        return updAppId;
    }

    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }


    /**
     * Gets operator type.
     *
     * @return String operatorType.
     */
    @ApiModelProperty(value="����^�C�v", notes="����^�C�v")
    public final String getOperatorType() {
        return operatorType;
    }

    /**
     * Sets opertor type.
     *
     * @param type
     *            The operator's type.
     */
    public final void setOperatorType(final String type) {
        this.operatorType = type;
    }

    /**
     * Gets Company ID.
     *
     * @return String companyId.
     */
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public final String getCompanyId() {
        return companyId;
    }

    /**
     * Sets Company ID.
     *
     * @param companyIdToSet
     *            The companyIdToSet where the operator belongs.
     */
    public final void setCompanyId(final String companyIdToSet) {
        this.companyId = companyIdToSet;
    }

    /**
     * Gets workstation number.
     *
     * @return String workStationID.
     */
    @ApiModelProperty(value="�^�[�~�i���ԍ�", notes="�^�[�~�i���ԍ�")
    public final String getWorkStationID() {
        return workStationID;
    }

    /**
     * Sets workstation number.
     *
     * @param terminalID
     *            The workStationID where the operator belongs.
     */
    public final void setWorkStationID(final String terminalID) {
        this.workStationID = terminalID;
    }

    /**
     * Gets operator's store number.
     *
     * @return String retailStoreID.
     */
    @ApiModelProperty(value="�����X�R�[�h", notes="�����X�R�[�h")
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the retail store number.
     *
     * @param storeID
     *            The store number of operator.
     */
    public final void setRetailStoreID(final String storeID) {
        this.retailStoreID = storeID;
    }

    /**
     * Gets the operator number.
     *
     * @return String The operatorID.
     */
    @ApiModelProperty(value="������ԍ�", notes="������ԍ�")
    public final String getNumber() {
        return number;
    }

    /**
     * Sets the operator number.
     *
     * @param operatorID
     *            The operatorID.
     */
    public final void setNumber(final String operatorID) {
        this.number = operatorID;
    }

    /**
     * Gets the operator's name.
     *
     * @return String The operatorName.
     */
    @ApiModelProperty(value="��������O", notes="��������O")
    public final String getName() {
        return name;
    }

    /**
     * Sets the operator's name.
     *
     * @param operatorName
     *            The operatorName.
     */
    public final void setName(final String operatorName) {
        this.name = operatorName;
    }

    /**
     * @return the status
     */
    @ApiModelProperty(value="������g��", notes="������g��")
    public String getStatus() {
        return status;
    }


    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nName:" + this.getName());
        sb.append("\nPasscode:" + this.getPasscode());
        sb.append("\nOperatorType:" + this.getOperatorType());
        sb.append("\nStoreID:" + this.getRetailStoreID());
        sb.append("\nTerminalID:" + this.getWorkStationID());
        return sb.toString();
    }

    /**
     * Returns role
     * @return
     */
    @ApiModelProperty(value="�������", notes="�������")
	public String getRole() {
		return role;
	}

	/**
	 * Sets role
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}
}
