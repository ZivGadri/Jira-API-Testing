package reporting.testrail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.util.Strings;
import reporting.testrail.constants.SystemProperties;
import reporting.testrail.entities.TestRailTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestRailMapper {

    private static final Logger logger = LogManager.getLogger(TestRailMapper.class);
    private HashMap<String, Integer> _testRailSuiteParamsMap = new HashMap<>();
    private HashMap<String, Integer> _testRailAutomationStatusParamsMap = new HashMap<>();

    /**
     * if there are suite names params in the current test run, this method will return them as list of strings.
     *
     * @return list of string
     * @see SystemProperties#testRailCustomSuiteNames
     */
    public List<String> getSuiteParamsAsListStrings() {
        try {
            List<String> _incomingSuiteParams = new ArrayList<>();
            String[] prms = SystemProperties.testRailCustomSuiteNames.trim().split(",");
            if (prms.length > 0) {
                if (!Strings.isNullOrEmpty(prms[0]))
                    _incomingSuiteParams = Arrays.asList(prms);
            }
            return _incomingSuiteParams;
        } catch (Exception e){
            logger.error("Failed to get suite params as Strings' list ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method handle the mapping of the custom suite name field under TestRail case fields
     *
     * @param testRailSuiteListParams {@link TestRail_Manager#getCaseFieldByName(String)}
     * @return {@code HashMap<Name,ID>}
     */
    public HashMap<String, Integer> mapSuiteParams(List<String> testRailSuiteListParams) {
        try {
            testRailSuiteListParams.stream().forEach(name -> {
                String[] split = name.split(",");
                if (split.length == 2) {
                    _testRailSuiteParamsMap.put(split[1].toUpperCase().trim(), Integer.parseInt(split[0]));
                } else {
                    logger.warn("Could not split properly specific item of custom TestRail suite names ('" + name + "')");
                }
            });
            return _testRailSuiteParamsMap;
        } catch (Exception e){
            logger.error("Failed to map suites list", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method handle the mapping of the custom automation status field under TestRail case fields
     *
     * @param testRailAutomationStatusParams {@link TestRail_Manager#getCaseFieldByName(String)}
     * @return {@code HashMap<String,Integer>} with the Automation status name and id
     */
    public HashMap<String, Integer> mapAutomationStatus(List<String> testRailAutomationStatusParams) {
        try {
            testRailAutomationStatusParams.stream().forEach(name -> {
                String[] split = name.split(",");
                if (split.length == 2) {
                    _testRailAutomationStatusParamsMap.put(split[1].toUpperCase().trim(), Integer.parseInt(split[0]));
                } else {
                    logger.warn("Could not split properly specific item of custom TestRail automation status ('" + name + "')");
                }
            });
            return _testRailAutomationStatusParamsMap;
        } catch (Exception e){
            logger.error("Failed to map automation status list", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method mapping the test cases per sections
     *
     * @param finalTestCases {@code HashMap<TestRailSuiteId, List<TestRailTestCases>>}
     * @param testCases All testCases under specific SuiteId}
     * @param currentSuiteName Current Suite name
     * @return {@code HashMap<TestRail_SectionId, List<TestRailTestCase>>}
     */
    public HashMap<Integer, List<TestRailTestCase>> mapTestCases(HashMap<Integer, List<TestRailTestCase>> finalTestCases, List<TestRailTestCase> testCases, String currentSuiteName) {
        try {
            testCases.stream().forEach(tc -> {
                if (tc.getCustom_suiteslist().contains(_testRailSuiteParamsMap.get(currentSuiteName))) {
                    List<TestRailTestCase> innerList = new ArrayList<>();

                    //if exists
                    if (finalTestCases.containsKey(tc.getSection_id())) {
                        innerList = finalTestCases.get(tc.getSection_id());
                    }
                    innerList.add(tc);
                    finalTestCases.put(tc.getSection_id(), innerList);
                }
            });
            return finalTestCases;
        } catch (Exception e){
            logger.error("Failed to map test cases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the id of the suite params that the user send as input
     *
     * @param testRailParamsMap SuitesNames in TestRail
     * @param incomingParams User params -DsuiteNames=...
     * @return matched {@code HashMap<SuiteName, ID>} or empty if no suites found.
     */
    public HashMap<String, Integer> findMatchSuiteParams(HashMap<String, Integer> testRailParamsMap, List<String> incomingParams) {
        try {
            HashMap<String, Integer> temp = new HashMap<>();
            if (incomingParams.size() > 0) {
                incomingParams.stream().forEach(p -> {
                    if (testRailParamsMap.containsKey(p.toUpperCase())) {
                        temp.put(p.toUpperCase(), testRailParamsMap.get(p.toUpperCase()));
                    }
                });
            }
            return temp;
        } catch (Exception e) {
            logger.error("Failed to find requested suite params", e);
            throw new RuntimeException(e);
        }
    }
}
