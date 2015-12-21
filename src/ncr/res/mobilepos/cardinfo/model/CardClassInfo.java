package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CardClassInfo")
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
	
	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public final String getCompanyId() {
		return companyId;
	}
	
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public final String getStoreId() {
		return storeId;
	}
	
	public final void setId(String id) {
		this.id = id;
	}
	
	public final String getId() {
		return id;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}
	
	public final String getKanaName() {
		return kanaName;
	}
	
	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public final String getShortName() {
		return shortName;
	}
	
	public final void setShortKanaName(String shortKanaName) {
		this.shortKanaName = shortKanaName;
	}
	
	public final String getShortKanaName() {
		return shortKanaName;
	}
	
	public final void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	
	public final String getCreditType() {
		return creditType;
	}
	
	public final void setMainCardDigitType(String mainCardDigitType) {
		this.mainCardDigitType = mainCardDigitType;
	}
	
	public final String getMainCardDigitType() {
		return mainCardDigitType;
	}
	
	public final void setRankType(String rankType) {
		this.rankType = rankType;
	}
	
	public final String getRankType() {
		return rankType;
	}

    public final void setCardStatusType(String cardStatus) {
        this.cardStatusType = cardStatus;
        
    }
    
    public final String getCardStatusType() {
        return cardStatusType;
        
    }
}
