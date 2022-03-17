/****************************************************
 * Copyright 2021 NCR Corporation
 *
 * Date             Quick look Id                  Change
 * -----------------------------------------------------------
 * 12-Dec-2021       cp185211            RESD-7817- Move SCO AImessage to MeX ResTablet mapping file to custom folder in MeX Host
 * 
 ****************************************************/
package ncr.res.mobilepos.uiconfig.model;

public enum UiConfigType {
    PHRASES("phrases"),
    PICKLIST("pickList"),
    OPTIONS("options"),
    IMAGES("images"),
    NOTICE("notice"),
    USABILITY("usability"),
    ADVERTISES("advertise"),
    RECEIPT("receipt"),
    SOUNDS("sounds"),
    SCOMAPS("scomaps"),

    SCOUTILS("scoutils"),

    UNKNOWN("");

    private final String key;

    /**
     * Private Constructor.
     *
     * @param key key to look up in web.xml.
     */
    UiConfigType(String key) {
        this.key = key;
    }

    /**
     * Returns the value.
     *
     * @return
     */
    public String getValue() {
        return this.key;
    }

    @Override
    /**
     * Returns the value.
     */
    public String toString() {
        return getValue();
    }

    // Request API types.
    private static final String API_OPTIONS = "options";
    private static final String AIP_USABILITY = "usability";
    private static final String API_PHRASES = "phrases";
    private static final String API_PICKLIST = "picklist";
    private static final String API_ADVERTISES = "advertise";
    private static final String API_RECEIPT = "receipt";
    private static final String API_SCOMAPS = "scomaps";

    private static final String API_SCOUTILS = "scoutils";


    /**
     * @param typeParam
     * @return
     */
    public static UiConfigType toEnum(String typeParam) {
        UiConfigType configType;
        switch (typeParam.toLowerCase()) {
            case API_OPTIONS:
                configType = UiConfigType.OPTIONS;
                break;
            case AIP_USABILITY:
                configType = UiConfigType.USABILITY;
                break;
            case API_PHRASES:
                configType = UiConfigType.PHRASES;
                break;
            case API_PICKLIST:
                configType = UiConfigType.PICKLIST;
                break;
            case API_ADVERTISES:
                configType = UiConfigType.ADVERTISES;
                break;
            case API_RECEIPT:
                configType = UiConfigType.RECEIPT;
                break;
            case API_SCOMAPS:
                configType = UiConfigType.SCOMAPS;
                break;

            case API_SCOUTILS:
                configType = UiConfigType.SCOUTILS;
                break;

            default:
                configType = UiConfigType.UNKNOWN;
        }
        return configType;
    }

}