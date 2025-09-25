package com.hellobooks.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Page object for Onboarding functionality
 */
public class OnboardingPage extends BasePage {

    // Step navigation
    private final By nextButton = By.cssSelector("button:contains('Next'), button:contains('Continue'), [data-testid*='next']");
    private final By backButton = By.cssSelector("button:contains('Back'), button:contains('Previous'), [data-testid*='back']");
    private final By skipButton = By.cssSelector("button:contains('Skip'), a:contains('Skip'), [data-testid*='skip']");
    private final By finishButton = By.cssSelector("button:contains('Finish'), button:contains('Complete'), button:contains('Get Started'), [data-testid*='finish']");
    
    // Organization/Company setup fields
    private final By organizationNameField = By.cssSelector("input[name*='organization'], input[name*='company'], input[placeholder*='organization' i], input[placeholder*='company' i], #organizationName, #companyName");
    private final By industryDropdown = By.cssSelector("select[name*='industry'], select[name*='sector'], [data-testid*='industry']");
    private final By companySizeDropdown = By.cssSelector("select[name*='size'], select[name*='employees'], [data-testid*='company-size']");
    private final By countryDropdown = By.cssSelector("select[name*='country'], [data-testid*='country']");
    private final By timezoneDropdown = By.cssSelector("select[name*='timezone'], [data-testid*='timezone']");
    private final By currencyDropdown = By.cssSelector("select[name*='currency'], [data-testid*='currency']");
    
    // User profile fields
    private final By jobTitleField = By.cssSelector("input[name*='title'], input[name*='position'], input[placeholder*='title' i], #jobTitle");
    private final By departmentField = By.cssSelector("input[name*='department'], select[name*='department'], [data-testid*='department']");
    
    // Progress indicators
    private final By progressBar = By.cssSelector(".progress, .stepper, [data-testid*='progress']");
    private final By stepIndicator = By.cssSelector(".step, .step-indicator, [data-testid*='step']");
    
    // Messages and validation
    private final By errorMessage = By.cssSelector(".error, .alert-error, .field-error, [data-testid*='error']");
    private final By successMessage = By.cssSelector(".success, .alert-success, [data-testid*='success']");
    private final By welcomeMessage = By.cssSelector("h1:contains('Welcome'), h2:contains('Welcome'), [data-testid*='welcome']");
    
    // Completion indicators
    private final By dashboardIndicator = By.cssSelector("[data-testid='dashboard'], .dashboard, h1:contains('Dashboard')");
    private final By completionMessage = By.cssSelector("*:contains('setup complete'), *:contains('onboarding complete'), [data-testid*='complete']");

    public OnboardingPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if on onboarding page
     */
    public boolean isOnOnboardingPage() {
        return getCurrentUrl().contains("onboarding") ||
               getCurrentUrl().contains("setup") ||
               isElementDisplayed(welcomeMessage) ||
               isElementDisplayed(progressBar) ||
               isElementDisplayed(nextButton);
    }

    /**
     * Enter organization name
     */
    public void enterOrganizationName(String organizationName) {
        if (isElementDisplayed(organizationNameField)) {
            enterText(organizationNameField, organizationName);
            logger.info("Entered organization name: {}", organizationName);
        } else {
            logger.warn("Organization name field not found");
        }
    }

    /**
     * Select industry
     */
    public void selectIndustry(String industry) {
        if (isElementDisplayed(industryDropdown)) {
            Select select = new Select(driver.findElement(industryDropdown));
            select.selectByVisibleText(industry);
            logger.info("Selected industry: {}", industry);
        } else {
            logger.warn("Industry dropdown not found");
        }
    }

    /**
     * Select company size
     */
    public void selectCompanySize(String size) {
        if (isElementDisplayed(companySizeDropdown)) {
            Select select = new Select(driver.findElement(companySizeDropdown));
            select.selectByVisibleText(size);
            logger.info("Selected company size: {}", size);
        } else {
            logger.warn("Company size dropdown not found");
        }
    }

    /**
     * Select country
     */
    public void selectCountry(String country) {
        if (isElementDisplayed(countryDropdown)) {
            Select select = new Select(driver.findElement(countryDropdown));
            select.selectByVisibleText(country);
            logger.info("Selected country: {}", country);
        } else {
            logger.warn("Country dropdown not found");
        }
    }

