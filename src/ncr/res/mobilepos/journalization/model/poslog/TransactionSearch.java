package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TransactionSearch")
public class TransactionSearch {
	
    @XmlElement(name = "POSLog")
    private PosLog posLog;
	
    @XmlElement(name = "AdditionalInformation")
    private AdditionalInformation additionalInformation;

	public final PosLog getPosLog() {
		return posLog;
	}

	public final void setPosLog(PosLog posLog) {
		this.posLog = posLog;
	}

	public final AdditionalInformation getAdditionalInformation() {
		return additionalInformation;
	}

	public final void setAdditionalInformation(
			AdditionalInformation additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}
