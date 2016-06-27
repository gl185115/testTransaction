package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Reservation Information
 * Model for reservation information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReservationInfo")
@ApiModel(value="ReservationInfo")
public class ReservationInfo {

    /*
     * the GuestNO field the value.
     */
    @XmlElement(name = "GuestNO")
    private String GuestNO;
    /*
     * the Line field the value.
     */
    @XmlElement(name = "Line")
    private String Line;
    /*
     * the RetailStoreId field the value.
     */
    @XmlElement(name = "RetailStoreId")
    private String RetailStoreId;
    /*
     * the BusinessDate field the value.
     */
    @XmlElement(name = "BusinessDate")
    private String BusinessDate;
    /*
     * the BusinessDateTime field the value.
     */
    @XmlElement(name = "BusinessDateTime")
    private String BusinessDateTime;
    /*
     * the TranNo field the value.
     */
    @XmlElement(name = "TranNo")
    private String TranNo;
    /*
     * the TerminalNo field the value.
     */
    @XmlElement(name = "TerminalNo")
    private String TerminalNo;
    /*
     * the OpeCode field the value.
     */
    @XmlElement(name = "OpeCode")
    private String OpeCode;
    /*
     * the OpeName field the value.
     */
    @XmlElement(name = "OpeName")
    private String OpeName;
    /*
     * the KaiinNo field the value.
     */
    @XmlElement(name = "KaiinNo")
    private String KaiinNo;
    /*
     * the KaiinNoSeiSan field the value.
     */
    @XmlElement(name = "KaiinNoSeiSan")
    private String KaiinNoSeiSan;
    /*
     * the OrderKbn field the value.
     */
    @XmlElement(name = "OrderKbn")
    private String OrderKbn;
    /*
     * the MdInternal field the value.
     */
    @XmlElement(name = "MdInternal")
    private String MdInternal;
    /*
     * the Md1 field the value.
     */
    @XmlElement(name = "Md1")
    private String Md1;
    /*
     * the Md2 field the value.
     */
    @XmlElement(name = "Md2")
    private String Md2;
    /*
     * the Md3 field the value.
     */
    @XmlElement(name = "Md3")
    private String Md3;
    /*
     * the Md4 field the value.
     */
    @XmlElement(name = "Md4")
    private String Md4;
    /*
     * the Md5 field the value.
     */
    @XmlElement(name = "Md5")
    private String Md5;
    /*
     * the Md6 field the value.
     */
    @XmlElement(name = "Md6")
    private String Md6;
    /*
     * the Md7 field the value.
     */
    @XmlElement(name = "Md7")
    private String Md7;
    /*
     * the Md8 field the value.
     */
    @XmlElement(name = "Md8")
    private String Md8;
    /*
     * the Md9 field the value.
     */
    @XmlElement(name = "Md9")
    private String Md9;
    /*
     * the Md10 field the value.
     */
    @XmlElement(name = "Md10")
    private String Md10;
    /*
     * the MdName field the value.
     */
    @XmlElement(name = "MdName")
    private String MdName;
    /*
     * the SalesCnt field the value.
     */
    @XmlElement(name = "SalesCnt")
    private String SalesCnt;
    /*
     * the SalesPrice field the value.
     */
    @XmlElement(name = "SalesPrice")
    private String SalesPrice;
    /*
     * the SalesAmt field the value.
     */
    @XmlElement(name = "SalesAmt")
    private String SalesAmt;
    /*
     * the DepositAmt field the value.
     */
    @XmlElement(name = "DepositAmt")
    private String DepositAmt;
    /*
     * the TelKakuninFlag field the value.
     */
    @XmlElement(name = "TelKakuninFlag")
    private String TelKakuninFlag;
    /*
     * the SeisanDate field the value.
     */
    @XmlElement(name = "SeisanDate")
    private String SeisanDate;
    /*
     * the SeisanDateTime field the value.
     */
    @XmlElement(name = "SeisanDateTime")
    private String SeisanDateTime;
    /*
     * the CommitSalesAmt field the value.
     */
    @XmlElement(name = "CommitSalesAmt")
    private String CommitSalesAmt;
    /*
     * the RepayAmt field the value.
     */
    @XmlElement(name = "RepayAmt")
    private String RepayAmt;
    /*
     * the BalanceAmt field the value.
     */
    @XmlElement(name = "BalanceAmt")
    private String BalanceAmt;
    /*
     * the StatusCode field the value.
     */
    @XmlElement(name = "StatusCode")
    private String StatusCode;
    /*
     * the POSFlag field the value.
     */
    @XmlElement(name = "POSFlag")
    private String POSFlag;
    /*
     * the PluFlag field the value.
     */
    @XmlElement(name = "PluFlag")
    private String PluFlag;
    /*
     * the Memo field the value.
     */
    @XmlElement(name = "Memo")
    private String Memo;
    /*
     * the MerchandiseHierarchy1 field the value.
     */
    @XmlElement(name = "MerchandiseHierarchy1")
    private String MerchandiseHierarchy1;
    /*
     * the MerchandiseHierarchy2 field the value.
     */
    @XmlElement(name = "MerchandiseHierarchy2")
    private String MerchandiseHierarchy2;
    /*
     * the MerchandiseHierarchy3 field the value.
     */
    @XmlElement(name = "MerchandiseHierarchy3")
    private String MerchandiseHierarchy3;
    /*
     * the MerchandiseHierarchy4 field the value.
     */
    @XmlElement(name = "MerchandiseHierarchy4")
    private String MerchandiseHierarchy4;
    /*
     * the MerchandiseHierarchy5 field the value.
     */
    @XmlElement(name = "MerchandiseHierarchy5")
    private String MerchandiseHierarchy5;
    /*
     * the TaxType field the value.
     */
    @XmlElement(name = "TaxType")
    private String TaxType;
    /*
     * the PosMdType field the value.
     */
    @XmlElement(name = "PosMdType")
    private String PosMdType;
    /*
     * the TagType field the value.
     */
    @XmlElement(name = "TagType")
    private String TagType;
    /*
     * the KeyPlu field the value.
     */
    @XmlElement(name = "KeyPlu")
    private String KeyPlu;
    /*
     * the MdType field the value.
     */
    @XmlElement(name = "MdType")
    private String MdType;
    /*
     * the Plu field the value.
     */
    @XmlElement(name = "Plu")
    private String Plu;
    /*
     * the Flag1 field the value.
     */
    @XmlElement(name = "Flag1")
    private String Flag1;
    /*
     * the Flag2 field the value.
     */
    @XmlElement(name = "Flag2")
    private String Flag2;

