package apiManager;

public enum RequestBodyTemplates {

    CREATE_PROJECT("" +
            "{\n" +
            "    \"key\": \"TPR\",\n" +
            "    \"name\": \"Test Project\",\n" +
            "    \"projectTypeKey\": \"software\",\n" +
            "    \"lead\": \"admin\",\n" +
            "    \"projectTemplateKey\": \"com.atlassian.jira-core-project-templates:jira-core-simplified-content-management\",\n" +
            "    \"description\": \"This is a test project.\",\n" +
            "    \"url\": \"http://www.example.com\",\n" +
            "    \"assigneeType\": \"PROJECT_LEAD\",\n" +
            "    \"avatarId\": 10100,\n" +
            "    \"permissionScheme\": 10000\n" +
            "}"
    );

    private String bodyTemplate;

    RequestBodyTemplates(String _requestBodyTemplates) {
        this.bodyTemplate = _requestBodyTemplates;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

}

