package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GroupLineInfo")
public class GroupLineInfo {

	@XmlElement(name = "GroupId")
	private String groupId;
	
	@XmlElement(name = "GroupName")
	private String groupName;
	
	@XmlElement(name = "Line")
	private String line;
	
	@XmlElement(name = "LineName")
	private String lineName;

	/**
	 * @return the groupId
	 */
	public final String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public final void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public final String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public final void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the line
	 */
	public final String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public final void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the lineName
	 */
	public final String getLineName() {
		return lineName;
	}

	/**
	 * @param lineName the lineName to set
	 */
	public final void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String crlf = "\r\n";
		
		str.append("GroupId : ").append(this.groupId).append(crlf)
		   .append("GroupName : ").append(this.groupName).append(crlf)
		   .append("Line : ").append(this.line).append(crlf)
		   .append("LineName : ").append(this.groupName).append(crlf);
		
		return str.toString();
	}
	
}
