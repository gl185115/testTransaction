package ncr.res.mobilepos.uiconfig.utils;

public enum RESConfigEnum {
    REQUESTTIMER("clientRequestTimer"),

    DIR_CUSTOM("dir_custom"),
    DIR_ADVERTISE("dir_advertise"),
    DIR_IMAGES("dir_images"),
    DIR_NOTICES("dir_notices"),
    DIR_PHRASES("dir_phrases"),
    DIR_PICKLIST("dir_pickList"),
    DIR_OPTIONS("dir_options"),
    DIR_USABILITY("dir_usability"),
    DIR_SCHEDULE("dir_schedule"),
    DIR_LOG("dir_log"),
    DIR_LOGS("dir_logs"),

    FILE_RULE("csv_rule"),
    FILE_STORESCSV("csv_stores"),
    FILE_STORESDETAILCSV("csv_storesDetail"),
    FILE_TABLESTORE("csv_tableStore"),
    FILE_TABLEGROUP("csv_tableGroup"),
    FILE_ACCESSLOGSFILE("csv_accesslog"),
    FILE_RESPROFILE("xml_resProfile"),
    FILE_SCHEDULE("xml_schedule"),
    FILE_NOTICESXML("xml_schedule_notices"),
    FILE_PICKLISTXML("xml_schedule_pickList"),
    FILE_RESTRANSPATH("config_resTransPath"),
    FILE_DEFAULTTRN("trn_default"),
    FILE_COMTRN("trn_com"),
    FILE_DEFAULTDST("dst_default"),

    ZIP_CUSTOMIZE("zip_customize"),

    COMPANYID("companyID"),
    USERID("userID"),
    PASSWORD("password"),
    RESOURCE("resource"),
    RESCONFIG("resconfig");

    private RESConfigEnum(String key) {
        if (configProperties == null) {
            configProperties = ConfigProperties.getInstance();
        }

//        this.value = configProperties.getValue(key);
        this.key = key;
    }

    public String getValue() {
        return configProperties.getValue(key);
    }

    @Override
    public String toString() {
        return getValue();
    }

    private ConfigProperties configProperties;
    private String key;
//    private String value;
}
