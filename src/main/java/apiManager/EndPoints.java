package apiManager;

public class EndPoints {

    public static final String CREATE_SESSION = "/rest/auth/1/session";
    public static final String CREATE_PROJECT = "/rest/api/2/project";
    public static final String CREATE_ISSUE = "/rest/api/2/issue";
    public static final String ADD_COMMENT = "/rest/api/2/issue/%s/comment";
    public static final String GET_ALL_COMMENTS = "/rest/api/2/issue/%s/comment";
    public static final String UPDATE_COMMENT = "/rest/api/2/issue/%s/comment/%s";
    public static final String DELETE_COMMENT = "/rest/api/2/issue/%s/comment/%s";
    public static final String DELETE_ISSUE = "/rest/api/2/issue/%s";
    public static final String GET_ISSUE = "/rest/api/2/issue/%s";
    public static final String DELETE_PROJECT = "/rest/api/2/project/%s";
    public static final String GET_PROJECT = "/rest/api/2/project/%s";

}
