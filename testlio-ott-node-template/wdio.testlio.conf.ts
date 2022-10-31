const tsConfig = require("./tsconfig.json");

export const config: WebdriverIO.Config = {
    runner: 'local',

    autoCompileOpts: {
        autoCompile: true,
        tsNodeOpts: {
            transpileOnly: true,
            project: 'tsconfig.json'
        },
        tsConfigPathsOpts: {
            baseUrl: './',
            paths: tsConfig.compilerOptions.paths
        }
    },

    user: 'BROWSERSTACK_USER',
    key:  'BROWSERSTACK_ACCESS_KEY',
    path: '/wd/hub',

    specs: [
        './test/specs/*.ts'
    ],

    maxInstances: 10,

    capabilities: [
        {
            build: 'BrowserStack Build',
            project: 'Example Smart TV Project',
            name: 'single_test',
            'browserstack.debug': true,
            platformName: 'android',
            platformVersion: '7.1',
            app: 'bs://278fdbfbd6bd9bb3c8621eba8c089def35a8274a',
            deviceName: 'Amazon Fire TV Stick 4K'
        }
    ],

    logLevel: 'info',

    bail: 0,

    waitforTimeout: 10000,

    connectionRetryTimeout: 120000,

    connectionRetryCount: 10,

    framework: 'mocha',

    reporters: [['allure', {
        disableWebdriverStepsReporting: true,
        outputDir: process.env.ALLURE_RESULTS_DIR
    }]],

    mochaOpts: {
        ui: 'bdd',
        timeout: 240000
    },
}
