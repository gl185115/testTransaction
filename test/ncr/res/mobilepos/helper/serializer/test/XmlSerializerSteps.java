package ncr.res.mobilepos.helper.serializer.test;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.*;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class XmlSerializerSteps extends Steps {
    private XmlSerializer<PosLog> xmlserializerPoslog;
    private XmlSerializer<Sale> xmlserializerSale;
    private XmlSerializer<Tender> xmlserializerTender;
    
    private PosLog poslog;
    private Transaction transaction;
    private Sale sale;
    private Tender tender;
    private RetailTransaction customerOrderTransaction;
    private LineItem lineItem;
    private List<LineItem> lineItems;
    private Discount discount;
    private Return returnActual;
    private Disposal actualDisposal;

    @Given("I have a XmlSerializer for PosLog")
    public final void IHaveAXmlSerializerForPosLog()
    {
        xmlserializerPoslog = new XmlSerializer<PosLog>();
    }
    
    @When("I have an POSLog xml: $xml")
    public final void IHaveAPOSLogXml(final String xml) throws JAXBException
    {
        poslog = xmlserializerPoslog.unMarshallXml(xml, PosLog.class);
    }
    
    
    @Given("I have a XmlSerializer for Sale")
    public final void IHaveAXmlSerializerForSale()
    {
        xmlserializerSale = new XmlSerializer<Sale>();
    }
    
    @When("I have an Sale xml: $xml")
    public final void IHaveSaleXml(final String xml) throws JAXBException
    {
        sale = xmlserializerSale.unMarshallXml(xml, Sale.class);
    }
    
    @Then("I should get Sale: $result")
    public final void IShouldGetSale(final ExamplesTable result)
    {
        Sale saleExpected = new Sale();

        Map<String, String> row = result.getRows().get(0);

        saleExpected.setDescription(row.get("Description"));
        saleExpected.setExtendedAmt(Long.parseLong(row.get("ExtendedAmount")));
        saleExpected.setItemID(new ItemID());
        saleExpected.getItemID().setPluCode(row.get("ItemID"));
        saleExpected.getItemID().setType(row.get("ItemType"));
        saleExpected.setQuantity(Integer.parseInt(row.get("Quantity")));
        saleExpected.setTaxableFlag(row.get("TaxableFlag"));
        saleExpected.setDepartment(row.get("Department"));
        saleExpected.setLine(row.get("Line"));
        saleExpected.setClas(row.get("Class"));

        assertThat(saleExpected.getDescription(),
                is(equalTo(sale.getDescription())));
        assertThat(saleExpected.getExtendedAmt(),
                is(equalTo(sale.getExtendedAmt())));
        assertThat(saleExpected.getItemID().getPluCode(),
                is(equalTo(sale.getItemID().getPluCode())));
        assertThat(saleExpected.getItemID().getType(),
                is(equalTo(sale.getItemID().getType())));
        assertThat(saleExpected.getQuantity(),
                is(equalTo(sale.getQuantity())));
        assertThat(saleExpected.getTaxableFlag(),
                is(equalTo(sale.getTaxableFlag())));
        assertThat(saleExpected.getClas(),
                is(equalTo(sale.getClas())));
        assertThat(saleExpected.getDepartment(),
                is(equalTo(sale.getDepartment())));
        assertThat(saleExpected.getLine(),
                is(equalTo(sale.getLine())));
     }
    
    @Given("I have a XmlSerializer for Tender")
    public final void IHaveAXmlSerializerForTest()
    {
        xmlserializerTender = new XmlSerializer<Tender>();
    }
    
    @When("I have an Tender xml: $xml")
    public final void IHaveTestXml(final String xml) throws JAXBException
    {
        tender = xmlserializerTender.unMarshallXml(xml, Tender.class);
    }
    
    @Then("I should get Tender: $tender")
    public final void IShouldGetTender(final ExamplesTable result)
    {
        Tender tenderExpected = new Tender();
        
        Map<String, String> row = result.getRows().get(0);
        
        Authorization authorization = new Authorization();
        authorization.setAuthorizationCode(row.get("authorizationcode"));
        
        CreditDebit creditdebit = new CreditDebit();
        creditdebit.setCardType(row.get("cardtype"));
        creditdebit.setCreditCardCompanyCode(row.get("creditcardcompanycode"));
        creditdebit.setExpirationDate(row.get("expirationdate"));

        TenderChange tenderChange = new TenderChange();
        if(row.get("tenderchange") != null){
            tenderChange.setAmount(Double.parseDouble(row.get("tenderchange")));
        }
        
        
        tenderExpected.setAmount(row.get("amount"));
        tenderExpected.setTenderType(row.get("tendertype"));
        tenderExpected.setTypeCode(row.get("typecode"));
        tenderExpected.setCreditDebit(creditdebit);
        tenderExpected.setAuthorization(authorization);
        tenderExpected.setTenderChange(tenderChange);
        
        assertThat(tender.getAmount(),
                is(equalTo(tenderExpected.getAmount())));
        assertThat(tender.getAuthorization().getAuthorizationCode(), 
                is(equalTo(tenderExpected
                            .getAuthorization()
                            .getAuthorizationCode())));
        assertThat(tender.getCreditDebit().getCardType(), 
                is(equalTo(tenderExpected.getCreditDebit().getCardType())));
        assertThat(tender.getCreditDebit().getCreditCardCompanyCode(), 
                is(equalTo(tenderExpected
                            .getCreditDebit()
                            .getCreditCardCompanyCode())));
        assertThat(tender.getCreditDebit().getExpirationDate(), 
                is(equalTo(tenderExpected
                            .getCreditDebit()
                            .getExpirationDate())));
        assertThat(tender.getTenderType(),
                            is(equalTo(tenderExpected.getTenderType())));
        assertThat(tender.getTypeCode(),
                    is(equalTo(tenderExpected.getTypeCode())));
        assertThat(tender.getTenderChange().getAmount(),
                is(equalTo(tenderExpected.getTenderChange().getAmount())));
     }
    
    @Then("I should get POSLog")
    public final void IShouldGetPOSLog()
    {
        transaction = poslog.getTransaction();
    }
    
    @Then("I should get Transaction: $transaction")
    public final void IShouldGetTransaction(final ExamplesTable result)
    {
        Transaction transactionExpected = new Transaction();
        
        Map<String, String> row = result.getRow(0);
        
        String beginDateTime = row.get("BeginDateTime");
        String businessDayDate = row.get("BusinessDayDate");
        String retailStoreID = row.get("RetailStoreID");
        String sequenceNo = row.get("SequenceNumber");
        WorkstationID workStationID = new WorkstationID();
        workStationID.setValue(row.get("WorkstationID"));
        OperatorID operatorID = new OperatorID();
        operatorID.setValue(row.get("OperatorID"));
        String workStationSubID = row.get("WorkstationSubID");
        String customerID = row.get("CustomerID");

        transactionExpected.setBeginDateTime(beginDateTime);
        transactionExpected.setBusinessDayDate(businessDayDate);
        transactionExpected.setRetailStoreID(retailStoreID);
        transactionExpected.setSequenceNo(sequenceNo);
        transactionExpected.setWorkStationID(workStationID);
        transactionExpected.setOperatorID(operatorID);
        transactionExpected.setCustomerid(customerID);
        
        assertThat("BeginDateTime", transaction.getBeginDateTime(),
                is(equalTo(transactionExpected.getBeginDateTime())));
        assertThat("BusinessDayDate", transaction.getBusinessDayDate(),
                is(equalTo(transactionExpected.getBusinessDayDate())));
        assertThat("RetailStoreID", transaction.getRetailStoreID(),
                is(equalTo(transactionExpected.getRetailStoreID())));
        assertThat("SequenceNo", transaction.getSequenceNo(),
                is(equalTo(transactionExpected.getSequenceNo())));
        assertThat("WorkStationID", transaction.getWorkStationID(),
                is(equalTo(transactionExpected.getWorkStationID())));
        assertThat("OperatorID", transaction.getOperatorID(),
                is(equalTo(transactionExpected.getOperatorID())));
        assertThat("Customerid", transaction.getCustomerid(),
                is(equalTo(transactionExpected.getCustomerid())));

        customerOrderTransaction = transaction.getRetailTransaction();
    }
    
    @Then("I should get Return: $ret")
    public final void IShouldGetReturn(final ExamplesTable ret)
    {
        Return returnExpected = new Return();
        returnActual = lineItem.getRetrn();
        
        Map<String, String> row = ret.getRow(0);
                
        returnExpected.setItemType(row.get("ItemType"));
        returnExpected.setDepartment(row.get("Department"));
        returnExpected.setLine(row.get("Line"));
        returnExpected.setClas(row.get("Class"));
        ItemID itemId = new ItemID();
        itemId.setPluCode(row.get("ItemID"));
        returnExpected.setItemID(itemId);
        returnExpected.setDescription(row.get("Description"));
        returnExpected.setActualsalesunitprice(
                Long.parseLong(row.get("ActualSalesUnitPrice")));
        returnExpected.setExtendedAmt(
                Long.parseLong(row.get("ExtendedAmount")));
        returnExpected.setQuantity(Integer.parseInt(row.get("Quantity")));
        
        assertThat("ItemType", returnActual.getItemType(),
                is(equalTo(returnExpected.getItemType())));
        assertThat("Department", returnActual.getDepartment(),
                is(equalTo(returnExpected.getDepartment())));
        assertThat("Line", returnActual.getLine(),
                is(equalTo(returnExpected.getLine())));
        assertThat("Class", returnActual.getClas(),
                is(equalTo(returnExpected.getClas())));
        assertThat("ItemID", returnActual.getItemID().getPluCode(),
                is(equalTo(returnExpected.getItemID().getPluCode())));
        assertThat("Description", returnActual.getDescription(),
                is(equalTo(returnExpected.getDescription())));
        assertThat("ActualSalesUnitPrice",
                returnActual.getActualsalesunitprice(),
                is(equalTo(returnExpected.getActualsalesunitprice())));
        assertThat("ExtendedAmount", returnActual.getExtendedAmt(),
                is(equalTo(returnExpected.getExtendedAmt())));
        assertThat("Quantity", returnActual.getQuantity(),
                is(equalTo(returnExpected.getQuantity())));
    }
    
    @Then("I should get Disposal: $ret")
    public final void IShouldGetDisposal(final ExamplesTable ret)
    {
        Disposal disposalExpected = new Disposal();
        actualDisposal = returnActual.getDisposal();
        
        Map<String, String> row = ret.getRow(0);
                
        disposalExpected.setMethod(row.get("Method"));
            
        assertThat("Quantity", actualDisposal.getMethod(),
                is(equalTo(disposalExpected.getMethod())));
    }
    
    @Then("I should get CustomerOrderTransaction: $customerOrderTransaction")
    public final void IShouldGetCustomerOrderTransaction(
            final ExamplesTable result)
    {
        RetailTransaction customerOrderTransExpted =
            new RetailTransaction();
        
        Map<String, String> row = result.getRow(0);
        
        customerOrderTransExpted.setItemCount(
                Integer.parseInt(row.get("ItemCount")));
        
        assertThat((customerOrderTransaction != null)?customerOrderTransaction.getItemCount():0,
                is(equalTo(customerOrderTransExpted.getItemCount())));
        
        lineItems = customerOrderTransaction.getLineItems();
    }
    
    @Then("I should get one of the following in  LineItems: $lineItem")
    public final void IShouldGetLineItem(final ExamplesTable result)
    {
        LineItem lineItemExpected = new LineItem();
        
        Map<String, String> row = result.getRow(0);
        String accountcode = row.get("AccountCode");
        
        if (accountcode !=  null && accountcode.equals("null")) {
            accountcode = null;
        }
        
        int index = Integer.parseInt(row.get("SequenceNumber"));
         //lineItemExpected.setSales(sales);    
        lineItemExpected.setSequenceNo(index);
         //Array Line Numbering starts from 0,
        // while LineItem Numbering starts from 1
        lineItem = lineItems.get(index - 1);
        
        assertThat(lineItem.getSequenceNo(),
                is(equalTo(lineItemExpected.getSequenceNo())));
        
        tender = lineItem.getTender();
        sale = lineItem.getSale();
        discount = lineItem.getDiscount();
        
        //Test The Accountcode to be returned in each LineItem
        assertThat(lineItem.getAccountCode(), is(equalTo(accountcode)));
        
    }
    
    @Then("I should get Discount: $discount")
    public final void IShouldGetDiscount(final ExamplesTable result)
    {
        
        Map<String, String> row = result.getRows().get(0);
        
        Discount discountexpected = new Discount();

        
        discountexpected.setAmount(row.get("amount"));    
    
        
        assertThat(discount.getAmount(),
                is(equalTo(discountexpected.getAmount())));
    }
    
    @Then("Tender should be NULL")
    public final void TenderShouldBeNULL()
    {
        Assert.assertNull(tender);
    }
    
    @Then("Sale should be NULL")
    public final void SaleShouldBeNULL()
    {
        Assert.assertNull(sale);
    }
    
    @Then("Discount should be NULL")
    public final void DiscountShouldBeNULL()
    {
        Assert.assertNull(discount);
    }
    
    @Then("I should get Customer Demographics of $expectedDemographics")
    public final void iShouldGetCustomerDemographicsOf(final String expectedDemographics) {
        Assert.assertEquals("Compare the demographics", expectedDemographics, customerOrderTransaction.getCustomer().getCustomerDemographic());
    }
    
    @Then("I should get PriceDerivationResult: $expectedPriceDerivationResults")
    public final void IShouldGetPriceDerivationResult(final ExamplesTable expectedPriceDerivationResults) {
        Map<String, String> expectedPriceDerivationResultsMap = expectedPriceDerivationResults.getRow(0);
        List<PriceDerivationResult> actualpriceDerivationResults = poslog.getTransaction().getRetailTransaction().getPriceDerivationResult();
        PriceDerivationResult actualpriceDericationResult = actualpriceDerivationResults.get(0);
        Assert.assertEquals("Compare the MethodCode", expectedPriceDerivationResultsMap.get("MethodCode"), actualpriceDericationResult.getMethodCode());
        Assert.assertEquals("Compare the SequenceNumber", Integer.parseInt(expectedPriceDerivationResultsMap.get("SequenceNumber")), actualpriceDericationResult.getSequenceNumber());
        Assert.assertEquals("Compare the Amount_Action", expectedPriceDerivationResultsMap.get("Amount_Action"), actualpriceDericationResult.getAmount().getAction());
        Assert.assertEquals("Compare the Amount_Value", Long.parseLong(expectedPriceDerivationResultsMap.get("Amount_Value")),
                                        (long)actualpriceDericationResult.getAmount().getAmount());
        Assert.assertEquals("Compare the PreviousPrice", Long.parseLong(expectedPriceDerivationResultsMap.get("PreviousPrice")),
                                        (long)actualpriceDericationResult.getPreviousPrice());
        Assert.assertEquals("Compare the PriceDerivationRuleID", expectedPriceDerivationResultsMap.get("PriceDerivationRuleID"), actualpriceDericationResult.getPriceDerivationRule().getPriceDerivationRuleID());
        Assert.assertEquals("Compare the PriceDerivationDescription", expectedPriceDerivationResultsMap.get("PriceDerivationDescription"), actualpriceDericationResult.getPriceDerivationRule().getDescription());
    }
    
    @Then("I should get RetailPriceModifier: $expectedPriceDerivationResults")
    public final void IShouldGetRetailPriceModifier(final ExamplesTable expectedRetailPriceModifier ) {
        Map<String, String> expectedRetailPriceModifierMap  = expectedRetailPriceModifier .getRow(0);
        List<RetailPriceModifier> actualRetailPriceModifierList = sale.getRetailPriceModifier();
        RetailPriceModifier actualRetailPriceModifier = actualRetailPriceModifierList.get(0);
        Assert.assertEquals("Compare the MethodCode", expectedRetailPriceModifierMap.get("MethodCode"), actualRetailPriceModifier.getMethodCode());
        Assert.assertEquals("Compare the SequenceNumber", Integer.parseInt(expectedRetailPriceModifierMap.get("SequenceNumber")), actualRetailPriceModifier.getSequenceNumber());
        Assert.assertEquals("Compare the Amount_Action", expectedRetailPriceModifierMap.get("Amount_Action"), actualRetailPriceModifier.getAmount().getAction());
        Assert.assertEquals("Compare the Amount_Value", Long.parseLong(expectedRetailPriceModifierMap.get("Amount_Value")),
                                        (long)actualRetailPriceModifier.getAmount().getAmount());
        Assert.assertEquals("Compare the PriceDerivationRuleID", expectedRetailPriceModifierMap.get("PriceDerivationRuleID"), actualRetailPriceModifier.getPriceDerivationRule().getPriceDerivationRuleID());
        
        String priceDervatioDescription = expectedRetailPriceModifierMap.get("PriceDerivationDescription");
        if (priceDervatioDescription.equals("null")) {
            priceDervatioDescription = null;
        }
        Assert.assertEquals("Compare the PriceDerivationDescription", priceDervatioDescription,
                actualRetailPriceModifier.getPriceDerivationRule().getDescription());
    }
    
    @Then("I should get Tax: $expectedTaxTable")
    public final void  IShouldGetTax(final ExamplesTable expectedTaxTable) {
        Map<String, String> expectedTax = expectedTaxTable.getRow(0);
        List<LineItem> lineItemList = poslog.getTransaction().getRetailTransaction().getLineItems();
        //The tax is in the Last line Item.
        List<Tax> actualtax = lineItemList.get(lineItemList.size() - 1).getTax();
        Assert.assertEquals("Compare the TaxType", expectedTax.get("TaxType"), actualtax.get(actualtax.size() - 1).getTaxType());
        Assert.assertEquals("Compare the Percent", expectedTax.get("Percent"), actualtax.get(actualtax.size() - 1).getPercent());
        Assert.assertEquals("Compare the Amount", expectedTax.get("Amount"), actualtax.get(actualtax.size() - 1).getAmount());
        Assert.assertEquals("Compare the TaxIncludedInTaxableAmountFlag", expectedTax.get("TaxIncludedInTaxableAmountFlag"),
               actualtax.get(actualtax.size() - 1).getTaxableAmt().getTaxIncludedInTaxableAmountFlag());
        Assert.assertEquals("Compare the TaxableAmount", expectedTax.get("TaxableAmount"),
                actualtax.get(actualtax.size() - 1).getTaxableAmt().getAmount());
    }
}
