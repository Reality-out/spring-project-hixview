package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.Classification;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class ClassificationValidator implements ConstraintValidator<ClassificationConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String classification, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (classification == null || classification.isBlank()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.enum.classification", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(Classification.class, classification)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.classification", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
