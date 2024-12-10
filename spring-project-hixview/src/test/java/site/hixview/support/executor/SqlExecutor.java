package site.hixview.support.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.sql.SQLException;
import java.util.List;

import static site.hixview.aggregate.vo.ExceptionMessage.FAILED_BATCH_PROCESSING;

public class SqlExecutor {
    private final List<List<Class<?>>> allEntityClassList = ObjectTestUtils.getAllEntity();
    private final List<String> generatedIdSchemaNames = allEntityClassList.getFirst().stream()
            .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
    private final List<String> noGeneratedIdSchemaNames = allEntityClassList.getLast().stream()
            .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();

    private final String[] generatedIdTruncateSqlStatements = generatedIdSchemaNames.stream()
            .map(schemaName -> "TRUNCATE TABLE " + schemaName).toArray(String[]::new);
    private final String[] generatedIdAlterSqlStatements = generatedIdSchemaNames.stream()
            .map(schemaName -> "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1").toArray(String[]::new);
    private final String[] noGeneratedIdTruncateSqlStatements = noGeneratedIdSchemaNames.stream()
            .map(schemaName -> "TRUNCATE TABLE " + schemaName).toArray(String[]::new);

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    public void resetAutoIncrement(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        int[] truncateResult = jdbcTemplate.batchUpdate(generatedIdTruncateSqlStatements);
        validateBatchProcess(truncateResult);
        int[] alterResult = jdbcTemplate.batchUpdate(generatedIdAlterSqlStatements);
        validateBatchProcess(alterResult);
    }

    public void resetTable(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        int[] generatedIdTruncateResult = jdbcTemplate.batchUpdate(generatedIdTruncateSqlStatements);
        validateBatchProcess(generatedIdTruncateResult);
        int[] noGeneratedIdTruncateResult = jdbcTemplate.batchUpdate(noGeneratedIdTruncateSqlStatements);
        validateBatchProcess(noGeneratedIdTruncateResult);
        int[] alterResult = jdbcTemplate.batchUpdate(generatedIdAlterSqlStatements);
        validateBatchProcess(alterResult);
    }

    private void validateBatchProcess(int[] result) {
        for (int i : result) {
            if (i == -3) {
                throw new IllegalStateException(new SQLException(FAILED_BATCH_PROCESSING));
            }
        }
    }
}
