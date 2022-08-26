import { ChainablePromiseElement } from "webdriverio";
import MobileScreen from "../../lib/mobile-screen";
import TestlioLoginScreen from "./testlio-login-screen";
import allureReporter from "@wdio/allure-reporter";

export default class TestlioStaticPageScreen extends MobileScreen {

    public get title(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='static-page-title']");
    }

    public get loginTab(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("//*[@resource-id='login-tab']");
    }

    public isPageLoaded(): () => Promise<boolean> {
        return this.title.isDisplayed;
    }

    public async goToLoginScreenTab(): Promise<TestlioLoginScreen> {
        allureReporter.startStep('Navigate to the Login Screen tab');
        await this.loginTab.click();
        const page = <TestlioLoginScreen> await this.waitForPageToLoad(new TestlioLoginScreen(this.driver));
        allureReporter.endStep();
        return page;
    }
}
