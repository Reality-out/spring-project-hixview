package site.hixview.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Enum.valueOf;

public abstract class JsonUtils {
    @SneakyThrows
    public static String serializeWithOneMap(ObjectMapper objectMapper, String keyName, List<String> valueList) {
        return objectMapper.writeValueAsString(new HashMap<>(){{
            put(keyName, valueList);
        }});
    }

    @SneakyThrows
    public static <T extends Enum<T>> String serializeEnumWithOneMap(ObjectMapper objectMapper, String keyName, List<T> valueList) {
        return objectMapper.writeValueAsString(new HashMap<>() {{
            put(keyName, valueList.stream().map(Enum::name).toList());
        }});
    }

    @SneakyThrows
    public static List<String> deserializeWithOneMapToList(ObjectMapper objectMapper, String keyName, String jsonString) {
        Map<String, List<String>> deserializedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
        });
        return deserializedMap.get(keyName) == null ? Collections.emptyList() : deserializedMap.get(keyName);
    }

    @SneakyThrows
    public static <T extends Enum<T>> List<T> deserializeEnumWithOneMapToList(ObjectMapper objectMapper, String keyName, String jsonString, Class<T> clazz) {
        Map<String, List<String>> deserializedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
        });
        return deserializedMap.get(keyName) == null ? Collections.emptyList() :
                deserializedMap.get(keyName).stream().map(string -> valueOf(clazz, string)).toList();
    }
}
