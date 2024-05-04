package uiManager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SoftwareProjectsPage extends PageObject {

    @FindAll({@FindBy(xpath = "//tbody[@class='projects-list']/tr")})
    private List<WebElement> softwareProjects;


    public SoftwareProjectsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, SoftwareProjectsPage.class);
    }

    public List<WebElement> getSoftwareProjects() {
        return softwareProjects;
    }
}