package springsideproject1.springsideproject1build.domain.validator.field;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Component
@RequiredArgsConstructor
public class CompanyArticleDtoNameValidator implements Validator {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleDto articleDto = (CompanyArticleDto) target;

        if (articleService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }
    }
}
