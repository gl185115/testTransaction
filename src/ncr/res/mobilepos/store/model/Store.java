/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * Store
 *
 * Is a Class for Store information.
 *
 */

package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Store")
@ApiModel(value="Store")
public class Store {
	@XmlElement(name = "CompanyId")
	private String companyId;
    /**
     * the id of the store.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;
    /**
     * the name of the store.
     */
    @XmlElement(name = "StoreName")
    private String storeName;
    /**
     * the address of the store.
     */
    @XmlElement(name = "Address")
    private String address;
    /**
     * the telephone number of the store.
     */
    @XmlElement(name = "Tel")
    private String tel;
    /**
     * the site url of the store.
     */
    @XmlElement(name = "Url")
    private String url;
    /**
     * the sales space name of the store.
     */
    @XmlElement(name = "SalesSpaceName")
    private String salesSpaceName;
    /**
     * the name of an event.
     */
    @XmlElement(name = "EventName")
    private String eventName;
    /**
     * an array of ads.
     */
    @XmlElement(name = "Ads")
    private String ads;
    /**
     * path to the electro image.
     */
    @XmlElement(name = "ElectroFilePath")
    private String electroFilePath;
    /**
     * path to stamp tax image.
     */
    @XmlElement(name = "StampTaxFilePath")
    private String stampTaxFilePath;

    private String updOpeCode;

    private String updAppId;

    @ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCompanyId() {
    	return companyId;
    }

    public final void setCompanyId(String companyId) {
    	this.companyId = companyId;
    }
    /**
     * gets the retail store id.
     *
     * @return String
     */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * sets the retail store id.
     *
     * @param storeID
     *            - the store id
     */
    public final void setRetailStoreID(final String storeID) {
        this.retailStoreID = storeID;
    }

    /**
     * gets the retail store name.
     *
     * @return String
     */
    @ApiModelProperty(value="店舗名", notes="店舗名")
    public final String getStoreName() {
        return storeName;
    }

    /**
     * sets the retail store id.
     *
     * @param name
     *            - the store name
     */
    public final void setStoreName(final String name) {
        this.storeName = name;
    }

    /**
     * gets the retail store address.
     *
     * @return String
     */
    @ApiModelProperty(value="アドレス", notes="アドレス")
    public final String getAddress() {
        return address;
    }

    /**
     * sets the retail store address.
     *
     * @param addr
     *            - the store address
     */
    public final void setAddress(final String addr) {
        this.address = addr;
    }

    /**
     * gets the retail store telephone number.
     *
     * @return String
     */
    @ApiModelProperty(value="電話番号", notes="電話番号")
    public final String getTel() {
        return tel;
    }

    /**
     * sets the retail store telephone number.
     *
     * @param telNum
     *            - the telephone number
     */
    public final void setTel(final String telNum) {
        this.tel = telNum;
    }

    /**
     * gets the retail store url.
     *
     * @return String
     */
    @ApiModelProperty(value="URL", notes="URL")
    public final String getUrl() {
        return url;
    }

    /**
     * sets the retail store url.
     *
     * @param urlAddr
     *            - the site url
     */
    public final void setUrl(final String urlAddr) {
        this.url = urlAddr;
    }

    /**
     * gets the retail store sales space name.
     *
     * @return String
     */
    @ApiModelProperty(value="販売スペース名", notes="販売スペース名")
    public final String getSalesSpaceName() {
        return salesSpaceName;
    }

    /**
     * sets the retail store sales space name.
     *
     * @param name
     *            - name of the sales space
     */
    public final void setSalesSpaceName(final String name) {
        this.salesSpaceName = name;
    }

    /**
     * gets the retail store event.
     *
     * @return String
     */
    @ApiModelProperty(value="イベント名", notes="イベント名")
    public final String getEventName() {
        return eventName;
    }

    /**
     * sets the retail store event.
     *
     * @param event
     *            - name of the event
     */
    public final void setEventName(final String event) {
        this.eventName = event;
    }

    /**
     * gets the retail store ads.
     *
     * @return String
     */
    @ApiModelProperty(value="レシート広告文", notes="レシート広告文")
    public final String getAds() {
        return ads;
    }

    /**
     * sets the retail store ads.
     *
     * @param storeADS
     *            - ADS
     */
    public final void setAds(final String storeADS) {
        this.ads = storeADS;
    }

    /**
     * gets the retail store file path to electro image.
     *
     * @return String
     */
    @ApiModelProperty(value="レシートロゴファイルパス", notes="レシートロゴファイルパス")
    public final String getElectroFilePath() {
        return electroFilePath;
    }

    /**
     * sets the retail store file path to electro image.
     *
     * @param filePath
     *            - file to path
     */
    public final void setElectroFilePath(final String filePath) {
        this.electroFilePath = filePath;
    }

    /**
     * gets the retail store file path to stamp tax image.
     *
     * @return String
     */
    @ApiModelProperty(value="印紙ロゴファイルパス", notes="印紙ロゴファイルパス")
    public final String getStampTaxFilePath() {
        return stampTaxFilePath;
    }

    /**
     * sets the retail store file path to stamp tax image.
     *
     * @param filePath
     *            - path to file
     */
    public final void setStampTaxFilePath(final String filePath) {
        this.stampTaxFilePath = filePath;
    }

    @ApiModelProperty(value="更新アプリケーションID", notes="更新アプリケーションID")
    public String getUpdAppId() {
        return updAppId;
    }

    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }

    @ApiModelProperty(value="更新担当者コード", notes="更新担当者コード")
    public String getUpdOpeCode() {
        return updOpeCode;
    }

    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }

}
