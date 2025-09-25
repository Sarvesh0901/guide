package com.hellobooks.automation.utils;

import com.hellobooks.automation.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * WebDriver factory for creating and managing browser instances
 */
public class WebDriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    public static WebDriver createDriver() {
        String browser = config.getBrowser().toLowerCase();
        WebDriver driver;

        logger.info("Creating {} driver instance", browser);

        switch (browser) {
            case "chrome":
                driver = createChromeDriver();
                break;
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "edge":
                driver = createEdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        configureDriver(driver);
        setDriver(driver);
        return driver;
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        // Note: Removed --disable-javascript as it breaks web app testing
        // Note: Removed --disable-plugins and --disable-images for better compatibility
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        if (config.getBooleanProperty("incognito", false)) {
            options.addArguments("--incognito");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        if (config.getBooleanProperty("incognito", false)) {
            options.addArguments("--inprivate");
        }

        return new EdgeDriver(options);
    }

    private static void configureDriver(WebDriver driver) {
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        
        // Maximize window if configured
        if (config.getBooleanProperty("maximize.window", true)) {
            driver.manage().window().maximize();
        }
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting WebDriver instance");
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    public static void removeDriver() {
        driverThreadLocal.remove();
    }
}