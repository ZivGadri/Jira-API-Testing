import org.testng.annotations.BeforeClass;

public class TestFlows extends BaseTests {

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    public void testCreateNewProjectAndAssertWithUI() {
        testProject = apiHelper.createNewProject(buildProjectObject());
        threadSleepLog(3, "right after creating a new project");
        softAssert.assertEquals(testProject.getName(), JIRA_PROJECT_NAME);
        softAssert.assertAll();
    }

    public void testCreatingANewIssueAndAssertUsingUI() {
        testIssue = apiHelper.createNewIssue(buildIssueObject(testProject));
        threadSleepLog(3, "right after creating a new issue");

    }

    public void testAddingACommentToNewIssue() {
        testComment = apiHelper.addComment(createCommentObject(testCommentText), testIssue);
        threadSleepLog(3, "right after adding a comment");
    }

    public void testUpdatingAComment() {
        testComment.setBody(updatedTestCommentText);
        testComment = apiHelper.updateComment(testComment, testIssue);
        threadSleepLog(3, "right after updating a comment");
    }

    public void testDeletingACommentFromIssue() {
        apiHelper.deleteComment(testComment, testIssue);
        threadSleepLog(3, "right after deleting a comment");
        softAssert.assertEquals(getNumberOfCommentsForIssue(testIssue), 0);
    }

    public void testDeleteIssueFromProject() {
        apiHelper.deleteIssue(testIssue);
        threadSleepLog(3, "right after deleting an issue");
        softAssert.assertEquals(
                apiHelper.getDeletedIssueGetResponseStatusCode(testIssue),
                404,
                "Status code for getting the deleted issue was not as expected");
    }

    public void testDeleteProjectFromWorkspace() {
        apiHelper.deleteProject(testProject);
        threadSleepLog(3, "right after deleting a project");
        softAssert.assertEquals(
                apiHelper.getDeletedIssueGetResponseStatusCode(testIssue),
                404,
                "Status code for getting the deleted project was not as expected");
    }
}