package site.hixview.support.scan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(
        basePackages = "site.hixview.domain.service",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class)
)
public abstract class ScanService {
}
