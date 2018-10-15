package ncr.res.mobilepos.journalization.dao;

import java.sql.SQLException;
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

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.EventDetail;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.GoldCertificate;
import ncr.res.mobilepos.journalization.model.GuestZoneInfo;
import ncr.res.mobilepos.journalization.model.Reservation;
import ncr.res.mobilepos.journalization.model.Salespersoninfo;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.journalization.model.SequenceNo;


public interface ICommonDAO {
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
    
   /**
    * 前捌一覧　取得
    * @param CompanyId
    * @param RetailStoreId
    * @param TrainingFlag
    * @param LayawayFlag
    * @param Queue
    * @param TxType
    * @param BussinessDayDate
    * @return　一覧リスト
    * @throws DaoException
    */
   public List<ForwardListInfo> getForwardList (String CompanyId, String RetailStoreId, String TrainingFlag, String LayawayFlag, String Queue, String TxType, String BussinessDayDate) throws DaoException;

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
