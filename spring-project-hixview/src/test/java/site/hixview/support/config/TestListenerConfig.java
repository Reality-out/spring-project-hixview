package site.hixview.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jdbc.callback.DynamicTestResetListener;
import site.hixview.support.jdbc.callback.TestResetAutoIncrementListener;
import site.hixview.support.jdbc.callback.TestResetTableListener;

@Configuration
public class TestListenerConfig {

    @Bean
    public DynamicTestResetListener dynamicTestResetListener() {
        DynamicTestResetListener listener = new DynamicTestResetListener();
        listener.addListener(OnlyRealRepositoryContext.ResetMode.RESET_AUTO_INCREMENT.name(), new TestResetAutoIncrementListener());
        listener.addListener(OnlyRealRepositoryContext.ResetMode.RESET_TABLE.name(), new TestResetTableListener());
        return listener;
    }
}
