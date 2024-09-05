package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImportanceValidator implements ConstraintValidator<Importance, Integer> {

    @Override
    public boolean isValid(Integer importance, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (importance == null) {
            context.buildConstraintViolationWithTemplate("{NotNull.article.importance}")
                    .addPropertyNode("importance").addConstraintViolation();
            return false;
        }
        if (!(importance == 0 || importance == 1)) {
            context.buildConstraintViolationWithTemplate("{Restrict.article.importance}")
                    .addPropertyNode("importance").addConstraintViolation();
            return false;
        }
        return true;
    }
}
