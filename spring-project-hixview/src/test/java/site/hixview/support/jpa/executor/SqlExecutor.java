package site.hixview.support.jpa.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.support.jpa.util.ObjectEntityTestUtils;

import java.util.List;

public class SqlExecutor {
    private final List<List<Class<?>>> allEntityClassList = ObjectEntityTestUtils.getAllEntity();
    private final String[] generatedIdSchemaNames = allEntityClassList.getFirst().stream()
            .map(ObjectEntityTestUtils::getTestSchemaNameFromEntity).toArray(String[]::new);
    private final String[] noGeneratedIdSchemaNames = allEntityClassList.getLast().stream()
            .map(ObjectEntityTestUtils::getTestSchemaNameFromEntity).toArray(String[]::new);

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    public void deleteAll(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, generatedIdSchemaNames);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, noGeneratedIdSchemaNames);
    }

    public void deleteOnlyWithGeneratedId(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, generatedIdSchemaNames);
    }
}