    /*
     * the Flag3 field the value.
     */
    @XmlElement(name = "Flag3")
    private String Flag3;
    /*

    /*
     * the Flag4 field the value.
     */
    @XmlElement(name = "Flag4")
    private String Flag4;
    /*

    /*
     * the Flag5 field the value.
     */
    @XmlElement(name = "Flag5")
    private String Flag5;
    /*

    /*
     * the Type1 field the value.
     */
    @XmlElement(name = "Type1")
    private String Type1;
    /*
     * the Type2 field the value.
     */
    @XmlElement(name = "Type2")
    private String Type2;
    /*
     * the InsDate field the value.
     */
    @XmlElement(name = "InsDate")
    private String InsDate;
    /*
     * the UpdDate field the value.
     */
    @XmlElement(name = "UpdDate")
    private String UpdDate;
    /*
     * the UpdAppId field the value.
     */
    @XmlElement(name = "UpdAppId")
    private String UpdAppId;
    /*
     * the UpdOpeCode field the value.
     */
    @XmlElement(name = "UpdOpeCode")
    private String UpdOpeCode;

    /*
     *the Department name.
     */
    @XmlElement(name = "DepartmentName")
    private DepartmentName DepartmentName;

    /**
     * Getter for the guest no.
     *
     * @return String
     */
    @ApiModelProperty(value="客注番号", notes="客注番号")
    public String getGuestNO() {
        return GuestNO;
    }

    /**
     * Setter for the guest no.
     *
     * @param the
     *            guestNo field the value.
     */
    public void setGuestNO(String guestNO) {
        this.GuestNO = guestNO;
    }

    /**
     * Getter for the line.
     *
     * @return String
     */
    @ApiModelProperty(value="行番号", notes="行番号")
    public String getLine() {
        return Line;
    }

    /**
     * Setter for the line.
     *
     * @param the
     *            Line field the value.
     */
    public void setLine(String line) {
        this.Line = line;
    }

    /**
     * Getter for the retailStore id.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付店舗番号", notes="客注受付店舗番号")
    public String getRetailStoreId() {
        return RetailStoreId;
    }

    /**
     * Setter for the RetailStore Id.
     *
     * @param the
     *            RetailStoreId field the value.
     */
    public void setRetailStoreId(String retailStoreId) {
        this.RetailStoreId = retailStoreId;
    }

    /**
     * Getter for the business date.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付日", notes="客注受付日")
    public String getBusinessDate() {
        return BusinessDate;
    }

    /**
     * Setter for the Business Date.
     *
     * @param the
     *            BusinessDate field the value.
     */
    public void setBusinessDate(String businessDate) {
        this.BusinessDate = businessDate;
    }

    /**
     * Getter for the business date time.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付日", notes="客注受付日")
    public String getBusinessDateTime() {
        return BusinessDateTime;
    }

    /**
     * Setter for the Business Date Time.
     *
     * @param the
     *            BusinessDateTime field the value.
     */
    public void setBusinessDateTime(String businessDateTime) {
        this.BusinessDateTime = businessDateTime;
    }

    /**
     * Getter for the tranNo.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付時の取引番号", notes="客注受付時の取引番号")
    public String getTranNo() {
        return TranNo;
    }

    /**
     * Setter for the TranNo.
     *
     * @param the
     *            TranNo field the value.
     */
    public void setTranNo(String tranNo) {
        this.TranNo = tranNo;
    }

