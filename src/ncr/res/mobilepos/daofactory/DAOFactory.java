/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* DAOFactory
*
* Abstract DAOFactory which manages the different storage
* implementation for the DAO
*
* Meneses, Chris Niven
*/

package ncr.res.mobilepos.daofactory;

import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.mobilepos.appserver.dao.IAppServerDAO;
import ncr.res.mobilepos.authentication.dao.IAuthAdminDao;
import ncr.res.mobilepos.authentication.dao.IAuthDeviceDao;
import ncr.res.mobilepos.authentication.dao.ICorpStoreDAO;
import ncr.res.mobilepos.authentication.dao.SQLServerCorpStoreDAO;
import ncr.res.mobilepos.barcode.dao.IBarCodeDAO;
import ncr.res.mobilepos.buyadditionalinfo.dao.IBuyadditionalInfoDAO;
import ncr.res.mobilepos.cardinfo.dao.ICardInfoDAO;
import ncr.res.mobilepos.cashAbstract.dao.ICashAbstractDAO;
import ncr.res.mobilepos.cashaccount.dao.ICashAccountDAO;
import ncr.res.mobilepos.credential.dao.ICredentialDAO;
import ncr.res.mobilepos.credential.dao.IGroupDAO;
import ncr.res.mobilepos.creditcard.dao.ICreditCardAbstractDAO;
import ncr.res.mobilepos.customerSearch.dao.ICustomerSearthDAO;
import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customerclass.dao.ICustomerClassInfoDAO;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.dao.ILinkDAO;
import ncr.res.mobilepos.devicelog.dao.IDeviceLogDAO;
import ncr.res.mobilepos.discountplaninfo.dao.DiscountPlanInfoCommomDAO;
import ncr.res.mobilepos.discountplaninfo.dao.IPremiumFlagDAO;
import ncr.res.mobilepos.discountplaninfo.dao.IPromotionInfoDAO;
import ncr.res.mobilepos.ej.dao.INameSystemInfoDAO;
import ncr.res.mobilepos.employee.dao.IEmployeeDao;
import ncr.res.mobilepos.eventlog.dao.IEventLogDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.IForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.dao.SQLServerForwardItemListDAO;
import ncr.res.mobilepos.futurePay.dao.IFuturePayDAO;
import ncr.res.mobilepos.journalization.dao.ICommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.mastermaintenance.dao.IMasterMaintenanceDAO;
import ncr.res.mobilepos.nationalityinfo.dao.INationalityInfoDAO;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.offlinecredit.dao.IOfflineCreditDAO;
import ncr.res.mobilepos.point.dao.IPointDAO;
import ncr.res.mobilepos.poslogstatus.dao.IPoslogStatusDAO;
import ncr.res.mobilepos.posmailinfo.dao.IPOSMailInfoDAO;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.IPriceMMInfoDAO;
import ncr.res.mobilepos.pricing.dao.IPricePromInfoDAO;
import ncr.res.mobilepos.promotion.dao.ICodeConvertDAO;
import ncr.res.mobilepos.promotion.dao.IMixMatchDAO;
import ncr.res.mobilepos.promotion.dao.IPromotionMsgInfoDAO;
import ncr.res.mobilepos.promotion.dao.IQrCodeInfoDAO;
import ncr.res.mobilepos.promotion.dao.ITaxRateInfoDAO;
import ncr.res.mobilepos.queuebuster.dao.IQueueBusterDAO;
import ncr.res.mobilepos.report.dao.IReportDAO;
import ncr.res.mobilepos.selfmode.dao.ISelfModeDAO;
import ncr.res.mobilepos.settlement.dao.ISettlementInfoDAO;
import ncr.res.mobilepos.store.dao.IStoreDAO;
import ncr.res.mobilepos.store.dao.SQLServerStoreDAO;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.tenderinfo.dao.ITenderInfoDAO;
import ncr.res.mobilepos.terminalInfo.dao.ITerminalInfoDAO;
import ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO;
import ncr.res.mobilepos.selfmode.dao.ISelfModeDAO;

/**
 * DAO Factory is an abstract class which represents as the factory for all DAO
 * objects.
 */
public abstract class DAOFactory {

    /**
     * The DAO Type for SQL Server.
     */
    public static final int SQLSERVER = 0;
    /**
     * The DAO type for CloudScape.
     */
    public static final int CLOUDSCAPE = 1;
    /**
     * The DAO type for Oracle.
     */
    public static final int ORACLE = 2;
    /**
     * The DAO type for Sybase.
     */
    public static final int SYBASE = 3;

    /**
     * Gets the DAO object for manipulating Departments in the database.
     *
     * @return DAO Object for Departments
     * @throws DaoException
     *             - Exception thrown when getting the DAO object fails
     * @see IDepartmentDAO
     */
    public abstract IDepartmentDAO getDepartmentDAO() throws DaoException;

    /**
     * Gets the DAO object for manipulating Items in the database.
     *
     * @return DAO Object for Items
     * @throws DaoException
     *             - Exception thrown when getting the DAO object fails
     * @see IItemDAO
     */
    public abstract IItemDAO getItemDAO() throws DaoException;

