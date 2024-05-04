package reporting;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reporting.testrail.TestResults;

public class TestListeners extends TestListeners_Helper implements ITestListener {

    @Override
    public void onStart(ITestContext iTestContext) {
        if (reportToTestRail) {
            setupTestRailReport(iTestContext);
        }
        if (reportToSlack) {
            printAllSuiteTestsToSlack(iTestContext);
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        demoProjectJira = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(DemoProject_Jira.class);
        logger.info("\n**********************************************************************************************" +
                        "\n*\n* Test started: '{}'\n*\n" +
                        "**********************************************************************************************\n",
                demoProjectJira.testName());
        sendSlackMessage(SLACK_CHANNEL, "**** Test started: " + demoProjectJira.testName() + " ****");
    }

    @Override
    public synchronized void onTestSuccess(ITestResult iTestResult) {
        logger.info("\n**********************************************************************************************" +
                        "\n*\n* \"Test finished with SUCCESS:\" '{}'\n*\n" +
                        "**********************************************************************************************\n",
                demoProjectJira.testName());
        if (reportToTestRail) {
            setTestResultForTestRailReporting(iTestResult, TestResults.SUCCESS);

        }
        if (reportToSlack) {
            sendTestResultSlackMessage("SUCCESS");
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult iTestResult) {
        logger.error("*************** Error '{}' Test has failed ***************\n", demoProjectJira.testName());
        if (reportToTestRail) {
            setTestResultForTestRailReporting(iTestResult, TestResults.FAILURE);

        }
        if (reportToSlack) {
            sendTestResultSlackMessage("FAILURE");
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        logger.info("*************** Skipping '{}' Test ***************\n", demoProjectJira.testName());
        if (reportToTestRail) {
            setTestResultForTestRailReporting(iTestResult, TestResults.SKIPPED);

        }
        if (reportToSlack) {
            sendTestResultSlackMessage("SKIP");
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        if (reportToTestRail) {
            testRail_Manager.addResultForCases(testRunID, testRailTestResults);
            testRail_Manager.closeTestPlan(testPlanID);
            testRailLink = String.format("https://" + TESTRAIL_HOST + "/index.php?/runs/view/%s&group_by=cases:section_id&group_order=asc", testPlanID + 1);
        }
        logger.info("*************** TestRail Report Successfully Generated & Sent to -" + testRailLink + "  ***************");

        sendSlackMessage(SLACK_CHANNEL, "\n**********************************************************" +
                "************************************\n                                                           " +
                "       Finished test run\n" +
                "***********************************************************************************************\n");
    }
}