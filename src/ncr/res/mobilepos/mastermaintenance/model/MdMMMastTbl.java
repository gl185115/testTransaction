package ncr.res.mobilepos.mastermaintenance.model;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

/**
 * MD_MMMAST_TBL Model Object.
 *
 * Encapsulates the MD_MMMAST_TBL TABLE information.
 *
 */
@CsvDataType
public class MdMMMastTbl extends MaintenanceTbl {
    /** The MixMatch Default value for mustbuyflag **/
    private static final String MUST_BUY_FLAG = "0";
    /** The MixMatch storeid. **/
    @CsvField(pos = 1, noValue = "NULL")
    private String storeId;
    /** The MixMatch Code. **/
    @CsvField(pos = 2, noValue = "NULL")
    private String mmCode;
    /** The MixMatch start day. **/
    @CsvField(pos = 3, noValue = "NULL")
    private String mmStartDateId;
    /** The MixMatch end day. **/
    @CsvField(pos = 4, noValue = "NULL")
    private String mmEndDateId;
    /** The MixMatch type. **/
    @CsvField(pos = 5, noValue = "NULL")
    private String mmType;
    /** The MixMatch price multi 1. **/
    @CsvField(pos = 6, noValue = "NULL")
    private String priceMulti1;
    /** The MixMatch discount price 1. **/
    @CsvField(pos = 7, noValue = "NULL")
    private String discountPrice1;
    /** The MixMatch employee price 11. **/
    @CsvField(pos = 8, noValue = "NULL")
    private String empPrice11;
    /** The MixMatch employee price 12. **/
    @CsvField(pos = 9, noValue = "NULL")
    private String empPrice12;
    /** The MixMatch employee preice 13. **/
    @CsvField(pos = 10, noValue = "NULL")
    private String empPrice13;
    /** The MixMatch price multi 2. **/
    @CsvField(pos = 11, noValue = "NULL")
    private String priceMulti2;
    /** The MixMatch discount price 2. **/
    @CsvField(pos = 12, noValue = "NULL")
    private String discountPrice2;
    /** The MixMatch employee price 21. **/
    @CsvField(pos = 13, noValue = "NULL")
    private String empPrice21;
    /** The MixMatch employee price 22. **/
    @CsvField(pos = 14, noValue = "NULL")
    private String empPrice22;
    /** The MixMatch employee price 23. **/
    @CsvField(pos = 15, noValue = "NULL")
    private String empPrice23;
    /** The MixMatch name. **/
    @CsvField(pos = 16, noValue = "NULL")
    private String mmName;
    /** The MixMatch must buy flag. **/
    @CsvField(pos = 23, noValue = "NULL")
    private String mustbuyflag;
    /**
     * @return the mmCode
     */
    public final String getStoreId() {
        return storeId;
    }
    /**
     * @param storeIdSet the storeId to set
     */
    public final void setStoreId(final String storeIdSet) {
        this.storeId = storeIdSet;
    }
    /**
     * @return the mmCode
     */
    public final String getMmCode() {
        return mmCode;
    }
    /**
     * @param mmCodeSet the mmCode to set
     */
    public final void setMmCode(final String mmCodeSet) {
        this.mmCode = mmCodeSet;
    }
    /**
     * @return the mmStartDateId
     */
    public final String getMmStartDateId() {
        return mmStartDateId;
    }
    /**
     * @param mmStartDateSet the mmStartDateId to set
     */
    public final void setMnStartDateId(final String mmStartDateSet) {
        this.mmStartDateId = mmStartDateSet;
    }
    /**
     * @return the mmEndDateId
     */
    public final String getMmEndDateId() {
        return mmEndDateId;
    }
    /**
     * @param mmEndDateIdSet the mmEndDateId to set
     */
    public final void setMmEndDateId(final String mmEndDateIdSet) {
        this.mmEndDateId = mmEndDateIdSet;
    }
    /**
     * @return the mmType
     */
    public final String getMmType() {
        return mmType;
    }
    /**
     * @param mmTypeSet the mmType to set
     */
    public final void setMmType(final String mmTypeSet) {
        this.mmType = mmTypeSet;
    }
    /**
     * @return the priceMulti1
     */
    public final String getPriceMulti1() {
        return priceMulti1;
    }
    /**
     * @param priceMultiSet the priceMulti1 to set
     */
    public final void setPriceMulti1(final String priceMultiSet) {
        this.priceMulti1 = priceMultiSet;
    }
    /**
     * @return the discountPrice1
     */
    public final String getDiscountPrice1() {
        return discountPrice1;
    }
    /**
     * @param discountPriceSet the discountPrice1 to set
     */
    public final void setDiscountPrice1(final String discountPriceSet) {
        this.discountPrice1 = discountPriceSet;
    }
    /**
     * @return the empPrice11
     */
    public final String getEmpPrice11() {
        return empPrice11;
    }
    /**
     * @param empPriceSet the empPrice11 to set
     */
    public final void setEmpPrice11(final String empPriceSet) {
        this.empPrice11 = empPriceSet;
    }
    /**
     * @return the empPrice12
     */
    public final String getEmpPrice12() {
        return empPrice12;
    }
    /**
     * @param empPriceSet the empPrice12 to set
     */
    public final void setEmpPrice12(final String empPriceSet) {
        this.empPrice12 = empPriceSet;
    }
    /**
     * @return the empPrice13
     */
    public final String getEmpPrice13() {
        return empPrice13;
    }
    /**
     * @param empPriceSet the empPrice13 to set
     */
    public final void setEmpPrice13(final String empPriceSet) {
        this.empPrice13 = empPriceSet;
    }
    /**
     * @return the priceMulti2
     */
    public final String getPriceMulti2() {
        return priceMulti2;
    }
    /**
     * @param empPriceMultiSet the priceMulti2 to set
     */
    public final void setPriceMulti2(final String empPriceMultiSet) {
        this.priceMulti2 = empPriceMultiSet;
    }
    /**
     * @return the discountPrice2
     */
    public final String getDiscountPrice2() {
        return discountPrice2;
    }
    /**
     * @param discountPriceSet the discountPrice2 to set
     */
    public final void setDiscountPrice2(final String discountPriceSet) {
        this.discountPrice2 = discountPriceSet;
    }
    /**
     * @return the empPrice21
     */
    public final String getEmpPrice21() {
        return empPrice21;
    }
    /**
     * @param empPriceSet the empPrice21 to set
     */
    public final void setEmpPrice21(final String empPriceSet) {
        this.empPrice21 = empPriceSet;
    }
    /**
     * @return the empPrice22
     */
    public final String getEmpPrice22() {
        return empPrice22;
    }
    /**
     * @param empPriceSet the empPrice22 to set
     */
    public final void setEmpPrice22(final String empPriceSet) {
        this.empPrice22 = empPriceSet;
    }
    /**
     * @return the empPrice23
     */
    public final String getEmpPrice23() {
        return empPrice23;
    }
    /**
     * @param empPriceSet the empPrice23 to set
     */
    public final void setEmpPrice23(final String empPriceSet) {
        this.empPrice23 = empPriceSet;
    }
    /**
     * @return the mmName
     */
    public final String getMmName() {
        return mmName;
    }
    /**
     * @param mmNameSet the mmName to set
     */
    public final void setMmName(final String mmNameSet) {
        this.mmName = mmNameSet;
    }
    /**
     * @return the mustbuyflag
     */
    public final String getMustBuyFlag() {
        try {
            Integer.parseInt(this.mustbuyflag);
        } catch (Exception e) {
            return "0";
        }
        return mustbuyflag;
    }
    /**
     * @param mustbuyflagSet the mustbuyflag to set
     */
    public final void setMustBuyFlag(final String mustbuyflagSet) {
        if (null == mustbuyflagSet) {
            this.mustbuyflag = MUST_BUY_FLAG;
        } else {
            this.mustbuyflag = mustbuyflagSet;
        }
    }
}
