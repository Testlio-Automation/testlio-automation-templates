import {WebdriverIO} from "@wdio/types/build/Options";
import {ChainablePromiseElement} from "webdriverio";
import AlertPopup from "./alert-popup";
import MobileScreen from "../../lib/mobile-screen";

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
        await this.usernameInput.setValue(username);
    }

    public async inputPassword(password: string) {
        await this.passwordInput.setValue(password);
    }

    public async clickLogin(): Promise<AlertPopup> {
        await this.loginButton.click();
        return <AlertPopup> await this.waitForPageToLoad(new AlertPopup(this.driver));
    }
}
