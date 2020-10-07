package ncr.res.mobilepos.mastersync.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "dataFiles")
@ApiModel(value = "dataFiles")
public class DataFile {
    @XmlElement(name = "type")
    private int type;

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "maintenanceId")
    private long maintenanceId;

    /**
     * DataFile������������B
     */
    public DataFile() {
        type = 0;
        version = "";
        maintenanceId = 0;
    }

    /**
     * �z�M�t�@�C���^�C�v���擾����B
     * @return �z�M�t�@�C���^�C�v
     */
    @ApiModelProperty(value = "�z�M�t�@�C���^�C�v", notes = "�z�M�t�@�C���^�C�v")
    public int getType() {
        return type;
    }

    /**
     * �z�M�t�@�C���^�C�v��ݒ肷��B
     * @param type �z�M�t�@�C���^�C�v
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * �z�M�t�@�C���o�[�W�������擾����B
     * @return �z�M�t�@�C���o�[�W����
     */
    @ApiModelProperty(value = "�z�M�t�@�C���o�[�W����", notes = "�z�M�t�@�C���o�[�W����")
    public String getVersion() {
        return version;
    }

    /**
     * �z�M�t�@�C���o�[�W������ݒ肷��B
     * @param version �z�M�t�@�C���o�[�W����
     */
    public void setVersion(final String version) {
        this.version = version == null ? "" : version;
    }

    /**
     * �z�M�t�@�C���쐬�������e�i���XID���擾����B
     * @return �z�M�t�@�C���쐬�������e�i���XID
     */
    @ApiModelProperty(value = "�z�M�t�@�C���쐬�������e�i���XID", notes = "�z�M�t�@�C���쐬�������e�i���XID")
    public long getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * �z�M�t�@�C���쐬�������e�i���XID��ݒ肷��B
     * @param maintenanceId �z�M�t�@�C���쐬�������e�i���XID
     */
    public void setMaintenanceId(final long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DataFile)) {
            return false;
        }

        DataFile dataFile = (DataFile) object;
        // �z�M�t�@�C���^�C�v�Ɣz�M�t�@�C���o�[�W��������v���Ă���Γ��l�Ƃ݂Ȃ�
        return this.getType() == dataFile.getType() && this.getVersion().compareToIgnoreCase(dataFile.getVersion()) == 0;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getType();
        result = 31 * result + this.getVersion().hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("type: ").append(getType());
        builder.append("; version: ").append(getVersion());
        builder.append("; maintenanceId: ").append(getMaintenanceId());

        return builder.toString();
    }
}
