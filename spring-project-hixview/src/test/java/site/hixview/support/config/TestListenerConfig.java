package site.hixview.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import site.hixview.support.jdbc.callback.TestDynamicResetListener;

@TestConfiguration
public class TestListenerConfig {

    private static final Logger log = LoggerFactory.getLogger(TestListenerConfig.class);

    @Bean
    public TestDynamicResetListener dynamicTestResetListener() {
        return new TestDynamicResetListener();
    }
}
