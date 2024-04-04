package reporting.testrail.constants;

public enum AutomationStatus {

    NOT_AUTOMATED("Not Automated"),
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    BLOCKED("Blocked"),
    COMPLETED("Completed"),
    WONT_DO("Won't Do");

    private String name;

    AutomationStatus(String _name) {
        this.name = _name;
    }

    public String getName() {
        return name;
    }
}