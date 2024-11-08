package site.hixview.support.context;

import org.springframework.boot.test.context.SpringBootTest;
import site.hixview.support.bean.RegisterDatabaseWeb;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.scan.ScanRepository;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = ScanRepository.class)
@TestSchemaName
@RegisterDatabaseWeb
public @interface OnlyRealRepositoryContext {
}
