/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for RetailTransaction
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Forward Model Object.
 *
 * <P>A Forward Node in POSLog XML.
 *
 * <P>The Forward node is under Sale Node.
 * And mainly holds the information of the Forward's transaction
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Forward")
public class Forward {
    /**
     * The private member variable that will hold the Forward Flag.
     */
    @XmlElement(name = "ForwardFlag")
    private String forwardFlag;
    
    /**
     * The private member variable that will hold the Forward Slip No.
     */
    @XmlElement(name = "ForwardSlipNo")
    private String forwardSlipNo;

    /**
     * The private member variable that will hold the Forward Line No.
     */
    @XmlElement(name = "ForwardLineNo")
    private String forwardLineNo;

    /**
     * The CarryFlag.
     */
    @XmlElement(name = "CarryFlag")
    private String carryFlag;

    /**
     * The CarryItemName1.
     */
    @XmlElement(name = "CarryItemName1")
    private String carryItemName1;

    /**
     * The CarryItemName2.
     */
    @XmlElement(name = "CarryItemName2")
    private String carryItemName2;

    /**
     * The CarryItemName3.
     */
    @XmlElement(name = "CarryItemName3")
    private String carryItemName3;

    /**
     * The CarryItemName4.
     */
    @XmlElement(name = "CarryItemName4")
    private String carryItemName4;

    /**
     * The CarryItemName5.
     */
    @XmlElement(name = "CarryItemName5")
    private String carryItemName5;

    /**
     * The CarryItemName6.
     */
    @XmlElement(name = "CarryItemName6")
    private String carryItemName6;

    /**
     * @param carryFlag the carryFlag to set
     */
    public final void setCarryFlag(final String carryFlag) {
        this.carryFlag = carryFlag;
    }

    /**
     * @return the carryFlag
     */
    public final String getCarryFlag() {
        return carryFlag;
    }

    /**
     * @param carryItemName1 the carryItemName1 to set
     */
    public final void setCarryItemName1(final String carryItemName1) {
        this.carryItemName1 = carryItemName1;
    }

    /**
     * @return the carryItemName1
     */
    public final String getCarryItemName1() {
        return carryItemName1;
    }

    /**
     * @param carryItemName2 the carryItemName2 to set
     */
    public final void setCarryItemName2(final String carryItemName2) {
        this.carryItemName2 = carryItemName2;
    }

    /**
     * @return the carryItemName2
     */
    public final String getCarryItemName2() {
        return carryItemName2;
    }
    
    /**
     * @param carryItemName3 the carryItemName3 to set
     */
    public final void setCarryItemName3(final String carryItemName3) {
        this.carryItemName3 = carryItemName3;
    }

    /**
     * @return the carryItemName3
     */
    public final String getCarryItemName3() {
        return carryItemName3;
    }
    
    /**
     * @param carryItemName4 the carryItemName4 to set
     */
    public final void setCarryItemName4(final String carryItemName4) {
        this.carryItemName4 = carryItemName4;
    }

    /**
     * @return the carryItemName4
     */
    public final String getCarryItemName4() {
        return carryItemName4;
    }
    
    /**
     * @param carryItemName5 the carryItemName5 to set
     */
    public final void setCarryItemName5(final String carryItemName5) {
        this.carryItemName5 = carryItemName5;
    }

    /**
     * @return the carryItemName5
     */
    public final String getCarryItemName5() {
        return carryItemName5;
    }
    
    /**
     * @param carryItemName6 the carryItemName6 to set
     */
    public final void setCarryItemName6(final String carryItemName6) {
        this.carryItemName6 = carryItemName6;
    }

    /**
     * @return the carryItemName6
     */
    public final String getCarryItemName6() {
        return carryItemName6;
    }
    
    /**
     * @return the forwardFlag
     */
    public final String getForwardFlag() {
        return forwardFlag;
    }
    
    /**
     * @param forwardFlag the forwardFlag to set
     */
    public final void setForwardFlag(final String forwardFlag) {
        this.forwardFlag = forwardFlag;
    }

    /**
     * @return the forwardSlipNo
     */
    public final String getForwardSlipNo() {
        return forwardSlipNo;
    }

    /**
     * @param forwardSlipNo the forwardSlipNo to set
     */
    public final void setForwardSlipNo(final String forwardSlipNo) {
        this.forwardSlipNo = forwardSlipNo;
    }

    /**
     * @return the forwardLineNo
     */
    public final String getForwardLineNo() {
        return forwardLineNo;
    }

    /**
     * @param forwardLineNo the forwardLineNo to set
     */
    public final void setForwardLineNo(final String forwardLineNo) {
        this.forwardLineNo = forwardLineNo;
    }
}
