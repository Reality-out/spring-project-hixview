package springsideproject1.springsideproject1build.domain.validation.validator.article;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import java.time.DateTimeException;
import java.time.LocalDate;

import static springsideproject1.springsideproject1build.domain.valueobject.WORD.DAYS;

@Component
@RequiredArgsConstructor
public class CompanyArticleEntryDateValidator implements Validator {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final LocalDate minDate = LocalDate.of(1960, 1, 1);
    private final LocalDate maxDate = LocalDate.now();

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        CompanyArticleDto articleDto = (CompanyArticleDto) target;

        try {
            LocalDate inputDate = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            if (inputDate.isBefore(minDate) || inputDate.isAfter(maxDate)) {
                errors.rejectValue(DAYS, "Range.java.lang.LocalDate");
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DAYS, "TypeButInvalid.java.lang.LocalDate");
        }
    }
}