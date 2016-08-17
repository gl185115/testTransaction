package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ChobiTagDiscount")
public class ChobiTagDiscount extends Cash {
	
	@XmlAttribute(name = "Code")
	private String code;
	
	public final String getCode() {
		return this.code;
	}
	
	public final void setCode(String val) {
		this.code = val;
	}
}