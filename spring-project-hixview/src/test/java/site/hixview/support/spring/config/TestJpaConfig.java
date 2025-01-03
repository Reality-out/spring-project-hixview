package site.hixview.support.spring.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import site.hixview.support.jpa.naming.TestPrefixPhysicalNamingStrategy;

import java.util.Map;
import java.util.Properties;

import static site.hixview.support.spring.config.TestDataSourceConfig.dataSource;

@TestConfiguration
public class TestJpaConfig {
    private final String packagesToScan = "site.hixview.jpa.entity";

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setPackagesToScan(packagesToScan);

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

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(packagesToScan);
        Properties properties = new Properties();
        properties.setProperty("hibernate.current_session_context_class", SpringSessionContext.class.getName());
        sessionFactoryBean.setHibernateProperties(properties);
        return sessionFactoryBean;
    }
}
