/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D

 *
 * Tender
 *
 * Model Class for Tender
 *
 * Beijing ODC mlwang
 * 2014/09/03
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * StoredValueInstrument Model Object.
 *
 * <P>A StoredValueInstrument Node in POSLog XML.
 *
 * <P>The StoredValueInstrument node is under Tender Node.
 * And holds the StoredValueInstrument type information from the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoredValueInstrument")
public class StoredValueInstrument {

    /**
     * The  attribute variable that will hold the typeCode of StoredValueInstrument.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;
    @XmlElement(name = "Brand")
    private String brand;
    @XmlElement(name = "Description")
    private String description;
    @XmlElement(name = "FaceValueAmount")
    private double faceValueAmount;
    @XmlElement(name = "SerialNumber")
    private String serialNumber;
    @XmlElement(name = "UnspentAmount")
    private UnspentAmount unspentAmount;
    @XmlElement(name = "ReferenceNumber")
    private String referenceNumber;
    @XmlElement(name = "CreditDebit")
    private CreditDebit creditDebit;
    public StoredValueInstrument(){
        creditDebit = new CreditDebit();
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getFaceValueAmount() {
        return faceValueAmount;
    }
    public void setFaceValueAmount(double faceValueAmount) {
        this.faceValueAmount = faceValueAmount;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public UnspentAmount getUnspentAmount() {
        return unspentAmount;
    }
    public void setUnspentAmount(UnspentAmount unspentAmount) {
        this.unspentAmount = unspentAmount;
    }
    public final String getReferenceNumber() {
        return referenceNumber;
    }
    public final void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    /**
     * @return creditDebit
     */
    public CreditDebit getCreditDebit() {
        return creditDebit;
    }
    /**
     * @param creditDebit ƒZƒbƒg‚·‚é creditDebit
     */
    public void setCreditDebit(CreditDebit creditDebit) {
        this.creditDebit = creditDebit;
    }
}
