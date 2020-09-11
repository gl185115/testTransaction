/**
 * 
 */
package ncr.res.mobilepos.promotion.model;

import java.util.List;

/**
 * @author hxd
 *
 */
public class ItemForPm {
	
	private int quantity;
	
	private double salePrice;
	
	private String itemEntryId;
	
	private String itemId;
	
	private Integer taxId;

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public List<PmItemInfo> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<PmItemInfo> keyList) {
		this.keyList = keyList;
	}

	private List<PmItemInfo> keyList;
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getItemEntryId() {
		return itemEntryId;
	}

	public void setItemEntryId(String itemEntryId) {
		this.itemEntryId = itemEntryId;
	}
}
