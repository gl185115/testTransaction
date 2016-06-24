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
 * POSLinks
 * a list of the POS Links saved in the database.
 * @author AP185142
 *
 */
@XmlRootElement(name = "POSLinks")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ POSLinkInfo.class })
@ApiModel(value="POSLinks")
public class POSLinks extends ResultBase {

    public POSLinks() {
		super();
		// TODO Auto-generated constructor stub
	}

	public POSLinks(int resultCode, int extendedResultCode, Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
     * the collection of POS Link information.
     */
    @XmlElementWrapper(name = "POSLinks")
    @XmlElementRef()
    private List<POSLinkInfo> posLinkInfos = null;

    /**
     * sets the poslink list.
     * @param poslinkinfos - the pos link information to set
     */
    public final void setPOSLinkInfos(final List<POSLinkInfo> poslinkinfos) {
        this.posLinkInfos = poslinkinfos;
    }

    /**
     * gets the pos link information.
     * @return the collection of pos link information
     */
    @ApiModelProperty(value="POSÉäÉìÉNèÓïÒ", notes="POSÉäÉìÉNèÓïÒ")
    public final List<POSLinkInfo> getPOSLinkInfos() {
        return this.posLinkInfos;
    }

    @Override
    public final String toString() {
        String ret = super.toString() + "; size:"
                + (this.posLinkInfos != null ? this.posLinkInfos.size() : 0);
        return ret;
    }
}
