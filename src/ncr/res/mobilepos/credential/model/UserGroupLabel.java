package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * holds the data for a User Group.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GroupLabel")
public class UserGroupLabel {
    /**
     * The GroupCode.
     */
    @XmlElement(name = "GroupCode")
    private int groupcode;

    /**
     * gets the GroupCode.
     *
     * @return GroupCode
     */
    public final int getGroupCode() {
        return groupcode;
    }

    /**
     * sets the GroupCode.
     *
     * @param groupCode
     *            - the GroupCode
     */
    public final void setGroupCode(final int groupCode) {
        this.groupcode = groupCode;
    }

    /**
     * The GroupName.
     */
    @XmlElement(name = "GroupName")
    private String groupname;

    /**
     * gets the GroupName.
     *
     * @return GroupName
     */
    public final String getGroupName() {
        return groupname;
    }

    /**
     * sets the GroupName.
     *
     * @param groupName
     *            - the groupName
     */
    public final void setGroupName(final String groupName) {
        this.groupname = groupName;
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
