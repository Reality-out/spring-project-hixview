package site.hixview.util;

import site.hixview.domain.error.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumUtils {
    private static final String NO_GET_VALUE_FOR_THE_ENUM = "해당 enum 타입에 getValue 메소드가 없습니다.";
    private static final String CANNOT_INVOKE_GET_VALUE = "getValue 메소드를 촉발할 수 없습니다.";
    private static final String NOT_HAVE_ACCESS_TO_GET_VALUE = "getValue 메소드에 대한 접근권이 없습니다.";

    public static <T extends Enum<T>> boolean inEnumConstants(Class<T> enumClass, String str) {
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Enum<T>> boolean inEnumValues(Class<T> enumClass, String str) {
        try {
            Method method = enumClass.getMethod("getValue");
            for (T enumConstant : enumClass.getEnumConstants()) {
                String value = (String) method.invoke(enumConstant);
                if (value.equals(str)) return true;
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(NO_GET_VALUE_FOR_THE_ENUM);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(CANNOT_INVOKE_GET_VALUE);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(NOT_HAVE_ACCESS_TO_GET_VALUE);
        }
        return false;
    }

    public static <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String str) {
        try {
            Method method = enumClass.getMethod("getValue");
            for (T enumConstant : enumClass.getEnumConstants()) {
                String value = (String) method.invoke(enumConstant);
                if (value.equals(str)) return enumConstant;
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(NO_GET_VALUE_FOR_THE_ENUM);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(CANNOT_INVOKE_GET_VALUE);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(NOT_HAVE_ACCESS_TO_GET_VALUE);
        }
        throw new NotFoundException("해당 한글 값과 일치하는 enum type 값이 없습니다.");
    }
}
