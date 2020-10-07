package ncr.res.mobilepos.mastersync.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "response")
@ApiModel(value = "MaintenanceLogResponse")
public class MaintenanceLogResponse extends ResultBase {
    /**
     * 処理結果 ... メンテナンスデータあり
     */
    public static final int RESULT_MAINTENANCE_DATA_EXISTS = 1;

    /**
     * 処理結果 ... メンテナンスデータなし
     */
    public static final int RESULT_MAINTENANCE_DATA_NOT_EXISTS = 0;

    @XmlElement(name = "result")
    private int result;

    @XmlElement(name = "dataCount")
    private int dataCount;

    @XmlElement(name = "maintenanceLog")
    private List<MaintenanceLog> maintenanceLogs;

    /**
     * MaintenanceLogResponseを初期化する。
     */
    public MaintenanceLogResponse() {
        super();

        result = 0;
        dataCount = 0;
        maintenanceLogs = new ArrayList<MaintenanceLog>();
    }

    /**
     * 処理結果を取得する。
     * @return 処理結果
     */
    @ApiModelProperty(value = "処理結果", notes = "処理結果")
    public int getResult() {
        return result;
    }

    /**
     * 処理結果を設定する。
     * @param result 処理結果
     */
    public void setResult(final int result) {
        this.result = result;
    }

    /**
     * 返送データ数を取得する。
     * @return 返送データ数
     */
    @ApiModelProperty(value = "返送データ数", notes = "返送データ数")
    public int getDataCount() {
        return dataCount;
    }

    /**
     * 返送データ数を設定する。
     * @param dataCount 返送データ数
     */
    public void setDataCount(final int dataCount) {
        this.dataCount = dataCount;
    }

    /**
     * メンテナンスデータの一覧を取得する。
     * @return メンテナンスデータ
     */
    @ApiModelProperty(value = "メンテナンスデータ", notes = "メンテナンスデータ")
    public List<MaintenanceLog> getMaintenanceLogs() {
        return maintenanceLogs;
    }

    /**
     * メンテナンスデータの一覧を設定する。
     * @param maintenanceLog メンテナンスデータ
     */
    public void setMaintenanceLogs(final List<MaintenanceLog> maintenanceLogs) {
        this.maintenanceLogs = maintenanceLogs == null ? new ArrayList<MaintenanceLog>() : maintenanceLogs;
    }

    /**
     * メンテナンスデータの一覧にメンテナンスデータを追加する。
     * @param maintenanceLog メンテナンスデータ
     */
    public void addMaintenanceLog(final MaintenanceLog maintenanceLog) {
        this.maintenanceLogs.add(maintenanceLog);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(super.toString());
        builder.append("; result: ").append(getResult());
        builder.append("; dataCount: ").append(getDataCount());

        for (MaintenanceLog log: getMaintenanceLogs()) {
            builder.append("; maintenanceLog: ").append(log);
        }

        return builder.toString();
    }
}
