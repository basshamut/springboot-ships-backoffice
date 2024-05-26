package org.demo.integration.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/integration/cucumber", glue = "org.demo.integration.cucumber.steps")
public class RunCucumberTest {
}