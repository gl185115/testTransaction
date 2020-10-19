package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchForwardWithTerminalidPosLog")
@ApiModel(value="SearchForwardWithTerminalidPosLog")
public class SearchForwardWithTerminalidPosLog extends ResultBase {

	@XmlElement(name = "PosLog")
	private PosLog poslog;

	@XmlElement(name = "posLogXml")
	private String posLogXml;

	@XmlElement(name = "Status")
	private int Status;

	@ApiModelProperty( value="PoslogXmlèÓïÒ", notes="PoslogXmlèÓïÒ")
	public String getPosLogXml() {
		return posLogXml;
	}

	public void setPosLogXml(String posLogXml) {
		this.posLogXml = posLogXml;
	}

	@ApiModelProperty( value="PoslogèÓïÒ", notes="PoslogèÓïÒ")
	public PosLog getPoslog() {
		return poslog;
	}

	public void setPoslog(PosLog poslog) {
		this.poslog = poslog;
	}

	@ApiModelProperty( value="éÊà¯èÛë‘", notes="éÊà¯èÛë‘")
	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	/**
	 * Overrides the toString() method.
	 *
	 * @return The String representration of POSLog.
	 */
	public final String toString() {
		StringBuffer str = new StringBuffer();
		str.append(super.toString()).append("\r\n");

		if (null != poslog) {
			str.append("Transaction: ").append(poslog.toString());
		}

		return str.toString();
	}
}
