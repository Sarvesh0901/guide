package com.hellobooks.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager for reading and managing test configuration properties
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties from: " + CONFIG_FILE_PATH, e);
        }
    }

    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    // Specific configuration getters
    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    public int getImplicitWait() {
        return getIntProperty("implicit.wait", 10);
    }

    public int getExplicitWait() {
        return getIntProperty("explicit.wait", 30);
    }

    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout", 30);
    }

    public String getTestEmailDomain() {
        return getProperty("test.email.domain", "@example.com");
    }

    public String getTestEmailPrefix() {
        return getProperty("test.email.prefix", "qa.automation");
    }

    public boolean useTimestampInEmail() {
        return getBooleanProperty("use.timestamp.in.email", true);
    }

    public String getOtpMode() {
        return getProperty("otp.mode", "mock");
    }

    public String getMockOtpValue() {
        return getProperty("otp.mock.value", "123456");
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }

    public boolean shouldCaptureScreenshots() {
        return getBooleanProperty("capture.screenshots", true);
    }

    public boolean screenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public String getReportPath() {
        return getProperty("report.path", "target/reports");
    }

    public String getAllureResultsDirectory() {
        return getProperty("allure.results.directory", "target/allure-results");
    }
}