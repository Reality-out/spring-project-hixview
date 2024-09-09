package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX_PATTERN;

public class CodeValidator implements ConstraintValidator<Code, String> {

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (code == null || code.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.code}").addConstraintViolation();
            return false;
        }
        if (!NUMBER_REGEX_PATTERN.matcher(code).find()) {
            context.buildConstraintViolationWithTemplate("{Pattern.company.code}").addConstraintViolation();
            return false;
        }
        if (code.length() != 6) {
            context.buildConstraintViolationWithTemplate("{Size.company.code}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
