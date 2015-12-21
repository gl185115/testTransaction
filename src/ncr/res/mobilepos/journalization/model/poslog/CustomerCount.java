package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CustomerCount")
public class CustomerCount {
	
	@XmlElement(name = "Visited")
	private String visited;
	
	@XmlElement(name = "Registered")
	private String registered;
	
	public final String getRegistered() {
		return this.registered;
	}
	public final void setRegistered(String str) {
		this.registered = str;
	}
	
	public final String getVisited() {
		return this.visited;
	}
	public final void setVisited(String str) {
		this.visited = str;
	}
}