package ncr.res.mobilepos.promotion.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.model.CouponInfo;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.promotion.dao.IMixMatchDAO;
import ncr.res.mobilepos.promotion.model.DetailInfo;
import ncr.res.mobilepos.promotion.model.GroupMixMatchData;
import ncr.res.mobilepos.promotion.model.GroupMixMatchItem;
import ncr.res.mobilepos.promotion.model.GroupMixMatchSubItem;
import ncr.res.mobilepos.promotion.model.ItemForPm;
import ncr.res.mobilepos.promotion.model.MixMatchData;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.MixMatchItem;
import ncr.res.mobilepos.promotion.model.MixMatchItemFactory;
import ncr.res.mobilepos.promotion.model.MixMatchRule;
import ncr.res.mobilepos.promotion.model.PmItemInfo;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.resource.PromotionConstants;

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
    
    private Map<String, MixMatchDetailInfo> bmMapByEntryid;
    
	private Map<String, List<MixMatchDetailInfo>> bmMapBySku;
    
    private Map<String,Map<String, Object>> detailInfoMap;
    
    private List<QrCodeInfo> qrCodeInfoList;
    
    private Map<String,CouponInfo> couponInfoMap;
    
    private Map<String, Sale> itemCacheMap;
    
    private Map<String, Set<String>> promotionItemCouponMap;
    
    private Map<String, Set<String>> couponMap;
    
    /**
     * the cache for the PM
     */
    private Map<String, Map<String, List<PmItemInfo>>> pmMap;
    
    /**
     * current item 
     */
    private Map<String, ItemForPm> itemOfManyPm;
    
    /**
     * the item  is  pm and Bm   and makeBM
     */
    private Map<String, ItemForPm> pmItemInBm;
    
    private Map<String, Object> preMap;
    
    public Map<String, ItemForPm> getItemOfManyPm() {
		return itemOfManyPm;
	}
    
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
        bmMapByEntryid = new HashMap<String, MixMatchDetailInfo>();
        bmMapBySku = new HashMap<String, List<MixMatchDetailInfo>>();
        detailInfoMap = new HashMap<String,Map<String, Object>>();
        qrCodeInfoList = new ArrayList<QrCodeInfo>();
        couponInfoMap = new HashMap<String,CouponInfo>();
        itemIdToMixMatchMap = new HashMap<String, String>();
        pmMap = new HashMap<>();
        itemOfManyPm = new HashMap<>();
        pmItemInBm = new HashMap<>();
        preMap = new HashMap<>();
        itemCacheMap = new HashMap<>();
        promotionItemCouponMap = new HashMap<>();
        couponMap = new HashMap<>();
    }
    public Map<String, Sale> getItemCacheMap() {
		return itemCacheMap;
	}
	public Map<String, Object> getPreMap() {
		return preMap;
	}
	public Map<String, ItemForPm> getPmItemInBm() {
		return pmItemInBm;
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
     * Combination of new List data structures
     * @param entryId the sale's entryId
     * @param info  MixMatchDetailInfo
     */
    public void setBmDetailMap(List<PriceMMInfo> priceMMInfoList, MixMatchDetailInfo info) {
    	boolean flag = false;
    	if (priceMMInfoList != null && !priceMMInfoList.isEmpty()) {
    		info.setPriceMMInfoList(priceMMInfoList);
    		for(Entry<String, MixMatchDetailInfo> entry : bmMapByEntryid.entrySet()){
                if(entry.getValue().getEntryId().equals(info.getEntryId())){
                	entry.getValue().setQuantity(info.getQuantity());
                	entry.getValue().setTruePrice(info.getTruePrice());
                	entry.getValue().setTaxId(info.getTaxId());
                    flag = true;
                }
            }
    		
    		if(!flag){
    			bmMapByEntryid.put(info.getEntryId(), info);
            }
    	}
    }
    
    /**
     * Combination of new map data structures
     * @param mixmatchCode the item's mixmatchCode
     * @param entryId the sale's entryId
     * @param info  MixMatchDetailInfo
     */
    public void setBmDetailMapList(List<PriceMMInfo> priceMMInfoList, MixMatchDetailInfo info) {
    	if (priceMMInfoList != null && !priceMMInfoList.isEmpty()) {
    		info.setPriceMMInfoList(priceMMInfoList);
    		this.setBmDetailMap(priceMMInfoList, info);
    		for (PriceMMInfo priceMMInfo : priceMMInfoList) {
    			this.setBmDetailMap(priceMMInfo.getMMNo(), info, false);
    		}
    	}
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
      if (info.getTaxId() != info.getOriginalTaxId()) {
    	  String mmNo_originaTaxId = mixmatchCode +"_" + info.getOriginalTaxId();
    	  if(bmDetailMap.containsKey(mmNo_originaTaxId)){
    		  for(MixMatchDetailInfo inf : bmDetailMap.get(mmNo_originaTaxId)){
                  if(inf.getEntryId().equals(info.getEntryId())){
                	  inf.setQuantity(0);
                  }
              }
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
    
    /**
     * add the rule to the bmRuleMap
     * add the mixMatch rule to the map
     * @param item item
     */
    public void addBmRuleMap(Item item) {
        tp.methodEnter("addBmRuleMap")
        .println("Item", item);
        
        List<PriceMMInfo> priceMMInfoList = item.getPriceMMInfoList();
        if (priceMMInfoList != null && !priceMMInfoList.isEmpty()) {
        	for (PriceMMInfo priceMMInfo : priceMMInfoList) {
        		MixMatchRule rule = new MixMatchRule();
        		String mmNo = priceMMInfo.getMMNo();
                if (!StringUtility.isNullOrEmpty(mmNo)) {
                    if (!bmRuleMap.containsKey(mmNo)) {
                        rule.setConditionCount1(priceMMInfo.getConditionCount1());
                        rule.setConditionCount2(priceMMInfo.getConditionCount2());
                        rule.setConditionCount3(priceMMInfo.getConditionCount3());

                        rule.setConditionPrice1(priceMMInfo.getConditionPrice1());
                        rule.setConditionPrice2(priceMMInfo.getConditionPrice2());
                        rule.setConditionPrice3(priceMMInfo.getConditionPrice3());
                        
                        rule.setDecisionPrice1(priceMMInfo.getDecisionPrice1());
                        rule.setDecisionPrice2(priceMMInfo.getDecisionPrice2());
                        rule.setDecisionPrice3(priceMMInfo.getDecisionPrice3());
                        
                        rule.setAveragePrice1(priceMMInfo.getAveragePrice1());
                        rule.setAveragePrice2(priceMMInfo.getAveragePrice2());
                        rule.setAveragePrice3(priceMMInfo.getAveragePrice3());
                        
                        // add by  hxd 20200402 start
                        rule.setConditionRate1(priceMMInfo.getConditionRate1());
                        rule.setConditionRate2(priceMMInfo.getConditionRate2());
                        rule.setConditionRate3(priceMMInfo.getConditionRate3());
                        
                        rule.setDecisionRate1(priceMMInfo.getDecisionRate1());
                        rule.setDecisionRate2(priceMMInfo.getDecisionRate2());
                        rule.setDecisionRate3(priceMMInfo.getDecisionRate3());
                        
                        rule.setDiscountClass(priceMMInfo.getDiscountClass());
                        rule.setSubNum1(priceMMInfo.getSubNum1());
                        rule.setMmDisplayNo(priceMMInfo.getMmDisplayNo());
                     // add by  hxd 20200402 end
                        
                        rule.setNote(priceMMInfo.getNote());
                        bmRuleMap.put(mmNo, rule);
                    }
                }
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
        	String key = mapList.getKey();
        	if(!StringUtility.isNullOrEmpty(key)) {
        		String[] keys = key.split("_");
        		if(keys.length > 1) {
                    String mmNo = keys[0];
                    MixMatchRule rule = bmRuleMap.get(mmNo);
        			if(!isSelectedCode(String.valueOf(rule.getSubNum1()), mmNo, "BM")) {
        				continue;
        			}else {
        				map.putAll(getMixMatchMap(mapList.getKey(),"", mapList.getValue()));
        			}
        		}
        	}
        }
        return map;
    }
    
    public boolean isSelectedCode(String couponFlag, String codeNo, String discountName) {
    	boolean isSelectedCode = true;
    	if(PromotionConstants.CUSTOMER_SALE_STRING.equals(couponFlag)) {
    		if(couponMap.containsKey(discountName)) {
        		Set<String> set = couponMap.get(discountName);
        		if(set.size() > 0 && !set.contains(codeNo)) {
        			isSelectedCode = false;
        		}
    		}else {
    			isSelectedCode = false;
        	}
    	}
    	return isSelectedCode;
    }
    
    /**
     * get the item of mixMatch info
     * @param mmNo mixMatchcode
     * @param isDelete  the flag of delete handle
     * @return the info of the mixMatch
     */
    public Map<String, Map<String, Object>> getEnabledMixMatchMap() {
    	Map<String, Map<String, Object>> retMap = new HashMap<String, Map<String, Object>>();
    	Map<String, Map<String, Object>> detailMmMap = new HashMap<String, Map<String, Object>>();
    	// clone bmDetailMap
    	Map<String, List<MixMatchDetailInfo>> clonedBmDetailMap = this.clonebmDetailMap();
    	int size = clonedBmDetailMap.size();
    	for (int i = size - 1; i >= 0; i--) {
			for (Entry<String, List<MixMatchDetailInfo>> entry : clonedBmDetailMap.entrySet()) {
				String key = entry.getKey();
				if(!StringUtility.isNullOrEmpty(key)) {
					String[] keys = key.split("_");
					if(keys.length > 1) {
                        String mmNo = keys[0];
                        MixMatchRule rule = bmRuleMap.get(mmNo);
						if(!isSelectedCode(String.valueOf(rule.getSubNum1()), mmNo, "BM")) {
							continue;
						}else {
							Map<String, Map<String, Object>> mixMap = this.getMixMatchMap(key, "", entry.getValue());
							detailMmMap.put(key, mixMap.get(key));
						}
					}
				}
			}
	
	    	if (detailMmMap.isEmpty()) {  
	    		return new HashMap<String, Map<String, Object>>();
	    	} else {
	    		// clean bm group
	    		this.cleanMixMatchMap(detailMmMap);
	    		// compute the discount of every BM
	    		this.computeMixMatchDiscount(detailMmMap);
	    		// sort by discount price
	    		List<Map<String, Object>> sortedMmMap = sortByValueAscending(detailMmMap);
	    		
	    		// coumute and select the best group
	    		if (sortedMmMap != null && !sortedMmMap.isEmpty()) {
		            for(Map<String, Object> map : sortedMmMap){
		            	this.setBmQuantity("rule3", map, clonedBmDetailMap);
		            	this.setBmQuantity("rule2", map, clonedBmDetailMap);
		            	this.setBmQuantity("rule1", map, clonedBmDetailMap);
		            	if(map.containsKey("remainder")) {
		            		DetailInfo detailInfo = (DetailInfo) map.get("remainder");
		            		List<MixMatchDetailInfo> entryList =  detailInfo.getEntryList();
		            		for (MixMatchDetailInfo mmInfo : entryList) {
		            			for (Entry<String, List<MixMatchDetailInfo>> entry1 : clonedBmDetailMap.entrySet()) {
		            				for (MixMatchDetailInfo entry2 : entry1.getValue()) {
		            					if (entry2.getEntryId().equals(mmInfo.getEntryId())) {
		            						int surplus = entry2.getQuantity() - mmInfo.getQuantity();
		            						entry2.setQuantity(surplus >= 0 ? surplus : 0);
		            					}
		            				}
		            			}
		                	}
		            	}
		            	retMap.put((String) map.get("keyValue"), map);
		            	break;
		            }
	    		}
	    	}
    	}

    	return retMap;
    }
	/**
     * computeMixMatchDiscount
     * @param detailMap
     */
    private void computeMixMatchDiscount(Map<String, Map<String, Object>> detailMmMap) {
		for(Entry<String, Map<String, Object>> mEntry : detailMmMap.entrySet()) {
			Map<String, Object> detailMap = mEntry.getValue();
	    	
			double sumTruePrice = 0d;
			double sumDiscountPrice = 0d;
			
			String maxRule = "";
			if (detailMap.containsKey("rule3")) {
				double rule3[] = this.computeBmDiscount("rule3", detailMap, maxRule);
				sumTruePrice = sumTruePrice + rule3[0];
				sumDiscountPrice = sumDiscountPrice + rule3[1];
				if ("".equals(maxRule)) {
					maxRule = "rule3";
				}
			}
			
			if (detailMap.containsKey("rule2")) {
				double rule2[] = this.computeBmDiscount("rule2", detailMap, maxRule);
				sumTruePrice = sumTruePrice + rule2[0];
				sumDiscountPrice = sumDiscountPrice + rule2[1];
				if ("".equals(maxRule)) {
					maxRule = "rule2";
				}
			}
			
			if (detailMap.containsKey("rule1")) {
				double rule1[] = this.computeBmDiscount("rule1", detailMap, maxRule);
				sumTruePrice = sumTruePrice + rule1[0];
				sumDiscountPrice = sumDiscountPrice + rule1[1];
				if ("".equals(maxRule)) {
					maxRule = "rule1";
				}
			}
			double sumNowPrice = sumDiscountPrice;
			if(detailMap.containsKey("remainder")) {
				DetailInfo detailinfo = (DetailInfo) detailMap.get("remainder");
				List<MixMatchDetailInfo> ruleList = detailinfo.getEntryList();
				double lastDecisionPrice = 0d;
				double lastDecisionPriceRate = 0;
				int allQuantity = 0;
				String bmDiscountType = (String) detailMap.get("discountClass");
				if (ruleList != null && !ruleList.isEmpty()) {
					double sumRemainderPrice = 0;
					for (MixMatchDetailInfo mmInfo : ruleList) {
						allQuantity += mmInfo.getQuantity();
						sumRemainderPrice += mmInfo.getTruePrice() * mmInfo.getQuantity();
					}
					sumTruePrice = sumTruePrice + sumRemainderPrice;
					if(PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
						if (detailMap.containsKey("lastDecisionPrice") && detailMap.get("lastDecisionPrice") != null
								&& !StringUtility.isNullOrEmpty((detailMap.get("lastDecisionPrice")).toString())) {
							lastDecisionPrice = (Double) detailMap.get("lastDecisionPrice");
						}
						if(lastDecisionPrice > 0) {
							sumDiscountPrice = sumDiscountPrice + lastDecisionPrice * allQuantity;
							sumNowPrice += lastDecisionPrice * allQuantity;
						}else {
							sumDiscountPrice = sumDiscountPrice + sumRemainderPrice;
							sumNowPrice += sumRemainderPrice;
						}
					}
					
					if(PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
						if (detailMap.containsKey("lastDecisionPriceRate") && detailMap.get("lastDecisionPriceRate") != null
								&& !StringUtility.isNullOrEmpty((detailMap.get("lastDecisionPriceRate")).toString())) {
							lastDecisionPriceRate = (Double) detailMap.get("lastDecisionPriceRate");
						}
						if(lastDecisionPriceRate > 0) {
							sumDiscountPrice = sumDiscountPrice + sumRemainderPrice * (100 - lastDecisionPriceRate) / 100;
							sumNowPrice += sumRemainderPrice * (100 - lastDecisionPriceRate) / 100;
						}else {
							sumDiscountPrice = sumDiscountPrice + sumRemainderPrice;
							sumNowPrice += sumRemainderPrice;
						}
					}
				}
			}
			detailMap.put("discountPrice", (sumTruePrice - sumDiscountPrice));
			detailMap.put("sumNowPrice", sumNowPrice);
			detailMap.put("sumTruePrice", sumTruePrice);
		}
    }
    
   
    /**
     * cleanMixMatchMap
     * @param map
     * 
     */
    private void cleanMixMatchMap(Map<String, Map<String, Object>> map) {
    	for(Entry<String, Map<String, Object>> entry : map.entrySet()) {
    		Map<String, Object> bmMap = entry.getValue();
    		if (!bmMap.containsKey("rule1") && !bmMap.containsKey("rule2") && !bmMap.containsKey("rule3")) {
    			continue;
    		}
    		DetailInfo remainderInfo = (DetailInfo) bmMap.get("remainder");
			List<MixMatchDetailInfo> remainderList = remainderInfo.getEntryList();
			Map<String, List<MixMatchDetailInfo>> tmpMap = new HashMap<>(); // save by sku
			for (MixMatchDetailInfo mmdi : remainderList) {
				String isHaveRemainder = "";
				// check rule3
				isHaveRemainder = isHaveRemainder + (isHaveRemainderInfo("rule3", bmMap, mmdi) ? "1" : "0");
				// check rule2
				isHaveRemainder = isHaveRemainder + (isHaveRemainderInfo("rule2", bmMap, mmdi) ? "1" : "0");
				// check rule1
				isHaveRemainder = isHaveRemainder + (isHaveRemainderInfo("rule1", bmMap, mmdi) ? "1" : "0");
				// Remainder info is not exist in rule
				if (isHaveRemainder.indexOf("1") == -1) {
					if (tmpMap.containsKey(mmdi.getSku())) {
						tmpMap.get(mmdi.getSku()).add(mmdi);
					} else {
						List<MixMatchDetailInfo> list = new ArrayList<>();
						list.add(mmdi);
						tmpMap.put(mmdi.getSku(), list);
					}
				}
			}
			
			double lastDecisionPrice01 = 0d;
			double lastDecisionPriceRate = 0;
			String bmDiscountType = (String) bmMap.get("discountClass");
			if(PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
				if (bmMap.containsKey("lastDecisionPrice") && bmMap.get("lastDecisionPrice") != null
						&& !StringUtility.isNullOrEmpty((bmMap.get("lastDecisionPrice")).toString())) {
					lastDecisionPrice01 = (Double) bmMap.get("lastDecisionPrice");
				}
			}
			
			if(PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
				if (bmMap.containsKey("lastDecisionPriceRate") && bmMap.get("lastDecisionPriceRate") != null
						&& !StringUtility.isNullOrEmpty((bmMap.get("lastDecisionPriceRate")).toString())) {
					lastDecisionPriceRate = (Double) bmMap.get("lastDecisionPriceRate");
				}
			}

			for(Entry<String, List<MixMatchDetailInfo>> entryMmdi : tmpMap.entrySet()) {
				double sumTruePrice01 = 0d;
				double sumDiscountPrice01 = 0d;
				double discout01 = 0d;
				double discout02 = 0d;
				List<MixMatchDetailInfo> skuLst = entryMmdi.getValue();
				if (!skuLst.isEmpty()) {
					for (MixMatchDetailInfo info : skuLst) {
						if (lastDecisionPrice01 > 0 && PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
							sumDiscountPrice01 = sumDiscountPrice01 + lastDecisionPrice01 * info.getQuantity();
						}else if(lastDecisionPriceRate > 0 && PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
							sumDiscountPrice01 = sumDiscountPrice01 + (100 - lastDecisionPriceRate) / 100 * info.getQuantity();
						} else {
							sumDiscountPrice01 = sumDiscountPrice01 + info.getTruePrice() * info.getQuantity();
						}
						sumTruePrice01 = sumTruePrice01 + info.getTruePrice() * info.getQuantity();
					}
					discout01 = sumTruePrice01 - sumDiscountPrice01;
					// ------------------------------------------------------
					MixMatchDetailInfo tmpInfo = skuLst.get(0);
					if (tmpInfo.getPriceMMInfoList() != null && tmpInfo.getPriceMMInfoList().size() == 1 && 
							tmpInfo.getPriceMMInfoList().get(0).getSku() == tmpInfo.getSku()) {
						String mmNo_taxId = tmpInfo.getMmNo() + "_" + tmpInfo.getTaxId();
						if (map.containsKey(mmNo_taxId)) {
							Map<String, Object> bmMapDetail = map.get(mmNo_taxId);
							if (bmMapDetail != null && !bmMapDetail.isEmpty()) {
								double sumTruePrice = 0d;
								double sumDiscountPrice = 0d;
								String maxRule = "";
								if (bmMapDetail.containsKey("rule3")) {
									double rule3[] = this.computeBmDiscount("rule3", bmMapDetail, maxRule);
									sumTruePrice = sumTruePrice + rule3[0];
									sumDiscountPrice = sumDiscountPrice + rule3[1];
									if ("".equals(maxRule)) {
										maxRule = "rule3";
									}
								}
								
								if (bmMapDetail.containsKey("rule2")) {
									double rule2[] = this.computeBmDiscount("rule2", bmMapDetail, maxRule);
									sumTruePrice = sumTruePrice + rule2[0];
									sumDiscountPrice = sumDiscountPrice + rule2[1];
									if ("".equals(maxRule)) {
										maxRule = "rule2";
									}
								}
								
								if (bmMapDetail.containsKey("rule1")) {
									double rule1[] = this.computeBmDiscount("rule1", bmMapDetail, maxRule);
									sumTruePrice = sumTruePrice + rule1[0];
									sumDiscountPrice = sumDiscountPrice + rule1[1];
									if ("".equals(maxRule)) {
										maxRule = "rule1";
									}
								}
								
								if (bmMapDetail.containsKey("lastDecisionPrice") && bmMapDetail.get("lastDecisionPrice") != null
										&& !StringUtility.isNullOrEmpty((bmMapDetail.get("lastDecisionPrice")).toString())) {
									DetailInfo detailinfo = (DetailInfo) bmMapDetail.get("remainder");
									List<MixMatchDetailInfo> ruleList = detailinfo.getEntryList();
									Double lastDecisionPrice = (Double) bmMapDetail.get("lastDecisionPrice");
									if (lastDecisionPrice > 0) {
										if (ruleList != null && !ruleList.isEmpty()) {
											for (MixMatchDetailInfo mmInfo : ruleList) {
												double truePrice = mmInfo.getTruePrice();
												if (lastDecisionPrice > 0 && PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
													sumDiscountPrice = sumDiscountPrice + lastDecisionPrice * mmInfo.getQuantity();
												}
												if(lastDecisionPriceRate > 0 && PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
													sumDiscountPrice = sumDiscountPrice + (100 - lastDecisionPriceRate) / 100 * mmInfo.getQuantity();
												}
												sumTruePrice = sumTruePrice + truePrice * mmInfo.getQuantity();
											}
										}
									}
								}
								discout02 = sumTruePrice - sumDiscountPrice;
							}
						}
						
					}
					
					if (discout02 > discout01) {
						for (MixMatchDetailInfo info : skuLst) {
							remainderList.remove(info);
						}
					}
				}
			}
    	}
    }
    
    
    /**
     * isHaveRemainderInfo
     * @param detail
     * 
     */
    private boolean isHaveRemainderInfo(String ruleNm, Map<String, Object> detailMap, MixMatchDetailInfo mmdi) {
    	boolean isHave = false;
    	if(detailMap.containsKey(ruleNm)) {
    		DetailInfo detailInfo = (DetailInfo) detailMap.get(ruleNm);
    		List<MixMatchDetailInfo> entryList = detailInfo.getEntryList();
    		for (MixMatchDetailInfo mmInfo : entryList) {
    			if (mmInfo.getEntryId().equals(mmdi.getEntryId()) || mmInfo.getSku().equals(mmdi.getSku())) {
    				isHave = true;
    				break;
    			}
        	}
    	}
    	return isHave;
    }
    
    /**
     * computeBmDiscount
     * @param ruleNm
     * @param detailMap
     * @param maxRule
     * 
     */
    private double[] computeBmDiscount(String ruleNm, Map<String, Object> detailMap, String maxRule) {
    	double[] retArr = new double[2];
    	double sumTruePrice = 0d;
		double sumDiscountPrice = 0d;
		if (detailMap.containsKey(ruleNm)) {
			DetailInfo ruleInfo = ((DetailInfo) detailMap.get(ruleNm));
			List<MixMatchDetailInfo> mmlst = ruleInfo.getEntryList();
			double averagePrice = ruleInfo.getAveragePrice();
			double currentConditionPriceRate = ruleInfo.getCurrentConditionPriceRate();
			boolean isRuleEnabled = true;
			double lastDecisionPrice = 0d;
			double lastDecisionPriceRate = 0;
			String bmDiscountType = (String) detailMap.get("discountClass");
			for (MixMatchDetailInfo mmInfo : mmlst) {
				sumTruePrice = sumTruePrice + mmInfo.getTruePrice() * mmInfo.getQuantity();
			}
			if ("".equals(maxRule)) {
				if(PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
					sumDiscountPrice = sumDiscountPrice + ruleInfo.getConditionPrice() * ruleInfo.getTimes();
				}else if(PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
					sumDiscountPrice = sumTruePrice * (100 - currentConditionPriceRate) / 100;
				}else {}
			} else {
				if(PromotionConstants.DISCOUNT_CLASS_3.equals(bmDiscountType)) {
					if (detailMap.containsKey("lastDecisionPrice") && detailMap.get("lastDecisionPrice") != null
							&& !StringUtility.isNullOrEmpty((detailMap.get("lastDecisionPrice")).toString())) {
						lastDecisionPrice = (Double) detailMap.get("lastDecisionPrice");
					}
					if (lastDecisionPrice > 0 && (averagePrice > lastDecisionPrice)) {
						isRuleEnabled = false;
						sumDiscountPrice = sumDiscountPrice + lastDecisionPrice * ruleInfo.getConditionCount() * ruleInfo.getTimes();
					}
					
					if (isRuleEnabled) {
						sumDiscountPrice = sumDiscountPrice + ruleInfo.getConditionPrice() * ruleInfo.getTimes();
					}
				}
				
				if(PromotionConstants.DISCOUNT_CLASS_1.equals(bmDiscountType)) {
					if (detailMap.containsKey("lastDecisionPriceRate") && detailMap.get("lastDecisionPriceRate") != null
							&& !StringUtility.isNullOrEmpty((detailMap.get("lastDecisionPriceRate")).toString())) {
						lastDecisionPriceRate = (Double) detailMap.get("lastDecisionPriceRate");
					}
					if (lastDecisionPriceRate > 0 && (currentConditionPriceRate < lastDecisionPriceRate)) {
						isRuleEnabled = false;
						sumDiscountPrice = sumTruePrice * (100 - lastDecisionPriceRate) / 100;
					}
					if (isRuleEnabled) {
						sumDiscountPrice = sumTruePrice * (100 - currentConditionPriceRate) / 100;
					}
				}


			}
			
		}
    	
    	retArr[0] = sumTruePrice;
    	retArr[1] = sumDiscountPrice;
    	
    	return retArr;
    }
    
    /**
     * setBmQuantity
     * @param ruleNm
     * @param map
     * @param bmDetailMap
     * 
     */
    private void setBmQuantity(String ruleNm, Map<String, Object> map, Map<String, List<MixMatchDetailInfo>> bmDetailMap) {
    	if(map.containsKey(ruleNm)) {
    		DetailInfo detailInfo = (DetailInfo) map.get(ruleNm);
    		List<MixMatchDetailInfo> entryList = detailInfo.getEntryList();
    		for (MixMatchDetailInfo mmInfo : entryList) {
    			for (Entry<String, List<MixMatchDetailInfo>> entry1 : bmDetailMap.entrySet()) {
    				for (MixMatchDetailInfo entry2 : entry1.getValue()) {
    					if (entry2.getEntryId().equals(mmInfo.getEntryId())) {
    						int surplus = entry2.getQuantity() - mmInfo.getQuantity();
    						entry2.setQuantity(surplus >= 0 ? surplus : 0);
    					}
    				}
    			}
        	}
    	}
    }
    
    /**
     * clone bmDetailMap
     * @return
     */
    private Map<String, List<MixMatchDetailInfo>> clonebmDetailMap() {
    	Map<String, List<MixMatchDetailInfo>> retMap = new HashMap<>();
    	for (Entry<String, List<MixMatchDetailInfo>> mEntry : bmDetailMap.entrySet()) {
    		List<MixMatchDetailInfo> newList = new ArrayList<>();
    		List<MixMatchDetailInfo> oldList = mEntry.getValue();
    		for (MixMatchDetailInfo info : oldList) {
    			MixMatchDetailInfo newInfo = new MixMatchDetailInfo();
				newInfo.setEntryId(info.getEntryId());
				newInfo.setMmNo(info.getMmNo());
				newInfo.setSku(info.getSku());
				newInfo.setOriginalTaxId(info.getOriginalTaxId());
				newInfo.setQuantity(info.getQuantity());
				newInfo.setTaxId(info.getTaxId());
				newInfo.setTruePrice(info.getTruePrice());
				List<PriceMMInfo> mmList = new ArrayList<>();
				for (PriceMMInfo mmInfo : info.getPriceMMInfoList()) {
					PriceMMInfo newMmInfo = new PriceMMInfo();
					newMmInfo = (PriceMMInfo) mmInfo.clone();
					mmList.add(newMmInfo); 
				}
				newInfo.setPriceMMInfoList(mmList);
				newList.add(newInfo);
    		}
    		retMap.put(mEntry.getKey(), newList);
		}
		
    	return retMap;
    }
    
    /**
     * sort by discount price
     * @param map
     * @return
     */
    private List<Map<String, Object>> sortByValueAscending(Map<String, Map<String, Object>> map){
        List<Map<String, Object>> list = new LinkedList<>();
        for(Entry<String, Map<String, Object>> mEntry : map.entrySet()) {
        	Map<String, Object> detail = mEntry.getValue();
        	if(detail.containsKey("rule1") || detail.containsKey("rule2") || detail.containsKey("rule3")) {
        		list.add(detail);
        	}
        }
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				double compare = (Double)o2.get("discountPrice") - (Double)o1.get("discountPrice");
				if (compare > 0) {
					return 1;
				} else if (compare == 0) {
					Integer mmNo2 = Integer.valueOf(((o2.get("keyValue")).toString()).split("_")[0]);
					Integer mmNo1 = Integer.valueOf(((o1.get("keyValue")).toString()).split("_")[0]);
					return (mmNo2 - mmNo1);
				} else {
					return -1;
				}
			}
		});

		return list;
	}

    /**
     * get the item of mixMatch info
     * @param mmNo mixMatchcode
     * @param isDelete  the flag of delete handle
     * @return the info of the mixMatch
     */
    public Map<String, Map<String, Object>> getMixMatchMap(String mmNo_taxId,String isDeleteOrUpdate,List<MixMatchDetailInfo> ruleList3) {
        Map<String, Map<String, Object>> detailMap = new HashMap<String, Map<String, Object>>();
        // the sum of quantity
        int sumCount = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        List<MixMatchDetailInfo> ruleList2 = new ArrayList<MixMatchDetailInfo>();
        List<MixMatchDetailInfo> ruleList1 = new ArrayList<MixMatchDetailInfo>();
        List<MixMatchDetailInfo> ruleList = new ArrayList<MixMatchDetailInfo>();
        String mmNo = mmNo_taxId.split("_")[0];
        MixMatchRule rule = bmRuleMap.get(mmNo);
        // List<MixMatchDetailInfo> ruleList3 = new ArrayList<MixMatchDetailInfo>();
        // ruleList3 = bmDetailMap.get(mmNo_taxId);
        map.put("note", rule.getNote());
        // sort by price
        Collections.sort(ruleList3);
        Set<String> tmItemSet = new HashSet<>();
        if(rule.getSubNum1() == PromotionConstants.CUSTOMER_SALE_INT) {
        	tmItemSet = itemsInDiscount("TM");
        }
        // loop the list matching rule3
        for (MixMatchDetailInfo mixMatchDetailInfo3 : ruleList3) {
        	// if the coupon has the tm then the saleItem can't make bm
        	if(tmItemSet.size() > 0 && tmItemSet.contains(mixMatchDetailInfo3.getEntryId())) {
        		continue;
        	}
            MixMatchDetailInfo info = new MixMatchDetailInfo();
            info.setEntryId(mixMatchDetailInfo3.getEntryId());
            info.setTruePrice(mixMatchDetailInfo3.getTruePrice());
            info.setTaxId(mixMatchDetailInfo3.getTaxId());
            info.setSku(mixMatchDetailInfo3.getSku());
            info.setPriceMMInfoList(mixMatchDetailInfo3.getPriceMMInfoList());
            int remainder = mixMatchDetailInfo3.getQuantity();
            if (!map.containsKey("promotionInfo") && null != mixMatchDetailInfo3.getPriceMMInfoList() && mixMatchDetailInfo3.getPriceMMInfoList().size() > 0) {
            	for (PriceMMInfo pmInfo: mixMatchDetailInfo3.getPriceMMInfoList()) {
            		if (mmNo.equals(pmInfo.getMMNo())) {
            			map.put("promotionInfo", pmInfo);
            			break;
            		}
            	}
            }
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
                ruleentryinfo1.setSku(mixMatchDetailInfo3.getSku());
                ruleentryinfo1.setPriceMMInfoList(mixMatchDetailInfo3.getPriceMMInfoList());
                DetailInfo detailinfo = new DetailInfo();

                
                detailinfo.setConditionCount(rule.getConditionCount3());
                // detailinfo.setRuleName(mmNo_taxId + "-rule3");
                // DecisionPrice > 0
                int times = 0;
                times = sumCount / rule.getConditionCount3();
                sumCount = sumCount % rule.getConditionCount3();
                remainder = mixMatchDetailInfo3.getQuantity() - sumCount;
                ruleentryinfo1.setQuantity(remainder);
                ruleList2.add(ruleentryinfo1);
                detailinfo.addEntryList(ruleList2);
                // if(null != rule.getDecisionPrice3()){
                if(PromotionConstants.DISCOUNT_CLASS_3.equals(rule.getDiscountClass())) {
                	map.put("lastDecisionPrice", rule.getDecisionPrice3());
                    detailinfo.setCurrentDecisionPrice(nullToZero(rule.getDecisionPrice3()));
                    detailinfo.setConditionPrice(rule.getConditionPrice3());
                }
                if(PromotionConstants.DISCOUNT_CLASS_1.equals(rule.getDiscountClass())) {
                	map.put("lastDecisionPriceRate", rule.getDecisionRate3());
                	detailinfo.setCurrentConditionPriceRate(nullToZero(rule.getConditionRate3()));
                	detailinfo.setCurrentDecisionPriceRate(nullToZero(rule.getDecisionRate3()));
                }
                    
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
            info.setSku(mixMatchDetailInfo2.getSku());
            info.setPriceMMInfoList(mixMatchDetailInfo2.getPriceMMInfoList());
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
                ruleentryinfo1.setSku(mixMatchDetailInfo2.getSku());
                ruleentryinfo1.setPriceMMInfoList(mixMatchDetailInfo2.getPriceMMInfoList());
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
                if(PromotionConstants.DISCOUNT_CLASS_3.equals(rule.getDiscountClass())) {
                	detailinfo.setConditionPrice(rule.getConditionPrice2());
                	detailinfo.setAveragePrice(rule.getAveragePrice2());
                	detailinfo.setCurrentDecisionPrice(nullToZero(rule.getDecisionPrice2()));
                	if (rule.getDecisionPrice2() != null && !map.containsKey("lastDecisionPrice")) {
                		map.put("lastDecisionPrice", rule.getDecisionPrice2());
                	}
                }
                if(PromotionConstants.DISCOUNT_CLASS_1.equals(rule.getDiscountClass())) {
                    if (rule.getDecisionRate2() != null && !map.containsKey("lastDecisionPriceRate")) {
                        map.put("lastDecisionPriceRate", rule.getDecisionRate2());
                    }
                	detailinfo.setCurrentConditionPriceRate(nullToZero(rule.getConditionRate2()));
                	detailinfo.setCurrentDecisionPriceRate(nullToZero(rule.getDecisionRate2()));
                }
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
            info.setSku(mixMatchDetailInfo1.getSku());
            info.setPriceMMInfoList(mixMatchDetailInfo1.getPriceMMInfoList());
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
                ruleentryinfo1.setSku(mixMatchDetailInfo1.getSku());
                ruleentryinfo1.setPriceMMInfoList(mixMatchDetailInfo1.getPriceMMInfoList());
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
                
                if(PromotionConstants.DISCOUNT_CLASS_3.equals(rule.getDiscountClass())) {
                    detailinfo.setCurrentDecisionPrice(nullToZero(rule.getDecisionPrice1()));
                    if (rule.getDecisionPrice1() != null && !map.containsKey("lastDecisionPrice")) {
                        map.put("lastDecisionPrice", rule.getDecisionPrice1());
                    }
                    detailinfo.setAveragePrice(rule.getAveragePrice1());
                    detailinfo.setConditionPrice(rule.getConditionPrice1());
                }
                if(PromotionConstants.DISCOUNT_CLASS_1.equals(rule.getDiscountClass())) {
                    if (rule.getDecisionRate1() != null && !map.containsKey("lastDecisionPriceRate")) {
                        map.put("lastDecisionPriceRate", rule.getDecisionRate1());
                    }
                	detailinfo.setCurrentConditionPriceRate(nullToZero(rule.getConditionRate1()));
                	detailinfo.setCurrentDecisionPriceRate(nullToZero(rule.getDecisionRate1()));
                }
                
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
        if(!map.containsKey("lastDecisionPriceRate")){
            map.put("lastDecisionPriceRate", "");
        }
            DetailInfo detailinfo = new DetailInfo();
            detailinfo.setEntryList(ruleList);
            map.put("remainder", detailinfo);
        if("true".equals(isDeleteOrUpdate) && !map.containsKey("rule1") && !map.containsKey("rule2") && !map.containsKey("rule3")){
            map.put("hasMixMatch", "false");
        }
        map.put("keyValue", mmNo_taxId);
        map.put("discountClass", rule.getDiscountClass());
        map.put("SubNum1", rule.getSubNum1());
        detailMap.put(mmNo_taxId, map);
        return detailMap;
    }
    
    private double nullToZero(Double d) {
    	if(null == d) {
    		return 0;
    	} else {
    		return d;
    	}
    }
    
    /**
     *  get all Items in the discount
     * @param discountName the discount name for example BM  PM  TM
     * @return the items in discount
     */
    public Set<String> itemsInDiscount(String discountName){
    	Set<String> tmItemSet = new HashSet<>();
    	if(couponMap.containsKey(discountName)) {
    		Set<String> tmCodeSet = couponMap.get(discountName);
    		for(String tmCode: tmCodeSet) {
    			if(promotionItemCouponMap.containsKey(tmCode)) {
    				tmItemSet.addAll(promotionItemCouponMap.get(tmCode));
    			}
    		}
    	}
    	if(promotionItemCouponMap.containsKey("mixItem")) {
    		tmItemSet.addAll(promotionItemCouponMap.get("mixItem"));
    	}
    	return tmItemSet;
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
        List<String> delBmKey = new ArrayList<>();
        for(Map.Entry<String, Map<String, Object>> entryMap : map.entrySet()){
        	mmNo_taxId = entryMap.getKey();
            Map<String, Object> childMap = entryMap.getValue();
            double discountPrice = (double) childMap.get("discountPrice");
            if(discountPrice < 0){
            	delBmKey.add(mmNo_taxId);
            }
        }
        for (String key : delBmKey) {
        	map.remove(key);
        }
        if (map.isEmpty()) {
        	return true;
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
        double price = 0.0;
        DetailInfo remainder = (DetailInfo)map.get("remainder");
        if( null != remainder) {
            List<MixMatchDetailInfo> ruleList = remainder.getEntryList();

            for(MixMatchDetailInfo info : ruleList){
                if(lastDecisionPrice > 0){
                    price += info.getQuantity() * lastDecisionPrice;
                }else{
                    price += info.getQuantity() * info.getTruePrice();
                }
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
    
    public Map<String,Set<String>> getPromotionItemCouponMap() {
		return promotionItemCouponMap;
	}
    
	public Map<String, Set<String>> getCouponMap() {
		return couponMap;
	}
    
	public Map<String, Map<String, List<PmItemInfo>> > getPmMap() {
		return pmMap;
	}
	
    public Map<String, MixMatchDetailInfo> getBmMapByEntryid() {
		return bmMapByEntryid;
	}
}
