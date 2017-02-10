/*
 * Copyright 2002,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Log: HexDump.java,v $
 * Revision 1.4  2005/09/15 03:20:00  art
 * Writer�֏o�͂���悤�ɏC���B
 * �}���`�X���b�h�ŗ��p�\�Ȃ悤�Ƀo�b�t�@���X���b�h�P�ʂɃA�T�C������悤�ɏC���B
 *
 * Revision 1.3  2005/03/16 02:28:00  kudo
 * �����W�w�莞�̕s��C��
 *
 * Revision 1.2  2005/03/03 04:52:17  kudo
 * �f�o�b�O�Ăяo���Ɏg�p���ɂ����̂ŁAString��ԑ����郁�\�b�h��ǉ�����
 *
 * Revision 1.1  2005/03/03 02:11:31  kudo
 * ���\�[�X�Forg.apache.commons.io.HexDump.java (Ver 1.0)
 * ���̂܂܎g�p�����jar�̔z���Ȃǂ��ʓ|�Ȃ̂ŁAncr��util�p�b�P�[�W�ɔq�؂����B
 *
 */
package ncr.realgate.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Writer;

/**
 * Dumps data in hexadecimal format.
 *
 * Derived from a HexDump utility I wrote in June 2001.
 *
 * Taken from the POI project.
 *
 * @author Scott Sanders (sanders at apache dot org)
 * @author Marc Johnson
 * @version Revision: 1.8  Date: 2004/02/23 04:35:59
 */
public class HexDump {

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public HexDump() { }

    /**
     * dump an array of bytes to an OutputStream
     *
     * @param data the byte array to be dumped
     * @param offset its offset, whatever that might mean
     * @param stream the OutputStream to which the data is to be
     *               written
     * @param index initial index into the byte array
     *
     * @exception IOException is thrown if anything goes wrong writing
     *            the data to stream
     * @exception ArrayIndexOutOfBoundsException if the index is
     *            outside the data array's bounds
     * @exception IllegalArgumentException if the output stream is
     *            null
     */

    public static void dump(byte[] data, long offset,
                            OutputStream stream, int index)
        throws IOException, ArrayIndexOutOfBoundsException {
        OutputStreamWriter os = new OutputStreamWriter(stream);
        dump(data, offset, os, index);
        os.flush();
    }

    /**
     * dump an array of bytes to an OutputStream
     *
     * @param data the byte array to be dumped
     * @param offset its offset, whatever that might mean
     * @param writer  the Writer to which the data is to be written
     * @param index initial index into the byte array
     *
     * @exception IOException is thrown if anything goes wrong writing
     *            the data to stream
     * @exception ArrayIndexOutOfBoundsException if the index is
     *            outside the data array's bounds
     * @exception IllegalArgumentException if the output stream is
     *            null
     */

