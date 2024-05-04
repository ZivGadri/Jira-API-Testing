package uiManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import uiManager.models.ProjectTypes;
import uiManager.pageHelpers.*;

public class UI_Manager {
    private static final Logger logger = LogManager.getLogger(UI_Manager.class);
    private WebDriver driver;
    private String username;
    private String password;
    private String jiraUrl;
    private LoginPageHelper loginPageHelper;
    private MainPageHelper mainPageHelper;
    private SoftwareProjectsPageHelper softwareProjectsPageHelper;
    private ProjectPageHelper projectPageHelper;
    private IssuePageHelper issuePageHelper;
    public UI_Manager(WebDriver driver, String username, String password, String jiraUrl) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
    }

    // Navigation methods
    public void navigateToJiraServerMainPage() {
        loginPageHelper = new LoginPageHelper(driver, jiraUrl);
        if (loginPageHelper.isInLoginPage()) {
            loginPageHelper.loginUser(username, password);
        }
        initMainPage();
    }

    public void navigateToProjects(ProjectTypes type) {
        if (type.equals(ProjectTypes.SOFTWARE)) {
            mainPageHelper.navigateToSoftwareProjects();
            initSoftwareProjectsPage();
        } else {
            mainPageHelper.navigateToBusinessProjects();
            // Initializing business projects page here - No need for this demo project
        }
    }

    public void navigateToSoftwareProject(String projectName) {
        softwareProjectsPageHelper.clickOnProject(projectName);
        initProjectPage();
    }

    public void navigateToProjectIssue(String issueId) {
        projectPageHelper.clickOnIssue(issueId);
        initIssuePage();
    }

    // Verification methods
    public boolean isProjectFoundInPage(String projectName) {
        return softwareProjectsPageHelper.isProjectFoundInList(projectName);
    }

    public boolean isIssueFoundInProject(String keyAndSummary) {
        return projectPageHelper.isIssueFoundInProject(keyAndSummary);
    }

    public boolean isCommentFoundInIssue(String comment) {
        return issuePageHelper.getCommentFromIssue().equals(comment);
    }

    public boolean isCommentDeleted() {
        return !issuePageHelper.isCommentFoundInIssue();
    }

    public int getNumberOfProjectsInList() {
        return softwareProjectsPageHelper.getSizeOfProjectList();
    }

    // Pages initializers
    private void initMainPage() {
        mainPageHelper = new MainPageHelper(driver);
    }
    private void initSoftwareProjectsPage() {
        softwareProjectsPageHelper = new SoftwareProjectsPageHelper(driver);
    }
    private void initProjectPage() {
        projectPageHelper = new ProjectPageHelper(driver);
    }
    private void initIssuePage() {
        issuePageHelper = new IssuePageHelper(driver);
    }
}