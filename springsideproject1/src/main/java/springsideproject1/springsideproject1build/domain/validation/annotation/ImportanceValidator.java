package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class ImportanceValidator implements ConstraintValidator<ImportanceConstraint, Integer> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(Integer importance, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (importance == null) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotNull.article.importance", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!(importance == 0 || importance == 1)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("Restrict.article.importance", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
