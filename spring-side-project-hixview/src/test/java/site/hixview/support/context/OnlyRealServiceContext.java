package site.hixview.support.context;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import site.hixview.support.postprocessor.MockRepositoryBeanFactoryPostProcessor;
import site.hixview.support.scan.ScanService;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = ScanService.class)
@Import({MockRepositoryBeanFactoryPostProcessor.class})
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public @interface OnlyRealServiceContext {
}
