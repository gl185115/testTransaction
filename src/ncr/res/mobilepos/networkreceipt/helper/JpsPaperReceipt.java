/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.networkreceipt.model.ReceiptLine;

/**
 * JpsPaperReceipt Class is a print service which suport
 * MobilePOS Paper Receipt processes.
 *
 */
public class JpsPaperReceipt implements Printable {
    /**
     * class instance of List of ReceiptLine.
     */
    private List<ReceiptLine> receiptLines;
    /**
     * class instance of logo.
     */
    private BufferedImage logo;
    /**
     * class instance of printer name.
     */
    private String printerName;
    /**
     * class instance of Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * class instance of debug trace printer.
     */
    private Trace.Printer tp;

    /**
     * Constructor.
     * @param printerNameToSet - the printer name
     * @param receiptLinesToSet - the List of RecieptLine to print
     * @param logoImg - the logo image
     */
    public JpsPaperReceipt(final String printerNameToSet,
            final List<ReceiptLine> receiptLinesToSet,
            final BufferedImage logoImg) {
        this.printerName = printerNameToSet;
        this.receiptLines = receiptLinesToSet;
        this.logo = logoImg;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * Constructor.
     * @param receiptLinesToSet - the List of RecieptLine to print
     * @param logoImg - the logo image
     */
    public JpsPaperReceipt(final List<ReceiptLine> receiptLinesToSet,
            final BufferedImage logoImg) {
        this.receiptLines = receiptLinesToSet;
        this.logo = logoImg;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    @Override
    public final int print(final Graphics g,
            final PageFormat pf, final int pageIndex)
            throws PrinterException {
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            int current = 60;
            g2d.setColor(Color.black);
            if (logo != null) {
                g2d.drawImage(logo, null, 12, 10);
                current = 60;
            } else {
                g2d.setFont(new Font("‚l‚r ƒSƒVƒbƒN", Font.BOLD, 18));
                g2d.drawString("‚m‚b‚q ƒXƒgƒA", 15, 14);
                current = 24;
            }

            int height = 0;
            for (int i = 0; i < receiptLines.size(); i++) {
                Font font = receiptLines.get(i).getFont();
                if (font.getSize() == 12) {
                    g2d.setFont(font);
                    g2d.drawString(receiptLines.get(i).getLinedata(),
                            0, current);
                    height = 13;
                } else if (font.getSize() == 10) {
                    g2d.setFont(font);
                    g2d.drawString(receiptLines.get(i).getLinedata(),
                            0, current);
                    height = 10;
                }
                current = current + height;
            }
            return Printable.PAGE_EXISTS;
        } else {
            return Printable.NO_SUCH_PAGE;
        }
    }

    /**
     * initial print work.
     * @return boolean true if successful false if not
     * @throws PrinterException  - thrown when an error in
     * the printer occurs
     */
    public final boolean printReceipt() throws PrinterException {
        //Format of receipt
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        paper.setImageableArea(5.5, 72, 164, 1390);
        pageFormat.setPaper(paper);

        //Create a PriterJob
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        printerJob.setPrintable(this, pageFormat);
        //Find printer driver installed
        PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService service = null;
        if (printerName == null || "".equals(printerName)) {
            service = PrintServiceLookup.lookupDefaultPrintService();
        } else {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(printerName)) {
                    service = services[i];
                    break;
                }
            }
        }

        if (service == null) {
            tp.println("There is no printer");
            return false;
        }

        if (services.length > 0) {            
            tp.println("selected printer: " + service.getName());
            printerJob.setPrintService(service);
            printerJob.print(aset);
        }
        return true;
    }
}
