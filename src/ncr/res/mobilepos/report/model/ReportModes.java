package ncr.res.mobilepos.report.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;
/**
 * ���藚��
 * �o�[�W���� �@�@�@�@�@�@�@     ������t  �@�@�@     �S���Җ� �@�@�@�@�@       ������e
 * 1.01           2014.10.21    MAJINHUI        ���|�[�g�o�͂�Ή�
 */
@ApiModel(value="ReportModes")
public class ReportModes extends ResultBase {
    // The report Mode
    ReportMode reportMode = null;

    /**
     * Gets the report Mode.
     *
     * @return the report Mode
     */
    @ApiModelProperty(value="���|�[�g���f��", notes="���|�[�g���f��")
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
