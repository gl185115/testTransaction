package ncr.res.mobilepos.report.model;

/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01   2014.11.19  LiQian   レポート出力を対応
 */
import ncr.res.mobilepos.report.constants.ReportConstants;

public class StoreReport extends Report {

    /**
     * Initializes StoreReport.
     */
    public StoreReport() {
        /* Add Columns specific to Store Sales Report */
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);
    }

    /**
     * Adds data to StoreReport number.
     *
     * @param data
     *            StoreReport number data to add
     * @return true if success
     */
    public final boolean addDataToStoreId(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
        return true;
    }

    /**
     * Adds data to StoreReport name.
     *
     * @param data
     *            StoreReport name data to add
     * @return true if success
     */
    public final boolean addDataToStoreName(final String data) {
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
     * Adds data to total sales Amount.
     *
     * @param data
     *            Total sales Amount data to add
     * @return true if success
     */
    public final boolean addDataToAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }

}
