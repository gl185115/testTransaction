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

}
