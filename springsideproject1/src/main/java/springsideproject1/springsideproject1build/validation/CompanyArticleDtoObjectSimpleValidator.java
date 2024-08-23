package springsideproject1.springsideproject1build.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.service.CompanyArticleService;
import springsideproject1.springsideproject1build.service.CompanyService;

@Component
@RequiredArgsConstructor
public class CompanyArticleDtoObjectSimpleValidator implements Validator {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleDto articleDto = (CompanyArticleDto) target;

        // name
        if (articleService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.reject("Exist.CompanyArticle.name");
        }

        // link
        if (articleService.findArticleByLink(articleDto.getLink()).isPresent()) {
            errors.reject("Exist.CompanyArticle.link");
        }
    }
}
