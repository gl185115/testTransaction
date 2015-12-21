package ncr.res.mobilepos.pricing.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Description")
public class Description {
    @XmlElement(name = "en")
    private String en;
    
    @XmlElement(name = "ja")
    private String ja;
	
    public final String getEn() {
		return en;
	}

	public final void setEn(String en) {
		this.en = en;
	}

	public final String getJa() {
		return ja;
	}

	public final void setJa(String ja) {
		this.ja = ja;
	}

	@Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("en: ").append(en).append(clrf)
        	.append("ja: ").append(ja).append(clrf);
        return str.toString();
    }

}
