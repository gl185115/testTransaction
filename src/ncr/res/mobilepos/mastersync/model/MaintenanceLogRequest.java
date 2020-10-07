package ncr.res.mobilepos.mastersync.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.helper.StringUtility;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "request")
@ApiModel(value = "MaintenanceLogRequest")
public class MaintenanceLogRequest {
    @XmlElement(name = "companyId")
    private String companyId;

    @XmlElement(name = "storeId")
    private String storeId;

    @XmlElement(name = "bizCatId")
    private String bizCatId;

    @XmlElement(name = "maintenanceId")
    private long maintenanceId;

    @XmlElement(name = "syncRecordCount")
    private int syncRecordCount;

    @XmlElement(name = "dataFiles")
    private List<DataFile> dataFiles;

    /**
     * MaintenanceLogRequest������������B
     */
    public MaintenanceLogRequest() {
        companyId = "";
        storeId = "";
        bizCatId = "";
        maintenanceId = -1;
        syncRecordCount = 0;
        dataFiles = new ArrayList<DataFile>();
    }

    /**
     * ��ЃR�[�h���擾����B
     * @return ��ЃR�[�h
     */
    @ApiModelProperty(value = "��ЃR�[�h", notes = "��ЃR�[�h")
    public String getCompanyId() {
        return companyId;
    }

    /**
     * ��ЃR�[�h��ݒ肷��B
     * @param companyId ��ЃR�[�h
     */
    public void setCompanyId(final String companyId) {
        this.companyId = companyId == null ? "" : companyId;
    }

    /**
     * �X�܃R�[�h���擾����B
     * @return �X�܃R�[�h
     */
    @ApiModelProperty(value = "�X�܃R�[�h", notes = "�X�܃R�[�h")
    public String getStoreId() {
        return storeId;
    }

    /**
     * �X�܃R�[�h��ݒ肷��B
     * @param storeId �X�܃R�[�h
     */
    public void setStoreId(final String storeId) {
        this.storeId = storeId == null ? "" : storeId;
    }

    /**
     * �ƑԃR�[�h���擾����B
     * @return �ƑԃR�[�h
     */
    @ApiModelProperty(value = "�ƑԃR�[�h", notes = "�ƑԃR�[�h")
    public String getBizCatId() {
        return bizCatId;
    }

    /**
     * �ƑԃR�[�h��ݒ肷��B
     * @param bizCatId �ƑԃR�[�h
     */
    public void setBizCatId(final String bizCatId) {
        this.bizCatId = bizCatId == null ? "" : bizCatId;
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
     * �������R�[�h�������擾����B
     * @return �������R�[�h����
     */
    @ApiModelProperty(value = "�������R�[�h����", notes = "�������R�[�h����")
    public int getSyncRecordCount() {
        return syncRecordCount;
    }

    /**
     * �������R�[�h������ݒ肷��B
     * @param syncRecordCount �������R�[�h����
     */
    public void setSyncRecordCount(final int syncRecordCount) {
        this.syncRecordCount = syncRecordCount;
    }

    /**
     * �z�M�t�@�C�������擾����B
     * @return �z�M�t�@�C�����
     */
    @ApiModelProperty(value = "�z�M�t�@�C�����", notes = "�z�M�t�@�C�����")
    public List<DataFile> getDataFiles() {
        return dataFiles;
    }

    /**
     * �z�M�t�@�C������ݒ肷��B
     * @param dataFiles �z�M�t�@�C�����
     */
    public void setDataFiles(final List<DataFile> dataFiles) {
        this.dataFiles = dataFiles == null ? new ArrayList<DataFile>() : dataFiles;
    }

    /**
     * ���N�G�X�g�̓��e���L�����ǂ������f����B
     * @return
     *      ���N�G�X�g�̓��e���L���ȏꍇ��true��Ԃ��B
     *      ���N�G�X�g�̓��e�������ȏꍇ��false��Ԃ��B
     */
    public boolean isValid() {
        // ��ЃR�[�h�A�X�܃R�[�h�A�ƑԃR�[�h�����ݒ�̏ꍇ�͖���
        if (StringUtility.isNullOrEmpty(getCompanyId(), getStoreId(), getBizCatId())) {
            return false;
        }

        // �����e�i���XID���}�C�i�X�l�̏ꍇ�͖���
        if (getMaintenanceId() < 0) {
            return false;
        }

        // �������R�[�h������0�ȉ��̏ꍇ�͖���
        if (getSyncRecordCount() <= 0) {
            return false;
        }

        // �z�M�t�@�C����񂪖��ݒ�̏ꍇ�͖���
        if (getDataFiles() == null || getDataFiles().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("companyId: ").append(getCompanyId());
        builder.append("; storeId: ").append(getStoreId());
        builder.append("; bizCatId: ").append(getBizCatId());
        builder.append("; maintenanceId: ").append(getMaintenanceId());
        builder.append("; syncRecordCount: ").append(getSyncRecordCount());

        for (DataFile dataFile : getDataFiles()) {
            builder.append("; dataFile: ").append(dataFile);
        }

        return builder.toString();
    }
}
