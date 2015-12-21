package ncr.res.mobilepos.mastermaintenance.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

/**
 * OpeMastTbl class is the object representation for OPE_MAST_TBL in the
 * SPART database.
 * @author CM185093
 *
 */
@CsvDataType
public class OpeMastTbl extends MaintenanceTbl {
    /** The Employee Code. **/
	@CsvField(pos = 1, noValue="NULL")
    private String empCode;
    /** The Operator Code. */
	@CsvField(pos = 2, noValue="NULL")
    private String opeCode;
    /** The Password. */
	@CsvField(pos = 3, noValue="NULL")
    private String password;
    /** The Operator Type. */
	@CsvField(pos = 4, noValue="NULL")
    private String opeType;
    /** The Operator Name. */
	@CsvField(pos = 5, noValue="NULL")
    private String opeName;
    /** The Operator Kana Name. */
	@CsvField(pos = 6, noValue="NULL")
    private String opeKanaName;
    /** The ZipCode. */
	@CsvField(pos = 7, noValue="NULL")
    private String zipCode;
    /** The Address. */
	@CsvField(pos = 8, noValue="NULL")
    private String address;
    /** The Telephone Number. */
	@CsvField(pos = 9, noValue="NULL")
    private String telNo;
    /** The Fax Number. */
	@CsvField(pos = 10, noValue="NULL")
    private String faxNo;
    /** The Security Level 1. */
	@CsvField(pos = 11, noValue="NULL")
    private String secLevel1;
    /** The Security Level 2. */
	@CsvField(pos = 12, noValue="NULL")
    private String secLevel2;
    /** The Sub Character 1. */
	@CsvField(pos = 13, noValue="NULL")
    private String subChar1;
    /** The Sub Character 2. */
	@CsvField(pos = 14, noValue="NULL")
    private String subChar2;
    /** The Sub Character 3. */
	@CsvField(pos = 15, noValue="NULL")
    private String subChar3;
    /** The insert date. */
	@CsvField(pos = 16, noValue="NULL")
    private String insDate;
    /** The Update date. **/
	@CsvField(pos = 17, noValue="NULL")
    private String updDate;
    /** The Update Application ID. */
	@CsvField(pos = 18, noValue="NULL")
    private String updAppId;
    /** The Updated Operator Code. */
	@CsvField(pos = 19, noValue="NULL")
    private String updOpeCode;
    /**
     * @return the empCode
     */
    public final String getEmpCode() {
        return empCode;
    }
    /**
     * @param empCodeToSet the empCode to set
     */
    public final void setEmpCode(final String empCodeToSet) {
        this.empCode = empCodeToSet;
    }
    /**
     * @return the opeCode
     */
    public final String getOpeCode() {
        return opeCode;
    }
    /**
     * @param opeCodeToSet the opeCode to set
     */
    public final void setOpeCode(final String opeCodeToSet) {
        this.opeCode = opeCodeToSet;
    }
    /**
     * @return the password
     */
    public final String getPassword() {
        return password;
    }
    /**
     * @param passwordToSet the password to set
     */
    public final void setPassword(final String passwordToSet) {
        this.password = passwordToSet;
    }
    /**
     * @return the opeType
     */
    public final String getOpeType() {
        return opeType;
    }
    /**
     * @param opeTypeToSet the opeType to set
     */
    public final void setOpeType(final String opeTypeToSet) {
        this.opeType = opeTypeToSet;
    }
    /**
     * @return the opeName
     */
    public final String getOpeName() {
        return opeName;
    }
    /**
     * @param opeNameToSet the opeName to set
     */
    public final void setOpeName(final String opeNameToSet) {
        this.opeName = opeNameToSet;
    }
    /**
     * @return the opeKanaName
     */
    public final String getOpeKanaName() {
        return opeKanaName;
    }
    /**
     * @param opeKanaNameToSet the opeKanaName to set
     */
    public final void setOpeKanaName(final String opeKanaNameToSet) {
        this.opeKanaName = opeKanaNameToSet;
    }
    /**
     * @return the zipCode
     */
    public final String getZipCode() {
        return zipCode;
    }
    /**
     * @param zipCodeToSet the zipCode to set
     */
    public final void setZipCode(final String zipCodeToSet) {
        this.zipCode = zipCodeToSet;
    }
    /**
     * @return the address
     */
    public final String getAddress() {
        return address;
    }
    /**
     * @param addressToSet the address to set
     */
    public final void setAddress(final String addressToSet) {
        this.address = addressToSet;
    }
    /**
     * @return the telNo
     */
    public final String getTelNo() {
        return telNo;
    }
    /**
     * @param telNoToSet the telNo to set
     */
    public final void setTelNo(final String telNoToSet) {
        this.telNo = telNoToSet;
    }
    /**
     * @return the faxNo
     */
    public final String getFaxNo() {
        return faxNo;
    }
    /**
     * @param faxNoToSet the faxNo to set
     */
    public final void setFaxNo(final String faxNoToSet) {
        this.faxNo = faxNoToSet;
    }
    /**
     * @return the secLevel1
     */
    public final String getSecLevel1() {
        return secLevel1;
    }
    /**
     * @param secLevel1ToSet the secLevel1 to set
     */
    public final void setSecLevel1(final String secLevel1ToSet) {
        this.secLevel1 = secLevel1ToSet;
    }
    /**
     * @return the secLevel2
     */
    public final String getSecLevel2() {
        return secLevel2;
    }
    /**
     * @param secLevel2ToSet the secLevel2 to set
     */
    public final void setSecLevel2(final String secLevel2ToSet) {
        this.secLevel2 = secLevel2ToSet;
    }
    /**
     * @return the subChar1
     */
    public final String getSubChar1() {
        return subChar1;
    }
    /**
     * @param subChar1ToSet the subChar1 to set
     */
    public final void setSubChar1(final String subChar1ToSet) {
        this.subChar1 = subChar1ToSet;
    }
    /**
     * @return the subChar2
     */
    public final String getSubChar2() {
        return subChar2;
    }
    /**
     * @param subChar2ToSet the subChar2 to set
     */
    public final void setSubChar2(final String subChar2ToSet) {
        this.subChar2 = subChar2ToSet;
    }
    /**
     * @return the subChar3
     */
    public final String getSubChar3() {
        return subChar3;
    }
    /**
     * @param subChar3ToSet the subChar3 to set
     */
    public final void setSubChar3(final String subChar3ToSet) {
        this.subChar3 = subChar3ToSet;
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

    @Override
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

}
