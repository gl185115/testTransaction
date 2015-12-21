package ncr.res.mobilepos.networkreceipt.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

@Ignore
public class SqlServerReceiptDaoTest  extends TestRunnerScenario{
    
    public SqlServerReceiptDaoTest(){
        super(new SqlServerReceiptDaoSteps());
    }
}
