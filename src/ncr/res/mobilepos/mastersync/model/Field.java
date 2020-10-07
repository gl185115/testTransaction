package ncr.res.mobilepos.mastersync.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "field")
@ApiModel(value = "Field")
public class Field {
    @XmlElement(name = "number")
    private int number;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "value")
    private Object value;

    /**
     * Field������������B
     */
    public Field() {
        number = 0;
        name = "";
        value = null;
    }

    /**
     * �t�B�[���hNo���擾����B
     * @return �t�B�[���hNo
     */
    @ApiModelProperty(value = "�t�B�[���hNo", notes = "�t�B�[���hNo")
    public int getNumber() {
        return number;
    }

    /**
     * �t�B�[���hNo��ݒ肷��B
     * @param number �t�B�[���hNo
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * �t�B�[���h�����擾����B
     * @return �t�B�[���h��
     */
    @ApiModelProperty(value = "�t�B�[���h��", notes = "�t�B�[���h��")
    public String getName() {
        return name;
    }

    /**
     * �t�B�[���h����ݒ肷��B
     * @param name �t�B�[���h��
     */
    public void setName(final String name) {
        this.name = name == null ? "" : name;
    }

    /**
     * �t�B�[���h�l���擾����B
     * @return �t�B�[���h�l
     */
    @ApiModelProperty(value = "�t�B�[���h�l", notes = "�t�B�[���h�l")
    public Object getValue() {
        return value;
    }

    /**
     * �t�B�[���h�l��ݒ肷��B
     * @param value �t�B�[���h�l
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("number: ").append(getNumber());
        builder.append("; name: ").append(getName());
        builder.append("; value: ").append(getValue());

        return builder.toString();
    }
}
