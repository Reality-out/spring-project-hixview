package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.domain.config.AppConfig;
import site.hixview.domain.config.ValidationConfig;
import site.hixview.domain.config.WebConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AppConfig.class,
        ValidationConfig.class,
        WebConfig.class})
public @interface RegisterAppValidationWeb {
}
