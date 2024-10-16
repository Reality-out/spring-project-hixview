package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.FirstCategory;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class FirstCategoryValidator implements ConstraintValidator<FirstCategoryConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String firstCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (firstCategory == null || firstCategory.isBlank()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(FirstCategory.class, firstCategory)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.firstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
