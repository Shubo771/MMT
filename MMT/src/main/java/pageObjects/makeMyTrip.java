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

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // ---------- Move calendar to 2026 ----------
	        while (!calendarHeader.getText().contains("2026")) {
	            wait.until(ExpectedConditions.elementToBeClickable(nextMonth)).click();
	            wait.until(ExpectedConditions.visibilityOf(calendarHeader));
	        }

	        // ---------- Get all enabled dates ----------
	        List<WebElement> dates = wait.until(
	                ExpectedConditions.presenceOfAllElementsLocatedBy(
	                        By.xpath("//div[contains(@class,'DayPicker-Day') and not(contains(@class,'disabled'))]")
	                )
	        );

	        int lowestPrice = Integer.MAX_VALUE;
	        WebElement lowestPriceDate = null;

	        // ---------- Find lowest price date ----------
	        for (WebElement date : dates) {
	            try {
	                WebElement priceElement = date.findElement(
	                        By.xpath(".//p[contains(@class,'price') or contains(@class,'todayPrice')]")
	                );

	                String priceText = priceElement.getText()
	                        .replace("₹", "")
	                        .replace(",", "")
	                        .trim();

	                if (!priceText.isEmpty()) {
	                    int price = Integer.parseInt(priceText);

	                    if (price < lowestPrice) {
	                        lowestPrice = price;
	                        lowestPriceDate = date;
	                    }
	                }
	            } catch (Exception ignored) {}
	        }

	        if (lowestPriceDate == null) {
	            throw new RuntimeException("Lowest price date not found in 2026");
	        }

	        // ---------- Close floating CTA if present ----------
	        try {
	            WebElement cta = driver.findElement(
	                    By.cssSelector(".tp-dt-enhanced-floating-cta")
	            );
	            js.executeScript("arguments[0].style.display='none';", cta);
	        } catch (Exception ignored) {}

	        // ---------- Click lowest price date safely ----------
	        js.executeScript(
	                "arguments[0].scrollIntoView({block:'center'});",
	                lowestPriceDate
	        );
	        wait.until(ExpectedConditions.elementToBeClickable(lowestPriceDate));
	        js.executeScript("arguments[0].click();", lowestPriceDate);

	        // ---------- Select +3 day ----------
	        int index = dates.indexOf(lowestPriceDate);
	        if (index + 3 < dates.size()) {
	            WebElement plusThreeDate = dates.get(index + 3);

	            js.executeScript(
	                    "arguments[0].scrollIntoView({block:'center'});",
	                    plusThreeDate
	            );
	            wait.until(ExpectedConditions.elementToBeClickable(plusThreeDate));
	            js.executeScript("arguments[0].click();", plusThreeDate);
	        } else {
	            throw new RuntimeException("Unable to select +3 day from lowest price date");
	        }
	    }


	       
	    public void clickSearch() {

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Close calendar / overlay
	        js.executeScript("document.body.click();");

	        // Stable Search button locator
	        By searchBtn = By.xpath("//a[contains(@class,'primaryBtn')]");

	        WebElement search = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(searchBtn)
	        );

	        // Scroll into view
	        js.executeScript(
	                "arguments[0].scrollIntoView({block:'center'});",
	                search
	        );

	        // JS click to avoid interception
	        js.executeScript("arguments[0].click();", search);
	    }
}
