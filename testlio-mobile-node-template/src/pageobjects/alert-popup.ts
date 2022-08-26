import MobileScreen from "../../lib/mobile-screen";
import { ChainablePromiseElement } from "webdriverio";

export default class AlertPopup extends MobileScreen {

    public get title(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("id=android:id/alertTitle");
    }

    public isPageLoaded(): () => Promise<boolean> {
        return this.title.isDisplayed;
    }

    public async getTitleString(): Promise<string> {
        return this.title.getText()
    }
}
