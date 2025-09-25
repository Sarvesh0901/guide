package com.hellobooks.automation.pages;

import com.hellobooks.automation.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Base page class with common functionality for all page objects
 */
public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigManager config;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigManager.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present
     */
    protected WebElement waitForElementToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for text to be present in element
     */
    protected boolean waitForTextToBePresentInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for element to disappear
     */
    protected boolean waitForElementToDisappear(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Click element with wait
     */
    protected void clickElement(By locator) {
        WebElement element = waitForElementToBeClickable(locator);
        element.click();
        logger.debug("Clicked element: {}", locator);
    }

    /**
     * Enter text with wait
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Entered text '{}' in element: {}", text, locator);
    }

    /**
     * Get text from element
     */
    protected String getText(By locator) {
        WebElement element = waitForElementToBeVisible(locator);
        String text = element.getText();
        logger.debug("Got text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = waitForElementToBePresent(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get attribute value
     */
    protected String getAttributeValue(By locator, String attribute) {
        WebElement element = waitForElementToBePresent(locator);
        String value = element.getAttribute(attribute);
        logger.debug("Got attribute '{}' value '{}' from element: {}", attribute, value, locator);
        return value;
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        WebElement element = waitForElementToBePresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.debug("Scrolled to element: {}", locator);
    }

    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Refresh page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        logger.debug("Page refreshed");
    }

    /**
     * Navigate to URL
     */
    public void navigateToUrl(String url) {
        driver.get(url);
        logger.info("Navigated to URL: {}", url);
    }

    /**
     * Wait for page to load completely
     */
    protected void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Get all elements matching locator
     */
    protected List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Check if text is present on page
     */
    protected boolean isTextPresentOnPage(String text) {
        return driver.getPageSource().contains(text);
    }
}