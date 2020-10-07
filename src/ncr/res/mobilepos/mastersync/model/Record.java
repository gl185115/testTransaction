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
@XmlRootElement(name = "record")
@ApiModel(value = "Record")
public class Record {
    @XmlElement(name = "number")
    private int number;

    @XmlElement(name = "fields")
    private List<Field> fields;

    /**
     * Record������������B
     */
    public Record() {
        number = 0;
        fields = new LinkedList<Field>();
    }

    /**
     * ���R�[�hNo���擾����B
     * @return ���R�[�hNo
     */
    @ApiModelProperty(value = "���R�[�hNo", notes = "���R�[�hNo")
    public int getNumber() {
        return number;
    }

    /**
     * ���R�[�hNo��ݒ肷��B
     * @param number ���R�[�hNo
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * �t�B�[���h�ꗗ���擾����B
     * @return �t�B�[���h�ꗗ
     */
    @ApiModelProperty(value = "�t�B�[���h�ꗗ", notes = "�t�B�[���h�ꗗ")
    public List<Field> getFields() {
        return fields;
    }

    /**
     * �t�B�[���h�ꗗ��ݒ肷��B
     * @param fields �t�B�[���h�ꗗ
     */
    public void setFields(final List<Field> fields) {
        this.fields = fields == null ? new LinkedList<Field>() : fields;
    }

    /**
     * �t�B�[���h�ꗗ�Ƀt�B�[���h��ǉ�����B
     * @param field �t�B�[���h
     */
    public void addField(final Field field) {
        fields.add(field);;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("number: ").append(getNumber());

        for (Field field : getFields()) {
            builder.append("; field: ").append(field);
        }

        return builder.toString();
    }
}
