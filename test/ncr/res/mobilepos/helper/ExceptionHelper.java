package ncr.res.mobilepos.helper;

import java.sql.SQLException;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;

public class ExceptionHelper {
	
	public static Exception getException(String exception) {
		Exception ex = new Exception();
		if ("DaoException".equalsIgnoreCase(exception)) {
        	ex = new DaoException();
        } else if ("Exception".equalsIgnoreCase(exception)) {
        	ex = new Exception();
        } else if ("SQLException".equalsIgnoreCase(exception)) {
        	ex = new DaoException(new SQLException());
        } else if ("SQLStatementException".equalsIgnoreCase(exception)) {
        	ex = new DaoException(new SQLStatementException());
        }
		return ex;
	}

}
