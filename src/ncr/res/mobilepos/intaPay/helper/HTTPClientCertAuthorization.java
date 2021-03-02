package ncr.res.mobilepos.intaPay.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONObject;

import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;
import ncr.res.mobilepos.intaPay.model.BaseModel;
import ncr.res.mobilepos.intaPay.model.PaymentInputModel;

public class HTTPClientCertAuthorization {

    protected String sendGetRequest(String url, String token) throws Exception {

        String response = ClientCustomSSLUtil.sendGetRequest(url, token);

        StringBuilder responseStr = new StringBuilder();
        responseStr.append("url:" + url).append("</br>");
        responseStr.append("token" + token).append("</br>");
        responseStr.append("response:" + response).append("</br>");

        return responseStr.toString();
    }

    protected String sendGetRequestWithCertificationFile(String url, String certPW, String certPath) throws Exception {

        String response = ClientCustomSSLUtil.sendGetRequestWithCertificationFile(url, certPW, certPath);

        StringBuilder responseStr = new StringBuilder();
        responseStr.append("url:" + url).append("</br>");
        responseStr.append("response:" + response).append("</br>");

        return responseStr.toString();
    }

    public static String createRequestStr(BaseModel model, String apiKey, String mchCode)
            throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, String> requestMap = convertModelToMap(model, apiKey, mchCode);
        JSONObject json = new JSONObject(requestMap);
        return json.toString();
    }

    public static Map<String, String> convertModelToMap(BaseModel model, String apiKey, String mchCode)
            throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (Util.isEmptyString(model.getMachant_id())) {
            model.setMachant_id(mchCode);
        }
        if (Util.isEmptyString(model.getNonce_str())) {
            model.setNonce_str(Util.getUUIDWithAllNumber(10));
        }
        if (Util.isEmptyString(model.getSign_type())) {
            model.setSign_type(IntaPayConstants.SIGN_TYPE_SHA256);
        }

        Map<String, String> requestMap = Util.getParametersByInputModel(model);
        String sign = SignUtil.getSign((SortedMap<String, String>) requestMap, apiKey, model.getSign_type());
        requestMap.put("sign", sign);
        return requestMap;
    }

    @SuppressWarnings("unused")
    private Map<String, String> convertModelToMap(PaymentInputModel model, String apiKey)
            throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (Util.isEmptyString(model.getNonce_str())) {
            model.setNonce_str(Util.getUUIDWithAllNumber(10));
        }
        if (Util.isEmptyString(model.getSign_type())) {
            model.setSign_type(IntaPayConstants.SIGN_TYPE_SHA256);
        }

        Map<String, String> requestMap = Util.getParametersByInputModel(model);
        String sign = SignUtil.getSign((SortedMap<String, String>) requestMap, apiKey, model.getSign_type());
        requestMap.put("sign", sign);
        return requestMap;
    }

    public static String getResponseByModelWithClientCert(BaseModel model, String url, String certPW, String certPath,
            String apiKey, String mchCode) throws Exception {

        String requestJson = createRequestStr(model, apiKey, mchCode);

        String response = ClientCustomSSLUtil.sendRequestWithCertificationFile(requestJson, url, certPW, certPath);
        JSONObject responseStr = new JSONObject();

        JSONObject jsonObj = new JSONObject(response);
        Map<String, String> map = new TreeMap<String, String>();
        for (String name : JSONObject.getNames(jsonObj)) {
            map.put(name, jsonObj.getString(name));
        }

        String sign = SignUtil.getSign((SortedMap<String, String>) map, apiKey, model.getSign_type());
        boolean result = sign.equals(map.get("sign"));

        responseStr.put("signChcek", result);
        responseStr.put("url", url);
        responseStr.put("request", requestJson);
        responseStr.put("response", response);

        return responseStr.toString();
    }

    public String getResponseByModel(BaseModel model, String url, String apiKey, String mchCode) throws Exception {

        String requestJson = createRequestStr(model, apiKey, mchCode);

        String response = ClientCustomSSLUtil.sendRequestJson(requestJson, url);

        StringBuilder responseStr = new StringBuilder();

        JSONObject jsonObj = new JSONObject(response);
        Map<String, String> map = new TreeMap<String, String>();
        for (String name : JSONObject.getNames(jsonObj)) {
            map.put(name, jsonObj.getString(name));
        }

        String sign = SignUtil.getSign((SortedMap<String, String>) map, IntaPayConstants.DEFAULT_API_KEY,
                model.getSign_type());
        boolean result = sign.equals(map.get("sign"));
        responseStr.append("signîFèÿ" + result).append("</br>");

        responseStr.append("url:" + url).append("</br>");
        responseStr.append("request:" + requestJson).append("</br>");
        responseStr.append("response:" + response).append("</br>");

        return responseStr.toString();
    }
}