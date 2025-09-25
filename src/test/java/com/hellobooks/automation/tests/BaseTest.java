package com.hellobooks.automation.tests;

import com.hellobooks.automation.config.ConfigManager;
import com.hellobooks.automation.utils.WebDriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * Base test class with common setup and teardown
 */
public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;
    protected ConfigManager config;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    @Step("Setup test environment")
    public void setUp(String browser) {
        logger.info("Setting up test environment");
        
        config = ConfigManager.getInstance();
        
        // Override browser if parameter is provided
        if (browser != null && !browser.isEmpty()) {
            System.setProperty("browser", browser);
        }
        
        // Create WebDriver instance
        driver = WebDriverFactory.createDriver();
        
        logger.info("Test setup completed. Browser: {}, BaseURL: {}", 
                   config.getBrowser(), config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    @Step("Cleanup test environment")
    public void tearDown() {
        logger.info("Cleaning up test environment");
        
        if (driver != null) {
            try {
                WebDriverFactory.quitDriver();
                logger.info("WebDriver instance closed successfully");
            } catch (Exception e) {
                logger.error("Error occurred while closing WebDriver", e);
            }
        }
    }

    /**
     * Get current test method name for logging and reporting
     */
    protected String getCurrentTestMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * Log test step
     */
    @Step("{stepDescription}")
    protected void logTestStep(String stepDescription) {
        logger.info("Test Step: {}", stepDescription);
    }

    /**
     * Navigate to base URL
     */
    @Step("Navigate to application home page")
    protected void navigateToHome() {
        driver.get(config.getBaseUrl());
        logger.info("Navigated to home page: {}", config.getBaseUrl());
    }
}