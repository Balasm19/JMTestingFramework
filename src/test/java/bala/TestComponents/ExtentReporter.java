package bala.TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.util.Properties;

public class ExtentReporter {

    public static ExtentReports getReportObject() {

        ExtentReports extentReports;

        File file = new File(System.getProperty("user.dir") + "/ExtentReports/TestResults.html");

        ExtentSparkReporter reporter = new ExtentSparkReporter(file);

        reporter.config().setReportName("JioMart Test Automation Results");

        reporter.config().setDocumentTitle("Test Results"); // Tab Title

        extentReports = new ExtentReports();

        extentReports.attachReporter(reporter);

        BaseTest baseTest = new BaseTest();
        Properties properties = baseTest.getProperties();

        extentReports.setSystemInfo("Tester", "Bala");
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("Browser", properties.getProperty("browser"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Page Load Timeout in Sec", properties.getProperty("pageLoadTimeout"));
        extentReports.setSystemInfo("Implicit Wait Duration in Sec", properties.getProperty("implicitWait"));
        extentReports.setSystemInfo("Explicit Wait Duration in Sec", properties.getProperty("explicitWait"));
        extentReports.setSystemInfo("Fluent Wait Duration in Sec", properties.getProperty("fluentWait"));
        extentReports.setSystemInfo("Tested on Approx No of Products/Page", properties.getProperty("requiredNoOfProductsToTest"));

        return extentReports;
    }
}
