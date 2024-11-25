package site.hixview.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static site.hixview.support.config.TestDataSourceConfig.dataSource;

@TestConfiguration
public class TestJdbcTemplateConfig {
    private static final Logger log = LoggerFactory.getLogger(TestJdbcTemplateConfig.class);

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
