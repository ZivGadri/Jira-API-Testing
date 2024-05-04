import apiManager.ApiHelper;
import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import apiManager.models.enums.IssueTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import properties.PropertiesManager;
import reporting.TestListeners_Helper;
import uiManager.UI_Manager;

import java.lang.reflect.Method;
import java.util.Objects;

public class BaseTests {
    private static final Logger logger = LogManager.getLogger(BaseTests.class);
    public static final String JIRA_BASE_URL = "http://localhost:8080";
    public final String JIRA_PROJECT_NAME = "Demo_Project";
    protected String JIRA_USER_NAME;
    protected String JIRA_PASSWORD;
    protected ApiHelper apiHelper;
    protected Project testProject;
    protected Issue testIssue;
    protected String issueSummary;
    protected Comment testComment;
    protected String testCommentText = "This is a test comment";
    protected String updatedTestCommentText = "This is an updated test comment";
    protected SoftAssert softAssert;
    protected PropertiesManager props;
    protected WebDriver webDriver;
    protected WebDriverFactory webDriverFactory;
    protected UI_Manager uiManager;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        logger.info("Starting before class initializations...");
        props = TestListeners_Helper.props;
        setJiraCredentials();
        apiHelper = new ApiHelper(JIRA_USER_NAME, JIRA_PASSWORD, JIRA_BASE_URL);
        webDriverFactory = new WebDriverFactory(
                Boolean.parseBoolean(getEnvVarWithDefault("IS_BROWSER_LOCAL", props.getIsBrowserLocal())),
                getEnvVarWithDefault("SAUCELABS_USER", props.getSauceLabsUser()),
                getEnvVarWithDefault("SAUCELABS_KEY", props.getSauceLabsKey()),
                getEnvVarWithDefault("BROWSER_TYPE", props.getBrowserType()),
                getEnvVarWithDefault("OS_PLATFORM", props.getOsPlatform())
                );
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        softAssert = new SoftAssert();
        webDriver = webDriverFactory.initWebDriver(method);
        uiManager = new UI_Manager(webDriver, JIRA_USER_NAME, JIRA_PASSWORD, JIRA_BASE_URL);
        uiManager.navigateToJiraServerMainPage();
    }

    @AfterMethod
    public void afterTest() {
        softAssert.assertAll();
        if (!Objects.isNull(webDriver)) {
            webDriver.quit();
        }
    }

    /**
     * This method will look for an environment variable by the varName parameter.
     * It will return the def value in case there will be no such env var - varName.
     *
     * @param varName   The key value for the environment variable
     * @param def       The default value to return if no env variable is found
     *
     * @return String value
     **/
    public String getEnvVarWithDefault(String varName, String def) {
        String returnEnvValue = System.getenv(varName);
        String returnPropValue = System.getProperty(varName);
        if ((Objects.isNull(returnEnvValue) || returnEnvValue.isEmpty())
                && (Objects.isNull(returnPropValue) || returnPropValue.isEmpty()))  {
            return def;
        } else if (Objects.isNull(returnEnvValue) || returnEnvValue.isEmpty()) {
            return returnPropValue;
        } else return returnEnvValue;
    }

    protected void setJiraCredentials() {
        JIRA_USER_NAME = getEnvVarWithDefault("JIRA_USER_NAME", props.getJiraUserName());
        JIRA_PASSWORD = getEnvVarWithDefault("JIRA_PASSWORD", props.getJiraPassword());
    }

    public static void threadSleepLog(long sec, String extraDetails) {
        logger.info("Thread is sleeping for {} second(s) {}", sec, extraDetails);
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public Project buildProjectObject() {
        return new Project.ProjectBuilder("ZIV", JIRA_PROJECT_NAME).
                setProjectTypeKey("software").
                setProjectTemplateKey("com.pyxis.greenhopper.jira:gh-scrum-template").
                setDescription("This is a project for demo testing").
                setLead(JIRA_USER_NAME).
                setAssigneeType("PROJECT_LEAD").
                setAvatarId(10200).
                build();
    }

    public static Issue buildIssueObject(Project project) {
        return new Issue(project, IssueTypes.BUG.getIssueType());
    }

    public static Comment createCommentObject(String comment) {
        return new Comment(comment);
    }

    public int getNumberOfCommentsForIssue(Issue issue) {
        return apiHelper.getNumOfCommentsForIssue(issue);
    }
}