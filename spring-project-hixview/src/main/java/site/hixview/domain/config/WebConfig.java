package site.hixview.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.hixview.domain.converter.EnumToStringConverter;
import site.hixview.domain.converter.StringToEnumConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter<>());
        registry.addConverter(new EnumToStringConverter<>());
    }
}
