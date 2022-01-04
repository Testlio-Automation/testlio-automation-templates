# Automated Web Testing using Java with Selenium, Appium and TestNG

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

### Desktop
```bash 
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=web \
  -Dbrowser.name=<browser name value>
```

Possible `-Dbrowser.name` values:
 - `chrome`
 - `firefox`
 - `MicrosoftEdge`
 - `"internet explorer"`

### Android (Chrome)
```bash 
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=mobile-web \
  -Dmobile.platform=android \
  -Dmobile.device.name=<your device name> \
  -Dmobile.platform.version=<your device os version>
```

### iOS (Safari)
```bash 
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=mobile-web \
  -Dmobile.platform=ios \
  -Dmobile.device.udid=<your device identifier> \
  -Dmobile.device.name=<your device model name> \
  -Dmobile.platform.version=<your device os version>
```

## Creating test package
```bash 
mvn package -DskipTests