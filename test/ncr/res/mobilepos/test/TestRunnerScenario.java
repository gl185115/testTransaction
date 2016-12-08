package ncr.res.mobilepos.test;
/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* TestRunnerScenario
*
* TestRunnerScenario is an abstract which handles the test in general.
*
* Campos, Carlos
*/



import static org.jbehave.scenario.reporters
.ScenarioReporterBuilder.Format.CONSOLE;
import static org.jbehave.scenario.reporters
.ScenarioReporterBuilder.Format.HTML;
import static org.jbehave.scenario.reporters.ScenarioReporterBuilder.Format.TXT;
import static org.jbehave.scenario.reporters.ScenarioReporterBuilder.Format.XML;

import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.MostUsefulConfiguration;
import org.jbehave.scenario.parser.ClasspathScenarioDefiner;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.parser.ScenarioNameResolver;
import org.jbehave.scenario.parser.UnderscoredCamelCaseResolver;
import org.jbehave.scenario.reporters.FilePrintStreamFactory;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.jbehave.scenario.reporters.ScenarioReporterBuilder;
import org.jbehave.scenario.steps.Steps;
import org.jbehave.scenario.steps.StepsConfiguration;
import org.jbehave.scenario.steps.StepsFactory;
import org.junit.Ignore;

@Ignore
public class TestRunnerScenario extends JUnitScenario{
    
    protected static final ScenarioNameResolver resolver =
        new UnderscoredCamelCaseResolver(".scenario");
    
    public TestRunnerScenario(){
    }
 
    public TestRunnerScenario(final Steps testRunnerScenarioSteps) {
         useConfiguration(new MostUsefulConfiguration() {
             
                   public ScenarioDefiner forDefiningScenarios() {
                       return new ClasspathScenarioDefiner(resolver,
                               new PatternScenarioParser(keywords()));
                }
                
                @Override
                public ScenarioReporter forReportingScenarios() {
                    return new ScenarioReporterBuilder(
                            new FilePrintStreamFactory(
                                    TestRunnerScenario.class, resolver))
                            .outputTo("target/jbehave-reports")
                            .outputAsAbsolute(true)
                            .withDefaultFormats()
                            .with(CONSOLE)
                            .with(TXT)
                            .with(HTML)
                            .with(XML)
                            .build();
                    }
                });       
    
            StepsConfiguration configuration = new StepsConfiguration();
            addSteps(new StepsFactory(configuration)
                .createCandidateSteps(testRunnerScenarioSteps));
     }
}
