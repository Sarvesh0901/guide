package com.hellobooks.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.hellobooks.automation.config.ConfigManager;
import com.hellobooks.automation.utils.ScreenshotUtils;
import com.hellobooks.automation.utils.WebDriverFactory;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReports listener for generating HTML reports
 */
public class ExtentReportListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(ExtentReportListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    @Override
    public void onStart(org.testng.ITestContext context) {
        setupExtentReports();
        logger.info("ExtentReports initialized");
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed and finalized");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        
        ExtentTest test = extent.createTest(testName, result.getMethod().getDescription())
                .assignCategory(className)
                .assignAuthor("QA Automation Team");
        
        extentTest.set(test);
        logger.info("Started test: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
            
            // Capture screenshot on pass if configured
            if (config.getBooleanProperty("screenshot.on.pass", false)) {
                attachScreenshot(result, "Pass Screenshot");
            }
        }
        logger.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure
            attachScreenshot(result, "Failure Screenshot");
        }
        logger.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable().getMessage());
        }
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    private void setupExtentReports() {
        String reportPath = config.getReportPath();
        
        // Create reports directory if it doesn't exist
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        // Generate report file name with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportFile = reportPath + File.separator + "HelloBooks_TestReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
        configureSparkReporter(sparkReporter);
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        extent.setSystemInfo("Application", "HelloBooks");
        extent.setSystemInfo("Environment", "Development");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", config.getBrowser());
        extent.setSystemInfo("Base URL", config.getBaseUrl());
    }

    private void configureSparkReporter(ExtentSparkReporter sparkReporter) {
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("HelloBooks Test Automation Report");
        sparkReporter.config().setReportName("Login & Signup Test Results");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        sparkReporter.config().setEncoding("utf-8");
    }

    private void attachScreenshot(ITestResult result, String description) {
        try {
            if (WebDriverFactory.getDriver() != null) {
                String screenshotPath = ScreenshotUtils.captureFailureScreenshot(
                    WebDriverFactory.getDriver(),
                    result.getMethod().getMethodName()
                );
                
                if (screenshotPath != null) {
                    ExtentTest test = extentTest.get();
                    if (test != null) {
                        test.addScreenCaptureFromPath(screenshotPath, description);
                    }
                    
                    // Attach to Allure as well
                    attachScreenshotToAllure();
                }
            }
        } catch (Exception e) {
            logger.error("Failed to attach screenshot", e);
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] attachScreenshotToAllure() {
        if (WebDriverFactory.getDriver() != null) {
            return ScreenshotUtils.getScreenshotAsBytes(WebDriverFactory.getDriver());
        }
        return new byte[0];
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }
}