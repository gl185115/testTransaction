//
// Copyright(c) 2015 NCR Japan Ltd.
//
package ncr.res.mobilepos.offlinecredit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Offline credit data holder.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OfflineData")
public class OfflineData {
    /**
     * Create empty instance.
     */
    public OfflineData() {
    }

    @XmlElement(name = "companyId")
    String companyId;
    /** 会社番号を設定する。*/
    public void setCompanyId(String val) {
        companyId = val;
    }
    /** 会社番号を取得する。*/
    public String getCompanyId() {
        return companyId;
    }
    @XmlElement(name = "retailStoreId")
    String retailStoreId;
    /** 店舗番号を設定する。*/
    public void setRetailStoreId(String val) {
        retailStoreId = val;
    }
    /** 店舗番号を取得する。*/
    public String getRetailStoreId() {
        return retailStoreId;
    }
    @XmlElement(name = "workstationId")
    String workstationId;
    /** 端末IDを設定する。*/
    public void setWorkstationId(String id) {
        workstationId = id;
    }
    /** 端末IDを取得する。*/
    public String getWorkstationId() {
        return workstationId;
    }
    @XmlElement(name = "sequenceNumber")
    int sequenceNumber;
    /** 取引番号を設定する。*/
    public void setSequenceNumber(int val) {
        sequenceNumber = val;
    }
    /** 取引番号を取得する。*/
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    @XmlElement(name = "businessDayDate")
    String businessDayDate;
    /** 営業日付を設定する。 */
    public void setBusinessDayDate(String val) {
        businessDayDate = val;
    }
    /** 営業日付を取得する。 */
    public String getBusinessDayDate() {
        return businessDayDate;
    }
    @XmlElement(name = "IV")
    String iv;
    /** IVを設定する。*/
    public void setIV(String val) {
        iv = val;
    }
    /** IVを取得する。*/
    public String getIV() {
        return iv;
    }
    @XmlElement(name = "personalData")
    String personalData;
    /** データ本体を設定する。*/
    public void setPersonalData(String val) {
        personalData = val;
    }
    /** データ本体を取得する。*/
    public String getPersonalData() {
        return personalData;
    }
}


