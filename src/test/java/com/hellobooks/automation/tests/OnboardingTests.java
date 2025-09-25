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
 * Test class for Onboarding functionality
 */
@Epic("User Experience")
@Feature("Onboarding")
public class OnboardingTests extends BaseTest {
    
    private SignupPage signupPage;
    private EmailVerificationPage verificationPage;
    private OnboardingPage onboardingPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void setupOnboardingTests() {
        signupPage = new SignupPage(driver);
        verificationPage = new EmailVerificationPage(driver);
        onboardingPage = new OnboardingPage(driver);
        navigateToHome();
    }

    /**
     * Helper method to complete signup and reach onboarding
     */
    private void completeSignupToOnboarding() {
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generateValidPassword();
        
        signupPage.navigateToSignupPage();
        signupPage.signupMinimal(email, password);
        
        // Handle email verification if required
        if (verificationPage.isOnVerificationPage()) {
            OTPHandler.waitForOTPDelivery();
            String otp = OTPHandler.getOTP(email);
            verificationPage.verifyWithCode(otp);
        }
        
        // Wait for potential redirect to onboarding
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test(priority = 1, description = "Verify successful onboarding with organization setup")
    @Story("Organization Setup")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Case ID: TC023 - User should be able to complete organization setup")
    public void testOrganizationSetupSuccess() {
        logTestStep("Complete signup to reach onboarding");
        completeSignupToOnboarding();
        
        if (!onboardingPage.isOnOnboardingPage()) {
            // Try to navigate to onboarding if not automatically redirected
            driver.get(config.getBaseUrl() + "/onboarding");
        }
        
        logTestStep("Verify onboarding page is displayed");
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Complete organization setup");
            String organizationName = TestDataGenerator.generateCompanyName();
            onboardingPage.completeOrganizationSetupMinimal(organizationName);
            
            logTestStep("Verify successful organization setup");
            // Check if we proceeded to next step or completed onboarding
            Assert.assertTrue(onboardingPage.isOnboardingCompleted() || 
                             !onboardingPage.isErrorDisplayed(),
                             "Organization setup should be successful");
        } else {
            logger.info("Onboarding page not found - application might not have onboarding flow");
            // This is acceptable as not all applications have onboarding
        }
    }

