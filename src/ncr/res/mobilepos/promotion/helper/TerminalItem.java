package ncr.res.mobilepos.promotion.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.model.CouponInfo;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.promotion.dao.IMixMatchDAO;
import ncr.res.mobilepos.promotion.model.DetailInfo;
import ncr.res.mobilepos.promotion.model.GroupMixMatchData;
import ncr.res.mobilepos.promotion.model.GroupMixMatchItem;
import ncr.res.mobilepos.promotion.model.GroupMixMatchSubItem;
import ncr.res.mobilepos.promotion.model.MixMatchData;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.MixMatchItem;
import ncr.res.mobilepos.promotion.model.MixMatchItemFactory;
import ncr.res.mobilepos.promotion.model.MixMatchRule;
import ncr.res.mobilepos.promotion.model.Sale;

/**
 * TerminalItem class encapsulates the Mix and Match promotion
 * for every terminal in a current transaction.
 * One Terminal is to one TerminalItem.
 * @author cc185102
 *
 */
public class TerminalItem {
    /** The terminalID.
     *  It is in SSSSTTTT format.
     */
    private String id;
    /** The Mix and match List. */
    private List<MixMatchItem> mixMatchList;
    /** The ItemEntry map. */
    private Map<String, String> itemEntryMap;
    /** The Sequence Number. */
    private String sequenceNumber;
    /** The Transaction Mode. */
    private int transactionMode;
    /** The Operator ID. */
    private String operatorid;
    /** The Begin Date Time. */
    private String beginDateTime;
    /** The Debug Trace Printer. */
    private Trace.Printer tp;
    /** */
    private Map<String, MixMatchRule> bmRuleMap;
    
    private Map<String, List<MixMatchDetailInfo>> bmDetailMap;
    
    private Map<String, List<MixMatchDetailInfo>> bmMapBySku;
    
    private Map<String,Map<String, Object>> detailInfoMap;
    
    private List<QrCodeInfo> qrCodeInfoList;
    
    private Map<String,CouponInfo> couponInfoMap;
    
    /** The ItemEntry map. */
    private Map<String, String> itemIdToMixMatchMap;
    
