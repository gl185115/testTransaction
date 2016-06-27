/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * Operator
 *
 * A Model Class for Operator
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.credential.model;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01   2014.12.04    LIQIAN   操作員状態取得
 */

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Operator is a Model Class that represents an Operator Signed On.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Details")
@ApiModel(value="Operator")
public class Operator extends ResultBase {
    /**
     * Operator number.
     */
    @XmlElement(name = "OperatorNo")
    private String operatorNo;
    /**
     * Operator name.
     */
    @XmlElement(name = "Name")
    private String name;
    /**
     * Response code.
     */
    @XmlElement(name = "Response")
    private String response;
    /**
     * DateTime when the operator signon.
     */
    @XmlElement(name = "SignOnAt")
    private String signOnAt;
    /**
     * DateTime when the operator signon.
     */
    @XmlElement(name = "Date")
    private String date;
    /**
     * Operator's password code number.
     */
    @XmlElement(name = "PassCode")
    private String passcode;
    /**
     * Operator's Type.
     */
    @XmlElement(name = "OperatorType")
    private int operatorType;
    /** The Days before the Password would expire.  */
    @XmlElement(name = "DaysPasswordBeforeExpire")
    private String daysPwdBeforeExpire;
    /**
     * The User Group {@link Permissions}.
     */
    @XmlElement(name = "Permissions")
    private Permissions permissions;
    /* 1.01 2014.12.04 操作員状態取得 ADD START*/
    /**
     * オペレータ  カナ名.
     */
    @XmlElement(name = "OperatorKanaName")
    private String opeKananame;
    /**
     * オペレータ   セキュリティレベル.
     */
    @XmlElement(name = "Securitylevel")
    private String securitylevel;
    /* 1.01 2014.12.04 操作員状態取得 ADD END*/

    /**
     * security level
     */
    @XmlElement(name = "Authorization")
    private Authorization authorization;

    @XmlAttribute(name = "OperatorNameKana")
    private String opeNameKana;

    private String updAppId;

    private String updOpeCode;

    /**
     * Spart Operator Not Found.
     */
    public static final int OPERATOR_NOT_FOUND = -1;
    /**
     * Spart Operator status for Inactive.
     */
    public static final int STATUS_INACTIVE = 0;
    /**
     * Spart Operator status for Active.
     */
    public static final int STATUS_ACTIVE = 1;
    /**
     * Spart Operator status for Deleted.
     */
    public static final int STATUS_DELETED = 2;

    /**
     * @return the daysPwdBeforeExpire
     */
    @ApiModelProperty(value="パスワード有効期", notes="パスワード有効期")
    public final String getDaysPwdBeforeExpire() {
        return daysPwdBeforeExpire;
    }
    /**
     * @param daysPwdBeforeExpireToSet the daysPwdBeforeExpire to set
     */
    public final void setDaysPwdBeforeExpire(
            final String daysPwdBeforeExpireToSet) {
        this.daysPwdBeforeExpire = daysPwdBeforeExpireToSet;
    }
    /**
     * Operator status for offline.
     */
    public static final int STATUS_OFFLINE = 0;
    /**
     * Operator status for online.
     */
    public static final int STATUS_ONLINE = 1;

    /**
     * Gets the operator password code number.
     *
     * @return passcode of an operator.
     */
    @ApiModelProperty(value="パスワード", notes="パスワード")
    public final String getPasscode() {
        return passcode;
    }

    /**
     * Sets the new operator password code number.
     *
     * @param passcodeToSet
     *            new passcode number.
     */
    public final void setPasscode(final String passcodeToSet) {
        this.passcode = passcodeToSet;
    }

    /**
     * Gets the date when the operator sign-on.
     *
     * @return date sign-on.
     */
    @ApiModelProperty(value="ログイン日", notes="ログイン日")
    public final String getDate() {
        return date;
    }

    /**
     * Sets the date when the operator sign-on.
     *
     * @param dateToSet
     *            date sign-on.
     */
    public final void setDate(final String dateToSet) {
        this.date = dateToSet;
    }

