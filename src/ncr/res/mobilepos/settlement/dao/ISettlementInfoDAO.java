package ncr.res.mobilepos.settlement.dao;

import ncr.res.mobilepos.settlement.model.SettlementInfo;

public interface ISettlementInfoDAO {
	SettlementInfo getCreditSummary(String companyId, String storeId, 
			String businessDayDate, int trainingFlag) throws Exception;
	
	SettlementInfo getVoucherList(String companyId, String storeId,
			String businessDayDate, int trainingFlag) throws Exception;
	
	SettlementInfo getTransactionCount(String companyId, String storeId,
	    String txtype, int trainingFlag) throws Exception;
}
