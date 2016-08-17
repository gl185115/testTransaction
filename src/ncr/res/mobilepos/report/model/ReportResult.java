/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportResult
 *
 * Model for the result of the report generation
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.model.ResultBase;

/**
 * ReportResult Class is a Model representation of the report and other
 * information of the result of the report generation.
 */
@XmlRootElement
public class ReportResult extends ResultBase {

    /**
     * The HTML format string of the generated report.
     */
    private String report;

    /**
     * Gets the Report.
     *
     * @return Report
     */
    public final String getReport() {
        return report;
    }

    /**
     * Sets the Report.
     *
     * @param reportToSet
     *            the new report
     */
    public final void setReport(final String reportToSet) {
        this.report = reportToSet;
    }
}
