package site.hixview.support.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.support.bean.RegisterDataSourceConfig;
import site.hixview.support.bean.RegisterJdbcTemplateConfig;
import site.hixview.support.bean.RegisterJpaConfig;
import site.hixview.support.bean.RegisterListenerConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@RegisterDataSourceConfig
@RegisterJdbcTemplateConfig
@RegisterJpaConfig
@RegisterListenerConfig
public @interface OnlyRealRepositoryContext {

    ResetMode resetMode() default ResetMode.RESET_AUTO_INCREMENT;

    enum ResetMode {
        RESET_AUTO_INCREMENT,
        RESET_TABLE;

        ResetMode() {
        }
    }
}