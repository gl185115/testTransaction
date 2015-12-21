package ncr.res.mobilepos.mastermaintenance.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

/**
 * MD_MAST_TBL Model Object.
 *
 * Encapsulates the MD_MAST_TBL TABLE information.
 *
 */
@CsvDataType
public class MdMastTbl extends MaintenanceTbl {
    /**
    * The MD_MAST_TBL StoreID.
    */
    @CsvField(pos = 1, noValue = "NULL")
    private String storeid;
    /**
     * The MD_MAST_TBL PLU.
     */
    @CsvField(pos = 2, noValue = "NULL")
    private String plu;
    /**
     * The MD_MAST_TBL MDType.
     */
    @CsvField(pos = 3, noValue = "NULL")
    private String mdType;
    /**
     * The MD_MAST_TBL mdInternal.
     */
    @CsvField(pos = 4, noValue = "NULL")
    private String mdInternal;
    /**
     * The MD_MAST_TBL MDVender.
     */
    @CsvField(pos = 5, noValue = "NULL")
    private String mdVender;
    /**
     * The MD_MAST_TBL Department.
     */
    @CsvField(pos = 6, noValue = "NULL")
    private String division;
    /**
     * The MD_MAST_TBL Class.
     */
    @CsvField(pos = 7, noValue = "NULL")
    private String category;
    /**
     * The MD_MAST_TBL Line.
     */
    @CsvField(pos = 8, noValue = "NULL")
    private String brand;
    /**
     * The MD_MAST_TBL SKU.
     */
    @CsvField(pos = 9, noValue = "NULL")
    private String sku;
    /**
     * The MD_MAST_TBL ItemSize.
     */
    @CsvField(pos = 10, noValue = "NULL")
    private String size;
    /**
     * The MD_MAST_TBL KeyPlu.
     */
    @CsvField(pos = 11, noValue = "NULL")
    private String keyPlu;
    /**
     * The MD_MAST_TBL MD01.
     */
    @CsvField(pos = 12, noValue = "NULL")
    private String md1;
    /**
     * The MD_MAST_TBL MD02.
     */
    @CsvField(pos = 13, noValue = "NULL")
    private String md2;
    /**
     * The MD_MAST_TBL MD03.
     */
    @CsvField(pos = 14, noValue = "NULL")
    private String md3;
    /**
     * The MD_MAST_TBL MD04.
     */
    @CsvField(pos = 15, noValue = "NULL")
    private String md4;
    /**
     * The MD_MAST_TBL MD05.
     */
    @CsvField(pos = 16, noValue = "NULL")
    private String md5;
    /**
     * The MD_MAST_TBL MD06.
     */
    @CsvField(pos = 17, noValue = "NULL")
    private String md6;
    /**
     * The MD_MAST_TBL MD07.
     */
    @CsvField(pos = 18, noValue = "NULL")
    private String md7;
    /**
     * The MD_MAST_TBL MD08.
     */
    @CsvField(pos = 19, noValue = "NULL")
    private String md8;
    /**
     * The MD_MAST_TBL MD09.
     */
    @CsvField(pos = 20, noValue = "NULL")
    private String md9;
    /**
     * The MD_MAST_TBL MD10.
     */
    @CsvField(pos = 21, noValue = "NULL")
    private String md10;
    /**
     * The MD_MAST_TBL MdName.
     */
    @CsvField(pos = 22, noValue = "NULL")
    private String mdName;
    /**
     * The MD_MAST_TBL MdName1.
     */
    @CsvField(pos = 23, noValue = "NULL")
    private String mdName1;
    /**
     * The MD_MAST_TBL MdName2.
     */
    @CsvField(pos = 24, noValue = "NULL")
    private String mdName2;
    /**
     * The MD_MAST_TBL MdKanaName.
     */
    @CsvField(pos = 25, noValue = "NULL")
    private String mdKanaName;
    /**
     * The MD_MAST_TBL MdKanaName1.
     */
    @CsvField(pos = 26, noValue = "NULL")
    private String mdKanaName1;
    /**
     * The MD_MAST_TBL MdKanaName2.
     */
    @CsvField(pos = 27, noValue = "NULL")
    private String mdKanaName2;
    /**
     * The MD_MAST_TBL mdShortName.
    */
    @CsvField(pos = 28, noValue = "NULL")
    private String mdShortName;
    /**
    * The MD_MAST_TBL OrgSalesPrice1.
    */
    @CsvField(pos = 29, noValue = "NULL")
    private String orgSalesPrice1;
    /**
    * The MD_MAST_TBL SalesPrice1.
    */
    @CsvField(pos = 30, noValue = "NULL")
    private String salesPrice1;
    /**
    * The MD_MAST_TBL SalesPrice2.
    */
    @CsvField(pos = 31, noValue = "NULL")
    private String salesPrice2;
    /**
    * The MD_MAST_TBL SalesPriceChgDate1.
    */
    @CsvField(pos = 32, noValue = "NULL")
    private String salesPriceChgDate1;
    /**
    * The MD_MAST_TBL SalesPriceChgDate2.
    */
    @CsvField(pos = 33, noValue = "NULL")
    private String salesPriceChgDate2;
    /**
    * The MD_MAST_TBL EmpPrice1.
    */
    @CsvField(pos = 34, noValue = "NULL")
    private String empPrice1;
    /**
    * The MD_MAST_TBL EmpPrice2.
    */
    @CsvField(pos = 35, noValue = "NULL")
    private String empPrice2;
    /**
    * The MD_MAST_TBL EmpPrice3.
    */
    @CsvField(pos = 36, noValue = "NULL")
    private String empPrice3;
    /**
    * The MD_MAST_TBL PuPrice1.
    */
    @CsvField(pos = 37, noValue = "NULL")
    private String puPrice1;
    /**
    * The MD_MAST_TBL PuPrice2.
    */
    @CsvField(pos = 38, noValue = "NULL")
    private String puPrice2;
    /**
    * The MD_MAST_TBL PuPriceChgDate1.
    */
    @CsvField(pos = 39, noValue = "NULL")
    private String puPriceChgDate1;
    /**
    * The MD_MAST_TBL PuPriceChgDate2.
    */
    @CsvField(pos = 40, noValue = "NULL")
    private String puPriceChgDate2;
    /**
    * The MD_MAST_TBL OrgCostPrice1.
    */
    @CsvField(pos = 41, noValue = "NULL")
    private String orgCostPrice1;
    /**
    * The MD_MAST_TBL CostPrice1.
    */
    @CsvField(pos = 42, noValue = "NULL")
    private String costPrice1;
    /**
    * The MD_MAST_TBL CostPrice2.
    */
    @CsvField(pos = 43, noValue = "NULL")
    private String costPrice2;
    /**
    * The MD_MAST_TBL CostPriceChgDate1.
    */
    @CsvField(pos = 44, noValue = "NULL")
    private String costPriceChgDate1;
    /**
    * The MD_MAST_TBL CostPriceChgDate2.
    */
    @CsvField(pos = 45, noValue = "NULL")
    private String costPriceChgDate2;
    /**
    * The MD_MAST_TBL SalesDate.
    */
    @CsvField(pos = 46, noValue = "NULL")
    private String salesDate;
    /**
    * The MD_MAST_TBL MakerPrice.
    */
    @CsvField(pos = 47, noValue = "NULL")
    private String makerPrice;
    /**
    * The MD_MAST_TBL TaxType.
    */
    @CsvField(pos = 48, noValue = "NULL")
    private String taxType;
    /**
    * The MD_MAST_TBL DiscountType.
    */
    @CsvField(pos = 49, noValue = "NULL")
    private String discountType;
    /**
    * The MD_MAST_TBL SeasonType.
    */
    @CsvField(pos = 50, noValue = "NULL")
    private String seasonType;
    /**
    * The MD_MAST_TBL PaymentType.
    */
    @CsvField(pos = 51, noValue = "NULL")
    private String paymentType;
    /**
    * The MD_MAST_TBL OrderType.
    */
    @CsvField(pos = 52, noValue = "NULL")
    private String orderType;
    /**
    * The MD_MAST_TBL PosMdType.
    */
    @CsvField(pos = 53, noValue = "NULL")
    private String posMdType;
    /**
    * The MD_MAST_TBL CatType.
    */
    @CsvField(pos = 54, noValue = "NULL")
    private String catType;
    /**
    * The MD_MAST_TBL OrderUnit.
    */
    @CsvField(pos = 55, noValue = "NULL")
    private String orderUnit;
    /**
    * The MD_MAST_TBL OrderPoint.
    */
    @CsvField(pos = 56, noValue = "NULL")
    private String orderPoint;
    /**
    * The MD_MAST_TBL BaseStockCnt.
    */
    @CsvField(pos = 57, noValue = "NULL")
    private String baseStockCnt;
    /**
    * The MD_MAST_TBL Conn1.
    */
    @CsvField(pos = 58, noValue = "NULL")
    private String conn1;
    /**
    * The MD_MAST_TBL ConnType1.
    */
    @CsvField(pos = 59, noValue = "NULL")
    private String connType1;
    /**
    * The MD_MAST_TBL Conn2.
    */
    @CsvField(pos = 60, noValue = "NULL")
    private String conn2;
    /**
    * The MD_MAST_TBL ConnType2.
    */
    @CsvField(pos = 61, noValue = "NULL")
    private String connType2;
    /**
    * The MD_MAST_TBL VenderCode.
    */
    @CsvField(pos = 62, noValue = "NULL")
    private String venderCode;
    /**
    * The MD_MAST_TBL VenderType.
    */
    @CsvField(pos = 63, noValue = "NULL")
    private String venderType;
    /**
    * The MD_MAST_TBL TagType.
    */
    @CsvField(pos = 64, noValue = "NULL")
    private String tagType;
    /**
    * The MD_MAST_TBL TagCode1.
    */
    @CsvField(pos = 65, noValue = "NULL")
    private String tagCode1;
    /**
    * The MD_MAST_TBL TagCode2.
    */
    @CsvField(pos = 66, noValue = "NULL")
    private String tagCode2;
    /**
    * The MD_MAST_TBL TagCode3.
    */
    @CsvField(pos = 67, noValue = "NULL")
    private String tagCode3;
    /**
    * The MD_MAST_TBL PointRate.
    */
    @CsvField(pos = 68, noValue = "NULL")
    private String pointRate;
    /**
    * The MD_MAST_TBL PictureFileName1.
    */
    @CsvField(pos = 69, noValue = "NULL")
    private String pictureFileName1;
    /**
    * The MD_MAST_TBL PictureFileName2.
    */
    @CsvField(pos = 70, noValue = "NULL")
    private String pictureFileName2;
    /**
    * The MD_MAST_TBL SubMoney1.
    */
    @CsvField(pos = 71, noValue = "NULL")
    private String subMoney1;
    /**
    * The MD_MAST_TBL SubMoney2.
    */
    @CsvField(pos = 72, noValue = "NULL")
    private String subMoney2;
    /**
    * The MD_MAST_TBL SubMoney3.
    */
    @CsvField(pos = 73, noValue = "NULL")
    private String subMoney3;
    /**
    * The MD_MAST_TBL SubMoney4.
    */
    @CsvField(pos = 74, noValue = "NULL")
    private String subMoney4;
    /**
    * The MD_MAST_TBL SubMoney5.
    */
    @CsvField(pos = 75, noValue = "NULL")
    private String subMoney5;
    /**
    * The MD_MAST_TBL SubCode1.
    */
    @CsvField(pos = 76, noValue = "NULL")
    private String subCode1;
    /**
    * The MD_MAST_TBL SubCode2.
    */
    @CsvField(pos = 77, noValue = "NULL")
    private String subCode2;
    /**
    * The MD_MAST_TBL SubCode3.
    */
    @CsvField(pos = 78, noValue = "NULL")
    private String subCode3;
    /**
    * The MD_MAST_TBL SubCode4.
    */
    @CsvField(pos = 79, noValue = "NULL")
    private String subCode4;
    /**
    * The MD_MAST_TBL SubCode5.
    */
    @CsvField(pos = 80, noValue = "NULL")
    private String subCode5;
    /**
    * The MD_MAST_TBL SubCode6.
    */
    @CsvField(pos = 81, noValue = "NULL")
    private String subCode6;
    /**
    * The MD_MAST_TBL SubCode7.
    */
    @CsvField(pos = 82, noValue = "NULL")
    private String subCode7;
    /**
    * The MD_MAST_TBL SubCode8.
    */
    @CsvField(pos = 83, noValue = "NULL")
    private String subCode8;
    /**
    * The MD_MAST_TBL SubCode9.
    */
    @CsvField(pos = 84, noValue = "NULL")
    private String subCode9;
    /**
    * The MD_MAST_TBL SubCode10.
    */
    @CsvField(pos = 85, noValue = "NULL")
    private String subCode10;
    /**
    * The MD_MAST_TBL SubTinyInt1.
    */
    @CsvField(pos = 86, noValue = "NULL")
    private String subTinyInt1;
    /**
    * The MD_MAST_TBL SubTinyInt2.
    */
    @CsvField(pos = 87, noValue = "NULL")
    private String subTinyInt2;
    /**
    * The MD_MAST_TBL SubTinyInt3.
    */
    @CsvField(pos = 88, noValue = "NULL")
    private String subTinyInt3;
    /**
    * The MD_MAST_TBL SubTinyInt4.
    */
    @CsvField(pos = 89, noValue = "NULL")
    private String subTinyInt4;
    /**
    * The MD_MAST_TBL SubTinyInt5.
    */
    @CsvField(pos = 90, noValue = "NULL")
    private String subTinyInt5;
    /**
    * The MD_MAST_TBL SubTinyInt6.
    */
    @CsvField(pos = 91, noValue = "NULL")
    private String subTinyInt6;
    /**
    * The MD_MAST_TBL SubTinyInt7.
    */
    @CsvField(pos = 92, noValue = "NULL")
    private String subTinyInt7;
    /**
    * The MD_MAST_TBL SubTinyInt8.
    */
    @CsvField(pos = 93, noValue = "NULL")
    private String subTinyInt8;
    /**
    * The MD_MAST_TBL SubTinyInt9.
    */
    @CsvField(pos = 94, noValue = "NULL")
    private String subTinyInt9;
    /**
    * The MD_MAST_TBL SubInt10.
    */
    @CsvField(pos = 95, noValue = "NULL")
    private String subInt10;
    /**
    * The MD_MAST_TBL InsDate.
    */
    @CsvField(pos = 96, noValue = "NULL")
    private String insDate;
    /**
    * The MD_MAST_TBL UpdDate.
    */
    @CsvField(pos = 97, noValue = "NULL")
    private String updDate;
    /**
    * The MD_MAST_TBL UpdAppId.
    */
    @CsvField(pos = 98, noValue = "NULL")
    private String updAppId;
    /**
    * The MD_MAST_TBL UpdOpeCode.
    */
    @CsvField(pos = 99, noValue = "NULL")
    private String updOpeCode;

