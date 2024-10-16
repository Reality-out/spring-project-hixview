package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.Press;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class PressValidator implements ConstraintValidator<PressConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String press, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (press == null || press.isBlank()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.press", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(Press.class, press)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
