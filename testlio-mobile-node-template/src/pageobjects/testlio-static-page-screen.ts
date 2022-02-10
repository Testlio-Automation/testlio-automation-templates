import {WebdriverIO} from "@wdio/types/build/Options";
import {ChainablePromiseElement} from "webdriverio";
import MobileScreen from "../../lib/mobile-screen";
import TestlioLoginScreen from "./testlio-login-screen";

export default class TestlioStaticPageScreen extends MobileScreen {

    public get title(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='static-page-title']");
    }

    public get loginTab(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='login-tab']");
    }

    public isPageLoaded(): any {
        return this.title.isDisplayed;
    }

    public async goToLoginScreenTab(): Promise<MobileScreen> {
        await this.loginTab.click();
        return this.waitForPageToLoad(new TestlioLoginScreen(this.driver));
    }
}
