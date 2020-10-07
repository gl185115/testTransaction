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
@XmlRootElement(name = "masterTable")
@ApiModel(value = "MasterTable")
public class MasterTable {
    @XmlElement(name = "tableName")
    private String tableName;

    @XmlElement(name = "recordCount")
    private int recordCount;

    @XmlElement(name = "records")
    private List<Record> records;

    /**
     * MasterTable������������B
     */
    public MasterTable() {
        tableName = "";
        recordCount = 0;
        records = new LinkedList<Record>();
    }

    /**
     * �e�[�u�������擾����B
     * @return �e�[�u����
     */
    @ApiModelProperty(value = "�e�[�u����", notes = "�e�[�u����")
    public String getTableName() {
        return tableName;
    }

    /**
     * �e�[�u������ݒ肷��B
     * @param tableName �e�[�u����
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * ���R�[�h�����擾����B
     * @return ���R�[�h��
     */
    @ApiModelProperty(value = "���R�[�h��", notes = "���R�[�h��")
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * ���R�[�h����ݒ肷��B
     * @param recordCount ���R�[�h��
     */
    public void setRecordCount(final int recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * �X�V���R�[�h�ꗗ���擾����B
     * @return �X�V���R�[�h�ꗗ
     */
    public List<Record> getRecords() {
        return records;
    }

    /**
     * �X�V���R�[�h�ꗗ��ݒ肷��B
     * @param records �X�V���R�[�h�ꗗ
     */
    public void setRecords(final List<Record> records) {
        this.records = records == null ? new LinkedList<Record>() : records;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("tableName: ").append(getTableName());
        builder.append("; recordCount: ").append(getRecordCount());

        for (Record record : getRecords()) {
            builder.append("; record: ").append(record);
        }

        return builder.toString();
    }
}
