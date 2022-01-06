# Automated Android App Testing using Java with Selenium, Appium and TestNG

## Project structure
- `src/main`
  - `java/com/testlio/lib` - Testlio framework (will be moved to external JAR)
  - `java/com/testlio/pages` - package with all Page Object Models to be implemented
  - `resources` - related project configuration files (Logback, Allure, AspectJ)
- `src/test`
  - `java/com/testlio/constants` - package for constants related to test classes
  - `java/com/testlio/tests` - package for classes describing test cases
  - `resources` - configuration files related to tests:
    - `credentials.properties` - app credentials config
    - `testng.xml` -  tests configuration to be executed when running them locally or on Testlio platform

## Run tests locally

```bash 
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=mobile-native \
  -Dmobile.platform=android \
  -Dmobile.device.name=<your device name> \
  -Dmobile.platform.version=<your device os version> \
  -Dmobile.app.path=<local path to apk>
```

## Creating test package
```bash 
mvn package -DskipTests