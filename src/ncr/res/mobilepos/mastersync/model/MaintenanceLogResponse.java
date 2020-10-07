package ncr.res.mobilepos.mastersync.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "response")
@ApiModel(value = "MaintenanceLogResponse")
public class MaintenanceLogResponse extends ResultBase {
    /**
     * �������� ... �����e�i���X�f�[�^����
     */
    public static final int RESULT_MAINTENANCE_DATA_EXISTS = 1;

    /**
     * �������� ... �����e�i���X�f�[�^�Ȃ�
     */
    public static final int RESULT_MAINTENANCE_DATA_NOT_EXISTS = 0;

    @XmlElement(name = "result")
    private int result;

    @XmlElement(name = "dataCount")
    private int dataCount;

    @XmlElement(name = "maintenanceLog")
    private List<MaintenanceLog> maintenanceLogs;

    /**
     * MaintenanceLogResponse������������B
     */
    public MaintenanceLogResponse() {
        super();

        result = 0;
        dataCount = 0;
        maintenanceLogs = new ArrayList<MaintenanceLog>();
    }

    /**
     * �������ʂ��擾����B
     * @return ��������
     */
    @ApiModelProperty(value = "��������", notes = "��������")
    public int getResult() {
        return result;
    }

    /**
     * �������ʂ�ݒ肷��B
     * @param result ��������
     */
    public void setResult(final int result) {
        this.result = result;
    }

    /**
     * �ԑ��f�[�^�����擾����B
     * @return �ԑ��f�[�^��
     */
    @ApiModelProperty(value = "�ԑ��f�[�^��", notes = "�ԑ��f�[�^��")
    public int getDataCount() {
        return dataCount;
    }

    /**
     * �ԑ��f�[�^����ݒ肷��B
     * @param dataCount �ԑ��f�[�^��
     */
    public void setDataCount(final int dataCount) {
        this.dataCount = dataCount;
    }

    /**
     * �����e�i���X�f�[�^�̈ꗗ���擾����B
     * @return �����e�i���X�f�[�^
     */
    @ApiModelProperty(value = "�����e�i���X�f�[�^", notes = "�����e�i���X�f�[�^")
    public List<MaintenanceLog> getMaintenanceLogs() {
        return maintenanceLogs;
    }

    /**
     * �����e�i���X�f�[�^�̈ꗗ��ݒ肷��B
     * @param maintenanceLog �����e�i���X�f�[�^
     */
    public void setMaintenanceLogs(final List<MaintenanceLog> maintenanceLogs) {
        this.maintenanceLogs = maintenanceLogs == null ? new ArrayList<MaintenanceLog>() : maintenanceLogs;
    }

    /**
     * �����e�i���X�f�[�^�̈ꗗ�Ƀ����e�i���X�f�[�^��ǉ�����B
     * @param maintenanceLog �����e�i���X�f�[�^
     */
    public void addMaintenanceLog(final MaintenanceLog maintenanceLog) {
        this.maintenanceLogs.add(maintenanceLog);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(super.toString());
        builder.append("; result: ").append(getResult());
        builder.append("; dataCount: ").append(getDataCount());

        for (MaintenanceLog log: getMaintenanceLogs()) {
            builder.append("; maintenanceLog: ").append(log);
        }

        return builder.toString();
    }
}
