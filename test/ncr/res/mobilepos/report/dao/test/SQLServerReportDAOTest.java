/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* SQLServerReportDAOTest
*
* Unit Test Class for SQLServerReportDAO Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.report.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

@Ignore
public class SQLServerReportDAOTest extends TestRunnerScenario {
    public SQLServerReportDAOTest(){
        super(new SQLServerReportDAOSteps());
        System.out.println("SQLServerReportDAOTest is depreciated.");
    }
}