    /**
    * @return the mdType
    */
    public final String getMdType() {
        return mdType;
    }
    /**
    * @param mdTypeToSet the mdType to set
    */
    public final void setMdType(final String mdTypeToSet) {
        this.mdType = mdTypeToSet;
    }
    /**
    * @return the mdInternal
    */
    public final String getMdInternal() {
        return mdInternal;
    }
    /**
    * @param mdInternalToSet the mdInternal to set
    */
    public final void setMdInternal(final String mdInternalToSet) {
        this.mdInternal = mdInternalToSet;
    }
    /**
    * @return the mdVender
    */
    public final String getMdVender() {
        return mdVender;
    }
    /**
    * @param mdVenderToSet the mdVender to set
    */
    public final void setMdVender(final String mdVenderToSet) {
        this.mdVender = mdVenderToSet;
    }
    /**
    * @return the division
    */
    public final String getDivision() {
        return division;
    }
    /**
    * @param divisionToSet the division to set
    */
    public final void setDivision(final String divisionToSet) {
        this.division = divisionToSet;
    }
    /**
    * @return the category
    */
    public final String getCategory() {
        return category;
    }
    /**
    * @param categoryToSet the category to set
    */
    public final void setCategory(final String categoryToSet) {
        this.category = categoryToSet;
    }
    /**
    * @return the brand
    */
    public final String getBrand() {
        return brand;
    }
    /**
    * @param brandToSet the brand to set
    */
    public final void setBrand(final String brandToSet) {
        this.brand = brandToSet;
    }
    /**
    * @return the sku
    */
    public final String getSku() {
        return sku;
    }
    /**
    * @param skuToSet the sku to set
    */
    public final void setSku(final String skuToSet) {
        this.sku = skuToSet;
    }
    /**
    * @return the size
    */
    public final String getSize() {
        return size;
    }
    /**
    * @param sizeToSet the size to set
    */
    public final void setSize(final String sizeToSet) {
        this.size = sizeToSet;
    }
    /**
    * @return the keyPlu
    */
    public final String getKeyPlu() {
        return keyPlu;
    }
    /**
    * @param keyPluToSet the keyPlu to set
    */
    public final void setKeyPlu(final String keyPluToSet) {
        this.keyPlu = keyPluToSet;
    }
    /**
    * @return the md1
    */
    public final String getMd1() {
       return this.md1;
    }
    /**
    * @param md1ToSet the md1 to set
    */
    public final void setMd1(final String md1ToSet) {
        this.md1 = md1ToSet;
    }
    /**
    * @return the md2
    */
    public final String getMd2() {
        return md2;
    }
    /**
    * @param md2ToSet the md2 to set
    */
    public final void setMd2(final String md2ToSet) {
        this.md2 = md2ToSet;
    }
    /**
    * @return the md3
    */
    public final String getMd3() {
        return md3;
    }
    /**
    * @param md3ToSet the md3 to set
    */
    public final void setMd3(final String md3ToSet) {
        this.md3 = md3ToSet;
    }
    /**
    * @return the md4
    */
    public final String getMd4() {
        return md4;
    }
    /**
    * @param md4ToSet the md4 to set
    */
    public final void setMd4(final String md4ToSet) {
        this.md4 = md4ToSet;
    }
    /**
    * @return the md5
    */
    public final String getMd5() {
        return md5;
    }
    /**
    * @param md5ToSet the md5 to set
    */
    public final void setMd5(final String md5ToSet) {
        this.md5 = md5ToSet;
    }
    /**
    * @return the md6
    */
    public final String getMd6() {
       return md6;
    }
    /**
    * @param md6ToSet the md6 to set
    */
    public final void setMd6(final String md6ToSet) {
        this.md6 = md6ToSet;
    }
    /**
    * @return the md7
    */
    public final String getMd7() {
        return md7;
    }
    /**
    * @param md7ToSet the md7 to set
    */
    public final void setMd7(final String md7ToSet) {
        this.md7 = md7ToSet;
    }
    /**
    * @return the md8
    */
    public final String getMd8() {
        try {
            Integer.parseInt(this.md8);
        } catch (Exception e) {
            return "0";
        }
        return md8;
    }
    /**
    * @param md8ToSet the md8 to set
    */
    public final void setMd8(final String md8ToSet) {
        this.md8 = md8ToSet;
    }
    /**
    * @return the md9
    */
    public final String getMd9() {
        return md9;
    }
    /**
    * @param md9ToSet the md9 to set
    */
    public final void setMd9(final String md9ToSet) {
        this.md9 = md9ToSet;
    }
    /**
    * @return the md10
    */
    public final String getMd10() {
       return md10;
    }
    /**
    * @param md10ToSet the md10 to set
    */
    public final void setMd10(final String md10ToSet) {
        this.md10 = md10ToSet;
    }
    /**
    * @return the mdName
    */
    public final String getMdName() {
        return mdName;
    }
    /**
    * @param mdNameToSet the mdName to set
    */
    public final void setMdName(final String mdNameToSet) {
        this.mdName = mdNameToSet;
    }
    /**
    * @return the mdName1
    */
    public final String getMdName1() {
        return mdName1;
    }
    /**
    * @param mdName1ToSet the mdName1 to set
    */
    public final void setMdName1(final String mdName1ToSet) {
        this.mdName1 = mdName1ToSet;
    }
    /**
    * @return the mdName2
    */
    public final String getMdName2() {
        return mdName2;
    }
    /**
    * @param mdName2ToSet the mdName2 to set
    */
    public final void setMdName2(final String mdName2ToSet) {
        this.mdName2 = mdName2ToSet;
    }
    /**
    * @return the mdKanaName
    */
    public final String getMdKanaName() {
        return mdKanaName;
    }
    /**
    * @param mdKanaNameToSet the mdKanaName to set
    */
    public final void setMdKanaName(final String mdKanaNameToSet) {
        this.mdKanaName = mdKanaNameToSet;
    }
    /**
    * @return the mdKanaName1
    */
    public final String getMdKanaName1() {
        return mdKanaName1;
    }
    /**
    * @param mdKanaName1ToSet the mdKanaName1 to set
    */
    public final void setMdKanaName1(final String mdKanaName1ToSet) {
        this.mdKanaName1 = mdKanaName1ToSet;
    }
    /**
    * @return the mdKanaName2
    */
    public final String getMdKanaName2() {
        return mdKanaName2;
    }
    /**
    * @param mdKanaName2ToSet the mdKanaName2 to set
    */
    public final void setMdKanaName2(final String mdKanaName2ToSet) {
        this.mdKanaName2 = mdKanaName2ToSet;
    }
    /**
    * @return the mdShortName
    */
    public final String getMdShortName() {
        return mdShortName;
    }
    /**
    * @param mdShortNameToSet the mdShortName to set
    */
    public final void setMdShortName(final String mdShortNameToSet) {
        this.mdShortName = mdShortNameToSet;
    }
    /**
    * @return the orgSalesPrice1
    */
    public final String getOrgSalesPrice1() {
        return orgSalesPrice1;
    }
    /**
    * @param orgSalesPrice1ToSet the orgSalesPrice1 to set
    */
    public final void setOrgSalesPrice1(final String orgSalesPrice1ToSet) {
        this.orgSalesPrice1 = orgSalesPrice1ToSet;
    }
    /**
    * @return the salesPrice1
    */
    public final String getSalesPrice1() {
        return salesPrice1;
    }
    /**
    * @param salesPrice1ToSet the salesPrice1 to set
    */
    public final void setSalesPrice1(final String salesPrice1ToSet) {
        this.salesPrice1 = salesPrice1ToSet;
    }
    /**
    * @return the salesPrice2
    */
    public final String getSalesPrice2() {
        return salesPrice2;
    }
    /**
    * @param salesPrice2ToSet the salesPrice2 to set
    */
    public final void setSalesPrice2(final String salesPrice2ToSet) {
        this.salesPrice2 = salesPrice2ToSet;
    }
    /**
    * @return the salesPriceChgDate1
    */
    public final String getSalesPriceChgDate1() {
        return salesPriceChgDate1;
    }
    /**
    * @param salesPriceChgDate1ToSet the salesPriceChgDate1 to set
    */
    public final void setSalesPriceChgDate1(
                         final String salesPriceChgDate1ToSet) {
        this.salesPriceChgDate1 = salesPriceChgDate1ToSet;
    }
    /**
    * @return the salesPriceChgDate2
    */
    public final String getSalesPriceChgDate2() {
        return salesPriceChgDate2;
    }
    /**
    * @param salesPriceChgDate2ToSet the salesPriceChgDate2 to set
    */
    public final void setSalesPriceChgDate2(
                         final String salesPriceChgDate2ToSet) {
        this.salesPriceChgDate2 = salesPriceChgDate2ToSet;
    }
    /**
    * @return the empPrice1
    */
    public final String getEmpPrice1() {
        return empPrice1;
    }
    /**
    * @param empPrice1ToSet the empPrice1 to set
    */
    public final void setEmpPrice1(final String empPrice1ToSet) {
        this.empPrice1 = empPrice1ToSet;
    }
    /**
    * @return the empPrice2
    */
    public final String getEmpPrice2() {
        return empPrice2;
    }
    /**
    * @param empPrice2ToSet the empPrice2 to set
    */
    public final void setEmpPrice2(final String empPrice2ToSet) {
        this.empPrice2 = empPrice2ToSet;
    }
    /**
    * @return the empPrice3
    */
    public final String getEmpPrice3() {
        return empPrice3;
    }
    /**
    * @param empPrice3ToSet the empPrice3 to set
    */
    public final void setEmpPrice3(final String empPrice3ToSet) {
        this.empPrice3 = empPrice3ToSet;
    }
    /**
    * @return the puPrice1
    */
    public final String getPuPrice1() {
        return puPrice1;
    }
    /**
    * @param puPrice1ToSet the puPrice1 to set
    */
    public final void setPuPrice1(final String puPrice1ToSet) {
        this.puPrice1 = puPrice1ToSet;
    }
    /**
    * @return the puPrice2
    */
    public final String getPuPrice2() {
        return puPrice2;
    }
    /**
    * @param puPrice2ToSet the puPrice2 to set
    */
    public final void setPuPrice2(final String puPrice2ToSet) {
        this.puPrice2 = puPrice2ToSet;
    }
    /**
    * @return the puPriceChgDate1
    */
    public final String getPuPriceChgDate1() {
        return puPriceChgDate1;
    }
    /**
    * @param puPriceChgDate1ToSet the puPriceChgDate1 to set
    */
    public final void setPuPriceChgDate1(final String puPriceChgDate1ToSet) {
        this.puPriceChgDate1 = puPriceChgDate1ToSet;
    }
    /**
    * @return the puPriceChgDate2
    */
    public final String getPuPriceChgDate2() {
        return puPriceChgDate2;
    }
    /**
    * @param puPriceChgDate2ToSet the puPriceChgDate2 to set
    */
    public final void setPuPriceChgDate2(final String puPriceChgDate2ToSet) {
        this.puPriceChgDate2 = puPriceChgDate2ToSet;
    }
    /**
    * @return the orgCostPrice1
    */
    public final String getOrgCostPrice1() {
        return orgCostPrice1;
    }
    /**
    * @param orgCostPrice1ToSet the orgCostPrice1 to set
    */
    public final void setOrgCostPrice1(final String orgCostPrice1ToSet) {
        this.orgCostPrice1 = orgCostPrice1ToSet;
    }
    /**
    * @return the costPrice1
    */
    public final String getCostPrice1() {
        return costPrice1;
    }
    /**
    * @param costPrice1ToSet the costPrice1 to set
    */
    public final void setCostPrice1(final String costPrice1ToSet) {
        this.costPrice1 = costPrice1ToSet;
    }
    /**
    * @return the costPrice2
    */
    public final String getCostPrice2() {
        return costPrice2;
    }
    /**
    * @param costPrice2ToSet the costPrice2 to set
    */
    public final void setCostPrice2(final String costPrice2ToSet) {
        this.costPrice2 = costPrice2ToSet;
    }
    /**
    * @return the costPriceChgDate1
    */
    public final String getCostPriceChgDate1() {
        return costPriceChgDate1;
    }
    /**
    * @param costPriceChgDate1ToSet the costPriceChgDate1 to set
    */
    public final void setCostPriceChgDate1(
                      final String costPriceChgDate1ToSet) {
        this.costPriceChgDate1 = costPriceChgDate1ToSet;
    }
    /**
    * @return the costPriceChgDate2
    */
    public final String getCostPriceChgDate2() {
        return costPriceChgDate2;
    }
    /**
    * @param costPriceChgDate2ToSet the costPriceChgDate2 to set
    */
    public final void setCostPriceChgDate2(
                      final String costPriceChgDate2ToSet) {
        this.costPriceChgDate2 = costPriceChgDate2ToSet;
    }
    /**
    * @return the salesDate
    */
    public final String getSalesDate() {
        return salesDate;
    }
    /**
    * @param salesDateToSet the salesDate to set
    */
    public final void setSalesDate(final String salesDateToSet) {
        this.salesDate = salesDateToSet;
    }
    /**
    * @return the makerPrice
    */
    public final String getMakerPrice() {
        return makerPrice;
    }
    /**
    * @param makerPriceToSet the makerPrice to set
    */
    public final void setMakerPrice(final String makerPriceToSet) {
        this.makerPrice = makerPriceToSet;
    }
    /**
    * @return the taxType
    */
    public final String getTaxType() {
        return taxType;
    }
    /**
    * @param taxTypeToSet the taxType to set
    */
    public final void setTaxType(final String taxTypeToSet) {
        this.taxType = taxTypeToSet;
    }
    /**
    * @return the discountType
    */
    public final String getDiscountType() {
        return discountType;
    }
    /**
    * @param discountTypeToSet the discountType to set
    */
    public final void setDiscountType(final String discountTypeToSet) {
        this.discountType = discountTypeToSet;
    }
    /**
    * @return the seasonType
    */
    public final String getSeasonType() {
        return seasonType;
    }
    /**
    * @param seasonTypeToSet the seasonType to set
    */
    public final void setSeasonType(final String seasonTypeToSet) {
        this.seasonType = seasonTypeToSet;
    }
    /**
    * @return the paymentType
    */
    public final String getPaymentType() {
        return paymentType;
    }
    /**
    * @param paymentTypeToSet the paymentType to set
    */
    public final void setPaymentType(final String paymentTypeToSet) {
        this.paymentType = paymentTypeToSet;
    }
    /**
    * @return the orderType
    */
    public final String getOrderType() {
        return orderType;
    }
    /**
    * @param orderTypeToSet the orderType to set
    */
    public final void setOrderType(final String orderTypeToSet) {
        this.orderType = orderTypeToSet;
    }
    /**
    * @return the posMdType
    */
    public final String getPosMdType() {
        return posMdType;
    }
    /**
    * @param posMdTypeToSet the posMdType to set
    */
    public final void setPosMdType(final String posMdTypeToSet) {
        this.posMdType = posMdTypeToSet;
    }
    /**
    * @return the catType
    */
    public final String getCatType() {
        return catType;
    }
    /**
    * @param catTypeToSet the catType to set
    */
    public final void setCatType(final String catTypeToSet) {
        this.catType = catTypeToSet;
    }
    /**
    * @return the orderUnit
    */
    public final String getOrderUnit() {
        return orderUnit;
    }
    /**
    * @param orderUnitToSet the orderUnit to set
    */
    public final void setOrderUnit(final String orderUnitToSet) {
        this.orderUnit = orderUnitToSet;
    }
    /**
    * @return the orderPoint
    */
    public final String getOrderPoint() {
        return orderPoint;
    }
    /**
    * @param orderPointToSet the orderPoint to set
    */
    public final void setOrderPoint(final String orderPointToSet) {
        this.orderPoint = orderPointToSet;
    }
    /**
    * @return the baseStockCnt
    */
    public final String getBaseStockCnt() {
        return baseStockCnt;
    }
    /**
    * @param baseStockCntToSet the baseStockCnt to set
    */
    public final void setBaseStockCnt(final String baseStockCntToSet) {
        this.baseStockCnt = baseStockCntToSet;
    }
    /**
    * @return the conn1
    */
    public final String getConn1() {
        return conn1;
    }
    /**
    * @param conn1ToSet the conn1 to set
    */
    public final void setConn1(final String conn1ToSet) {
        this.conn1 = conn1ToSet;
    }
    /**
    * @return the connType1
    */
    public final String getConnType1() {
        return connType1;
    }
    /**
    * @param connType1ToSet the connType1 to set
    */
    public final void setConnType1(final String connType1ToSet) {
        this.connType1 = connType1ToSet;
    }
    /**
    * @return the venderCode
    */
    public final String getVenderCode() {
        return venderCode;
    }
    /**
    * @param venderCodel the venderCodel to set
    */
    public final void setVenderCode(final String venderCodel) {
        this.venderCode = venderCodel;
    }
    /**
    * @return the venderType
    */
    public final String getVenderType() {
        return venderType;
    }
    /**
    * @param venderTypeToSet the venderType to set
    */
    public final void setVenderType(final String venderTypeToSet) {
        this.venderType = venderTypeToSet;
    }
    /**
    * @return the tagType
    */
    public final String getTagType() {
        return tagType;
    }
    /**
    * @param tagTypeToSet the tagType to set
    */
    public final void setTagType(final String tagTypeToSet) {
        this.tagType = tagTypeToSet;
    }
    /**
    * @return the tagCode1
    */
    public final String getTagCode1() {
        return tagCode1;
    }
    /**
    * @param tagCode1ToSet the tagCode1 to set
    */
    public final void setTagCode1(final String tagCode1ToSet) {
        this.tagCode1 = tagCode1ToSet;
    }
    /**
    * @return the tagCode2
    */
    public final String getTagCode2() {
        return tagCode2;
    }
    /**
    * @param tagCode2ToSet the tagCode2 to set
    */
    public final void setTagCode2(final String tagCode2ToSet) {
        this.tagCode2 = tagCode2ToSet;
    }
    /**
    * @return the tagCode3
    */
    public final String getTagCode3() {
        return tagCode3;
    }
    /**
    * @param tagCode3ToSet the tagCode3 to set
    */
    public final void setTagCode3(final String tagCode3ToSet) {
        this.tagCode3 = tagCode3ToSet;
    }
    /**
    * @return the PointRate
    */
    public final String getPointRate() {
        return pointRate;
    }
    /**
    * @param pointRateToSet the PointRate to set
    */
    public final void setPointRate(final String pointRateToSet) {
        this.pointRate = pointRateToSet;
    }
    /**
    * @return the pictureFileName1
    */
    public final String getPictureFileName1() {
        return pictureFileName1;
    }
    /**
    * @param pictureFileName1ToSet the pictureFileName1 to set
    */
    public final void setPictureFileName1(final String pictureFileName1ToSet) {
        this.pictureFileName1 = pictureFileName1ToSet;
    }
    /**
    * @return the pictureFileName2
    */
    public final String getPictureFileName2() {
        return pictureFileName2;
    }
    /**
    * @param pictureFileName2ToSet the pictureFileName2 to set
    */
    public final void setPictureFileName2(final String pictureFileName2ToSet) {
        this.pictureFileName2 = pictureFileName2ToSet;
    }
    /**
    * @return the subMoney1
    */
    public final String getSubMoney1() {
        return subMoney1;
    }
    /**
    * @param subMoney1ToSet the subMoney1 to set
    */
    public final void setSubMoney1(final String subMoney1ToSet) {
        this.subMoney1 = subMoney1ToSet;
    }
    /**
    * @return the subMoney2
    */
    public final String getSubMoney2() {
        return subMoney2;
    }
    /**
    * @param subMoney2ToSet the subMoney2 to set
    */
    public final void setSubMoney2(final String subMoney2ToSet) {
        this.subMoney2 = subMoney2ToSet;
    }
    /**
    * @return the subMoney3
    */
    public final String getSubMoney3() {
        return subMoney3;
    }
    /**
    * @param subMoney3ToSet the subMoney3 to set
    */
    public final void setSubMoney3(final String subMoney3ToSet) {
        this.subMoney3 = subMoney3ToSet;
    }
    /**
    * @return the subMoney4
    */
    public final String getSubMoney4() {
        return subMoney4;
    }
    /**
    * @param subMoney4ToSet the subMoney4 to set
    */
    public final void setSubMoney4(final String subMoney4ToSet) {
        this.subMoney4 = subMoney4ToSet;
    }
    /**
    * @return the subMoney5
    */
    public final String getSubMoney5() {
        return subMoney5;
    }
    /**
    * @param subMoney5ToSet the subMoney5 to set
    */
    public final void setSubMoney5(final String subMoney5ToSet) {
        this.subMoney5 = subMoney5ToSet;
    }
    /**
    * @return the subCode1
    */
    public final String getSubCode1() {
        return subCode1;
    }
    /**
    * @param subCode1ToSet the subCode1 to set
    */
    public final void setSubCode1(final String subCode1ToSet) {
        this.subCode1 = subCode1ToSet;
    }
    /**
    * @return the subCode2
    */
    public final String getSubCode2() {
        return subCode2;
    }
    /**
    * @param subCode2ToSet the subCode2 to set
    */
    public final void setSubCode2(final String subCode2ToSet) {
        this.subCode2 = subCode2ToSet;
    }
    /**
    * @return the subCode3
    */
    public final String getSubCode3() {
        return subCode3;
    }
    /**
    * @param subCode3ToSet the subCode3 to set
    */
    public final void setSubCode3(final String subCode3ToSet) {
        this.subCode3 = subCode3ToSet;
    }
    /**
    * @return the subCode4
    */
    public final String getSubCode4() {
        return subCode4;
    }
    /**
    * @param subCode4ToSet the subCode4 to set
    */
    public final void setSubCode4(final String subCode4ToSet) {
        this.subCode4 = subCode4ToSet;
    }
    /**
    * @return the subCode5
    */
    public final String getSubCode5() {
        return subCode5;
    }
    /**
    * @param subCode5ToSet the subCode5 to set
    */
    public final void setSubCode5(final String subCode5ToSet) {
        this.subCode5 = subCode5ToSet;
    }
    /**
    * @return the subCode6
    */
    public final String getSubCode6() {
        return subCode6;
    }
    /**
    * @param subCode6ToSet the subCode6 to set
    */
    public final void setSubCode6(final String subCode6ToSet) {
        this.subCode6 = subCode6ToSet;
    }
    /**
    * @return the subCode7
    */
    public final String getSubCode7() {
        return subCode7;
    }
    /**
    * @param subCode7ToSet the subCode7 to set
    */
    public final void setSubCode7(final String subCode7ToSet) {
        this.subCode7 = subCode7ToSet;
    }
    /**
    * @return the subCode8
    */
    public final String getSubCode8() {
        return subCode8;
    }
    /**
    * @param subCode8ToSet the subCode8 to set
    */
    public final void setSubCode8(final String subCode8ToSet) {
        this.subCode8 = subCode8ToSet;
    }
    /**
    * @return the subCode9
    */
    public final String getSubCode9() {
        return subCode9;
    }
    /**
    * @param subCode9ToSet the subCode9 to set
    */
    public final void setSubCode9(final String subCode9ToSet) {
        this.subCode9 = subCode9ToSet;
    }
    /**
    * @return the subCode10
    */
    public final String getSubCode10() {
        return subCode10;
    }
    /**
    * @param subCode10ToSet the subCode10 to set
    */
    public final void setSubCode10(final String subCode10ToSet) {
        this.subCode10 = subCode10ToSet;
    }
    /**
    * @return the SubTinyInt1
    */
    public final String getSubTinyInt1() {
        return subTinyInt1;
    }
    /**
    * @param subTinyInt1ToSet the SubTinyInt1 to set
    */
    public final void setSubTinyInt1(final String subTinyInt1ToSet) {
        this.subTinyInt1 = subTinyInt1ToSet;
    }
    /**
    * @return the SubTinyInt2
    */
    public final String getSubTinyInt2() {
        return subTinyInt2;
    }
    /**
    * @param subTinyInt2ToSet the SubTinyInt2 to set
    */
    public final void setSubTinyInt2(final String subTinyInt2ToSet) {
        this.subTinyInt2 = subTinyInt2ToSet;
    }
    /**
    * @return the SubTinyInt3
    */
    public final String getSubTinyInt3() {
        return subTinyInt3;
    }
    /**
    * @param subTinyInt3ToSet the SubTinyInt3 to set
    */
    public final void setSubTinyInt3(final String subTinyInt3ToSet) {
        this.subTinyInt3 = subTinyInt3ToSet;
    }
    /**
    * @return the SubTinyInt4
    */
    public final String getSubTinyInt4() {
        return subTinyInt4;
    }
    /**
    * @param subTinyInt4ToSet the SubTinyInt4 to set
    */
    public final void setSubTinyInt4(final String subTinyInt4ToSet) {
        this.subTinyInt4 = subTinyInt4ToSet;
    }
    /**
    * @return the SubTinyInt5
    */
    public final String getSubTinyInt5() {
        return subTinyInt5;
    }
    /**
    * @param subTinyInt5ToSet the SubTinyInt5 to set
    */
    public final void setSubTinyInt5(final String subTinyInt5ToSet) {
        this.subTinyInt5 = subTinyInt5ToSet;
    }
    /**
    * @return the SubTinyInt6
    */
    public final String getSubTinyInt6() {
        return subTinyInt6;
    }
    /**
    * @param subTinyInt6ToSet the SubTinyInt6 to set
    */
    public final void setSubTinyInt6(final String subTinyInt6ToSet) {
        this.subTinyInt6 = subTinyInt6ToSet;
    }
    /**
    * @return the SubTinyInt7
    */
    public final String getSubTinyInt7() {
        return subTinyInt7;
    }
    /**
    * @param subTinyInt7ToSet the SubTinyInt7 to set
    */
    public final void setSubTinyInt7(final String subTinyInt7ToSet) {
        this.subTinyInt7 = subTinyInt7ToSet;
    }
    /**
    * @return the SubTinyInt8
    */
    public final String getSubTinyInt8() {
        return subTinyInt8;
    }
    /**
    * @param subTinyInt8ToSet the SubTinyInt8 to set
    */
    public final void setSubTinyInt8(final String subTinyInt8ToSet) {
        this.subTinyInt8 = subTinyInt8ToSet;
    }
    /**
    * @return the SubTinyInt9
    */
    public final String getSubTinyInt9() {
        return subTinyInt9;
    }
    /**
    * @param subTinyInt9ToSet the SubTinyInt9 to set
    */
    public final void setSubTinyInt9(final String subTinyInt9ToSet) {
        this.subTinyInt9 = subTinyInt9ToSet;
    }
    /**
    * @return the SubInt10
    */
    public final String getSubInt10() {
        return subInt10;
    }
    /**
    * @param subInt10ToSet the SubInt10 to set
    */
    public final void setSubInt10(final String subInt10ToSet) {
        this.subInt10 = subInt10ToSet;
    }
    /**
    * @return the insDate
    */
    public final String getInsDate() {
        return insDate;
    }
    /**
    * @param insDateToSet the insDate to set
    */
    public final void setInsDate(final String insDateToSet) {
        this.insDate = insDateToSet;
    }
    /**
    * @return the updDate
    */
    public final String getUpdDate() {
        return updDate;
    }
    /**
    * @param updDateToSet the updDate to set
    */
    public final void setUpdDate(final String updDateToSet) {
        this.updDate = updDateToSet;
    }
    /**
    * @return the updAppId
    */
    public final String getUpdAppId() {
        return updAppId;
    }
    /**
    * @param updAppIdToSet the updAppId to set
    */
    public final void setUpdAppId(final String updAppIdToSet) {
        this.updAppId = updAppIdToSet;
    }
    /**
    * @return the updOpeCode
    */

