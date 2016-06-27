package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "EventInformation")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="EventInformation")
public class EventInformation extends ResultBase{
    /**
     * EventDatial instance. Contains event detail.
     */
    @XmlElement(name = "EventDetail")
    private EventDetail eventDetial = null;

    /**
     * @return the eventDetial
     */
    @ApiModelProperty(value="イベントの詳細を得てください", notes="イベントの詳細を得てください")
    public EventDetail getEventDetial() {
        return eventDetial;
    }

    /**
     * @param eventDetial the eventDetial to set
     */
    public void setEventDetial(EventDetail eventDetial) {
        this.eventDetial = eventDetial;
    }

    @Override
    public final String toString() {

        StringBuilder sb = new StringBuilder();
        String crlf = "\r\n";
        sb.append(super.toString());

        if(null != this.eventDetial){
            sb.append(crlf).append("eventDetial: ").append(this.eventDetial.toString());
        }

        return sb.toString();
    }

}
