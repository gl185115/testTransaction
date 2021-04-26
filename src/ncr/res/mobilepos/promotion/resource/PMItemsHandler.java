/**
 * 
 */
package ncr.res.mobilepos.promotion.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.dao.IPMInfoDAO;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.model.DetailInfo;
import ncr.res.mobilepos.promotion.model.ItemForPm;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.PmDiscountInfo;
import ncr.res.mobilepos.promotion.model.PmItemInfo;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.model.Sale;

/**
 * @author
 * 
 * handle  Items for the PM
 *
 */
public class PMItemsHandler {
	
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	private String progname = "PMItemsHandler";

	/**
	 * cache and count PM
	 * @param pmMap the Cache of PMlist
	 * @param saleItem current itemInfo
	 * @param promotion the mixMatch map
	 * @param pmList item PMlist
	 * @throws DaoException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public void makePmInfo(TerminalItem terminalItem, Promotion promotion, boolean isCoupon) throws DaoException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> preMap = terminalItem.getPreMap();

		Map<String, Map<String, Object>> bmMap = null;
		if(null != promotion.getMap() && promotion.getMap().size() > 0) {
			bmMap = promotion.getMap();
		}else {
			if(null != preMap && preMap.containsKey("BM")) {
				bmMap =  (Map<String, Map<String, Object>>) preMap.get("BM");
			}
		}
		Map<String, Map<String, List<PmItemInfo>>> newMap  = getPmItemWithOutBm(bmMap, terminalItem, isCoupon);
		Map<String, PmDiscountInfo> map = cleanMap(newMap,terminalItem);
		if(map.size() > 0) {
			promotion.setPmMap(map);
		}
	}
	
	public void makePmInfo(TerminalItem terminalItem, Promotion promotion) throws DaoException, IllegalAccessException, InvocationTargetException {
		makePmInfo(terminalItem,promotion,false);
	}
	
	@SuppressWarnings("unchecked")
	public List<PmItemInfo> getPmByItemId(TerminalItem terminalItem, Sale saleItem,
			Map<String, String> paraMap, DAOFactory daoFactory) throws DaoException, IllegalAccessException, InvocationTargetException{
		String functionName = "PMItemsHandler.getPmByItemId";
		List<PmItemInfo> listPm = null;
		IPMInfoDAO pmDao = daoFactory.getPMInfoDAO();
		Map<String, Sale> itemCacheMap = terminalItem.getItemCacheMap();
		if(itemCacheMap.containsKey(saleItem.getItemId())) {
			List<PmItemInfo> oldList = (List<PmItemInfo>) itemCacheMap.get(saleItem.getItemId()).getPmInfoList();
			listPm = new ArrayList<>();
			if(null != oldList && oldList.size() > 0) {
				for(PmItemInfo info : oldList) {
					PmItemInfo nInfo = new PmItemInfo();
					BeanUtils.copyProperties(nInfo, info);
					listPm.add(nInfo);
				}
			}
		} else {
			listPm = pmDao.getPmItemInfo(paraMap.get("companyId"), paraMap.get("retailStoreId"), paraMap.get("itemId"), paraMap.get("businessDate"));
		}
		if(null != listPm && listPm.size() > 0) {
			saleItem.setPmInfoList(listPm);
		}
		return listPm;
	}
	

	public boolean cachePmInfo(
			List<PmItemInfo> listPm,
			TerminalItem terminalItem,
			Sale saleItem,
			boolean isItemEntry,
			boolean usePromotion,
			double changePrice)
			throws DaoException, IllegalAccessException, InvocationTargetException {
		Map<String, ItemForPm> itemOfManyPmMap = terminalItem.getItemOfManyPm();
		Map<String, Map<String, List<PmItemInfo>>> pmMap = terminalItem.getPmMap();
		
		if(null != listPm && listPm.size() > 0){
			Sale sale = new Sale();
			BeanUtils.copyProperties(sale, saleItem);
			List<PricePromInfo> pricePromInfos = saleItem.getPricePromList();
			if(null != saleItem.getPriceUrgentInfo()) {
				sale.setActualSalesUnitPrice(saleItem.getPriceUrgentInfo().getUrgentPrice());
			}
			if(saleItem.getPriceUrgentInfo() == null && null != pricePromInfos && pricePromInfos.size() > 0) {
				boolean hasPromPrice = true;
				double  price = 0;
				for(PricePromInfo info : pricePromInfos) {
			    	if(PromotionConstants.DISCOUNT_CLASS_1.equals(info.getDiscountClass()) || PromotionConstants.DISCOUNT_CLASS_2.equals(info.getDiscountClass())) {
			    		hasPromPrice = false;
			    		break;
			    	}
			    	price = info.getSalesPrice();
				}
				if(hasPromPrice) {
					sale.setActualSalesUnitPrice(price);
				}
			}
			if(changePrice > 0 && !usePromotion) {
				sale.setActualSalesUnitPrice(changePrice);
			}
			if(itemOfManyPmMap.containsKey(sale.getItemEntryId())) {
				ItemForPm itemForPm = itemOfManyPmMap.get(sale.getItemEntryId());
				if(isItemEntry) {
					itemForPm.setQuantity(itemForPm.getQuantity() + sale.getQuantity());
				} else {
					itemForPm.setQuantity(sale.getQuantity());
				}
			}else {
				ItemForPm itemForPm = new ItemForPm();
				itemForPm.setQuantity(sale.getQuantity());
				itemForPm.setKeyList(listPm);
				itemForPm.setItemEntryId(sale.getItemEntryId());
				itemForPm.setSalePrice(sale.getActualSalesUnitPrice());
				itemForPm.setItemId(saleItem.getItemId());
				itemForPm.setTaxId(sale.getTaxId());
				itemOfManyPmMap.put(sale.getItemEntryId(), itemForPm);
			}
			for(PmItemInfo info : listPm) {
				if(isItemEntry) {
					changeCachePm(pmMap, sale, info, false);
				} else {
					changeCachePm(pmMap, sale, info, true);
				}
				
			}
			return true;
		} else {
			return false;
		}
	}

	private List<MixMatchDetailInfo> itemsInBm(Map<String, Map<String, Object>> bmMap,boolean isCoupon) {
		List<MixMatchDetailInfo> list = new ArrayList<>();
		if(bmMap != null && bmMap.size() > 0) {
			for(Map.Entry<String, Map<String, Object>> v : bmMap.entrySet()) {
				Map<String, Object> vMap = v.getValue();
				if(vMap.size() > 0 && vMap.containsKey("hasMixMatch") && "false".equals(vMap.get("hasMixMatch"))) {
					continue;
				}
				if(vMap.size() > 0 && vMap.containsKey("SubNum1") && PromotionConstants.CUSTOMER_SALE_INT == (Integer)vMap.get("SubNum1") && !isCoupon) {
					continue;
				}
				if(vMap.size() == 0) {
					continue;
				}
				if(vMap.containsKey("rule1")) {
					DetailInfo detailinfo = (DetailInfo)vMap.get("rule1");
					salesInBm(list,detailinfo.getEntryList());
				}
				if(vMap.containsKey("rule2")) {
					DetailInfo detailinfo = (DetailInfo)vMap.get("rule2");
					salesInBm(list,detailinfo.getEntryList());
				}
				if(vMap.containsKey("rule3")) {
					DetailInfo detailinfo = (DetailInfo)vMap.get("rule3");
					salesInBm(list,detailinfo.getEntryList());
				}
				if(vMap.containsKey("remainder")) {
					DetailInfo detailinfo = (DetailInfo)vMap.get("remainder");
					salesInBm(list,detailinfo.getEntryList());
				}
			}
		}
		return list;
	}

	/**
	 *  make PM
	 * @param pmMap
	 * @return
	 */
	@SuppressWarnings({ "unused", "null" })
	private List<PmDiscountInfo> createPm(Map<String, Map<String, List<PmItemInfo>>> pmMap, Map<String, ItemForPm> itemOfManyPm) {
		List<PmDiscountInfo> list = new ArrayList<>();
		for(Map.Entry<String, Map<String, List<PmItemInfo>>> map : pmMap.entrySet()) {
			String key = map.getKey();
			String[] keys = key.split("_");
			//current PM count
			int pmCnt = 0;
			// current PM price
			double pmPrice = 0;
			// discountClass
			String discountClass = "";
			// get pmCnt pmPrice By the key
			if(keys.length >= 5) {
				pmCnt = Integer.parseInt(keys[1]);
				discountClass =  keys[4];
			}
			Map<String, List<PmItemInfo>> pmGroupMap = map.getValue();
			if(pmGroupMap.size() == pmCnt && pmGroupMap.size() > 1) {
				
				 int minGroupQuantity = 0;
				 boolean isContinue = false;
				
				HashMap<String, Double> bundleList = new HashMap<String, Double>();
				HashMap<String, Integer> bundleListItemsCnt = new HashMap<String, Integer>();
				
				for(Map.Entry<String,List<PmItemInfo>> mmEntry : pmGroupMap.entrySet()) {
					List<PmItemInfo> currentList = mmEntry.getValue();
					sortList(currentList);
					
					if (PromotionConstants.DISCOUNT_CLASS_3.equals(discountClass)) {
						int bundleCtr = 0;
						for(int i = 0; i < currentList.size(); i++) {
							PmItemInfo pmInfo =  currentList.get(i);
							for(int x = 0; x < pmInfo.getQuantity(); x++) {
								String bundleKey = "Bundle" + bundleCtr + "-" + pmInfo.getPmPrice();

								if(bundleList.size() == 0 || !bundleList.containsKey(bundleKey)) {
									bundleList.put(bundleKey, pmInfo.getSalePrice());
								} else {
									bundleList.put(bundleKey, bundleList.get(bundleKey) + pmInfo.getSalePrice());
								}
								bundleListItemsCnt.put(bundleKey, bundleListItemsCnt.containsKey(bundleKey) ? bundleListItemsCnt.get(bundleKey) + 1 : 1);
								bundleCtr ++;
							}
						}
					}
					
					int currentMinQuantity = 0;
					if(currentList.size() == 0) {
						isContinue = true;
						break;
					}
					for(PmItemInfo ptInfo : currentList) {
						currentMinQuantity += ptInfo.getQuantity();
						
					}
					if(currentMinQuantity == 0) {
						isContinue = true;
						break;
					}
					if(minGroupQuantity == 0 || minGroupQuantity > currentMinQuantity) {
						minGroupQuantity = currentMinQuantity;
					}
				}
				if(isContinue) {
					continue;
				}

				int removeBundleCnt = 0;
				if (bundleList.size() > 0) {
					for (Map.Entry<String, Double> entry : bundleList.entrySet()) {
						String bundleKey = entry.getKey();
						Double bundleVal = entry.getValue();
						Double bundlePrice = Double.parseDouble(bundleKey.split("-")[1]);
						int bundleItemCnt = bundleListItemsCnt.get(bundleKey);
						if (bundleVal < bundlePrice || bundleItemCnt < pmCnt) {
							removeBundleCnt ++;
						}
					}
					minGroupQuantity = bundleList.size() - removeBundleCnt;
				}
				
				PmDiscountInfo discountInfo = new PmDiscountInfo();
				List<PmItemInfo> pmItemList  = new ArrayList<PmItemInfo>();
				double pmItemPrice = 0;
				int allQuantity = 0;
				double allPrice = 0;
				double pmPriceRate = 0;
				double decisionPMPrice = 0;
				double decisionPMPriceRate = 0;
				double pmDiscountExempt = 0;
				int pmDiscountExemptCnt = 0;
				int subNum1 = 0;
				double discountPrice = 0;
				boolean  canMakePm = true;
				for(Map.Entry<String,List<PmItemInfo>> mmEntry : pmGroupMap.entrySet()) {
					List<PmItemInfo> currentList = mmEntry.getValue();
					if(null == currentList || (currentList != null && currentList.size() == 0)) {
						canMakePm = false;
						break;
					}
					sortList(currentList);
					pmItemList.addAll(currentList);
					int currentQuantity = 0;
					for(int i = 0; i < currentList.size(); i++) {
						PmItemInfo info =  currentList.get(i);
						allQuantity += info.getQuantity();
						allPrice += info.getSalePrice() * info.getQuantity();
						currentQuantity += info.getQuantity();
						if(i == 0) {
							subNum1 = info.getSubNum1();
							pmPrice = info.getPmPrice();
							pmPriceRate = info.getPmRate();
							decisionPMPrice =  info.getDecisionPMPrice();
							decisionPMPriceRate = info.getDecisionPMRate();
						}						
						if (minGroupQuantity > 1 && currentQuantity > 1 && 
								((PromotionConstants.DISCOUNT_CLASS_3.equals(discountClass) && decisionPMPrice > 0) || 
								(PromotionConstants.DISCOUNT_CLASS_1.equals(discountClass) && decisionPMPriceRate > 0))) {
							if (!(PromotionConstants.DISCOUNT_CLASS_3.equals(discountClass) && pmPrice < (decisionPMPrice * pmCnt))) {
								minGroupQuantity = 1;
								continue;
							}
						}			
						if(currentQuantity > minGroupQuantity) {			
							if(PromotionConstants.DISCOUNT_CLASS_3.equals(discountClass) && info.getSalePrice() <= decisionPMPrice) {
								pmDiscountExempt += info.getSalePrice() * (currentQuantity - minGroupQuantity);
								pmDiscountExemptCnt = currentQuantity - minGroupQuantity;
							}
							pmItemPrice += info.getSalePrice() * ((info.getQuantity() + minGroupQuantity - currentQuantity) < 0 ? 0 : (info.getQuantity() + minGroupQuantity - currentQuantity));
						}else {
							pmItemPrice += info.getSalePrice() * info.getQuantity();
						}
					}
				}
				if(canMakePm) {
					if(PromotionConstants.DISCOUNT_CLASS_1.equals(discountClass)) {
						discountPrice = pmItemPrice  * pmPriceRate / 100;
						if(decisionPMPriceRate > 0) {
							discountPrice = discountPrice + (allPrice - pmItemPrice) * decisionPMPriceRate / 100;
						}
					}
					
					if(PromotionConstants.DISCOUNT_CLASS_3.equals(discountClass)) {
						discountPrice = pmItemPrice - pmPrice * minGroupQuantity;
						int pmUserQuantity = pmCnt * minGroupQuantity;
						if(decisionPMPrice > 0 && allQuantity > pmUserQuantity) {
							discountPrice = allPrice - pmPrice * minGroupQuantity  -  (allQuantity - pmUserQuantity - pmDiscountExemptCnt) * decisionPMPrice - pmDiscountExempt;
						}
					}
					if(discountPrice > 0) {
						discountInfo.setDiscountPrice(discountPrice);
						discountInfo.setPmList(pmItemList);
						discountInfo.setSumNowPrice(allPrice - discountPrice);
						discountInfo.setSumTruePrice(allPrice);
						discountInfo.setPmKey(key);
						discountInfo.setTimes(minGroupQuantity);
						discountInfo.setSubNum1(subNum1);
						list.add(discountInfo);
					}

				}
			}
		}
		return list;
	}
	
