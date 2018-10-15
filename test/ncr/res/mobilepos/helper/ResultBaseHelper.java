package ncr.res.mobilepos.helper;

import java.lang.reflect.Field;

import ncr.res.mobilepos.model.ResultBase;

public class ResultBaseHelper {
	
	public static int getErrorCode(String error) {
		int res = -1;
		
		try {
			for (Field f : ResultBase.class.getDeclaredFields()) {
				if (error.equalsIgnoreCase(f.getName())) {
					res = f.getInt(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getErrorMessage(String error) {
		String res = null;

		try {
			for (Field f : ResultBase.class.getDeclaredFields()) {
				if (error.equalsIgnoreCase(f.getName())) {
					res = (String)f.get(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
