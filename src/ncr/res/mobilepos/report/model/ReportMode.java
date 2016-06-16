package ncr.res.mobilepos.report.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01            2014.10.21    MAJINHUI        レポート出力を対応
 * 1.02            2014.12.22    FENGSHA         売上表を対応
 * 1.03            2014.12.25    MAJINHUI        会計レポート出力を対応
 * 1.04            2015.1.21     MAJINHUI        点検・精算レポート出力を対応
 * 1.05            2015.2.13     MAJINHUI        レポート出力を対応
 */
@ApiModel(value="ReportMode")
public class ReportMode {
	
	private String type;
	private int TrainingFlag;
    //実在高
	private double CalculateTotalAmt;
	//計算在高
	private double RealTotalAmt;
	//差額
	private double GapAmt;
	
	@ApiModelProperty(value="実在高", notes="実在高")
	public double getCalculateTotalAmt() {
		return CalculateTotalAmt;
	}

	public void setCalculateTotalAmt(double calculateTotalAmt) {
		CalculateTotalAmt = calculateTotalAmt;
	}

	@ApiModelProperty(value="計算在高", notes="計算在高")
	public double getRealTotalAmt() {
		return RealTotalAmt;
	}

	public void setRealTotalAmt(double realTotalAmt) {
		RealTotalAmt = realTotalAmt;
	}

	@ApiModelProperty(value="差額", notes="差額")
	public double getGapAmt() {
		return GapAmt;
	}

	public void setGapAmt(double gapAmt) {
		GapAmt = gapAmt;
	}

	@ApiModelProperty(value="トレーニングフラグ", notes="トレーニングフラグ")
    public int getTrainingFlag() {
		return TrainingFlag;
	}

	public void setTrainingFlag(int trainingFlag) {
		TrainingFlag = trainingFlag;
	}
	
	@ApiModelProperty(value="タイプ", notes="タイプ")
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/* 1.02 2014.12.03 FENGSHA 売上表を対応 START*/
    //販 売 員 number
    private String salesManNo;
    //販 売 員 name
    private String salesManName;
    //担 当 者 numbe
    private String operaterNo;
    // report type
    private String reportType;
    //the division
    private String div;
   //the division name
    private String divName;
    //the search store id
    private String storeidSearch;
   //客数の合計
    private long AllGuestCount;
  //点数の合計
    private long AllItemCount;
  //金額の合計
    private double AllSalesAmount;
    /* 1.02 2014.12.03 FENGSHA 売上表を対応 END*/

    //1.04 2015.1.21 MAJINHUI 点検・精算レポート出力を対応　add start
    //cash in draw Amount
    private double AllDrawAmount;
    //print id
    private String tillId;
    //1.04 2015.1.21 MAJINHUI 点検・精算レポート出力を対応　add end

    private List<ItemMode> rptlist = new ArrayList<ItemMode>();
    // the store name
    private String storeName;
    // businessDayDate
    private String businessDayDate;
    // begindatetime
    private String begindatetime;
    // companyID
    private String companyID;
    // storeID
    private String storeID;
    // language
    private String language;

    // subdateid2
    private String subdateid2;
    // subdateid1
    private String subdateid1;
    // workStationID
    private String workStationID;
    // sequenceNo
    private String sequenceNo;

    // 1.03 2014.12.25 MAJINHUI 会計レポート出力を対応　ADD START
    //TEL
    private String telephone;

    // < 売 上 >
    // 売 上
    private String sales;
    // 売 上点数
    private long salesPoints;
    // 売 上金額
    private double salesAmt;
    // 売 上 値 引
    private String salesDiscount;
    // 売 上 値 引点数
    private long salesDiscountPoints;
    // 売 上 値 引金額
    private double salesDiscountAmt;
    // 取 消
    private String cancel;
    // 取 消点数
    private long cancelPoints;
    // 取 消金額
    private double cancelAmt;
    // 小 計
    private String salesSubTotal;
    // 小 計点数
    private long salesTotalPoints;
    // 小 計金額
    private double salesTotalAmt;

    // 返 品 売 上
    private String returnSales;
    // 返 品 売 上点数
    private long returnSalesPoints;
    // 返 品 売 上金額
    private double returnSalesAmt;
    // 返 品 値 引
    private String returnDiscount;
    // 返 品 値 引点数
    private long returnDiscountPoints;
    // 返 品 値 引金額
    private double returnDiscountAmt;
    // 返 品 取 消
    private String returnCancel;
    // 返 品 取 消点数
    private long returnCancelPoint;
    // 返 品 取 消金額
    private double returnCancelAmt;
    // 小 計
    private String returnSubTotal;
    // 小 計点数
    private long returnTotalPoints;
    // 小 計金額
    private double returnTotalAmt;

    // 純 売 上
    private String netSales;
    // 純 売 上点数
    private long netSalesPoints;
    // 純 売 上金額
    private double netSalesAmt;
    // 純 売 上 (税 抜 )
    private String taxNetSales;
    // 純 売 上 (税 抜 )金額
    private double taxNetSalesAmt;
    // 総 売 上
    private String totalSales;
    // 総 売 上点数
    private long totalSalesPoints;
    // 総 売 上金額
    private double totalSalesAmt;
    // 総 売 上 (税 抜 )
    private String taxTotalSales;
    // 総 売 上 (税 抜 )金額
    private double taxTotalSalesAmt;

    // 客 数
    private String customers;
    // 客 数点数
    private long customersNo;

    // < 税 別 内 訳 >
    // 課 税 (税 抜 )
    private String taxation;
    // 課 税 (税 抜 )点数
    private long taxationPoints;
    // 課 税 (税 抜 )金額
    private double taxationAmt;
    // 税
    private String tax;
    // 税金額
    private double taxAmt;
    // 非 課 税
    private String taxExemption;
    // 非 課 税点数
    private long taxExemptionPoints;
    // 非 課 税金額
    private double taxExemptionAmt;
    // 計
    private String taxSubtotal;
    // 計点数
    private long taxSubtotalPoints;
    // 計金額
    private double taxSubtotalAmt;

    // < 値 引 内 訳 >
    // 明 細 割 引
    private String itemDiscounts;
    // 明 細 割 引点数
    private long itemDiscountsPoints;
    // 明 細 割 引金額
    private double itemDiscountsAmt;
    // 明 細 値 引
    private String itemNebiki;
    // 明 細 値 引点数
    private long itemNebikiPoints;
    // 明 細 値 引金額
    private double itemNebikiAmt;
    // イ ベ ン ト
    private String eventsName;
    // イ ベ ン ト点数
    private long eventsPoints;
    // イ ベ ン ト金額
    private double eventsAmt;
    // 社 員 販 売
    private String employeeSales;
    // 社 員 販 売点数
    private long employeeSalesPoints;
    // 社 員 販 売金額
    private double employeeSalesAmt;
    // 小 計 割 引
    private String subtotalDiscounts;
    // 小 計 割 引点数
    private long subtotalDiscountsPoints;
    // 小 計 割 引金額
    private double subtotalDiscountsAmt;
    // 小 計 値 引
    private String subtotalNebiki;
    // 小 計 値 引点数
    private long subtotalNebikiPoints;
    // 小 計 値 引金額
    private double subtotalNebikiAmt;
    // 計
    private String discountsSubtotal;
    // 計金額
    private double discountSubtotalAmt;

    // <ギフトカード販売>
    // 販 売
    private String sell;
    // 販 売点数
    private long sellPoints;
    // 販 売金額
    private double sellAmt;
    // 販 売 取 消
    private String sellCancel;
    // 販 売 取 消点数
    private long sellCancelPoints;
    // 販 売 取 消金額
    private double sellCancelAmt;
    // 計
    private String sellSubtotal;
    // 計点数
    private long sellSubtotalPoints;
    // 計金額
    private double sellSubtotalAmt;

