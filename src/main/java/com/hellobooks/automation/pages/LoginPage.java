package com.hellobooks.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page object for Login functionality
 */
public class LoginPage extends BasePage {

    // Locators - These will need to be updated based on actual application elements
    private final By emailField = By.cssSelector("input[type='email'], input[name='email'], #email, input[placeholder*='email' i]");
    private final By passwordField = By.cssSelector("input[type='password'], input[name='password'], #password");
    private final By loginButton = By.cssSelector("button[type='submit'], button:contains('Login'), button:contains('Sign In'), input[type='submit']");
    private final By rememberMeCheckbox = By.cssSelector("input[type='checkbox'][name*='remember'], input[type='checkbox'][id*='remember']");
    private final By forgotPasswordLink = By.cssSelector("a[href*='forgot'], a:contains('Forgot Password'), a:contains('Reset Password')");
    private final By signupLink = By.cssSelector("a[href*='signup'], a[href*='register'], a:contains('Sign Up'), a:contains('Register')");
    
    // Error message locators
    private final By emailErrorMessage = By.cssSelector(".error:contains('email'), .field-error:contains('email'), [data-testid*='email-error']");
    private final By passwordErrorMessage = By.cssSelector(".error:contains('password'), .field-error:contains('password'), [data-testid*='password-error']");
    private final By generalErrorMessage = By.cssSelector(".error, .alert-error, .notification-error, [data-testid*='error']");
    
    // Success indicators
    private final By successMessage = By.cssSelector(".success, .alert-success, .notification-success");
    private final By dashboardIndicator = By.cssSelector("[data-testid='dashboard'], .dashboard, h1:contains('Dashboard')");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to login page
     */
    public void navigateToLoginPage() {
        String loginUrl = config.getBaseUrl() + "/login";
        navigateToUrl(loginUrl);
        waitForPageToLoad();
        logger.info("Navigated to login page");
    }

    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        waitForElementToBeVisible(emailField);
        enterText(emailField, email);
        logger.info("Entered email: {}", email);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        waitForElementToBeVisible(passwordField);
        enterText(passwordField, password);
        logger.info("Entered password");
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        clickElement(loginButton);
        logger.info("Clicked login button");
    }

    /**
     * Check/uncheck remember me
     */
    public void setRememberMe(boolean remember) {
        if (isElementDisplayed(rememberMeCheckbox)) {
            WebElement checkbox = driver.findElement(rememberMeCheckbox);
            if (checkbox.isSelected() != remember) {
                clickElement(rememberMeCheckbox);
                logger.info("Set remember me to: {}", remember);
            }
        } else {
            logger.warn("Remember me checkbox not found on page");
        }
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        if (isElementDisplayed(forgotPasswordLink)) {
            clickElement(forgotPasswordLink);
            logger.info("Clicked forgot password link");
        } else {
            logger.warn("Forgot password link not found");
        }
    }

    /**
     * Click signup link
     */
    public void clickSignupLink() {
        if (isElementDisplayed(signupLink)) {
            clickElement(signupLink);
            logger.info("Clicked signup link");
        } else {
            logger.warn("Signup link not found");
        }
    }

    /**
     * Perform complete login
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        logger.info("Performed login for user: {}", email);
    }

    /**
     * Perform login with remember me
     */
    public void loginWithRememberMe(String email, String password, boolean rememberMe) {
        enterEmail(email);
        enterPassword(password);
        setRememberMe(rememberMe);
        clickLoginButton();
        logger.info("Performed login with remember me {} for user: {}", rememberMe, email);
    }

    /**
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailField);
    }

    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }

    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButton);
    }

    /**
     * Check if password is masked
     */
    public boolean isPasswordMasked() {
        String passwordType = getAttributeValue(passwordField, "type");
        return "password".equals(passwordType);
    }

    /**
     * Get email field value
     */
    public String getEmailFieldValue() {
        return getAttributeValue(emailField, "value");
    }

    /**
     * Get email error message
     */
    public String getEmailErrorMessage() {
        if (isElementDisplayed(emailErrorMessage)) {
            return getText(emailErrorMessage);
        }
        return "";
    }

    /**
     * Get password error message
     */
    public String getPasswordErrorMessage() {
        if (isElementDisplayed(passwordErrorMessage)) {
            return getText(passwordErrorMessage);
        }
        return "";
    }

    /**
     * Get general error message
     */
    public String getGeneralErrorMessage() {
        if (isElementDisplayed(generalErrorMessage)) {
            return getText(generalErrorMessage);
        }
        return "";
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(generalErrorMessage) || 
               isElementDisplayed(emailErrorMessage) || 
               isElementDisplayed(passwordErrorMessage);
    }

    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        // Wait a bit for potential redirect
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check multiple indicators of successful login
        return isElementDisplayed(dashboardIndicator) || 
               getCurrentUrl().contains("dashboard") || 
               getCurrentUrl().contains("home") ||
               !getCurrentUrl().contains("login");
    }

    /**
     * Check if still on login page
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("login") || isElementDisplayed(loginButton);
    }

    /**
     * Clear all fields
     */
    public void clearAllFields() {
        if (isElementDisplayed(emailField)) {
            driver.findElement(emailField).clear();
        }
        if (isElementDisplayed(passwordField)) {
            driver.findElement(passwordField).clear();
        }
        logger.info("Cleared all login form fields");
    }
}