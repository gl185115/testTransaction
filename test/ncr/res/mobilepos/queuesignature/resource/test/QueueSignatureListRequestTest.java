/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* ReportResourceTest
*
* Unit Test Class for ReportResource Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.queuesignature.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

public class QueueSignatureListRequestTest extends TestRunnerScenario {
    public QueueSignatureListRequestTest(){
        super(new QueueSignatureListRequestSteps());        
    }
}
