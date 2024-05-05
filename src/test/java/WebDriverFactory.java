import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import reporting.DemoProject_Jira;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    protected static final String SAUCELABS_WEBDRIVER_URL = "https://%s:%s@ondemand.saucelabs.com:443/wd/hub";
    private final String SAUCELABS_USER;
    private final String SAUCELABS_KEY;
    private final String BROWSER_TYPE;
    private final String OS_PLATFORM;
    private String sessionId;
    private final boolean isBrowserLocal;
    private RemoteWebDriver webDriver;
    private String testName;

    public WebDriverFactory(boolean isBrowserLocal, String saucelabsUser, String saucelabsKey, String browserType, String osPlatform) {
        this.isBrowserLocal = isBrowserLocal;
        this.SAUCELABS_USER = saucelabsUser;
        this.SAUCELABS_KEY = saucelabsKey;
        this.BROWSER_TYPE = browserType;
        this.OS_PLATFORM = osPlatform;
    }

    public RemoteWebDriver initWebDriver(Method method) {
        try {
            webDriver = createWebDriver(method);
            logger.info("Web driver created successfully. Session ID: " + sessionId);
            return webDriver;
        } catch (Exception e) {
            logger.info("Web driver initialization process failed.");
            throw new WebDriverException();
        }
    }

    public RemoteWebDriver createWebDriver(Method method) {
        try {
            testName = method.getDeclaredAnnotation(DemoProject_Jira.class).testName();
        } catch (NullPointerException npe) {
            logger.error("No 'DemoProject_Jira.class' annotation was found. setting testName as method name");
            testName = method.getName();
        }
        try {
            if (!isBrowserLocal) {
                return createRemoteDriver();
            } else {
                return createLocalDriver();
            }
        } catch (Exception e)
        {
           logger.error("Failed to create Web driver.\nMore info: " + e.getMessage());
           throw new RuntimeException("Failed to create Web driver.\nMore info: " + e.getMessage());
        }
    }

    /**
     * Creates the remote web driver to connect to Sauce Labs.
     * See <a href="https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/">...</a>
     * for supported OS/browser combinations.
     *
     */
    private RemoteWebDriver createRemoteDriver() throws MalformedURLException {
        logger.info("Starting the creation of remote web driver for test: {}", testName);
        URL remoteURL = new URL (String.format(SAUCELABS_WEBDRIVER_URL, SAUCELABS_USER, SAUCELABS_KEY));
        MutableCapabilities caps;
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", SAUCELABS_USER);
        sauceOptions.setCapability("accessKey", SAUCELABS_KEY);
        sauceOptions.setCapability("idleTimeout", "90");
        sauceOptions.setCapability("extendedDebugging", true);
        sauceOptions.setCapability("name", testName);
        sauceOptions.setCapability("seleniumVersion", "3.141.59");
        caps = getBrowserCapabilities(BROWSER_TYPE);
        caps.setCapability(CapabilityType.PLATFORM_NAME, OS_PLATFORM);
        caps.setCapability(CapabilityType.BROWSER_VERSION, "latest");
        caps.setCapability("sauce:options", sauceOptions);

        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, false);

        logger.info(caps.toJson());

        RemoteWebDriver webDriver = new RemoteWebDriver(remoteURL, caps);
        webDriver.setFileDetector(new LocalFileDetector());
        sessionId = webDriver.getSessionId().toString();

        return webDriver;
    }

    /**
     * Create a local Selenium instance.
     */
    private RemoteWebDriver createLocalDriver() {
        logger.info("Starting the creation of local web driver for test: {}", testName);
        RemoteWebDriver webDriver;
        if ("chrome".equalsIgnoreCase(BROWSER_TYPE)) {
            // Install driver
            WebDriverManager.chromedriver().setup();
            webDriver =
                    new ChromeDriver(new ChromeOptions().merge(getBrowserCapabilities(BROWSER_TYPE)));
            sessionId = webDriver.getSessionId().toString();
        } else if ("firefox".equalsIgnoreCase(BROWSER_TYPE)) {
            // Install driver
            WebDriverManager.firefoxdriver().setup();
            webDriver =
                    new FirefoxDriver(new FirefoxOptions().merge(getBrowserCapabilities(BROWSER_TYPE)));
            sessionId = webDriver.getSessionId().toString();
        } else if ("safari".equalsIgnoreCase(BROWSER_TYPE)) {
            // Merging causes duplicates.
            webDriver = new SafariDriver(new SafariOptions());
            sessionId = webDriver.getSessionId().toString();
        } else if ("edge".equalsIgnoreCase(BROWSER_TYPE)) {
            // Install driver
            WebDriverManager.edgedriver().setup();
            webDriver =
                    new EdgeDriver(new EdgeOptions().merge(getBrowserCapabilities(BROWSER_TYPE)));
            sessionId = webDriver.getSessionId().toString();
        } else {
            throw new RuntimeException("Invalid browser: " + BROWSER_TYPE);
        }
        // Width of 2000 is chosen to ensure all elements are visible
        webDriver.manage().window().setSize(new Dimension(2000, 768));

        return webDriver;
    }

    private static MutableCapabilities getBrowserCapabilities(String browser) {
        Map<String, Object> prefs = new HashMap();
        switch (browser.toLowerCase()) {
            case "chrome":
                prefs.put("profile.default_content_setting_values.notifications", 2);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", prefs);
                options.setExperimentalOption("w3c", true);
                return options;
            case "firefox":
                FirefoxOptions ffo = new FirefoxOptions();
                ffo.addPreference("dom.webnotifications.enabled", false);
                return ffo;
            case "safari":
                return new SafariOptions();
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("ms:inPrivate", true);
                return edgeOptions;
            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }
}