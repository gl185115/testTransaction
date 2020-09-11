/**
 * 
 */
package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * @author hxd
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PmItemInfo")
@ApiModel(value="PmItemInfo")
public class PmItemInfo extends PMInfo {
	

	@XmlElement(name = "PmSalePrice")
	private double salePrice;
	
    @XmlElement(name = "PMItemEntryID")
    private String itemEntryId;
    
    @XmlElement(name = "PmItemIDType")
    private String itemIdType;
    
    @XmlElement(name = "PmQuantity")
    private int quantity;
    
	@XmlElement(name = "TotalSalePrice")
	private double totalSalePrice;
	
	public String getItemId() {
		return itemId;
	}

	private String itemId;
    
//	private List<String> keyList;


//	public Sale getSale() {
//		return sale;
//	}

	public double getTotalSalePrice() {
		return totalSalePrice;
	}

	public void setTotalSalePrice(double totalSalePrice) {
		this.totalSalePrice = totalSalePrice;
	}

	public void setSale(Sale sale) {
		if(sale.getActualSalesUnitPrice() > 0) {
			this.salePrice = sale.getActualSalesUnitPrice();
		}
		this.itemEntryId = sale.getItemEntryId();
		if(sale.getQuantity() > 0) {
			this.quantity = sale.getQuantity();
		}
		this.itemIdType = sale.getItemIdType();
		this.itemId = sale.getItemId();
	}

	public double getSalePrice() {
		return salePrice;
	}

	public String getItemEntryId() {
		return itemEntryId;
	}

	public void setItemEntryId(String itemEntryId) {
		this.itemEntryId = itemEntryId;
	}

	public String getItemIdType() {
		return itemIdType;
	}

	public void setItemIdType(String itemIdType) {
		this.itemIdType = itemIdType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

//	public List<String> getKeyList() {
//		return keyList;
//	}
//
//	public void setKeyList(List<String> keyList) {
//		this.keyList = keyList;
//	}
	

}
