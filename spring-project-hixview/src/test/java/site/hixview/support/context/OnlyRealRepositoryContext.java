package site.hixview.support.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.support.bean.set.RegisterRepositorySettingConfig;
import site.hixview.support.jdbc.callback.TestDynamicResetListener;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@RegisterRepositorySettingConfig
@TestExecutionListeners(
        listeners = TestDynamicResetListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface OnlyRealRepositoryContext {

    ResetMode resetMode() default ResetMode.RESET_AUTO_INCREMENT;

    enum ResetMode {
        RESET_AUTO_INCREMENT,
        RESET_TABLE;

        ResetMode() {
        }
    }
}