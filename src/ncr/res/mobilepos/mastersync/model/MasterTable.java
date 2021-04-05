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

    @XmlElement(name = "outputType")
    private int outputType;

    @XmlElement(name = "outputPath")
    private String outputPath;

    @XmlElement(name = "recordCount")
    private int recordCount;

    @XmlElement(name = "records")
    private List<Record> records;

    @XmlElement(name = "pickListImages")
    private List<PickListImage> pickListImages;

    /**
     * MasterTable������������B
     */
    public MasterTable() {
        tableName = "";
        outputType = 0;
        outputPath = "";
        recordCount = 0;
        records = new LinkedList<Record>();
        pickListImages = new LinkedList<PickListImage>();
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
     * �o�̓^�C�v���擾����B
     * @return �o�̓^�C�v
     */
    @ApiModelProperty(value = "�o�̓^�C�v", notes = "�o�̓^�C�v")
    public int getOutputType() {
        return outputType;
    }

    /**
     * �o�̓^�C�v��ݒ肷��B
     * @param outputType �o�̓^�C�v
     */
    public void setOutputType(final int outputType) {
        this.outputType = outputType;
    }

    /**
     * �o�͐�p�X���擾����B
     * @return �o�͐�p�X
     */
    @ApiModelProperty(value = "�o�͐�p�X", notes = "�o�͐�p�X")
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * �o�͐�p�X��ݒ肷��B
     * @param outputPath �o�͐�p�X
     */
    public void setOutputPath(final String outputPath) {
        this.outputPath = outputPath;
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

    /**
     * �s�b�N���X�g�摜�ꗗ���擾����B
     * @return �s�b�N���X�g�摜�ꗗ
     */
    public List<PickListImage> getPickListImages() {
        return pickListImages;
    }

    /**
     * �s�b�N���X�g�摜�ꗗ��ݒ肷��B
     * @param pickListImages �s�b�N���X�g�摜�ꗗ
     */
    public void setPickListImages(final List<PickListImage> pickListImages) {
        this.pickListImages = pickListImages == null ? new LinkedList<PickListImage>() : pickListImages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("tableName: ").append(getTableName());
        builder.append("; outputType: ").append(getOutputType());
        builder.append("; outputPath: ").append(getOutputPath());
        builder.append("; recordCount: ").append(getRecordCount());

        for (Record record : getRecords()) {
            builder.append("; record: ").append(record);
        }

        for (PickListImage image : getPickListImages()) {
            builder.append(": pickListImage: ").append(image);
        }

        return builder.toString();
    }
}
