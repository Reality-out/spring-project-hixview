package site.hixview.support.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class TestDataSourceConfig {
    @Bean
    public static DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/project1build")
                .username("JunHyeok")
                .password("1growwhigh!")
                .build();
    }

}
