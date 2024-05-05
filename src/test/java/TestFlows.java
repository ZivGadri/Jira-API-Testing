import apiManager.models.Issue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import uiManager.models.ProjectTypes;

public class TestFlows extends BaseTests {
    private static final Logger logger = LogManager.getLogger(TestFlows.class);
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    public void testCreateNewProjectAndAssertWithUI() {
        testProject = buildProjectObject();
        String projectId = apiHelper.createProjectUsingCurl(buildProjectObject());
        logger.info("Project ID retrieved: {}", projectId);
        testProject.setId(projectId);
        threadSleepLog(3, "right after creating a new project and then starting UI verification...");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        Assert.assertTrue(uiManager.isProjectFoundInPage(testProject.getName()));
    }

    public void testCreatingANewIssueAndAssertUsingUI() {
        Issue testIssueObject = buildIssueObject(testProject);
        issueSummary = testIssueObject.getFields().getSummary();
        testIssue = apiHelper.createNewIssue(testIssueObject);
        threadSleepLog(3, "right after creating a new issue and then starting UI verification...");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        Assert.assertTrue(uiManager.isIssueFoundInProject(testIssue.getKey() + " " + issueSummary));
    }

    public void testAddingCommentToNewIssue() {
        testComment = apiHelper.addComment(createCommentObject(testCommentText), testIssue);
        threadSleepLog(3, "right after adding a comment and then starting UI verification...");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        Assert.assertTrue(uiManager.isCommentFoundInIssue(testComment.getBody()));
    }

    public void testUpdatingCommentInIssue() {
        testComment.setBody(updatedTestCommentText);
        testComment = apiHelper.updateComment(testComment, testIssue);
        threadSleepLog(3, "right after updating a comment and then starting UI verification...");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        Assert.assertTrue(uiManager.isCommentFoundInIssue(testComment.getBody()));
    }

    public void testDeletingACommentFromIssue() {
        apiHelper.deleteComment(testComment, testIssue);
        threadSleepLog(3, "right after deleting a comment and then starting UI verification...");
        softAssert.assertEquals(getNumberOfCommentsForIssue(testIssue), 0);
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        softAssert.assertTrue(uiManager.isCommentDeleted());
    }

    public void testDeleteIssueFromProject() {
        apiHelper.deleteIssue(testIssue);
        threadSleepLog(3, "right after deleting an issue and then starting UI verification...");
        softAssert.assertEquals(
                apiHelper.getDeletedIssueGetResponseStatusCode(testIssue),
                404,
                "Status code for getting the deleted issue was not as expected");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        softAssert.assertFalse(uiManager.isIssueFoundInProject(testIssue.getKey() + " " + issueSummary));
    }

    public void testDeleteProjectFromWorkspace() {
        apiHelper.deleteProject(testProject);
        threadSleepLog(3, "right after deleting a project and then starting UI verification...");
        softAssert.assertEquals(
                apiHelper.getDeletedProjectGetResponseStatusCode(testProject),
                404,
                "Status code for getting the deleted project was not as expected");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        softAssert.assertEquals(uiManager.getNumberOfProjectsInList(), 0, "The number of projects in the list was not zero");
    }
}