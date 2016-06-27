package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "ForwardList")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardList")
public class ForwardList extends ResultBase {

    @XmlElementWrapper(name = "ForwardListInfo")
    @XmlElementRef()
    private List<ForwardListInfo> ForwardListInfo;
    @ApiModelProperty(value="�����X�g���", notes="�����X�g���")
    public List<ForwardListInfo> getForwardListInfo() {
        return ForwardListInfo;
    }

    public void setForwardListInfo(List<ForwardListInfo> forwardListInfo) {
        ForwardListInfo = forwardListInfo;
    }

    @Override
    public final String toString() {

        StringBuilder sb = new StringBuilder();
        String crlf = "\r\n";

        if(null != this.ForwardListInfo){
            sb.append(crlf).append("ForwardListInfo: ").append(this.ForwardListInfo.toString());
        }
        return sb.toString();
    }
}
