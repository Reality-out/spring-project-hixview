package springsideproject1.springsideproject1build.domain.validation.validator.article;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.LINK;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.SUBJECT_COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

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
