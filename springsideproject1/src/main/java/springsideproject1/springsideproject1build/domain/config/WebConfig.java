package springsideproject1.springsideproject1build.domain.config;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springsideproject1.springsideproject1build.web.filter.CompanyArticleDtoSupportFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean companyArticleDtoSupportFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CompanyArticleDtoSupportFilter());
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
