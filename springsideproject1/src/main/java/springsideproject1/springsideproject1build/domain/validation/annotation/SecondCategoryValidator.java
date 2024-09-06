package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategory;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategoryValue;

public class SecondCategoryValidator implements ConstraintValidator<SecondCategory, String> {

    @Override
    public boolean isValid(String secondCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (secondCategory == null || secondCategory.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.secondCategory}").addConstraintViolation();
            return false;
        }
        if (!containsWithSecondCategory(secondCategory) && !containsWithSecondCategoryValue(secondCategory)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.company.secondCategory}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
