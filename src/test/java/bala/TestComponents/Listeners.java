package bala.TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listeners extends BaseTest implements ITestListener {

    ExtentReports extentReports = ExtentReporter.getReportObject();
    ExtentTest test;
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static ThreadLocal<String> cucumberScenarioName = new ThreadLocal<>();
    private static ThreadLocal<String> screenshotPath = new ThreadLocal<>();

    public void onTestStart(ITestResult result) {

    }

    public void onTestSuccess(ITestResult result) {

        String scenarioName = getScenarioName();
        test = extentReports.createTest(scenarioName != null ? scenarioName : result.getMethod().getMethodName());
        extentTest.set(test);

        extentTest.get().log(Status.PASS, "Test Passed!");
    }

    public void onTestFailure(ITestResult result) {

        String scenarioName = getScenarioName();
        String filePath = getScreenshotFilePath();

        test = extentReports.createTest(scenarioName != null ? scenarioName : result.getMethod().getMethodName());
        extentTest.set(test);

        extentTest.get().log(Status.FAIL, "Test Failed!");
        extentTest.get().fail(result.getThrowable());

        if (scenarioName == null) {
            try {
                driver = (WebDriver) result.getTestClass().getRealClass().getField("driver")
                        .get(result.getInstance());
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            filePath = getScreenshot(result.getMethod().getMethodName(), driver);
            extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
        }
        else
            extentTest.get().addScreenCaptureFromPath(filePath, scenarioName);
    }

    public void onTestSkipped(ITestResult result) {

        String scenarioName = getScenarioName();
        test = extentReports.createTest(scenarioName != null ? scenarioName : result.getMethod().getMethodName());
        extentTest.set(test);

        extentTest.get().log(Status.SKIP, "Test Skipped!");
        extentTest.get().skip(result.getThrowable());
    }

    public void onFinish(ITestContext context) {

        extentReports.flush();
    }

    public static void setScenarioName(String name) {

        cucumberScenarioName.set(name);
    }

    private static String getScenarioName() {

        return cucumberScenarioName.get();
    }

    public static void setScreenshotFilePath(String filePath) {

        screenshotPath.set(filePath);
    }

    private static String getScreenshotFilePath() {

        return screenshotPath.get();
    }
}
