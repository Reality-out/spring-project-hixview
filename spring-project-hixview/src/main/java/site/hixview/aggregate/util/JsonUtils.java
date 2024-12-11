package site.hixview.aggregate.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_MAP_TO_JSON;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;

public abstract class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Long> parseToLongList(String jsonString, String key) {
        try {
            Map<String, List<Long>> parsedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
            });
            return parsedMap.get(key);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + jsonString);
        }
    }

    public static List<String> parseToStringList(String jsonString, String key) {
        try {
            Map<String, List<String>> parsedMap = objectMapper.readValue(jsonString, new TypeReference<>() {
            });
            return parsedMap.get(key);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + jsonString);
        }
    }

    public static String mapLongList(List<Long> longList, String key) {
        try {
            return (objectMapper.writeValueAsString(new HashMap<>() {{
                put(key, longList);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + longList);
        }
    }

    public static String mapStringList(List<String> stringList, String key) {
        try {
            return (objectMapper.writeValueAsString(new HashMap<>() {{
                put(key, stringList);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + stringList);
        }
    }
}
