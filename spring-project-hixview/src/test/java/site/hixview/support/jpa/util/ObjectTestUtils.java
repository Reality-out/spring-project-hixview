package site.hixview.support.jpa.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ClassUtils;
import site.hixview.aggregate.error.UnexpectedClassTypeException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    static List<List<Class<?>>> getAllEntityClass() {
        ArrayList<Class<?>> generatedIdEntityClass = new ArrayList<>();
        ArrayList<Class<?>> noGeneratedIdEntityClass = new ArrayList<>();
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
            boolean hasAutoIncrement = false;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class)) {
                    hasAutoIncrement = true;
                    generatedIdEntityClass.add(clazz);
                    break;
                }
            }
            if (!hasAutoIncrement) {
                noGeneratedIdEntityClass.add(clazz);
            }
        }
        return List.of(generatedIdEntityClass, noGeneratedIdEntityClass);
    }

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
        throw new UnexpectedClassTypeException(NOT_ENTITY_CLASS + clazz.getSimpleName());
    }

    static void resetAutoIncrement(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));
        transactionTemplate.execute(status -> {
            List<String> schemaNames = ObjectTestUtils.getGeneratedIdClassList().stream()
                    .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
            String[] sqlStatements = schemaNames.stream().flatMap(schemaName -> Stream.of(
                            "TRUNCATE TABLE " + schemaName, "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1"))
                    .toArray(String[]::new);
            jdbcTemplate.batchUpdate(sqlStatements);
            return null;
        });
    }

    static void resetTable(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(
                applicationContext.getBean(PlatformTransactionManager.class));
        List<List<Class<?>>> allEntityClassList = ObjectTestUtils.getAllEntityClass();
        transactionTemplate.execute(status -> {
            List<String> generatedIdSchemaNames = allEntityClassList.getFirst().stream()
                    .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
            String[] sqlStatements = generatedIdSchemaNames.stream().flatMap(schemaName -> Stream.of(
                            "TRUNCATE TABLE " + schemaName, "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1"))
                    .toArray(String[]::new);
            jdbcTemplate.batchUpdate(sqlStatements);
            return null;
        });
        transactionTemplate.execute(status -> {
            List<String> noGeneratedIdSchemaNames = allEntityClassList.getLast().stream()
                    .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
            String[] sqlStatements = noGeneratedIdSchemaNames.stream()
                    .map(schemaName -> "TRUNCATE TABLE " + schemaName).toArray(String[]::new);
            jdbcTemplate.batchUpdate(sqlStatements);
            return null;
        });
    }
}
