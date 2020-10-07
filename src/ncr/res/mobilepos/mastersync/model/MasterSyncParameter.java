package ncr.res.mobilepos.mastersync.model;

public class MasterSyncParameter {
    private int syncGroupId;
    private int syncTableId;
    private String databaseName;
    private String schemaName;
    private String tableName;
    private String functionName;

    public MasterSyncParameter() {
        syncGroupId = 0;
        syncTableId = 0;
        databaseName = "";
        schemaName = "";
        tableName = "";
        functionName = "";
    }

    public int getSyncGroupId() {
        return syncGroupId;
    }

    public void setSyncGroupId(final int syncGroupId) {
        this.syncGroupId = syncGroupId;
    }

    public int getSyncTableId() {
        return syncTableId;
    }

    public void setSyncTableId(final int syncTableId) {
        this.syncTableId = syncTableId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName == null ? "" : databaseName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(final String schemaName) {
        this.schemaName = schemaName == null ? "" : schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName == null ? "" : tableName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName == null ? "" : functionName;
    }
}
