package ncr.res.mobilepos.journalization.model;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01     2014.11.19  FENGSHA   客層情報取得(新規) 
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
import java.util.List;

/**
 * @author 
 * 
 * GuestZone Class is a Model representation of the GuestZone List.
 */
@XmlRootElement(name = "GuestZone")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ GuestZoneInfo.class })
@ApiModel(value="GuestZone")
public class GuestZone extends ResultBase{
	
	/*This list is used to save guest zone information*/
	@XmlElementWrapper(name = "GuestZoneInfos")
	private List<GuestZoneInfo> guestZoneInfos = null;
	
	/**
	 * Sets the guestzone information of the list.
	 *
	 * @param guestzone information ToSet
	 *            the new guestzoneinfos
	 */
	public final void setGuestZoneInfos(final List<GuestZoneInfo> guestZoneInfos) {
		this.guestZoneInfos = guestZoneInfos;
	}
	
	/**
	 * Gets the guestzone list.
	 *
	 * @return the guestzone list
	 */
	@ApiModelProperty(value="ゲストのゾーン情報", notes="ゲストのゾーン情報")
	public  List<GuestZoneInfo> getGuestZoneInfos() {
		return this.guestZoneInfos;
	}

	@Override
	public final String toString() {
		
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
		
		if(null != this.guestZoneInfos){
			sb.append(crlf).append("GuestZoneInfos :").append(this.guestZoneInfos.toString());
		}
		
		return sb.toString();
	}
}
