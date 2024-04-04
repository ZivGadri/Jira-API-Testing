package reporting;

public enum SlackChannel {

    TEST_SLACK_CHANNEL_NAME("THE WEBHOOK CODE IS INSERTED HERE (AFTER 'services/')");

    private String code;

    SlackChannel(String code) {
        setCode(code);
    }

    public String getCode() {
        return  code;
    }

    private void setCode(String code) {
        this.code = code;
    }

}
