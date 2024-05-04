package uiManager.pageHelpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import uiManager.pages.LoginPage;

public class LoginPageHelper extends LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPageHelper.class);
    public LoginPageHelper(WebDriver driver, String jiraServerUrl) {
        super(driver, jiraServerUrl);
    }

    public boolean isInLoginPage() {
        return isElementFoundInPage(getLoginBtn());
    }

    public void loginUser(String username, String password) {
        logger.info("Inserting user name '{}' to relevant text field", username);
        sendText(getUsernameField(), username);
        logger.info("Inserting password '{}' to relevant text field", password);
        sendText(getPasswordField(), password);
        clickButton(getLoginBtn(), "Log In");
    }
}