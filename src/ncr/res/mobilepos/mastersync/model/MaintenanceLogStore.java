package ncr.res.mobilepos.mastersync.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "maintenanceLogStore")
@ApiModel(value = "MaintenanceLogStore")
public class MaintenanceLogStore {
    @XmlElement(name = "tableName")
    private String tableName;

    @XmlElement(name = "companyId")
    private String companyId;

    @XmlElement(name = "storeId")
    private String storeId;

    @XmlElement(name = "bizCatId")
    private String bizCatId;

    /**
     * MaintenanceLogStore������������B
     */
    public MaintenanceLogStore() {
        tableName = "";
        companyId = "";
        storeId = "";
        bizCatId = null;
    }

    /**
     * �����e�i���X���O�X�܃e�[�u�������擾����B
     * @return �����e�i���X���O�X�܃e�[�u����
     */
    @ApiModelProperty(value = "�����e�i���X���O�X�܃e�[�u����", notes = "�����e�i���X���O�X�܃e�[�u����")
    public String getTableName() {
        return tableName;
    }

    /**
     * �����e�i���X���O�X�܃e�[�u������ݒ肷��B
     * @param tableName �����e�i���X���O�X�܃e�[�u����
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName == null ? "" : tableName;
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
     * @return
     */
    @ApiModelProperty(value = "�ƑԃR�[�h", notes = "�ƑԃR�[�h")
    public String getBizCatId() {
        return bizCatId;
    }

    /**
     * �ƑԃR�[�h��ݒ肷��B
     * @param bizCatId
     */
    public void setBizCatId(final String bizCatId) {
        this.bizCatId = bizCatId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("tableName: ").append(getTableName());
        builder.append("; companyId: ").append(getCompanyId());
        builder.append("; storeId: ").append(getStoreId());
        builder.append("; bizCatId: ").append(getBizCatId());

        return builder.toString();
    }
}
