package ncr.res.mobilepos.uiconfig.model.fileInfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PictureInfoList")
public class PictureInfoList extends ResultBase {

    @XmlElement(name = "result")
    private List<String> result;
    /**
  	 * @return the Result
  	 */
	public List<String> getResult() {
		return result;
	}
	/**
	 * @param Result the Result to set
	 */
	public void setResult(List<String> result) {
		this.result = result;
	}

	public final String toString() {
		StringBuilder ret = new StringBuilder();
		String crlf = "\r\n";
		if (null != this.result) {
			for (String list : result) {
				ret.append(crlf).append("PictureInfo : ").append(list.toString());
			}
		}
		return ret.toString();
	}

	
}
