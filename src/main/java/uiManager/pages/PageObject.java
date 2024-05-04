package uiManager.pages;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject {
    private static final Logger logger = LogManager.getLogger(PageObject.class);
    private static final int DEFAULT_EXPLICIT_WAIT = 10;
    protected WebDriver driver;
    protected WebDriverWait wait;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void navigateToUrl(String url) {
        logger.info("navigating to url: {}", url);
        driver.get(url);
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        wait.until((ExpectedCondition<Boolean>) driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            return jsExecutor.executeScript("return document.readyState").equals("complete");
        });
    }

    public void clickButton(WebElement element, String buttonTitle) {
        try {
            logger.info("Clicking on {} button", buttonTitle);
            clickButtonByWebElement(element, 3);
            threadSleepLog(2, "after clicking button " + buttonTitle);
        } catch (NoSuchElementException nsee) {
            logger.error("Failed to click on " + buttonTitle + " button.\nMore info: " + nsee.getMessage());
            throw new NoSuchElementException("Failed to click on " + buttonTitle + " button");
        } catch (TimeoutException te) {
            logger.error("Wait interrupted.\nMore info: " + te.getMessage());
            throw new NoSuchElementException("Wait interrupted");
        } catch (Exception e) {
            logger.error("Failed to click on " + buttonTitle + " button.\nMore info: " + e.getMessage());
            throw new RuntimeException("Failed to click on " + buttonTitle + " button");
        }
    }

    public void sendText(WebElement element, String textToSend) {
        clearTextField(element);
        element.sendKeys(textToSend);
        threadSleepLog(1, String.format("after text '%s' inserted to text box.", textToSend));
    }

    private void clearTextField(WebElement element) {
        int textSize = element.getAttribute("value").length();
        String backSpace = StringUtils.repeat("\b", textSize);

        while (!element.getAttribute("value").isEmpty()) {
            element.sendKeys(backSpace);
        }
    }

    private void clickButtonByWebElement(WebElement element, int seconds) {
        scrollintoView(element);
        try {
            waitForClickable(seconds, element);
        } catch (ElementNotInteractableException enie) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click", element);
        }
    }

    private Object scrollintoView(WebElement element) {
        try {
            return ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true)", element);
        } catch (Exception e) {
            logger.debug("Unable to scroll into element view: " + element, e);
        }
        return null;
    }

    private WebElement waitForClickable(int seconds, WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), seconds);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void threadSleepLog(long sec, String extraDetails) {
        logger.info("Thread is sleeping for {} second(s) {}", sec, extraDetails);
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public boolean isElementFoundInPage(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (NoSuchElementException nsee) {
            logger.info("Element was not found in page");
            return false;
        }
    }

    public WebElement findElementByParentElement(WebElement parent, By findChildBy) {
        WebElement elementToReturn;
        try {
            elementToReturn = parent.findElement(findChildBy);
        } catch (NoSuchElementException nsee) {
            logger.error("Could not find the child element by the WebElement and provided search info.");
            throw new NoSuchElementException("Could not find the child element by the WebElement and provided search info.");
        }
        return elementToReturn;
    }
}