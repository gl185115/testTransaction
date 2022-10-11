package ncr.res.mobilepos.promotion.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.promotion.model.DetailInfo;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.MixMatchRule;
import ncr.res.mobilepos.promotion.model.Sale;

/**
 * The Mix and Match Promotiuon Resource.
 *
 */
public class MixMatchPromotionResource {
	
	private List<MixSale> mixSales;
	
	private List<MixMatch> mixMatchs;

	class MixMatch {
		private List<MixRule> ruleList;
		private List<Sale> sales;
		private Map<String, List<Sale>> completeMixMatch;
		private double salePrice;
		private double discountPrice;
		private int  allQuantity;
		private boolean hasLastPrice;
		private double lastPrice;
		private String mmNo;
		private Map<String, Sale> zeroQuantityItems;
		
		public double getDiscountPrice() {
			return discountPrice;
		}

		public void setDiscountPrice(double discountPrice) {
			this.discountPrice = discountPrice;
		}

		public Map<String, Sale> getZeroQuantityItems() {
			return zeroQuantityItems;
		}

		public boolean isHasLastPrice() {
			return hasLastPrice;
		}

		public void setHasLastPrice(boolean hasLastPrice) {
			this.hasLastPrice = hasLastPrice;
		}

		public double getLastPrice() {
			return lastPrice;
		}

		public void setLastPrice(double lastPrice) {
			this.lastPrice = lastPrice;
		}

		public int getAllQuantity() {
			return allQuantity;
		}
		
		public Map<String, List<Sale>> getCompleteMixMatch() {
			return completeMixMatch;
		}

		public void setCompleteMixMatch(Map<String, List<Sale>> completeMixMatch) {
			this.completeMixMatch = completeMixMatch;
		}

		public void setAllQuantity(int allQuantity) {
			this.allQuantity = allQuantity;
		}

		public List<MixRule> getRuleList() {
			return ruleList;
		}

		public void setRuleList(List<MixRule> ruleList) {
			this.ruleList = ruleList;
		}

		public List<Sale> getSales() {
			return sales;
		}

		public double getSalePrice() {
			return salePrice;
		}

		public void setSalePrice(double salePrice) {
			this.salePrice = salePrice;
		}

		public String getMmNo() {
			return mmNo;
		}

		public void setMmNo(String mmNo) {
			this.mmNo = mmNo;
		}
	}

	class MixRule {
		private String ruleName;
		
		private double price;

		private double lastSalePrice;

		private int conditionQuantity;
		
		public String getRuleName() {
			return ruleName;
		}

		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getLastSalePrice() {
			return lastSalePrice;
		}

		public void setLastSalePrice(double lastSalePrice) {
			this.lastSalePrice = lastSalePrice;
		}

		public int getConditionQuantity() {
			return conditionQuantity;
		}

		public void setConditionQuantity(int conditionQuantity) {
			this.conditionQuantity = conditionQuantity;
		}
	}

	class MixSale {
		private String itemEntryId;

		private int quantity;

		private List<String> mixMatchs;

		public String getItemEntryId() {
			return itemEntryId;
		}

