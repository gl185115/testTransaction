package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.model.ResultBase;
/**
 * @author 
 * 
 * Sales SearchGuestOrder Class is a Model representation of the guest order List.
 */
@XmlRootElement(name = "EventList")
@XmlAccessorType(XmlAccessType.NONE)
public class EventList extends ResultBase {
    
	/*
	 * This list is used to save event information
	 * 
	 */
	@XmlElementWrapper(name = "EventDetails")
    @XmlElementRef()
	private List<EventDetail> eventDetails;
	 
	
	/**
	 * @return the eventDetails
	 */
	public List<EventDetail> getEventDetails() {
		return eventDetails;
	}


	/**
	 * @param eventDetails the eventDetails to set
	 */
	public void setEventDetails(List<EventDetail> eventDetails) {
		this.eventDetails = eventDetails;
	}


	@Override
    public final String toString() {
    	
    	StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
    	
		if(null != this.eventDetails){
			sb.append(crlf).append("eventDetails: ").append(this.eventDetails.toString());
		}
		
    	return sb.toString();
    }
}
