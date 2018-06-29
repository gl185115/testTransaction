package ncr.res.mobilepos.futurePay.helper;

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

public class HTTPBasicAuthorization {

    /**
     * HTTP Basic Authentication
     *
     * @param address
     * @param username
     * @param password
     * @return
     * @throws MalformedURLException 
     * @throws IOException
     * @throws Exception
     */
    public static List<String> connection(String address, String params, String username,
            String password, String intTimeout) throws MalformedURLException, IOException{
        List<String> lstReturn = new ArrayList<String>();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String author = "Basic "
                + Base64.encode((username + ":" + password).getBytes());
        conn.setRequestProperty("Authorization", author);
        conn.setRequestProperty("Content-Length", String  
                .valueOf(params.length())); 
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if(!StringUtility.isNullOrEmpty(intTimeout)){
            conn.setConnectTimeout(Integer.parseInt(intTimeout) * 1000);
            conn.setReadTimeout(Integer.parseInt(intTimeout) * 1000);
        }
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params.toString());
        printWriter.flush();
        conn.connect();
        int intConnectionStatus = conn.getResponseCode();
        lstReturn.add(String.valueOf(intConnectionStatus));
        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder strbReturn = new StringBuilder();
        String strReadLine = "";
        while((strReadLine = br.readLine()) != null){
            strbReturn.append(strReadLine.trim());
        }
        lstReturn.add(strbReturn.toString());
        return lstReturn;
    }
    public static List<String> connection(String address, String params, String username,
            String password, String readTimeout ,String connectTimeout) throws MalformedURLException, IOException{
        List<String> lstReturn = new ArrayList<String>();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String author = "Basic "
                + Base64.encode((username + ":" + password).getBytes());
        conn.setRequestProperty("Authorization", author);
        conn.setRequestProperty("Content-Length", String  
                .valueOf(params.length())); 
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if(!StringUtility.isNullOrEmpty(connectTimeout)){
            conn.setConnectTimeout(Integer.parseInt(connectTimeout) * 1000);
        } else {
            if(!StringUtility.isNullOrEmpty(readTimeout)){
                conn.setConnectTimeout(Integer.parseInt(readTimeout) * 1000);
            }
        }
        if(!StringUtility.isNullOrEmpty(readTimeout)){
            conn.setReadTimeout(Integer.parseInt(readTimeout) * 1000);
        }
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params.toString());
        printWriter.flush();
        conn.connect();
        int intConnectionStatus = conn.getResponseCode();
        lstReturn.add(String.valueOf(intConnectionStatus));
        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder strbReturn = new StringBuilder();
        String strReadLine = "";
        while((strReadLine = br.readLine()) != null){
            strbReturn.append(strReadLine.trim());
        }
        lstReturn.add(strbReturn.toString());
        return lstReturn;
    }
}