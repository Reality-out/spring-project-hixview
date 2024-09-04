package springsideproject1.springsideproject1build.domain.validation.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import springsideproject1.springsideproject1build.domain.validation.annotation.Scale;

import static springsideproject1.springsideproject1build.domain.entity.company.Scale.containsWithScale;
import static springsideproject1.springsideproject1build.domain.entity.company.Scale.containsWithScaleValue;

public class ScaleValidator implements ConstraintValidator<Scale, String> {

    @Override
    public boolean isValid(String scale, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (scale == null || scale.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.scale}")
                    .addPropertyNode("scale").addConstraintViolation();
            return false;
        }
        if (!containsWithScale(scale) && !containsWithScaleValue(scale)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.company.scale}")
                    .addPropertyNode("scale").addConstraintViolation();
            return false;
        }
        return true;
    }
}
