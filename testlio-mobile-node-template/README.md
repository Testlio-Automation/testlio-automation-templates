# Automated Mobile App Testing using Javascript with Typescript, Appium and WebdriverIO

## Project structure
- `lib` - components of the Testlio common components (to be moved to the separate library)
- `src/pageobjects` - package with all Page Object Models to be implemented
- `test/specs` - package for modules describing test cases
- `config.ts` - app variables config
- `wdio.local.conf.ts` - WebdriverIO config for local test execution
- `wdio.testlio.conf.ts` - WebdriverIO config for test execution on Testlio platform (please do not change it)

## Run tests locally
1.  Set your Appium desired capabilities to `wdio.local.conf.ts`:
```js
{
  capabilities: [
    {
      platformName: '<put your value here>', // Android or iOS
      platformVersion: '<put your value here>', // Device OS version
      app: '<put your value here>', // Absolute local path to the app under test
      deviceName: '<put your value here>', // 
      automationName: '<put your value here>', // UiAutomator2, XCUITest or other
      autoGrantPermissions: true // For Android only
    }
  ]
}
```

2. Run the tests:
```bash 
npm run test-local
```

## Creating test package
```bash 
npm run package
```
