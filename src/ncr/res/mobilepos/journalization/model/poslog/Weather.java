package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Weather")
public class Weather {
	/*
	 *  private attribute code variable for member
	 */
	@XmlAttribute(name = "Code")
	private String code;
	/**
	 * private value element
	 */
	@XmlValue
	private String elementValue;
	/**
	 * 
	 * @return the code variable
	 */
	public final String getCode() {
		return this.code;
	}
	/**
	 * sets the code variable
	 * @param cd
	 */
	public final void setCode(String cd) {
		this.code = cd;
	}
	/**
	 * return the weather element value
	 * @return
	 */
	public final String getElementValue() {
		return this.elementValue;
	}
	/**
	 * sets the weather element value
	 * @param str
	 */
	public final void setElementValue(String str) {
		this.elementValue = str;
	}
}