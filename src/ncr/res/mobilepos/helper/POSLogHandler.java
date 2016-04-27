package ncr.res.mobilepos.helper;
/**
 * ���藚��
 * �o�[�W����         ������t               �S���Җ�           ������e
 * 1.01    2014.12.26      LiQian    PosLog�o�^
 */
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.TransactionVariable;
import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.journalization.model.poslog.ControlTransaction;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Discount;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Sale;
import ncr.res.mobilepos.journalization.model.poslog.TenderControlTransaction;
import ncr.res.mobilepos.journalization.model.poslog.TenderSummary;
import ncr.res.mobilepos.journalization.model.poslog.TillSettle;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.model.poslog.TenderExchange;
import ncr.res.mobilepos.journalization.model.poslog.StoredValueFund;
import ncr.res.mobilepos.journalization.model.poslog.Tender;

/**
 * A Helper Class that handles the information of a given POSLog.
 */
public final class POSLogHandler {

    public final static String TRANSACTION_STATUS_VOIDED = "Voided";
    // 1.01    2014.12.26      LiQian    PosLog�o�^  Start
	public final static String LAYAWAY_ITEMTYPE_STOCK = "Stock";
	public final static String PREVIOUSLAYAWAY_ACTION_COMPLETED = "Completed";
	// 1.01    2014.12.26      LiQian    PosLog�o�^  End

    /** Default Constructor. */
    private POSLogHandler() {
    }

    /**
     * Get the SalesTotal from a given POSLog.
     * @param poslog    The POSLog object
     * @return            The SalesTotal
     */
    public static double calculateSaleTotal(final PosLog poslog) {
        double saletotal = 0;

        if (!POSLogHandler.isValid(poslog)) {
            return saletotal;
        }

        List<LineItem> lineItems = poslog
                                    .getTransaction()
                                    .getRetailTransaction()
                                    .getLineItems();

        if (null == lineItems) {
            return saletotal;
        }

        for (LineItem lineItem : lineItems) {
            Sale sale = lineItem.getSale();
            Discount discount  = lineItem.getDiscount();
            double amount = 0;

            //Computation for Sale
            if (null != sale) {
                amount = sale.getExtendedDiscountAmount();

                //Is there Extended Amount from Discount?
                //If no, then the amount should be from the Extended Amount only
                if (0 == amount) {
                    amount = sale.getExtendedAmt();
                }
            }

            //Computation for Discount
            if (null != discount) {
                amount -= Double.valueOf(discount.getAmount());
            }

            saletotal += amount;
        }

        return saletotal;
    }

    /**
     * Make a POSLog XML into its POSLog Object representation.
     *
     * @param xml                The POSLog Xml
     * @return                    The POSLog Object
     * @throws JAXBException     The Exception thrown when the method fails
     */
    public static PosLog toObject(final String xml) throws JAXBException {
        XmlSerializer<PosLog> poslogSerializer =
            new XmlSerializer<PosLog>();

        return poslogSerializer.unMarshallXml(xml, PosLog.class);
    }

    /**
     * A simple validation of a POSLog.
     *
     * @param poslog     The POSLog to be validated
     *
     * @return            TRUE, when POSLog is correct, else FALSE
     */
    public static boolean isValid(final PosLog poslog) {
        if (poslog == null) {
            return false;
        }

        boolean result = true;
        Transaction transaction = poslog.getTransaction();
        if (transaction == null) {
            result = false;

        // check for required elements
        // elements will be used in saving of poslog to db
        } else if (transaction.getRetailStoreID() == null ||
                transaction.getWorkStationID() == null ||
                transaction.getSequenceNo() == null ||
                transaction.getBusinessDayDate() == null ||
                transaction.getBeginDateTime() == null) {
            result = false;
        }

        return result;
    }

