package com.hellobooks.automation.utils;

import com.github.javafaker.Faker;
import com.hellobooks.automation.config.ConfigManager;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating test data
 */
public class TestDataGenerator {
    private static final Faker faker = new Faker();
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Generate a unique email address for testing
     * @return unique email address
     */
    public static String generateUniqueEmail() {
        String prefix = config.getTestEmailPrefix();
        String domain = config.getTestEmailDomain();
        
        if (config.useTimestampInEmail()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            return prefix + "+" + timestamp + domain;
        } else {
            String randomString = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
            return prefix + "+" + randomString + domain;
        }
    }

    /**
     * Generate a valid password that meets common password policies
     * @return valid password
     */
    public static String generateValidPassword() {
        // Generate password with: 8+ chars, uppercase, lowercase, number, special char
        String upper = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        String lower = RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String digits = RandomStringUtils.randomNumeric(2);
        String special = RandomStringUtils.random(1, "!@#$%^&*");
        
        return upper + lower + digits + special;
    }

    /**
     * Generate a weak password for negative testing
     * @return weak password
     */
    public static String generateWeakPassword() {
        return "weak123";
    }

    /**
     * Generate invalid email formats for negative testing
     * @return invalid email
     */
    public static String generateInvalidEmail() {
        String[] invalidEmails = {
            "invalid.email",
            "test@",
            "@domain.com",
            "test..test@domain.com",
            "test@domain",
            "test space@domain.com"
        };
        return invalidEmails[faker.random().nextInt(invalidEmails.length)];
    }

    /**
     * Generate a first name
     * @return first name
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generate a last name
     * @return last name
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Generate a company name
     * @return company name
     */
    public static String generateCompanyName() {
        return faker.company().name();
    }

    /**
     * Generate a phone number
     * @return phone number
     */
    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    /**
     * Generate random text of specified length
     * @param length desired length
     * @return random text
     */
    public static String generateRandomText(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Generate XSS payload for security testing
     * @return XSS payload
     */
    public static String generateXSSPayload() {
        return "<script>alert('XSS')</script>";
    }

    /**
     * Generate SQL injection payload for security testing
     * @return SQL injection payload
     */
    public static String generateSQLInjectionPayload() {
        return "'; DROP TABLE users; --";
    }

    /**
     * Generate very long text for boundary testing
     * @return very long text (256+ characters)
     */
    public static String generateVeryLongText() {
        return RandomStringUtils.randomAlphanumeric(300);
    }

    /**
     * Get mock OTP value from configuration
     * @return mock OTP
     */
    public static String getMockOTP() {
        return config.getMockOtpValue();
    }
}