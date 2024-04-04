package reporting.testrail.constants;

import java.text.MessageFormat;

public enum TestRailEndPoints {

    GET_PROJECTS("get_projects"),
    GET_PROJECT("get_project/{0}"),
    GET_PLANS("get_plans/{0}"),
    GET_PLANS_IS_COMPLETE("get_plans/{0}&is_completed={1}"),
    ADD_PLAN("add_plan/{0}"),
    GET_PLAN("get_plan/{0}"),
    GET_CASE("get_case/{0}"),
    GET_CASE_FIELDS("get_case_fields"),
    GET_CASES("get_cases/{0}&suite_id={1}"),
    GET_CASES_SUITE_ID_SECTION_ID("get_cases/{0}&suite_id={1}&section_id={2}"),
    GET_SECTIONS_PROJECT_ID_SUITE_ID("get_sections/{0}&suite_id={1}"),
    GET_SECTION("get_section/{0}"),
    UPDATE_CASE("update_case/{0}"),
    GET_SUITE("get_suite/{0}"),
    GET_SUITES("get_suites/{0}"),
    UPDATE_PLAN("update_plan/{0}"),
    ADD_PLAN_ENTRY("add_plan_entry/{0}"),
    UPDATE_PLAN_ENTRY("update_plan_entry/{0}/{1}"),
    CLOSE_RUN("close_run/{0}"),
    CLOSE_PLAN("close_plan/{0}"),
    GET_TESTS("get_tests/{0}"),
    ADD_RESULTS_FOR_CASES("add_results_for_cases/{0}"),
    ADD_RESULTS_FOR_CASE("add_result_for_case/{0}/{1}");

    TestRailEndPoints(String _path) {
        path = _path;
    }

    private String path;

    public String uri() {
        return SystemProperties.HTTPS + SystemProperties.testRailHost +SystemProperties.testRailHostUrl +SystemProperties.testRailHostRoot + path;
    }

    public String format(Object... params) {
        int i =0;
        Object[] tempParams = new Object[params.length];
        for (Object b:params) {
            tempParams[i++] = String.valueOf(b);
        }
        return MessageFormat.format(uri(), tempParams);
    }
}
