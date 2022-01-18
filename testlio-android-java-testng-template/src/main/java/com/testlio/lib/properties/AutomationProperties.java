package com.testlio.lib.properties;

import com.testlio.lib.env.BrowserTypes;
import com.testlio.lib.env.ExecutionTypes;
import com.testlio.lib.env.MobileTypes;
import com.testlio.lib.env.ProvidersTypes;
import com.testlio.lib.utility.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Slf4j
@Resource.Classpath("allure-env/environment.properties")
public class AutomationProperties {

    @Setter
    @Getter
    private String testMethod;

    @Setter
    @Getter
    private String testBuildName;

    @Getter
    private AtomicInteger testMethodOrderNumberInThread = new AtomicInteger(0);

    @Setter
    @Getter
    private int implicitWait;

    @Property("timeout.beetween.tests")
    @Getter
    private boolean timeoutBetweenTests = false;

    @Property("timeout.beetween.tests.value")
    @Getter
    private int timeoutBetweenTestsValue = 7;

    @Property("thread.counts")
    @Getter
    private int threadCounts = 1;

    @Property("parallel.mode")
    @Getter
    private String parallelMode = "tests";

    @Property("implicit.wait")
    @Getter
    private int defaultImplicitWait;

    @Property("maximize.screen")
    @Getter
    private boolean maximizedScreen = false;

    @Property("full.screen")
    @Getter
    private boolean fullScreen = false;

    @Property("take.screenshots.on.annotated.steps")
    @Getter
    private boolean takesScreenshotOnEachStep = false;

    @Property("execution.type")
    @Getter
    private String executionType = "web";

    @Property("appium.screenshots.dir")
    @Getter
    private String screenshotsDir = "";

    @Property("device.type")
    @Getter
    private String deviceType = "phone";

    @Property("provider")
    @Getter
    private String provider = "local";

    @Property("ios.pixel.ratio")
    private String iosPixelRatio;

    @Property("browser.name")
    @Getter
    private String browserName = "chrome";

    @Property("selenium.version")
    @Getter
    private String seleniumVersion;

    @Property("gecko.version")
    @Getter
    private String geckoVersion = "0.26.0";

    @Property("browser.version")
    @Getter
    private String browserVersion;

    @Property("browser.platform")
    @Getter
    private String browserPlatform;

    @Property("mobile.platform")
    @Getter
    private String mobileDevicePlatform = "android";

    @Property("mobile.platform.version")
    @Getter
    private String mobileDevicePlatformVersion = "";

    @Property("mobile.app.path")
    @Getter
    private String mobileAppPath = "";

    @Property("mobile.device.name")
    @Getter
    private String mobileDeviceName = "";

    @Property("mobile.device.udid")
    @Getter
    private String mobileDeviceUdid;

    @Property("mobile.package.id")
    @Getter
    private String mobilePackageId = "";

    @Property("mobile.app.activity")
    @Getter
    private String mobileActivity = "";

    @Property("mobile.app.grant.autopermissions")
    @Getter
    private boolean grantAutoPermissions = false;

    @Property("mobile.app.use.new.wda")
    @Getter
    private boolean useNewWDA = false;

    @Property("mobile.app.use.prebuilt.wda")
    @Getter
    private boolean usePrebuiltWDA = false;

    @Property("mobile.app.no.reset")
    @Getter
    private boolean noReset = false;

    @Property("mobile.app.full.reset")
    @Getter
    private boolean fullReset = true;

    @Property("appiumAutomationName")
    @Getter
    private String appiumAutomationName = "UiAutomator2";

    @Property("appium.server.hub.address")
    @Getter
    private String appiumServerHubAddress;

    @Property("xcode.org.id")
    @Getter
    private String xcodeOrgId;

    @Property("xcode.signing.id")
    @Getter
    private String xcodeSigningId;

    @Property("wda.bundle.id")
    @Getter
    private String wdaBundleId;

    @Property("app.bundle.id")
    @Getter
    private String appBundleId;

    @Property("web.driver.agent.url")
    @Getter
    private String webDriverAgentUrl;

    @Property("analytics.execution")
    @Getter
    private String analyticsExecution = "false";

    @Property("app.url")
    @Getter
    private String appUrl;

    @Property("session.url")
    @Getter
    private String sessionUrl;

    public AutomationProperties() {
        PropertyLoader.populate(this);
        log.info(format("Implicit wait set to: %d", defaultImplicitWait));
        this.implicitWait = defaultImplicitWait;
    }

    public void incrementTestMethodOrderNumberInThread() {
        this.testMethodOrderNumberInThread.incrementAndGet();
    }

    public void resetImplicitWait() {
        this.implicitWait = defaultImplicitWait;
    }

    public boolean isLocalExecutionOnWindows() {
        return isLocalExecution() && getLocalWebExecutionOsName().startsWith("win");
    }

    public boolean isLocalExecutionOnOSX() {
        return isLocalExecution() && getLocalWebExecutionOsName().startsWith("mac");
    }

    public boolean isLocalExecutionOnLinux() {
        return isLocalExecution() && getLocalWebExecutionOsName().startsWith("lin");
    }

    public boolean isMobileExecution() {
        return isMobileNativeExecution() || isMobileWebExecution();
    }

    public boolean isMobileNativeExecution() {
        return getExecutionType().equalsIgnoreCase(ExecutionTypes.MOBILE_NATIVE.getName());
    }

    public boolean isMobileWebExecution() {
        return getExecutionType().equalsIgnoreCase(ExecutionTypes.MOBILE_WEB.getName());
    }

    public boolean isWebExecution() {
        return getExecutionType().equalsIgnoreCase(ExecutionTypes.WEB.getName());
    }

    public boolean isLocalExecution() {
        return provider.equalsIgnoreCase(ProvidersTypes.LOCAL.getName());
    }

    public boolean isTestlioExecution() {
        return provider.equalsIgnoreCase(ProvidersTypes.TESTLIO.getName());
    }

    public boolean isIEExecution() {
        return getBrowserName().equalsIgnoreCase(BrowserTypes.IE.getName());
    }

    public boolean isNotIEExecution() {
        return !isIEExecution();
    }

    public String returnLocalOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public boolean isIOSExecution() {
        return isMobileExecution() && getMobileDevicePlatform().equalsIgnoreCase(MobileTypes.IOS.getName());
    }

    public boolean isAndroidExecution() {
        return isMobileExecution() && getMobileDevicePlatform().equalsIgnoreCase(MobileTypes.ANDROID.getName());
    }

    public int getIosPixelRatio() {
        if (isParsable(iosPixelRatio)) {
            return parseInt(iosPixelRatio);
        }
        return Constant.DEFAULT_IOS_PIXEL_RATIO;
    }

    public boolean isFirefoxExecution() {
        return getBrowserName().equalsIgnoreCase(BrowserTypes.FIREFOX.getName());
    }

    public boolean isEdgeExecution() {
        return getBrowserName().equalsIgnoreCase(BrowserTypes.EDGE.getName());
    }

    public boolean isNonChromiumExecution() {
        return isIEExecution() || isFirefoxExecution();
    }

    private String getLocalWebExecutionOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

}