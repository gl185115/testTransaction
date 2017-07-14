package ncr.res.mobilepos.line.resource.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.servlet.ServletContext;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.SearchedLine;
import ncr.res.mobilepos.line.resource.LineResource;

public class ListLineResourceSteps extends Steps {
	private DBInitiator dbInitRESMaster = null;
	private LineResource lineResource = null;
	private SearchedLine lineList = null;
	private DAOFactory daoFactory;

	@BeforeScenario
	public final void SetUpClass() {
		Requirements.SetUp();
		dbInitRESMaster = new DBInitiator("ListLineResourceSteps","test/ncr/res/mobilepos/line/resource/test/mst_line_info_(nodata).xml", DATABASE.RESMaster);
		GlobalConstant.setMaxSearchResults(50);
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}

	@Given("data in table MST_LINEINFO with $path")
	public final void initLineInfoTableWithData(final String path) {
		try {
			dbInitRESMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, path);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Given("a Line Resource")
    public final void givenLineResource() {
    	ServletContext servletContext = Requirements.getMockServletContext();
        lineResource = new LineResource();
        lineResource.setContext(servletContext);
    }

	@When("I get line list of retailstoreid $retailstoreid with limit $limit companyid $companyid")
	public final void getLineListOfRetailstoreidWithLimit(final String retailstoreid, final int limit, final String companyid) {
		lineList = lineResource.list(retailstoreid, null, null, null, limit, companyid);
	}

	@Then("I should get NCRWSSResultCode $expectedCode and $length line info list")
	public final void thenShouldGetResults(final int expected, final int length) {
		Assert.assertEquals(expected, lineList.getNCRWSSResultCode());
		Assert.assertEquals(length, lineList.getLines().size());
	}

	@Then("I should have the following data $expectedlineinfo")
    public final void iShoudHaveTheFollowingList(
            final ExamplesTable expectedlineinfo) throws DataSetException{
        ITable actualTable = dbInitRESMaster.getTableSnapshot("MST_LINEINFO");

        assertThat("Compare the actual number of rows in MST_LINEINFO",
                actualTable.getRowCount(),
                is(equalTo(expectedlineinfo.getRowCount())));

        int i = 0;
        for (Map<String, String> lineInfoList : expectedlineinfo.getRows()) {
            String storeid = (String)actualTable.getValue(i, "StoreId");
            String line = (String)actualTable.getValue(i, "Line");
            String dpt = (String)actualTable.getValue(i, "Dpt");

            assertThat("MST_LINEINFO (row"+i+" - STOREID",
                    storeid.trim(),
                    is(equalTo(lineInfoList.get("STOREID").trim())));
            assertThat("MST_LINEINFO (row"+i+" - LINE",
                    line.trim(),
                    is(equalTo(lineInfoList.get("LINE").trim())));
            assertThat("MST_LINEINFO (row"+i+" - DPT",
                    dpt.trim(),
                    is(equalTo(lineInfoList.get("DPT").trim())));

            i++;
        }
    }

	@Then("I should get the following line list entries $listLine")
	public final void thenIShouldHaveTheFollowingLineList(final ExamplesTable expectedLineList) throws DataSetException {
		try {
			Assert.assertEquals("Must exact number of Line Entries from the List",
					expectedLineList.getRowCount(), lineList.getLines().size());

			int i = 0;
			for (Map<String, String> tempLineList : expectedLineList.getRows()) {
				Line actualLineInfo = new Line(lineList.getLines().get(i));

				Assert.assertEquals("Compare the StoreId " + i,
						tempLineList.get("StoreId"), actualLineInfo.getRetailStoreId());
				Assert.assertEquals("Compare the Line " + i,
						tempLineList.get("Line"), actualLineInfo.getLine());
				Assert.assertEquals("Compare the Dpt " + i,
						tempLineList.get("Dpt"), actualLineInfo.getDepartment());

				if (tempLineList.get("LineName").equals("null") || tempLineList.get("LineName").equals("NULL")) {
	                Assert.assertNull("Assume that LineName is "
	                        + "null for row " + i + ":",
	                        actualLineInfo.getDescription().getEn());
	            } else {
	            	Assert.assertEquals("Compare the LineName EN " + i,
							tempLineList.get("LineName"), actualLineInfo.getDescription().getEn());
	            }

				if (tempLineList.get("LineNameLocal").equals("null") || tempLineList.get("LineNameLocal").equals("NULL")) {
	                Assert.assertNull("Assume that LineName is "
	                        + "null for row " + i + ":",
	                        actualLineInfo.getDescription().getJa());
	            } else {
	            	Assert.assertEquals("Compare the LineNameLocal JA " + i,
						tempLineList.get("LineNameLocal"), actualLineInfo.getDescription().getJa());
	            }

				if (tempLineList.get("TaxType").equals("null") || tempLineList.get("TaxType").equals("NULL")) {
					Assert.assertNull("Assume that TaxType is "
							+ "null for row " + i + ":",
	                        String.valueOf(actualLineInfo.getTaxType()));
				} else {
					Assert.assertEquals("Compare the TaxType " + i,
							tempLineList.get("TaxType"), actualLineInfo.getTaxType());
				}

				if (tempLineList.get("TaxRate").equals("null") || tempLineList.get("TaxRate").equals("NULL")) {
					Assert.assertNull("Assume that TaxRate is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getTaxRate());
				} else {
					Assert.assertEquals("Compare the TaxRate " + i,
							tempLineList.get("TaxRate"), actualLineInfo.getTaxRate());
				}

				if (tempLineList.get("DiscountType").equals("null") || tempLineList.get("DiscountType").equals("NULL")) {
					Assert.assertNull("Assume that DiscountType is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getDiscountType());
				} else {
					Assert.assertEquals("Compare the DiscountType " + i,
						tempLineList.get("DiscountType"), actualLineInfo.getDiscountType());
				}

				if (tempLineList.get("ExceptionFlag").equals("null") || tempLineList.get("ExceptionFlag").equals("NULL")) {
					Assert.assertNull("Assume that ExceptionFlag is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getExceptionFlag());
				} else {
					Assert.assertEquals("Compare the ExceptionFlag " + i,
						tempLineList.get("ExceptionFlag"), actualLineInfo.getExceptionFlag());
				}

				if (tempLineList.get("DiscountFlag").equals("null") || tempLineList.get("DiscountFlag").equals("NULL")) {
					Assert.assertNull("Assume that DiscountFlag is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getDiscountFlag());
				} else {
					Assert.assertEquals("Compare the DiscountFlag " + i,
						tempLineList.get("DiscountFlag"), actualLineInfo.getDiscountFlag());
				}
				if (tempLineList.get("DiscountAmt").equals("null") || tempLineList.get("DiscountAmt").equals("NULL")) {
					Assert.assertNull("Assume that DiscountAmt is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getDiscountAmount());
				} else {
					Assert.assertEquals("Compare the Discountamount row" + i
	                        + ": ", Integer.parseInt(tempLineList.get("DiscountAmt")),
	                          java.math.BigDecimal.valueOf(actualLineInfo.getDiscountAmount()).intValue());
				}

				if (tempLineList.get("DiscountRate").equals("null") || tempLineList.get("DiscountRate").equals("NULL")) {
					Assert.assertNull("Assume that DiscountRate is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getDiscountRate());
				} else {
					Assert.assertEquals("Compare the DiscountRate row" + i
	                        + ": ", Integer.parseInt(tempLineList.get("DiscountRate")),
	                          java.math.BigDecimal.valueOf(actualLineInfo.getDiscountRate()).intValue());
				}

				if (tempLineList.get("AgeRestrictedFlag").equals("null") || tempLineList.get("AgeRestrictedFlag").equals("NULL")) {
					Assert.assertNull("Assume that AgeRestrictedFlag is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getAgeRestrictedFlag());
				} else {
					Assert.assertEquals("Compare the AgeRestrictedFlag " + i,
						tempLineList.get("AgeRestrictedFlag"), actualLineInfo.getAgeRestrictedFlag());
				}

				if (tempLineList.get("InheritFlag").equals("null") || tempLineList.get("InheritFlag").equals("NULL")) {
					Assert.assertNull("Assume that InheritFlag is "
							+ "null for row " + i + ":",
	                        actualLineInfo.getInheritFlag());
				} else {
					Assert.assertEquals("Compare the InheritFlag " + i,
						tempLineList.get("InheritFlag"), actualLineInfo.getInheritFlag());
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
