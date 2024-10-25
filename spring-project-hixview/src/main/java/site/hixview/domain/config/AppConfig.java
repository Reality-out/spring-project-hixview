package site.hixview.domain.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class AppConfig {

    @Bean
    public MessageSource messageSource() throws IOException {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(getMessageResourceBaseNames());
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        return messageSource;
    }

    private String[] getMessageResourceBaseNames() throws IOException {
        List<String> baseNames = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:message/*.properties");

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            baseNames.add("message/" + Objects.requireNonNull(filename).substring(0, filename.lastIndexOf('.')));
        }

        return baseNames.toArray(new String[0]);
    }
}
