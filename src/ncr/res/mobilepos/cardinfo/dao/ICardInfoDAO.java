package ncr.res.mobilepos.cardinfo.dao;

import java.util.List;

import ncr.res.mobilepos.cardinfo.model.CardClassInfo;
import ncr.res.mobilepos.cardinfo.model.CardTypeInfo;
import ncr.res.mobilepos.cardinfo.model.MemberInfo;
import ncr.res.mobilepos.cardinfo.model.StatusInfo;
import ncr.res.mobilepos.exception.DaoException;

public interface ICardInfoDAO {

    List<CardClassInfo> getCardClassInfo(String companyId, String storeId, String cardClassId, String membershipId)
            throws Exception;

    public List<CardTypeInfo> getCardTypeInfoByCardType(String companyId, String storeId, String cardTypeNo)
            throws Exception;

    public MemberInfo getMemberInfoById(String companyId, String storeId, String membershipId) throws Exception;

    public StatusInfo getStatusInfoByCode(String companyId, String storeId, String statusCode) throws DaoException;

}
