package ncr.res.mobilepos.uiconfig.model.schedule;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "schedule")
public class Schedule {
    @XmlElement(name = "deploy")
    private List<Deploy> deploy;

    public List<Deploy> getDeploy() {
        return deploy;
    }

    public void setDeploy(List<Deploy> deploy) {
        this.deploy = deploy;
    }

    /**
     * Convert to string.
     *
     * @return String
     */
    @Override
    public final String toString() {
        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }

    /**
     * Returns a deploy for specific companyID..
     *
     * @param companyID companyID to filter deploys.
     * @return deploys which has given companyID.
     */
    public Deploy getCompanyDeploy(String companyID) {
        for (Deploy element : this.deploy) {
            if (element.getCompany().getId().equalsIgnoreCase(companyID)) {
                return element;
            }
        }
        return null;
    }

}