    /**
     * Gets the DAO object for poslog status in the database.
     *
     * @return DAO Object for poslog status
     * @throws DaoException
     *             - Exception thrown when getting the DAO object fails
     * @see IPoslogStatusDAO
     */
    public abstract IPoslogStatusDAO getPoslogStatusDAO() throws DaoException;

    /**
     * Gets the DAO object for manipulating PosLog in the database.
     *
     * @return DAO Object for PosLog
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see IPosLogDAO
     */
    public abstract IPosLogDAO getPOSLogDAO() throws DaoException;

    /**
     * Gets the DAO object for manipulating Customer in the database.
     *
     * @return DAO Object for Customer
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see ICustomerDAO
     */
    public abstract ICustomerDAO getCustomerDAO() throws DaoException;

    /**
     * Gets the DAO object for manipulating Admin information in the database
     * for Authentication.
     *
     * @return DAO Object for Admin of Authentication
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see IAuthAdminDao
     */
    public abstract IAuthAdminDao getAuthAdminDAO() throws DaoException;

    /**
     * Gets the DAO object for manipulating Device information in the database
     * for Authentication.
     *
     * @return DAO Object for Device of Authentication
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see IAuthDeviceDao
     */
    public abstract IAuthDeviceDao getAuthDeviceDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations for Credential Service.
     *
     * @return CredentialDao
     * @throws DaoException
     *             - Exception
     */
    public abstract ICredentialDAO getCredentialDAO() throws DaoException;

    /**
     * Gets the DAO object for Report Generation.
     *
     * @return IReportDAO object
     * @throws DaoException
     *             - Exception
     */
    public abstract IReportDAO getReportDAO() throws DaoException;

    /**
     * Gets the DAO object for Receipt Generation.
     *
     * @return IReceiptDAO object
     * @throws DaoException
     *             -Exception
     */
    public abstract IReceiptDAO getReceiptDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on System Configuration.
     *
     * @return The DAO object for SystemConfig
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerSystemConfigDAO
     */
    public abstract SQLServerSystemConfigDAO getSystemConfigDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on Store.
     *
     * @return The DAO object for Store
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerStoreDAO
     */
    public abstract IStoreDAO getStoreDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on Store.
     *
     * @return The DAO object for CorpStore
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerCorpStoreDAO
     */
    public abstract ICorpStoreDAO getCorpStoreDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on QueueBustering.
     *
     * @return The DAO object for QueueBuster
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     */
    public abstract IQueueBusterDAO getQueBusterDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on BusinessDate.
     *
     * @return The DAO object for BusinessDate
     * @throws DaoException
     *             Exception thrown when getting the DAO object fail
     */
    public abstract ISystemSettingDAO getSystemSettingDAO() throws DaoException;

    /**
     * Gets the DAO object for performing operations on TerminalInfo.
     *
     * @return The DAO object for TerminalInfo
     * @throws DaoException
     *             Exception thrown when getting the DAO object fail
     */
    public abstract ITerminalInfoDAO getTerminalInfoDAO() throws DaoException;

    /**
     * Gets the DAO object for Transfer transactions between smart phone and
     * POS.
     *
     * @return IForwardItemListDAO The DAO object
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerForwardItemListDAO
     */
    public abstract IForwardItemListDAO getForwardItemListDAO() throws DaoException;

    /**
     * Gets the DAO object for Transfer transactions between smart phone and
     * POS.
     *
     * @return ITxlCardFailureDAO The DAO object
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     */
    public abstract ITxlCardFailureDAO getTxlCardFailureDAO();

    /**
     * Gets the DAO object for Peripheral Controls.
     *
     * @return IPeripheralControlDAO The DAO object
     * @throws DaoException
     *             Exception
     */
    public abstract IDeviceInfoDAO getDeviceInfoDAO() throws DaoException;

    /**
     * Gets the DAO object for mobile device log upload/download.
     *
     * @return IDeviceLogDAO The DAO object
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     */
    public abstract IDeviceLogDAO getDeviceLogDao() throws DaoException;

    /**
     * Gets the DepartmentDao.
     *
     * @return IDepartmentDao The DAO object
     * @throws DaoException
     *             - Exception
     */
    public abstract IDepartmentDAO getDepartmentDao() throws DaoException;

    /**
     * Gets the DAO object for QueueBuster Link.
     *
     * @return ILinkDAO The DAO object
     * @throws DaoException
     *             Exception
     */
    public abstract ILinkDAO getQueueBusterLinkDAO() throws DaoException;

    /**
     * Gets the DAO object for Credential User Group.
     *
     * @return {@link IGroupDAO} The DAO object
     * @throws DaoException
     *             Exception
     */
    public abstract IGroupDAO getGroupDAO() throws DaoException;

    /**
     * Gets the DAO object for CreditAuthorization Link.
     *
     * @return ILinkDAO The DAO object
     * @throws DaoException
     *             Exception
     */
    public abstract ILinkDAO getCreditAuthorizationLinkDAO() throws DaoException;

