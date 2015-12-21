package ncr.res.mobilepos.report.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DailyReportItems")
public class DailyReportItems extends ResultBase {

    @XmlElement(name = "ReportItems")
    private List<DailyReport> reportItems;

    /**
     * @return the reportItems
     */
    public List<DailyReport> getReportItems() {
        return reportItems;
    }

    /**
     * @param reportItems the reportItems to set
     */
    public void setReportItems(List<DailyReport> reportItems) {
        this.reportItems = reportItems;
    }

    @Override
    public String toString() {
        return "DailyReportItems [reportItems=" + reportItems + "]";
    }
    
    
}
