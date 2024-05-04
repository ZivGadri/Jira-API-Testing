package uiManager.pageHelpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uiManager.models.MainUpperMenus;
import uiManager.models.SubMenuItems;
import uiManager.pages.MainPage;

public class MainPageHelper extends MainPage {
    private static final Logger logger = LogManager.getLogger(MainPageHelper.class);
    public MainPageHelper(WebDriver driver) {
        super(driver);
    }

    public void navigateToSoftwareProjects() {
        clickOnMenu(MainUpperMenus.PROJECTS);
        clickOnSubMenuItem(SubMenuItems.Projects.SOFTWARE.getProjectSubMenu());
    }

    public void navigateToBusinessProjects() {
        clickOnMenu(MainUpperMenus.PROJECTS);
        clickOnSubMenuItem(SubMenuItems.Projects.BUSINESS.getProjectSubMenu());
    }

    private void clickOnMenu(MainUpperMenus menu) {
        for (WebElement element : getUpperMenus()) {
            String currentMenu = element.getText();
            if (currentMenu.equalsIgnoreCase(menu.toString())) {
                clickButton(element, currentMenu);
                break;
            }
            logger.error("Upper menu '{}' requested was not found", menu);
            throw new RuntimeException("Upper menu requested was not found");
        }
    }

    private void clickOnSubMenuItem(String subMenuItem) {
        WebElement elementToClick = null;
        if (subMenuItem.equals(SubMenuItems.Projects.SOFTWARE.getProjectSubMenu())) {
            elementToClick = getSoftwareProjectsSubMenuBtn();
        } else if (subMenuItem.equals(SubMenuItems.Projects.BUSINESS.getProjectSubMenu())) {
            elementToClick = getBusinessProjectsSubMenuBtn();
        } else if (subMenuItem.equals(SubMenuItems.Dashboards.MANAGE_DASHBOARDS.getDashboardSubMenu())) {
            elementToClick = getManageDashboardsSubMenuBtn();
        } // And so on for all the usable submenus...

        try {
            clickButton(elementToClick, subMenuItem);
        } catch (StaleElementReferenceException | NoSuchElementException | NullPointerException e) {
            logger.error("Could not click on te submenu provided: '{}'", subMenuItem);
            throw new RuntimeException();
        }
    }


}
