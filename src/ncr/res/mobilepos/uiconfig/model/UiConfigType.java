package ncr.res.mobilepos.uiconfig.model;

public enum UiConfigType {
    PICKLIST("picklist"),
    OPTIONS("options"),
    IMAGES("images"),
    NOTICE("notice"),
    USABILITY("usability"),
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
            default:
                configType = UiConfigType.UNKNOWN;
        }
        return configType;
    }

}



