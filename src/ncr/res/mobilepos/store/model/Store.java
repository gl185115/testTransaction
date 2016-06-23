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

    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
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
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
    @ApiModelProperty(value="�X�ܖ�", notes="�X�ܖ�")
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
    @ApiModelProperty(value="�A�h���X", notes="�A�h���X")
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
    @ApiModelProperty(value="�d�b�ԍ�", notes="�d�b�ԍ�")
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
    @ApiModelProperty(value="�̔��X�y�[�X��", notes="�̔��X�y�[�X��")
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
    @ApiModelProperty(value="�C�x���g��", notes="�C�x���g��")
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
    @ApiModelProperty(value="���V�[�g�L����", notes="���V�[�g�L����")
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
    @ApiModelProperty(value="���V�[�g���S�t�@�C���p�X", notes="���V�[�g���S�t�@�C���p�X")
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
    @ApiModelProperty(value="�󎆃��S�t�@�C���p�X", notes="�󎆃��S�t�@�C���p�X")
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

    @ApiModelProperty(value="�X�V�A�v���P�[�V����ID", notes="�X�V�A�v���P�[�V����ID")
    public String getUpdAppId() {
        return updAppId;
    }

    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }

    @ApiModelProperty(value="�X�V�S���҃R�[�h", notes="�X�V�S���҃R�[�h")
    public String getUpdOpeCode() {
        return updOpeCode;
    }

    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }

}
