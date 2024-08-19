package springsideproject1.springsideproject1build.validation.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDtoNoNumber;

import java.time.DateTimeException;
import java.time.LocalDate;

import static springsideproject1.springsideproject1build.domain.article.Press.containsWithPress;

@Component
public class CompanyArticleDtoNoNumberValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleDtoNoNumber articleDto = (CompanyArticleDtoNoNumber) target;
        LocalDate minDate = LocalDate.of(1960, 1, 1);
        LocalDate maxDate = LocalDate.now();

        // press
        if (!containsWithPress(articleDto.getPress())) {
            errors.rejectValue("press", "typeMismatch.enum");
        }

        // date
        try {
            LocalDate inputDate = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDate());

            if (inputDate.isBefore(minDate) || inputDate.isAfter(maxDate)) {
                errors.rejectValue("date", "Range.java.lang.LocalDate", new Object[]{minDate, maxDate}, null);
            }
        } catch (DateTimeException e) {
            errors.rejectValue("date", "TypeButInvalid.java.lang.LocalDate");
        }

        // importance
        if (!(articleDto.getImportance() == 0 || articleDto.getImportance() == 1)) {
            errors.rejectValue("importance", "Restrict");
        }
    }
}
