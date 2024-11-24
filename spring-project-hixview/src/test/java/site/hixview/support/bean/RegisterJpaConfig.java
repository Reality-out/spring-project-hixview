package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.config.TestJpaConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestJpaConfig.class})
public @interface RegisterJpaConfig {
}
