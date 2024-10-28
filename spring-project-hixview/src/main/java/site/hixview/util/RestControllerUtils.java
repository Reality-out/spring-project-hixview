package site.hixview.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.ExceptionMessage.COMPARE_SAME_VALUE_OF_ERROR_HIERARCHY;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.MessageUtils.getDefaultMessage;

public abstract class RestControllerUtils {
    /**
     * Constant
     */
    private static final Map<String, Integer> errorHierarchy = new HashMap<>() {{
        put("NotBlank", 0);
        put("NotNull", 0);
        put("typeMismatch", 0);
        put("Pattern", 1);
        put("Range", 1);
        put("Size", 2);
        put("Restrict", 3);
    }};

    /**
     * Method
     */
    public static void processMessagePatternString(MessageSource source, String defaultMessage, Map<String, String> registeredFieldErrorMap, String field, Map<String, String> returnedFieldErrorMap) {
        String errMessage = defaultMessage.substring(1, defaultMessage.length() - 1);
        String errCode = errMessage.split("\\.")[0];
        if (registeredFieldErrorMap.containsKey(field)) {
            compareAndProcessErrorHierarchy(source, field, errCode, errMessage, registeredFieldErrorMap, returnedFieldErrorMap);
        } else {
            registeredFieldErrorMap.put(field, errCode);
            returnedFieldErrorMap.put(field, encodeWithUTF8(getDefaultMessage(source, errCode, errMessage)));
        }
    }

    public static Map<String, String> getMapWithNameMessageFromProperty(MessageSource source, BindingResult bindingResult) {
        Map<String, String> fieldErrorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            for (String code : Objects.requireNonNull(fieldError.getCodes())) {
                try {
                    fieldErrorMap.put(fieldError.getField(),
                            encodeWithUTF8(source.getMessage(code, null, Locale.getDefault())));
                    break;
                } catch (NoSuchMessageException ignored) {
                }
            }
        }
        return fieldErrorMap;
    }

    private static void compareAndProcessErrorHierarchy(MessageSource source, String field, String errCode, String errMessage, Map<String, String> registeredFieldErrorMap, Map<String, String> returnedFieldErrorMap) {
        int hierarchyDif = errorHierarchy.get(registeredFieldErrorMap.get(field)) - errorHierarchy.get(errCode);
        if (hierarchyDif > 0) {
            registeredFieldErrorMap.put(field, errCode);
            returnedFieldErrorMap.put(field, encodeWithUTF8(getDefaultMessage(source, errCode, errMessage)));
        } else if (hierarchyDif == 0) {
            throw new IllegalStateException(COMPARE_SAME_VALUE_OF_ERROR_HIERARCHY);
        }
    }
}
