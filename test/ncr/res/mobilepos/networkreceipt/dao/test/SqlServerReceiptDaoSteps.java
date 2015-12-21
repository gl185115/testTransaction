package ncr.res.mobilepos.networkreceipt.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.dao.SQLServerPastelPortLogDAO;
import ncr.res.mobilepos.networkreceipt.dao.SQLServerReceiptDAO;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptContent;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;
import ncr.res.mobilepos.networkreceipt.model.ReceiptCredit;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;


@SuppressWarnings("deprecation")
public class SqlServerReceiptDaoSteps extends Steps{
    
    private SQLServerReceiptDAO ssrd;
    private SQLServerPastelPortLogDAO ssppd;
    private PaperReceipt resultPR;
    private ReceiptCredit resultCredit;
    
    private PaperReceiptHeader header;
    private List<String> commercialList = new ArrayList<String>();
    
    private PaperReceiptFooter footer;
    
    private PaperReceiptPayment payment;
    
    private String printerName;
    private String docTaxStampPath;
    private String logoFilePath;
    private NetPrinterInfo printerInfo;
    int result = 0;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
        GlobalConstant.setCorpid("0001");
    }
    
    @AfterScenario
    public final void TearDown()
    {
        Requirements.TearDown();
    }
    
    @Given ("I have a SQLServerReceiptDao")
    public final void createSqlServerReceiptDao() throws DaoException
    {
        ssrd = new SQLServerReceiptDAO();
        
        DBInitiator dbinit = new DBInitiator("SqlServerReceiptDaoSteps",
                "test/ncr/res/mobilepos/networkreceipt/dao/test/"
                + "forReceiptDoaTesting.xml", DATABASE.RESMaster);
        Assert.assertNotNull(dbinit);
    }
    
    @Then ("I should have a SQLServerReceiptDao")
    public final void checkSqlServerReceiptDao()
    {
        Assert.assertNotNull(ssrd);
    }
    
    @Given ("I have a SQLServerPastelPortLogDao")
    public final void createSqlServerPastelPortLogDao() throws DaoException
    {
    	ssppd = new SQLServerPastelPortLogDAO();
        
        DBInitiator dbinit = new DBInitiator("SqlServerReceiptDaoSteps",
                "test/ncr/res/mobilepos/networkreceipt/dao/test/"
                + "forReceiptDoaTestingPastelPort.xml", DATABASE.RESTransaction);
        Assert.assertNotNull(dbinit);
    }
    
   
    
    @Then ("I should have a SQLServerPastelPortLogDao")
    public final void checkSqlServerPastelPortLogDao()
    {
        Assert.assertNotNull(ssppd);
    }
    
    @When("I ask for paper receipt header using $storeid")
    public final void getPaperReceiptHeader(final String storeid){
        try {
            header = ssrd.getPaperReceiptHeader(storeid);
            commercialList = header.getCommercialList();
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @Then("the header of paper receipt will be $address, $tel,"
            + " $url, $ads1, $ads2, $ads3")
    public final void checkHeader(
            final String address, final String tel, final String url,
            final String ads1, final String ads2, final String ads3){
        assertThat(address, is(header.getAddress()));
        assertThat(tel, is(header.getTel()));
        assertThat(url, is(header.getSiteUrl()));
        if(commercialList.get(0) != null){
            assertThat(ads1, is(commercialList.get(0)));
        }
        if(commercialList.get(1) != null){
            assertThat(ads2, is(commercialList.get(1)));
        }
        if(commercialList.get(2) != null){
            assertThat(ads3, is(commercialList.get(2)));
        }
    }
    
    @When("I ask for paper receipt footer using $storeId, $operatorNo, $termId, $txId")
    public final void getPaperReceiptFooter(final String storeId, final String operatorNo,
            final String termId, final String txId){
        try {
            footer = ssrd.getPaperReceiptFooter(storeId, operatorNo, termId, txId);
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @Then("the footer of paper receipt will be $shopName, $registerNum,"
            + " $departmentName, $tradeNum, $saleMan, $holdName")
    public final void checkFooter(
            final String shopName, final String registerNum, 
            final String departmentName, final String tradeNum,
            final String saleMan, final String holdName){
        assertThat(shopName, is(footer.getShopName()));
        assertThat(registerNum, is(footer.getRegisterNum()));
        assertThat(departmentName, is(footer.getDepartmentName()));
        assertThat(tradeNum, is(footer.getTradeNum()));
        assertThat(saleMan, is(footer.getSaleMan()));
        assertThat(holdName, is(footer.getHoldName()));
    }
    
    @When("I ask for paper receipt payment using $corpId,"
            + " $storeId, $termId, $txId, $txDate")
    public final void getPaperReceiptPayment(final String corpId,
            final String storeId, final String termId,
            final String txId, final String txDate){
        try {
            payment = ssrd.getPaperReceiptPayment(corpId, storeId,
                    termId, txId, txDate);
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @Then("the payment of paper receipt will be $crCompanyCode,"
            + " $companyName, $recvCompanyCode, $panLast4, $caStatus,"
            + " $expiryMaster, $paymentSeq, $approvalCode, $traceNum,"
            + " $settlementNum, $creditAmount")
    public final void checkPayment(final String crCompanyCode,
            final String companyName, final String recvCompanyCode,
            final String panLast4, final String caStatus, 
            final String expiryMaster, final String paymentSeq,
            final String approvalCode, final String traceNum,
            final String settlementNum, final String creditAmount){
        assertThat(crCompanyCode, is(payment.getCrCompanyCode()));
        //assertThat(companyName, is(payment.getCompanyName()));
        assertThat(recvCompanyCode, is(payment.getRecvCompanyCode()));
        assertThat(panLast4, is(payment.getPanLast4()));
        assertThat(caStatus, is(payment.getCaStatus()));
        //assertThat(expiryMaster, is(payment.getExpiryMaster()));
        assertThat(paymentSeq, is(payment.getPaymentSeq()));
        assertThat(approvalCode, is(payment.getApprovalCode()));
        assertThat(traceNum, is(payment.getTraceNum()));
        assertThat(settlementNum, is(payment.getSettlementNum()));
        assertThat(creditAmount, is(payment.getCreditAmount()));
    }
    
    @When("I ask for printer name using $storeid, $termid")
    public final void getPrinterName(final String storeid,final String termid){
        try {
            printerName = ssrd.getPrinterName(storeid, termid);
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @Then("I will get the printer $printerName")
    public final void checkPrinterName(final String expectedprinterName){
        assertThat(expectedprinterName, is(this.printerName));
    }
    
    @When("I ask for Documentary Tax Stamp path using $storeid")
    public final void getDocTaxStampPath(final String storeid){
        try {
            docTaxStampPath = ssrd.getDocTaxStampPath(storeid);
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @Then("I will get the Documentary Tax Stamp Path $path")
    public final void checkDocTaxStampPath(final String path){
        assertThat(path, is(this.docTaxStampPath));
    }
    
    @When("I ask for Logo path using $storeid")
    public final void getLogoFilePath(final String storeid) {
        try {
            logoFilePath = ssrd.getLogoFilePath(storeid);
        } catch (DaoException e) {
            result = 1;
        }
    }
    @Then("I will get the logo file path $path")
    public final void checkLogoFilePath(final String path){
        assertThat(path, is(this.logoFilePath));
    }
    
    @When("I ask for Net Printer Info using $storeid, $termid")
    public final void getPrinterInfo(final String storeid, final String termid){
        try {
            printerInfo = ssrd.getPrinterInfo(storeid, termid);
        } catch (DaoException e) {
            result = 1;
        }
    }
    @Then("I will get the Net  Printer Information"
            + " $ipaddress, $portTCP, $portUDP")
    public final void checkPrinterInfo(final String ipaddress,
            final String portTCP, final String portUDP){
        assertThat(ipaddress, is(this.printerInfo.getUrl()));
        assertThat(portTCP, is(String.valueOf(this.printerInfo.getPortTCP())));
        assertThat(portUDP, is(String.valueOf(this.printerInfo.getPortUDP())));
    }
    
    @When ("I ask for a paperreceipt using xml $poslog, $txid,"
            + " $deviceno, $operatorno")
    public final void getPaperReceipt(String xml, String txid,
            String deviceno, String operatorno) throws DaoException
    {
        if(txid.equals("null")) {
            txid = null;
        }
        if(deviceno.equals("null")) {
            deviceno = null;
        }
        if(operatorno.equals("null")) {
            operatorno = null;
        }
        
        XmlSerializer<PosLog> transSerializer = new XmlSerializer<PosLog>();
        PosLog poslog = null;
        try {
            poslog = transSerializer.unMarshallXml(xml, PosLog.class);
        } catch (JAXBException e) {
            result = 1;
        }
        if(poslog != null){
            resultPR = ssrd.getPaperReceipt(poslog, txid,
                    deviceno, operatorno, null);
        }
    }
    
    @Then ("the Paper Receipt should be retrieved")
    public final void successPaperReceipt()
    {
        PaperReceiptContent prc = resultPR.getReceiptContent();
        PaperReceiptPayment paymentReceipt = resultPR.getReceiptPayment();
        PaperReceiptFooter prf = resultPR.getReceiptFooter();
        PaperReceiptHeader prh = resultPR.getReceiptHeader();
        
        assertThat(null, is(equalTo(prc.getDiscount())));
        assertThat((long)10000, is(equalTo(prc.getPaymentCash())));
        assertThat((long)500, is(equalTo(prc.getPaymentCredit())));
        assertThat((double)6375, is(equalTo(prc.getPaymentChange())));
        assertThat((double)3625, is(equalTo(prc.getSubTotal())));
        assertThat((double)3625, is(equalTo(prc.getTotal())));
        
        //The date is set to the normal date in the posLog,
        //The date will change to either kanji or english format when
        //ReceiptFormatter.getPaperReceipt is called and in the 
        //Mail receipt, it will be formatted in the formatToHtml function
        assertThat("2013-10-10T14:30:41",
                is(equalTo(prc.getReceiptDateTime())));
        assertThat("172", is(equalTo(prc.getTax())));
        assertThat(1, is(equalTo(prc.getProductItemList().size())));
        assertThat("#201 B/T C&D GR",
                is(equalTo(prc.getProductItemList().get(0).getProductName())));
        assertThat((double)2300,
                is(equalTo(prc.getProductItemList().get(0).getAmount())));
        assertThat((double)200,
                is(equalTo(prc.getProductItemList()
                        .get(0).getDiscountAmount())));
        assertThat((double)2300,
                is(equalTo(prc.getProductItemList().get(0).getPrice())));
        assertThat(1,
                is(equalTo(prc.getProductItemList().get(0).getQuantity())));
        
        assertThat("Sales Space Name",
                is(equalTo(prf.getDepartmentName())));
        assertThat("Event Name", is(equalTo(prf.getHoldName())));
        assertThat("1111", is(equalTo(prf.getRegisterNum())));
        assertThat("Operator1", is(equalTo(prf.getSaleMan())));
        assertThat("NCR Plaza", is(equalTo(prf.getShopName())));
        assertThat("4190", is(equalTo(prf.getTradeNum())));
        
        assertThat("000-0000 City Street 00-00", is(equalTo(prh.getAddress())));
        assertThat("http://www.ncr.co.jp", is(equalTo(prh.getSiteUrl())));
        assertThat("00-0000-0000", is(equalTo(prh.getTel())));
        assertThat("sentence1", is(prh.getCommercialList().get(0)));
        assertThat("sentence2", is(prh.getCommercialList().get(1)));
        assertThat("sentence3", is(prh.getCommercialList().get(2)));
        
        assertThat("123456", is(equalTo(paymentReceipt.getTraceNum())));
        assertThat("12", is(paymentReceipt.getCrCompanyCode()));
        assertThat("JCB", is(paymentReceipt.getCompanyName()));
        assertThat("12345", is(paymentReceipt.getRecvCompanyCode()));
        assertThat("************7755", is(paymentReceipt.getPanLast4()));
        assertThat("00", is(paymentReceipt.getCaStatus()));
        assertThat("**/**", is(paymentReceipt.getExpiryMaster()));
        assertThat("12345", is(paymentReceipt.getPaymentSeq()));
        assertThat("1234567", is(paymentReceipt.getApprovalCode()));
        assertThat("123456", is(paymentReceipt.getTraceNum()));
        assertThat("120420-00011111-12345-03111",
                is(paymentReceipt.getSettlementNum()));
        assertThat("10000", is(paymentReceipt.getCreditAmount()));
        
    }
    
    @Then ("The Paper Receipt should be empty")
    public final void failPaperReceipt()
    {
        Assert.assertNull(resultPR.getReceiptContent());
        Assert.assertNull(resultPR.getReceiptHeader());
        Assert.assertNull(resultPR.getReceiptPayment());
        Assert.assertNull(resultPR.getReceiptFooter());
    }
    
    @When ("I request a receipt credit using $txid")
    public final void getReceiptCredit(String txid) throws DaoException
    {
        if(txid.equals("null")) {
            txid = null;
        }
        
        resultCredit = ssrd.getReceiptCredit(txid);
    }
    
    @Then ("Receipt Credit should be empty")
    public final void failReceiptCredit()
    {
        Assert.assertNull(resultCredit.getPan());
        Assert.assertNull(resultCredit.getExpirationdate());
    }
    
    @Then ("Receipt Credit should be retrieved")
    public final void successReceiptCredit()
    {
        assertThat("1234567890123456", is (equalTo(resultCredit.getPan())));
        assertThat("1231", is (equalTo(resultCredit.getExpirationdate())));
    }

}

