package ncr.res.mobilepos.report.model;


import ncr.res.mobilepos.model.ResultBase;
/**
 * 改定履歴
 * バージョン 　　　　　　　     改定日付  　　　     担当者名 　　　　　       改定内容
 * 1.01           2014.10.21    MAJINHUI        レポート出力を対応
 */
public class ReportModes extends ResultBase {
    // The report Mode
    ReportMode reportMode = null;

    /**
     * Gets the report Mode.
     *
     * @return the report Mode
     */
    public ReportMode getReportMode() {
        return reportMode;
    }

    /**
     * Sets the report Mode.
     *
     * @param reportMode
     *            the new report Mode
     */
    public void setReportMode(ReportMode reportMode) {
        this.reportMode = reportMode;
    }
}
