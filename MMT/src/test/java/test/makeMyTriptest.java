package test;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import pageObjects.makeMyTrip;


public class makeMyTriptest {

	protected WebDriver driver;

    @BeforeMethod
    public void setup(ITestContext context) {
        driver = new ChromeDriver();
        context.setAttribute("driver", driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get("https://www.makemytrip.com/");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
   
    //-----------------------SMOKE TEST----------------------------
    
    @Test(description = "Round Trip booking ")
    @Story("Checking basic functioanlity")
    @Severity(SeverityLevel.CRITICAL)
    public void smoketest() throws InterruptedException {

        makeMyTrip flight1 = new makeMyTrip(driver);

        flight1.clickAnywhereActions();
        AllureUtils.attachScreenshot(driver, "Popup closed");

        flight1.selectRoundTrip();
        AllureUtils.attachScreenshot(driver, "Round trip selected");

        flight1.selectFromCityKolkata();
        AllureUtils.attachScreenshot(driver, "From city selected");

        flight1.selectToCityDelhi();
        AllureUtils.attachScreenshot(driver, "To city selected");

        flight1.clickSearch();
        AllureUtils.attachScreenshot(driver, "Search clicked");
    }
    
    //-----------------REGRESSION TEST-----------------------
    
    @Test(description = "Round Trip booking with lowest price in 2026")
    @Story("Round Trip Lowest Price")
    @Severity(SeverityLevel.CRITICAL)
    public void regressiontest() throws InterruptedException {

    	makeMyTrip flight = new makeMyTrip(driver);

        flight.clickAnywhereActions();
        AllureUtils.attachScreenshot(driver, "Popup closed");
        
        flight.selectRoundTrip();
        AllureUtils.attachScreenshot(driver, "Round trip selected");
        
        

        flight.selectFromCityKolkata();
        AllureUtils.attachScreenshot(driver, "From city selected");
        

        flight.selectToCityDelhi();
        AllureUtils.attachScreenshot(driver, "To city selected");
        
        flight.selectDates2026LowestPricePlus3();
        AllureUtils.attachScreenshot(driver, "Dates selected");
        

        flight.clickSearch();
        AllureUtils.attachScreenshot(driver, "Search clicked");
    }
    
}