    /**
     * Getter for the terminal no.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付時の端末番号(POS)", notes="客注受付時の端末番号(POS)")
    public String getTerminalNo() {
        return TerminalNo;
    }

    /**
     * Setter for the Terminal No.
     *
     * @param the
     *            TerminalNo field the value.
     */
    public void setTerminalNo(String terminalNo) {
        this.TerminalNo = terminalNo;
    }

    /**
     * Getter for the opecode .
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付の販売員コード", notes="客注受付の販売員コード")
    public String getOpeCode() {
        return OpeCode;
    }

    /**
     * Setter for the OpeCode.
     *
     * @param the
     *            OpeCode field the value.
     */
    public void setOpeCode(String opeCode) {
        this.OpeCode = opeCode;
    }

    /**
     * Getter for the opeName.
     *
     * @return String
     */
    @ApiModelProperty(value="客注受付の販売員名", notes="客注受付の販売員名")
    public String getOpeName() {
        return OpeName;
    }

    /**
     * Setter for the OpeName.
     *
     * @param the
     *            OpeName field the value.
     */
    public void setOpeName(String opeName) {
        this.OpeName = opeName;
    }

    /**
     * Getter for the kaiin no.
     *
     * @return String
     */
    @ApiModelProperty(value="会員番号(客注入金時)", notes="会員番号(客注入金時)")
    public String getKaiinNo() {
        return KaiinNo;
    }

    /**
     * Setter for the KaiinNo.
     *
     * @param the
     *            KaiinNo field the value.
     */
    public void setKaiinNo(String kaiinNo) {
        this.KaiinNo = kaiinNo;
    }

    /**
     * Getter for the KaiinNoSeiSan.
     *
     * @return String
     */
    @ApiModelProperty(value="会員番号(客注精算時)", notes="会員番号(客注精算時)")
    public String getKaiinNoSeiSan() {
        return KaiinNoSeiSan;
    }

    /**
     * Setter for the KaiinNoSeiSan.
     *
     * @param the
     *            KaiinNoSeiSan field the value.
     */
    public void setKaiinNoSeiSan(String kaiinNoSeiSan) {
        this.KaiinNoSeiSan = kaiinNoSeiSan;
    }

    /**
     * Getter for the OrderKbn.
     *
     * @return String
     */
    @ApiModelProperty(value="客注区分", notes="客注区分")
    public String getOrderKbn() {
        return OrderKbn;
    }

    /**
     * Setter for the OrderKbn.
     *
     * @param the
     *            OrderKbn field the value.
     */
    public void setOrderKbn(String orderKbn) {
        this.OrderKbn = orderKbn;
    }

    /**
     * Getter for the MdInternal.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード", notes="商品コード")
    public String getMdInternal() {
        return MdInternal;
    }

    /**
     * Setter for the MdInternal.
     *
     * @param the
     *            MdInternal field the value.
     */
    public void setMdInternal(String mdInternal) {
        this.MdInternal = mdInternal;
    }

    /**
     * Getter for the Md1.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(1)   類番(3桁)", notes="商品コード(1)   類番(3桁)")
    public String getMd1() {
        return Md1;
    }

    /**
     * Setter for the Md1.
     *
     * @param the
     *            Md1 field the value.
     */
    public void setMd1(String md1) {
        this.Md1 = md1;
    }

    /**
     * Getter for the Md2.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(2)   型番(2桁)", notes="商品コード(2)   型番(2桁)")
    public String getMd2() {
        return Md2;
    }

    /**
     * Setter for the Md2.
     *
     * @param the
     *            Md2 field the value.
     */
    public void setMd2(String md2) {
        this.Md2 = md2;
    }

    /**
     * Getter for the Md3.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(3)   生地番(3桁)", notes="商品コード(3)   生地番(3桁)")
    public String getMd3() {
        return Md3;
    }

    /**
     * Setter for the Md3.
     *
     * @param the
     *            Md3 field the value.
     */
    public void setMd3(String md3) {
        this.Md3 = md3;
    }

    /**
     * Getter for the Md4.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(4)   色番(2桁)", notes="商品コード(4)   色番(2桁)")
    public String getMd4() {
        return Md4;
    }

    /**
     * Setter for the Md4.
     *
     * @param the
     *            Md4 field the value.
     */
    public void setMd4(String md4) {
        this.Md4 = md4;
    }

    /**
     * Getter for the Md5.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(5)   体型(2桁)", notes="商品コード(5)   体型(2桁)")
    public String getMd5() {
        return Md5;
    }

    /**
     * Setter for the Md5.
     *
     * @param the
     *            Md5 field the value.
     */
    public void setMd5(String md5) {
        this.Md5 = md5;
    }

    /**
     * Getter for the Md6.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(6)   サイズ(4桁)", notes="商品コード(6)   サイズ(4桁)")
    public String getMd6() {
        return Md6;
    }

    /**
     * Setter for the Md6.
     *
     * @param the
     *            Md6 field the value.
     */

    public void setMd6(String md6) {
        this.Md6 = md6;
    }

    /**
     * Getter for the Md7.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(7)   ■未使用■", notes="商品コード(7)   ■未使用■")
    public String getMd7() {
        return Md7;
    }

    /**
     * Setter for the Md7.
     *
     * @param the
     *            Md7 field the value.
     */
    public void setMd7(String md7) {
        this.Md7 = md7;
    }

