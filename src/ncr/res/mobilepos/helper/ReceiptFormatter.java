   /*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * ReceiptFormatter
    *
    * Helper class for receipt formatting
    *
    * Jessel G. De la Cerna
    */

package ncr.res.mobilepos.helper;

import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.res.mobilepos.consolidation.constant.TransactionVariable;
import ncr.res.mobilepos.journalization.model.poslog.Authorization;
import ncr.res.mobilepos.journalization.model.poslog.CreditDebit;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptContent;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;
import ncr.res.mobilepos.networkreceipt.model.ReceiptLine;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMixMatchBlock;
import ncr.res.mobilepos.networkreceipt.model.ReceiptProductItem;

/**
 * ReceiptFormatter is a helper class that generates
 * a receipt for a given Normal transaction.
 */
public class ReceiptFormatter {
    /**
     * maximum line number of small receipt.
     */
    private static final int MAX_LINE_NUM_SMALL = 35;
    /**
     * maximum line number of big receipt.
     */
    private static final int MAX_LINE_NUM_BIG = 35;
    /** The Percentage value. */
    private static final int FULL_PERCENT = 100;
    /** Small Font Size. */
    private static final int FONT_SMALL = 10;
    /** Big Font Size. */
    private static final int FONT_BIG = 12;

    /**
     * Helper method of toReceiptFormat that will format the credit printing
     * 
     * @param isCredit
     * @param creditDebit
     * @param authorization
     * @return sb
     */
    private static String creditReceiptFormat(boolean isCredit, 
    		CreditDebit creditDebit, Authorization authorization) {
        StringBuilder sb = new StringBuilder("");
    	if (isCredit) {
            String hideMemNumber =
                creditDebit.getCreditCardCompanyCode() + "************";
            sb.append("<p style='border-top:none; border-right:none;"
                    + " border-left:none; border-bottom: 1px solid #000;"
                    + " width: 200px;' /></p><table style='font-size:10px;'>");
            String lineTRTemp = formatLineTR("Member No.", hideMemNumber);
            sb.append(lineTRTemp);
            lineTRTemp = formatLineTR("Approval No.",
                        authorization.getAuthorizationCode());
            sb.append(lineTRTemp);
        }
    	
    	return sb.toString();
    }

    /**
     * Helper method of toReceiptFormat that will format the credit printing
     * 
     * @param isCredit
     * @param creditDebit
     * @param authorization
     * @return sb
     */
    private String creditReceiptFormat(boolean isCredit, final String creditNum, 
    		 Authorization authorization, String language) {
        StringBuilder sb = new StringBuilder("");
        ReceiptCaptions caption = new ReceiptCaptions(language);
    	if (isCredit) {
    		String hideMemNumber = toEncryption(creditNum);
            sb.append("<p style='border-top:none; border-right:none;"
                    + " border-left:none; border-bottom: 1px solid #000;"
                    + " width: 200px;' /></p><table style='font-size:10px;'>");
            String lineTRTemp = formatLineTR(caption.getMemberNumber(), hideMemNumber);
            sb.append(lineTRTemp);
            lineTRTemp = formatLineTR(caption.getApprovalNumber(),
                        authorization.getAuthorizationCode());
            sb.append(lineTRTemp);
        }
    	return sb.toString();
    }
    
    /**
     * Helper method of toReceiptFormatMail that will format the credit printing
     * 
     * @param isCredit
     * @param lineTR
     * @param creditDebit
     * @param authorization
     * @return sb
     */
    private static String creditMailReceiptFormat(boolean isCredit, String lineTR,
    		CreditDebit creditDebit, Authorization authorization) {
    	StringBuilder sb = new StringBuilder("");
        if (isCredit) {
            String hideMemNumber =
                creditDebit.getCreditCardCompanyCode()
                    + "************";
            sb.append("<table style='font-size:10px;'>");
            String lineTRTemp = lineTR.replace("Description", "Member No.");
            lineTRTemp = lineTRTemp.replace("DescValue", hideMemNumber);
            sb.append(lineTRTemp);
            lineTRTemp = lineTR.replace("Description", "Approval No.");
            lineTRTemp = lineTRTemp.replace("DescValue",
                    authorization.getAuthorizationCode());
            sb.append(lineTRTemp);
        }

        return sb.toString();
    }
    
    /**
     * Helper method that will format the line printing.
     * 
     * @param desc
     * @param descValue
     * @return lineTRTemp
     */
    private static String formatLineTR(String desc, String descValue) {
        String lineTR = "<tr><td style='width:90px;'>Description</td>"
                + "<td style='width:110px;text-align:right;'>DescValue</td></tr>\n";
        String lineTRTemp = lineTR.replace("Description", desc);
        lineTRTemp = lineTRTemp.replace("DescValue", descValue);    	
    	return lineTRTemp;
    }
    
    /**
     * @param desc
     * @param descValue
     * @param lineTR
     * @return lineTRTemp
     */
    private static String formatLineTR(String desc, String descValue, String lineTR) {
        String lineTRTemp = lineTR.replace("Description", desc);
        lineTRTemp = lineTRTemp.replace("DescValue", descValue);    	
    	return lineTRTemp;
    }
    /**
     * Helper method that will format the sales item printing
     * 
     * @param lineItem
     * @return saleItemTRTemp
     */
    private static String formatSaleItem(LineItem lineItem, String saleItemTR) {       
        String qtyPrice = lineItem.getSale().getQuantity() > 1
                ? String.valueOf("(" + lineItem.getSale().getQuantity()
                  + " x @" + lineItem.getSale().getActualsalesunitprice()
                    + ")") : "";
            String mdName = lineItem.getSale().getDescription().trim();
            String saleItemTRTemp  = saleItemTR.replace("MdName", mdName);
            saleItemTRTemp  = saleItemTRTemp.replace("QtyPrice", qtyPrice);
            saleItemTRTemp  = saleItemTRTemp.replace("Creditor",
                    String.valueOf(getCurrencySymbol(
                            lineItem.getSale().getExtendedAmt())));

            return saleItemTRTemp;
    }

    /**
     * Helper method that will format the tender printing.
     * 
     * @param lineItem
     * @param totalAmt
     * @return sb
     */
    private static String formatTender(LineItem lineItem, double totalAmt, String taxTR) {  
        StringBuilder sb = new StringBuilder("");        
        String lineTRTemp = "";
        if (totalAmt != 0) {
            lineTRTemp = formatLineTR("Total Amount",
                    String.valueOf(getCurrencySymbol(totalAmt)));
            sb.append(lineTRTemp);
            sb.append(taxTR);
        }
        if (lineItem.getTender().getTenderType()
                .equals(TransactionVariable.CREDITDEBIT)) {
            lineTRTemp = formatLineTR("Credit",
                    String.valueOf(getCurrencySymbol(Long.valueOf(
                            lineItem.getTender().getAmount()))));
            sb.append(lineTRTemp);
        } else {
            if (lineItem.getTender().getTenderType()
                    .equals(TransactionVariable.CASH)) {
                lineTRTemp = formatLineTR("Cash",
                     String.valueOf(getCurrencySymbol(Long.valueOf(
                       lineItem.getTender().getAmount()))));
                sb.append(lineTRTemp);
            }
            if (lineItem.getTender().getTenderChange()
                    .getAmount() != 0) {
               lineTRTemp = formatLineTR("Change",
                   String.valueOf(getCurrencySymbol(Double.valueOf(
                    lineItem.getTender().getTenderChange()
                     .getAmount()))));
                sb.append(lineTRTemp);
            }
        }

    	return sb.toString();
    }
    
