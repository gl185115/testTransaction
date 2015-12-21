package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Map;

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
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;


public class DBInitiator  extends DBTestCase {
    private IDataSet dataset;
    public enum DATABASE { RESMaster, RESTransaction }
    public DBInitiator(final String name, DATABASE dbName)
    {
        super(name);
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:sqlserver://149.25.136.82:1433;databaseName=" + dbName.toString());
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
            IDatabaseConnection connection = getConnection();
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
    throws DatabaseUnitException, SQLException, Exception
    {               
        System.out.println("DataSet:" + dataSetXml);   
        IDatabaseConnection connection = getConnection();
        connection
            .getConfig()
            .setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "[?]");
        IDataSet datasetOperation =
            new XmlDataSet(new FileInputStream(dataSetXml));
        operation.execute(connection, datasetOperation);
    }
    
    public final void ExecuteOperationNoKey(final DatabaseOperation operation,
            final String primaryKey, final String dataSetXml)
    throws DatabaseUnitException, SQLException, Exception
    {               
        System.out.println("NoKey DataSet:" + dataSetXml);   
        IDatabaseConnection connection = getConnection();
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
    throws DatabaseUnitException, SQLException, Exception
    {               
        System.out.println("IdentityInsert DataSet:" + dataSetXml);
        IDataSet datasetOperation =
            new XmlDataSet(new FileInputStream(dataSetXml));
        new InsertIdentityOperation(operation)
            .execute(getConnection(), datasetOperation);                
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
      IDataSet dataSet = getConnection().createDataSet(new String[]
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
             dataSet = getConnection()
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
             table = getConnection().createQueryTable(tableName, query);
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
        IDatabaseConnection connection = getConnection();
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
