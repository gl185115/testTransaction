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
    public void isMultiAndSplitConsistency() {
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("123 123"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("1234567890 123456789"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("  123  123  "));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode(" 123  123"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("456  456"));
        Assert.assertTrue(BarcodeAssignmentUtility.isMultiLineBarcode("789789 789789 789789"));

        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode("123 123").length);
        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode("1234567890 123456789").length);
        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode("  123  123  ").length);
        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode(" 123  123").length);
        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode("456  456").length);
        Assert.assertEquals(2, BarcodeAssignmentUtility.splitDoubleBarcode("789789 789789 789789").length);

        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode("789789"));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 789789 "));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 789789"));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode("789789 "));
        Assert.assertFalse(BarcodeAssignmentUtility.isMultiLineBarcode(" 78978 "));

        Assert.assertEquals(1, BarcodeAssignmentUtility.splitDoubleBarcode("789789").length);
        Assert.assertEquals(1, BarcodeAssignmentUtility.splitDoubleBarcode(" 789789 ").length);
        Assert.assertEquals(1, BarcodeAssignmentUtility.splitDoubleBarcode(" 789789").length);
        Assert.assertEquals(1, BarcodeAssignmentUtility.splitDoubleBarcode("789789 ").length);
        Assert.assertEquals(1, BarcodeAssignmentUtility.splitDoubleBarcode(" 78978 ").length);
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
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("123 4"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("123 45"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("123 456"));
        Assert.assertTrue(BarcodeAssignmentUtility.isWithCCode("123 4567"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode("123 45678"));

        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(" 123 4"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(" 123 45"));
        Assert.assertFalse(BarcodeAssignmentUtility.isWithCCode(" 123 456"));
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
    public void getBarcodeAssignmentItemIdWithSingleBarcode() throws Exception {
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

        ItemSale baItem3 = new ItemSale();
        baItem3.setId("something3");
        List<String> formats3 = new ArrayList<String>();
        formats3.add("5555[0-9]{9}");
        List<String> nextFormats3 = new ArrayList<String>();
        nextFormats3.add("6666[0-9]{9}");
        baItem3.setFormat(formats3);
        baItem3.setNextFormat(nextFormats3);
        baItemSales.add(baItem3);

        String singleBarcode = ("8888111222333");
        Assert.assertEquals("something",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba).getId());

        singleBarcode = ("7777111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba));

        singleBarcode = ("8888111222333");
        Assert.assertEquals("something",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba).getId());

        // Only matches with the first one in BarcodeAssignment.
        singleBarcode = ("8888111222333");
        Assert.assertEquals("something",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba).getId());

        singleBarcode = ("7777111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba));

        singleBarcode = ("5555111222333");
        Assert.assertEquals("something3",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithSingleBarcode(singleBarcode, ba).getId());
    }

    @Test
    public void getBarcodeAssignmentItemIdWithDoubleBarcode() throws Exception {
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

        String[] doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("8888111222333 7777111222333");
        Assert.assertEquals("something",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba).getId());

        doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("7777111222333 7777111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba));

        doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("8888111222333 5555111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba));

        doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("8888111222333 6666111222333");
        Assert.assertEquals("something2",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba).getId());

        doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("7777111222333 6666111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba));

        doubleBarcode = BarcodeAssignmentUtility.splitDoubleBarcode("7777111222333 7777111222333");
        Assert.assertEquals(null,
                BarcodeAssignmentUtility.getBarcodeAssignmentItemIdWithDoubleBarcode(doubleBarcode, ba));
    }

    @Test
    public void getBarcodeAssignmentItemId() throws Exception {
        BarcodeAssignment ba = new BarcodeAssignment();

        Sale baSale = new Sale();
        List<ItemSale> baItemSales = new ArrayList<ItemSale>();
        baSale.setSales(baItemSales);
        ba.setSale(baSale);

        ItemSale baItem = new ItemSale();
        baItem.setId("something double1");
        List<String> formats = new ArrayList<String>();
        formats.add("1111[0-9]{9}");
        List<String> nextFormats = new ArrayList<String>();
        nextFormats.add("1112[0-9]{9}");
        baItem.setFormat(formats);
        baItem.setNextFormat(nextFormats);
        baItem.setType("double");
        baItemSales.add(baItem);

        ItemSale baItem2 = new ItemSale();
        baItem2.setId("something double2");
        List<String> formats2 = new ArrayList<String>();
        formats2.add("2221[0-9]{9}");
        List<String> nextFormats2 = new ArrayList<String>();
        nextFormats2.add("2222[0-9]{9}");
        baItem2.setFormat(formats2);
        baItem2.setNextFormat(nextFormats2);
        baItem2.setType("double");
        baItemSales.add(baItem2);

        ItemSale baItem4 = new ItemSale();
        baItem4.setId("something single1");
        List<String> formats4 = new ArrayList<String>();
        formats4.add("4444[0-9]{9}");
        List<String> nextFormats4 = new ArrayList<String>();
        baItem4.setFormat(formats4);
        baItem4.setNextFormat(nextFormats4);
        baItem4.setType("single");
        baItemSales.add(baItem4);

        ItemSale baItem5 = new ItemSale();
        baItem5.setId("something single2");
        List<String> formats5 = new ArrayList<String>();
        List<String> nextFormats5 = new ArrayList<String>();
        nextFormats5.add("5555[0-9]{9}");
        baItem5.setFormat(formats5);
        baItem5.setNextFormat(nextFormats5);
        baItem5.setType("single");
        baItemSales.add(baItem5);

        // Single barcode matches with a part of 'something1'.
        Assert.assertEquals("something double1",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("1111111222333", ba));
        // Now it knows if it is a part of double barcode assignment.
        Assert.assertTrue(BarcodeAssignmentUtility.isPartOfDoubleBarcode("1111111222333", ba));

        // Single barcode never matches with a 'next' of 'something1'.
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("1112111222333", ba));

        // Double barcode can matches with 'something2'
        Assert.assertEquals("something double2",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("2221111222333 2222111222333", ba));

        // Double barcode can't match with 'something2', only first line match, second line doen't.
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("2221111222333 2223111222333", ba));

        // Single barcode can matches with ba which only has format.
        Assert.assertEquals("something single1",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("4444111222333", ba));
        // Now it knows if it is not a part of double barcode assignment.
        Assert.assertFalse(BarcodeAssignmentUtility.isPartOfDoubleBarcode("4444111222333", ba));

        // Double barcode can't match with 'something4' which is for single barcode.
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("4444111222333 2222111222333", ba));

        // BA which has only next-format, never matches.
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("555511222333", ba));
        Assert.assertEquals("",
                BarcodeAssignmentUtility.getBarcodeAssignmentItemId("555511222333 555511222333", ba));
    }

}