package site.hixview.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.util.List;

import static site.hixview.util.EnumUtils.convertToEnum;
import static site.hixview.util.EnumUtils.inEnumValues;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;
import static site.hixview.util.JsonUtils.serializeWithOneMap;

public abstract class FilterUtils {

    public static final String IMAGE_PATH_PREFIX = "/images";
    public static final String NEWEST_IMAGE_PATH_PREFIX = "/images/main/newest/";
    public static final String IMAGE_PATH_SUFFIX = ".png";

    public static void applyStrip(ModifiableHttpServletRequest request, final String constant) {
        String string = request.getParameter(constant);
        if (string != null) request.setParameter(constant, string.strip());
    }

    public static <T extends Enum<T>> void applyUppercaseAndConvertToEnumWithString (
            ModifiableHttpServletRequest request, Class<T> enumClass, String paramName) {
        String classification = request.getParameter(paramName);
        if (classification != null) {
            request.setParameter(paramName, classification.toUpperCase());
            if (inEnumValues(enumClass, classification))
                request.setParameter(paramName, convertToEnum(enumClass, classification).name());
        }
    }

    public static <T extends Enum<T>> void applyUppercaseAndConvertToEnumWithMap (
            ModifiableHttpServletRequest request, Class<T> enumClass, String paramName, String keyName) {
        ObjectMapper objectMapper = new ObjectMapper();
        String parameter = request.getParameter(paramName);
        if (parameter != null) {
            List<String> classificationList = deserializeWithOneMapToList(objectMapper, keyName, parameter);
            for (int i = 0; i < classificationList.size(); i++){
                String classification = classificationList.get(i);
                if (classification != null) {
                    classificationList.set(i, classification.toUpperCase());
                    if (inEnumValues(enumClass, classification))
                        classificationList.set(i, convertToEnum(enumClass, classification).name());
                }
            }
            request.setParameter(paramName, serializeWithOneMap(objectMapper, keyName, classificationList));
        }
    }
}