    public static void dump(byte[] data, long offset,
                            Writer writer, int index)
            throws IOException, ArrayIndexOutOfBoundsException,
            IllegalArgumentException {
        if ((index < 0) || (index >= data.length)) {
            throw new ArrayIndexOutOfBoundsException(
                    "illegal index: " + index + " into array of length "
                    + data.length);
        }
        if (writer == null) {
            throw new IllegalArgumentException("cannot write to nullwriter");
        }
        long display_offset = offset + index;

        for (int j = index; j < data.length; j += 16) {
            int chars_read = data.length - j;

            if (chars_read > 16) {
                chars_read = 16;
            }
            writer.write(dump(display_offset));
            writer.write(' ');
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    writer.write(dump(data[k + j]));
                } else {
                    writer.write("  ");
                }
                writer.write(' ');
            }
            for (int k = 0; k < chars_read; k++) {
                if ((data[k + j] >= ' ') && (data[k + j] < 127)) {
                    writer.write((char) data[k + j]);
                } else {
                    writer.write('.');
                }
            }
            writer.write(EOL);
            display_offset += chars_read;
        }
    }

    /** line-separator (initializes to "line.separator" system property. */
    public static final String EOL =
            System.getProperty("line.separator");
    private static final ThreadLocal _lbuffer = new ThreadLocal() {
            protected Object initialValue() {
                return new char[8];
            }
        };
    private static final ThreadLocal _cbuffer = new ThreadLocal() {
            protected Object initialValue() {
                return new char[2];
            }
        };
    private static final char _hexcodes[] =
            {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F'
            };
    private static final int _shifts[] =
            {
                28, 24, 20, 16, 12, 8, 4, 0
            };

    private static char[] dump(long value) {
        char[] lbuffer = (char[])_lbuffer.get();
        for (int j = 0; j < 8; j++) {
            lbuffer[j] = _hexcodes[((int) (value >> _shifts[j])) & 15];
        }
        return lbuffer;
    }

    private static char[] dump(byte value) {
        char[] cbuffer = (char[])_cbuffer.get();
        for (int j = 0; j < 2; j++) {
            cbuffer[j] = _hexcodes[(value >> _shifts[j + 6]) & 15];
        }
        return cbuffer;
    }

    /**
     * �w�肳�ꂽ�o�C�g�z���16�i�_���v�̕�����Ƃ��ĕԑ�����B
     *
     * @param data    16�i�_���v�����o�C�g�z��
     * @param caption �_���v�ɑ΂������
     */
    public static String dump(byte[] data, String caption) {
	return dump(data, caption, 0, data.length);
    }

    /**
     * �w�肳�ꂽ�o�C�g�z���16�i�_���v�Ƃ��ė^����ꂽ���C�^�[�֏o�͂���B
     *
     * @param data    16�i�_���v�����o�C�g�z��
     * @param caption �_���v�ɑ΂������
     * @param wtr �o�͐惉�C�^�[
     */
    public static void dump(byte[] data, String caption, Writer wtr) {
	dump(data, caption, 0, data.length, wtr);
    }

    /**
     * �w�肳�ꂽ�o�C�g�z���16�i�_���v�̕�����Ƃ��ĕԑ�����B
     * �_���v�J�n�ʒu�A�����O�X�̎w�肪�\�B
     *
     * @param data    16�i�_���v�����o�C�g�z��
     * @param caption �_���v�ɑ΂������
     * @param start   16�i�_���v������o�C�g�z��̊J�n�ʒu
     * @param length  16�i�_���v������o�C�g�z��̒���
     */
    public static String dump(byte[] data, String caption,
    					int start, int length) {
        CharArrayWriter caw = new CharArrayWriter();
        dump(data, caption, start, length, caw);
        caw.close();
        return caw.toString();
    }

    /**
     * �w�肳�ꂽ�o�C�g�z���16�i�_���v�Ƃ��ė^����ꂽ���C�^�[�֏o�͂���B
     * �_���v�J�n�ʒu�A�����O�X�̎w�肪�\�B
     *
     * @param data    16�i�_���v�����o�C�g�z��
     * @param caption �_���v�ɑ΂������
     * @param start   16�i�_���v������o�C�g�z��̊J�n�ʒu
     * @param length  16�i�_���v������o�C�g�z��̒���
     * @param wtr �o�͐惉�C�^�[
     */
    public static void dump(byte[] data, String caption,
    					int start, int length, Writer wtr) {
	ByteArrayOutputStream ba = new ByteArrayOutputStream();
	String hdr = EOL + "[" + caption + "] [" + length + " byte]" + EOL;
	try {
            wtr.write(hdr);
	    if ((start != 0) || (length != data.length)) {
		ByteArrayOutputStream baw = new ByteArrayOutputStream();
		baw.write(data, start, length <= data.length - start ? length : data.length - start);
		dump(baw.toByteArray(), 0, wtr, 0);
	    } else {
		dump(data, 0, wtr, 0);
	    }
	} catch (Exception e) {
            try {
                wtr.write(e.getMessage());
                wtr.write(EOL);
            } catch (IOException ioe) {
                // ���̎��_�ł̗�O�͖�������
            }
	}
    }
}