    /**
     * Gets the DAO object for Signature Link.
     *
     * @return ILinkDAO The DAO object.
     * @throws DaoException
     *             Exception
     */
    public abstract ILinkDAO getSignatureLinkDAO() throws DaoException;

    /**
     * Gets the DAO Object for MixMatch.
     *
     * @return IMixMatchDAO
     * @throws DaoException
     *             exception
     */
    public abstract IMixMatchDAO getMixMatchDAO() throws DaoException;

    /**
     * Gets the DAO Object for MixMatch.
     *
     * @return IMixMatchDAO
     * @throws DaoException
     *             exception
     */
    public abstract IMasterMaintenanceDAO getMasterMaintenanceDAO() throws DaoException;

    /**
     * Gets the DAO Object for Code Convert.
     *
     * @return ICodeConvertDAO
     * @throws DaoException
     *             exception
     */
    public abstract ICodeConvertDAO getCodeConvertDAO() throws DaoException;

    /**
     * Gets the DAO Object for QrCodeInfo.
     *
     * @return QrCodeInfoDAO
     * @throws DaoException
     *             exception
     */
    public abstract IQrCodeInfoDAO getQrCodeInfoDAO() throws DaoException;

    /**
     * Gets the DAO Object for TaxRateInfo.
     *
     * @return TaxRateInfoDAO
     * @throws DaoException
     *             exception
     */
    public abstract ITaxRateInfoDAO getTaxRateInfoDAO() throws DaoException;

    public abstract INameSystemInfoDAO getNameSystemInfo() throws DaoException;

    public abstract ICashAccountDAO getCashAccountDAO() throws DaoException;

    /**
     * Gets the DAO object for Till Info.
     *
     * @return ITillInfoDAO The DAO object.
     * @throws DaoException
     *             Exception
     */
    public abstract ITillInfoDAO getTillInfoDAO() throws DaoException;

    /**
     * Retrieves the concrete Factory for the DAO.
     *
     * @param whichFactory
     *            Identifies which storage implementation the DAO Factory to
     *            retrieve is specific to.
     * @return The DAOFactory for the specified storage implementation.
     */
    public static DAOFactory getDAOFactory(final int whichFactory) {
        switch (whichFactory) {
        case SQLSERVER:
            return new SQLServerDAOFactory();
        case CLOUDSCAPE:
            return null;
        case ORACLE:
            return null;
        case SYBASE:
            return null;
        default:
            return null;
        }
    }

    public abstract ICommonDAO getCommonDAO() throws DaoException;

    public abstract ICustomerSearthDAO getCustomerSearthDAO() throws DaoException;

    public abstract IFuturePayDAO getFuturePayDAO() throws DaoException;

    public abstract ICardInfoDAO getCardInfoDAO() throws Exception;

    public abstract ITenderInfoDAO getTenderInfoDAO() throws Exception;

    public abstract ICashAbstractDAO getCashAbstractDAO() throws Exception;

    public abstract IBarCodeDAO getDiscountInfo() throws Exception;

    public abstract ICustomerClassInfoDAO getCustomerClassInfoDAO() throws Exception;

    public abstract INationalityInfoDAO getNationalityInfoDAO() throws Exception;

    public abstract IBuyadditionalInfoDAO getBuyadditionalInfoDAO() throws Exception;

    public abstract ISettlementInfoDAO getSettlementInfoDAO() throws Exception;

    public abstract DiscountPlanInfoCommomDAO getDiscountPlanInfoCommomDAO() throws DaoException;

    public abstract IPromotionInfoDAO getPromotionInfoDAO() throws DaoException;

    public abstract IEventLogDAO getEventLogDAO() throws DaoException;

    public abstract IPremiumFlagDAO getPremiumFlagDAO() throws DaoException;

    public abstract IPointDAO getPointDAO() throws Exception;

    /**
     * Gets the DAO object for performing operations on Store.
     *
     * @return The DAO object for CM Preset Info.
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerStoreDAO
     */
    public abstract IStoreDAO getCMPresetInfoDAO() throws DaoException;

    /**
     * Get Offline Credit data DAO. return the DAO.
     *
     * @throws DaoException
     *             Exception thrown when getting the DAO object fails.
     * @see SQLServerStoreDAO
     */
    public abstract IOfflineCreditDAO getOfflineCreditDAO() throws DaoException;

    public abstract ICardInfoDAO getMemberInfo() throws Exception;

	public abstract ICardInfoDAO getStatusInfo() throws Exception;

	public abstract ICreditCardAbstractDAO getCreditCardInfo() throws Exception;

	public abstract IAppServerDAO getAppServerDAO() throws DaoException;

	public abstract IPricePromInfoDAO getPricePromInfoDAO() throws DaoException;

	public abstract IPriceMMInfoDAO getPriceMMInfoDAO() throws DaoException;

	public abstract IPromotionMsgInfoDAO getPromotionMsgInfoDAO() throws DaoException;

	public abstract IPOSMailInfoDAO getPOSMailInfoDAO() throws DaoException;

	public abstract IEmployeeDao getEmployeeDao() throws DaoException;
	
	public abstract ISelfModeDAO getISelfModeDAO()throws DaoException;
}