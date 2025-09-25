package com.hellobooks.automation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import com.hellobooks.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retry analyzer for failed tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int count = 0;
    private final int maxTry;

    public RetryAnalyzer() {
        this.maxTry = ConfigManager.getInstance().getRetryCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxTry) {
            count++;
            logger.info("Retrying test '{}' for the {} time", result.getMethod().getMethodName(), count);
            return true;
        }
        return false;
    }
}