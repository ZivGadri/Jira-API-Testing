package uiManager.pageHelpers;

import org.openqa.selenium.WebDriver;
import uiManager.pages.IssuePage;

public class IssuePageHelper extends IssuePage {
    public IssuePageHelper(WebDriver driver) {
        super(driver);
    }

    public String getCommentFromIssue() {
        return getIssueComment().getText();
    }

    public boolean isCommentFoundInIssue() {
        return isElementFoundInPage(getIssueComment());
    }
}
