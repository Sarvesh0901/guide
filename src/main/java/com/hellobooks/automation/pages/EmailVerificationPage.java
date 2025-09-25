package com.hellobooks.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for Email Verification functionality
 */
public class EmailVerificationPage extends BasePage {

    // Locators for email verification elements
    private final By verificationCodeField = By.cssSelector("input[name*='code'], input[name*='otp'], input[placeholder*='code' i], input[placeholder*='verification' i], #verificationCode, #otp");
    private final By verifyButton = By.cssSelector("button[type='submit'], button:contains('Verify'), button:contains('Confirm'), input[type='submit']");
    private final By resendCodeLink = By.cssSelector("a:contains('Resend'), button:contains('Resend'), [data-testid*='resend']");
    private final By changeEmailLink = By.cssSelector("a:contains('Change Email'), a:contains('Different Email'), [data-testid*='change-email']");
    
    // Message locators
    private final By verificationMessage = By.cssSelector("*:contains('verification code'), *:contains('enter the code'), *:contains('check your email')");
    private final By errorMessage = By.cssSelector(".error, .alert-error, .notification-error, [data-testid*='error']");
    private final By successMessage = By.cssSelector(".success, .alert-success, .notification-success, [data-testid*='success']");
    
    // Navigation indicators
    private final By onboardingIndicator = By.cssSelector("[data-testid*='onboarding'], .onboarding, *:contains('Welcome'), h1:contains('Setup')");
    private final By dashboardIndicator = By.cssSelector("[data-testid='dashboard'], .dashboard, h1:contains('Dashboard')");

    public EmailVerificationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if on verification page
     */
    public boolean isOnVerificationPage() {
        return getCurrentUrl().contains("verify") || 
               getCurrentUrl().contains("confirmation") ||
               isElementDisplayed(verificationCodeField) ||
               isElementDisplayed(verificationMessage);
    }

    /**
     * Enter verification code
     */
    public void enterVerificationCode(String code) {
        if (isElementDisplayed(verificationCodeField)) {
            enterText(verificationCodeField, code);
            logger.info("Entered verification code");
        } else {
            logger.warn("Verification code field not found");
        }
    }

    /**
     * Click verify button
     */
    public void clickVerifyButton() {
        if (isElementDisplayed(verifyButton)) {
            clickElement(verifyButton);
            logger.info("Clicked verify button");
        } else {
            logger.warn("Verify button not found");
        }
    }

    /**
     * Click resend code link
     */
    public void clickResendCode() {
        if (isElementDisplayed(resendCodeLink)) {
            clickElement(resendCodeLink);
            logger.info("Clicked resend code link");
        } else {
            logger.warn("Resend code link not found");
        }
    }

    /**
     * Complete verification with code
     */
    public void verifyWithCode(String code) {
        enterVerificationCode(code);
        clickVerifyButton();
        logger.info("Completed verification with provided code");
    }

    /**
     * Complete verification with mock OTP
     */
    public void verifyWithMockOTP() {
        String mockCode = config.getMockOtpValue();
        verifyWithCode(mockCode);
        logger.info("Completed verification with mock OTP");
    }

    /**
     * Get verification message
     */
    public String getVerificationMessage() {
        if (isElementDisplayed(verificationMessage)) {
            return getText(verificationMessage);
        }
        return "";
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        if (isElementDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }

    /**
     * Check if verification was successful
     */
    public boolean isVerificationSuccessful() {
        // Wait for potential redirect
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return isElementDisplayed(successMessage) ||
               isElementDisplayed(onboardingIndicator) ||
               isElementDisplayed(dashboardIndicator) ||
               getCurrentUrl().contains("onboarding") ||
               getCurrentUrl().contains("dashboard") ||
               getCurrentUrl().contains("home");
    }

    /**
     * Check if error is displayed
     */
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }
}