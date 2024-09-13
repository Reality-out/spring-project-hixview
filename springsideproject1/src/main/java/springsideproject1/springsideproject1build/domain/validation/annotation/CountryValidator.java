package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import springsideproject1.springsideproject1build.domain.entity.Country;

import java.util.Locale;

import static springsideproject1.springsideproject1build.util.EnumUtils.inEnumConstants;

public class CountryValidator implements ConstraintValidator<CountryConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (country == null || country.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.company.country", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(Country.class, country)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.company.country", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
