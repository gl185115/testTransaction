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
     * Recordを初期化する。
     */
    public Record() {
        number = 0;
        fields = new LinkedList<Field>();
    }

    /**
     * レコードNoを取得する。
     * @return レコードNo
     */
    @ApiModelProperty(value = "レコードNo", notes = "レコードNo")
    public int getNumber() {
        return number;
    }

    /**
     * レコードNoを設定する。
     * @param number レコードNo
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * フィールド一覧を取得する。
     * @return フィールド一覧
     */
    @ApiModelProperty(value = "フィールド一覧", notes = "フィールド一覧")
    public List<Field> getFields() {
        return fields;
    }

    /**
     * フィールド一覧を設定する。
     * @param fields フィールド一覧
     */
    public void setFields(final List<Field> fields) {
        this.fields = fields == null ? new LinkedList<Field>() : fields;
    }

    /**
     * フィールド一覧にフィールドを追加する。
     * @param field フィールド
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
