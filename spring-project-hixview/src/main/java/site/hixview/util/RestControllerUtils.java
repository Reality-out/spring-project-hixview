package site.hixview.util;

import org.springframework.context.MessageSource;

import java.util.Map;

import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.ControllerUtils.errorHierarchy;
import static site.hixview.util.MessageUtils.getDefaultMessage;

public abstract class RestControllerUtils {
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

    private static void compareAndProcessErrorHierarchy(MessageSource source, String field, String errCode, String errMessage, Map<String, String> registeredFieldErrorMap, Map<String, String> returnedFieldErrorMap) {
        int hierarchyDif = errorHierarchy.get(registeredFieldErrorMap.get(field)) - errorHierarchy.get(errCode);
        if (hierarchyDif > 0) {
            registeredFieldErrorMap.put(field, errCode);
            returnedFieldErrorMap.put(field, encodeWithUTF8(getDefaultMessage(source, errCode, errMessage)));
        } else if (hierarchyDif == 0) {
            throw new IllegalStateException("같은 수치의 오류 위계 구조를 비교하고 있습니다.");
        }
    }

}
