package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX_PATTERN;

public class CodeValidator implements ConstraintValidator<Code, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (code == null || code.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.code", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!NUMBER_REGEX_PATTERN.matcher(code).find()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("Pattern.company.code", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (code.length() != 6) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("Size.company.code", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}