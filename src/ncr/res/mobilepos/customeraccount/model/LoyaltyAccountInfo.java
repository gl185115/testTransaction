package ncr.res.mobilepos.customeraccount.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "LoyaltyAccountInfo")
@ApiModel(value = "LoyaltyAccountInfo")
public class LoyaltyAccountInfo extends ResultBase {

	/**
	 * LoyaltyAccountList
	 */
	@XmlElement(name = "LoyaltyAccountList")
	private List<LoyaltyAccount> LoyaltyAccountList = new ArrayList<LoyaltyAccount>();

	/**
	 * 
	 * @return LoyaltyAccountList
	 */
	public List<LoyaltyAccount> getLoyaltyAccountList() {
		return LoyaltyAccountList;
	}

	/**
	 * 
	 * @param loyaltyAccountList
	 */
	public void setLoyaltyAccountList(List<LoyaltyAccount> loyaltyAccountList) {
		LoyaltyAccountList = loyaltyAccountList;
	}

	@Override
	public String toString() {
		StringBuffer rs = new StringBuffer();
		if (LoyaltyAccountList != null && LoyaltyAccountList.size() > 0) {

			for (LoyaltyAccount loyaltyAccount : LoyaltyAccountList) {
				rs.append(loyaltyAccount.toString());
			}
		}
		return rs.toString();
	}
}
