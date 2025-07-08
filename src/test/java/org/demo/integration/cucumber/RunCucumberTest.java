package org.demo.integration.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:/integration/cucumber", glue = "org.demo.integration.cucumber")
public class RunCucumberTest {
}