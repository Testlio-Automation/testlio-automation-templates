import {WebdriverIO} from "@wdio/types/build/Options";
import {ChainablePromiseElement} from "webdriverio";
import AlertPopup from "./alert-popup";
import MobileScreen from "../../lib/mobile-screen";
import allureReporter from "@wdio/allure-reporter";

export default class TestlioLoginScreen extends MobileScreen {

    public get title(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='login-title']");
    }

    public get usernameInput(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//android.widget.EditText[contains(@text, 'Username')]");
    }

    public get passwordInput(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//android.widget.EditText[contains(@text, 'Password')]");
    }

    public get loginButton(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='login-button']");
    }

    public isPageLoaded(): any {
        return this.title.isDisplayed;
    }

    public async inputUsername(username: string) {
        allureReporter.startStep('Input username');
        await this.usernameInput.setValue(username);
        allureReporter.endStep();
    }

    public async inputPassword(password: string) {
        allureReporter.startStep('Input password');
        await this.passwordInput.setValue(password);
        allureReporter.endStep();
    }

    public async clickLogin(): Promise<AlertPopup> {
        allureReporter.startStep('Click Login button');
        await this.loginButton.click();
        const page = <AlertPopup> await this.waitForPageToLoad(new AlertPopup(this.driver));
        allureReporter.endStep();
        return page;
    }
}
