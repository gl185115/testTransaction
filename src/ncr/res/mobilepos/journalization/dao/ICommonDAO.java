package ncr.res.mobilepos.journalization.dao;

import java.sql.SQLException;
/**
 * �V�K����
 * �o�[�W����      �V�K���t        �S���Җ�      �V�K���e
 * 1.01             2014.11.19      LIQIAN     �q�w���擾
 * 1.02             2014.11.19      FENGSHA    �O������擾��Ή�
 * 1.03             2014.11.19      GUZHEN     �̔������擾
 * 1.04             2014.11.20      MLWANG     �O����ԍ��擾
 * 1.05             2014.12.11      FENGSHA     ����ԍ��ɂ��O����ԍ��擾
 * 1.06             2014.12.22      LiQian     ������擾
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
    /* 1.05 2014.12.11 FENGSHA ����ԍ��ɂ��O����ԍ��擾 ADD START */
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
    /* 1.05 2014.12.11 FENGSHA ����ԍ��ɂ��O����ԍ��擾 ADD END */
    
   /**
    * �O�J�ꗗ�@�擾
    * @param CompanyId
    * @param RetailStoreId
    * @param TrainingFlag
    * @param LayawayFlag
    * @param Queue
    * @param TxType
    * @param BussinessDayDate
    * @return�@�ꗗ���X�g
    * @throws DaoException
    */
   public List<ForwardListInfo> getForwardList (String CompanyId, String RetailStoreId, String TrainingFlag, String LayawayFlag, String Queue, String TxType, String BussinessDayDate) throws DaoException;

   /**
    * �O�J���R�[�h�@�X�e�[�^�X�̍X�V
    * @param CompanyId
    * @param RetailStoreId
    * @param WorkstationId
    * @param SequenceNumber
    * @param Queue
    * @param BusinessDayDate
    * @param TrainingFlag
    * @param Status
    * @return�@ResultBase
    * @throws SQLException
    */
   public int updateForwardStatus(String CompanyId, String RetailStoreId, String WorkstationId, String SequenceNumber, String Queue, String BusinessDayDate, String TrainingFlag, int Status) throws DaoException, SQLException;
}
