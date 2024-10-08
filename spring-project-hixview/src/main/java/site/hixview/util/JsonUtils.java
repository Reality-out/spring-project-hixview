package site.hixview.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Enum.valueOf;

public abstract class JsonUtils {
    @SneakyThrows
    public static String serializeWithOneMap(ObjectMapper objectMapper, String keyName, List<String> valueList) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String value : valueList) {
            arrayNode.add(value);
        }
        objectNode.set(keyName, arrayNode);
        return objectMapper.writeValueAsString(objectNode);
    }

    @SneakyThrows
    public static <T extends Enum<T>> String serializeEnumWithOneMap(ObjectMapper objectMapper, String keyName, List<T> valueList) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (T value : valueList) {
            arrayNode.add(value.name());
        }
        objectNode.set(keyName, arrayNode);
        return objectMapper.writeValueAsString(objectNode);
    }

    @SneakyThrows
    public static List<String> deserializeWithOneMapToList(ObjectMapper objectMapper, String keyName, String jsonString) {
        Map<String, List<String>> deserializedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
        });
        return deserializedMap.get(keyName) == null ? Collections.emptyList() : deserializedMap.get(keyName);
    }

    @SneakyThrows
    public static <T extends Enum<T>> List<T> deserializeWithOneMapToList(ObjectMapper objectMapper, String keyName, String jsonString, Class<T> clazz) {
        Map<String, List<String>> deserializedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
        });
        return deserializedMap.get(keyName) == null ? Collections.emptyList() :
                deserializedMap.get(keyName).stream().map(string -> valueOf(clazz, string)).toList();
    }
}
