package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CardTypeInfo")
@ApiModel(value="CardTypeInfo")
public class CardTypeInfo extends ResultBase {
    @XmlElement(name = "CompanyId")
    private String companyId;
    @XmlElement(name = "StoreId")
    private String storeId;
    @XmlElement(name = "CardTypeId")
    private String cardTypeId;
    @XmlElement(name = "CardTypeName")
    private String cardTypeName;
    @XmlElement(name = "CardTypeKanaName")
    private String cardTypeKanaName;
    @XmlElement(name = "CardTypeShortName")
    private String cardTypeShortName;
    @XmlElement(name = "CardTypeShorKanaName")
    private String cardTypeShortKanaName;
    @XmlElement(name = "TerminalCompanyId")
    private String terminalCompanyId;
    @XmlElement(name = "CardClassId")
    private String cardClassId;
    @XmlElement(name = "CardCompanyId")
    private String cardCompanyId;
    @XmlElement(name = "MemberType")
    private String memberType;
    @XmlElement(name = "MemberRank")
    private String memberRank;
    @XmlElement(name = "NewRegistFlag")
    private int newRegistFlag;
    @XmlElement(name = "CardMergeFlag")
    private int cardMergeFlag;
    @XmlElement(name = "PointExpdateAddMonth")
    private int pointExpdateAddMonth;
    @XmlElement(name = "PointExpdatePrintFlag")
    private int pointExpdatePrintFlag;
    @XmlElement(name = "DisplayDigitType")
    private String displayDigitType;
    @XmlElement(name = "PrintDigitType")
    private String printDigitType;
    @XmlElement(name = "CardCategory")
    private String cardCategory;
    @XmlElement(name = "PointAllowanceFlag")
    private String pointAllowanceFlag;
    @XmlElement(name = "UCUseArea")
    private String uCUseArea;
    @XmlElement(name = "PrefixCode16From")
    private String prefixCode16From;
    @XmlElement(name = "PrefixCode16To")
    private String prefixCode16To;
    @XmlElement(name = "PrefixCode13")
    private String prefixCode13;
    @XmlElement(name = "CardStatusType")
    private String cardStatusType;
    @XmlElement(name = "MagneticDataType")
    private String magneticDataType;
    @XmlElement(name = "SubCode1")
    private String subCode1;
    @XmlElement(name = "OwnCreditFlag")
    private String ownCreditFlag;

    /**
     * @return the companyId
     */
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the storeId
     */
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
    public String getStoreId() {
        return storeId;
    }

    /**
     * @param storeId
     *            the storeId to set
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * @return the cardTypeId
     */
    @ApiModelProperty(value="�J�[�h�^�C�v�R�[�h", notes="�J�[�h�^�C�v�R�[�h")
    public String getCardTypeId() {
        return cardTypeId;
    }

    /**
     * @param cardTypeId
     *            the cardTypeId to set
     */
    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    /**
     * @return the cardTypeName
     */
    @ApiModelProperty(value="�J�[�h�^�C�v����", notes="�J�[�h�^�C�v����")
    public String getCardTypeName() {
        return cardTypeName;
    }

    /**
     * @param cardTypeName
     *            the cardTypeName to set
     */
    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    /**
     * @return the cardTypeKanaName
     */
    @ApiModelProperty(value="�J�[�h�^�C�v����(�J�i)", notes="�J�[�h�^�C�v����(�J�i)")
    public String getCardTypeKanaName() {
        return cardTypeKanaName;
    }

    /**
     * @param cardTypeKanaName
     *            the cardTypeKanaName to set
     */
    public void setCardTypeKanaName(String cardTypeKanaName) {
        this.cardTypeKanaName = cardTypeKanaName;
    }

    /**
     * @return the cardTypeShortName
     */
    @ApiModelProperty(value="�J�[�h�^�C�v����", notes="�J�[�h�^�C�v����")
    public String getCardTypeShortName() {
        return cardTypeShortName;
    }

    /**
     * @param cardTypeShortName
     *            the cardTypeShortName to set
     */
    public void setCardTypeShortName(String cardTypeShortName) {
        this.cardTypeShortName = cardTypeShortName;
    }

    /**
     * @return the cardTypeShortKanaName
     */
    @ApiModelProperty(value="�J�[�h�^�C�v����(�J�i)", notes="�J�[�h�^�C�v����(�J�i)")
    public String getCardTypeShortKanaName() {
        return cardTypeShortKanaName;
    }

    /**
     * @param cardTypeShortKanaName
     *            the cardTypeShortKanaName to set
     */
    public void setCardTypeShortKanaName(String cardTypeShortKanaName) {
        this.cardTypeShortKanaName = cardTypeShortKanaName;
    }

