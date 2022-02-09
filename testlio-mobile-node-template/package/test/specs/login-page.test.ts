import { describe, it, before, after } from 'mocha';
import { expect } from 'chai';
import TestlioStaticPageScreen from '../../src/pageobjects/testlio-static-page-screen';
import { config } from '../../config';
import TestlioLoginScreen from "../../src/pageobjects/testlio-login-screen";
import AlertPopup from "../../src/pageobjects/alert-popup";

const SUCCESSFUL_LOGIN_ALERT_TITLE = 'Successful login';

describe('When on login screen', () => {
    let loginScreen: TestlioLoginScreen;

    before(async() => {
        const staticPageScreen: TestlioStaticPageScreen = new TestlioStaticPageScreen(driver);
        loginScreen = <TestlioLoginScreen> await staticPageScreen.goToLoginScreenTab();
    });

    it('Checks that can login with correct credentials', async() => {
        await loginScreen.inputUsername(config.username);
        await loginScreen.inputPassword(config.password);
        const alertPopup: AlertPopup = await loginScreen.clickLogin();
        const alertTitle = await alertPopup.getTitleString();
        expect(alertTitle).to.equal(SUCCESSFUL_LOGIN_ALERT_TITLE);
    });
}).timeout(10000);
