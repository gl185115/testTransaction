package ncr.res.mobilepos.creditauthorization.helper;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class ResponseTable.
 */
public class ResponseTable extends DefaultHandler {

    /** The error code. */
    private String errCode;

    /** The current data. */
    private StringBuilder currData;

    /** The m. */
    @SuppressWarnings("rawtypes")
    private Map m;

    /**
     * Instantiates a new response table.
     */
    @SuppressWarnings("rawtypes")
    public ResponseTable() {
        errCode = null;

        m = new HashMap();
    }

    /**
     * Gets the hash map.
     *
     * @return the hash map
     */
    @SuppressWarnings("rawtypes")
    public final Map getHashMap() {
        return m;
    }

    /**
     * Checks if is exist status.
     *
     * @param errorCode
     *            the error code
     * @return true, if is exist status
     */
    public final boolean isExistStatus(final String errorCode) {
        if (m.containsKey(errorCode)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the error status.
     *
     * @param errorCode
     *            the error code
     * @return the error status
     */
    @SuppressWarnings("rawtypes")
    public final String getErrorStatus(final String errorCode) {
        if (!isExistStatus(errorCode)) {
            return "00";
        }
        if (!((Map) m.get(errorCode)).containsKey("Status")) {
            return "00";
        }
        return (((Map) m.get(errorCode)).get("Status")).toString();
    }

    /**
     * Gets the error message.
     *
     * @param errorCode
     *            the error code
     * @return the error message
     */
    @SuppressWarnings("rawtypes")
    public final String getErrorMessage(final String errorCode) {
        if (!isExistStatus(errorCode)) {
            return " ";
        }
        if (!((Map) m.get(errorCode)).containsKey("Message")) {
            return " ";
        }
        return (((Map) m.get(errorCode)).get("Message")).toString();
    }

    /**
     * Gets the error alternate message.
     *
     * @param errorCode
     *            the error code
     * @return the error alt message
     */
    @SuppressWarnings("rawtypes")
    public final String getErrorAltMessage(final String errorCode) {
        if (!isExistStatus(errorCode)) {
            return " ";
        }
        if (!((Map) m.get(errorCode)).containsKey("AltMessage")) {
            return " ";
        }
        return (((Map) m.get(errorCode)).get("AltMessage")).toString();
    }

    /**
     * 要素の開始タグ読み込み時.
     *
     * @param uri
     *            the uri
     * @param localName
     *            the local name
     * @param qName
     *            the q name
     * @param attributes
     *            the attributes
     * @throws SAXException
     *             the SAX exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void startElement(final String uri, final String localName,
            final String qName, final Attributes attributes)
            throws SAXException {
        currData = new StringBuilder();
        if ("Error".equals(qName)) {
            errCode = "";
            errCode = attributes.getValue("code");
            m.put(errCode, new HashMap());
            ((Map) m.get(errCode)).put("Status", "");
            ((Map) m.get(errCode)).put("Message", "");
            ((Map) m.get(errCode)).put("AltMessage", "");
        } else {
            super.startElement(uri, localName, qName, attributes);
        }
    }

    /**
     * テキストデータ読み込み時.
     *
     * @param ch
     *            the ch
     * @param offset
     *            the offset
     * @param length
     *            the length
     * @throws SAXException
     *             the SAX exception
     */
    @Override
    public final void characters(final char[] ch, final int offset,
            final int length) throws SAXException {
        if (currData != null) {
            currData.append(ch, offset, length);
        } else {
            super.characters(ch, offset, length);
        }
    }

    /**
     * 要素の終了タグ読み込み時.
     *
     * @param uri
     *            the uri
     * @param localName
     *            the local name
     * @param qName
     *            the q name
     * @throws SAXException
     *             the SAX exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void endElement(final String uri, final String localName,
            final String qName) throws SAXException {
        if (currData != null && currData.length() > 0) {
            if ("Status".equals(qName)) {
                ((Map) m.get(errCode)).put("Status", currData.toString());
            } else if ("Message".equals(qName)) {
                ((Map) m.get(errCode)).put("Message", currData.toString());
            } else if ("AltMessage".equals(qName)) {
                ((Map) m.get(errCode)).put("AltMessage", currData.toString());
            } else {
                super.endElement(uri, localName, qName);
            }
        }
        currData = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
     */
    @Override
    public final void error(final SAXParseException ex) throws SAXException {
        StringBuilder sb = new StringBuilder(200);
        sb.append("[error ");
        sb.append(Integer.toString(ex.getLineNumber()));
        sb.append(":");
        sb.append(Integer.toString(ex.getColumnNumber()));
        sb.append("] ");
        sb.append(ex.getMessage());
        throw new SAXException(sb.toString());
    }
}