    @ApiModelProperty(hidden=true)
    public String getUpdAppId() {
        return updAppId;
    }

    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }

    @ApiModelProperty(hidden=true)
    public String getUpdOpeCode() {
        return updOpeCode;
    }

    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }

    @ApiModelProperty(value="従業員カナ氏名", notes="従業員カナ氏名")
    public String getOpeNameKana() {
    	return this.opeNameKana;
    }
    public void setOpeNameKana(String str) {
    	this.opeNameKana = str;
    }

    /**
     * Gets the operator signon datetime.
     *
     * @return datetime when the operator sign-on.
     */
    @ApiModelProperty(value="ログイン日時", notes="ログイン日時")
    public final String getSignOnAt() {
        return signOnAt;
    }

    /**
     * Sets the operator signon datetime.
     *
     * @param signOnAtToSet
     *            datetime the operator sign-on.
     */
    public final void setSignOnAt(final String signOnAtToSet) {
        this.signOnAt = signOnAtToSet;
    }
    /**
     * Gets response.
     *
     * @return response.
     */
    @ApiModelProperty(value="レスポンス", notes="レスポンス")
    public final String getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param responseToSet
     *            response.
     */
    public final void setResponse(final String responseToSet) {
        this.response = responseToSet;
    }

    /**
     * Gets the operator number.
     *
     * @return operator number.
     */
    @ApiModelProperty(value="従業員番号", notes="従業員番号")
    public final String getOperatorNo() {
        return operatorNo;
    }

    /**
     * Sets operator number.
     *
     * @param operatorNoToSet
     *            operator number to set.
     */
    public final void setOperatorNo(final String operatorNoToSet) {
        this.operatorNo = operatorNoToSet;
    }

    /**
     * Gets operator's name.
     *
     * @return name.
     */
    @ApiModelProperty(value="従業員氏名", notes="従業員氏名")
    public final String getName() {
        return name;
    }

    /**
     * Sets operator's name.
     *
     * @param nameToSet
     *            operato's name to set.
     */
    public final void setName(final String nameToSet) {
        this.name = nameToSet;
    }

    /**
     * Helper method to check string with alpha characters.
     *
     * @param str
     *            The string to check.
     * @return true if has alpha, otherwise false.
     */
    public static boolean hasAlpha(final String str) {
        Pattern p = Pattern.compile("[^0-9]");
        return p.matcher(str).find();
    }
    /**
     * The Getter for Operator Type.
     * @return  The Operator type.
     */
    @ApiModelProperty(value="従業員区分", notes="従業員区分")
    public final int getOperatorType() {
        return operatorType;
    }
    /**
     * The Setter for Operator Type.
     * @param operatorTypeToSet the Operator Type.
     */
    public final void setOperatorType(final int operatorTypeToSet) {
        this.operatorType = operatorTypeToSet;
    }

    /**
     * @return the permissions
     */
    @ApiModelProperty(value="従業員権限情報", notes="従業員権限情報")
    public final Permissions getPermissions() {
        return permissions;
    }

    /**
     * @param permissionsToSet the permissions to set
     */
    public final void setPermissions(final Permissions permissionsToSet) {
        this.permissions = permissionsToSet;
    }
    /* 1.01 2014.12.04 操作員状態取得 ADD START*/
    /**
     * get the operator kananame
     * @return opeKananame
     */
    @ApiModelProperty(value="従業員カナ氏名", notes="従業員カナ氏名")
    public final String getOpeKananame() {
        return opeKananame;
    }
    /**
     * set the operator kananame
     * @param opeKananame
     */
    public final void setOpeKananame(String opeKananame) {
        this.opeKananame = opeKananame;
    }
    /**
     * get the operator Security level
     * @return securitylevel
     */
    @ApiModelProperty(value="セキュリティレベル", notes="セキュリティレベル")
    public final String getSecuritylevel() {
        return securitylevel;
    }

    /**
     * set the operator Security level
     * @param securitylevel
     */
    public final void setSecuritylevel(String securitylevel) {
        this.securitylevel = securitylevel;
    }
    /* 1.01 2014.12.04 操作員状態取得 ADD END*/

    /**
     * set the operator security level
     * @param authorization
     */
    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    @ApiModelProperty(value="従業員認証権限", notes="従業員認証権限")
    public Authorization getAuthorization() {
        return this.authorization;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("; OperatorNo: ").append(operatorNo);
        sb.append("; Date: ").append(date);
        sb.append("; Name: ").append(name);
        sb.append("; Response: ").append(response);
        sb.append("; SignOnAt: ").append(signOnAt);
        /* 1.01 2014.12.04 操作員状態取得 ADD START*/
        sb.append("; OperatorKananame: ").append(opeKananame);
        sb.append("; Securitylevel: ").append(securitylevel);
        /* 1.01 2014.12.04 操作員状態取得 ADD END*/

        return sb.toString();
    }
}
