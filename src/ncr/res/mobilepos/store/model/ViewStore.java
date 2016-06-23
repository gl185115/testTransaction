package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Store JSON data for view store.
 */
@XmlRootElement(name = "ViewStore")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ViewStore")
public class ViewStore extends ResultBase {
    /**
     * Store instance. Holds store data.
     */
    @XmlElement(name = "Store")
    private Store store = null;

    /**
     * The Store Logo.
     */
    @XmlElement(name = "StoreLogo")
    private StoreLogo logo;

    /**
     * Sets store.
     * @param storeObj The Store instance.
     */
    public final void setStore(final Store storeObj) {
        this.store = storeObj;
    }

    /**
     * Gets store.
     * @return store The Store instance.
     */
    @ApiModelProperty(value="ìXï‹", notes="ìXï‹")
    public final Store getStore() {
        return store;
    }

    /**
     * @return the logo
     */
    @ApiModelProperty(value="ÉçÉS", notes="ÉçÉS")
    public final StoreLogo getLogo() {
        return logo;
    }

    /**
     * @param logoToSet the logo to set
     */
    public final void setLogo(final StoreLogo logoToSet) {
        this.logo = logoToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Store: ").append(store).append("; ");
        return sb.toString();
    }
}
