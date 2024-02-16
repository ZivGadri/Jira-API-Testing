public class EndPoints {

    public static final String createSession = "/rest/auth/1/session";
    public static final String createProject = "/rest/api/2/project";
    public static final String createIssue = "/rest/api/2/issue";
    public static final String addComment = "/rest/api/2/issue/{issueIdOrKey}/comment";
    public static final String updateComment = "/rest/api/2/issue/{issueIdOrKey}/comment/{id}";
    public static final String deleteComment = "/rest/api/2/issue/{issueIdOrKey}/comment/{id}";
    public static final String deleteIssue = "/rest/api/2/issue/{issueIdOrKey}";
    public static final String deleteProject = "/rest/api/2/project/{projectIdOrKey}";

}
