package springsideproject1.springsideproject1build.domain.validator.field;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.CODE;

@Component
@RequiredArgsConstructor
public class CompanyDtoCodeValidator implements Validator {

    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Company.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyDto companyDto = (CompanyDto) target;

        if (companyService.findCompanyByCode(companyDto.getCode()).isPresent()) {
            errors.rejectValue(CODE, "Exist");
        }
    }
}
