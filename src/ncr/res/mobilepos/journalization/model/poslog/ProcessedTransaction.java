package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ProcessedTransaction")
public class ProcessedTransaction {

    /**
     * 元運用日付                                                                                        
     */
    @XmlElement(name = "BusinessDayDateFrom")
    private String businessDayDateFrom;
	
    /**
     * 元運用時刻                                                                                        
     */
    @XmlElement(name = "BusinessDayDateTimeFrom")
    private String businessDayDateTimeFrom;	
	
    /**
     * 元会社コード                                                                                       
     */
    @XmlElement(name = "CompanyIdFrom")
    private String companyIdFrom;
    /**
     * 元会社コード(端末)                                                                                       
     */
    @XmlElement(name = "TerminalCompanyIdFrom")
    private String terminalCompanyIdFrom;
    /**
     * 
     * 元組織コード                                                                                       
     */
    @XmlElement(name = "DivisionIdFrom")
    private String divisionIdFrom;
    /**
     * 
     */
    @XmlElement(name = "StoreIdFrom")
    private String storeIdFrom;
    /**
     * 
     */
    @XmlElement(name = "TerminalIdFrom")
    private String terminalIdFrom;
    /**
     * 
     */
    @XmlElement(name = "TransactionIdFrom")
    private String transactionIdFrom;
    /**
     * 
     */
    @XmlElement(name = "SalesAmount")
    private int salesAmount;
    
    @XmlElement(name = "SalesAmountByRank")
    private int salesAmountByRank;

	@XmlElement(name = "ItemAmount")
    private int itemAmount;

	/**
     * 
     */
    @XmlElement(name = "AmountForPoints")
    private int amountForPoints;
    /**
     * 
     */
    @XmlElement(name = "CorrectionPoints")
    private int correctionPoints;
    
    @XmlElement(name = "LostPoints")
    private int lostPoints;

    @XmlElement(name = "Points")
    private int points;
    /**
     * 
     */
    @XmlElement(name = "BasicPoints")
    private int basicPoints;
    /**
     * 
     */
    @XmlElement(name = "TermPoints")
    private int termPoints;
    /**
     * 
     */
    @XmlElement(name = "BonusPoints")
    private int bonusPoints;
    /**
     * 
     */
    @XmlElement(name = "PointsGeneratedTotal")
    private int pointsGeneratedTotal;
    
    @XmlElement(name = "OperatorIDFrom")
    private String operatorIdFrom;
    
    @XmlElement(name = "SalesQuantityByRank")
    private int salesQtyByRank;
    
    @XmlElement(name = "PointsLater")
    private int pointsLater;
    
