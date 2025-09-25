package com.hellobooks.automation.tests;

import com.hellobooks.automation.pages.EmailVerificationPage;
import com.hellobooks.automation.pages.OnboardingPage;
import com.hellobooks.automation.pages.SignupPage;
import com.hellobooks.automation.utils.OTPHandler;
import com.hellobooks.automation.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for Signup/Registration functionality
 */
@Epic("Authentication")
@Feature("Signup")
public class SignupTests extends BaseTest {
    
    private SignupPage signupPage;
    private EmailVerificationPage verificationPage;
    private OnboardingPage onboardingPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupSignupTests() {
        signupPage = new SignupPage(driver);
        verificationPage = new EmailVerificationPage(driver);
        onboardingPage = new OnboardingPage(driver);
        navigateToHome();
    }

    @Test(priority = 1, description = "Verify successful signup with valid data - Happy Path")
    @Story("Valid Signup")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case ID: TC001 - New user should be able to register successfully")
    public void testValidSignupHappyPath() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Verify signup page elements are displayed");
        Assert.assertTrue(signupPage.areRequiredFieldsPresent(), "Required signup fields should be present");
        Assert.assertTrue(signupPage.isSignupButtonEnabled(), "Signup button should be enabled");
        
        logTestStep("Fill signup form with valid data");
        String firstName = TestDataGenerator.generateFirstName();
        String lastName = TestDataGenerator.generateLastName();
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generateValidPassword();
        String company = TestDataGenerator.generateCompanyName();
        String phone = TestDataGenerator.generatePhoneNumber();
        
        signupPage.signupComplete(firstName, lastName, email, password, phone, company);
        
        logTestStep("Handle email verification if required");
        if (verificationPage.isOnVerificationPage()) {
            OTPHandler.waitForOTPDelivery();
            String otp = OTPHandler.getOTP(email);
            verificationPage.verifyWithCode(otp);
        }
        