    /**
     * @return the terminalCompanyId
     */
    @ApiModelProperty(value="��ЃR�[�h(�[��)", notes="��ЃR�[�h(�[��)")
    public String getTerminalCompanyId() {
        return terminalCompanyId;
    }

    /**
     * @param terminalCompanyId
     *            the terminalCompanyId to set
     */
    public void setTerminalCompanyId(String terminalCompanyId) {
        this.terminalCompanyId = terminalCompanyId;
    }

    /**
     * @return the cardClassId
     */
    @ApiModelProperty(value="�J�[�h�敪", notes="�J�[�h�敪")
    public String getCardClassId() {
        return cardClassId;
    }

    /**
     * @param cardClassId
     *            the cardClassId to set
     */
    public void setCardClassId(String cardClassId) {
        this.cardClassId = cardClassId;
    }

    /**
     * @return the cardCompanyId
     */
    @ApiModelProperty(value="��ЃR�[�h(�J�[�h)", notes="��ЃR�[�h(�J�[�h)")
    public String getCardCompanyId() {
        return cardCompanyId;
    }

    /**
     * @param cardCompanyId
     *            the cardCompanyId to set
     */
    public void setCardCompanyId(String cardCompanyId) {
        this.cardCompanyId = cardCompanyId;
    }

    /**
     * @return the memberType
     */
    @ApiModelProperty(value="����敪", notes="����敪")
    public String getMemberType() {
        return memberType;
    }

    /**
     * @param memberType
     *            the memberType to set
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    /**
     * @return the memberRank
     */
    @ApiModelProperty(value="��������N", notes="��������N")
    public String getMemberRank() {
        return memberRank;
    }