    // <前 受 金>
    // 前 受 金　
    private String advances;
    // 前 受 金点数
    private long advancesPoints;
    // 前 受 金金額
    private double advancesAmt;
    // 前 受 金 取 消
    private String advancesCancel;
    // 前 受 金 取 消点数
    private long advancesCancelPoints;
    // 前 受 金 取 消金額
    private double advancesCancelAmt;

    // 計
    private String advancesSubtotal;
    // 計点数
    private long advancesSubtotalPoints;
    // 計金額
    private double advancesSubtotalAmt;
    //　現 金件数
    private long cashPoints;
    // ク レ ジ ッ ト件数
    private long creditPoints;
    // 銀 聯件数
    private long unionPayPoints;
    // ギ フ ト カ ー ド件数
    private long giftCardPoints;
    // 商 品 券件数
    private long giftCertificatesPoints;
    // 振 込 入 金件数
    private long transferPaymentPoints;
    //前受金売上件数
    private long AdvancesSalesPoints;
    // 金 種 別 内 訳点数計
    private long goldSpeciesPoints;
    
    // 買 物 券件数
    private long shoppingTicketPoints;
    // ギ フ ト 券件数
    private long giftVoucherPoints;
    // 伊 勢 丹件数
    private long isetanPoints;
    // DC件数
    private long DCLablePoints;
    // AMEX件数
    private long AMEXLablePoints;
    // JCB件数
    private long JCBLablePoints;
    // Diners件数
    private long dinersLablePoints;
    // Iカード件数
    private long ICardPoints;
    // 三 井件数
    private long mitsuiLablePoints;
    // 軽 井 沢件数
    private long karuizawaPoints;
    // Chel 1000件数
    private long chel1000Points;
    //Chel 2000件数
    private long chel2000Points;
    //その他件数
    private long sonotaPoints;
    // 計件数
    private long giftCertificatesSubtotalPoints;
    // 回 収 金件数
    private long collectedfundsPoints;
    // 払戻金件数
    private long modorikinnPoints;
    
    // <金 種 別 内 訳>
    // 現 金
    private String cash;
    // 現 金金額
    private double cashAmt;
    // ク レ ジ ッ ト
    private String credit;
    // ク レ ジ ッ ト金額
    private double creditAmt;
    // 銀 聯
    private String unionPay;
    // 銀 聯金額
    private double unionPayAmt;
    // ギ フ ト カ ー ド
    private String giftCard;
    // ギ フ ト カ ー ド金額
    private double giftCardAmt;
    // 商 品 券
    private String giftCertificates;
    // 商 品 券金額
    private double giftCertificatesAmt;
    // 振 込 入 金
    private String transferPayment;
    // 振 込 入 金金額
    private double transferPaymentAmt;
    //1.05   2015.2.13   MAJINHUI   レポート出力を対応 add start
    //前受金売上
    private String AdvancesSales;
    //前受金売上金額
    private double AdvancesSalesAmt;
    //1.05   2015.2.13   MAJINHUI   レポート出力を対応 add end
    
    // 計
    private String goldSpecies;
    // 計金額
    private double goldSpeciesSubtotal;

    // <商 品 券 内 訳>
    // 買 物 券
    private String shoppingTicket;
    // 買 物 券金額
    private double shoppingTicketAmt;
    // ギ フ ト 券
    private String giftVoucher;
    // ギ フ ト 券金額
    private double giftVoucherAmt;
    // 伊 勢 丹
    private String isetan;
    // 伊 勢 丹金額
    private double isetanAmt;
    // DC
    private String DCLable;
    // DC金額
    private double DCAmt;
    // AMEX
    private String AMEXLable;
    // AMEX金額
    private double AMEXAmt;
    // JCB
    private String JCBLable;
    // JCB金額
    private double JCBAmt;
    // Diners
    private String dinersLable;
    // Diners金額
    private double dinersAmt;
    // Iカード
    private String ICard;
    // Iカード
    private double ICardAmt;
    // 三 井金額
    private String mitsuiLable;
    // 三 井金額
    private double mitsuiAmt;
    // 軽 井 沢
    private String karuizawa;
    // 軽 井 沢金額
    private double karuizawaAmt;
    // Chel 1000
    private String chel1000;
    // Chel 1000 金額
    private double chel1000Amt;
    //Chel 2000
    private String chel2000;
    //Chel 2000金額
    private double chel2000Amt;
    private String sonota;
    private double sonotaAmt;
    // 計
    private String giftCertificatesSubtotal;
    // 計金額
    private double giftCertificatesSubtotalAmt;

    // 回 収 金
    private String collectedfunds;
    // 回 収 金金額
    private double collectedfundAmt;
    // 払戻金
    private String modorikinn;
    // 払戻金金額
    private double modorikinnAmt;
    
    // 中 止 回 数
    private String discontinuation;
    // 中 止 回 数点数
    private long discontinuationPoints;
    // 両 替 回 数
    private String exchange;
    // 両 替 回 数点数
    private long exchangePoints;

    // <収 入 印 紙>
    // 印 紙 200
    private String stamp200;
    // 印 紙 200金額
    private double stamp200Amt;
    // 印 紙 400
    private String stamp400;
    // 印 紙 400金額
    private double stamp400Amt;
    // 印 紙 600
    private String stamp600;
    // 印 紙 600金額
    private double stamp600Amt;
    // 印 紙 1,000
    private String stamp1000;
    // 印 紙 1,000金額
    private double stamp1000Amt;
    // 印 紙 2,000
    private String stamp2000;
    // 印 紙 2,000金額
    private double stamp2000Amt;
    // 印 紙 4,000
    private String stamp4000;
    // 印 紙 4,000金額
    private double stamp4000Amt;

    @ApiModelProperty(value="電話", notes="電話")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @ApiModelProperty(value="回 収 金件数", notes="回 収 金件数")
	public long getCollectedfundsPoints() {
		return collectedfundsPoints;
	}

	public void setCollectedfundsPoints(long collectedfundsPoints) {
		this.collectedfundsPoints = collectedfundsPoints;
	}

	@ApiModelProperty(value="払戻金件数", notes="払戻金件数")
	public long getModorikinnPoints() {
		return modorikinnPoints;
	}

	public void setModorikinnPoints(long modorikinnPoints) {
		this.modorikinnPoints = modorikinnPoints;
	}

	@ApiModelProperty(value="現 金件数", notes="現 金件数")
	public long getCashPoints() {
		return cashPoints;
	}

	public void setCashPoints(long cashPoints) {
		this.cashPoints = cashPoints;
	}

	@ApiModelProperty(value="ク レ ジ ッ ト件数", notes="ク レ ジ ッ ト件数")
	public long getCreditPoints() {
		return creditPoints;
	}

	public void setCreditPoints(long creditPoints) {
		this.creditPoints = creditPoints;
	}

	@ApiModelProperty(value="銀 聯件数", notes="銀 聯件数")
	public long getUnionPayPoints() {
		return unionPayPoints;
	}

	public void setUnionPayPoints(long unionPayPoints) {
		this.unionPayPoints = unionPayPoints;
	}

	@ApiModelProperty(value="ギ フ ト カ ー ド件数", notes="ギ フ ト カ ー ド件数")
	public long getGiftCardPoints() {
		return giftCardPoints;
	}

	public void setGiftCardPoints(long giftCardPoints) {
		this.giftCardPoints = giftCardPoints;
	}

	@ApiModelProperty(value="商 品 券件数", notes="商 品 券件数")
	public long getGiftCertificatesPoints() {
		return giftCertificatesPoints;
	}

	public void setGiftCertificatesPoints(long giftCertificatesPoints) {
		this.giftCertificatesPoints = giftCertificatesPoints;
	}

	@ApiModelProperty(value="振 込 入 金件数", notes="振 込 入 金件数")
	public long getTransferPaymentPoints() {
		return transferPaymentPoints;
	}

	public void setTransferPaymentPoints(long transferPaymentPoints) {
		this.transferPaymentPoints = transferPaymentPoints;
	}

	@ApiModelProperty(value="前受金売上件数", notes="前受金売上件数")
	public long getAdvancesSalesPoints() {
		return AdvancesSalesPoints;
	}