		public void setItemEntryId(String itemEntryId) {
			this.itemEntryId = itemEntryId;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public List<String> getMixMatchs() {
			return mixMatchs;
		}

		public void setMixMatchs(List<String> mixMatchs) {
			this.mixMatchs = mixMatchs;
		}
	}
  public  void  createBm() throws IllegalAccessException, InvocationTargetException {
	  mixMatchs= new ArrayList<MixMatch>();
	  for(MixMatch item : mixMatchs) {
		   makeMixMatch(item);
	  }
  }

private void makeMixMatch(MixMatch item) throws IllegalAccessException, InvocationTargetException {
	
	List<Sale> deleteList = new ArrayList<>();
	int allQuantity = 0;
	for(Sale sale : item.getSales()) {
		if(sale.getQuantity() == 0) {
			deleteList.add(sale);
		}else {
			allQuantity += sale.getQuantity();
		}
	}
	
	item.getSales().removeAll(deleteList);
	item.setAllQuantity(allQuantity);
	double salePrice = 0;
	List<MixRule> ruleList = item.getRuleList();
	Map<String, List<Sale>> map = new HashMap<String, List<Sale>>();
	 List<Sale> conditionList = new ArrayList<>();
	  for(MixRule rule : ruleList) {
		  int conditionQuantity =  rule.getConditionQuantity();
		  if(item.getAllQuantity() < rule.getConditionQuantity()) {
			  continue;
		  }else {
			  int quantity = 0;
			  int index = 0;
			  boolean  hasLastPriceflag = rule.getLastSalePrice() > 0;
			  boolean useLastPriceFlag = false;
			  List<Sale> currentList = new ArrayList<>();
			  if(conditionList.size() > 0) {
				  Collections.copy(currentList, conditionList);;
			  }else {
				  currentList = item.getSales();
			  }
			  conditionList = new ArrayList<>();
			  for(Sale sale : currentList) {
				  quantity += sale.getQuantity();
				  if(map.containsKey("hasRule") && hasLastPriceflag) {
					  useLastPriceFlag = true;
				  }
				  if( quantity >= conditionQuantity && !useLastPriceFlag) {
					  if(quantity > conditionQuantity) {
						  quantity = quantity - conditionQuantity;
						  Sale nSale = new Sale(sale);
						  //BeanUtils.copyProperties(nSale, sale);
						  nSale.setQuantity(sale.getQuantity() - quantity);
						  conditionList.add(nSale);
						  map.put(rule.getRuleName() + index++, conditionList);
						  map.put("hasRule", new ArrayList<>());
						  conditionList = new ArrayList<>();
						  nSale = new Sale(sale);
						  //BeanUtils.copyProperties(nSale, sale);
						  nSale.setQuantity(quantity);
						  conditionList.add(nSale);
					  } else {
						  conditionList.add(sale);
					  }

				  } else {
					  conditionList.add(sale);
				  }
			  }
			  if(hasLastPriceflag && map.size() > 2) {
				  int size = map.size() - 1;
				  double currentRulePrice = rule.getPrice() * size;
				  salePrice += currentRulePrice;
				  if(useLastPriceFlag) {
					  item.setHasLastPrice(true);
					  item.setLastPrice(rule.getLastSalePrice());
				  }
				  break;
			  }
		  }
	  }
	  map.put("remainder", conditionList);
	  for(Sale s :conditionList) {
		  if(item.hasLastPrice) {
			  salePrice += s.getQuantity() * item.getLastPrice();
		  }else {
			  salePrice += s.getQuantity() + s.getActualSalesUnitPrice();
		  }
	  }
	  item.setSalePrice(salePrice);
	  item.setCompleteMixMatch(map);
}
  
