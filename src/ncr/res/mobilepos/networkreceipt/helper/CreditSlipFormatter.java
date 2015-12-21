package ncr.res.mobilepos.networkreceipt.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.res.mobilepos.helper.Formatter;
import ncr.res.mobilepos.helper.PrintFormatter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;

/**
 * class that handles formatting of credit slip.
 *
 */
public class CreditSlipFormatter extends PrintFormatter {

    /**
     * Constructor.
     * @param lineMax - maximum number of line
     */
    public CreditSlipFormatter(final int lineMax) {
        super(lineMax);
    }

    /**
     * Get credit card slip format.
     * @param payment  PaperReceiptPayment model that holds
     * the payment data
     * @param footer  PaperReceiptFooter model that holds
     * the footer data
     * @param lang  the language to use
     * @return List of String - formatted credit card slip data
     */
    public final List<String> getCreditCardSlipFormat(
            final PaperReceiptPayment payment,
            final PaperReceiptFooter footer, final String lang) {
        if (payment == null || footer == null) {
            return new ArrayList<String>();
        }
        //if it did not set language, default is Japanese
        Locale locale = Formatter.getLanguage(lang);
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        List<String> creditSlipLines = new ArrayList<String>();

        addItemStr(creditSlipLines,
                rb.getString("recptpayment") + "  "
                + rb.getString("recptcreditcard"),
                getCurrencySymbol(locale.getLanguage(),
                        Long.valueOf(payment.getCreditAmount())));

        addSeparator(creditSlipLines, " ");
        addItemStr(creditSlipLines,
                rb.getString("recptcordcompany"), payment.getCrCompanyCode());
        addItemStr(creditSlipLines, " " + payment.getCompanyName(),
                payment.getRecvCompanyCode());
        addItemStr(creditSlipLines, rb.getString("recptpan"),
                payment.getPanLast4() + "*" + payment.getCaStatus());
        addItemStr(creditSlipLines, rb.getString("recptexpiry"),
                payment.getExpiryMaster());
        addItemStr(creditSlipLines, rb.getString("recptslip"),
                payment.getPaymentSeq());
        addItemStr(creditSlipLines, rb.getString("recptamount"),
                getCurrencySymbol(locale.getLanguage(),
                        Long.valueOf(payment.getCreditAmount())));
        addItemStr(creditSlipLines, rb.getString("recptapproval"),
                payment.getApprovalCode());
        addItemStr(creditSlipLines, rb.getString("recptseq"),
                payment.getTraceNum());
        addTextLeftAlign(creditSlipLines,
                rb.getString("recptpaymentsettlement"));
        addTextRightAlign(creditSlipLines,
        		payment.getSettlementNum().replace("--",""));
        
        // Add sign space
        addTextLeftAlign(creditSlipLines, rb.getString("recptsignspace"));
        addSeparator(creditSlipLines, "-");
        addItemStr(creditSlipLines, "|", "|");
        addItemStr(creditSlipLines, "|", "|");
        if ("ja".equals(locale.getLanguage())) {
            addItemStr(creditSlipLines, "|", rb.getString("recptcust") + "Å@|");
        } else {
            addItemStr(creditSlipLines, "| " + rb.getString("recptcust"), "|");
        }
        addItemStr(creditSlipLines, "|", "|");
        addSeparator(creditSlipLines, "-");

        addSeparator(creditSlipLines, "-");

        //Add footer
        addTextLeftAlign(creditSlipLines, rb.getString("recptstorename")
                + ":" + footer.getShopName());
        addTextLeftAlign(creditSlipLines, rb.getString("recptoperatorname")
                + ":" + footer.getSaleMan());
        addTextLeftAlign(creditSlipLines, rb.getString("recptterminalid")
                + ":" + footer.getRegisterNum());
        addTextLeftAlign(creditSlipLines, rb.getString("recpttransactionid")
                + ":" + footer.getTradeNum());
        if (footer.getDepartmentName() != null
                && !"".equals(footer.getDepartmentName())) {
            addTextLeftAlign(creditSlipLines,
                    rb.getString("recptsalesspacename")
                    + ":" + footer.getDepartmentName());
        }
        if (footer.getHoldName() != null && !"".equals(footer.getHoldName())) {
            addTextLeftAlign(creditSlipLines,
                    rb.getString("recpteventname")
                    + ":" + footer.getHoldName());
        }

        return creditSlipLines;
    }
}