    /**
     * @param lineItem
     * @param totalAmt
     * @param taxTR
     * @param lineTR
     * @return sb
     */
    private static String formatTenderEmail(LineItem lineItem, double totalAmt, String taxTR, String lineTR) {  
        StringBuilder sb = new StringBuilder("");        
        String lineTRTemp = "";
        if (totalAmt != 0) {
            lineTRTemp = formatLineTR("Total Amount",
                    String.valueOf(getCurrencySymbol(totalAmt)), lineTR);
            sb.append(lineTRTemp);
            sb.append(taxTR);
        }
        if (lineItem.getTender().getTenderType()
                .equals(TransactionVariable.CREDITDEBIT)) {
            lineTRTemp = formatLineTR("Credit",
                    String.valueOf(getCurrencySymbol(Long.valueOf(
                            lineItem.getTender().getAmount()))), lineTR);
            sb.append(lineTRTemp);
        } else {
            if (lineItem.getTender().getTenderType()
                    .equals(TransactionVariable.CASH)) {
                lineTRTemp = formatLineTR("Cash",
                     String.valueOf(getCurrencySymbol(Long.valueOf(
                       lineItem.getTender().getAmount()))), lineTR);
                sb.append(lineTRTemp);
            }
            if (lineItem.getTender().getTenderChange()
                    .getAmount() != 0) {
               lineTRTemp = formatLineTR("Change",
                   String.valueOf(getCurrencySymbol(Double.valueOf(
                    lineItem.getTender().getTenderChange()
                     .getAmount()))), lineTR);
                sb.append(lineTRTemp);
            }
        }

    	return sb.toString();
    }
    
    /**
     * Helper method that will format the tender printing.
     * 
     * @param lineItem
     * @param totalAmt
     * @return sb
     */
    private static String formatTender(LineItem lineItem, double totalAmt, String taxTR, String language) {  
        StringBuilder sb = new StringBuilder("");
        ReceiptCaptions caption = new ReceiptCaptions(language);
        String lineTRTemp = "";
        if (totalAmt != 0) {
            lineTRTemp = formatLineTR(caption.getTotalAmount(),
                    String.valueOf(getCurrencySymbol(totalAmt)));
            sb.append(lineTRTemp);
            sb.append(taxTR);
        }
        if (lineItem.getTender().getTenderType()
                .equals(TransactionVariable.CREDITDEBIT)) {
            lineTRTemp = formatLineTR(caption.getCredit(),
                    String.valueOf(getCurrencySymbol(Long.valueOf(
                            lineItem.getTender().getAmount()))));
            sb.append(lineTRTemp);
        } else {
            if (lineItem.getTender().getTenderType()
                    .equals(TransactionVariable.CASH)) {
                lineTRTemp = formatLineTR(caption.getCash(),
                     String.valueOf(getCurrencySymbol(Long.valueOf(
                       lineItem.getTender().getAmount()))));
                sb.append(lineTRTemp);
            }
            if (lineItem.getTender().getTenderChange()
                    .getAmount() != 0) {
               lineTRTemp = formatLineTR(caption.getChange(),
                   String.valueOf(getCurrencySymbol(Double.valueOf(
                    lineItem.getTender().getTenderChange()
                     .getAmount()))));
                sb.append(lineTRTemp);
            }
        }

    	return sb.toString();
    }
    
    /**
     * Helper method that will format customer id printing.
     * 
     * @param posLog
     * @return
     */
    private static String formatCustomerID(PosLog posLog) {
    	StringBuilder sb = new StringBuilder("");
        if (posLog.getTransaction().getCustomerid() != null) {
            sb.append("<tr><td>Customer ID:<td><td>"
                    + posLog.getTransaction().getCustomerid() + "<td><tr>\n");
        }   

        return sb.toString();
    }
    
    /**
     * Helper method that will format customer id printing.
     * 
     * @param posLog
     * @return
     */
    private static String formatCustomerID(PosLog posLog, String language) {
    	StringBuilder sb = new StringBuilder("");
    	ReceiptCaptions caption = new ReceiptCaptions(language);
        if (posLog.getTransaction().getCustomerid() != null) {
            sb.append("<tr><td>" + caption.getCustomerID() + ":<td><td>"
                    + posLog.getTransaction().getCustomerid() + "<td><tr>\n");
        } //trim date
        return sb.toString();
    }
    
    /**
     * Helper method of toReceiptFormat that will format the item discount printing.
     * 
     * @param lineItem
     * @param totalAmt
     * @return sb
     */
    private static String getItemDiscountReceiptFormat(LineItem lineItem, double totalAmt) {
    	StringBuilder sb = new StringBuilder("");
        if (lineItem.getDiscount() != null) {
            //Add the Sub total Label
            String lineTRTemp = formatLineTR("Sub total",
                    getCurrencySymbol(totalAmt));
            sb.append(lineTRTemp);
            //Add the Discount Label
            lineTRTemp = formatLineTR("Discount",
            		String.valueOf(getCurrencySymbol(Long.valueOf(
                              lineItem.getDiscount().getAmount()))));
            sb.append(lineTRTemp);
        }
       
    	return sb.toString();
    }
    
    /**
     * Helper method of toMailReceiptFormat that will format the item discount printing.
     * 
     * @param lineItem
     * @param totalAmt
     * @return sb
     */
    private static String getItemDiscountReceiptFormat(LineItem lineItem, double totalAmt, String language) {
    	StringBuilder sb = new StringBuilder("");
    	ReceiptCaptions caption = new ReceiptCaptions(language);
        if (lineItem.getDiscount() != null) {
            //Add the Sub total Label
            String lineTRTemp = formatLineTR(caption.getSubTotal(),
                    getCurrencySymbol(totalAmt));
            sb.append(lineTRTemp);
            //Add the Discount Label
            lineTRTemp = formatLineTR(caption.getDiscount(),
            		String.valueOf(getCurrencySymbol(Long.valueOf(
                              lineItem.getDiscount().getAmount()))));
            sb.append(lineTRTemp);
        }
        
    	return sb.toString();
    }
    
    /**
     * Helper method that will get the item discount.
     * 
     * @param lineItem
     * @param lineTR
     * @return
     */
    private static String getItemDiscountReceiptFormatMail(LineItem lineItem, String lineTR) {
    	StringBuilder sb = new StringBuilder("");
	    if (lineItem.getDiscount() != null) {
	        String lineTRTemp = lineTR.replace("Description", "Discount");
	        lineTRTemp = lineTRTemp.replace("DescValue", String.valueOf(
	                      getCurrencySymbol(Long.valueOf(
	                      lineItem.getDiscount().getAmount()))));
	        sb.append(lineTRTemp);
	    }
	    
	    return sb.toString();
    }
    
    /**
     * Helper method of toReceiptFormat that will format the start tender item.
     * 
     * @param endOfSaleTR
     * @return sb
     */
    private static String startTenderItemReceiptFormat(boolean endOfSaleTR) {
    	StringBuilder sb = new StringBuilder("");
        if (endOfSaleTR) {
            sb.append("</tbody></table>\n<p style='border-top:none;"
                + " border-right:none; border-left:none; border-bottom:"
                + " 1px solid #000; width: 200px;' /></p>\n");
            /* @END: SaleItem in receipt format. */
            /* @START: TenderItem in receipt format. */
            sb.append("<table style='font-size:10px;'>\n");
        }

        return sb.toString();
    }
    
    /**
     * Helper method for start of tender.
     * 
     * @param endOfSaleTR
     * @param lineTR
     * @param totalAmt
     * @return
     */
    private static String startTenderItemReceiptFormatMail(boolean endOfSaleTR,
    		String lineTR, double totalAmt) {
    	StringBuilder sb = new StringBuilder("");
        if (endOfSaleTR) {
           
            sb.append("</tbody></table>\n");
            /* @END: SaleItem in receipt format. */
            /* @START: TenderItem in receipt format. */
            sb.append("<table>\n");
            String lineTRTemp = lineTR.replace("Description", "Sub total");
            lineTRTemp = lineTRTemp.replace("DescValue",
                    getCurrencySymbol(totalAmt));
            sb.append(lineTRTemp);
        }
        
        return sb.toString();
    }
    
