import apiManager.ApiHelper;

public class TestFlows extends BaseTests {

    public TestFlows(ApiHelper apiHelper) {
        super(apiHelper);
    }

    public void testCreateNewProjectAndAssertWithUI() {
        testProject = apiHelper.createNewProject(buildProjectObject());
        threadSleepLog(3, "right after creating a new project");
        softAssert.assertEquals(testProject.getName(), PROJECT_NAME);
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
}