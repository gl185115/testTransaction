package ncr.res.mobilepos.helper;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
import ncr.res.mobilepos.networkreceipt.model.EmuConst.BarcodeType;
import ncr.res.mobilepos.networkreceipt.model.ItemMode;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;

/**
 * 
 * @author user
 * 
 */
public class ReceiptNormalFormatter extends Formatter {

    /**
     * 
     */
    private StringBuilder sb = new StringBuilder();
    /**
     * 
     */
    private StringBuilder line = new StringBuilder();
    /**
     * 
     */
    private int itemQuantity = 0;
    /**
     * 
     */
    private int flg = 0;

    /**
     * Class Constructor
     * 
     * @param receipt
     * @param lineLength
     * @param language
     * @param returnMod
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    public ReceiptNormalFormatter(ReceiptMode receipt, int lineLength,
            int flg) throws UnsupportedEncodingException,
            ParseException {

        this.flg = flg;
        this.receipt = receipt;
        this.lineMaxLimit = lineLength;
        String retryflag = receipt.getRetryflag();

        Locale locale = getLanguage(receipt.getLanguage());
        this.rb = ResourceBundle.getBundle("label", locale);

        // If this is a retry request, add "Reissue" to receipt.
        if (retryflag != null && "true".equalsIgnoreCase(retryflag)) {
            String line1 = this.rb.getString("recptreissue");
            byte[] line1bytes = cmdCopy(this.setCharHeight(1),
                    this.getAlign(1), line1.getBytes(), new byte[] {0x0A});
            String line2 = this.rb.getString("recptnotareceipt");
            byte[] line2bytes = cmdCopy(this.setCharHeight(0),
                    this.getAlign(1), line2.getBytes(), new byte[] {0x0A});
            String line3 = this.rb.getString("recptalreadyissued");
            byte[] line3bytes = cmdCopy(this.setCharHeight(0),
                    this.getAlign(1), line3.getBytes(), new byte[] {0x0A});
            byte[] line = cmdCopy(line1bytes, line2bytes, line3bytes);
            addLineToReceipt(line);
        } else {
            this.receiptCmd.add(0, null);
        }
        this.headerFormat();
        this.itemFormat();
        this.footerFormat();
        if (retryflag != null && "true".equalsIgnoreCase(retryflag)) {
            this.addLineToReceipt(1, 1, 0, this.rb.getString("recptreissue"));
        }
    }

    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     */
    private void headerFormat() throws UnsupportedEncodingException {

        // Add one line
        this.receiptCmd.add(this.setFeedLines(1));

        // Add title
        // ................ Receipt ...............
        this.addLineToReceipt(1, 1, 1, this.rb.getString("receipt"));

        // Add store name
        int digit = this.getRelativePosition(this.rb.getString("recpttelno")
                + this.receipt.getTelNo());

        line.setLength(0);
        line.append(this.formatLine(digit, 0,
                this.rb.getString("recptstorenamestr"),
                this.receipt.getStoreName()));
        // Store : XXXXXX..........................
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add tel number
        line.setLength(0);
        line.append(this.formatLine(digit, 0, this.rb.getString("recpttelno"),
                this.receipt.getTelNo()));
        // TEL   : 123-123-1234....................
        this.addLineToReceipt(0, 0, 1, line.toString());

        // Add ads
        if (this.receipt.getAds() != null) {        	
	        String[] adses = this.receipt.getAds().split("\\u007C");
	        for (String ads : adses) {
	            // XXXXXXXXXX..............................
	            this.addLineToReceipt(0, 0, 0, ads);
	        }
        }
        // Add one line
        this.receiptCmd.add(this.setFeedLines(1));

        // Add operator
        line.setLength(0);
        line.append(this.rb.getString("recptoperator"));
        line.append(this.receipt.getOperatorID());
        line.append(this.getBlanks(2));
        line.append(this.receipt.getClerkName());
        this.addLineToReceipt(0, 0, 1, line.toString());
    }

