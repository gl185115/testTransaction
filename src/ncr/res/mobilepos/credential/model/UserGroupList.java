package ncr.res.mobilepos.credential.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * holds a list of user groups.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "UserGroups")
@XmlSeeAlso({ UserGroupLabel.class })
@ApiModel(value="UserGroupList")
public class UserGroupList extends ResultBase {

    /**
     * The list of groups.
     */
    @XmlElement(name = "GroupsList")
    @XmlElementRef()
    private List<UserGroupLabel> userGroups;

    /**
     * sets the list of groups.
     * @param listToSet - the list of groups to set.
     */
    public final void setGroupList(final List<UserGroupLabel> listToSet) {
        this.userGroups = listToSet;
    }

    /**
     * gets the list of user groups.
     * @return list of user groups
     */
    @ApiModelProperty(value="ユーザグループリスト", notes="ユーザグループリスト")
    public final List<UserGroupLabel> getGroupList() {
        return this.userGroups;
    }
}
