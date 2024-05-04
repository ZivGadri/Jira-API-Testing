package uiManager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage extends PageObject {

    @FindAll({@FindBy(xpath = "//div[contains(@class,'primary')]//ul[@class='aui-nav']/li")})
    private List<WebElement> upperMenus;

    @FindBy(id = "project_type_software")
    private WebElement softwareProjectsSubMenuBtn;

    @FindBy(id = "project_type_business")
    private WebElement businessProjectsSubMenuBtn;


    @FindBy(id = "manage_dash_link")
    private WebElement manageDashboardsSubMenuBtn;

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, MainPage.class);
    }

    public List<WebElement> getUpperMenus() {
        return upperMenus;
    }

    public WebElement getSoftwareProjectsSubMenuBtn() {
        return softwareProjectsSubMenuBtn;
    }

    public WebElement getBusinessProjectsSubMenuBtn() {
        return businessProjectsSubMenuBtn;
    }

    public WebElement getManageDashboardsSubMenuBtn() {
        return manageDashboardsSubMenuBtn;
    }

}
