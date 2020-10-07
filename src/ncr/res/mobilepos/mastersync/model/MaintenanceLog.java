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
@XmlRootElement(name = "maintenanceLog")
@ApiModel(value = "MaintenanceLog")
public class MaintenanceLog {
    @XmlElement(name = "tableName")
    private String tableName;

    @XmlElement(name = "maintenanceId")
    private long maintenanceId;

    @XmlElement(name = "maintenanceLogId")
    private String maintenanceLogId;

    @XmlElement(name = "maintenanceType")
    private int maintenanceType;

    @XmlElement(name = "syncGroupId")
    private int syncGroupId;

    @XmlElement(name = "referenceCondition")
    private String referenceCondition;

    @XmlElement(name = "masterUpdDate")
    private String masterUpdDate;

    @XmlElement(name = "masterUpdAppId")
    private String masterUpdAppId;

    @XmlElement(name = "masterUpdOpeCode")
    private String masterUpdOpeCode;

    @XmlElement(name = "targetStoreType")
    private String targetStoreType;

    @XmlElement(name = "startDate")
    private String startDate;

    @XmlElement(name = "endDate")
    private String endDate;

    @XmlElement(name = "startTime")
    private String startTime;

    @XmlElement(name = "endTime")
    private String endTime;

    @XmlElement(name = "store")
    private MaintenanceLogStore store;

    @XmlElement(name = "masterTables")
    private List<MasterTable> masterTables;

    /**
     * MaintenanceDataを初期化する。
     */
    public MaintenanceLog() {
        tableName = "";
        maintenanceId = 0;
        maintenanceLogId = "";
        maintenanceType = 0;
        syncGroupId = 0;
        referenceCondition = "";
        masterUpdDate = "";
        masterUpdAppId = "";
        masterUpdOpeCode = "";
        targetStoreType = "";
        startDate = "";
        endDate = "";
        startTime = "";
        endTime = "";
        store = new MaintenanceLogStore();
        masterTables = new LinkedList<MasterTable>();
    }

    /**
     * メンテナンスログテーブル名を取得する。
     * @return メンテナンスログテーブル名
     */
    @ApiModelProperty(value = "メンテナンスログテーブル名", notes = "メンテナンスログテーブル名")
    public String getTableName() {
        return tableName;
    }

    /**
     * メンテナンスログテーブル名を設定する。
     * @param tableName メンテナンスログテーブル名
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName == null ? "" : tableName;
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
     * メンテナンスログIDを取得する。
     * @return メンテナンスログID
     */
    @ApiModelProperty(value = "メンテナンスログID", notes = "メンテナンスログID")
    public String getMaintenanceLogId() {
        return maintenanceLogId;
    }

    /**
     * メンテナンスログIDを設定する。
     * @param maintenanceLogId メンテナンスログID
     */
    public void setMaintenanceLogId(final String maintenanceLogId) {
        this.maintenanceLogId = maintenanceLogId == null ? "" : maintenanceLogId;
    }

    /**
     * メンテナンス区分を取得する。
     * @return メンテナンス区分
     */
    @ApiModelProperty(value = "メンテナンス区分", notes = "メンテナンス区分")
    public int getMaintenanceType() {
        return maintenanceType;
    }

