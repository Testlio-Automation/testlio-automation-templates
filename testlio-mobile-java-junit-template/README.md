# Automated Mobile App Testing using Java with Selenium, Appium and TestNG

## Project structure
- `src/main`
  - `java/com/testlio/pages` - package with all Page Object Models to be implemented
  - `resources` - related project configuration files (i.e. Allure properties)
- `src/test`
  - `java/com/testlio`
    - `constants` - package for constants related to test classes
    - `tests` - package for classes describing test cases
  - `resources` - configuration files related to tests:
    - `credentials.properties` - app credentials config
    - `assets` - hardcoded test assets to be used in fake tests (images, videos, etc)
    - `suites/*.testng.xml` -  tests configurations (TestNG suites) to be executed when running them locally or on Testlio platform. Starts with name of the suite (for instance, `basic`, `stress` or `login`)

## Install Testlio framework
To create a test script compatible with Testlio platform you have to use our framework, containing all the supported libraries by our engine. You can find a JAR library with the Testlio framework in the parent directory.

In order to use it, you have to install it to the local Maven repository:
```shell
mvn install:install-file \
   -Dfile=../java-automation-framework-2.0-jar-with-dependencies.jar \
   -DgroupId=com.testlio \
   -DartifactId=java-automation-framework \
   -Dversion=2.0 \
   -Dpackaging=jar \
   -DgeneratePom=true
```

## Run Tests
Create a `credentials.properties` file in the directory `src/test/resources` with the values from `Android Testlio Mobile Login Sample App` in 1pass as follows:
 ```bash
 user.username=<username>
 user.password=<password>
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
./package.sh
```
By default, the command above will create a package with `basic` suite. To change the suite you need to pass parameter specifying the suite name. For example:
```bash 
./package.sh stress
```