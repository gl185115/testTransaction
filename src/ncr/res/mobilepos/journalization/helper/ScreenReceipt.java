/*
 * Copyright (c) 2015 NCR/JAPAN Corporation
 *
 * Helper Class for creating screen receipt
 */

package ncr.res.mobilepos.journalization.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.helper.PaperReceiptPrint;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.networkreceipt.resource.NetworkReceipt;

public class ScreenReceipt {
    static final String[] EMPTY_ARRAY = new String[0];

    // 7197 printer escape sequence
    
    // ESC + 0x61 + n
    // n: 0 - Left Aligned
    //    1 - Center Aligned
    //    2 - Right Aligned
    static final byte SELECT_JUSTIFICATION = 0x61;

    // GS + 0x21 + nn
    // n(:0 to 7) - left 4bit: horizontal, right 4bit: vertical
    // for ex: GS + 0x21 + 0x11 -- double height, double width
    //         GS + 0x21 + 0x10 -- double width
//    static final byte SELECT_CHARACTER_SIZE = 0x21;
    static final byte SELECT_CHARACTER_SIZE = 0x4c;

    // GS + 0x77 + n
    // n(:0 to 4) + 1/8mm
    static final byte SELECT_BARCODE_WIDTH = 0x77;

    static class ReceiptPrintableTx {
        final String txType;
        ReceiptPrintableTx(String type) {
            txType = type;
        }
        List<List<byte[]>> print(NetworkReceipt nr, IReceiptDAO receiptDAO,
                                 PosLog poslog,
                                 ReceiptMode receiptMode) throws Exception {
            return nr.normalReceipt(receiptDAO,
                                    receiptMode,
                                    Boolean.FALSE.toString(),
                                    Boolean.FALSE.toString(),
                                    Boolean.TRUE.toString(),
                                    Boolean.FALSE.toString(),
                                    Boolean.FALSE.toString());
        }
    }
    static class VoidReceiptPrintableTx extends ReceiptPrintableTx {
        VoidReceiptPrintableTx(String type) {
            super(type);
        }
        List<List<byte[]>> print(NetworkReceipt nr, IReceiptDAO receiptDAO,
                                 PosLog poslog,
                                 ReceiptMode receiptMode,
                                 int trainingFlag) throws Exception {
            return nr.voidReceipt(poslog,
                                  receiptDAO,
                                  receiptMode,
                                  trainingFlag);
        }
    }
    static class ReturnReceiptPrintableTx extends ReceiptPrintableTx {
        ReturnReceiptPrintableTx(String type) {
            super(type);
        }
        List<List<byte[]>> print(NetworkReceipt nr, IReceiptDAO receiptDAO,
                                 PosLog poslog,
                                 ReceiptMode receiptMode,
                                 int trainingFlag) throws Exception {
            NetworkReceipt.ReturnReceiptResult ret = nr.returnReceipt(
                                              poslog,
                                              receiptDAO,
                                              receiptMode,
                                              Boolean.FALSE.toString(),
                                              Boolean.FALSE.toString(),
                                              Boolean.TRUE.toString(),
                                              Boolean.FALSE.toString(),
                                              trainingFlag);
            return ret.list;
        }
    }
    static final ReceiptPrintableTx[] RECEIPT_PRINTABLE_TXS = {
        new ReceiptPrintableTx(TxTypes.SALES),
        new VoidReceiptPrintableTx(TxTypes.VOID),
        new ReturnReceiptPrintableTx(TxTypes.RETURN),
        new ReceiptPrintableTx(TxTypes.LAYAWAY),
        new ReceiptPrintableTx(TxTypes.HOLD),
        new ReceiptPrintableTx(TxTypes.RESERVATION),
        new ReceiptPrintableTx(TxTypes.CUSTOMERORDER)
    };
    public static void print(PosLog poslog, AdditionalInformation info, 
                             String language, ServletContext cxt) throws Exception {
        ReceiptPrintableTx ptr = null;
        for (ReceiptPrintableTx p : RECEIPT_PRINTABLE_TXS) {
            if (p.txType.equals(info.getTransactionType())) {
                ptr = p;
                break;
            }
        }
        if (ptr == null) {
            return; // no receipt
        }
        NetworkReceipt nr = new NetworkReceipt(cxt);
        ReceiptMode receiptMode = nr.resolvePoslog(poslog);
        nr.setLanguage(language, receiptMode);

        IReceiptDAO receiptDAO = DAOFactory.getDAOFactory(
                                       DAOFactory.SQLSERVER).getReceiptDAO();
        List<List<byte[]>> receipts = ptr.print(nr, receiptDAO,
                                                poslog, receiptMode);
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> attr = new ArrayList<String>();
        boolean top = true;
        for (List<byte[]> reList: receipts) {
            for (byte[] line: reList) {
                if (line != null && line.length > 0) {
                    StringBuilder a = new StringBuilder();
                    if (Arrays.equals(PaperReceiptPrint.LOGOBYTES, line)) {
                    } else if (Arrays.equals(PaperReceiptPrint.SIGNBYTES, line)) {
                    } else if (Arrays.equals(PaperReceiptPrint.OTHERBYTES, line)) {
                    } else {
                        if (!top) top = true;
                        for (int i = 0; i < line.length; i++) {
                            if (line[i] == 0x1b && line[i + 1] == SELECT_JUSTIFICATION) {
                                a.append('A').append((char)('0' + line[i + 2]));
                                i += 2;
                            } else if (line[i] == 0x1d && line[i + 1] == SELECT_CHARACTER_SIZE) {
                                a.append('S').append((char)('0' + (line[i + 6] >> 4)))
                                    .append((char)('0' + (line[i + 6] & 0x0f)));
                                i += 6;
                            } else if (line[i] >= 0x1b && line[i] < 0x20) {
                                // skip
                                break;
                            } else {
                                list.add(new String(line, i, line.length - i));
                                attr.add(a.toString());
                                break;
                            }
                        }
                    }
                } else {
                    if (!top) {
                        list.add("");
                        attr.add("");
                    }
                }
            }
        }
        info.setReceipt(list.toArray(EMPTY_ARRAY));
        info.setReceiptAttributes(attr.toArray(EMPTY_ARRAY));
    }
}
