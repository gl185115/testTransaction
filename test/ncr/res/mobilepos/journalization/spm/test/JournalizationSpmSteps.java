package ncr.res.mobilepos.journalization.spm.test;

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps for testing SPM
 * @author JD185128
 *
 */
@SuppressWarnings("deprecation")
public class JournalizationSpmSteps extends Steps{
	private ServletContext context;
	private JournalizationResource journalization;
	private PosLogResp posLogResp = null;
	private long fileSize = 0;
	
	@BeforeScenario
	public void setUp(){/* Mock the context */
		Requirements.SetUp();
		context = Requirements.getMockServletContext();
	}
	
	@AfterScenario
	public void tearDown(){
		Requirements.TearDown();
	}
	
	@When("I request to journalization for {$nth} times")
	public void whenJournalize(int nth){
		journalization = new JournalizationResource(context);
		journalization.init();
		posLogResp = null;
		String poslogxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><POSLog><Transaction><RetailStoreID>0031</RetailStoreID><WorkstationID>5001</WorkstationID><SequenceNumber>4209</SequenceNumber><BusinessDayDate>2014-04-07</BusinessDayDate><BeginDateTime>2014-04-07T16:05:42</BeginDateTime><OperatorID>111</OperatorID><ReceiptDateTime>2014-04-07T16:05:42</ReceiptDateTime><TillID>0</TillID><RetailTransaction><PriceDerivationResult MethodCode=\"Promotion\"><SequenceNumber>1</SequenceNumber><Amount Action=\"Subtract\">101</Amount><PreviousPrice>1101</PreviousPrice><PriceDerivationRule><PriceDerivationRuleID>0002</PriceDerivationRuleID><Description>Type2</Description><ReasonCode>0002</ReasonCode></PriceDerivationRule></PriceDerivationResult><Customer><CustomerDemographic>02</CustomerDemographic></Customer><ItemCount>6</ItemCount><LineItem><SequenceNumber>1</SequenceNumber><Sale TaxableFlag=\"True\" Department=\"9999\" Line=\"0\" Class=\"0\"><ItemID Type=\"Code128\">0000000000050</ItemID><Description>Item B5</Description><RegularSalesUnitPrice>556</RegularSalesUnitPrice><ActualSalesUnitPrice>556</ActualSalesUnitPrice><ExtendedAmount>556</ExtendedAmount><ExtendedDiscountAmount>101</ExtendedDiscountAmount><ItemSellingRule /><Quantity>1</Quantity><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>1</SequenceNumber><Amount Action=\"Subtract\">51</Amount><PriceDerivationRule ApplicationType=\"MixMatch\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><MMQuantity>1</MMQuantity><ReasonCode>0002</ReasonCode></PriceDerivationRule></RetailPriceModifier><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>2</SequenceNumber><Percent Action=\"Subtract\">10</Percent><Amount Action=\"Subtract\">50</Amount><PriceDerivationRule ApplicationType=\"Total\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><ReasonCode>02</ReasonCode></PriceDerivationRule></RetailPriceModifier><Tax TaxType=\"Sales\"><Amount>33</Amount><Percent>8</Percent><TaxableAmount TaxIncludedInTaxableAmountFlag=\"false\">422</TaxableAmount></Tax><Tax TaxType=\"Sales\" NormalTaxType=\"Exceptional\"><Amount>1</Amount></Tax></Sale></LineItem><LineItem><SequenceNumber>2</SequenceNumber><Sale TaxableFlag=\"True\" Department=\"9999\" Line=\"0\" Class=\"0\"><ItemID Type=\"Code128\">0000000000040</ItemID><Description>Item B4</Description><RegularSalesUnitPrice>545</RegularSalesUnitPrice><ActualSalesUnitPrice>545</ActualSalesUnitPrice><ExtendedAmount>545</ExtendedAmount><ExtendedDiscountAmount>99</ExtendedDiscountAmount><ItemSellingRule /><Quantity>1</Quantity><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>1</SequenceNumber><Amount Action=\"Subtract\">50</Amount><PriceDerivationRule ApplicationType=\"MixMatch\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><MMQuantity>1</MMQuantity><ReasonCode>0002</ReasonCode></PriceDerivationRule></RetailPriceModifier><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>2</SequenceNumber><Percent Action=\"Subtract\">10</Percent><Amount Action=\"Subtract\">49</Amount><PriceDerivationRule ApplicationType=\"Total\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><ReasonCode>02</ReasonCode></PriceDerivationRule></RetailPriceModifier><Tax TaxType=\"Sales\"><Amount>33</Amount><Percent>8</Percent><TaxableAmount TaxIncludedInTaxableAmountFlag=\"false\">413</TaxableAmount></Tax></Sale></LineItem><LineItem><SequenceNumber>3</SequenceNumber><Sale TaxableFlag=\"True\" Department=\"9999\" Line=\"0\" Class=\"0\"><ItemID Type=\"Code128\">0000000000400</ItemID><Description>Item C4</Description><RegularSalesUnitPrice>1440</RegularSalesUnitPrice><ActualSalesUnitPrice>1440</ActualSalesUnitPrice><ExtendedAmount>1440</ExtendedAmount><ExtendedDiscountAmount>793</ExtendedDiscountAmount><ItemSellingRule /><Quantity>1</Quantity><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>1</SequenceNumber><Percent Action=\"Subtract\">50</Percent><Amount Action=\"Subtract\">720</Amount><PriceDerivationRule ApplicationType=\"Item\"><PriceDerivationRuleID>0001</PriceDerivationRuleID><ReasonCode>01</ReasonCode></PriceDerivationRule></RetailPriceModifier><RetailPriceModifier MethodCode=\"Markdown\"><SequenceNumber>2</SequenceNumber><Percent Action=\"Subtract\">10</Percent><Amount Action=\"Subtract\">73</Amount><PriceDerivationRule ApplicationType=\"Total\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><ReasonCode>02</ReasonCode></PriceDerivationRule></RetailPriceModifier><Tax TaxType=\"Sales\"><Amount>47</Amount><Percent>8</Percent><TaxableAmount TaxIncludedInTaxableAmountFlag=\"false\">600</TaxableAmount></Tax></Sale></LineItem><LineItem><SequenceNumber>4</SequenceNumber><Discount><Percentage>10</Percentage><Amount>172</Amount><PriceDerivationRule ApplicationType=\"Total\"><PriceDerivationRuleID>0002</PriceDerivationRuleID><ReasonCode>02</ReasonCode></PriceDerivationRule></Discount></LineItem><LineItem><SequenceNumber>5</SequenceNumber><Tender TenderType=\"Cash\" TypeCode=\"Sale\"><Amount>2000</Amount><TenderChange><Amount>452</Amount></TenderChange></Tender></LineItem><LineItem><SequenceNumber>6</SequenceNumber><Tax TaxType=\"Sales\"><Amount>114</Amount><Percent>8</Percent><TaxableAmount TaxIncludedInTaxableAmountFlag=\"false\">1434</TaxableAmount></Tax></LineItem><Total TotalType=\"TransactionSubtotal\">1720</Total><Total TotalType=\"TransactionGrandAmount\">1548</Total></RetailTransaction></Transaction></POSLog>";
		int trainingmode = 0;
		for(int i=1; i<=nth; i++){
			posLogResp = journalization.journalize(poslogxml, trainingmode);
			System.out.println("#" + i + " " + poslogxml);
		}
	}
	
