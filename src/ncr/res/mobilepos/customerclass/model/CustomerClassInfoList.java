package ncr.res.mobilepos.customerclass.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CustomerClassInfoList")
public class CustomerClassInfoList extends ResultBase {
	@XmlElement(name = "CustomerClassInfoList")
	private List<CustomerClassInfo> customerClassInfoList;

	public final List<CustomerClassInfo> getCustomerClassInfoList() {
		return customerClassInfoList;
	}

	public final void setCustomerClassInfoList(List<CustomerClassInfo> customerClassInfoList) {
		this.customerClassInfoList = customerClassInfoList;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; ");
		sb.append("CustomerClassInfoList: ").append(customerClassInfoList).append("; ");
		return sb.toString();
	}
}
