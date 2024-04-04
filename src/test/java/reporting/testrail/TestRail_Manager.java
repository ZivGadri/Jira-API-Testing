package reporting.testrail;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reporting.testrail.constants.AutomationStatus;
import reporting.testrail.constants.Suites;
import reporting.testrail.constants.SystemProperties;
import reporting.testrail.constants.TestRailEndPoints;
import reporting.testrail.entities.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRail_Manager {

    private static final Logger logger = LogManager.getLogger(TestRail_Manager.class);

    private final String AUTHORIZATION = "Authorization";
    private final String BASIC = "Basic ";
    private TestRailMapper testRailMapper = new TestRailMapper();
    private HashMap<String, Integer> suitesItems;
    private HashMap<String, Integer> automationStatusItems;
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    private int projectId;
    private boolean printInFailure = true;

    /**
     * Constructor
     *
     * @param projectName must be initialized with specific TestRail project name
     * @param passwordOrKey TestRail password or private key
     * @param username TestRail username
     * @param host TestRail Host name
     * @see #getTestRailProjectByName(String)
     */
    public TestRail_Manager(String projectName, String username, String passwordOrKey, String host) {
        SystemProperties.testRailProjectName = SystemProperties.propSetter("automation.tools.testrail.project.name", projectName);
        SystemProperties.testRailUsername = SystemProperties.propSetter("automation.tools.testrail.username", username);
        SystemProperties.testRailPassword = SystemProperties.propSetter("automation.tools.testrail.password", passwordOrKey);
        SystemProperties.testRailHost = SystemProperties.propSetter("automation.tools.testrail.host", host);
        projectId = getTestRailProjectByName(projectName).getId();
    }

    /**
     * Constructor
     *
     * @param projectName must be initialized with specific TestRail project name
     * @param passwordOrKey TestRail password or private key
     * @param username TestRail username
     * @param host TestRail Host name
     * @param printInFailure True if should log all in case of failure
     * @see #getTestRailProjectByName(String)
     */
    public TestRail_Manager(String projectName, String username, String passwordOrKey, String host, boolean printInFailure) {
        this.printInFailure = printInFailure;
        SystemProperties.testRailProjectName = SystemProperties.propSetter("automation.tools.testrail.project.name", projectName);
        SystemProperties.testRailUsername = SystemProperties.propSetter("automation.tools.testrail.username", username);
        SystemProperties.testRailPassword = SystemProperties.propSetter("automation.tools.testrail.password", passwordOrKey);
        SystemProperties.testRailHost = SystemProperties.propSetter("automation.tools.testrail.host", host);
        projectId = getTestRailProjectByName(projectName).getId();
    }

    /**
     * This method gets the project ID
     *
     * @return Project ID
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * This method checks if TestRailPassword property encoded
     *
     * @return encoded (base64) password
     */
    public String encodeCredentials() {
        if (validateBase64(SystemProperties.testRailPassword)) {
            return SystemProperties.testRailPassword;
        }
        String basicAuthUnencoded = String.format("%s:%s", SystemProperties.testRailUsername, SystemProperties.testRailPassword);
        return encodeToBase64(basicAuthUnencoded);
    }

    /**
     * This method create the start of the API request with the Headers
     *
     * @return {@link RequestSpecification}
     */
    public RequestSpecification testRailRequest() {
        if (printInFailure)
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        return RestAssured.given().log().ifValidationFails(LogDetail.ALL)
                .header(AUTHORIZATION, BASIC + encodeCredentials())
                .contentType(ContentType.JSON);
    }

    /**
     * This method create the start of the API request with the Headers
     *
     * @return {@link RequestSpecification}
     */
    public RequestSpecification testRailRequestGet() {
        if (printInFailure)
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        return RestAssured.given().log().ifValidationFails(LogDetail.ALL)
                .header(AUTHORIZATION, BASIC + encodeCredentials())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    /**
     * This method gets all TestRail projects
     *
     * @return list of all user's TestRail projects {@code List<TestRailProject>}
     * @see TestRailProject
     */
    public List<TestRailProject> getAllTestRailProjects() {
        try {
            Response response = testRailRequestGet().get(TestRailEndPoints.GET_PROJECTS.uri());
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(response.as(TestRailProject[].class));
        } catch (Exception e){
            logger.error("Failed to get projects from TestRail", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets specific TestRail project by name
     *
     * @param projectName requested TestRail project name
     * @return TestRail project {@link TestRailProject}
     */
    public TestRailProject getTestRailProjectByName(String projectName) {
        try {
            List<TestRailProject> testRailProjects = getAllTestRailProjects();
            if (testRailProjects != null) {
                for (TestRailProject project : testRailProjects) {
                    if (project.getName().equalsIgnoreCase(projectName)) {
                        projectId = project.getId();
                        logger.info("Test Project ID: [ " + projectId + " ].");
                        return project;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get project - " + projectName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Project - " + projectName + " does not exists on TestRail");
    }

    /**
     * This method gets specific TestRail project by ID
     *
     * @param projectId requested TestRail project ID
     * @return TestRail project {@link TestRailProject}
     */
    public TestRailProject getTestRailProjectByID(int projectId){
        try {
            Response response = testRailRequestGet().get(String.format(TestRailEndPoints.GET_PROJECT.format(projectId)));
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return response.as(TestRailProject.class);
        } catch (Exception e) {
            logger.error("Failed to get project with ID - " + projectId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets all the test cases of the project
     *
     * @return List of TestRail test cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getAllProjectTestCases() {
        try {
            List<TestRailTestSuite> suites = getTestSuites();
            if (suites != null) {
                List<TestRailTestCase> allTestCases = new ArrayList<>();
                for (TestRailTestSuite suite : suites) {
                    allTestCases.addAll(getCasesForTestSuiteByID(suite.getId()));
                }
                return allTestCases;
            }
        } catch (Exception e) {
            logger.error("Failed to test cases for project ID - " + projectId, e);
            throw new RuntimeException(e);

        }
        throw new RuntimeException("There are no test cases under project id - " + projectId);
    }

    /**
     * Get test cases of specific section id.
     *
     * @param testSuiteId Suite ID
     * @param sectionId Section ID
     * @return List of TestRail test cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getCasesForSectionByID(int testSuiteId, int sectionId) {
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_CASES_SUITE_ID_SECTION_ID.format(projectId, testSuiteId, sectionId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailTestCase[].class));
        } catch (Exception e) {
            logger.error("Failed to get test cases under suite - " + testSuiteId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get test cases of specific section name.
     *
     * @param testSuiteId Suite ID
     * @param sectionName Section name
     * @return List of TestRail test cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getCasesForSectionByName(int testSuiteId, String sectionName) {
        try {
            List<TestRailSection> allTestSections = getSections(testSuiteId);
            if (allTestSections != null) {
                for (TestRailSection section : allTestSections) {
                    if (section.getName().equalsIgnoreCase(sectionName)) {
                        return getCasesForSectionByID(testSuiteId, section.getId());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get section with name - " + sectionName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("There are no test cases under section with name - " + sectionName);
    }

    /**
     * This method gets test cases of specific test suite id
     *
     * @param testSuiteId Test Suite ID
     * @return List of TestRail test cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getCasesForTestSuiteByID(int testSuiteId) {
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_CASES.format(projectId, testSuiteId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailTestCase[].class));
        } catch (Exception e) {
            logger.error("Failed to get test cases under suite - " + testSuiteId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets test cases of specific test suite name
     *
     * @param testSuiteName Test Suite Name
     * @return List of TestRail test cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getCasesForTestSuiteByName(String testSuiteName) {
        try {
            List<TestRailTestSuite> allTestTestSuites = getTestSuites();
            if (allTestTestSuites != null) {
                for (TestRailTestSuite testSuite : allTestTestSuites) {
                    if (testSuite.getName().equalsIgnoreCase(testSuiteName)) {
                        return getCasesForTestSuiteByID(testSuite.getId());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test cases under suite - " + testSuiteName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("There are no test cases under test suite with name - " + testSuiteName);
    }

    /**
     * This method gets all the test cases in the project with a specific 'custom_automation_status' value (including 'None' test cases suite)
     *
     * @param testSuiteName Test Suite Name
     * @param automationStatus Requested AutomationStatus value ('custom_automation_status') as {@link AutomationStatus}
     * @return List of requested TestRail cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getAutomatedCasesForTestSuiteByName(String testSuiteName, AutomationStatus automationStatus) {
        return getAutomatedCasesForTestSuiteByName(testSuiteName, automationStatus, true);
    }

    /**
     * This method gets all the test cases in the project with a specific 'custom_automation_status' value
     * Depends on the request test case suite (including none)
     * @param testSuiteName Test Suite Name
     * @param automationStatus Requested AutomationStatus value ('custom_automation_status') as {@link AutomationStatus}
     * @param include_none In case you would like to get cases that have 'None' custom test case suite - True, Else - False
     * @return List of requested TestRail cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getAutomatedCasesForTestSuiteByName(String testSuiteName, AutomationStatus automationStatus, boolean include_none) {
        try {
            List<TestRailTestCase> MappedTestCases = new ArrayList<>();

            TestRailCaseFields fields = getCaseFieldByName(SystemProperties.TEST_RAIL_AUTOMATION_STATUS);
            if (fields != null) {
                automationStatusItems = testRailMapper.mapAutomationStatus(fields.getAutomationStatusItems());
                int automationStatusID = automationStatusItems.get(automationStatus.getName().toUpperCase());
                List<TestRailTestCase> allSuiteCases = getCasesForTestSuiteByName(testSuiteName);
                if (allSuiteCases != null) {
                    for (TestRailTestCase testCase : allSuiteCases) {
                        String automationStatusNum = testCase.getCustom_automation_status();
                        boolean contain_none = testCase.getCustom_suiteslist() == null? false:testCase.getCustom_suiteslist().contains(Suites.NONE.ordinal());
                        if (Integer.parseInt(automationStatusNum) == automationStatusID) {
                            if (include_none || !contain_none) {
                                MappedTestCases.add(testCase);
                            }
                        }
                    }
                    return MappedTestCases;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test cases with automation status - " + automationStatus, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("There are no test cases with automation status - " + automationStatus);
    }

    /**
     * This method gets all the test cases in the project with a specific 'custom_suiteslist' name.
     *
     * @param suitesName Requested Suites name ('custom_suiteslist')
     * @return list of TestRail cases {@code List<TestRailTestCase>}
     * @see TestRailTestCase
     */
    public List<TestRailTestCase> getCasesWithSuiteName(String suitesName) {
        try {
            List<TestRailTestCase> MappedTestCases = new ArrayList<>();
            TestRailCaseFields fields = getCaseFieldByName(SystemProperties.TEST_RAIL_CUSTOM_FIELD_NAME);
            if (fields != null) {
                suitesItems = testRailMapper.mapSuiteParams(fields.getSuiteItems());
                int suiteID = suitesItems.get(suitesName.toUpperCase());
                List<TestRailTestCase> allTestCases = getAllProjectTestCases();
                if (allTestCases != null) {
                    for (TestRailTestCase testCase : allTestCases) {
                        List<Integer> suitesList = testCase.getCustom_suiteslist();
                        for (Integer suite : suitesList) {
                            if (suite.intValue() == suiteID)
                                MappedTestCases.add(testCase);
                        }
                    }
                    return MappedTestCases;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test cases with suite - " + suitesName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("There are no test cases with suite name - " + suitesName);
    }

    /**
     * This method gets a specific test case
     *
     * @param testCaseId TestRail case ID
     * @return TestRail test case {@link TestRailTestCase}
     */
    public TestRailTestCase getTestCaseByID(int testCaseId) {
        try {
            String tcId = Integer.toString(testCaseId);
            Response postResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_CASE.format(tcId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestCase.class);
        } catch (Exception e) {
            logger.error("Failed to get test case with id - " + testCaseId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method updates exist test case
     *
     * @param testCaseData The test case to be changed {@link TestRailTestCase}
     * @return {@link TestRailTestCase}
     */
    public TestRailTestCase updateTestCase(TestRailTestCase testCaseData) {
        try {
            Response postResponse = testRailRequest()
                    .body(testCaseData)
                    .post(TestRailEndPoints.UPDATE_CASE.format(testCaseData.getId()));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestCase.class);
        } catch (Exception e) {
            logger.error("Failed to update case - " + testCaseData.getTitle(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the cases id (in test run) from the TestRailTest list
     *
     * @param testCasesIds TestRail test case ID {@code List<TestRailTest>}
     * @return list of cases ID {@code List<Integer>}
     * @see TestRailTest
     */
    public List<Integer> getCasesIds(List<TestRailTest> testCasesIds) {
        List<Integer> casesIds = new ArrayList<>();
        for (TestRailTest testCaseId : testCasesIds) {
            casesIds.add(testCaseId.getCase_id());
        }
        return casesIds;
    }

    /**
     * This method gets the test cases id from the TestRailTestCase list
     *
     * @param testCasesIds TestRail test case ID {@code List<TestRailTestCase>}
     * @return list of cases ID {@code List<Integer>}
     * @see TestRailTestCase
     */
    public List<Integer> getTestCasesIDsList(List<TestRailTestCase> testCasesIds) {
        List<Integer> casesIds = new ArrayList<>();
        for (TestRailTestCase testCaseId : testCasesIds) {
            casesIds.add(testCaseId.getId());
        }
        return casesIds;
    }

    /**
     * This method gets all custom fields for test cases
     *
     * @return a list of available test case custom fields {@code List<TestRailCaseFields>}
     * @see TestRailCaseFields
     */
    public List<TestRailCaseFields> getCaseFields(){
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_CASE_FIELDS.uri());
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailCaseFields[].class));
        }
        catch (Exception e){
            logger.error("Failed to get test case fields", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a field name and search it in the available test case custom fields list
     *
     * @param fieldName requested field's name
     * @return The requested field if found {@link TestRailCaseFields}
     */
    public TestRailCaseFields getCaseFieldByName(String fieldName) {
        try {
            List<TestRailCaseFields> lst = getCaseFields();
            for (TestRailCaseFields field : lst) {
                if (field.getSystem_name().equals(fieldName)) {
                    return field;
                }
            }
        } catch (Exception e){
            logger.error("Failed to get field with name - " + fieldName, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * This method gets all TestRail Test suite ID's sections
     *
     * @param testSuiteId Test Suite ID
     * @return List of sections {@code List<TestRailSection>}
     * @see TestRailSection
     */
    public List<TestRailSection> getSections(int testSuiteId) {
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_SECTIONS_PROJECT_ID_SUITE_ID.format(projectId, testSuiteId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailSection[].class));
        } catch (Exception e) {
            logger.error("Failed to get test cases under suite - " + testSuiteId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific section
     *
     * @param sectionId Requested section ID
     * @return TestRail section {@link TestRailSection}
     */
    public TestRailSection getSectionByID(int sectionId){
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_SECTION.format(sectionId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return getResponse.as(TestRailSection.class);
        } catch (Exception e) {
            logger.error("Failed to get section with ID - " + sectionId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets all Test Suites of TestRail project
     *
     * @return list of suites {@code List<TestRailTestSuite>}
     * @see TestRailTestSuite
     */
    public List<TestRailTestSuite> getTestSuites() {
        try {
            RequestSpecification requestSpecification = testRailRequestGet();
            Response getResponse = requestSpecification
                    .get(TestRailEndPoints.GET_SUITES.format(projectId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailTestSuite[].class));
        } catch (Exception e) {
            logger.error("Failed to get suites", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific Test Suite by ID
     *
     * @param testSuiteId Requested suite ID
     * @return TestRail Test Suite {@link TestRailTestSuite}
     */
    public TestRailTestSuite getTestSuiteByID(int testSuiteId) {
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_SUITE.format(testSuiteId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return getResponse.as(TestRailTestSuite.class);
        } catch (Exception e) {
            logger.error("Failed to get test suite with ID - " + testSuiteId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific Test Suite by ID
     *
     * @param testSuiteName Requested suite Name
     * @return TestRail Test Suite {@link TestRailTestSuite}
     */
    public TestRailTestSuite getTestSuiteByName(String testSuiteName) {
        try {
            List<TestRailTestSuite> allTestSuites = getTestSuites();
            if (allTestSuites != null) {
                for (TestRailTestSuite testSuite : allTestSuites) {
                    if (testSuite.getName().equalsIgnoreCase(testSuiteName)) {
                        return testSuite;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test suite with name - " + testSuiteName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Test suite with name " + testSuiteName + " does not exists on TestRail");
    }

    /**
     * This method gets all Test Plans of TestRail project
     *
     * @return list of test plans {@code List<TestRailTestPlan>}
     * @see TestRailTestPlan
     */
    public List<TestRailTestPlan> getTestPlans() {
        try {
            Response response = testRailRequestGet()
                    .get(TestRailEndPoints.GET_PLANS.format(projectId));
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(response.as(TestRailTestPlan[].class));
        } catch (Exception e) {
            logger.error("Failed to get test plans for project - " + projectId, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * This method gets all Test Plans of TestRail project according to requested is_completed status.
     *
     * @param isCompleted 1 to return completed test plans only. 0 to return active test plans only.
     * @return list of test plans {@code List<TestRailTestPlan>}
     * @see TestRailTestPlan
     */
    public List<TestRailTestPlan> getTestPlans(int isCompleted) {
        try {
            Response response = testRailRequestGet()
                    .get(TestRailEndPoints.GET_PLANS_IS_COMPLETE.format(projectId, isCompleted));
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(response.as(TestRailTestPlan[].class));
        } catch (Exception e) {
            logger.error("Failed to get test plans for project - " + projectId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific Test Plan by ID
     *
     * @param testPlanID Requested Test Plan ID
     * @return TestRail Test Plan {@link TestRailTestPlan}
     */
    public TestRailTestPlan getTestPlanByID(int testPlanID){
        try {
            Response response = testRailRequestGet()
                    .get(TestRailEndPoints.GET_PLAN.format(testPlanID));
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return response.as(TestRailTestPlan.class);
        } catch (Exception e) {
            logger.error("Failed to get test plan with ID - " + testPlanID, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific Test Plan by name
     *
     * @param testPlanName Requested Test Plan name
     * @return TestRail Test Plan {@link TestRailTestPlan}
     */
    public TestRailTestPlan getTestPlanByName(String testPlanName){
        try {
            List<TestRailTestPlan> allTestPlans = getTestPlans();
            if (allTestPlans != null) {
                for (TestRailTestPlan testPlan : allTestPlans) {
                    if (testPlan.getName().equalsIgnoreCase(testPlanName)) {
                        return testPlan;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test plan with name - " + testPlanName, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Test plan with name " + testPlanName + " does not exists on TestRail");
    }

    /**
     * This method add new Test Plan
     *
     * @param testPlanName Name of TestPlan in TestRail
     * @return TestRail Test Plan {@link TestRailTestPlan}
     */
    public TestRailTestPlan addTestPlan(String testPlanName) {
        TestRailTestPlan testRailTestPlan = new TestRailTestPlan();
        testRailTestPlan.setName(testPlanName);
        try {
            Response postResponse = testRailRequest()
                    .body(testRailTestPlan)
                    .post(TestRailEndPoints.ADD_PLAN.format(projectId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestPlan.class);
        } catch (Exception e) {
            logger.error("Failed to add test plan - " + testPlanName + " for project - " + projectId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method updates exist test plan
     *
     * @param testRailTestPlan The test plan to be changed {@link TestRailTestPlan}
     * @return {@link TestRailTestPlan}
     */
    public TestRailTestPlan updateTestPlan(TestRailTestPlan testRailTestPlan) {
        try {
            Response postResponse = testRailRequest()
                    .body(testRailTestPlan)
                    .post(TestRailEndPoints.UPDATE_PLAN.format(testRailTestPlan.getId()));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestPlan.class);
        } catch (Exception e) {
            logger.error("Failed to add test plan - " + testRailTestPlan.getId() + " for project - " + projectId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets a specific test plan entry under a test plan by name (Test Run)
     *
     * @param testPlanID Test Plan ID
     * @param testRunName Requested test run name
     * @return Test Plan Entry {@link TestRailTestPlanEntry}
     */
    public TestRailTestRun getTestRunByName(int testPlanID, String testRunName) {
        try {
            List<TestRailTestPlanEntry> testRuns = getTestPlanEntries(testPlanID);
            if (testRuns != null) {
//                for (TestRailTestPlanEntry testRun : testRuns) {
//                    if (testRun.getName().equals(testRunName)) {
//                        return testRun;
                for (TestRailTestPlanEntry testRun : testRuns) {
                    for (TestRailTestRun runs : testRun.runs){
                        if (runs.getName().equalsIgnoreCase(testRunName))
                            return runs;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get test plan entry with name - " + testRunName + " from test plan ID - " + testPlanID, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Test plan entry with name " + testRunName + " does not exists on TestRail");
    }

    /**
     * This methods add new test Plan Entry to a test plan
     *
     * @param testPlanId Exist Test Plan ID
     * @param suiteId Test Suite ID which contain the test cases
     * @param casesIds list of cases IDs which should be in the plan {@link #getCasesIds(List)}
     * @return {@link TestRailTestPlanEntry}
     */
    public TestRailTestPlanEntry addTestPlanEntry(int testPlanId, int suiteId, List<Integer> casesIds) {
        TestRailTestPlanEntry testRailTestPlanEntry = new TestRailTestPlanEntry();
        testRailTestPlanEntry.setSuite_id(suiteId);
        testRailTestPlanEntry.setCase_ids(casesIds);
        testRailTestPlanEntry.setInclude_all(false);
        try {
            Response postResponse = testRailRequest()
                    .body(testRailTestPlanEntry)
                    .post(TestRailEndPoints.ADD_PLAN_ENTRY.format(testPlanId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestPlanEntry.class);
        } catch (Exception e) {
            logger.error("Failed to add test plan entry for plan - " + testPlanId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This methods add new test Plan Entry to a test plan
     *
     * @param testPlanId Exist Test Plan ID
     * @param suiteId Test Suite ID which contain the test cases
     * @param casesIds list of cases IDs which should be in the plan {@link #getCasesIds(List)}
     * @param run_name Test run name to be created
     * @return {@link TestRailTestPlanEntry}
     */
    public TestRailTestPlanEntry addTestPlanEntry(int testPlanId, int suiteId, List<Integer> casesIds, String run_name) {
        TestRailTestPlanEntry testRailTestPlanEntry = new TestRailTestPlanEntry();
        testRailTestPlanEntry.setSuite_id(suiteId);
        testRailTestPlanEntry.setCase_ids(casesIds);
        testRailTestPlanEntry.setName(run_name);
        testRailTestPlanEntry.setInclude_all(false);
        try {
            Response postResponse = testRailRequest()
                    .body(testRailTestPlanEntry)
                    .post(TestRailEndPoints.ADD_PLAN_ENTRY.format(testPlanId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestPlanEntry.class);
        } catch (Exception e) {
            logger.error("Failed to add test plan entry for plan - " + testPlanId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method updates exist test plan entry
     *
     * @param testPlanId Test plan ID
     * @param entry The test plan entry to be changed {@link TestRailTestPlanEntry}
     * @return {@link TestRailTestPlanEntry}
     */
    public TestRailTestPlanEntry updateTestPlanEntry(int testPlanId, TestRailTestPlanEntry entry) {
        try {
            Response postResponse = testRailRequest()
                    .body(entry)
                    .post(TestRailEndPoints.UPDATE_PLAN_ENTRY.format(testPlanId, entry.getId()));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return postResponse.as(TestRailTestPlanEntry.class);
        } catch (Exception e) {
            logger.error("Failed to add test plan entry for plan - " + testPlanId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets all the entries of specific test plan (Test Runs)
     *
     * @param testPlanID Test Plan ID
     * @return list of Test Plan Entries {@code List<TestRailTestPlanEntry>}
     * @see TestRailTestPlanEntry
     */
    public List<TestRailTestPlanEntry> getTestPlanEntries(int testPlanID) {
        try {
            TestRailTestPlan testPlan = getTestPlanByID(testPlanID);
            if (testPlan != null)
                return testPlan.entries;
        } catch (Exception e) {
            logger.error("Failed to get entries from test plan ID - " + testPlanID, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Test plan with ID " + testPlanID + " does not exists on TestRail");
    }

    /**
     * This method gets all the tests from all test runs
     *
     * @param testPlanID Test Plan ID
     * @return list of all tests {@code List<TestRailTest>}
     * @see TestRailTest
     */
    public List<TestRailTest> getAllTestRunsTests(int testPlanID) {
        try {
            List<TestRailTestPlanEntry> testRuns = getTestPlanEntries(testPlanID);
            if (testRuns != null) {
                List<TestRailTest> TestRunsCases = new ArrayList<>();
                for (TestRailTestPlanEntry testRun : testRuns) {
                    for (TestRailTestRun runs : testRun.runs){
                        int testRunID = runs.getId();
                        TestRunsCases.addAll(getTestRunTests(testRunID));
                    }
                }
                return TestRunsCases;
            }
        } catch (Exception e){
            logger.error("Failed to get cases from test runs under test plan ID - " + testPlanID, e);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("There are no test Runs under test plan ID - " + testPlanID);
    }

    /**
     * This method gets all the tests under the specific test run ID
     *
     * @param testRunId Requested test run ID
     * @return list of all tests {@code List<TestRailTest>}
     * @see TestRailTest
     */
    public List<TestRailTest> getTestRunTests(int testRunId) {
        try {
            Response getResponse = testRailRequestGet()
                    .get(TestRailEndPoints.GET_TESTS.format(testRunId));
            getResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return Arrays.asList(getResponse.as(TestRailTest[].class));
        } catch (Exception e) {
            logger.error("Failed to get test run - " + testRunId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method update the result of specific test
     *
     * @param testRunID Test run ID
     * @param testCaseID Requested test case ID
     * @param result Test Case results {@link TestRailTestResult}
     * @return True if success
     */
    public boolean addResultForCase(int testRunID, int testCaseID, TestRailTestResult result){
        try {
            Response postResponse = testRailRequest()
                    .body(result)
                    .post(TestRailEndPoints.ADD_RESULTS_FOR_CASE.format(testRunID,testCaseID));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return true;
        } catch (Exception e) {
            logger.error("Failed to set result for Case - " + testCaseID + " under test run - " + testRunID, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method update the results of one or more cases under the same run ID
     *
     * @param testRunId Test run ID
     * @param results List of Test Case results {@link TestRailTestResults}
     * @return True if success
     */
    public boolean addResultForCases(int testRunId, TestRailTestResults results) {
        try {
            Response postResponse = testRailRequest()
                    .body(results)
                    .post(TestRailEndPoints.ADD_RESULTS_FOR_CASES.format(testRunId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return true;
        } catch (Exception e) {
            logger.error("Failed to set results for test run - " + testRunId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method closes the given test run ID
     *
     * @param testRunId Test run ID
     * @return True if success
     */
    public boolean closeTestRun(int testRunId) {
        try {
            RestAssured.urlEncodingEnabled = false;
            Response postResponse = testRailRequest()
                    .post(TestRailEndPoints.CLOSE_RUN.format(testRunId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            return true;
        } catch (Exception e) {
            logger.error("Failed to close test run with ID - " + testRunId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method closes the given test plan ID
     *
     * @param testPlanId The ID of the test plan
     * @return True if success
     */
    public TestRailTestPlan closeTestPlan(int testPlanId) {
        try {
            RestAssured.urlEncodingEnabled = false;
            Response postResponse = testRailRequest()
                    .post(TestRailEndPoints.CLOSE_PLAN.format(testPlanId));
            postResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
            logger.info("Test plan with ID ["+testPlanId+"] was closed");
            return postResponse.as(TestRailTestPlan.class);
        } catch (Exception e) {
            logger.error("Failed to close test plan with ID - " + false, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This methods gets all the Test Cases under the given Test Suite and filtered it according to the given Suite name.
     * Returns all the test cases that contains the suite name under the requested test suite.
     *
     * @param test_suite_name Project Test Suite name which contain the test cases.
     * @param suite_name Test Case Suite name to filter the test cases according to.
     *
     * @return Filtered {@link TestRailTestCase} list according to the requested params.
     *
     * @see #getCasesForTestSuiteByName(String)
     * @see #getRequiredSuiteID(Suites)
     */
    public List<TestRailTestCase> getTestSuiteCasesWithRequiredSuite(String test_suite_name, Suites suite_name) {
        try {
            List<TestRailTestCase> testCases = new ArrayList<>();
            List<TestRailTestCase> casesUnderSuite = getCasesForTestSuiteByName(test_suite_name);
            int requestedSuiteID = getRequiredSuiteID(suite_name);
            for (TestRailTestCase testCase : casesUnderSuite) {
                List<Integer> suitesID = testCase.getCustom_suiteslist();
                for (int id : suitesID) {
                    if (id == requestedSuiteID)
                        testCases.add(testCase);
                }
            }
            return testCases;
        } catch (NullPointerException npe) {
            logger.error("Failed to get test cases with suite [" + suite_name.name() + "] under test suite [" + test_suite_name + "]");
            throw npe;
        } catch (RuntimeException rte) {
            throw new RuntimeException("Failed to get test cases with suite [" + suite_name.name() + "] under test suite [" + test_suite_name + "]", rte);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get test cases with suite [" + suite_name.name() + "] under test suite [" + test_suite_name + "]", e);
        }
    }

    /**
     * This method gets all the Test Cases from the given test cases list and filtered it according to the given Suite name.
     * Returns all the test cases that contains the suite name from the given test cases list.
     *
     * @param suite_name Test Case Suite name to filter the test cases according to
     * @param test_cases Test cases list to filter
     *
     * @return Filtered {@link TestRailTestCase} list according to the requested params.
     *
     * @see #getRequiredSuiteID(Suites)
     */
    public List<TestRailTestCase> getTestSuiteCasesWithRequiredSuite(Suites suite_name, List<TestRailTestCase> test_cases) {
        try {
            List<TestRailTestCase> mapped_test_cases = new ArrayList<>();
            int requestedSuiteID = getRequiredSuiteID(suite_name);
            for (TestRailTestCase test_case : test_cases) {
                List<Integer> suitesID = test_case.getCustom_suiteslist();
                if (suitesID != null) {
                    for (int id : suitesID) {
                        if (id == requestedSuiteID)
                            mapped_test_cases.add(test_case);
                    }
                }
            }
            return mapped_test_cases;
        } catch (NullPointerException npe) {
            logger.error("Failed to get test cases with suite [" + suite_name.name() + "]");
            throw npe;
        } catch (RuntimeException rte) {
            throw new RuntimeException("Failed to get test cases with suite [" + suite_name.name() + "]", rte);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get test cases with suite [" + suite_name.name() + "]", e);
        }
    }

    /**
     * This method gets the ID of the requested Test Case Suite name (for required projects).
     * @param suite_name Test Case Suite name to searching for.
     * @return Suite ID
     *
     * @see #getRequiredProjectsCaseFieldItems()
     * @see TestRailMapper#mapSuiteParams(List)
     */
    public int getRequiredSuiteID(Suites suite_name) {
        try {
            List<String> items = getRequiredProjectsCaseFieldItems();
            HashMap<String, Integer> suitesItems = testRailMapper.mapSuiteParams(items);
            return suitesItems.get(suite_name.name().toUpperCase());
        } catch (NullPointerException npe) {
            logger.error("Invalid TestRail suite [" + suite_name.name() + "], Please check the parameter");
            throw npe;
        }
    }

    /**
     * This method gets the ID of the requested Test Case Suite name.
     * @param suite_name Test Case Suite name to searching for.
     * @return Suite ID
     *
     * @see #getCaseFieldByName(String)
     * @see TestRailMapper#mapSuiteParams(List)
     */
    public int getSuiteID(Suites suite_name) {
        try {
            TestRailCaseFields fields = getCaseFieldByName("custom_suiteslist");
            HashMap<String, Integer> suitesItems = testRailMapper.mapSuiteParams(fields.getSuiteItems());
            return suitesItems.get(suite_name.name().toUpperCase());
        } catch (NullPointerException npe) {
            logger.error("Invalid TestRail suite [" + suite_name.name() + "], Please check the parameter");
            throw npe;
        }
    }

    /**
     * This method gets all the case suites of the required projects.
     * @return List of case suite
     *
     * @see #getCaseFieldByName(String)
     */
    private List<String> getRequiredProjectsCaseFieldItems() {
        try {
            List<TestRailFieldConfig> configs = getCaseFieldByName(SystemProperties.TEST_RAIL_CUSTOM_FIELD_NAME).getConfigs();
            for (TestRailFieldConfig config : configs) {
                if (config.getOptions().isIs_required())
                    return Arrays.asList(config.getOptions().getItems().split("\n"));
            }
            throw new RuntimeException("There are no required projects for the requested case field [" + SystemProperties.TEST_RAIL_CUSTOM_FIELD_NAME + "]");
        } catch (Exception e) {
            throw new RuntimeException("There are no required projects for the requested case field [" + SystemProperties.TEST_RAIL_CUSTOM_FIELD_NAME + "]", e);
        }
    }

    private static boolean validateBase64(String origin) {
        String base64pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern pattern = Pattern.compile(base64pattern);
        Matcher matcher = pattern.matcher(origin);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private static String encodeToBase64(String origin) {
        return Base64.getEncoder().encodeToString(origin.getBytes());
    }
}
