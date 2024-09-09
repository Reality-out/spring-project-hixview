package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPress;

public class PressValidator implements ConstraintValidator<Press, String> {

    @Override
    public boolean isValid(String press, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (press == null || press.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.article.press}").addConstraintViolation();
            return false;
        }
        if (!containsWithPress(press)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.article.press}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
