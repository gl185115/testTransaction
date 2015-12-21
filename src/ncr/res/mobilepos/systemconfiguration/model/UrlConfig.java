/**
 * Copyright(c) 2015 NCR Japan Ltd.
 */
package ncr.res.mobilepos.systemconfiguration.model;

import java.lang.reflect.Field;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Special URL configuration holder.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Url")
public class UrlConfig {
    /**
     * create an empty instance.
     */
    public UrlConfig() {
    }

    @XmlElement(name = "EnterpriseServerUri")
    private String EnterpriseServerUri;
    @XmlElement(name = "Report")
    private String report;
    @XmlElement(name = "TransactionQueue")
    private String transactionQueue;
    @XmlElement(name = "Layaway")
    private String layaway;
    @XmlElement(name = "GiftInterface")
    private String giftInterface; 
    //add Training Mode
    @XmlElement(name = "TransactionSearch_Training")
    private String transactionSearch_Training;
    @XmlElement(name = "Report_Training")
    private String report_Training;
    @XmlElement(name = "TransactionQueue_Training")
    private String transactionQueue_Training;
    @XmlElement(name = "Layaway_Training")
    private String layaway_Training;
    @XmlElement(name = "GiftInterface_Training")
    private String giftInterface_Training;
    @XmlElement(name = "CatalogImage")
    private String catalogImage;
    @XmlElement(name = "CatalogImage_Training")
    private String catalogImage_Training;
    
	/**
	 * @return the transactionSearch_Training
	 */
	public String getTransactionSearch_Training() {
		return transactionSearch_Training;
	}
	/**
	 * @param transactionSearch_Training the transactionSearch_Training to set
	 */
	public void setTransactionSearch_Training(String transactionSearch_Training) {
		this.transactionSearch_Training = transactionSearch_Training;
	}
	/**
	 * @return the report_Training
	 */
	public String getReport_Training() {
		return report_Training;
	}
	/**
	 * @param report_Training the report_Training to set
	 */
	public void setReport_Training(String report_Training) {
		this.report_Training = report_Training;
	}
	/**
	 * @return the transactionQueue_Training
	 */
	public String getTransactionQueue_Training() {
		return transactionQueue_Training;
	}
	/**
	 * @param transactionQueue_Training the transactionQueue_Training to set
	 */
	public void setTransactionQueue_Training(String transactionQueue_Training) {
		this.transactionQueue_Training = transactionQueue_Training;
	}
	/**
	 * @return the layaway_Training
	 */
	public String getLayaway_Training() {
		return layaway_Training;
	}
	/**
	 * @param layaway_Training the layaway_Training to set
	 */
	public void setLayaway_Training(String layaway_Training) {
		this.layaway_Training = layaway_Training;
	}
	/**
	 * @return the giftInterface_Training
	 */
	public String getGiftInterface_Training() {
		return giftInterface_Training;
	}
	/**
	 * @param giftInterface_Training the giftInterface_Training to set
	 */
	public void setGiftInterface_Training(String giftInterface_Training) {
		this.giftInterface_Training = giftInterface_Training;
	}
	//add Training Mode End
	
	/** set EnterpriseServerUri server URL (root only) */
    public void setEnterpriseServerUri(String s) {
        EnterpriseServerUri = s;
    }
    /** get EnterpriseServerUri server URL (root only) */
    public String getEnterpriseServerUri() {
        return EnterpriseServerUri;
    }
    /** set Report server URL (root only) */
    public void setReport(String s) {
        report = s;
    }
    /** get Report server URL (root only) */
    public String getReport() {
        return report;
    }
    /** set TransactionQueue server URL (root only) */
    public void setTransactionQueue(String s) {
        transactionQueue = s;
    }
    /** get TransactionQueue server URL (root only) */
    public String getTransactionQueue() {
        return transactionQueue;
    }
    /** set Layaway server URL (root only) */
    public void setLayaway(String s) {
        layaway = s;
    }
    /** get Layaway server URL (root only) */
    public String getLayaway() {
        return layaway;
    }
    /** set GiftInterface server URL (root only) */
    public void setGiftInterface(String s) {
        giftInterface = s;
    }
    /** get GiftInterface server URL (root only) */
    public String getGiftInterface() {
        return giftInterface;
    }

    
    /**
	 * @return the catalogImage
	 */
	public final String getCatalogImage() {
		return catalogImage;
	}
	/**
	 * @param catalogImage the catalogImage to set
	 */
	public final void setCatalogImage(String catalogImage) {
		this.catalogImage = catalogImage;
	}
	/**
	 * @return the catalogImage_Training
	 */
	public final String getCatalogImage_Training() {
		return catalogImage_Training;
	}
	/**
	 * @param catalogImage_Training the catalogImage_Training to set
	 */
	public final void setCatalogImage_Training(String catalogImage_Training) {
		this.catalogImage_Training = catalogImage_Training;
	}
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            for (Field f: UrlConfig.class.getDeclaredFields()) {
                sb.append(f.getName())
                  .append('=')
                  .append(f.get(this))
                  .append(',');
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }
}

