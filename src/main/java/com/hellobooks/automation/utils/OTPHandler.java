package com.hellobooks.automation.utils;

import com.hellobooks.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OTP (One-Time Password) handler for email verification
 */
public class OTPHandler {
    private static final Logger logger = LoggerFactory.getLogger(OTPHandler.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Get OTP based on configured mode
     * @param email Email address for which OTP is requested
     * @return OTP code
     */
    public static String getOTP(String email) {
        String otpMode = config.getOtpMode().toLowerCase();
        
        switch (otpMode) {
            case "mock":
                return getMockOTP();
            case "external_api":
                return getOTPFromExternalAPI(email);
            case "manual_input":
                return getManualOTP();
            default:
                logger.warn("Unknown OTP mode: {}. Using mock OTP", otpMode);
                return getMockOTP();
        }
    }

    /**
     * Get mock OTP for testing
     */
    private static String getMockOTP() {
        String mockOTP = config.getMockOtpValue();
        logger.info("Using mock OTP: {}", mockOTP);
        return mockOTP;
    }

    /**
     * Get OTP from external API service (placeholder implementation)
     * This would integrate with services like MailSlurp, Mailinator, etc.
     */
    private static String getOTPFromExternalAPI(String email) {
        logger.info("Attempting to retrieve OTP from external API for email: {}", email);
        
        // Placeholder implementation
        // In real scenario, this would:
        // 1. Connect to external email service API
        // 2. Fetch latest email for the provided address
        // 3. Extract OTP code from email content
        // 4. Return the extracted code
        
        try {
            // Simulate API call delay
            Thread.sleep(2000);
            
            // For demo purposes, return mock OTP
            String otp = config.getMockOtpValue();
            logger.info("Retrieved OTP from external API: {}", otp);
            return otp;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for external API response", e);
            return getMockOTP();
        } catch (Exception e) {
            logger.error("Failed to retrieve OTP from external API", e);
            return getMockOTP();
        }
    }

    /**
     * Get OTP via manual input (for interactive testing)
     */
    private static String getManualOTP() {
        logger.info("Manual OTP input mode - using mock OTP for automation");
        
        // In interactive mode, this could pause execution and wait for user input
        // For automated tests, we'll use mock OTP
        return getMockOTP();
    }

    /**
     * Validate OTP format
     */
    public static boolean isValidOTPFormat(String otp) {
        if (otp == null || otp.trim().isEmpty()) {
            return false;
        }
        
        // Common OTP formats: 4-8 digits
        return otp.matches("^\\d{4,8}$");
    }

    /**
     * Wait for OTP to be available (simulates email delivery delay)
     */
    public static void waitForOTPDelivery() {
        try {
            int waitTime = config.getIntProperty("otp.wait.seconds", 3);
            logger.info("Waiting {} seconds for OTP delivery", waitTime);
            Thread.sleep(waitTime * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupted while waiting for OTP delivery");
        }
    }

    /**
     * Mock email verification for testing purposes
     * This simulates clicking a verification link in email
     */
    public static boolean simulateEmailVerification(String email) {
        logger.info("Simulating email verification for: {}", email);
        
        try {
            // Simulate verification process delay
            Thread.sleep(1000);
            
            // In real scenario, this would:
            // 1. Generate verification token
            // 2. Make API call to verification endpoint
            // 3. Return success/failure status
            
            logger.info("Email verification simulated successfully for: {}", email);
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted during email verification simulation", e);
            return false;
        }
    }
}