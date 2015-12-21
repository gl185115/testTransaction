package ncr.res.mobilepos.networkreceipt.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.imageio.ImageTypeSpecifier;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.networkreceipt.model.*;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import java.util.Arrays;
import java.util.List;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Map;

import junit.framework.Assert;


public class NetworkReceiptModelSteps extends Steps {
    private PaperReceipt testPaperReceipt = null;
    private PaperReceiptContent testPaperReceiptContent = null;
    private PaperReceiptCredit testPaperReceiptCredit = null;
    private PaperReceiptFooter testPaperReceiptFooter = null;
    private PaperReceiptHeader testPaperReceiptHeader = null;
    private ReceiptCredit testReceiptCredit = null;
    private ReceiptLine testReceiptLine = null;
    private ReceiptProductItem testReceiptProductItem = null;

    
//==============================================================================
//            RECEIPT LINE
//==============================================================================
    @Given("I have an empty Receipt Line")
    public final void givenEmptyReceiptLine(){        
        testReceiptLine = new ReceiptLine();
    }
    
    @Given("I have a Receipt Line with Font: {$fontname} , {$style},"
            + " {$size} and LineData: {$linedata}")
    public final void giventFullReceiptLine(
            final String fontname, final int style,
            final int size, final String linedata)
    {
        Font font = new Font(fontname,style,size);
        
        testReceiptLine = new ReceiptLine(font, linedata);
    }
    
    @When ("I add a Font : {$fontname}, {$style}, {$size}")
    public final void whenAddFont(final String fontname,
            final int style, final int size)
    {
        Font font = new Font(fontname,style,size);
        testReceiptLine.setFont(font);
    }
    
    @When ("I add LineData: {$linedata}")
    public final void whenAddLineData(final String linedata)
    {
        testReceiptLine.setLinedata(linedata);
    }
    
    @Then ("I should have a Font : {$fontname} , {$style}, {$size}")
    public final void thenCheckFont(final String fontname,
            final int style, final int size)
    {
        assertThat(fontname,
                is(equalTo(testReceiptLine.getFont().getFontName())));
        assertThat(style, is(equalTo(testReceiptLine.getFont().getStyle())));
        assertThat(size, is(equalTo(testReceiptLine.getFont().getSize())));    
    }
    
    @Then ("I should have a LineData: {$linedata}")
    public final void thenCheckLinedata(final String linedata)
    {
        assertThat(linedata, is(equalTo(testReceiptLine.getLinedata())));
    }
    

    @Then("I should get an empty Receipt Line")
    public final void thenCheckEmptyReceiptLine()
    {
        Assert.assertNotNull(testReceiptLine);
        
        Assert.assertNull(testReceiptLine.getFont());
        Assert.assertNull(testReceiptLine.getLinedata());
    }

//==============================================================================
//    END OF RECEIPT LINE
//==============================================================================
    
    
//==============================================================================
//    RECEIPT PRODUCT ITEM
//==============================================================================

    @Given ("I have a ReceiptProductItem")
    public final void givenReceiptProductItem()
    {
        testReceiptProductItem = new ReceiptProductItem();
    }
    
    @Then ("I should have a ReceiptProductItem")
    public final void checkReceiptProductItem()
    {
        Assert.assertNotNull(testReceiptProductItem);
    }
    
    @When ("I set the amount to {$amount}")
    public final void setReceiptProductItemAmount(final long amount)
    {
        testReceiptProductItem.setAmount(amount);
    }
    
    @Then ("I should get amount {$amount}")
    public final void getReceiptProductItemAmount(final double amount)
    {
        assertThat(amount, is(equalTo(testReceiptProductItem.getAmount())));
    }
    
    @When ("I set the discountamount to {$discountamount}")
    public final void setReceiptProductItemDisAmount(
            final double discountamount)
    {
        testReceiptProductItem.setDiscountAmount(discountamount);
    }
    
    @Then ("I should get discountamount {$discountamount}")
    public final void getReceiptProductItemDisAmount(
            final double discountamount)
    {
        assertThat(discountamount,
                is(equalTo(testReceiptProductItem.getDiscountAmount())));
    }    
    
    @When ("I set the productname to {$productname}")
    public final void setReceiptProductItemProdName(final String productname)
    {
        testReceiptProductItem.setProductName(productname);
    }
    
