package ncr.res.mobilepos.mastersync.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "maintenanceLog")
@ApiModel(value = "MaintenanceLog")
public class MaintenanceLog {
    @XmlElement(name = "tableName")
    private String tableName;

    @XmlElement(name = "maintenanceId")
    private long maintenanceId;

    @XmlElement(name = "maintenanceLogId")
    private String maintenanceLogId;

    @XmlElement(name = "maintenanceType")
    private int maintenanceType;

    @XmlElement(name = "syncGroupId")
    private int syncGroupId;

    @XmlElement(name = "referenceCondition")
    private String referenceCondition;

    @XmlElement(name = "masterUpdDate")
    private String masterUpdDate;

    @XmlElement(name = "masterUpdAppId")
    private String masterUpdAppId;

    @XmlElement(name = "masterUpdOpeCode")
    private String masterUpdOpeCode;

    @XmlElement(name = "targetStoreType")
    private String targetStoreType;

    @XmlElement(name = "startDate")
    private String startDate;

    @XmlElement(name = "endDate")
    private String endDate;

    @XmlElement(name = "startTime")
    private String startTime;

    @XmlElement(name = "endTime")
    private String endTime;

    @XmlElement(name = "store")
    private MaintenanceLogStore store;

    @XmlElement(name = "masterTables")
    private List<MasterTable> masterTables;

    /**
     * MaintenanceData������������B
     */
    public MaintenanceLog() {
        tableName = "";
        maintenanceId = 0;
        maintenanceLogId = "";
        maintenanceType = 0;
        syncGroupId = 0;
        referenceCondition = "";
        masterUpdDate = "";
        masterUpdAppId = "";
        masterUpdOpeCode = "";
        targetStoreType = "";
        startDate = "";
        endDate = "";
        startTime = "";
        endTime = "";
        store = new MaintenanceLogStore();
        masterTables = new LinkedList<MasterTable>();
    }

    /**
     * �����e�i���X���O�e�[�u�������擾����B
     * @return �����e�i���X���O�e�[�u����
     */
    @ApiModelProperty(value = "�����e�i���X���O�e�[�u����", notes = "�����e�i���X���O�e�[�u����")
    public String getTableName() {
        return tableName;
    }

