package site.hixview.support.jpa.context;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.SpringProjectHixviewApplication;
import site.hixview.support.spring.bean.set.RegisterRepositorySettingConfig;

import java.lang.annotation.*;

import static site.hixview.aggregate.vo.Reference.JPA_REPOSITORY_REFERENCE;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = SpringProjectHixviewApplication.class)
@EnableJpaRepositories(basePackages = JPA_REPOSITORY_REFERENCE)
@Transactional
@RegisterRepositorySettingConfig
public @interface OnlyRealRepositoryContext {
}