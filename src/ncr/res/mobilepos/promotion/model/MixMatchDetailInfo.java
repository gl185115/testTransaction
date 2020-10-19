package ncr.res.mobilepos.promotion.model;

import java.util.List;

import ncr.res.mobilepos.pricing.model.PriceMMInfo;

public class MixMatchDetailInfo extends RuleEntryInfo implements Comparable<MixMatchDetailInfo> {
    private String mmNo;
    
    private List<PriceMMInfo> priceMMInfoList;
    
	public MixMatchDetailInfo() {
        super();
    }

    public String getMmNo() {
        return mmNo;
    }

    public void setMmNo(String mmNo) {
        this.mmNo = mmNo;
    }



    public List<PriceMMInfo> getPriceMMInfoList() {
		return priceMMInfoList;
	}

	public void setPriceMMInfoList(List<PriceMMInfo> priceMMInfoList) {
		this.priceMMInfoList = priceMMInfoList;
	}

	@Override
    public int compareTo(MixMatchDetailInfo o) {
        return new Double(o.getTruePrice()).compareTo(this.getTruePrice());
    }

}