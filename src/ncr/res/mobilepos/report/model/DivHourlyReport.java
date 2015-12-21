package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;

public class DivHourlyReport extends Report{
     /**
     * Construct for Department Hourly  report.
     *
     * @param deviceNo
     *            device identifier of the device requesting the Department hourly report
     * @param operatorNo
     *            operator identifier of the operator requesting the Department hourly
     *            report
     */
    public DivHourlyReport() {
        addColumn(ReportConstants.COLHEADER_NUMBER);
        addColumn(ReportConstants.COLHEADER_NAME);
        addColumn(ReportConstants.COLHEADER_TIMEZONE);
        addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        addColumn(ReportConstants.COLHEADER_ITEMS);
        addColumn(ReportConstants.COLHEADER_AMOUNT);
    }
    /**
     * Adds data to Department number.
     *
     * @param data
     *            Department number data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURNum(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
        return true;
    }

    /**
     * Adds data to Department name.
     *
     * @param data
     *            Department name data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURName(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
        return true;
    }
    /**
     * Adds data to number of Time Zone.
     *
     * @param data
     *            Number of Customers data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURTime(final String data) {
        this.addDataToColumn(ReportConstants.PARAM2, data);
        return true;
    }
    /**
     * Adds data to number of Customers.
     *
     * @param data
     *            Number of Customers data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURCustomers(final String data) {
        this.addDataToColumn(ReportConstants.PARAM3, data);
        return true;
    }

    /**
     * Adds data to number of purchased Items.
     *
     * @param data
     *            Number of purchased Items data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURItems(final String data) {
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }

    /**
     * Adds data to total sales Amount.
     *
     * @param data
     *            Total sales Amount data to add
     * @return true if success
     */
    public final boolean addDataToDPTHOURAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM5, data);
        return true;
    }

}