    /**     
     * @param item
     * @param price
     * @param digit
     * @throws UnsupportedEncodingException
     */
    private void addQuantity(ItemMode item, String price, int digit)
    		throws UnsupportedEncodingException {        
        int col = 0;
        sb.setLength(0);
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            sb.append("Å~ ");
            sb.append(this.getAmountByMode(item.getQuantity(), 1));
            col = StringUtility.isNullOrEmpty(item.getMmID()) ? 4 : 3;
        } else {
            sb.append(this.getAmountByMode(item.getQuantity(), 1));
            sb.append(this.rb.getString("recptunitstr"));
            col = 5;
        }
        line.setLength(0);
        line.append(this.formatLine(digit, col, item.getItemID(),
                sb.toString(), price));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add quantity
        if (item.getQuantity() > 1) {
            sb.setLength(0);
            sb.append("(@");
            sb.append(this.getBlanks(4));
            sb.append(this.getAmountByMode(item.getActualSalesUnitPrice(),
                    1));
            sb.append(" Å~ ");
            sb.append(item.getQuantity());
            sb.append(this.rb.getString("recptunitstr"));
            sb.append(" )");

            line.setLength(0);
            line.append(this.formatLine(6, 1, sb.toString()));
            this.addLineToReceipt(0, 0, 0, line.toString());
        }  	
    }
    
    /**     
     * @param item
     * @param digit
     * @throws UnsupportedEncodingException
     */
    private void addReasonCode(ItemMode item, int digit)
    		throws UnsupportedEncodingException {
        if (!StringUtility.isNullOrEmpty(item.getReasonCode())) {

            sb.setLength(0);
            if (item.getPercent() != 0) {
                sb.append(this.getAmountByMode(item.getPercent(), 1));
                sb.append("%");
                if ("en".equals(this.rb.getLocale().getLanguage())) {
                    sb.append(" ");
                }
                sb.append(this.rb.getString("recptDiscount"));
                digit = 4;
            } else {
                sb.append(this.rb.getString("recptitemdiscount"));
                digit = 7;
            }
            if ("en".equals(this.rb.getLocale().getLanguage())) {
                digit = 1;
            }
            String discount = sb.toString();

            sb.setLength(0);
            sb.append(this.rb.getString("recptreasoncode"));
            sb.append(item.getReasonCode());

            line.setLength(0);
            line.append(this.formatLine(0, digit, discount, sb.toString(),
                    this.getAmountByMode(item.getDiscountAmount(), -1)));

            this.addLineToReceipt(0, 0, 0, line.toString());
        }    	
    }
    
    private void setItemDescription(ItemMode item) throws UnsupportedEncodingException {
        sb.setLength(0);
        if (!StringUtility.isNullOrEmpty(item.getNonSalesFlag())
                && "True".equals(item.getNonSalesFlag())) {
            sb.append("Å¶");
        }
        if (!StringUtility.isNullOrEmpty(item.getDiscountableFlag())
                && "False".equals(item.getDiscountableFlag())) {
            sb.append("*");
        }
        
        if (sb.length() != 0) {
            line.setLength(0);
            line.append(this.formatLine(0, 1, item.getItemName(),
                    sb.toString()));
            this.addLineToReceipt(0, 0, 0, line.toString());
        } else {
            // Description
            this.addLineToReceipt(0, 0, 0, item.getItemName());
        }

    }
    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     */
    private void itemFormat() throws UnsupportedEncodingException {

        // item format
        int mmQuantity = 0;
        for (ItemMode item : this.receipt.getItemList()) {

            if (!StringUtility.isNullOrEmpty(item.getMmID())) {
                if (item.getMmSequence() == 1) {
                    // Description
                    line.setLength(0);
                    line.append("<").append(item.getMmName()).append(">");
                    this.addLineToReceipt(0, 0, 0, line.toString());
                }
                mmQuantity += item.getMmQuantity();
            }

            // description
            setItemDescription(item);

            // ItemID, Quantity, ActualSalesUnitPrice
            String price = this.getAmountByMode(item.getExtendedAmount(), 1);

            // Add quantity
            int digit = StringUtility.isNullOrEmpty(item.getMmID()) ? 1 : 2;
            addQuantity(item, price, digit);

            // Add reason code
            addReasonCode(item, digit);

            if (item.getMmSequence() != 0
                    && item.getMmSequence() == item.getMmSize()) {

                // Add Discountable
                sb.setLength(0);
                sb.append(rb.getString("MMDiscountablestr"));
                sb.append(this.getBlanks(2));
                sb.append(mmQuantity);
                sb.append(this.rb.getString("recptunitstr"));

                line.setLength(0);
                line.append(this.formatLine(6, 1, sb.toString(),
                        this.getAmountByMode(item.getMmPreviousPrice(), 1)));
                this.addLineToReceipt(0, 0, 0, line.toString());

                // Add discount
                line.setLength(0);
                line.append(this.formatLine(6, 1,
                        this.rb.getString("MMDiscountstr"),
                        this.getAmountByMode(item.getMmAmount(), -1)));
                this.addLineToReceipt(0, 0, 0, line.toString());

                // Add selling price
                line.setLength(0);
                line.append(this.formatLine(6, 1,
                        this.rb.getString("MMSalesPricestr"),
                        this.getAmountByMode(item.getMmSellingPrice(), 1)));
                this.addLineToReceipt(0, 0, 0, line.toString());
                mmQuantity = 0;
            }

            itemQuantity += item.getQuantity();
        }
    }

    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     * @throws ParseException ParseException
     */
    private void footerFormat() throws UnsupportedEncodingException,
            ParseException {

        if (this.receipt.getTotalDiscount() != 0) {

            // Add sub total
            line.setLength(0);
            line.append(this.formatLine(5, 1,
                    this.rb.getString("recptamountfordiscount"),
                    this.getAmountByMode(this.receipt.getSubtotal(), 1)));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add total discount
            sb.setLength(0);
            if (this.receipt.getTotalPercent() != 0) {
                sb.append(this.receipt.getTotalPercent());
                sb.append("%");
                if ("en".equals(this.rb.getLocale().getLanguage())) {
                    sb.append(" ");
                }
                sb.append(this.rb.getString("recptDiscount"));
            } else {
                sb.append(this.rb.getString("recptdiscountamount"));
            }

            line.setLength(0);
            line.append(this.formatLine(5, 1, this.sb.toString(),
                    this.getAmountByMode(this.receipt.getTotalDiscount(), -1)));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add total discount reason code
            sb.setLength(0);
            sb.append(this.rb.getString("recptreasoncode"));
            sb.append(this.receipt.getTotalReasonCode());

            line.setLength(0);
            line.append(this.formatLine(8, 1, sb.toString()));
            this.addLineToReceipt(0, 0, 1, line.toString());
        } else {
            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));
        }

        // Add tax percent
        sb.setLength(0);
        sb.append(this.getAmountByMode(this.itemQuantity, 1));
        sb.append(this.rb.getString("recptunitstr"));

        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recptnumofitems"),
                sb.toString()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add total
        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recpttotalstr"),
                this.getAmountByMode(this.receipt.getGrandAmount(), 1)));
        this.addLineToReceipt(1, 0, 0, line.toString());

        // Add tax
        sb.setLength(0);
        sb.append("(");
        sb.append(this.rb.getString("recpttaxstr"));

        line.setLength(0);
        line.append(this.formatLine(0, 1, sb.toString(),
                this.getAmountByMode(this.receipt.getTaxAmount(), 1) + ")"));
        this.addLineToReceipt(0, 0, 1, line.toString());

        // Add tender
        this.addTender();

        // Add payment
        sb.setLength(0);
        sb.append(this.getAmountByMode(
                this.receipt.getCashPament() + this.receipt.getCreditPament()
                        + this.receipt.getMiscPament(), 1));

        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recptpaymentstr"),
                sb.toString()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add change
        if (this.receipt.getCashPament() != 0
                && this.receipt.getTenderChange() != 0) {

            line.setLength(0);
            line.append(this.formatLine(0, 1, this.rb.getString("recptchange"),
                    this.getAmountByMode(this.receipt.getTenderChange(), 1)));
            this.addLineToReceipt(0, 0, 1, line.toString());
        } else {
            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));
        }
        
        // for fantamiliar point system
        // MemberInfo member = this.receipt.getMemberInfo();
        // if (member != null) {
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptline"),""));
        //     this.addLineToReceipt(0, 0, 0, line.toString()); 	
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"",""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointtarget"),
        //             member.getAmountForPoints()) +
        //             this.rb.getString("recptpointamountsymble"));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointscorea"),
        //             member.getCorrectionPoints()) +
        //             this.rb.getString("recptpointsymble"));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointuse"),
        //             "0") +
        //             this.rb.getString("recptpointsymble"));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointscoreb"),
        //             member.getPointsPrior()) +
        //             this.rb.getString("recptpointsymble"));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
          
        //     String recpttotal = this.rb.getString("recptpointtotal");          
        //     String businessDate = receipt.getBusinessDayDate();
        //     recpttotal = recpttotal.replace("0000/00/00",
        //   		     businessDate.substring(0,4) + "/12/31");
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1, recpttotal,
        //         member.getLostPoints()) + 
        //         this.rb.getString("recptpointsymble"));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"",""));
        //     this.addLineToReceipt(0, 0, 0, line.toString()); 
        //     line.setLength(0);
        //     line.append(this.formatLine(0,
        // 		    1, this.rb.getString("recptpointmemberno"),
        // 		    member.getInputtedMembershipId()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     String sDate = member.getExpirationDate();
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        // 		    this.rb.getString("recptpointexpirationdate"),
        // 		     sDate.substring(0,4) + "/" + sDate.substring(4,6) +
        //      		"/" + sDate.substring(6,8)));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointremark1"),""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);            
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointremark2"),""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"",""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptline"),""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1, "", ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString()); 
        // }
        
        this.addBarCode();
    }

    /**
     * Add bar code
     * 
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws ParseException ParseException
     */
    private void addBarCode() throws UnsupportedEncodingException,
            ParseException {

        // Add tranNo, storeID, TerminalID
        sb.setLength(0);
        sb.append(this.receipt.getStoreID());
        sb.append("/");
        sb.append(this.receipt.getWorkStationID());

        line.setLength(0);
        line.append(this.formatLine(0, 1,
                this.rb.getString("recpttransactionidstr"), sb.toString()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add time and date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

        String date = sdf2.format(sdf.parse(this.receipt.getBusinessDayDate()));
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        sdf2 = new SimpleDateFormat("HH:mm");
        String time = sdf2.format(sdf.parse(this.receipt.getBeginDateTime()));

        line.setLength(0);
        line.append(this.formatLine(2, 10, this.receipt.getSequenceNo(), time,
                date));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add bar code
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf2 = new SimpleDateFormat("yyMMdd");
        String bcDate = sdf2.format(sdf.parse(date));

        line.setLength(0);
        line.append("A");
        line.append(this.receipt.getStoreID());
        line.append(this.receipt.getWorkStationID());
        line.append(bcDate);
        line.append(this.receipt.getSequenceNo());
        line.append("A");

        this.receiptCmd.add(this.getBarCode(1, 2, 65, 0, 2,
                BarcodeType.CODEBAR, line.toString()));

        // TODO Reference number is null, so fix with "0"
        if ((this.receipt.getCreditPament() != 0) 
                && (this.receipt.getReferenceNo() == null)){
                this.receipt.setReferenceNo("000000000000000000000000");
        }
        /** RES-5528
        if (!StringUtility.isNullOrEmpty(this.receipt.getReferenceNo())) {

            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));

            // Add bar code
            line.setLength(0);
            line.append(rb.getString("recptcreditbarcode1"));
            this.addLineToReceipt(0, 1, 0, line.toString());

            String[] rs = this.formatReferenceNo(this.receipt.getReferenceNo());
            sb.setLength(0);
            sb.append("A");
            sb.append(rs[0]);
            sb.append(rs[1]);
            sb.append("A");
            this.receiptCmd.add(this.getBarCode(1, 2, 55, 0, 0,
                    BarcodeType.CODEBAR, sb.toString()));

            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));

            // Add bar code
            line.setLength(0);
            line.append(rb.getString("recptcreditbarcode2"));
            this.addLineToReceipt(0, 1, 0, line.toString());

            sb.setLength(0);
            sb.append("B");
            sb.append(rs[2]);
            sb.append(rs[3]);
            sb.append("B");
            this.receiptCmd.add(this.getBarCode(1, 2, 50, 0, 0,
                    BarcodeType.CODEBAR, sb.toString()));
        }
         */
        if (flg == 1) {
            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));
            // copy for card company
            this.addLineToReceipt(0, 1, 0, this.rb.getString("recptcreditslip"));
        }

    }

    /**
     *  Add Ndivided
     * @throws UnsupportedEncodingException 
     */
    private void addNdivided() throws UnsupportedEncodingException {
        
        if ("1".equalsIgnoreCase(this.receipt.getPaymentMethodCode()) ||
        		"3".equalsIgnoreCase(this.receipt.getPaymentMethodCode())) {
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptndivided"),
                    this.receipt.getNDivided()));
            this.addLineToReceipt(0, 0, 0, line.toString());
        }    	
    }
    
    /**
     * Add signature column
     */
    private void addSignatureColumn() {
        if (flg == 1) {
            // Add signature column
            this.addLineToReceipt(0, 0, 0,
                    this.rb.getString("recptsignspace"));

            if ("true".equals(this.receipt.getSignatureFlag())) {
                // Add signature column
                this.addSignColumn();
            } else {
                this.addLineToReceipt(0, 0, 0,
                        this.rb.getString("recptsigndisplay1"));
                this.addLineToReceipt(0, 0, 0,
                        this.rb.getString("recptsigndisplay2"));
            }
        }    	
    }
    
    /**
     * @throws UnsupportedEncodingException
     */
    private void addTender() throws UnsupportedEncodingException {

        if (this.receipt.getCashPament() != 0) {

            // Add cash
            line.setLength(0);
            line.append(this.formatLine(4, 1, this.rb.getString("recptcash"),
                    this.getAmountByMode(this.receipt.getCashPament(), 1)));
            this.addLineToReceipt(0, 0, 0, line.toString());
        }

        if (this.receipt.getCreditPament() != 0) {

            //
            line.setLength(0);
            for (int i = 0; i < this.lineMaxLimit / 2; i++) {
                line.append("ÅE");
            }
            String dot = line.toString();
            this.addLineToReceipt(0, 1, 0, dot);
            //
            this.addLineToReceipt(0, 1, 0, rb.getString("recptcredittitle"));
            //
            this.addLineToReceipt(0, 1, 0, dot);

            // Add card company
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptcordcompanyname"),
                    this.receipt.getCardCompany()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add credit number
            sb.setLength(0);
            sb.append(this.receipt.getCreditNo());
            sb.append("*00");

            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptcreditno"),
                    sb.toString()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add expiry
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptexpiry"),
                    "XX/XX"));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add slip number
            sb.setLength(0);
            sb.append(String.format("%05d",
                    Integer.valueOf(this.receipt.getSlipNo())));

            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptslip"),
                    sb.toString()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add payment method
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recpttender"),
                    this.receipt.getPaymentMethod()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add Ndivided
            addNdivided();
            
            // Add credit payment
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptpaymentamount"),
                    this.getCurrencySymbol(this.receipt.getCreditPament())));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add credit approval number
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptapproval"),
                    this.receipt.getApprovalNo()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add credit issue sequence
            line.setLength(0);
            String issueSeq = this.receipt.getIssueSequence() == null ? " " : 
                this.receipt.getIssueSequence();
            line.append(this.formatLine(0, 1, rb.getString("recptseq"),
                    issueSeq));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add credit terminal id
            line.setLength(0);
            line.append(this.formatLine(0, 1, rb.getString("recptcreditterid"),
                    this.receipt.getCreditTerminalID()));
            this.addLineToReceipt(0, 0, 0, line.toString());

            // Add credit reference number
            this.addLineToReceipt(0, 0, 0,
                    rb.getString("recptpaymentsettlement"));
            // RES-5556
            if (this.receipt.getReferenceNo() != null) {
                line.setLength(0);
                line.append(this.receipt.getReferenceNo());
                line.append(this.getBlanks(2));
                this.addLineToReceipt(0, 2, 0, line.toString());
            }
            addSignatureColumn();

            this.addLineToReceipt(0, 1, 0, dot);

        }

        if (this.receipt.getMiscPament() != 0) {
            line.setLength(0);
            line.append(this.formatLine(4, 1, rb.getString("recptmics"),
                    this.getAmountByMode(this.receipt.getMiscPament(), 1)));
            this.addLineToReceipt(0, 0, 1, line.toString());
        } else {
            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));
        }
    }
}
