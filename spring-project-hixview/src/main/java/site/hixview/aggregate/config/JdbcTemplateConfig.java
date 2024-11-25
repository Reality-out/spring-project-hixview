package site.hixview.aggregate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static site.hixview.aggregate.config.DataSourceConfig.dataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
