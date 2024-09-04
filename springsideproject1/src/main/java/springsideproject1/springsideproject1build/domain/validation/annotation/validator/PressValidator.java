package springsideproject1.springsideproject1build.domain.validation.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import springsideproject1.springsideproject1build.domain.validation.annotation.Press;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPress;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPressValue;

public class PressValidator implements ConstraintValidator<Press, String> {

    @Override
    public boolean isValid(String press, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (press == null || press.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.article.press}")
                    .addPropertyNode("press").addConstraintViolation();
            return false;
        }
        if (!containsWithPress(press) && !containsWithPressValue(press)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.article.press}")
                    .addPropertyNode("press").addConstraintViolation();
            return false;
        }
        return true;
    }
}