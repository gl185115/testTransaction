// Copyright(c) 2003 NCR Japan Ltd.
//
// $Log: CompressionFilter.java,v $
// Revision 1.8  2005/07/29 07:20:11  art
// レスポンスの圧縮を実行しない処理タイプを追加
//
// Revision 1.7  2003/07/09 08:55:33  art
// 一度バッファリングし、content-lengthを設定するように変更した。
//
// Revision 1.6  2003/05/22 09:07:07  art
// リダイレクトのように出力がされない場合に未初期化のStreamをクローズしようとして
// 例外となるバグを修正。
//
// Revision 1.5  2003/05/22 05:17:24  art
// 圧縮だけでなく、伸張機能もフィルターへ移動した。
//
// Revision 1.4  2003/05/19 13:10:00  art
// Encodingを設定する前に、FlushBufferを呼び出された場合に対応した。
//
// Revision 1.3  2003/05/19 10:41:12  art
// 複数回クローズを呼び出されることに対応
//
// Revision 1.2  2003/05/13 02:14:26  art
// デバッグ用に最小サイズを小さくしていたのを元に戻した。
//
// Revision 1.1  2003/05/13 01:46:48  art
// レスポンス圧縮フィルタ
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
 * 出力では指定されたサイズ以上のデータをgzipまたはdeflateで圧縮し、
 * 入力ではgzipかどうかを判定しそうであれば伸張するフィルター。<br>
 *
 * 当フィルターが利用する<code>init-param</code>。
 * <table border="1">
 * <tr><th>名前</th><th>意味</th><th>デフォルト</th></tr>
 * <tr><td>threshold</td><td>圧縮開始サイズ</td><td>250</td></tr>
 * <tr><td>type</td><td>処理タイプ(0:送受信,1:受信のみ)</td><td>0</td></tr>
 * <tr><td>debug</td><td>log出力</td><td>0</td></tr>
 * </table>
 * @author NCR Japan Ltd.
 * @version $Revision: 1.8 $ $Date: 2005/07/29 07:20:11 $
 */
public class CompressionFilter implements Filter {

    static final String DEFAULT_CHAR_ENCODE = "Windows-31J";

    /**
     * 最小圧縮開始サイズ({@value})。<br>
     * 0（圧縮しない）を除く、この値以下の設定は、自動的にここで示した値にエスカレーションする。
     */
    static final int MINTHRESHOLD = 250;

    /**
     * 既定の圧縮開始サイズ({@value})
     */
    public static final int DEFAULTTHRESHOLD = 250;

    static final int ENC_NONE = 0;
    static final int ENC_DEFLATE = 1;
    static final int ENC_GZIP = 2;

    // 初期バッファサイズ
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
     * デバッグログは、filterConfig#getServletContext()で呼び出したコンテキストのlogメソッドを
     * 利用すること。
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
     * フィルターを設置する。<br>
     * 圧縮するかしないかの判断を行うthresholdパラメータの値を取得する。
     * 未設定時のデフォルトは{@link #DEFAULTTHRESHOLD}バイトである。
     *
     * @param filterConfig フィルター構成オブジェクト。
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
     * フィルターを破棄する。当実装では何も行わない。
     */
    @Override
    public void destroy() {
    }

    /**
     * RequesetのAccept-Encodingメタデータをチェックし、gzipまたはdeflateを
     * クライアントがサポートしていれば、圧縮して返送する。<br>
     * ただし受信ストリームについては先頭2バイトが、gzipのマジックナンバー(\037\213)に等しいか
     * チェックする必要があるため、必ずフィルタリングする。
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response,
                          FilterChain chain ) throws IOException, ServletException {
        ServletRequest req = null;
        if (request instanceof HttpServletRequest) {
            req = new UncompressionRequestWrapper((HttpServletRequest)request);
            String s = request.getCharacterEncoding();
            // ISO-8859-1 が指定されていることは、現時点では考えにくいのでここで設定しておく。
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
     * gzipかどうかの判定機能を持つ入力ストリームを返送する。
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
         * 先頭2バイトがgzipのマジックナンバーに一致するかどうかを判定し、一致すれば
         * GZIP伸張を行うストリーム。
         */
        class UncompressionInputStream extends ServletInputStream {
            int second;
            InputStream origStream;
            InputStream stream;
            boolean closed;

            UncompressionInputStream() throws IOException {
                origStream = origRequest.getInputStream();
                // -1ならclose、0以上なら有効データなので、初期値はｰ1未満にする。
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
         * 無視する。
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
            // ISO-8859-1 が指定されていることは、現時点では考えにくい
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