    /**
     * Helper method that get the item discount
     * 
     * @param lineItem
     * @return discount
     */
    private static long getItemDiscount(LineItem lineItem) {
    	long discount = 0;
         if (lineItem.getDiscount() != null) {
             discount = Long.valueOf(lineItem.getDiscount().getAmount());
         }

    	return discount;
    }
    
    
    /***
     * Helper Method that will format POSLog object
     * to receipt format using Html Elements.
     *
     * @param     posLog     The POSLog object to be converted into receipt.
     * @return             The Receipt format of the POSlog.
     */
    public static String toReceiptFormat(final PosLog posLog) {
        if (null == posLog || posLog.getTransaction().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        double totalAmt = 0;
        Authorization authorization = null;
        CreditDebit creditDebit = null;
        sb.append("<html>\n<head></head>\n<body>\n"
                + "<table style='font-size:10px;'>\n<tbody>\n");
        sb.append(formatCustomerID(posLog));
        String dateTime = posLog.getTransaction().getBeginDateTime() //trim date
                            .replace("T", " ").replace("-", "/");
        sb.append("<tr><td>Dev#: " + posLog.getTransaction().getWorkStationID()
            + "<td><td>Tx#: " + posLog.getTransaction().getSequenceNo()
            + "<td><tr>\n<tr><td>Date:<td><td>" + dateTime
            + "<td><tr>\n</tbody>\n</table>\n"
            + "<p style='border-top:none; border-right:none; border-left:none;"
            + " border-bottom: 1px solid #000; width: 200px;' /></p><br />\n"
            + "<table style='padding-top:10px; font-size:10px;'>\n<tbody>");
        String taxTR = "<tr><td style='width:90px;text-align:right;'>(Tax</td>"
                + "<td style='width:110px;text-align:right;'>TxAmt)</td></tr>\n";
        String saleItemTR = "<tr><td colspan='3' style='width:inherit;"
                + "text-align:left;'>MdName</td></tr><tr>"
                + "<td style='width:140px;text-align:center;'>QtyPrice</td>"
                + "<td style='width:60px;text-align:right;'>Creditor</td></tr>\n";
        String taxAmt = "";
        String lineTRTemp = "";
        boolean endOfSaleTR = true;
        boolean isCredit = false;
        for (LineItem lineItem : posLog.getTransaction()
                      .getRetailTransaction()
                      .getLineItems()) {
            /* @START: SaleItem in receipt format. */
            if (lineItem.getSale() != null) {
                totalAmt = totalAmt + lineItem.getSale().getExtendedAmt();
                sb.append(formatSaleItem(lineItem, saleItemTR));
            } else {
            	lineTRTemp = startTenderItemReceiptFormat(endOfSaleTR);
            	sb.append(lineTRTemp);
            	endOfSaleTR = false;           
	            lineTRTemp = getItemDiscountReceiptFormat(lineItem, totalAmt);
	            sb.append(lineTRTemp);
	            totalAmt = totalAmt - getItemDiscount(lineItem);
	            if (lineItem.getTender() != null) {
	            	String tender = formatTender(lineItem, totalAmt, taxTR);
	            	sb.append(tender);
	                totalAmt = 0;                   
	            }
	            if (lineItem.getTender() != null && 
	            		(lineItem.getTender().getTenderType()
	                            .equals(TransactionVariable.CREDITDEBIT))) {
	                authorization =
	                        lineItem.getTender().getAuthorization();
	                    creditDebit = lineItem.getTender().getCreditDebit();
	                    isCredit = true;
	            }
	            if (null != lineItem.getTax() && null != lineItem.getTax().get(0)) {
	                taxAmt = getCurrencySymbol(
	                        Long.valueOf(lineItem.getTax().get(0).getAmount()));
	            } /* @END: TenderItem in receipt format. */ 
            }
        }
        sb.append("</table>\n");
        /* @START: DISPLAY MEMBERSHIP NUMBER & APPROV. CODE. */
        lineTRTemp = creditReceiptFormat(isCredit, creditDebit, authorization);
        sb.append(lineTRTemp);
        /* @END: DISPLAY MEMBERSHIP NUMBER & APPROV. CODE. */
        sb.append("</table></body>\n</html>");
         /* Add the taxAmt */
        String receiptStr = sb.toString().replace("TxAmt", taxAmt);    
        return receiptStr;
    }

    /***
     * Get the formatted price value in yen currency.
     *
     * @param amt to format in yen currency
     *
     * @return string of culture's currency symbol e.g. \(￥ 1,000)
     */
    public static String getCurrencySymbol(final double amt) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        DecimalFormat df = (DecimalFormat) nf;
        return df.format(amt);
    }
    /***
     * Get the formatted price value in yen currency.
     *
     * @param amt to format in currency
     * @param language - for now en/ja
     *
     * @return string of culture's currency symbol e.g. \(￥ 1,000)
     */
    public static String getCurrencySymbol(
            final String language, final double amt) {

        Locale locale = null;
        if ( "ja".equals(language)) {
            locale = Locale.JAPAN;
        } else {
            locale = Locale.US;
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        double amount = amt;
        if (!"ja".equals(language)) {
            amount = amt / FULL_PERCENT;
        }
        return df.format(amount);
    }
    /**
     * get the formatted price value with comma.
     * @param amt - amount to format
     * @return e.g. 1000->1,000
     */
    public static String getNumberWithComma(final double amt) {
        DecimalFormat df = new DecimalFormat();
        return df.format(amt);
    }

    /**
     * get the formatted price value with comma.
     * @param amt - amount to format
     * @param language - en/ja determines whether to use decimal
     * @return e.g. 1000->1,000
     */
    public static String getNumberWithComma(
            final String language, final double amt) {
        DecimalFormat df = new DecimalFormat();
        double amount = amt;
        if (!"ja".equals(language)) {
            df.applyPattern("###,###,##0.00");
            amount = amt / FULL_PERCENT;
        }
        return df.format(amount);
    }

    /**
     * format posLog to receipt email format.
     * @param posLog - the posLog object that contains
     * the data to format
     * @return receipt email formatted string
     */
    public static String toReceiptFormatMail(final PosLog posLog) {
        if (null == posLog || posLog.getTransaction().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        double totalAmt = 0;
        Authorization authorization = null;
        CreditDebit creditDebit = null;

        sb.append("<html>\n<body>\n<table>\n<tbody>\n");

        sb.append(formatCustomerID(posLog));
        // trim date
        String dateTime = posLog.getTransaction().getBeginDateTime()
                            .replace("T", " ").replace("-", "/");
        sb.append("<tr><td>Dev#: <td><td>Tx#: "
                + posLog.getTransaction().getSequenceNo()
                + "<td><tr>\n<tr><td>Date:<td><td>"  + dateTime + "<td>"
                + "<tr>\n</tbody>\n</table>\n<table'>\n<tbody>");

        String lineTR = "<tr><td>Description</td><td>DescValue</td></tr>\n";
        String lineTRTemp = lineTR;

        String taxTR = "<tr><td>(Tax</td><td>TxAmt)</td></tr>\n";
        String taxAmt = "";

        String saleItemTR = "<tr><td colspan='3''>MdName</td></tr><tr>"
            + "<td>QtyPrice</td><td>Creditor</td></tr>\n";

        boolean endOfSaleTR = true;
        boolean isCredit = false;

        for (LineItem lineItem
                : posLog.getTransaction()
                        .getRetailTransaction().getLineItems()) {
            /* @START: SaleItem in receipt format. */
            if (lineItem.getSale() != null) {
                totalAmt = totalAmt + lineItem.getSale().getExtendedAmt();
                sb.append(formatSaleItem(lineItem, saleItemTR));
            } else {
            	sb.append(startTenderItemReceiptFormatMail(endOfSaleTR, lineTR, totalAmt));
                endOfSaleTR = false;
                sb.append(getItemDiscountReceiptFormatMail(lineItem, lineTR));
                totalAmt = totalAmt - getItemDiscount(lineItem); 
	            if (lineItem.getTender() != null) {
	            	String tender = formatTenderEmail(lineItem, totalAmt, taxTR, lineTR);
	            	sb.append(tender);
	                totalAmt = 0;                   
	            }
	            if (lineItem.getTender() != null && 
	            		(lineItem.getTender().getTenderType()
	                            .equals(TransactionVariable.CREDITDEBIT))) {
	                authorization =
	                        lineItem.getTender().getAuthorization();
	                    creditDebit = lineItem.getTender().getCreditDebit();
	                    isCredit = true;
	            }	            
                if (null != lineItem.getTax() && null != lineItem.getTax().get(0)) {
                    taxAmt = getCurrencySymbol(
                            Long.valueOf(lineItem.getTax().get(0).getAmount()));
                }
                /* @END: TenderItem in receipt format. */
            }
        }
        sb.append("</table>\n");
        /* @START: DISPLAY MEMBERSHIP NUMBER & APPROV. CODE. */
        lineTRTemp = creditMailReceiptFormat(isCredit, lineTR, creditDebit, authorization);
        sb.append(lineTRTemp);
        /* @END: DISPLAY MEMBERSHIP NUMBER & APPROV. CODE. */
        sb.append("</table></body>\n</html>");
         /* Add the taxAmt */
        String receiptStr = sb.toString().replace("TxAmt", taxAmt);        
        return receiptStr;
    }

    /***
     * Helper Method that will format POSLog object.
     * to receipt format using Html Elements
     *
     * @param     posLog     The POSLog object to be converted into receipt.
     * @param     creditNum  The creditNum used in the transaction.
     * @param     language   The POSLog object to be converted into receipt.
     * @return             The Receipt format of the POSlog.
     */
    public final String toMailReceiptFormat(final PosLog posLog,
            final String creditNum, final String language) {
        if (null == posLog || posLog.getTransaction().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        double totalAmt = 0;
        Authorization authorization = null;
        ReceiptCaptions caption = new ReceiptCaptions(language);
        sb.append("<html>\n<head></head>\n<body>\n"
                 + "<table style='font-size:10px;'>\n<tbody>\n");
        sb.append(formatCustomerID(posLog, language));
        String dateTime = posLog.getTransaction().getBeginDateTime()
                            .replace("T", " ").replace("-", "/");
        sb.append("<tr><td>" + caption.getDeviceNumber() + ": "
            + posLog.getTransaction().getWorkStationID() + "<td><td>"
            + caption.getTransactionNumber() + ": "
            + posLog.getTransaction().getSequenceNo() + "<td><tr>\n"
            + "<tr><td>" + caption.getDate() + ":<td><td>" + dateTime
            + "<td><tr>\n</tbody>\n</table>\n<p style='border-top:none;"
            + " border-right:none; border-left:none; border-bottom:"
            + " 1px solid #000; width: 200px;' /></p><br />\n"
            + "<table style='padding-top:10px; font-size:10px;'>\n<tbody>");
        String lineTR = "<tr><td style='width:90px;'>Description</td>"
            + "<td style='width:110px;text-align:right;'>DescValue</td></tr>\n";
        String taxTR = "<tr><td style='width:90px;text-align:right;'>("
         + caption.getTax() + "</td><td style='width:110px;text-align:right;'>"
         + "TxAmt)</td></tr>\n";
        String lineTRTemp = lineTR, taxAmt = "";
        String saleItemTR = "<tr><td colspan='3' style='width:inherit;"
         + "text-align:left;'>MdName</td></tr><tr><td style='width:140px;"
         + "text-align:center;'>QtyPrice</td><td style='width:60px;"
         + "text-align:right;'>Creditor</td></tr>\n";
        boolean endOfSaleTR = true, isCredit = false;
        for (LineItem lineItem : posLog.getTransaction()
                      .getRetailTransaction().getLineItems()) {
            if (lineItem.getSale() != null) { /*@START:SaleItem in rcpt frmt.*/
                totalAmt = totalAmt + lineItem.getSale().getExtendedAmt();
                sb.append(formatSaleItem(lineItem, saleItemTR));
            } else {
            	lineTRTemp = startTenderItemReceiptFormat(endOfSaleTR);
            	sb.append(lineTRTemp);
            	endOfSaleTR = false; 
	            lineTRTemp = getItemDiscountReceiptFormat(lineItem, totalAmt, language);
	            sb.append(lineTRTemp);
	            totalAmt = totalAmt - getItemDiscount(lineItem);
	            if (lineItem.getTender() != null) {
	            	String tender = formatTender(lineItem, totalAmt, taxTR, language);
	            	sb.append(tender);
	                totalAmt = 0;                   
	            }
	            if (lineItem.getTender() != null && 
	            		(lineItem.getTender().getTenderType()
	                            .equals(TransactionVariable.CREDITDEBIT))) {
	                authorization =
	                        lineItem.getTender().getAuthorization();	               
	                isCredit = true;
	            }
                if (null != lineItem.getTax() && null != lineItem.getTax().get(0)) {
                    taxAmt = getCurrencySymbol(
                            Long.valueOf(lineItem.getTax().get(0).getAmount()));
                } /* @END: TenderItem in receipt format. */
            }
        }
        sb.append("</table>\n");
        /* @START: DISPLAY MEMBERSHIP NUMBER & APPROV. CODE. */
        sb.append(creditReceiptFormat(isCredit, creditNum, authorization, language));
        sb.append("</table></body>\n</html>");
         /* Add the taxAmt */
        String receiptStr = sb.toString().replace("TxAmt", taxAmt);
        return receiptStr;
    }

    /**    
     * @param receipt
     * @param endOfSaleTR
     * @param borderLine
     */
    private void startOfSaleTR(List<String> receipt, boolean endOfSaleTR, String borderLine) {
        if (endOfSaleTR) {
            endOfSaleTR = false;
            //border line
            receipt.add(" ");
            receipt.add(borderLine);
            receipt.add(" ");
        }
    }
    
    /**
     * @param lineItem
     * @param receipt
     * @param totalAmt
     * @return
     * @throws UnsupportedEncodingException
     */
    private double getDiscount(LineItem lineItem, List<String> receipt, double totalAmt)
    		throws UnsupportedEncodingException {
    	String temp = null;
    	double ttlAmt = totalAmt;
        if (lineItem.getDiscount() != null) {
            temp = getCurrencySymbol(totalAmt);
            receipt.add("Sub total:"
                    + getSpaces("Sub total:" + temp) + temp);

            temp = getCurrencySymbol(
                    Long.valueOf(
                            lineItem.getDiscount().getAmount()));
            receipt.add("Discount:"
                    + getSpaces("Discount:" + temp) + temp);
            ttlAmt = totalAmt
                - Long.valueOf(lineItem.getDiscount().getAmount());
        }
        
        return ttlAmt;
    }
    
    /**
     * @param posLog
     * @param receipt
     * @throws UnsupportedEncodingException
     */
    private void setCustomerId(PosLog posLog, List<String> receipt)
    		throws UnsupportedEncodingException {
        //Custermer ID
    	String temp = null;
        if (posLog.getTransaction().getCustomerid() != null) {
            temp = posLog.getTransaction().getCustomerid();
            receipt.add("Customer ID:" + getSpaces("Customer ID:"
                    + temp) + temp);
        }
    }
    
    /**
     * @param isCredit
     * @param receipt
     * @param authorization
     * @param borderLine
     * @param creditNum
     * @throws UnsupportedEncodingException
     */
    private void setCredit(boolean isCredit, List<String> receipt, 
    				Authorization authorization, String borderLine,
    				String creditNum) throws UnsupportedEncodingException {
        if (isCredit) {
            String hideMemNumber = toEncryption(creditNum);
            receipt.add(" ");
            receipt.add(borderLine);
            receipt.add(" ");
            receipt.add("Member No.:" + getSpaces("Member No.:"
                    + hideMemNumber) + hideMemNumber);
            receipt.add("Approval No.:" + getSpaces("Approval No.:"
                    + authorization.getAuthorizationCode())
                    + authorization.getAuthorizationCode());
        }    	
    }

    /**
     * @param lineItem
     * @param receipt
     * @throws UnsupportedEncodingException
     */
    private void tenderCash(LineItem lineItem, List<String> receipt)
    		throws UnsupportedEncodingException {
    	String temp = null;
        if (lineItem.getTender()
                .getTenderType()
                .equals(TransactionVariable.CASH)) {
            temp = getCurrencySymbol(
                    Long.valueOf(lineItem
                            .getTender()
                            .getAmount()));
            receipt.add("Cash:"
                    + getSpaces("Cash:" + temp) + temp);
        }
        if (lineItem.getTender()
                .getTenderChange().getAmount() != 0) {
            temp = getCurrencySymbol(
                    Double.valueOf(
                            lineItem.getTender()
                                     .getTenderChange()
                                     .getAmount()));
            receipt.add("Change:"
                    + getSpaces("Change:" + temp) + temp);
        }
    }    	
    
    /**
     * @param qtynum
     * @param actualsalesunitprice
     * @return
     */
    private String setQtyPrice(int qtynum, double actualsalesunitprice) {
    	String qtyPrice = "";
        if (qtynum > 1) {
            qtyPrice = "      ("
                + String.valueOf(qtynum)
                + " x @" + String.valueOf(actualsalesunitprice) + ")";
        }
    	return qtyPrice;
    }
    /**
     * Helper Method that will format POSLog object
     * to receipt format with text.
     *
     * @param posLog The POSLog object to be converted into receipt.
     * @param creditNum the number of the credit used in the transaction.
     * @return List of string - line by line receipt string
     * @throws UnsupportedEncodingException - thrown when encoding is
     * not supported
     */
    public final List<String> toPaperReceiptFormat(
            final PosLog posLog , final String creditNum)
    throws UnsupportedEncodingException {
        if (null == posLog || posLog.getTransaction().isEmpty()) {
            return Arrays.asList("");
        }

        String borderLine = "--------------------------------";
        String temp = null;
        String temp1 = null;

        List<String> receipt = new ArrayList<String>();
        double totalAmt = 0;
        Authorization authorization = null;
        CreditDebit creditDebit = null;
        int taxIndex = 0;

        //Custermer ID
        setCustomerId(posLog, receipt);

        //DeviceID and TxID
        temp = posLog.getTransaction().getWorkStationID().getValue();
        temp1 = posLog.getTransaction().getSequenceNo();
        receipt.add("Dev#:" + temp + getSpaces("Dev#:"
                + temp + "Tx#:" + temp1) + "Tx#:" + temp1);

        //date
        String dateTime = posLog.getTransaction().getBeginDateTime()
                .replace("T", " ").replace("-", "/");
        receipt.add("Date:" + getSpaces("Date:" + dateTime) + dateTime);

        //border line
        receipt.add(" ");
        receipt.add(borderLine);
        receipt.add(" ");

        //sales detail
        boolean endOfSaleTR = true;
        boolean isCredit = false;
        String taxAmt = "";
        for (LineItem lineItem : posLog.getTransaction()
                      .getRetailTransaction().getLineItems()) {
            if (lineItem.getSale() != null) {
                int qtynum = lineItem.getSale().getQuantity();
                double actualsalesunitprice =
                    lineItem.getSale().getActualsalesunitprice();
                String qtyPrice = setQtyPrice(qtynum, actualsalesunitprice);
                totalAmt = totalAmt + lineItem.getSale().getExtendedAmt();
                String mdName = lineItem.getSale().getDescription().trim();
                receipt.add(mdName);
                temp = getCurrencySymbol(lineItem.getSale().getExtendedAmt());
                receipt.add(qtyPrice + getSpaces(qtyPrice + temp) + temp);
            } else {
            	startOfSaleTR(receipt, endOfSaleTR, borderLine);
                endOfSaleTR = false;
                totalAmt = getDiscount(lineItem, receipt, totalAmt);
                if (lineItem.getTender() != null) {
                    if (totalAmt != 0) {
                        temp = getCurrencySymbol(totalAmt);
                        receipt.add("Total Amount:"
                                + getSpaces("Total Amount:" + temp) + temp);
                        receipt.add("        (Tax      TxAmt)");
                        taxIndex = receipt.size();
                        totalAmt = 0;
                    }
                    if (lineItem.getTender()
                                .getTenderType()
                                .equals(TransactionVariable.CREDITDEBIT)) {
                        temp = getCurrencySymbol(Long.valueOf(
                                lineItem.getTender().getAmount()));
                        receipt.add("Credit:"
                                + getSpaces("Credit:" + temp) + temp);
                        authorization =
                            lineItem.getTender().getAuthorization();
                        creditDebit = lineItem.getTender().getCreditDebit();
                        isCredit = true;
                    } else {
                    	tenderCash(lineItem, receipt);
                    }
                }
                if (null != lineItem.getTax() && null != lineItem.getTax().get(0)) {
                    taxAmt = getCurrencySymbol(
                            Long.valueOf(lineItem.getTax().get(0).getAmount()));
                }
            }
        }

        receipt.set(taxIndex - 1,
                "        (Tax" + getSpaces("        (Tax" + taxAmt
                + ")") + taxAmt + ")");

        setCredit(isCredit, receipt, authorization, borderLine,
				   creditNum);

        return receipt;
    }

    /**
     * Helper method of getPaperReceiptFormat that will print the product item
     * 
     * @param receiptLines
     * @param receiptContent
     * @param language
     * @param rb
     * @throws UnsupportedEncodingException
     */
    private void addProductItem(final List<ReceiptLine> receiptLines,
    		final PaperReceiptContent receiptContent, final String language,
    		final ResourceBundle rb) throws UnsupportedEncodingException {
    	Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);
    	String tmp = null, tmpRb = null;
 
        for (ReceiptProductItem item : receiptContent.getProductItemList()) {
            //add product name
            addLineToReceipt(receiptLines, small, item.getProductName());
            //add product quantity and price and amount
            String qtyprice = "  " + getNumberWithComma(language,
                    item.getPrice())
                + " * " + item.getQuantity() + rb.getString("recptunit");
            tmp = qtyprice + getSpaces(qtyprice
                    + getNumberWithComma(language,
                            item.getAmount()))
                    + getNumberWithComma(language,
                            item.getAmount());
            addLineToReceipt(receiptLines, small, tmp);
            if (item.getDiscountAmount() > 0) { //add item discount
                String discountAmt = "-" + getNumberWithComma(
                        language,
                        Double.valueOf(item.getDiscountAmount()));
                tmpRb = "    " + rb.getString("recptitemdiscount");
                tmp = tmpRb + getSpaces(tmpRb + discountAmt) + discountAmt;
                addLineToReceipt(receiptLines, small, tmp);
            }
        }
        
        
    }
    
    /**
     * Helper Method that will format PaperReceipt object
     * to a list of receipt lines.
     *
     * @param receipt The receipt model to conver.
     * @param lang the language to use
     * @return list of receipt lines for printing
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final List<ReceiptLine> getPaperReceiptFormat(
            final PaperReceipt receipt,
            final String lang) throws UnsupportedEncodingException {
        Locale locale = null;
        String language = "en";
        if (lang == null || !"en".equals(lang)) {
            locale = Locale.JAPANESE;
            language = "ja";
        } else {
            locale = Locale.ENGLISH;
        }
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);
        List<ReceiptLine> receiptLines = new ArrayList<ReceiptLine>();
        Font big = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_BIG);
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);
        Font fontCommer = new Font("Gulim", Font.BOLD, FONT_BIG);
        String tmp = null, tmpRb = null;
        PaperReceiptHeader receiptHeader = receipt.getReceiptHeader();
        addLineToReceipt(receiptLines, small, //add header
                toCentering(receiptHeader.getSiteUrl(), MAX_LINE_NUM_SMALL));
        addLineToReceipt(receiptLines, small, //add header address
                toCentering(receiptHeader.getAddress(), MAX_LINE_NUM_SMALL));
        addLineToReceipt(receiptLines, small, //add header tel
            toCentering("TEL " + receiptHeader.getTel(), MAX_LINE_NUM_SMALL));
        addLineToReceipt(receiptLines, small, " ");
        //add commercial
        for (int i = 0; i < receiptHeader.getCommercialList().size(); i++) {
            addLineToReceipt(receiptLines, fontCommer, toCentering(
                  receiptHeader.getCommercialList().get(i), MAX_LINE_NUM_BIG));
        }
        addLineToReceipt(receiptLines, small, "  ");
        //add content
        PaperReceiptContent receiptContent = receipt.getReceiptContent();
        addLineToReceipt(receiptLines, small,
            getReceiptDateTime(receiptContent.getReceiptDateTime(), language));
        addLineToReceipt(receiptLines, small, "  ");
        //add product items
        addProductItem(receiptLines, receiptContent, language, rb);
        addLineToReceipt(receiptLines, small, "  ");
        //add sub total
        String subtotal = getCurrencySymbol(language,
                receiptContent.getSubTotal());
        tmpRb = rb.getString("recptsubtotal");
        tmp = tmpRb + getSpaces(tmpRb + subtotal) + subtotal;
        addLineToReceipt(receiptLines, small, tmp);
        String discount = receiptContent.getDiscount(); //add discount
        if (discount != null) {
            tmpRb = rb.getString("recptmemberDiscount");
            tmp = tmpRb + getSpaces(tmpRb + "-"
                    + getCurrencySymbol(language,
                            Long.valueOf(discount)))
                    + "-" + getCurrencySymbol(language,
                            Long.valueOf(discount));
            addLineToReceipt(receiptLines, small, tmp);
        }
        //add tax
        String tax = getCurrencySymbol(language,
                Long.valueOf(receiptContent.getTax()));
        tmpRb = "(" + rb.getString("recpttax") + ")";
        tmp = tmpRb + getSpaces(tmpRb + tax) + tax;
        addLineToReceipt(receiptLines, small, tmp);
        addLineToReceipt(receiptLines, small, "  ");
        //add total
        String total = getCurrencySymbol(language,
                receiptContent.getTotal());
        tmpRb = rb.getString("recpttotal");
        tmp = tmpRb + getSpacesBigFont(tmpRb + total) + total;
        addLineToReceipt(receiptLines, big, tmp);
        String paymentCredit = getCurrencySymbol(//add credit amount
                language,
                receiptContent.getPaymentCredit());
        tmpRb = rb.getString("recptpayment")
                    + "  " + rb.getString("recptcreditcard");
        tmp = tmpRb + getSpacesBigFont(tmpRb + paymentCredit) + paymentCredit;
        addLineToReceipt(receiptLines, big, tmp);
        //add payment cash
        String paymentCash = getCurrencySymbol(language,
                receiptContent.getPaymentCash());
        tmpRb = getSpaces(rb.getString("recptpayment").getBytes("MS932").length)
                            + "  " + rb.getString("recptcash");
        tmp = tmpRb + getSpacesBigFont(tmpRb + paymentCash) + paymentCash;
        addLineToReceipt(receiptLines, big, tmp);
        String paymentChange = getCurrencySymbol(//add pament change
                language,
                receiptContent.getPaymentChange());
        tmpRb = getSpaces(rb.getString("recptpayment").getBytes("MS932").length)
                    + "  " + rb.getString("recptchange");
        tmp = tmpRb + getSpacesBigFont(tmpRb + paymentChange) + paymentChange;
        addLineToReceipt(receiptLines, big, tmp);
        addLineToReceipt(receiptLines, small, "  ");
        //add credit information
        PaperReceiptPayment receiptPayment = receipt.getReceiptPayment();
        if (receiptPayment != null) {
            //add card company
            String cardCompany = receiptPayment.getCrCompanyCode();
            tmpRb = rb.getString("recptcordcompany");
            tmp = tmpRb + getSpaces(tmpRb + cardCompany) + cardCompany;
            addLineToReceipt(receiptLines, small, tmp);
            //add company name
            String companyName = " " + receiptPayment.getCompanyName();
            String recvCompanyCode = receiptPayment.getRecvCompanyCode();
            tmp = companyName + getSpaces(companyName + recvCompanyCode)
                              + recvCompanyCode;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit number (to encryption)
            String csStatus = receiptPayment.getCaStatus();
            String pan = receiptPayment.getPanLast4() + "*" + csStatus;
            tmpRb = rb.getString("recptpan");
            tmp = tmpRb + getSpaces(tmpRb + pan) + pan;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit expiration
            String expiration = receiptPayment.getExpiryMaster();
            tmpRb = rb.getString("recptexpiry");
            tmp = tmpRb + getSpaces(tmpRb + expiration) + expiration;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit slip number
            String slip = receiptPayment.getPaymentSeq();
            tmpRb = rb.getString("recptslip");
            tmp = tmpRb + getSpaces(tmpRb + slip) + slip;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit amount
            String amt = getCurrencySymbol(language,
                    Long.valueOf(
                            receiptPayment.getCreditAmount()));
            tmpRb = rb.getString("recptamount");
            tmp = tmpRb + getSpaces(tmpRb + amt) + amt;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit approval number
            String approval = receiptPayment.getApprovalCode();
            tmpRb = rb.getString("recptapproval");
            tmp = tmpRb + getSpaces(tmpRb + approval) + approval;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit process number
            String process = receiptPayment.getTraceNum();
            tmpRb = rb.getString("recptseq");
            tmp = tmpRb + getSpaces(tmpRb + process) + process;
            addLineToReceipt(receiptLines, small, tmp);
            //add settlement management number
            String settlement = receiptPayment.getSettlementNum();
            tmp = rb.getString("recptpaymentsettlement");
            addLineToReceipt(receiptLines, small, tmp);
            addLineToReceipt(receiptLines, small,
                    getSpaces(settlement) + settlement);

        }
        addLineToReceipt(receiptLines, small, "・・・・・・・・・・・・・・・・");
        //add footer
        PaperReceiptFooter receiptFooter = receipt.getReceiptFooter();
        //add shop name and register number
        tmp = rb.getString("recptstorename") + ":"
                + receiptFooter.getShopName();
        addLineToReceipt(receiptLines, small, tmp);
        //add operator name
        tmp = rb.getString("recptoperatorname") + ":"
                + receiptFooter.getSaleMan();
        addLineToReceipt(receiptLines, small, tmp);
        //add terminal id
        tmp = rb.getString("recptterminalid")
                + ":" + receiptFooter.getRegisterNum();
        addLineToReceipt(receiptLines, small, tmp);
        //add transaction id
        tmp = rb.getString("recpttransactionid")
                + ":" + receiptFooter.getTradeNum();
        addLineToReceipt(receiptLines, small, tmp);
        if (receiptFooter.getDepartmentName() != null
                && !"".equals(receiptFooter.getDepartmentName())) {
            //add department name and trade number
            tmp = rb.getString("recptsalesspacename")
                    + ":" + receiptFooter.getDepartmentName();
            addLineToReceipt(receiptLines, small, tmp);
        }
        if (receiptFooter.getHoldName() != null
                && !"".equals(receiptFooter.getHoldName())) {
            tmp = rb.getString("recpteventname") //add event name
                    + ":" + receiptFooter.getHoldName();
            addLineToReceipt(receiptLines, small, tmp);
        }
        return receiptLines;
    }

    /**
     * get paper receipt header format.
     * @param receiptHeader the paper receipt header to use
     * @param language language to use
     * @return list of ReceiptLine class
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final List<ReceiptLine> getPaperReceiptHeaderFormat(
            final PaperReceiptHeader receiptHeader, final String language)
    throws UnsupportedEncodingException {

        List<ReceiptLine> receiptLines = new ArrayList<ReceiptLine>();
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);
        Font fontCommer = new Font("Gulim", Font.BOLD, FONT_BIG);
        //add header
        addLineToReceipt(receiptLines, small, toCentering(
                receiptHeader.getSiteUrl(), MAX_LINE_NUM_SMALL));
        //add header address
        addLineToReceipt(receiptLines, small, toCentering(
                receiptHeader.getAddress(), MAX_LINE_NUM_SMALL));
        //add header tel
        addLineToReceipt(receiptLines, small, toCentering("TEL "
                + receiptHeader.getTel(), MAX_LINE_NUM_SMALL));
        //add commercial
        for (int i = 0; i < receiptHeader.getCommercialList().size(); i++) {
            addLineToReceipt(receiptLines, fontCommer,
                    toCentering(receiptHeader.getCommercialList().get(i),
                    MAX_LINE_NUM_BIG));
        }
        //
        addLineToReceipt(receiptLines, small, "  ");
        return receiptLines;
    }

    private void addMixMatchItem(final List<ReceiptLine> receiptLines,
    		final ReceiptMixMatchBlock block, final String language, 
    		final ResourceBundle rb) throws UnsupportedEncodingException {
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);
        String tmp = null;
        String tmpRb = null;
        for(ReceiptProductItem item : block.getProductItemList()){
            // Add item name
            addLineToReceipt(receiptLines, small,
                    item.getProductName());
            // Add price*quantity    amount
            String qtyprice = "  " + getNumberWithComma(language,
                    item.getPrice())
                              + " * " + item.getQuantity()
                              + rb.getString("recptunit");
            tmp = qtyprice + getSpaces(qtyprice
                    + getNumberWithComma(language,
                            item.getAmount()))
                    + getNumberWithComma(language,
                            item.getAmount());
            addLineToReceipt(receiptLines, small, tmp);
          //add item discount
            if (item.getDiscountAmount() > 0) {
                String discountAmt =
                    "-" + getNumberWithComma(language,
                            Double.valueOf(item.getDiscountAmount()));
                tmpRb = "    " + rb.getString("recptitemdiscount");
                tmp = tmpRb + getSpaces(tmpRb + discountAmt)
                    + discountAmt;
                addLineToReceipt(receiptLines, small, tmp);
            }
        }
    }
    
    /**
     * Get paper receipt content format.
     * @param receiptContent - the PaperReceiptContent model to use
     * @param lang - language to use
     * @return list of ReceiptLine class
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final List<ReceiptLine> getPaperReceiptContentFormat(
            final PaperReceiptContent receiptContent, final String lang)
            throws UnsupportedEncodingException {
        //if it did not set language, default is Japanese
        Locale locale = getLocalLanguage(lang);
        String language = getLanguage(lang);

        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        List<ReceiptLine> receiptLines = new ArrayList<ReceiptLine>();
        Font big = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_BIG);
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);

        String tmp = null;
        String tmpRb = null;

        //add content
        // If training mode, add training mode message.
        if (receiptContent.isTrainingModeFlag()) {
            addLineToReceipt(receiptLines, small,
                    rb.getString("recpttrainingmode"));
            addLineToReceipt(receiptLines, small, "  ");
        }
        //add receipt date time  format: YYYY年MM月DD日(月) HH:MM
        //(if language is ja)
        //format: YYYY/MM/DD HH:MM:ss (if language is en)
        addLineToReceipt(receiptLines, small,
                getReceiptDateTime(
                        receiptContent.getReceiptDateTime(), language));
        //
        addLineToReceipt(receiptLines, small, "  ");
        //add product items
        addProductItem(receiptLines, receiptContent, language, rb);
        //
        addLineToReceipt(receiptLines, small, "  ");
        // Add Mix Match Blocks
        if(receiptContent.getMmBlockList() != null){
            for(ReceiptMixMatchBlock block : receiptContent.getMmBlockList()){
                // Add Mix Match name.
                addLineToReceipt(receiptLines, small,
                        "<" + block.getMmName() + ">");
                // Add Mix Match items.
                addMixMatchItem(receiptLines, block, language, rb);
                // Add Mix Match aggregate info
                // Add Discountable
                tmp = rb.getString("MMDiscountable")
                        + "  " + block.getMmItemCount()
                        + rb.getString("recptunit");
                String mmAmount = getNumberWithComma(language,
                        block.getMmPreviousPrice());
                addLineToReceipt(receiptLines, small,
                        tmp + getSpaces(tmp + mmAmount) + mmAmount);
                // Add discount
                addLineToReceipt(receiptLines, small,
                        rb.getString("MMDiscount")
                        + getSpaces(rb.getString("MMDiscount")
                                + "-" + getNumberWithComma(language,
                                        block.getMmAmount()))
                        + "-" + getNumberWithComma(language,
                                block.getMmAmount()));
                // Add sales price
                addLineToReceipt(receiptLines, small,
                        rb.getString("MMSalesPrice")
                        + getSpaces(rb.getString("MMSalesPrice")
                                + getNumberWithComma(language,
                                        block.getMmItemsSalesPrice()))
                        + getNumberWithComma(language,
                                block.getMmItemsSalesPrice()));
                // Add space line
                addLineToReceipt(receiptLines, small, "  ");
            }
        }
        //add sub total
        String subtotal = getCurrencySymbol(language,
                receiptContent.getSubTotal());
        tmpRb = rb.getString("recptsubtotal");
        tmp = tmpRb + getSpaces(tmpRb + subtotal) + subtotal;
        addLineToReceipt(receiptLines, small, tmp);

        //add discount
        String discount = receiptContent.getDiscount();
        if (discount != null) {
            tmpRb = rb.getString("recptmemberDiscount");
            tmp = tmpRb + getSpaces(tmpRb + "-"
                    + getCurrencySymbol(language,
                            Long.valueOf(discount)))
                    + "-" + getCurrencySymbol(language,
                            Long.valueOf(discount));
            addLineToReceipt(receiptLines, small, tmp);
        }
        //
        addLineToReceipt(receiptLines, small, "  ");
        //add total
        String total = getCurrencySymbol(language,
                receiptContent.getTotal());
        tmpRb = rb.getString("recpttotal");
        tmp = tmpRb + getSpacesBigFont(tmpRb + total) + total;
        addLineToReceipt(receiptLines, big, tmp);
        
        //add tax
        String tax = getCurrencySymbol(language,
                Long.valueOf(receiptContent.getTax())) + ")";
        tmpRb = "(" + rb.getString("recpttax");
        tmp = tmpRb + getSpaces(tmpRb + tax) + tax;
        addLineToReceipt(receiptLines, small, tmp);
        
        //add payment
        String tmpPaymentlength = getSpaces(
                rb.getString("recptpayment").getBytes("MS932").length);
        if (receiptContent.getPaymentCredit() != 0) {
            String paymentCredit =
                    getCurrencySymbol(language,
                            receiptContent.getPaymentCredit());
                tmpRb = rb.getString("recptpayment")
                            + "  " + rb.getString("recptcreditcard");
                tmp = tmpRb + getSpacesBigFont(tmpRb + paymentCredit)
                        + paymentCredit;
                addLineToReceipt(receiptLines, big, tmp);
                if (receiptContent.getPaymentCash() != 0) {
                    String paymentCash = getCurrencySymbol(language,
                            receiptContent.getPaymentCash());
                    tmpRb = tmpPaymentlength
                                    + "  " + rb.getString("recptcash");
                    tmp = tmpRb + getSpacesBigFont(tmpRb + paymentCash)
                            + paymentCash;
                    addLineToReceipt(receiptLines, big, tmp);
                }
        } else {
            String paymentCash = getCurrencySymbol(language,
                    receiptContent.getPaymentCash());
            tmpRb = rb.getString("recptpayment")
                            + "  " + rb.getString("recptcash");
            tmp = tmpRb + getSpacesBigFont(tmpRb + paymentCash) + paymentCash;
            addLineToReceipt(receiptLines, big, tmp);
        }

        //add pament change
        String paymentChange = getCurrencySymbol(language,
                    receiptContent.getPaymentChange());
        tmpRb = tmpPaymentlength + "  " + rb.getString("recptchange");
        tmp = tmpRb + getSpacesBigFont(tmpRb + paymentChange) + paymentChange;
        addLineToReceipt(receiptLines, big, tmp);
        // add miscellaneous tender
        if (receiptContent.getPaymentVoucher() != 0) {
            String paymentMics = getCurrencySymbol(language,
                    receiptContent.getPaymentVoucher());
            tmpRb = tmpPaymentlength + "  " + rb.getString("recptmics");
            tmp = tmpRb + getSpacesBigFont(tmpRb + paymentMics) + paymentMics;
            addLineToReceipt(receiptLines, big, tmp);
        }
        //
        addLineToReceipt(receiptLines, small, "  ");

        return receiptLines;
    }

    /**
     * gets the format for receipt payments.
     * @param receiptPayment - the PaperReceiptPayment to use
     * @param language - language to use
     * @return list of ReceiptLine class
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final List<ReceiptLine> getPaperReceiptPaymentFormat(
            final PaperReceiptPayment receiptPayment,
            final String language) throws UnsupportedEncodingException {
        //if it did not set language, default is Japanese
        Locale locale = null;
        if (language == null || !"en".equals(language))  {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        List<ReceiptLine> receiptLines = new ArrayList<ReceiptLine>();
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);

        String tmp = null;
        String tmpRb = null;

        //add credit information
        if (receiptPayment != null) {
            //add card company
            String cardCompany = receiptPayment.getCrCompanyCode();
            tmpRb = rb.getString("recptcordcompany");
            tmp = tmpRb + getSpaces(tmpRb + cardCompany) + cardCompany;
            addLineToReceipt(receiptLines, small, tmp);
            //add company name
            String companyName = " " + receiptPayment.getCompanyName();
            String recvCompanyCode = receiptPayment.getRecvCompanyCode();
            tmp = companyName + getSpaces(companyName + recvCompanyCode)
                              + recvCompanyCode;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit number (to encryption)
            String csStatus = receiptPayment.getCaStatus();
            String pan = receiptPayment.getPanLast4() + "*" + csStatus;
            tmpRb = rb.getString("recptpan");
            tmp = tmpRb + getSpaces(tmpRb + pan) + pan;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit expiration
            String expiration = receiptPayment.getExpiryMaster();
            tmpRb = rb.getString("recptexpiry");
            tmp = tmpRb + getSpaces(tmpRb + expiration) + expiration;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit slip number
            String slip = receiptPayment.getPaymentSeq();
            tmpRb = rb.getString("recptslip");
            tmp = tmpRb + getSpaces(tmpRb + slip) + slip;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit amount
            String amt = getCurrencySymbol(language,
                    Long.valueOf(receiptPayment.getCreditAmount()));
            tmpRb = rb.getString("recptamount");
            tmp = tmpRb + getSpaces(tmpRb + amt) + amt;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit approval number
            String approval = receiptPayment.getApprovalCode();
            tmpRb = rb.getString("recptapproval");
            tmp = tmpRb + getSpaces(tmpRb + approval) + approval;
            addLineToReceipt(receiptLines, small, tmp);
            //add credit process number
            String process = receiptPayment.getTraceNum();
            tmpRb = rb.getString("recptseq");
            tmp = tmpRb + getSpaces(tmpRb + process) + process;
            addLineToReceipt(receiptLines, small, tmp);
            //add settlement management number
            String settlement = receiptPayment.getSettlementNum();
            tmp = rb.getString("recptpaymentsettlement");
            addLineToReceipt(receiptLines, small, tmp);
            addLineToReceipt(receiptLines, small,
                    getSpaces(settlement) + settlement);
        } else {
            receiptLines = null;
        }

        return receiptLines;
    }

    /**
     * get paper receipt footer format.
     * @param receiptFooter - the PaperReceiptFooter to use
     * @param language - language to use
     * @return list of ReceiptLine class
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final List<ReceiptLine> getPaperReceiptFooterFormat(
            final PaperReceiptFooter receiptFooter, final String language)
    throws UnsupportedEncodingException {
        //if it did not set language, default is Japanese
        Locale locale = null;
        if (language == null || !"en".equals(language)) {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        List<ReceiptLine> receiptLines = new ArrayList<ReceiptLine>();
        Font small = new Font("ＭＳ ゴシック", Font.PLAIN, FONT_SMALL);

        String tmp = null;
        //
        addLineToReceipt(receiptLines, small, "・・・・・・・・・・・・・・・・");
        //add footer
        //add shop name and register number
        tmp = rb.getString("recptstorename")
                + ":" + receiptFooter.getShopName();
        addLineToReceipt(receiptLines, small, tmp);
        //add operator name
        tmp = rb.getString("recptoperatorname")
                + ":" + receiptFooter.getSaleMan();
        addLineToReceipt(receiptLines, small, tmp);
        //add terminal id
        tmp = rb.getString("recptterminalid")
                + ":" + receiptFooter.getRegisterNum();
        addLineToReceipt(receiptLines, small, tmp);
        //add transaction id
        tmp = rb.getString("recpttransactionid")
                + ":" + receiptFooter.getTradeNum();
        addLineToReceipt(receiptLines, small, tmp);

        if (receiptFooter.getDepartmentName() != null
                && !"".equals(receiptFooter.getDepartmentName())) {
            //add department name and trade number
            tmp = rb.getString("recptsalesspacename")
                    + ":" + receiptFooter.getDepartmentName();
            addLineToReceipt(receiptLines, small, tmp);
        }
        if (receiptFooter.getHoldName() != null
                && !"".equals(receiptFooter.getHoldName())) {
            //add event name
            tmp = rb.getString("recpteventname")
                    + ":" + receiptFooter.getHoldName();
            addLineToReceipt(receiptLines, small, tmp);
        }

        return receiptLines;
    }

    /**
     * get bytes from receipt lines string.
     * @param receiptLines - list of ReceiptLine to convert
     * @return byte array equivalent of the List of ReceiptLines
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    public final byte[] getReceiptTextByByte(
            final List<ReceiptLine> receiptLines)
    throws UnsupportedEncodingException {
        byte[] buffer = new byte[receiptLines.size() * 40];
        byte[] endLine = {0x0A};
        int index = 0;
        for (ReceiptLine line : receiptLines) {
            byte[] lineBits = line.getLinedata().getBytes("MS932");
            byte[] temp = new byte[lineBits.length + 1];
            System.arraycopy(lineBits, 0, temp, 0, lineBits.length);
            System.arraycopy(endLine, 0, temp, lineBits.length, endLine.length);
            System.arraycopy(temp, 0, buffer, index, temp.length);
            index = index + temp.length;
        }
        return buffer;
    }

    /**
     * center the data.
     * @param data - data to format
     * @param digits - number of digits of the data
     * @return String - formated data
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    private String toCentering(final String data, final int digits)
    throws UnsupportedEncodingException {
        String centering = null;
        int num = (digits - data.getBytes("MS932").length) / 2;
        centering = getSpaces(num) + data;
        return centering;
    }

    /**
     * adds a line to the receipt.
     * @param receiptLines - the the list to add to
     * @param font - font of the line to add
     * @param line - the line to add
     */
    private void addLineToReceipt(final List<ReceiptLine> receiptLines,
            final Font font, final String line) {
        ReceiptLine receiptLine = new ReceiptLine();
        receiptLine.setFont(font);
        receiptLine.setLinedata(line);
        receiptLines.add(receiptLine);
    }

    /**
     * Helper method that will get local language.
     * 
     * @param language
     * @return
     */
    private static Locale getLocalLanguage(String language) {
        Locale locale = null;
        if (language == null || !"en".equals(language)) {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }
        
        return locale;
    }
    
    /**
     * Helper method for the language.
     *
     * @param lang
     * @return language
     */
    private static String getLanguage(String lang) {
        String language = "en";
        if (lang == null || !"en".equals(lang)) {
            language = "ja";
        }
        
        return language;
    }
    
    /**
     * Helper method that will get day of the week.
     * 
     * @param receiptCal
     * @param rb
     * @return
     */
    private static String getDayOfWeek(final Calendar receiptCal, final ResourceBundle rb) {
    	String week = null;
		switch (receiptCal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			week = rb.getString("recptsunday");
			break;
		case Calendar.MONDAY:
			week = rb.getString("recptmonday");
			break;
		case Calendar.TUESDAY:
			week = rb.getString("recpttuesday");
			break;
		case Calendar.WEDNESDAY:
			week = rb.getString("recptwednesday");
			break;
		case Calendar.THURSDAY:
			week = rb.getString("recptthursday");
			break;
		case Calendar.FRIDAY:
			week = rb.getString("recptfriday");
			break;
		case Calendar.SATURDAY:
			week = rb.getString("recptsaturday");
			break;
		default:
			break;
		}
        
        return week;
    }
    
    /**
     * gets the localized date time for the receipt.
     * @param dateTime - date/time string to localize
     * @param language - language to use
     * @return localized and formated date string
     */
    public static String getReceiptDateTime(
            final String dateTime, final String language) {
        //add receipt date time  format: YYYY年MM月DD日(月)
        if (dateTime == null || "".equals(dateTime)) {
            return dateTime;
        }
        String receiptDateTime = null;
        Locale locale = getLocalLanguage(language);
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        String[] tmp = dateTime.split("T");
        String[] date = tmp[0].split("-");

        String week = null;

        Calendar receiptCal = Calendar.getInstance();
        receiptCal.clear();
        receiptCal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1])
                - 1, Integer.valueOf(date[2]));
        week = getDayOfWeek(receiptCal, rb);
        receiptDateTime = date[0] + rb.getString("recptyear")
                        + date[1] + rb.getString("recptmonth")
                        + date[2] + rb.getString("recptday")
                        + "(" + week + ") "
                        + tmp[1];

        return receiptDateTime;
    }

    /**
     * Get space.
     * @param str  The data of a line
     * @return String with spaces
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    private String getSpaces(final String str)
    throws UnsupportedEncodingException {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < MAX_LINE_NUM_SMALL
                    - str.getBytes("MS932").length; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }
    /**
     * returns string with spaces in big font.
     * @param str - the string to add spaces to
     * @return String with spaces
     * @throws UnsupportedEncodingException - thrown when encoding
     * is unsupported
     */
    private String getSpacesBigFont(final String str)
    throws UnsupportedEncodingException {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < MAX_LINE_NUM_BIG
                - str.getBytes("MS932").length; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }
    /**
     * returns a string with the specified number of spaces.
     * @param num - the number of spaces
     * @return String with spaces
     */
    private String getSpaces(final int num) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < num; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    /**
     * encrypt a String
     * e.g. 1234567890 --> ******7890
     * @param regex - regular expression
     * @return encrypted string
     */
    private String toEncryption(final String regex) {
        return StringUtility.replaceStringIndexWith(regex, 4, '*');
    }
}
