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
public class ReceiptReturnFormatter extends Formatter {

    /**
     * class instance of PaperReceiptContent.
     */
    private ReceiptMode receipt;
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
     * @param returnMod
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    public ReceiptReturnFormatter(ReceiptMode receipt, int lineLength,
            int flg) throws UnsupportedEncodingException,
            ParseException {

        this.flg = flg;
        this.receipt = receipt;
        this.lineMaxLimit = lineLength;
        String retryflag = receipt.getRetryflag();

        Locale locale = getLanguage(receipt.getLanguage());
        this.rb = ResourceBundle.getBundle("label", locale);

        if (flg == 3) {
            line.setLength(0);
            if ("en".equals(this.rb.getLocale().getLanguage())) {
                line.append(this.getAsterisk(12, 0, 0));
                line.append(this.getBlanks(1));
                line.append(this.rb.getString("recptretcashtitle"));
                line.append(this.getBlanks(1));
                line.append(this.getAsterisk(12, 0, 0));
            } else {
                line.append(this.formatLine(0, 0, this.getAsterisk(4, 1, 1),
                        this.rb.getString("recptretcashtitle"),
                        this.getAsterisk(4, 1, 1)));
            }
            this.addLineToReceipt(1, 1, 0, line.toString());
        } else if (flg == 1){
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
        } else {
            this.receiptCmd.add(0, null);
        }

        this.headerFormat();
        this.itemFormat();
        this.footerFormat();
        this.addBarCode();
        
        if ((flg == 1) && (retryflag != null)
               && "true".equalsIgnoreCase(retryflag)) {
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

        if (flg == 1 || flg == 2) {
            // Add title
            this.addLineToReceipt(1, 1, 1, this.rb.getString("receipt"));
        }

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
        if (this.receipt.getAds() != null) {
	        String[] adses = this.receipt.getAds().split("\\u007C");
	        for (String ads : adses) {
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

        sb.setLength(0);
        line.setLength(0);
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            sb.append(this.getAsterisk(16, 0, 0));
            line.append(this.formatLine(0, 1, sb.toString(),
                    this.rb.getString("recptrettitle"), sb.toString()));
        } else {
            sb.append(this.getAsterisk(8, 1, 0));
            line.append(this.formatLine(0, 2, sb.toString(),
                    this.rb.getString("recptrettitle"), sb.toString()));
        }
        this.addLineToReceipt(0, 0, 1, line.toString());
    }

    /**
     * @param item
     * @throws UnsupportedEncodingException
     */
    private void addDiscount(ItemMode item) throws UnsupportedEncodingException {
        if (item.getExtendedDiscountAmount() > 0) {
            sb.setLength(0);
            sb.append(this.getAmountByMode(
                    item.getExtendedDiscountAmount(), 1));

            line.setLength(0);
            line.append(this.formatLine(4, 1,
                    this.rb.getString("recptretdiscount"), sb.toString()));
            this.addLineToReceipt(0, 0, 0, line.toString());
        }    	
    }
    
    /**
     * @param item
     * @param price
     * @throws UnsupportedEncodingException
     */
    private void addQuantity(ItemMode item, String price)
    		throws UnsupportedEncodingException {
        int col = 0;
        int digit = StringUtility.isNullOrEmpty(item.getMmID()) ? 1 : 2;
        sb.setLength(0);
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            col = StringUtility.isNullOrEmpty(item.getMmID()) ? 4 : 3;
            sb.append("Å~ ");
            sb.append(this.getAmountByMode(item.getQuantity(), -1));
        } else {
            col = StringUtility.isNullOrEmpty(item.getMmID()) ? 5 : 4;
            sb.append(this.getAmountByMode(item.getQuantity(), -1));
            sb.append(this.rb.getString("recptunitstr"));
        }
        line.setLength(0);
        line.append(this.formatLine(digit, col, item.getItemID(),
                sb.toString(), price));

        this.addLineToReceipt(0, 0, 0, line.toString()); 	
    }
    
    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     */
    private void itemFormat() throws UnsupportedEncodingException {

        // item format
        for (ItemMode item : this.receipt.getItemList()) {

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

            // ItemID, Quantity, ActualSalesUnitPrice
            String price = this.getAmountByMode(item.getExtendedAmount(), -1);

            // Add quantity
            addQuantity(item, price);

            // Add discount
            addDiscount(item);

            itemQuantity += item.getQuantity();
        }
    }

    /**
     * execute receipt format
     * 
     * @throws UnsupportedEncodingException
     */
    private void footerFormat() throws UnsupportedEncodingException {

        // Add one line
        this.receiptCmd.add(this.setFeedLines(1));

        // Add tax percent
        sb.setLength(0);
        sb.append(this.getAmountByMode(this.itemQuantity, -1));
        sb.append(this.rb.getString("recptunitstr"));

        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recptnumofitems"),
                sb.toString()));
        this.addLineToReceipt(0, 0, 0, line.toString());

