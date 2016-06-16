package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="EventDetail")
@ApiModel(value="EventDetail")
public class EventDetail{
	
	@XmlElement(name="EventId")
    private String eventId;
	
	@XmlElement(name="EventName")
    private String eventName;
	
	@XmlElement(name="StartDateId")
    private int startDateId;
	
	@XmlElement(name="EndDateId")
    private int endDateId;
	
	@XmlElement(name="MdInternal")
    private String mdInternal;
	
	@XmlElement(name="SalesPrice")
    private int salesPrice;
	
	@XmlElement(name="EventKbn")
    private int eventKbn;
	

	/**
	 * @return the eventId
	 */
	@ApiModelProperty(value="�C�x���g�R�[�h", notes="�C�x���g�R�[�h")
	public String getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventName
	 */
	 @ApiModelProperty(value="�C�x���g��", notes="�C�x���g��")
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	
	/**
	 * @return the startDateId
	 */
	@ApiModelProperty(value="�n�߃R�[�h", notes="�n�߃R�[�h")
	public int getStartDateId() {
		return startDateId;
	}

	/**
	 * @param startDateId the startDateId to set
	 */
	public void setStartDateId(int startDateId) {
		this.startDateId = startDateId;
	}

	/**
	 * @return the endDateId
	 */
	@ApiModelProperty(value="�I�ăR�[�h", notes="�I�ăR�[�h")
	public int getEndDateId() {
		return endDateId;
	}

	/**
	 * @param endDateId the endDateId to set
	 */
	public void setEndDateId(int endDateId) {
		this.endDateId = endDateId;
	}

	/**
	 * @return the mdInternal
	 */
	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
	public String getMdInternal() {
		return mdInternal;
	}

	/**
	 * @param mdInternal the mdInternal to set
	 */
	public void setMdInternal(String mdInternal) {
		this.mdInternal = mdInternal;
	}

	/**
	 * @return the salesPrice
	 */
	@ApiModelProperty(value="���P��", notes="���P��")
	public int getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice the salesPrice to set
	 */
	public void setSalesPrice(int salesPrice) {
		this.salesPrice = salesPrice;
	}
	

	/**
	 * @return the eventKbn
	 */
	@ApiModelProperty(value="�C�x���g�敪", notes="�C�x���g�敪")
	public int getEventKbn() {
		return eventKbn;
	}

	/**
	 * @param eventKbn the eventKbn to set
	 */
	public void setEventKbn(int eventKbn) {
		this.eventKbn = eventKbn;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		
		if(null != this.eventId){
			sb.append("eventId: ").append(this.eventId.toString());
		}
		
		if(null != this.eventName){
			sb.append(crlf).append("eventName: ").append(this.eventName.toString());
		}
		
		if(null != this.mdInternal){
			sb.append(crlf).append("mdInternal: ").append(this.mdInternal.toString());
		}
		
			sb.append(crlf).append("salesPrice: ").append(this.salesPrice)
			.append(crlf).append("eventKbn: ").append(this.eventKbn)
			.append(crlf).append("startDateId: ").append(this.startDateId)
			.append(crlf).append("endDateId: ").append(this.endDateId);
		
		return sb.toString();
	}

}
