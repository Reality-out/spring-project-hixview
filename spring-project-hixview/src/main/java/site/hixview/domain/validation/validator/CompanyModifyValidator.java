package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.Word.CODE;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.ErrorCodeName.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CompanyModifyValidator implements Validator {

    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyDto companyDto = (CompanyDto) target;

        if (companyService.findCompanyByCode(companyDto.getCode()).isEmpty()) {
            errors.rejectValue(CODE, NOT_FOUND);
        }
        if (companyService.findCompanyByName(companyDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, NOT_FOUND);
        }
    }
}
