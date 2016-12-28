/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.dao;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.TransactionVariable;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.model.poslog.Amount;
import ncr.res.mobilepos.journalization.model.poslog.Discount;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.PriceDerivationResult;
import ncr.res.mobilepos.journalization.model.poslog.RetailPriceModifier;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Sale;
import ncr.res.mobilepos.journalization.model.poslog.Tax;
import ncr.res.mobilepos.journalization.model.poslog.Tender;
import ncr.res.mobilepos.journalization.model.poslog.TenderChange;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptContent;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;
import ncr.res.mobilepos.networkreceipt.model.ReceiptCredit;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMixMatchBlock;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
import ncr.res.mobilepos.networkreceipt.model.ReceiptProductItem;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * A Data Access Object implementation for Receipt Generation.
 */
public class SQLServerReceiptDAO extends AbstractDao implements IReceiptDAO {
    /**
     * class instance of the DBManager.
     */
    private DBManager dbManager;
    /**
     * class instance of the Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * class instance of the debug log printer.
     */
    private Trace.Printer tp;
    /**
     * constant. the company id of guest accounts.
     */
    private static final String GUEST_CORP_ID = "9999";
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "RecptDAO";

    /**
     * constructor.
     *
     * @throws DaoException - thrown when any exception occurs
     */
    public SQLServerReceiptDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {

        return dbManager;
    }

    @Override
    public final PaperReceipt getPaperReceipt(final PosLog poslog,
            final String txid, final String deviceNo, final String operatorNo,
            final BufferedImage logoImg) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("deviceNo", deviceNo)
                .println("operatorNo", operatorNo)
                .println("poslog", poslog.toString());

        PaperReceipt paperReceipt = new PaperReceipt();
        Transaction transaction = poslog.getTransaction();
        // for fantamiliar points system
        paperReceipt.setBusinessDate(transaction.getBusinessDayDate());
        // Convert price derivation result list to mix match block map,
        // key=PriceDerivationRuleID.
        // Because PriceDerivationResult is the aggregate for every Mix Match.
        // Use key for searching matched items.
        boolean haveMixMatch = false;
        Map<String, ReceiptMixMatchBlock> mmBlockMap = null;
        if (transaction.getRetailTransaction()
                .getPriceDerivationResult() != null) {
            List<PriceDerivationResult> priceDerivationResults = new ArrayList<PriceDerivationResult>();
            priceDerivationResults = transaction.getRetailTransaction()
                    .getPriceDerivationResult();
            mmBlockMap = new HashMap<String, ReceiptMixMatchBlock>();
            for (PriceDerivationResult result : priceDerivationResults) {
                String mmRuleID = result.getPriceDerivationRule()
                        .getPriceDerivationRuleID();
                ReceiptMixMatchBlock mmBlock = new ReceiptMixMatchBlock();
                // Set Mix Match Rule ID
                mmBlock.setMmRuleID(mmRuleID);
                // Set Mix Match Name
                mmBlock.setMmName(result.getPriceDerivationRule()
                        .getDescription());
                // Set Mix Match total discount
                mmBlock.setMmAmount(result.getAmount().getAmount());
                // Set Mix Match total previous price
                mmBlock.setMmPreviousPrice(result.getPreviousPrice());
                // Set Mix Match sales price.
                mmBlock.setMmItemsSalesPrice(mmBlock.getMmPreviousPrice()
                        - mmBlock.getMmAmount());
                // Initialize the Mix Match item count.
                mmBlock.setMmItemCount(0);
                // Create new Mix Match Item list and set into MM block
                mmBlock.setProductItemList(new ArrayList<ReceiptProductItem>());
                mmBlockMap.put(mmRuleID, mmBlock);
            }
            haveMixMatch = true;
        }

        boolean subTotalFlg = true;
        boolean haveCreditPayment = false;

        String corpId = GlobalConstant.getCorpid();
        String storeId = transaction.getRetailStoreID();

        // get receipt header object
        PaperReceiptHeader receiptHeader = getPaperReceiptHeader(storeId);

