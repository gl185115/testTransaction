package ncr.res.mobilepos.barcodeassignment.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.barcodeassignment.model.ItemSale;
import ncr.res.mobilepos.barcodeassignment.model.Sale;

public class BarcodeAssignmentUtilityTest {
    @Test
    public void isMultiLineBarcode() throws Exception {
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("123 123"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("1234567890 123456789"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("  123  123  "));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode(" 123  123"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("456  456"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("789789 789789 789789"));

        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode("789789"));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 789789 "));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 789789"));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode("789789 "));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 78978 "));
    }

    @Test
    public void splitDoubleBarcode() throws Exception {
        String[] checkArray;
        checkArray = BarcodeAssignmentUtility.splitDoubleBarcode("123 456");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("123", checkArray[0]);
        Assert.assertEquals("456", checkArray[1]);

        checkArray =     BarcodeAssignmentUtility.splitDoubleBarcode("1234567890 4567890123");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("1234567890", checkArray[0]);
        Assert.assertEquals("4567890123", checkArray[1]);

        checkArray =     BarcodeAssignmentUtility.splitDoubleBarcode("  123  456  ");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("123", checkArray[0]);
        Assert.assertEquals("", checkArray[1]);

        checkArray =     BarcodeAssignmentUtility.splitDoubleBarcode("  123 456  ");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("123", checkArray[0]);
        Assert.assertEquals("456", checkArray[1]);

        checkArray =       BarcodeAssignmentUtility.splitDoubleBarcode("456  789");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("456", checkArray[0]);
        Assert.assertEquals("", checkArray[1]);

        checkArray =       BarcodeAssignmentUtility.splitDoubleBarcode("456 789");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("456", checkArray[0]);
        Assert.assertEquals("789", checkArray[1]);

        checkArray =      BarcodeAssignmentUtility.splitDoubleBarcode("789789 0123456 789789");
        Assert.assertEquals(2, checkArray.length);
        Assert.assertEquals("789789", checkArray[0]);
        Assert.assertEquals("0123456", checkArray[1]);
    }

    @Test
    public void isWithCCode() throws Exception {
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode("123 4"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode("123 45"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode("123 456"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode("123 4567"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("123 45678"));

        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode(" 123 4"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode(" 123 45"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode(" 123 456"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode(" 123 4567"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(" 123 45678"));

        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("12345678"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("12345678 "));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(" 12345678"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(""));
    }

    @Test
    public void isPartOfDoubleBarcode() throws Exception {
        BarcodeAssignment ba = new BarcodeAssignment();

        Sale baSale = new Sale();
        List<ItemSale> baItemSales = new ArrayList<ItemSale>();
        baSale.setSales(baItemSales);
        ba.setSale(baSale);

        ItemSale baItem = new ItemSale();
        baItem.setType("double");
        List<String> formats = new ArrayList<String>();
        formats.add("9784[0-9]{9}");
        List<String> nextFormats = new ArrayList<String>();
        baItem.setFormat(formats);
        baItem.setNextFormat(nextFormats);
        baItemSales.add(baItem);

        Assert.assertTrue(BarcodeAssignmentUtility.isPartOfDoubleBarcode("9784111222333", ba));
        Assert.assertFalse(BarcodeAssignmentUtility.isPartOfDoubleBarcode("9784111222333 ", ba));
        Assert.assertFalse(BarcodeAssignmentUtility.isPartOfDoubleBarcode(" 9784111222333", ba));

        baItem.setType("single");
        Assert.assertFalse(BarcodeAssignmentUtility.isPartOfDoubleBarcode("9784111222333", ba));
    }

    @Test
    public void matchCodeWithItemFormat() throws Exception {
        ItemSale baItem = new ItemSale();
        List<String> formats = new ArrayList<String>();
        formats.add("8888[0-9]{9}");
        List<String> nextFormats = new ArrayList<String>();
        nextFormats.add("7777[0-9]{9}");
        baItem.setFormat(formats);
        baItem.setNextFormat(nextFormats);

        Assert.assertTrue(BarcodeAssignmentUtility.matchCodeWithItemFormat("8888111222333", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemFormat("8888111222333 ", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemFormat(" 8888111222333", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemFormat("888811122233", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemFormat("777711122233", baItem));
    }

    @Test
    public void matchCodeWithItemNextFormat() throws Exception {
        ItemSale baItem = new ItemSale();
        List<String> formats = new ArrayList<String>();
        formats.add("8888[0-9]{9}");
        List<String> nextFormats = new ArrayList<String>();
        nextFormats.add("7777[0-9]{9}");
        baItem.setFormat(formats);
        baItem.setNextFormat(nextFormats);

        Assert.assertTrue(BarcodeAssignmentUtility.matchCodeWithItemNextFormat("7777111222333", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemNextFormat("7777111222333 ", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemNextFormat(" 7777111222333", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemNextFormat("777711122233", baItem));
        Assert.assertFalse(BarcodeAssignmentUtility.matchCodeWithItemNextFormat("888811122233", baItem));
    }

    @Test
    public void getItemIdWith2LineCode() throws Exception {
        BarcodeAssignment ba = new BarcodeAssignment();

        Sale baSale = new Sale();
        List<ItemSale> baItemSales = new ArrayList<ItemSale>();
        baSale.setSales(baItemSales);
        ba.setSale(baSale);

        ItemSale baItem = new ItemSale();
        baItem.setId("something");
        List<String> formats = new ArrayList<String>();
        formats.add("8888[0-9]{9}");
        List<String> nextFormats = new ArrayList<String>();
        nextFormats.add("7777[0-9]{9}");
        baItem.setFormat(formats);
        baItem.setNextFormat(nextFormats);
        baItemSales.add(baItem);

        ItemSale baItem2 = new ItemSale();
        baItem2.setId("something2");
        List<String> formats2 = new ArrayList<String>();
        formats2.add("8888[0-9]{9}");
        List<String> nextFormats2 = new ArrayList<String>();
        nextFormats2.add("6666[0-9]{9}");
        baItem2.setFormat(formats2);
        baItem2.setNextFormat(nextFormats2);
        baItemSales.add(baItem2);


        Assert.assertEquals("something",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("8888111222333 7777111222333", ba));
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("9784111222333 ", ba));
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("9784111222333", ba));
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("9784111222333", ba));

        Assert.assertEquals("",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("8888111222333 5555111222333", ba));

        Assert.assertEquals("something2",
                BarcodeAssignmentUtility.getItemIdWith2LineCode("8888111222333 6666111222333", ba));

    }

}