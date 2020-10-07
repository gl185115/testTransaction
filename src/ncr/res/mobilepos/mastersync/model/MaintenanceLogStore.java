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
     * MaintenanceLogStoreを初期化する。
     */
    public MaintenanceLogStore() {
        tableName = "";
        companyId = "";
        storeId = "";
        bizCatId = null;
    }

    /**
     * メンテナンスログ店舗テーブル名を取得する。
     * @return メンテナンスログ店舗テーブル名
     */
    @ApiModelProperty(value = "メンテナンスログ店舗テーブル名", notes = "メンテナンスログ店舗テーブル名")
    public String getTableName() {
        return tableName;
    }

    /**
     * メンテナンスログ店舗テーブル名を設定する。
     * @param tableName メンテナンスログ店舗テーブル名
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName == null ? "" : tableName;
    }

    /**
     * 会社コードを取得する。
     * @return 会社コード
     */
    @ApiModelProperty(value = "会社コード", notes = "会社コード")
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 会社コードを設定する。
     * @param companyId 会社コード
     */
    public void setCompanyId(final String companyId) {
        this.companyId = companyId == null ? "" : companyId;
    }

    /**
     * 店舗コードを取得する。
     * @return 店舗コード
     */
    @ApiModelProperty(value = "店舗コード", notes = "店舗コード")
    public String getStoreId() {
        return storeId;
    }

    /**
     * 店舗コードを設定する。
     * @param storeId 店舗コード
     */
    public void setStoreId(final String storeId) {
        this.storeId = storeId == null ? "" : storeId;
    }

    /**
     * 業態コードを取得する。
     * @return
     */
    @ApiModelProperty(value = "業態コード", notes = "業態コード")
    public String getBizCatId() {
        return bizCatId;
    }

    /**
     * 業態コードを設定する。
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
