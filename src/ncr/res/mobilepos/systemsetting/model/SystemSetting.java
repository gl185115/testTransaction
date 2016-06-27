package ncr.res.mobilepos.systemsetting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * A model Class that represents the System Settings
 * configured in the Web Store Server.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SystemSetting")
@ApiModel(value="SystemSetting")
public class SystemSetting extends ResultBase {
    /**
     * Date Setting.
     */
    @XmlElement(name = "DateSettings")
    private DateSetting dateSetting;

    /**
     * Getter for Date Setting.
     * @return  date setting
     */
    @ApiModelProperty( value="���t�ݒ�", notes="���t�ݒ�")
    public final DateSetting getDateSetting() {
        return dateSetting;
    }

    /**
     * Setter for Date Setting.
     * @param dateSettingToSet      Setting of Date to set.
     */
    public final void setDateSetting(final DateSetting dateSettingToSet) {
        this.dateSetting = dateSettingToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (dateSetting != null) {
            sb.append(dateSetting.toString());
        }
        return sb.toString();
    }
}
