package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.config.TestDataSourceConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestDataSourceConfig.class})
public @interface RegisterDataSourceConfig {
}
