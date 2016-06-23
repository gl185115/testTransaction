package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * ViewPrinterInfo Class.
 * holds the PrinterInfo
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ViewPrinterInfo")
@ApiModel(value="ViewPrinterInfo")
public class ViewPrinterInfo extends ResultBase {

    public ViewPrinterInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ViewPrinterInfo(int resultCode, int extendedResultCode,
			Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
		// TODO Auto-generated constructor stub
	}
	/**
     * the information on about a printer.
     */
    @XmlElement(name = "PrinterInfo")
    private PrinterInfo printerinfo;
    /**
     * returns the device info.
     * @return PrinterInfo model
     */
    @ApiModelProperty(value="プリント情報", notes="プリント情報")
    public final PrinterInfo getPrinterInfo() {
        return printerinfo;
    }
    /**
     * sets the printer info.
     * @param printerinfoToSet - new printer info
     */
    public final void setPrinterInfo(final PrinterInfo printerinfoToSet) {
        printerinfo = printerinfoToSet;
    }
}
