
package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;
public class GroupReport extends Report {

    /**
     * Initializes GroupReport
     */
    public GroupReport() {
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);
    }

    /**
     * Adds data to Group number.
     *
     * @param data
     *            group number data to add
     * @return true if success
     */
    public final boolean addDataToGroupNum(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
        return true;
    }

    /**
     * Add value to name.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataGroupName(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
        return true;
    }

    /**
     * Add value to sales customers.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToCustomers(final String data) {
        this.addDataToColumn(ReportConstants.PARAM2, data);
        return true;
    }

    /**
     * Add value to sales item.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToItems(final String data) {
        this.addDataToColumn(ReportConstants.PARAM3, data);
        return true;
    }

    /**
     * Add value to sales amount.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }
}
