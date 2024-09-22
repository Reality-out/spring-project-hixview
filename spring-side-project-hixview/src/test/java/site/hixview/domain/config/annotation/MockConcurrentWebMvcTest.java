package site.hixview.domain.config.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import site.hixview.domain.postprocessor.MockServiceBeanFactoryPostProcessor;
import site.hixview.domain.postprocessor.MockValidatorBeanFactoryPostProcessor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest(properties = {"junit.jupiter.execution.parallel.mode.classes.default=concurrent"})
@Import({MockServiceBeanFactoryPostProcessor.class,
        MockValidatorBeanFactoryPostProcessor.class})
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public @interface MockConcurrentWebMvcTest {

}
