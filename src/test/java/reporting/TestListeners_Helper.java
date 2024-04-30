package reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import reporting.testrail.TestRail_Manager;
import reporting.testrail.TestResults;
import reporting.testrail.constants.Suites;
import reporting.testrail.entities.TestRailTestCase;
import reporting.testrail.entities.TestRailTestResult;
import reporting.testrail.entities.TestRailTestResults;
import properties.PropertiesManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

import static apiManager.ApiHelper.logFailUnexpected;
import static io.restassured.RestAssured.given;

public class TestListeners_Helper {

    protected static final Logger logger = LogManager.getLogger(TestListeners_Helper.class);
    protected static final String pathToLocalPropertiesFile = System.getProperty("pathToLocalPropertiesFile", ".\\src\\test\\resources\\JiraTesting.properties");
    public static PropertiesManager props = setProperties();
    protected TestRail_Manager testRail_Manager;
    protected boolean reportToTestRail = Boolean.parseBoolean(getEnvVarWithDefault("REPORT_TO_TESTRAIL", props.reportToTestRail()));
    protected String TESTRAIL_USERNAME = getEnvVarWithDefault("TESTRAIL_USERNAME", props.getTestRailUserName());
    protected String TESTRAIL_KEY = getEnvVarWithDefault("TESTRAIL_KEY", props.getTestRailKey());
    protected String TESTRAIL_HOST = getEnvVarWithDefault("TESTRAIL_HOST", props.getTestRailHost());
    protected String PROJECT_NAME = getEnvVarWithDefault("TEST_RAIL_PROJECT_NAME", props.getTestRailProjectName());
    protected Suites TESTRAIL_TEST_SUITE_NAME = Suites.valueOf(getEnvVarWithDefault("TESTRAIL_SUITE", Suites.REGRESSION.name()));
    protected boolean reportToSlack = Boolean.parseBoolean(System.getProperty("REPORT_TO_SLACK", props.reportToSlack()));
    protected SlackChannel SLACK_CHANNEL = getSlackChannel();
    protected List<TestRailTestCase> testRailTestCases;
    protected List<Integer> casesIDs;
    protected TestRailTestResult result;
    protected TestRailTestResults testRailTestResults;
    protected int testRunID;
    protected int testPlanID;
    protected String testPlanName;
    protected String testRailSuiteName;
    protected String testRailLink;
    protected static String SLACK_REPORT;
    protected String TESTRAIL_TESTCASE_LINK = "\n****************************************************************************************" +
            "\n* Test Rail - Test Case Link: https://" + TESTRAIL_HOST + "/index.php?/cases/view/{} " + "\n* Test Name - {} " +
            "\n*****************************************************************************************";
    protected int testCounter = 1;
    protected DemoProject_Jira demoProjectJira;

    protected void setupTestRailReport(ITestContext iTestContext) {
        String suiteName = iTestContext.getCurrentXmlTest().getSuite().getName();
        testPlanName = setTestPlanName(suiteName);
        logger.info("Current run TestPlan Name is = {}", testPlanName);
        testRail_Manager = new TestRail_Manager(PROJECT_NAME, TESTRAIL_USERNAME, TESTRAIL_KEY, TESTRAIL_HOST);
        testRailTestCases = testRail_Manager.getTestSuiteCasesWithRequiredSuite(testRailSuiteName, TESTRAIL_TEST_SUITE_NAME);
        casesIDs = testRail_Manager.getTestCasesIDsList(testRailTestCases);
        testRail_Manager.addTestPlan(testPlanName);
        testPlanID = testRail_Manager.getTestPlanByName(testPlanName).getId();
        int suiteID = testRail_Manager.getTestSuiteByName(testRailSuiteName).getId();
        testRail_Manager.addTestPlanEntry(testPlanID, suiteID, casesIDs);
        testRailTestResults = new TestRailTestResults();
        testRunID = testRail_Manager.getTestRunByName(testPlanID, testRailSuiteName).getId();
    }

    protected static void printAllSuiteTestsToSlack(ITestContext iTestContext) {
        int numOfTests = iTestContext.getAllTestMethods().length;
        StringBuilder stringBuilder = new StringBuilder("\nThe following flows will be executed - total of " + numOfTests + " tests:\n```");
        for (int i = 0; i < numOfTests; i++) {
            String testName = iTestContext.getSuite().getAllMethods().get(i).getConstructorOrMethod().getMethod().getAnnotation(DemoProject_Jira.class).testName();
            stringBuilder.append(i + 1).append(". ").append(testName).append("\n");
        }
        stringBuilder.append("```").append("\n\n\n").append("Test Results: \n");
        sendSlackMessage(SlackChannel.TEST_SLACK_CHANNEL_NAME, stringBuilder.toString());
    }

