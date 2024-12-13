package site.hixview.support.spring.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.spring.config.TestJpaConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestJpaConfig.class})
public @interface RegisterJpaConfig {
}
