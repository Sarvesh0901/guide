package com.hellobooks.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page object for Signup/Registration functionality
 */
public class SignupPage extends BasePage {

    // Form field locators - Generic selectors that should work with most applications
    private final By firstNameField = By.cssSelector("input[name*='firstName'], input[name*='first_name'], input[placeholder*='first name' i], #firstName, #first_name");
    private final By lastNameField = By.cssSelector("input[name*='lastName'], input[name*='last_name'], input[placeholder*='last name' i], #lastName, #last_name");
    private final By emailField = By.cssSelector("input[type='email'], input[name*='email'], input[placeholder*='email' i], #email");
    private final By passwordField = By.cssSelector("input[type='password'], input[name*='password'], input[placeholder*='password' i], #password");
    private final By confirmPasswordField = By.cssSelector("input[name*='confirm'], input[name*='repeat'], input[placeholder*='confirm' i], #confirmPassword, #confirm_password");
    private final By phoneField = By.cssSelector("input[type='tel'], input[name*='phone'], input[placeholder*='phone' i], #phone");
    private final By companyField = By.cssSelector("input[name*='company'], input[name*='organization'], input[placeholder*='company' i], #company");
    
    // Checkboxes and buttons
    private final By termsCheckbox = By.cssSelector("input[type='checkbox'][name*='terms'], input[type='checkbox'][id*='terms'], input[type='checkbox'][name*='agree']");
    private final By signupButton = By.cssSelector("button[type='submit'], button:contains('Sign Up'), button:contains('Register'), button:contains('Create Account'), input[type='submit']");
    private final By loginLink = By.cssSelector("a[href*='login'], a:contains('Login'), a:contains('Sign In')");
    
    // Error message locators
    private final By emailErrorMessage = By.cssSelector(".error:contains('email'), .field-error:contains('email'), [data-testid*='email-error'], .invalid-feedback:contains('email')");
    private final By passwordErrorMessage = By.cssSelector(".error:contains('password'), .field-error:contains('password'), [data-testid*='password-error'], .invalid-feedback:contains('password')");
    private final By confirmPasswordErrorMessage = By.cssSelector(".error:contains('confirm'), .field-error:contains('confirm'), [data-testid*='confirm-error']");
    private final By firstNameErrorMessage = By.cssSelector(".error:contains('first'), .field-error:contains('name'), [data-testid*='firstname-error']");
    private final By generalErrorMessage = By.cssSelector(".error, .alert-error, .notification-error, [data-testid*='error'], .invalid-feedback");
    
    // Success and navigation indicators
    private final By successMessage = By.cssSelector(".success, .alert-success, .notification-success, [data-testid*='success']");
    private final By verificationMessage = By.cssSelector("*:contains('verification'), *:contains('confirm your email'), *:contains('check your email')");
    private final By onboardingIndicator = By.cssSelector("[data-testid*='onboarding'], .onboarding, *:contains('Welcome'), *:contains('Setup')");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to signup page
     */
    public void navigateToSignupPage() {
        String signupUrl = config.getBaseUrl() + "/signup";
        navigateToUrl(signupUrl);
        waitForPageToLoad();
        logger.info("Navigated to signup page");
    }

    /**
     * Navigate to signup page via register URL
     */
    public void navigateToRegisterPage() {
        String registerUrl = config.getBaseUrl() + "/register";
        navigateToUrl(registerUrl);
        waitForPageToLoad();
        logger.info("Navigated to register page");
    }

    /**
     * Enter first name
     */
    public void enterFirstName(String firstName) {
        if (isElementDisplayed(firstNameField)) {
            enterText(firstNameField, firstName);
            logger.info("Entered first name: {}", firstName);
        } else {
            logger.warn("First name field not found");
        }
    }

