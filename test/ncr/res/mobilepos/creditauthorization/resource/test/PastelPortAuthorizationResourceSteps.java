/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResourceSteps
 *
 * Steps Class of CreditAuthorizationResource
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.resource.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.Jis1;
import ncr.res.mobilepos.creditauthorization.model.Jis1Param;
import ncr.res.mobilepos.creditauthorization.model.Jis2;
import ncr.res.mobilepos.creditauthorization.model.Jis2Param;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.creditauthorization
          .resource.CreditAuthorizationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class PastelPortAuthorizationResourceSteps extends Steps {
    private CreditAuthorizationResource creditAuthorizationResource;
    private CreditAuthorizationResp creditAuthResp;
    private ResultBase resultBase;
    private PastelPortResp resp;

    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("PastelPortAuthorizationResourceTest",
                "test/ncr/res/mobilepos/creditauthorization/resource/"
                + "test/pastelport_authorization_dataset.xml", DATABASE.RESTransaction);
    }

    @AfterScenario
    public void TearDown(){
        Requirements.TearDown();
        creditAuthorizationResource = null;
        creditAuthResp = null;
        resultBase = null;
    }

    @Given("a CreditAuthorizationResource")
    public void createResource() throws  JAXBException{
        creditAuthorizationResource = new CreditAuthorizationResource();
        ServletContext servletContext = Requirements.getMockServletContext();
        servletContext.setAttribute(GlobalConstant.PASTELPORTENV_PARAM_KEY,
                new PastelPortEnv());

        PastelPortEnv env =
                (PastelPortEnv)servletContext.getAttribute(
                        GlobalConstant.PASTELPORTENV_PARAM_KEY);
        env.setCompanyCode("111");
        env.setPastelPortMultiServerIP("localhost");
        env.setConnectionPortNO(8012);
        env.setHandledCompanyCode("1234567");
        env.setHandledCompanySubCode("8901");
        env.setMscedit("JIS1");
        env.setCompanyCode("12345");
        env.setTimeOut(65);
        env.setPaymentClassification("");
        env.setSignLessReceiptWord("ƒTƒCƒ“ƒŒƒX‚µ‚½");
        env.setTradingUnfinishedReceiptWord("ŽæˆøŽ¸”s");
        env.setRetryCount(1);
        env.setPrintPPLog(true);

        servletContext.setAttribute(GlobalConstant.JIS1_PARAM_KEY,
                new Jis1Param());

        Jis1Param jis1Param = (Jis1Param)servletContext.getAttribute(
                GlobalConstant.JIS1_PARAM_KEY);
        List<Jis1> jis11List = new ArrayList<Jis1>();
        Jis1 jis11 = new Jis1();
        jis11.setCompanyinfo("DINERS");
        jis11.setPosition("1");
        jis11.setDigits("6");
        jis11.setCompanycodebegin("200000");
        jis11.setCompanycodeend("300000");
        jis11.setPosition1("1");
        jis11.setDigits1("14");
        jis11.setExpirationposition("16");
        jis11.setCrcompanycode("16");
        jis11List.add(jis11);

        Jis1 jis12 = new Jis1();
        jis12.setCompanyinfo("JCB");
        jis12.setPosition("1");
        jis12.setDigits("6");
        jis12.setCompanycodebegin("300000");
        jis12.setCompanycodeend("499999");
        jis12.setPosition1("1");
        jis12.setDigits1("16");
        jis12.setExpirationposition("18");
        jis12.setCrcompanycode("12");
        jis11List.add(jis12);
        jis1Param.setJis1s(jis11List);

        servletContext.setAttribute(GlobalConstant.JIS2_PARAM_KEY,
                new Jis2Param());

        Jis2Param jis2Param = (Jis2Param)servletContext
            .getAttribute(GlobalConstant.JIS2_PARAM_KEY);
        List<Jis2> jis21List = new ArrayList<Jis2>();

        Jis2 jis21 = new Jis2();
        jis21.setCompanyinfo("JCB");
        jis21.setIdmark("a");
        jis21.setCategorycode("9");
        jis21.setCorpcode("9660");
        jis21.setPosition("0");
        jis21.setDigits("0");
        jis21.setCompanycode("0");
        jis21.setAccountnumberdigits("14");
        jis21.setPosition1("13");
        jis21.setDigits1("14");
        jis21.setExpirationposition("40");
        jis21.setCrcompanycode("16");
        jis21List.add(jis21);

        Jis2 jis22 = new Jis2();
        jis22.setCompanyinfo("SMBC_OMC");
        jis22.setIdmark("s");
        jis22.setCategorycode("6");
        jis22.setCorpcode("3070");
        jis22.setPosition("11");
        jis22.setDigits("5");
        jis22.setCompanycode("490120");
        jis22.setAccountnumberdigits("16");
        jis22.setPosition1("11");
        jis22.setDigits1("16");
        jis22.setExpirationposition("40");
        jis22.setCrcompanycode("12");
        jis21List.add(jis22);

        jis2Param.setJis2(jis21List);
        creditAuthorizationResource.setContext(servletContext);
    }

    @Then("I should have a CreditAuthorizationResource")
    public void testCreditAuthorizationResource(){
        assertNotNull(creditAuthorizationResource);
    }

    @When("I have a strMsgjson {$strMsgjson}")
    public void testAuthorizeCreditWithPastalPort(String strMsgjson, String companyId, String storeId) {
        String companyid = companyId;
        String storeid = storeId;
    	String sendDataStr = setTxDateTime(strMsgjson);
        resp=creditAuthorizationResource
            .authorizeCreditWithPastelPort(sendDataStr, companyid, storeid);
    }

    @Then("I should get a PastelPortResp: $expected")
    public void tesTresp(ExamplesTable expected){
        Map<String, String> row = expected.getRows().get(0);
         PastelPortResp respExpacted = new PastelPortResp();
          if("OK".equals(row.get("errorcode"))){
         respExpacted.setErrorcode("    ");//row.get("errorcode")
          }else{
              respExpacted.setErrorcode(row.get("errorcode"));
          }
          if("null".equals(row.get("status"))){
         respExpacted.setStatus(null);
          }else {
              respExpacted.setStatus(row.get("status"));
          }
         respExpacted.setPosReturn(Boolean
                     .parseBoolean((row.get("posreturn"))));
         if("null".equals(row.get("cancelnoticeflag"))){
             respExpacted.setCancelnoticeflag(null);
         }else{
         respExpacted.setCancelnoticeflag(row.get("cancelnoticeflag"));
         }
         if("null".equals(row.get("creditstatus"))){
             respExpacted.setCreditstatus(null);
         }else{
             respExpacted.setCreditstatus(row.get("creditstatus"));
         }
         if("null".equals(row.get("resultcode"))){
             respExpacted.setResultCode(null);
         }else{
         respExpacted.setResultCode(row.get("resultcode"));
         }
        assertEquals(respExpacted.getErrorcode(),resp.getErrorcode());
        assertEquals(respExpacted.getStatus(),resp.getStatus());
        assertEquals(respExpacted.getPosReturn(),resp.getPosReturn());
        assertEquals(respExpacted.getCancelnoticeflag(),
                    resp.getCancelnoticeflag());
        assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
        assertEquals(respExpacted.getResultCode(),resp.getResultCode());
    }

	private String setTxDateTime(String strMsgjson) {
		strMsgjson = strMsgjson.substring(1, strMsgjson.length() - 1);
		String[] attr = strMsgjson.split(",");
		String[] key = new String[attr.length];
		String[] value = new String[attr.length];
		for (int i = 0; i < attr.length; i++) {
			key[i] = attr[i].split(":")[0].replaceAll("\"", "").replace("'", "");
			value[i] = attr[i].split(":")[1].replaceAll("\"", "").replace("'", "");
		}
		for (int i = 0; i < key.length; i++) {
			if ("txdatetime".equals(key[i])) {
				value[i] = value[i].replace(value[i].substring(0, 8),
						new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
								.format(new Date()));
			} else if ("voidtxdatetime".equals(key[i])) {
				value[i] = value[i].replace(value[i].substring(0, 8),
						new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
								.format(new Date()));
			}
		}
		String sendDataStr = "";
		for (int k = 0; k < key.length; k++) {
			sendDataStr += "\"" + key[k] + "\":\"" + value[k] + "\",";
		}
		sendDataStr = "{" + sendDataStr.substring(0, sendDataStr.length() - 1)
				+ "}";
		return sendDataStr;
	}
}