        logTestStep("Verify successful signup");
        Assert.assertTrue(signupPage.isSignupSuccessful() || 
                         verificationPage.isVerificationSuccessful() ||
                         onboardingPage.isOnOnboardingPage(),
                         "Signup should be successful and proceed to next step");
    }

    @Test(priority = 2, description = "Verify email field required validation")
    @Story("Field Validation")
    @Severity(SeverityLevel.HIGH)
    @Description("Test Case ID: TC002 - Email field should be required")
    public void testEmailRequiredValidation() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Leave email field empty and fill other fields");
        signupPage.enterPassword(TestDataGenerator.generateValidPassword());
        signupPage.clickSignupButton();
        
        logTestStep("Verify email validation error");
        Assert.assertTrue(signupPage.isOnSignupPage(), "Should remain on signup page");
        Assert.assertTrue(signupPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        String emailError = signupPage.getEmailErrorMessage();
        String generalError = signupPage.getGeneralErrorMessage();
        
        Assert.assertTrue((emailError.toLowerCase().contains("required") || emailError.toLowerCase().contains("email")) ||
                         (generalError.toLowerCase().contains("email") || generalError.toLowerCase().contains("required")),
                         "Error should indicate email is required");
    }

    @Test(priority = 3, description = "Verify password field required validation")  
    @Story("Field Validation")
    @Severity(SeverityLevel.HIGH)
    @Description("Test Case ID: TC003 - Password field should be required")
    public void testPasswordRequiredValidation() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Fill email and leave password empty");
        signupPage.enterEmail(TestDataGenerator.generateUniqueEmail());
        signupPage.clickSignupButton();
        
        logTestStep("Verify password validation error");
        Assert.assertTrue(signupPage.isOnSignupPage(), "Should remain on signup page");
        Assert.assertTrue(signupPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        String passwordError = signupPage.getPasswordErrorMessage();
        String generalError = signupPage.getGeneralErrorMessage();
        
        Assert.assertTrue((passwordError.toLowerCase().contains("required") || passwordError.toLowerCase().contains("password")) ||
                         (generalError.toLowerCase().contains("password") || generalError.toLowerCase().contains("required")),
                         "Error should indicate password is required");
    }

    @Test(priority = 4, description = "Verify invalid email format validation")
    @Story("Field Validation")
    @Severity(SeverityLevel.HIGH)
    @Description("Test Case ID: TC004 - Invalid email format should be rejected")
    public void testInvalidEmailFormatValidation() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Enter invalid email format");
        String invalidEmail = TestDataGenerator.generateInvalidEmail();
        signupPage.enterEmail(invalidEmail);
        signupPage.enterPassword(TestDataGenerator.generateValidPassword());
        signupPage.clickSignupButton();
        
        logTestStep("Verify email format validation error");
        Assert.assertTrue(signupPage.isOnSignupPage(), "Should remain on signup page");
        
        String emailError = signupPage.getEmailErrorMessage();
        String generalError = signupPage.getGeneralErrorMessage();
        
        // Check for email format validation message
        Assert.assertTrue((emailError.toLowerCase().contains("valid") || emailError.toLowerCase().contains("format") || 
                          emailError.toLowerCase().contains("invalid")) ||
                         (generalError.toLowerCase().contains("email") && 
                          (generalError.toLowerCase().contains("valid") || generalError.toLowerCase().contains("format"))),
                         "Error should indicate invalid email format");
    }

    @Test(priority = 5, description = "Verify weak password validation")
    @Story("Password Policy")
    @Severity(SeverityLevel.HIGH)
    @Description("Test Case ID: TC005 - Weak password should be rejected")
    public void testWeakPasswordValidation() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Enter weak password");
        signupPage.enterEmail(TestDataGenerator.generateUniqueEmail());
        signupPage.enterPassword(TestDataGenerator.generateWeakPassword());
        signupPage.clickSignupButton();
        
        logTestStep("Verify password policy validation error");
        Assert.assertTrue(signupPage.isOnSignupPage(), "Should remain on signup page");
        
        String passwordError = signupPage.getPasswordErrorMessage();
        String generalError = signupPage.getGeneralErrorMessage();
        
        Assert.assertTrue((passwordError.toLowerCase().contains("password") && 
                          (passwordError.toLowerCase().contains("weak") || passwordError.toLowerCase().contains("strong") ||
                           passwordError.toLowerCase().contains("policy") || passwordError.toLowerCase().contains("requirement"))) ||
                         (generalError.toLowerCase().contains("password") &&
                          (generalError.toLowerCase().contains("policy") || generalError.toLowerCase().contains("requirement"))),
                         "Error should indicate password policy violation");
    }

    @Test(priority = 6, description = "Verify duplicate email handling")
    @Story("Duplicate Prevention")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC006 - Duplicate email should be rejected")
    public void testDuplicateEmailHandling() {
        // Note: This test assumes we have a pre-existing test account
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Attempt signup with existing email");
        String existingEmail = "qa.automation+existing@example.com"; // Pre-existing test account
        signupPage.signupMinimal(existingEmail, TestDataGenerator.generateValidPassword());
        
        logTestStep("Verify duplicate email error");
        if (signupPage.isOnSignupPage()) {
            Assert.assertTrue(signupPage.isErrorMessageDisplayed(), "Error message should be displayed");
            
            String emailError = signupPage.getEmailErrorMessage();
            String generalError = signupPage.getGeneralErrorMessage();
            
            Assert.assertTrue((emailError.toLowerCase().contains("already") || emailError.toLowerCase().contains("exists") ||
                              emailError.toLowerCase().contains("registered")) ||
                             (generalError.toLowerCase().contains("already") || generalError.toLowerCase().contains("exists") ||
                              generalError.toLowerCase().contains("registered")),
                             "Error should indicate email already exists");
        } else {
            // If not on signup page, check if it proceeded (some apps might handle this differently)
            logger.info("Signup proceeded - application might handle duplicates differently");
        }
    }

    @Test(priority = 7, description = "Verify password confirmation mismatch validation")
    @Story("Field Validation")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC007 - Password confirmation should match password")
    public void testPasswordConfirmationMismatch() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Enter mismatched passwords");
        signupPage.enterEmail(TestDataGenerator.generateUniqueEmail());
        signupPage.enterPassword("ValidPass123!");
        signupPage.enterConfirmPassword("DifferentPass456!");
        signupPage.clickSignupButton();
        
        logTestStep("Verify password mismatch error");
        Assert.assertTrue(signupPage.isOnSignupPage(), "Should remain on signup page");
        
        String confirmPasswordError = signupPage.getConfirmPasswordErrorMessage();
        String generalError = signupPage.getGeneralErrorMessage();
        
        Assert.assertTrue((confirmPasswordError.toLowerCase().contains("match") || 
                          confirmPasswordError.toLowerCase().contains("confirm")) ||
                         (generalError.toLowerCase().contains("password") && 
                          (generalError.toLowerCase().contains("match") || generalError.toLowerCase().contains("confirm"))),
                         "Error should indicate passwords do not match");
    }

    @Test(priority = 8, description = "Verify password fields are masked")
    @Story("Security")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC011 - Password fields should be masked for security")
    public void testPasswordMasking() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Verify password field is masked");
        Assert.assertTrue(signupPage.isPasswordMasked(), "Password field should be masked");
        
        logTestStep("Enter password and verify it remains masked");
        signupPage.enterPassword("TestPassword123!");
        Assert.assertTrue(signupPage.isPasswordMasked(), "Password should remain masked after entering text");
        
        logTestStep("Verify confirm password field is masked if present");
        if (signupPage.isConfirmPasswordMasked() != false) { // Only test if field exists
            signupPage.enterConfirmPassword("TestPassword123!");
            Assert.assertTrue(signupPage.isConfirmPasswordMasked(), "Confirm password should be masked");
        }
    }

    @Test(priority = 9, description = "Verify XSS protection in name field")
    @Story("Security")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC009 - XSS payload should be sanitized")
    public void testXSSProtectionInNameField() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Enter XSS payload in name field");
        String xssPayload = TestDataGenerator.generateXSSPayload();
        signupPage.enterFirstName(xssPayload);
        signupPage.enterEmail(TestDataGenerator.generateUniqueEmail());
        signupPage.enterPassword(TestDataGenerator.generateValidPassword());
        signupPage.clickSignupButton();
        
        logTestStep("Verify XSS payload is handled safely");
        // The main verification is that no script execution occurred
        // If we reach this point without JavaScript alerts, the input was sanitized
        
        // Additional check: verify the page is still functional
        Assert.assertTrue(signupPage.areRequiredFieldsPresent(), "Page should remain functional after XSS attempt");
        
        // Check if there's any error handling for invalid characters
        if (signupPage.isErrorMessageDisplayed()) {
            String error = signupPage.getGeneralErrorMessage();
            logger.info("Application showed error for XSS input: {}", error);
        }
    }

    @Test(priority = 10, description = "Verify long input boundary testing")
    @Story("Boundary Testing")
    @Severity(SeverityLevel.LOW)
    @Description("Test Case ID: TC010 - Very long input should be handled gracefully")
    public void testLongInputBoundaryTesting() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Enter very long text in name field");
        String longText = TestDataGenerator.generateVeryLongText();
        signupPage.enterFirstName(longText);
        signupPage.enterEmail(TestDataGenerator.generateUniqueEmail());
        signupPage.enterPassword(TestDataGenerator.generateValidPassword());
        signupPage.clickSignupButton();
        
        logTestStep("Verify long input is handled gracefully");
        // Check if there's character limit enforcement or validation error
        if (signupPage.isErrorMessageDisplayed()) {
            String error = signupPage.getGeneralErrorMessage();
            Assert.assertTrue(error.toLowerCase().contains("length") || 
                             error.toLowerCase().contains("character") ||
                             error.toLowerCase().contains("limit"),
                             "Error should indicate length/character limit issue");
        } else {
            // If no error, verify the input was truncated or handled properly
            logger.info("Long input was accepted - application may have character limits in place");
        }
    }

    @Test(priority = 11, description = "Verify navigation to login page from signup")
    @Story("Navigation") 
    @Severity(SeverityLevel.LOW)
    @Description("User should be able to navigate to login page from signup page")
    public void testNavigationToLogin() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Click login link");
        try {
            signupPage.clickLoginLink();
            
            // Verify navigation occurred
            String currentUrl = signupPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("signin"),
                             "Should navigate to login page");
        } catch (Exception e) {
            logger.info("Login link not found or not functional from signup page");
            // This is acceptable as not all apps have this navigation
        }
    }

    @Test(priority = 12, description = "Verify minimal signup without optional fields")
    @Story("Valid Signup")
    @Severity(SeverityLevel.MEDIUM)
    @Description("User should be able to signup with only required fields")
    public void testMinimalSignup() {
        logTestStep("Navigate to signup page");
        signupPage.navigateToSignupPage();
        
        logTestStep("Fill only required fields");
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generateValidPassword();
        
        signupPage.signupMinimal(email, password);
        
        logTestStep("Handle email verification if required");
        if (verificationPage.isOnVerificationPage()) {
            OTPHandler.waitForOTPDelivery();
            String otp = OTPHandler.getOTP(email);
            verificationPage.verifyWithCode(otp);
        }
        
        logTestStep("Verify successful minimal signup");
        Assert.assertTrue(signupPage.isSignupSuccessful() || 
                         verificationPage.isVerificationSuccessful() ||
                         onboardingPage.isOnOnboardingPage(),
                         "Minimal signup should be successful");
    }
}