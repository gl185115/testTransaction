package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Instrument")
public class Instrument {

    /**
     * Previous balance of brand.
     */
    @XmlElement(name = "Brand")
    private String brand;

    /**
     * Previous FaceValueAmount.
     */
    @XmlElement(name = "FaceValueAmount")
    private int faceValueAmount;

    /**
     * Previous SerialNumber.
     */
    @XmlElement(name = "SerialNumber")
    private String serialNumber;
    /***
     * cardNumber;
     */
    @XmlElement(name = "CardNumber")
    private String cardNumber;

    /***
     * authorizationNumber;
     */
    @XmlElement(name = "AuthorizationNumber")
    private String authorizationNumber;

    /**
     * expirationDate
     */
    @XmlElement(name = "ExpirationDate")
    private String expirationDate;

    /***
     * priorFaceValueAmount;
     */
    @XmlElement(name = "PriorFaceValueAmount")
    private Integer priorFaceValueAmount;

    /***
     * currentFaceValueAmount;
     */
    @XmlElement(name = "CurrentFaceValueAmount")
    private Integer currentFaceValueAmount;

    /***
     * chargeAmount;
     */
    @XmlElement(name = "ChargeAmount")
    private Integer chargeAmount;

    /***
     * jis1;
     */
    @XmlElement(name = "JIS1")
    private String jis1;
    /***
     * jis2;
     */
    @XmlElement(name = "JIS2")
    private String jis2;

    /***
     * pin;
     */
    @XmlElement(name = "PIN")
    private String pin;

    /**
     * @return serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber セットする serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return faceValueAmount
     */
    public int getFaceValueAmount() {
        return faceValueAmount;
    }

    /**
     * @param faceValueAmount セットする faceValueAmount
     */
    public void setFaceValueAmount(int faceValueAmount) {
        this.faceValueAmount = faceValueAmount;
    }

    /**
     * @return brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand セットする brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public final void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the authorizationNumber
     */
    public final String getAuthorizationNumber() {
        return authorizationNumber;
    }

    /**
     * @param authorizationNumber the authorizationNumber to set
     */
    public final void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    /**
     * @return the expirationDate
     */
    public final String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param ExpirationDate
     *            the ExpirationDate to set
     */
    public final void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the priorFaceValueAmount
     */
    public final Integer getPriorFaceValueAmount() {
        return priorFaceValueAmount;
    }

    /**
     * @param priorFaceValueAmount the priorFaceValueAmount to set
     */
    public final void setPriorFaceValueAmount(Integer priorFaceValueAmount) {
        this.priorFaceValueAmount = priorFaceValueAmount;
    }

    /**
     * @return the currentFaceValueAmount
     */
    public final Integer getCurrentFaceValueAmount() {
        return currentFaceValueAmount;
    }

    /**
     * @param currentFaceValueAmount the currentFaceValueAmount to set
     */
    public final void setCurrentFaceValueAmount(Integer currentFaceValueAmount) {
        this.currentFaceValueAmount = currentFaceValueAmount;
    }

    /**
     * @return the chargeAmount
     */
    public final Integer getChargeAmount() {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public final void setChargeAmount(Integer chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the jis1
     */
    public final String getJis1() {
        return jis1;
    }

    /**
     * @param jis1 the jis1 to set
     */
    public final void setJis1(String jis1) {
        this.jis1 = jis1;
    }

    /**
     * @return the jis2
     */
    public final String getJis2() {
        return jis2;
    }

    /**
     * @param jis2 the jis2 to set
     */
    public final void setJis2(String jis2) {
        this.jis2 = jis2;
    }

    /**
     * @return the pin
     */
    public final String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public final void setPin(String pin) {
        this.pin = pin;
    }
}
