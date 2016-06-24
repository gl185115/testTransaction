package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DepartmentName")
@ApiModel(value="DepartmentName")
public class DepartmentName {
    @XmlElement(name = "en")
    private String en;
    
    @XmlElement(name = "ja")
    private String ja;
    @ApiModelProperty(value="‰pŒê”Å", notes="‰pŒê”Å")
	public final String getEn() {
		return en;
	}

	public final void setEn(String en) {
		this.en = en;
	}
	@ApiModelProperty(value="“ú–{Œê”Å", notes="“ú–{Œê”Å")
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
