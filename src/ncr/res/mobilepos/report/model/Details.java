/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportParam
 *
 * Model for the details to be included in the Detail Report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Details Class is a Model representation of the details in a Detail type Sales
 * Report.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Details")
public class Details {

    /** Business date. */
    @XmlElement(name = "BusinessDate")
    private String businessDate;

    /** Store Number. */
    @XmlElement(name = "StoreNumber")
    private String storeNumber;

    /** Department. */
    @XmlElement(name = "Department")
    private String department;

    /** Time Zone. */
    @XmlElement(name = "TimeZone")
    private String timeZone;

    /** Operator Number. */
    @XmlElement(name = "DetailsOperatorNumber")
    private String detailsOperatorNumber;

    /** Device Number. */
    @XmlElement(name = "DetailsDeviceNumber")
    private String detailsDeviceNumber;

    /**
     * Retrieves the Business Date.
     *
     * @return Business Date
     */
    public final String getBusinessDate() {
        return businessDate;
    }

    /**
     * Sets the Business Date.
     *
     * @param businessDateToSet
     *            the new business date
     */
    public final void setBusinessDate(final String businessDateToSet) {
        this.businessDate = businessDateToSet;
    }

    /**
     * Retrieves the Store Number.
     *
     * @return Store Number
     */
    public final String getStoreNumber() {
        return storeNumber;
    }

    /**
     * Sets the Store Number.
     *
     * @param storeNumberToSet
     *            the new store number
     */
    public final void setStoreNumber(final String storeNumberToSet) {
        this.storeNumber = storeNumberToSet;
    }

    /**
     * Retrieves the Department.
     *
     * @return Department
     */
    public final String getDepartment() {
        return department;
    }

    /**
     * Sets the Department.
     *
     * @param departmentToSet
     *            the new department
     */
    public final void setDepartment(final String departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Retrieves the Time Zone.
     *
     * @return Time Zone
     */
    public final String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the Time Zone.
     *
     * @param timeZoneToSet
     *            the new time zone
     */
    public final void setTimeZone(final String timeZoneToSet) {
        this.timeZone = timeZoneToSet;
    }

    /**
     * Retrieves the Operator Number.
     *
     * @return Operator Number
     */
    public final String getDetailsOperatorNumber() {
        return detailsOperatorNumber;
    }

    /**
     * Sets the Operator Number.
     *
     * @param operatorNumber
     *            Operator Number to set
     */
    public final void setDetailsOperatorNumber(final String operatorNumber) {
        this.detailsOperatorNumber = operatorNumber;
    }

    /**
     * Retrieves the Device Number.
     *
     * @return Device Number
     */
    public final String getDetailsDeviceNumber() {
        return detailsDeviceNumber;
    }

    /**
     * Sets the Device Number.
     *
     * @param deviceNumber
     *            Device Number to set
     */
    public final void setDetailsDeviceNumber(final String deviceNumber) {
        this.detailsDeviceNumber = deviceNumber;
    }
}
