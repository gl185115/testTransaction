package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoreInfo")
@ApiModel(value="StoreInfo")
public class StoreInfo extends ResultBase {
	@ApiModelProperty(value="ïÐR[h", notes="ïÐR[h")
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    @ApiModelProperty(value="XÜR[h", notes="XÜR[h")
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @ApiModelProperty(value="XÜ¼", notes="XÜ¼")
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @ApiModelProperty(value="XÜ¼iªÌj", notes="XÜ¼iªÌj")
    public String getStoreShortName() {
        return storeShortName;
    }

    public void setStoreShortName(String storeShortName) {
        this.storeShortName = storeShortName;
    }

    @ApiModelProperty(value="XÜæª", notes="XÜæª")
    public String getStoreKubun() {
        return storeKubun;
    }

    public void setStoreKubun(String storeKubun) {
        this.storeKubun = storeKubun;
    }

    @ApiModelProperty(value="XÜXÖÔ", notes="XÜXÖÔ")
    public String getStoreZip() {
        return storeZip;
    }

    public void setStoreZip(String storeZip) {
        this.storeZip = storeZip;
    }

    @ApiModelProperty(value="XÜZ", notes="XÜZ")
    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    @ApiModelProperty(value="XÜZP", notes="XÜZP")
    public String getStoreAddr1() {
        return storeAddr1;
    }

    public void setStoreAddr1(String storeAddr1) {
        this.storeAddr1 = storeAddr1;
    }

    @ApiModelProperty(value="XÜdbÔ", notes="XÜdbÔ")
    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    @ApiModelProperty(value="XÜFAXÔ", notes="XÜFAXÔ")
    public String getStoreFax() {
        return storeFax;
    }

    public void setStoreFax(String storeFax) {
        this.storeFax = storeFax;
    }

    @ApiModelProperty(value="V[gL¶", notes="V[gL¶")
    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    @ApiModelProperty(value="JX^}[fBXvCbZ[W", notes="JX^}[fBXvCbZ[W")
    public String getCdMsg() {
        return cdMsg;
    }

    public void setCdMsg(String cdMsg) {
        this.cdMsg = cdMsg;
    }

    @ApiModelProperty(value="V[gGNgrbg}bvt@CpX", notes="V[gGNgrbg}bvt@CpX")
    public String getElectroFilePath() {
        return electroFilePath;
    }

    public void setElectroFilePath(String electroFilePath) {
        this.electroFilePath = electroFilePath;
    }

    @ApiModelProperty(value="óÅã[rbg}bvt@CpX", notes="óÅã[rbg}bvt@CpX")
    public String getStampTaxFilePath() {
        return stampTaxFilePath;
    }

    public void setStampTaxFilePath(String stampTaxFilePath) {
        this.stampTaxFilePath = stampTaxFilePath;
    }

    @ApiModelProperty(value="éÆÌR[h", notes="éÆÌR[h")
    public String getStoreCompCode() {
        return storeCompCode;
    }

    public void setStoreCompCode(String storeCompCode) {
        this.storeCompCode = storeCompCode;
    }

    @ApiModelProperty(value="ÆÔR[h", notes="ÆÔR[h")
    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    @ApiModelProperty(value="GAR[h", notes="GAR[h")
    public String getSubCode2() {
        return subCode2;
    }

    public void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    @ApiModelProperty(value="ÓCÒ¼", notes="ÓCÒ¼")
    public String getSubCode3() {
        return subCode3;
    }

    public void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    @ApiModelProperty(value="XÖÔ", notes="XÖÔ")
    public String getSubCode4() {
        return subCode4;
    }

    public void setSubCode4(String subCode4) {
        this.subCode4 = subCode4;
    }

    @ApiModelProperty(value="XÖÔ2", notes="XÖÔ2")
    public String getSubCode5() {
        return subCode5;
    }

    public void setSubCode5(String subCode5) {
        this.subCode5 = subCode5;
    }

    @ApiModelProperty(value="N/fr`[Á¿X¼ãi", notes="N/fr`[Á¿X¼ãi")
    public String getSubCode6() {
        return subCode6;
    }

    public void setSubCode6(String subCode6) {
        this.subCode6 = subCode6;
    }

    @ApiModelProperty(value="N/fr`[Á¿X¼ºi", notes="N/fr`[Á¿X¼ºi")
    public String getSubCode7() {
        return subCode7;
    }

