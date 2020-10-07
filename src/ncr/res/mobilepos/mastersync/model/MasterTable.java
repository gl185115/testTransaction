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
     * MasterTableを初期化する。
     */
    public MasterTable() {
        tableName = "";
        recordCount = 0;
        records = new LinkedList<Record>();
    }

    /**
     * テーブル名を取得する。
     * @return テーブル名
     */
    @ApiModelProperty(value = "テーブル名", notes = "テーブル名")
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブル名を設定する。
     * @param tableName テーブル名
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * レコード数を取得する。
     * @return レコード数
     */
    @ApiModelProperty(value = "レコード数", notes = "レコード数")
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * レコード数を設定する。
     * @param recordCount レコード数
     */
    public void setRecordCount(final int recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 更新レコード一覧を取得する。
     * @return 更新レコード一覧
     */
    public List<Record> getRecords() {
        return records;
    }

    /**
     * 更新レコード一覧を設定する。
     * @param records 更新レコード一覧
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