    /**
     * Getter for the Md8.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(8)   ■未使用■", notes="商品コード(8)   ■未使用■")
    public String getMd8() {
        return Md8;
    }

    /**
     * Setter for the Md8.
     *
     * @param the
     *            Md8 field the value.
     */
    public void setMd8(String md8) {
        this.Md8 = md8;
    }

    /**
     * Getter for the Md9.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(9)   ■未使用■", notes="商品コード(9)   ■未使用■")
    public String getMd9() {
        return Md9;
    }

    /**
     * Setter for the Md9.
     *
     * @param the
     *            Md9 field the value.
     */
    public void setMd9(String md9) {
        this.Md9 = md9;
    }

    /**
     * Getter for the Md10.
     *
     * @return String
     */
    @ApiModelProperty(value="商品コード(10)   ■未使用■", notes="商品コード(10)   ■未使用■")
    public String getMd10() {
        return Md10;
    }

    /**
     * Setter for the Md10.
     *
     * @param the
     *            Md10 field the value.
     */
    public void setMd10(String md10) {
        this.Md10 = md10;
    }

    /**
     * Getter for the MdName.
     *
     * @return String
     */
    @ApiModelProperty(value="商品名", notes="商品名")
    public String getMdName() {
        return MdName;
    }

    /**
     * Setter for the MdName.
     *
     * @param the
     *            MdName field the value.
     */
    public void setMdName(String mdName) {
        this.MdName = mdName;
    }

    /**
     * Getter for the SalesCnt.
     *
     * @return String
     */
    @ApiModelProperty(value="客注数量", notes="客注数量")
    public String getSalesCnt() {
        return SalesCnt;
    }

    /**
     * Setter for the MdName.
     *
     * @param the
     *            MdName field the value.
     */
    public void setSalesCnt(String salesCnt) {
        this.SalesCnt = salesCnt;
    }

    /**
     * Getter for the SalesPrice.
     *
     * @return String
     */
    @ApiModelProperty(value="客注時の上代単価", notes="客注時の上代単価")
    public String getSalesPrice() {
        return SalesPrice;
    }

    /**
     * Setter for the MdName.
     *
     * @param the
     *            MdName field the value.
     */
    public void setSalesPrice(String salesPrice) {
        this.SalesPrice = salesPrice;
    }

    /**
     * Getter for the SalesAmt.
     *
     * @return String
     */
    @ApiModelProperty(value="客注時の上代金額", notes="客注時の上代金額")
    public String getSalesAmt() {
        return SalesAmt;
    }

    /**
     * Setter for the MdName.
     *
     * @param the
     *            MdName field the value.
     */
    public void setSalesAmt(String salesAmt) {
        this.SalesAmt = salesAmt;
    }

    /**
     * Getter for the DepositAmt.
     *
     * @return String
     */
    @ApiModelProperty(value="客注金額（預かり額）", notes="客注金額（預かり額）")
    public String getDepositAmt() {
        return DepositAmt;
    }

    /**
     * Setter for the MdName.
     *
     * @param the
     *            MdName field the value.
     */
    public void setDepositAmt(String depositAmt) {
        this.DepositAmt = depositAmt;
    }

    /**
     * Getter for the TelKakuninFlag.
     *
     * @return String
     */
    @ApiModelProperty(value="TEL確認フラグ", notes="TEL確認フラグ")
    public String getTelKakuninFlag() {
        return TelKakuninFlag;
    }

    /**
     * Setter for the TelKakuninFlag.
     *
     * @param the
     *            TelKakuninFlag field the value.
     */
    public void setTelKakuninFlag(String telKakuninFlag) {
        this.TelKakuninFlag = telKakuninFlag;
    }

    /**
     * Getter for the SeisanDate.
     *
     * @return String
     */
    @ApiModelProperty(value="客注精算/返金日(営業日付) ", notes="客注精算/返金日(営業日付) ")
    public String getSeisanDate() {
        return SeisanDate;
    }

    /**
     * Setter for the SeisanDate.
     *
     * @param the
     *            SeisanDate field the value.
     */
    public void setSeisanDate(String seisanDate) {
        this.SeisanDate = seisanDate;
    }

    /**
     * Getter for the SeisanDateTime.
     *
     * @return String
     */
    @ApiModelProperty(value="客注精算/返金日(System日付)", notes="客注精算/返金日(System日付)")
    public String getSeisanDateTime() {
        return SeisanDateTime;
    }

    /**
     * Setter for the SeisanDateTime.
     *
     * @param the
     *            SeisanDateTime field the value.
     */
    public void setSeisanDateTime(String seisanDateTime) {
        this.SeisanDateTime = seisanDateTime;
    }

    /**
     * Getter for the CommitSalesAmt.
     *
     * @return String
     */
    @ApiModelProperty(value="精算時の売価金額", notes="精算時の売価金額")
    public String getCommitSalesAmt() {
        return CommitSalesAmt;
    }

