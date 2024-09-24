package site.hixview.support.scan;

import jakarta.validation.Validator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "site.hixview.domain.validation.validator",
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Validator.class)
)
public abstract class ScanValidator {
}
