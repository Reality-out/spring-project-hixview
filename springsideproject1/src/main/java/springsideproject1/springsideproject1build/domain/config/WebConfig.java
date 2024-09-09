package springsideproject1.springsideproject1build.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springsideproject1.springsideproject1build.web.interceptor.HandleUrlLastSlashInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final HandleUrlLastSlashInterceptor handleUrlLastSlashInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handleUrlLastSlashInterceptor).addPathPatterns("/**");
    }
}
