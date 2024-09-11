package springsideproject1.springsideproject1build.domain.validation.validator.article;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;

import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Component
@RequiredArgsConstructor
public class ArticleMainModifyValidator implements Validator {

    private final ArticleMainService articleMainService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ArticleMainDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ArticleMainDto articleMainDto = (ArticleMainDto) target;

        if (articleMainService.findArticleByName(articleMainDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, "NotFound");
        }
    }
}