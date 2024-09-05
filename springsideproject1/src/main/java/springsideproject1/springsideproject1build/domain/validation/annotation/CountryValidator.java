package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static springsideproject1.springsideproject1build.domain.entity.company.Country.containsWithCountry;
import static springsideproject1.springsideproject1build.domain.entity.company.Country.containsWithCountryValue;

public class CountryValidator implements ConstraintValidator<Country, String> {

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (country == null || country.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.company.country}")
                    .addPropertyNode("country").addConstraintViolation();
            return false;
        }
        if (!containsWithCountry(country) && !containsWithCountryValue(country)) {
            context.buildConstraintViolationWithTemplate("{typeMismatch.enum.company.country}")
                    .addPropertyNode("country").addConstraintViolation();
            return false;
        }
        return true;
    }
}
