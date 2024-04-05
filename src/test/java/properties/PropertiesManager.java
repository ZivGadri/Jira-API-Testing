package properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManager {

    private static final Logger logger = LogManager.getLogger(PropertiesManager.class);
    protected static Properties prop;

    public PropertiesManager(InputStream inputStream) throws IOException {
        if (!Objects.isNull(inputStream)) {
            prop = readProperties(inputStream);
        } else {
            logger.error("Please pass a java.io File argument");
            throw new NullPointerException("file object is null");
        }
    }

    private Properties readProperties(InputStream in) throws IOException {
        Properties properties = new Properties();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        properties.load(reader);
        return properties;
    }

    public String getJiraUserName() {
        return prop.getProperty("JIRA_USER_NAME");
    }
    public String getJiraPassword() {
        return prop.getProperty("JIRA_PASSWORD");
    }
    public String getTestRailUserName() {
        return prop.getProperty("TESTRAIL_USERNAME");
    }
    public String getTestRailKey() {
        return prop.getProperty("TESTRAIL_KEY");
    }
    public String getTestRailHost() {
        return prop.getProperty("TESTRAIL_HOST");
    }
    public String getTestRailProjectName() {
        return prop.getProperty("TEST_RAIL_PROJECT_NAME");
    }
    public String getSlackChannel() { return prop.getProperty("SLACK_CHANNEL"); }
    public String reportToTestRail() { return prop.getProperty("REPORT_TO_TESTRAIL"); }
    public String reportToSlack() { return prop.getProperty("REPORT_TO_SLACK"); }
    public String getSauceLabsUser() { return prop.getProperty("SAUCELABS_USER"); }
    public String getSauceLabsKey() { return prop.getProperty("SAUCELABS_KEY"); }
    public String getIsBrowserLocal() { return prop.getProperty("IS_BROWSER_LOCAL"); }
    public String getBrowserType() { return prop.getProperty("BROWSER_TYPE"); }
    public String getOsPlatform() { return prop.getProperty("OS_PLATFORM"); }
}