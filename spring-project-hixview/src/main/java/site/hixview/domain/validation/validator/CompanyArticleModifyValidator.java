package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.Word.*;

@Component
@RequiredArgsConstructor
public class CompanyArticleModifyValidator implements Validator {

    private final CompanyArticleEntryDateValidator entryDateValidator;
    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleDto articleDto = (CompanyArticleDto) target;

        entryDateValidator.validate(articleDto, errors);

        if (articleService.findArticleByName(articleDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, "NotFound");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isEmpty()) {
            errors.rejectValue(LINK, "NotFound");
        }
        if (companyService.findCompanyByName(articleDto.getSubjectCompany()).isEmpty()) {
            errors.rejectValue(SUBJECT_COMPANY, "NotFound");
        }
    }
}
