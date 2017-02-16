// Copyright(c) 2003 NCR Japan Ltd.
//
// $Log: CompressionFilter.java,v $
// Revision 1.8  2005/07/29 07:20:11  art
// ���X�|���X�̈��k�����s���Ȃ������^�C�v��ǉ�
//
// Revision 1.7  2003/07/09 08:55:33  art
// ��x�o�b�t�@�����O���Acontent-length��ݒ肷��悤�ɕύX�����B
//
// Revision 1.6  2003/05/22 09:07:07  art
// ���_�C���N�g�̂悤�ɏo�͂�����Ȃ��ꍇ�ɖ���������Stream���N���[�Y���悤�Ƃ���
// ��O�ƂȂ�o�O���C���B
//
// Revision 1.5  2003/05/22 05:17:24  art
// ���k�����łȂ��A�L���@�\���t�B���^�[�ֈړ������B
//
// Revision 1.4  2003/05/19 13:10:00  art
// Encoding��ݒ肷��O�ɁAFlushBuffer���Ăяo���ꂽ�ꍇ�ɑΉ������B
//
// Revision 1.3  2003/05/19 10:41:12  art
// ������N���[�Y���Ăяo����邱�ƂɑΉ�
//
// Revision 1.2  2003/05/13 02:14:26  art
// �f�o�b�O�p�ɍŏ��T�C�Y�����������Ă����̂����ɖ߂����B
//
// Revision 1.1  2003/05/13 01:46:48  art
// ���X�|���X���k�t�B���^
//
// $Id: CompressionFilter.java,v 1.8 2005/07/29 07:20:11 art Exp $
//
package ncr.realgate.servlet.filter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
/**
 * �o�͂ł͎w�肳�ꂽ�T�C�Y�ȏ�̃f�[�^��gzip�܂���deflate�ň��k���A
 * ���͂ł�gzip���ǂ����𔻒肵�����ł���ΐL������t�B���^�[�B<br>
 *
 * ���t�B���^�[�����p����<code>init-param</code>�B
 * <table border="1">
 * <tr><th>���O</th><th>�Ӗ�</th><th>�f�t�H���g</th></tr>
 * <tr><td>threshold</td><td>���k�J�n�T�C�Y</td><td>250</td></tr>
 * <tr><td>type</td><td>�����^�C�v(0:����M,1:��M�̂�)</td><td>0</td></tr>
 * <tr><td>debug</td><td>log�o��</td><td>0</td></tr>
 * </table>
 * @author NCR Japan Ltd.
 * @version $Revision: 1.8 $ $Date: 2005/07/29 07:20:11 $
 */
public class CompressionFilter implements Filter {

    static final String DEFAULT_CHAR_ENCODE = "Windows-31J";

    /**
     * �ŏ����k�J�n�T�C�Y({@value})�B<br>
     * 0�i���k���Ȃ��j�������A���̒l�ȉ��̐ݒ�́A�����I�ɂ����Ŏ������l�ɃG�X�J���[�V��������B
     */
    static final int MINTHRESHOLD = 250;

    /**
     * ����̈��k�J�n�T�C�Y({@value})
     */
    public static final int DEFAULTTHRESHOLD = 250;

    static final int ENC_NONE = 0;
    static final int ENC_DEFLATE = 1;
    static final int ENC_GZIP = 2;

    // �����o�b�t�@�T�C�Y
    static final int INIT_CAPA = 8000;

    private int threshold = DEFAULTTHRESHOLD;
    private static final String KEY_THRESHOLD = "threshold";
    private static final String KEY_TYPE = "type";
    private static final int BOTH_TYPE = 0;
    private static final int REQ_ONLY_TYPE = 1;
    private static final int RESP_ONLY_TYPE = 2;
    private int procType = 0;
    private int debugLevel = 0;
    private static final String KEY_DEBUG = "debug";

    static final byte[] MAGIC;
    static {
        MAGIC = new byte[2];
        MAGIC[0] = 0x1f;
        MAGIC[1] = (byte)0x8b;
    }