    /**
     * メンテナンス区分を設定する。
     * @param maintenanceType メンテナンス区分
     */
    public void setMaintenanceType(final int maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    /**
     * マスタ連携グループIDを取得する。
     * @return マスタ連携グループID
     */
    @ApiModelProperty(value = "マスタ連携グループID", notes = "マスタ連携グループID")
    public int getSyncGroupId() {
        return syncGroupId;
    }

    /**
     * マスタ連携グループIDを設定する。
     * @param syncGroupId マスタ連携グループID
     */
    public void setSyncGroupId(final int syncGroupId) {
        this.syncGroupId = syncGroupId;
    }

    /**
     * マスタ参照条件を取得する。
     * @return マスタ参照条件
     */
    @ApiModelProperty(value = "マスタ参照条件", notes = "マスタ参照条件")
    public String getReferenceCondition() {
        return referenceCondition;
    }

    /**
     * マスタ参照条件を設定する。
     * @param referenceCondition マスタ参照条件
     */
    public void setReferenceCondition(final String referenceCondition) {
        this.referenceCondition = referenceCondition == null ? "" : referenceCondition;
    }

    /**
     * マスタ最終更新日時を取得する。
     * @return マスタ最終更新日時
     */
    @ApiModelProperty(value = "マスタ最終更新日時", notes = "マスタ最終更新日時")
    public String getMasterUpdDate() {
        return masterUpdDate;
    }

    /**
     * マスタ最終更新日時を設定する。
     * @param masterUpdDate マスタ最終更新日時
     */
    public void setMasterUpdDate(final String masterUpdDate) {
        this.masterUpdDate = masterUpdDate == null ? "" : masterUpdDate;
    }

    /**
     * マスタ最終更新プログラムIDを取得する。
     * @return マスタ最終更新プログラムID
     */
    @ApiModelProperty(value = "マスタ最終更新プログラムID", notes = "マスタ最終更新プログラムID")
    public String getMasterUpdAppId() {
        return masterUpdAppId;
    }

    /**
     * マスタ最終更新プログラムIDを設定する。
     * @param masterUpdAppId マスタ最終更新プログラムID
     */
    public void setMasterUpdAppId(final String masterUpdAppId) {
        this.masterUpdAppId = masterUpdAppId == null ? "" : masterUpdAppId;
    }

    /**
     * マスタ最終更新ユーザIDを取得する。
     * @return マスタ最終更新ユーザID
     */
    @ApiModelProperty(value = "マスタ最終更新ユーザID", notes = "マスタ最終更新ユーザID")
    public String getMasterUpdOpeCode() {
        return masterUpdOpeCode;
    }

    /**
     * マスタ最終更新ユーザIDを設定する。
     * @param masterUpdOpeCode マスタ最終更新ユーザID
     */
    public void setMasterUpdOpeCode(final String masterUpdOpeCode) {
        this.masterUpdOpeCode = masterUpdOpeCode == null ? "" : masterUpdOpeCode;
    }

    /**
     * 対象店舗設定区分を取得する。
     * @return 対象店舗設定区分
     */
    @ApiModelProperty(value = "対象店舗設定区分", notes = "対象店舗設定区分")
    public String getTargetStoreType() {
        return targetStoreType;
    }

    /**
     * 対象店舗設定区分を設定する。
     * @param targetStoreType 対象店舗設定区分
     */
    public void setTargetStoreType(final String targetStoreType) {
        this.targetStoreType = targetStoreType;
    }

    /**
     * 期間FROMを取得する。
     * @return 期間FROM
     */
    @ApiModelProperty(value = "期間FROM", notes = "期間FROM")
    public String getStartDate() {
        return startDate;
    }

    /**
     * 期間FROMを設定する。
     * @param startDate 期間FROM
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * 期間TOを取得する。
     * @return 期間TO
     */
    @ApiModelProperty(value = "期間TO", notes = "期間TO")
    public String getEndDate() {
        return endDate;
    }

    /**
     * 期間TOを設定する。
     * @param endDate 期間TO
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * 開始時刻を取得する。
     * @return 開始時刻
     */
    @ApiModelProperty(value = "開始時刻", notes = "開始時刻")
    public String getStartTime() {
        return startTime;
    }

    /**
     * 開始時刻を設定する。
     * @param startTime 開始時刻
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * 終了時刻を取得する。
     * @return 終了時刻
     */
    @ApiModelProperty(value = "終了時刻", notes = "終了時刻")
    public String getEndTime() {
        return endTime;
    }

    /**
     * 終了時刻を設定する。
     * @param endTime 終了時刻
     */
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    /**
     * メンテナンスログ店舗情報を取得する。
     * @return メンテナンスログ店舗情報
     */
    @ApiModelProperty(value = "メンテナンスログ店舗情報", notes = "メンテナンスログ店舗情報")
    public MaintenanceLogStore getStore() {
        return store;
    }

    /**
     * メンテナンスログ店舗情報を設定する。
     * @param store メンテナンスログ店舗情報
     */
    public void setStore(final MaintenanceLogStore store) {
        this.store = store == null ? new MaintenanceLogStore() : store;
    }

    /**
     * マスタテーブル一覧を取得する。
     * @return マスタテーブル一覧
     */
    @ApiModelProperty(value = "マスタテーブル一覧", notes = "マスタテーブル一覧")
    public List<MasterTable> getMasterTables() {
        return masterTables;
    }

    /**
     * マスタテーブル一覧を設定する。
     * @param masterTables マスタテーブル一覧
     */
    public void setMasterTables(final List<MasterTable> masterTables) {
        this.masterTables = masterTables == null ? new LinkedList<MasterTable>() : masterTables;
    }

    /**
     * マスタテーブルを一覧に追加する。
     * @param masterTable マスタテーブル
     */
    public void addMasterTable(final MasterTable masterTable) {
        this.masterTables.add(masterTable);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("tableName: ").append(getTableName());
        builder.append("; maintenanceId: ").append(getMaintenanceId());
        builder.append("; maintenanceLogId: ").append(getMaintenanceLogId());
        builder.append("; maintenanceType: ").append(getMaintenanceType());
        builder.append("; syncGroupId: ").append(getSyncGroupId());
        builder.append("; referenceCondition: ").append(getReferenceCondition());
        builder.append("; masterUpdDate: ").append(getMasterUpdDate());
        builder.append("; masterUpdAppId: ").append(getMasterUpdAppId());
        builder.append("; masterUpdOpeCode: ").append(getMasterUpdOpeCode());
        builder.append("; targetStoreType: ").append(getTargetStoreType());
        builder.append("; startDate: ").append(getStartDate());
        builder.append("; endDate: ").append(getEndDate());
        builder.append("; startTime: ").append(getStartTime());
        builder.append("; endTime: ").append(getEndTime());
        builder.append("; store: ").append(getStore());

        for (MasterTable masterTable : getMasterTables()) {
            builder.append("; masterTable: ").append(masterTable);
        }

        return builder.toString();
    }
}
