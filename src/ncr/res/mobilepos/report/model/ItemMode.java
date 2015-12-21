package ncr.res.mobilepos.report.model;
/**
 * ���藚��
 * �o�[�W����      ������t      �S���Җ�        ������e
 * 1.01            2014.10.21    FENGSHA         ���|�[�g�o�͂�Ή�
 */
public class ItemMode {

    /** Division�ݒ� flag */
    private String isHeaderFlag;

    /** ���ԑсA�q���A�_���A���z�l�ݒ�flag */
    private String isBodyFlag;

    /** �q���A�_���A���z���v�l�ݒ� flag */
    private String isFooterFlag;

    /** column name **/
    private String ItemName;

    /** �q�� **/
    private long GuestCnt;

    /** �_�� **/
    private long ItemCnt;

    /** ���z **/
    private double SalesAmt;

    /** Division name **/
    private String divisionName;

    /** �q�����v **/
    private long GuestCntSum;

    /** �_�����v **/
    private long ItemCntSum;

    /** ���z���v **/
    private double SalesAmtSum;

    /** column code **/
    private String ItemCode;


    /**
     * @return itemCode
     */
    public String getItemCode() {
        return ItemCode;
    }
    /**
     * @param itemCode �Z�b�g���� itemCode
     */
    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }
    /**
     * @return itemName
     */
    public String getItemName() {
        return ItemName;
    }
    /**
     * @param itemName �Z�b�g���� itemName
     */
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    /**
     * @return guestCnt
     */
    public long getGuestCnt() {
        return GuestCnt;
    }
    /**
     * @param guestCnt �Z�b�g���� guestCnt
     */
    public void setGuestCnt(long guestCnt) {
        GuestCnt = guestCnt;
    }
    /**
     * @return itemCnt
     */
    public long getItemCnt() {
        return ItemCnt;
    }
    /**
     * @param itemCnt �Z�b�g���� itemCnt
     */
    public void setItemCnt(long itemCnt) {
        ItemCnt = itemCnt;
    }
    /**
     * @return salesAmt
     */
    public double getSalesAmt() {
        return SalesAmt;
    }
    /**
     * @param salesAmt �Z�b�g���� salesAmt
     */
    public void setSalesAmt(double salesAmt) {
        SalesAmt = salesAmt;
    }
    /**
     * @return isHeaderFlag
     */
    public String getIsHeaderFlag() {
        return isHeaderFlag;
    }
    /**
     * @param isHeaderFlag �Z�b�g���� isHeaderFlag
     */
    public void setIsHeaderFlag(String isHeaderFlag) {
        this.isHeaderFlag = isHeaderFlag;
    }
    /**
     * @return guestCntSum
     */
    public long getGuestCntSum() {
        return GuestCntSum;
    }
    /**
     * @param guestCntSum �Z�b�g���� guestCntSum
     */
    public void setGuestCntSum(long guestCntSum) {
        GuestCntSum = guestCntSum;
    }
    /**
     * @return itemCntSum
     */
    public long getItemCntSum() {
        return ItemCntSum;
    }
    /**
     * @param itemCntSum �Z�b�g���� itemCntSum
     */
    public void setItemCntSum(long itemCntSum) {
        ItemCntSum = itemCntSum;
    }
    /**
     * @return salesAmtSum
     */
    public double getSalesAmtSum() {
        return SalesAmtSum;
    }
    /**
     * @param salesAmtSum �Z�b�g���� salesAmtSum
     */
    public void setSalesAmtSum(double salesAmtSum) {
        SalesAmtSum = salesAmtSum;
    }
    /**
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @param divisionName �Z�b�g���� divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /**
     * @return isFooterFlag
     */
    public String getIsFooterFlag() {
        return isFooterFlag;
    }
    /**
     * @param isFooterFlag �Z�b�g���� isFooterFlag
     */
    public void setIsFooterFlag(String isFooterFlag) {
        this.isFooterFlag = isFooterFlag;
    }
    /**
     * @return isBodyFlag
     */
    public String getIsBodyFlag() {
        return isBodyFlag;
    }
    /**
     * @param isBodyFlag �Z�b�g���� isBodyFlag
     */
    public void setIsBodyFlag(String isBodyFlag) {
        this.isBodyFlag = isBodyFlag;
    }
}
