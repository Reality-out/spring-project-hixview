package site.hixview.support.context;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import site.hixview.support.postprocessor.MockServiceBeanFactoryPostProcessor;
import site.hixview.support.postprocessor.MockValidatorBeanFactoryPostProcessor;
import site.hixview.support.bean.RegisterAppAndValidation;
import site.hixview.support.property.TestSchemaName;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest
@Import({MockServiceBeanFactoryPostProcessor.class, MockValidatorBeanFactoryPostProcessor.class})
@RegisterAppAndValidation
@AutoConfigureMockMvc
@TestSchemaName
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public @interface OnlyRealControllerContext {
}
