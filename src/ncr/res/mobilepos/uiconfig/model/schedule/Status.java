package ncr.res.mobilepos.uiconfig.model.schedule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "status")
public class Status {

    @XmlElement(name = "total")
    private String total = "0";

    @XmlElement(name = "ready")
    private String ready = "0";

    @XmlElement(name = "opened")
    private String opened = "0";

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }
}
