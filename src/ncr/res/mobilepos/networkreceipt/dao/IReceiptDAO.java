/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D.
 *
 * IReceiptDAO
 *
 * IReceiptDAO is a DAO Interface for Receipt
 */
package ncr.res.mobilepos.networkreceipt.dao;

import java.awt.image.BufferedImage;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * IReceiptDAO is a DAO Interface for Receipt.
 */
public interface IReceiptDAO {
    /**
     * Generates the paper receipt.
     *
     * @param poslog - poslog to generate the receipt from
     * @param txid - transaction id of the poslog
     * @param deviceNo - terminal no of the requesting terminal
     * @param operatorNo - number of the operator of the transaction
     * @param logoImg - logo image
     * @return PaperReceipt model
     * @throws DaoException - thrown when any exception occurs
     */
    PaperReceipt getPaperReceipt(PosLog poslog, String txid, String deviceNo,
            String operatorNo, BufferedImage logoImg) throws DaoException;

    /**
     * Get the header of paper receipt.
     *
     * @param storeid - store id
     * @return PaperReceiptHeader model
     * @throws DaoException - thrown when any exception occurs
     */
    PaperReceiptHeader getPaperReceiptHeader(String storeid)
            throws DaoException;

    /**
     * Get the payment of paper receipt.
     *
     * @param corpId - company id
     * @param storeId - store id
     * @param termId - terminal id
     * @param paymentSeq - payment sequence of the transaction
     * @param txDate - date of the transaction
     * @return PaperReceiptPayment model
     * @throws DaoException - thrown when any exception occurs
     */
    PaperReceiptPayment getPaperReceiptPayment(String corpId, String storeId,
            String termId, String paymentSeq, String txDate)
            throws DaoException;

    /**
     * Get the footer of paper Receipt.
     *
     * @param storeId - store id
     * @param operatorNo - operator number
     * @param termId - teminal id
     * @param txId - transaction id
     * @return PaperReceiptFooter model
     * @throws DaoException - thrown when any exception occurs
     */
    PaperReceiptFooter getPaperReceiptFooter(String storeId,
            String operatorNo, String termId, String txId) throws DaoException;

    /**
     * TODO legu Get Receipt information.
     *
     * @param storeId - store id
     * @param operatorNo - operator number
     * @param termId - teminal id
     * @param txId - transaction id
     * @return model
     * @throws DaoException - thrown when any exception occurs
     */
    ReceiptMode getReceiptInfo(PosLog poslog, String storeId)
            throws DaoException;

    /**
     * Get printer name from MST_PRINTERINFO.
     *
     * @param storeid - store id
     * @param deviceNO - terminal id
     * @return String - name of the printer
     * @throws DaoException - thrown when any exception occurs
     */
    String getPrinterName(String storeid, String deviceNO)
            throws DaoException;

    /**
     * Get Documentary Tax Stamp Path.
     *
     * @param storeid - store id
     * @return String - path to doc tax stamp
     * @throws DaoException - thrown when any exception occurs
     */
    String getDocTaxStampPath(String storeid)
            throws DaoException;

    /**
     * Get logo file path
     *
     * @param storeid
     * @return
     * @throws DaoException
     */
    String getLogoFilePath(String storeid) throws DaoException;

    /**
     * Get printer information.
     *
     * @param storeid - store id
     * @param termid - terminal id
     * @return NetPrinterInfo model
     * @throws DaoException - thrown when any exception occurs
     */
    NetPrinterInfo getPrinterInfo(String storeid, String termid)
            throws DaoException;

    /**
     * Get the transaction POSLog XML in the TXL_POSLOG table. // TODO legu
     * by specifying the transaction number.
     *
     * @param terminalid        The Terminal ID
     * @param storeid           The Store ID
     * @param txid              The Transaction Number
     * @param summarydateid     The Summary Date Id
     * @param trainingflag      The Training Flag
     * @return                 The POSLog XML, else,
     *                            empty string with transaction not found.
     * @throws DaoException    Exception thrown when setting up
     *                            the prepared Statement fails.
     */
     String getPOSLogTransaction(String terminalid, String storeid,
             String txid, String summarydateid, int trainingflag) throws DaoException;
     /**
     * get operator name from MST_USER_CREDENTIALS.
     *
     * @param operatorNo - operator number to query
     * @return String - the operator name
     * @throws DaoException - thrown when any exception occurs
      */
     String getOperatorName(String operatorNo) throws DaoException;
}
