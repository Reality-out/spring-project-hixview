package site.hixview.support.context;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import site.hixview.support.bean.RegisterAppAndValidation;
import site.hixview.support.postprocessor.MockServiceBeanFactoryPostProcessor;
import site.hixview.support.scan.ScanController;
import site.hixview.support.scan.ScanValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = {ScanController.class, ScanValidator.class})
@Import({MockServiceBeanFactoryPostProcessor.class})
@RegisterAppAndValidation
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public @interface RealControllerAndValidatorContext {
}
