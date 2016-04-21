package ncr.res.mobilepos.searchapi.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import atg.taglib.json.util.JSONObject;
import atg.taglib.json.util.JSONException;

public class UrlConnectionHelper {

	public static JSONObject connectionForGet(String address)
			throws MalformedURLException, IOException, JSONException {
		JSONObject result = null;

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
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
	
	/**
	 * connection the remote server
	 * @param address The Remote server address
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

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        URL url = new URL(address);
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeOut * 1000);
        conn.setReadTimeout(timeOut * 1000);
        conn.setAllowUserInteraction(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "*/*");
//        conn.setRequestProperty("connection", "Keep-Alive");
//        conn.setRequestProperty("Charset", "UTF-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);
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
