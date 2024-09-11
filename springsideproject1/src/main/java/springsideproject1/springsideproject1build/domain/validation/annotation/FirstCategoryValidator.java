package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategory;

public class FirstCategoryValidator implements ConstraintValidator<FirstCategory, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String firstCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (firstCategory == null || firstCategory.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!containsWithFirstCategory(firstCategory)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.firstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
