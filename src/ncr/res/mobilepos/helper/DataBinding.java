package ncr.res.mobilepos.helper;


import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * JAXB APIを使って、各種XMLをオブジェクト変換する。(又は、その逆も)
 *
 */
public class DataBinding<T> {

    private JAXBContext jaxbContext;

	/**
	 * パラメータに設定したクラスに変換するJAXBContextオブジェクトの生成
	 *
     * @param clas
     *    変換するクラス
     *
     * @throws JAXBExceptio JAXBContextの作成に失敗した場合
     */
    public DataBinding(final Class<T> clas) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(clas);
    }


    /**
     * xmlの前にゴミ（不要な文字）が含まれていたら削除して、
     * 削除のxmlを返す。
     *
     * @param strXml    チェックするxml
     * @return          Returns the exact/correct xml format.
     */
    public final String toTrim(final String strXml) {
        if (null == strXml || strXml.isEmpty()) {
            return "";
        }

        return strXml.substring(strXml.indexOf('<'));
    }

    /**
     * パラメータにセットしたxmlのオブジェクトへの変換
     *
     * @param xml            変換元のxml
     * @return               xmlから変換されたオブジェクト
     * @throws JAXBException 変換に失敗した場合
     */
    @SuppressWarnings("unchecked")
    public final T unMarshallXml(final String xml) throws JAXBException {
        if (xml.isEmpty()) {
            return null;
        }

        T object;

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        //Trim the CreditAuthorizationXML to get the exact xml string
        String trimedXml = toTrim(xml);

        StringReader reader = new StringReader(trimedXml);

        object =  (T) unmarshaller.unmarshal(reader);

        return object;
    }


    /**
     * パラメータにセットしたxmlファイルのオブジェクトへの変換
     *
     * @param xml             変換元のxmlファイル
     * @return                xmlファイルから変換されたオブジェクト
     * @throws JAXBException  変換に失敗した場合
     */
    @SuppressWarnings("unchecked")
    public final T unMarshallXml(File fileXml) throws JAXBException {

        if (!fileXml.exists()) {
            return null;
        }

        T object;

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        object =  (T) unmarshaller.unmarshal(fileXml);

        return object;
    }


    /**
     * パラメータにセットしたオブジェクトのxmlへの変換
     *
     * @param object         変換元のオブジェクト
     * @param encoding       使用するエンコーディング
     * @return String        変換されたxml
     * @throws JAXBException 変換に失敗した場合
     */
    public final String marshallObj(final T object, final String encoding) throws JAXBException {
        StringWriter sw = new StringWriter();

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.marshal(object, sw);

        return sw.getBuffer().toString();
    }
}
