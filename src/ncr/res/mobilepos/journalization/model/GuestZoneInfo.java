package ncr.res.mobilepos.journalization.model;
/**
 * ‰ü’è—š—ğ
 * ƒo[ƒWƒ‡ƒ“      ‰ü’è“ú•t        ’S“–Ò–¼      ‰ü’è“à—e
 * 1.01     2014.11.19  FENGSHA   ‹q‘wî•ñæ“¾(V‹K)
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jsefa.xml.annotation.XmlElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 *  @author
 *  GuestZoneInfo Class is a Model representation of the GuestZone.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GuestZoneInfo")
@ApiModel(value="GuestZoneInfo")
public class GuestZoneInfo {
	/* the guest id */
	@XmlElement(name = "GuestCode")
	private String guestcode;

	/* the guest zone name */
	@XmlElement(name = "GuestZoneName")
	private String guestzonename;

	/* the guest zone kana name */
	@XmlElement(name = "GuestZoneKanaName")
	private String guestzonekananame;

	/* the guest different */
	@XmlElement(name = "GuestGen")
	private String guestgen;

	/* the guest sex */
	@XmlElement(name = "GuestSex")
	private String guestsex;

	/* the guest type */
	@XmlElement(name = "GuestType")
	private String guesttype;

	/* This a column of reserved */
	@XmlElement(name = "SubCode")
	private String subcode;

	/**
	 * Gets the guest code.
	 *
	 * @return the guest code
	 */
	@ApiModelProperty(value="‹q‘wƒR[ƒh", notes="‹q‘wƒR[ƒh")
	public String getGuestcode() {
		return guestcode;
	}

	/**
	 * Sets the guest code of the column.
	 *
	 * @param guestcodeToSet
	 *            the new guest code
	 */
	public void setGuestcode(String guestcode) {
		this.guestcode = guestcode;
	}

	/**
	 * Gets the guestzone name.
	 *
	 * @return the guestzone name
	 */
	@ApiModelProperty(value="‹q‘w–¼Ìi", notes="‹q‘w–¼Ì")
	public String getGuestzonename() {
		return guestzonename;
	}

	/**
	 * Sets the guestzone name of the column.
	 *
	 * @param guestzonenameToSet
	 *            the new guestzone name
	 */
	public void setGuestzonename(String guestzonename) {
		this.guestzonename = guestzonename;
	}

	/**
	 * Gets the guestzone kananname.
	 *
	 * @return the guestzone kananname
	 */
	@ApiModelProperty(value="‹q‘wƒJƒi–¼Ì", notes="‹q‘wƒJƒi–¼Ì")
	public String getGuestzonekananame() {
		return guestzonekananame;
	}

	/**
	 * Sets the guestzone kananame of the column.
	 *
	 * @param guestzonekananameToSet
	 *            the new guestzone kananame
	 */
	public void setGuestzonekananame(String guestzonekananame) {
		this.guestzonekananame = guestzonekananame;
	}

	/**
	 * Gets the guestzone kananname.
	 *
	 * @return the guestzone kananname
	 */
	@ApiModelProperty(value="¢‘ã‹æ•ª", notes="¢‘ã‹æ•ª")
	public String getGuestgen() {
		return guestgen;
	}

	/**
	 * Sets the guest gen of the column.
	 *
	 * @param guestgenToSet
	 *            the new guestzone kananame
	 */
	public void setGuestgen(String guestgen) {
		this.guestgen = guestgen;
	}

	/**
	 * Gets the guest sex .
	 *
	 * @return the guest sex
	 */
	@ApiModelProperty(value="«•Ê‹æ•ª", notes="«•Ê‹æ•ª")
	public String getGuestsex() {
		return guestsex;
	}

	/**
	 * Sets the guest sex of the column.
	 *
	 * @param guestsexToSet
	 *            the new guest sex
	 */
	public void setGuestsex(String guestsex) {
		this.guestsex = guestsex;
	}

	/**
	 * Gets the guest type.
	 *
	 * @return the guest type
	 */
	@ApiModelProperty(value="‚»‚Ì‘¼‹æ•ª", notes="‚»‚Ì‘¼‹æ•ª")
	public String getGuesttype() {
		return guesttype;
	}

	/**
	 * Sets the guest type of the column.
	 *
	 * @param guesttypeToSet
	 *            the new guest type
	 */
	public void setGuesttype(String guesttype) {
		this.guesttype = guesttype;
	}

	/**
	 * Gets the subcode.
	 *
	 * @return the subcode
	 */
	@ApiModelProperty(value="ƒTƒuƒR[ƒh", notes="ƒTƒuƒR[ƒh")
	public String getSubcode() {
		return subcode;
	}

	/**
	 * Sets the subcode of the column.
	 *
	 * @param subcodeToSet
	 *            the new subcode
	 */
	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";

		if(null != this.guestcode){
			sb.append("GuestCode: ").append(this.guestcode.toString());
		}

		if(null != this.guestzonename){
			sb.append(crlf).append("GuestZoneName: ").append(this.guestzonename.toString());
		}

		if(null != this.guestzonekananame){
			sb.append(crlf).append("GuestZoneKanaName: ").append(this.guestzonekananame.toString());
		}

		if(null != this.guestgen){
			sb.append(crlf).append("GuestGen: ").append(this.guestgen.toString());
		}

		if(null != this.guestsex){
			sb.append(crlf).append("GuestSex: ").append(this.guestsex.toString());
		}

		if(null != this.guesttype){
			sb.append(crlf).append("GuestType: ").append(this.guesttype.toString());
		}

		if(null != this.subcode){
			sb.append(crlf).append("SubCode: ").append(this.subcode.toString());
		}

		return sb.toString();
	}


}
