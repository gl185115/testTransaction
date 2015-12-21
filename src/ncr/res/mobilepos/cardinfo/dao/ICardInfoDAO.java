package ncr.res.mobilepos.cardinfo.dao;

import java.util.List;

import ncr.res.mobilepos.cardinfo.model.CardClassInfo;
import ncr.res.mobilepos.cardinfo.model.CardTypeInfo;

public interface ICardInfoDAO {

    List<CardClassInfo> getCardClassInfo(String companyId, String storeId, 
    		String cardClassId, String membershipId) throws Exception;
    public List<CardTypeInfo> getCardTypeInfoByCardType (String companyId, String storeId, 
            String cardTypeNo ) throws Exception;

}