    /**
     * �����e�i���X���O�e�[�u������ݒ肷��B
     * @param tableName �����e�i���X���O�e�[�u����
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName == null ? "" : tableName;
    }

    /**
     * �����e�i���XID���擾����B
     * @return �����e�i���XID
     */
    @ApiModelProperty(value = "�����e�i���XID", notes = "�����e�i���XID")
    public long getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * �����e�i���XID��ݒ肷��B
     * @param maintenanceId �����e�i���XID
     */
    public void setMaintenanceId(final long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    /**
     * �����e�i���X���OID���擾����B
     * @return �����e�i���X���OID
     */
    @ApiModelProperty(value = "�����e�i���X���OID", notes = "�����e�i���X���OID")
    public String getMaintenanceLogId() {
        return maintenanceLogId;
    }

    /**
     * �����e�i���X���OID��ݒ肷��B
     * @param maintenanceLogId �����e�i���X���OID
     */
    public void setMaintenanceLogId(final String maintenanceLogId) {
        this.maintenanceLogId = maintenanceLogId == null ? "" : maintenanceLogId;
    }

    /**
     * �����e�i���X�敪���擾����B
     * @return �����e�i���X�敪
     */
    @ApiModelProperty(value = "�����e�i���X�敪", notes = "�����e�i���X�敪")
    public int getMaintenanceType() {
        return maintenanceType;
    }

    /**
     * �����e�i���X�敪��ݒ肷��B
     * @param maintenanceType �����e�i���X�敪
     */
    public void setMaintenanceType(final int maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    /**
     * �}�X�^�A�g�O���[�vID���擾����B
     * @return �}�X�^�A�g�O���[�vID
     */
    @ApiModelProperty(value = "�}�X�^�A�g�O���[�vID", notes = "�}�X�^�A�g�O���[�vID")
    public int getSyncGroupId() {
        return syncGroupId;
    }

    /**
     * �}�X�^�A�g�O���[�vID��ݒ肷��B
     * @param syncGroupId �}�X�^�A�g�O���[�vID
     */
    public void setSyncGroupId(final int syncGroupId) {
        this.syncGroupId = syncGroupId;
    }

    /**
     * �}�X�^�Q�Ə������擾����B
     * @return �}�X�^�Q�Ə���
     */
    @ApiModelProperty(value = "�}�X�^�Q�Ə���", notes = "�}�X�^�Q�Ə���")
    public String getReferenceCondition() {
        return referenceCondition;
    }

    /**
     * �}�X�^�Q�Ə�����ݒ肷��B
     * @param referenceCondition �}�X�^�Q�Ə���
     */
    public void setReferenceCondition(final String referenceCondition) {
        this.referenceCondition = referenceCondition == null ? "" : referenceCondition;
    }

    /**
     * �}�X�^�ŏI�X�V�������擾����B
     * @return �}�X�^�ŏI�X�V����
     */
    @ApiModelProperty(value = "�}�X�^�ŏI�X�V����", notes = "�}�X�^�ŏI�X�V����")
    public String getMasterUpdDate() {
        return masterUpdDate;
    }

    /**
     * �}�X�^�ŏI�X�V������ݒ肷��B
     * @param masterUpdDate �}�X�^�ŏI�X�V����
     */
    public void setMasterUpdDate(final String masterUpdDate) {
        this.masterUpdDate = masterUpdDate == null ? "" : masterUpdDate;
    }

    /**
     * �}�X�^�ŏI�X�V�v���O����ID���擾����B
     * @return �}�X�^�ŏI�X�V�v���O����ID
     */
    @ApiModelProperty(value = "�}�X�^�ŏI�X�V�v���O����ID", notes = "�}�X�^�ŏI�X�V�v���O����ID")
    public String getMasterUpdAppId() {
        return masterUpdAppId;
    }

    /**
     * �}�X�^�ŏI�X�V�v���O����ID��ݒ肷��B
     * @param masterUpdAppId �}�X�^�ŏI�X�V�v���O����ID
     */
    public void setMasterUpdAppId(final String masterUpdAppId) {
        this.masterUpdAppId = masterUpdAppId == null ? "" : masterUpdAppId;
    }

    /**
     * �}�X�^�ŏI�X�V���[�UID���擾����B
     * @return �}�X�^�ŏI�X�V���[�UID
     */
    @ApiModelProperty(value = "�}�X�^�ŏI�X�V���[�UID", notes = "�}�X�^�ŏI�X�V���[�UID")
    public String getMasterUpdOpeCode() {
        return masterUpdOpeCode;
    }

    /**
     * �}�X�^�ŏI�X�V���[�UID��ݒ肷��B
     * @param masterUpdOpeCode �}�X�^�ŏI�X�V���[�UID
     */
    public void setMasterUpdOpeCode(final String masterUpdOpeCode) {
        this.masterUpdOpeCode = masterUpdOpeCode == null ? "" : masterUpdOpeCode;
    }

    /**
     * �ΏۓX�ܐݒ�敪���擾����B
     * @return �ΏۓX�ܐݒ�敪
     */
    @ApiModelProperty(value = "�ΏۓX�ܐݒ�敪", notes = "�ΏۓX�ܐݒ�敪")
    public String getTargetStoreType() {
        return targetStoreType;
    }

    /**
     * �ΏۓX�ܐݒ�敪��ݒ肷��B
     * @param targetStoreType �ΏۓX�ܐݒ�敪
     */
    public void setTargetStoreType(final String targetStoreType) {
        this.targetStoreType = targetStoreType;
    }

    /**
     * ����FROM���擾����B
     * @return ����FROM
     */
    @ApiModelProperty(value = "����FROM", notes = "����FROM")
    public String getStartDate() {
        return startDate;
    }

    /**
     * ����FROM��ݒ肷��B
     * @param startDate ����FROM
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * ����TO���擾����B
     * @return ����TO
     */
    @ApiModelProperty(value = "����TO", notes = "����TO")
    public String getEndDate() {
        return endDate;
    }

    /**
     * ����TO��ݒ肷��B
     * @param endDate ����TO
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * �J�n�������擾����B
     * @return �J�n����
     */
    @ApiModelProperty(value = "�J�n����", notes = "�J�n����")
    public String getStartTime() {
        return startTime;
    }

    /**
     * �J�n������ݒ肷��B
     * @param startTime �J�n����
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * �I���������擾����B
     * @return �I������
     */
    @ApiModelProperty(value = "�I������", notes = "�I������")
    public String getEndTime() {
        return endTime;
    }

    /**
     * �I��������ݒ肷��B
     * @param endTime �I������
     */
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    /**
     * �����e�i���X���O�X�܏����擾����B
     * @return �����e�i���X���O�X�܏��
     */
    @ApiModelProperty(value = "�����e�i���X���O�X�܏��", notes = "�����e�i���X���O�X�܏��")
    public MaintenanceLogStore getStore() {
        return store;
    }

    /**
     * �����e�i���X���O�X�܏���ݒ肷��B
     * @param store �����e�i���X���O�X�܏��
     */
    public void setStore(final MaintenanceLogStore store) {
        this.store = store == null ? new MaintenanceLogStore() : store;
    }

    /**
     * �}�X�^�e�[�u���ꗗ���擾����B
     * @return �}�X�^�e�[�u���ꗗ
     */
    @ApiModelProperty(value = "�}�X�^�e�[�u���ꗗ", notes = "�}�X�^�e�[�u���ꗗ")
    public List<MasterTable> getMasterTables() {
        return masterTables;
    }

    /**
     * �}�X�^�e�[�u���ꗗ��ݒ肷��B
     * @param masterTables �}�X�^�e�[�u���ꗗ
     */
    public void setMasterTables(final List<MasterTable> masterTables) {
        this.masterTables = masterTables == null ? new LinkedList<MasterTable>() : masterTables;
    }

    /**
     * �}�X�^�e�[�u�����ꗗ�ɒǉ�����B
     * @param masterTable �}�X�^�e�[�u��
     */
    public void addMasterTable(final MasterTable masterTable) {
        this.masterTables.add(masterTable);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("tableName: ").append(getTableName());
        builder.append("; maintenanceId: ").append(getMaintenanceId());
        builder.append("; maintenanceLogId: ").append(getMaintenanceLogId());
        builder.append("; maintenanceType: ").append(getMaintenanceType());
        builder.append("; syncGroupId: ").append(getSyncGroupId());
        builder.append("; referenceCondition: ").append(getReferenceCondition());
        builder.append("; masterUpdDate: ").append(getMasterUpdDate());
        builder.append("; masterUpdAppId: ").append(getMasterUpdAppId());
        builder.append("; masterUpdOpeCode: ").append(getMasterUpdOpeCode());
        builder.append("; targetStoreType: ").append(getTargetStoreType());
        builder.append("; startDate: ").append(getStartDate());
        builder.append("; endDate: ").append(getEndDate());
        builder.append("; startTime: ").append(getStartTime());
        builder.append("; endTime: ").append(getEndTime());
        builder.append("; store: ").append(getStore());

        for (MasterTable masterTable : getMasterTables()) {
            builder.append("; masterTable: ").append(masterTable);
        }

        return builder.toString();
    }
}