    /**
     * Setter for the CommitSalesAmt.
     *
     * @param the
     *            CommitSalesAmt field the value.
     */
    public void setCommitSalesAmt(String commitSalesAmt) {
        this.CommitSalesAmt = commitSalesAmt;
    }

    /**
     * Getter for the RepayAmt.
     *
     * @return String
     */
    @ApiModelProperty(value="客注返金額", notes="客注返金額")
    public String getRepayAmt() {
        return RepayAmt;
    }

    /**
     * Setter for the RepayAmt.
     *
     * @param the
     *            RepayAmt field the value.
     */
    public void setRepayAmt(String repayAmt) {
        this.RepayAmt = repayAmt;
    }

    /**
     * Getter for the BalanceAmt.
     *
     * @return String
     */
    @ApiModelProperty(value="客注残額(客注精算後残額)", notes="客注残額(客注精算後残額)")
    public String getBalanceAmt() {
        return BalanceAmt;
    }

    /**
     * Setter for the BalanceAmt.
     *
     * @param the
     *            BalanceAmt field the value.
     */
    public void setBalanceAmt(String balanceAmt) {
        this.BalanceAmt = balanceAmt;
    }

    /**
     * Getter for the StatusCode.
     *
     * @return String
     */
    @ApiModelProperty(value="精算状態", notes="精算状態")
    public String getStatusCode() {
        return StatusCode;
    }

    /**
     * Setter for the statusCode.
     *
     * @param the
     *            statusCode field the value.
     */
    public void setStatusCode(String statusCode) {
        this.StatusCode = statusCode;
    }

    /**
     * Getter for the POSFlag.
     *
     * @return String
     */
    @ApiModelProperty(value="POS使用中フラグ", notes="POS使用中フラグ")
    public String getPOSFlag() {
        return POSFlag;
    }

    /**
     * Setter for the pOSFlag.
     *
     * @param the
     *            pOSFlag field the value.
     */
    public void setPOSFlag(String pOSFlag) {
        this.POSFlag = pOSFlag;
    }

    /**
     * Getter for the PluFlag.
     *
     * @return String
     */
    @ApiModelProperty(value="PLU未登録Flag", notes="PLU未登録Flag")
    public String getPluFlag() {
        return PluFlag;
    }

    /**
     * Setter for the pluFlag.
     *
     * @param the
     *            pluFlag field the value.
     */
    public void setPluFlag(String pluFlag) {
        this.PluFlag = pluFlag;
    }

    /**
     * Getter for the Memo.
     *
     * @return String
     */
    @ApiModelProperty(value="簡易メモ", notes="簡易メモ")
    public String getMemo() {
        return Memo;
    }

    /**
     * Setter for the Memo.
     *
     * @param the
     *            Memo field the value.
     */
    public void setMemo(String memo) {
        this.Memo = memo;
    }

    /**
     * Getter for the MerchandiseHierarchy1.
     *
     * @return String
     */
    @ApiModelProperty(value="もらった商品（1）", notes="もらった商品（1）")
    public String getMerchandiseHierarchy1() {
        return MerchandiseHierarchy1;
    }

    /**
     * Setter for the MerchandiseHierarchy1.
     *
     * @param the
     *            MerchandiseHierarchy1 field the value.
     */
    public void setMerchandiseHierarchy1(String merchandiseHierarchy1) {
        this.MerchandiseHierarchy1 = merchandiseHierarchy1;
    }

    /**
     * Getter for the MerchandiseHierarchy2.
     *
     * @return String
     */
    @ApiModelProperty(value="もらった商品（2）", notes="もらった商品（2）")
    public String getMerchandiseHierarchy2() {
        return MerchandiseHierarchy2;
    }

    /**
     * Setter for the MerchandiseHierarchy2.
     *
     * @param the
     *            MerchandiseHierarchy2 field the value.
     */
    public void setMerchandiseHierarchy2(String merchandiseHierarchy2) {
        this.MerchandiseHierarchy2 = merchandiseHierarchy2;
    }

    /**
     * Getter for the MerchandiseHierarchy3.
     *
     * @return String
     */
    @ApiModelProperty(value="もらった商品（3）", notes="もらった商品（3）")
    public String getMerchandiseHierarchy3() {
        return MerchandiseHierarchy3;
    }

    /**
     * Setter for the MerchandiseHierarchy3.
     *
     * @param the
     *            MerchandiseHierarchy3 field the value.
     */
    public void setMerchandiseHierarchy3(String merchandiseHierarchy3) {
        this.MerchandiseHierarchy3 = merchandiseHierarchy3;
    }

    /**
     * Getter for the MerchandiseHierarchy4.
     *
     * @return String
     */
    @ApiModelProperty(value="もらった商品（4）", notes="もらった商品（4）")
    public String getMerchandiseHierarchy4() {
        return MerchandiseHierarchy4;
    }

    /**
     * Setter for the MerchandiseHierarchy4.
     *
     * @param the
     *            MerchandiseHierarchy4 field the value.
     */
    public void setMerchandiseHierarchy4(String merchandiseHierarchy4) {
        this.MerchandiseHierarchy4 = merchandiseHierarchy4;
    }

