package springsideproject1.springsideproject1build.domain.validator.article.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import springsideproject1.springsideproject1build.domain.validator.article.annotation.Press;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPress;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPressValue;

public class PressValidator implements ConstraintValidator<Press, String> {

    private String message;

    @Override
    public void initialize(Press constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String press, ConstraintValidatorContext context) {
        if (!containsWithPress(press) && !containsWithPressValue(press)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
