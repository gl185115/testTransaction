package ncr.res.mobilepos.test;
/**
 * Copyright (c) 2016 NCR/JAPAN Corporation SW-R&D
 * TestRunnerScenario revised for jbehave v3 or above.
 */

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.Steps;
import org.junit.Ignore;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;
import static org.jbehave.core.reporters.Format.HTML;

@Ignore
public class TestRunnerScenario extends JUnitStory {

    private final Steps scenarioSteps;

    /**
     * Constructor.
     * @param testRunnerScenarioSteps
     */
    public TestRunnerScenario(final Steps testRunnerScenarioSteps) {
        this.scenarioSteps = testRunnerScenarioSteps;
    }

    @Override
    public Configuration configuration() {
        Configuration config = new MostUsefulConfiguration();

        // StoryPathResolver, Story file is given with the extension.
        StoryPathResolver storyPathResolver = new UnderscoredCamelCaseResolver(".scenario");
        config.useStoryPathResolver(storyPathResolver);

        // StoryLoader, scenario files are written in SJIS.
        StoryLoader storyLoader = new LoadFromClasspath(Charset.forName("MS932"));
        config.useStoryLoader(storyLoader);

        // FailureStrategy
        PendingStepStrategy failureStrategy = new FailingUponPendingStep();
        config.usePendingStepStrategy(new FailingUponPendingStep());

        // StoryReporterBuilder
        StoryReporterBuilder reporterBuilder = new StoryReporterBuilder();
        reporterBuilder.withDefaultFormats();
        reporterBuilder.withFormats(
                CONSOLE,
                TXT,
                HTML,
                XML);
        config.useStoryReporterBuilder(reporterBuilder);

        // ParameterConverters - Adds EmptryStringConverter
        ParameterConverters parameterConverters = new ParameterConverters();
        parameterConverters.addConverters(customConverters());
        config.useParameterConverters(parameterConverters);

        return config;
    }

    private ParameterConverter[] customConverters() {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();
        converters.add(new EmptyStringConverter());
        return converters.toArray(new ParameterConverter[converters.size()]);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), this.scenarioSteps);
    }

}
