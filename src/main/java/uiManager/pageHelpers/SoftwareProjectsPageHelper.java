package uiManager.pageHelpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uiManager.pages.SoftwareProjectsPage;

public class SoftwareProjectsPageHelper extends SoftwareProjectsPage {
    private static final Logger logger = LogManager.getLogger(SoftwareProjectsPageHelper.class);
    public SoftwareProjectsPageHelper(WebDriver driver) {
        super(driver);
    }

    public boolean isProjectFoundInList(String projectName) {
        for (WebElement projectRow : getSoftwareProjects()) {
            if (findElementByParentElement(projectRow, By.xpath("./td/a")).getText().equals(projectName)) {
                return true;
            }
        }
        return false;
    }

    public void clickOnProject(String projectName) {
        for (WebElement projectRow : getSoftwareProjects()) {
            if (findElementByParentElement(projectRow, By.xpath("./td/a")).getText().equals(projectName)) {
                clickButton(findElementByParentElement(projectRow, By.xpath("./td/a")), projectName);
                waitForPageLoad();
                clickButton(getBacklogSideMenu(), "Backlog");
                return;
            }
        }
        logger.error("Could not find the requested project: '{}'", projectName);
        throw new RuntimeException("Could not find the requested project");
    }

    public int getSizeOfProjectList() {
        return getSoftwareProjects().size();
    }
}