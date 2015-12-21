package ncr.res.mobilepos.journalization.dao;

/**
 * 新規履歴
 * バージョン      新規日付        担当者名      新規内容
 * 1.01             2014.11.19      LIQIAN     客層情報取得
 * 1.02             2014.11.19      FENGSHA    前受金情報取得を対応
 * 1.03             2014.11.19      GUZHEN     販売員情報取得
 * 1.04             2014.11.20      MLWANG     前受金番号取得
 * 1.05             2014.12.11      FENGSHA     取引番号により前受金番号取得
 * 1.06             2014.12.22      LiQian     金種情報取得
 */
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.EventDetail;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.GuestZoneInfo;
import ncr.res.mobilepos.journalization.model.GoldCertificate;
import ncr.res.mobilepos.journalization.model.Salespersoninfo;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.journalization.model.Reservation;
import ncr.res.mobilepos.journalization.model.SequenceNo;


public interface IBarneysCommonDAO {
    // 1.01 2014.11.19 LIQIAN  客層情報を対応  START
    /**
     * @return return the guest zone information
     *
     * @throws DaoException
     *            Thrown when process fails.
     */
    ArrayList<GuestZoneInfo> getGuestZoneList() throws DaoException;
   // 1.01 2014.11.19 LIQIAN   客層情報を対応   END
   // 1.02 2014.11.19 FENGSHA  前受金情報を対応   START
    /**
     * @param guestNo
     *
     * @return return the guest order information
     *
     * @throws DaoException
     *         Thrown when process fails.
     */
    SearchGuestOrder searchGuestOrderInfo(String guestNo) throws DaoException;
   // 1.02 2014.11.19 FENGSHA  前受金情報を対応   START
   // 1.03 2014.11.19 GUZHEN   出力を対応           START
    /**
     * @param OpeKanaName
     * @return the SalesPerson information
     * @throws DaoException
     *             Thrown when process fails.
     */
    ArrayList<Salespersoninfo> SearchSalesPerson(String OpeKanaName)
            throws DaoException;

    // 1.03 2014.11.19 GUZHEN 出力を対応 END
    // 1.04 2014.11.20 MLWANG 前受金番号を対応 START
    /**
     * @return the sequence number
     * @param SequenceTypeId
     * @throws DaoException
     *             Thrown when process fails.
     */
    public SequenceNo getNextSequenceNo(String SequenceTypeId)
            throws DaoException;

    /* 1.05 2014.12.11 FENGSHA 取引番号により前受金番号取得 ADD START */
    /**
     * @param storeId
     *            the store Id
     * @param deviceId
     *            the device Id
     * @param sequenceNo
     *            the sequence No
     * @param businessDate
     *            the business Date
     * @return return the guest order information
     * @throws DaoException
     *             Thrown when process fails.
     */
    public SearchGuestOrder searchGuestOrderInfoBySequenceNo(String storeId,
            String deviceId, String sequenceNo, String businessDate)
            throws DaoException;
    /* 1.05 2014.12.11 FENGSHA 取引番号により前受金番号取得 ADD END */
    /* 1.06 2014.12.22 LiQian 金種情報取得 ADD Start */
    /**
     * 
     * @param compCat
     * @return GoldCertificate
     * @throws DaoException
     */
	public GoldCertificate getGoldCertificateType(String compCat) throws DaoException;
	/* 1.06 2014.12.22 LiQian 金種情報取得 ADD END */
	
	   /**
     * @return list of the event information
     *
     * @throws DaoException
     *            Thrown when process fails.
     */
    List<EventDetail> getEventList(String eventId,int eventKbn,
    		int businessDateId,String storeId,String pluCode) throws DaoException;
    
    /**
     *get event login result set.
     *
     * @param eventId
     *            The event id.
     * @param storeId
     *            The store id.
     * @return event detail for event login check use.
     * @throws DaoException
     *             Thrown when error occurs.
     */
   public EventDetail getEventLoginResultSet(String eventId,String storeId) throws DaoException;
   
   /**
    * @param reservationId
    *
    * @return return the reservation information
    *
    * @throws DaoException
    *         Thrown when process fails.
    */
   Reservation searchReservationInfo(String reservationId) throws DaoException;
   
   /**
    * 前捌一覧　取得
    * @param CompanyId
    * @param RetailStoreId
    * @param TrainingFlag
    * @return　一覧リスト
    * @throws DaoException
    */
   public List<ForwardListInfo> getForwardList (String CompanyId, String RetailStoreId, String TrainingFlag, String LayawayFlag) throws DaoException;
   
   /**
    * 前捌レコード　ステータスの更新
    * @param CompanyId
    * @param RetailStoreId
    * @param WorkstationId
    * @param SequenceNumber
    * @param Queue
    * @param BusinessDayDate
    * @param TrainingFlag
    * @param Status
    * @return　ResultBase
    * @throws SQLException 
    */
   public int updateForwardStatus(String CompanyId, String RetailStoreId, String WorkstationId, String SequenceNumber, String Queue, String BusinessDayDate, String TrainingFlag, int Status) throws DaoException, SQLException;
}
