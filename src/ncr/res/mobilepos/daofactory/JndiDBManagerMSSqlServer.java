package ncr.res.mobilepos.daofactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ncr.res.mobilepos.exception.DaoException;

public final class JndiDBManagerMSSqlServer extends JndiDBManager  {

	private static volatile DBManager instance;
    
    private JndiDBManagerMSSqlServer() throws DaoException  {
    	super();
        try {
            setInitialContext(new InitialContext());
            Context env = (Context) this.initContext.lookup("java:comp/env");
            dataSource = (DataSource) env.lookup("jdbc/MSSQLSERVER");
        } catch (NamingException e) {
            throw new DaoException("NamingException: Unable to find datasource", e);
        }
    }
    
    public static DBManager getInstance() throws DaoException {
    	if (instance == null) {
            instance = new JndiDBManagerMSSqlServer();
        }
        return instance;
    }
}
