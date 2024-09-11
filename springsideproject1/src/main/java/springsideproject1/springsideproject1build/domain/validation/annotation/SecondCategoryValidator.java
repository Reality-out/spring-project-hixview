package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static springsideproject1.springsideproject1build.domain.entity.SecondCategory.containedWithSecondCategory;

public class SecondCategoryValidator implements ConstraintValidator<SecondCategory, String> {

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
        if (!containedWithSecondCategory(secondCategory)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.secondCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
