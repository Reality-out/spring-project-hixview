package site.hixview.support.jpa.util;

import jakarta.persistence.Table;
import org.springframework.core.annotation.AnnotationUtils;
import site.hixview.aggregate.error.NotExpectedClassArgumentException;

import static site.hixview.aggregate.vo.ExceptionMessage.NOT_ENTITY_CLASS;

public interface ObjectTestUtils {
    String TEST_TABLE_PREFIX = "test_";

    default String getTestSchemaNameFromEntity(Class<?> clazz) {
        Table tableAnnotation = AnnotationUtils.findAnnotation(clazz, Table.class);
        if (tableAnnotation != null) {
            return TEST_TABLE_PREFIX + tableAnnotation.name();
        }
        throw new NotExpectedClassArgumentException(NOT_ENTITY_CLASS + clazz.getSimpleName());
    }
}
