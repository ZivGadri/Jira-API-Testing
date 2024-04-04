import apiManager.ApiHelper;
import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import apiManager.models.enums.IssueTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;
import properties.PropertiesManager;

import java.io.InputStream;
import java.util.Objects;

import static apiManager.ApiHelper.logFailUnexpected;

public class BaseTests {
    private static final Logger logger = LogManager.getLogger(BaseTests.class);
    public static final String JIRA_PROJECT_NAME = "Ziv Gadri Demo Project";
    protected String JIRA_USER_NAME;
    protected String JIRA_PASSWORD;
    protected ApiHelper apiHelper;
    protected Project testProject;
    protected Issue testIssue;
    protected Comment testComment;
    protected String testCommentText = "This is a test comment";
    protected String updatedTestCommentText = "This is an updated test comment";
    protected SoftAssert softAssert;
    protected PropertiesManager props;
    protected final static String pathToLocalPropertiesFile = System.getProperty("pathToLocalPropertiesFile", "src/test/resources/JiraTesting.properties");

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        setProperties();
        setCredentialsVariables();
        apiHelper = new ApiHelper(JIRA_USER_NAME, JIRA_PASSWORD);
    }

    public void setProperties() {
        try {
            logger.info("Start reading properties file - {}", pathToLocalPropertiesFile);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(pathToLocalPropertiesFile);
            if (Objects.isNull(inputStream)) {
                logger.error("Path to properties passed = '{}' Is not valid", pathToLocalPropertiesFile);
                throw new ExceptionInInitializerError("Could not parse provided properties file. Please provide a valid - 'pathToLocalPropertiesFile' parameter");
            }
            props = new PropertiesManager(inputStream);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
    }

    /**
     * This method will look for an environment variable by the varName parameter.
     * It will return the def value in case there will be no such env var - varName
     *
     * @param varName   The key value for the environment variable
     * @param def       The default value to return if no env variable is found
     *
     * @return String value
     **/
    public static String getEnvVarWithDefault(String varName, String def) {
        String returnEnvValue = System.getenv(varName);
        String returnPropValue = System.getProperty(varName);
        if ((Objects.isNull(returnEnvValue) || returnEnvValue.isEmpty())
                && (Objects.isNull(returnPropValue) || returnPropValue.isEmpty()))  {
            return def;
        } else if (Objects.isNull(returnEnvValue) || returnEnvValue.isEmpty()) {
            return returnPropValue;
        } else return returnEnvValue;
    }

    protected void setCredentialsVariables() {
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

    public static Project buildProjectObject() {
        return new Project.ProjectBuilder("Example", JIRA_PROJECT_NAME).
                setProjectTypeKey("Test").
                setProjectTemplateKey("com.atlassian.jira-core-project-templates:jira-core-project-management").
                setDescription("This is a project for demo testing").
                setLead("Ziv Gadri").
                setUrl("http://atlassian.com").
                setAssigneeType("PROJECT_LEAD").
                setAvatarId(130384).
                setIssueSecurityScheme(12345).
                setPermissionScheme(12345).
                setNotificationScheme(12345).
                setCategoryId(11111).
                build();
    }

    public static Issue buildIssueObject(Project project) {
        return new Issue(project, IssueTypes.BUG);
    }

    public static Comment createCommentObject(String comment) {
        return new Comment(comment);
    }

    public int getNumberOfCommentsForIssue(Issue issue) {
        return apiHelper.getNumOfCommentsForIssue(issue);
    }
}