    /**
     * Checks whether a POSLog transaction is a Normal Sale or not
     *
     * @param transaction		the transaction to be checked
     *
     * @return					TRUE, if transaction is Normal Sale, else FALSE
     */
    public static boolean isNormalSaleTransaction(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction().getLineItems();

        for (LineItem lineItem : lineItems) {
            if (lineItem.getSale() != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether a POSLog transaction is a Return Without Receipt or not
     *
     * @param transaction		the transaction to be checked
     *
     * @return					TRUE, if transaction is Return Without Receipt, else FALSE
     */
    public static boolean isReturnNoReceiptTransaction(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction().getLineItems();

        for (LineItem lineItem : lineItems) {
            if (lineItem.getRetrn() != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether a POSLog transaction is a Donation or not
     *
     * @param transaction		the transaction to be checked
     *
     * @return					TRUE, if transaction is Donation, else FALSE
     */
    public static boolean isDonationTransaction(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction().getLineItems();

        for (LineItem lineItem : lineItems) {
            if (lineItem.getPaymentOnAccount() != null) {
                String accountNumber = lineItem.getPaymentOnAccount().getAccountNumber();
                if (accountNumber != null && "1".equals(accountNumber)) {
                    return true;
                }
            }
        }

        return false;
    }
    // 1.01    2014.12.26      LiQian    PosLog�o�^  Start
    
    /**
     * Checks whether a POSLog transaction is a Layaway or not
     *
     * @param transaction
     *            the transaction to be checked
     *
     * @return TRUE, if transaction is Layaway, else FALSE
     */
    public static boolean isLayawayTransaction(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
		
		if (lineItems != null && lineItems.size() > 0) {
			for (LineItem lineItem : lineItems) {
				if (lineItem.getLayaway() != null) {
					if (LAYAWAY_ITEMTYPE_STOCK.equals(lineItem.getLayaway()
							.getItemType())) {
						if (lineItem.getLayaway().getInventoryReservationID() != null) {
							return true;
						}
					}
				}
			}
		}
		
        return false;
    }
     
    /**
     * Checks whether a POSLog transaction is a hold or not
     * 
     * @param transaction
     *            the transaction to be checked
     * 
     * @return TRUE, if transaction is hold, else FALSE
     */
    public static boolean isHoldTransaction(Transaction transaction) {

        boolean result = false;
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getRainCheck() != null) {
                    if (lineItem.getRainCheck().getReasonCode() != null) {
                        if (TxTypes.HOLD.equals(lineItem.getRainCheck()
                                .getReasonCode())) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Checks whether a POSLog transaction is a reservation or not
     * 
     * @param transaction
     *            the transaction to be checked
     * 
     * @return TRUE, if transaction is reservation, else FALSE
     */
    public static boolean isReservationTransaction(Transaction transaction) {

        boolean result = false;
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getRainCheck() != null) {
                    if (lineItem.getRainCheck().getReasonCode() != null) {
                        if (TxTypes.RESERVATION.equals(lineItem.getRainCheck()
                                .getReasonCode())) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Checks whether a POSLog transaction is a customerOrder or not
     * 
     * @param transaction
     *            the transaction to be checked
     * 
     * @return TRUE, if transaction is customerOrder, else FALSE
     */
    public static boolean isCustomerOrderTransaction(Transaction transaction) {
        
        boolean result = false;
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getRainCheck() != null) {
                    if (lineItem.getRainCheck().getReasonCode() != null) {
                        if (TxTypes.CUSTOMERORDER.equals(lineItem
                                .getRainCheck().getReasonCode())) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Checks whether a POSLog transaction is a Previous Layaway or not
     *
     * @param transaction
     *            the transaction to be checked
     *
     * @return TRUE, if transaction is Previous Layaway , else FALSE
     */
    public static boolean isPreviousLayawayTransaction(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
        
		if (lineItems != null && lineItems.size() > 0) {
			for (LineItem lineItem : lineItems) {
				if (lineItem.getPreviousLayaway() != null) {
					if (PREVIOUSLAYAWAY_ACTION_COMPLETED.equals(lineItem
							.getPreviousLayaway().getAction())) {
						return true;
					}
				}
			}
		}
		
        return false;
    }
    // 1.01    2014.12.26      LiQian    PosLog�o�^   End

    /**
     * check if SummaryReceipt transaction
     * and check if operator transaction is sign on, off or auto sign off
     *
     * @param controlTransaction
     * @return result
     */
    private static String operatorTransactionType(ControlTransaction controlTransaction) {
        String result = "";
        if(controlTransaction != null){
            if(controlTransaction.getSummaryReceipt() != null){
                result = TxTypes.SUMMARY_RECEIPT;
            } else if(controlTransaction.getOperatorSignOff() != null) {
                result = TxTypes.SIGNOFF;
            } else if(controlTransaction.getPosEod() != null) {
                result = TxTypes.POSEOD;
            } else if(controlTransaction.getOperatorSignOn() != null) {
                result = TxTypes.SIGNON;
            } else if(controlTransaction.getAutoSignOff() != null) {
                result = TxTypes.AUTOSIGNOFF;
            } else if (controlTransaction.getStoredValueFund() != null) {
                StoredValueFund strdValueFund = controlTransaction.getStoredValueFund();
                if (strdValueFund.getInstrument() != null) {
                    result = TxTypes.INQ;
                }
            //add ReceiptReprint Start
            }else if(controlTransaction.getReceiptReprint() != null){
            	result = TxTypes.RECEIPT_REPRINT;
            }
          //add ReceiptReprint End
        }

        return result;
    }

    /**
     * Helper method of tenderTransaction
     * @param tenderControlTransaction
     * @return
     */
    private static String tenderExchange(TenderControlTransaction tenderControlTransaction) {
        String result = "";
        TenderExchange tndrExchange = tenderControlTransaction.getTenderExchange();
        if (tndrExchange.getExchangeDetail() != null) {
            result = TxTypes.EXCHANGE;
        }

        return result;
    }

    /**
     * check if a SOD, EOD, Loan or Pickup transaction
     *
     * @param tenderControlTransaction
     * @return result
     */
    private static String tenderTransaction(TenderControlTransaction tenderControlTransaction) {
        String result = "";
        if (tenderControlTransaction != null) {
        	String dayPart = tenderControlTransaction.getDayPart();
            if (!StringUtility.isNullOrEmpty(dayPart)) {
         	   result = getMatchingTxType(dayPart);
         	} else if (tenderControlTransaction.getTenderExchange() != null) {
                result = tenderExchange(tenderControlTransaction);
            } else if (tenderControlTransaction.getGuarantee() != null) {
            	result = TxTypes.GUARANTEE;
            }
        }
        return result;
    }

    /**
     * @param retailTransaction
     * @param transaction
     * @return
     */
    private static String retailTransaction(RetailTransaction retailTransaction,
            Transaction transaction) {
        String result = "";
        if (retailTransaction != null) {
            String transactionStatus = retailTransaction.getTransactionStatus();

            if (transactionStatus == null) {
                if (retailTransaction.getTransactionLink() != null) {
                	if(TxTypes.LAYAWAY.equals(retailTransaction.getTransactionLink().getReasonCode())){
                		result = TxTypes.LAYAWAY;  // �O�������
                	} else if(TxTypes.HOLD.equals(retailTransaction.getTransactionLink().getReasonCode())){
                        result = TxTypes.HOLD;  // Hold����
                    } else if(TxTypes.CUSTOMERORDER.equals(retailTransaction.getTransactionLink().getReasonCode())){
                        result = TxTypes.CUSTOMERORDER;  // �q������
                    } else if(TxTypes.RESERVATION.equals(retailTransaction.getTransactionLink().getReasonCode())){
                        result = TxTypes.RESERVATION;  // �\�����
                	} else {
                		result = TxTypes.RETURN;   // return with receipt
                	}
                } else {
                    if (isReturnNoReceiptTransaction(transaction)) {
                        result = TxTypes.RETURN; // return no receipt
                     // 1.01    2014.12.26      LiQian    PosLog�o�^  Start
                    } else if (isLayawayTransaction(transaction)) {
                        result = TxTypes.LAYAWAY;
                    } else if (isHoldTransaction(transaction)) {
                    	result = TxTypes.HOLD;
                    } else if (isReservationTransaction(transaction)) {
                    	result = TxTypes.RESERVATION;
                    } else if (isCustomerOrderTransaction(transaction)) {
                    	result = TxTypes.CUSTOMERORDER;                    
                    } else if (isPreviousLayawayTransaction(transaction)
                            || isNormalSaleTransaction(transaction)) {
                        result = TxTypes.SALES;
                     // 1.01    2014.12.26      LiQian    PosLog�o�^   End
                    } else if (isDonationTransaction(transaction)) {
                        result = TxTypes.PAYIN;
                    } else if (isPostPoint(transaction)) {
                        result = TxTypes.POSTPOINT;
                    } else if (isPointTicket(retailTransaction)) {
                        result = TxTypes.POINTTICKET;
                    } else {
                        result = isCardPoints(retailTransaction);
                    }
                }
            } else if (transactionStatus.equals(TRANSACTION_STATUS_VOIDED)) {
                result = TxTypes.VOID;
            }
        }

        return result;
    }

    /**
     * Checks the type of the POSLog transaction
     *
     * @param posLog			the posLog to be checked
     *
     * @return					TRANSACTION_NORMAL if a normal transaction
     * 							TRANSACTION_RETURN_WITH_RECEIPT if a return with receipt transaction
     * 							TRANSACTION_RETURN_NO_RECEIPT if a return without receipt transaction
     * 							TRANSACTION_VOID if a void transaction
     * 							TRANSACTION_CANCEL if a cancel transaction
     */
    public static String getTransactionType(PosLog posLog) {

        String result = "";
        Transaction transaction = posLog.getTransaction();

        // check if a Cancel transaction
        String cancelFlag = transaction.getCancelFlag();
        if (cancelFlag != null && "true".equalsIgnoreCase(cancelFlag)) {
            return TxTypes.CANCEL;
        }

        // check if SummaryReceipt transaction
        // and check if operator transaction is sign on, off or auto sign off
        ControlTransaction controlTransaction = transaction.getControlTransaction();
        result = operatorTransactionType(controlTransaction);
        if (!"".equals(result)) {
            return result;
        }

        // check if a SOD, EOD, Loan, Pickup or CashToDrawer transaction
        TenderControlTransaction tenderControlTransaction =
                transaction.getTenderControlTransaction();
        result = tenderTransaction(tenderControlTransaction);
        if (!"".equals(result)) {
            return result;
        }

        // check if a Normal, Return w/ Receipt, Return w/o Receipt or Void transaction
        RetailTransaction retailTransaction = transaction.getRetailTransaction();
        result = retailTransaction(retailTransaction, transaction);
        if (!"".equals(result)) {
            return result;
        }

        return TxTypes.OTHER;
    }
    /**
     * identify the poslog type if post point.
     * @param transaction
     * @return
     */
    private static boolean isPostPoint(Transaction transaction) {
        List<LineItem> lineItems = transaction.getRetailTransaction()
                .getLineItems();
        
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getPostPoint() != null) {
                   return true;
                }
            }
        }
        return false;
    }
    /**
     * check if poslog has card info
     * @param retailTransaction
     * @return
     */
    private static String isCardPoints(RetailTransaction retailTransaction) {
        String updateType = "";
        List<LineItem> lineItems = retailTransaction.getLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getUpdateInfo() != null) {
                    updateType = lineItem.getUpdateInfo().getUpdateType();
                    if (updateType == null) { //new member has no update type
                        updateType = TxTypes.NEWMEMBER;
                    } else  {
                        switch(updateType) {
                        case "21":
                            updateType = TxTypes.CARDSWITCH;
                            break;
                        case "22":
                            updateType = TxTypes.CARDMERGE;
                            break;
                        case "23":
                            updateType = TxTypes.CARDSTOP;
                            break;
                        default:
                      }
                    }
                }
            }
        }
        return updateType;
    }
    
    private static String getMatchingTxType(String dayPart)  {
    	String result = "";
    	try {
    		for (Field field : TxTypes.class.getDeclaredFields()) {
        		field.setAccessible(true);
        		String value = (String) field.get(TxTypes.class);
        		if (dayPart.equalsIgnoreCase(value)) {
        			result = value;
        		}
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
   /**
     * static function that will check if poslog type is point ticket issue
     * @param retailTransaction
     * @return
     */
    private static boolean isPointTicket(RetailTransaction retailTransaction) {
        List<LineItem> lineItems = retailTransaction.getLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            for (LineItem lineItem : lineItems) {
                if (lineItem.getPointTicket() != null) {
                   return true;
                }
            }
        }
        return false;
    }
    //Add More POSLog helper functions here

}
