package ncr.res.mobilepos.mastersync.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.helper.StringUtility;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "request")
@ApiModel(value = "MaintenanceLogRequest")
public class MaintenanceLogRequest {
    @XmlElement(name = "companyId")
    private String companyId;

    @XmlElement(name = "storeId")
    private String storeId;

    @XmlElement(name = "bizCatId")
    private String bizCatId;

    @XmlElement(name = "maintenanceId")
    private long maintenanceId;

    @XmlElement(name = "syncRecordCount")
    private int syncRecordCount;

    @XmlElement(name = "dataFiles")
    private List<DataFile> dataFiles;

    /**
     * MaintenanceLogRequestを初期化する。
     */
    public MaintenanceLogRequest() {
        companyId = "";
        storeId = "";
        bizCatId = "";
        maintenanceId = -1;
        syncRecordCount = 0;
        dataFiles = new ArrayList<DataFile>();
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
     * @return 業態コード
     */
    @ApiModelProperty(value = "業態コード", notes = "業態コード")
    public String getBizCatId() {
        return bizCatId;
    }

    /**
     * 業態コードを設定する。
     * @param bizCatId 業態コード
     */
    public void setBizCatId(final String bizCatId) {
        this.bizCatId = bizCatId == null ? "" : bizCatId;
    }

    /**
     * メンテナンスIDを取得する。
     * @return メンテナンスID
     */
    @ApiModelProperty(value = "メンテナンスID", notes = "メンテナンスID")
    public long getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * メンテナンスIDを設定する。
     * @param maintenanceId メンテナンスID
     */
    public void setMaintenanceId(final long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    /**
     * 同期レコード件数を取得する。
     * @return 同期レコード件数
     */
    @ApiModelProperty(value = "同期レコード件数", notes = "同期レコード件数")
    public int getSyncRecordCount() {
        return syncRecordCount;
    }

    /**
     * 同期レコード件数を設定する。
     * @param syncRecordCount 同期レコード件数
     */
    public void setSyncRecordCount(final int syncRecordCount) {
        this.syncRecordCount = syncRecordCount;
    }

    /**
     * 配信ファイル情報を取得する。
     * @return 配信ファイル情報
     */
    @ApiModelProperty(value = "配信ファイル情報", notes = "配信ファイル情報")
    public List<DataFile> getDataFiles() {
        return dataFiles;
    }

    /**
     * 配信ファイル情報を設定する。
     * @param dataFiles 配信ファイル情報
     */
    public void setDataFiles(final List<DataFile> dataFiles) {
        this.dataFiles = dataFiles == null ? new ArrayList<DataFile>() : dataFiles;
    }

    /**
     * リクエストの内容が有効かどうか判断する。
     * @return
     *      リクエストの内容が有効な場合はtrueを返す。
     *      リクエストの内容が無効な場合はfalseを返す。
     */
    public boolean isValid() {
        // 会社コード、店舗コード、業態コードが未設定の場合は無効
        if (StringUtility.isNullOrEmpty(getCompanyId(), getStoreId(), getBizCatId())) {
            return false;
        }

        // メンテナンスIDがマイナス値の場合は無効
        if (getMaintenanceId() < 0) {
            return false;
        }

        // 同期レコード件数が0以下の場合は無効
        if (getSyncRecordCount() <= 0) {
            return false;
        }

        // 配信ファイル情報が未設定の場合は無効
        if (getDataFiles() == null || getDataFiles().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("companyId: ").append(getCompanyId());
        builder.append("; storeId: ").append(getStoreId());
        builder.append("; bizCatId: ").append(getBizCatId());
        builder.append("; maintenanceId: ").append(getMaintenanceId());
        builder.append("; syncRecordCount: ").append(getSyncRecordCount());

        for (DataFile dataFile : getDataFiles()) {
            builder.append("; dataFile: ").append(dataFile);
        }

        return builder.toString();
    }
}
