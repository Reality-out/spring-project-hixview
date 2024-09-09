package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategory;

public class FirstCategoryValidator implements ConstraintValidator<FirstCategory, String> {

    @Override
    public boolean isValid(String firstCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (firstCategory == null || firstCategory.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.firstCategory}").addConstraintViolation();
            return false;
        }
        if (!containsWithFirstCategory(firstCategory)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.company.firstCategory}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
