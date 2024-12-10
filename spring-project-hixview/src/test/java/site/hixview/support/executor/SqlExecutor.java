package site.hixview.support.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static site.hixview.aggregate.vo.ExceptionMessage.FAILED_BATCH_PROCESSING;

public class SqlExecutor {
    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    public void resetAutoIncrement(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));
        transactionTemplate.execute(status -> {
            List<String> schemaNames = ObjectTestUtils.getGeneratedIdEntity().stream()
                    .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
            String[] sqlStatements = schemaNames.stream().flatMap(schemaName -> Stream.of(
                            "TRUNCATE TABLE " + schemaName, "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1"))
                    .toArray(String[]::new);
            jdbcTemplate.batchUpdate(sqlStatements);
            return null;
        });
    }

    public void resetTable(ApplicationContext applicationContext) {
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(
                applicationContext.getBean(PlatformTransactionManager.class));
        List<List<Class<?>>> allEntityClassList = ObjectTestUtils.getAllEntity();
        List<String> generatedIdSchemaNames = allEntityClassList.getFirst().stream()
                .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
        List<String> noGeneratedIdSchemaNames = allEntityClassList.getLast().stream()
                .map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();

        String[] truncateSqlStatements = new ArrayList<>() {{
            addAll(generatedIdSchemaNames);
            addAll(noGeneratedIdSchemaNames);
        }}.stream().map(schemaName -> "TRUNCATE TABLE " + schemaName).toArray(String[]::new);
        int[] truncateResult = jdbcTemplate.batchUpdate(truncateSqlStatements);
        validateBatchProcess(truncateResult);

        String[] alterSqlStatements = generatedIdSchemaNames.stream()
                .map(schemaName -> "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1").toArray(String[]::new);
        int[] alterResult = jdbcTemplate.batchUpdate(alterSqlStatements);
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
