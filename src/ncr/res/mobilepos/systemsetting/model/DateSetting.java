package ncr.res.mobilepos.systemsetting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A model Class that represents the Date Settings configured
 * in the Web Store Server.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DateSettings")
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
