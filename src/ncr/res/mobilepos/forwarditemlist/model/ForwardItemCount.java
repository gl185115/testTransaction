package ncr.res.mobilepos.forwarditemlist.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ForwardItemCount")
@ApiModel(value="ForwardItemCount")
public class ForwardItemCount extends ResultBase {

    @XmlElement(name = "Count")
    private String count;

    /**
     * @return the count
     */
    @ApiModelProperty( value="前捌きデータ数", notes="前捌きデータ数")
    public final String getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public final void setCount(String count) {
        this.count = count;
    }

}
