package ncr.res.mobilepos.systemsetting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * A model Class that represents the Date Settings configured
 * in the Web Store Server.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DateSettings")
@ApiModel(value="DateSetting")
public class DateSetting {
    /**
     * Represents the Today for Date Setting.
     */
    @XmlElement(name = "Today")
    private String today;
    /**
     * Represents the End of Day for Date Setting.
     */
    @XmlElement(name = "EndOfDay")
    private String eod;
    /**
     * Represents the number of skips.
     */
    @XmlElement(name = "Skips")
    private int skips;

    /**
     * Getter for Today for Date Setting.
     * @return  Current date.
     */
    @ApiModelProperty( value="今日", notes="今日")
    public final String getToday() {
        return today;
    }
    /**
     * Setter for Today for Date Setting.
     * @param todayToSet    Date to set.
     */
    public final void setToday(final String todayToSet) {
        this.today = todayToSet;
    }
    /**
     * Getter for the End od Day.
     * @return  end of day
     */
    @ApiModelProperty( value="一日の終わる", notes="一日の終わる")
    public final String getEod() {
        return eod;
    }
    /**
     * Setter for the End od Day.
     * @param eodToSet  End of day to set
     */
    public final void setEod(final String eodToSet) {
        this.eod = eodToSet;
    }
    /**
     * Getter for number of skips.
     * @return  number of skips
     */
    @ApiModelProperty( value="スキップ", notes="スキップ")
    public final int getSkips() {
        return skips;
    }
    /**
     * Setter for number of skips.
     * @param skipsToSet    Number of skips
     */
    public final void setSkips(final int skipsToSet) {
        this.skips = skipsToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("eod:" + eod);
        sb.append("\nskips:" + skips);
        sb.append("\ntoday:" + today);
        return sb.toString();
    }
}
