package site.hixview.aggregate.util;

import static site.hixview.aggregate.vo.ExceptionMessage.FOR_THE_CLASS;

public abstract class ExceptionUtils {
    public static String getFormattedExceptionMessage(String message, String dataName, Object dataValue, Class<?> clazz) {
        return "%s%s %s%s%s".formatted(message, dataName, dataValue, FOR_THE_CLASS, clazz.getSimpleName());
    }
}