    @Then ("I should get productname {$productname}")
    public final void getReceiptProductItemProdName(final String productname)
    {
        assertThat(productname,
                is(equalTo(testReceiptProductItem.getProductName())));
    }    
    
    @When ("I set the price to {$price}")
    public final void setReceiptProductItemPrice(final long price)
    {
        testReceiptProductItem.setPrice(price);
    }
    
    @Then ("I should get price {$price}")
    public final void getReceiptProductItemPrice(final double price)
    {
        assertThat(price, is(equalTo(testReceiptProductItem.getPrice())));
    }
    
    @When ("I set the qty to {$qty}")
    public final void setReceiptProductItemQty(final int qty)
    {
        testReceiptProductItem.setQuantity(qty);
    }
    
    @Then ("I should get qty {$qty}")
    public final void getReceiptProductItemQty(final int qty)
    {
        assertThat(qty, is(equalTo(testReceiptProductItem.getQuantity())));
    }
    
//==============================================================================
//    END OF RECEIPT PRODUCT ITEM
//==============================================================================

//==============================================================================
//    RECEIPT CREDIT
//==============================================================================

    @Given ("I have a ReceiptCredit")
    public final void givenReceiptCredit()
    {
        testReceiptCredit= new ReceiptCredit();
    }
    
    @Then ("I should have a ReceiptCredit")
    public final void checkReceiptCredit()
    {
        Assert.assertNotNull(testReceiptCredit);
    }
    
    @When ("I set the expirationdate to {$expirationdate}")
    public final void setReceiptCreditExpDate(final String expirationdate)
    {
        testReceiptCredit.setExpirationdate(expirationdate);
    }
    
    @Then ("I should get expirationdate {$expirationdate}")
    public final void getReceiptCreditExpDate(final String expirationdate)
    {
        assertThat(expirationdate,
                is(equalTo(testReceiptCredit.getExpirationdate())));
    }
    
    @When ("I set the pan to {$pan}")
    public final void setReceiptCreditPan(final String pan)
    {
        testReceiptCredit.setPan(pan);
    }
    
    @Then ("I should the get pan {$pan}")
    public final void getReceiptCreditPan(final String pan)
    {
        assertThat(pan, is(equalTo(testReceiptCredit.getPan())));
    }
    
//==============================================================================
//    END OF RECEIPT CREDIT
//==============================================================================
    
    
//==============================================================================
//    PAPER RECEIPT HEADER
//==============================================================================

    @Given ("I have a PaperReceiptHeader")
    public final void givenPaperReceiptHeader()
    {
        testPaperReceiptHeader= new PaperReceiptHeader();
    }
    
    @Then ("I should have a PaperReceiptHeader")
    public final void checkPaperReceiptHeader()
    {
        Assert.assertNotNull(testPaperReceiptHeader);
    }
    
    @When ("I set the logoImg")
    public final void setPaperReceiptHeaderLogo()
    {
        BufferedImage img = new BufferedImage(50,50,
                BufferedImage.TYPE_INT_RGB);
        testPaperReceiptHeader.setLogoImg(img);
    }
    
    @Then ("I should get the same logoImg")
    public final void getPaperReceiptHeaderLogo()
    {
        BufferedImage img = testPaperReceiptHeader.getLogoImg();
        assertThat(50, is(equalTo(img.getHeight())));
        assertThat(50, is(equalTo(img.getWidth())));
        assertThat(BufferedImage.TYPE_INT_RGB, is(equalTo(img.getType())));
    }
    
    @When ("I set the address {$address}")
    public final void setPaperReceiptHeaderAddress(final String address)
    {
        testPaperReceiptHeader.setAddress(address);
    }
    
    @Then ("I should get the address {$address}")
    public final void getPaperReceiptHeaderAddress(final String address)
    {
        assertThat(address, is(equalTo(testPaperReceiptHeader.getAddress())));
    }
    
    @When ("I set the siteUrl {$siteUrl}")
    public final void setPaperReceiptHeaderURL(final String siteUrl)
    {
        testPaperReceiptHeader.setSiteUrl(siteUrl);
    }
    
    @Then ("I should get the siteURL {$siteURL}")
    public final void getPaperReceiptHeaderURL(final String siteURL)
    {
        assertThat(siteURL, is(equalTo(testPaperReceiptHeader.getSiteUrl())));
    }
    
