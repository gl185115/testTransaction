// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Guid.java,v $
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Guid.java,v 1.1 2002/12/25 01:57:37 kudo Exp $
//
package ncr.realgate.util;

/**
 * GUID(Global Unique IDentity)�܂���uuid(Universion Unique Identity)��ϊ��^�쐬���郆�[�e�B���e�B�B
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.1 $ $Date: 2002/12/25 01:57:37 $
 */
public final class Guid {

    /**
     * GUID(uuid)�̃o�C�g��
     */
    public static final int LENGTH = 16;

    private Guid() {
    }

    /** 
     * uuid�������16�o�C�g�̔z��ɕϊ�����B
     * @param uuid {xxxxxxxx-xx-..}�`����GUID
     * @return 16�o�C�g�Œ蒷�o�C�g�z��ɂ��GUID
     * @throws NullPointerException uuid�p�����[�^��null
     * @throws IllegalArgumentException uuid�p�����[�^�������ȕ�����\���ł͂Ȃ��B
     */
    public static byte[] toBytes(CharSequence uuid) {
	if (uuid == null) {
	    throw new NullPointerException("Guid#toBytes");
	}
	StringBuffer sb = new StringBuffer(32);
	for (int i = 0; i < uuid.length(); i++) {
	    char ch = uuid.charAt(i);
	    if (ch != '-' && ch != '{' && ch != '}')
		sb.append(ch);
	}
	if (sb.length() != 32) {
	    throw new IllegalArgumentException("Bad GUID:" + uuid);
	}
	Nibble.changeEndian(sb, 0, 6);
	Nibble.changeEndian(sb, 2, 4);
	Nibble.changeEndian(sb, 8, 10);
	Nibble.changeEndian(sb, 12, 14);
	byte[] buuid = new byte[16];
	for (int i = 0; i < 16; i++) {
	    buuid[i] = (byte)Integer.parseInt(sb.substring(i * 2, i * 2 + 2), 16);
	}
	return buuid;
    }

    /**
     * uuid���ǉ��t�H�[�}�b�g�ɕϊ�����B
     *
     * @param uuid ����uuid���܂ރo�C�g�z��
     * @param offset uuid�̊J�n�I�t�Z�b�g
     * @return {xxxx...} �`���̕�����
     * @throws IndexOutOfBoundsException uuid�p�����[�^��offset�p�����[�^�Ŏw�肳�ꂽ�ʒu�ȍ~��16�o�C�g�ɖ����Ȃ��B
     * @throws NullPointerException uuid�p�����[�^��null
     */
    public static String toString(byte[] uuid, int offset) {
	if (uuid == null) {
	    throw new NullPointerException("Guid#toString");
	} else if (offset < 0 || (uuid.length - offset) < 16) {
	    throw new IndexOutOfBoundsException("Guid#toString length=" + 
					       uuid.length + ", offset=" +
					       offset);
	}
	StringBuffer sb = new StringBuffer(64);
	sb.append('{');
	sb.append(Nibble.toString(uuid, offset, 4));
	Nibble.changeEndian(sb, 1, 7);
	Nibble.changeEndian(sb, 3, 5);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 4, 2));
	Nibble.changeEndian(sb, 10, 12);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 6, 2));
	Nibble.changeEndian(sb, 15, 17);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 8, 2));
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 10, 6));
	sb.append('}');
	return sb.toString();
    }

    /**
     * uuid���ǉ��t�H�[�}�b�g�ɕϊ�����B
     *
     * @param uuid ����uuid
     * @return {xxxx...} �`���̕�����
     * @throws IndexOutOfBoundsException uuid�p�����[�^�̒�����16�o�C�g�ɖ����Ȃ��B
     * @throws NullPointerException uuid�p�����[�^��null
     */
    public static String toString(byte[] uuid) {
	return toString(uuid, 0);
    }

    /**
     * uuid�𐶐�����B
     *
     * @param aid AIWID���w�肷��B
     * @return 16�o�C�g�Œ蒷��uuid
     * @throws NullPointerException id�p�����[�^��null
     */
    public static byte[] create(String aid) {
	if (aid == null) {
	    throw new NullPointerException("Guid#createGuid");
	}
	byte u[] = new byte[16];
	java.util.Arrays.fill(u, (byte)0);
	java.rmi.server.UID uid = new java.rmi.server.UID();
	java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
	try {
	    java.io.DataOutputStream dx = new java.io.DataOutputStream(bo);
	    uid.write(dx);
	    byte[] b = bo.toByteArray();
	    for (int i = 0, n = b.length; i < n && i < 16; i++) {
		u[i] = b[i];
	    }
	    dx.close();
	} catch (java.io.IOException e) {
	    e.printStackTrace();
	}
	byte[] id = Nibble.toBytes(aid);
	for (int i = 0, n = id.length; i < n && i < 2; i++) {
	    u[14 + i] = id[i];
	}
	return u;
    }
}

