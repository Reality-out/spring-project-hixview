package site.hixview.support.jdbc.callback;

import io.micrometer.common.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestResetTableListener implements TestExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(TestResetTableListener.class);

    @Override
    public void prepareTestInstance(@NonNull TestContext testContext) {
        resetTable(testContext);
    }

    @Override
    public void afterTestMethod(@NonNull TestContext testContext) {
        resetTable(testContext);
    }

    private void resetTable(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        List<List<Class<?>>> allEntityClassList = ObjectTestUtils.getAllEntityClass();
        List<String> generatedIdSchema = allEntityClassList.getFirst().stream().map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
        List<String> noGeneratedIdSchema = allEntityClassList.getLast().stream().map(ObjectTestUtils::getTestSchemaNameFromEntity).toList();
        String[] generatedIdSql = generatedIdSchema.stream()
                .flatMap(schemaName -> Stream.of("TRUNCATE TABLE " + schemaName, "ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1"))
                .toArray(String[]::new);
        String[] noGeneratedIdSql = noGeneratedIdSchema.stream()
                .flatMap(schemaName -> Stream.of("TRUNCATE TABLE " + schemaName))
                .toArray(String[]::new);
        jdbcTemplate.batchUpdate(Stream.concat(Arrays.stream(generatedIdSql), Arrays.stream(noGeneratedIdSql)).toArray(String[]::new));
    }
}