package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;

import java.util.Locale;

import static springsideproject1.springsideproject1build.util.EnumUtils.inEnumConstants;

public class SecondCategoryValidator implements ConstraintValidator<SecondCategoryConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String secondCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (secondCategory == null || secondCategory.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.secondCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(SecondCategory.class, secondCategory)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.secondCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