    /**
     * Select timezone
     */
    public void selectTimezone(String timezone) {
        if (isElementDisplayed(timezoneDropdown)) {
            Select select = new Select(driver.findElement(timezoneDropdown));
            select.selectByVisibleText(timezone);
            logger.info("Selected timezone: {}", timezone);
        } else {
            logger.warn("Timezone dropdown not found");
        }
    }

    /**
     * Select currency
     */
    public void selectCurrency(String currency) {
        if (isElementDisplayed(currencyDropdown)) {
            Select select = new Select(driver.findElement(currencyDropdown));
            select.selectByVisibleText(currency);
            logger.info("Selected currency: {}", currency);
        } else {
            logger.warn("Currency dropdown not found");
        }
    }

    /**
     * Enter job title
     */
    public void enterJobTitle(String jobTitle) {
        if (isElementDisplayed(jobTitleField)) {
            enterText(jobTitleField, jobTitle);
            logger.info("Entered job title: {}", jobTitle);
        } else {
            logger.warn("Job title field not found");
        }
    }

    /**
     * Click next button
     */
    public void clickNext() {
        if (isElementDisplayed(nextButton)) {
            clickElement(nextButton);
            logger.info("Clicked next button");
        } else {
            logger.warn("Next button not found or not enabled");
        }
    }

    /**
     * Click back button
     */
    public void clickBack() {
        if (isElementDisplayed(backButton)) {
            clickElement(backButton);
            logger.info("Clicked back button");
        } else {
            logger.warn("Back button not found");
        }
    }

    /**
     * Click skip button
     */
    public void clickSkip() {
        if (isElementDisplayed(skipButton)) {
            clickElement(skipButton);
            logger.info("Clicked skip button");
        } else {
            logger.warn("Skip button not found");
        }
    }

    /**
     * Click finish button
     */
    public void clickFinish() {
        if (isElementDisplayed(finishButton)) {
            clickElement(finishButton);
            logger.info("Clicked finish button");
        } else {
            logger.warn("Finish button not found");
        }
    }

    /**
     * Complete organization setup with minimal data
     */
    public void completeOrganizationSetupMinimal(String organizationName) {
        enterOrganizationName(organizationName);
        clickNext();
        logger.info("Completed minimal organization setup");
    }

    /**
     * Complete organization setup with full data
     */
    public void completeOrganizationSetupFull(String organizationName, String industry, String size, String country) {
        enterOrganizationName(organizationName);
        selectIndustry(industry);
        selectCompanySize(size);
        selectCountry(country);
        clickNext();
        logger.info("Completed full organization setup");
    }

    /**
     * Skip current step
     */
    public void skipCurrentStep() {
        if (isElementDisplayed(skipButton)) {
            clickSkip();
        } else {
            clickNext();
        }
        logger.info("Skipped current onboarding step");
    }

    /**
     * Complete all onboarding steps with minimal data
     */
    public void completeOnboardingMinimal() {
        int maxSteps = 5; // Prevent infinite loops
        int currentStep = 0;
        
        while (isOnOnboardingPage() && currentStep < maxSteps) {
            // Fill minimal required data if present
            if (isElementDisplayed(organizationNameField)) {
                enterOrganizationName("Test Company");
            }
            
            // Try to proceed to next step
            if (isElementDisplayed(finishButton)) {
                clickFinish();
                break;
            } else if (isElementDisplayed(nextButton)) {
                clickNext();
            } else if (isElementDisplayed(skipButton)) {
                clickSkip();
            }
            
            currentStep++;
            // Wait for page transition
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Completed onboarding process");
    }

    /**
     * Check if next button is enabled
     */
    public boolean isNextButtonEnabled() {
        return isElementEnabled(nextButton);
    }

    /**
     * Check if finish button is displayed
     */
    public boolean isFinishButtonDisplayed() {
        return isElementDisplayed(finishButton);
    }

    /**
     * Get current step information
     */
    public String getCurrentStepInfo() {
        if (isElementDisplayed(stepIndicator)) {
            return getText(stepIndicator);
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
     * Check if onboarding is completed
     */
    public boolean isOnboardingCompleted() {
        // Wait for potential redirect
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return isElementDisplayed(dashboardIndicator) ||
               isElementDisplayed(completionMessage) ||
               getCurrentUrl().contains("dashboard") ||
               getCurrentUrl().contains("home") ||
               !getCurrentUrl().contains("onboarding");
    }

    /**
     * Check if error is displayed
     */
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }
}