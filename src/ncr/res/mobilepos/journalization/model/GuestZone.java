package ncr.res.mobilepos.journalization.model;
/**
 * ‰ü’è—š—ğ
 * ƒo[ƒWƒ‡ƒ“      ‰ü’è“ú•t        ’S“–Ò–¼      ‰ü’è“à—e
 * 1.01     2014.11.19  FENGSHA   ‹q‘wî•ñæ“¾(V‹K) 
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
	@ApiModelProperty(value="‹q‘wî•ñ", notes="‹q‘wî•ñ")
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
