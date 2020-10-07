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
     * DataFileを初期化する。
     */
    public DataFile() {
        type = 0;
        version = "";
        maintenanceId = 0;
    }

    /**
     * 配信ファイルタイプを取得する。
     * @return 配信ファイルタイプ
     */
    @ApiModelProperty(value = "配信ファイルタイプ", notes = "配信ファイルタイプ")
    public int getType() {
        return type;
    }

    /**
     * 配信ファイルタイプを設定する。
     * @param type 配信ファイルタイプ
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * 配信ファイルバージョンを取得する。
     * @return 配信ファイルバージョン
     */
    @ApiModelProperty(value = "配信ファイルバージョン", notes = "配信ファイルバージョン")
    public String getVersion() {
        return version;
    }

    /**
     * 配信ファイルバージョンを設定する。
     * @param version 配信ファイルバージョン
     */
    public void setVersion(final String version) {
        this.version = version == null ? "" : version;
    }

    /**
     * 配信ファイル作成時メンテナンスIDを取得する。
     * @return 配信ファイル作成時メンテナンスID
     */
    @ApiModelProperty(value = "配信ファイル作成時メンテナンスID", notes = "配信ファイル作成時メンテナンスID")
    public long getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * 配信ファイル作成時メンテナンスIDを設定する。
     * @param maintenanceId 配信ファイル作成時メンテナンスID
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
        // 配信ファイルタイプと配信ファイルバージョンが一致していれば同値とみなす
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