    @When ("I set the tel {$tel}")
    public final void setPaperReceiptHeaderTel(final String tel)
    {
        testPaperReceiptHeader.setTel(tel);
    }
    
    @Then ("I should get the tel {$tel}")
    public final void getPaperReceiptHeaderTel(final String tel)
    {
        assertThat(tel, is(equalTo(testPaperReceiptHeader.getTel())));
    }
    
    @When ("I set the commercialList")
    public final void setPaperReceiptHeaderCommList()
    {
        List<String> commList = Arrays.asList("String1","String2","String3");
        testPaperReceiptHeader.setCommercialList(commList);
    }
    
    @Then ("I should get the same commercialList")
    public final void getPaperReceiptHeaderCommList()
    {
        List<String> commList = testPaperReceiptHeader.getCommercialList();
        
        assertThat("String1",is(equalTo(commList.get(0))));
        assertThat("String2",is(equalTo(commList.get(1))));
        assertThat("String3",is(equalTo(commList.get(2))));
    }
//==============================================================================
//    END OF PAPER RECEIPT HEADER
//==============================================================================

//==============================================================================
//    PAPER RECEIPT FOOTER
//==============================================================================

    @Given ("I have a PaperReceiptFooter")
    public final void givenPaperReceiptFooter()
    {
        testPaperReceiptFooter= new PaperReceiptFooter();
    }
    
    @Then ("I should have a PaperReceiptFooter")
    public final void checkPaperReceiptFooter()
    {
        Assert.assertNotNull(testPaperReceiptFooter);
    }
    
    @When ("I set the shopname to {$shopname}")
    public final void setPaperReceiptFooterShopName(final String shopname)
    {
        testPaperReceiptFooter.setShopName(shopname);
    }
    
    @Then ("I should get the shopname {$shopname}")
    public final void getPaperReceiptFooterShopName(final String shopname)
    {
        assertThat(shopname,is(equalTo(testPaperReceiptFooter.getShopName())));
    }
    
    @When ("I set the registernum to {$registernum}")
    public final void setPaperReceiptFooterRegNum(final String registernum)
    {
        testPaperReceiptFooter.setRegisterNum(registernum);
    }
    
    @Then ("I should get the registernum {$registernum}")
    public final void getPaperReceiptFooterRegNum(final String registernum)
    {
        assertThat(registernum,is(equalTo(testPaperReceiptFooter
                .getRegisterNum())));
    }
    
    @When ("I set the departmentname to {$departmentname}")
    public final void setPaperReceiptFooterDeptNum(final String departmentname)
    {
        testPaperReceiptFooter.setDepartmentName(departmentname);
    }
    
    @Then ("I should get the departmentname {$departmentname}")
    public final void getPaperReceiptFooterDeptNum(final String departmentname)
    {
        assertThat(departmentname,is(equalTo(testPaperReceiptFooter
                .getDepartmentName())));
    }
    
    @When ("I set the tradenumber to {$tradenumber}")
    public final void setPaperReceiptFooterTradeNum(final String tradenumber)
    {
        testPaperReceiptFooter.setTradeNum(tradenumber);
    }
    
    @Then ("I should get the tradenumber {$tradenumber}")
    public final void getPaperReceiptFooterTradeNum(final String tradenumber)
    {
        assertThat(tradenumber,is(equalTo(testPaperReceiptFooter
                .getTradeNum())));
    }
    
    @When ("I set the saleman to {$saleman}")
    public final void setPaperReceiptFooterSaleMan(final String saleman)
    {
        testPaperReceiptFooter.setSaleMan(saleman);
    }
    
    @Then ("I should get the saleman {$saleman}")
    public final void getPaperReceiptFooterSaleMan(final String saleman)
    {
        assertThat(saleman,is(equalTo(testPaperReceiptFooter.getSaleMan())));
    }
    
    @When ("I set the holdname to {$holdname}")
    public final void setPaperReceiptFooterHoldName(final String holdname)
    {
        testPaperReceiptFooter.setHoldName(holdname);
    }
    
