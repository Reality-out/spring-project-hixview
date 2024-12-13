package site.hixview.support.spring.bean.set;

import site.hixview.support.spring.bean.RegisterDataSourceConfig;
import site.hixview.support.spring.bean.RegisterJdbcTemplateConfig;
import site.hixview.support.spring.bean.RegisterJpaConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RegisterDataSourceConfig
@RegisterJdbcTemplateConfig
@RegisterJpaConfig
public @interface RegisterRepositorySettingConfig {
}
