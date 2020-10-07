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
     * Fieldを初期化する。
     */
    public Field() {
        number = 0;
        name = "";
        value = null;
    }

    /**
     * フィールドNoを取得する。
     * @return フィールドNo
     */
    @ApiModelProperty(value = "フィールドNo", notes = "フィールドNo")
    public int getNumber() {
        return number;
    }

    /**
     * フィールドNoを設定する。
     * @param number フィールドNo
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * フィールド名を取得する。
     * @return フィールド名
     */
    @ApiModelProperty(value = "フィールド名", notes = "フィールド名")
    public String getName() {
        return name;
    }

    /**
     * フィールド名を設定する。
     * @param name フィールド名
     */
    public void setName(final String name) {
        this.name = name == null ? "" : name;
    }

    /**
     * フィールド値を取得する。
     * @return フィールド値
     */
    @ApiModelProperty(value = "フィールド値", notes = "フィールド値")
    public Object getValue() {
        return value;
    }

    /**
     * フィールド値を設定する。
     * @param value フィールド値
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
