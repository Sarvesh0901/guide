package com.hellobooks.automation.utils;

import com.hellobooks.automation.config.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for capturing screenshots
 */
public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String SCREENSHOT_DIR = "target/screenshots";

    /**
     * Capture screenshot and save to file
     * @param driver WebDriver instance
     * @param testName test name for screenshot filename
     * @return path to captured screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (!config.shouldCaptureScreenshots()) {
            return null;
        }

        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_%s.png", testName.replaceAll("[^a-zA-Z0-9]", "_"), timestamp);
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            // Capture screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);

            FileUtils.copyFile(sourceFile, destFile);
            logger.info("Screenshot captured: {}", filePath);

            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Capture screenshot for failed test
     * @param driver WebDriver instance
     * @param testName test name
     * @return path to captured screenshot
     */
    public static String captureFailureScreenshot(WebDriver driver, String testName) {
        if (!config.screenshotOnFailure()) {
            return null;
        }
        return captureScreenshot(driver, testName + "_FAILED");
    }

    /**
     * Get screenshot as byte array for Allure reporting
     * @param driver WebDriver instance
     * @return screenshot as byte array
     */
    public static byte[] getScreenshotAsBytes(WebDriver driver) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes", e);
            return new byte[0];
        }
    }
}