package reporting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DemoProject_Jira {
    String[] testRailCaseId() default "";

    String testName() default "";
}