        // get receipt footer object
        PaperReceiptFooter receiptFooter = getPaperReceiptFooter(storeId, operatorNo, deviceNo, txid);

        // get receipt content object
        PaperReceiptContent receiptContent = new PaperReceiptContent();
        receiptContent.setPaymentCredit(0);
        receiptContent.setPaymentVoucher(0);
        PaperReceiptPayment payment = new PaperReceiptPayment();
        List<ReceiptProductItem> productItemList = new ArrayList<ReceiptProductItem>();
        if (transaction.getReceiptDateTime() == null) {
            receiptContent.setReceiptDateTime("");
            tp.println("No receipt date time.");
        } else {
            receiptContent.setReceiptDateTime(transaction.getReceiptDateTime());
        }
        // Get training mode flag.
        boolean trainingModeFlg = false;
        if (transaction.getTrainingModeFlag() != null
                && "true".equalsIgnoreCase(transaction.getTrainingModeFlag())) {
            trainingModeFlg = true;
        } else {
            trainingModeFlg = false;
        }
        receiptContent.setTrainingModeFlag(trainingModeFlg);
        // Add void void transaction
        RetailTransaction custTran = transaction
                .getRetailTransaction();
        if (custTran != null && custTran.getTransactionStatus() != null
                && "Voided".equals(custTran.getTransactionStatus())) {
            receiptContent.setVoidDateTime(custTran.getVoidBeginDateTime());
        }