    /**
     * @param memberRank
     *            the memberRank to set
     */
    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }

    /**
     * @return the newRegistFlag
     */
    @ApiModelProperty(value="�V�K�o�^�t���O", notes="�V�K�o�^�t���O")
    public int getNewRegistFlag() {
        return newRegistFlag;
    }

    /**
     * @param newRegistFlag
     *            the newRegistFlag to set
     */
    public void setNewRegistFlag(int newRegistFlag) {
        this.newRegistFlag = newRegistFlag;
    }

    /**
     * @return the cardMergeFlag
     */
    @ApiModelProperty(value="�ؑ�/���Z�ۃt���O", notes="�ؑ�/���Z�ۃt���O")
    public int getCardMergeFlag() {
        return cardMergeFlag;
    }

    /**
     * @param cardMergeFlag
     *            the cardMergeFlag to set
     */
    public void setCardMergeFlag(int cardMergeFlag) {
        this.cardMergeFlag = cardMergeFlag;
    }

    /**
     * @return the pointExpdateAddMonth
     */
    @ApiModelProperty(value="�L��������������", notes="�L��������������")
    public int getPointExpdateAddMonth() {
        return pointExpdateAddMonth;
    }

    /**
     * @param pointExpdateAddMonth
     *            the pointExpdateAddMonth to set
     */
    public void setPointExpdateAddMonth(int pointExpdateAddMonth) {
        this.pointExpdateAddMonth = pointExpdateAddMonth;
    }

    /**
     * @return the pointExpdatePrintFlag
     */
    @ApiModelProperty(value="�L�������󎚃t���O", notes="�L�������󎚃t���O")
    public int getPointExpdatePrintFlag() {
        return pointExpdatePrintFlag;
    }

    /**
     * @param pointExpdatePrintFlag
     *            the pointExpdatePrintFlag to set
     */
    public void setPointExpdatePrintFlag(int pointExpdatePrintFlag) {
        this.pointExpdatePrintFlag = pointExpdatePrintFlag;
    }

    /**
     * @return the displayDigitType
     */
    @ApiModelProperty(value="�\�������敪", notes="�\�������敪")
    public String getDisplayDigitType() {
        return displayDigitType;
    }

    /**
     * @param displayDigitType
     *            the displayDigitType to set
     */
    public void setDisplayDigitType(String displayDigitType) {
        this.displayDigitType = displayDigitType;
    }

    /**
     * @return the printDigitType
     */
    @ApiModelProperty(value="�󎚌����敪", notes="�󎚌����敪")
    public String getPrintDigitType() {
        return printDigitType;
    }

    /**
     * @param printDigitType
     *            the printDigitType to set
     */
    public void setPrintDigitType(String printDigitType) {
        this.printDigitType = printDigitType;
    }

    /**
     * @return the cardCategory
     */
    @ApiModelProperty(value="�J�[�h���", notes="�J�[�h���")
    public String getCardCategory() {
        return cardCategory;
    }

    /**
     * @param cardCategory
     *            the cardCategory to set
     */
    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    /**
     * @return the pointAllowanceFlag
     */
    @ApiModelProperty(value="�|�C���g�����s�t���O", notes="�|�C���g�����s�t���O")
    public String getPointAllowanceFlag() {
        return pointAllowanceFlag;
    }

    /**
     * @param pointAllowanceFlag
     *            the pointAllowanceFlag to set
     */
    public void setPointAllowanceFlag(String pointAllowanceFlag) {
        this.pointAllowanceFlag = pointAllowanceFlag;
    }

    /**
     * @return the uCUseArea
     */
    @ApiModelProperty(value="UC�g�p��1", notes="UC�g�p��1")
    public String getuCUseArea() {
        return uCUseArea;
    }

    /**
     * @param uCUseArea
     *            the uCUseArea to set
     */
    public void setuCUseArea(String uCUseArea) {
        this.uCUseArea = uCUseArea;
    }

    /**
     * @return the prefixCode16From
     */
    @ApiModelProperty(value="�擪�R�[�h�i16���j�iFROM�j", notes="�擪�R�[�h�i16���j�iFROM�j")
    public String getPrefixCode16From() {
        return prefixCode16From;
    }

    /**
     * @param prefixCode16From
     *            the prefixCode16From to set
     */
    public void setPrefixCode16From(String prefixCode16From) {
        this.prefixCode16From = prefixCode16From;
    }

    /**
     * @return the prefixCode16To
     */
    @ApiModelProperty(value="�擪�R�[�h�i16���j�iTO�j", notes="�擪�R�[�h�i16���j�iTO�j")
    public String getPrefixCode16To() {
        return prefixCode16To;
    }

    /**
     * @param prefixCode16To
     *            the prefixCode16To to set
     */
    public void setPrefixCode16To(String prefixCode16To) {
        this.prefixCode16To = prefixCode16To;
    }

    /**
     * @return the prefixCode13
     */
    @ApiModelProperty(value="�擪�R�[�h�i13���j", notes="�擪�R�[�h�i13���j")
    public String getPrefixCode13() {
        return prefixCode13;
    }

    /**
     * @param prefixCode13
     *            the prefixCode13 to set
     */
    public void setPrefixCode13(String prefixCode13) {
        this.prefixCode13 = prefixCode13;
    }

    /**
     * @return the cardStatusType
     */
    @ApiModelProperty(value="�J�[�h��ԋ敪", notes="�J�[�h��ԋ敪")
    public String getCardStatusType() {
        return cardStatusType;
    }

    /**
     * @param cardStatusType
     *            the cardStatusType to set
     */
    public void setCardStatusType(String cardStatusType) {
        this.cardStatusType = cardStatusType;
    }

    /**
     * @return the magneticDataType
     */
    @ApiModelProperty(value="���C�f�[�^�敪", notes="���C�f�[�^�敪")
    public String getMagneticDataType() {
        return magneticDataType;
    }

    /**
     * @param magneticDataType
     *            the magneticDataType to set
     */
    public void setMagneticDataType(String magneticDataType) {
        this.magneticDataType = magneticDataType;
    }

    /**
     * @return the subCode1
     */
    @ApiModelProperty(value="�X�܉�ЃR�[�h(�J�[�h)", notes="�X�܉�ЃR�[�h(�J�[�h)")
    public String getSubCode1() {
        return subCode1;
    }

    /**
     * @param subCode1
     *            the subCode1 to set
     */
    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    @ApiModelProperty(value="���ЃN���W�b�g�t���O", notes="���ЃN���W�b�g�t���O")
    public String getOwnCreditFlag() {
		return ownCreditFlag;
	}

	public void setOwnCreditFlag(String ownCreditFlag) {
		this.ownCreditFlag = ownCreditFlag;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CardTypeInfo [companyId=" + companyId + ", storeId=" + storeId + ", cardTypeId=" + cardTypeId
                + ", cardTypeName=" + cardTypeName + ", cardTypeKanaName=" + cardTypeKanaName + ", cardTypeShortName="
                + cardTypeShortName + ", cardTypeShortKanaName=" + cardTypeShortKanaName + ", terminalCompanyId="
                + terminalCompanyId + ", cardClassId=" + cardClassId + ", cardCompanyId=" + cardCompanyId
                + ", memberType=" + memberType + ", memberRank=" + memberRank + ", newRegistFlag=" + newRegistFlag
                + ", cardMergeFlag=" + cardMergeFlag + ", pointExpdateAddMonth=" + pointExpdateAddMonth
                + ", pointExpdatePrintFlag=" + pointExpdatePrintFlag + ", displayDigitType=" + displayDigitType
                + ", printDigitType=" + printDigitType + ", cardCategory=" + cardCategory + ", pointAllowanceFlag="
                + pointAllowanceFlag + ", uCUseArea=" + uCUseArea + ", prefixCode16From=" + prefixCode16From
                + ", prefixCode16To=" + prefixCode16To + ", prefixCode13=" + prefixCode13 + ", cardStatusType="
                + cardStatusType + ", magneticDataType=" + magneticDataType + "]";
    }

}
