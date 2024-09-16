package springsideproject1.springsideproject1build.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Company.CODE;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;

@Component
@RequiredArgsConstructor
public class CompanyAddValidator implements Validator {

    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyDto companyDto = (CompanyDto) target;

        if (companyService.findCompanyByCode(companyDto.getCode()).isPresent()) {
            errors.rejectValue(CODE, "Exist");
        }
        if (companyService.findCompanyByName(companyDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }
    }
}