    @Then ("I should get the holdname {$holdname}")
    public final void getPaperReceiptFooterHoldName(final String holdname)
    {
        assertThat(holdname,is(equalTo(testPaperReceiptFooter.getHoldName())));
    }
    
//==============================================================================
//    END OF PAPER RECEIPT FOOTER
//==============================================================================

    
//==============================================================================
//    PAPER RECEIPT CREDIT
//==============================================================================

    @Given ("I have a PaperReceiptCredit")
    public final void givenPaperReceiptCredit()
    {
        testPaperReceiptCredit= new PaperReceiptCredit();
    }
    
    @Then ("I should have a PaperReceiptCredit")
    public final void checkPaperReceiptCredit()
    {
        Assert.assertNotNull(testPaperReceiptCredit);
    }
    
    @When ("I set the creditcompany to {$creditcompany}")
    public final void setPaperReceiptCreditCreditCo(final String creditcompany)
    {
        testPaperReceiptCredit.setCreditCompany(creditcompany);
    }
    
    @Then ("I should get the creditcompany {$creditcompany}")
    public final void getPaperReceiptCreditCreditCo(final String creditcompany)
    {
        assertThat(creditcompany,
                is(equalTo(testPaperReceiptCredit.getCreditCompany())));
    }
    
    @When ("I set the jcb to {$jcb}")
    public final void setPaperReceiptCreditJcb(final String jcb)
    {
        testPaperReceiptCredit.setJcb(jcb);
    }
    
    @Then ("I should get the jcb {$jcb}")
    public final void getPaperReceiptCreditJcb(final String jcb)
    {
        assertThat(jcb, is(equalTo(testPaperReceiptCredit.getJcb())));
    }
    
    @When ("I set the creditnum to {$creditnum}")
    public final void setPaperReceiptCreditCreditNum(final String creditnum)
    {
        testPaperReceiptCredit.setCreditNum(creditnum);
    }
    
    @Then ("I should get the creditnum {$creditnum}")
    public final void getPaperReceiptCreditCreditNum(final String creditnum)
    {
        assertThat(creditnum,
                is(equalTo(testPaperReceiptCredit.getCreditNum())));
    }
    
    @When ("I set the creditexpiration to {$creditexpiration}")
    public final void setPaperReceiptCreditCreditExp(
            final String creditexpiration)
    {
        testPaperReceiptCredit.setCreditExpiration(creditexpiration);
    }
    
    @Then ("I should get the creditexpiration {$creditexpiration}")
    public final void getPaperReceiptCreditCreditExp(
            final String creditexpiration)
    {
        assertThat(creditexpiration,
                is(equalTo(testPaperReceiptCredit.getCreditExpiration())));
    }
    
    @When ("I set the creditslipnum to {$creditslipnum}")
    public final void setPaperReceiptCreditCreditSlipNum(
            final String creditslipnum)
    {
        testPaperReceiptCredit.setCreditSlipNum(creditslipnum);
    }
    
    @Then ("I should get the creditslipnum {$creditslipnum}")
    public final void getPaperReceiptCreditCreditSlipNum(
            final String creditslipnum)
    {
        assertThat(creditslipnum,
                is(equalTo(testPaperReceiptCredit.getCreditSlipNum())));
    }
    
    @When ("I set the creditamount to {$creditamount}")
    public final void setPaperReceiptCreditCreditAmount(
            final String creditamount)
    {
        testPaperReceiptCredit.setCreditAmount(creditamount);
    }
    
    @Then ("I should get the credit amount {$creditamount}")
    public final void getPaperReceiptCreditCreditAmount(
            final String creditamount)
    {
        assertThat(creditamount,
                is(equalTo(testPaperReceiptCredit.getCreditAmount())));
    }
    
    @When ("I set the approvalnum to {$approvalnum}")
    public final void setPaperReceiptCreditApprovNum(final String approvalnum)
    {
        testPaperReceiptCredit.setApprovalNum(approvalnum);
    }
    
    @Then ("I should get the approvalnum {$approvalnum}")
    public final void getPaperReceiptCreditApprovNum(final String approvalnum)
    {
        assertThat(approvalnum,
                is(equalTo(testPaperReceiptCredit.getApprovalNum())));
    }
    
    @When ("I set the creditprocessnum to {$creditprocessnum}")
    public final void setPaperReceiptCreditCredProcNum(
            final String creditprocessnum)
    {
        testPaperReceiptCredit.setCreditProcessNum(creditprocessnum);
    }
    
