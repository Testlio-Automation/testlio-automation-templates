# Automated Android App Testing using Java with Selenium, Appium and TestNG

## Project structure
- `src/main`
  - `java/com/testlio/pages` - package with all Page Object Models to be implemented
  - `resources` - related project configuration files (i.e. Allure properties)
- `src/test`
  - `java/com/testlio/constants` - package for constants related to test classes
  - `java/com/testlio/tests` - package for classes describing test cases
  - `resources` - configuration files related to tests:
    - `credentials.properties` - app credentials config
    - `testng.xml` -  tests configuration to be executed when running them locally or on Testlio platform
  - `testlio-sample-mobile-app.apk` - Sample Android application to be used with the test examples
  - `testlio-sample-mobile-app.ipa` - Sample native iOS application to be used with the test examples (to be installed on the real device)
  - `testlio-sample-mobile-app.app` - Sample x86-compatible iOS application to be used with the test examples (to be installed on the simulator)

## Install Testlio framework
To create a test script compatible with Testlio platform you have to use our framework, containing all the supported libraries by our engine. You can find a JAR library with the Testlio framework in the parent directory.

In order to use it, you have to install it to the local Maven repository:
```shell
mvn install:install-file \
   -Dfile=../java-automation-framework-1.0-jar-with-dependencies.jar \
   -DgroupId=com.testlio \
   -DartifactId=java-automation-framework \
   -Dversion=1.0 \
   -Dpackaging=jar \
   -DgeneratePom=true
```

## Run tests locally
### Android
```bash 
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=mobile-native \
  -Dmobile.platform=android \
  -Dmobile.device.name=<your device name> \
  -Dmobile.platform.version=<your device os version> \
  -Dmobile.app.path=<absolute local path to apk>
```

### iOS
```shell
mvn clean test \
  -Dprovider=local \
  -Dexecution.type=mobile-native \
  -Dmobile.platform=ios \
  -Dmobile.app.path=<absolute local path to ipa> \
  -Dmobile.device.udid=<your device identifier> \
  -Dmobile.device.name=<your device model name> \
  -Dmobile.platform.version=<your device os version>
```

## Creating test package
```bash 
mvn package -DskipTests
```