	@Then("the spm data should be written")
	public void thenSpmDataWritten() throws NamingException{
		Assert.assertTrue("SPM file size increases.",
				getSPMFile().getTotalSpace() > fileSize);
		fileSize = getSPMFile().getTotalSpace();
	}
	
	@Given("that the spm file is not found")
	public final void givenSpmFileEmpty() throws NamingException{
		getSPMFile().delete();		
		fileSize = 0;		
	}
	
	@Then("the spm data should not be written")
	public void thenSpmDataNotWritten() throws NamingException{
		System.out.println("fileSize:" + fileSize);
		System.out.println("getSPMFileSize:" + getSPMFile().getTotalSpace());
		Assert.assertEquals("SPM file size does not increase.", fileSize,
				getSPMFile().length());
		fileSize = getSPMFile().getTotalSpace();
	}
	
	@When("I stopped the tomcat server")
	public final void stopServer(){
		Requirements.ContextDestroyed();
		fileSize = 0;
	}
	
	@Then("the buffered spm data should be written")
	public final void thenWrite() throws NamingException{
		Assert.assertTrue("SPM file size increases.",
				getSPMFile().getTotalSpace() >= fileSize);
		fileSize = getSPMFile().getTotalSpace();
	}
	
	@Then("the SPM file exist")
	public final void thenSPMCreated() throws NamingException{
		Assert.assertTrue("SPM file existing.", getSPMFile().exists());
	}
	
	@Given("that the spm directory path is not available")
	public final void givenSpmDirNotAvailable(){
		InitialContext initContext = null;
		try {
			initContext = new InitialContext();
			initContext.rebind("java:comp/env/Journalization/spmPath",
					"XX:/ncr/res/log/SPM_DIR");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Given("that the spm entry does not exist in web.xml")
	public final void givenSpmEntryNotExist(){
		InitialContext initContext = null;
		try {
			initContext = new InitialContext();
			initContext
					.removeFromEnvironment("java:comp/env/Journalization/spmPath");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Then("I should get journalization response")
	public final void getJournalizationResult(){
		Assert.assertNotNull("Success journalization.", posLogResp);
	}
	
	@When("I request for {$poslogxml} journalization")
	public final void whenJournalizePosLog(String posLogXml, int trainingMode){
		journalization = new JournalizationResource(context);
		journalization.init();
		posLogResp = null;
		posLogResp = journalization.journalize(posLogXml, trainingMode);
	}
	
	private File getSPMFile() throws NamingException{
		javax.naming.Context env = (javax.naming.Context) new InitialContext().lookup("java:comp/env");
		String spmPath = (String) (env).lookup("Journalization/spmPath");
		String serverID = (String) (env).lookup("serverID");
		String spmFullPath = spmPath + "/" + JournalizationResource.SPM_FILENAME + "_" + serverID;
		return new File(spmFullPath);
	}
}
