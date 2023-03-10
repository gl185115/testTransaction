package ncr.res.mobilepos.settlement.dao;

import java.util.List;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.model.VoucherDetails;

public interface ISettlementInfoDAO {
	SettlementInfo getVoucherList(String companyId, String storeId,
			String businessDayDate, String terminalId, int trainingFlag) throws Exception;
	
	SettlementInfo getVoucherListByTillId(String companyId, String storeId, 
			String businessDayDate, String tillId, int trainingFlag) throws Exception;
	
	SettlementInfo getTransactionCount(String companyId, String storeId,
		String txtype, int trainingFlag) throws Exception;
	
	SettlementInfo getTxCountByBusinessDate(String companyId, String storeId, String workStationId,
			String txtype, String businessDate, int trainingFlag) throws Exception;
	
	SettlementInfo getCredit(final String companyId, final String storeId,
			final String terminalId, final String businessDate, final int trainingFlag, final String dataType, 
			final String itemLevel1) throws Exception;
	
	SettlementInfo getCreditByTillId(final String companyId, final String storeId, final String tillId,
			final String businessDate, final int trainingFlag, final String dataType, 
			final String itemLevel1) throws Exception;
	
	List<VoucherDetails> getVoucherDetails(final String companyId, final String storeId,
			final String terminalId, final String businessDate, final int trainingFlag,
			final String itemLevel3) throws Exception;
	
	List<VoucherDetails> getVoucherDetailsByTillId(String companyId, String storeId, String tillId,
			String businessDate, int trainingFlag, String itemLevel3) throws Exception;
	
	SettlementInfo getPaymentAmtByTerminalId(final String companyId, final String storeId,
			final String businessDate, int trainingFlag, String terminalId) throws Exception;
	
	SettlementInfo getPaymentAmtByTxType(final String companyId, final String storeId,
			final String businessDate, int trainingFlag, String txType) throws Exception;
}
