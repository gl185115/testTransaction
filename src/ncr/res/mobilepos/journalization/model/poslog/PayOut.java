package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PayOut Model Object.
 *
 * <P>A PayOut Node in POSLog XML.
 *
 * <P>The PayOut node is under TenderControlTransaction Node.
 * And holds the PayOut transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PayOut")
public class PayOut extends PayIn {

}