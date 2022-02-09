import MobileScreen from "../../lib/mobile-screen";
import {WebdriverIO} from "@wdio/types/build/Options";
import {ChainablePromiseElement} from "webdriverio";

export default class AlertPopup extends MobileScreen {

    public get title(): ChainablePromiseElement<Promise<WebdriverIO.Element>> {
        return this.driver.$("id=android:id/alertTitle");
    }

    public isPageLoaded(): any {
        return this.title.isDisplayed;
    }

    public async getTitleString(): Promise<string> {
        return this.title.getText()
    }
}
