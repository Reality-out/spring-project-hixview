package springsideproject1.springsideproject1build.util;

import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import static springsideproject1.springsideproject1build.util.EnumUtils.convertToEnum;
import static springsideproject1.springsideproject1build.util.EnumUtils.inEnumValues;

public abstract class FilterUtils {

    public static void applyStrip(ModifiableHttpServletRequest request, final String constant) {
        String string = request.getParameter(constant);
        if (string != null) request.setParameter(constant, string.strip());
    }

    public static <T extends Enum<T>> void applyUppercaseAndConvertToEnum(
            ModifiableHttpServletRequest request, Class<T> enumClass, String paramName) {
        String articleClassName = request.getParameter(paramName);
        if (articleClassName != null) {
            request.setParameter(paramName, articleClassName.toUpperCase());
            if (inEnumValues(enumClass, articleClassName))
                request.setParameter(paramName, convertToEnum(enumClass, articleClassName).name());
        }
    }
}
