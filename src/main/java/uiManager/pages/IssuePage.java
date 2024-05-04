package uiManager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class IssuePage extends PageObject {
    @FindBy(id = "DETAILS-details-module-nav")
    private WebElement issueInfoBtn;

    @FindBy(xpath = "//div[@class='action-body flooded']")
    private WebElement issueComment;

    public IssuePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public WebElement getIssueInfoBtn() {
        return issueInfoBtn;
    }

    public WebElement getIssueComment() {
        return issueComment;
    }
}