    public void setSubCode7(String subCode7) {
        this.subCode7 = subCode7;
    }

    @ApiModelProperty(value="NWbg¤iR[h", notes="NWbg¤iR[h")
    public String getSubCode8() {
        return subCode8;
    }

    public void setSubCode8(String subCode8) {
        this.subCode8 = subCode8;
    }

    @ApiModelProperty(value="¢`[1ÚT¶¾", notes="¢`[1ÚT¶¾")
    public String getSubCode9() {
        return subCode9;
    }

    public void setSubCode9(String subCode9) {
        this.subCode9 = subCode9;
    }

    @ApiModelProperty(value="¢`[2ÚT¶¾", notes="¢`[2ÚT¶¾")
    public String getSubCode10() {
        return subCode10;
    }

    public void setSubCode10(String subCode10) {
        this.subCode10 = subCode10;
    }

    @ApiModelProperty(value="¢`[3ÚT¶¾", notes="¢`[3ÚT¶¾")
    public String getSubCode11() {
        return subCode11;
    }

    public void setSubCode11(String subCode11) {
        this.subCode11 = subCode11;
    }

    @ApiModelProperty(value="¢`[4ÚT¶¾", notes="¢`[4ÚT¶¾")
    public String getSubCode12() {
        return subCode12;
    }

    public void setSubCode12(String subCode12) {
        this.subCode12 = subCode12;
    }

    @ApiModelProperty(value="JXNú", notes="JXNú")
    public String getSubCode13() {
        return subCode13;
    }

    public void setSubCode13(String subCode13) {
        this.subCode13 = subCode13;
    }

    @ApiModelProperty(value="JX", notes="JX")
    public String getSubCode14() {
        return subCode14;
    }

    public void setSubCode14(String subCode14) {
        this.subCode14 = subCode14;
    }

    @ApiModelProperty(value="ópXÜ¼", notes="ópXÜ¼")
    public String getSubCode15() {
        return subCode15;
    }

    public void setSubCode15(String subCode15) {
        this.subCode15 = subCode15;
    }

    @ApiModelProperty(value="ópZ1", notes="ópZ1")
    public String getSubCode16() {
        return subCode16;
    }

    public void setSubCode16(String subCode16) {
        this.subCode16 = subCode16;
    }

    @ApiModelProperty(value="ópZ2", notes="ópZ2")
    public String getSubCode17() {
        return subCode17;
    }

    public void setSubCode17(String subCode17) {
        this.subCode17 = subCode17;
    }

    @ApiModelProperty(value="¢`[", notes="¢`[")
    public String getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }
    
    @ApiModelProperty(value="¸Zñ", notes="¸Zñ")
    public int getStoreSettleCount() {
        return storeSettleCount;
    }

    public void setStoreSettleCount(int storeSettleCount) {
        this.storeSettleCount = storeSettleCount;
    }

    @ApiModelProperty(value="HOSTIFXVú", notes="HOSTIFXVú")
    public String getHostUpdDate() {
        return hostUpdDate;
    }

    public void setHostUpdDate(String hostUpdDate) {
        this.hostUpdDate = hostUpdDate;
    }

    @ApiModelProperty(value="Xe[^X", notes="Xe[^X")
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

    @ApiModelProperty(value="XÜZQ", notes="XÜZQ")
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
    
    @XmlElement(name = "StoreSettleCount")
    private int storeSettleCount;
    
    @XmlElement(name = "SubSmallInt1")
    private String subSmallInt1;
    
    @ApiModelProperty(value="ÝÉXVL³", notes="ÝÉXVL³")
    public String getSubSmallInt1() {
    	return this.subSmallInt1;
    }
    public void setSubSmallInt1(String str) {
    	this.subSmallInt1 = str;
    }

    @ApiModelProperty(value="ïÐ¼", notes="ïÐ¼")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    @ApiModelProperty(value="ÆÒo^Ô", notes="ÆÒo^Ô")
    public String getBusinessRegistrationNo() {
        return businessRegistrationNo;
    }

    public void setBusinessRegistrationNo(String businessRegistrationNo) {
        this.businessRegistrationNo = businessRegistrationNo;
    }

    @XmlElement(name = "HostUpdDate")
    private String hostUpdDate;

    @XmlElement(name = "Status")
    private String status;
    
    @XmlElement(name = "CompanyName")
    private String companyName;

    @XmlElement(name = "BusinessRegistrationNo")
    private String businessRegistrationNo;

}
