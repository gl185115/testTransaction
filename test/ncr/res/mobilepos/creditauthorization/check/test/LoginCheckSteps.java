package ncr.res.mobilepos.creditauthorization.check.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.check.LoginCheck;
import ncr.res.mobilepos.creditauthorization.model.TxlNpsTxlog;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.pastelport.platform.CommonTx;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class LoginCheckSteps extends Steps {
	public LoginCheck loginCheck;
	private String dataSetFile = "test/ncr/res/mobilepos/creditauthorization/"
			+ "check/test/credit_authorization_dataset.xml";
	private boolean reLogin;

	@BeforeScenario
	public void SetUp() {
		Requirements.SetUp();
		new DBInitiator("LoginCheckTest", dataSetFile, DATABASE.RESTransaction);
	}

	@AfterScenario
	public void TearDown() {
		Requirements.TearDown();
	}

	@Given("a LoginCheck")
	public void createLoginCheck() throws DaoException {
		loginCheck = new LoginCheck();
	}

	@Then("I should have a LoginCheck")
	public void iHaveLoginCheck() {
		assertNotNull(loginCheck);
	}

	@When("I have a CommonTx and the same key record in DB: $xml")
	public void iHaveCommonTx_INDB(String xml) throws Exception {
		TxlNpsTxlog txlNpsTxlog = unMarshall(xml);

		CommonTx tx = new CommonTx();
		tx.setFieldValue("corpid", txlNpsTxlog.getCorpid());
		tx.setFieldValue("storeid", txlNpsTxlog.getStoreid());
		tx.setFieldValue("terminalid", txlNpsTxlog.getTermid());
		tx.setFieldValue("txid", txlNpsTxlog.getTxid());
		tx.setFieldValue("paymentseq", txlNpsTxlog.getPaymentseq());
		tx.setFieldValue("guid", txlNpsTxlog.getGuid());
		reLogin = loginCheck.loginCheck(tx);
	}

	@Then("It should be reLogin true: ($expected)")
	public void testVoidCheck_RELOGIN(String expected) {
		assertEquals(expected, String.valueOf(reLogin));
	}

	@When("I have a CommonTx and the same key record not in DB: $xml")
	public void iHaveCommonTx_NOTINDB(String xml) throws JSONException,
			Exception {
		TxlNpsTxlog txlNpsTxlog = unMarshall(xml);

		CommonTx tx = new CommonTx();
		tx.setFieldValue("corpid", txlNpsTxlog.getCorpid());
		tx.setFieldValue("storeid", txlNpsTxlog.getStoreid());
		tx.setFieldValue("terminalid", txlNpsTxlog.getTermid());
		tx.setFieldValue("txid", txlNpsTxlog.getTxid());
		tx.setFieldValue("paymentseq", txlNpsTxlog.getPaymentseq());
		tx.setFieldValue("guid", txlNpsTxlog.getGuid());
		tx.setFieldValue("brand", txlNpsTxlog.getBrand());
		tx.setFieldValue("service", txlNpsTxlog.getService());
		tx.setFieldValue("amount", txlNpsTxlog.getAmount());
		tx.setFieldValue("txdatetime", txlNpsTxlog.getTxdatetime());
		tx.setFieldValue("rwid", txlNpsTxlog.getRwid());
		tx.setFieldValue("txtype", txlNpsTxlog.getTxtype());
		tx.setFieldValue("rwkind", txlNpsTxlog.getRwkind());
		tx.setFieldValue("xmldocument", txlNpsTxlog.getXmldocument());
		reLogin = loginCheck.loginCheck(tx);
	}

	@Then("It should be reLogin false: ($expected)")
	public void testVoidCheck_LOGIN(String expected) {
		assertEquals(expected, String.valueOf(reLogin));
	}

	private TxlNpsTxlog unMarshall(String xml) throws JAXBException {
		XmlSerializer<TxlNpsTxlog> serializer = new XmlSerializer<TxlNpsTxlog>();
		TxlNpsTxlog creditAuth = serializer.unMarshallXml(xml,
				TxlNpsTxlog.class);
		return creditAuth;
	}

}
