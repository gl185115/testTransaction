/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* JsonSerializerTest
*
* Test Steps Class for Json Serializer.
*
* cc185102
*/
package ncr.res.mobilepos.helper.serializer.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;

import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * The Test Steps class for Json Serializer.
 * @author cc185102
 *
 */
public class JsonMarshallerSteps extends Steps {
    /**
     * Member variable provided to act as the current
     * Json String to be tested.
     */
    private String strJson;
    /**
     * Member variable provided to act as the serializer
     * for Employee.
     */
    private JsonMarshaller<Employee> jsonEmployeeSerializer;
    /**
     * Member variable provided to act as the serializer
     * for Employees.
     */
    private JsonMarshaller<Employees> jsonEmployeesSerializer;
    /**
     * Member variable provided to act as the serializer
     * for ItemMaintenance.
     */
    private JsonMarshaller<ItemMaintenance> jsonItemMaintenanceSerializer;
    /**
     * Member variable provided to act as the actual Employee object
     * resulted from Serialization.
     */
    private Employee actualEmployee;
    /**
     * Member variable provided to act as the actual Employees object
     * resuled from serialization.
     */
    private Employees actualEmployees;
    /**
     * Member variable provided to act as the exception thrown.
     */
    private Exception actualException;
    /**
     * Member variable provided to act as the actual ItemMaintenance Object.
     */
    private ItemMaintenance actualItemMaintenace;
    /**
     * Default Constructor.
     */
    public JsonMarshallerSteps() {
        jsonEmployeeSerializer = new JsonMarshaller<Employee>();
        jsonEmployeesSerializer = new JsonMarshaller<Employees>();
        jsonItemMaintenanceSerializer = new JsonMarshaller<ItemMaintenance>();
    }
    /**
     * Given Step: Ask for a JSON String.
     * @param jsonString The JSON provided.
     */
    @Given("a JSON String ($jsonString)")
    public final void aJSONString(final String jsonString) {
       this.strJson = jsonString;
    }
    /**
     * When Step: Convert the actual
     * JSON String specified into object.
     */
    @When("the JSON String is converted into Employee object")
    public final void theJSONStringIsConvertedIntoEmployeeObject() {
        try {
            actualEmployee =
                jsonEmployeeSerializer.unMarshall(this.strJson, Employee.class);
            actualException = null;
        } catch (Exception ex) {
           actualException = ex;
        }
    }
    /**
     * When Step: Convert the actual
     * JSON String specified into ItemMaintenance object.
     */
    @When("the JSON String is converted into ItemMaintenance object")
    public final void theJSONStringIsConvertedIntoItemMaintenanceObject() {
        try {
            actualItemMaintenace =
                jsonItemMaintenanceSerializer.unMarshall(this.strJson,
                        ItemMaintenance.class);
            actualException = null;
        } catch (Exception ex) {
           actualException = ex;
        }
    }
    /**
     * When Step: Convert the actual
     * JSON String specified into Employees object.
     */
    @When("the JSON String is converted into Employees object")
    public final void theJSONStringIsConvertedIntoEmployeesObject() {
        try {
            actualEmployees =
                jsonEmployeesSerializer.unMarshall(this.strJson,
                        Employees.class);
            actualException = null;
        } catch (Exception ex) {
           actualException = ex;
        }
    }
   /**
    * Then Step: Compare the Actual Employee with the Expected Employee.
    * @param employee The expected employee to test
    */
   @Then("Employee object should have the following values: $employee")
   public final void employeeObjectShouldHaveTheFollowingValues(
           final ExamplesTable employee) {
       Map<String, String> expectedEmployee = employee.getRow(0);

       String expOperatorName  = expectedEmployee.get("OperatorName");
       String expOperatorID = expectedEmployee.get("OperatorID");

       if (expOperatorName.equals("NULL")) {
           expOperatorName = null;
       }

       if (expOperatorID.equals("NULL")) {
           expOperatorID = null;
       }

       Assert.assertEquals("Compare the Employee's name",
               expOperatorName,
               actualEmployee.getName());
       Assert.assertEquals("Compare the Employee's number",
               expOperatorID,
               actualEmployee.getNumber());
   }
   /**
    * Then Step: Compare the Actual Employees with expected Employees.
    * @param employees The expected employees to test
    */
   @Then("the Employees object should have the following values: $employees")
   public final void theEmployeesObjectShouldHaveTheFollowingValues(
           final ExamplesTable employees) {
       List<Employee> actEmpList = actualEmployees.getEmployeeList();
       Assert.assertEquals("The number of Employee(s) must be exact",
               employees.getRowCount(),
               actEmpList.size());
       int index = 0;
       for (Map<String, String> expectedEmployee : employees.getRows()) {
           Assert.assertEquals(
                   Integer.parseInt(
                           expectedEmployee.get("NCRWSSExtendedResultCode")),
                   actualEmployees.getNCRWSSExtendedResultCode());
           Assert.assertEquals(
                   Integer.parseInt(
                           expectedEmployee.get("NCRWSSResultCode")),
                   actualEmployees.getNCRWSSResultCode());
           Assert.assertEquals(
                   expectedEmployee.get("RetailStoreID"),
                   actualEmployees.getRetailtStoreID());
           /*
            * Compare iterated Employee
            */
           Employee actEmployee = actEmpList.get(index);
           Assert.assertEquals("Compare the Employee's name",
                   expectedEmployee.get("OperatorName"),
                   actEmployee.getName());
           Assert.assertEquals("Compare the Employee's number",
                   expectedEmployee.get("OperatorID"),
                   actEmployee.getNumber());
           index++;
       }
  }

