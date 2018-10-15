package ncr.res.mobilepos.uiconfig.model.fileInfo;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "FileInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class FileInfo {
	
	@XmlElement(name = "FilePath")
	private String filePath;

	@XmlElement(name = "FileName")
	private String fileName;
	
	@XmlElement(name = "Index")
	private int index;
	
	@XmlElement(name = "ExistFlag")
	private boolean existFlag;
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * @return the existFlag
	 */
	public boolean getExistFlag() {
		return existFlag;
	}

	/**
	 * @param existFlag the existFlag to set
	 */
	public void setExistFlag(boolean existFlag) {
		this.existFlag = existFlag;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("FilePath: ").append(filePath);
		sb.append("FileName: ").append(fileName);
		sb.append("Index: ").append(index);
		sb.append("ExistFlag: ").append(existFlag);
		
		return sb.toString();
	}
}
