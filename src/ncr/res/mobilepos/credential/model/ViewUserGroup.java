package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Contains User Group Details and Result Code.
 * @author RD185102
 *
 */
@XmlRootElement(name = "ViewUserGroup")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ViewUserGroup")
public class ViewUserGroup extends ResultBase {

    /**
     * UserGroup instance. Holds User Group Details.
     */
    @XmlElement(name = "Group")
    private UserGroup userGroup = null;

    /**
     * Sets UserGroup.
     * @param userGroupToSet User Group to set.
     */
    public final void setUserGroup(final UserGroup userGroupToSet) {
        this.userGroup = userGroupToSet;
    }

    /**
     * Getter for UserGroup.
     * @return  userGroup   UserGroup instance.
     */
    @ApiModelProperty(value="グループ情報", notes="グループ情報")
    public final UserGroup getUserGroup() {
        return userGroup;
    }
}
