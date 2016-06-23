package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
     *
     * @return security level 1
     */
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚P", notes="‹–‰Âƒtƒ‰ƒO‚P")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚Q", notes="‹–‰Âƒtƒ‰ƒO‚Q")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚R", notes="‹–‰Âƒtƒ‰ƒO‚R")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚S", notes="‹–‰Âƒtƒ‰ƒO‚S")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚T", notes="‹–‰Âƒtƒ‰ƒO‚T")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚U", notes="‹–‰Âƒtƒ‰ƒO‚U")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚V", notes="‹–‰Âƒtƒ‰ƒO‚V")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚W", notes="‹–‰Âƒtƒ‰ƒO‚W")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚X", notes="‹–‰Âƒtƒ‰ƒO‚X")
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
    @ApiModelProperty(value="‹–‰Âƒtƒ‰ƒO‚P‚O", notes="‹–‰Âƒtƒ‰ƒO‚P‚O")
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
    @Override
    public String toString() {
        return "Authorization [secLevel1=" + secLevel1 + ", secLevel2=" + secLevel2 + ", secLevel3=" + secLevel3
                + ", secLevel4=" + secLevel4 + ", secLevel5=" + secLevel5 + ", secLevel6=" + secLevel6 + ", secLevel7="
                + secLevel7 + ", secLevel8=" + secLevel8 + ", secLevel9=" + secLevel9 + ", secLevel10=" + secLevel10
                + "]";
    }




}