	public  Map<String, List<PmItemInfo>> allPmMix(Map<String, List<PmItemInfo>> map, List<PmItemInfo> l) {
		int i = 0;
		if(map == null ||(map != null && map.size() == 0)) {
			map = new HashMap<>();
			for(PmItemInfo info : l) {
				List<PmItemInfo> list = new ArrayList<PmItemInfo>();
				list.add(info);
				i++;
				map.put(String.valueOf(i),list);
			}
		}else {
			Map<String, List<PmItemInfo>> map2 =  new HashMap<>();
			for(Map.Entry<String, List<PmItemInfo>> mm : map.entrySet()) {
				for(PmItemInfo info : l) {
					List<PmItemInfo> list = new ArrayList<PmItemInfo>();
					list.addAll(mm.getValue());
					list.add(info);
					i++;
					map2.put(String.valueOf(i),list);
				}
			}
			return map2;
		}
		return map;
	}

	/**
	 *  sort list  by salePrice
	 * @param list
	 */
	private void sortList(List<PmItemInfo> list) {
		Collections.sort(list, new Comparator<PmItemInfo>() {
			@Override
			public int compare(PmItemInfo o1, PmItemInfo o2) {
				if(o2.getSalePrice() - o1.getSalePrice() > 0) {
					return 1;
				}else if(o2.getSalePrice() - o1.getSalePrice() == 0) {
					return o1.getItemEntryId().compareTo(o2.getItemEntryId());
				} else {
					return -1;
				}
			}
		});
	} 
	
