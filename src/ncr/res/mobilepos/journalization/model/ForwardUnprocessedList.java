package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "ForwardUnprocessedList")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardUnprocessedList")
public class ForwardUnprocessedList extends ResultBase {

    @XmlElementWrapper(name = "ForwardListInfo")
    @XmlElementRef()
    private List<ForwardUnprocessedListInfo> ForwardUnprocessedListInfo;
    @ApiModelProperty(value="順リスト情報", notes="順リスト情報")
    public List<ForwardUnprocessedListInfo> getForwardUnprocessedListInfo() {
        return ForwardUnprocessedListInfo;
    }

    public void setForwardUnprocessedListInfo(List<ForwardUnprocessedListInfo> forwardUnprocessedList) {
    	ForwardUnprocessedListInfo = forwardUnprocessedList;
    }

    @Override
    public final String toString() {

        StringBuilder sb = new StringBuilder();
        String crlf = "\r\n";

        if(null != this.ForwardUnprocessedListInfo){
            sb.append(crlf).append("ForwardListInfo: ").append(this.ForwardUnprocessedListInfo.toString());
        }
        return sb.toString();
    }
}
