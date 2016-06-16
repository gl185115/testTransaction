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
 * ���藚��
 * �o�[�W����      ������t      �S���Җ�        ������e
 * 1.01            2014.10.21    MAJINHUI        ���|�[�g�o�͂�Ή�
 * 1.02            2014.12.22    FENGSHA         ����\��Ή�
 * 1.03            2014.12.25    MAJINHUI        ��v���|�[�g�o�͂�Ή�
 * 1.04            2015.1.21     MAJINHUI        �_���E���Z���|�[�g�o�͂�Ή�
 * 1.05            2015.2.13     MAJINHUI        ���|�[�g�o�͂�Ή�
 */
@ApiModel(value="ReportMode")
public class ReportMode {
	
	private String type;
	private int TrainingFlag;
    //���ݍ�
	private double CalculateTotalAmt;
	//�v�Z�ݍ�
	private double RealTotalAmt;
	//���z
	private double GapAmt;
	
	@ApiModelProperty(value="���ݍ�", notes="���ݍ�")
	public double getCalculateTotalAmt() {
		return CalculateTotalAmt;
	}

	public void setCalculateTotalAmt(double calculateTotalAmt) {
		CalculateTotalAmt = calculateTotalAmt;
	}

	@ApiModelProperty(value="�v�Z�ݍ�", notes="�v�Z�ݍ�")
	public double getRealTotalAmt() {
		return RealTotalAmt;
	}

	public void setRealTotalAmt(double realTotalAmt) {
		RealTotalAmt = realTotalAmt;
	}

	@ApiModelProperty(value="���z", notes="���z")
	public double getGapAmt() {
		return GapAmt;
	}

	public void setGapAmt(double gapAmt) {
		GapAmt = gapAmt;
	}

	@ApiModelProperty(value="�g���[�j���O�t���O", notes="�g���[�j���O�t���O")
    public int getTrainingFlag() {
		return TrainingFlag;
	}

	public void setTrainingFlag(int trainingFlag) {
		TrainingFlag = trainingFlag;
	}
	
	@ApiModelProperty(value="�^�C�v", notes="�^�C�v")
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/* 1.02 2014.12.03 FENGSHA ����\��Ή� START*/
    //�� �� �� number
    private String salesManNo;
    //�� �� �� name
    private String salesManName;
    //�S �� �� numbe
    private String operaterNo;
    // report type
    private String reportType;
    //the division
    private String div;
   //the division name
    private String divName;
    //the search store id
    private String storeidSearch;
   //�q���̍��v
    private long AllGuestCount;
  //�_���̍��v
    private long AllItemCount;
  //���z�̍��v
    private double AllSalesAmount;
    /* 1.02 2014.12.03 FENGSHA ����\��Ή� END*/

    //1.04 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή��@add start
    //cash in draw Amount
    private double AllDrawAmount;
    //print id
    private String tillId;
    //1.04 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή��@add end

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

    // 1.03 2014.12.25 MAJINHUI ��v���|�[�g�o�͂�Ή��@ADD START
    //TEL
    private String telephone;

    // < �� �� >
    // �� ��
    private String sales;
    // �� ��_��
    private long salesPoints;
    // �� ����z
    private double salesAmt;
    // �� �� �l ��
    private String salesDiscount;
    // �� �� �l ���_��
    private long salesDiscountPoints;
    // �� �� �l �����z
    private double salesDiscountAmt;
    // �� ��
    private String cancel;
    // �� ���_��
    private long cancelPoints;
    // �� �����z
    private double cancelAmt;
    // �� �v
    private String salesSubTotal;
    // �� �v�_��
    private long salesTotalPoints;
    // �� �v���z
    private double salesTotalAmt;

    // �� �i �� ��
    private String returnSales;
    // �� �i �� ��_��
    private long returnSalesPoints;
    // �� �i �� ����z
    private double returnSalesAmt;
    // �� �i �l ��
    private String returnDiscount;
    // �� �i �l ���_��
    private long returnDiscountPoints;
    // �� �i �l �����z
    private double returnDiscountAmt;
    // �� �i �� ��
    private String returnCancel;
    // �� �i �� ���_��
    private long returnCancelPoint;
    // �� �i �� �����z
    private double returnCancelAmt;
    // �� �v
    private String returnSubTotal;
    // �� �v�_��
    private long returnTotalPoints;
    // �� �v���z
    private double returnTotalAmt;

    // �� �� ��
    private String netSales;
    // �� �� ��_��
    private long netSalesPoints;
    // �� �� ����z
    private double netSalesAmt;
    // �� �� �� (�� �� )
    private String taxNetSales;
    // �� �� �� (�� �� )���z
    private double taxNetSalesAmt;
    // �� �� ��
    private String totalSales;
    // �� �� ��_��
    private long totalSalesPoints;
    // �� �� ����z
    private double totalSalesAmt;
    // �� �� �� (�� �� )
    private String taxTotalSales;
    // �� �� �� (�� �� )���z
    private double taxTotalSalesAmt;

    // �q ��
    private String customers;
    // �q ���_��
    private long customersNo;

    // < �� �� �� �� >
    // �� �� (�� �� )
    private String taxation;
    // �� �� (�� �� )�_��
    private long taxationPoints;
    // �� �� (�� �� )���z
    private double taxationAmt;
    // ��
    private String tax;
    // �ŋ��z
    private double taxAmt;
    // �� �� ��
    private String taxExemption;
    // �� �� �œ_��
    private long taxExemptionPoints;
    // �� �� �ŋ��z
    private double taxExemptionAmt;
    // �v
    private String taxSubtotal;
    // �v�_��
    private long taxSubtotalPoints;
    // �v���z
    private double taxSubtotalAmt;

    // < �l �� �� �� >
    // �� �� �� ��
    private String itemDiscounts;
    // �� �� �� ���_��
    private long itemDiscountsPoints;
    // �� �� �� �����z
    private double itemDiscountsAmt;
    // �� �� �l ��
    private String itemNebiki;
    // �� �� �l ���_��
    private long itemNebikiPoints;
    // �� �� �l �����z
    private double itemNebikiAmt;
    // �C �x �� �g
    private String eventsName;
    // �C �x �� �g�_��
    private long eventsPoints;
    // �C �x �� �g���z
    private double eventsAmt;
    // �� �� �� ��
    private String employeeSales;
    // �� �� �� ���_��
    private long employeeSalesPoints;
    // �� �� �� �����z
    private double employeeSalesAmt;
    // �� �v �� ��
    private String subtotalDiscounts;
    // �� �v �� ���_��
    private long subtotalDiscountsPoints;
    // �� �v �� �����z
    private double subtotalDiscountsAmt;
    // �� �v �l ��
    private String subtotalNebiki;
    // �� �v �l ���_��
    private long subtotalNebikiPoints;
    // �� �v �l �����z
    private double subtotalNebikiAmt;
    // �v
    private String discountsSubtotal;
    // �v���z
    private double discountSubtotalAmt;

    // <�M�t�g�J�[�h�̔�>
    // �� ��
    private String sell;
    // �� ���_��
    private long sellPoints;
    // �� �����z
    private double sellAmt;
    // �� �� �� ��
    private String sellCancel;
    // �� �� �� ���_��
    private long sellCancelPoints;
    // �� �� �� �����z
    private double sellCancelAmt;
    // �v
    private String sellSubtotal;
    // �v�_��
    private long sellSubtotalPoints;
    // �v���z
    private double sellSubtotalAmt;

    // <�O �� ��>
    // �O �� ���@
    private String advances;
    // �O �� ���_��
    private long advancesPoints;
    // �O �� �����z
    private double advancesAmt;
    // �O �� �� �� ��
    private String advancesCancel;
    // �O �� �� �� ���_��
    private long advancesCancelPoints;
    // �O �� �� �� �����z
    private double advancesCancelAmt;

    // �v
    private String advancesSubtotal;
    // �v�_��
    private long advancesSubtotalPoints;
    // �v���z
    private double advancesSubtotalAmt;
    //�@�� ������
    private long cashPoints;
    // �N �� �W �b �g����
    private long creditPoints;
    // �� ������
    private long unionPayPoints;
    // �M �t �g �J �[ �h����
    private long giftCardPoints;
    // �� �i ������
    private long giftCertificatesPoints;
    // �U �� �� ������
    private long transferPaymentPoints;
    //�O������㌏��
    private long AdvancesSalesPoints;
    // �� �� �� �� ��_���v
    private long goldSpeciesPoints;
    
    // �� �� ������
    private long shoppingTicketPoints;
    // �M �t �g ������
    private long giftVoucherPoints;
    // �� �� �O����
    private long isetanPoints;
    // DC����
    private long DCLablePoints;
    // AMEX����
    private long AMEXLablePoints;
    // JCB����
    private long JCBLablePoints;
    // Diners����
    private long dinersLablePoints;
    // I�J�[�h����
    private long ICardPoints;
    // �O �䌏��
    private long mitsuiLablePoints;
    // �y �� �򌏐�
    private long karuizawaPoints;
    // Chel 1000����
    private long chel1000Points;
    //Chel 2000����
    private long chel2000Points;
    //���̑�����
    private long sonotaPoints;
    // �v����
    private long giftCertificatesSubtotalPoints;
    // �� �� ������
    private long collectedfundsPoints;
    // ���ߋ�����
    private long modorikinnPoints;
    
    // <�� �� �� �� ��>
    // �� ��
    private String cash;
    // �� �����z
    private double cashAmt;
    // �N �� �W �b �g
    private String credit;
    // �N �� �W �b �g���z
    private double creditAmt;
    // �� ��
    private String unionPay;
    // �� �����z
    private double unionPayAmt;
    // �M �t �g �J �[ �h
    private String giftCard;
    // �M �t �g �J �[ �h���z
    private double giftCardAmt;
    // �� �i ��
    private String giftCertificates;
    // �� �i �����z
    private double giftCertificatesAmt;
    // �U �� �� ��
    private String transferPayment;
    // �U �� �� �����z
    private double transferPaymentAmt;
    //1.05   2015.2.13   MAJINHUI   ���|�[�g�o�͂�Ή� add start
    //�O�������
    private String AdvancesSales;
    //�O���������z
    private double AdvancesSalesAmt;
    //1.05   2015.2.13   MAJINHUI   ���|�[�g�o�͂�Ή� add end
    
    // �v
    private String goldSpecies;
    // �v���z
    private double goldSpeciesSubtotal;

    // <�� �i �� �� ��>
    // �� �� ��
    private String shoppingTicket;
    // �� �� �����z
    private double shoppingTicketAmt;
    // �M �t �g ��
    private String giftVoucher;
    // �M �t �g �����z
    private double giftVoucherAmt;
    // �� �� �O
    private String isetan;
    // �� �� �O���z
    private double isetanAmt;
    // DC
    private String DCLable;
    // DC���z
    private double DCAmt;
    // AMEX
    private String AMEXLable;
    // AMEX���z
    private double AMEXAmt;
    // JCB
    private String JCBLable;
    // JCB���z
    private double JCBAmt;
    // Diners
    private String dinersLable;
    // Diners���z
    private double dinersAmt;
    // I�J�[�h
    private String ICard;
    // I�J�[�h
    private double ICardAmt;
    // �O ����z
    private String mitsuiLable;
    // �O ����z
    private double mitsuiAmt;
    // �y �� ��
    private String karuizawa;
    // �y �� ����z
    private double karuizawaAmt;
    // Chel 1000
    private String chel1000;
    // Chel 1000 ���z
    private double chel1000Amt;
    //Chel 2000
    private String chel2000;
    //Chel 2000���z
    private double chel2000Amt;
    private String sonota;
    private double sonotaAmt;
    // �v
    private String giftCertificatesSubtotal;
    // �v���z
    private double giftCertificatesSubtotalAmt;

    // �� �� ��
    private String collectedfunds;
    // �� �� �����z
    private double collectedfundAmt;
    // ���ߋ�
    private String modorikinn;
    // ���ߋ����z
    private double modorikinnAmt;
    
    // �� �~ �� ��
    private String discontinuation;
    // �� �~ �� ���_��
    private long discontinuationPoints;
    // �� �� �� ��
    private String exchange;
    // �� �� �� ���_��
    private long exchangePoints;

    // <�� �� �� ��>
    // �� �� 200
    private String stamp200;
    // �� �� 200���z
    private double stamp200Amt;
    // �� �� 400
    private String stamp400;
    // �� �� 400���z
    private double stamp400Amt;
    // �� �� 600
    private String stamp600;
    // �� �� 600���z
    private double stamp600Amt;
    // �� �� 1,000
    private String stamp1000;
    // �� �� 1,000���z
    private double stamp1000Amt;
    // �� �� 2,000
    private String stamp2000;
    // �� �� 2,000���z
    private double stamp2000Amt;
    // �� �� 4,000
    private String stamp4000;
    // �� �� 4,000���z
    private double stamp4000Amt;

    @ApiModelProperty(value="�d�b", notes="�d�b")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @ApiModelProperty(value="�� �� ������", notes="�� �� ������")
	public long getCollectedfundsPoints() {
		return collectedfundsPoints;
	}

	public void setCollectedfundsPoints(long collectedfundsPoints) {
		this.collectedfundsPoints = collectedfundsPoints;
	}

	@ApiModelProperty(value="���ߋ�����", notes="���ߋ�����")
	public long getModorikinnPoints() {
		return modorikinnPoints;
	}

	public void setModorikinnPoints(long modorikinnPoints) {
		this.modorikinnPoints = modorikinnPoints;
	}

	@ApiModelProperty(value="�� ������", notes="�� ������")
	public long getCashPoints() {
		return cashPoints;
	}

	public void setCashPoints(long cashPoints) {
		this.cashPoints = cashPoints;
	}

	@ApiModelProperty(value="�N �� �W �b �g����", notes="�N �� �W �b �g����")
	public long getCreditPoints() {
		return creditPoints;
	}

	public void setCreditPoints(long creditPoints) {
		this.creditPoints = creditPoints;
	}

	@ApiModelProperty(value="�� ������", notes="�� ������")
	public long getUnionPayPoints() {
		return unionPayPoints;
	}

	public void setUnionPayPoints(long unionPayPoints) {
		this.unionPayPoints = unionPayPoints;
	}

	@ApiModelProperty(value="�M �t �g �J �[ �h����", notes="�M �t �g �J �[ �h����")
	public long getGiftCardPoints() {
		return giftCardPoints;
	}

	public void setGiftCardPoints(long giftCardPoints) {
		this.giftCardPoints = giftCardPoints;
	}

	@ApiModelProperty(value="�� �i ������", notes="�� �i ������")
	public long getGiftCertificatesPoints() {
		return giftCertificatesPoints;
	}

	public void setGiftCertificatesPoints(long giftCertificatesPoints) {
		this.giftCertificatesPoints = giftCertificatesPoints;
	}

	@ApiModelProperty(value="�U �� �� ������", notes="�U �� �� ������")
	public long getTransferPaymentPoints() {
		return transferPaymentPoints;
	}

	public void setTransferPaymentPoints(long transferPaymentPoints) {
		this.transferPaymentPoints = transferPaymentPoints;
	}

	@ApiModelProperty(value="�O������㌏��", notes="�O������㌏��")
	public long getAdvancesSalesPoints() {
		return AdvancesSalesPoints;
	}

	public void setAdvancesSalesPoints(long advancesSalesPoints) {
		AdvancesSalesPoints = advancesSalesPoints;
	}

	@ApiModelProperty(value="�� �� �� �� ��_���v", notes="�� �� �� �� ��_���v")
	public long getGoldSpeciesPoints() {
		return goldSpeciesPoints;
	}

	public void setGoldSpeciesPoints(long goldSpeciesPoints) {
		this.goldSpeciesPoints = goldSpeciesPoints;
	}

	@ApiModelProperty(value="�� �� ������", notes="�� �� ������")
	public long getShoppingTicketPoints() {
		return shoppingTicketPoints;
	}

	public void setShoppingTicketPoints(long shoppingTicketPoints) {
		this.shoppingTicketPoints = shoppingTicketPoints;
	}

	@ApiModelProperty(value="�M �t �g ������", notes="�M �t �g ������")
	public long getGiftVoucherPoints() {
		return giftVoucherPoints;
	}

	public void setGiftVoucherPoints(long giftVoucherPoints) {
		this.giftVoucherPoints = giftVoucherPoints;
	}

	@ApiModelProperty(value="�� �� �O����", notes="�� �� �O����")
	public long getIsetanPoints() {
		return isetanPoints;
	}

	public void setIsetanPoints(long isetanPoints) {
		this.isetanPoints = isetanPoints;
	}

	@ApiModelProperty(value="DC����", notes="DC����")
	public long getDCLablePoints() {
		return DCLablePoints;
	}

	public void setDCLablePoints(long dCLablePoints) {
		DCLablePoints = dCLablePoints;
	}

	@ApiModelProperty(value="AMEX����", notes="AMEX����")
	public long getAMEXLablePoints() {
		return AMEXLablePoints;
	}

	public void setAMEXLablePoints(long aMEXLablePoints) {
		AMEXLablePoints = aMEXLablePoints;
	}

	@ApiModelProperty(value="JCB����", notes="JCB����")
	public long getJCBLablePoints() {
		return JCBLablePoints;
	}

	public void setJCBLablePoints(long jCBLablePoints) {
		JCBLablePoints = jCBLablePoints;
	}

	@ApiModelProperty(value="Diners����", notes="Diners����")
	public long getDinersLablePoints() {
		return dinersLablePoints;
	}

	public void setDinersLablePoints(long dinersLablePoints) {
		this.dinersLablePoints = dinersLablePoints;
	}

	@ApiModelProperty(value="I�J�[�h����", notes="I�J�[�h����")
	public long getICardPoints() {
		return ICardPoints;
	}

	public void setICardPoints(long iCardPoints) {
		ICardPoints = iCardPoints;
	}

	@ApiModelProperty(value="�O �䌏��", notes="�O �䌏��")
	public long getMitsuiLablePoints() {
		return mitsuiLablePoints;
	}

	public void setMitsuiLablePoints(long mitsuiLablePoints) {
		this.mitsuiLablePoints = mitsuiLablePoints;
	}

	@ApiModelProperty(value="�y �� �򌏐�", notes="�y �� �򌏐�")
	public long getKaruizawaPoints() {
		return karuizawaPoints;
	}

	public void setKaruizawaPoints(long karuizawaPoints) {
		this.karuizawaPoints = karuizawaPoints;
	}

	@ApiModelProperty(value="Chel 1000����", notes="Chel 1000����")
	public long getChel1000Points() {
		return chel1000Points;
	}

	public void setChel1000Points(long chel1000Points) {
		this.chel1000Points = chel1000Points;
	}

	@ApiModelProperty(value="Chel 2000����", notes="Chel 2000����")
	public long getChel2000Points() {
		return chel2000Points;
	}

	public void setChel2000Points(long chel2000Points) {
		this.chel2000Points = chel2000Points;
	}

	@ApiModelProperty(value="���̑�����", notes="���̑�����")
	public long getSonotaPoints() {
		return sonotaPoints;
	}

	public void setSonotaPoints(long sonotaPoints) {
		this.sonotaPoints = sonotaPoints;
	}

	@ApiModelProperty(value="�v����", notes="�v����")
	public long getGiftCertificatesSubtotalPoints() {
		return giftCertificatesSubtotalPoints;
	}

	public void setGiftCertificatesSubtotalPoints(
			long giftCertificatesSubtotalPoints) {
		this.giftCertificatesSubtotalPoints = giftCertificatesSubtotalPoints;
	}
    
    // 1.04 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή��@ADD START
	@ApiModelProperty(value="���ׂĈ������z", notes="���ׂĈ������z")
    public double getAllDrawAmount() {
        return AllDrawAmount;
    }

    public void setAllDrawAmount(double allDrawAmount) {
        AllDrawAmount = allDrawAmount;
    }
    
    @ApiModelProperty(value="�h�����[�R�[�h", notes="�h�����[�R�[�h")
    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }
    // 1.04 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή��@ADD END

    @ApiModelProperty(value="�� ��", notes="�� ��")
    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    @ApiModelProperty(value="�� ��_��", notes="�� ��_��")
    public long getSalesPoints() {
        return salesPoints;
    }

    public void setSalesPoints(long salesPoints) {
        this.salesPoints = salesPoints;
    }

    @ApiModelProperty(value="�� ����z", notes="�� ����z")
    public double getSalesAmt() {
        return salesAmt;
    }

    public void setSalesAmt(double salesAmt) {
        this.salesAmt = salesAmt;
    }

    @ApiModelProperty(value="�� �� �l ��", notes="�� �� �l ��")
    public String getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(String salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    @ApiModelProperty(value="�� �� �l ���_��", notes="�� �� �l ���_��")
    public long getSalesDiscountPoints() {
        return salesDiscountPoints;
    }

    public void setSalesDiscountPoints(long salesDiscountPoints) {
        this.salesDiscountPoints = salesDiscountPoints;
    }

    @ApiModelProperty(value="�� �� �l �����z", notes="�� �� �l �����z")
    public double getSalesDiscountAmt() {
        return salesDiscountAmt;
    }

    public void setSalesDiscountAmt(double salesDiscountAmt) {
        this.salesDiscountAmt = salesDiscountAmt;
    }

    @ApiModelProperty(value="�� ��", notes="�� ��")
    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    @ApiModelProperty(value="�� ���_��", notes="�� ���_��")
    public long getCancelPoints() {
        return cancelPoints;
    }

    public void setCancelPoints(long cancelPoints) {
        this.cancelPoints = cancelPoints;
    }

    @ApiModelProperty(value="�� �����z", notes="�� �����z")
    public double getCancelAmt() {
        return cancelAmt;
    }

    public void setCancelAmt(double cancelAmt) {
        this.cancelAmt = cancelAmt;
    }

    @ApiModelProperty(value="�� �v", notes="�� �v")
    public String getSalesSubTotal() {
        return salesSubTotal;
    }

    public void setSalesSubTotal(String salesSubTotal) {
        this.salesSubTotal = salesSubTotal;
    }

    @ApiModelProperty(value="�� �v�_��", notes="�� �v�_��")
    public long getSalesTotalPoints() {
        return salesTotalPoints;
    }

    public void setSalesTotalPoints(long salesTotalPoints) {
        this.salesTotalPoints = salesTotalPoints;
    }

    @ApiModelProperty(value="�� �v���z", notes="�� �v���z")
    public double getSalesTotalAmt() {
        return salesTotalAmt;
    }

    public void setSalesTotalAmt(double salesTotalAmt) {
        this.salesTotalAmt = salesTotalAmt;
    }
    //1.05   2015.2.13   MAJINHUI   ���|�[�g�o�͂�Ή� add start
    @ApiModelProperty(value="�O�������", notes="�O�������")
    public String getAdvancesSales() {
        return AdvancesSales;
    }

    public void setAdvancesSales(String advancesSales) {
        AdvancesSales = advancesSales;
    }

    @ApiModelProperty(value="�O���������z", notes="�O���������z")
    public double getAdvancesSalesAmt() {
        return AdvancesSalesAmt;
    }

    public void setAdvancesSalesAmt(double advancesSalesAmt) {
        AdvancesSalesAmt = advancesSalesAmt;
    }
    //1.05   2015.2.13   MAJINHUI   ���|�[�g�o�͂�Ή� add end
    @ApiModelProperty(value="�� �i �� ��", notes="�� �i �� ��")
    public String getReturnSales() {
        return returnSales;
    }

    public void setReturnSales(String returnSales) {
        this.returnSales = returnSales;
    }

    @ApiModelProperty(value="�� �i �� ��_��", notes="�� �i �� ��_��")
    public long getReturnSalesPoints() {
        return returnSalesPoints;
    }

    public void setReturnSalesPoints(long returnSalesPoints) {
        this.returnSalesPoints = returnSalesPoints;
    }

    @ApiModelProperty(value="�� �i �� ��_��", notes="�� �i �� ��_��")
    public double getReturnSalesAmt() {
        return returnSalesAmt;
    }

    public void setReturnSalesAmt(double returnSalesAmt) {
        this.returnSalesAmt = returnSalesAmt;
    }

    @ApiModelProperty(value="�� �i �l ��", notes="�� �i �l ��")
    public String getReturnDiscount() {
        return returnDiscount;
    }

    public void setReturnDiscount(String returnDiscount) {
        this.returnDiscount = returnDiscount;
    }

    @ApiModelProperty(value="�� �i �l ���_��", notes="�� �i �l ���_��")
    public long getReturnDiscountPoints() {
        return returnDiscountPoints;
    }

    public void setReturnDiscountPoints(long returnDiscountPoints) {
        this.returnDiscountPoints = returnDiscountPoints;
    }

    @ApiModelProperty(value="�� �i �l �����z", notes="�� �i �l �����z")
    public double getReturnDiscountAmt() {
        return returnDiscountAmt;
    }

    public void setReturnDiscountAmt(double returnDiscountAmt) {
        this.returnDiscountAmt = returnDiscountAmt;
    }

    @ApiModelProperty(value="�� �i �� ��", notes="�� �i �� ��")
    public String getReturnCancel() {
        return returnCancel;
    }

    public void setReturnCancel(String returnCancel) {
        this.returnCancel = returnCancel;
    }

    @ApiModelProperty(value="�� �i �� ���_��", notes="�� �i �� ���_��")
    public long getReturnCancelPoint() {
        return returnCancelPoint;
    }

    public void setReturnCancelPoint(long returnCancelPoint) {
        this.returnCancelPoint = returnCancelPoint;
    }

    @ApiModelProperty(value="�� �i �� �����z", notes="�� �i �� �����z")
    public double getReturnCancelAmt() {
        return returnCancelAmt;
    }

    public void setReturnCancelAmt(double returnCancelAmt) {
        this.returnCancelAmt = returnCancelAmt;
    }

    @ApiModelProperty(value="�� �v", notes="�� �v")
    public String getReturnSubTotal() {
        return returnSubTotal;
    }

    public void setReturnSubTotal(String returnSubTotal) {
        this.returnSubTotal = returnSubTotal;
    }

    @ApiModelProperty(value="�� �v�_��", notes="�� �v�_��")
    public long getReturnTotalPoints() {
        return returnTotalPoints;
    }

    public void setReturnTotalPoints(long returnTotalPoints) {
        this.returnTotalPoints = returnTotalPoints;
    }

    @ApiModelProperty(value="�� �v���z", notes="�� �v���z")
    public double getReturnTotalAmt() {
        return returnTotalAmt;
    }

    public void setReturnTotalAmt(double returnTotalAmt) {
        this.returnTotalAmt = returnTotalAmt;
    }

    @ApiModelProperty(value="�� �� ��", notes="�� �� ��")
    public String getNetSales() {
        return netSales;
    }

    public void setNetSales(String netSales) {
        this.netSales = netSales;
    }

    @ApiModelProperty(value="�� �� ��_��", notes="�� �� ��_��")
    public long getNetSalesPoints() {
        return netSalesPoints;
    }

    public void setNetSalesPoints(long netSalesPoints) {
        this.netSalesPoints = netSalesPoints;
    }

    @ApiModelProperty(value="�� �� ����z", notes="�� �� ����z")
    public double getNetSalesAmt() {
        return netSalesAmt;
    }

    public void setNetSalesAmt(double netSalesAmt) {
        this.netSalesAmt = netSalesAmt;
    }

    @ApiModelProperty(value="�� �� �� (�� �� )", notes="�� �� �� (�� �� )")
    public String getTaxNetSales() {
        return taxNetSales;
    }

    public void setTaxNetSales(String taxNetSales) {
        this.taxNetSales = taxNetSales;
    }

    @ApiModelProperty(value="�� �� �� (�� �� )���z", notes="�� �� �� (�� �� )���z")
    public double getTaxNetSalesAmt() {
        return taxNetSalesAmt;
    }

    public void setTaxNetSalesAmt(double taxNetSalesAmt) {
        this.taxNetSalesAmt = taxNetSalesAmt;
    }

    @ApiModelProperty(value="�� �� ��", notes="�� �� ��")
    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    @ApiModelProperty(value="�� �� ��_��", notes="�� �� ��_��")
    public long getTotalSalesPoints() {
        return totalSalesPoints;
    }

    public void setTotalSalesPoints(long totalSalesPoints) {
        this.totalSalesPoints = totalSalesPoints;
    }

    @ApiModelProperty(value="�� �� ����z", notes="�� �� ����z")
    public double getTotalSalesAmt() {
        return totalSalesAmt;
    }

    public void setTotalSalesAmt(double totalSalesAmt) {
        this.totalSalesAmt = totalSalesAmt;
    }

    @ApiModelProperty(value="�� �� �� (�� �� )", notes="�� �� �� (�� �� )")
    public String getTaxTotalSales() {
        return taxTotalSales;
    }

    public void setTaxTotalSales(String taxTotalSales) {
        this.taxTotalSales = taxTotalSales;
    }

    @ApiModelProperty(value="�� �� �� (�� �� )���z", notes="�� �� �� (�� �� )���z")
    public double getTaxTotalSalesAmt() {
        return taxTotalSalesAmt;
    }

    public void setTaxTotalSalesAmt(double taxTotalSalesAmt) {
        this.taxTotalSalesAmt = taxTotalSalesAmt;
    }

    @ApiModelProperty(value="�q ��", notes="�q ��")
    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    @ApiModelProperty(value="�q ���_��", notes="�q ���_��")
    public long getCustomersNo() {
        return customersNo;
    }

    public void setCustomersNo(long customersNo) {
        this.customersNo = customersNo;
    }

    @ApiModelProperty(value="�� �� (�� �� )", notes="�� �� (�� �� ) ")
    public String getTaxation() {
        return taxation;
    }

    public void setTaxation(String taxation) {
        this.taxation = taxation;
    }

    @ApiModelProperty(value="�� �� (�� �� )�_��", notes="�� �� (�� �� )�_��")
    public long getTaxationPoints() {
        return taxationPoints;
    }

    public void setTaxationPoints(long taxationPoints) {
        this.taxationPoints = taxationPoints;
    }

    @ApiModelProperty(value="�� �� (�� �� )���z", notes="�� �� (�� �� )���z")
    public double getTaxationAmt() {
        return taxationAmt;
    }

    public void setTaxationAmt(double taxationAmt) {
        this.taxationAmt = taxationAmt;
    }

    @ApiModelProperty(value="��", notes="��")
    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @ApiModelProperty(value="�ŋ��z", notes="�ŋ��z")
    public double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(double taxAmt) {
        this.taxAmt = taxAmt;
    }

    @ApiModelProperty(value="�� �� ��", notes="�� �� ��")
    public String getTaxExemption() {
        return taxExemption;
    }

    public void setTaxExemption(String taxExemption) {
        this.taxExemption = taxExemption;
    }

    @ApiModelProperty(value="�� �� �œ_��", notes="�� �� �œ_��")
    public long getTaxExemptionPoints() {
        return taxExemptionPoints;
    }

    public void setTaxExemptionPoints(long taxExemptionPoints) {
        this.taxExemptionPoints = taxExemptionPoints;
    }

    @ApiModelProperty(value="�� �� �ŋ��z", notes="�� �� �ŋ��z")
    public double getTaxExemptionAmt() {
        return taxExemptionAmt;
    }

    public void setTaxExemptionAmt(double taxExemptionAmt) {
        this.taxExemptionAmt = taxExemptionAmt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getTaxSubtotal() {
        return taxSubtotal;
    }

    public void setTaxSubtotal(String taxSubtotal) {
        this.taxSubtotal = taxSubtotal;
    }

    @ApiModelProperty(value="�v�_��", notes="�v�_��")
    public long getTaxSubtotalPoints() {
        return taxSubtotalPoints;
    }

    public void setTaxSubtotalPoints(long taxSubtotalPoints) {
        this.taxSubtotalPoints = taxSubtotalPoints;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getTaxSubtotalAmt() {
        return taxSubtotalAmt;
    }

    public void setTaxSubtotalAmt(double taxSubtotalAmt) {
        this.taxSubtotalAmt = taxSubtotalAmt;
    }

    @ApiModelProperty(value="�� �� �� ��", notes="�� �� �� ��")
    public String getItemDiscounts() {
        return itemDiscounts;
    }

    public void setItemDiscounts(String itemDiscounts) {
        this.itemDiscounts = itemDiscounts;
    }

    @ApiModelProperty(value="�� �� �� ���_��", notes="�� �� �� ���_��")
    public long getItemDiscountsPoints() {
        return itemDiscountsPoints;
    }

    public void setItemDiscountsPoints(long itemDiscountsPoints) {
        this.itemDiscountsPoints = itemDiscountsPoints;
    }

    @ApiModelProperty(value="�� �� �� �����z", notes="�� �� �� �����z")
    public double getItemDiscountsAmt() {
        return itemDiscountsAmt;
    }

    public void setItemDiscountsAmt(double itemDiscountsAmt) {
        this.itemDiscountsAmt = itemDiscountsAmt;
    }

    @ApiModelProperty(value="�� �� �l ��", notes="�� �� �l ��")
    public String getItemNebiki() {
        return itemNebiki;
    }

    public void setItemNebiki(String itemNebiki) {
        this.itemNebiki = itemNebiki;
    }

    @ApiModelProperty(value="�� �� �l ���_��", notes="�� �� �l ���_��")
    public long getItemNebikiPoints() {
        return itemNebikiPoints;
    }

    public void setItemNebikiPoints(long itemNebikiPoints) {
        this.itemNebikiPoints = itemNebikiPoints;
    }

    @ApiModelProperty(value="�� �� �l �����z", notes="�� �� �l �����z")
    public double getItemNebikiAmt() {
        return itemNebikiAmt;
    }

    public void setItemNebikiAmt(double itemNebikiAmt) {
        this.itemNebikiAmt = itemNebikiAmt;
    }

    @ApiModelProperty(value="�C �x �� �g", notes="�C �x �� �g")
    public String getEventsName() {
        return eventsName;
    }

    public void setEventsName(String eventsName) {
        this.eventsName = eventsName;
    }

    @ApiModelProperty(value="�C �x �� �g�_��", notes="�C �x �� �g�_��")
    public long getEventsPoints() {
        return eventsPoints;
    }

    public void setEventsPoints(long eventsPoints) {
        this.eventsPoints = eventsPoints;
    }

    @ApiModelProperty(value="�C �x �� �g���z", notes="�C �x �� �g���z")
    public double getEventsAmt() {
        return eventsAmt;
    }

    public void setEventsAmt(double eventsAmt) {
        this.eventsAmt = eventsAmt;
    }

    @ApiModelProperty(value="�� �� �� ��", notes="�� �� �� ��")
    public String getEmployeeSales() {
        return employeeSales;
    }

    public void setEmployeeSales(String employeeSales) {
        this.employeeSales = employeeSales;
    }

    @ApiModelProperty(value="�� �� �� ���_��", notes="�� �� �� ���_��")
    public long getEmployeeSalesPoints() {
        return employeeSalesPoints;
    }

    public void setEmployeeSalesPoints(long employeeSalesPoints) {
        this.employeeSalesPoints = employeeSalesPoints;
    }

    @ApiModelProperty(value="�� �� �� �����z", notes="�� �� �� �����z")
    public double getEmployeeSalesAmt() {
        return employeeSalesAmt;
    }

    public void setEmployeeSalesAmt(double employeeSalesAmt) {
        this.employeeSalesAmt = employeeSalesAmt;
    }

    @ApiModelProperty(value="�� �v �� ��", notes="�� �v �� ��")
    public String getSubtotalDiscounts() {
        return subtotalDiscounts;
    }

    public void setSubtotalDiscounts(String subtotalDiscounts) {
        this.subtotalDiscounts = subtotalDiscounts;
    }

    @ApiModelProperty(value="�� �v �� ���_��", notes="�� �v �� ���_��")
    public long getSubtotalDiscountsPoints() {
        return subtotalDiscountsPoints;
    }

    public void setSubtotalDiscountsPoints(long subtotalDiscountsPoints) {
        this.subtotalDiscountsPoints = subtotalDiscountsPoints;
    }

    @ApiModelProperty(value="�� �v �� �����z", notes="�� �v �� �����z")
    public double getSubtotalDiscountsAmt() {
        return subtotalDiscountsAmt;
    }

    public void setSubtotalDiscountsAmt(double subtotalDiscountsAmt) {
        this.subtotalDiscountsAmt = subtotalDiscountsAmt;
    }

    @ApiModelProperty(value="�� �v �l ��", notes="�� �v �l ��")
    public String getSubtotalNebiki() {
        return subtotalNebiki;
    }

    public void setSubtotalNebiki(String subtotalNebiki) {
        this.subtotalNebiki = subtotalNebiki;
    }

    @ApiModelProperty(value="�� �v �l ���_��", notes="�� �v �l ���_��")
    public long getSubtotalNebikiPoints() {
        return subtotalNebikiPoints;
    }

    public void setSubtotalNebikiPoints(long subtotalNebikiPoints) {
        this.subtotalNebikiPoints = subtotalNebikiPoints;
    }

    @ApiModelProperty(value="�� �v �l �����z", notes="�� �v �l �����z")
    public double getSubtotalNebikiAmt() {
        return subtotalNebikiAmt;
    }

    public void setSubtotalNebikiAmt(double subtotalNebikiAmt) {
        this.subtotalNebikiAmt = subtotalNebikiAmt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getDiscountsSubtotal() {
        return discountsSubtotal;
    }

    public void setDiscountsSubtotal(String discountsSubtotal) {
        this.discountsSubtotal = discountsSubtotal;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getDiscountSubtotalAmt() {
        return discountSubtotalAmt;
    }

    public void setDiscountSubtotalAmt(double discountSubtotalAmt) {
        this.discountSubtotalAmt = discountSubtotalAmt;
    }

    @ApiModelProperty(value="�� ��", notes="�� ��")
    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    @ApiModelProperty(value="�� ���_��", notes="�� ���_��")
    public long getSellPoints() {
        return sellPoints;
    }

    public void setSellPoints(long sellPoints) {
        this.sellPoints = sellPoints;
    }

    @ApiModelProperty(value="�� �����z", notes="�� �����z")
    public double getSellAmt() {
        return sellAmt;
    }

    public void setSellAmt(double sellAmt) {
        this.sellAmt = sellAmt;
    }

    @ApiModelProperty(value="�� �� �� ��", notes="�� �� �� ��")
    public String getSellCancel() {
        return sellCancel;
    }

    public void setSellCancel(String sellCancel) {
        this.sellCancel = sellCancel;
    }

    @ApiModelProperty(value="�� �� �� ���_��", notes="�� �� �� ���_��")
    public long getSellCancelPoints() {
        return sellCancelPoints;
    }

    public void setSellCancelPoints(long sellCancelPoints) {
        this.sellCancelPoints = sellCancelPoints;
    }

    @ApiModelProperty(value="�� �� �� �����z", notes="�� �� �� �����z")
    public double getSellCancelAmt() {
        return sellCancelAmt;
    }

    public void setSellCancelAmt(double sellCancelAmt) {
        this.sellCancelAmt = sellCancelAmt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getSellSubtotal() {
        return sellSubtotal;
    }

    public void setSellSubtotal(String sellSubtotal) {
        this.sellSubtotal = sellSubtotal;
    }

    @ApiModelProperty(value="�v�_��", notes="�v�_��")
    public long getSellSubtotalPoints() {
        return sellSubtotalPoints;
    }

    public void setSellSubtotalPoints(long sellSubtotalPoints) {
        this.sellSubtotalPoints = sellSubtotalPoints;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getSellSubtotalAmt() {
        return sellSubtotalAmt;
    }

    public void setSellSubtotalAmt(double sellSubtotalAmt) {
        this.sellSubtotalAmt = sellSubtotalAmt;
    }

    @ApiModelProperty(value="�O �� ��", notes="�O �� ��")
    public String getAdvances() {
        return advances;
    }

    public void setAdvances(String advances) {
        this.advances = advances;
    }

    @ApiModelProperty(value="�O �� ���_��", notes="�O �� ���_��")
    public long getAdvancesPoints() {
        return advancesPoints;
    }

    public void setAdvancesPoints(long advancesPoints) {
        this.advancesPoints = advancesPoints;
    }

    @ApiModelProperty(value="�O �� �����z", notes="�O �� �����z")
    public double getAdvancesAmt() {
        return advancesAmt;
    }

    public void setAdvancesAmt(double advancesAmt) {
        this.advancesAmt = advancesAmt;
    }

    @ApiModelProperty(value="�O �� �� �� ��", notes="�O �� �� �� ��")
    public String getAdvancesCancel() {
        return advancesCancel;
    }

    public void setAdvancesCancel(String advancesCancel) {
        this.advancesCancel = advancesCancel;
    }

    @ApiModelProperty(value="�O �� �� �� ���_��", notes="�O �� �� �� ���_��")
    public long getAdvancesCancelPoints() {
        return advancesCancelPoints;
    }

    public void setAdvancesCancelPoints(long advancesCancelPoints) {
        this.advancesCancelPoints = advancesCancelPoints;
    }

    @ApiModelProperty(value="�O �� �� �� �����z", notes="�O �� �� �� �����z")
    public double getAdvancesCancelAmt() {
        return advancesCancelAmt;
    }

    public void setAdvancesCancelAmt(double advancesCancelAmt) {
        this.advancesCancelAmt = advancesCancelAmt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getAdvancesSubtotal() {
        return advancesSubtotal;
    }

    public void setAdvancesSubtotal(String advancesSubtotal) {
        this.advancesSubtotal = advancesSubtotal;
    }

    @ApiModelProperty(value="�v�_��", notes="�v�_��")
    public long getAdvancesSubtotalPoints() {
        return advancesSubtotalPoints;
    }

    public void setAdvancesSubtotalPoints(long advancesSubtotalPoints) {
        this.advancesSubtotalPoints = advancesSubtotalPoints;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getAdvancesSubtotalAmt() {
        return advancesSubtotalAmt;
    }

    public void setAdvancesSubtotalAmt(double advancesSubtotalAmt) {
        this.advancesSubtotalAmt = advancesSubtotalAmt;
    }

    @ApiModelProperty(value="�� ��", notes="�� ��")
    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    @ApiModelProperty(value="�� �����z", notes="�� �����z")
    public double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt = cashAmt;
    }

    @ApiModelProperty(value="�N �� �W �b �g", notes="�N �� �W �b �g")
    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    @ApiModelProperty(value="�N �� �W �b �g���z", notes="�N �� �W �b �g���z")
    public double getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(double creditAmt) {
        this.creditAmt = creditAmt;
    }

    @ApiModelProperty(value="�� ��", notes="�� ��")
    public String getUnionPay() {
        return unionPay;
    }

    public void setUnionPay(String unionPay) {
        this.unionPay = unionPay;
    }

    @ApiModelProperty(value="�� �����z", notes="�� �����z")
    public double getUnionPayAmt() {
        return unionPayAmt;
    }

    public void setUnionPayAmt(double unionPayAmt) {
        this.unionPayAmt = unionPayAmt;
    }

    @ApiModelProperty(value="�M �t �g �J �[ �h", notes="�M �t �g �J �[ �h")
    public String getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(String giftCard) {
        this.giftCard = giftCard;
    }

    @ApiModelProperty(value="�M �t �g �J �[ �h���z", notes="�M �t �g �J �[ �h���z")
    public double getGiftCardAmt() {
        return giftCardAmt;
    }

    public void setGiftCardAmt(double giftCardAmt) {
        this.giftCardAmt = giftCardAmt;
    }

    @ApiModelProperty(value="�� �i ��", notes="�� �i ��")
    public String getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(String giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @ApiModelProperty(value="�� �i �����z", notes="�� �i �����z")
    public double getGiftCertificatesAmt() {
        return giftCertificatesAmt;
    }

    public void setGiftCertificatesAmt(double giftCertificatesAmt) {
        this.giftCertificatesAmt = giftCertificatesAmt;
    }

    @ApiModelProperty(value="�U �� �� ��", notes="�U �� �� ��")
    public String getTransferPayment() {
        return transferPayment;
    }

    public void setTransferPayment(String transferPayment) {
        this.transferPayment = transferPayment;
    }

    @ApiModelProperty(value="�U �� �� �����z", notes="�U �� �� �����z")
    public double getTransferPaymentAmt() {
        return transferPaymentAmt;
    }

    public void setTransferPaymentAmt(double transferPaymentAmt) {
        this.transferPaymentAmt = transferPaymentAmt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getGoldSpecies() {
        return goldSpecies;
    }

    public void setGoldSpecies(String goldSpecies) {
        this.goldSpecies = goldSpecies;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getGoldSpeciesSubtotal() {
        return goldSpeciesSubtotal;
    }

    public void setGoldSpeciesSubtotal(double goldSpeciesSubtotal) {
        this.goldSpeciesSubtotal = goldSpeciesSubtotal;
    }

    @ApiModelProperty(value="�� �� ��", notes="�� �� ��")
    public String getShoppingTicket() {
        return shoppingTicket;
    }

    public void setShoppingTicket(String shoppingTicket) {
        this.shoppingTicket = shoppingTicket;
    }

    @ApiModelProperty(value="�� �� �����z", notes="�� �� �����z")
    public double getShoppingTicketAmt() {
        return shoppingTicketAmt;
    }

    public void setShoppingTicketAmt(double shoppingTicketAmt) {
        this.shoppingTicketAmt = shoppingTicketAmt;
    }

    @ApiModelProperty(value="�M �t �g ��", notes="�M �t �g ��")
    public String getGiftVoucher() {
        return giftVoucher;
    }

    public void setGiftVoucher(String giftVoucher) {
        this.giftVoucher = giftVoucher;
    }

    @ApiModelProperty(value="�M �t �g �����z", notes="�M �t �g �����z")
    public double getGiftVoucherAmt() {
        return giftVoucherAmt;
    }

    public void setGiftVoucherAmt(double giftVoucherAmt) {
        this.giftVoucherAmt = giftVoucherAmt;
    }

    @ApiModelProperty(value="�� �� �O", notes="�� �� �O")
    public String getIsetan() {
        return isetan;
    }

    public void setIsetan(String isetan) {
        this.isetan = isetan;
    }

    @ApiModelProperty(value="�� �� �O���z", notes="�� �� �O���z")
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

    @ApiModelProperty(value="DC���z", notes="DC���z")
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

    @ApiModelProperty(value="AMEX���z", notes="AMEX���z")
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

    @ApiModelProperty(value="JCB���z", notes="JCB���z")
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

    @ApiModelProperty(value="Diners���z", notes="Diners���z")
    public double getDinersAmt() {
        return dinersAmt;
    }

    public void setDinersAmt(double dinersAmt) {
        this.dinersAmt = dinersAmt;
    }

    @ApiModelProperty(value="�O ����z", notes="�O ����z")
    public String getMitsuiLable() {
        return mitsuiLable;
    }

    public void setMitsuiLable(String mitsuiLable) {
        this.mitsuiLable = mitsuiLable;
    }

    @ApiModelProperty(value="�O ����z", notes="�O ����z")
    public double getMitsuiAmt() {
        return mitsuiAmt;
    }

    public void setMitsuiAmt(double mitsuiAmt) {
        this.mitsuiAmt = mitsuiAmt;
    }

    @ApiModelProperty(value="�y �� ��", notes="�y �� ��")
    public String getKaruizawa() {
        return karuizawa;
    }

    public void setKaruizawa(String karuizawa) {
        this.karuizawa = karuizawa;
    }

    @ApiModelProperty(value="�y �� ����z", notes="�y �� ����z")
    public double getKaruizawaAmt() {
        return karuizawaAmt;
    }

    public void setKaruizawaAmt(double karuizawaAmt) {
        this.karuizawaAmt = karuizawaAmt;
    }

    @ApiModelProperty(value="I�J�[�h", notes="I�J�[�h")
    public String getICard() {
        return ICard;
    }

    public void setICard(String iCard) {
        ICard = iCard;
    }

    @ApiModelProperty(value="I�J�[�h���z", notes="I�J�[�h���z")
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

    @ApiModelProperty(value="Chel 1000 ���z", notes="Chel 1000 ���z")
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

    @ApiModelProperty(value="Chel 2000���z", notes="Chel 2000���z")
    public double getChel2000Amt() {
        return chel2000Amt;
    }

    public void setChel2000Amt(double chel2000Amt) {
        this.chel2000Amt = chel2000Amt;
    }

    @ApiModelProperty(value="�v", notes="�v")
    public String getGiftCertificatesSubtotal() {
        return giftCertificatesSubtotal;
    }

    public void setGiftCertificatesSubtotal(String giftCertificatesSubtotal) {
        this.giftCertificatesSubtotal = giftCertificatesSubtotal;
    }

    @ApiModelProperty(value="�v���z", notes="�v���z")
    public double getGiftCertificatesSubtotalAmt() {
        return giftCertificatesSubtotalAmt;
    }

    public void setGiftCertificatesSubtotalAmt(
            double giftCertificatesSubtotalAmt) {
        this.giftCertificatesSubtotalAmt = giftCertificatesSubtotalAmt;
    }

    @ApiModelProperty(value="�� �� ��", notes="�� �� ��")
    public String getCollectedfunds() {
        return collectedfunds;
    }

    public void setCollectedfunds(String collectedfunds) {
        this.collectedfunds = collectedfunds;
    }

    @ApiModelProperty(value="���|�[�g���f��", notes="���|�[�g���f��")
    public double getCollectedfundAmt() {
        return collectedfundAmt;
    }

    public void setCollectedfundAmt(double collectedfundAmt) {
        this.collectedfundAmt = collectedfundAmt;
    }

    @ApiModelProperty(value="�� �� �����z", notes="�� �� �����z")
    public String getDiscontinuation() {
        return discontinuation;
    }

    public void setDiscontinuation(String discontinuation) {
        this.discontinuation = discontinuation;
    }

    @ApiModelProperty(value="�� �~ �� ���_��", notes="�� �~ �� ���_��")
    public long getDiscontinuationPoints() {
        return discontinuationPoints;
    }

    public void setDiscontinuationPoints(long discontinuationPoints) {
        this.discontinuationPoints = discontinuationPoints;
    }

    @ApiModelProperty(value="�� �� �� ��", notes="�� �� �� ��")
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @ApiModelProperty(value="�� �� �� ���_��", notes="�� �� �� ���_��")
    public long getExchangePoints() {
        return exchangePoints;
    }

    public void setExchangePoints(long exchangePoints) {
        this.exchangePoints = exchangePoints;
    }

    @ApiModelProperty(value="�� �� 200", notes="�� �� 200")
    public String getStamp200() {
        return stamp200;
    }

    public void setStamp200(String stamp200) {
        this.stamp200 = stamp200;
    }

    @ApiModelProperty(value="�� �� 200���z", notes="�� �� 200���z")
    public double getStamp200Amt() {
        return stamp200Amt;
    }

    public void setStamp200Amt(double stamp200Amt) {
        this.stamp200Amt = stamp200Amt;
    }

    @ApiModelProperty(value="�� �� 400", notes="�� �� 400")
    public String getStamp400() {
        return stamp400;
    }

    public void setStamp400(String stamp400) {
        this.stamp400 = stamp400;
    }

    @ApiModelProperty(value="�� �� 400���z", notes="�� �� 400���z")
    public double getStamp400Amt() {
        return stamp400Amt;
    }

    public void setStamp400Amt(double stamp400Amt) {
        this.stamp400Amt = stamp400Amt;
    }

    @ApiModelProperty(value="�� �� 600", notes="�� �� 600")
    public String getStamp600() {
        return stamp600;
    }

    public void setStamp600(String stamp600) {
        this.stamp600 = stamp600;
    }

    @ApiModelProperty(value="�� �� 600���z", notes="�� �� 600���z")
    public double getStamp600Amt() {
        return stamp600Amt;
    }

    public void setStamp600Amt(double stamp600Amt) {
        this.stamp600Amt = stamp600Amt;
    }

    @ApiModelProperty(value="�� �� 1,000", notes="�� �� 1,000")
    public String getStamp1000() {
        return stamp1000;
    }

    public void setStamp1000(String stamp1000) {
        this.stamp1000 = stamp1000;
    }

    @ApiModelProperty(value="�� �� 1,000���z", notes="�� �� 1,000���z")
    public double getStamp1000Amt() {
        return stamp1000Amt;
    }

    public void setStamp1000Amt(double stamp1000Amt) {
        this.stamp1000Amt = stamp1000Amt;
    }

    @ApiModelProperty(value="�� �� 2,000", notes="�� �� 2,000")
    public String getStamp2000() {
        return stamp2000;
    }

    public void setStamp2000(String stamp2000) {
        this.stamp2000 = stamp2000;
    }

    @ApiModelProperty(value="�� �� 2,000���z", notes="�� �� 2,000���z")
    public double getStamp2000Amt() {
        return stamp2000Amt;
    }

    public void setStamp2000Amt(double stamp2000Amt) {
        this.stamp2000Amt = stamp2000Amt;
    }

    @ApiModelProperty(value="�� �� 4,000", notes="�� �� 4,000")
    public String getStamp4000() {
        return stamp4000;
    }

    public void setStamp4000(String stamp4000) {
        this.stamp4000 = stamp4000;
    }

    @ApiModelProperty(value="�� �� 4,000���z", notes="�� �� 4,000���z")
    public double getStamp4000Amt() {
        return stamp4000Amt;
    }

    public void setStamp4000Amt(double stamp4000Amt) {
        this.stamp4000Amt = stamp4000Amt;
    }

    // 1.03 2014.12.25 MAJINHUI ��v���|�[�g�o�͂�Ή��@ADD END
    @ApiModelProperty(value="�W�v���t", notes="�W�v���t")
    public String getBusinessDayDate() {
        return businessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        this.businessDayDate = businessDayDate;
    }

    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    @ApiModelProperty(value="�X�ܔԍ�", notes="�X�ܔԍ�")
    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    @ApiModelProperty(value="���t", notes="���t")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    @ApiModelProperty(value="�\�����tID", notes="�\�����tID")
    public String getSubdateid2() {
		return subdateid2;
	}

	public void setSubdateid2(String subdateid2) {
		this.subdateid2 = subdateid2;
	}

	@ApiModelProperty(value="�\�����tID", notes="�\�����tID")
	public String getSubdateid1() {
        return subdateid1;
    }

    public void setSubdateid1(String subdateid1) {
        Calendar cal = getTime(subdateid1);
        int month = cal.get(Calendar.MONTH) + 1;
        this.subdateid1 = cal.get(Calendar.YEAR) + "-" + month + "-"
                + cal.get(Calendar.DATE);
    }

    @ApiModelProperty(value="�J�����_�[", notes="�J�����_�[")
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

    @ApiModelProperty(value="���M�Ǘ�����", notes="���M�Ǘ�����")
    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    @ApiModelProperty(value="�X�ܖ�", notes="�X�ܖ�")
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return rptlist
     */
    @ApiModelProperty(value="���|�[�g���X�g", notes="���|�[�g���X�g")
    public List<ItemMode> getRptlist() {
        return rptlist;
    }

    /**
     * @param rptlist �Z�b�g���� rptlist
     */
    public void setRptlist(List<ItemMode> rptlist) {
        this.rptlist = rptlist;
    }
    /* 1.02 2014.12.03 FENGSHA ����\��Ή� START*/
    /**
     * @return salesManNo
     */
    @ApiModelProperty(value="�̔��l���ԍ�", notes="�̔��l���ԍ�")
    public String getSalesManNo() {
        return salesManNo;
    }

    /**
     * @param salesManNo �Z�b�g���� salesManNo
     */
    public void setSalesManNo(String salesManNo) {
        this.salesManNo = salesManNo;
    }

    /**
     * @return operaterNo
     */
    @ApiModelProperty(value="�]�ƈ��ԍ�", notes="�]�ƈ��ԍ�")
    public String getOperaterNo() {
        return operaterNo;
    }

    /**
     * @param operaterNo �Z�b�g���� operaterNo
     */
    public void setOperaterNo(String operaterNo) {
        this.operaterNo = operaterNo;
    }

    /**
     * @return reportType
     */
    @ApiModelProperty(value="���|�[�g�^�C�v", notes="���|�[�g�^�C�v")
    public String getReportType() {
        return reportType;
    }

    /**
     * @param reportType �Z�b�g���� reportType
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
     * @param div �Z�b�g���� div
     */
    public void setDiv(String div) {
        this.div = div;
    }

    /**
     * @return storeidSearch
     */
    @ApiModelProperty(value="�X�ܔԍ�����", notes="�X�ܔԍ�����")
    public String getStoreidSearch() {
        return storeidSearch;
    }

    /**
     * @param storeidSearch �Z�b�g���� storeidSearch
     */
    public void setStoreidSearch(String storeidSearch) {
        this.storeidSearch = storeidSearch;
    }

    /**
     * @return salesManName
     */
    @ApiModelProperty(value="�̔��l�����O", notes="�̔��l�����O")
    public String getSalesManName() {
        return salesManName;
    }

    /**
     * @param salesManName �Z�b�g���� salesManName
     */
    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    /**
     * @return divName
     */
    @ApiModelProperty(value="div���O", notes="div���O")
    public String getDivName() {
        return divName;
    }

    /**
     * @param divName �Z�b�g���� divName
     */
    public void setDivName(String divName) {
        this.divName = divName;
    }

    /**
     * @return allGuestCount
     */
    @ApiModelProperty(value="�q���̍��v", notes="�q���̍��v")
    public long getAllGuestCount() {
        return AllGuestCount;
    }

    /**
     * @param allGuestCount �Z�b�g���� allGuestCount
     */
    public void setAllGuestCount(long allGuestCount) {
        AllGuestCount = allGuestCount;
    }

    /**
     * @return allItemCount
     */
    @ApiModelProperty(value="�_���̍��v", notes="�_���̍��v")
    public long getAllItemCount() {
        return AllItemCount;
    }

    /**
     * @param allItemCount �Z�b�g���� allItemCount
     */
    public void setAllItemCount(long allItemCount) {
        AllItemCount = allItemCount;
    }

    /**
     * @return allSalesAmount
     */
    @ApiModelProperty(value="���z�̍��v", notes="���z�̍��v")
    public double getAllSalesAmount() {
        return AllSalesAmount;
    }

    /**
     * @param allSalesAmount �Z�b�g���� allSalesAmount
     */
    
    public void setAllSalesAmount(double allSalesAmount) {
        AllSalesAmount = allSalesAmount;
    }

    @ApiModelProperty(value="POSLog�̃V�X�e������", notes="POSLog�̃V�X�e������")
	public String getBegindatetime() {
		return begindatetime;
	}

	public void setBegindatetime(String begindatetime) {
		this.begindatetime = begindatetime;
	}

	@ApiModelProperty(value="���ߋ�", notes="���ߋ�")
	public String getModorikinn() {
		return modorikinn;
	}

	public void setModorikinn(String modorikinn) {
		this.modorikinn = modorikinn;
	}

	@ApiModelProperty(value="���ߋ����z", notes="���ߋ����z")
	public double getModorikinnAmt() {
		return modorikinnAmt;
	}

	public void setModorikinnAmt(double modorikinnAmt) {
		this.modorikinnAmt = modorikinnAmt;
	}

	@ApiModelProperty(value="���̑�", notes="���̑�")
	public String getSonota() {
		return sonota;
	}

	public void setSonota(String sonota) {
		this.sonota = sonota;
	}

	@ApiModelProperty(value="���̑����z", notes="���̑����z")
	public double getSonotaAmt() {
		return sonotaAmt;
	}

	public void setSonotaAmt(double sonotaAmt) {
		this.sonotaAmt = sonotaAmt;
	}

    /* 1.02 2014.12.03 FENGSHA ����\��Ή� END*/

}