	/**
	 *  sort list  by discount
	 * @param list
	 */
	private void sortDiscountList(List<PmDiscountInfo> list) {
		Collections.sort(list, new Comparator<PmDiscountInfo>() {
			@Override
			public int compare(PmDiscountInfo o1, PmDiscountInfo o2) {
				int o2Times = o2.getTimes();
				int o1Times = o1.getTimes();
				double reminder = (o2.getDiscountPrice() / o2Times) - (o1.getDiscountPrice() / o1Times);
				if(reminder > 0) {
					return 1;
				}else if(reminder == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
	} 

	/**
	 * item_update will call
	 * @param pmMap
	 * @param saleItem
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void updateCache(TerminalItem terminalItem, Sale saleItem) throws IllegalAccessException, InvocationTargetException {
		Map<String, Map<String, List<PmItemInfo>>> pmMap = terminalItem.getPmMap();
		Map<String, ItemForPm> itemOfManyPmMap = terminalItem.getItemOfManyPm();
		String keyForItemForPm = saleItem.getItemEntryId();
		if(itemOfManyPmMap.containsKey(keyForItemForPm) && itemOfManyPmMap.size() > 0) {
			saleItem.setQuantity(saleItem.getQuantity());
			ItemForPm itemForPm = itemOfManyPmMap.get(keyForItemForPm);
			List<PmItemInfo> list = itemForPm.getKeyList();
			if(saleItem.getQuantity() <= 0) {
				itemForPm.setQuantity(0);
				for(PmItemInfo info : list) {
					changeCachePm(pmMap, saleItem, info, true);
				}
			}
			if(saleItem.getQuantity() > 0 && itemForPm.getSalePrice() != saleItem.getActualSalesUnitPrice()) {
				itemForPm.setSalePrice(saleItem.getActualSalesUnitPrice());
				for(PmItemInfo currentInfo : list) {
					if(currentInfo.getItemEntryId().equals(saleItem.getItemEntryId())) {
						currentInfo.setSalePrice(saleItem.getActualSalesUnitPrice());
					}
				}
			}
			
			if(saleItem.getQuantity() > 0  && saleItem.getQuantity() != itemForPm.getQuantity()) {
				itemForPm.setQuantity(saleItem.getQuantity());
				for(PmItemInfo info : list) {
					changeCachePm(pmMap, saleItem, info, true);
				}
			}

		}
	}
	
	
	private Map<String, Map<String, List<PmItemInfo>>> getPmItemWithOutBm(
			Map<String, Map<String, Object>> bmMap, 
			TerminalItem terminalItem,
			boolean isCoupon) throws IllegalAccessException, InvocationTargetException{
		// if current Item not In mixMatch then
		Map<String, Map<String, List<PmItemInfo>>> pmMap = terminalItem.getPmMap();
		Map<String, ItemForPm> itemOfManyPmMap = terminalItem.getItemOfManyPm();
		Map<String, ItemForPm> pmItemInBm = terminalItem.getPmItemInBm();
		List<MixMatchDetailInfo> bmList = itemsInBm(bmMap, isCoupon); 
		
		Map<String, Map<String, List<PmItemInfo>>> newMap  = makeNewMap(pmMap, terminalItem);
		if(bmList.size() == 0) {
			pmItemInBm.clear();
		}
		for(MixMatchDetailInfo info : bmList) {
			String key = info.getEntryId();
			if (itemOfManyPmMap.size() > 0 && itemOfManyPmMap.containsKey(key)) {
				ItemForPm itemForPm = itemOfManyPmMap.get(key);
				ItemForPm itemInBm = new ItemForPm();
				int quantity = itemForPm.getQuantity();
				int bmQuantity = info.getQuantity();
				//make full cacheMap
				List<PmItemInfo> list = itemForPm.getKeyList();
				itemInBm.setQuantity(quantity - bmQuantity);
				itemInBm.setSalePrice(itemForPm.getSalePrice());
				pmItemInBm.put(key, itemInBm);
				Sale sale = new Sale();
				sale.setItemEntryId(key);
				sale.setQuantity(quantity - bmQuantity);
				sale.setActualSalesUnitPrice(itemForPm.getSalePrice());
				sale.setTaxId(itemForPm.getTaxId());
				for(PmItemInfo pm : list) {
					changeCachePm(newMap, sale, pm, true);
				}
			} 
		}
		return newMap;
	}

	private Map<String, PmDiscountInfo> cleanMap(Map<String,Map<String, List<PmItemInfo>>> map,TerminalItem terminalItem) throws IllegalAccessException, InvocationTargetException {
		return cleanMap(map,terminalItem, true, null);
	}
	
	private Map<String, PmDiscountInfo> cleanMap(Map<String,Map<String, List<PmItemInfo>>> map,TerminalItem terminalItem,boolean needCopy,Map<String, PmDiscountInfo> cleanMap) throws IllegalAccessException, InvocationTargetException {
		Map<String, ItemForPm> itemOfManyPm = terminalItem.getItemOfManyPm();
		Map<String, PmDiscountInfo> resultMap = null;
		if(null == cleanMap) {
			resultMap = new HashMap<>();
		} else {
			resultMap = cleanMap;
		}
		 
		Map<String, Map<String, List<PmItemInfo>>> newMap =  new HashMap<>();
		if(needCopy) {
			newMap = map;
		} else {
			newMap = map;
		}
		
		List<PmDiscountInfo>  pmList = createPm(newMap, itemOfManyPm);
		if(null != pmList && pmList.size() > 0) {
			sortDiscountList(pmList);
			PmDiscountInfo info = pmList.get(0);
			List<PmItemInfo> pmItemList = info.getPmList();
			newMap.remove(info.getPmKey());
			resultMap.put(info.getPmKey(), info);
			if(null != pmItemList && pmItemList.size() > 0) {
				double decisionPMPrice = pmItemList.get(0).getDecisionPMPrice();
				double decisionPMPriceRate = pmItemList.get(0).getDecisionPMRate();
				if (decisionPMPrice == 0 && decisionPMPriceRate == 0) {
					pmItemList = sortListByPair(pmItemList, terminalItem);
				}
				for(PmItemInfo pmItem : pmItemList) {
					String itemEntryId = pmItem.getItemEntryId();
					if(itemOfManyPm.containsKey(itemEntryId)) {
						ItemForPm ifp = itemOfManyPm.get(itemEntryId);
						List<PmItemInfo> keyList = ifp.getKeyList();
						for(PmItemInfo keyItem : keyList) {
							if(keyItem.getPmNo() == pmItem.getPmNo()) {
								continue;
							}else {
								Sale sale = new Sale();
								sale.setItemEntryId(itemEntryId);
								sale.setQuantity(0);
								sale.setItemId(ifp.getItemId());
								sale.setTaxId(ifp.getTaxId());
								changeCachePm(newMap, sale, keyItem, true);
							}
						}

					}
				}
			}
			return cleanMap(newMap,terminalItem, false, resultMap);
		}else {
			return resultMap;
		}
	}
	
	private List<PmItemInfo> sortListByPair(List<PmItemInfo> pmItemList, TerminalItem terminalItem) throws IllegalAccessException, InvocationTargetException {
		Map<String, ItemForPm> itemOfManyPm = terminalItem.getItemOfManyPm();
		List<PmItemInfo> pmItemListCopy = new ArrayList<PmItemInfo>();
		List<PmItemInfo> result = new ArrayList<PmItemInfo>();
		pmItemListCopy.addAll(pmItemList);
		int pairLeftCount = 0;
		int pairRightCount = 0;
		String pairLeftId = "";
		String pairRightId = "";
		String newItemId = "";
		String prevItemId = "";
		// transfer 1st pmItem from pmItemListCopy into result
		prevItemId = itemOfManyPm.get(pmItemListCopy.get(0).getItemEntryId()).getItemId();
		pairLeftId = prevItemId;
		pairLeftCount += itemOfManyPm.get(pmItemListCopy.get(0).getItemEntryId()).getQuantity();
		result.add(pmItemListCopy.get(0));
		pmItemListCopy.remove(0);
		// alternate adding pmItem from pmItemListCopy into result by itemEntryId
		while(pmItemListCopy.size() > 0) {
			boolean shouldBreak = true;
			for(int i=0; i < pmItemListCopy.size(); i++){
				newItemId = itemOfManyPm.get(pmItemListCopy.get(i).getItemEntryId()).getItemId();
				if (!prevItemId.equals(newItemId)) {
					// transfer pmItem of index i from pmItemListCopy into result
					prevItemId = itemOfManyPm.get(pmItemListCopy.get(i).getItemEntryId()).getItemId();
					if (pairRightId.equals("")) pairRightId = prevItemId;
					if (prevItemId.equals(pairLeftId)) pairLeftCount += itemOfManyPm.get(pmItemListCopy.get(i).getItemEntryId()).getQuantity();
					if (prevItemId.equals(pairRightId)) pairRightCount += itemOfManyPm.get(pmItemListCopy.get(i).getItemEntryId()).getQuantity();
					result.add(pmItemListCopy.get(i));
					pmItemListCopy.remove(i);
					shouldBreak = false;
				}
			}
			if (shouldBreak) {
				break;
			}
		}
		if (result.size() % 2 != 0  && pairLeftCount!=pairRightCount) {
			// remove last extra pm item from result without a pair
			result.remove(result.size()-1);
		}
		return result;
	}
	
	private Map<String, Map<String, List<PmItemInfo>>> makeNewMap(Map<String,Map<String, List<PmItemInfo>>> map, 
			TerminalItem terminalItem) throws IllegalAccessException, InvocationTargetException{
		Map<String, Map<String, List<PmItemInfo>>> newMap =  new HashMap<>();
		Set<String> itemsInPromotionSet = terminalItem.itemsInDiscount("TM");
		for(Map.Entry<String, Map<String, List<PmItemInfo>>> entry : map.entrySet()) {
			String key = entry.getKey();
        	if(!StringUtility.isNullOrEmpty(key)) {
        		String[] keys = key.split("_");
        		if(keys.length > 5) {
        			String couponCustomerCode = keys[keys.length - 1];
        			// when the PM is coupon then if the selected PM code not equals the PM then the PM
        			// can't make
        			if(!terminalItem.isSelectedCode(couponCustomerCode, keys[0], "PM")) {
        				continue;
        			}
        		}
        	}
			Map<String, List<PmItemInfo>> copyMap = new HashMap<>();
			for(Map.Entry<String, List<PmItemInfo>> entry1 : entry.getValue().entrySet()) {
				List<PmItemInfo> nl = new ArrayList<>();
				for(PmItemInfo info : entry1.getValue()) {
					if(info.getSubNum1() == PromotionConstants.CUSTOMER_SALE_INT && itemsInPromotionSet.size() > 0 && itemsInPromotionSet.contains(info.getItemEntryId())) {
						continue;
					}else {
						PmItemInfo nInfo = new PmItemInfo();
						BeanUtils.copyProperties(nInfo, info);
						nl.add(nInfo);
					}
				}
				copyMap.put(entry1.getKey(), nl);
			}
			newMap.put(entry.getKey(), copyMap);
		}
		return newMap;
	}
	

	/**
	 * @param pmMap
	 * @param saleItem
	 * @param info
	 * @param itemOfManyPm
	 * @param isUpdate
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void changeCachePm(Map<String, Map<String, List<PmItemInfo>>> pmMap, Sale saleItem, PmItemInfo info, boolean isUpdate) throws IllegalAccessException, InvocationTargetException {
		int quantity = saleItem.getQuantity();
		StringBuilder sb = new StringBuilder();
		sb.append(info.getPmNo()).append("_").append(info.getpMCnt()).append("_");
		if(PromotionConstants.DISCOUNT_CLASS_1.equals(info.getDiscountClass())) {
			sb.append(info.getPmRate());
		}else if(PromotionConstants.DISCOUNT_CLASS_3.equals(info.getDiscountClass())) {
			sb.append(info.getPmPrice());
		}else if(PromotionConstants.DISCOUNT_CLASS_2.equals(info.getDiscountClass())) {
			sb.append(0);
		}else {
			sb.append(0);
		}
		sb.append("_").append(saleItem.getTaxId());
		sb.append("_").append(info.getDiscountClass());
		sb.append("_").append(info.getSubNum1());
		String key =  sb.toString();
		int groupNo = info.getPMGroupNo();
		info.setSale(saleItem);
		if(pmMap.containsKey(key)) {
			Map<String, List<PmItemInfo>> groupMap = pmMap.get(key);
			// has the groupMap value of the groupNo 
			if(groupMap.containsKey(String.valueOf(groupNo))) {
				List<PmItemInfo> list = groupMap.get(String.valueOf(groupNo));
				if(quantity == 0){
					List<PmItemInfo> deleteList = getListWithCurrentList(list, saleItem);
					list.removeAll(deleteList);
				} else {
					boolean itemInList = false;
					for(PmItemInfo pii : list) {
						if(pii.getItemEntryId().equals(info.getItemEntryId())) {
							if(isUpdate) {
								pii.setQuantity(quantity);
							}else {
								pii.setQuantity(quantity + pii.getQuantity());
							}
							itemInList = true;
							break;
						}
					}
					if(!itemInList) {
						list.add(info);
					}
				}
			} else {
				if(quantity > 0) {
					List<PmItemInfo> nlist = new ArrayList<>();
					nlist.add(info);
					groupMap.put(String.valueOf(groupNo), nlist);
				}
			}		 
		}else {
			if(quantity > 0) {
				Map<String, List<PmItemInfo>> childMap = new HashMap<>();
				List<PmItemInfo> list = new ArrayList<>();
				list.add(info);
				childMap.put(String.valueOf(groupNo), list);
				pmMap.put(key, childMap);
			}
		}
	}


	private List<PmItemInfo> getListWithCurrentList(List<PmItemInfo> list, Sale item){
		List<PmItemInfo> newList = new ArrayList<PmItemInfo>();
		for(PmItemInfo info : list) {
			if(item.getItemEntryId().equals(info.getItemEntryId())) {
				newList.add(info);
			}
		}
		return newList;
	}

	private void salesInBm(List<MixMatchDetailInfo> oldList, List<MixMatchDetailInfo> newList){
		if(oldList.size() != 0 && newList.size() != 0) {
			for(MixMatchDetailInfo info : newList) {
				boolean isInOlist = false;
				for(MixMatchDetailInfo oInfo : oldList) {
					if(info.getEntryId().equals(oInfo.getEntryId())) {
						oInfo.setQuantity(info.getQuantity() + oInfo.getQuantity());
						isInOlist = true;
					}
				}
				if(!isInOlist) {
					MixMatchDetailInfo ninfo = new MixMatchDetailInfo();
					ninfo.setEntryId(info.getEntryId());
					ninfo.setQuantity(info.getQuantity());
					oldList.add(ninfo);
				}
			}
		}
		if(oldList.size() == 0 && newList.size() != 0) {
			for(MixMatchDetailInfo info : newList) {
				MixMatchDetailInfo ninfo = new MixMatchDetailInfo();
				ninfo.setEntryId(info.getEntryId());
				ninfo.setQuantity(info.getQuantity());
				oldList.add(ninfo);
			}
		} 
	}
	
}
