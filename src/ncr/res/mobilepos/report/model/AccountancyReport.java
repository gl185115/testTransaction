package ncr.res.mobilepos.report.model;

/**
 * ���藚��
 * �o�[�W����      ������t      �S���Җ�        ������e
 * 1.01            2014.10.21    FENGSHA         ���|�[�g�o�͂�Ή�
 * 1.02            2014.12.08    FENGSHA         ���|�[�g�o�͂�Ή�
 * 1.03            2014.12.30    MAJINHUI        ��v���|�[�g�o�͂�Ή�
 */
import ncr.res.mobilepos.report.constants.ReportConstants;

public class AccountancyReport extends Report {

    // 1.02 2014.12.08 FENGSHA ���|�[�g�o�͂�Ή� MOD start
    /**
     * Initializes AccountancyReport.
     */
    public AccountancyReport() {
        /* Add Columns specific to Accountancy Sales Report */
        this.addColumn(ReportConstants.COLITEMNAME);
        this.addColumn(ReportConstants.COLACCOUNT);
        this.addColumn(ReportConstants.COLAMOUNT);
        this.addColumn(ReportConstants.SALESORPAY);
        //1.03  2014.12.30   MAJINHUI  ��v���|�[�g�o�͂�Ή��@ADD START
        this.addColumn(ReportConstants.COLITEM);
        //1.03  2014.12.30   MAJINHUI  ��v���|�[�g�o�͂�Ή��@ADD END
    }

    // 1.02 2014.12.08 FENGSHA ���|�[�g�o�͂�Ή� MOD end
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
    //1.03  2014.12.30   MAJINHUI  ��v���|�[�g�o�͂�Ή��@ADD START
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
    //1.03  2014.12.30   MAJINHUI  ��v���|�[�g�o�͂�Ή��@ADD END

}
