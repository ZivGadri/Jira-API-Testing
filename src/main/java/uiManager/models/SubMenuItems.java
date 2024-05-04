package uiManager.models;

public class SubMenuItems {

    public enum Dashboards {
        VIEW_SYSTEM_DASHBOARD("View System Dashboard"),
        MANAGE_DASHBOARDS("Manage Dashboards");

        private String dashboardSubMenu;

        Dashboards(String dashboardSubMenu) {
            this.dashboardSubMenu = dashboardSubMenu;
        }

        public String getDashboardSubMenu() {
            return this.dashboardSubMenu;
        }
    }

    public enum Projects {
        SOFTWARE("Software"),
        BUSINESS("Business"),
        IMPORT_EXTERNAL_PROJECTS("Import External Project"),
        CREATE_PROJECT("Create Project");

        private String projectSubMenu;

        Projects(String projectSubMenu) {
            this.projectSubMenu = projectSubMenu;
        }

        public String getProjectSubMenu() {
            return this.projectSubMenu;
        }
    }
}