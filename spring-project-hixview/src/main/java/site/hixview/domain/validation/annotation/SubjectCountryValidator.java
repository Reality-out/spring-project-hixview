package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.Country;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class SubjectCountryValidator implements ConstraintValidator<SubjectCountryConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (country == null || country.isBlank()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.subjectCountry", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(Country.class, country)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.article.subjectCountry", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
