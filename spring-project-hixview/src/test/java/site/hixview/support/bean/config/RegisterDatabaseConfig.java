package site.hixview.support.bean.config;

import org.springframework.context.annotation.Import;
import site.hixview.aggregate.config.DatabaseConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DatabaseConfig.class})
public @interface RegisterDatabaseConfig {
}
