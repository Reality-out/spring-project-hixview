package site.hixview.support.spring.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class TestJdbcTemplateConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(TestDataSourceConfig.dataSource());
    }
}
