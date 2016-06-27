package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * ViewPosLinkInfo class. Holds POS Link Info.
 *
 * @author RD185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ViewPosLinkInfo")
@ApiModel(value="ViewPosLinkInfo")
public class ViewPosLinkInfo extends ResultBase {
    /**
     * The information of the POS Link.
     */
    @XmlElement(name = "POSLinkInfo")
    private POSLinkInfo poslinkinfo;

    public ViewPosLinkInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ViewPosLinkInfo(int resultCode, int extendedResultCode,
			Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
     * Returns the POS link information.
     *
     * @return POSLinkInfo model.
     */
	@ApiModelProperty(value="POSÉäÉìÉNèÓïÒ", notes="POSÉäÉìÉNèÓïÒ")
    public final POSLinkInfo getPosLinkInfo() {
        return poslinkinfo;
    }

    /**
     * sets the POS Link information.
     *
     * @param poslinkinfoToSet
     *            - New POS Link info
     */
    public final void setPosLinkInfo(final POSLinkInfo poslinkinfoToSet) {
        poslinkinfo = poslinkinfoToSet;
    }

    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
