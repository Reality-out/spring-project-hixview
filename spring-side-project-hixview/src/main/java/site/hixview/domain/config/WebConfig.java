package site.hixview.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.hixview.web.interceptor.HandleUrlLastSlashInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final HandleUrlLastSlashInterceptor handleUrlLastSlashInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handleUrlLastSlashInterceptor).addPathPatterns("/**");
    }
}
