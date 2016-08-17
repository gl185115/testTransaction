package ncr.res.mobilepos.deviceinfo.model.test;

import static org.junit.Assert.assertEquals;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class POSLinkInfoTestSteps extends Steps{
    POSLinkInfo posLinkInfo;
    
    @Given("A POSLinkInfo model")
    public final void createPOSLinkInfoModel(){    
        posLinkInfo = new POSLinkInfo();       
    }
    @Then("its RetailStoreID is {$retailStoreID}")
    public final void itsRetailStoreID(final String retailStoreID){
        posLinkInfo.setRetailStoreId(retailStoreID);
        assertEquals(retailStoreID, posLinkInfo.getRetailStoreId());
    }
    
    @Then("its POSLink ID is {$posLinkID}")
    public final void itsPosLinkID(final String posLinkID){
        posLinkInfo.setPosLinkId(posLinkID);
        assertEquals(posLinkID, posLinkInfo.getPosLinkId());
    }
    
    @Then("its Link Name is {$linkName}")
    public final void itsLinkName(final String linkName){
        posLinkInfo.setLinkName(linkName);
        assertEquals(linkName, posLinkInfo.getLinkName());
    }
}
