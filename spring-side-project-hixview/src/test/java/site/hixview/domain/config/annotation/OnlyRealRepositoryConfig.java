package site.hixview.domain.config.annotation;

import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = ScanRepositoryConfig.class)
@RegisterDatabaseConfig
public @interface OnlyRealRepositoryConfig {
}