    /**
     * �f�o�b�O���O�́AfilterConfig#getServletContext()�ŌĂяo�����R���e�L�X�g��log���\�b�h��
     * ���p���邱�ƁB
     */
    FilterConfig filterConfig;

    private int getKeyValue(FilterConfig cfg, String k, int def) {
        String s = cfg.getInitParameter(k);
        try {
            return (s == null) ? def : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * �t�B���^�[��ݒu����B<br>
     * ���k���邩���Ȃ����̔��f���s��threshold�p�����[�^�̒l���擾����B
     * ���ݒ莞�̃f�t�H���g��{@link #DEFAULTTHRESHOLD}�o�C�g�ł���B
     *
     * @param filterConfig �t�B���^�[�\���I�u�W�F�N�g�B
     */
    @Override
    public void init(FilterConfig initFilterConfig) throws ServletException {
        if (initFilterConfig != null) {
            filterConfig = initFilterConfig;
            threshold = getKeyValue(filterConfig, KEY_THRESHOLD, DEFAULTTHRESHOLD);
            if (threshold != 0 && threshold < MINTHRESHOLD) {
                threshold = MINTHRESHOLD;
            }
            procType = getKeyValue(filterConfig, KEY_TYPE, BOTH_TYPE);
            if (procType != BOTH_TYPE && procType != REQ_ONLY_TYPE) {
                throw new ServletException("bad type configuration:" + procType);
            }
            debugLevel = getKeyValue(filterConfig, KEY_DEBUG, 0);
        }
    }

    /**
     * �t�B���^�[��j������B�������ł͉����s��Ȃ��B
     */
    @Override
    public void destroy() {
    }

    /**
     * Requeset��Accept-Encoding���^�f�[�^���`�F�b�N���Agzip�܂���deflate��
     * �N���C�A���g���T�|�[�g���Ă���΁A���k���ĕԑ�����B<br>
     * ��������M�X�g���[���ɂ��Ă͐擪2�o�C�g���Agzip�̃}�W�b�N�i���o�[(\037\213)�ɓ�������
     * �`�F�b�N����K�v�����邽�߁A�K���t�B���^�����O����B
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response,
                          FilterChain chain ) throws IOException, ServletException {
        ServletRequest req = null;
        if (request instanceof HttpServletRequest) {
            req = new UncompressionRequestWrapper((HttpServletRequest)request);
            String s = request.getCharacterEncoding();
            // ISO-8859-1 ���w�肳��Ă��邱�Ƃ́A�����_�ł͍l���ɂ����̂ł����Őݒ肵�Ă����B
            if (s == null || s.equals("ISO-8859-1")) {
                request.setCharacterEncoding(DEFAULT_CHAR_ENCODE);
            }
        } else {
            req = request;
        }
        if (threshold == 0 || procType == REQ_ONLY_TYPE) {
            chain.doFilter(req, response);
            return;
        }
        int encType = getEncodingType(request);
        if (encType == ENC_NONE) {
            chain.doFilter(req, response);
        } else {
            if (response instanceof HttpServletResponse) {
                CompressionResponseWrapper newResponse =
                        new CompressionResponseWrapper((HttpServletResponse)response, encType);
                newResponse.threshold = threshold;
                try {
                    chain.doFilter(req, newResponse);
                } finally {
                    newResponse.finishResponse();
                }
            }
        }
    }

    int getEncodingType(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            Enumeration e = ((HttpServletRequest)request).getHeaders("Accept-Encoding");
            while (e.hasMoreElements()) {
                String name = ((String)e.nextElement()).toLowerCase();
                if (name.indexOf("gzip") >= 0) {
                    return ENC_GZIP;
                } else if (name.indexOf("deflate") >= 0) {
                    return ENC_DEFLATE;
                }
            }
        }
        return ENC_NONE;
    }

    /**
     * gzip���ǂ����̔���@�\�������̓X�g���[����ԑ�����B
     */
    class UncompressionRequestWrapper extends HttpServletRequestWrapper {
        HttpServletRequest origRequest;
        ServletInputStream stream = null;
        BufferedReader reader = null;
        private UncompressionInputStream rootStream;

        UncompressionRequestWrapper(HttpServletRequest req) {
            super(req);
            origRequest = req;
        }

        public ServletInputStream getInputStream() throws IOException {
            if (stream != null) {
                return stream;
            }
            if (reader != null) {
                throw new IllegalStateException("getReader() already been called");
            }
            stream = createInputStream();
            return stream;
        }

        public BufferedReader getReader() throws IOException {
            if (reader != null) {
                return reader;
            }
            if (stream != null) {
                throw new IllegalStateException("getInputStream() already been called");
            }
            String charEnc = origRequest.getCharacterEncoding();
            if (debugLevel > 0) {
                ServletContext cx = filterConfig.getServletContext();
                cx.log("CompressionFilter: input encoding=" + charEnc);
            }
            if (charEnc == null) {
                charEnc = DEFAULT_CHAR_ENCODE;
            }
            createInputStream();
            reader = new BufferedReader(new InputStreamReader(rootStream, charEnc));
            return reader;
        }

        UncompressionInputStream createInputStream() throws IOException {
            rootStream = new UncompressionInputStream();
            return rootStream;
        }

        /**
         * �擪2�o�C�g��gzip�̃}�W�b�N�i���o�[�Ɉ�v���邩�ǂ����𔻒肵�A��v�����
         * GZIP�L�����s���X�g���[���B
         */
        class UncompressionInputStream extends ServletInputStream {
            int second;
            InputStream origStream;
            InputStream stream;
            boolean closed;

            UncompressionInputStream() throws IOException {
                origStream = origRequest.getInputStream();
                // -1�Ȃ�close�A0�ȏ�Ȃ�L���f�[�^�Ȃ̂ŁA�����l�Ͱ1�����ɂ���B
                second = -128;
            }

            @Override
            public boolean isFinished() {
                return closed;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new NotImplementedException();
            }

            @Override
            public void close() throws IOException {
                if (stream != null) {
                    stream.close();
                } else if (origStream != null) {
                    origStream.close();
                }
                closed = true;
            }

            @Override
            public int read() throws IOException {
                if (closed) {
                    return -1;
                }
                if (stream == null) {
                    if (second >= -1) {
                        // 2nd byte alread read.
                        stream = origStream;
                        return second;
                    }
                    int n = origStream.read();
                    if (n < 0) {
                        closed = true;
                        return -1;
                    } else if ((byte)n != MAGIC[0]) {
                        stream = origStream;
                        return n;
                    }
                    second = origStream.read();
                    if ((byte)second != MAGIC[1]) {
                        return n;
                    }
                    // GZIP stream
                    stream = new GZIPInputStream(new FilterInputStream(origStream) {
                        int count = 0;
                        public int read() throws IOException {
                            if (count < 2) {
                                return (MAGIC[count++] & 0xff);
                            }
                            return super.read();
                        }
                        public int read(byte[] b, int off, int len)
                                throws IOException {
                            if (count < 2) {
                                int r = 0;
                                for (int i = 0; count < MAGIC.length && i < len;
                                     i++, r++) {
                                    b[off + i] = MAGIC[count++];
                                }
                                return r;
                            }
                            return super.read(b, off, len);
                        }
                    });
                }
                return stream.read();
            }

            @Override
            public int read(byte[] b) throws IOException {
                return read(b, 0, b.length);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (closed) {
                    return -1;
                }
                if (stream == null) {
                    int n = read();
                    if (n < 0) {
                        return n;
                    }
                    b[off] = (byte)n;
                    return 1;
                }
                return stream.read(b, off, len);
            }
        }
    }

    class CompressionResponseWrapper
            extends HttpServletResponseWrapper {
        int encType;
        int threshold;
        HttpServletResponse origResponse;
        ServletOutputStream stream = null;
        PrintWriter writer = null;
        private CompressionOutputStream rootStream;
        boolean flushRequired;

        CompressionResponseWrapper(HttpServletResponse resp, int initEncType) {
            super(resp);
            origResponse = resp;
            encType = initEncType;
            flushRequired = false;
        }
        @Override
        public void flushBuffer() throws IOException {
            if (rootStream != null && rootStream.buffered) {
                flushRequired = true;
            } else {
                super.flushBuffer();
            }
        }

        void delayedflush() throws IOException {
            if (flushRequired) {
                super.flushBuffer();
                flushRequired = false;
            }
        }

        void finishResponse() {
            if (rootStream != null && !rootStream.closed) {
                try {
                    if (writer != null) {
                        writer.close();
                    } else {
                        if (stream != null)
                            stream.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        /**
         * ��������B
         */
        @Override
        public void setContentLength(int length) {
        }

        private void forceContentLength(int length) {
            super.setContentLength(length);
        }

        ServletOutputStream createOutputStream() throws IOException {
            rootStream = new CompressionOutputStream();
            return rootStream;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {

            if (writer != null)
                throw new IllegalStateException("getWriter() has already been called for this response");

            if (stream == null) {
                stream = createOutputStream();
            }
            return stream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {

            if (writer != null)
                return writer;

            if (stream != null)
                throw new IllegalStateException("getOutputStream() has already been called for this response");

            createOutputStream();
            String charEnc = origResponse.getCharacterEncoding();
            // ISO-8859-1 ���w�肳��Ă��邱�Ƃ́A�����_�ł͍l���ɂ���
            if (debugLevel > 0) {
                ServletContext cx = filterConfig.getServletContext();
                cx.log("CompressionFilter: output encoding=" + charEnc);
            }
            if (charEnc == null || charEnc.equals("ISO-8859-1")) {
                charEnc = DEFAULT_CHAR_ENCODE;
            }
            writer = new PrintWriter(new OutputStreamWriter(rootStream, charEnc));

            return writer;
        }

        class CompressionOutputStream extends ServletOutputStream {
            byte[] buff;
            int buffSize;
            boolean closed;
            boolean buffered;
            OutputStream stream;
            ByteArrayOutputStream byteStream;

            CompressionOutputStream() {
                buff = new byte[threshold];
                buffSize = 0;
                stream = null;
                closed = false;
                buffered = true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                throw new NotImplementedException();
            }

            @Override
            public void close() throws IOException {
                buffered = false;
                closed = true;
                ServletOutputStream o = origResponse.getOutputStream();
                if (stream != null) {
                    stream.close();
                    buffSize = byteStream.size();
                    buff = byteStream.toByteArray();
                    stream = null;
                }
                if (buffSize > 0) {
                    forceContentLength(buffSize);
                    o.write(buff, 0, buffSize);
                }
                o.close();
            }

            @Override
            public void flush() throws IOException {
                if (stream != null) {
                    stream.flush();
                }
            }

            @Override
            public void write(int b) throws IOException {
                if (stream != null) {
                    stream.write(b);
                } else {
                    buff[buffSize++] = (byte)b;
                    if (buffSize >= buff.length) {
                        createStream();
                        stream.write(buff, 0, buffSize);
                    }
                }
            }

            @Override
            public void write(byte b[]) throws IOException {
                write(b, 0, b.length);
            }

            @Override
            public void write(byte b[], int off, int len) throws IOException {
                if (stream != null) {
                    stream.write(b, off, len);
                } else {
                    if (len < (buff.length - buffSize)) {
                        System.arraycopy(b, off, buff, buffSize, len);
                        buffSize += len;
                    } else {
                        createStream();
                        if (buffSize > 0) {
                            stream.write(buff, 0, buffSize);
                        }
                        stream.write(b, off, len);
                    }
                }
            }

            void createStream() throws IOException {
                byteStream = new ByteArrayOutputStream(INIT_CAPA);
                if (encType == ENC_DEFLATE) {
                    origResponse.setHeader("Content-Encoding", "deflate");
                    Deflater dfl = new Deflater(
                            Deflater.DEFAULT_COMPRESSION, true);
                    stream = new DeflaterOutputStream(byteStream, dfl);
                } else if (encType == ENC_GZIP) {
                    origResponse.setHeader("Content-Encoding", "gzip");
                    stream = new GZIPOutputStream(byteStream);
                } else {
                    assert false;
                }
            }
        }
    }
}
