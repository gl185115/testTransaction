package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Printers
 * Model for representing list of printer information.
 */
// please note all members are small letters
@XmlRootElement(name = "printers")
@ApiModel(value="Printers")
public class Printers extends ResultBase {
	/**
	 * Default constructor.
	 */
	public Printers() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param resultCode
	 *            The resulting error code.
	 * @param extendedResultCode
	 *            The extended error code.
	 * @param throwable
	 *            The exception.
	 */
	public Printers(final int resultCode, final int extendedResultCode,
			final Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
    /**
     * The list of printers is set here.
     */
    private PrinterInfo[] printers = null;
    /**
     * Setter for the printer list.
     * @param printersToSet the printer list
     */
    public final void setPrinters(final PrinterInfo[] printersToSet) {
        this.printers = (PrinterInfo[]) printersToSet.clone();
    }
    /**
     * Getter for the printer list.
     * @return PrinterInfo[] array of PrinterInfo
     */
    @ApiModelProperty(value="ˆóüî•ñ‚ğ“¾‚é", notes="ˆóüî•ñ‚ğ“¾‚é")
    public final PrinterInfo[] getPrinters() {
        if (printers == null) {
            return new PrinterInfo[0];
        }
        return (PrinterInfo[]) printers.clone();
    }
    /**
     * Convert to string.
     * @return String
     */
    public final String toString() {
        String ret = super.toString() + "; size:";

        if (this.printers != null) {
            ret += this.printers.length;
        } else {
            ret += 0;
        }
        return ret;
    }
}
