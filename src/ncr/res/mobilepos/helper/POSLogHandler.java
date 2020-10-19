package ncr.res.mobilepos.helper;
/**
 * â¸íËóöó
 * ÉoÅ[ÉWÉáÉì         â¸íËì˙ït               íSìñé“ñº           â¸íËì‡óe
 * 1.01    2014.12.26      LiQian    PosLogìoò^
 */
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.journalization.model.poslog.ControlTransaction;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.StoredValueFund;
import ncr.res.mobilepos.journalization.model.poslog.TenderControlTransaction;
import ncr.res.mobilepos.journalization.model.poslog.TenderExchange;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.model.poslog.TransactionLink;

/**
 * A Helper Class that handles the information of a given POSLog.
 */
public final class POSLogHandler {

    public final static String TRANSACTION_STATUS_VOIDED = "Voided";
	public final static String LAYAWAY_ITEMTYPE_STOCK = "Stock";
	public final static String PREVIOUSLAYAWAY_ACTION_COMPLETED = "Completed";

    /** Default Constructor. */
    private POSLogHandler() {
    }

    /**
     * Make a POSLog XML into its POSLog Object representation.
     *
     * @param xml                The POSLog Xml
     * @return                    The POSLog Object
     * @throws JAXBException     The Exception thrown when the method fails
     */
    public static PosLog toObject(final String xml) throws JAXBException {
       return GlobalConstant.poslogDataBinding.unMarshallXml(xml);
    }

    /**
     * A simple validation of a POSLog.
     *
     * @param poslog     The POSLog to be validated
     *
     * @return            TRUE, when POSLog is correct, else FALSE
     */
    public static boolean isValid(final PosLog poslog) throws ParseException {
        if (poslog == null) {
            return false;
        }

        Transaction transaction = poslog.getTransaction();
        if (transaction == null) {
            return false;
        }

        // check for required elements
        // elements will be used in saving of poslog to db
        if (transaction.getRetailStoreID() == null ||
            transaction.getWorkStationID() == null ||
            transaction.getSequenceNo() == null ||
            transaction.getBusinessDayDate() == null ||
            transaction.getBeginDateTime() == null) {
            return false;
        }

        // Format check for required dates. If either date is not parsable, throws ParseException.
        DateFormatUtility.parseDate(transaction.getBusinessDayDate(),"yyyy-MM-dd");
        DateFormatUtility.parseDate(transaction.getBeginDateTime(),"yyyy-MM-dd'T'HH:mm:ss");

        // Successfully goes through the validation.
        return true;
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
    // 1.01    2014.12.26      LiQian    PosLogìoò^   End

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
            } else if(controlTransaction.getPosSod() != null) {
                result = TxTypes.POSSOD;
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

                	TransactionLink transactionLink = getNormalTransactionLink(retailTransaction.getTransactionLink());

                	if(TxTypes.LAYAWAY.equals(transactionLink.getReasonCode())){
                		result = TxTypes.LAYAWAY;  // ëOéÛã‡í˘ê≥
                	} else if(TxTypes.HOLD.equals(transactionLink.getReasonCode())){
                        result = TxTypes.HOLD;  // Holdí˘ê≥
                    } else if(TxTypes.CUSTOMERORDER.equals(transactionLink.getReasonCode())){
                        result = TxTypes.CUSTOMERORDER;  // ãqíçí˘ê≥
                    } else if(TxTypes.RESERVATION.equals(transactionLink.getReasonCode())){
                        result = TxTypes.RESERVATION;  // ó\ñÒí˘ê≥
                	} else {
                		result = TxTypes.RETURN;   // return with receipt
                	}
                } else {
                    if (isReturnNoReceiptTransaction(transaction)) {
                        result = TxTypes.RETURN; // return no receipt
                     // 1.01    2014.12.26      LiQian    PosLogìoò^  Start
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
                     // 1.01    2014.12.26      LiQian    PosLogìoò^   End
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

    /**
     * static function that will get not resume TransactionLink
     * @param transactionLinks
     * @return TransactionLink
     */
    public static TransactionLink getNormalTransactionLink(List<TransactionLink> transactionLinks) {
    	TransactionLink result = new TransactionLink();
        if (transactionLinks != null && transactionLinks.size() > 0) {
            for (TransactionLink transactionLink : transactionLinks) {
            	if (!TxTypes.RESUME.equals(transactionLink.getReasonCode())){
            		result = transactionLink;
            		break;
            	}
            }
        }
        return result;
    }
}