  public void cleanBm() throws IllegalAccessException, InvocationTargetException {
	  mixSales = new ArrayList<>();
	  mixMatchs= new ArrayList<MixMatch>();
	  List<Sale> otherSale = new ArrayList<>();
	  List<String> mms = new ArrayList<>();
	  for(MixSale sale : mixSales) {
		  int quantity = sale.getQuantity();
		  int currentRemainderQuantity = quantity;
		  if(otherSale.size() > 0) {
			  for(Sale s : otherSale) {
				  if(s.getItemEntryId().equals(sale.getItemEntryId())) {
					  currentRemainderQuantity = quantity - s.getQuantity();
					  break;
				  }
			  }
		  }
		  List<String> mmNos = sale.getMixMatchs();
		  List<MixMatch> nMixMatchs = new ArrayList<>();
		  for(MixMatch m : mixMatchs) {
			  for(String ss : mms) {
				  if(!ss.equals(m.getMmNo())) {
					  for(String s : mmNos) {
						  if(s.equals(m.getMmNo()) && m.getSalePrice() >= 0 && m.getCompleteMixMatch().size() > 2) {
							  if(currentRemainderQuantity != quantity) {
								  for(Sale item : m.getSales()) {
									  if(item.getItemEntryId().equals(sale.getItemEntryId())) {
										  item.setQuantity(currentRemainderQuantity);
									  }
									  break;
								  }
								  makeMixMatch(m);
								  if(m.getCompleteMixMatch().size() > 2) {
									  nMixMatchs.add(m);
								  }
							  }else {
								  nMixMatchs.add(m);
							  }
						  }  
					  }
				  }
			  }
		  }
		  Collections.sort(nMixMatchs,new Comparator<MixMatch>() {
			@Override
			public int compare(MixMatch o1, MixMatch o2) {
				double a = o2.getSalePrice() - o1.getSalePrice();
				if(a > 0) {
					return 1;
				} else if(a == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		  int remainderQuantity = currentRemainderQuantity;
		  for(MixMatch m : nMixMatchs) {
			  if(remainderQuantity != currentRemainderQuantity) {
				  List<Sale> currentSales = m.getSales();
				  for(Sale s : currentSales) {
					  if(s.getItemEntryId().equals(sale.getItemEntryId())) {
						  s.setQuantity(remainderQuantity);
						  makeMixMatch(m);
						  break;
					  }
				  }
			  }else {
				  mms.add(m.getMmNo());
			  }
			  
			  Map<String, List<Sale>> cm = m.getCompleteMixMatch();
			  if(cm.size() > 2) {
				  int useQuantity = 0;
				  for(Map.Entry<String, List<Sale>> mEntry : cm.entrySet()) {
					  List<Sale> rl = mEntry.getValue();
					  if(!"hasRule".equals(mEntry.getKey()) && "remainder".equals(mEntry.getKey()) && m.hasLastPrice) {
						  for(Sale s : rl) {
							  if(s.getItemEntryId().equals(sale.getItemEntryId())) {
								  useQuantity += s.getQuantity();
							  }else {
								  boolean hasSale = false;
								  for(Sale ss : otherSale) {
									  if(ss.getItemEntryId().equals(s.getItemEntryId())) {
										  ss.setQuantity(ss.getQuantity() + s.getQuantity());
										  hasSale = true;
										  break;
									  }
								  }
								  if(hasSale) {
									  otherSale.add(s);
								  }

							  }
						  }
					  }
				  }
				  
				  if(remainderQuantity > useQuantity) {
					  remainderQuantity = remainderQuantity - useQuantity;
				  }
			  }

		  }
		  
		  

	  }
  }
  
  
  public void addSale(Sale sale) {
	  
  }
 
  class MixmatchRule{
		private String mmNo;
		private int conditionCount;
		private double conditionPrice;
		private Double decisionPrice;
		private double averagePrice;
		private String note;
		private Integer conditionRate;
		private Integer decisionRate;
		private String  discountClass;
		private String ruleName;
		
		public String getRuleName() {
			return ruleName;
		}
		public void setRuleName(String ruleName) {
			this.ruleName = ruleName;
		}
		public String getMmNo() {
			return mmNo;
		}
		public void setMmNo(String mmNo) {
			this.mmNo = mmNo;
		}
		public int getConditionCount() {
			return conditionCount;
		}
		public void setConditionCount(int conditionCount) {
			this.conditionCount = conditionCount;
		}
		public double getConditionPrice() {
			return conditionPrice;
		}
		public void setConditionPrice(double conditionPrice) {
			this.conditionPrice = conditionPrice;
		}
		public Double getDecisionPrice() {
			return decisionPrice;
		}
		public void setDecisionPrice(Double decisionPrice) {
			this.decisionPrice = decisionPrice;
		}
		public double getAveragePrice() {
			return averagePrice;
		}
		public void setAveragePrice(double averagePrice) {
			this.averagePrice = averagePrice;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public Integer getConditionRate() {
			return conditionRate;
		}
		public void setConditionRate(Integer conditionRate) {
			this.conditionRate = conditionRate;
		}
		public Integer getDecisionRate() {
			return decisionRate;
		}
		public void setDecisionRate(Integer decisionRate) {
			this.decisionRate = decisionRate;
		}
		public String getDiscountClass() {
			return discountClass;
		}
		public void setDiscountClass(String discountClass) {
			this.discountClass = discountClass;
		}
  }
  
  public Map<String, Map<String, Object>> getMixMatchMap(String mmNo_taxId,String isDeleteOrUpdate,List<MixMatchDetailInfo> ruleList3) {
      Map<String, Map<String, Object>> detailMap = new HashMap<String, Map<String, Object>>();
      Map<String,MixMatchRule> bmRuleMap = new HashMap<String,MixMatchRule>();
      // the sum of quantity
      int sumCount = 0;
      Map<String, Object> map = new HashMap<String, Object>();
      List<MixMatchDetailInfo> ruleList2 = new ArrayList<MixMatchDetailInfo>();
      List<MixMatchDetailInfo> ruleList1 = new ArrayList<MixMatchDetailInfo>();
      List<MixMatchDetailInfo> ruleList = new ArrayList<MixMatchDetailInfo>();
      MixMatchRule rule = bmRuleMap.get(mmNo_taxId.split("_")[0]);
      map.put("note", rule.getNote());
      // sort by price
      Collections.sort(ruleList3);
      
      // loop the list matching rule3
      for (MixMatchDetailInfo mixMatchDetailInfo3 : ruleList3) {
          MixMatchDetailInfo info = new MixMatchDetailInfo();
          info.setEntryId(mixMatchDetailInfo3.getEntryId());
          info.setTruePrice(mixMatchDetailInfo3.getTruePrice());
          info.setTaxId(mixMatchDetailInfo3.getTaxId());
          info.setSku(mixMatchDetailInfo3.getSku());
          info.setPriceMMInfoList(mixMatchDetailInfo3.getPriceMMInfoList());
          int remainder = mixMatchDetailInfo3.getQuantity();
          if (!map.containsKey("promotionInfo") && null != mixMatchDetailInfo3.getPriceMMInfoList() && mixMatchDetailInfo3.getPriceMMInfoList().size() > 0) {
          	for (PriceMMInfo pmInfo: mixMatchDetailInfo3.getPriceMMInfoList()) {
          		if (mmNo_taxId.split("_")[0].equals(pmInfo.getMMNo())) {
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
              int times = 0;
              times = sumCount / rule.getConditionCount3();
              sumCount = sumCount % rule.getConditionCount3();
              remainder = mixMatchDetailInfo3.getQuantity() - sumCount;
              ruleentryinfo1.setQuantity(remainder);
              ruleList2.add(ruleentryinfo1);
              detailinfo.addEntryList(ruleList2);
              if("3".equals(rule.getDiscountClass())) {
              	map.put("lastDecisionPrice", rule.getDecisionPrice3());
                  detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice3() != null ? rule.getDecisionPrice3() : 0);
                  detailinfo.setConditionPrice(rule.getConditionPrice3());
              }
              if("1".equals(rule.getDiscountClass())) {
              	map.put("lastDecisionPriceRate", rule.getDecisionRate3());
              	double rateConditionPrice = 0;
              	for (MixMatchDetailInfo mixInfo : ruleList2) {
              		rateConditionPrice += mixInfo.getTruePrice() * mixInfo.getQuantity();
              	}
              	detailinfo.setConditionPrice(rateConditionPrice * (100 - rule.getConditionRate3()) / 100);
              }
              detailinfo.setAveragePrice(rule.getAveragePrice3());
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
              detailinfo.setConditionCount(rule.getConditionCount2());
                  detailinfo.setAveragePrice(rule.getAveragePrice2());
              // }
              int times = sumCount / rule.getConditionCount2();
              sumCount = sumCount % rule.getConditionCount2();
              remainder = mixMatchDetailInfo2.getQuantity() - sumCount;
              ruleentryinfo1.setQuantity(remainder);
              ruleList1.add(ruleentryinfo1);
              detailinfo.addEntryList(ruleList1);
              if("3".equals(rule.getDiscountClass())) {
                  detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice2() != null ? rule.getDecisionPrice2() : 0);
                  if (rule.getDecisionPrice2() != null && !map.containsKey("lastDecisionPrice")) {
                      map.put("lastDecisionPrice", rule.getDecisionPrice2());
                  }
              }
              if("1".equals(rule.getDiscountClass())) {
                  if (rule.getDecisionRate2() != null && !map.containsKey("lastDecisionPriceRate")) {
                      map.put("lastDecisionPriceRate", rule.getDecisionRate2());
                  }
              	double rateConditionPrice = 0;
              	for (MixMatchDetailInfo mixInfo : ruleList1) {
              		rateConditionPrice += mixInfo.getTruePrice() * mixInfo.getQuantity();
              	}
              	detailinfo.setConditionPrice(rateConditionPrice * (100 - rule.getConditionRate2()) / 100);
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
              detailinfo.setConditionPrice(rule.getConditionPrice1());
              if (rule.getDecisionPrice1() != null && !map.containsKey("lastDecisionPrice")) {
                  map.put("lastDecisionPrice", rule.getDecisionPrice1());
              }
              detailinfo.setAveragePrice(rule.getAveragePrice1());
              int times = sumCount / rule.getConditionCount1();
              sumCount = sumCount % rule.getConditionCount1();
              remainder = mixMatchDetailInfo1.getQuantity() - sumCount;
              ruleentryinfo1.setQuantity(remainder);
              ruleList.add(ruleentryinfo1);
              detailinfo.addEntryList(ruleList);
              
              if("3".equals(rule.getDiscountClass())) {
                  detailinfo.setCurrentDecisionPrice(rule.getDecisionPrice2() != null ? rule.getDecisionPrice2() : 0);
                  if (rule.getDecisionPrice1() != null && !map.containsKey("lastDecisionPrice")) {
                      map.put("lastDecisionPrice", rule.getDecisionPrice1());
                  }
              }
              if("1".equals(rule.getDiscountClass())) {
                  if (rule.getDecisionRate1() != null && !map.containsKey("lastDecisionPriceRate")) {
                      map.put("lastDecisionPriceRate", rule.getDecisionRate1());
                  }
              	double rateConditionPrice = 0;
              	for (MixMatchDetailInfo mixInfo : ruleList) {
              		rateConditionPrice += mixInfo.getTruePrice() * mixInfo.getQuantity();
              	}
              	detailinfo.setConditionPrice(rateConditionPrice * (100 - rule.getConditionRate1()) / 100);
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
      detailMap.put(mmNo_taxId, map);
      return detailMap;
  }
}