    @Then ("I should get the creditprocessnum {$creditprocessnum}")
    public final void getPaperReceiptCreditCredProcNum(
            final String creditprocessnum)
    {
        assertThat(creditprocessnum,
                is(equalTo(testPaperReceiptCredit.getCreditProcessNum())));
    }
    
    @When ("I set the terminalidnum to {$terminalidnum}")
    public final void setPaperReceiptCreditTermIdNum(final String terminalidnum)
    {
        testPaperReceiptCredit.setTerminalIdentificationNum(terminalidnum);
    }
    
    @Then ("I should get the terminalidnum {$terminalidnum}")
    public final void getPaperReceiptCreditTermIdNum(final String terminalidnum)
    {
        assertThat(terminalidnum,
                is(equalTo(testPaperReceiptCredit
                        .getTerminalIdentificationNum())));
    }
    
    @When ("I set the settlementmannum to {$settlementmannum}")
    public final void setPaperReceiptCreditSetManNum(
            final String settlementmannum)
    {
        testPaperReceiptCredit.setSettlementManageNum(settlementmannum);
    }
    
    @Then ("I should get the settlementmannum {$settlementmannum}")
    public final void getPaperReceiptCreditSetManNum(
            final String settlementmannum)
    {
        assertThat(settlementmannum,
                is(equalTo(testPaperReceiptCredit.getSettlementManageNum())));
    }
    
//==============================================================================
//    END OF PAPER RECEIPT CREDIT
//==============================================================================
    
    
    
//==============================================================================
//    PAPER RECEIPT CONTENT
//==============================================================================

    @Given ("I have a PaperReceiptContent")
    public final void givenPaperReceiptContent()
    {
        testPaperReceiptContent= new PaperReceiptContent();
    }
    
    @Then ("I should have a PaperReceiptContent")
    public final void checkPaperReceiptContent()
    {
        Assert.assertNotNull(testPaperReceiptContent);
    }
    
    @When ("I set the receiptdatetime to {$receiptdatetime}")
    public final void setPaperReceiptContentRecDateTime(
            final String receiptdatetime)
    {
        testPaperReceiptContent.setReceiptDateTime(receiptdatetime);
    }
    
    @Then ("I should get the receiptdatetime {$receiptdatetime}")
    public final void getPaperReceiptContentRecDateTime(
            final String receiptdatetime)
    {
        assertThat(receiptdatetime,
                is(equalTo(testPaperReceiptContent.getReceiptDateTime())));
    }
    
    @When ("I set the contentdiscount to {$contentdiscount}")
    public final void setPaperReceiptContentDiscount(
            final String contentdiscount)
    {
        testPaperReceiptContent.setDiscount(contentdiscount);
    }
    
    @Then ("I should get the contentdiscount {$contentdiscount}")
    public final void getPaperReceiptContentDiscount(
            final String contentdiscount)
    {
        assertThat(contentdiscount,
                is(equalTo(testPaperReceiptContent.getDiscount())));
    }
    
    @When ("I set the contenttax to {$contenttax}")
    public final void setPaperReceiptContentTax(final String contenttax)
    {
        testPaperReceiptContent.setTax(contenttax);
    }
    
    @Then ("I should get the contenttax {$contenttax}")
    public final void getPaperReceiptContentTax(final String contenttax)
    {
        assertThat(contenttax, is(equalTo(testPaperReceiptContent.getTax())));
    }
    
    @When ("I set the contenttotal to {$contenttotal}")
    public final void setPaperReceiptContentTotal(final long contenttotal)
    {
        testPaperReceiptContent.setTotal(contenttotal);
    }
    
    @Then ("I should get the contenttotal {$contenttotal}")
    public final void getPaperReceiptContentTotal(final double contenttotal)
    {
        assertThat(contenttotal,
                is(equalTo(testPaperReceiptContent.getTotal())));
    }
    
    @When ("I set the contentpaycred to {$contentpaycred}")
    public final void setPaperReceiptContentPayCredit(final long contentpaycred)
    {
        testPaperReceiptContent.setPaymentCredit(contentpaycred);
    }
    
    @Then ("I should get the contentpaycred {$contentpaycred}")
    public final void getPaperReceiptContentPayCredit(final long contentpaycred)
    {
        assertThat(contentpaycred,
                is(equalTo(testPaperReceiptContent.getPaymentCredit())));
    }
    
