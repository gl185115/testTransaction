package ncr.res.mobilepos.settlement.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.model.VoucherDetails;

public interface ISettlementInfoDAO {
	SettlementInfo getCreditSummary(String companyId, String storeId, 
			String businessDayDate, int trainingFlag) throws Exception;
	
	SettlementInfo getVoucherList(String companyId, String storeId,
			String businessDayDate, String terminalId, int trainingFlag) throws Exception;
	
	SettlementInfo getTransactionCount(String companyId, String storeId,
	    String txtype, int trainingFlag) throws Exception;
	
	SettlementInfo getCredit(final String companyId, final String storeId,
            final String terminalId, final String businessDate, final int trainingFlag, final String dataType, 
            final String itemLevel1, final String itemLevel2) throws Exception;
	
	List<VoucherDetails> getVoucherDetails(final String companyId, final String storeId,
            final String terminalId, final String businessDate, final int trainingFlag,
            final String itemLevel3) throws Exception;
}
