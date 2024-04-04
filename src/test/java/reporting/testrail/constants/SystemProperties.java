package reporting.testrail.constants;

import org.testng.util.Strings;

public class SystemProperties {

    public static final String HTTPS = propSetter("automation.tools.testrail.https", "https://");
    public static final String testRailEnabled = propSetter("automation.tools.testrail.enabled", "false");
    public static final String testRailHostUrl = propSetter("automation.tools.testrail.url", "/index.php?");
    public static final String testRailHostRoot = propSetter("automation.tools.testrail.url.root", "/api/v2/");
    public static String testRailHost = propSetter("automation.tools.testrail.host", "");
    public static String testRailUsername = propSetter("automation.tools.testrail.username", "");
    public static String testRailPassword = propSetter("automation.tools.testrail.password", "");
    public static String testRailProjectName = propSetter("automation.tools.testrail.project.name", "");
    public static final String testRailTestPlan = propSetter("automation.tools.testrail.plan", "");

    public static final String TEST_RAIL_CUSTOM_FIELD_NAME = "custom_suiteslist";
    public static final String TEST_RAIL_AUTOMATION_STATUS = "custom_automation_status";
    public static final String testRailCustomSuitesName = propSetter("automation.tools.testrail.field.name", TEST_RAIL_CUSTOM_FIELD_NAME);
    public static final String testRailCustomSuiteNames = propSetter("suiteNames", "");

    public static String propSetter(String k, String v) {
        String item = System.getProperty(k);
        if (!Strings.isNullOrEmpty(item)) {
            return item;
        }
        System.setProperty(k, v);
        return System.getProperty(k);
    }
}
