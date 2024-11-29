package site.hixview.support.jpa.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import site.hixview.aggregate.error.NotExpectedClassArgumentException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static site.hixview.aggregate.vo.ExceptionMessage.NOT_ENTITY_CLASS;
import static site.hixview.aggregate.vo.Reference.JPA_ENTITY_REFERENCE;

public interface ObjectTestUtils {
    /**
     * Constant
     */
    String TEST_TABLE_PREFIX = "test_";

    /**
     * Method
     */
    static List<Class<?>> getGeneratedIdClassList() {
        ArrayList<Class<?>> generatedIdClassList = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(JPA_ENTITY_REFERENCE)) {
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

    static String getTestSchemaNameFromEntity(Class<?> clazz) {
        Table tableAnnotation = AnnotationUtils.findAnnotation(clazz, Table.class);
        if (tableAnnotation != null) {
            return TEST_TABLE_PREFIX + tableAnnotation.name();
        }
        throw new NotExpectedClassArgumentException(NOT_ENTITY_CLASS + clazz.getSimpleName());
    }
}
