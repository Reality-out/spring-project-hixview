package site.hixview.support.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.SpringProjectHixviewApplication;
import site.hixview.support.bean.set.RegisterRepositorySettingConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = SpringProjectHixviewApplication.class)
@Transactional
@RegisterRepositorySettingConfig
public @interface OnlyRealRepositoryContext {

    ResetMode resetMode() default ResetMode.RESET_AUTO_INCREMENT;

    enum ResetMode {
        RESET_AUTO_INCREMENT,
        RESET_TABLE;

        ResetMode() {
        }
    }
}