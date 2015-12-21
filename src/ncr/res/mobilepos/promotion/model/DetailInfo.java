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
                        info = new MixMatchDetailInfo();
                        info.setEntryId(entryId);
                        info.setQuantity(quantity + info1.getQuantity());
                        info.setTruePrice(price);
                    }
                }
                entryList.add(info);
                entryList.removeAll(removeList);
            }
        }
        
    }

}
