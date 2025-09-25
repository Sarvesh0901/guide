# HelloBooks QA Automation Test Cases

## Test Case Documentation

| ID | Suite | Title | Prerequisites/Test Data | Steps | Expected Result | Priority | Type | Status |
|---|---|---|---|---|---|---|---|---|
| TC001 | Signup | Happy Path - New User Registration | Valid unique email, strong password | 1. Navigate to signup page<br>2. Enter valid email<br>3. Enter strong password<br>4. Confirm password<br>5. Fill required fields<br>6. Submit form<br>7. Complete email verification | Account created successfully, redirected to onboarding | P1 | Positive | |
| TC002 | Signup | Email Field - Required Validation | Empty email field | 1. Navigate to signup page<br>2. Leave email field empty<br>3. Enter password<br>4. Submit form | Error message "Email is required" displayed, form not submitted | P1 | Negative | |
| TC003 | Signup | Password Field - Required Validation | Empty password field | 1. Navigate to signup page<br>2. Enter valid email<br>3. Leave password field empty<br>4. Submit form | Error message "Password is required" displayed, form not submitted | P1 | Negative | |
| TC004 | Signup | Email Format - Invalid Format | Invalid email format | 1. Navigate to signup page<br>2. Enter invalid email (e.g., test@domain)<br>3. Enter valid password<br>4. Submit form | Error message "Please enter a valid email" displayed | P1 | Negative | |
| TC005 | Signup | Password Policy - Weak Password | Weak password (e.g., "123") | 1. Navigate to signup page<br>2. Enter valid email<br>3. Enter weak password<br>4. Submit form | Error message about password requirements displayed | P1 | Negative | |
| TC006 | Signup | Duplicate Email Handling | Existing registered email | 1. Navigate to signup page<br>2. Enter already registered email<br>3. Enter valid password<br>4. Submit form | Error message "Email already registered" displayed | P2 | Negative | |
| TC007 | Signup | Password Confirmation Mismatch | Mismatched passwords | 1. Navigate to signup page<br>2. Enter valid email<br>3. Enter password<br>4. Enter different confirmation password<br>5. Submit form | Error message "Passwords do not match" displayed | P2 | Negative | |
| TC008 | Signup | Email Verification Process | Valid signup data | 1. Complete signup process<br>2. Check email for verification link<br>3. Click verification link | Email verified, account activated | P1 | Positive | |
| TC009 | Signup | XSS in Name Field | XSS payload in name | 1. Navigate to signup page<br>2. Enter script tag in name field<br>3. Complete other fields<br>4. Submit form | Input sanitized, no script execution | P2 | Security | |
| TC010 | Signup | Long Input Boundary Test | 256+ character input | 1. Navigate to signup page<br>2. Enter very long text in name field<br>3. Complete other fields<br>4. Submit form | Graceful error or character limit enforced | P3 | Negative | |
| TC011 | Signup | Password Masking | Any password | 1. Navigate to signup page<br>2. Enter password in password field<br>3. Observe password display | Password characters are masked | P2 | Positive | |
| TC012 | Signup | Form Field Navigation | Valid data | 1. Navigate to signup page<br>2. Use Tab key to navigate through fields<br>3. Complete form using keyboard only | All fields accessible via keyboard navigation | P3 | Positive | |
| TC013 | Login | Happy Path - Valid Credentials | Valid registered user | 1. Navigate to login page<br>2. Enter valid email<br>3. Enter correct password<br>4. Click login | Successfully logged in, redirected to dashboard | P1 | Positive | |
| TC014 | Login | Invalid Password | Valid email, wrong password | 1. Navigate to login page<br>2. Enter valid email<br>3. Enter incorrect password<br>4. Click login | Error message "Invalid credentials" displayed, no session created | P1 | Negative | |
| TC015 | Login | Unregistered Email | Unregistered email | 1. Navigate to login page<br>2. Enter unregistered email<br>3. Enter any password<br>4. Click login | Error message "User not found" displayed, no session created | P1 | Negative | |
| TC016 | Login | Empty Email Field | Empty email | 1. Navigate to login page<br>2. Leave email field empty<br>3. Enter password<br>4. Click login | Error message "Email is required" displayed | P1 | Negative | |
| TC017 | Login | Empty Password Field | Empty password | 1. Navigate to login page<br>2. Enter valid email<br>3. Leave password field empty<br>4. Click login | Error message "Password is required" displayed | P1 | Negative | |
| TC018 | Login | Remember Me Functionality | Valid credentials with Remember Me | 1. Navigate to login page<br>2. Enter valid credentials<br>3. Check "Remember Me"<br>4. Login<br>5. Close browser<br>6. Reopen and navigate to site | User remains logged in without re-entering credentials | P2 | Positive | |
| TC019 | Login | Logout Functionality | Logged in user | 1. Login with valid credentials<br>2. Click logout button<br>3. Try to access protected page | Successfully logged out, redirected to login page | P1 | Positive | |
| TC020 | Login | Session Invalidation | Logged in user | 1. Login with valid credentials<br>2. Logout<br>3. Use browser back button | Cannot access protected content, session invalidated | P2 | Positive | |
| TC021 | Login | Rate Limiting/Account Lockout | Valid email, multiple failures | 1. Navigate to login page<br>2. Enter valid email<br>3. Enter wrong password 5+ times<br>4. Attempt to login again | Account temporarily locked or rate limited | P3 | Security | |
| TC022 | Login | Forgot Password Link | N/A | 1. Navigate to login page<br>2. Check for "Forgot Password" link<br>3. Click link | Forgot password page or modal opens | P3 | Positive | |
| TC023 | Onboarding | Organization Setup | Newly registered user | 1. Complete signup process<br>2. Login for first time<br>3. Fill organization details<br>4. Submit | Organization created, user proceeds to next step | P1 | Positive | |
| TC024 | Onboarding | Required Fields Validation | New user in onboarding | 1. Navigate to onboarding step<br>2. Leave required fields empty<br>3. Try to proceed | Error messages displayed for required fields | P2 | Negative | |
| TC025 | Onboarding | Skip Optional Steps | New user in onboarding | 1. Navigate through onboarding<br>2. Skip optional steps<br>3. Complete required steps only | Successfully complete onboarding with minimal data | P2 | Positive | |
| TC026 | Onboarding | Navigation Between Steps | New user in onboarding | 1. Start onboarding process<br>2. Use "Back" and "Next" buttons<br>3. Navigate between steps | Smooth navigation, data preserved between steps | P2 | Positive | |
| TC027 | General | Browser Compatibility - Chrome | N/A | Execute core test cases in Chrome browser | All functionality works as expected | P1 | Positive | |
| TC028 | General | Browser Compatibility - Firefox | N/A | Execute core test cases in Firefox browser | All functionality works as expected | P2 | Positive | |
| TC029 | General | Responsive Design - Mobile View | N/A | 1. Resize browser to mobile dimensions<br>2. Test login and signup flows | UI adapts properly to mobile view | P3 | Positive | |
| TC030 | Security | HTTPS Enforcement | N/A | 1. Navigate to HTTP version of site<br>2. Attempt to login | Redirected to HTTPS, secure connection enforced | P2 | Security | |

