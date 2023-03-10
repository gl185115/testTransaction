package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Authorization")
@ApiModel(value="Authorization")
public class Authorization {
    /**
     * security level 1 (returns, exchange, cancellation)
     */
    @XmlElement(name = "secLevel1")
    private boolean secLevel1;
    /**
     * security level 2 (payment)
     */
    @XmlElement(name = "secLevel2")
    private boolean secLevel2;
    /**
     * security level 3 (withdrawal)
     */
    @XmlElement(name = "secLevel3")
    private boolean secLevel3;
    /**
     * security level 4 (payoff)
     */
    @XmlElement(name = "secLevel4")
    private boolean secLevel4;
    /**
     * security level 5 (discount)
     */
    @XmlElement(name = "secLevel5")
    private boolean secLevel5;
    /**
     * security level 6 (price change)
     */
    @XmlElement(name = "secLevel6")
    private boolean secLevel6;
    /**
     * security level 7 (pmagnification)
     */
    @XmlElement(name = "secLevel7")
    private boolean secLevel7;
    /**
     * security level 8 (TBD)
     */
    @XmlElement(name = "secLevel8")
    private boolean secLevel8;
    /**
     * security level 9 Headquarters
     */
    @XmlElement(name = "secLevel9")
    private boolean secLevel9;
    /**
     * security level 10 
     */
    @XmlElement(name = "secLevel10")
    private boolean secLevel10;
    /**
     * security level 11
     */
    @XmlElement(name = "secLevel11")
    private boolean secLevel11;
    /**
     * security level 12 
     */
    @XmlElement(name = "secLevel12")
    private boolean secLevel12;
    /**
     * security level 13 
     */
    @XmlElement(name = "secLevel13")
    private boolean secLevel13;
    /**
     * security level 14 
     */
    @XmlElement(name = "secLevel14")
    private boolean secLevel14;
    /**
     * security level 15 
     */
    @XmlElement(name = "secLevel15")
    private boolean secLevel15;
    /**
     * 
     * @return security level 1
     */
    @ApiModelProperty(value="許可フラグ１", notes="許可フラグ１")
    public boolean isSecLevel1() {
        return secLevel1;
    }
    /**
     * 
     * @param set the security level 
     */
    public void setSecLevel1(boolean secLevel1) {
        this.secLevel1 = secLevel1;
    }
    /**
     * 
     * @return security level 2
     */
    @ApiModelProperty(value="許可フラグ２", notes="許可フラグ２")
    public boolean isSecLevel2() {
        return secLevel2;
    }
    /**
     * 
     * @param set the security level 2
     */
    public void setSecLevel2(boolean secLevel2) {
        this.secLevel2 = secLevel2;
    }
    /**
     * 
     * @return security level 3
     */
    @ApiModelProperty(value="許可フラグ３", notes="許可フラグ３")
    public boolean isSecLevel3() {
        return secLevel3;
    }
    /**
     * 
     * @param set the security level 3
     */
    public void setSecLevel3(boolean secLevel3) {
        this.secLevel3 = secLevel3;
    }
    /**
     * 
     * @return security level 4
     */
    @ApiModelProperty(value="許可フラグ４", notes="許可フラグ４")
    public boolean isSecLevel4() {
        return secLevel4;
    }
    /**
     * 
     * @param set the security level 4
     */
    public void setSecLevel4(boolean secLevel4) {
        this.secLevel4 = secLevel4;
    }
    /**
     * 
     * @return security level 5
     */
    @ApiModelProperty(value="許可フラグ５", notes="許可フラグ５")
    public boolean isSecLevel5() {
        return secLevel5;
    }
    /**
     * 
     * @param set the security level 5
     */
    public void setSecLevel5(boolean secLevel5) {
        this.secLevel5 = secLevel5;
    }
    /**
     * 
     * @return security level 6
     */
    @ApiModelProperty(value="許可フラグ６", notes="許可フラグ６")
    public boolean isSecLevel6() {
        return secLevel6;
    }
    /**
     * 
     * @param set the security level 6
     */
    public void setSecLevel6(boolean secLevel6) {
        this.secLevel6 = secLevel6;
    }
    /**
     * 
     * @return security level 7
     */
    @ApiModelProperty(value="許可フラグ７", notes="許可フラグ７")
    public boolean isSecLevel7() {
        return secLevel7;
    }
    /**
     * 
     * @param set the security level 7
     */
    public void setSecLevel7(boolean secLevel7) {
        this.secLevel7 = secLevel7;
    }
    /**
     * 
     * @return security level 8
     */
    @ApiModelProperty(value="許可フラグ８", notes="許可フラグ８")
    public boolean isSecLevel8() {
        return secLevel8;
    }
    /**
     *
     * @param set the security level 8
     */
    public void setSecLevel8(boolean secLevel8) {
        this.secLevel8 = secLevel8;
    }
    /**
     * 
     * @return security level 9
     */
    @ApiModelProperty(value="許可フラグ９", notes="許可フラグ９")
    public boolean isSecLevel9() {
        return secLevel9;
    }
    /**
     * 
     * @param set the security level 9
     */
    public void setSecLevel9(boolean secLevel9) {
        this.secLevel9 = secLevel9;
    }
    /**
     * 
     * @return security level 10
     */
    @ApiModelProperty(value="許可フラグ１０", notes="許可フラグ１０")
    public boolean isSecLevel10() {
        return secLevel10;
    }
    /**
     * 
     * @param set the security level 10
     */
    public void setSecLevel10(boolean secLevel10) {
        this.secLevel10 = secLevel10;
    }
    /**
     * 
     * @return security level 11
     */
    @ApiModelProperty(value="許可フラグ１１", notes="許可フラグ１１")
    public boolean isSecLevel11() {
        return secLevel11;
    }
    /**
     * 
     * @param set the security level 11
     */
    public void setSecLevel11(boolean secLevel11) {
        this.secLevel11 = secLevel11;
    }
    /**
     * 
     * @return security level 12
     */
    @ApiModelProperty(value="許可フラグ１２", notes="許可フラグ１２")
    public boolean isSecLevel12() {
        return secLevel12;
    }
    /**
     * 
     * @param set the security level 12
     */
    public void setSecLevel12(boolean secLevel12) {
        this.secLevel12 = secLevel12;
    }
    /**
     * 
     * @return security level 13
     */
    @ApiModelProperty(value="許可フラグ１３", notes="許可フラグ１３")
    public boolean isSecLevel13() {
        return secLevel13;
    }
    /**
     * 
     * @param set the security level 13
     */
    public void setSecLevel13(boolean secLevel13) {
        this.secLevel13 = secLevel13;
    }
    /**
     * 
     * @return security level 14
     */
    @ApiModelProperty(value="許可フラグ１４", notes="許可フラグ１４")
    public boolean isSecLevel14() {
        return secLevel14;
    }
    /**
     * 
     * @param set the security level 14
     */
    public void setSecLevel14(boolean secLevel14) {
        this.secLevel14 = secLevel14;
    }
    /**
     * 
     * @return security level 15
     */
    @ApiModelProperty(value="許可フラグ１５", notes="許可フラグ１５")
    public boolean isSecLevel15() {
        return secLevel15;
    }
    /**
     * 
     * @param set the security level 15
     */
    public void setSecLevel15(boolean secLevel15) {
        this.secLevel15 = secLevel15;
    }
    @Override
    public String toString() {
		return "Authorization [secLevel1=" + secLevel1 + ", secLevel2=" + secLevel2 + ", secLevel3=" + secLevel3
				+ ", secLevel4=" + secLevel4 + ", secLevel5=" + secLevel5 + ", secLevel6=" + secLevel6 + ", secLevel7="
				+ secLevel7 + ", secLevel8=" + secLevel8 + ", secLevel9=" + secLevel9 + ", secLevel10=" + secLevel10
				+ ", secLevel11=" + secLevel11 + ", secLevel12=" + secLevel12 + ", secLevel13=" + secLevel13
				+ ", secLevel14=" + secLevel14 + ", secLevel15=" + secLevel15 + "]";
    }


         

}
