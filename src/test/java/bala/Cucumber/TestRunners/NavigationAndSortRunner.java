package bala.Cucumber.TestRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions (features = "src/test/java/bala/Cucumber",
        glue = "bala.StepDefinitions",
        tags = "@NavigationAndSortFunctionality",
        monochrome = true,
        plugin = {"html:CucumberReports/cucumber.html"})
public class NavigationAndSortRunner extends AbstractTestNGCucumberTests {

}
