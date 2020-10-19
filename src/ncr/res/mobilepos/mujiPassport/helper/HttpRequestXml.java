package ncr.res.mobilepos.mujiPassport.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.helper.StringUtility;

public class HttpRequestXml {

    /**
     * @param address
     * @param params
     * @param intTimeout
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static List<String> connection(String address, String params, String intTimeout)
            throws MalformedURLException, IOException {
        return connection(address, params, intTimeout, intTimeout);
    }

    /**
     * @param address
     * @param params
     * @param readTimeout
     * @param connectTimeout
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static List<String> connection(String address, String params, String readTimeout, String connectTimeout)
            throws MalformedURLException, IOException {
        List<String> lstReturn = new ArrayList<String>();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if (!StringUtility.isNullOrEmpty(connectTimeout)) {
            conn.setConnectTimeout(Integer.parseInt(connectTimeout) * 1000);
        } else {
            if (!StringUtility.isNullOrEmpty(readTimeout)) {
                conn.setConnectTimeout(Integer.parseInt(readTimeout) * 1000);
            }
        }
        if (!StringUtility.isNullOrEmpty(readTimeout)) {
            conn.setReadTimeout(Integer.parseInt(readTimeout) * 1000);
        }
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params.toString());
        printWriter.flush();
        conn.connect();
        int intConnectionStatus = conn.getResponseCode();
        lstReturn.add(String.valueOf(intConnectionStatus));
        InputStream in = null;
        if (intConnectionStatus == 200) {
            in = conn.getInputStream();
        } else {
            in = conn.getErrorStream();
        }
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder strbReturn = new StringBuilder();
            String strReadLine = "";
            while ((strReadLine = br.readLine()) != null) {
                strbReturn.append(strReadLine.trim());
            }
            br.close();
            lstReturn.add(strbReturn.toString());
        }
        return lstReturn;
    }
}