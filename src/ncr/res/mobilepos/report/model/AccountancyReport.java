package ncr.res.mobilepos.report.model;

/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01            2014.10.21    FENGSHA         レポート出力を対応
 * 1.02            2014.12.08    FENGSHA         レポート出力を対応
 * 1.03            2014.12.30    MAJINHUI        会計レポート出力を対応
 */
import ncr.res.mobilepos.report.constants.ReportConstants;

public class AccountancyReport extends Report {

    // 1.02 2014.12.08 FENGSHA レポート出力を対応 MOD start
    /**
     * Initializes AccountancyReport.
     */
    public AccountancyReport() {
        /* Add Columns specific to Accountancy Sales Report */
        this.addColumn(ReportConstants.COLITEMNAME);
        this.addColumn(ReportConstants.COLACCOUNT);
        this.addColumn(ReportConstants.COLAMOUNT);
        this.addColumn(ReportConstants.SALESORPAY);
        //1.03  2014.12.30   MAJINHUI  会計レポート出力を対応　ADD START
        this.addColumn(ReportConstants.COLITEM);
        //1.03  2014.12.30   MAJINHUI  会計レポート出力を対応　ADD END
    }

    // 1.02 2014.12.08 FENGSHA レポート出力を対応 MOD end
    /**
     * Adds data to Accountancy number.
     *
     * @param data
     *            Accountancy number data to add
     * @return true if success
     */
    public final boolean addDataToATYItemName(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
        return true;
    }

    /**
     * Adds data to Accountancy name.
     *
     * @param data
     *            Accountancy name data to add
     * @return true if success
     */
    public final boolean addDataToATYSaleAccount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
        return true;
    }

    /**
     * Adds data to number of AccountancyAmount.
     *
     * @param data
     *            Number of Accountancysales data to add
     * @return true if success
     */
    public final boolean addDataToATYSaleAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM2, data);
        return true;
    }

    /**
     * Adds data to SalesOrPay.
     *
     * @param data
     *            QU fen id data to add
     * @return true if success
     */
    public final boolean addDateToSalesOrPay(final String data) {
        this.addDataToColumn(ReportConstants.PARAM3, data);
        return true;
    }
    //1.03  2014.12.30   MAJINHUI  会計レポート出力を対応　ADD START
    /**
     * Adds data to ATYItem
     *
     * @param data
     *            QU fen id data to add
     * @return true if success
     */
    public final boolean addDateToATYItem(final String data) {
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }
    //1.03  2014.12.30   MAJINHUI  会計レポート出力を対応　ADD END

}
