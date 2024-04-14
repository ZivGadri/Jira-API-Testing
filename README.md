## Jira-API-Testing
This project is a short and succinct demonstration of API testing Framework. This project uses Rest Assured for data manipulation, 
and, for verifications, along with the actual Rest Assured response verification, I used TestNG and Selenium frameworks for UI assertions.

The project contains several tests running in a predefined order, where initially an API request is sent for creating, updating, 
or deleting a specific resource (data entity). Then, a UI process will take place in order to verify the successful API 
request is visually presented to the users.

### Reporting:
* **TestRail** - In this project, I have decided to demonstrate an infrastructure of test runs being reported to TestRail via its [APIs][testrail api].
It makes a use of a customised annotation (_"@DemoProject_Jira"_) that can take a testrail case ID(s) as a String - _String[] testRailCaseId()_ and 
report its test run result, it's also able to take another test name variable - _String testName()_ for an easier and more readable reference inside 
the actual TestRail report. For this feature to work, you'll need to fill in the corresponding parameters over the _JiraTesting.properties_ file, and
provide the TestRail host and credentials, as well as the project name of which the tests are found in, and set the _REPORT_TO_TESTRAIL_ parameter to **true**.  


* **Slack** - As well as reporting to TestRail, This infrastructure demonstrates an optional reports for a selected Slack channel by 
[Sending messages using incoming webhooks][slack webhook].
This process must use a Slack app (for tests reporting) to be installed over the Slack's workspace (Explanation of [how to build a Slack app][slack app]).
You will need to set the _REPORT_TO_SLACK_ parameter to **true** and provide the channel's name inside the properties file, as well as set up the channel's 
name and the actual webhook URL code (after '...services/') in the **SlackChannel** class (_src/test/java/reporting/SlackChannel.java_). 


### Tracking and monitoring:
A minor addition in this infrastructure is the ability to easily connect the web driver to Saucelabs service for a flexible use of a variety of browsers,
and be able to watch the test recordings over the web. You will obviously need a saucelabs username and key here, and provide those under the corresponding parameters in the _JiraTesting.properties_ file.
Also, the _IS_BROWSER_LOCAL_ property in the _JiraTesting.properties_ file must be **false** in order to run over SauceLabs services.  
###### NOTE - This project is for demo purposes, and since it only runs locally over Jira server platform it cannot run from external services such as SauceLabs.  

### Prerequisites:
* Since this project is based on [JIRA Server platform APIs][jira server platform api], you will need to download and install 
a local jira server from this [**link**](https://www.atlassian.com/software/jira/update). Use default settings. Make sure you keep the server's port on 8080. If changed, you'll need to update the 
_JIRA_BASE_URL_ parameter in **ApiHelper** class accordingly.  
When finished, check the "**Launch Jira Software in browser**" and click finish. Wait a couple of minutes until the server finishes initialization process.  
After server started, refresh the browser. Click on "**Set it up for me**" (recommended) and then "**Continue to MyAtlassian**".  
Sign up for an account (trial account is good enough) if non exists.  
Type in an organization name and generate a license for the "**Jira Software (Data Center)**".  
Confirm license key installation over the localhost server.  
Fill in the Email, Username and Password in **Administrator account setup** and click **Next** 
###### NOTE - You will need to add these Username and Password values under Jira credentials in the _JiraTesting.properties_ file

* Java (version 8 or above) is needed. You can download it from this [link](https://www.oracle.com/il-en/java/technologies/downloads/).
* You'll need Maven. Download it from this [link](https://maven.apache.org/download.cgi).

### References
* JIRA server platform REST API reference - [link][jira server platform api]
* JIRA server platform cookie based authentication- [link][cookie-based auth]

[jira server platform api]: https://docs.atlassian.com/software/jira/docs/api/REST/7.6.1/
[cookie-based auth]: https://developer.atlassian.com/server/jira/platform/cookie-based-authentication/
[testrail api]: https://support.testrail.com/hc/en-us/categories/7076541806228-API-Manual
[slack webhook]: https://api.slack.com/messaging/webhooks
[slack app]: https://support.testrail.com/hc/en-us/categories/7076541806228-API-Manual