    /**
     * @return the operatorIdFrom
     */
    public String getOperatorIdFrom() {
        return operatorIdFrom;
    }
    /**
     * @param operatorIdFrom the operatorIdFrom to set
     */
    public void setOperatorIdFrom(String operatorIdFrom) {
        this.operatorIdFrom = operatorIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getBusinessDayDateFrom() {
        return businessDayDateFrom;
    }
    /**
     * 
     * @param businessDayDateFrom
     */
    public void setBusinessDayDateFrom(String businessDayDateFrom) {
        this.businessDayDateFrom = businessDayDateFrom;
    }

    /**
     * 
     * @return
     */
    public String getBusinessDayDateTimeFrom() {
        return businessDayDateTimeFrom;
    }
    /**
     * 
     * @param businessDayDateFrom
     */
    public void setBusinessDayDateTimeFrom(String businessDayDateTimeFrom) {
        this.businessDayDateTimeFrom = businessDayDateTimeFrom;
    }
    /**
     * 
     * @return
     */
    public String getCompanyIdFrom() {
        return companyIdFrom;
    }
    /**
     * 
     * @param companyIdFrom
     */
    public void setCompanyIdFrom(String companyIdFrom) {
        this.companyIdFrom = companyIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getTerminalCompanyIdFrom() {
        return terminalCompanyIdFrom;
    }
    /**
     * 
     * @param terminalCompanyIdFrom
     */
    public void setTerminalCompanyIdFrom(String terminalCompanyIdFrom) {
        this.terminalCompanyIdFrom = terminalCompanyIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getDivisionIdFrom() {
        return divisionIdFrom;
    }
    /**
     * 
     * @param divisionIdFrom
     */
    public void setDivisionIdFrom(String divisionIdFrom) {
        this.divisionIdFrom = divisionIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getStoreIdFrom() {
        return storeIdFrom;
    }
    /**
     * 
     * @param storeIdFrom
     */
    public void setStoreIdFrom(String storeIdFrom) {
        this.storeIdFrom = storeIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getTerminalIdFrom() {
        return terminalIdFrom;
    }
    /**
     * 
     * @param terminalIdFrom
     */
    public void setTerminalIdFrom(String terminalIdFrom) {
        this.terminalIdFrom = terminalIdFrom;
    }
    /**
     * 
     * @return
     */
    public String getTransactionIdFrom() {
        return transactionIdFrom;
    }
    /**
     * 
     * @param transactionIdFrom
     */
    public void setTransactionIdFrom(String transactionIdFrom) {
        this.transactionIdFrom = transactionIdFrom;
    }
    /**
     * 
     * @return
     */
    public int getSalesAmount() {
        return salesAmount;
    }
    /**
     * 
     * @param salesAmount
     */
    public void setSalesAmount(int salesAmount) {
        this.salesAmount = salesAmount;
    }
    
    public int getSalesAmountByRank() {
        return salesAmountByRank;
    }
    
    public void setSalesAmountByRank(int salesAmountByRank) {
        this.salesAmountByRank = salesAmountByRank;
    }
    public int getItemAmount() {
        return itemAmount;
    }
    /**
     * 
     * @param itemAmount
     */
    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }
    /**
     * 
     * @return
     */
    public int getAmountForPoints() {
        return amountForPoints;
    }
    /**
     * 
     * @param amountForPoints
     */
    public void setAmountForPoints(int amountForPoints) {
        this.amountForPoints = amountForPoints;
    }

    public int getCorrectionPoints() {
        return correctionPoints;
    }

    public void setCorrectionPoints(int correctionPoints) {
        this.correctionPoints = correctionPoints;
    }
    
    public int getLostPoints() {
        return lostPoints;
    }

    public void setLostPoints(int lostPoints) {
        this.lostPoints = lostPoints;
    }

    /**
     * 
     * @return
     */
    public int getPoints() {
        return points;
    }
    /**
     * 
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }
    /**
     * 
     * @return
     */
    public int getBasicPoints() {
        return basicPoints;
    }
    /**
     * 
     * @param basicPoints
     */
    public void setBasicPoints(int basicPoints) {
        this.basicPoints = basicPoints;
    }
    /**
     * 
     * @return
     */
    public int getTermPoints() {
        return termPoints;
    }
    /**
     * 
     * @param termPoints
     */
    public void setTermPoints(int termPoints) {
        this.termPoints = termPoints;
    }
    /**
     * 
     * @return
     */
    public int getBonusPoints() {
        return bonusPoints;
    }
    /**
     * 
     * @param bonusPoints
     */
    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
    /**
     * 
     * @return
     */
    public int getPointsGeneratedTotal() {
        return pointsGeneratedTotal;
    }
    /**
     * 
     * @param pointsGeneratedTotal
     */
    public void setPointsGeneratedTotal(int pointsGeneratedTotal) {
        this.pointsGeneratedTotal = pointsGeneratedTotal;
    }
    
    
    public int getSalesQtyByRank() {
		return salesQtyByRank;
	}
    
	public void setSalesQtyByRank(int salesQtyByRank) {
		this.salesQtyByRank = salesQtyByRank;
	}
	
	public int getPointsLater() {
		return pointsLater;
	}
	
	public void setPointsLater(int pointsLater) {
		this.pointsLater = pointsLater;
	}
	
	@Override
    public String toString() {
        return "ProcessedTransaction [businessDayDateFrom=" + businessDayDateFrom + ", terminalCompanyIdFrom="
                + terminalCompanyIdFrom + ", divisionIdFrom=" + divisionIdFrom + ", storeIdFrom=" + storeIdFrom
                + ", terminalIdFrom=" + terminalIdFrom + ", transactionIdFrom=" + transactionIdFrom + ", salesAmount="
                + salesAmount + ", amountForPoints=" + amountForPoints + ", points=" + points + ", basicPoints="
                + basicPoints + ", termPoints=" + termPoints + ", bonusPoints=" + bonusPoints
                + ", pointsGeneratedTotal=" + pointsGeneratedTotal + "]";
    }
    
}