        // Add total
        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recpttotalstr"),
                this.getAmountByMode(this.receipt.getGrandAmount(), -1)));
        this.addLineToReceipt(1, 0, 0, line.toString());

        // Add tax
        sb.setLength(0);
        sb.append("(");
        sb.append(this.rb.getString("recpttaxstr"));

        line.setLength(0);
        line.append(this.formatLine(0, 1, sb.toString(),
                this.getAmountByMode(this.receipt.getTaxAmount(), -1) + ")"));
        this.addLineToReceipt(0, 0, 1, line.toString());

        if (flg != 3) {
            // Add tender
            this.addTender();
        }

        // Add payment
        sb.setLength(0);
        sb.append(this.getAmountByMode(this.receipt.getGrandAmount(), -1));

        line.setLength(0);
        line.append(this.formatLine(0, 1, this.rb.getString("recptpaymentstr"),
                sb.toString()));
        this.addLineToReceipt(0, 0, 1, line.toString());
        
        // for fantamiliar points system        
        MemberInfo member = receipt.getMemberInfo();
        if (member != null) {
        	line.setLength(0);
        	line.append(this.formatLine(0, 1,"", ""));
        	line.append(this.formatLine(0, 1,
        			this.rb.getString("recptline"), ""));
        	this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1,"", ""));
        	this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1, this.rb.getString("recptpointtarget"),
        			    member.getAmountForPoints() +
        			    this.rb.getString("recptpointamountsymble")));
        	this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1, this.rb.getString("recptpointscorea"),
        			    member.getCorrectionPoints() +
        			    this.rb.getString("recptpointsymble")));
        	this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1, this.rb.getString("recptpointuse"),
    			    "0" + this.rb.getString("recptpointsymble")));
        	this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1,
        			this.rb.getString("recptpointscoreb"),
        			member.getPointsPrior() + this.rb.getString("recptpointsymble")));
        	this.addLineToReceipt(0, 0, 0, line.toString());
            String recpttotal =  rb.getString("recptpointtotal"); 
            
            String businessDate = this.receipt.getBusinessDayDate();
            recpttotal = recpttotal.replace("0000/00/00",
         		     businessDate.substring(0,4) + "/12/31");
            line.setLength(0);
            line.append(this.formatLine(0, 1, recpttotal,
            		member.getLostPoints() + this.rb.getString("recptpointsymble")));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1, "", ""));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointmemberno"),
            		member.getInputtedMembershipId().getElementValue()));
            this.addLineToReceipt(0, 0, 0, line.toString());
            String sDate = member.getExpirationDate();
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointexpirationdate"),
            		sDate.substring(0,4) + "/" + sDate.substring(4,6) +
            		"/" + sDate.substring(6,8)));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointremark1"),
            		""));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointremark2"),
            		""));
            this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1,"", ""));
        	this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptline"), ""));
            this.addLineToReceipt(0, 0, 0, line.toString());
        	line.setLength(0);
        	line.append(this.formatLine(0, 1,
        			this.rb.getString("recptpointCancelHeader"), ""));
        	this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointcommserialnumber"),
            		member.getPointsAcknowledgeId()));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointtranseqnumber"),
            		member.getPiontsTransactionId()));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointresponse"),
            		member.getServerStatusCode()));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointmemberstat"),
            		member.getStatusCode()));
            this.addLineToReceipt(0, 0, 0, line.toString());
            long total = Long.valueOf( member.getPointsPrior()) ; 
            total += Long.valueOf(member.getCorrectionPoints());
            if (total < 0 ) {
            	total = 0;
            }
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptpointbalance"),
            		String.valueOf(total)));
            this.addLineToReceipt(0, 0, 0, line.toString());
            line.setLength(0);
            line.append(this.formatLine(0, 1,
            		this.rb.getString("recptline"), ""));
            this.addLineToReceipt(0, 0, 0, line.toString());
        }
        if (flg == 3) {
            int aigen = 1;
            if ("en".equals(this.rb.getLocale().getLanguage())) {
                aigen = 0;
            }
            this.addRemarksColumn(aigen,
                    this.rb.getString("recptretappellation"));
            this.addRemarksColumn(0, this.rb.getString("recpttel"));
            this.addRemarksColumn(0, this.rb.getString("recptretcastcolum"));
            this.addRemarksColumn(0, "");
            this.addRemarksColumn(0, this.rb.getString("recptmgtapproval"));
        }

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

        if (flg != 3) {
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
                    && (this.receipt.getReferenceNo() == null)) {
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

                String[] rs = this.formatReferenceNo(this.receipt
                        .getReferenceNo());
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
            if (flg == 2) {
                // Add one line
                this.receiptCmd.add(this.setFeedLines(1));
                // copy for card company
                this.addLineToReceipt(0, 1, 0,
                        this.rb.getString("recptcreditslip"));
            }
        } else {
            // Add one line
            this.receiptCmd.add(this.setFeedLines(1));
            this.addLineToReceipt(0, 1, 0, this.rb.getString("recptretmemo"));

            line.setLength(0);
            if ("en".equals(this.rb.getLocale().getLanguage())) {
                line.append(this.getAsterisk(12, 0, 0));
                line.append(this.getBlanks(1));
                line.append(this.rb.getString("recptretcashtitle"));
                line.append(this.getBlanks(1));
                line.append(this.getAsterisk(12, 0, 0));
            } else {
                line.append(this.formatLine(0, 0, this.getAsterisk(4, 1, 1),
                        this.rb.getString("recptretcashtitle"),
                        this.getAsterisk(4, 1, 1)));
            }
            this.addLineToReceipt(1, 1, 0, line.toString());
        }

    }

    /**
     * Add Ndivided
     * @throws UnsupportedEncodingException 
     */
    private void addNDivided() throws UnsupportedEncodingException {
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
        if (flg == 2) {
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
     * Add original reference number
     */
    private void addOriginalRefNo() {
        if (receipt.getOriginalReferenceNo() != null) {
	        this.addLineToReceipt(0, 0, 0,
	                rb.getString("recptOrigReferenceNo"));
	        line.setLength(0);
	        line.append(this.receipt.getOriginalReferenceNo());
	        line.append(this.getBlanks(2));
	        this.addLineToReceipt(0, 2, 0, line.toString());
        }    	
    }
    
    /**
     * @throws UnsupportedEncodingException
     */
    private void addTender() throws UnsupportedEncodingException {

        if (this.receipt.getCreditPament() != 0) {

            //
            line.setLength(0);
            for (int i = 0; i < this.lineMaxLimit / 2; i++) {
                line.append("ÅE");
            }
            String dot = line.toString();
            this.addLineToReceipt(0, 1, 0, dot);
            //
            this.addLineToReceipt(0, 1, 0, rb.getString("recptretcredittitle"));
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
            addNDivided();
            
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

            if (this.receipt.getReferenceNo() != null) {  
                line.setLength(0);
                line.append(this.receipt.getReferenceNo());
                line.append(this.getBlanks(2));
                this.addLineToReceipt(0, 2, 0, line.toString());
            }

            addSignatureColumn();

            this.addLineToReceipt(0, 1, 0, dot);

            // RES-5556 
            // Add Original reference number
            addOriginalRefNo();
            
            // Add original payment TODO

        }
    }
}
