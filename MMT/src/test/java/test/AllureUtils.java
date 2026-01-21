package test;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Attachment;

public class AllureUtils {
	
	@Attachment(value = "{stepName}", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver, String stepName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{stepName}", type = "text/plain")
    public static String attachText(String stepName, String message) {
        return message;
    }



}
