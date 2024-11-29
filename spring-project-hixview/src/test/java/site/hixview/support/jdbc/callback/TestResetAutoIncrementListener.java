package site.hixview.support.jdbc.callback;

import io.micrometer.common.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import site.hixview.support.jpa.util.ObjectTestUtils;

public class TestResetAutoIncrementListener implements TestExecutionListener, ObjectTestUtils {

    private static final Logger log = LoggerFactory.getLogger(TestResetAutoIncrementListener.class);

    @Override
    public void afterTestClass(@NonNull TestContext testContext) {
        resetAutoIncrement(testContext);
    }

    private void resetAutoIncrement(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        for (String schemaName : ObjectTestUtils.getGeneratedIdClassList().stream().map(ObjectTestUtils::getTestSchemaNameFromEntity).toList()) {
            log.info(schemaName);
            jdbcTemplate.execute("TRUNCATE TABLE " + schemaName);
            jdbcTemplate.execute("ALTER TABLE " + schemaName + " AUTO_INCREMENT = 1");
        }
    }
}