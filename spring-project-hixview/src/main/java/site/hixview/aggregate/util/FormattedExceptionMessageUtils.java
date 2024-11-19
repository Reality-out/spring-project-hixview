package site.hixview.aggregate.util;

public class FormattedExceptionMessageUtils {
    // EntityNotFoundException
    public static String articleEntityNotFound(String columnName, Object columnValue) {
        return String.format("해당 컬럼 값에 대한 ArticleEntity를 찾을 수 없습니다: %s = %s", columnName, columnValue);
    }
}
