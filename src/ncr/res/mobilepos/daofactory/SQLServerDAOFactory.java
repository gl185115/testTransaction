/*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * SQLServerDAOFactory
    *
    * Concrete DAOFactory for SQLServer storage implementation
    *
    * Meneses, Chris Niven
    */

package ncr.res.mobilepos.daofactory;

import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.mobilepos.appserver.dao.IAppServerDAO;
import ncr.res.mobilepos.appserver.dao.SQLServerAppServerDAO;
import ncr.res.mobilepos.authentication.dao.AuthAdminDao;
import ncr.res.mobilepos.authentication.dao.AuthDeviceDao;
import ncr.res.mobilepos.authentication.dao.IAuthAdminDao;
import ncr.res.mobilepos.authentication.dao.IAuthDeviceDao;
import ncr.res.mobilepos.authentication.dao.ICorpStoreDAO;
import ncr.res.mobilepos.authentication.dao.SQLServerCorpStoreDAO;
import ncr.res.mobilepos.barcode.dao.IBarCodeDAO;
import ncr.res.mobilepos.barcode.dao.SQLServerBarCodeDAO;
import ncr.res.mobilepos.buyadditionalinfo.dao.IBuyadditionalInfoDAO;
import ncr.res.mobilepos.buyadditionalinfo.dao.SQLServerBuyadditionalInfoDAO;
import ncr.res.mobilepos.cardinfo.dao.ICardInfoDAO;
import ncr.res.mobilepos.cardinfo.dao.SQLServerCardInfoDAO;
import ncr.res.mobilepos.cashAbstract.dao.ICashAbstractDAO;
import ncr.res.mobilepos.cashAbstract.dao.SQLServerCashAbstractDAO;
import ncr.res.mobilepos.cashaccount.dao.ICashAccountDAO;
import ncr.res.mobilepos.cashaccount.dao.SQLCashAccountDAO;
import ncr.res.mobilepos.classinfo.dao.IClassInfoDAO;
import ncr.res.mobilepos.classinfo.dao.SQLServerClassInfoDAO;
import ncr.res.mobilepos.credential.dao.ICredentialDAO;
import ncr.res.mobilepos.credential.dao.IGroupDAO;
import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.credential.dao.SQLServerGroupDAO;
import ncr.res.mobilepos.creditcard.dao.ICreditCardAbstractDAO;
import ncr.res.mobilepos.creditcard.dao.SQLServerCreditCardDAO;
import ncr.res.mobilepos.customerSearch.dao.ICustomerSearthDAO;
import ncr.res.mobilepos.customerSearch.dao.SQLServerCustomerSearchDAO;
import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customeraccount.dao.SQLServerCustomerInfoDAO;
import ncr.res.mobilepos.customerclass.dao.ICustomerClassInfoDAO;
import ncr.res.mobilepos.customerclass.dao.SQLServerCustomerClassInfoDAO;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.dao.SQLServerDepartmentDAO;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.dao.ILinkDAO;
import ncr.res.mobilepos.deviceinfo.dao.SQLCreditAuthorizationLinkDAO;
import ncr.res.mobilepos.deviceinfo.dao.SQLDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.dao.SQLQueueBusterLinkDAO;
import ncr.res.mobilepos.deviceinfo.dao.SQLSignatureLinkDAO;
import ncr.res.mobilepos.devicelog.dao.IDeviceLogDAO;
import ncr.res.mobilepos.devicelog.dao.SQLServerDeviceLogDAO;
import ncr.res.mobilepos.discountplaninfo.dao.DiscountPlanInfoCommomDAO;
import ncr.res.mobilepos.discountplaninfo.dao.IPremiumFlagDAO;
import ncr.res.mobilepos.discountplaninfo.dao.IPromotionInfoDAO;
import ncr.res.mobilepos.discountplaninfo.dao.SQLServerDiscountPlanInfoCommonDAO;
import ncr.res.mobilepos.discountplaninfo.dao.SQLServerPremiumFlagDAO;
import ncr.res.mobilepos.discountplaninfo.dao.SQLServerPromotionInfoDAO;
import ncr.res.mobilepos.eventlog.dao.IEventLogDAO;
import ncr.res.mobilepos.eventlog.dao.SQLServerEventLogDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.IForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.dao.SQLServerForwardItemListDAO;
import ncr.res.mobilepos.giftcard.dao.SQLServerTxlCardFailureDAO;
import ncr.res.mobilepos.journalization.dao.ICommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.dao.SQLServerCommonDAO;
import ncr.res.mobilepos.journalization.dao.SQLServerPosLogDAO;
import ncr.res.mobilepos.line.dao.ILineDAO;
import ncr.res.mobilepos.line.dao.SQLServerLineDAO;
import ncr.res.mobilepos.mastermaintenance.dao.IMasterMaintenanceDAO;
import ncr.res.mobilepos.mastermaintenance.dao.SQLServerMasterMaintenanceDAO;
import ncr.res.mobilepos.nationalityinfo.dao.INationalityInfoDAO;
import ncr.res.mobilepos.nationalityinfo.dao.SQLServerNationalityInfoDAO;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.networkreceipt.dao.SQLServerReceiptDAO;
import ncr.res.mobilepos.offlinecredit.dao.IOfflineCreditDAO;
import ncr.res.mobilepos.offlinecredit.dao.SQLServerOfflineCreditDAO;
import ncr.res.mobilepos.point.dao.IPointDAO;
import ncr.res.mobilepos.point.dao.SQLServerPointDAO;
import ncr.res.mobilepos.poslogstatus.dao.IPoslogStatusDAO;
import ncr.res.mobilepos.poslogstatus.dao.SQLServerPoslogStatusDAO;
import ncr.res.mobilepos.posmailinfo.dao.IPOSMailInfoDAO;
import ncr.res.mobilepos.posmailinfo.dao.SQLServerPOSMailInfoDAO;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.IPriceMMInfoDAO;
import ncr.res.mobilepos.pricing.dao.IPricePromInfoDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerPriceMMInfoDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerPricePromInfoDAO;
import ncr.res.mobilepos.promotion.dao.ICodeConvertDAO;
import ncr.res.mobilepos.promotion.dao.IMixMatchDAO;
import ncr.res.mobilepos.promotion.dao.IPromotionMsgInfoDAO;
import ncr.res.mobilepos.promotion.dao.IQrCodeInfoDAO;
import ncr.res.mobilepos.promotion.dao.SQLServerCodeConvertDAO;
import ncr.res.mobilepos.promotion.dao.SQLServerMixMatchDAO;
import ncr.res.mobilepos.promotion.dao.SQLServerPromotionMsgInfoDAO;
import ncr.res.mobilepos.promotion.dao.SQLServerQrCodeInfoDAO;
import ncr.res.mobilepos.queuebuster.dao.IQueueBusterDAO;
import ncr.res.mobilepos.queuebuster.dao.SQLServerQueueBusterDao;
import ncr.res.mobilepos.queuesignature.dao.IQueueSignatureDao;
import ncr.res.mobilepos.queuesignature.dao.QueueSignatureDao;
import ncr.res.mobilepos.report.dao.IReportDAO;
import ncr.res.mobilepos.report.dao.SQLServerReportDAO;
import ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO;
import ncr.res.mobilepos.settlement.dao.SQLServerSettlementInfoDAO;
import ncr.res.mobilepos.store.dao.IStoreDAO;
import ncr.res.mobilepos.store.dao.SQLServerStoreDAO;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.systemsetting.dao.SQLServerSystemSettingDAO;
import ncr.res.mobilepos.tenderinfo.dao.ITenderInfoDAO;
import ncr.res.mobilepos.tenderinfo.dao.SQLServerTenderInfoDAO;
import ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO;
import ncr.res.mobilepos.tillinfo.dao.SQLServerTillInfoDAO;

