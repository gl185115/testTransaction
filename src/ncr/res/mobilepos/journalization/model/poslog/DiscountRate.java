package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DiscountRate")
public class DiscountRate {
	
	@XmlElement(name = "Kind")
	private List<Kind> kind;
	
	public final List<Kind> getKind() {
		return this.kind;
	}
	public final void setKind(List<Kind> knd) {
		this.kind = knd;
	}
	
}