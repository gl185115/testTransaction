package ncr.res.mobilepos.systemconfiguration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "member")
@ApiModel(value="member")
public class Member {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> members = new ArrayList<ItemSale>();

    public List<ItemSale> getMembers() {
        return members;
    }

    public void setMembers(List<ItemSale> members) {
        this.members = members;
    }
}
