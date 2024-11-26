package site.hixview.support.jdbc.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ObjectTestUtils;

import java.util.Arrays;
import java.util.List;

public class TestResetAutoIncrementListener implements TestExecutionListener, ObjectTestUtils {

    private static final Logger log = LoggerFactory.getLogger(TestResetAutoIncrementListener.class);

    @Override
    public void afterTestClass(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        OnlyRealRepositoryContext annotation = testContext.getTestClass().getAnnotation(OnlyRealRepositoryContext.class);
        List<String> tableNames = Arrays.stream(annotation.resetTables()).map(this::getTestSchemaNameFromEntity).toList();
        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            jdbcTemplate.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
        }
    }
}
