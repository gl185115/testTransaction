/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * PaperReceiptHeader Class is a Model representation of the
 * information which identifies the header of paper receipt.
 */
public class PaperReceiptHeader {
    /**
     * logo image.
     */
    private BufferedImage logoImg;
    /**
     * address of the store.
     */
    private String address;
    /**
     * telephone number of the store.
     */
    private String tel;
    /**
     * list of commercials/ads.
     */
    private List<String> commercialList;
    /**
     * url og the store website.
     */
    private String siteUrl;

    /**
     * default constructor.
     */
    public PaperReceiptHeader() {
        this.logoImg = null;
        this.commercialList = new ArrayList<String>();
    }

    /**
     * constructor.
     * @param logoImgToSet - logo image
     * @param addressStr - address of the store
     * @param telStr - telephone number of the store
     * @param siteUrlStr - website url of the store
     * @param commercialListToSet - list of commercials
     */
    public PaperReceiptHeader(final BufferedImage logoImgToSet,
                            final String addressStr,
                            final String telStr,
                            final String siteUrlStr,
                            final List<String> commercialListToSet) {
        this.logoImg = logoImgToSet;
        this.address = addressStr;
        this.tel = telStr;
        this.siteUrl = siteUrlStr;
        this.commercialList = commercialListToSet;
    }

    /**
     * @return logoImg
     */
    public final BufferedImage getLogoImg() {
        return logoImg;
    }

    /**
     * @param logoImgToSet  logoImg
     */
    public final void setLogoImg(final BufferedImage logoImgToSet) {
        this.logoImg = logoImgToSet;
    }

    /**
     * @return address
     */
    public final String getAddress() {
        return address;
    }

    /**
     * @param addressToSet  address
     */
    public final void setAddress(final String addressToSet) {
        this.address = addressToSet;
    }

    /**
     * @return tel
     */
    public final String getTel() {
        return tel;
    }

    /**
     * @param telToSet  tel
     */
    public final void setTel(final String telToSet) {
        this.tel = telToSet;
    }

    /**
     * @return commercialList
     */
    public final List<String> getCommercialList() {
        return commercialList;
    }

    /**
     * @param commercialListToSet  commercialList
     */
    public final void setCommercialList(
            final List<String> commercialListToSet) {
        this.commercialList = commercialListToSet;
    }

    /**
     * @return siteUrl
     */
    public final String getSiteUrl() {
        return siteUrl;
    }

    /**
     * @param siteUrlToSet  siteUrl
     */
    public final void setSiteUrl(final String siteUrlToSet) {
        this.siteUrl = siteUrlToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address=" + address);
        sb.append("\nCommercial List Count=" + commercialList.size());
        sb.append("\nSite Url=" + siteUrl);
        sb.append("\nTelephone Number=" + tel);
        return sb.toString();
    }
}
