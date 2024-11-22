package site.hixview.support.bean.config;

import org.springframework.context.annotation.Import;
import site.hixview.aggregate.config.JpaConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({JpaConfig.class})
public @interface RegisterJpaConfig {
}
