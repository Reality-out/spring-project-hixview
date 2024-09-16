package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.name.EntityName.Article.LINK;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COMPANY;
import static site.hixview.domain.vo.Word.NAME;

@Component
@RequiredArgsConstructor
public class CompanyArticleAddComplexValidator implements Validator {

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

        if (articleService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isPresent()) {
            errors.rejectValue(LINK, "Exist");
        }
        if (companyService.findCompanyByName(articleDto.getSubjectCompany()).isEmpty()) {
            errors.rejectValue(SUBJECT_COMPANY, "NotFound");
        }
    }
}
