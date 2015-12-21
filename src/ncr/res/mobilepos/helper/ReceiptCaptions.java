   /*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * ReceiptCaptions
    *
    * Provides captions for receipt in English or Japanese
    *
    * Chris Meneses
    */
package ncr.res.mobilepos.helper;
/**
 * Provides captions for Receipt.
 *
 */
public class ReceiptCaptions {

    /**
     * flag for language to use.
     */
    private Languages language;

    /**
     * constructor.
     * @param languageToSet - language code of language to set
     */
    public ReceiptCaptions(final String languageToSet) {
        try {
            this.language = Languages.valueOf(languageToSet.toLowerCase());
        } catch (Exception e) {
            this.language = Languages.unknown;
        }
    }

    /**
     * Email Subject of receipt.
     * @return localized subject of the email
     */
    public final String getEmailSubject() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Receipt of Transaction %s";
            break;
        /*Japanese*/
        case ja:
            caption = "取引レシート %s";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Customer ID.
     * @return String - the localized customer id caption
     */
    public final String getCustomerID() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Customer ID";
            break;
        /*Japanese*/
        case ja:
            caption = "会員番号";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Device Number.
     * @return String - the localized device number caption
     */
    public final String getDeviceNumber() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Dev#";
            break;
        /*Japanese*/
        case ja:
            caption = "デバイス番号";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Transaction Number.
     * @return String - the localized transaction number caption
     */
    public final String getTransactionNumber() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Tx#";
            break;
        /*Japanese*/
        case ja:
            caption = "取引番号";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Date.
     * @return String - the localized date caption
     */
    public final String getDate() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Date";
            break;
        /*Japanese*/
        case ja:
            caption = "取引日時";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Sub Total.
     * @return String - the localized subtotal caption
     */
    public final String getSubTotal() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Sub Total";
            break;
        /*Japanese*/
        case ja:
            caption = "小 計";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Discount.
     * @return String - the localized discount caption
     */
    public final String getDiscount() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Discount";
            break;
        /*Japanese*/
        case ja:
            caption = "割 引";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Total Amount.
     * @return String - the localized total amount caption
     */
    public final String getTotalAmount() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Total Amount";
            break;
        /*Japanese*/
        case ja:
            caption = "合 計";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Tax.
     * @return String - the localized tax caption
     */
    public final String getTax() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Tax";
            break;
        /*Japanese*/
        case ja:
            caption = "内税";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Cash.
     * @return String - the localized cash caption
     */
    public final String getCash() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Cash";
            break;
        /*Japanese*/
        case ja:
            caption = "お 預";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Change.
     * @return String - the localized change caption
     */
    public final String getChange() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Change";
            break;
        /*Japanese*/
        case ja:
            caption = "お 釣";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Credit.
     * @return String - the localized credit caption
     */
    public final String getCredit() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Credit Card";
            break;
        /*Japanese*/
        case ja:
            caption = "クレジット";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Member Number.
     * @return String - the localized member number caption
     */
    public final String getMemberNumber() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Member No.";
            break;
        /*Japanese*/
        case ja:
            caption = "カード番号";
            break;
        default:
            break;
        }

        return caption;
    }

    /**
     * Approval Nunmber.
     * @return String - the localized approval number caption
     */
    public final String getApprovalNumber() {
        String caption = "";

        switch (language) {
        /*English*/
        case en:
            caption = "Approval No.";
            break;
        /*Japanese*/
        case ja:
            caption = "承認番号";
            break;
        default:
            break;
        }

        return caption;
    }
}