        double totalAmt = 0; // total amount
        String creditAmt = null; // credit amount
        for (LineItem lineItem : transaction.getRetailTransaction()
                .getLineItems()) {

            // for fantamiliar points system
            if (lineItem.getPoints() != null) {
                paperReceipt.setMemberInfo(lineItem.getPoints());
            }
            Sale sale = lineItem.getSale();
            if (sale != null) {
                ReceiptProductItem productItem = new ReceiptProductItem();
                if (StringUtility.isNullOrEmpty(sale.getDescription().trim())) {
                    productItem.setProductName(sale.getDepartment().trim());
                } else {
                    productItem.setProductName(sale.getDescription().trim());
                }
                double actualsalesunitprice = sale.getRegularsalesunitprice();
                if (actualsalesunitprice <= sale.getActualsalesunitprice()) {
                    actualsalesunitprice = sale.getActualsalesunitprice();
                }
                productItem.setQuantity(sale.getQuantity());
                productItem.setPrice(actualsalesunitprice);
                // Amount = price * quantity
                productItem.setAmount(productItem.getPrice()
                        * productItem.getQuantity());
                boolean mmFound = false;
                double itemTotalDscnt = 0;
                // Item with mix match promotion.
                List<RetailPriceModifier> retailModifier = sale
                        .getRetailPriceModifier();
                if (sale.getRetailPriceModifier() != null) {
                    for (Iterator<RetailPriceModifier> rpm = retailModifier
                            .iterator(); rpm.hasNext();) {
                        RetailPriceModifier priceModifier = rpm.next();
                        Amount amnt = priceModifier.getAmount();
                        // price override
                        if ("PriceOverride".equalsIgnoreCase(priceModifier.getMethodCode())) {
                            if ("add".equalsIgnoreCase(priceModifier.getAmount().getAction())) {
                                actualsalesunitprice += Math.round(amnt
                                        .getAmount()
                                        / productItem.getQuantity());
                            } else {
                                actualsalesunitprice -= Math.round(amnt
                                        .getAmount()
                                        / productItem.getQuantity());
                            }
                            productItem.setPrice(actualsalesunitprice);
                            productItem.setAmount(productItem.getPrice()
                                    * productItem.getQuantity());
                            sale.setExtendedAmt((long) (productItem.getAmount()));
                        } else if ("Item".equalsIgnoreCase(priceModifier.getPriceDerivationRule()
                                .getApplicationType())) {
                            productItem.setDiscountAmount(amnt.getAmount());
                        } else if ("Mixmatch".equalsIgnoreCase(priceModifier.getPriceDerivationRule()
                                .getApplicationType())) {
                            String ruleId = priceModifier
                                    .getPriceDerivationRule()
                                    .getPriceDerivationRuleID();
                            ReceiptMixMatchBlock mmBlock = mmBlockMap
                                    .get(ruleId);
                            mmBlock.getProductItemList().add(productItem);
                            mmBlock.setMmItemCount(mmBlock.getMmItemCount()
                                    + productItem.getQuantity());
                            itemTotalDscnt += amnt.getAmount();
                            mmFound = true;
                        }
                    }
                }
                if (!mmFound) {
                    productItemList.add(productItem);
                }
                sale.setDiscountAmount(productItem.getDiscountAmount());
                totalAmt += sale.getExtendedAmt() - sale.getDiscountAmount()
                        - itemTotalDscnt;

            } else {
                Discount discount = lineItem.getDiscount();
                if (discount != null) {
                    receiptContent.setSubTotal(totalAmt);
                    subTotalFlg = false;
                    receiptContent.setDiscount(discount.getAmount());
                    totalAmt = totalAmt - Long.valueOf(discount.getAmount());
                } else {
                    if (subTotalFlg) {
                        receiptContent.setSubTotal(totalAmt);
                    }
                }
                Tender tender = lineItem.getTender();
                if (tender != null) {
                    // set total
                    if (totalAmt != 0) {
                        receiptContent.setTotal(totalAmt);
                    }
                    // if use the credit
                    if (tender.getTenderType().equals(
                            TransactionVariable.CREDITDEBIT)) {
                        haveCreditPayment = true;
                        creditAmt = tender.getAmount();
                        if (!trainingModeFlg) {
                            payment = getPaperReceiptPayment(corpId, storeId,
                                    deviceNo, txid,
                                    transaction.getBusinessDayDate());
                        } else {
                            payment = null;
                        }
                        receiptContent
                                .setPaymentCredit(Long.valueOf(creditAmt));
                    } else {
                        // If there is not credit payment, set payment to null
                        if (!haveCreditPayment) {
                            payment = null;
                            receiptContent.setPaymentCredit(0);
                        }
                        if (tender.getTenderType().equals(
                                TransactionVariable.CASH)) {
                            receiptContent.setPaymentCash(Long.valueOf(tender
                                    .getAmount()));
                        }
                        TenderChange tenderChange = tender.getTenderChange();
                        if (tenderChange != null
                                && tenderChange.getAmount() != 0) {
                            receiptContent.setPaymentChange(Double
                                    .valueOf(tenderChange.getAmount()));
                        }
                        if (tender.getTenderType().equals(
                                TransactionVariable.VOUCHER)) {
                            receiptContent.setPaymentVoucher(Long
                                    .valueOf(tender.getAmount()));
                        }
                    }
                }
                List<Tax> tax = lineItem.getTax();
                if (null != tax && null != tax.get(0)) {
                    receiptContent.setTax(tax.get(0).getAmount());
                }
            }
        }
        // If there is Mix Match promotion, add it into Receipt content.
        if (haveMixMatch && !mmBlockMap.isEmpty()) {
            List<ReceiptMixMatchBlock> mmBlockList = new ArrayList<ReceiptMixMatchBlock>(
                    mmBlockMap.values());
            receiptContent.setMmBlockList(mmBlockList);
        }
        receiptContent.setProductItemList(productItemList);
        paperReceipt.setReceiptHeader(receiptHeader);
        paperReceipt.setReceiptContent(receiptContent);
        paperReceipt.setReceiptPayment(payment);
        paperReceipt.setReceiptFooter(receiptFooter);

