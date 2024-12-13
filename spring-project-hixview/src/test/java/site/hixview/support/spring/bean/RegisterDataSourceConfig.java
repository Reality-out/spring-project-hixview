package site.hixview.support.spring.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.spring.config.TestDataSourceConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestDataSourceConfig.class})
public @interface RegisterDataSourceConfig {
}
