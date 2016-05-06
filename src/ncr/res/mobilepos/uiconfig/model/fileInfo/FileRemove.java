package ncr.res.mobilepos.uiconfig.model.fileInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileRemove")
@XmlAccessorType(XmlAccessType.NONE)
public class FileRemove {
	
	@XmlElement(name = "fileName")
	private String fileName;
	
	@XmlElement(name = "fullName")
	private String fullName;
	
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

        sb.append("fileName: ").append(fileName);
        sb.append("fullName: ").append(fullName);
		
		return sb.toString();
	}
}
