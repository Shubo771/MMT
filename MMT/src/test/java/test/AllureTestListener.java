package test;


import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class AllureTestListener implements ITestListener{
	 
	 @Override
	    public void onTestSuccess(ITestResult result) {
	        WebDriver driver = (WebDriver) result.getTestContext()
	                .getAttribute("driver");
	        AllureUtils.attachScreenshot(driver, "TEST PASSED");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	        WebDriver driver = (WebDriver) result.getTestContext()
	                .getAttribute("driver");
	        AllureUtils.attachScreenshot(driver, "TEST FAILED");
	        AllureUtils.attachText("Failure Reason", result.getThrowable().toString());
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	        WebDriver driver = (WebDriver) result.getTestContext()
	                .getAttribute("driver");
	        AllureUtils.attachScreenshot(driver, "TEST SKIPPED");
	    }

	    @Override
	    public void onStart(ITestContext context) {}

	    @Override
	    public void onFinish(ITestContext context) {}

}
