package bala.TestComponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    int count = 0;
    int maxRetry = 1;

    public boolean retry(ITestResult iTestResult) {

        if (count < maxRetry) {
            count++;
            return true;
        }
        else
            return false;
    }
}
