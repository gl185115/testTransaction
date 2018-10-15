package ncr.res.mobilepos.helper;

import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Map;


public class DBInitiator  extends DBTestCase {

	private static final String DBUNIT_SERVER_IP = "localhost";

    private static final String DBUNIT_DRIVER_CLASS_SQLS =
                        "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DBUNIT_CONNECTION_URL_SQLS =
                        "jdbc:sqlserver://" + DBUNIT_SERVER_IP + ":1433;databaseName=";

    private static final String DBUNIT_DRIVER_CLASS_HSQLDB = "org.hsqldb.jdbcDriver";
    private static final String DBUNIT_CONNECTION_URL_HSQLDB =
                        "jdbc:hsqldb:file:testdb/testdb;shutdown=true";

    private static final IDataTypeFactory DEFAULT_DATA_TYPEFACTORY = new MsSqlDataTypeFactory();

    private IDataSet dataset;
    public enum DATABASE { RESMaster, RESTransaction }
    public DBInitiator(String name) {
		super(name);
	}
    public DBInitiator(final String name, DATABASE dbName)
    {
        super(name);
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                DBUNIT_DRIVER_CLASS_SQLS );
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                DBUNIT_CONNECTION_URL_SQLS + dbName.name());

        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                "entsvr");

        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                "ncrsa_ora" );
    }
    public DBInitiator(final String name, final String dataSetXml, final DATABASE dbName)
    {
        this(name, dbName);
        try {
            System.out.println("DataSet:" + dataSetXml);
            IDatabaseConnection connection = getConfiguredConnection();
            connection
                .getConfig()
                .setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
            dataset = new XmlDataSet(new FileInputStream(dataSetXml));
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final IDatabaseConnection getConfiguredConnection() throws Exception {
        IDatabaseConnection connection = getConnection();
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, DEFAULT_DATA_TYPEFACTORY);
        config.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
        return connection;
    }

    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected final void tearDown() throws Exception {
      getConnection().close();
      super.tearDown();
    }
    public final void ExecuteOperation(final DatabaseOperation operation,
            final String dataSetXml)
    throws Exception
    {               
        IDatabaseConnection connection = getConfiguredConnection();
        connection
            .getConfig()
            .setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
        IDataSet datasetOperation =
            new XmlDataSet(new FileInputStream(dataSetXml));
        operation.execute(connection, datasetOperation);
    }
    
    public final void ExecuteOperationNoKey(final DatabaseOperation operation,
            final String primaryKey, final String dataSetXml)
    throws Exception
    {               
        System.out.println("NoKey DataSet:" + dataSetXml);   
        IDatabaseConnection connection = getConfiguredConnection();
        connection
            .getConfig()
            .setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
        connection
            .getConfig()
            .setProperty(DatabaseConfig.PROPERTY_PRIMARY_KEY_FILTER,
                    new ThisPrimaryKeyFilter(primaryKey));
        IDataSet datasetOperation =
            new XmlDataSet(new FileInputStream(dataSetXml));
        operation.execute(connection, datasetOperation);
    }
    
    public final void ExecuteIdentityInsertOperation(
            final DatabaseOperation operation,
            final String dataSetXml)
    throws Exception
    {               
        System.out.println("IdentityInsert DataSet:" + dataSetXml);
        IDataSet datasetOperation =
            new XmlDataSet(new FileInputStream(dataSetXml));
        new InsertIdentityOperation(operation)
            .execute(getConfiguredConnection(), datasetOperation);
    }
    
    @Override
    protected final IDataSet getDataSet() throws Exception {
        return dataset;
    }
    
    @Test
    public void testCreateDevice() throws Exception {     
    }
    
    public final void exportTable(final String tableName) throws Exception
    {
      IDataSet dataSet = getConfiguredConnection().createDataSet(new String[]
      {
              tableName
      });
      
      File outputFile = new File("c:\\temp", tableName + ".xml");
      FlatXmlDataSet.write(dataSet, new FileOutputStream(outputFile));

    }
    
    //Get a table value
    public final ITable getTableSnapshot(final String tableName) {
        ITable table = null;
        IDataSet dataSet = null;
        
        try {
             dataSet = getConfiguredConnection()
             .createDataSet(new String[]
                 {
                         tableName
                 });
             
            table =  dataSet.getTable(tableName);
        }  catch (DatabaseUnitException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            //TODO:
        }        
        return table;
    }

    public final ITable getQuery(final String tableName, final String query){
        ITable table = null;
        try {
             table = getConfiguredConnection().createQueryTable(tableName, query);
        } catch (DataSetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //TODO: 
        }
        return table;
    }

    /**
     * Manually set-up DataSet.xml by code.
     * @param dataSetXml    The DataSet.xml filename
     * @param replacements  The Replacements
     * @throws Exception    The exception thrown when error occur.
     */
    public final void executeWithReplacement(final String dataSetXml,
            final Map<String, Object> replacements) throws Exception {
      //Get database connection.
      ReplacementDataSet rDataSet = null;

      try {
        IDatabaseConnection connection = getConfiguredConnection();
        connection
            .getConfig()
            .setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
        dataset = new XmlDataSet(new FileInputStream(dataSetXml));
        //Decorate the class and call addReplacementObject method
        rDataSet = new ReplacementDataSet(dataset);
        for (String key : replacements.keySet()) {
          rDataSet.addReplacementObject(key,
                  replacements.get(key));
        }
        dataset = rDataSet;
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
          rDataSet = null;
      }
    }

    class ThisPrimaryKeyFilter implements IColumnFilter {
        private String pseudoKey = null;

        ThisPrimaryKeyFilter(final String pseudoKeyToSet) {
            this.pseudoKey = pseudoKeyToSet;
        }

        public boolean accept(final String tableName, final Column column) {
            return column.getColumnName().equalsIgnoreCase(pseudoKey);
        }

    }
}
