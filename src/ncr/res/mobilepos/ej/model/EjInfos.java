package ncr.res.mobilepos.ej.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "EjInfos")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="EjInfos")
public class EjInfos extends ResultBase{

	@XmlElement(name = "EjList")
	private List<EjInfo> ejList;

	public List<EjInfo> getEjList() {
		return ejList;
	}

	public void setEjList(List<EjInfo> ejList) {
		this.ejList = ejList;
	}
    
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
