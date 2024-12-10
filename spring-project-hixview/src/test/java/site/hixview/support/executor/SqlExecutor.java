package site.hixview.support.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.util.List;

public class SqlExecutor {
    private final List<List<Class<?>>> allEntityClassList = ObjectTestUtils.getAllEntity();
    private final String[] generatedIdSchemaNames = allEntityClassList.getFirst().stream()
            .map(ObjectTestUtils::getTestSchemaNameFromEntity).toArray(String[]::new);
    private final String[] noGeneratedIdSchemaNames = allEntityClassList.getLast().stream()
            .map(ObjectTestUtils::getTestSchemaNameFromEntity).toArray(String[]::new);

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    public void deleteAll(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, generatedIdSchemaNames);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, noGeneratedIdSchemaNames);
    }

    public void deleteOnlyWithGeneratedId(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, generatedIdSchemaNames);
    }
}
