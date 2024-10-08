package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.name.EntityName.Company.CODE;
import static site.hixview.domain.vo.Word.NAME;

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
