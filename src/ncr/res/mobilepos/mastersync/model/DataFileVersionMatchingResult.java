package ncr.res.mobilepos.mastersync.model;

/**
 * 配信ファイルバージョンチェック結果の定義
 */
public enum DataFileVersionMatchingResult {
    /**
     * 配信ファイルのバージョンが一致
     */
    DATA_FILE_VERSION_MATCH,
    /**
     * 配信ファイルのバージョンが不一致
     */
    DATA_FILE_VERSION_NOT_MATCH,
    /**
     * サーバ側の配信ファイル情報の取得に失敗
     */
    SERVER_DATA_FILE_GET_FAILED,
    /**
     * 配信ファイルの数が不一致
     */
    DATA_FILE_COUNT_NOT_MATCH
}
