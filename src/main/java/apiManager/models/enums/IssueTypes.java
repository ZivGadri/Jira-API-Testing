package apiManager.models.enums;

public enum IssueTypes {
    BUG("Bug"),
    TASK("Task"),
    STORY("Story"),
    EPIC("Epic");

    private String type;

    IssueTypes(String type) {
        this.type = type;
    }

    public String getIssueType() {
        return type;
    }
}
