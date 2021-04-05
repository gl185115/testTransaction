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
     * MasterTableを初期化する。
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
     * 出力タイプを取得する。
     * @return 出力タイプ
     */
    @ApiModelProperty(value = "出力タイプ", notes = "出力タイプ")
    public int getOutputType() {
        return outputType;
    }

    /**
     * 出力タイプを設定する。
     * @param outputType 出力タイプ
     */
    public void setOutputType(final int outputType) {
        this.outputType = outputType;
    }

    /**
     * 出力先パスを取得する。
     * @return 出力先パス
     */
    @ApiModelProperty(value = "出力先パス", notes = "出力先パス")
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * 出力先パスを設定する。
     * @param outputPath 出力先パス
     */
    public void setOutputPath(final String outputPath) {
        this.outputPath = outputPath;
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

    /**
     * ピックリスト画像一覧を取得する。
     * @return ピックリスト画像一覧
     */
    public List<PickListImage> getPickListImages() {
        return pickListImages;
    }

    /**
     * ピックリスト画像一覧を設定する。
     * @param pickListImages ピックリスト画像一覧
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
