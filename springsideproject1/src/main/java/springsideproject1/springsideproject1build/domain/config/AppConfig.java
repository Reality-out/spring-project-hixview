package springsideproject1.springsideproject1build.domain.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("message.error", "message.manager", "message.others", "message.user");
        source.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        return source;
    }
}
