package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.FirstCategory;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class SubjectFirstCategoryValidator implements ConstraintValidator<SubjectFirstCategoryConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String subjectFirstCategory, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (subjectFirstCategory == null || subjectFirstCategory.isBlank()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.subjectFirstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(FirstCategory.class, subjectFirstCategory)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.firstCategory", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
