package ncr.res.mobilepos.uiconfig.model.schedule;

import ncr.res.mobilepos.uiconfig.model.UiConfigType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "deploy")
public class Deploy {
    @XmlAttribute(name = "ID")
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    @XmlElement(name = "company")
    private Company company;
    @XmlElement(name = "config")
    private List<Config> config;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Config> getConfig() {
        return config;
    }

    public void setConfig(List<Config> config) {
        this.config = config;
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


    public final Config getResourceConfig(UiConfigType configType) {
        for (Config element : this.config) {
            if (configType.toString().equalsIgnoreCase(element.getResource())) {
                return element;
            }
        }
        return null;
    }

}