    @When ("I set the contentpaycash to {$contentpaycash}")
    public final void setPaperReceiptContentPayCash(final long contentpaycash)
    {
        testPaperReceiptContent.setPaymentCash(contentpaycash);
    }
    
    @Then ("I should get the contentpaycash {$contentpaycash}")
    public final void getPaperReceiptContentPayCash(final long contentpaycash)
    {
        assertThat(contentpaycash,
                is(equalTo(testPaperReceiptContent.getPaymentCash())));
    }
    
    @When ("I set the contentpaychange to {$contentpaychange}")
    public final void setPaperReceiptContentPayChange(
            final long contentpaychange)
    {
        testPaperReceiptContent.setPaymentChange(contentpaychange);
    }
    
    @Then ("I should get the contentpaychange {$contentpaychange}")
    public final void getPaperReceiptContentPayChange(
            final double contentpaychange)
    {
        assertThat(contentpaychange,
                is(equalTo(testPaperReceiptContent.getPaymentChange())));
    }
    
    @When ("I set the subtotal to {$subtotal}")
    public final void setPaperReceiptContentSubTotal(final long subtotal)
    {
        testPaperReceiptContent.setSubTotal(subtotal);
    }
    
    @Then ("I should get the subtotal {$subtotal}")
    public final void getPaperReceiptContentSubTotal(final double subtotal)
    {
        assertThat(subtotal,
                is(equalTo(testPaperReceiptContent.getSubTotal())));
    }
    
    @When ("I set the productItemList")
    public final void setPaperReceiptProdItemList()
    {
        ReceiptProductItem a = new ReceiptProductItem();
        ReceiptProductItem b = new ReceiptProductItem();
        
        a.setAmount(100);
        b.setAmount(200);
        
        a.setDiscountAmount(100);
        b.setDiscountAmount(200);
        
        a.setPrice(1000);
        b.setPrice(2000);
        
        a.setProductName("prod1");
        b.setProductName("prod2");
        
        a.setQuantity(1);
        b.setQuantity(2);
        
        List<ReceiptProductItem> prodList = Arrays.asList(a,b);
        
        testPaperReceiptContent.setProductItemList(prodList);
    }
    
    @Then ("I should get the same productItemList")
    public final void getPaperReceiptProdItemList()
    {
        List<ReceiptProductItem> prodList =
            testPaperReceiptContent.getProductItemList();
        
        ReceiptProductItem rpi = prodList.get(0);
        
        assertThat((double)100, is(equalTo(rpi.getAmount())));
        assertThat((double)1000, is(equalTo(rpi.getPrice())));
        assertThat(1, is(equalTo(rpi.getQuantity())));
        assertThat((double)100, is(equalTo(rpi.getDiscountAmount())));
        assertThat("prod1", is(equalTo(rpi.getProductName())));
        
        rpi = prodList.get(1);
        
        assertThat((double)200, is(equalTo(rpi.getAmount())));
        assertThat((double)2000, is(equalTo(rpi.getPrice())));
        assertThat(2, is(equalTo(rpi.getQuantity())));
        assertThat((double)200, is(equalTo(rpi.getDiscountAmount())));
        assertThat("prod2", is(equalTo(rpi.getProductName())));
    }
    
//==============================================================================
//    END OF PAPER RECEIPT CONTENT
//==============================================================================

    
    @Given("I have an empty Paper Receipt")
    public final void EmptyPaperReceipt(){        
        testPaperReceipt = new PaperReceipt();
    }
    
    @Then("I should have an empty Paper Receipt")
    public final void getEmptyPaperReceipt()
    {
        Assert.assertNotNull(testPaperReceipt);
        Assert.assertNotNull(testPaperReceipt.getReceiptContent());
        Assert.assertNotNull(testPaperReceipt.getReceiptPayment());
        Assert.assertNotNull(testPaperReceipt.getReceiptFooter());
        Assert.assertNotNull(testPaperReceipt.getReceiptHeader());        
    }
    
