/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

/**
 * PaperReceiptFooter Class is a Model representation of the
 * information which identifies the footer of paper receipt.
 */
public class PaperReceiptFooter {
    /**
     * shop name.
     */
    private String shopName;
    /**
     * terminal number.
     */
    private String registerNum;
    /**
     * department name.
     */
    private String departmentName;
    /**
     * trade number.
     */
    private String tradeNum;
    /**
     * operator.
     */
    private String saleMan;
    /**
     * hold name.
     */
    private String holdName;

    /**
     * default constructor.
     */
    public PaperReceiptFooter() {

    }

    /**
     * constructor.
     * @param shopNameToSet - name of the shop
     * @param registerNumToSet - the terminal number
     * @param departmentNameToSet - department name
     * @param tradeNumToSet - trade number
     * @param saleManToSet - operator name/number
     * @param holdNameToSet - hold name
     */
    public PaperReceiptFooter(final String shopNameToSet,
                            final String registerNumToSet,
                            final String departmentNameToSet,
                            final String tradeNumToSet,
                            final String saleManToSet,
                            final String holdNameToSet) {
        this.shopName = shopNameToSet;
        this.registerNum = registerNumToSet;
        this.departmentName  = departmentNameToSet;
        this.tradeNum = tradeNumToSet;
        this.saleMan = saleManToSet;
        this.holdName = holdNameToSet;
    }

    /**
     * @return shopName
     */
    public final String getShopName() {
        return shopName;
    }

    /**
     * @param shopNameToSet  shopName
     */
    public final void setShopName(final String shopNameToSet) {
        this.shopName = shopNameToSet;
    }

    /**
     * @return registerNum
     */
    public final String getRegisterNum() {
        return registerNum;
    }

    /**
     * @param registerNumToSet  registerNum
     */
    public final void setRegisterNum(final String registerNumToSet) {
        this.registerNum = registerNumToSet;
    }

    /**
     * @return departmentName
     */
    public final String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentNameToSet  departmentName
     */
    public final void setDepartmentName(final String departmentNameToSet) {
        this.departmentName = departmentNameToSet;
    }

    /**
     * @return tradeNum
     */
    public final String getTradeNum() {
        return tradeNum;
    }

    /**
     * @param tradeNumToSet  tradeNum
     */
    public final void setTradeNum(final String tradeNumToSet) {
        this.tradeNum = tradeNumToSet;
    }

    /**
     * @return saleMan
     */
    public final String getSaleMan() {
        return saleMan;
    }

    /**
     * @param saleManToSet  saleMan
     */
    public final void setSaleMan(final String saleManToSet) {
        this.saleMan = saleManToSet;
    }

    /**
     * @return holdName
     */
    public final String getHoldName() {
        return holdName;
    }

    /**
     * @param holdNameToSet  holdName
     */
    public final void setHoldName(final String holdNameToSet) {
        this.holdName = holdNameToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Department Name=" + departmentName);
        sb.append("\nHold Name=" + holdName);
        sb.append("\nRegister Number=" + registerNum);
        sb.append("\nSale Man=" + saleMan);
        sb.append("\nShop Name=" + shopName);
        sb.append("\nTrade Number=" + tradeNum);
        return sb.toString();
    }
}