    /**
     * Enter last name
     */
    public void enterLastName(String lastName) {
        if (isElementDisplayed(lastNameField)) {
            enterText(lastNameField, lastName);
            logger.info("Entered last name: {}", lastName);
        } else {
            logger.warn("Last name field not found");
        }
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
     * Enter password confirmation
     */
    public void enterConfirmPassword(String confirmPassword) {
        if (isElementDisplayed(confirmPasswordField)) {
            enterText(confirmPasswordField, confirmPassword);
            logger.info("Entered password confirmation");
        } else {
            logger.warn("Confirm password field not found");
        }
    }

    /**
     * Enter phone number
     */
    public void enterPhone(String phone) {
        if (isElementDisplayed(phoneField)) {
            enterText(phoneField, phone);
            logger.info("Entered phone: {}", phone);
        } else {
            logger.warn("Phone field not found");
        }
    }

    /**
     * Enter company name
     */
    public void enterCompany(String company) {
        if (isElementDisplayed(companyField)) {
            enterText(companyField, company);
            logger.info("Entered company: {}", company);
        } else {
            logger.warn("Company field not found");
        }
    }

    /**
     * Accept terms and conditions
     */
    public void acceptTerms(boolean accept) {
        if (isElementDisplayed(termsCheckbox)) {
            WebElement checkbox = driver.findElement(termsCheckbox);
            if (checkbox.isSelected() != accept) {
                clickElement(termsCheckbox);
                logger.info("Set terms acceptance to: {}", accept);
            }
        } else {
            logger.warn("Terms checkbox not found");
        }
    }

    /**
     * Click signup button
     */
    public void clickSignupButton() {
        clickElement(signupButton);
        logger.info("Clicked signup button");
    }

    /**
     * Click login link
     */
    public void clickLoginLink() {
        if (isElementDisplayed(loginLink)) {
            clickElement(loginLink);
            logger.info("Clicked login link");
        } else {
            logger.warn("Login link not found");
        }
    }

    /**
     * Perform complete signup with minimal required fields
     */
    public void signupMinimal(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        if (isElementDisplayed(confirmPasswordField)) {
            enterConfirmPassword(password);
        }
        if (isElementDisplayed(termsCheckbox)) {
            acceptTerms(true);
        }
        clickSignupButton();
        logger.info("Performed minimal signup for email: {}", email);
    }

    /**
     * Perform complete signup with all fields
     */
    public void signupComplete(String firstName, String lastName, String email, String password, String phone, String company) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        if (isElementDisplayed(confirmPasswordField)) {
            enterConfirmPassword(password);
        }
        enterPhone(phone);
        enterCompany(company);
        if (isElementDisplayed(termsCheckbox)) {
            acceptTerms(true);
        }
        clickSignupButton();
        logger.info("Performed complete signup for email: {}", email);
    }

    /**
     * Check if password is masked
     */
    public boolean isPasswordMasked() {
        String passwordType = getAttributeValue(passwordField, "type");
        return "password".equals(passwordType);
    }

    /**
     * Check if confirm password is masked
     */
    public boolean isConfirmPasswordMasked() {
        if (isElementDisplayed(confirmPasswordField)) {
            String passwordType = getAttributeValue(confirmPasswordField, "type");
            return "password".equals(passwordType);
        }
        return false;
    }

    /**
     * Check if signup button is enabled
     */
    public boolean isSignupButtonEnabled() {
        return isElementEnabled(signupButton);
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
     * Get confirm password error message
     */
    public String getConfirmPasswordErrorMessage() {
        if (isElementDisplayed(confirmPasswordErrorMessage)) {
            return getText(confirmPasswordErrorMessage);
        }
        return "";
    }

    /**
     * Get first name error message
     */
    public String getFirstNameErrorMessage() {
        if (isElementDisplayed(firstNameErrorMessage)) {
            return getText(firstNameErrorMessage);
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
     * Check if any error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(generalErrorMessage) || 
               isElementDisplayed(emailErrorMessage) || 
               isElementDisplayed(passwordErrorMessage) ||
               isElementDisplayed(confirmPasswordErrorMessage) ||
               isElementDisplayed(firstNameErrorMessage);
    }

    /**
     * Check if signup was successful
     */
    public boolean isSignupSuccessful() {
        // Wait a bit for potential redirect or success message
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return isElementDisplayed(successMessage) || 
               isElementDisplayed(verificationMessage) ||
               getCurrentUrl().contains("verify") ||
               getCurrentUrl().contains("confirmation") ||
               getCurrentUrl().contains("onboarding") ||
               isElementDisplayed(onboardingIndicator);
    }

    /**
     * Check if on signup page
     */
    public boolean isOnSignupPage() {
        return getCurrentUrl().contains("signup") || 
               getCurrentUrl().contains("register") || 
               isElementDisplayed(signupButton);
    }

    /**
     * Check if verification step is shown
     */
    public boolean isVerificationStepShown() {
        return isElementDisplayed(verificationMessage) ||
               getCurrentUrl().contains("verify") ||
               getCurrentUrl().contains("confirmation");
    }

    /**
     * Get email field value
     */
    public String getEmailFieldValue() {
        return getAttributeValue(emailField, "value");
    }

    /**
     * Clear all form fields
     */
    public void clearAllFields() {
        if (isElementDisplayed(firstNameField)) driver.findElement(firstNameField).clear();
        if (isElementDisplayed(lastNameField)) driver.findElement(lastNameField).clear();
        if (isElementDisplayed(emailField)) driver.findElement(emailField).clear();
        if (isElementDisplayed(passwordField)) driver.findElement(passwordField).clear();
        if (isElementDisplayed(confirmPasswordField)) driver.findElement(confirmPasswordField).clear();
        if (isElementDisplayed(phoneField)) driver.findElement(phoneField).clear();
        if (isElementDisplayed(companyField)) driver.findElement(companyField).clear();
        logger.info("Cleared all signup form fields");
    }

    /**
     * Check if all required fields are present
     */
    public boolean areRequiredFieldsPresent() {
        return isElementDisplayed(emailField) && isElementDisplayed(passwordField) && isElementDisplayed(signupButton);
    }
}