    @Given ("I have a full Paper Receipt")
    public final void FullPaperReceipt()
    {
        PaperReceiptFooter prf = new PaperReceiptFooter();
        PaperReceiptHeader prh = new PaperReceiptHeader();
        PaperReceiptContent prc = new PaperReceiptContent();
        PaperReceiptPayment payment = new PaperReceiptPayment();
        
        prf.setDepartmentName("dept");
        prh.setSiteUrl("url");
        prc.setReceiptDateTime("todaytime");
        payment.setApprovalCode("123");
        
        
        testPaperReceipt = new PaperReceipt(prh, prc, prf);
        testPaperReceipt.setReceiptPayment(payment);
    }
    
    @Then ("I should have a full Paper Receipt")
    public final void getFullPaperReceipt()
    {
        getEmptyPaperReceipt();
        
        assertThat("dept",is(equalTo(testPaperReceipt
                .getReceiptFooter().getDepartmentName())));
        assertThat("url",is(equalTo(testPaperReceipt
                .getReceiptHeader().getSiteUrl())));
        assertThat("todaytime",is(equalTo(testPaperReceipt
                .getReceiptContent().getReceiptDateTime())));
        assertThat("123",is(equalTo(testPaperReceipt
                .getReceiptPayment().getApprovalCode())));
    }
    
    @When ("I set the paperreceiptfooter")
    public final void setPaperReceiptReceiptFooter()
    {
        PaperReceiptFooter prf = new PaperReceiptFooter();
        
        prf.setDepartmentName("dept");
        prf.setHoldName("holdname");
        
        testPaperReceipt.setReceiptFooter(prf);
    }

    @Then ("I should get the same paperreceiptfooter")
    public final void getPaperReceiptReceiptFooter()
    {
        PaperReceiptFooter prf = testPaperReceipt.getReceiptFooter();
        
        Assert.assertNotNull(prf);
        assertThat("dept",is(equalTo(testPaperReceipt
                .getReceiptFooter().getDepartmentName())));
        assertThat("holdname",is(equalTo(testPaperReceipt
                .getReceiptFooter().getHoldName())));
    }
    
    @When ("I set the paperreceiptheader")
    public final void setPaperReceiptReceiptHeader()
    {
        PaperReceiptHeader prh = new PaperReceiptHeader();
        
        prh.setAddress("address");
        prh.setSiteUrl("url");
        
        testPaperReceipt.setReceiptHeader(prh);
    }

    @Then ("I should get the same paperreceiptheader")
    public final void getPaperReceiptReceiptHeader()
    {
        PaperReceiptHeader prh = testPaperReceipt.getReceiptHeader();
        
        Assert.assertNotNull(prh);
        assertThat("address",is(equalTo(testPaperReceipt
                .getReceiptHeader().getAddress())));
        assertThat("url",is(equalTo(testPaperReceipt
                .getReceiptHeader().getSiteUrl())));
    }
    
    @When ("I set the paperreceiptcontent")
    public final void setPaperReceiptReceiptContent()
    {
        PaperReceiptContent prc = new PaperReceiptContent();
        
        prc.setDiscount("discount");
        prc.setTax("tax");
        
        testPaperReceipt.setReceiptContent(prc);
    }

    @Then ("I should get the same paperreceiptcontent")
    public final void getPaperReceiptReceiptContent()
    {
        PaperReceiptContent prc = testPaperReceipt.getReceiptContent();
        
        Assert.assertNotNull(prc);
        assertThat("discount",is(equalTo(testPaperReceipt
                .getReceiptContent().getDiscount())));
        assertThat("tax",is(equalTo(testPaperReceipt
                .getReceiptContent().getTax())));
    }
    
    @When ("I set the paperreceiptcredit")
    public final void setPaperReceiptReceiptCredit()
    {
        PaperReceiptPayment prcred = new PaperReceiptPayment();
        
        prcred.setApprovalCode("approval");
        prcred.setPanLast4("123");
        
        testPaperReceipt.setReceiptPayment(prcred);
    }

    @Then ("I should get the same paperreceiptcredit")
    public final void getPaperReceiptReceiptCredit()
    {
        PaperReceiptPayment prc = testPaperReceipt.getReceiptPayment();
        
        Assert.assertNotNull(prc);
        assertThat("approval",is(equalTo(testPaperReceipt
                .getReceiptPayment().getApprovalCode())));
        assertThat("123",is(equalTo(testPaperReceipt
                .getReceiptPayment().getPanLast4())));
    }
}
