package ncr.res.mobilepos.uiconfig.model.fileInfo;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "FileDownLoadInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class FileDownLoadInfo extends ResultBase {
	
	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "result")
	private String result;
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";

        sb.append("status: ").append(status);
        sb.append(crlf).append("result: ").append(result);
		
		return sb.toString();
	}
}
