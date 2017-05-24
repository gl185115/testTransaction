/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* Customer
*
* Customer Class is a Model representation and mainly holds
* the Customer's Information.
*
* Campos, Carlos
*/
package ncr.res.mobilepos.customeraccount.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

 /**
 * Customer Class is a Model representation and mainly holds
 * the Customer's Information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Customer")
@ApiModel(value="Customer")
public class Customer extends ResultBase {
    /**
     * The Customer ID.
     */
    @XmlElement(name = "CustomerID")
    private String customerid;
    /**
     * The Name of the Customer.
     */
    @XmlElement(name = "CustomerName")
    private String customername;
    /**
     * The Discount Rate for the Customer.
     */
    @XmlElement(name = "DiscountRate")
    private String discountrate;
    /**
     * The Destination Receipt of the Customer.
     */
    @XmlElement (name = "DestReceipt")
    private String destreceipt;
    /**
     * The EMail Address of the Customer.
     */
    @XmlElement (name = "MailAddress")
    private String mailaddress;
    /**
     * The date the Customer is registered in the database.
     */
    @XmlElement (name = "UpdDate")
    private String upddate;
    /**
     * The Destination Receipt of the Customer.
     */
    @XmlElement (name = "Grade")
    private String grade;
    /**
     * The EMail Address of the Customer.
     */
    @XmlElement (name = "Points")
    private int points;
    /**
     * The Customer's Address.
     */
    @XmlElement (name = "Address")
    private String address;
    /**
     * The Customer's id
     */
    @XmlElement (name = "id")
    private String id;
    /**
     * The Customer's rank
     */
    @XmlElement (name = "rank")
    private String rank;
    /**
     * The Customer's SexType
     */
    @XmlElement (name = "SexType")
    private String sexType;
    /**
     * The Customer's birthMonth.
     */
    @XmlElement (name = "birthMonth")
    private String birthMonth;

    /**
     * Get The Customer's Address.
     * @return The Customer's Address
     */
    @ApiModelProperty(value="ƒAƒhƒŒƒX", notes="ƒAƒhƒŒƒX")
    public final String getAddress() {
        return address;
    }

    /**
     * Set the Customer's Address.
     * @param custAddress Customer's Address.
     */
    public final void setAddress(final String custAddress) {
        this.address = custAddress;
    }

    /**
     * Gets the Memberfs ID.
     *
     * @return      Returns Memberfs ID.
     */
    @ApiModelProperty(value="ŒÚ‹q”Ô†", notes="ŒÚ‹q”Ô†")
    public final String getCustomerid() {
        return customerid;
    }

    /**
     * Sets the Memberfs ID.
     * @param customerID        The new value for Memberfs ID.
     */
    public final void setCustomerid(final String customerID) {
        this.customerid = customerID;
    }

    /**
     * Gets the Memberfs full name.
     * @return      Returns Memberfs full name.
     */
    @ApiModelProperty(value="ŒÚ‹q–¼", notes="ŒÚ‹q–¼")
    public final String getCustomername() {
        return customername;
    }

    /**
     * Sets the Memberfs full name.
     *
     * @param customerName     The new value for Memberfs full name.
     */
    public final void setCustomername(final String customerName) {
        this.customername = customerName;
    }

    /**
     * Gets the Memberfs default value of discount rate.
     * @return      Returns Memberfs discount rate.
     */
    @ApiModelProperty(value="Š„ˆø—¦", notes="Š„ˆø—¦")
    public final String getDiscountrate() {
        return discountrate;
    }

    /**
    * Sets the Memberfs value of discount rate.
    * @param discountRate   The new value for Memberfs discount rate.
    */
    public final void setDiscountrate(final String discountRate) {
        this.discountrate = discountRate;
    }

    /**
     * Gets the Memberfs choice for the destination of receipt.
     *
     * @return      Returns destination of receipt.
     */
    @ApiModelProperty(value="—Ìû‘‚Ìˆ¶æ", notes="—Ìû‘‚Ìˆ¶æ")
    public final String getDestreceipt() {
        return destreceipt;
    }

    /**
     * Sets the Memberfs choice for the destination of receipt.
     * @param destReceipt   The new value for destination of receipt.
     */
    public final void setDestreceipt(final String destReceipt) {
        this.destreceipt = destReceipt;
    }

    /**
     * Gets the Memberfs Mail address which receipts are sent to.
     * @return      Returns Memberfs Mail address.
     */
    @ApiModelProperty(value="ƒ[ƒ‹ƒAƒhƒŒƒX", notes="ƒ[ƒ‹ƒAƒhƒŒƒX")
    public final String getMailaddress() {
        return mailaddress;
    }

    /**
     * Sets the Memberfs Mail address which receipts are sent to.
     *
     * @param mailAddress   The new value for Memberfs Mail address.
     */
    public final void setMailaddress(final String mailAddress) {
        this.mailaddress = mailAddress;
    }

    /**
     * Gets the Updated date and time.
     *
     * @return      Returns the Updated date and time.
     */
    @ApiModelProperty(value="XVŠú“ú", notes="XVŠú“ú")
    public final String getUpddate() {
        return upddate;
    }

    /**
     * Sets the Updated date and time.
     * @param updDate   The new value for the updated date and time.
     */
    public final void setUpddate(final String updDate) {
        this.upddate = updDate;
    }

    /**
     * Gets the Memberfs Grade.
     * @return      Returns Memberfs Grade.
     */
    @ApiModelProperty(value="ŒÚ‹q“™‹‰", notes="ŒÚ‹q“™‹‰")
    public final String getGrade() {
         return grade;
    }

    /**
     * Sets the Memberfs Grade.
     * @param memberGrade     The new value for Memberfs Grade.
     */
    public final void setGrade(final String memberGrade) {
        this.grade = memberGrade;
    }

    /**
     * Gets the Memberfs Points.
     * @return      Returns Memberfs Points.
     */
    @ApiModelProperty(value="ŒÚ‹qÏ•ª", notes="ŒÚ‹qÏ•ª")
    public final int getPoints() {
        return points;
    }

    /**
     * Sets the Memberfs Points.
     * @param custPoints    The new value for Memberfs Points.
     */
    public final void setPoints(final int custPoints) {
        this.points = custPoints;
    }
    
    /**
     * Gets the Customer's id
     * @return      Returns Customer's id
     */
    @ApiModelProperty(value="‰ïˆõ”Ô†", notes="‰ïˆõ”Ô†")
    public final String getId() {
        return id;
    }

    /**
     * Sets the Customer's id
     * @param id    The new value for Customer's id
     */
    public final void setId(final String id) {
        this.id = id;
    }
    
    /**
     * Gets the Customer's rank
     * @return      Returns Customer's rank
     */
    @ApiModelProperty(value="‰ïˆõ‚Ìƒ‰ƒ“ƒN", notes="‰ïˆõ‚Ìƒ‰ƒ“ƒN")
    public final String getRank() {
        return rank;
    }

    /**
     * Sets the Customer's rank
     * @param rank    The new value for Customer's rank
     */
    public final void setRank(final String rank) {
        this.rank = rank;
    }
    
    /**
     * Gets the Customer's SexType
     * @return      Returns Customer's SexType
     */
    @ApiModelProperty(value="‰ïˆõ‚Ì«•Ê", notes="‰ïˆõ‚Ì«•Ê")
    public final String getSexType() {
        return sexType;
    }

    /**
     * Sets the Customer's SexType
     * @param sexType    The new value for Customer's SexType
     */
    public final void setSexType(final String sexType) {
        this.sexType = sexType;
    }
    
    /**
     * Gets the Customer's birthMonth.
     * @return      Returns Customer's birthMonth.
     */
    @ApiModelProperty(value="‰ïˆõ‚Ì’a¶Œ", notes="‰ïˆõ‚Ì’a¶Œ")
    public final String getBirthMonth() {
        return birthMonth;
    }

    /**
     * Sets the Customer's birthMonth.
     * @param birthMonth    The new value for Customer's birthMonth.
     */
    public final void setBirthMonth(final String birthMonth) {
        this.birthMonth = birthMonth;
    }
}