    /**
     * Getter for the MerchandiseHierarchy5.
     *
     * @return String
     */
    @ApiModelProperty(value="もらった商品（5）", notes="もらった商品（5）")
    public String getMerchandiseHierarchy5() {
        return MerchandiseHierarchy5;
    }

    /**
     * Setter for the MerchandiseHierarchy5.
     *
     * @param the
     *            MerchandiseHierarchy5 field the value.
     */
    public void setMerchandiseHierarchy5(String merchandiseHierarchy5) {
        this.MerchandiseHierarchy5 = merchandiseHierarchy5;
    }

    /**
     * Getter for the MdName.
     *
     * @return String
     */
    @ApiModelProperty(value="課税区分", notes="課税区分")
    public String getTaxType() {
        return TaxType;
    }

    /**
     * Setter for the TaxType.
     *
     * @param the
     *            TaxType field the value.
     */
    public void setTaxType(String taxType) {
        this.TaxType = taxType;
    }

    /**
     * Getter for the MdName.
     *
     * @return String
     */
    @ApiModelProperty(value="売上外商品Flag", notes="売上外商品Flag")
    public String getPosMdType() {
        return PosMdType;
    }

    /**
     * Setter for the PosMdType.
     *
     * @param the
     *            PosMdType field the value.
     */
    public void setPosMdType(String posMdType) {
        this.PosMdType = posMdType;
    }

    /**
     * Getter for the MdName.
     *
     * @return String
     */
    @ApiModelProperty(value="在庫更新区分", notes="在庫更新区分")
    public String getTagType() {
        return TagType;
    }

    /**
     * Setter for the TagType.
     *
     * @param the
     *            TagType field the value.
     */
    public void setTagType(String tagType) {
        this.TagType = tagType;
    }

    /**
     * Getter for the KeyPlu.
     *
     * @return String
     */
    @ApiModelProperty(value="商品Id", notes="商品Id")
    public String getKeyPlu() {
        return KeyPlu;
    }

    /**
     * Setter for the KeyPlu.
     *
     * @param the
     *            keyPlu field the value.
     */
    public void setKeyPlu(String keyPlu) {
        this.KeyPlu = keyPlu;
    }

    /**
     * Getter for the MdType.
     *
     * @return String
     */
    @ApiModelProperty(value="ポイント計算用P/S区分", notes="ポイント計算用P/S区分")
    public String getMdType() {
        return MdType;
    }

    /**
     * Setter for the mdType.
     *
     * @param the
     *            mdType field the value.
     */
    public void setMdType(String mdType) {
        this.MdType = mdType;
    }

    /**
     * Getter for the Plu.
     *
     * @return String
     */
    @ApiModelProperty(value="JANコード", notes="JANコード")
    public String getPlu() {
        return Plu;
    }

    /**
     * Setter for the plu.
     *
     * @param the
     *            plu field the value.
     */
    public void setPlu(String plu) {
        this.Plu = plu;
    }

    /**
     * Getter for the Flag1.
     *
     * @return String
     */
    @ApiModelProperty(value="フラグ（1）", notes="フラグ（1）")
    public String getFlag1() {
        return Flag1;
    }

    /**
     * Setter for the Flag1.
     *
     * @param the
     *            Flag1 field the value.
     */
    public void setFlag1(String flag1) {
        this.Flag1 = flag1;
    }

    /**
     * Getter for the Flag2.
     *
     * @return String
     */
    @ApiModelProperty(value="フラグ（2）", notes="フラグ（2）")
    public String getFlag2() {
        return Flag2;
    }

    /**
     * Setter for the Flag2.
     *
     * @param the
     *            Flag2 field the value.
     */
    public void setFlag2(String flag2) {
        this.Flag2 = flag2;
    }



    /**
	 * @return the flag3
	 */
    @ApiModelProperty(value="フラグ（3）", notes="フラグ（3）")
	public String getFlag3() {
		return Flag3;
	}

	/**
	 * @param flag3 the flag3 to set
	 */
	public void setFlag3(String flag3) {
		Flag3 = flag3;
	}

	/**
	 * @return the flag4
	 */
    @ApiModelProperty(value="フラグ（4）", notes="フラグ（4）")
	public String getFlag4() {
		return Flag4;
	}

	/**
	 * @param flag4 the flag4 to set
	 */
	public void setFlag4(String flag4) {
		Flag4 = flag4;
	}

	/**
	 * @return the flag5
	 */
    @ApiModelProperty(value="フラグ（5）", notes="フラグ（5）")
	public String getFlag5() {
		return Flag5;
	}

	/**
	 * @param flag5 the flag5 to set
	 */
	public void setFlag5(String flag5) {
		Flag5 = flag5;
	}

	/**
     * Getter for the Type1.
     *
     * @return String
     */
    @ApiModelProperty(value="タイプ（1）", notes="タイプ（1）")
    public String getType1() {
        return Type1;
    }

    /**
     * Setter for the Type1.
     *
     * @param the
     *            Type1 field the value.
     */
    public void setType1(String type1) {
        this.Type1 = type1;
    }

