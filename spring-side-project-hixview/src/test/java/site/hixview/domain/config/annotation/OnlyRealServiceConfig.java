package site.hixview.domain.config.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import site.hixview.domain.config.postprocessor.MockRepositoryBeanFactoryPostProcessor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = RegisterServiceConfig.class, properties = {"junit.jupiter.execution.parallel.mode.classes.default=concurrent"})
@Import({MockRepositoryBeanFactoryPostProcessor.class})
@RegisterMainSettingConfig
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public @interface OnlyRealServiceConfig {
}
