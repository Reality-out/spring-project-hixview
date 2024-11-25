package site.hixview.aggregate.util;

import jakarta.persistence.Table;
import org.springframework.core.annotation.AnnotationUtils;
import site.hixview.aggregate.error.NotExpectedClassArgumentException;

import static site.hixview.aggregate.vo.ExceptionMessage.NOT_ENTITY_CLASS;

public abstract class JpaUtils {
    public static String getSchemaNameFromEntity(Class<?> clazz) {
        Table tableAnnotation = AnnotationUtils.findAnnotation(clazz, Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        }
        throw new NotExpectedClassArgumentException(NOT_ENTITY_CLASS + clazz.getSimpleName());
    }
}
