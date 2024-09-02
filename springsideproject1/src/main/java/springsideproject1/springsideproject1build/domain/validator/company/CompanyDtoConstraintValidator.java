package springsideproject1.springsideproject1build.domain.validator.company;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;

import java.time.LocalDate;

import static springsideproject1.springsideproject1build.domain.entity.company.Country.containsWithCountry;
import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.Scale.containsWithScale;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategory;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategoryValue;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;

@Component
public class CompanyDtoConstraintValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyDto companyDto = (CompanyDto) target;
        LocalDate minDate = LocalDate.of(1960, 1, 1);
        LocalDate maxDate = LocalDate.now();

        // country
        if (!containsWithCountry(companyDto.getCountry())) {
            errors.rejectValue(COUNTRY, "typeMismatch.enum");
        }

        // scale
        if (!containsWithScale(companyDto.getScale())) {
            errors.rejectValue(SCALE, "typeMismatch.enum");
        }

        // firstCategory
        if (!containsWithFirstCategory(companyDto.getFirstCategory())
                && !containsWithFirstCategoryValue(companyDto.getFirstCategory())) {
            errors.rejectValue(FIRST_CATEGORY, "typeMismatch.enum");
        }

        // secondCategory
        if (!containsWithSecondCategory(companyDto.getSecondCategory())
                && !containsWithSecondCategoryValue(companyDto.getSecondCategory())) {
            errors.rejectValue(SECOND_CATEGORY, "typeMismatch.enum");
        }
    }
}
