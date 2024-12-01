package site.hixview.support.bean;

import org.springframework.context.annotation.Import;
import site.hixview.support.aspect.TestResetListenerAspect;
import site.hixview.support.config.TestListenerConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TestListenerConfig.class, TestResetListenerAspect.class})
public @interface RegisterListenerConfig {
}
