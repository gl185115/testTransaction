package ncr.res.mobilepos.uiconfig.model.schedule;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "task")
public class Task {

    @XmlElement(name = "status")
    private Status status;

    @XmlElement(name = "target")
    private Target target;

    @XmlElement(name = "effective")
    private String effective;

    @XmlElement(name = "filename")
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Convert to string.
     *
     * @return String
     */
    @Override
    public final String toString() {
        ObjectWriter mapper = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }

    private static final SimpleDateFormat EFFECTIVE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    /**
     * Returns effective in Date.
     *
     * @return null if the date is empty or has invalid format.
     */
    private Date getEffectiveDate() {
        if (effective == null) {
            return null;
        }
        try {
            return EFFECTIVE_DATE_FORMAT.parse(this.effective);
        } catch (ParseException e) {
            // Effective date has invalid format, then falls through and returns null.
        }
        return null;
    }

    /**
     * Returns if the task is effective.
     *
     * @return true: effective, false: ineffective.
     */
    public boolean isEffective() {
        Date effectiveDate = this.getEffectiveDate();
        if (effectiveDate == null) {
            return false;
        }
        // Compares with current date.
        return new Date().after(effectiveDate);
    }

    /**
     * Comparator for sorting by effective date.
     */
    public static final Comparator<Task> TaskEffectiveDateComparator = new Comparator<Task>() {
        public int compare(Task o1, Task o2) {
            Date date1 = o1.getEffectiveDate();
            Date date2 = o2.getEffectiveDate();
            if (date1 == null && date2 == null) {
                return 0;
            }
            if (date1 == null) {
                return 1;
            }
            if (date2 == null) {
                return -1;
            }
            return date2.compareTo(date1);
        }
    };
}
