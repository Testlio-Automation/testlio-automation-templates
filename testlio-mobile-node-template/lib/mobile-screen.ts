import { WebdriverIO } from "@wdio/types/build/Options";

export default abstract class MobileScreen {

    public driver: WebdriverIO.Browser;

    constructor(driver: WebdriverIO.Browser) {
        this.driver = driver;
    }

    abstract isPageLoaded(): any;

    public async waitForPageToLoad(screen: MobileScreen): Promise<MobileScreen> {
        await this.driver.waitUntil(
            screen.isPageLoaded(),
            {
                timeout: 5000,
                timeoutMsg: 'Page load timeout exceeded'
            }
        )
        return screen;
    }
}
