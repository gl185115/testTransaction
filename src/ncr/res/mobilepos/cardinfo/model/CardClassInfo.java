package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CardClassInfo")
@ApiModel(value="CardClassInfo")
public class CardClassInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "CardClassId")
	private String id;
	@XmlElement(name = "CardClassName")
	private String name;
	@XmlElement(name = "CardClassKanaName")
	private String kanaName;
	@XmlElement(name = "CardClassShortName")
	private String shortName;
	@XmlElement(name = "CardClassShortKanaName")
	private String shortKanaName;
	@XmlElement(name = "CreditType")
	private String creditType;
	@XmlElement(name = "MainCardDigitType")
	private String mainCardDigitType;
	@XmlElement(name = "RankType")
	private String rankType;
	@XmlElement(name = "CardStatusType")
	private String cardStatusType;
	@XmlElement(name = "SubCode1")
	private String subcode1;
	
	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}
	
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}
	
	public final void setId(String id) {
		this.id = id;
	}

	@ApiModelProperty(value="�J�[�h�敪�R�[�h", notes="�J�[�h�敪�R�[�h")
	public final String getId() {
		return id;
	}
	
	public final void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value="�J�[�h�敪����", notes="�J�[�h�敪����")
	public final String getName() {
		return name;
	}
	
	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}

	@ApiModelProperty(value="�J�[�h�敪����(�J�i)", notes="�J�[�h�敪����(�J�i)")
	public final String getKanaName() {
		return kanaName;
	}
	
	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@ApiModelProperty(value="�J�[�h�敪����", notes="�J�[�h�敪����")
	public final String getShortName() {
		return shortName;
	}
	
	public final void setShortKanaName(String shortKanaName) {
		this.shortKanaName = shortKanaName;
	}

	@ApiModelProperty(value="�J�[�h�敪����(�J�i)", notes="�J�[�h�敪����(�J�i)")
	public final String getShortKanaName() {
		return shortKanaName;
	}
	
	public final void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	@ApiModelProperty(value="�N���W�b�g�敪", notes="�N���W�b�g�敪")
	public final String getCreditType() {
		return creditType;
	}
	
	public final void setMainCardDigitType(String mainCardDigitType) {
		this.mainCardDigitType = mainCardDigitType;
	}

	@ApiModelProperty(value="���C���J�[�h����", notes="���C���J�[�h����")
	public final String getMainCardDigitType() {
		return mainCardDigitType;
	}
	
	public final void setRankType(String rankType) {
		this.rankType = rankType;
	}

	@ApiModelProperty(value="�����N�敪", notes="�����N�敪")
	public final String getRankType() {
		return rankType;
	}

    public final void setCardStatusType(String cardStatus) {
        this.cardStatusType = cardStatus;
        
    }

	@ApiModelProperty(value="�J�[�h��ԋ敪", notes="�J�[�h��ԋ敪")
    public final String getCardStatusType() {
        return cardStatusType;
        
    }
    public final void setSubCode1(String subcode1) {
        this.subcode1 = subcode1;
        
    }

	@ApiModelProperty(value="�J�[�h�ԍ��擪", notes="�J�[�h�ԍ��擪")
    public final String getSubCode1() {
        return subcode1;
        
    }
}