   /**
    * Then Step: Compare the Actual Employees with expected Employees.
    * @param itemMaintenance The expected ItemMaintenance to test
    */
   @Then("the ItemMaintenace object should have the following values:"
           + " $employees")
   public final void theItemMaintenaceObjectShouldHaveTheFollowingValues(
           final ExamplesTable itemMaintenance) {

       for (Map<String, String> expItemMntnce : itemMaintenance.getRows()) {
           Assert.assertEquals(
                   Integer.parseInt(
                           expItemMntnce.get("NCRWSSExtendedResultCode")),
                   actualItemMaintenace.getNCRWSSExtendedResultCode());
           Assert.assertEquals(
                   Integer.parseInt(
                           expItemMntnce.get("NCRWSSResultCode")),
                   actualItemMaintenace.getNCRWSSResultCode());
           /*
            * Compare iterated Employee
            */
           Item actItem = actualItemMaintenace.getItem();
           Assert.assertEquals("Compare the Item's ActualSalesPrice",
                   Double.parseDouble(
                           expItemMntnce.get("ActualSalesPrice")),
                   actItem.getActualSalesUnitPrice());
           Assert.assertEquals("Compare the Item's ActualSalesPrice",
                   Double.parseDouble(
                           expItemMntnce.get("RegularSalesUnitPrice")),
                   actItem.getRegularSalesUnitPrice());
           Assert.assertEquals("Compare the Item's Class",
                   expItemMntnce.get("Class"),
                   actItem.getItemClass());
           Assert.assertEquals("Compare the Item's Discount",
                   Double.parseDouble(expItemMntnce.get("Discount")),
                   actItem.getDiscount());
           Assert.assertEquals("Compare the Item's Department",
                   expItemMntnce.get("Department"),
                   actItem.getDepartment());
           Assert.assertEquals("Compare the Item's DescriptionEN",
                   expItemMntnce.get("DescriptionEN"),
                   actItem.getDescription().getEn());
           Assert.assertEquals("Compare the Item's DescriptionJP",
                   expItemMntnce.get("DescriptionJP"),
                   actItem.getDescription().getJa());
           Assert.assertEquals("Compare the Item's ID",
                   expItemMntnce.get("ItemID"),
                   actItem.getItemId());
           Assert.assertEquals("Compare the Item's Discount Amount",
                   Double.parseDouble(
                           expItemMntnce.get("DiscountAmount")),
                   actItem.getDiscountAmount());
           Assert.assertEquals("Compare the Item's Line",
                   expItemMntnce.get("Line"),
                   actItem.getLine());
       }
  }

   /**
    * Then Step: Expect an exception thrown.
    * @param exceptionType the Exception Type expected
    */
   @Then("it should throw with {$exceptionType}")
   public final void itShouldFailWith(final String exceptionType) {
       if (exceptionType.equals("IOException")) {
           Assert.assertTrue("Compare the JSON serialization Exception: ",
                   actualException instanceof IOException);
       } else if (exceptionType.equals("UnrecognizedPropertyException")) {
           Assert.assertTrue("Compare the JSON serialization Exception: ",
                   actualException instanceof UnrecognizedPropertyException);
       } else if (exceptionType.equals("NoException")) {
           Assert.assertNull(actualException);
       } else if (null != actualException) {
           actualException.printStackTrace();
           Assert.fail("Failed to serialize the Object: "
                   + "JSON format: "
                   + this.strJson);
       }
   }
}