## Test Data Requirements

### Valid Test Users
- **Primary Test User**: qa.automation+primary@example.com / ValidPass123!
- **Secondary Test User**: qa.automation+secondary@example.com / ValidPass123!

### Invalid Test Data
- **Invalid Emails**: test@, @domain.com, invalid.email, test..test@domain.com
- **Weak Passwords**: 123, password, weak
- **XSS Payloads**: `<script>alert('XSS')</script>`
- **Long Text**: 300+ character strings

### OTP Handling Strategy
- **Mode**: Mock OTP for automated tests
- **Value**: 123456 (configurable)
- **Fallback**: Manual input option for exploratory testing

## Priority Definitions
- **P1 (High)**: Core functionality, must work for basic user flows
- **P2 (Medium)**: Important features, significant impact on user experience  
- **P3 (Low)**: Nice-to-have features, minor impact

## Test Types
- **Positive**: Valid input, expected happy path behavior
- **Negative**: Invalid input, error handling verification
- **Security**: Security-related validations and protections

## Automation Priority
**High Priority for Automation (P1)**: TC001, TC002, TC003, TC004, TC005, TC008, TC013, TC014, TC015, TC016, TC017, TC019, TC023
**Medium Priority for Automation (P2)**: TC006, TC007, TC009, TC018, TC020, TC024, TC025, TC026
**Manual/Exploratory Testing**: TC021, TC022, TC027, TC028, TC029, TC030