    /**
     * Getter for the Type2.
     *
     * @return String
     */
    @ApiModelProperty(value="タイプ（2）", notes="タイプ（2）")
    public String getType2() {
        return Type2;
    }

    /**
     * Setter for the Type2.
     *
     * @param the
     *            Type2 field the value.
     */
    public void setType2(String type2) {
        this.Type2 = type2;
    }

    /**
     * Getter for the InsDate.
     *
     * @return String
     */
    @ApiModelProperty(value="挿入日付", notes="挿入日付")
    public String getInsDate() {
        return InsDate;
    }

    /**
     * Setter for the InsDate.
     *
     * @param the
     *            InsDate field the value.
     */
    public void setInsDate(String insDate) {
        this.InsDate = insDate;
    }

    /**
     * Getter for the UpdDate.
     *
     * @return String
     */
    @ApiModelProperty(value="更新日付", notes="更新日付")
    public String getUpdDate() {
        return UpdDate;
    }

    /**
     * Setter for the UpdDate.
     *
     * @param the
     *            UpdDate field the value.
     */
    public void setUpdDate(String updDate) {
        this.UpdDate = updDate;
    }

    /**
     * Getter for the UpdAppId.
     *
     * @return String
     */
    @ApiModelProperty(value="更新アプリケーションID", notes="更新アプリケーションID")
    public String getUpdAppId() {
        return UpdAppId;
    }

    /**
     * Setter for the UpdAppId.
     *
     * @param the
     *            UpdAppId field the value.
     */
    public void setUpdAppId(String updAppId) {
        this.UpdAppId = updAppId;
    }

    /**
     * Getter for the UpdOpeCode.
     *
     * @return String
     */
    @ApiModelProperty(value="更新担当者コード", notes="更新担当者コード")
    public String getUpdOpeCode() {
        return UpdOpeCode;
    }

    /**
     * Setter for the UpdOpeCode.
     *
     * @param the
     *            UpdOpeCode field the value.
     */
    public void setUpdOpeCode(String updOpeCode) {
        this.UpdOpeCode = updOpeCode;
    }

