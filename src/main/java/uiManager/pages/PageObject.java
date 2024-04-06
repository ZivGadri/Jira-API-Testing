package uiManager.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject {
    private static final Logger logger = LogManager.getLogger(PageObject.class);
    private static final int DEFAULT_EXPLICIT_WAIT = 10;
    protected WebDriver webDriver;

    public PageObject(WebDriver webDriver) {
        this.webDriver = webDriver;
        waitForPageLoad();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public final void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(webDriver, DEFAULT_EXPLICIT_WAIT);
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

    private void clickButtonByWebElement(WebElement element, int seconds) {
        scrollintoView(element);
        try {
            waitForClickable(seconds, element);
        } catch (ElementNotInteractableException enie) {
            ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].click", element);
        }
    }

    private Object scrollintoView(WebElement element) {
        try {
            return ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true)", element);
        } catch (Exception e) {
            logger.debug("Unable to scroll into element view: " + element, e);
        }
        return null;
    }

    private WebElement waitForClickable(int seconds, WebElement element) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), seconds);
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
}