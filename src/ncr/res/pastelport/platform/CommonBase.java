package ncr.res.pastelport.platform;

import java.util.LinkedHashMap;
import java.util.Map;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

/**
 * base class that save data in json way.
 *
 * @version $Revision: 1.0 $ $Date: 2012/04/20 02:49:47 $
 */
public abstract class CommonBase {
    /** Field Digit one. */
    public static final int  DIGIT_ONE = 1;
    /** Field Digit two. */
    public static final int  DIGIT_TWO = 2;
    /** Field Digit three. */
    public static final int  DIGIT_THREE = 3;
    /** Field Digit Four. */
    public static final int  DIGIT_FOUR = 4;
    /** Field Digit Five. */
    public static final int  DIGIT_FIVE = 5;
    /** Field Digit Six. */
    public static final int  DIGIT_SIX = 6;
    /** Field Digit Seven. */
    public static final int  DIGIT_SEVEN = 7;
    /** Field Digit Eight. */
    public static final int  DIGIT_EIGHT = 8;
    /** Field Digit Nine. */
    public static final int  DIGIT_NINE = 9;
    /** Field Digit Ten. */
    public static final int  DIGIT_TEN = 10;
    /** Field Digit Eleven. */
    public static final int  DIGIT_ELEVEN = 11;
    /** Field Digit Twelve. */
    public static final int  DIGIT_TWELVE = 12;
    /** Field Digit Thirteen. */
    public static final int  DIGIT_THIRTEN = 13;
    /** Field Digit Fourteen. */
    public static final int  DIGIT_FOURTEN = 14;
    /** Field Digit Sixteen. */
    public static final int  DIGIT_SIXTEN = 16;
    /** Field Digit Twenty. */
    public static final int  DIGIT_TWENTY = 20;
    /** Field Digit Twenty two. */
    public static final int  DIGIT_TWENTY_TWO = 22;
    /** Field Digit Twenty five. */
    public static final int  DIGIT_TWENTY_FIVE = 25;
    /** Field Digit Thirty Seven. */
    public static final int  DIGIT_THIRTY_SEVEN = 37;
    /** Field Digit Thirty FIVE. */
    public static final int  DIGIT_THIRTY_FIVE = 35;
    /** Field Digit Sixty Nine. */
    public static final int  DIGIT_SIXTY_NINE = 69;
    /** Field Digit MAX. */
    public static final int  DIGIT_NINETEN_NINTY_EIGHT = 1998;
    /** Field Digit MAX. */
    public static final int  DIGIT_TWO_THOUSAND = 2000;
    /**
     * The Top Element.
     */
    private String topElement = "topelement";
    /**
     * The xml data.
     */
    private Map<String, XmlData> xml;
    /**
     * The JSON Object.
     */
    private JSONObject jsonObj;
    /**
     * The JSON string.
     */
    private String fety = "JSON";

    /**
     * create a new CommonBase object.
     */
    public CommonBase() {
        super();
        jsonObj = new JSONObject();
        this.xml = new LinkedHashMap<String, XmlData>();
    }

    /**
     * send the context of XmlData to linkedhashmap.
     * @param key The Key
     * @param field The Xml Data.
     */
    public final void setField(final String key, final XmlData field) {
        xml.put(key, field);
    }

    /**
     * get the content by the key.
     * @param key The key.
     * @return The Xml Data.
     */
    public final XmlData getField(final String key) {
        return (XmlData) xml.get(key);
    }

    /**
     * get the value corresponding to the key of.
     * @param key The key.
     * @return The field Value.
     * @throws JSONException The Exception thrown when error occur.
     */
    public final String getFieldValue(final String key) throws JSONException {
        String value = "";
        if ("xml".equals(fety)) {
            XmlData tag = getField(key);
            if (tag == null) {
                return value;
            }
            value = tag.getValue();
        } else {
            value = getJsonValue(key);
            if ("".equals(value)) {
                throw new JSONException("Null pointer");
            }
        }
        return value;
    }

    /**
     * set the value corresponding to the key of.
     * @param key   The Key.
     * @param value The Value.
     * @throws JSONException The exception thrown when method failed.
     */
    public final void setFieldValue(final String key, final String value)
    throws JSONException {
        if ("xml".equals(fety)) {
            XmlData tag = getField(key);
            if (tag == null) {
                tag = new XmlData();
                setField(key, tag);
            }
            tag.setValue(value);
        } else {
            jsonObj.put(key, value);
        }
    }

    /**
     * get all keys from the linkedlist.
     * @return Array of keys.
     */
    public final String[] getAllKeys() {
        Object[] oarr = xml.keySet().toArray();
        if (oarr.length == 0) {
            return new String[0];
        }
        String[] sarr = new String[oarr.length];
        for (int i = 0; i < oarr.length; i++) {
            sarr[i] = (String) oarr[i];
        }
        return sarr;
    }

    /**
     * set the root element.
     * @param topElementToSet The Top Element to set.
     */
    public final void setTopElement(final String topElementToSet) {
        this.topElement = topElementToSet;
    }

    /**
     * to clear all of the XML date that is currently held.
     */
    public void clear() {
        xml.clear();
    }

    /**
     * @return the xml form of the object.
     */
    public final String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"shift_jis\"?>\r\n");
        sb.append("<").append(topElement).append(">\r\n");

        String[] keys = getAllKeys();

        for (int i = 0; i < keys.length; i++) {
            sb.append("    ");
            sb.append(makeTag(getField(keys[i]), keys[i]));
            sb.append("\r\n");
        }
        sb.append("</").append(topElement).append(">\r\n");
        return sb.toString();
    }

    /**
     * Make an Xml Tag.
     *
     * @param field     The Field.
     * @param tagname   The Tag Name.
     * @param detail    The Detail.
     * @return      The Xml Node in String.
     */
    protected final String makeTag(final XmlData field,
            final String tagname, final boolean detail) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagname);
        if (detail || field.isEncrypted()) {
            sb.append(" encrypt=\"").append(field.isEncrypted()).append("\"");
        }
        if (detail || !field.canLogging()) {
            sb.append(" logging=\"").append(field.canLogging()).append("\"");
        }
        sb.append(">");
        if (field.canLogging()) {
            sb.append(field.getValue());
        }
        sb.append("</").append(tagname).append(">");
        return sb.toString();
    }

    /**
     * Make an Xml tag.
     * @param field     The Field.
     * @param tagname   The Tag name.
     * @return  The Xml Node in String.
     */
    protected final String makeTag(final XmlData field, final String tagname) {
        return this.makeTag(field, tagname, false);
    }

    /**
     * @param key The key.
     * @return get the value corresponding to the key of
     * @throws JSONException The exception thrown when error occur.
     */
    protected final String getJsonValue(final String key) throws JSONException {
        String ret = "GETJSONVALUEFAILED";
        ret = jsonObj.getString(key);
        return ret;
    }

    /**
     * Set the data of Json.
     *
     * @param strJson
     *            : the Json string to set
     *
     * @throws JSONException The exception Thrown when error occur.
     */
    public final void setJsonValue(final String strJson) throws JSONException {
        jsonObj = new JSONObject(strJson);
    }

}