    public final String getUpdOpeCode() {
        return updOpeCode;
    }
    /**
    * @param updOpeCodeToSet the updOpeCode to set
    */
    public final void setUpdOpeCode(final String updOpeCodeToSet) {
        this.updOpeCode = updOpeCodeToSet;
    }
    /**
    * @return the plu
    */

    public final String getPlu() {
        return plu;
    }
    /**
    * @param pluToSet the plu to set
    */
    public final void setPlu(final String pluToSet) {
        this.plu = pluToSet;
    }
    /**
    * @return the storeid
    */
    public final String getStoreid() {
        return storeid;
    }
    /**
    * @param storeidToSet the storeid to set
    */
    public final void setStoreid(final String storeidToSet) {
        this.storeid = storeidToSet;
    }
    /**
    * @return the conn2
    */
    public final String getConn2() {
        return conn2;
    }
    /**
    * @param conn2ToSet the conn2 to set
    */
    public final void setConn2(final String conn2ToSet) {
        this.conn2 = conn2ToSet;
    }
    /**
    * @return the connType2
    */
    public final String getConnType2() {
        return connType2;
    }
    /**
    * @param connType2ToSet the connType2 to set
    */
    public final void setConnType2(final String connType2ToSet) {
        this.connType2 = connType2ToSet;
    }
    /**
     * String representation of MdMastTbl model.
     * @return String representation.
     */
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
    /**
     * The default constructor.
     */
    public MdMastTbl() {

    }

}
