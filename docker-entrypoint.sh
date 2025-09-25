#!/bin/bash
set -e

echo "========================================"
echo "HelloBooks QA Automation - Docker Mode"
echo "========================================"

# Function to show usage
show_usage() {
    echo "Usage: docker run [options] hellobooks-qa [command]"
    echo ""
    echo "Commands:"
    echo "  test                 - Run all tests (default)"
    echo "  login               - Run login tests only"
    echo "  signup              - Run signup tests only"
    echo "  onboarding          - Run onboarding tests only"
    echo "  single              - Run single test (password masking)"
    echo "  compile             - Compile project only"
    echo "  help                - Show this help"
    echo ""
    echo "Environment Variables:"
    echo "  BASE_URL            - Override base URL"
    echo "  BROWSER             - Browser to use (chrome, firefox, edge)"
    echo "  HEADLESS            - Run in headless mode (true/false)"
    echo "  TEST_SUITE          - Specific test suite to run"
    echo ""
    echo "Examples:"
    echo "  docker run hellobooks-qa"
    echo "  docker run hellobooks-qa login"
    echo "  docker run -e BASE_URL=https://staging.hellobooks.ai/ hellobooks-qa"
    echo "  docker run -e HEADLESS=false hellobooks-qa"
}

# Set default values
BASE_URL=${BASE_URL:-"https://dev.hellobooks.ai/"}
BROWSER=${BROWSER:-"chrome"}
HEADLESS=${HEADLESS:-"true"}

# Build Maven command based on arguments
MAVEN_CMD="mvn"
TEST_ARGS=""

# Parse command line arguments
case "${1:-test}" in
    "test"|"")
        TEST_ARGS="test"
        echo "Running all tests..."
        ;;
    "login")
        TEST_ARGS="test -Dtest=LoginTests"
        echo "Running login tests only..."
        ;;
    "signup")
        TEST_ARGS="test -Dtest=SignupTests"
        echo "Running signup tests only..."
        ;;
    "onboarding")
        TEST_ARGS="test -Dtest=OnboardingTests"
        echo "Running onboarding tests only..."
        ;;
    "single")
        TEST_ARGS="test -Dtest=LoginTests#testPasswordMasking"
        echo "Running single test (password masking)..."
        ;;
    "compile")
        TEST_ARGS="compile"
        echo "Compiling project only..."
        ;;
    "help"|"-h"|"--help")
        show_usage
        exit 0
        ;;
    *)
        echo "Unknown command: $1"
        show_usage
        exit 1
        ;;
esac

# Add system properties
SYSTEM_PROPS="-Dbase.url=${BASE_URL} -Dbrowser=${BROWSER} -Dheadless=${HEADLESS}"

# Add custom test suite if specified
if [ -n "$TEST_SUITE" ]; then
    SYSTEM_PROPS="$SYSTEM_PROPS -Dtest=$TEST_SUITE"
fi

# Construct full command
FULL_CMD="$MAVEN_CMD $TEST_ARGS $SYSTEM_PROPS"

echo "Configuration:"
echo "  Base URL: $BASE_URL"
echo "  Browser: $BROWSER"
echo "  Headless: $HEADLESS"
echo "  Command: $FULL_CMD"
echo ""

# Ensure target directories exist
mkdir -p target/reports target/screenshots target/logs

echo "Starting test execution..."
echo "========================================"

# Execute the Maven command
exec $FULL_CMD