package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.config.TestJdbcTemplateConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestJdbcTemplateConfig.class})
public @interface RegisterJdbcTemplateConfig {
}
