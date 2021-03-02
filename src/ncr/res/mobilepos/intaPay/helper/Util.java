package ncr.res.mobilepos.intaPay.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmptyString(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        if (uuid.contains("-")) {
            uuid = uuid.replaceAll("-", "");
        }
        return uuid;
    }

    public static String getNonceString() {
        String r = Util.getUUID();
        return r.substring(0, 10);
    }

    public static String getUUIDWithAllNumber(int count) {
        String str = new Date().getTime() + "";
        boolean flag = true;
        while (flag) {
            str += convertNumber(getUUID());
            if (str.length() >= count) {
                flag = false;
                str = str.substring(0, count);
            }
        }
        return str;
    }

    public static String convertNumber(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String str1 = m.replaceAll("").trim();
        return str1;
    }

    public static Map<String, String> getParametersByInputModel(Object obj) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Map<String, String> requestMap = new TreeMap<String, String>();

        Field[] fields = obj.getClass().getDeclaredFields();
        setParams(fields, requestMap, obj);

        Class<?> superClass = obj.getClass().getSuperclass();
        if (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            setParams(superFields, requestMap, obj);
        }
        return requestMap;
    }

    public static String createRequestForm(Map<String, String> sPara, String strMethod, String strButtonName, String actionUrl) {
        // „pre-request params
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + actionUrl + "\" method=\"" + strMethod + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        // Pls don't set name attribute for the submit button
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
        System.out.println("sbHtml:" + sbHtml);
        return sbHtml.toString();
    }

    private static void setParams(Field[] fields, Map<String, String> requestMap, Object obj) throws IllegalArgumentException, IllegalAccessException, SecurityException, InvocationTargetException {
        String fieldName;
        Object fieldValue;
        for (Field field : fields) {
            field.setAccessible(true);
            fieldName = field.getName();
            fieldValue = field.get(obj);
            if (fieldValue != null) {
                requestMap.put(fieldName, fieldValue + "");
            }
        }
    }

}