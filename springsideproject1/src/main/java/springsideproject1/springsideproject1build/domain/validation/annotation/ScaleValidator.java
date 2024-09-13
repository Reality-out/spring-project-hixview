package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import springsideproject1.springsideproject1build.domain.entity.Scale;

import java.util.Locale;

import static springsideproject1.springsideproject1build.util.EnumUtils.inEnumConstants;

public class ScaleValidator implements ConstraintValidator<ScaleConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String scale, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (scale == null || scale.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.scale", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(Scale.class, scale)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.scale", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
