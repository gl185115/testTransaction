package ncr.res.mobilepos.uiconfig.model.fileInfo;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "FileInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class FileInfo extends ResultBase {
	
	@XmlElement(name = "FileName")
	private String fileName;

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

        sb.append("FileName: ").append(fileName);
		
		return sb.toString();
	}
}
