package springsideproject1.springsideproject1build.domain.validation;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;

import java.time.DateTimeException;
import java.time.LocalDate;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPress;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.DATE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.PRESS;

@Component
public class CompanyArticleDtoFieldValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleDto articleDto = (CompanyArticleDto) target;
        LocalDate minDate = LocalDate.of(1960, 1, 1);
        LocalDate maxDate = LocalDate.now();

        // press
        if (!containsWithPress(articleDto.getPress())) {
            errors.rejectValue(PRESS, "typeMismatch.enum");
        }

        // date
        try {
            LocalDate inputDate = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDate());

            if (inputDate.isBefore(minDate) || inputDate.isAfter(maxDate)) {
                errors.rejectValue(DATE, "Range.java.lang.LocalDate", new Object[]{minDate, maxDate}, null);
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DATE, "TypeButInvalid.java.lang.LocalDate");
        }

        // importance
        if (!(articleDto.getImportance() == 0 || articleDto.getImportance() == 1)) {
            errors.rejectValue("importance", "Restrict");
        }
    }
}