	public void setAdvancesSalesPoints(long advancesSalesPoints) {
		AdvancesSalesPoints = advancesSalesPoints;
	}

	@ApiModelProperty(value="金 種 別 内 訳点数計", notes="金 種 別 内 訳点数計")
	public long getGoldSpeciesPoints() {
		return goldSpeciesPoints;
	}

	public void setGoldSpeciesPoints(long goldSpeciesPoints) {
		this.goldSpeciesPoints = goldSpeciesPoints;
	}

	@ApiModelProperty(value="買 物 券件数", notes="買 物 券件数")
	public long getShoppingTicketPoints() {
		return shoppingTicketPoints;
	}

	public void setShoppingTicketPoints(long shoppingTicketPoints) {
		this.shoppingTicketPoints = shoppingTicketPoints;
	}

	@ApiModelProperty(value="ギ フ ト 券件数", notes="ギ フ ト 券件数")
	public long getGiftVoucherPoints() {
		return giftVoucherPoints;
	}

	public void setGiftVoucherPoints(long giftVoucherPoints) {
		this.giftVoucherPoints = giftVoucherPoints;
	}

	@ApiModelProperty(value="伊 勢 丹件数", notes="伊 勢 丹件数")
	public long getIsetanPoints() {
		return isetanPoints;
	}

	public void setIsetanPoints(long isetanPoints) {
		this.isetanPoints = isetanPoints;
	}

	@ApiModelProperty(value="DC件数", notes="DC件数")
	public long getDCLablePoints() {
		return DCLablePoints;
	}

	public void setDCLablePoints(long dCLablePoints) {
		DCLablePoints = dCLablePoints;
	}

	@ApiModelProperty(value="AMEX件数", notes="AMEX件数")
	public long getAMEXLablePoints() {
		return AMEXLablePoints;
	}

	public void setAMEXLablePoints(long aMEXLablePoints) {
		AMEXLablePoints = aMEXLablePoints;
	}

	@ApiModelProperty(value="JCB件数", notes="JCB件数")
	public long getJCBLablePoints() {
		return JCBLablePoints;
	}

	public void setJCBLablePoints(long jCBLablePoints) {
		JCBLablePoints = jCBLablePoints;
	}

	@ApiModelProperty(value="Diners件数", notes="Diners件数")
	public long getDinersLablePoints() {
		return dinersLablePoints;
	}

	public void setDinersLablePoints(long dinersLablePoints) {
		this.dinersLablePoints = dinersLablePoints;
	}

	@ApiModelProperty(value="Iカード件数", notes="Iカード件数")
	public long getICardPoints() {
		return ICardPoints;
	}

	public void setICardPoints(long iCardPoints) {
		ICardPoints = iCardPoints;
	}

	@ApiModelProperty(value="三 井件数", notes="三 井件数")
	public long getMitsuiLablePoints() {
		return mitsuiLablePoints;
	}

	public void setMitsuiLablePoints(long mitsuiLablePoints) {
		this.mitsuiLablePoints = mitsuiLablePoints;
	}

	@ApiModelProperty(value="軽 井 沢件数", notes="軽 井 沢件数")
	public long getKaruizawaPoints() {
		return karuizawaPoints;
	}

	public void setKaruizawaPoints(long karuizawaPoints) {
		this.karuizawaPoints = karuizawaPoints;
	}

	@ApiModelProperty(value="Chel 1000件数", notes="Chel 1000件数")
	public long getChel1000Points() {
		return chel1000Points;
	}

	public void setChel1000Points(long chel1000Points) {
		this.chel1000Points = chel1000Points;
	}

	@ApiModelProperty(value="Chel 2000件数", notes="Chel 2000件数")
	public long getChel2000Points() {
		return chel2000Points;
	}

	public void setChel2000Points(long chel2000Points) {
		this.chel2000Points = chel2000Points;
	}

	@ApiModelProperty(value="その他件数", notes="その他件数")
	public long getSonotaPoints() {
		return sonotaPoints;
	}

	public void setSonotaPoints(long sonotaPoints) {
		this.sonotaPoints = sonotaPoints;
	}

	@ApiModelProperty(value="計件数", notes="計件数")
	public long getGiftCertificatesSubtotalPoints() {
		return giftCertificatesSubtotalPoints;
	}

	public void setGiftCertificatesSubtotalPoints(
			long giftCertificatesSubtotalPoints) {
		this.giftCertificatesSubtotalPoints = giftCertificatesSubtotalPoints;
	}
    
    // 1.04 2015.1.21 MAJINHUI 点検・精算レポート出力を対応　ADD START
	@ApiModelProperty(value="すべて引く金額", notes="すべて引く金額")
    public double getAllDrawAmount() {
        return AllDrawAmount;
    }

    public void setAllDrawAmount(double allDrawAmount) {
        AllDrawAmount = allDrawAmount;
    }
    
