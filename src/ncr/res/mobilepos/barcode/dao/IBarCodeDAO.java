package ncr.res.mobilepos.barcode.dao;

import atg.taglib.json.util.JSONObject;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.xebioapi.model.JSONData;

public interface IBarCodeDAO {

    /**
     * 
     * @param companyId
     * @param storeId
     * @param cardType
     * @param seqNo
     * @param discountType
     * @return barcodeInfo
     * @throws DaoException
     */
	
	 public JSONData getDiscountInfo(String companyId, String storeId, String cardType, String seqNo, String discountType) throws DaoException;
	 
	 JSONObject isMemberCard(String cardCode) throws DaoException; 
}