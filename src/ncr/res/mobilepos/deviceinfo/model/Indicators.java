package ncr.res.mobilepos.deviceinfo.model;

import ncr.res.mobilepos.model.ResultBase;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Indicators")
public class Indicators extends ResultBase{
	
    @XmlElement(name = "Indicators")
    private List<IndicatorInfo> indicators;
    
    /**
	 * @return the Indicators
	 */
	public List<IndicatorInfo> getIndicators() {
        return indicators;
	}

	/**
	 * @param Indicators the Indicators to set
	 */
	public final void setIndicatorsList(final List<IndicatorInfo> indicators) {
		this.indicators = indicators;
	}

	public final String toString() {
		StringBuilder ret = new StringBuilder();
		String crlf = "\r\n";
		if (null != this.indicators) {
			for (IndicatorInfo IndicatorInfo : indicators) {
				ret.append(crlf).append("Indicators : ").append(IndicatorInfo.toString());
			}
		}
		return ret.toString();
	}
}