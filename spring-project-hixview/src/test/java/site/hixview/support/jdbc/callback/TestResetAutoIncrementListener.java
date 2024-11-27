package site.hixview.support.jdbc.callback;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.util.ClassUtils;
import site.hixview.aggregate.error.NotExpectedClassArgumentException;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static site.hixview.aggregate.vo.ExceptionMessage.NOT_ENTITY_CLASS;
import static site.hixview.aggregate.vo.Reference.JPA_REPOSITORY_REFERENCE;

public class TestResetAutoIncrementListener implements TestExecutionListener, ObjectTestUtils {

    private static final Logger log = LoggerFactory.getLogger(TestResetAutoIncrementListener.class);

    @Override
    public void afterTestClass(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        for (String schemaName : getGeneratedIdClassList().stream().map(this::getTestSchemaNameFromEntity).toList()) {
             jdbcTemplate.execute("TRUNCATE TABLE " + schemaName);
             jdbcTemplate.execute("ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1");
        }
    }

    private List<Class<?>> getGeneratedIdClassList() {
        ArrayList<Class<?>> generatedIdClassList = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        ClassLoader classLoader = this.getClass().getClassLoader();
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(JPA_REPOSITORY_REFERENCE)) {
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(Objects.requireNonNull(beanDefinition.getBeanClassName()), classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class)) {
                    generatedIdClassList.add(clazz);
                    break;
                }
            }
        }
        return generatedIdClassList;
    }

    private String getTestSchemaNameFromEntity(Class<?> clazz) {
        Table tableAnnotation = AnnotationUtils.findAnnotation(clazz, Table.class);
        if (tableAnnotation != null) {
            return TEST_TABLE_PREFIX + tableAnnotation.name();
        }
        throw new NotExpectedClassArgumentException(NOT_ENTITY_CLASS + clazz.getSimpleName());
    }
}