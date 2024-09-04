package springsideproject1.springsideproject1build.domain.validator.company;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;

import java.time.LocalDate;

@Component
public class CompanyDtoConstraintValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyDto companyDto = (CompanyDto) target;
        LocalDate minDate = LocalDate.of(1960, 1, 1);
        LocalDate maxDate = LocalDate.now();
    }
}
