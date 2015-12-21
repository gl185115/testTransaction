package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * ViewDeviceInfo Class.
 * holds the DeviceInfo
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ViewDeviceInfo")
public class ViewDeviceInfo extends ResultBase {
	/**
	 * Default constructor.
	 */
    public ViewDeviceInfo() {
		super();
	}
    /**
     * Constructor.
     * @param resultCode	The resulting error code.
     * @param extendedResultCode	The extended error code.
     * @param throwable	The exception.
     */
	public ViewDeviceInfo(int resultCode, int extendedResultCode,
			Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
	/**
     * the information on about a device.
     */
    @XmlElement(name = "DeviceInfo")
    private DeviceInfo deviceinfo;
    /**
     * returns the device info.
     * @return DeviceInfo model
     */
    public final DeviceInfo getDeviceInfo() {
        return deviceinfo;
    }
    /**
     * sets the device info.
     * @param deviceinfoToSet - new device info
     */
    public final void setDeviceInfo(final DeviceInfo deviceinfoToSet) {
        deviceinfo = deviceinfoToSet;
    }
}