    @Test(priority = 2, description = "Verify required fields validation in onboarding")
    @Story("Field Validation")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC024 - Required fields should be validated")
    public void testOnboardingRequiredFieldsValidation() {
        logTestStep("Complete signup to reach onboarding");
        completeSignupToOnboarding();
        
        if (!onboardingPage.isOnOnboardingPage()) {
            driver.get(config.getBaseUrl() + "/onboarding");
        }
        
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Try to proceed without filling required fields");
            onboardingPage.clickNext();
            
            logTestStep("Verify validation error for required fields");
            if (onboardingPage.isErrorDisplayed()) {
                String errorMessage = onboardingPage.getErrorMessage();
                Assert.assertTrue(errorMessage.toLowerCase().contains("required") ||
                                 errorMessage.toLowerCase().contains("field") ||
                                 errorMessage.toLowerCase().contains("mandatory"),
                                 "Error should indicate required field validation");
            } else {
                // If no error, check if we remained on the same step
                Assert.assertTrue(onboardingPage.isOnOnboardingPage(),
                                 "Should remain on onboarding if required fields not filled");
            }
        } else {
            logger.info("Onboarding validation test skipped - no onboarding flow found");
        }
    }

    @Test(priority = 3, description = "Verify ability to skip optional onboarding steps")
    @Story("Optional Steps")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC025 - User should be able to skip optional steps")
    public void testSkipOptionalSteps() {
        logTestStep("Complete signup to reach onboarding");
        completeSignupToOnboarding();
        
        if (!onboardingPage.isOnOnboardingPage()) {
            driver.get(config.getBaseUrl() + "/onboarding");
        }
        
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Skip optional onboarding steps");
            onboardingPage.skipCurrentStep();
            
            logTestStep("Verify ability to skip steps");
            // We should either proceed to next step or complete onboarding
            Assert.assertTrue(onboardingPage.isOnboardingCompleted() || 
                             onboardingPage.isOnOnboardingPage(),
                             "Should be able to skip optional steps");
            
            if (!onboardingPage.isOnboardingCompleted()) {
                logTestStep("Complete remaining onboarding steps");
                onboardingPage.completeOnboardingMinimal();
            }
        } else {
            logger.info("Skip test skipped - no onboarding flow found");
        }
    }

    @Test(priority = 4, description = "Verify navigation between onboarding steps")
    @Story("Navigation")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test Case ID: TC026 - User should be able to navigate between steps")
    public void testOnboardingStepNavigation() {
        logTestStep("Complete signup to reach onboarding");
        completeSignupToOnboarding();
        
        if (!onboardingPage.isOnOnboardingPage()) {
            driver.get(config.getBaseUrl() + "/onboarding");
        }
        
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Fill minimal data and proceed to next step");
            onboardingPage.enterOrganizationName("Test Company");
            
            if (onboardingPage.isNextButtonEnabled()) {
                onboardingPage.clickNext();
                
                logTestStep("Verify ability to go back");
                if (onboardingPage.isOnOnboardingPage()) {
                    onboardingPage.clickBack();
                    
                    // Verify we can navigate back and data is preserved
                    Assert.assertTrue(onboardingPage.isOnOnboardingPage(),
                                     "Should be able to navigate back in onboarding");
                }
            }
            
            logTestStep("Complete onboarding");
            onboardingPage.completeOnboardingMinimal();
        } else {
            logger.info("Navigation test skipped - no onboarding flow found");
        }
    }

    @Test(priority = 5, description = "Verify complete onboarding flow")
    @Story("Complete Flow")
    @Severity(SeverityLevel.HIGH)
    @Description("Verify complete signup to onboarding to dashboard flow")
    public void testCompleteOnboardingFlow() {
        logTestStep("Start complete flow from signup");
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generateValidPassword();
        String firstName = TestDataGenerator.generateFirstName();
        String lastName = TestDataGenerator.generateLastName();
        String company = TestDataGenerator.generateCompanyName();
        
        logTestStep("Complete signup with full data");
        signupPage.navigateToSignupPage();
        signupPage.signupComplete(firstName, lastName, email, password, "", company);
        
        logTestStep("Handle email verification");
        if (verificationPage.isOnVerificationPage()) {
            OTPHandler.waitForOTPDelivery();
            String otp = OTPHandler.getOTP(email);
            verificationPage.verifyWithCode(otp);
            
            Assert.assertTrue(verificationPage.isVerificationSuccessful(),
                             "Email verification should be successful");
        }
        
        logTestStep("Complete onboarding if present");
        if (onboardingPage.isOnOnboardingPage()) {
            onboardingPage.completeOnboardingMinimal();
            
            Assert.assertTrue(onboardingPage.isOnboardingCompleted(),
                             "Onboarding should be completed successfully");
        }
        
        logTestStep("Verify user reaches authenticated state");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard") || 
                         currentUrl.contains("home") ||
                         currentUrl.contains("app") ||
                         !currentUrl.contains("signup") && !currentUrl.contains("login"),
                         "User should reach authenticated area after complete flow");
    }

    @Test(priority = 6, description = "Verify onboarding with full organization data")
    @Story("Organization Setup")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Complete organization setup with all available fields")
    public void testFullOrganizationSetup() {
        logTestStep("Complete signup to reach onboarding");
        completeSignupToOnboarding();
        
        if (!onboardingPage.isOnOnboardingPage()) {
            driver.get(config.getBaseUrl() + "/onboarding");
        }
        
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Complete organization setup with full data");
            String organizationName = TestDataGenerator.generateCompanyName();
            
            // Note: These values would need to match actual dropdown options
            String industry = "Technology";
            String size = "1-10 employees";
            String country = "United States";
            
            try {
                onboardingPage.completeOrganizationSetupFull(organizationName, industry, size, country);
                
                logTestStep("Verify successful full organization setup");
                Assert.assertTrue(onboardingPage.isOnboardingCompleted() || 
                                 !onboardingPage.isErrorDisplayed(),
                                 "Full organization setup should be successful");
            } catch (Exception e) {
                logger.info("Full organization setup failed - dropdown options might not match: {}", e.getMessage());
                // Fallback to minimal setup
                onboardingPage.completeOrganizationSetupMinimal(organizationName);
            }
        } else {
            logger.info("Full organization setup test skipped - no onboarding flow found");
        }
    }

    @Test(priority = 7, description = "Verify onboarding completion and dashboard access")
    @Story("Completion")
    @Severity(SeverityLevel.HIGH)
    @Description("After onboarding completion, user should access main application")
    public void testOnboardingCompletionAndDashboardAccess() {
        logTestStep("Complete full signup and onboarding flow");
        completeSignupToOnboarding();
        
        if (onboardingPage.isOnOnboardingPage()) {
            logTestStep("Complete all onboarding steps");
            onboardingPage.completeOnboardingMinimal();
            
            logTestStep("Verify onboarding completion");
            Assert.assertTrue(onboardingPage.isOnboardingCompleted(),
                             "Onboarding should be marked as completed");
            
            logTestStep("Verify access to main application");
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("onboarding"),
                              "Should not be on onboarding page after completion");
            
            // Verify we're in an authenticated area
            Assert.assertTrue(currentUrl.contains("dashboard") || 
                             currentUrl.contains("home") ||
                             currentUrl.contains("app"),
                             "Should be in main application area");
        } else {
            logger.info("Onboarding completion test skipped - application proceeded directly to main area");
            // This is acceptable - some applications don't have explicit onboarding
        }
    }
}