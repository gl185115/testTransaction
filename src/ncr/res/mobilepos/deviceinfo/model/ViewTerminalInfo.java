package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ViewTerminalInfo")
@ApiModel(value="ViewTerminalInfo")
public class ViewTerminalInfo extends ResultBase {
	@XmlElement(name = "TerminalInfo")
	private TerminalInfo terminalInfo;
	
    public void setTerminalInfo(TerminalInfo terminalInfo) {
    	this.terminalInfo = terminalInfo;
    }
    
    @ApiModelProperty(value="í[ññèÓïÒ", notes="í[ññèÓïÒ")
    public TerminalInfo getTerminalInfo() {
    	return terminalInfo;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("TerminalInfo: ").append(terminalInfo).append("; ");
        return sb.toString();
    }
}
