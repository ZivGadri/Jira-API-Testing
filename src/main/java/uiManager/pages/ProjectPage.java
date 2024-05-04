package uiManager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProjectPage extends PageObject{

    @FindAll({@FindBy(xpath = "//div[contains(@class,'js-issue-list')]/div[contains(@class,'js-issue')]")})
    private List<WebElement> issuesList;

    public ProjectPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getIssuesList() {
        return issuesList;
    }
}