/**
 * SQLServerDAOFactory is DAO Factory dedicated for SQLServer only.
 *
 * @see DAOFactory
 */
public class SQLServerDAOFactory extends DAOFactory {

    /**
     * The constructor of the class.
     */
    public SQLServerDAOFactory() {
    }

    @Override
    public final ICorpStoreDAO getCorpStoreDAO() throws DaoException {
        return new SQLServerCorpStoreDAO();
    }

    @Override
    public final IDepartmentDAO getDepartmentDAO() throws DaoException {
        return new SQLServerDepartmentDAO();
    }

    @Override
    public final IItemDAO getItemDAO() throws DaoException {
        return new SQLServerItemDAO();
    }

    @Override
    public final IPoslogStatusDAO getPoslogStatusDAO() throws DaoException {
        return new SQLServerPoslogStatusDAO();
    }

    @Override
    public final IClassInfoDAO getClassInfoDAO() throws DaoException {
        return new SQLServerClassInfoDAO();
    }

    @Override
    public final ILineDAO getLineDAO() throws DaoException {
        return new SQLServerLineDAO();
    }

    @Override
    public final IPosLogDAO getPOSLogDAO() throws DaoException {
        return new SQLServerPosLogDAO();
    }

    @Override
    public final IAuthAdminDao getAuthAdminDAO() throws DaoException {
        return new AuthAdminDao();
    }

