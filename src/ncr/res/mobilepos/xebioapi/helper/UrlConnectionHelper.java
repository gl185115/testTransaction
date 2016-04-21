package ncr.res.mobilepos.xebioapi.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.res.mobilepos.helper.StringUtility;

public class UrlConnectionHelper {

	public static JSONObject connectionForGet(String address, int timeOut)
			throws MalformedURLException, IOException, JSONException {
		JSONObject result = null;

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true); 
		conn.setUseCaches(false);
		conn.setConnectTimeout(timeOut);
		conn.setReadTimeout(timeOut);
		conn.setRequestMethod("GET");
		conn.connect();

		String contentType = conn.getHeaderField("Content-Type");
		String charSet = "utf-8";
		if (!StringUtility.isNullOrEmpty(contentType)) {
		    for (String elm : contentType.replace(" ", "").split(";")) {
	            if (elm.startsWith("charset=")) {
	                charSet = elm.substring(8);
	                break;
	            }
	        }
		}
		
		int intConnectionStatus = conn.getResponseCode();
		if (intConnectionStatus == 200) {
			InputStream in = conn.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in,charSet));
			StringBuilder strbReturn = new StringBuilder();
			String strReadLine = "";
			while ((strReadLine = br.readLine()) != null) {
				strbReturn.append(strReadLine.trim());
			}

			result = new JSONObject(strbReturn.toString());
		}
		return result;
	}
    public static JSONObject connectionForPost(String address, String data, int timeOut)
            throws MalformedURLException, IOException, JSONException {
        JSONObject result = null;
        
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(timeOut);
        conn.setReadTimeout(timeOut);
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("content-type", "application/json;charset=utf-8");
        conn.setRequestMethod("POST");
        OutputStream outStrm = conn.getOutputStream();
        outStrm.write(data.getBytes("UTF-8"));
        outStrm.flush();
        outStrm.close();
        
        int intConnectionStatus = conn.getResponseCode();
        InputStream in = null;
        String strReadLine = "";
        
        if (intConnectionStatus == 200) {
            in = conn.getInputStream();
        } else if (intConnectionStatus == 500) {
            in = conn.getErrorStream();
        }
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strbReturn = new StringBuilder();
            while ((strReadLine = br.readLine()) != null) {
                strbReturn.append(strReadLine.trim());
            }
            result = new JSONObject(strbReturn.toString());
        }
        
        return result;
	}
}
