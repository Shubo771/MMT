package pageObjects;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class makeMyTrip {

	 WebDriver driver;
	    WebDriverWait wait;

	    public makeMyTrip(WebDriver driver) 
	    {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        PageFactory.initElements(driver,this);
	    }

	    // Close popup
	    @FindBy(xpath = "//span[@class='commonModal__close']")
	    WebElement closePopup;

	    // Round Trip
	    @FindBy(xpath = "//li[@data-cy='roundTrip']")
	    WebElement roundTrip;

	    // From
	    @FindBy(id = "fromCity")
	    WebElement fromCity;

	    @FindBy(xpath = "//input[@placeholder='From']")
	    WebElement fromInput;

	    @FindBy(xpath = "//li[@role='option']//span[text()='Kolkata, India']")
	    WebElement kolkataOption;

	    // To
	    @FindBy(id = "toCity")
	    WebElement toCity;

	    @FindBy(xpath = "//input[@placeholder='To']")
	    WebElement toInput;

	    @FindBy(xpath = "//li[@role='option']//span[text()='New Delhi, India']")
	    WebElement delhiOption;

	    // Calendar
	    @FindBy(xpath = "//span[@aria-label='Next Month']")
	    WebElement nextMonth;

	    @FindBy(xpath = "//div[@class='DayPicker-Caption']")
	    WebElement calendarHeader;

	    @FindBy(xpath = "//div[contains(@class,'DayPicker-Day') and not(contains(@class,'disabled'))]")
	    List<WebElement> enabledDates;


	    // Search
	    @FindBy(xpath = "//a[text()='Search']")
	    WebElement searchBtn;

	    // ---------------- METHODS ----------------

	    public void closePopupIfPresent() {
	        try {
	            wait.until(ExpectedConditions.elementToBeClickable(closePopup)).click();
	        } catch (Exception e) {}
	    }

	    public void selectRoundTrip() {
	        wait.until(ExpectedConditions.elementToBeClickable(roundTrip)).click();
	    }

	    public void selectFromCityKolkata() {
	        fromCity.click();
	        wait.until(ExpectedConditions.visibilityOf(fromInput)).sendKeys("Kolk");
	        wait.until(ExpectedConditions.elementToBeClickable(kolkataOption));
	        new Actions(driver).moveToElement(kolkataOption).click().perform();
	    }

	    public void selectToCityDelhi() {
	        toCity.click();
	        wait.until(ExpectedConditions.visibilityOf(toInput)).sendKeys("Delhi");
	        wait.until(ExpectedConditions.elementToBeClickable(delhiOption));
	        new Actions(driver).moveToElement(delhiOption).click().perform();
	    }
	    public void clickAnywhereActions() {
	        Actions actions = new Actions(driver);
	        actions.moveByOffset(10, 10).click().perform();}
	    
	    public void selectDates2026LowestPricePlus3() {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        Actions actions = new Actions(driver);

	        // Ensure calendar is open and focused
	        wait.until(ExpectedConditions.visibilityOf(calendarHeader));

	        // 🔹 Force interaction so MakeMyTrip loads prices
	        js.executeScript("arguments[0].scrollIntoView(true);", calendarHeader);
	        actions.moveToElement(calendarHeader).pause(Duration.ofMillis(500)).perform();

	        // ---------- Move calendar to 2026 ----------
	        while (!calendarHeader.getText().contains("2026")) {
	            js.executeScript("arguments[0].click();", nextMonth);
	            wait.until(ExpectedConditions.visibilityOf(calendarHeader));

	            // Important: trigger lazy load
	            actions.moveToElement(calendarHeader).pause(Duration.ofMillis(300)).perform();
	        }

	        int lowestPrice = Integer.MAX_VALUE;
	        WebElement lowestPriceDate = null;

	        // Wait for dates & prices to load
	        List<WebElement> dates = wait.until(
	                ExpectedConditions.visibilityOfAllElements(enabledDates)
	        );

	        for (WebElement date : dates) {
	            try {
	                WebElement priceElement = date.findElement(By.xpath(".//following-sibling::div[contains(@class,'price')]"));
	                String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();

	                if (!priceText.isEmpty()) {
	                    int price = Integer.parseInt(priceText);

	                    if (price < lowestPrice) {
	                        lowestPrice = price;
	                        lowestPriceDate = date;
	                    }
	                }

	            } catch (NoSuchElementException e) {
	                // Ignore dates without price
	            }
	        }

	        if (lowestPriceDate == null) {
	            throw new RuntimeException("No priced date found in 2026 calendar");
	        }

	        // 🔹 Scroll date into view before clicking
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", lowestPriceDate);
	        actions.moveToElement(lowestPriceDate).pause(Duration.ofMillis(300)).click().perform();
	    }



	       
	        public void clickSearch() {

	            // Close calendar / price overlay
	            clickAnywhereActions();

	            // Wait for Search button to be clickable
	            wait.until(ExpectedConditions.visibilityOf(searchBtn));
	            wait.until(ExpectedConditions.elementToBeClickable(searchBtn));

	            // JS click to avoid interception
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
	        }
}
