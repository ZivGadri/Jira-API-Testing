package uiManager.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends PageObject {

    @FindBy(id = "login-form-username")
    private WebElement usernameField;

    @FindBy(id = "login-form-password")
    private WebElement passwordField;

    @FindBy(id = "login")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver, String jiraUrl) {
        super(driver);
        navigateToUrl(jiraUrl);
        PageFactory.initElements(driver, LoginPage.class);
    }

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, LoginPage.class);
    }

    public WebElement getUsernameField() {
        return usernameField;
    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public WebElement getLoginBtn() {
        return loginBtn;
    }
}
