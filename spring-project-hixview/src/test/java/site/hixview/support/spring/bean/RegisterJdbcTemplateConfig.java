package site.hixview.support.spring.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.spring.config.TestJdbcTemplateConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestJdbcTemplateConfig.class})
public @interface RegisterJdbcTemplateConfig {
}
