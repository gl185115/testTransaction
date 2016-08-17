package ncr.res.mobilepos.networkreceipt.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.res.mobilepos.helper.PrintFormatter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptContent;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMixMatchBlock;
import ncr.res.mobilepos.networkreceipt.model.ReceiptProductItem;

public class VoidReceiptFormatter extends PrintFormatter {

    public VoidReceiptFormatter(int lineMax) {
        super(lineMax);
    }
    
    public final List<String> getVoidReceiptFormat(
            final PaperReceiptHeader header, final PaperReceipt receipt,
            final PaperReceiptFooter footer, final String language)
                    throws UnsupportedEncodingException {
        if (header == null || receipt == null || footer == null) {
            return new ArrayList<String>();
        }
        //if it did not set language, default is Japanese
        Locale locale = null;
        if (language == null || !"en".equals(language)) {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);
        List<String> lines = new ArrayList<String>();
        
        // Add header
        addTextCenterAlign(lines, header.getSiteUrl());// URL
        addTextCenterAlign(lines, header.getAddress());// Address
        addTextCenterAlign(lines, header.getTel());// Tel number
        // Ads
        for (int i = 0; i < header.getCommercialList().size(); i++) {
            addTextCenterAlign(lines, header.getCommercialList().get(i));
        }
        addSeparator(lines, " ");
        
        PaperReceiptContent content = receipt.getReceiptContent();
        
        // void date time
        addTextLeftAlign(lines,
                getReceiptDateTime(content.getVoidDateTime(), language));
        
        addSeparator(lines, " ");

        addTextLeftAlign(lines, rb.getString("recptvoid"));
        addSeparator(lines, " ");
        addTextLeftAlign(lines,
                getReceiptDateTime(content.getReceiptDateTime(), language));
        addSeparator(lines, " ");
        // Add items
        for (ReceiptProductItem item : content.getProductItemList()) {
            //add item name
            addTextLeftAlign(lines, item.getProductName());
            //add item quantity and price and amount
            String qtyprice = "  " + getNumberWithComma(language,
                    item.getPrice())
                              + " * " + item.getQuantity()
                              + rb.getString("recptunit");
            addItemStr(lines, qtyprice,
                    getNumberWithComma(language, item.getAmount()));
            //add item discount
            if (item.getDiscountAmount() > 0) {
                String discountAmt =
                    "-" + getNumberWithComma(language,
                            Double.valueOf(item.getDiscountAmount()));
                addItemStr(lines, "    " + rb.getString("recptitemdiscount"),
                        discountAmt);
            }
        }
        addSeparator(lines, " ");
        // Add mix match blocks
        if(content.getMmBlockList() != null){
            for(ReceiptMixMatchBlock block : content.getMmBlockList()){
                // Add Mix Match name.
                addTextLeftAlign(lines, "<" + block.getMmName() + ">");
                // Add Mix Match items.
                for(ReceiptProductItem item : block.getProductItemList()){
                    // Add item name
                    addTextLeftAlign(lines, item.getProductName());
                    // Add price*quantity    amount
                    String qtyprice = "  " + getNumberWithComma(language,
                            item.getPrice())
                                      + " * " + item.getQuantity()
                                      + rb.getString("recptunit");
                    addItemStr(lines, qtyprice,
                            getNumberWithComma(language, item.getAmount()));
                    //add item discount
                    if (item.getDiscountAmount() > 0) {
                        String discountAmt =
                            "-" + getNumberWithComma(language,
                                    Double.valueOf(item.getDiscountAmount()));
                        addItemStr(lines,
                                "    " + rb.getString("recptitemdiscount"),
                                discountAmt);
                    }
                }
                // Add Mix Match aggregate info
                // Add Discountable
                addItemStr(lines,
                           rb.getString("MMDiscountable")
                           + "  " + block.getMmItemCount()
                           + rb.getString("recptunit"),
                           getNumberWithComma(language,
                                block.getMmPreviousPrice()));
                // Add discount
                addItemStr(lines, rb.getString("MMDiscount"),
                        "-" + getNumberWithComma(language,
                                block.getMmAmount()));
                // Add sales price
                addItemStr(lines, rb.getString("MMSalesPrice"), 
                        getNumberWithComma(language,
                                block.getMmItemsSalesPrice()));
                // Add space line
                addSeparator(lines, " ");
            }
        }
        
        // Add sub total
        addItemStr(lines, rb.getString("recptsubtotal"),
                getCurrencySymbol(language, content.getSubTotal()));
        // Add discount
        String discount = content.getDiscount();
        if (discount != null) {
            addItemStr(lines, rb.getString("recptmemberDiscount"),
                    "-" + getCurrencySymbol(language, Long.valueOf(discount)));
        }
        addSeparator(lines, " ");
        
        // Add total
        addItemStr(lines, rb.getString("recpttotal"),
                getCurrencySymbol(language, content.getTotal()));
        // Add tax
        addItemStr(lines, "(" + rb.getString("recpttax"),
                getCurrencySymbol(language,
                        Long.valueOf(content.getTax())) + ")");
        // Add payment
        addItemStr(lines, rb.getString("recptpayment")
                + "  " + rb.getString("recptcash"),
                getCurrencySymbol(language, content.getPaymentCash()));
        String paymentlength = getSpaces(
                rb.getString("recptpayment").getBytes("MS932").length);
        addItemStr(lines, paymentlength + "  " + rb.getString("recptchange"),
                getCurrencySymbol(language, content.getPaymentChange()));
        // Add miscellaneous tender
        if (content.getPaymentVoucher() != 0) {
            String paymentMics = getCurrencySymbol(language,
                    content.getPaymentVoucher());
            addItemStr(lines, "  " + rb.getString("recptmics"), paymentMics);
        }
        addSeparator(lines, " ");
        addTextLeftAlign(lines, "・・・・・・・・・・・・・・・・");
        
        // Add primary transaction footer
        PaperReceiptFooter primaryFooter = receipt.getReceiptFooter();
        addTextLeftAlign(lines, rb.getString("recptstorename")
                + ":" + primaryFooter.getShopName());
        addTextLeftAlign(lines, rb.getString("recptoperatorname")
                + ":" + primaryFooter.getSaleMan());
        addTextLeftAlign(lines, rb.getString("recptterminalid")
                + ":" + primaryFooter.getRegisterNum());
        addTextLeftAlign(lines, rb.getString("recpttransactionid")
                + ":" + primaryFooter.getTradeNum());
        if (content.getCustomerTierCode() != null &&
                !"".equals(content.getCustomerTierCode())) {
            addTextLeftAlign(lines, rb.getString("recptcusttier")
                    + ":" + content.getCustomerTierCode()
                    + " " + content.getCustomerTierName());
        }
        addSeparator(lines, " ");
        addTextLeftAlign(lines, rb.getString("recptvoid"));
        addSeparator(lines, " ");
        addUnderbar(lines, rb.getString("recptreason"));
        addSeparator(lines, " ");
        addUnderbar(lines, rb.getString("recptmgtapproval"));
        addSeparator(lines, " ");
        
        // Add void footer
        addTextLeftAlign(lines, rb.getString("recptstorename")
                + ":" + footer.getShopName());
        addTextLeftAlign(lines, rb.getString("recptoperatorname")
                + ":" + footer.getSaleMan());
        addTextLeftAlign(lines, rb.getString("recptterminalid")
                + ":" + footer.getRegisterNum());
        addTextLeftAlign(lines, rb.getString("recpttransactionid")
                + ":" + footer.getTradeNum());
        if (footer.getDepartmentName() != null
                && !"".equals(footer.getDepartmentName())) {
            //add department name
            addTextLeftAlign(lines, rb.getString("recptsalesspacename")
                    + ":" + footer.getDepartmentName());
        }
        if (footer.getHoldName() != null
                && !"".equals(footer.getHoldName())) {
            //add event name
            addTextLeftAlign(lines, rb.getString("recpteventname")
                    + ":" + footer.getHoldName());
        }
        return lines;
    }

}
