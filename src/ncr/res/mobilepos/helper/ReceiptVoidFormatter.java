package ncr.res.mobilepos.helper;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
import ncr.res.mobilepos.networkreceipt.model.ItemMode;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;

public class ReceiptVoidFormatter extends Formatter {

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
     * Class Constructor
     * 
     * @param receipt
     * @param lineLength
     * @param returnMod
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    public ReceiptVoidFormatter(ReceiptMode receipt, int lineLength)
        throws UnsupportedEncodingException,
            ParseException {

        this.receipt = receipt;
        this.lineMaxLimit = lineLength;
        this.receiptCmd.add(0, null);

        Locale locale = getLanguage(receipt.getLanguage());
        this.rb = ResourceBundle.getBundle("label", locale);

        this.headerFormat();
        this.itemFormat();
        this.footerFormat();
    }

    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     */
    private void headerFormat() throws UnsupportedEncodingException {

        // Add one line
        this.receiptCmd.add(this.setFeedLines(1));

        // Add store name
        int digit = this.getRelativePosition(this.rb.getString("recpttelno")
                + this.receipt.getTelNo());

        line.setLength(0);
        line.append(this.formatLine(digit, 0,
                this.rb.getString("recptstorenamestr"),
                this.receipt.getStoreName()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add tel number
        line.setLength(0);
        line.append(this.formatLine(digit, 0, this.rb.getString("recpttelno"),
                this.receipt.getTelNo()));
        this.addLineToReceipt(0, 0, 1, line.toString());

        // Add ads
        String[] adses = this.receipt.getAds().split("\\u007C");
        for (String ads : adses) {
            this.addLineToReceipt(0, 0, 0, ads);
        }

        // Add one line
        this.receiptCmd.add(this.setFeedLines(1));

        // Add void operator
        line.setLength(0);
        line.append(this.rb.getString("recptoperator"));
        line.append(this.receipt.getVoiderOperatorID());
        line.append(this.getBlanks(2));
        line.append(this.receipt.getVoidClerkName());
        this.addLineToReceipt(0, 0, 1, line.toString());

        // void title
        sb.setLength(0);
        int astSize = 15;
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            astSize = 17;
        }
        sb.append(this.getAsterisk(astSize, 0, 0));
        line.setLength(0);
        line.append(this.formatLine(0, 1, sb.toString(),
                this.rb.getString("recptvoidstr"), sb.toString()));
        this.addLineToReceipt(0, 0, 1, line.toString());

        // Add operator
        line.setLength(0);
        line.append(this.rb.getString("recptoperator"));
        line.append(this.receipt.getOperatorID());
        line.append(this.getBlanks(2));
        line.append(this.receipt.getClerkName());
        this.addLineToReceipt(0, 0, 0, line.toString());

        // customer tier
        line.setLength(0);
        line.append(this.rb.getString("recptcusttierstr"));
        line.append(this.getBlanks(1));
        line.append(this.receipt.getCustGraphicId());
        line.append(this.getBlanks(1));
        line.append(this.receipt.getCustGraphicName());
        this.addLineToReceipt(0, 0, 0, line.toString());
    }

    /**    
     * @param item
     * @param digit
     * @param price
     * @throws UnsupportedEncodingException
     */
    private void addQuantity(ItemMode item, int digit, String price)
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
    
    private void addDescription(ItemMode item)
    		throws UnsupportedEncodingException {
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

            addDescription(item);

            // ItemID, Quantity, ActualSalesUnitPrice
            String price = this.getAmountByMode(item.getExtendedAmount(), 1);

            // Add quantity
            int digit = StringUtility.isNullOrEmpty(item.getMmID()) ? 1 : 2;
            addQuantity(item, digit, price);

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
        sb.append(this.getAmountByMode(this.receipt.getGrandAmount(), 1));

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
        
        // for fantamiliar points system        
        // MemberInfo member = receipt.getMemberInfo();
        // if (member != null) {
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptline"), ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"", ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointtarget"),
    			 //    member.getAmountForPoints() +
    			 //    this.rb.getString("recptpointamountsymble")));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointscorea"),
    			 //    member.getCorrectionPoints() +
    			 //    this.rb.getString("recptpointsymble")));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointuse"),
    			 //    "0" + this.rb.getString("recptpointsymble")));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointscoreb"),
        // 			member.getPointsPrior() + this.rb.getString("recptpointsymble")));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     String recpttotal =  this.rb.getString("recptpointtotal");          
        //     String businessDate = receipt.getBusinessDayDate();
        //     recpttotal = recpttotal.replace("0000/00/00",
        //  		     businessDate.substring(0,4) + "/12/31");
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		recpttotal,
        // 			member.getLostPoints() +
        // 			this.rb.getString("recptpointsymble")));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"", ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointmemberno"),
        //     		member.getInputtedMembershipId()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     String sDate = member.getExpirationDate();
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointexpirationdate"),
        //     		sDate.substring(0,4) + "/" + sDate.substring(4,6) +
        //     		"/" + sDate.substring(6,8)));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointremark1"), ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointremark2"), ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,"", ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptline"), ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        // 	line.setLength(0);
        // 	line.append(this.formatLine(0, 1,
        // 			this.rb.getString("recptpointCancelHeader"), ""));
        // 	this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		rb.getString("recptpointcommserialnumber"),
        //     		member.getPointsAcknowledgeId()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointtranseqnumber"),
        //     		member.getPiontsTransactionId()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointresponse"),
        //     		member.getServerStatusCode()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointmemberstat"),
        //     		member.getStatusCode()));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     long total = Long.valueOf( member.getPointsPrior()) ; 
        //     total += Long.valueOf(member.getCorrectionPoints());
        //     if (total < 0 ) {
        //     	total = 0;
        //     }
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptpointbalance"),
        //     		String.valueOf(total)));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        //     line.setLength(0);
        //     line.append(this.formatLine(0, 1,
        //     		this.rb.getString("recptline"), ""));
        //     this.addLineToReceipt(0, 0, 0, line.toString());
        // }
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
        this.addLineToReceipt(0, 0, 1, line.toString());

        // void title
        sb.setLength(0);
        sb.setLength(0);
        int astSize = 15;
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            astSize = 17;
        }
        sb.append(this.getAsterisk(astSize, 0, 0));
        line.setLength(0);
        line.append(this.formatLine(0, 1, sb.toString(),
                this.rb.getString("recptvoidstr"), sb.toString()));
        this.addLineToReceipt(0, 0, 1, line.toString());

        // Add remarks column
        this.addRemarksColumn(0, this.rb.getString("recptreason"));
        this.addRemarksColumn(0, this.rb.getString("recptmgtapproval"));

        sb.setLength(0);
        // Add void store id
        sb.append(this.receipt.getVoiderRetailStoreID());
        sb.append("/");
        // Add void workstation id
        sb.append(this.receipt.getVoiderWorkstationID());

        // Add time and date
        line.setLength(0);
        line.append(this.formatLine(0, 1,
                this.rb.getString("recpttransactionidstr"), sb.toString()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("yyyy/MM/dd");

        String date2 = sdf2.format(sdf.parse(this.receipt
                .getVoiderBusinessDayDate()));
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        sdf2 = new SimpleDateFormat("HH:mm");
        String time2 = sdf2.format(sdf.parse(this.receipt
                .getVoiderBeginDateTime()));

        line.setLength(0);
        line.append(this.formatLine(2, 10,
                this.receipt.getVoiderSequenceNumber(), time2, date2));
        this.addLineToReceipt(0, 0, 0, line.toString());

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
