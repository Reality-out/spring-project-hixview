package site.hixview.support.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.hixview.support.bean.RegisterRepository;
import site.hixview.support.bean.config.RegisterDatabaseConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RegisterDatabaseConfig
@RegisterRepository
public @interface OnlyRealRepositoryContext {
}