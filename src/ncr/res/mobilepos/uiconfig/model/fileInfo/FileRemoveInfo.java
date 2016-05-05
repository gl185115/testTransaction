package ncr.res.mobilepos.uiconfig.model.fileInfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "FileRemoveInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class FileRemoveInfo extends ResultBase {
	
	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "description")
	private List<FileRemove> description;
	
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
	 * @return the description
	 */
	public List<FileRemove> getDescrption() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(List<FileRemove> description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";

        sb.append("status: ").append(status);
        sb.append(crlf).append("description: ").append(description);
		return sb.toString();
	}
}