    @Override
    public final IAuthDeviceDao getAuthDeviceDAO() throws DaoException {
        return new AuthDeviceDao();
    }

    @Override
    public final ICustomerDAO getCustomerDAO() throws DaoException {
        return new SQLServerCustomerInfoDAO();
    }

    @Override
    public final ICredentialDAO getCredentialDAO() throws DaoException {
        return new SQLServerCredentialDAO();
    }

    @Override
    public final IReportDAO getReportDAO() throws DaoException {
        return new SQLServerReportDAO();
    }

    @Override
    public final SQLServerSystemConfigDAO getSystemConfigDAO() throws DaoException {
        return new SQLServerSystemConfigDAO();
    }

    @Override
    public final IReceiptDAO getReceiptDAO() throws DaoException {
        return new SQLServerReceiptDAO();
    }

    @Override
    public final IAppServerDAO getAppServerDAO() throws DaoException {
    	return new SQLServerAppServerDAO();
    }

    @Override
    public final IForwardItemListDAO getForwardItemListDAO() throws DaoException {
        return new SQLServerForwardItemListDAO();
    }

    @Override
    public final ITxlCardFailureDAO getTxlCardFailureDAO() {
        return new SQLServerTxlCardFailureDAO();
    }

    @Override
    public final IDeviceInfoDAO getDeviceInfoDAO() throws DaoException {
        return new SQLDeviceInfoDAO();
    }

    @Override
    public final IQueueSignatureDao getQueueSignatureDao() throws DaoException {
        return new QueueSignatureDao();
    }

    @Override
    public final IQueueBusterDAO getQueBusterDAO() throws DaoException {
        return new SQLServerQueueBusterDao();
    }

    @Override
    public final ISystemSettingDAO getSystemSettingDAO() throws DaoException {
        return new SQLServerSystemSettingDAO();
    }

    @Override
    public final IDeviceLogDAO getDeviceLogDao() throws DaoException {
        return new SQLServerDeviceLogDAO();
    }

    @Override
    public final IStoreDAO getStoreDAO() throws DaoException {
        return new SQLServerStoreDAO();
    }

    @Override
    public final IDepartmentDAO getDepartmentDao() throws DaoException {
        return new SQLServerDepartmentDAO();
    }

    @Override
    public final ILinkDAO getQueueBusterLinkDAO() throws DaoException {
        return new SQLQueueBusterLinkDAO();
    }

    @Override
    public final ILinkDAO getCreditAuthorizationLinkDAO() throws DaoException {
        return new SQLCreditAuthorizationLinkDAO();
    }

    @Override
    public final ILinkDAO getSignatureLinkDAO() throws DaoException {
        return new SQLSignatureLinkDAO();
    }

    @Override
    public final IGroupDAO getGroupDAO() throws DaoException {
        return new SQLServerGroupDAO();
    }

    @Override
    public final IMixMatchDAO getMixMatchDAO() throws DaoException {
        return new SQLServerMixMatchDAO();
    }

    @Override
    public final ICodeConvertDAO getCodeConvertDAO() throws DaoException {
        return new SQLServerCodeConvertDAO();
    }

    @Override
    public final IQrCodeInfoDAO getQrCodeInfoDAO() throws DaoException {
        return new SQLServerQrCodeInfoDAO();
    }

