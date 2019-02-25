package ncr.res.mobilepos.ej.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import atg.taglib.json.util.JSONObject;
import ncr.res.mobilepos.constant.GlobalConstant;
import atg.taglib.json.util.JSONException;

public class UrlConnectionHelper {
	
	/**
	 * connection the remote server
	 * @param address The Remote server address
	 * @param timeOut
	 * @throws  NoSuchAlgorithmException  The Exception of Algorithm
	 * @throws KeyManagementException The Exception of the  KeyManagement
	 * @throws IOException The Exception of IO
	 * @throws JSONException The Exception Of JSON
	 * @return JSONObject  The response Object to JSON
	 */
    public static JSONObject connectionHttpsForGet(String address , int timeOut)
            throws NoSuchAlgorithmException, KeyManagementException, IOException, JSONException {
        JSONObject result = null;

        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {     
                public X509Certificate[] getAcceptedIssuers() { 
                    return new X509Certificate[0];
                } 
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                } 
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } 
        };
        URL url = new URL(address);
        
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        
        String userpass = GlobalConstant.getAuthenticationUid().trim() + ":" + GlobalConstant.getAuthenticationPassword().trim();
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpass.getBytes());
        conn.setRequestProperty ("Authorization", basicAuth);
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setConnectTimeout(timeOut * 1000);
        conn.setReadTimeout(timeOut * 1000);
        conn.setAllowUserInteraction(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();
        
        int intConnectionStatus = conn.getResponseCode();
        if (intConnectionStatus == 200) {
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder strbReturn = new StringBuilder();
            String strReadLine = "";
            while ((strReadLine = br.readLine()) != null) {
                strbReturn.append(strReadLine.trim());
            }
            result = new JSONObject(strbReturn.toString());
        }
        return result;
    }
}
