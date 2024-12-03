package site.hixview.support.bean.set;

import site.hixview.support.bean.RegisterDataSourceConfig;
import site.hixview.support.bean.RegisterJdbcTemplateConfig;
import site.hixview.support.bean.RegisterJpaConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RegisterDataSourceConfig
@RegisterJdbcTemplateConfig
@RegisterJpaConfig
public @interface RegisterRepositorySettingConfig {
}
