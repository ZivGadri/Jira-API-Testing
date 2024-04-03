import apiManager.models.Comment;

public class TestFlows extends BaseTests {

    public void testCreateNewProjectAndAssertWithUI() {
        testProject = apiHelper.createNewProject(buildProjectObject());
        threadSleepLog(3);
        softAssert.assertEquals(testProject.getName(), PROJECT_NAME);
        softAssert.assertAll();
    }

    public void testCreatingANewIssueAndAssertUsingUI() {
        testIssue = apiHelper.createNewIssue(buildIssueObject(testProject));
        threadSleepLog(3);

    }

    public void testAddingACommentToNewIssue() {
        testComment = apiHelper.AddComment(createCommentObject(testCommentText), testIssue);
        threadSleepLog(3);
    }

    public void testUpdatingAComment() {
        testComment.setBody(updatedTestCommentText);
        Comment updatedTestComment = apiHelper.UpdateComment(testComment, testIssue);
    }
}
