package ncr.res.mobilepos.promotion.model;


public class MixMatchDetailInfo extends RuleEntryInfo implements Comparable<MixMatchDetailInfo> {
//    private String entryId;
//    private double price;
//    private int quantity;
    private String mmNo;

    public MixMatchDetailInfo() {
        super();
    }

    public String getMmNo() {
        return mmNo;
    }

    public void setMmNo(String mmNo) {
        this.mmNo = mmNo;
    }

//    public String getEntryId() {
//        return entryId;
//    }
//
//    public void setEntryId(String entryId) {
//        this.entryId = entryId;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }

    @Override
    public int compareTo(MixMatchDetailInfo o) {
        return new Double(o.getTruePrice()).compareTo(this.getTruePrice());
    }

}