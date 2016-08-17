package ncr.res.mobilepos.pricing.model.test;

import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.pricing.model.Department;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

import static org.junit.Assert.assertEquals;

public class DepartmentModelSteps extends Steps{
    Department department;

    @Given("a department model")
    public final void createDptModel(){    
        department = new Department();        
    }
        
    @Then("its DPT is {$dpt}")
    public final void itsDPT(final String dpt){
        department.setDpt(dpt);
        assertEquals(dpt, department.getDpt());
    }
    
    @Then("its DptName is {$dptname}")
    public final void itsDptName(final String dptname){
        department.setDptName(dptname);
        assertEquals(dptname, department.getDptName());        
    }
        
    @Then("its DptKanaName is {$dptKanaName}")
    public final void itsDptKanaName(final String dptKanaName){
        department.setDptKanaName(dptKanaName);
        assertEquals(dptKanaName, department.getDptKanaName());
    }
    
    @Then("its Category is {$category}")
    public final void itsCategory(final String category){
        department.setCategory(category);
        assertEquals(category, department.getCategory());        
    }
    
    @Then("xml string should be {$xml}")
    public final void seriallize(final String xml) throws Exception{
        XmlSerializer<Department> posLogRespSrlzr =
            new XmlSerializer<Department>();
        String actual = posLogRespSrlzr
                        .marshallObj(Department.class, department, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }
}
