package ncr.res.mobilepos.helper;


import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * JAXB API���g���āA�e��XML���I�u�W�F�N�g�ϊ�����B(���́A���̋t��)
 *
 */
public class DataBinding<T> {

    private JAXBContext jaxbContext;

	/**
	 * �p�����[�^�ɐݒ肵���N���X�ɕϊ�����JAXBContext�I�u�W�F�N�g�̐���
	 *
     * @param clas
     *    �ϊ�����N���X
     *
     * @throws JAXBExceptio JAXBContext�̍쐬�Ɏ��s�����ꍇ
     */
    public DataBinding(final Class<T> clas) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(clas);
    }


    /**
     * xml�̑O�ɃS�~�i�s�v�ȕ����j���܂܂�Ă�����폜���āA
     * �폜��xml��Ԃ��B
     *
     * @param strXml    �`�F�b�N����xml
     * @return          Returns the exact/correct xml format.
     */
    public final String toTrim(final String strXml) {
        if (null == strXml || strXml.isEmpty()) {
            return "";
        }

        return strXml.substring(strXml.indexOf('<'));
    }

    /**
     * �p�����[�^�ɃZ�b�g����xml�̃I�u�W�F�N�g�ւ̕ϊ�
     *
     * @param xml            �ϊ�����xml
     * @return               xml����ϊ����ꂽ�I�u�W�F�N�g
     * @throws JAXBException �ϊ��Ɏ��s�����ꍇ
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
     * �p�����[�^�ɃZ�b�g����xml�t�@�C���̃I�u�W�F�N�g�ւ̕ϊ�
     *
     * @param xml             �ϊ�����xml�t�@�C��
     * @return                xml�t�@�C������ϊ����ꂽ�I�u�W�F�N�g
     * @throws JAXBException  �ϊ��Ɏ��s�����ꍇ
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
     * �p�����[�^�ɃZ�b�g�����I�u�W�F�N�g��xml�ւ̕ϊ�
     *
     * @param object         �ϊ����̃I�u�W�F�N�g
     * @param encoding       �g�p����G���R�[�f�B���O
     * @return String        �ϊ����ꂽxml
     * @throws JAXBException �ϊ��Ɏ��s�����ꍇ
     */
    public final String marshallObj(final T object, final String encoding) throws JAXBException {
        StringWriter sw = new StringWriter();

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.marshal(object, sw);

        return sw.getBuffer().toString();
    }
}
