package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.entity.company.Scale.containsWithScale;

public class ScaleValidator implements ConstraintValidator<Scale, String> {

    @Override
    public boolean isValid(String scale, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (scale == null || scale.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.scale}").addConstraintViolation();
            return false;
        }
        if (!containsWithScale(scale)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.company.scale}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
