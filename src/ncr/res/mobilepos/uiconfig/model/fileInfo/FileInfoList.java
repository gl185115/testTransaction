package ncr.res.mobilepos.uiconfig.model.fileInfo;

import ncr.res.mobilepos.model.ResultBase;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "FileInfoList")
public class FileInfoList extends ResultBase{
	
    @XmlElement(name = "FileInfoList")
    private List<FileInfo> fileInfoList;
    
    /**
	 * @return the FileInfoList
	 */
	public List<FileInfo> getFileInfoList() {
        return fileInfoList;
	}

	/**
	 * @param FileInfoList the FileInfoList to set
	 */
	public final void setFileInfoList(final List<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}

	public final String toString() {
		StringBuilder ret = new StringBuilder();
		String crlf = "\r\n";
		if (null != this.fileInfoList) {
			for (FileInfo fileInfo : fileInfoList) {
				ret.append(crlf).append("FileInfo : ").append(fileInfo.toString());
			}
		}
		return ret.toString();
	}
	
}