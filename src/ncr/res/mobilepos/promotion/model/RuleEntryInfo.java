package ncr.res.mobilepos.promotion.model;

public class RuleEntryInfo {
    private String entryId;
    private int quantity;
    private double truePrice;
    private int taxId;
    private String originalTaxId;
    public String getEntryId() {
        return entryId;
    }
    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTruePrice() {
        return truePrice;
    }
    public void setTruePrice(double truePrice) {
        this.truePrice = truePrice;
    }
    public int getTaxId() {
        return taxId;
    }
    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }
    public String getOriginalTaxId() {
        return originalTaxId;
    }
    public void setOriginalTaxId(String originalTaxId) {
        this.originalTaxId = originalTaxId;
    }

}
