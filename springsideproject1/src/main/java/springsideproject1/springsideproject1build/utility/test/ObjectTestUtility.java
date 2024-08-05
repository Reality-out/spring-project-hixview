package springsideproject1.springsideproject1build.utility.test;

import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public interface ObjectTestUtility {

    /**
     * Constant
     */
    String ALL_QUERY_STRING = "?*";

    /**
     * For DB
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
     * Request
     */
    default MockHttpServletRequestBuilder processGetWithSingleParam(String url, String key, String value) {
        return get(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).param(key, value);
    }

    default MockHttpServletRequestBuilder processPostWithSingleParam(String url, String key, String value) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).param(key, value);
    }

    default MockHttpServletRequestBuilder processPostWithMultipleParam(String url, Map<String, String> map) {
        MockHttpServletRequestBuilder requestBuilder = post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED);
        for (String str : map.keySet()) {
            requestBuilder = requestBuilder.param(str, map.get(str));
        }
        return requestBuilder;
    }
}
