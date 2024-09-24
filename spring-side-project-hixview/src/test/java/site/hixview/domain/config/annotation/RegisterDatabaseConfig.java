package site.hixview.domain.config.annotation;

import org.springframework.context.annotation.Import;
import site.hixview.domain.config.DatabaseConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DatabaseConfig.class})
@interface RegisterDatabaseConfig {
}
