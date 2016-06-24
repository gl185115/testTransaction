package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * @author 
 * 
 * Reservation Class is a Model representation of the Reservation information List.
 */
@XmlRootElement(name = "Reservation")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ SearchGuestOrderInfo.class })
@ApiModel(value="Reservation")
public class Reservation extends ResultBase {
    
	/*
	 * This list is used to save Reservation information
	 * 
	 */
    @XmlElementWrapper(name = "Reservations")
    @XmlElementRef()
    private List<ReservationInfo> ReservationInfo = null;
    

    /**
     * the Line
     */
    @XmlElement(name = "MaxLine")
    private String maxLine;

    
	/**
	 * @return the reservationInfo
	 */
    
    @ApiModelProperty(value="予約情報", notes="予約情報")
	public List<ReservationInfo> getReservationInfo() {
		return ReservationInfo;
	}

	/**
	 * @param reservationInfo the reservationInfo to set
	 */
	public void setReservationInfo(List<ReservationInfo> reservationInfo) {
		ReservationInfo = reservationInfo;
	}

	

	/**
	 * @return the maxLine
	 */
	 @ApiModelProperty(value="マックス・ライン", notes="マックス・ライン")
	public String getMaxLine() {
		return maxLine;
	}

	/**
	 * @param maxLine the maxLine to set
	 */
	public void setMaxLine(String maxLine) {
		this.maxLine = maxLine;
	}

	@Override
    public final String toString() {
    	
    	StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
    	
		if(null != this.ReservationInfo){
			sb.append(crlf).append("ReservationInfo: ").append(this.ReservationInfo.toString());
		}
		
			sb.append(crlf).append("maxLine: ").append(this.maxLine.toString());
		
    	return sb.toString();
    }
}
