/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package ncr.res.mobilepos.intaPay.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;

@SuppressWarnings("deprecation")
public class ClientCustomSSLUtil {
    private static final Logger LOGGER = Logger.getInstance();
    private static final String PROG_NAME = "intaPay";

    public static String sendGetRequest(String url, String token) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        StringBuffer sb = new StringBuffer();

        try {
            if(!Util.isEmptyString(token)){
                httpGet.addHeader("TOKEN", token);
            }
            httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), IntaPayConstants.ENCODE_UTF8));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "ClientCustomSSLUtil", Logger.RES_EXCEP_GENERAL, "FileInputStream Exception:" + errors.toString()); 
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch (Exception e) {
            }
        }

        return sb.toString();
    }

    public static String sendGetRequestWithCertificationFile(String url, String pw, String filePath) {
        StringBuffer sb = new StringBuffer();
        HttpGet httpGet = new HttpGet(url);
        KeyStore keyStore = null;
        InputStream instream = null;
        SSLContext sslcontext = null;
        SSLContextBuilder sslContextBuilder = null;
        SSLConnectionSocketFactory sslsf = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            instream = new FileInputStream(filePath);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        boolean ignoreSSL = true;

        try {
            sslContextBuilder = SSLContexts.custom();
            if (ignoreSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                sslContextBuilder.loadTrustMaterial(trustStore, new AnyTrustStrategy());
            }
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(instream, pw.toCharArray());
            sslcontext = sslContextBuilder.loadKeyMaterial(keyStore, pw.toCharArray()).build();
            sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), IntaPayConstants.ENCODE_UTF8));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "sendGetRequestWithCertificationFile", Logger.RES_EXCEP_GENERAL, "FileInputStream Exception:" + errors.toString());            
        } finally {
            try {
                instream.close();
                response.close();
                httpclient.close();
            } catch (Exception e) {
            }
        }

        return sb.toString();
    }
    
    public static String sendRequestWithCertificationFile(String requestXml, String url, String pw, String filePath) {
        String functionName = "sendRequestWithCertificationFile";
    	RequestConfig requestConfig = null;
        StringBuffer sb = new StringBuffer();
        HttpPost httpost = getPostMethod(url, "application/json");
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(180000).build();
        httpost.setConfig(requestConfig);
        StringEntity se = new StringEntity(requestXml, IntaPayConstants.ENCODE_UTF8);
        // se.setContentType("text/json");
        httpost.setEntity(se);

        KeyStore keyStore = null;
        InputStream instream = null;
        SSLContext sslcontext = null;
        SSLContextBuilder sslContextBuilder = null;
        SSLConnectionSocketFactory sslsf = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        boolean ignoreSSL = true;
                
        try {
            instream = new FileInputStream(filePath);
        } catch (Exception e1) {
            StringWriter errors = new StringWriter();
            e1.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL, "FileInputStream Exception:" + errors.toString());
        }

        try {
            sslContextBuilder = SSLContexts.custom();
            if (ignoreSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                sslContextBuilder.loadTrustMaterial(trustStore, new AnyTrustStrategy());
            }
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(instream, pw.toCharArray());
            sslContextBuilder.loadKeyMaterial(keyStore, pw.toCharArray());
            sslcontext = sslContextBuilder.build();
            sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
                public boolean retryRequest(
                        IOException exception,
                        int executionCount,
                        HttpContext context) {
                	
                    LOGGER.logNormal(PROG_NAME, functionName, "retry intapay:" + url + " " + requestXml);
                    return false;
                }
            };
            httpclient = HttpClients.custom().setRetryHandler(myRetryHandler).build();                        
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            LOGGER.logNormal(PROG_NAME, functionName, "request intapay:" + url + " " + requestXml);
            response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), IntaPayConstants.ENCODE_UTF8));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
            }
            else
            {
            	LOGGER.logError(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL, "response entity is null");
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL, "Exception:" + errors.toString());
        } finally {
            try {
                instream.close();
                response.close();
                httpclient.close();
            } catch (Exception e) {
            }
        }

        return sb.toString();
    }

    public static String sendRequest(String requestXml, String url) {
        String jsonStr = "";
        try {
            HttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            HttpPost httpost = getPostMethod(url, null);
            httpost.setConfig(requestConfig);
            StringEntity se = new StringEntity(requestXml, IntaPayConstants.ENCODE_UTF8);
            se.setContentType("text/json");
            httpost.setEntity(se);
            HttpResponse response = client.execute(httpost);
            jsonStr = EntityUtils.toString(response.getEntity(), IntaPayConstants.ENCODE_UTF8);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "sendRequest", Logger.RES_EXCEP_GENERAL, "Exception:" + errors.toString());
        }
        return jsonStr;
    }

    public static String sendRequestJson(String requestXml, String url) {
        String jsonStr = "";
        try {
            HttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            HttpPost httpost = getPostMethod(url, "application/json; charset=UTF-8");
            httpost.setConfig(requestConfig);
            StringEntity se = new StringEntity(requestXml, IntaPayConstants.ENCODE_UTF8);
            httpost.setEntity(se);
            HttpResponse response = client.execute(httpost);
            jsonStr = EntityUtils.toString(response.getEntity(), IntaPayConstants.ENCODE_UTF8);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "sendRequestJson", Logger.RES_EXCEP_GENERAL, "Exception:" + errors.toString());
        }
        return jsonStr;
    }

    public static String sendRequestJsonForWechatPayManagementTest(String requestXml, String url, String token, String deviceId) {
        String jsonStr = "";
        try {
            HttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            HttpPost httpost = getPostMethod(url, "application/json; charset=UTF-8");
            httpost.addHeader("TOKEN", token);
            httpost.addHeader("DEVICE_ID", deviceId);
            httpost.setConfig(requestConfig);
            StringEntity se = new StringEntity(requestXml, IntaPayConstants.ENCODE_UTF8);
            httpost.setEntity(se);
            HttpResponse response = client.execute(httpost);
            jsonStr = EntityUtils.toString(response.getEntity(), IntaPayConstants.ENCODE_UTF8);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "sendRequestJsonForWechatPayManagementTest", Logger.RES_EXCEP_GENERAL, "Exception:" + errors.toString());
        }
        return jsonStr;
    }

    public static String sendRequestServerToServer(Map<String, String> requestMap, String url) {
        String jsonStr = "";
        try {
            HttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            HttpPost httpost = getPostMethod(url, null);
            httpost.setConfig(requestConfig);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            NameValuePair n;
            for (Entry<String, String> e : requestMap.entrySet()) {
                n = new BasicNameValuePair(e.getKey(), e.getValue());
                list.add(n);
            }
            UrlEncodedFormEntity se = new UrlEncodedFormEntity(list, IntaPayConstants.ENCODE_UTF8);
            httpost.setEntity(se);
            HttpResponse response = client.execute(httpost);
            jsonStr = EntityUtils.toString(response.getEntity(), IntaPayConstants.ENCODE_UTF8);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOGGER.logError(PROG_NAME, "sendRequestServerToServer", Logger.RES_EXCEP_GENERAL, "Exception:" + errors.toString());
        }
        return jsonStr;
    }

    public static String postAndUpload(String urlAddress, byte[] file, String fileName, boolean ignoreSsl) throws Exception {
        URL url = new URL(urlAddress);
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            if (ignoreSsl) {
                ignoreSsl();
            }
        }

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        URLConnection u = url.openConnection();
        u.setDoInput(true);
        u.setDoOutput(true);
        u.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        DataOutputStream request = new DataOutputStream(u.getOutputStream());
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"" + crlf);
        request.writeBytes(crlf);
        request.write(file);
        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();
        u.getOutputStream();
        return IOUtils.toString(u.getInputStream(), "UTF-8");
    }

    /**
     * 
     * 
     * @throws Exception
     */
    private static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    }

    static class AnyTrustStrategy implements TrustStrategy {

        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }

    private static HttpPost getPostMethod(String url, String contentType) {
        String host = getHost(url);
        HttpPost pmethod = new HttpPost(url);
        pmethod.addHeader("Connection", "keep-alive");
        pmethod.addHeader("Accept", "*/*");
        if (!Util.isEmptyString(contentType)) {
            pmethod.addHeader("Content-Type", contentType);
        } else {
            pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
        pmethod.addHeader("Host", host);
        pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
        pmethod.addHeader("Cache-Control", "max-age=0");
        pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        return pmethod;
    }

    public static String getHost(String url) {
        String host = "";
        if (!Util.isEmptyString(url)) {
            if (url.indexOf("://") > 0) {
                url = url.substring(url.indexOf("://") + "://".length());
                host = url.substring(0, url.indexOf("/"));
            }
        }
        return host;
    }
}
