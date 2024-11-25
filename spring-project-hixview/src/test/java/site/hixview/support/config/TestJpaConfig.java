package site.hixview.support.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import site.hixview.support.jpa.naming.TestPrefixPhysicalNamingStrategy;

import java.util.Map;
import java.util.Properties;

import static site.hixview.support.config.TestDataSourceConfig.dataSource;

@TestConfiguration
@EnableJpaRepositories(basePackages = "site.hixview.jpa.repository")
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
        propertiesMap.put("hibernate.physical_naming_strategy", TestPrefixPhysicalNamingStrategy.class.getName());
        return properties;
    }
}
