package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * {@link StoreLogo} Class holds information of the Customer Store Logo.
 *
 * @author CC185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoreLogo")
public class StoreLogo {
    /** The LOGO image in Base64 string format.  */
    @XmlElement(name = "Image")
    private String image;

    /**
     * @return the image
     */
    public final String getImage() {
        return image;
    }

    /**
     * @param imageToSet the image to set
     */
    public final void setImage(final String imageToSet) {
        this.image = imageToSet;
    }

    /**
     * Convert to string.
     * @return String
     */
    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
