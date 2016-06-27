package ncr.res.mobilepos.systemsetting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * A model class that represents the current Date and Time of the server.
 * @author RD185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DateSettings")
@ApiModel(value="DateTime")
public class DateTime extends ResultBase {
    /**
     * Represents  the server's current date and time.
     */
    @XmlElement(name = "DateTime")
    private String currentDateTime;

    /**
     * @return the currentDateTime
     */
    @ApiModelProperty( value="汎用日付", notes="汎用日付")
    public final String getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * @param currentDateTimeToSet the currentDateTime to set
     */
    public final void setCurrentDateTime(final String currentDateTimeToSet) {
        this.currentDateTime = currentDateTimeToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("DateTime: " + currentDateTime);

        return sb.toString();
    }
}
