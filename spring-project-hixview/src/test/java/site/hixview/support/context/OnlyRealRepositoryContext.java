package site.hixview.support.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import site.hixview.support.bean.RegisterDataSourceConfig;
import site.hixview.support.bean.RegisterJdbcTemplateConfig;
import site.hixview.support.bean.RegisterJpaConfig;
import site.hixview.support.jdbc.callback.TestResetAutoIncrementListener;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RegisterDataSourceConfig
@RegisterJdbcTemplateConfig
@RegisterJpaConfig
@TestExecutionListeners(
        listeners = TestResetAutoIncrementListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface OnlyRealRepositoryContext {
    /**
     * Names of the tables that must be reset when all the test methods have finished running
     */
    Class[] resetTables() default {};
}