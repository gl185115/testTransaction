/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportParam
 *
 * Model for the details required in generating a report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ReportParam Class is a Model representation of the information needed to
 * create a report.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReportParam")
public class ReportParam {

    /** Identifies the Report type. */
    @XmlElement(name = "ReportType")
    private String reportType;

    /** Specific information on the Sales Report type. */
    @XmlElement(name = "SalesReportFields")
    private SalesReportFields salesReportFields;

    /**
     * Retrieves the Report Type.
     *
     * @return Report Type
     */
    public final String getReportType() {
        return reportType;
    }

    /**
     * Sets the Report Type.
     *
     * @param reportTypeToSet
     *            the new report type
     */
    public final void setReportType(final String reportTypeToSet) {
        this.reportType = reportTypeToSet;
    }

    /**
     * Retrieves the Sales Report Fields.
     *
     * @return Sales Report Fields
     */
    public final SalesReportFields getSalesReportFields() {
        return salesReportFields;
    }

    /**
     * Sets the Sales Report Fields.
     *
     * @param salesReportFieldsToSet
     *            the new sales report fields
     */
    public final void setSalesReportFields(
            final SalesReportFields salesReportFieldsToSet) {
        this.salesReportFields = salesReportFieldsToSet;
    }
}
