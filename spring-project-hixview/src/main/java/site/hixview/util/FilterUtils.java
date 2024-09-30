package site.hixview.util;

import site.hixview.web.request.ModifiableHttpServletRequest;

import static site.hixview.util.EnumUtils.convertToEnum;
import static site.hixview.util.EnumUtils.inEnumValues;

public abstract class FilterUtils {

    public static void applyStrip(ModifiableHttpServletRequest request, final String constant) {
        String string = request.getParameter(constant);
        if (string != null) request.setParameter(constant, string.strip());
    }

    public static <T extends Enum<T>> void applyUppercaseAndConvertToEnum(
            ModifiableHttpServletRequest request, Class<T> enumClass, String paramName) {
        String classification = request.getParameter(paramName);
        if (classification != null) {
            request.setParameter(paramName, classification.toUpperCase());
            if (inEnumValues(enumClass, classification))
                request.setParameter(paramName, convertToEnum(enumClass, classification).name());
        }
    }
}