    @ApiModelProperty(value="ドロワーコード", notes="ドロワーコード")
    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }
    // 1.04 2015.1.21 MAJINHUI 点検・精算レポート出力を対応　ADD END

    @ApiModelProperty(value="売 上", notes="売 上")
    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    @ApiModelProperty(value="売 上点数", notes="売 上点数")
    public long getSalesPoints() {
        return salesPoints;
    }

    public void setSalesPoints(long salesPoints) {
        this.salesPoints = salesPoints;
    }

    @ApiModelProperty(value="売 上金額", notes="売 上金額")
    public double getSalesAmt() {
        return salesAmt;
    }

    public void setSalesAmt(double salesAmt) {
        this.salesAmt = salesAmt;
    }

    @ApiModelProperty(value="売 上 値 引", notes="売 上 値 引")
    public String getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(String salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    @ApiModelProperty(value="売 上 値 引点数", notes="売 上 値 引点数")
    public long getSalesDiscountPoints() {
        return salesDiscountPoints;
    }

    public void setSalesDiscountPoints(long salesDiscountPoints) {
        this.salesDiscountPoints = salesDiscountPoints;
    }

    @ApiModelProperty(value="売 上 値 引金額", notes="売 上 値 引金額")
    public double getSalesDiscountAmt() {
        return salesDiscountAmt;
    }

    public void setSalesDiscountAmt(double salesDiscountAmt) {
        this.salesDiscountAmt = salesDiscountAmt;
    }

    @ApiModelProperty(value="取 消", notes="取 消")
    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    @ApiModelProperty(value="取 消点数", notes="取 消点数")
    public long getCancelPoints() {
        return cancelPoints;
    }

    public void setCancelPoints(long cancelPoints) {
        this.cancelPoints = cancelPoints;
    }

    @ApiModelProperty(value="取 消金額", notes="取 消金額")
    public double getCancelAmt() {
        return cancelAmt;
    }

    public void setCancelAmt(double cancelAmt) {
        this.cancelAmt = cancelAmt;
    }

    @ApiModelProperty(value="小 計", notes="小 計")
    public String getSalesSubTotal() {
        return salesSubTotal;
    }

    public void setSalesSubTotal(String salesSubTotal) {
        this.salesSubTotal = salesSubTotal;
    }

    @ApiModelProperty(value="小 計点数", notes="小 計点数")
    public long getSalesTotalPoints() {
        return salesTotalPoints;
    }

    public void setSalesTotalPoints(long salesTotalPoints) {
        this.salesTotalPoints = salesTotalPoints;
    }

    @ApiModelProperty(value="小 計金額", notes="小 計金額")
    public double getSalesTotalAmt() {
        return salesTotalAmt;
    }

    public void setSalesTotalAmt(double salesTotalAmt) {
        this.salesTotalAmt = salesTotalAmt;
    }
    //1.05   2015.2.13   MAJINHUI   レポート出力を対応 add start
    @ApiModelProperty(value="前受金売上", notes="前受金売上")
    public String getAdvancesSales() {
        return AdvancesSales;
    }

    public void setAdvancesSales(String advancesSales) {
        AdvancesSales = advancesSales;
    }

    @ApiModelProperty(value="前受金売上金額", notes="前受金売上金額")
    public double getAdvancesSalesAmt() {
        return AdvancesSalesAmt;
    }

    public void setAdvancesSalesAmt(double advancesSalesAmt) {
        AdvancesSalesAmt = advancesSalesAmt;
    }
    //1.05   2015.2.13   MAJINHUI   レポート出力を対応 add end
    @ApiModelProperty(value="返 品 売 上", notes="返 品 売 上")
    public String getReturnSales() {
        return returnSales;
    }

    public void setReturnSales(String returnSales) {
        this.returnSales = returnSales;
    }

    @ApiModelProperty(value="返 品 売 上点数", notes="返 品 売 上点数")
    public long getReturnSalesPoints() {
        return returnSalesPoints;
    }

    public void setReturnSalesPoints(long returnSalesPoints) {
        this.returnSalesPoints = returnSalesPoints;
    }

    @ApiModelProperty(value="返 品 売 上点数", notes="返 品 売 上点数")
    public double getReturnSalesAmt() {
        return returnSalesAmt;
    }

    public void setReturnSalesAmt(double returnSalesAmt) {
        this.returnSalesAmt = returnSalesAmt;
    }

    @ApiModelProperty(value="返 品 値 引", notes="返 品 値 引")
    public String getReturnDiscount() {
        return returnDiscount;
    }

    public void setReturnDiscount(String returnDiscount) {
        this.returnDiscount = returnDiscount;
    }

    @ApiModelProperty(value="返 品 値 引点数", notes="返 品 値 引点数")
    public long getReturnDiscountPoints() {
        return returnDiscountPoints;
    }

    public void setReturnDiscountPoints(long returnDiscountPoints) {
        this.returnDiscountPoints = returnDiscountPoints;
    }

    @ApiModelProperty(value="返 品 値 引金額", notes="返 品 値 引金額")
    public double getReturnDiscountAmt() {
        return returnDiscountAmt;
    }

    public void setReturnDiscountAmt(double returnDiscountAmt) {
        this.returnDiscountAmt = returnDiscountAmt;
    }

    @ApiModelProperty(value="返 品 取 消", notes="返 品 取 消")
    public String getReturnCancel() {
        return returnCancel;
    }

    public void setReturnCancel(String returnCancel) {
        this.returnCancel = returnCancel;
    }

    @ApiModelProperty(value="返 品 取 消点数", notes="返 品 取 消点数")
    public long getReturnCancelPoint() {
        return returnCancelPoint;
    }

    public void setReturnCancelPoint(long returnCancelPoint) {
        this.returnCancelPoint = returnCancelPoint;
    }

    @ApiModelProperty(value="返 品 取 消金額", notes="返 品 取 消金額")
    public double getReturnCancelAmt() {
        return returnCancelAmt;
    }

    public void setReturnCancelAmt(double returnCancelAmt) {
        this.returnCancelAmt = returnCancelAmt;
    }

    @ApiModelProperty(value="小 計", notes="小 計")
    public String getReturnSubTotal() {
        return returnSubTotal;
    }

    public void setReturnSubTotal(String returnSubTotal) {
        this.returnSubTotal = returnSubTotal;
    }

    @ApiModelProperty(value="小 計点数", notes="小 計点数")
    public long getReturnTotalPoints() {
        return returnTotalPoints;
    }

    public void setReturnTotalPoints(long returnTotalPoints) {
        this.returnTotalPoints = returnTotalPoints;
    }

    @ApiModelProperty(value="小 計金額", notes="小 計金額")
    public double getReturnTotalAmt() {
        return returnTotalAmt;
    }

    public void setReturnTotalAmt(double returnTotalAmt) {
        this.returnTotalAmt = returnTotalAmt;
    }

    @ApiModelProperty(value="純 売 上", notes="純 売 上")
    public String getNetSales() {
        return netSales;
    }

    public void setNetSales(String netSales) {
        this.netSales = netSales;
    }

    @ApiModelProperty(value="純 売 上点数", notes="純 売 上点数")
    public long getNetSalesPoints() {
        return netSalesPoints;
    }

    public void setNetSalesPoints(long netSalesPoints) {
        this.netSalesPoints = netSalesPoints;
    }

    @ApiModelProperty(value="純 売 上金額", notes="純 売 上金額")
    public double getNetSalesAmt() {
        return netSalesAmt;
    }

    public void setNetSalesAmt(double netSalesAmt) {
        this.netSalesAmt = netSalesAmt;
    }

    @ApiModelProperty(value="純 売 上 (税 抜 )", notes="純 売 上 (税 抜 )")
    public String getTaxNetSales() {
        return taxNetSales;
    }

    public void setTaxNetSales(String taxNetSales) {
        this.taxNetSales = taxNetSales;
    }

    @ApiModelProperty(value="純 売 上 (税 抜 )金額", notes="純 売 上 (税 抜 )金額")
    public double getTaxNetSalesAmt() {
        return taxNetSalesAmt;
    }

    public void setTaxNetSalesAmt(double taxNetSalesAmt) {
        this.taxNetSalesAmt = taxNetSalesAmt;
    }

    @ApiModelProperty(value="総 売 上", notes="総 売 上")
    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    @ApiModelProperty(value="総 売 上点数", notes="総 売 上点数")
    public long getTotalSalesPoints() {
        return totalSalesPoints;
    }

    public void setTotalSalesPoints(long totalSalesPoints) {
        this.totalSalesPoints = totalSalesPoints;
    }

    @ApiModelProperty(value="総 売 上金額", notes="総 売 上金額")
    public double getTotalSalesAmt() {
        return totalSalesAmt;
    }

    public void setTotalSalesAmt(double totalSalesAmt) {
        this.totalSalesAmt = totalSalesAmt;
    }

    @ApiModelProperty(value="総 売 上 (税 抜 )", notes="総 売 上 (税 抜 )")
    public String getTaxTotalSales() {
        return taxTotalSales;
    }

    public void setTaxTotalSales(String taxTotalSales) {
        this.taxTotalSales = taxTotalSales;
    }

    @ApiModelProperty(value="総 売 上 (税 抜 )金額", notes="総 売 上 (税 抜 )金額")
    public double getTaxTotalSalesAmt() {
        return taxTotalSalesAmt;
    }

    public void setTaxTotalSalesAmt(double taxTotalSalesAmt) {
        this.taxTotalSalesAmt = taxTotalSalesAmt;
    }

    @ApiModelProperty(value="客 数", notes="客 数")
    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    @ApiModelProperty(value="客 数点数", notes="客 数点数")
    public long getCustomersNo() {
        return customersNo;
    }

    public void setCustomersNo(long customersNo) {
        this.customersNo = customersNo;
    }

    @ApiModelProperty(value="課 税 (税 抜 )", notes="課 税 (税 抜 ) ")
    public String getTaxation() {
        return taxation;
    }

    public void setTaxation(String taxation) {
        this.taxation = taxation;
    }

    @ApiModelProperty(value="課 税 (税 抜 )点数", notes="課 税 (税 抜 )点数")
    public long getTaxationPoints() {
        return taxationPoints;
    }

    public void setTaxationPoints(long taxationPoints) {
        this.taxationPoints = taxationPoints;
    }

    @ApiModelProperty(value="課 税 (税 抜 )金額", notes="課 税 (税 抜 )金額")
    public double getTaxationAmt() {
        return taxationAmt;
    }

    public void setTaxationAmt(double taxationAmt) {
        this.taxationAmt = taxationAmt;
    }

    @ApiModelProperty(value="税", notes="税")
    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @ApiModelProperty(value="税金額", notes="税金額")
    public double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(double taxAmt) {
        this.taxAmt = taxAmt;
    }

    @ApiModelProperty(value="非 課 税", notes="非 課 税")
    public String getTaxExemption() {
        return taxExemption;
    }

    public void setTaxExemption(String taxExemption) {
        this.taxExemption = taxExemption;
    }

    @ApiModelProperty(value="非 課 税点数", notes="非 課 税点数")
    public long getTaxExemptionPoints() {
        return taxExemptionPoints;
    }

    public void setTaxExemptionPoints(long taxExemptionPoints) {
        this.taxExemptionPoints = taxExemptionPoints;
    }

    @ApiModelProperty(value="非 課 税金額", notes="非 課 税金額")
    public double getTaxExemptionAmt() {
        return taxExemptionAmt;
    }

    public void setTaxExemptionAmt(double taxExemptionAmt) {
        this.taxExemptionAmt = taxExemptionAmt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getTaxSubtotal() {
        return taxSubtotal;
    }

    public void setTaxSubtotal(String taxSubtotal) {
        this.taxSubtotal = taxSubtotal;
    }

    @ApiModelProperty(value="計点数", notes="計点数")
    public long getTaxSubtotalPoints() {
        return taxSubtotalPoints;
    }

    public void setTaxSubtotalPoints(long taxSubtotalPoints) {
        this.taxSubtotalPoints = taxSubtotalPoints;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getTaxSubtotalAmt() {
        return taxSubtotalAmt;
    }

    public void setTaxSubtotalAmt(double taxSubtotalAmt) {
        this.taxSubtotalAmt = taxSubtotalAmt;
    }

    @ApiModelProperty(value="明 細 割 引", notes="明 細 割 引")
    public String getItemDiscounts() {
        return itemDiscounts;
    }

    public void setItemDiscounts(String itemDiscounts) {
        this.itemDiscounts = itemDiscounts;
    }

    @ApiModelProperty(value="明 細 割 引点数", notes="明 細 割 引点数")
    public long getItemDiscountsPoints() {
        return itemDiscountsPoints;
    }

    public void setItemDiscountsPoints(long itemDiscountsPoints) {
        this.itemDiscountsPoints = itemDiscountsPoints;
    }

    @ApiModelProperty(value="明 細 割 引金額", notes="明 細 割 引金額")
    public double getItemDiscountsAmt() {
        return itemDiscountsAmt;
    }

    public void setItemDiscountsAmt(double itemDiscountsAmt) {
        this.itemDiscountsAmt = itemDiscountsAmt;
    }

    @ApiModelProperty(value="明 細 値 引", notes="明 細 値 引")
    public String getItemNebiki() {
        return itemNebiki;
    }

    public void setItemNebiki(String itemNebiki) {
        this.itemNebiki = itemNebiki;
    }

    @ApiModelProperty(value="明 細 値 引点数", notes="明 細 値 引点数")
    public long getItemNebikiPoints() {
        return itemNebikiPoints;
    }

    public void setItemNebikiPoints(long itemNebikiPoints) {
        this.itemNebikiPoints = itemNebikiPoints;
    }

    @ApiModelProperty(value="明 細 値 引金額", notes="明 細 値 引金額")
    public double getItemNebikiAmt() {
        return itemNebikiAmt;
    }

    public void setItemNebikiAmt(double itemNebikiAmt) {
        this.itemNebikiAmt = itemNebikiAmt;
    }

    @ApiModelProperty(value="イ ベ ン ト", notes="イ ベ ン ト")
    public String getEventsName() {
        return eventsName;
    }

    public void setEventsName(String eventsName) {
        this.eventsName = eventsName;
    }

    @ApiModelProperty(value="イ ベ ン ト点数", notes="イ ベ ン ト点数")
    public long getEventsPoints() {
        return eventsPoints;
    }

    public void setEventsPoints(long eventsPoints) {
        this.eventsPoints = eventsPoints;
    }

    @ApiModelProperty(value="イ ベ ン ト金額", notes="イ ベ ン ト金額")
    public double getEventsAmt() {
        return eventsAmt;
    }

    public void setEventsAmt(double eventsAmt) {
        this.eventsAmt = eventsAmt;
    }

    @ApiModelProperty(value="社 員 販 売", notes="社 員 販 売")
    public String getEmployeeSales() {
        return employeeSales;
    }

    public void setEmployeeSales(String employeeSales) {
        this.employeeSales = employeeSales;
    }

    @ApiModelProperty(value="社 員 販 売点数", notes="社 員 販 売点数")
    public long getEmployeeSalesPoints() {
        return employeeSalesPoints;
    }

    public void setEmployeeSalesPoints(long employeeSalesPoints) {
        this.employeeSalesPoints = employeeSalesPoints;
    }

    @ApiModelProperty(value="社 員 販 売金額", notes="社 員 販 売金額")
    public double getEmployeeSalesAmt() {
        return employeeSalesAmt;
    }

    public void setEmployeeSalesAmt(double employeeSalesAmt) {
        this.employeeSalesAmt = employeeSalesAmt;
    }

    @ApiModelProperty(value="小 計 割 引", notes="小 計 割 引")
    public String getSubtotalDiscounts() {
        return subtotalDiscounts;
    }

    public void setSubtotalDiscounts(String subtotalDiscounts) {
        this.subtotalDiscounts = subtotalDiscounts;
    }

    @ApiModelProperty(value="小 計 割 引点数", notes="小 計 割 引点数")
    public long getSubtotalDiscountsPoints() {
        return subtotalDiscountsPoints;
    }

    public void setSubtotalDiscountsPoints(long subtotalDiscountsPoints) {
        this.subtotalDiscountsPoints = subtotalDiscountsPoints;
    }

    @ApiModelProperty(value="小 計 割 引金額", notes="小 計 割 引金額")
    public double getSubtotalDiscountsAmt() {
        return subtotalDiscountsAmt;
    }

    public void setSubtotalDiscountsAmt(double subtotalDiscountsAmt) {
        this.subtotalDiscountsAmt = subtotalDiscountsAmt;
    }

    @ApiModelProperty(value="小 計 値 引", notes="小 計 値 引")
    public String getSubtotalNebiki() {
        return subtotalNebiki;
    }

    public void setSubtotalNebiki(String subtotalNebiki) {
        this.subtotalNebiki = subtotalNebiki;
    }

    @ApiModelProperty(value="小 計 値 引点数", notes="小 計 値 引点数")
    public long getSubtotalNebikiPoints() {
        return subtotalNebikiPoints;
    }

    public void setSubtotalNebikiPoints(long subtotalNebikiPoints) {
        this.subtotalNebikiPoints = subtotalNebikiPoints;
    }

    @ApiModelProperty(value="小 計 値 引金額", notes="小 計 値 引金額")
    public double getSubtotalNebikiAmt() {
        return subtotalNebikiAmt;
    }

    public void setSubtotalNebikiAmt(double subtotalNebikiAmt) {
        this.subtotalNebikiAmt = subtotalNebikiAmt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getDiscountsSubtotal() {
        return discountsSubtotal;
    }

    public void setDiscountsSubtotal(String discountsSubtotal) {
        this.discountsSubtotal = discountsSubtotal;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getDiscountSubtotalAmt() {
        return discountSubtotalAmt;
    }

    public void setDiscountSubtotalAmt(double discountSubtotalAmt) {
        this.discountSubtotalAmt = discountSubtotalAmt;
    }

    @ApiModelProperty(value="販 売", notes="販 売")
    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    @ApiModelProperty(value="販 売点数", notes="販 売点数")
    public long getSellPoints() {
        return sellPoints;
    }

    public void setSellPoints(long sellPoints) {
        this.sellPoints = sellPoints;
    }

    @ApiModelProperty(value="販 売金額", notes="販 売金額")
    public double getSellAmt() {
        return sellAmt;
    }

    public void setSellAmt(double sellAmt) {
        this.sellAmt = sellAmt;
    }

    @ApiModelProperty(value="販 売 取 消", notes="販 売 取 消")
    public String getSellCancel() {
        return sellCancel;
    }

    public void setSellCancel(String sellCancel) {
        this.sellCancel = sellCancel;
    }

    @ApiModelProperty(value="販 売 取 消点数", notes="販 売 取 消点数")
    public long getSellCancelPoints() {
        return sellCancelPoints;
    }

    public void setSellCancelPoints(long sellCancelPoints) {
        this.sellCancelPoints = sellCancelPoints;
    }

    @ApiModelProperty(value="販 売 取 消金額", notes="販 売 取 消金額")
    public double getSellCancelAmt() {
        return sellCancelAmt;
    }

    public void setSellCancelAmt(double sellCancelAmt) {
        this.sellCancelAmt = sellCancelAmt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getSellSubtotal() {
        return sellSubtotal;
    }

    public void setSellSubtotal(String sellSubtotal) {
        this.sellSubtotal = sellSubtotal;
    }

    @ApiModelProperty(value="計点数", notes="計点数")
    public long getSellSubtotalPoints() {
        return sellSubtotalPoints;
    }

    public void setSellSubtotalPoints(long sellSubtotalPoints) {
        this.sellSubtotalPoints = sellSubtotalPoints;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getSellSubtotalAmt() {
        return sellSubtotalAmt;
    }

    public void setSellSubtotalAmt(double sellSubtotalAmt) {
        this.sellSubtotalAmt = sellSubtotalAmt;
    }

    @ApiModelProperty(value="前 受 金", notes="前 受 金")
    public String getAdvances() {
        return advances;
    }

    public void setAdvances(String advances) {
        this.advances = advances;
    }

    @ApiModelProperty(value="前 受 金点数", notes="前 受 金点数")
    public long getAdvancesPoints() {
        return advancesPoints;
    }

    public void setAdvancesPoints(long advancesPoints) {
        this.advancesPoints = advancesPoints;
    }

    @ApiModelProperty(value="前 受 金金額", notes="前 受 金金額")
    public double getAdvancesAmt() {
        return advancesAmt;
    }

    public void setAdvancesAmt(double advancesAmt) {
        this.advancesAmt = advancesAmt;
    }

    @ApiModelProperty(value="前 受 金 取 消", notes="前 受 金 取 消")
    public String getAdvancesCancel() {
        return advancesCancel;
    }

    public void setAdvancesCancel(String advancesCancel) {
        this.advancesCancel = advancesCancel;
    }

    @ApiModelProperty(value="前 受 金 取 消点数", notes="前 受 金 取 消点数")
    public long getAdvancesCancelPoints() {
        return advancesCancelPoints;
    }

    public void setAdvancesCancelPoints(long advancesCancelPoints) {
        this.advancesCancelPoints = advancesCancelPoints;
    }

    @ApiModelProperty(value="前 受 金 取 消金額", notes="前 受 金 取 消金額")
    public double getAdvancesCancelAmt() {
        return advancesCancelAmt;
    }

    public void setAdvancesCancelAmt(double advancesCancelAmt) {
        this.advancesCancelAmt = advancesCancelAmt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getAdvancesSubtotal() {
        return advancesSubtotal;
    }

    public void setAdvancesSubtotal(String advancesSubtotal) {
        this.advancesSubtotal = advancesSubtotal;
    }

    @ApiModelProperty(value="計点数", notes="計点数")
    public long getAdvancesSubtotalPoints() {
        return advancesSubtotalPoints;
    }

    public void setAdvancesSubtotalPoints(long advancesSubtotalPoints) {
        this.advancesSubtotalPoints = advancesSubtotalPoints;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getAdvancesSubtotalAmt() {
        return advancesSubtotalAmt;
    }

    public void setAdvancesSubtotalAmt(double advancesSubtotalAmt) {
        this.advancesSubtotalAmt = advancesSubtotalAmt;
    }

    @ApiModelProperty(value="現 金", notes="現 金")
    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    @ApiModelProperty(value="現 金金額", notes="現 金金額")
    public double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt = cashAmt;
    }

    @ApiModelProperty(value="ク レ ジ ッ ト", notes="ク レ ジ ッ ト")
    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    @ApiModelProperty(value="ク レ ジ ッ ト金額", notes="ク レ ジ ッ ト金額")
    public double getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(double creditAmt) {
        this.creditAmt = creditAmt;
    }

    @ApiModelProperty(value="銀 聯", notes="銀 聯")
    public String getUnionPay() {
        return unionPay;
    }

    public void setUnionPay(String unionPay) {
        this.unionPay = unionPay;
    }

    @ApiModelProperty(value="銀 聯金額", notes="銀 聯金額")
    public double getUnionPayAmt() {
        return unionPayAmt;
    }

    public void setUnionPayAmt(double unionPayAmt) {
        this.unionPayAmt = unionPayAmt;
    }

    @ApiModelProperty(value="ギ フ ト カ ー ド", notes="ギ フ ト カ ー ド")
    public String getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(String giftCard) {
        this.giftCard = giftCard;
    }

    @ApiModelProperty(value="ギ フ ト カ ー ド金額", notes="ギ フ ト カ ー ド金額")
    public double getGiftCardAmt() {
        return giftCardAmt;
    }

    public void setGiftCardAmt(double giftCardAmt) {
        this.giftCardAmt = giftCardAmt;
    }

    @ApiModelProperty(value="商 品 券", notes="商 品 券")
    public String getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(String giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @ApiModelProperty(value="商 品 券金額", notes="商 品 券金額")
    public double getGiftCertificatesAmt() {
        return giftCertificatesAmt;
    }

    public void setGiftCertificatesAmt(double giftCertificatesAmt) {
        this.giftCertificatesAmt = giftCertificatesAmt;
    }

    @ApiModelProperty(value="振 込 入 金", notes="振 込 入 金")
    public String getTransferPayment() {
        return transferPayment;
    }

    public void setTransferPayment(String transferPayment) {
        this.transferPayment = transferPayment;
    }

    @ApiModelProperty(value="振 込 入 金金額", notes="振 込 入 金金額")
    public double getTransferPaymentAmt() {
        return transferPaymentAmt;
    }

    public void setTransferPaymentAmt(double transferPaymentAmt) {
        this.transferPaymentAmt = transferPaymentAmt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getGoldSpecies() {
        return goldSpecies;
    }

    public void setGoldSpecies(String goldSpecies) {
        this.goldSpecies = goldSpecies;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getGoldSpeciesSubtotal() {
        return goldSpeciesSubtotal;
    }

    public void setGoldSpeciesSubtotal(double goldSpeciesSubtotal) {
        this.goldSpeciesSubtotal = goldSpeciesSubtotal;
    }

    @ApiModelProperty(value="買 物 券", notes="買 物 券")
    public String getShoppingTicket() {
        return shoppingTicket;
    }

    public void setShoppingTicket(String shoppingTicket) {
        this.shoppingTicket = shoppingTicket;
    }

    @ApiModelProperty(value="買 物 券金額", notes="買 物 券金額")
    public double getShoppingTicketAmt() {
        return shoppingTicketAmt;
    }

    public void setShoppingTicketAmt(double shoppingTicketAmt) {
        this.shoppingTicketAmt = shoppingTicketAmt;
    }

    @ApiModelProperty(value="ギ フ ト 券", notes="ギ フ ト 券")
    public String getGiftVoucher() {
        return giftVoucher;
    }

    public void setGiftVoucher(String giftVoucher) {
        this.giftVoucher = giftVoucher;
    }

    @ApiModelProperty(value="ギ フ ト 券金額", notes="ギ フ ト 券金額")
    public double getGiftVoucherAmt() {
        return giftVoucherAmt;
    }

    public void setGiftVoucherAmt(double giftVoucherAmt) {
        this.giftVoucherAmt = giftVoucherAmt;
    }

    @ApiModelProperty(value="伊 勢 丹", notes="伊 勢 丹")
    public String getIsetan() {
        return isetan;
    }

    public void setIsetan(String isetan) {
        this.isetan = isetan;
    }

    @ApiModelProperty(value="伊 勢 丹金額", notes="伊 勢 丹金額")
    public double getIsetanAmt() {
        return isetanAmt;
    }

    public void setIsetanAmt(double isetanAmt) {
        this.isetanAmt = isetanAmt;
    }

    @ApiModelProperty(value="DC", notes="DC")
    public String getDCLable() {
        return DCLable;
    }

    public void setDCLable(String dCLable) {
        DCLable = dCLable;
    }

    @ApiModelProperty(value="DC金額", notes="DC金額")
    public double getDCAmt() {
        return DCAmt;
    }

    public void setDCAmt(double dCAmt) {
        DCAmt = dCAmt;
    }

    @ApiModelProperty(value="AMEX", notes="AMEX")
    public String getAMEXLable() {
        return AMEXLable;
    }

    public void setAMEXLable(String aMEXLable) {
        AMEXLable = aMEXLable;
    }

    @ApiModelProperty(value="AMEX金額", notes="AMEX金額")
    public double getAMEXAmt() {
        return AMEXAmt;
    }

    public void setAMEXAmt(double aMEXAmt) {
        AMEXAmt = aMEXAmt;
    }

    @ApiModelProperty(value="JCB", notes="JCB")
    public String getJCBLable() {
        return JCBLable;
    }

    public void setJCBLable(String jCBLable) {
        JCBLable = jCBLable;
    }

    @ApiModelProperty(value="JCB金額", notes="JCB金額")
    public double getJCBAmt() {
        return JCBAmt;
    }

    public void setJCBAmt(double jCBAmt) {
        JCBAmt = jCBAmt;
    }

    @ApiModelProperty(value="Diners", notes="Diners")
    public String getDinersLable() {
        return dinersLable;
    }

    public void setDinersLable(String dinersLable) {
        this.dinersLable = dinersLable;
    }

    @ApiModelProperty(value="Diners金額", notes="Diners金額")
    public double getDinersAmt() {
        return dinersAmt;
    }

    public void setDinersAmt(double dinersAmt) {
        this.dinersAmt = dinersAmt;
    }

    @ApiModelProperty(value="三 井金額", notes="三 井金額")
    public String getMitsuiLable() {
        return mitsuiLable;
    }

    public void setMitsuiLable(String mitsuiLable) {
        this.mitsuiLable = mitsuiLable;
    }

    @ApiModelProperty(value="三 井金額", notes="三 井金額")
    public double getMitsuiAmt() {
        return mitsuiAmt;
    }

    public void setMitsuiAmt(double mitsuiAmt) {
        this.mitsuiAmt = mitsuiAmt;
    }

    @ApiModelProperty(value="軽 井 沢", notes="軽 井 沢")
    public String getKaruizawa() {
        return karuizawa;
    }

    public void setKaruizawa(String karuizawa) {
        this.karuizawa = karuizawa;
    }

    @ApiModelProperty(value="軽 井 沢金額", notes="軽 井 沢金額")
    public double getKaruizawaAmt() {
        return karuizawaAmt;
    }

    public void setKaruizawaAmt(double karuizawaAmt) {
        this.karuizawaAmt = karuizawaAmt;
    }

    @ApiModelProperty(value="Iカード", notes="Iカード")
    public String getICard() {
        return ICard;
    }

    public void setICard(String iCard) {
        ICard = iCard;
    }

    @ApiModelProperty(value="Iカード金額", notes="Iカード金額")
    public double getICardAmt() {
        return ICardAmt;
    }

    public void setICardAmt(double iCardAmt) {
        ICardAmt = iCardAmt;
    }

    @ApiModelProperty(value="Chel 1000", notes="Chel 1000")
    public String getChel1000() {
        return chel1000;
    }

    public void setChel1000(String chel1000) {
        this.chel1000 = chel1000;
    }

    @ApiModelProperty(value="Chel 1000 金額", notes="Chel 1000 金額")
    public double getChel1000Amt() {
        return chel1000Amt;
    }

    public void setChel1000Amt(double chel1000Amt) {
        this.chel1000Amt = chel1000Amt;
    }

    @ApiModelProperty(value="Chel 2000", notes="Chel 2000")
    public String getChel2000() {
        return chel2000;
    }

    public void setChel2000(String chel2000) {
        this.chel2000 = chel2000;
    }

    @ApiModelProperty(value="Chel 2000金額", notes="Chel 2000金額")
    public double getChel2000Amt() {
        return chel2000Amt;
    }

    public void setChel2000Amt(double chel2000Amt) {
        this.chel2000Amt = chel2000Amt;
    }

    @ApiModelProperty(value="計", notes="計")
    public String getGiftCertificatesSubtotal() {
        return giftCertificatesSubtotal;
    }

    public void setGiftCertificatesSubtotal(String giftCertificatesSubtotal) {
        this.giftCertificatesSubtotal = giftCertificatesSubtotal;
    }

    @ApiModelProperty(value="計金額", notes="計金額")
    public double getGiftCertificatesSubtotalAmt() {
        return giftCertificatesSubtotalAmt;
    }

    public void setGiftCertificatesSubtotalAmt(
            double giftCertificatesSubtotalAmt) {
        this.giftCertificatesSubtotalAmt = giftCertificatesSubtotalAmt;
    }

    @ApiModelProperty(value="回 収 金", notes="回 収 金")
    public String getCollectedfunds() {
        return collectedfunds;
    }

    public void setCollectedfunds(String collectedfunds) {
        this.collectedfunds = collectedfunds;
    }

    @ApiModelProperty(value="レポートモデル", notes="レポートモデル")
    public double getCollectedfundAmt() {
        return collectedfundAmt;
    }

    public void setCollectedfundAmt(double collectedfundAmt) {
        this.collectedfundAmt = collectedfundAmt;
    }

    @ApiModelProperty(value="回 収 金金額", notes="回 収 金金額")
    public String getDiscontinuation() {
        return discontinuation;
    }

    public void setDiscontinuation(String discontinuation) {
        this.discontinuation = discontinuation;
    }

    @ApiModelProperty(value="中 止 回 数点数", notes="中 止 回 数点数")
    public long getDiscontinuationPoints() {
        return discontinuationPoints;
    }

    public void setDiscontinuationPoints(long discontinuationPoints) {
        this.discontinuationPoints = discontinuationPoints;
    }

    @ApiModelProperty(value="両 替 回 数", notes="両 替 回 数")
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @ApiModelProperty(value="両 替 回 数点数", notes="両 替 回 数点数")
    public long getExchangePoints() {
        return exchangePoints;
    }

    public void setExchangePoints(long exchangePoints) {
        this.exchangePoints = exchangePoints;
    }

    @ApiModelProperty(value="印 紙 200", notes="印 紙 200")
    public String getStamp200() {
        return stamp200;
    }

    public void setStamp200(String stamp200) {
        this.stamp200 = stamp200;
    }

    @ApiModelProperty(value="印 紙 200金額", notes="印 紙 200金額")
    public double getStamp200Amt() {
        return stamp200Amt;
    }

    public void setStamp200Amt(double stamp200Amt) {
        this.stamp200Amt = stamp200Amt;
    }

    @ApiModelProperty(value="印 紙 400", notes="印 紙 400")
    public String getStamp400() {
        return stamp400;
    }

    public void setStamp400(String stamp400) {
        this.stamp400 = stamp400;
    }

    @ApiModelProperty(value="印 紙 400金額", notes="印 紙 400金額")
    public double getStamp400Amt() {
        return stamp400Amt;
    }

    public void setStamp400Amt(double stamp400Amt) {
        this.stamp400Amt = stamp400Amt;
    }

    @ApiModelProperty(value="印 紙 600", notes="印 紙 600")
    public String getStamp600() {
        return stamp600;
    }

    public void setStamp600(String stamp600) {
        this.stamp600 = stamp600;
    }

    @ApiModelProperty(value="印 紙 600金額", notes="印 紙 600金額")
    public double getStamp600Amt() {
        return stamp600Amt;
    }

    public void setStamp600Amt(double stamp600Amt) {
        this.stamp600Amt = stamp600Amt;
    }

    @ApiModelProperty(value="印 紙 1,000", notes="印 紙 1,000")
    public String getStamp1000() {
        return stamp1000;
    }

    public void setStamp1000(String stamp1000) {
        this.stamp1000 = stamp1000;
    }

    @ApiModelProperty(value="印 紙 1,000金額", notes="印 紙 1,000金額")
    public double getStamp1000Amt() {
        return stamp1000Amt;
    }

    public void setStamp1000Amt(double stamp1000Amt) {
        this.stamp1000Amt = stamp1000Amt;
    }

    @ApiModelProperty(value="印 紙 2,000", notes="印 紙 2,000")
    public String getStamp2000() {
        return stamp2000;
    }

    public void setStamp2000(String stamp2000) {
        this.stamp2000 = stamp2000;
    }

    @ApiModelProperty(value="印 紙 2,000金額", notes="印 紙 2,000金額")
    public double getStamp2000Amt() {
        return stamp2000Amt;
    }

    public void setStamp2000Amt(double stamp2000Amt) {
        this.stamp2000Amt = stamp2000Amt;
    }

    @ApiModelProperty(value="印 紙 4,000", notes="印 紙 4,000")
    public String getStamp4000() {
        return stamp4000;
    }

    public void setStamp4000(String stamp4000) {
        this.stamp4000 = stamp4000;
    }

    @ApiModelProperty(value="印 紙 4,000金額", notes="印 紙 4,000金額")
    public double getStamp4000Amt() {
        return stamp4000Amt;
    }

    public void setStamp4000Amt(double stamp4000Amt) {
        this.stamp4000Amt = stamp4000Amt;
    }

    // 1.03 2014.12.25 MAJINHUI 会計レポート出力を対応　ADD END
    @ApiModelProperty(value="集計日付", notes="集計日付")
    public String getBusinessDayDate() {
        return businessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        this.businessDayDate = businessDayDate;
    }

    @ApiModelProperty(value="会社コード", notes="会社コード")
    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    @ApiModelProperty(value="店舗番号", notes="店舗番号")
    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    @ApiModelProperty(value="言葉", notes="言葉")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    @ApiModelProperty(value="予備日付ID", notes="予備日付ID")
    public String getSubdateid2() {
		return subdateid2;
	}

	public void setSubdateid2(String subdateid2) {
		this.subdateid2 = subdateid2;
	}

	@ApiModelProperty(value="予備日付ID", notes="予備日付ID")
	public String getSubdateid1() {
        return subdateid1;
    }

    public void setSubdateid1(String subdateid1) {
        Calendar cal = getTime(subdateid1);
        int month = cal.get(Calendar.MONTH) + 1;
        this.subdateid1 = cal.get(Calendar.YEAR) + "-" + month + "-"
                + cal.get(Calendar.DATE);
    }

    @ApiModelProperty(value="カレンダー", notes="カレンダー")
    private Calendar getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date;
        Calendar cal = null;
        try {
            date = sdf.parse(time);
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    @ApiModelProperty(value="POSNO", notes="POSNO")
    public String getWorkStationID() {
        return workStationID;
    }

    public void setWorkStationID(String workStationID) {
        this.workStationID = workStationID;
    }

    @ApiModelProperty(value="送信管理順序", notes="送信管理順序")
    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    @ApiModelProperty(value="店舗名", notes="店舗名")
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return rptlist
     */
    @ApiModelProperty(value="レポートリスト", notes="レポートリスト")
    public List<ItemMode> getRptlist() {
        return rptlist;
    }

    /**
     * @param rptlist セットする rptlist
     */
    public void setRptlist(List<ItemMode> rptlist) {
        this.rptlist = rptlist;
    }
    /* 1.02 2014.12.03 FENGSHA 売上表を対応 START*/
    /**
     * @return salesManNo
     */
    @ApiModelProperty(value="販売人員番号", notes="販売人員番号")
    public String getSalesManNo() {
        return salesManNo;
    }

    /**
     * @param salesManNo セットする salesManNo
     */
    public void setSalesManNo(String salesManNo) {
        this.salesManNo = salesManNo;
    }

    /**
     * @return operaterNo
     */
    @ApiModelProperty(value="従業員番号", notes="従業員番号")
    public String getOperaterNo() {
        return operaterNo;
    }

    /**
     * @param operaterNo セットする operaterNo
     */
    public void setOperaterNo(String operaterNo) {
        this.operaterNo = operaterNo;
    }

    /**
     * @return reportType
     */
    @ApiModelProperty(value="レポートタイプ", notes="レポートタイプ")
    public String getReportType() {
        return reportType;
    }

    /**
     * @param reportType セットする reportType
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * @return div
     */
    @ApiModelProperty(value="div", notes="div")
    public String getDiv() {
        return div;
    }

    /**
     * @param div セットする div
     */
    public void setDiv(String div) {
        this.div = div;
    }

    /**
     * @return storeidSearch
     */
    @ApiModelProperty(value="店舗番号検索", notes="店舗番号検索")
    public String getStoreidSearch() {
        return storeidSearch;
    }

    /**
     * @param storeidSearch セットする storeidSearch
     */
    public void setStoreidSearch(String storeidSearch) {
        this.storeidSearch = storeidSearch;
    }

    /**
     * @return salesManName
     */
    @ApiModelProperty(value="販売人員名前", notes="販売人員名前")
    public String getSalesManName() {
        return salesManName;
    }

    /**
     * @param salesManName セットする salesManName
     */
    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    /**
     * @return divName
     */
    @ApiModelProperty(value="div名前", notes="div名前")
    public String getDivName() {
        return divName;
    }

    /**
     * @param divName セットする divName
     */
    public void setDivName(String divName) {
        this.divName = divName;
    }

    /**
     * @return allGuestCount
     */
    @ApiModelProperty(value="客数の合計", notes="客数の合計")
    public long getAllGuestCount() {
        return AllGuestCount;
    }

    /**
     * @param allGuestCount セットする allGuestCount
     */
    public void setAllGuestCount(long allGuestCount) {
        AllGuestCount = allGuestCount;
    }

    /**
     * @return allItemCount
     */
    @ApiModelProperty(value="点数の合計", notes="点数の合計")
    public long getAllItemCount() {
        return AllItemCount;
    }

    /**
     * @param allItemCount セットする allItemCount
     */
    public void setAllItemCount(long allItemCount) {
        AllItemCount = allItemCount;
    }

    /**
     * @return allSalesAmount
     */
    @ApiModelProperty(value="金額の合計", notes="金額の合計")
    public double getAllSalesAmount() {
        return AllSalesAmount;
    }

    /**
     * @param allSalesAmount セットする allSalesAmount
     */
    
    public void setAllSalesAmount(double allSalesAmount) {
        AllSalesAmount = allSalesAmount;
    }

    @ApiModelProperty(value="POSLogのシステム日時", notes="POSLogのシステム日時")
	public String getBegindatetime() {
		return begindatetime;
	}

	public void setBegindatetime(String begindatetime) {
		this.begindatetime = begindatetime;
	}

	@ApiModelProperty(value="払戻金", notes="払戻金")
	public String getModorikinn() {
		return modorikinn;
	}

	public void setModorikinn(String modorikinn) {
		this.modorikinn = modorikinn;
	}

	@ApiModelProperty(value="払戻金金額", notes="払戻金金額")
	public double getModorikinnAmt() {
		return modorikinnAmt;
	}

	public void setModorikinnAmt(double modorikinnAmt) {
		this.modorikinnAmt = modorikinnAmt;
	}

	@ApiModelProperty(value="その他", notes="その他")
	public String getSonota() {
		return sonota;
	}

	public void setSonota(String sonota) {
		this.sonota = sonota;
	}

	@ApiModelProperty(value="その他金額", notes="その他金額")
	public double getSonotaAmt() {
		return sonotaAmt;
	}

	public void setSonotaAmt(double sonotaAmt) {
		this.sonotaAmt = sonotaAmt;
	}

    /* 1.02 2014.12.03 FENGSHA 売上表を対応 END*/

}
