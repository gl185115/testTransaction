package ncr.res.mobilepos.cashAbstract.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;
import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "DispensingCodeList")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(DispensingCode.class)
@ApiModel(value="DispensingCodeList")
public class DispensingCodeList extends ResultBase  {

    @XmlElementWrapper(name = "dispensingCodeList")
    @XmlElementRef
	private List<DispensingCode> dispensingCodeLists;

	public List<DispensingCode> getDispensingCodeLists() {
		return dispensingCodeLists;
	}

	public void setDispensingCodeLists(List<DispensingCode> dispensingCodeLists) {
		this.dispensingCodeLists = dispensingCodeLists;
	}

	@Override
	public String toString() {
		 StringBuilder sb = new StringBuilder();
	        sb.append(super.toString());
		return sb.toString();
	}
	
	
}
