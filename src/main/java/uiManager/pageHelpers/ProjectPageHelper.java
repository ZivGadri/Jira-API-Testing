package uiManager.pageHelpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uiManager.pages.ProjectPage;

public class ProjectPageHelper extends ProjectPage {
    private static final Logger logger = LogManager.getLogger(ProjectPageHelper.class);
    public ProjectPageHelper(WebDriver driver) {
        super(driver);
    }

    public boolean isIssueFoundInProject(String keyAndSummary) {
        for (WebElement issue : getIssuesList()) {
            if (issue.getText().equals(keyAndSummary)) {
                return true;
            }
        }
        return false;
    }

    public void clickOnIssue(String issueId) {
        for (WebElement issue : getIssuesList()) {
            if (issue.getAttribute("data-issue-id").equals(issueId)) {
                clickButton(issue, "Issue ID: " + issueId);
                return;
            }
        }
        logger.error("Could not find the requested issue id: '{}'", issueId);
        throw new RuntimeException("Could not find the requested issue");
    }
}