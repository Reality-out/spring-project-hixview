package site.hixview.support.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import site.hixview.support.jdbc.callback.DynamicTestResetListener;
import site.hixview.support.jdbc.callback.TestResetAutoIncrementListener;
import site.hixview.support.jdbc.callback.TestResetTableListener;

import static site.hixview.support.context.OnlyRealRepositoryContext.ResetMode;

@TestConfiguration
public class TestListenerConfig {

    @Bean
    public DynamicTestResetListener dynamicTestResetListener() {
        DynamicTestResetListener listener = new DynamicTestResetListener();
        listener.addListener(ResetMode.RESET_AUTO_INCREMENT.name(), new TestResetAutoIncrementListener());
        listener.addListener(ResetMode.RESET_TABLE.name(), new TestResetTableListener());
        return listener;
    }
}
