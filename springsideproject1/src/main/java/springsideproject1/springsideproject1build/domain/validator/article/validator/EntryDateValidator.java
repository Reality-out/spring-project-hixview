package springsideproject1.springsideproject1build.domain.validator.article.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import springsideproject1.springsideproject1build.domain.validator.article.annotation.EntryDate;

import java.time.DateTimeException;
import java.time.LocalDate;

public class EntryDateValidator implements ConstraintValidator<EntryDate, LocalDate> {

    private final LocalDate minDate = LocalDate.of(1960, 1, 1);
    private final LocalDate maxDate = LocalDate.now();

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (date == null) {
            context.buildConstraintViolationWithTemplate("{NotNull.article.days}").addConstraintViolation();
            return false;
        }
        try {
            LocalDate inputDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

            if (inputDate.isBefore(minDate) || inputDate.isAfter(maxDate)) {
                context.buildConstraintViolationWithTemplate("{Range.java.lang.LocalDate}").addConstraintViolation();
                return false;
            }
        } catch (DateTimeException e) {
            context.buildConstraintViolationWithTemplate("{TypeButInvalid.java.lang.LocalDate}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
