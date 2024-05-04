import apiManager.models.Issue;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import uiManager.models.ProjectTypes;

public class TestFlows extends BaseTests {

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    public void testCreateNewProjectAndAssertWithUI() {
        testProject = buildProjectObject();
        String projectId = apiHelper.createProjectUsingCurl(buildProjectObject());
        testProject.setId(projectId);
        threadSleepLog(3, "right after creating a new project");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        Assert.assertTrue(uiManager.isProjectFoundInPage(testProject.getName()));
    }

    public void testCreatingANewIssueAndAssertUsingUI() {
        Issue testIssueObject = buildIssueObject(testProject);
        issueSummary = testIssueObject.getFields().getSummary();
        testIssue = apiHelper.createNewIssue(testIssueObject);
        threadSleepLog(3, "right after creating a new issue");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        Assert.assertTrue(uiManager.isIssueFoundInProject(testIssue.getKey() + " " + issueSummary));
    }

    public void testAddingCommentToNewIssue() {
        testComment = apiHelper.addComment(createCommentObject(testCommentText), testIssue);
        threadSleepLog(3, "right after adding a comment");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        Assert.assertTrue(uiManager.isCommentFoundInIssue(testComment.getBody()));
    }

    public void testUpdatingCommentInIssue() {
        testComment.setBody(updatedTestCommentText);
        testComment = apiHelper.updateComment(testComment, testIssue);
        threadSleepLog(3, "right after updating a comment");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        Assert.assertTrue(uiManager.isCommentFoundInIssue(testComment.getBody()));
    }

    public void testDeletingACommentFromIssue() {
        apiHelper.deleteComment(testComment, testIssue);
        threadSleepLog(3, "right after deleting a comment");
        softAssert.assertEquals(getNumberOfCommentsForIssue(testIssue), 0);
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        uiManager.navigateToSoftwareProject(testProject.getName());
        uiManager.navigateToProjectIssue(testIssue.getId());
        softAssert.assertTrue(uiManager.isCommentDeleted());
    }

    public void testDeleteIssueFromProject() {
        apiHelper.deleteIssue(testIssue);
        threadSleepLog(3, "right after deleting an issue");
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
        threadSleepLog(3, "right after deleting a project");
        uiManager.navigateToProjects(ProjectTypes.SOFTWARE);
        Assert.assertEquals(uiManager.getNumberOfProjectsInList(), 0, "The number of projects in the list was not zero");
    }
}