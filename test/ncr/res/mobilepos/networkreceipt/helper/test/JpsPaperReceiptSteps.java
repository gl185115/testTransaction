package ncr.res.mobilepos.networkreceipt.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.imageio.ImageTypeSpecifier;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.dao.SQLServerReceiptDAO;
import ncr.res.mobilepos.networkreceipt.helper.JpsPaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.*;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
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


public class JpsPaperReceiptSteps extends Steps{
    
    private JpsPaperReceipt jpsPaper;

    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown()
    {
        Requirements.TearDown();
    }
    
    @Given ("I create a JPS PaperReceipt using $printername")
    public final void createJPSReceipt(final String printername)
    {
        ReceiptLine a = new ReceiptLine();
        ReceiptLine b = new ReceiptLine();
        ReceiptLine c = new ReceiptLine();
        
        List<ReceiptLine> list = Arrays.asList(a,b,c);
        
        BufferedImage img =
            new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        jpsPaper = new JpsPaperReceipt(printername, list, img);
    }
    
    @Given ("I create a JPS PaperReceipt without a name")
    public final void createJPSReceipt()
    {
        ReceiptLine a = new ReceiptLine();
        ReceiptLine b = new ReceiptLine();
        ReceiptLine c = new ReceiptLine();
        
        List<ReceiptLine> list = Arrays.asList(a,b,c);
        
        BufferedImage img =
            new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        jpsPaper = new JpsPaperReceipt(list, img);
    }
    
    @Then ("I should have a JPS PaperReceipt")
    public final void checkReceipt()
    {
        Assert.assertNotNull(jpsPaper);
        jpsPaper = null;
    }
}