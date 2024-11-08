package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.domain.config.DatabaseConfig;
import site.hixview.domain.config.WebConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DatabaseConfig.class,
        WebConfig.class})
public @interface RegisterDatabaseWeb {
}