	/**
	 * @return the departmentName
	 */
    @ApiModelProperty(value="部署名", notes="部署名")
	public DepartmentName getDepartmentName() {
		return DepartmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(DepartmentName departmentName) {
		DepartmentName = departmentName;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";

		if(null != this.GuestNO){
			sb.append("GuestNo :").append(this.GuestNO.toString());
		}

		if(null != this.Line){
			sb.append(crlf).append("Line :").append(this.Line.toString());
		}

		if(null != this.RetailStoreId){
			sb.append(crlf).append("RetailStoreId :").append(this.RetailStoreId.toString());
		}

		if(null != this.BusinessDate){
			sb.append(crlf).append("BusinessDate :").append(this.BusinessDate.toString());
		}

		if(null != this.BusinessDateTime){
			sb.append(crlf).append("BusinessDateTime :").append(this.BusinessDateTime.toString());
		}
		if(null != this.TranNo){
			sb.append(crlf).append("TranNo :").append(this.TranNo.toString());
		}

		if(null != this.TerminalNo){
			sb.append(crlf).append("TerminalNo :").append(this.TerminalNo.toString());
		}

		if(null != this.OpeCode){
			sb.append(crlf).append("OpeCode :").append(this.OpeCode.toString());
		}

		if(null != this.OpeName){
			sb.append(crlf).append("OpeName :").append(this.OpeName.toString());
		}

		if(null != this.KaiinNo){
			sb.append(crlf).append("KaiinNo :").append(this.KaiinNo.toString());
		}

		if(null != this.KaiinNoSeiSan){
			sb.append(crlf).append("KaiinNoSeiSan :").append(this.KaiinNoSeiSan.toString());
		}

		if(null != this.OrderKbn){
			sb.append(crlf).append("OrderKbn :").append(this.OrderKbn.toString());
		}

		if(null != this.MdInternal){
			sb.append(crlf).append("MdInternal :").append(this.MdInternal.toString());
		}

		if(null != this.Md1){
			sb.append(crlf).append("Md1 :").append(this.Md1.toString());
		}

		if(null != this.Md2){
			sb.append(crlf).append("Md2 :").append(this.Md2.toString());
		}

		if(null != this.Md3){
			sb.append(crlf).append("Md3 :").append(this.Md3.toString());
		}

		if(null != this.Md4){
			sb.append(crlf).append("Md4 :").append(this.Md4.toString());
		}

		if(null != this.Md5){
			sb.append(crlf).append("Md5 :").append(this.Md5.toString());
		}

		if(null != this.Md6){
			sb.append(crlf).append("Md6 :").append(this.Md6.toString());
		}

		if(null != this.Md7){
			sb.append(crlf).append("Md7 :").append(this.Md7.toString());
		}

		if(null != this.Md8){
			sb.append(crlf).append("Md8 :").append(this.Md8.toString());
		}

		if(null != this.Md9){
			sb.append(crlf).append("Md9 :").append(this.Md9.toString());
		}

		if(null != this.Md10){
			sb.append(crlf).append("Md10 :").append(this.Md10.toString());
		}

		if(null != this.MdName){
			sb.append(crlf).append("MdName :").append(this.MdName.toString());
		}

		if(null != this.SalesCnt){
			sb.append(crlf).append("SalesCnt :").append(this.SalesCnt.toString());
		}

		if(null != this.SalesPrice){
			sb.append(crlf).append("SalesPrice :").append(this.SalesPrice.toString());
		}

		if(null != this.SalesAmt){
			sb.append(crlf).append("SalesAmt :").append(this.SalesAmt.toString());
		}

		if(null != this.DepositAmt){
			sb.append(crlf).append("DepositAmt :").append(this.DepositAmt.toString());
		}

		if(null != this.TelKakuninFlag){
			sb.append(crlf).append("TelKakuninFlag :").append(this.TelKakuninFlag.toString());
		}

		if(null != this.SeisanDate){
			sb.append(crlf).append("SeisanDate :").append(this.SeisanDate.toString());
		}

		if(null != this.SeisanDateTime){
			sb.append(crlf).append("SeisanDateTime :").append(this.SeisanDateTime.toString());
		}

		if(null != this.CommitSalesAmt){
			sb.append(crlf).append("CommitSalesAmt :").append(this.CommitSalesAmt.toString());
		}

		if(null != this.RepayAmt){
			sb.append(crlf).append("RepayAmt :").append(this.RepayAmt.toString());
		}

		if(null != this.BalanceAmt){
			sb.append(crlf).append("BalanceAmt :").append(this.BalanceAmt.toString());
		}

		if(null != this.StatusCode){
			sb.append(crlf).append("StatusCode :").append(this.StatusCode.toString());
		}

		if(null != this.POSFlag){
			sb.append(crlf).append("POSFlag :").append(this.POSFlag.toString());
		}

		if(null != this.PluFlag){
			sb.append(crlf).append("PluFlag :").append(this.PluFlag.toString());
		}

		if(null != this.Memo){
			sb.append(crlf).append("Memo :").append(this.Memo.toString());
		}

		if(null != this.MerchandiseHierarchy1){
			sb.append(crlf).append("MerchandiseHierarchy1 :").append(this.MerchandiseHierarchy1.toString());
		}
		if(null != this.MerchandiseHierarchy2){
			sb.append(crlf).append("MerchandiseHierarchy2 :").append(this.MerchandiseHierarchy2.toString());
		}
		if(null != this.MerchandiseHierarchy3){
			sb.append(crlf).append("MerchandiseHierarchy3 :").append(this.MerchandiseHierarchy3.toString());
		}
		if(null != this.MerchandiseHierarchy4){
			sb.append(crlf).append("MerchandiseHierarchy4 :").append(this.MerchandiseHierarchy4.toString());
		}

		if(null != this.MerchandiseHierarchy5){
			sb.append(crlf).append("MerchandiseHierarchy5 :").append(this.MerchandiseHierarchy5.toString());
		}

		if(null != this.TaxType){
			sb.append(crlf).append("TaxType :").append(this.TaxType.toString());
		}

		if(null != this.PosMdType){
			sb.append(crlf).append("PosMdType :").append(this.PosMdType.toString());
		}

		if(null != this.TagType){
			sb.append(crlf).append("TagType :").append(this.TagType.toString());
		}

		if(null != this.KeyPlu){
			sb.append(crlf).append("KeyPlu :").append(this.KeyPlu.toString());
		}

		if(null != this.MdType){
			sb.append(crlf).append("MdType :").append(this.MdType.toString());
		}

		if(null != this.Plu){
			sb.append(crlf).append("Plu :").append(this.Plu.toString());
		}

		if(null != this.Flag1){
			sb.append(crlf).append("Flag1 :").append(this.Flag1.toString());
		}

		if(null != this.Flag2){
			sb.append(crlf).append("Flag2 :").append(this.Flag2.toString());
		}

		if(null != this.Flag3){
			sb.append(crlf).append("Flag3 :").append(this.Flag3.toString());
		}

		if(null != this.Flag4){
			sb.append(crlf).append("Flag4 :").append(this.Flag4.toString());
		}

		if(null != this.Flag5){
			sb.append(crlf).append("Flag5 :").append(this.Flag5.toString());
		}

		if(null != this.Type1){
			sb.append(crlf).append("Type1 :").append(this.Type1.toString());
		}

		if(null != this.Type2){
			sb.append(crlf).append("Type2 :").append(this.Type2.toString());
		}

		if(null != this.InsDate){
			sb.append(crlf).append("InsDate :").append(this.InsDate.toString());
		}

		if(null != this.UpdDate){
			sb.append(crlf).append("UpdDate :").append(this.UpdDate.toString());
		}

		if(null != this.UpdAppId){
			sb.append(crlf).append("UpdAppId :").append(this.UpdAppId.toString());
		}

		if(null != this.UpdOpeCode){
			sb.append(crlf).append("UpdOpeCode :").append(this.UpdOpeCode.toString());
		}

		if(null != this.DepartmentName){
			sb.append(crlf).append("DepartmentName :").append(this.DepartmentName.toString());
		}
		return sb.toString();
	}

}
