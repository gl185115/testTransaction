package ncr.res.mobilepos.simpleprinterdriver;

/**
 * Checks Printer Results.
 *
 */
public class PrinterCheckResult{
    /**
     * Timeout.
     */
    private boolean flgTimeout;
    /**
     * Flag printer status.
     */
    private boolean flgPrinterStatus;
    /**
     * Error status.
     */
    private byte errorStatus;
    /**
     * Error number.
     */
    private int errorNo;

    /**
     * Constructor.
     */
    public PrinterCheckResult() {
        this.flgTimeout = false;
        this.flgPrinterStatus = true;
    }
    /**
     * If time out.
     * @return  timeout flag.
     */
    public final boolean isFlgTimeout() {
        return flgTimeout;
    }
    /**
     * Setter for flag timeout.
     * @param flgTimeoutToSet   Flag timeout to set
     */
    public final void setFlgTimeout(final boolean flgTimeoutToSet) {
        this.flgTimeout = flgTimeoutToSet;
    }
    /**
     * If can print.
     * @return  Flag printer status
     */
    public final boolean isFlgPrinterStatus() {
        return flgPrinterStatus;
    }
    /**
     * Setter for flag printer status.
     * @param flgPrinterStatusToSet Flag printer status to set
     */
    public final void setFlgPrinterStatus(final boolean flgPrinterStatusToSet) {
        this.flgPrinterStatus = flgPrinterStatusToSet;
    }
    /**
     * Getter for printer Error Status.
     * @return  Error status
     */
    public final byte getErrorStatus() {
        return errorStatus;
    }
    /**
     * Setter for error status.
     * @param errorStatusToSet  Error status to set
     */
    public final void setErrorStatus(final byte errorStatusToSet) {
        this.errorStatus = errorStatusToSet;
    }
    /**
     * Getter for Error number.
     * @return  Error number
     */
    public final int getErrorNo() {
        return errorNo;
    }
    /**
     * Setter for Error number.
     * @param errorNoToSet  Error number to set
     */
    public final void setErrorNo(final int errorNoToSet) {
        this.errorNo = errorNoToSet;
    }
}
