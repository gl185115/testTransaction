package ncr.res.mobilepos.systemconfiguration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlRootElement(name = "SystemConfigInfo")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="SystemConfigInfo")
public class SystemConfigInfo{
	/** The Category. */
	@XmlElement(name = "Category")
	private String category;

	/** The Key Id. */
	@XmlElement(name = "KeyId")
	private String keyId;

	/** The Value. */
	@XmlElement(name = "Value")
	private String value;

	/**
     * @return the category
     */
    public final String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public final void setCategory(final String category) {
        this.category = category;
    }

    /**
     * @return the keyId
     */
    public final String getKeyId() {
        return keyId;
    }

    /**
     * @param keyId the keyId to set
     */
    public final void setKeyId(final String keyId) {
        this.keyId = keyId;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public final void setValue(final String value) {
        this.value = value;
    }

}