    public Map<String, Map<String, Object>> getDetailInfoMap() {
        return detailInfoMap;
    }
    public void setDetailInfoMap(Map<String, Map<String, Object>> detailInfoMap) {
        this.detailInfoMap = detailInfoMap;
    }
    /** The Custom Constructor.
     *
     * @param retailStoreIDToSet  The Retail Store ID.
     * @param workstationIDToSet The Workstation ID.
     * @param sequenceNumberToSet The Sequence Number.
     */
    public TerminalItem(final String retailStoreIDToSet,
            final String workstationIDToSet,
            final String sequenceNumberToSet) {
        String terminaItemId = TerminalItemsHandler
                .constructTerminalItemId(retailStoreIDToSet, workstationIDToSet);
        this.id = terminaItemId;
        this.sequenceNumber = sequenceNumberToSet;
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
        itemEntryMap = new HashMap<String, String>();
        mixMatchList = new ArrayList<MixMatchItem>();
        bmRuleMap = new HashMap<String, MixMatchRule>();
        bmDetailMap = new HashMap<String, List<MixMatchDetailInfo>>();
        bmMapBySku = new HashMap<String, List<MixMatchDetailInfo>>();
        detailInfoMap = new HashMap<String,Map<String, Object>>();
        qrCodeInfoList = new ArrayList<QrCodeInfo>();
        couponInfoMap = new HashMap<String,CouponInfo>();
        itemIdToMixMatchMap = new HashMap<String, String>();
    }
    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }
    /**
     * @param idToSet the id to set
     */
    public final void setId(final String idToSet) {
        this.id = idToSet;
    }
    /**
     * @return the mixMatchList
     */
    public final List<MixMatchItem> getMixMatchList() {
        return mixMatchList;
    }
    /**
     * @param mixMatchListToSet the mixMatchList to set
     */
    public final void setMixMatchList(
            final List<MixMatchItem> mixMatchListToSet) {
        this.mixMatchList = mixMatchListToSet;
    }
    /**
     * @return the sequenceNumber
     */
    public final String getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * @param sequenceNumberToSet the sequenceNumber to set
     */
    public final void setSequenceNumber(final String sequenceNumberToSet) {
        this.sequenceNumber = sequenceNumberToSet;
    }
    /**
     * @return the transactionMode
     */
    public final int getTransactionMode() {
        return transactionMode;
    }
    /**
     * @param transactionModeToSet the transactionMode to set
     */
    public final void setTransactionMode(final int transactionModeToSet) {
        this.transactionMode = transactionModeToSet;
    }
    /**
     * @return the operatorid
     */
    public final String getOperatorid() {
        return operatorid;
    }
    /**
     * @param operatoridToSet the operatorid to set
     */
    public final void setOperatorid(final String operatoridToSet) {
        this.operatorid = operatoridToSet;
    }
    /**
     * @return the beginDateTime
     */
    public final String getBeginDateTime() {
        return beginDateTime;
    }
    /**
     * @param beginDateTimeToSet the beginDateTime to set
     */
    public final void setBeginDateTime(final String beginDateTimeToSet) {
        this.beginDateTime = beginDateTimeToSet;
    }

    /**
     * Get an existing MixMatchItem in The TerminalItem.
     * @param mixMatchcode The Mix and Match Code.
     * @return The {@link MixMatchItem}
     */
    public final MixMatchItem getMixMatchItem(final String mixMatchcode) {
        if (mixMatchcode == null) {
            return null;
        }

        for (MixMatchItem mixMatchItem : mixMatchList) {
            if (mixMatchcode.equals(mixMatchItem.getCode())) {
                return mixMatchItem;
            } else if (mixMatchItem instanceof GroupMixMatchItem) {
                for (MixMatchItem groupMixMatchSubItem
                        : ((GroupMixMatchItem) mixMatchItem).getSubItems()) {
                    if (mixMatchcode.equals(groupMixMatchSubItem.getCode())) {
                        return mixMatchItem;
                    }
                }
            }
        }

        return null;
    }
    /**
     * Add a Mix and Match Item.
     * @param mixMatchItem the Mix and Match Item to Add.
     */
    public final void addMixMatchItem(final MixMatchItem mixMatchItem) {
        mixMatchList.add(mixMatchItem);
    }
    /**
     * Add the item entry to the item entry map.
     * @param retailStoreId the Store indentifier for
     *                      retrieving mix match data.
     * @param itemToAdd the item to be added.
     * @param mixMatchCode the item's mixmatch code.
     * @return MixMatchItem
     * @throws Exception exception
     */
    public final MixMatchItem addItem(
            final String companyId,
            final String retailStoreId,
            final Sale itemToAdd,
            final String mixMatchCode) throws Exception {
        tp.methodEnter("addItem")
            .println("RetailStoreId", retailStoreId)
            .println("ItemToAdd", itemToAdd)
            .println("MixMatchCode", mixMatchCode);

        MixMatchItem mixMatchItem = null;
        String mmCode = MixMatchItem.GENERAL_PROMOTION;
        try {
            int mixMatchType = MixMatchItemFactory.UNKNOWN;

            if (Item.TAX_FREE.equals(itemToAdd.getTaxType())) {
                mmCode = MixMatchItem.GENERAL_PROMOTION;
            } else if (mixMatchCode != null) {
                mmCode = mixMatchCode;
            }
            mixMatchItem = getMixMatchItem(mmCode);
            if (mixMatchItem == null) {
                IMixMatchDAO mixMatchDao = DAOFactory.getDAOFactory(
                        DAOFactory.SQLSERVER).getMixMatchDAO();
                // Search for Normal MixMatch Data
                MixMatchData mixMatchData = mixMatchDao.getNormalMixMatchData(companyId, retailStoreId, mmCode);

                if (mixMatchData != null
                        && itemToAdd.getDiscountable()
                        && !Item.TAX_FREE.equals(itemToAdd.getTaxType()) ) {
                    mixMatchType = mixMatchData.getType();
                } else {
                    mmCode = MixMatchItem.GENERAL_PROMOTION;
                    itemToAdd.setMixMatchCode(null);
                    mixMatchItem = getMixMatchItem(
                            mmCode);
                }
                
                if (mixMatchItem == null) {
                    mixMatchItem = MixMatchItemFactory.createMixMatchItem(
                            mixMatchType,
                            mmCode);
                    //Is the MixMatch object is still null.
                    //If yes, Mix Match Type is
                    //unavailable/unknown in the database.
                    if (null == mixMatchItem) {
                        tp.println(" Type with value "
                                 + mixMatchType
                                + " is Unknown."
                                + "Can't create Mix and Match Step.");
                        return null;
                    } else {
                        // Check if mixMatchItem is GroupMixMatch
                        if (mixMatchItem instanceof GroupMixMatchItem) {
                            // Get corresponding Group MixMatch Data
                            mixMatchData = mixMatchDao.getGroupMixMatchData(companyId, 
                                    retailStoreId, mmCode);
                            mixMatchItem = this.setGroupMixMatchSubItem(
                                    (GroupMixMatchItem) mixMatchItem,
                                    mixMatchDao, mixMatchData,
                                    mmCode, companyId, retailStoreId);
                        }

                        mixMatchItem.setMixMatchData(mixMatchData);
                        addMixMatchItem(mixMatchItem);
                    }
                }
            }
            //Sales that is saved in the terminal
            //does not include Price Override change to ActualSalesUnitPrice.
            mixMatchItem.addItem((Sale) itemToAdd.clone());
            // save item entry and create mix match item
            // if entry is not yet existing
            if (itemEntryMap.get(itemToAdd.getItemEntryId()) == null) {
                itemEntryMap.put(itemToAdd.getItemEntryId(),
                        mmCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            tp.methodExit(mixMatchItem);
        }
        return mixMatchItem;
    }
    
    /**
     * Combination of new map data structures
     * @param mixmatchCode the item's mixmatchCode
     * @param entryId the sale's entryId
     * @param info  MixMatchDetailInfo
     */
    public void setBmDetailMap(String mixmatchCode, MixMatchDetailInfo info,boolean isUpdate) {
      boolean flag = false;
      List<MixMatchDetailInfo> list = null;
      String mmNo_taxId = mixmatchCode +"_" + info.getTaxId();
      if (!StringUtility.isNullOrEmpty(info.getOriginalTaxId())) {
    	  String mmNo_originaTaxId = mixmatchCode +"_" + info.getOriginalTaxId();
    	  if(bmDetailMap.containsKey(mmNo_originaTaxId)){
    		  bmDetailMap.remove(mmNo_originaTaxId);
    	  }
      }
      if(bmDetailMap.containsKey(mmNo_taxId)){
          for(MixMatchDetailInfo inf : bmDetailMap.get(mmNo_taxId)){
              if(inf.getEntryId().equals(info.getEntryId())){
                  inf.setQuantity(info.getQuantity());
                  inf.setTruePrice(info.getTruePrice());
                  inf.setTaxId(info.getTaxId());
                  flag = true;
              }
          }
          if(!flag){
              bmDetailMap.get(mmNo_taxId).add(info);
          }
      }else{
          list = new ArrayList<MixMatchDetailInfo>();
          list.add(info);
          bmDetailMap.put(mmNo_taxId, list);
      }
      if(!itemEntryMap.containsKey(info.getEntryId())){
          itemEntryMap.put(info.getEntryId(), mixmatchCode);
      }
  }
    
    
    /**
     * Combination of new map data structures
     * @param mixmatchCode the item's mixmatchCode
     * @param entryId the sale's entryId
     * @param info  MixMatchDetailInfo
     */
    public void setBmMapBySku(String mixmatchCode, MixMatchDetailInfo info,boolean isUpdate) {
      boolean flag = false;
      List<MixMatchDetailInfo> list = null;
      if(bmMapBySku.containsKey(mixmatchCode)){
          for(MixMatchDetailInfo inf : bmMapBySku.get(mixmatchCode)){
              if(inf.getEntryId().equals(info.getEntryId())){
                      if(!isUpdate){
                          inf.setQuantity(inf.getQuantity() + info.getQuantity());
                      } else{
                          inf.setQuantity(info.getQuantity());
                      }
                      inf.setTruePrice(info.getTruePrice());
                  flag = true;
              }
          }
          if(!flag){
              bmMapBySku.get(mixmatchCode).add(info);
          }
      }else{
          list = new ArrayList<MixMatchDetailInfo>();
          list.add(info);
          bmMapBySku.put(mixmatchCode, list);
      }
      if(!itemEntryMap.containsKey(info.getEntryId())){
          itemEntryMap.put(info.getEntryId(), mixmatchCode);
      }
  }
    
    /**
     * add the rule to the bmRuleMap
     * add the mixMatch rule to the map
     * @param mmNo  the mmNo of the item
     * @param item item
     */
    public void addBmRuleMap(String mmNo, Item item,String entryId) {
        
        tp.methodEnter("addBmRuleMap")
        .println("mmNo", mmNo)
        .println("Item", item);
        MixMatchRule rule = new MixMatchRule();
//        if(!itemEntryMap.containsKey(entryId)){
//            itemEntryMap.put(entryId, mmNo);
//        }
        if(!itemIdToMixMatchMap.containsKey(item.getItemId())){
            itemIdToMixMatchMap.put(item.getItemId(), mmNo);
        }
        if (!StringUtility.isNullOrEmpty(mmNo)) {
            if (!bmRuleMap.containsKey(mmNo)) {
                rule.setConditionCount1(item.getRuleQuantity1());
                rule.setConditionCount2(item.getRuleQuantity2());
                rule.setConditionCount3(item.getRuleQuantity3());

                rule.setConditionPrice1(item.getConditionPrice1());
                rule.setConditionPrice2(item.getConditionPrice2());
                rule.setConditionPrice3(item.getConditionPrice3());
                
                rule.setDecisionPrice1(item.getDecisionPrice1());
                rule.setDecisionPrice2(item.getDecisionPrice2());
                rule.setDecisionPrice3(item.getDecisionPrice3());
                
                rule.setAveragePrice1(item.getAveragePrice1());
                rule.setAveragePrice2(item.getAveragePrice2());
                rule.setAveragePrice3(item.getAveragePrice3());
                rule.setNote(item.getNote());
                bmRuleMap.put(mmNo, rule);
            }
        }
    }
    
    public List<QrCodeInfo> getQrCodeInfoList(Item item) {
        tp.methodEnter("getQrCodeInfoList").println("Item", item);
        if(item.getQrCodeList().size() > 0){
            for(QrCodeInfo itemQrCodeInfo : item.getQrCodeList()){
                boolean flag = true;
                for(QrCodeInfo returnQrCodeInfo : qrCodeInfoList){
                    if(itemQrCodeInfo.getPromotionId().equals(returnQrCodeInfo.getPromotionId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    qrCodeInfoList.add(itemQrCodeInfo);
                }
            }
        }
        return qrCodeInfoList;
    }
    
    public Map<String,CouponInfo> getCouponInfoMap(Item item){
        
        if (!StringUtility.isNullOrEmpty(item.getCouponNo())) {
            if(!couponInfoMap.containsKey(item.getCouponNo())){
                CouponInfo couponInfo = new CouponInfo();
                couponInfo.setCouponNo(item.getCouponNo());
                couponInfo.setEvenetName(item.getEvenetName());
                couponInfo.setIssueCount(item.getIssueCount());
                couponInfo.setIssueType(item.getIssueType());
                couponInfo.setReceiptName(item.getReceiptName());
                couponInfo.setUnitPrice(item.getUnitPrice());
                couponInfoMap.put(item.getCouponNo(), couponInfo);
            }
        }
        return couponInfoMap;
    }

    /**
     * get the mixMatchs
     * @return the result of the mixMatch
     * @throws DaoException
     */
    public Map<String, Map<String, Object>> getMixMatchMap(){
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Map.Entry<String, List<MixMatchDetailInfo>> mapList : bmDetailMap.entrySet()) {
            map.putAll(getMixMatchMap(mapList.getKey(),""));
        }
        return map;
    }
    

    /**
     * get the item of mixMatch info
     * @param mmNo mixMatchcode
     * @param isDelete  the flag of delete handle
     * @return the info of the mixMatch
     */
    public Map<String, Map<String, Object>> getMixMatchMap(String mmNo_taxId,String isDeleteOrUpdate) {
        Map<String, Map<String, Object>> detailMap = new HashMap<String, Map<String, Object>>();
        // the sum of quantity
        int sumCount = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        List<MixMatchDetailInfo> ruleList2 = new ArrayList<MixMatchDetailInfo>();
        List<MixMatchDetailInfo> ruleList1 = new ArrayList<MixMatchDetailInfo>();
        List<MixMatchDetailInfo> ruleList = new ArrayList<MixMatchDetailInfo>();
        MixMatchRule rule = bmRuleMap.get(mmNo_taxId.split("_")[0]);
        List<MixMatchDetailInfo> ruleList3 = new ArrayList<MixMatchDetailInfo>();
        ruleList3 = bmDetailMap.get(mmNo_taxId);
        map.put("note", rule.getNote());
        // sort by price
        Collections.sort(ruleList3);
        // loop the list matching rule3
        for (MixMatchDetailInfo mixMatchDetailInfo3 : ruleList3) {
            MixMatchDetailInfo info = new MixMatchDetailInfo();
            info.setEntryId(mixMatchDetailInfo3.getEntryId());
            info.setTruePrice(mixMatchDetailInfo3.getTruePrice());
            info.setTaxId(mixMatchDetailInfo3.getTaxId());
            int remainder = mixMatchDetailInfo3.getQuantity();
            // the Sum of remainder quantity
            int sumCount1 = 0;
            for (MixMatchDetailInfo rule2info : ruleList2) {
                sumCount1 = sumCount1 + rule2info.getQuantity();
            }
            if (sumCount1 >= 0) {
                sumCount = sumCount1 + mixMatchDetailInfo3.getQuantity();
            } else {
                sumCount = sumCount + mixMatchDetailInfo3.getQuantity();
            }
            if (rule.getConditionCount3() > 0 && (sumCount / rule.getConditionCount3()) >= 1) {
                if (map.containsKey("rule1")) {
                    map.remove("rule1");
                }
                if (map.containsKey("rule2")) {
                    map.remove("rule2");
                }
                MixMatchDetailInfo ruleentryinfo1 = new MixMatchDetailInfo();
                ruleentryinfo1.setEntryId(mixMatchDetailInfo3.getEntryId());
                ruleentryinfo1.setTruePrice(mixMatchDetailInfo3.getTruePrice());
                ruleentryinfo1.setTaxId(mixMatchDetailInfo3.getTaxId());
                DetailInfo detailinfo = new DetailInfo();
                detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice3() != null ? rule.getDecisionPrice3() : 0);
                detailinfo.setConditionPrice(rule.getConditionPrice3());
                
                detailinfo.setConditionCount(rule.getConditionCount3());
                // DecisionPrice > 0
                int times = 0;
                times = sumCount / rule.getConditionCount3();
                sumCount = sumCount % rule.getConditionCount3();
                remainder = mixMatchDetailInfo3.getQuantity() - sumCount;
                ruleentryinfo1.setQuantity(remainder);
                ruleList2.add(ruleentryinfo1);
                detailinfo.addEntryList(ruleList2);
                // if(null != rule.getDecisionPrice3()){
                    map.put("lastDecisionPrice", rule.getDecisionPrice3());
                    // detailinfo.setAveragePrice(rule.getAveragePrice3() > rule.getDecisionPrice3() ? rule.getDecisionPrice3() : rule.getAveragePrice3());
                // }else{
                    detailinfo.setAveragePrice(rule.getAveragePrice3());
                // }
                if (map.containsKey("rule3")) {
                    ((DetailInfo) map.get("rule3")).addEntryList(ruleList2);
                    ((DetailInfo) map.get("rule3")).setTimes(((DetailInfo) map.get("rule3")).getTimes() + times);
                } else {
                    detailinfo.setTimes(times);
                    map.put("rule3", detailinfo);
                }

                ruleList2.clear();
            }
            if (sumCount == mixMatchDetailInfo3.getQuantity() + sumCount1) {
                info.setQuantity(mixMatchDetailInfo3.getQuantity());

            } else {
                if (sumCount == 0) {
                    continue;
                }
                info.setQuantity(sumCount);
            }
            ruleList2.add(info);
        }

        for (MixMatchDetailInfo mixMatchDetailInfo2 : ruleList2) {
            sumCount = 0;
            MixMatchDetailInfo info = new MixMatchDetailInfo();
            info.setEntryId(mixMatchDetailInfo2.getEntryId());
            info.setTruePrice(mixMatchDetailInfo2.getTruePrice());
            info.setTaxId(mixMatchDetailInfo2.getTaxId());
            int remainder = mixMatchDetailInfo2.getQuantity();
            int sumCount1 = 0;
            for (MixMatchDetailInfo rule1info : ruleList1) {
                sumCount1 = sumCount1 + rule1info.getQuantity();
            }
            if (sumCount1 > 0) {
                sumCount = sumCount1 + mixMatchDetailInfo2.getQuantity();
            } else {
                sumCount = sumCount + mixMatchDetailInfo2.getQuantity();
            }
            if (rule.getConditionCount2() > 0 && (sumCount / rule.getConditionCount2()) >= 1) {
                if (map.containsKey("rule1")) {
                    map.remove("rule1");
                }
                MixMatchDetailInfo ruleentryinfo1 = new MixMatchDetailInfo();
                ruleentryinfo1.setEntryId(mixMatchDetailInfo2.getEntryId());
                ruleentryinfo1.setTruePrice(mixMatchDetailInfo2.getTruePrice());
                ruleentryinfo1.setTaxId(mixMatchDetailInfo2.getTaxId());
                DetailInfo detailinfo = new DetailInfo();
                detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice2() != null ? rule.getDecisionPrice2() : 0);
                detailinfo.setConditionPrice(rule.getConditionPrice2());
//                detailinfo.setAveragePrice(rule.getAveragePrice2());
                detailinfo.setConditionCount(rule.getConditionCount2());
                if (rule.getDecisionPrice2() != null && !map.containsKey("lastDecisionPrice")) {
                    map.put("lastDecisionPrice", rule.getDecisionPrice2());
                }
                // if(map.containsKey("lastDecisionPrice")){
                //     detailinfo.setAveragePrice(rule.getAveragePrice2() > (double)map.get("lastDecisionPrice") ? (double)map.get("lastDecisionPrice") : rule.getAveragePrice2());
                // } else{
                    detailinfo.setAveragePrice(rule.getAveragePrice2());
                // }
                int times = sumCount / rule.getConditionCount2();
                sumCount = sumCount % rule.getConditionCount2();
                remainder = mixMatchDetailInfo2.getQuantity() - sumCount;
                ruleentryinfo1.setQuantity(remainder);
                ruleList1.add(ruleentryinfo1);
                detailinfo.addEntryList(ruleList1);
                if (map.containsKey("rule2")) {
                    ((DetailInfo) map.get("rule2")).addEntryList(ruleList1);
                    ((DetailInfo) map.get("rule2")).setTimes(((DetailInfo) map.get("rule2")).getTimes() + times);
                } else {
                    detailinfo.setTimes(times);
                    map.put("rule2", detailinfo);
                }

                ruleList1.clear();
            }
            if (sumCount == mixMatchDetailInfo2.getQuantity() + sumCount1) {
                info.setQuantity(mixMatchDetailInfo2.getQuantity());

            } else {
                if (sumCount == 0) {
                    continue;
                }
                info.setQuantity(sumCount);
            }
            ruleList1.add(info);
        }
        for (MixMatchDetailInfo mixMatchDetailInfo1 : ruleList1) {
            sumCount = 0;
            MixMatchDetailInfo info = new MixMatchDetailInfo();
            info.setEntryId(mixMatchDetailInfo1.getEntryId());
            info.setTruePrice(mixMatchDetailInfo1.getTruePrice());
            info.setTaxId(mixMatchDetailInfo1.getTaxId());
            int remainder = mixMatchDetailInfo1.getQuantity();
            int sumCount1 = 0;
            for (MixMatchDetailInfo ruleInfo : ruleList) {
                sumCount1 = sumCount1 + ruleInfo.getQuantity();
            }
            if (sumCount1 > 0) {
                sumCount = sumCount1 + mixMatchDetailInfo1.getQuantity();
            } else {
                sumCount = sumCount + mixMatchDetailInfo1.getQuantity();
            }
            if (rule.getConditionCount1() > 0 &&(sumCount / rule.getConditionCount1()) >= 1) {
                MixMatchDetailInfo ruleentryinfo1 = new MixMatchDetailInfo();
                ruleentryinfo1.setEntryId(mixMatchDetailInfo1.getEntryId());
                ruleentryinfo1.setTruePrice(mixMatchDetailInfo1.getTruePrice());
                ruleentryinfo1.setTaxId(mixMatchDetailInfo1.getTaxId());
                DetailInfo detailinfo = new DetailInfo();
                detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice1() != null ? rule.getDecisionPrice1() : 0);
                detailinfo.setConditionCount(rule.getConditionCount1());
//                detailinfo.setAveragePrice(rule.getAveragePrice1());
                detailinfo.setConditionPrice(rule.getConditionPrice1());
                if (rule.getDecisionPrice1() != null && !map.containsKey("lastDecisionPrice")) {
                    map.put("lastDecisionPrice", rule.getDecisionPrice1());
                }
                // if(map.containsKey("lastDecisionPrice")){
                //     detailinfo.setAveragePrice(rule.getAveragePrice1() > (double)map.get("lastDecisionPrice") ? (double)map.get("lastDecisionPrice") : rule.getAveragePrice1());
                // } else{
                    detailinfo.setAveragePrice(rule.getAveragePrice1());
                // }
                int times = sumCount / rule.getConditionCount1();
                sumCount = sumCount % rule.getConditionCount1();
                remainder = mixMatchDetailInfo1.getQuantity() - sumCount;
                ruleentryinfo1.setQuantity(remainder);
                ruleList.add(ruleentryinfo1);
                detailinfo.addEntryList(ruleList);
                if (map.containsKey("rule1")) {
                    ((DetailInfo) map.get("rule1")).addEntryList(ruleList);
                    ((DetailInfo) map.get("rule1")).setTimes(((DetailInfo) map.get("rule1")).getTimes() + times);
                } else {
                    detailinfo.setTimes(times);
                    map.put("rule1", detailinfo);
                }
                ruleList.clear();
            }
            if (sumCount == (mixMatchDetailInfo1.getQuantity() + sumCount1)) {
                info.setQuantity(mixMatchDetailInfo1.getQuantity());

            } else {
                if (sumCount == 0) {
                    continue;
                }
                info.setQuantity(sumCount);
            }
            ruleList.add(info);
        }
        if(!map.containsKey("lastDecisionPrice")){
            map.put("lastDecisionPrice", "");
        }
            DetailInfo detailinfo = new DetailInfo();
            detailinfo.setEntryList(ruleList);
            map.put("remainder", detailinfo);
        if("true".equals(isDeleteOrUpdate) && !map.containsKey("rule1") && !map.containsKey("rule2") && !map.containsKey("rule3")){
            map.put("hasMixMatch", "false");
        }
        detailMap.put(mmNo_taxId, map);
        return detailMap;
    }
    

    public Map<String, Map<String, Object>> getTheNewMap(Map<String, Map<String, Object>> map){
        for(Map.Entry<String, Map<String, Object>> entryMap : map.entrySet()){
            double beforeBmPrice = 0;
            double bmPrice = 0;
            String mmNo = entryMap.getKey();
            Map<String, Object> childMap = entryMap.getValue();
            double lastDecisionPrice =!isNullOrEmpty(childMap.get("lastDecisionPrice")) ? (Double)childMap.get("lastDecisionPrice") :0;
            bmPrice += getPrice(childMap,"rule1",lastDecisionPrice);
            bmPrice += getPrice(childMap,"rule2",lastDecisionPrice);
            bmPrice += getPrice(childMap,"rule3",lastDecisionPrice);
            bmPrice += getRemainderPrice(childMap,lastDecisionPrice);
            if(!"".equals(mmNo)){
                List<MixMatchDetailInfo> ruleList  = bmDetailMap.get(mmNo);
                for(MixMatchDetailInfo info : ruleList){
                    beforeBmPrice += info.getQuantity() * info.getTruePrice();
                }
                if(beforeBmPrice < bmPrice){
                    childMap.put("hasMixMatch", "false");
                    return map;
                }
            }
        }
        return map;
    }
    

    
    public boolean isDeleteBm(Map<String, Map<String, Object>> map){
        String mmNo_taxId = "";
        double beforeBmPrice = 0;
        double bmPrice = 0;
        for(Map.Entry<String, Map<String, Object>> entryMap : map.entrySet()){
        	mmNo_taxId = entryMap.getKey();
            Map<String, Object> childMap = entryMap.getValue();
            double lastDecisionPrice =!isNullOrEmpty(childMap.get("lastDecisionPrice")) ? (Double)childMap.get("lastDecisionPrice") :0;
            bmPrice += getPrice(childMap,"rule1",lastDecisionPrice);
            bmPrice += getPrice(childMap,"rule2",lastDecisionPrice);
            bmPrice += getPrice(childMap,"rule3",lastDecisionPrice);
            bmPrice += getRemainderPrice(childMap,lastDecisionPrice);
        }
        if(!"".equals(mmNo_taxId)){
            List<MixMatchDetailInfo> ruleList  = bmDetailMap.get(mmNo_taxId);
            for(MixMatchDetailInfo info : ruleList){
                beforeBmPrice += info.getQuantity() * info.getTruePrice();
            }
            if(beforeBmPrice < bmPrice){
                return true;
            }
        }
        return false;
    }
    
    private double getPrice(Map<String, Object> map,String rule,double lastDecisionPrice){
        double price = 0.0;
        if(map.containsKey(rule)){
            DetailInfo detailinfo = (DetailInfo)map.get(rule);
            int times = detailinfo.getTimes();
            double conditionPrice = detailinfo.getConditionPrice();
            if(detailinfo.getAveragePrice() <= lastDecisionPrice || lastDecisionPrice == 0){
                price = times * conditionPrice;
            }else if(detailinfo.getAveragePrice() > lastDecisionPrice && lastDecisionPrice > 0) {
                price = times * lastDecisionPrice * detailinfo.getConditionCount();
            }
           
        }
        return price;
    }
    
    private double getRemainderPrice(Map<String, Object> map,double lastDecisionPrice){
        DetailInfo remainder = (DetailInfo)map.get("remainder");
        List<MixMatchDetailInfo> ruleList = remainder.getEntryList();
        double price = 0.0;
        for(MixMatchDetailInfo info : ruleList){
            if(lastDecisionPrice > 0){
                price += info.getQuantity() * lastDecisionPrice;
            }else{
                price += info.getQuantity() * info.getTruePrice();
            }
        }
        return price;
    }
    /**
     * Checks values if null or empty.
     *
     * @param values
     *            optional variables.
     * @return true if null/empty, false if not null/empty.
     */
    public static boolean isNullOrEmpty(final Object... values) {
        boolean isNullEmpty = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null || "".equals(values[i])) {
                isNullEmpty = true;
                break;
            }
        }
        return isNullEmpty;
    }
    
    
    /**
     * Get the mix match code associated to an item entry.
     * @param itemEntryId the item entry id
     * @return String
     */
    public final String getItemMixMatchCode(final String itemEntryId) {
        return itemEntryMap.get(itemEntryId);
    }
    
    /**
     * Get the mix match code associated to an item entry.
     * @param itemEntryId the item entry id
     * @return String
     */
    public final String getItemMixMatchCodeByItemId(final String itemEntryId) {
        return itemIdToMixMatchMap.get(itemEntryId);
    }

    /**
     * Deletes ItemEntry in a list of TerminalItem's ItemEntry.
     * @param itemEntryId to be removed.
     */
    public final void deleteTerminalItemEntry(String itemEntryId){
        itemEntryMap.remove(itemEntryId);
    }
    
    /**
     * Helper method for creating SubItem for Group MixMatch.
     * @param mixMatchItem The Group MixMatch Instance.
     * @param mixMatchCode the Group MixMatchCode.
     * @param retailStoreId The Retail Store ID.
     * @param mixMatchDao The Mix and Match Data Access Object instance
     * @param mixMatchData The Mix and Match Data
     * @return  The Updated Group MixMatch with SubItem.
     * @throws DaoException Exception thrown when error occur.
     */
    private GroupMixMatchItem setGroupMixMatchSubItem(
            final GroupMixMatchItem mixMatchItem,
            final IMixMatchDAO mixMatchDao,
            final MixMatchData mixMatchData,
            final String mixMatchCode,
            final String companyId,
            final String retailStoreId) throws DaoException {
        if (mixMatchData == null) {
            tp.println("GroupMixAndMatch Data with code "
                    + mixMatchCode
                    + " is Unknown."
                    + "Can't create Mix and Match Step.");
            return null;
        }

        // Create mixMatchSubItems for the GroupMixMatch
        List<GroupMixMatchSubItem> tempSubItems = new ArrayList<GroupMixMatchSubItem>();
        List<String> subCodes = ((GroupMixMatchData) mixMatchData)
                .getSubCodes();

        for (String subcode : subCodes) {
            // Get Group Mix Match Sub Item Data
            MixMatchData subItemData = mixMatchDao.getNormalMixMatchData(companyId, 
                    retailStoreId, subcode);

            if (subItemData == null) {
                tp.println("MixAndMatch Data with code "
                        + subcode
                        + " is Unknown."
                        + "Can't create Mix and Match Step.");
                return null;
            }

            // Create Group Mix Match Sub Item
            GroupMixMatchSubItem subItem =
                MixMatchItemFactory.createSubMixMatchItem(
                    subItemData.getType(),
                    subcode);

            if (subItem == null) {
                tp.println("MixAndMatch Type with value "
                        + subItemData.getType()
                        + " is Unknown."
                        + "Can't create Mix and Match Step.");
                return null;
            }

            subItem.setMixMatchData(subItemData);
            tempSubItems.add(subItem);
        }

       mixMatchItem.setSubItems(tempSubItems);
       return mixMatchItem;
    }
}