    protected static void sendSlackMessage(SlackChannel channel, String text) {
        if (Boolean.parseBoolean(SLACK_REPORT)) {
            try {
                logger.info("Before sends message = {} to slack Channel = {} ", text, channel);
                String path = String.format("https://hooks.slack.com/services/%s", channel.getCode());
                Map<String, Object> jsonAsMap = new HashMap<>();
                jsonAsMap.put("text", text);
                given().
                        contentType("application/json").
                        body(jsonAsMap).
                        when().
                        log().ifValidationFails().
                        post(path).
                        then().
                        log().ifValidationFails().
                        statusCode(200);
                logger.info("Message sent into slack Channel = {} , with text = {} ", channel, text);
            } catch (NullPointerException npe) {
                logger.info("No Slack reporting was made. 'channel' parameter is null.\nA non recognizable or no slack channel at all was sent as a parameter.");
            } catch (AssertionError ae) {
                logger.info("Slack message send request failed.\nMore details: " + ae.getMessage());
            }
        } else {
            logger.info("'SLACK_REPORT' parameter set to false, not reporting to slack.");
        }
    }

    protected void sendTestResultSlackMessage(String resultString) {
        String message = String.format("%s. %s - %s", testCounter, demoProjectJira.testName(), resultString);
        sendSlackMessage(SLACK_CHANNEL, message);
        testCounter++;
    }

    protected void setTestResultForTestRailReporting(ITestResult iTestResult, TestResults testResult) {
        result = new TestRailTestResult();
        switch (testResult) {
            case SUCCESS:
                result.setComment("Passed");
                result.setStatus_id(1);
                break;
            case FAILURE:
                result.setComment("Failed");
                result.setStatus_id(5);
                break;
            case SKIPPED:
                result.setComment("Skipped");
                result.setStatus_id(2);
        }
        String caseID = setCaseIdAndAddResult(demoProjectJira, iTestResult.getName().trim());
        logger.info(TESTRAIL_TESTCASE_LINK, caseID, iTestResult.getName().trim());
    }

    protected String setTestPlanName(String suiteName) {
        StringBuilder testPlanName = new StringBuilder("JIRA-API-UI-TESTS");
        testPlanName.append(suiteName).
                append(" | Test_Run-").append(UUID.randomUUID().toString().substring(0, 5));
        return testPlanName.toString();
    }

    protected String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentStamp = new Timestamp(now.getTime());
        return currentStamp.toString();
    }

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

    protected synchronized String setCaseIdAndAddResult(DemoProject_Jira demoProjectJira, String testName) {
        String testCaseId = "";
        String[] testCasesIds = demoProjectJira.testRailCaseId();
        int cases = testCasesIds.length;
        if (cases > 1) {
            for (int i = 0; i < cases; i++) {
                String caseID = testCasesIds[i];
                Optional<TestRailTestCase> caseMatch = testRailTestCases.stream().filter(Case -> Case.getId().toString().contains(caseID)).findAny();
                if (caseMatch.isPresent()) {
                    result.setCase_id(Integer.valueOf(caseID));
                    testRailTestResults.addResult(result);
                    testCaseId = caseID;
                }
            }
        } else if (cases == 1) {
            result.setCase_id(Integer.valueOf(testCasesIds[0]));
            testRailTestResults.addResult(result);
            testCaseId = testCasesIds[0];
        } else {
            logger.warn("There is no Test Rail case ID at '{}' Test Case", testName);
        }
        return testCaseId;
    }

    private static PropertiesManager setProperties() {
        try {
            logger.info("Start reading properties file - {}", pathToLocalPropertiesFile);
            InputStream inputStream = new FileInputStream(pathToLocalPropertiesFile);
            return new PropertiesManager(inputStream);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        throw new RuntimeException("Could not parse provided properties file. Please provide a valid - 'pathToLocalPropertiesFile' parameter");
    }

    private SlackChannel getSlackChannel() {
        try {
            return SlackChannel.valueOf(getEnvVarWithDefault("SLACK_CHANNEL", props.getSlackChannel()));
        } catch (IllegalArgumentException iae) {
            logger.error("The slack channel provided is not valid. Setting slack channel to null");
            return null;
        }
    }
}