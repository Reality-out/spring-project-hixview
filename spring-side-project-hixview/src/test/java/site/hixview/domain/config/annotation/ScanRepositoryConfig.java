package site.hixview.domain.config.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@Configuration
@ComponentScan(
        basePackages = "site.hixview.repository.jdbc",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class)
)
abstract class ScanRepositoryConfig {
}
