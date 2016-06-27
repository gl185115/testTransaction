package ncr.res.mobilepos.deviceinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The object that holds the list of devices with resultcode.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchedDevice")
@XmlSeeAlso({ DeviceInfo.class })
@ApiModel(value="SearchedDevice")
public class SearchedDevice extends ResultBase {

    public SearchedDevice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchedDevice(int resultCode, int extendedResultCode,
			Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
		// TODO Auto-generated constructor stub
	}

	/** The devices. */
    @XmlElementWrapper(name = "DeviceList")
    @XmlElementRef()
    private List<DeviceInfo> devices;

    /**
     * Sets the devices.
     *
     * @param deviceList the list of devices
     */
    public final void setDevices(final List<DeviceInfo> deviceList) {
        this.devices = deviceList;
    }

    /**
     * Gets the devices.
     *
     * @return the devices
     */
    @ApiModelProperty(value="í[ññèÓïÒ", notes="í[ññèÓïÒ")
    public final List<DeviceInfo> getDevices() {
        return devices;
    }

    @Override
    public final String toString() {
        int noOfDevices = 0;
        if (null != devices) {
            noOfDevices = devices.size();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Devices count: " + noOfDevices);
        return sb.toString();
    }

}
