package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoreInfo")
public class StoreInfo extends ResultBase {
    
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreShortName() {
        return storeShortName;
    }

    public void setStoreShortName(String storeShortName) {
        this.storeShortName = storeShortName;
    }

    public String getStoreKubun() {
        return storeKubun;
    }

    public void setStoreKubun(String storeKubun) {
        this.storeKubun = storeKubun;
    }

    public String getStoreZip() {
        return storeZip;
    }

    public void setStoreZip(String storeZip) {
        this.storeZip = storeZip;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    public String getStoreAddr1() {
        return storeAddr1;
    }

    public void setStoreAddr1(String storeAddr1) {
        this.storeAddr1 = storeAddr1;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getStoreFax() {
        return storeFax;
    }

    public void setStoreFax(String storeFax) {
        this.storeFax = storeFax;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getCdMsg() {
        return cdMsg;
    }

    public void setCdMsg(String cdMsg) {
        this.cdMsg = cdMsg;
    }

    public String getElectroFilePath() {
        return electroFilePath;
    }

    public void setElectroFilePath(String electroFilePath) {
        this.electroFilePath = electroFilePath;
    }

    public String getStampTaxFilePath() {
        return stampTaxFilePath;
    }

    public void setStampTaxFilePath(String stampTaxFilePath) {
        this.stampTaxFilePath = stampTaxFilePath;
    }

    public String getStoreCompCode() {
        return storeCompCode;
    }

    public void setStoreCompCode(String storeCompCode) {
        this.storeCompCode = storeCompCode;
    }

    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    public String getSubCode2() {
        return subCode2;
    }

    public void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    public String getSubCode3() {
        return subCode3;
    }

    public void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    public String getSubCode4() {
        return subCode4;
    }

    public void setSubCode4(String subCode4) {
        this.subCode4 = subCode4;
    }

    public String getSubCode5() {
        return subCode5;
    }

    public void setSubCode5(String subCode5) {
        this.subCode5 = subCode5;
    }

    public String getSubCode6() {
        return subCode6;
    }

    public void setSubCode6(String subCode6) {
        this.subCode6 = subCode6;
    }

    public String getSubCode7() {
        return subCode7;
    }

    public void setSubCode7(String subCode7) {
        this.subCode7 = subCode7;
    }

    public String getSubCode8() {
        return subCode8;
    }

    public void setSubCode8(String subCode8) {
        this.subCode8 = subCode8;
    }

    public String getSubCode9() {
        return subCode9;
    }

    public void setSubCode9(String subCode9) {
        this.subCode9 = subCode9;
    }

    public String getSubCode10() {
        return subCode10;
    }

    public void setSubCode10(String subCode10) {
        this.subCode10 = subCode10;
    }

    public String getSubCode11() {
        return subCode11;
    }

    public void setSubCode11(String subCode11) {
        this.subCode11 = subCode11;
    }

    public String getSubCode12() {
        return subCode12;
    }

    public void setSubCode12(String subCode12) {
        this.subCode12 = subCode12;
    }

    public String getSubCode13() {
        return subCode13;
    }

    public void setSubCode13(String subCode13) {
        this.subCode13 = subCode13;
    }

    public String getSubCode14() {
        return subCode14;
    }

    public void setSubCode14(String subCode14) {
        this.subCode14 = subCode14;
    }

    public String getSubCode15() {
        return subCode15;
    }

    public void setSubCode15(String subCode15) {
        this.subCode15 = subCode15;
    }

    public String getSubCode16() {
        return subCode16;
    }

    public void setSubCode16(String subCode16) {
        this.subCode16 = subCode16;
    }

    public String getSubCode17() {
        return subCode17;
    }

    public void setSubCode17(String subCode17) {
        this.subCode17 = subCode17;
    }

    public String getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }

    public String getHostUpdDate() {
        return hostUpdDate;
    }

    public void setHostUpdDate(String hostUpdDate) {
        this.hostUpdDate = hostUpdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "CompanyId")
    private String companyid;

    @XmlElement(name = "StoreId")
    private String storeId;

    @XmlElement(name = "StoreName")
    private String storeName;

    @XmlElement(name = "StoreShortName")
    private String storeShortName;

    @XmlElement(name = "StoreKubun")
    private String storeKubun;

    @XmlElement(name = "StoreZip")
    private String storeZip;

    @XmlElement(name = "StoreAddr")
    private String storeAddr;

    @XmlElement(name = "StoreAddr1")
    private String storeAddr1;

    @XmlElement(name = "StoreAddr2")
    private String storeAddr2;

    @XmlElement(name = "StoreTel")
    private String storeTel;

    @XmlElement(name = "StoreFax")
    private String storeFax;

    @XmlElement(name = "Ads")
    private String ads;

    @XmlElement(name = "CdMsg")
    private String cdMsg;

    @XmlElement(name = "ElectroFilePath")
    private String electroFilePath;

    @XmlElement(name = "StampTaxFilePath")
    private String stampTaxFilePath;

    @XmlElement(name = "StoreCompCode")
    private String storeCompCode;

    @XmlElement(name = "SubCode1")
    private String subCode1;

    @XmlElement(name = "SubCode2")
    private String subCode2;

    @XmlElement(name = "SubCode3")
    private String subCode3;

    @XmlElement(name = "SubCode4")
    private String subCode4;

    @XmlElement(name = "SubCode5")
    private String subCode5;

    @XmlElement(name = "SubCode6")
    private String subCode6;

    @XmlElement(name = "SubCode7")
    private String subCode7;

    @XmlElement(name = "SubCode8")
    private String subCode8;

    @XmlElement(name = "SubCode9")
    private String subCode9;

    @XmlElement(name = "SubCode10")
    private String subCode10;

    @XmlElement(name = "SubCode11")
    private String subCode11;

    @XmlElement(name = "SubCode12")
    private String subCode12;

    @XmlElement(name = "SubCode13")
    private String subCode13;

    public String getStoreAddr2() {
        return storeAddr2;
    }

    public void setStoreAddr2(String storeAddr2) {
        this.storeAddr2 = storeAddr2;
    }

    @XmlElement(name = "SubCode14")
    private String subCode14;

    @XmlElement(name = "SubCode15")
    private String subCode15;

    @XmlElement(name = "SubCode16")
    private String subCode16;

    @XmlElement(name = "SubCode17")
    private String subCode17;

    @XmlElement(name = "SubNum1")
    private String subNum1;
    
    @XmlElement(name = "SubSmallInt1")
    private String subSmallInt1;
    
    public String getSubSmallInt1() {
    	return this.subSmallInt1;
    }
    public void setSubSmallInt1(String str) {
    	this.subSmallInt1 = str;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @XmlElement(name = "HostUpdDate")
    private String hostUpdDate;

    @XmlElement(name = "Status")
    private String status;
    
    @XmlElement(name = "CompanyName")
    private String companyName;



}