        tp.methodExit(paperReceipt.toString());
        return paperReceipt;
    }

    @Override
    public final PaperReceiptHeader getPaperReceiptHeader(final String storeid)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeid);

        // Create the Model of PaperReceiptHeader
        PaperReceiptHeader header = new PaperReceiptHeader();
        String ads = null; // Commercial
        List<String> adsList = new ArrayList<String>();

        Connection connection = null;
        PreparedStatement getHeaderStmt = null;
        ResultSet rs = null;

        try {

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getHeaderStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-storeInfo"));
            getHeaderStmt.setString(1, storeid);

            rs = getHeaderStmt.executeQuery();

            if (rs.next()) {
                header.setAddress(rs.getString("StoreAddr")); // set address
                header.setTel(rs.getString("StoreTel")); // set telephone number
                header.setSiteUrl(rs.getString("StoreUrl")); // set the url of
                                                                // store
                ads = rs.getString("Ads"); // set commercial text
            } else {
                tp.println("Store not found");
            }
            if (ads != null) {
                if (ads.contains("|")) {
                    String[] adsArray = ads.split("\\Q|\\E");
                    for (int i = 0; i < adsArray.length; i++) {
                        adsList.add(adsArray[i]);
                    }
                } else {
                    adsList.add(ads);
                }
                header.setCommercialList(adsList);
            } else {
                tp.println("No ads.");
                header.setCommercialList(new ArrayList<String>());
            }

        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get store info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getHeaderStmt, rs);

            tp.methodExit(header.toString());
        }
        return header;
    }

    @Override
    public final PaperReceiptPayment getPaperReceiptPayment(
            final String corpId, final String storeId, final String termId,
            final String txId, final String txDate) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("corpId", corpId)
                .println("storeId", storeId).println("termId", termId)
                .println("txId", txId).println("txDate", txDate);

        PaperReceiptPayment payment = new PaperReceiptPayment();
        // If corp id equals 9999, set payment with dummy data.
        if (corpId.equals(GUEST_CORP_ID)) {
            payment.setCrCompanyCode("99");
            payment.setRecvCompanyCode("99");
            payment.setPanLast4("************9999");
            payment.setExpiryMaster("**/**");
            payment.setCreditAmount("9999");
            payment.setCompanyName("JCB");
            payment.setPaymentSeq(String.format("%05d", 1));
            payment.setApprovalCode(String.format("%07d",
                    Integer.valueOf("9999999")));
            payment.setTraceNum(String.format("%06d", Integer.valueOf("999999")));
            payment.setSettlementNum(getSettlementNum("999999999999999999999999"));
            payment.setCaStatus(String.format("%02d", 0));
            tp.println("This is dummy data for 9999 demo environment.");
            tp.println(payment.toString());
            tp.methodExit(payment.toString());
            return payment;
        }
        String crCompanyCode = "";
        int paymentSeq = 0;
        String approvalCode = "";
        String traceNo = "";
        String settlementNum = "";
        int caStatus = 0;

        Connection connection = null;
        PreparedStatement getPaymentStmt = null;
        PreparedStatement getCreditCompanyName = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            connection = dbManager.getConnection();

            rs = new SQLServerPastelPortLogDAO().getPastelPortLogData(storeId, txId, txDate);
            if (rs.next()) {
                crCompanyCode = String.valueOf(rs.getInt("CrCompanyCode"));
                payment.setCrCompanyCode(crCompanyCode);
                payment.setRecvCompanyCode(String.valueOf(rs
                        .getInt("RecvCompanyCode")));
                payment.setPanLast4(rs.getString("Pan"));
                caStatus = rs.getInt("CaStatus");
                payment.setExpiryMaster("**/**");
                paymentSeq = rs.getInt("PaymentSeq");
                approvalCode = rs.getString("ApprovalCode");
                traceNo = rs.getString("TraceNum");
                settlementNum = rs.getString("SettlementNum");
                payment.setPaymentSeq(rs.getString("PaymentSeq"));
                payment.setCaStatus(rs.getString("CaStatus"));
                payment.setApprovalCode(approvalCode);
                payment.setTraceNum(traceNo);
                payment.setSettlementNum(settlementNum);
                payment.setCreditAmount(String.valueOf(rs.getInt("Amount")));
            } else {
                tp.println("PastelPort Log not found");
            }
            closeConnectionObjects(null, getPaymentStmt, rs);
            // get credit company name from PRM_CREDIT_COMPANY
            getCreditCompanyName = connection.prepareStatement(sqlStatement
                    .getProperty("get-creditCompanyName"));
            getCreditCompanyName.setString(1, crCompanyCode);

            rs1 = getCreditCompanyName.executeQuery();
            if (rs1.next()) {
                payment.setCompanyName(rs1.getString("CompanyName"));
            } else {
                payment.setCompanyName("");
                tp.println("No company name");
            }
            payment.setPaymentSeq(String.format("%05d", paymentSeq));
            payment.setApprovalCode(String.format("%07d",
                    Integer.valueOf(approvalCode)));
            payment.setTraceNum(String.format("%06d", Integer.valueOf(traceNo)));
            payment.setSettlementNum(getSettlementNum(settlementNum));
            payment.setCaStatus(String.format("%02d", caStatus));
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get credit company info.", sqlEx);
        } finally {
            closeConnectionObjects(null, getPaymentStmt, rs);
            closeConnectionObjects(connection, getCreditCompanyName, rs1);

            tp.methodExit(payment.toString());
        }

        return payment;
    }

    @Override
    public final PaperReceiptFooter getPaperReceiptFooter(final String storeId,
            final String operatorNo, final String termId, final String txId)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeId", storeId)
                .println("operatorNo", operatorNo).println("termId", termId)
                .println("txId", txId);

        PaperReceiptFooter footer = new PaperReceiptFooter();
        footer.setRegisterNum(termId);
        footer.setTradeNum(txId);
        String operatorName = getOperatorName(operatorNo);
        if (operatorName == null) {
            operatorName = "";
        }
        footer.setSaleMan(operatorName);

        Connection connection = null;
        PreparedStatement getFooterStmt = null;
        ResultSet rs = null;

        try {

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getFooterStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-storeInfo"));
             getFooterStmt.setString(1, storeId);

            rs = getFooterStmt.executeQuery();

            if (rs.next()) {
                footer.setShopName(rs.getString("StoreName"));
                footer.setDepartmentName(rs.getString("SalesSpaceName"));
                footer.setHoldName(rs.getString("EventName"));
            } else {
                tp.println("Store not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get store info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getFooterStmt, rs);

            tp.methodExit(footer.toString());
        }

        return footer;
    }

    @Override
    public final String getPrinterName(final String storeid, final String termid)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeid)
                .println("termid", termid);

        String printerName = "";
        Connection connection = null;
        PreparedStatement getPrinterNameStmt = null;
        ResultSet rs = null;
        try {

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getPrinterNameStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-printerName"));
           getPrinterNameStmt.setString(SQLStatement.PARAM1, storeid);
            getPrinterNameStmt.setString(SQLStatement.PARAM2, termid);

            rs = getPrinterNameStmt.executeQuery();
            if (rs.next()) {
                printerName = rs.getString("PrinterName");
                tp.println("printerName", printerName);
            } else {
                tp.println("Printer Info not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get printer name.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getPrinterNameStmt, rs);

            tp.methodExit(printerName);
        }

        return printerName;
    }

    @Override
    public final NetPrinterInfo getPrinterInfo(final String storeid,
            final String termid) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid:" + storeid)
                .println("termid:" + termid);

        NetPrinterInfo netPrinterInfo = new NetPrinterInfo();
        Connection connection = null;
        PreparedStatement getnetPrinterInfoStmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getnetPrinterInfoStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-netprinterinfo"));
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM1, storeid);
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM2, termid);

            rs = getnetPrinterInfoStmt.executeQuery();
            if (rs.next()) {
                netPrinterInfo.setUrl(rs.getString("IpAddress"));
                netPrinterInfo.setPortTCP(rs.getInt("PortNumTcp"));
                netPrinterInfo.setPortUDP(rs.getInt("PortNumUdp"));
            } else {
                tp.println("Printer Info is not found for Store=" + storeid
                        + ";Terminal=" + termid);
                netPrinterInfo = null;
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get printer info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getnetPrinterInfoStmt, rs);

            tp.methodExit(netPrinterInfo);
        }

        return netPrinterInfo;
    }

    @Override
    public final String getDocTaxStampPath(final String storeid)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeid);

        String docTaxPath = null;
        Connection connection = null;
        PreparedStatement getDocTaxStampPathStmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getDocTaxStampPathStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-stamp-tax-path"));
           getDocTaxStampPathStmt.setString(1, storeid);

            rs = getDocTaxStampPathStmt.executeQuery();
            if (rs.next()) {
                docTaxPath = rs.getString("storedoctax");
            } else {
                tp.println("DocTaxStamp path not found for store=" + storeid);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get documentary tax path.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getDocTaxStampPathStmt, rs);

            tp.methodExit(docTaxPath);
        }
        return docTaxPath;
    }

    @Override
    public final String getLogoFilePath(final String storeid)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeid);

        String logoPath = null;
        Connection connection = null;
        PreparedStatement getLogoPathStmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getLogoPathStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-stamp-tax-path"));
            getLogoPathStmt.setString(1, storeid);

            rs = getLogoPathStmt.executeQuery();
            if (rs.next()) {
                logoPath = rs.getString("storelogo");
            } else {
                tp.println("Logo path not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get logo path.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getLogoPathStmt, rs);

            tp.methodExit(logoPath);
        }
        return logoPath;
    }

    /**
     * gets the settlement number.
     *
     * @param settlementNum - the settlement number
     * @return String - formatted settlement number
     */
    private String getSettlementNum(final String settlementNum) {

        if (settlementNum.length() != 24) {
            return settlementNum;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(settlementNum.substring(0, 6)).append("-");
        sb.append(settlementNum.substring(6, 14)).append("-");
        sb.append(settlementNum.substring(14, 19)).append("-");
        sb.append(settlementNum.substring(19));

        return sb.toString();
    }

    /**
     * get credit number from TXL_CREDITLOG.
     *
     * @param txid - transaction id
     * @return credit number
     * @throws DaoException - thrown when any exception occurs
     */
    public final ReceiptCredit getReceiptCredit(final String txid)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid);

        ReceiptCredit receiptCredit = new ReceiptCredit();
        Connection connection = null;
        PreparedStatement getCreditStmnt = null;
        ResultSet rs = null;
        try {

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getCreditStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-credit-byid"));
            getCreditStmnt.setString(1, txid);

            rs = getCreditStmnt.executeQuery();
            if (rs.next()) {
                receiptCredit.setPan(rs.getString("txPan"));
                receiptCredit.setExpirationdate(rs
                        .getString("txExpirationDate"));
            } else {
                tp.println("Credit Info not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get credit info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getCreditStmnt, rs);

            tp.methodExit(receiptCredit.toString());
        }
        return receiptCredit;
    }

    /**
     * get operator name from MST_USER_CREDENTIALS.
     *
     * @param operatorNo - operator number to query
     * @return String - the operator name
     * @throws DaoException - thrown when any exception occurs
     */
    @Override
    public final String getOperatorName(final String operatorNo) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("operatorNo", operatorNo);

        String operatorName = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement prepdStatement = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            prepdStatement = connection.prepareStatement(sqlStatement
                    .getProperty("get-operator-status"));
            prepdStatement.setString(1, operatorNo);
            result = prepdStatement.executeQuery();
            if (result.next()) {
                operatorName = result.getString("OperatorName");
            } else {
                tp.println("Operator not found");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get operator.", e);
            throw new DaoException("SQLException:" + " @SQLServerReceiptDAO."
                    + functionName + " - Error get operator name", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, result);

            tp.methodExit(operatorName);
        }

        return operatorName;
    }

    /**
     * get receipt information
     *
     * @param poslog
     * @param storeId
     */
    @Override
    public ReceiptMode getReceiptInfo(PosLog poslog, String storeId)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("poslog", poslog.toString())
                .println("storeId", storeId);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet rs = null;
        ReceiptMode receipt = new ReceiptMode();

        // get operator name by operator id
        String operatorNo = poslog.getTransaction().getOperatorID().getValue();
        tp.println("operatorNo", operatorNo);
        String clerkName = this.getOperatorName(operatorNo);
        receipt.setClerkName(StringUtility.convNullToEmpty(clerkName));
        Transaction tran = poslog.getTransaction();
        if (tran.getRetailTransaction() != null &&
                !StringUtility.isNullOrEmpty(tran.getRetailTransaction().getVoidOperatorID())) {
            String voidClerkName = this.getOperatorName(tran.getRetailTransaction()
                    .getVoidOperatorID());
            receipt.setVoidClerkName(voidClerkName);
        }

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-storeInfo"));
            selectStmt.setString(SQLStatement.PARAM1, storeId);

            rs = selectStmt.executeQuery();
            if (rs.next()) {
                receipt.setStoreName(rs.getString("StoreName"));
                receipt.setTelNo(rs.getString("StoreTel"));
                receipt.setStoreType(rs.getInt("StoreType"));
                receipt.setAds(rs.getString("Ads"));
                receipt.setStoreAddress(rs.getString("StoreAddr"));
            } else {
                tp.println("Store not found");
            }
            closeConnectionObjects(null, selectStmt, rs);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get store or customer tier info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, selectStmt, rs);

            tp.methodExit(receipt.toString());
        }

        return receipt;
    }

    /**
     * Get the transaction POSLog XML in the TXL_POSLOG table. by specifying the
     * transaction number.
     *
     * @param terminalid        The Terminal ID
     * @param storeid           The Store ID
     * @param txid              The Transaction Number
     * @param summarydateid     The Summary Date ID
     * @param trainingflag      The Training Flag
     * @return The POSLog XML, else, empty string with transaction not found.
     * @throws DaoException Exception thrown when setting up the prepared
     *             Statement fails.
     */
    @Override
    public final String getPOSLogTransaction(final String terminalid,
            final String storeid, final String txid, final String summarydateid,
            final int trainingflag)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("terminalid", terminalid)
                .println("storeid", storeid).println("txid", txid)
                .println("summarydate", summarydateid).println("trainingflag", trainingflag);

        Connection connection = null;
        String posLogXML = "";

        try {
            connection = dbManager.getConnection();
            posLogXML = getTransaction(terminalid, storeid, txid,
                    summarydateid, connection, trainingflag);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get poslog.", ex);
            throw new DaoException("SQLException: @" + "SQLServerReceiptDAO."
                    + functionName, ex);
        } finally {
            closeConnectionObjects(connection, null, null);

            tp.methodExit();
        }
        return posLogXML;
    }

    /**
     * Get the transaction POSLog XML by specifying the transaction number.
     *
     * @param terminalid    The Device Terminal ID
     * @param storeid       The Store ID
     * @param txid          The Transaction Number
     * @param summarydateid The Summary Date Id
     * @param trainingflag  The Training Flag
     * @param connection The Database Connection
     * @return The POSLog XML representation for the Transaction
     * @throws DaoException The Exception thrown when the getting of Transaction
     *             fails.
     */
    private String getTransaction(final String terminalid,
            final String storeid, final String txid,
            final String summarydateid, final Connection connection,
            final int trainingflag)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("terminalid", terminalid)
                .println("storeid", storeid).println("txid", txid)
                .println("summarydate", summarydateid)
                .println("trainingflag", trainingflag);

        PreparedStatement select = null;
        ResultSet result = null;
        String posLogXML = "";
        //start add by mlwang 2014/09/03
        java.sql.Date summarydate = java.sql.Date.valueOf(summarydateid);
        //end add 2014/09/03
        try {
            /*
             * Retrieves sql query statement from
             * /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection
                    .prepareStatement(sqlStatement
                            .getProperty("get-poslog-xml-by-terminalid-storeid-txid-summarydateid"));

            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, terminalid);
            select.setString(SQLStatement.PARAM3, txid);
            //start add by mlwang 2014/09/03
            select.setDate(SQLStatement.PARAM4, summarydate);
            select.setInt(SQLStatement.PARAM5, trainingflag);
            //end add 2014/09/03

           result = select.executeQuery();
            if (result.next()) {
                posLogXML = result.getString(result.findColumn("Tx"));
            } else {
                tp.println("No transaction found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get poslog xml.", ex);
            throw new DaoException("Exception: @getTransaction - "
                    + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(null, select, result);

            tp.methodExit();
        }
        return posLogXML;
    }
}