    @Override
    public IMasterMaintenanceDAO getMasterMaintenanceDAO() throws DaoException {
        return new SQLServerMasterMaintenanceDAO();
    }

    @Override
    public final ICashAccountDAO getCashAccountDAO() throws DaoException {
        return new SQLCashAccountDAO();
    }

    @Override
    public final ITillInfoDAO getTillInfoDAO() throws DaoException {
        return new SQLServerTillInfoDAO();
    }

    @Override
    public DiscountPlanInfoCommomDAO getDiscountPlanInfoCommomDAO() throws DaoException {
        return new SQLServerDiscountPlanInfoCommonDAO();
    }

    @Override
    public ICommonDAO getCommonDAO() throws DaoException {
        return new SQLServerCommonDAO();
    }

    @Override
    public ICustomerSearthDAO getCustomerSearthDAO() throws DaoException {
        return new SQLServerCustomerSearchDAO();
    }

    @Override
    public ICardInfoDAO getCardInfoDAO() throws Exception {
        return new SQLServerCardInfoDAO();
    }

    @Override
    public ITenderInfoDAO getTenderInfoDAO() throws Exception {
        return new SQLServerTenderInfoDAO();
    }

    @Override
    public ICustomerClassInfoDAO getCustomerClassInfoDAO() throws Exception {
        return new SQLServerCustomerClassInfoDAO();
    }

    @Override
    public INationalityInfoDAO getNationalityInfoDAO() throws Exception {
        return new SQLServerNationalityInfoDAO();
    }

    @Override
    public IBuyadditionalInfoDAO getBuyadditionalInfoDAO() throws Exception {
        return new SQLServerBuyadditionalInfoDAO();
    }

    @Override
    public IBarCodeDAO getDiscountInfo() throws Exception {
        return new SQLServerBarCodeDAO();
    }

    @Override
    public ISettlementInfoDAO getSettlementInfoDAO() throws Exception {
        return new SQLServerSettlementInfoDAO();
    }

    @Override
    public IPromotionInfoDAO getPromotionInfoDAO() throws DaoException {
        return new SQLServerPromotionInfoDAO();
    }

    @Override
    public IEventLogDAO getEventLogDAO() throws DaoException {
        return new SQLServerEventLogDAO();
    }

    @Override
    public ICashAbstractDAO getCashAbstractDAO() throws DaoException {
        return new SQLServerCashAbstractDAO();
    }

    @Override
    public IPremiumFlagDAO getPremiumFlagDAO() throws DaoException {
        return new SQLServerPremiumFlagDAO();
    }

    @Override
    public IStoreDAO getCMPresetInfoDAO() throws DaoException {
        return new SQLServerStoreDAO();
    }

    @Override
    public IOfflineCreditDAO getOfflineCreditDAO() throws DaoException {
        return new SQLServerOfflineCreditDAO();
    }

    @Override
    public IPointDAO getPointDAO() throws Exception {
        return new SQLServerPointDAO();
    }

    @Override
    public ICardInfoDAO getMemberInfo() throws Exception {
        return new SQLServerCardInfoDAO();
    }

	@Override
	public ICardInfoDAO getStatusInfo() throws Exception {
		return new SQLServerCardInfoDAO();
	}

	@Override
	public ICreditCardAbstractDAO getCreditCardInfo() throws Exception {
		return new SQLServerCreditCardDAO();
	}

	@Override
	public final IPricePromInfoDAO getPricePromInfoDAO() throws DaoException {
		return new SQLServerPricePromInfoDAO();
	}

	@Override
	public IPriceMMInfoDAO getPriceMMInfoDAO() throws DaoException {
		// TODO Auto-generated method stub
		return new SQLServerPriceMMInfoDAO();
	}

	@Override
	public final IPromotionMsgInfoDAO getPromotionMsgInfoDAO() throws DaoException {
		return new SQLServerPromotionMsgInfoDAO();
	}
	
	/** ADD BGN 情報伝達機能 **/
	public IPOSMailInfoDAO getPOSMailInfoDAO() throws DaoException {
		return new SQLServerPOSMailInfoDAO();
	}
	/** ADD END 情報伝達機能 **/

}