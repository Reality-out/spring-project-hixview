package site.hixview.util.test;

import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.util.ControllerUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public interface ObjectTestUtils {

    /**
     * Constant
     */
    // Request Key
    String nameDatePressString = "nameDatePressString";
    String linkString = "linkString";

    // Others
    String INVALID_VALUE = "invalidValue";
    String ALL_QUERY_STRING = "?*";

    /**
     * DataBase
     */
    default void resetTable(JdbcTemplate jdbcTemplateTest, String tableName) {
        resetTable(jdbcTemplateTest, tableName, false);
    }

    default void resetTable(JdbcTemplate jdbcTemplateTest, String tableName, boolean hasAutoIncrement) {
        jdbcTemplateTest.execute("DELETE FROM " + tableName);
        if (hasAutoIncrement) {
            jdbcTemplateTest.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
        }
    }

    /**
     * Random
     */
    default String getRandomLongString(int minDigits) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        while (sb.length() < minDigits) {
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder getWithNoParam(String url) {
        return get(url).contentType(MediaType.TEXT_HTML);
    }

    default MockHttpServletRequestBuilder getWithSingleParam(String url, String key, String value) {
        return get(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).param(key, value);
    }

    default MockHttpServletRequestBuilder getWithMultipleParam(String url, Map<String, String> map) {
        MockHttpServletRequestBuilder requestBuilder = get(url).contentType(MediaType.APPLICATION_FORM_URLENCODED);
        for (String str : map.keySet()) {
            requestBuilder = requestBuilder.param(str, map.get(str));
        }
        return requestBuilder;
    }

    default MockHttpServletRequestBuilder postWithSingleParam(String url, String key, String value) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).param(key, value);
    }

    default MockHttpServletRequestBuilder postWithMultipleParams(String url, Map<String, String> map) {
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED);
        for (String str : map.keySet()) {
            requestBuilder = requestBuilder.param(str, map.get(str));
        }
        return requestBuilder;
    }

    /**
     * URL Encoding
     */
    default String toStringForUrl(List<String> list) {
        return list.stream()
                .map(ControllerUtils::encodeWithUTF8)
                .collect(Collectors.joining(","));
    }
}