package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPress;

public class PressValidator implements ConstraintValidator<Press, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String press, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (press == null || press.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.press", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!containsWithPress(press)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
