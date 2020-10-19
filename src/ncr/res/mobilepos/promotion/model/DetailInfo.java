package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;


public class DetailInfo {
    private int times = 0;
    /** ê¨óßâøäi*/
    private double conditionPrice = 0; 
    /**ìñëOê¨óßå„âøäi*/
    private double currentDecisionPrice = 0;
    
    /**ê¨óßêî*/
    private int conditionCount = 0;
    
    /**ïΩãœâøäi*/
    private double averagePrice = 0;
    
    /**ê¨óßå„äÑà¯ó¶1*/
    private double currentDecisionPriceRate = 0;
    
    /**ê¨óßäÑà¯ó¶1*/
    private double currentConditionPriceRate = 0;
    
    public double getCurrentDecisionPriceRate() {
		return currentDecisionPriceRate;
	}
	public void setCurrentDecisionPriceRate(double currentDecisionPriceRate) {
		this.currentDecisionPriceRate = currentDecisionPriceRate;
	}
	public double getCurrentConditionPriceRate() {
		return currentConditionPriceRate;
	}
	public void setCurrentConditionPriceRate(double currentConditionPriceRate) {
		this.currentConditionPriceRate = currentConditionPriceRate;
	}
    public double getAveragePrice() {
        return averagePrice;
    }
    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
    public int getConditionCount() {
        return conditionCount;
    }
    public void setConditionCount(int conditionCount) {
        this.conditionCount = conditionCount;
    }
    public double getCurrentDecisionPrice() {
        return currentDecisionPrice;
    }
    public void setCurrentDecisionPrice(double currentDecisionPrice) {
        this.currentDecisionPrice = currentDecisionPrice;
    }
    private List<MixMatchDetailInfo> entryList;
    
    
    public List<MixMatchDetailInfo> getEntryList() {
        return entryList;
    }
    public void setEntryList(List<MixMatchDetailInfo> entryList) {
        this.entryList = entryList;
    }
    public double getConditionPrice() {
        return conditionPrice;
    }
    public void setConditionPrice(double conditionPrice) {
        this.conditionPrice = conditionPrice;
    }
    public DetailInfo(){
        entryList = new ArrayList<MixMatchDetailInfo>();
    }
    public int getTimes() {
        return times;
    }
    public void setTimes(int times) {
        this.times = times;
    }
    
    public void addEntryList(List<MixMatchDetailInfo> list){
        if(entryList.size() == 0){
            for(MixMatchDetailInfo info : list){
                entryList.add(info);
            }
        }else{
            List<MixMatchDetailInfo> removeList = new ArrayList<MixMatchDetailInfo>();
            for(MixMatchDetailInfo info : list){
                for(MixMatchDetailInfo info1 : entryList){
                    if(info.getEntryId().equals(info1.getEntryId())){
                        removeList.add(info1);
                        int quantity = info.getQuantity();
                        String entryId = info.getEntryId();
                        double price = info.getTruePrice();
                        String sku = info.getSku();
                        info = new MixMatchDetailInfo();
                        info.setEntryId(entryId);
                        info.setQuantity(quantity + info1.getQuantity());
                        info.setTruePrice(price);
                        info.setSku(sku);
                    }
                }
                entryList.add(info);
                entryList.removeAll(removeList);
            }
        }
        
    }

}
