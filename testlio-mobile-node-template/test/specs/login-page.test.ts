import { describe, it, before, beforeEach, afterEach } from 'mocha';
import { expect } from 'chai';
import TestlioStaticPageScreen from '../../src/pageobjects/testlio-static-page-screen';
import { config } from '../../config';
import TestlioLoginScreen from "../../src/pageobjects/testlio-login-screen";
import allureReporter from '@wdio/allure-reporter'

const SUCCESSFUL_LOGIN_ALERT_TITLE = 'Successful login';

describe('When on login screen', function() {
    let loginScreen: TestlioLoginScreen;

    before(async function() {
        const staticPageScreen = new TestlioStaticPageScreen(driver);
        loginScreen = await staticPageScreen.goToLoginScreenTab();
    });

    beforeEach((async function() {
        driver.startRecordingScreen();
    }))

    afterEach((async function() {
        const base64Video = await driver.stopRecordingScreen();

        if (base64Video) {
            const buffer = Buffer.from(base64Video, 'base64');
            allureReporter.addAttachment(this.currentTest.title, buffer, 'video/mp4');
        }
    }))

    it('Checks that can login with correct credentials', async function() {
        allureReporter.startStep('Input Credentials');
        await loginScreen.inputUsername(config.username);
        await loginScreen.inputPassword(config.password);
        allureReporter.endStep();

        const alertPopup = await loginScreen.clickLogin();
        const alertTitle = await alertPopup.getTitleString();
        expect(alertTitle).to.equal(SUCCESSFUL_LOGIN_ALERT_TITLE);
    });
}).timeout(10000);
