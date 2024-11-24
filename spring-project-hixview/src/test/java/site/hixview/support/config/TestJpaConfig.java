package site.hixview.support.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import site.hixview.support.naming.TestPrefixPhysicalNamingStrategy;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@TestConfiguration
public class TestJpaConfig {
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setPackagesToScan("site.hixview.jpa.entity");

        Properties properties = new Properties();
        properties.putAll(jpaProperties().getProperties());
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/project1build")
                .username("JunHyeok")
                .password("1growwhigh!")
                .build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public JpaProperties jpaProperties() {
        JpaProperties properties = new JpaProperties();
        properties.setDatabase(Database.MYSQL);
        properties.setGenerateDdl(false);
        properties.setShowSql(true);
        properties.setOpenInView(false);
        Map<String, String> propertiesMap = properties.getProperties();
        propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        propertiesMap.put("hibernate.hbm2ddl.auto", "none");
        propertiesMap.put("hibernate.physical_naming_strategy", TestPrefixPhysicalNamingStrategy.class.getName());
        return properties;
    }
}
